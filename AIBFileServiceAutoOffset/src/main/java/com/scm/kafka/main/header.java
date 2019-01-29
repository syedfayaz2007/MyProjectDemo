package com.scm.kafka.main;

import java.sql.Date;
import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonFormat;

/*import com.scm.kafka.daoservices.file_parse_begin_date;
import com.scm.kafka.daoservices.file_parse_end_date;
import com.scm.kafka.daoservices.file_upload_time;
*/
public class header {
	//Header Objects
		
	//Getter and Setter Method
	
	Long  request_id;	
	String fileends;
	String total_records;
	
	
	
	
	
	Long cco_user_id ;
	public Long getCco_user_id() {
		return cco_user_id;
	}
	public void setCco_user_id(Long cco_user_id) {
		this.cco_user_id = cco_user_id;
	}
	public String getCco_user_name() {
		return cco_user_name;
	}
	public void setCco_user_name(String cco_user_name) {
		this.cco_user_name = cco_user_name;
	}
	String cco_user_name ;
	
	
	public String getFileends() {
		return fileends;
	}
	public void setFileends(String fileends) {
		this.fileends = fileends;
	}
	public Long getRequest_id() {
		return request_id;
	}
	public String getTotal_records() {
		return total_records;
	}
	public void setTotal_records(String total_records) {
		this.total_records = total_records;
	}
	public void setRequest_id(Long request_id) {
		this.request_id = request_id;
	}
	public String getAppl_name() {
		return appl_name;
	}
	public void setAppl_name(String appl_name) {
		this.appl_name = appl_name;
	}
	public String getFile_name() {
		return file_name;
	}
	public void setFile_name(String file_name) {
		this.file_name = file_name;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getCreated_by() {
		return created_by;
	}
	public void setCreated_by(String created_by) {
		this.created_by = created_by;
	}
	public Date getCreation_date() {
		return creation_date;
	}
	public void setCreation_date(Date creation_date) {
		this.creation_date = creation_date;
	}
	public Long getLast_updated_by() {
		return last_updated_by;
	}
	public void setLast_updated_by(Long last_updated_by) {
		this.last_updated_by = last_updated_by;
	}
	public Date getLast_update_date() {
		return last_update_date;
	}
	public void setLast_update_date(Date last_update_date) {
		this.last_update_date = last_update_date;
	}
	public String getInput_source() {
		return input_source;
	}
	public void setInput_source(String input_source) {
		this.input_source = input_source;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	String appl_name ;
	String file_name ;
	String status ;
	String created_by ;
	Date creation_date;
	Long last_updated_by ;
	Date last_update_date ;
	String input_source ;
	String description;
	String file_type;
	
	public String getFile_type() {
		return file_type;
	}
	public void setFile_type(String file_type) {
		this.file_type = file_type;
	}
	
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd'T'HH:mm:ss.SSSX")
	Timestamp file_upload_time  ;
	
	public Timestamp getFile_upload_time() {
		return file_upload_time;
	}
	public void setFile_upload_time(Timestamp file_upload_time) {
		this.file_upload_time = file_upload_time;
	}
	public Date getFile_parse_begin_date() {
		return file_parse_begin_date;
	}
	public void setFile_parse_begin_date(Date file_parse_begin_date) {
		this.file_parse_begin_date = file_parse_begin_date;
	}
	public Date getFile_parse_end_date() {
		return file_parse_end_date;
	}
	public void setFile_parse_end_date(Date file_parse_end_date) {
		this.file_parse_end_date = file_parse_end_date;
	}
	Date  file_parse_begin_date  ;
	Date file_parse_end_date  ;

	
	
    public Timestamp getParsing_start() {
		return parsing_start;
	}
	public void setParsing_start(Timestamp parsing_start) {
		this.parsing_start = parsing_start;
	}
	public Timestamp getParsing_end() {
		return parsing_end;
	}
	public void setParsing_end(Timestamp parsing_end) {
		this.parsing_end = parsing_end;
	}
	
    
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd'T'HH:mm:ss.SSSX")
	Timestamp  parsing_start  ;
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd'T'HH:mm:ss.SSSX")
	Timestamp parsing_end  ;
	
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd'T'HH:mm:ss.SSSX")
	Timestamp filedate  ;
	
	
	/*public Long getRequest_id() {return request_id;}
	public void setHDRrequest_id(Long request_id) {this.request_id = request_id;}
	
	public String getHDRappl_name() {return appl_name;	}
	public void setHDRappl_name(String appl_name) {this.appl_name = appl_name;}
	public String getHDRfile_name() {return file_name;	}
	public void setHDRfile_name(String file_name) {this.file_name = file_name;}		
	public String getHDRstatus() {return status;	}
	public void setHDRstatus(String status) {this.status = status;}		
	
	public Long getHDRcreated_by() {return created_by;	}
	public void setHDRcreated_by(Long created_by) {this.created_by = created_by;}
	public Date getHDRcreation_date() {return creation_date;	}
	public void setHDRcreation_date(Date creation_date) {this.creation_date = creation_date;}
	public Long getHDRlast_updated_by() {return last_updated_by;	}
	public void setHDRlast_updated_by(Long last_updated_by) {this.last_updated_by = last_updated_by;}
	public Date getHDRlast_update_date() {return last_update_date;	}
	public void setHDRlast_update_date(Date last_update_date) {this.last_update_date = last_update_date;}
	
	public String getHDRinput_source() {return input_source;	}
	public void setHDRinput_source(String input_source) {this.input_source = input_source;}	
	public String getHDRdescription() {return description;	}
	public void setHDRdescription(String description) {this.description = description;}	*/
	
	public Timestamp getFiledate() {
		return filedate;
	}
	public void setFiledate(Timestamp filedate) {
		this.filedate = filedate;
	}
	String entitled_company;
	public String getEntitled_company() {
		return entitled_company;
	}
	public void setEntitled_company(String entitled_company) {
		this.entitled_company = entitled_company;
	}
	public String getInventoryname() {
		return inventoryname;
	}
	public void setInventoryname(String inventoryname) {
		this.inventoryname = inventoryname;
	}
	public String getApplianceid() {
		return applianceid;
	}
	public void setApplianceid(String applianceid) {
		this.applianceid = applianceid;
	}
	public String getInvuploaddate() {
		return invuploaddate;
	}
	public void setInvuploaddate(String invuploaddate) {
		this.invuploaddate = invuploaddate;
	}
	public String getCustomerpartyId() {
		return customerpartyId;
	}
	public void setCustomerpartyId(String customerpartyId) {
		this.customerpartyId = customerpartyId;
	}
	String inventoryname;
	String applianceid;
	String invuploaddate;
	String customerpartyId;
	
	public String getAttribute1() {
		return attribute1;
	}
	public void setAttribute1(String attribute1) {
		this.attribute1 = attribute1;
	}
	public String getAttribute2() {
		return attribute2;
	}
	public void setAttribute2(String attribute2) {
		this.attribute2 = attribute2;
	}
	public String getAttribute3() {
		return attribute3;
	}
	public void setAttribute3(String attribute3) {
		this.attribute3 = attribute3;
	}
	public String getAttribute4() {
		return attribute4;
	}
	public void setAttribute4(String attribute4) {
		this.attribute4 = attribute4;
	}
	public String getAttribute5() {
		return attribute5;
	}
	public void setAttribute5(String attribute5) {
		this.attribute5 = attribute5;
	}
	public String getAttribute6() {
		return attribute6;
	}
	public void setAttribute6(String attribute6) {
		this.attribute6 = attribute6;
	}
	public String getAttribute7() {
		return attribute7;
	}
	public void setAttribute7(String attribute7) {
		this.attribute7 = attribute7;
	}
	public String getAttribute8() {
		return attribute8;
	}
	public void setAttribute8(String attribute8) {
		this.attribute8 = attribute8;
	}
	public String getAttribute9() {
		return attribute9;
	}
	public void setAttribute9(String attribute9) {
		this.attribute9 = attribute9;
	}
	public String getAttribute10() {
		return attribute10;
	}
	public void setAttribute10(String attribute10) {
		this.attribute10 = attribute10;
	}
	String attribute1;
	String attribute2;
	String attribute3;
	String attribute4;
	String attribute5;
	String attribute6;
	String attribute7;
	String attribute8;
	String attribute9;
	String attribute10;
	
	
	
	
	

}
	

