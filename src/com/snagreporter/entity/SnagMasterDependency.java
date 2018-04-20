package com.snagreporter.entity;

import java.io.Serializable;

public class SnagMasterDependency implements Serializable {
	String DId;
	String ParentSnagId;
	String SnagId;
	String JobType;
	String ProjectId;
	String FloorId;
	String ApartmentID;
	String BuildingId;
	boolean IsDataSyncToWeb;
	 
	public void setDId(String name){
		DId=name;
	}
	public String getDId(){
		return DId;
	}
	public void setParentSnagId(String name){
		ParentSnagId=name;
	}
	public String getParentSnagId(){
		return ParentSnagId;
	}
	
	public void setSnagId(String name){
		SnagId=name;
	}
	public String getSnagId(){
		return SnagId;
	}

	
	public void setJobType(String id){
		JobType=id;
	}
	public String getJobType(){
		return JobType;
	}
	
	public void setProjectId(String id){
		ProjectId=id;
	}
	public String getProjectId(){
		return ProjectId;
	}
	
	public void setFloorId(String id){
		FloorId=id;
	}
	public String getFloorId(){
		return FloorId;
	}
	
	public void setBuildingId(String id){
		BuildingId=id;
	}
	public String getBuildingId(){
		return BuildingId;
	}
	public void setApartmentID(String id){
		ApartmentID=id;
	}
	public String getApartmentID(){
		return ApartmentID;
	}
	
	public void setIsDataSyncToWeb(boolean id){
		IsDataSyncToWeb=id;
	}
	public boolean getIsDataSyncToWeb(){
		return IsDataSyncToWeb;
	}


}
