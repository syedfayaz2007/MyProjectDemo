package com.scm.kafka.main;

import java.io.IOException;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.TimeZone;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRebalanceListener;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser.Feature;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.scm.kafka.beans.*;
import com.scm.kafka.main.header;
import com.scm.kafka.daoservices.dbConnectionimpl;
import com.scm.kafka.servicesprovider.commonUtility;

import joptsimple.internal.Strings;

public class AIBFileUploadAutoOffsetConsumer {
	static KafkaConsumer<String, String> consumer = null;
	static Map<Integer, Integer> hm = null;
	static dbConnectionimpl dbCon = null;
	static String aibCommitTable = null;
	static String aibErrorTable = null;
	static Integer intOffset = null;
	static Integer intPartition_Number = null;
	static String JsongetString = null;

	// static List<Long> OFFSET = new ArrayList<Long>();
	static List<Long> OFFSET = new ArrayList<Long>();
	static List<Long> PARTITION_NUMBER = new ArrayList<Long>();
	static List<String> JASON_DATA = new ArrayList<String>();
	static List<lines> p_line_req_tab_i = new ArrayList<lines>();
	static List<header> p_header_req_obj_i = new ArrayList<header>();
	static List<String> uniDataBatch = new ArrayList<String>();
	static Set<String> uniErrDataBatch = new HashSet<String>();
	static Set<Long> uniqDataBatch = new HashSet<Long>();
	static List<String> dupDataBatch = new ArrayList<String>();
	static Set<String> uniDataBatchMain = new HashSet<String>();
	static List<String> dupDataBatchMain = new ArrayList<String>();
	static List<Integer> setOFFSET_dup = new ArrayList<Integer>();
	static List<String> setdate = new ArrayList<String>();
	static List<String> setjson_string = new ArrayList<String>();
	static List<String> seterr_msg = new ArrayList<String>();
	static List<String> seterr_code = new ArrayList<String>();
	static List<Integer> setPARTITION_dup = new ArrayList<Integer>();
	static int batchSize = 0;
	static int Poll_ms = 0;
	static int size = 0;
	static int counter = 0;
	static int dupcounter = 0;
	static String HDRTableColsList = null;
	static String LINESTableColsList = null;
	static String errorTableColsList = null;
	static String HDRTableName = null;
	static String LINESTableName = null;
	static int initFlag = 0, initFlag0 = 0, initFlag1 = 0, initFlag2 = 0,
			initFlag3 = 0;
	static String topicName = "";
	static String groupId = "";
	static String error_msg = "";
	static String err_msg_req_id = "";
	static String err_msgs = "";

	static String dbHost;
	static String dbUser;
	static String dbPass;

