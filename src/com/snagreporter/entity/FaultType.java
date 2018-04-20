package com.snagreporter.entity;

import java.io.Serializable;

public class FaultType implements Serializable {
	String JobTypeID;
	String ID;
	String JobType;
	String FaultType;
	String FaultDetails;
	boolean IsSyncedToWeb;
	public void setIsSyncedToWeb(boolean name){
		IsSyncedToWeb=name;
	}
	public boolean getIsSyncedToWeb(){
		return IsSyncedToWeb;
	}
	public void setJobType(String name){
		JobType=name;
	}
	public String getJobType(){
		return JobType;
	}
	
	public void setJobTypeID(String name){
		JobTypeID=name;
	}
	public String getJobTypeID(){
		return JobTypeID;
	}

	
	public void setID(String id){
		ID=id;
	}
	public String getID(){
		return ID;
	}
	
	public void setFaultType(String id){
		FaultType=id;
	}
	public String getFaultType(){
		return FaultType;
	}
	
	public void setFaultDetails(String id){
		FaultDetails=id;
	}
	public String getFaultDetails(){
		return FaultDetails;
	}
	


}
