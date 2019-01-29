package com.scm.kafka.daoservices;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import com.scm.kafka.main.header;
import com.scm.kafka.main.lines;
import com.scm.kafka.servicesprovider.commonUtility;

import joptsimple.internal.Strings;
import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.OracleTypes;
import oracle.sql.ARRAY;
import oracle.sql.ArrayDescriptor;
import oracle.sql.STRUCT;
import oracle.sql.StructDescriptor;

public class dbConnectionimpl implements dbConnection {
	static Connection con = null;
	// Connection con1 = null;
	static String content = "PLSQL ERROR: Return Code -1 for exception occured in PLSQL Block";
	static int RETURN_VARIABLE = 0;
	static long totaltimetillnow;
	Long request_id;
	static Integer RETURN_VARIABLE1 = null;
	static String RETURN_VARIABLE2 = null;
	static String RETURN_VARIABLE3 = null;
	static String RETURN_VARIABLE4 = null;
	static int reqNo = 123;
	static int applicationRequestID = 0;
	static String attribute3QuoteId = null, strQuoteNumber = null,
			strTransactionType = null, strSubTrxType = null;
	File file1 = new File("SAVAJOBFAIL.txt");
	String DB_URL;
	String DB_USER_ID;
	String DB_PASSWORD;
	String groupID;
	String topicName;
	String carsErrorTableName, carsErrorTableNameColsList, carsOutPutTableName,
			carsOutputTableColsList;

	OracleCallableStatement callStmt1;
	
	public dbConnectionimpl(){
		
		try {
			commonUtility comm = new commonUtility();
			Properties prop = comm.readProp();
			DB_URL = prop.getProperty("dbHost");
			DB_USER_ID = prop.getProperty("dbUserID");
			DB_PASSWORD = prop.getProperty("dbPassword");
		
			Class.forName("oracle.jdbc.driver.OracleDriver");
			con = DriverManager.getConnection(DB_URL.trim() + "",
					DB_USER_ID.trim(), DB_PASSWORD.trim());
			
			
		} catch (Exception e) {
			System.err.println("Exception in DB connection" + e.getMessage());
			e.getStackTrace();
			
		}

		
	}

	public Connection con() {

		System.out.println(" Connection Methond");
		commonUtility comm = new commonUtility();
		Properties prop = comm.readProp();
		DB_URL = prop.getProperty("dbHost");
		DB_USER_ID = prop.getProperty("dbUserID");
		DB_PASSWORD = prop.getProperty("dbPassword");
		groupID = prop.getProperty("groupId");
		topicName = prop.getProperty("topicName");

		try {

			Class.forName("oracle.jdbc.driver.OracleDriver");
			con = DriverManager.getConnection(DB_URL.trim() + "",
					DB_USER_ID.trim(), DB_PASSWORD.trim());
			
			return con;
		} catch (Exception e) {
			System.err.println("Exception in DB connection" + e.getMessage());
			e.getStackTrace();
			return null;
		}

	}

	public Statement dbConn() {

		commonUtility comm = new commonUtility();
		Properties prop = comm.readProp();
		DB_URL = prop.getProperty("dbHost");
		DB_USER_ID = prop.getProperty("dbUserID");
		DB_PASSWORD = prop.getProperty("dbPassword");
		groupID = prop.getProperty("groupId");
		topicName = prop.getProperty("topicName");

		Statement stmt = null;
		try {

			Class.forName("oracle.jdbc.driver.OracleDriver");
            con = DriverManager.getConnection(DB_URL, DB_USER_ID, DB_PASSWORD);
		    stmt = con.createStatement();
			return stmt;
			
		} catch (Exception e) {
			System.err.println("Exception in dbConn" + e.getMessage());
			e.getStackTrace();
			return null;
		}
	}

	
	public HashMap<Integer, Integer> executeQuery(String query) {
		HashMap<Integer, Integer> hm = new HashMap<Integer, Integer>();
		try {
			System.out.println("inside executeQuery function");
			Statement stmt = dbConn();
			String query1 = null;
			try {
				query1 = query;
			} catch (NullPointerException e) {
				System.out.println("Null Pointer Exception thrown");
			}
			System.out.println("Query : " + query1);
			if (!Strings.isNullOrEmpty(query1)) {
				ResultSet rs1 = stmt.executeQuery(query1);
				while (rs1.next()) {
				/*	System.out
							.println("Commited Offset found in Table => Partition : "
									+ rs1.getInt("PARTITION_NUMBER")
									+ " , Offset : " + rs1.getInt("OFFSET"));*/
					hm.put(rs1.getInt("PARTITION_NUMBER"), rs1.getInt("OFFSET"));
				}
				//con.close();
				return hm;
			}
		} catch (Exception e) {
			System.out.println("Exception in executeQuery : "
					+ e.getStackTrace());
			e.printStackTrace();
		}
		return null;
	}


