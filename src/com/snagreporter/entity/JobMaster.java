package com.snagreporter.entity;

import java.io.Serializable;

public class JobMaster implements Serializable {
	String ID;
	String Job;
	String JobDetails;
	String ProjectID;
	String ProjectName;
	String ContractorID;
	String ContractorName;
	String SubContractorID;
	String SubContractorName;
	String CreatedBy;
	
	String CreatedDate;
	String ModifiedBy;
	String ModifiedDate;
	String SubSubContractorID;
	String SubSubContractorName;
	
	public void setSubSubContractorName(String name){
		SubSubContractorName=name;
	}
	public String getSubSubContractorName(){
		return SubSubContractorName;
	}
	
	public void setSubSubContractorID(String name){
		SubSubContractorID=name;
	}
	public String getSubSubContractorID(){
		return SubSubContractorID;
	}
	
	public void setModifiedDate(String name){
		ModifiedDate=name;
	}
	public String getModifiedDate(){
		return ModifiedDate;
	}
	
	public void setModifiedBy(String name){
		ModifiedBy=name;
	}
	public String getModifiedBy(){
		return ModifiedBy;
	}
	
	public void setCreatedDate(String name){
		CreatedDate=name;
	}
	public String getCreatedDate(){
		return CreatedDate;
	}
	
	public void setCreatedBy(String name){
		CreatedBy=name;
	}
	public String getCreatedBy(){
		return CreatedBy;
	}
	
	public void setSubContractorName(String name){
		SubContractorName=name;
	}
	public String getSubContractorName(){
		return SubContractorName;
	}
	public void setSubContractorID(String name){
		SubContractorID=name;
	}
	public String getSubContractorID(){
		return SubContractorID;
	}
	
	public void setContractorName(String name){
		ContractorName=name;
	}
	public String getContractorName(){
		return ContractorName;
	}
	
	public void setContractorID(String name){
		ContractorID=name;
	}
	public String getContractorID(){
		return ContractorID;
	}
	public void setProjectName(String name){
		ProjectName=name;
	}
	public String getProjectName(){
		return ProjectName;
	}
	
	public void setProjectID(String name){
		ProjectID=name;
	}
	public String getProjectID(){
		return ProjectID;
	}
	public void setJob(String name){
		Job=name;
	}
	public String getJob(){
		return Job;
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
