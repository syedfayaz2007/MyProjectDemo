package com.scm.kafka.main;

import java.sql.Date;


public class contract_obj {
	//Line Objects
	String  contract_number ;
	Long contract_id;	
	
	public String getLINEContractNumber() {return contract_number;	}
	public void setLINErequest_id(String contract_number) {this.contract_number = contract_number;}
	
	public Long getLINEContractId() {return contract_id;	}
	public void setLINEContractId(Long contract_id) {this.contract_id = contract_id;}
	
}