	public static void main(String[] argv) {
		commonUtility comm = new commonUtility();
		Properties prop = comm.readProp();
		topicName = prop.getProperty("topicName");
		groupId = prop.getProperty("groupId");
		String strServerName = prop.getProperty("bootstrapServer");
		batchSize = Integer.parseInt(prop.getProperty("BatchSize").toString());
		Poll_ms = Integer.parseInt(prop.getProperty("Poll_Ms").toString());
		HDRTableName = prop.getProperty("AIBHDRTable");
		LINESTableName = prop.getProperty("AIBLINESTable");
		aibCommitTable = prop.getProperty("AIBCommitTable");
		aibErrorTable = prop.getProperty("AIBErrorTable");
		// HDRTableColsList = prop.getProperty("AIBHDRTableColsList");
		// LINESTableColsList = prop.getProperty("AIBLINESTableColsList");
	    errorTableColsList = prop.getProperty("AIBErrorTableColsList");

		prop.getProperty("AIBErrorTable");

		dbHost = prop.getProperty("dbHost");
		dbUser = prop.getProperty("dbUserID");
		dbPass = prop.getProperty("dbPassword");
		System.out.println("================================================");
		System.out.println("topic Name : " + topicName);
		System.out.println("group Id : " + groupId);
		System.out.println("Server : " + strServerName);
		System.out.println("Commit Offset table : " + aibCommitTable);
		System.out.println("ERROR JSON Table : " + aibErrorTable);
		System.out.println("Batch Size : " + batchSize);
		System.out.println("================================================");
		Properties configProperties = new Properties();
		configProperties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,
				strServerName);
		configProperties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
				"org.apache.kafka.common.serialization.StringDeserializer");
		configProperties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
				"org.apache.kafka.common.serialization.StringDeserializer");
		configProperties.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
		configProperties.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, true);
		configProperties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
		configProperties
				.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, 100);

		configProperties.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, 300000);
		configProperties.put(ConsumerConfig.REQUEST_TIMEOUT_MS_CONFIG, 350000);

		configProperties.put(ConsumerConfig.MAX_PARTITION_FETCH_BYTES_CONFIG,
				15728640);

		dbCon = new dbConnectionimpl();
		System.out.println("dbconnection ");

		/*
		 * String Query = " select * from " + aibCommitTable +
		 * " where topic = '" + topicName + "' and consumer='" + groupId + "'";
		 * hm = dbCon.executeQuery(Query); if (hm.size() != 0) {
		 * System.out.println("hm size is : " + hm.size()); initFlag = 1;
		 * initFlag0 = 1; initFlag1 = 1; initFlag2 = 1; initFlag3 = 1;
		 * 
		 * }
		 */
		System.out.println("Subscribed to topic " + topicName);
		ObjectMapper objectMapper = new ObjectMapper().configure(
				DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		objectMapper.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);
		objectMapper.configure(
				MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
		objectMapper.configure(Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);
		
		objectMapper.configure(Feature.ALLOW_BACKSLASH_ESCAPING_ANY_CHARACTER,false);
		
		
			
		/*objectMapper.configure(
				SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, true);
		*/
		objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
		
		
		System.out.println("Consumer start ");
		consumer = new KafkaConsumer<String, String>(configProperties);

		System.out.println("Consumer end ");
		int i = 0;
		boolean insertFlag = false;
		boolean recCheck = false;

		try {
			consumer.subscribe(Arrays.asList(topicName));
			//dbConnectionimpl getconn = new dbConnectionimpl();
			System.out.println("Consumer Subscribe");
			while (true) {
				ConsumerRecords<String, String> records = consumer
						.poll(Poll_ms);
				//System.out.println("Polling");
				recCheck = false;
				for (TopicPartition partition : records.partitions()) {
					System.out.println(" ----partition()  ---- :" + partition);
					List<ConsumerRecord<String, String>> partitionRecords = records
							.records(partition);
					for (ConsumerRecord<String, String> record : partitionRecords) {

						String message = null;
						recCheck = true;
						JsongetString = new String(record.value());
						intPartition_Number = record.partition();
						intOffset = (int) record.offset();

					
						//System.out.println("JsongetString " + JsongetString);

						JsongetString = JsongetString.replaceAll("\\\\\"", "");

						JsongetString = JsongetString.replace("\\", "");
/*						JsongetString = JsongetString.replace("host name",
								"host_name");
						JsongetString = JsongetString.replace(" ip address",
								"ip_address");
						JsongetString = JsongetString.replace("ip address",
								"ip_address");

						JsongetString = JsongetString.replace(
								"collected serial number",
								"collected_serial_number");
						JsongetString = JsongetString.replace("serial number",
								"serial_number");

						JsongetString = JsongetString.replace(
								"collected equipment type",
								"collected_equipment_type");
						JsongetString = JsongetString.replace(
								"collected product id", "collected_product_id");
						JsongetString = JsongetString.replace("pce product id",
								"pce_product_id");
						JsongetString = JsongetString.replace(
								"snpid validation status",
								"snpid_validation_status");
						JsongetString = JsongetString.replace(
								"sn/pid validation status",
								"snpid_validation_status");
						JsongetString = JsongetString.replace("product id",
								"product_id");
						JsongetString = JsongetString.replace("product family",
								"product_family");
						JsongetString = JsongetString.replace("product name",
								"product_name");
						JsongetString = JsongetString.replace("equipment type",
								"equipment_type");
						JsongetString = JsongetString.replace("item type",
								"item_type");
						JsongetString = JsongetString.replace(
								"parent equipment", "parent_equipment");
						JsongetString = JsongetString.replace("child hardware",
								"child_hardware");
						JsongetString = JsongetString.replace("product type",
								"product_type");
						JsongetString = JsongetString.replace("list price",
								"list_price");
						JsongetString = JsongetString.replace(
								"last day of support", "last_day_of_support");
						JsongetString = JsongetString.replace(
								"recognized not recognized",
								"recognized_not_recognized");
						JsongetString = JsongetString.replace(
								"recognized/not recognized",
								"recognized_not_recognized");
						JsongetString = JsongetString.replace(
								"device diagnostics supported",
								"device_diagnostics_supported");
						JsongetString = JsongetString.replace("customer name",
								"customer_name");
						JsongetString = JsongetString.replace(
								"last inventory name", "last_inventory_name");
						JsongetString = JsongetString.replace(
								"bill-to customer", "bill_to_customer");
						JsongetString = JsongetString.replace(
								"installed-at site id", "installed_at_site_id");
						JsongetString = JsongetString.replace(
								"installed-at site name",
								"installed_at_site_name");
						JsongetString = JsongetString.replace(
								"installed-at country", "installed_at_country");
						JsongetString = JsongetString.replace(
								"installed-at address", "installed_at_address");
						JsongetString = JsongetString.replace(
								"installed-at city", "installed_at_city");
						JsongetString = JsongetString.replace(
								"installed-at state", "installed_at_state");
						JsongetString = JsongetString.replace(
								"installed-at province",
								"installed_at_province");
						JsongetString = JsongetString.replace(
								"installed-at postal code",
								"installed_at_postal_code");

						JsongetString = JsongetString.replace("snmp location",
								"snmp_location");
						JsongetString = JsongetString.replace(
								"contract number", "contract_number");
						JsongetString = JsongetString.replace(
								"contract status", "contract_status");
						JsongetString = JsongetString.replace(
								"coverage status", "coverage_status");
						JsongetString = JsongetString.replace(
								"covered begin date", "covered_begin_date");
						JsongetString = JsongetString.replace(
								"contract coverage ends",
								"contract_coverage_ends");
						JsongetString = JsongetString.replace("service level",
								"service_level");
						JsongetString = JsongetString.replace(
								"instance number", "instance_number");
						JsongetString = JsongetString.replace(
								"parent instance id", "parent_instance_id");
						JsongetString = JsongetString.replace(
								"install site guid", "install_site_guid");
						JsongetString = JsongetString.replace("gu name",
								"gu_name");
						JsongetString = JsongetString.replace(
								"authenticated site", "authenticated_site");
						JsongetString = JsongetString.replace("duplicated sn",
								"duplicated_sn");
						JsongetString = JsongetString.replace("relationship",
								"relationship");
						JsongetString = JsongetString.replace(
								"entitledcompany", "entitled_company");
						JsongetString = JsongetString.replace(
								"customerpartyid", "customer_party_id");
*/
						/*DateFormat dateFormat = new SimpleDateFormat(
								"yyyy-MM-dd");
						objectMapper
								.disable(DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE);
						// dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
						objectMapper.setDateFormat(dateFormat);*/
						
						//System.out.println(JsongetString);

						AIBCounsumerRespBean responseObj = null;

						try {
							
							/*SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX");
						    df.setTimeZone(TimeZone.getTimeZone("UTC"));
						    objectMapper
							.disable(DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE);
						    objectMapper.setDateFormat(df);*/
							
							 objectMapper
								.disable(DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE);
							
							responseObj = objectMapper.readValue(JsongetString,
									AIBCounsumerRespBean.class);
							objectMapper
									.configure(
											DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
											false);
							uniDataBatch.add(JsongetString);

						} catch (JsonParseException e) {
							System.out
									.println("1. Json Parse Exception exception- hence inserting into error table");
							e.printStackTrace();
							Long Appl_Req_ID = 0L;
							message = e.getMessage();
							message = message.substring(0, 70);

							if (!Strings
									.isNullOrEmpty(JsongetString.toString())) {
								// String jsonErr = JsongetString.toString();
								// String arr1[] = jsonErr.split(",");
								// int re_Id=
								// Integer.parseInt(arr1[1].replaceAll("[\"_:a-zA-Z]",
								// ""));
								// System.out.println("Appl_Req_Id => "+ re_Id);
								// dbCon.insertErrData(re_Id,JsongetString,
								// DupErrDataTable,intOffset,carsErrorTableColsList);
								String err_code = "JAVA - 0003";
								String error_message = "JSON Parsing Exception - Wrong JSON Format";

								String jsonErr = JsongetString.toString();
								String arr1[] = jsonErr.split("\"");

								if (JsongetString.length() > 2000)
									JsongetString = JsongetString.substring(0,
											2000);
								else
									JsongetString = JsongetString.substring(0,
											JsongetString.length());

								if (arr1.length > 1) {
									/*
									 * String re_Id = arr1[4].substring(
									 * arr1[3].indexOf(":") + 2).trim();
									 */
									String re_Id = arr1[4].substring(
											arr1[4].indexOf(":") + 2,
											arr1[4].indexOf(",")).trim();
									Appl_Req_ID = Long.parseLong(re_Id);

									dbCon.insertErrorErrData("ID", Appl_Req_ID,
											topicName, intOffset,
											intPartition_Number, JsongetString,
											err_code, message, "-1", "sysdate",
											"-1", "sysdate", aibErrorTable,
											errorTableColsList);
								}
								dupcounter++;
								System.out.println("Partition :"
										+ intPartition_Number
										+ " Offset going to updated :"
										+ intOffset);
								// hm.put(intPartition_Number, intOffset);

								// getconn.updateCommitedOffset(hm,
								// aibCommitTable);
								continue;
							}
						} catch (JsonMappingException e) {
							System.out
									.println("2. JSON Mapping Exception Exception - hence error data inserting into error table ");
							e.printStackTrace();
							String err_code = "JAVA - MAPPING EXCEPTION";
							String error_message = "JSON Mapping Exception - Attributes Not Able to Match with Java Object";
							Long Appl_Req_ID = 0L;
							message = e.getMessage();
							message = message.substring(0, 70);

							if (!Strings
									.isNullOrEmpty(JsongetString.toString())) {

								String jsonErr = JsongetString.toString();
								String arr1[] = jsonErr.split("\"");

								if (JsongetString.length() > 2000)
									JsongetString = JsongetString.substring(0,
											2000);
								else
									JsongetString = JsongetString.substring(0,
											JsongetString.length());

								if (arr1.length > 1) {

									String re_Id = arr1[4].substring(
											arr1[4].indexOf(":") + 2,
											arr1[4].indexOf(",")).trim();

									Appl_Req_ID = Long.parseLong(re_Id);

									dbCon.insertErrorErrData("ID", Appl_Req_ID,
											topicName, intOffset,
											intPartition_Number, JsongetString,
											err_code, message, "-1", "sysdate",
											"-1", "sysdate", aibErrorTable,
											errorTableColsList);

								}
								dupcounter++;
								System.out.println("Partition :"
										+ intPartition_Number
										+ " Offset going to updated :"
										+ intOffset);
								// hm.put(intPartition_Number, intOffset);
								// getconn.updateCommitedOffset(hm,
								// aibCommitTable);
								continue;
							}

						} catch (IOException e) {
							System.out
									.println("3. IO Exception Exception , hence error data  inserting into error table");
							String err_code = "JAVA - 0002";
							String error_message = "IO Exception  , Please Contact Support Administrator";
							Long Appl_Req_ID = 0L;

							message = e.getMessage();
							message = message.substring(0, 70);
							if (!Strings
									.isNullOrEmpty(JsongetString.toString())) {

								String jsonErr = JsongetString.toString();
								System.out.println("jsonErr" + jsonErr);
								String arr1[] = jsonErr.split("\"");

								if (JsongetString.length() > 2000)
									JsongetString = JsongetString.substring(0,
											2000);
								else
									JsongetString = JsongetString.substring(0,
											JsongetString.length());

								if (arr1.length > 1) {

									System.out.println(arr1[2]);
									String re_Id = arr1[4].substring(
											arr1[4].indexOf(":") + 2,
											arr1[4].indexOf(",")).trim();

									Appl_Req_ID = Long.parseLong(re_Id);
									dbCon.insertErrorErrData("ID", Appl_Req_ID,
											topicName, intOffset,
											intPartition_Number, JsongetString,
											err_code, message, "-1", "sysdate",
											"-1", "sysdate", aibErrorTable,
											errorTableColsList);

								}
								dupcounter++;
								System.out.println("Partition :"
										+ intPartition_Number
										+ " Offset going to updated :"
										+ intOffset);
								// hm.put(intPartition_Number, intOffset);
								// getconn.updateCommitedOffset(hm,
								// aibCommitTable);
								continue;
							}
							continue;
						}
						// hm.put(intPartition_Number, intOffset);
						if (size == uniDataBatch.size()) {
							err_msgs = "another error";
							dupcounter++;
						} else {
							prepareBatchQuery(comm, responseObj, intOffset,
									JsongetString, intPartition_Number,
									HDRTableName, HDRTableColsList, counter);
							counter++;
						}
						size = uniDataBatch.size();

						System.out.println("----- Size : " + size);

						if (i % 5 == 0) {
							long lastOffset = partitionRecords.get(
									partitionRecords.size() - 1).offset();
							System.out.println("----- lastOffset : "
									+ lastOffset);
						}
						if (++i % batchSize == 0) {
							System.out.println("----- Firt batchsize : ");
							if (uniDataBatch.size() > 0
									|| dupDataBatchMain.size() > 0) {
								if (uniDataBatch.size() > 0) {
									/*
									 * System.out .println(
									 * "1. ========== Insert into Unique table ==========="
									 * );
									 */
									dbCon.executeAIBUploadInterfaceProcedure(
											p_header_req_obj_i,
											p_line_req_tab_i, JASON_DATA,
											PARTITION_NUMBER, OFFSET,aibErrorTable,errorTableColsList);

									/*
									 * System.out.println(" intPartition_Number: "
									 * + intPartition_Number);
									 * System.out.println(" intOffset: " +
									 * intOffset);
									 */
									/*
									 * hm.put(intPartition_Number, intOffset);
									 * getconn.updateCommitedOffset(hm,
									 * aibCommitTable);
									 */
								}
								if (dupDataBatchMain.size() > 0) {
									/*
									 * System.out .println(
									 * "1. =========Insert into Duplicate table ============="
									 * ); System.out
									 * .println("----- setOFFSET_dup length : "
									 * + setOFFSET_dup.size());
									 */
									/*
									 * dbCon.executeSavaDupInterfaceBatchProcedure
									 * ( setjson_string, setOFFSET_dup, setdate,
									 * seterr_code, seterr_msg,
									 * setPARTITION_dup);
									 */
								}
								insertFlag = true;
							}
							insertFlag = true;

							uniDataBatch.clear();
							uniqDataBatch.clear();
							uniDataBatchMain.clear();
							dupDataBatchMain.clear();
							size = 0;
							counter = 0;
							dupcounter = 0;
						}
					}
					long lastOffset = partitionRecords.get(
							partitionRecords.size() - 1).offset();
				}
				/*if (recCheck) {
					getconn.updateNewOffset(hm, aibCommitTable, topicName,
							groupId);
				}*/
				if (uniDataBatch.size() > 0 || dupDataBatchMain.size() > 0) {
					if (uniDataBatch.size() > 0) {
						System.out
								.println("2. ========== Insert into Unique table =======");
						String file_name = "";
						String status = "NEW";
						String input_source = "CONTRACT";
						DateFormat dateFormat = new SimpleDateFormat(
								"yyyy/MM/dd HH:mm:ss");
						Date date = new Date();
						/*
						 * System.out.println("Start Time : " +
						 * dateFormat.format(date));
						 */

						dbCon.executeAIBUploadInterfaceProcedure(
								p_header_req_obj_i, p_line_req_tab_i,
								JASON_DATA, PARTITION_NUMBER, OFFSET,aibErrorTable,errorTableColsList);
						DateFormat dateFormat1 = new SimpleDateFormat(
								"yyyy/MM/dd HH:mm:ss");
						Date date1 = new Date();
						/*
						 * System.out.println("End Time : " +
						 * dateFormat1.format(date1));
						 */

						/*
						 * hm.put(intPartition_Number, intOffset);
						 * getconn.updateCommitedOffset(hm, aibCommitTable);
						 */
					}
					if (dupDataBatchMain.size() > 0) {
						System.out
								.println("2. ========= Insert into Duplicate table =========");
						/*
						 * dbCon.executeSavaDupInterfaceBatchProcedure(
						 * setjson_string, setOFFSET_dup, setdate, seterr_code,
						 * seterr_msg, setPARTITION_dup);
						 */
					}
					insertFlag = true;

					uniDataBatch.clear();
					uniqDataBatch.clear();
					uniDataBatchMain.clear();
					dupDataBatchMain.clear();
					size = 0;
					counter = 0;
					dupcounter = 0;
					System.out
							.println("Insert flag : " + insertFlag
									+ ", Unique batch Size : "
									+ uniDataBatch.size()
									+ ", Duplicate batch Size : "
									+ dupDataBatch.size());
					// System.exit(0);
				}
				insertFlag = false;
			}

		} catch (Exception e) {
			System.out.println("Exception in Consumer class : "
					+ e.getMessage());
			e.printStackTrace();
			String errMsg = e.getMessage().toString();
			if (errMsg.contains("poll()")) {
				System.out.println("True Contain");
				// SavaConsumer.main(argv);//change 17
			} else {
				System.out.println("Please check the other Ex exception");
			}
		} finally {
			consumer.close();
			System.out.println("===========we are in finally ===========");

		}

	}

	public static void prepareBatchQuery(commonUtility commObj,
			AIBCounsumerRespBean obj, Integer offset, String JsongetString,
			int Partition_Number, String Table, String colsList, int counter) {
		if (JsongetString.contains("\\")) {
			err_msg_req_id = "Invalid Jason";
			System.out.println("test - prepareDupDataBatchQuery");

			prepareDupDataBatchQuery(JsongetString, offset, Partition_Number,
					err_msg_req_id, Table, colsList, dupcounter);
			dupcounter++;
			counter--;
		} else
			try {
				System.out.println("********* Counter prepareBatchQuery : " + counter);

				p_header_req_obj_i.addAll(obj.getHeader());
				p_line_req_tab_i.addAll(obj.getLines());
				/*
				 * p_request_id_io.add(obj.getRequest_no());
				 * p_request_level_i.add(obj.getRequest_level());
				 * p_header_req_obj_i.addAll(obj.getHeader());
				 * p_line_req_tab_i.addAll(obj.getLines());
				 * p_override_req_tab_i.addAll(obj.getOverride());
				 * p_request_continue_i.add(obj.getRequest_continue());
				 */

				OFFSET.add((long) offset);
				PARTITION_NUMBER.add((long) Partition_Number);
				JASON_DATA.add((String) JsongetString);

			} catch (Exception e) {
				System.out.println("Exception in prepareBatchQuery - 1: ");

				e.printStackTrace();
				System.out.println("Exception in prepareBatchQuery : "
						+ e.getMessage());
			}
	}

	public static void prepareDupDataBatchQuery(String jsonData,
			Integer offset, int Partition_Number, String err_message,
			String Table, String colsList, int dupcounter) {
		String query = null;
		String err_code = "JAVA - 0001";
		System.out.println("********* DupCounter : " + dupcounter);

		// setAppl_ID_dup.add((long)obj.getAPPL_REQUEST_ID());

		setjson_string.add(jsonData);

		setdate.add("sysdate");
		seterr_code.add(err_code);
		seterr_msg.add(err_message);
		setOFFSET_dup.add(offset);
		setPARTITION_dup.add(Partition_Number);
		try {
			query = "insert into " + Table + " (" + colsList + ")"
					+ " values (" + jsonData + "', sysdate ," + offset
					+ err_message + ")";
			if (!Strings.isNullOrEmpty(query)) {
				dupDataBatchMain.add(query);
			}
		} catch (Exception e) {
			System.out.println("Exception in prepareBatchQuery : "
					+ e.getMessage());
		}
	}
}