package com.scm.kafka.daoservices;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface dbConnection {
	public HashMap<Integer, Integer> executeQuery(String query);
	//public void insertErrData(int appl_req_id , String Json ,String errTable,Integer offset,String AMPDupErrDataTableColsList) ;
	//public void copyTableData(String srctable, String trgTable, String Errtable);
	//public void deleteTableData(String tabelName);
	public void updateCommitedOffset(Map<Integer, Integer> hm, String ccwCommitTable) throws SQLException ;
	public void updateNewOffset(Map<Integer, Integer> hm, String ccwCommitTable, String topicName, String group) throws SQLException;
	public void updateBeginningOffset(long l, int partition_Number, String ccwCommitTable) throws SQLException;
	public void insertCommitedOffset(int part_num, long intOffset, String ccwCommitTable,String topicName, String groupId) throws SQLException ;
		
}
