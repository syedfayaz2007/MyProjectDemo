package com.scm.kafka.beans;


import java.util.List;

import com.scm.kafka.main.contract_obj;
import com.scm.kafka.main.header;
import com.scm.kafka.main.lines;


public class AIBCounsumerRespBean {
	
	
	

			public List<header> header;
			public List<lines> lines;
			
					
					//getter and setter for header
					public void setHeader(List<header> header) {this.header = header;}			
					public List<header> getHeader() {return header;	}
					
					//getter and setter for lines
					public void setLines(List<lines> lines) {this.lines = lines;}			
					public List<lines> getLines() {return lines;	}
					
				
		/*	//header object
			@JsonProperty("header")
			
			//lines object
			@JsonProperty("lines")
		    
		  //override object
		  	@JsonProperty("override")*/


				
			
	

	
}