	public void updateCommitedOffset(Map<Integer, Integer> hm,
			String ccwCommitTable) throws SQLException {
		try {
			Statement stmt = dbConn();
			for (Map.Entry e : hm.entrySet()) {
				String offsetCommitQuery = "update " + ccwCommitTable
						+ " set OFFSET =" + e.getValue()
						+ " where partition_number = " + e.getKey()
						+ " and  consumer = '" + groupID + "'";
				/*System.out.println("updateCommitedOffset  -- After Insert "
						+ offsetCommitQuery);*/
				if (!Strings.isNullOrEmpty(offsetCommitQuery)) {
					stmt.execute(offsetCommitQuery);
					con.commit();
				}
			}
			
		} catch (Exception e) {
			System.out.println("updateCommitedOffset Exception : "
					+ e.getMessage());
			con.close();
		}
		finally{
			//System.out.println("finally block - updateCommitedOffset ");
			//			con.close();
			
		}
	}

	public void updateNewOffset(Map<Integer, Integer> hm,
			String ccwCommitTable, String topicName, String group)
			throws SQLException {
		try {
			Statement stmt = dbConn();
			//System.out.println("inside updateNewOffset function ");
			String query = "select * from " + ccwCommitTable
					+ " where consumer = '" + groupID + "'";
			ResultSet rs = stmt.executeQuery(query);
			boolean flag = false;
			for (Map.Entry e : hm.entrySet()) {
				while (rs.next()) {
					if (Integer.parseInt(e.getKey().toString()) == rs.getInt(1)) {
						flag = true;
						break;
					}
				}
				if (flag) {
					String offsetCommitQuery = "update " + ccwCommitTable
							+ " set OFFSET =" + e.getValue()
							+ " where partition_number = " + e.getKey()
							+ " and  consumer = '" + groupID + "'";

					System.out.println("offsetCommitQuery" + offsetCommitQuery);
					
					if (!Strings.isNullOrEmpty(offsetCommitQuery)) {
						stmt.execute(offsetCommitQuery);
						con.commit();
					}
				} else {
					insertCommitedOffset(
							Integer.parseInt(e.getKey().toString()),
							Integer.parseInt(e.getValue().toString()),
							ccwCommitTable, topicName, group);
				}
			}
		} catch (Exception e) {
			System.out.println("updateNewOffset Exception : " + e.getMessage());
			con.close();
		}
		finally{
			//System.out.println("finally block -updateBeginningOffset ");
			//con.close();
			
		}
	
	}

	public void updateBeginningOffset(long l, int partition_Number,
			String ccwCommitTable) throws SQLException {
		try {
			Statement stmt = dbConn();
			String offsetCommitQuery = "update " + ccwCommitTable
					+ " set OFFSET =" + l + " where partition_number = "
					+ partition_Number + " and  consumer = '" + groupID + "'";
			//System.out.println("offsetCommitQuery " + offsetCommitQuery);
			if (!Strings.isNullOrEmpty(offsetCommitQuery)) {
				stmt.execute(offsetCommitQuery);
				con.commit();
			}
			
		} catch (Exception e) {
			System.out.println("insertCommitedOffset Exception : "
					+ e.getMessage());
			con.close();
		}
		finally{
			//System.out.println("finally block -updateBeginningOffset ");
			//con.close();
			
		}
		
	}

