package com.snagreporter.entity;

import java.io.Serializable;

public class QCC implements Serializable {
	String QCCStatus;
	String ID;
	String Description;
	
	
	public void setDescription(String name){
		Description=name;
	}
	public String getDescription(){
		return Description;
	}
	
	public void setQCCStatus(String name){
		QCCStatus=name;
	}
	public String getQCCStatus(){
		return QCCStatus;
	}

	
	public void setID(String id){
		ID=id;
	}
	public String getID(){
		return ID;
	}
	


}
