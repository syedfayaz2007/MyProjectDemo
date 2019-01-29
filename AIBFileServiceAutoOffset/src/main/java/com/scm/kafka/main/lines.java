package com.scm.kafka.main;

import java.sql.Date;


public class lines {
	
	
	
	Long customer_party_id;
	
	String source_input_Type;
	
	
		
	
	public String getSource_input_Type() {
		return source_input_Type;
	}
	public void setSource_input_Type(String source_input_Type) {
		this.source_input_Type = source_input_Type;
	}
	public Long getCustomer_party_id() {
		return customer_party_id;
	}
	public void setCustomer_party_id(Long customer_party_id) {
		this.customer_party_id = customer_party_id;
	}
	public String getEntitled_company() {
		return entitled_company;
	}
	public void setEntitled_company(String entitled_company) {
		this.entitled_company = entitled_company;
	}
	String entitled_company ;
	
	
	//Line Objects
	Long request_id ;
	public Long getRequest_id() {
		return request_id;
	}
	public void setRequest_id(Long request_id) {
		this.request_id = request_id;
	}
	public Long getRequest_line_id() {
		return request_line_id;
	}
	public void setRequest_line_id(Long request_line_id) {
		this.request_line_id = request_line_id;
	}
	public String getHost_name() {
		return host_name;
	}
	public void setHost_name(String host_name) {
		this.host_name = host_name;
	}
	public String getIp_address() {
		return ip_address;
	}
	public void setIp_address(String ip_address) {
		this.ip_address = ip_address;
	}
	public String getSerial_number() {
		return serial_number;
	}
	public void setSerial_number(String serial_number) {
		this.serial_number = serial_number;
	}
	public String getCollected_serial_number() {
		return collected_serial_number;
	}
	public void setCollected_serial_number(String collected_serial_number) {
		this.collected_serial_number = collected_serial_number;
	}
	public String getCollected_equipment_type() {
		return collected_equipment_type;
	}
	public void setCollected_equipment_type(String collected_equipment_type) {
		this.collected_equipment_type = collected_equipment_type;
	}
	public String getCollected_product_id() {
		return collected_product_id;
	}
	public void setCollected_product_id(String collected_product_id) {
		this.collected_product_id = collected_product_id;
	}
	public String getPce_product_id() {
		return pce_product_id;
	}
	public void setPce_product_id(String pce_product_id) {
		this.pce_product_id = pce_product_id;
	}
	public String getSn_pid_validation_status() {
		return sn_pid_validation_status;
	}
	public void setSn_pid_validation_status(String sn_pid_validation_status) {
		this.sn_pid_validation_status = sn_pid_validation_status;
	}
	public String getProduct_id() {
		return product_id;
	}
	public void setProduct_id(String product_id) {
		this.product_id = product_id;
	}
	public String getProduct_family() {
		return product_family;
	}
	public void setProduct_family(String product_family) {
		this.product_family = product_family;
	}
	public String getProduct_name() {
		return product_name;
	}
	public void setProduct_name(String product_name) {
		this.product_name = product_name;
	}
	public String getEquipment_type() {
		return equipment_type;
	}
	public void setEquipment_type(String equipment_type) {
		this.equipment_type = equipment_type;
	}
	public String getItem_type() {
		return item_type;
	}
	public void setItem_type(String item_type) {
		this.item_type = item_type;
	}
	public String getParent_equipment() {
		return parent_equipment;
	}
	public void setParent_equipment(String parent_equipment) {
		this.parent_equipment = parent_equipment;
	}
	public String getChild_hardware() {
		return child_hardware;
	}
	public void setChild_hardware(String child_hardware) {
		this.child_hardware = child_hardware;
	}
	public String getProduct_type() {
		return product_type;
	}
	public void setProduct_type(String product_type) {
		this.product_type = product_type;
	}
	public String getList_price() {
		return list_price;
	}
	public void setList_price(String list_price) {
		this.list_price = list_price;
	}
	public String getLast_day_of_support() {
		return last_day_of_support;
	}
	public void setLast_day_of_support(String last_day_of_support) {
		this.last_day_of_support = last_day_of_support;
	}
	public String getRecognized_not_recognized() {
		return recognized_not_recognized;
	}
	public void setRecognized_not_recognized(String recognized_not_recognized) {
		this.recognized_not_recognized = recognized_not_recognized;
	}
	public String getDevice_diagnostics_supported() {
		return device_diagnostics_supported;
	}
	public void setDevice_diagnostics_supported(String device_diagnostics_supported) {
		this.device_diagnostics_supported = device_diagnostics_supported;
	}
	public String getCustomer_name() {
		return customer_name;
	}
	public void setCustomer_name(String customer_name) {
		this.customer_name = customer_name;
	}
	public String getLast_inventory_name() {
		return last_inventory_name;
	}
	public void setLast_inventory_name(String last_inventory_name) {
		this.last_inventory_name = last_inventory_name;
	}
	public String getBill_to_customer() {
		return bill_to_customer;
	}
	public void setBill_to_customer(String bill_to_customer) {
		this.bill_to_customer = bill_to_customer;
	}
	public Long getInstalled_at_site_id() {
		return installed_at_site_id;
	}
	public void setInstalled_at_site_id(Long installed_at_site_id) {
		this.installed_at_site_id = installed_at_site_id;
	}
	public String getInstalled_at_site_name() {
		return installed_at_site_name;
	}
	public void setInstalled_at_site_name(String installed_at_site_name) {
		this.installed_at_site_name = installed_at_site_name;
	}
	public String getInstalled_at_country() {
		return installed_at_country;
	}
	public void setInstalled_at_country(String installed_at_country) {
		this.installed_at_country = installed_at_country;
	}
	public String getInstalled_at_address() {
		return installed_at_address;
	}
	public void setInstalled_at_address(String installed_at_address) {
		this.installed_at_address = installed_at_address;
	}
	public String getInstalled_at_city() {
		return installed_at_city;
	}
	public void setInstalled_at_city(String installed_at_city) {
		this.installed_at_city = installed_at_city;
	}
	public String getInstalled_at_state() {
		return installed_at_state;
	}
	public void setInstalled_at_state(String installed_at_state) {
		this.installed_at_state = installed_at_state;
	}
	public String getInstalled_at_province() {
		return installed_at_province;
	}
	public void setInstalled_at_province(String installed_at_province) {
		this.installed_at_province = installed_at_province;
	}
	public String getInstalled_at_postal_code() {
		return installed_at_postal_code;
	}
	public void setInstalled_at_postal_code(String installed_at_postal_code) {
		this.installed_at_postal_code = installed_at_postal_code;
	}
	public String getSnmp_location() {
		return snmp_location;
	}
	public void setSnmp_location(String snmp_location) {
		this.snmp_location = snmp_location;
	}
	public String getContract_number() {
		return contract_number;
	}
	public void setContract_number(String contract_number) {
		this.contract_number = contract_number;
	}
	public String getContract_status() {
		return contract_status;
	}
	public void setContract_status(String contract_status) {
		this.contract_status = contract_status;
	}
	public String getCoverage_status() {
		return coverage_status;
	}
	public void setCoverage_status(String coverage_status) {
		this.coverage_status = coverage_status;
	}
	public String getCovered_begin_date() {
		return covered_begin_date;
	}
	public void setCovered_begin_date(String covered_begin_date) {
		this.covered_begin_date = covered_begin_date;
	}
	public String getContract_coverage_ends() {
		return contract_coverage_ends;
	}
	public void setContract_coverage_ends(String contract_coverage_ends) {
		this.contract_coverage_ends = contract_coverage_ends;
	}
	public String getService_level() {
		return service_level;
	}
	public void setService_level(String service_level) {
		this.service_level = service_level;
	}
	public Long getInstance_number() {
		return instance_number;
	}
	public void setInstance_number(Long instance_number) {
		this.instance_number = instance_number;
	}
	public Long getParent_instance_id() {
		return parent_instance_id;
	}
	public void setParent_instance_id(Long parent_instance_id) {
		this.parent_instance_id = parent_instance_id;
	}
	public Long getInstall_site_guid() {
		return install_site_guid;
	}
	public void setInstall_site_guid(Long install_site_guid) {
		this.install_site_guid = install_site_guid;
	}
	public String getGu_name() {
		return gu_name;
	}
	public void setGu_name(String gu_name) {
		this.gu_name = gu_name;
	}
	public String getAuthenticated_site() {
		return authenticated_site;
	}
	public void setAuthenticated_site(String authenticated_site) {
		this.authenticated_site = authenticated_site;
	}
	public String getDuplicated_sn() {
		return duplicated_sn;
	}
	public void setDuplicated_sn(String duplicated_sn) {
		this.duplicated_sn = duplicated_sn;
	}
	public String getRelationship() {
		return relationship;
	}
	public void setRelationship(String relationship) {
		this.relationship = relationship;
	}
	public String getSheet_name() {
		return sheet_name;
	}
	public void setSheet_name(String sheet_name) {
		this.sheet_name = sheet_name;
	}
	public String getRequest_status() {
		return request_status;
	}
	public void setRequest_status(String request_status) {
		this.request_status = request_status;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Long getCreated_by() {
		return created_by;
	}
	public void setCreated_by(Long created_by) {
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
	Long request_line_id;
	String host_name;
	String ip_address;
	String serial_number;
	String collected_serial_number;
	String collected_equipment_type;
	String collected_product_id;
	String pce_product_id;
	String sn_pid_validation_status ;
	
	String product_id   ;
	String product_family   ;
	String product_name  ;
	String equipment_type ;
	String item_type  ;
	String parent_equipment;
	String child_hardware  ;
	String product_type  ;
	String list_price    ;
	String last_day_of_support  ;
	String recognized_not_recognized   ;
	String device_diagnostics_supported  ;
	String customer_name   ;
	String last_inventory_name  ;
	String bill_to_customer ;
	Long installed_at_site_id  ;
	String installed_at_site_name   ;
	
	String installed_at_country    ;
	String installed_at_address   ;
	String installed_at_city    ;
	String installed_at_state    ;
	String installed_at_province  ;
	String installed_at_postal_code  ;
	String snmp_location  ;
	String contract_number  ;
	String contract_status  ;
	String coverage_status  ;
	String covered_begin_date  ;
	String contract_coverage_ends  ;
	String service_level  ;
	Long instance_number  ;
	Long parent_instance_id   ;
	Long install_site_guid ;
	String gu_name ;
	String authenticated_site  ;
	String duplicated_sn   ;
	String relationship   ;
	String sheet_name    ;
	String request_status   ;
	String status   ;
	
	Long created_by ;
	Date creation_date;
	Long last_updated_by;
	Date last_update_date;
			

	
}
