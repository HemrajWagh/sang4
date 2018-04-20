package com.snagreporter.entity;

import java.io.Serializable;

public class TimeStampMaster implements Serializable {
	String TableName;
	String Caption;
	String TimeStampValue;
	int  SequenceNo;
	int FromRowNo;
	public void setFromRowNo(int name){
		FromRowNo=name;
	}
	public int getFromRowNo(){
		return FromRowNo;
	}
	public void setSequenceNo(int name){
		SequenceNo=name;
	}
	public int getSequenceNo(){
		return SequenceNo;
	}
	public void setTableName(String name){
		TableName=name;
	}
	public String getTableName(){
		return TableName;
	}
	
	public void setTimeStampValue(String name){
		TimeStampValue=name;
	}
	public String getTimeStampValue(){
		return TimeStampValue;
	}

	
	public void setCaption(String id){
		Caption=id;
	}
	public String getCaption(){
		return Caption;
	}
	


}
