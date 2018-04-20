package com.snagreporter.entity;

import java.io.Serializable;

public class FloorMaster implements Serializable {
	String Floor;
	String ID;
	int NoOfApartments;
	String BuildingID;
	String BuildingName;
	String ProjectID;
	String ProjectName;
	public int SnagCount;
	public boolean isInspStarted;
	public String FloorType;
String ImageName;
String FloorPlanImage;
	

Boolean isDataSyncToWeb;


public void setFloorPlanImage(String id){
	FloorPlanImage=id;
}
public String getFloorPlanImage(){
	return FloorPlanImage;
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
	
	public void setFloor(String name){
		Floor=name;
	}
	public String getFloor(){
		return Floor;
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
	
	public void setNoOfApartments(int name){
		NoOfApartments=name;
	}
	public int getNoOfApartments(){
		return NoOfApartments;
	}

	
	public void setBuildingID(String id){
		BuildingID=id;
	}
	public String getBuildingID(){
		return BuildingID;
	}
	

}
