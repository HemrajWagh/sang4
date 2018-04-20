package com.snagreporter.entity;

import java.io.Serializable;

public class JobType implements Serializable {
	String JobType;
	String ID;
	String JobDetails;
	boolean IsSyncedToWeb;
	String ParentID;
	String ParentJob;
	String SeqNo;
	public void setParentID(String name){
		ParentID=name;
	}
	public String getParentID(){
		return ParentID;
	}
	public void setParentJob(String name){
		ParentJob=name;
	}
	public String getParentJob(){
		return ParentJob;
	}
	public void setSeqNo(String name){
		SeqNo=name;
	}
	public String getSeqNo(){
		return SeqNo;
	}
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
	
	public void setJobDetails(String name){
		JobDetails=name;
	}
	public String getJobDetails(){
		return JobDetails;
	}

	
	public void setID(String id){
		ID=id;
	}
	public String getID(){
		return ID;
	}
	


}
