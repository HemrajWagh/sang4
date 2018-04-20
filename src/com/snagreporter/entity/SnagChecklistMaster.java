package com.snagreporter.entity;

import java.io.Serializable;

public class SnagChecklistMaster implements Serializable {
	String JobType;
	String ID;
	String FaultType;
	String ChecklistDescription;
	String CreatedBy;
	String CreatedDate;
	String ModifiedBy;
	String ModifiedDate;
	String EnteredOnMachineID;
	double PercentageComplete;
	boolean isSelected;
	public void setPercentageComplete(double name){
		PercentageComplete=name;
	}
	public double getPercentageComplete(){
		return PercentageComplete;
	}
	
	public void setIsSelected(boolean name){
		isSelected=name;
	}
	public boolean getIsSelected(){
		return isSelected;
	}
	

	
	public void setEnteredOnMachineID(String name){
		EnteredOnMachineID=name;
	}
	public String getEnteredOnMachineID(){
		return EnteredOnMachineID;
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
	public void setCreatedBy(String name){
		CreatedBy=name;
	}
	public String getCreatedBy(){
		return CreatedBy;
	}
	public void setFaultType(String name){
		FaultType=name;
	}
	public String getFaultType(){
		return FaultType;
	}
	
	public void setJobType(String name){
		JobType=name;
	}
	public String getJobType(){
		return JobType;
	}
	
	public void setChecklistDescription(String name){
		ChecklistDescription=name;
	}
	public String getChecklistDescription(){
		return ChecklistDescription;
	}

	
	public void setID(String id){
		ID=id;
	}
	public String getID(){
		return ID;
	}
	

	public void setCreatedDate(String name){
		CreatedDate=name;
	}
	public String getCreatedDate(){
		return CreatedDate;
	}
	


}
