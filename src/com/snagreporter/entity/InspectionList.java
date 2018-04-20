package com.snagreporter.entity;

import java.io.Serializable;

public class InspectionList implements Serializable {
	String ChecklistDescription;
	String ID;
	String AreaID;
	String AreaType;
	String AreaName;
	String JobType;
	String CreatedBy;
	String CreatedDate;
	String ModifiedBy;
	String ModifiedDate;
	String EnteredOnMachineID;
	boolean IsSelected;
	
	public void setIsSelected(boolean name){
		IsSelected=name;
	}
	public boolean getIsSelected(){
		return IsSelected;
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
	public void setAreaName(String name){
		AreaName=name;
	}
	public String getAreaName(){
		return AreaName;
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
	
	public void setAreaID(String id){
		AreaID=id;
	}
	public String getAreaID(){
		return AreaID;
	}
	
	public void setAreaType(String id){
		AreaType=id;
	}
	public String getAreaType(){
		return AreaType;
	}
	
	public void setCreatedDate(String name){
		CreatedDate=name;
	}
	public String getCreatedDate(){
		return CreatedDate;
	}

}
