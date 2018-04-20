package com.snagreporter.entity;

import java.io.Serializable;

public class ApartmentMaster implements Serializable {
	String ApartmentNo;
	String ID;
	//int NoOfRooms;
	String FloorID;
	String Floor;
	
	String ProjectID;
	String ProjectName;
	String BuildingID;
	String BuildingName;
	String AptPlanID;
	String AptPlanName;
	String AptType;
String ImageName;
public int SnagCount;
public boolean isInspStarted;

Boolean isDataSyncToWeb;
String StatusForUpload;
public void setStatusForUpload(String id){
	StatusForUpload=id;
}
public String getStatusForUpload(){
	return StatusForUpload;
}
public void setisDataSyncToWeb(boolean id){
	isDataSyncToWeb=id;
}
public boolean getisDataSyncToWeb(){
	return isDataSyncToWeb;
}
	
	public void setImageName(String id){
		ImageName=id;
	}
	public String getImageName(){
		return ImageName;
	}
	
	public void setBuildingID(String name){
		BuildingID=name;
	}
	public String getBuildingID(){
		return BuildingID;
	}
	
	public void setBuildingName(String name){
		BuildingName=name;
	}
	public String getBuildingName(){
		return BuildingName;
	}
	
	public void setAptPlanID(String name){
		AptPlanID=name;
	}
	public String getAptPlanID(){
		return AptPlanID;
	}
	
	public void setAptPlanName(String name){
		AptPlanName=name;
	}
	public String getAptPlanName(){
		return AptPlanName;
	}
	
	public void setAptType(String name){
		AptType=name;
	}
	public String getAptType(){
		return AptType;
	}
	
	public void setApartmentNo(String name){
		ApartmentNo=name;
	}
	public String getApartmentNo(){
		return ApartmentNo;
	}
	
	public void setID(String id){
		ID=id;
	}
	public String getID(){
		return ID;
	}
	
	public void setProjectID(String name){
		ProjectID=name;
	}
	public String getProjectID(){
		return ProjectID;
	}
	public void setProjectName(String name){
		ProjectName=name;
	}
	public String getProjectName(){
		return ProjectName;
	}
	
	public void setFloorID(String id){
		FloorID=id;
	}
	public String getFloorID(){
		return FloorID;
	}
	
	public void setFloor(String id){
		Floor=id;
	}
	public String getFloor(){
		return Floor;
	}
}