	public void insertCommitedOffset(int part_num, long intOffset,
			String ccwCommitTable, String topicName, String groupId)
			throws SQLException {
		try {
			
			Statement stmt = dbConn();
			String offsetCommitQuery = "insert into " + ccwCommitTable
					+ "(Partition_number, offset,topic,consumer) values ("
					+ part_num + "," + intOffset + ",'" + topicName + "','"
					+ groupId + "')";
			//System.out.println("Offset Query : " + offsetCommitQuery);
			if (!Strings.isNullOrEmpty(offsetCommitQuery)) {
				stmt.execute(offsetCommitQuery);
				con.commit();
				
			}
		} catch (Exception e) {
			System.out.println("insertCommitedOffset Exception : "
					+ e.getMessage());
			con.close();
		}
		finally{
			//System.out.println("finally block -insertCommitedOffset ");
			//con.close();
		}
	}

	
	public void insertErrorErrData(String ID, Long APPL_REQUEST_ID,
			String TOPIC, Integer offset, Integer partition, String Json,
			String ERROR_CODE, String ERROR_MESSAGE, String created_by,
			String created_date, String updated_by, String updated_date,
			String errTable, String errorTableColsList) {
		ID = " xxcss_o.XXCSS_CARS_KAFKA_ERRLOG_SEQ.nextval ";
		try {
			Statement stmt = dbConn();
			String query = "insert into " + errTable + " ("
					+ errorTableColsList + ")" + " values (" + ID
					+ "," + APPL_REQUEST_ID + " ,'" + TOPIC + "' ," + offset
					+ "," + partition + ",'" + Json + "','" + ERROR_CODE
					+ "','" + ERROR_MESSAGE + "','" + created_by + "',"
					+ created_date + ",'" + updated_by + "'," + updated_date
					+ "" + ")";
			//System.out.println("Err Qruey : " + query);
			if (!Strings.isNullOrEmpty(query)) {
				stmt.execute(query);
				con.commit();
				//con.close();
			}
		} catch (Exception e) {
			System.out
					.println("Error in insert Error data : " + e.getMessage());
		}
	}

		
	public void insertAIBErrData(String ID, Long APPL_REQUEST_ID, String TOPIC,
			Integer offset, Integer partition, String Json, String ERROR_CODE,
			String ERROR_MESSAGE, String created_by, String created_date,
			String updated_by, String updated_date, String errTable,
			String SavaDupErrDataTableColsList) {
		ID = " xxcss_o.XXCSS_CARS_KAFKA_ERRLOG_SEQ.nextval ";
		try {
			Statement stmt = dbConn();
			String query = "insert into " + errTable + " ("
					+ SavaDupErrDataTableColsList + ")" + " values (" + ID
					+ "," + APPL_REQUEST_ID + " ,'" + TOPIC + "' ," + offset
					+ "," + partition + ",'" + Json + "','" + ERROR_CODE
					+ "','" + ERROR_MESSAGE + "','" + created_by + "',"
					+ created_date + ",'" + updated_by + "'," + updated_date
					+ "" + ")";
			// System.out.println("Err Qruey : "+ query);
			if (!Strings.isNullOrEmpty(query)) {
				stmt.execute(query);
				con.commit();
				//con.close();
			}
		} catch (Exception e) {
			System.out
					.println("Error in insert Error data : " + e.getMessage());
		}
	}

