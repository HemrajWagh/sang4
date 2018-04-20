package com.snagreporter.entity;

import java.io.Serializable;

public class BuildingMaster implements Serializable {
	String BuildingName;
	String ID;
	int NoOfFloors;
	String ProjectID;
	String ProjectName;
	int NoOfElevators;
	String BuildingType;
	public int SnagCount;
	public boolean isInspStarted;
	
String ImageName;
	

Boolean isDataSyncToWeb;

public void setBuildingType(String id){
	BuildingType=id;
}
public String getBuildingType(){
	return BuildingType;
}

public void setisDataSyncToWeb(boolean id){
	isDataSyncToWeb=id;
}
public boolean getisDataSyncToWeb(){
	return isDataSyncToWeb;
}
String StatusForUpload;
public void setStatusForUpload(String id){
	StatusForUpload=id;
}
public String getStatusForUpload(){
	return StatusForUpload;
}

	public void setImageName(String id){
		ImageName=id;
	}
	public String getImageName(){
		return ImageName;
	}
	public void setBuildingName(String name){
		BuildingName=name;
	}
	public String getBuildingName(){
		return BuildingName;
	}
	
	public void setID(String id){
		ID=id;
	}
	public String getID(){
		return ID;
	}
	
	public void setNoOfFloors(int name){
		NoOfFloors=name;
	}
	public int getNoOfFloors(){
		return NoOfFloors;
	}
	public void setNoOfElevators(int name){
		NoOfElevators=name;
	}
	public int getNoOfElevators(){
		return NoOfElevators;
	}
	
	public void setProjectID(String id){
		ProjectID=id;
	}
	public String getProjectID(){
		return ProjectID;
	}
	
	public void setProjectName(String id){
		ProjectName=id;
	}
	public String getProjectName(){
		return ProjectName;
	}
}
