package com.snagreporter.entity;

import java.io.Serializable;

public class InspectionMaster implements Serializable {
	String ProjectID;
	String ID;
	String BuildingID;
	String AreaType;
	String FloorID;
	String ApartmentID;
	String AptAreaID;
	String JobType;
	String CreatedBy;
	String CreatedDate;
	String ModifiedBy;
	String ModifiedDate;
	String EnteredOnMachineID;
	double PercentageComplete;
	
	public void setPercentageComplete(double name){
		PercentageComplete=name;
	}
	public double getPercentageComplete(){
		return PercentageComplete;
	}
	
	public void setAptAreaID(String name){
		AptAreaID=name;
	}
	public String getAptAreaID(){
		return AptAreaID;
	}
	
	public void setApartmentID(String name){
		ApartmentID=name;
	}
	public String getApartmentID(){
		return ApartmentID;
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
	public void setProjectID(String name){
		ProjectID=name;
	}
	public String getProjectID(){
		return ProjectID;
	}
	
	public void setJobType(String name){
		JobType=name;
	}
	public String getJobType(){
		return JobType;
	}
	
	public void setBuildingID(String name){
		BuildingID=name;
	}
	public String getBuildingID(){
		return BuildingID;
	}

	
	public void setID(String id){
		ID=id;
	}
	public String getID(){
		return ID;
	}
	
	public void setFloorID(String id){
		FloorID=id;
	}
	public String getFloorID(){
		return FloorID;
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