	/*
	 * public void executeSavaDupInterfaceBatchProcedure( List<String>
	 * setjson_string, List<Integer> setOFFSET_dup, List<String> setdate,
	 * List<String> seterr_code, List<String> seterr_msg, List<Integer>
	 * setPARTITION_dup) throws SQLException { try {
	 * Class.forName("oracle.jdbc.driver.OracleDriver"); } catch
	 * (ClassNotFoundException e) { e.printStackTrace(); }
	 * 
	 * // con = // DriverManager.getConnection(
	 * "jdbc:oracle:thin:@(DESCRIPTION=(CONNECT_TIMEOUT=5)(TRANSPORT_CONNECT_TIMEOUT=3)(RETRY_COUNT=1)(ADDRESS_LIST=(LOAD_BALANCE=ON)(FAILOVER=ON)(ADDRESS=(PROTOCOL=TCP)(HOST=173.38.69.52)(PORT=1541))(ADDRESS=(PROTOCOL=TCP)(HOST=173.38.69.53)(PORT=1541))(ADDRESS=(PROTOCOL=TCP)(HOST=173.38.69.54)(PORT=1541)))(CONNECT_DATA=(SERVICE_NAME=TS3CSF_SRVC_OTH.cisco.com)(SERVER=DEDICATED)))"
	 * , // "XXCSS_SAIB_DAAS_U", "TRQ_1N3v");//TS3CSF con = con();
	 * 
	 * String setjson_string1[] = setjson_string .toArray(new
	 * String[setjson_string.size()]); String setdate1[] = setdate.toArray(new
	 * String[setdate.size()]); String seterr_msg1[] = seterr_msg .toArray(new
	 * String[seterr_msg.size()]); String seterr_code1[] =
	 * seterr_code.toArray(new String[seterr_code .size()]); Integer
	 * setOFFSET_dup1[] = setOFFSET_dup .toArray(new
	 * Integer[setOFFSET_dup.size()]); Integer setPARTITION_dup1[] =
	 * setPARTITION_dup .toArray(new Integer[setPARTITION_dup.size()]); //
	 * String proc = //
	 * "{call Apps.xxcss_scm_sava_kafka_pkg.SAVA_Kafka_Err_Data_Insert (?,?,?,?,?)}"
	 * ; String proc =
	 * "{call Apps.xxcss_cars_trx.cars_errinsert_proc (?,?,?,?,?,?,?)}";
	 * 
	 * try { ArrayDescriptor oracleVarchar2Collection = ArrayDescriptor
	 * .createDescriptor("APPS.FND_TABLE_OF_VARCHAR2_4000", con);
	 * ArrayDescriptor oracleNumberCollection = ArrayDescriptor
	 * .createDescriptor("APPS.FND_TABLE_OF_NUMBER", con);
	 * System.out.println("Calling Duplicate procedure....!!!");
	 * CallableStatement callStmt = con.prepareCall(proc); ARRAY set_json = new
	 * ARRAY(oracleVarchar2Collection, con, setjson_string1); ARRAY setoffset =
	 * new ARRAY(oracleNumberCollection, con, setOFFSET_dup1); ARRAY
	 * setPARTITION = new ARRAY(oracleNumberCollection, con, setPARTITION_dup1);
	 * ARRAY setDate = new ARRAY(oracleVarchar2Collection, con, setdate1); ARRAY
	 * setERROR_MESSAGE = new ARRAY(oracleVarchar2Collection, con, seterr_msg1);
	 * ARRAY setERROR_CODE = new ARRAY(oracleVarchar2Collection, con,
	 * seterr_code1);
	 * 
	 * callStmt.setObject(1, set_json); callStmt.setObject(2, setDate);
	 * callStmt.setObject(3, setoffset); callStmt.setObject(4, setERROR_CODE);
	 * callStmt.setObject(5, setERROR_MESSAGE); callStmt.setObject(6,
	 * setPARTITION);
	 * 
	 * callStmt.registerOutParameter(7, java.sql.Types.INTEGER);
	 * callStmt.execute(); RETURN_VARIABLE = callStmt.getInt(7);
	 * System.out.println("this is the value for return variable " +
	 * RETURN_VARIABLE); if (RETURN_VARIABLE == -1) { try {
	 * 
	 * file1 = new File(new File( System.getProperty("java.io.tmpdir")),
	 * "SAVAJOBFAIL.TXT"); if (file1.createNewFile()) {
	 * System.out.println("File is created!"); FileWriter fw = new
	 * FileWriter(file1.getAbsoluteFile()); BufferedWriter bw = new
	 * BufferedWriter(fw); bw.write(content); bw.close();
	 * System.exit(RETURN_VARIABLE); }
	 * 
	 * } catch (IOException e) {
	 * 
	 * e.printStackTrace(); }
	 * 
	 * } else { System.out.println("No file created"); } // callStmt.execute();
	 * System.out.println("====Duplicate batch inserted successfully===="); }
	 * catch (SQLException buex) {
	 * System.out.println("Exception in executeDupBatchProcedure : " +
	 * buex.getMessage()); } setjson_string.clear(); setOFFSET_dup.clear();
	 * setdate.clear(); seterr_msg.clear(); seterr_code.clear();
	 * setPARTITION_dup.clear(); }
	 */
	public void executeAIBUploadInterfaceProcedure(List<header> HEADER_OBJ,
			List<lines> LINE_OBJ, List<String> JASON_DATA,
			List<Long> setPARTITION, List<Long> setOFFSET,String aibErrorTable,String errorTableColsList) throws SQLException {

		//Connection con = null;
		OracleCallableStatement callStmt1 = null;
		Statement stmt = null;
		ResultSet rsmtuser = null;
		String strattribute2 = null;
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		try {
			// = con();
			//System.out.println("Before Connection");
            
			/*if( con == null){
				System.out.println("Acquire Connection");
			//con = con();
			}
			
           
			 || !(con.isValid(0))
			*/
			
			con = con();

			header[] HEADER_OBJ1 = HEADER_OBJ.toArray(new header[HEADER_OBJ
					.size()]);
			lines[] LINE_OBJ1 = LINE_OBJ.toArray(new lines[LINE_OBJ.size()]);

			String JASON_DATA1[] = JASON_DATA.toArray(new String[JASON_DATA
					.size()]);

			String proc = "{call APPS.XXCSS_AIB_INSIGHT_PVT_PKG.POPULATE_HDR_LINE_DATA(?,?,?,?,?)}";
			
			//System.out.println("Before Connection");

			StructDescriptor oracleHDRObjectCollection = StructDescriptor
					.createDescriptor("APPS.XXCSS_AIB_HDR_TYPE", con);
			StructDescriptor oracleLineObjectCollection = StructDescriptor
					.createDescriptor("APPS.XXCSS_AIB_LINE_TYPE", con);
			
		     strattribute2 = "TOPIC~" + topicName + "#PARTITION~" +setPARTITION.get(0) + "#OFFSET~" + setOFFSET.get(0);
			

			
			callStmt1 = (OracleCallableStatement) con.prepareCall(proc);

			STRUCT headerStruct = null;
			STRUCT linesStruct = null;
			long user_id = -1;

			int l_count = 0;

			STRUCT[] structArray = new STRUCT[HEADER_OBJ.size()];
			header HDRproject = null;

			for (int i = 0; i < HEADER_OBJ1.length; ++i) {
				HDRproject = HEADER_OBJ1[i];
				try {
					String sql = "select user_id from apps.fnd_user where user_name = upper('"
							+ HDRproject.getCreated_by() + "')";

				//	System.out.println("sql : " + sql);
					stmt = con.createStatement();
					rsmtuser = stmt.executeQuery(sql);
					while (rsmtuser.next())
						user_id = rsmtuser.getInt(1);
					//System.out.println("sql value: " + user_id);
				} catch (SQLException e) {
					e.printStackTrace();
				} finally {
					rsmtuser.close();
					stmt.close();
				}
				
					Object[] headerFields = new Object[] {
						HDRproject.getRequest_id(), HDRproject.getFile_name(),
						"NEW", user_id, HDRproject.getCreation_date(), user_id,
						HDRproject.getLast_update_date(), HDRproject.getFile_type(),
						HDRproject.getDescription(),
						HDRproject.getTotal_records(),
						HDRproject.getCco_user_id(), HDRproject.getCreated_by(),
						HDRproject.getFile_upload_time(),
						HDRproject.getParsing_start(),
						HDRproject.getParsing_end(),
						"","","","","","","","","","",
						HDRproject.getEntitled_company(),
						HDRproject.getCustomerpartyId(),
						HDRproject.getApplianceid(),
						HDRproject.getInventoryname(),
						HDRproject.getInvuploaddate()
						};

					
				/*System.out.println("EC "+HDRproject.getEntitled_company());
				System.out.println("CI "+ HDRproject.getCustomerpartyId() );
				System.out.println("AI "+HDRproject.getApplianceid());
				System.out.println("getInventoryname"+HDRproject.getInventoryname());
				System.out.println("getInvuploaddate"+HDRproject.getInvuploaddate());*/
				
				headerStruct = new STRUCT(oracleHDRObjectCollection, con,
						headerFields);

				structArray[i] = headerStruct;
			}

			ArrayDescriptor headerTypeArrayDesc = ArrayDescriptor
					.createDescriptor("APPS.XXCSS_AIB_HDR_TAB", con);
			ARRAY arrayOfProjects = new ARRAY(headerTypeArrayDesc, con,
					structArray);
			//System.out.println("After Header Construct");
			lines LINESproject;
			STRUCT[] structArrayLines = new STRUCT[LINE_OBJ.size()];

			/*try {
				if (HDRproject.getFileends().equals("true")) {
					String sql = " update apps.xxcss_aib_header_stg set end_of_file = 'true' where request_id ="
							+ HDRproject.getRequest_id() + "";
					System.out.println("sql : " + sql);
					stmt = con.createStatement();
					rsmtuser = stmt.executeQuery(sql);
				}

			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				rsmtuser.close();
				stmt.close();
			}*/

			int value = 0;

			/*try {
				String sql = "select nvl(max(request_line_id),0) from apps.xxcss_aib_line_stg where request_id ="
						+ HDRproject.getRequest_id() + "";
				// System.out.println("sql : " + sql);
				stmt = con.createStatement();
				rsmtuser = stmt.executeQuery(sql);
				while (rsmtuser.next())
					value = rsmtuser.getInt(1);
				System.out.println("sql value: " + value);
			}

			catch (SQLException e) {
				e.printStackTrace();
			} finally {
				rsmtuser.close();
				stmt.close();
			}*/

			for (int i = 0; i < (LINE_OBJ1.length); i++) {
				LINESproject = LINE_OBJ1[i];
				try {
					String cvb = null;
					String cce = null;
					String ldos= null;
					
					
					Object[] linesFields = new Object[] {
							HDRproject.getRequest_id(),ldos,
							LINESproject.getHost_name(),
							LINESproject.getIp_address(),
							LINESproject.getSerial_number(),
							LINESproject.getCollected_serial_number(),
							LINESproject.getCollected_equipment_type(),
							LINESproject.getCollected_product_id(),
							LINESproject.getPce_product_id(),
							LINESproject.getSn_pid_validation_status(),
							LINESproject.getProduct_id(),
							LINESproject.getProduct_family(),
							LINESproject.getProduct_name(),
							LINESproject.getEquipment_type(),
							LINESproject.getItem_type(),
							LINESproject.getParent_equipment(),
							LINESproject.getChild_hardware(),
							LINESproject.getProduct_type(),
							LINESproject.getList_price(),
							ldos,
							LINESproject.getRecognized_not_recognized(),
							LINESproject.getDevice_diagnostics_supported(),
							LINESproject.getCustomer_name(),
							LINESproject.getLast_inventory_name(),
							LINESproject.getBill_to_customer(),
							LINESproject.getInstalled_at_site_id(),
							LINESproject.getInstalled_at_site_name(),
							LINESproject.getInstalled_at_country(),
							LINESproject.getInstalled_at_address(),
							LINESproject.getInstalled_at_city(),
							LINESproject.getInstalled_at_state(),
							LINESproject.getInstalled_at_province(),
							LINESproject.getInstalled_at_postal_code(),
							LINESproject.getSnmp_location(),
							LINESproject.getContract_number(),
							LINESproject.getContract_status(),
							LINESproject.getCoverage_status(),
							cvb,
							cce,
							LINESproject.getService_level(),
							LINESproject.getInstance_number(),
							LINESproject.getParent_instance_id(),
							LINESproject.getInstall_site_guid(),
							LINESproject.getGu_name(),
							LINESproject.getAuthenticated_site(),
							LINESproject.getDuplicated_sn(),
							LINESproject.getRelationship(),
							LINESproject.getSheet_name(),
							LINESproject.getRequest_status(), "NEW",
							LINESproject.getCreated_by(),
							HDRproject.getCreation_date(),
							LINESproject.getLast_updated_by(),
							LINESproject.getLast_update_date(),
							LINESproject.getCustomer_party_id(),
							LINESproject.getEntitled_company() ,
							"","","","","","","","","","",LINESproject.getLast_day_of_support(),LINESproject.getCovered_begin_date(),
							LINESproject.getContract_coverage_ends(),LINESproject.getSource_input_Type()
							};
					// System.out.println("Termination Date :");
					linesStruct = new STRUCT(oracleLineObjectCollection, con,
							linesFields);

				} catch (NullPointerException e) {
					System.out.println("Null Pointer Exception");
					continue;
				} catch (StringIndexOutOfBoundsException e) {
					System.out
							.println("StringIndexOutOfBoundsException Exception");
					l_count = l_count + 1;
					continue;
				}
				structArrayLines[i] = linesStruct;

			}
			
/*			DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			Date date = new Date();
			System.out.println("Start Time : "+dateFormat.format(date));
		*/	

			long start =  System.nanoTime();
			// your code
			
			System.out.println("strattribute2....!!!" + strattribute2);
			
			//System.out.println("structArrayLines Lenght : "	+ structArrayLines.length);
			ArrayDescriptor LinesTypeArrayDesc = ArrayDescriptor
					.createDescriptor("APPS.XXCSS_AIB_LINE_TAB", con);
			ARRAY LinesArrayOfProjects = new ARRAY(LinesTypeArrayDesc, con,
					structArrayLines);
			callStmt1.setObject(1, arrayOfProjects);// setREQUEST_NUMBER);
			callStmt1.setObject(2, LinesArrayOfProjects);
			callStmt1.registerOutParameter(3, OracleTypes.VARCHAR);// Status
			callStmt1.setObject(4, HDRproject.getRequest_id());// Status
			callStmt1.setObject(5, strattribute2);// Status
			// System.out.println("***************************2222************");
			callStmt1.execute();
			
			/*DateFormat dateFormat1 = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			Date date1 = new Date();
			System.out.println("Start Time : "+dateFormat1.format(date1));
			*/
			/*long end = System.nanoTime();
			long diff = end-start;
			long diffInMillis = diff/1000000;
			totaltimetillnow =  totaltimetillnow + diffInMillis;
			
			System.out.println("diffInMillis :" + diffInMillis + "- diff " + diff);
			System.out.println("totaltimetillnow :" + totaltimetillnow );
			*/

			RETURN_VARIABLE2 = callStmt1.getString(3);
			System.out.println("API Call Status :" + RETURN_VARIABLE2);

			if (l_count > 0) {
				try {
					String sql_value = " update apps.xxcss_aib_header_stg set attribute2 = '"
							+ l_count
							+ "' where request_id ="
							+ HDRproject.getRequest_id() + "";
					System.out.println("sql : " + sql_value);
					stmt = con.createStatement();
					rsmtuser = stmt.executeQuery(sql_value);

				} catch (SQLException e) {
					e.printStackTrace();
				} finally {
					rsmtuser.close();
					stmt.close();
				}

			}

			if (RETURN_VARIABLE2.equalsIgnoreCase("E")) {
				// Set value of Error
				RETURN_VARIABLE4 = "ERROR";
				String carsRequestID = "111";
				String ErrorCode = "ERROR";
				String ErrorMessage = "ERROR";
				Integer intPartition = setPARTITION.get(0).intValue();
				Integer intOffset = setOFFSET.get(0).intValue();
				String json_data = JASON_DATA.get(0);
				// Insert in CARS Kafka Error Log
				insertErrorErrData("ID", HDRproject.getRequest_id(), topicName,
						intOffset, intPartition, json_data, ErrorCode,
						ErrorMessage, "-1", "sysdate", "-1", "sysdate",
						aibErrorTable, carsErrorTableNameColsList);
			}

			if (RETURN_VARIABLE == -1) {
				try {
					if (file1.exists()) {
						file1.delete();
					}
					if (file1.createNewFile()) {
						System.out.println("File is created!");
						FileWriter fw = new FileWriter(file1.getAbsoluteFile());
						BufferedWriter bw = new BufferedWriter(fw);
						bw.write(content);
						bw.close();
						System.exit(RETURN_VARIABLE);
					}

				} catch (IOException e) {

					e.printStackTrace();
				}

			} else {
				System.out.println("No file created");

			}

			// callStmt.execute();
			System.out.println("====batch inserted successfully====");
		} catch (NullPointerException npex) {

			System.out.println(" Exception in executeBatchProcedure : ");
			npex.printStackTrace();

		} catch (SQLException buex) {
			buex.printStackTrace();
			System.out.println(" Exception in executeBatchProcedure : "
					+ buex.getMessage());

		} finally {
			callStmt1.close();
			//con1.close();
			//System.out.println(" Closing DB Connection Final Block : ");
			con.close();
		}

		// REQUEST_NUMBER.clear();
		// REQUEST_LEVEL.clear();
		HEADER_OBJ.clear();
		LINE_OBJ.clear();

		setPARTITION.clear();
		setOFFSET.clear();

	}

}
