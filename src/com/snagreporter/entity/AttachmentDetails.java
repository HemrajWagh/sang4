package com.snagreporter.entity;

import java.io.Serializable;

public class AttachmentDetails implements Serializable {
	String AttachmentID;
	String ProjectID;
	String BuildingID;
	String FloorID;
	String ApartmentID;
	String AptAreaID;
	String SnagID;
	String AreaType;
	String JobType;
	String FileName;
	String LocalFilePath;
	String CreatedBy;
	String CreatedDate;
	String ModifiedBy;
	String ModifiedDate;
	//String EnteredOnMachineID;
	//String UploadNow;
	boolean IsUploadedToWeb;
	//String ForAreaType;
	boolean IsSyncToWeb;
	
	public void setLocalFilePath(String name){
		LocalFilePath=name;
	}
	public String getLocalFilePath(){
		return LocalFilePath;
	}
	public void setIsSyncToWeb(boolean name){
		IsSyncToWeb=name;
	}
	public boolean getIsSyncToWeb(){
		return IsSyncToWeb;
	}
	public void setIsUploadedToWeb(boolean name){
		IsUploadedToWeb=name;
	}
	public boolean getIsUploadedToWeb(){
		return IsUploadedToWeb;
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
	public void setFileName(String name){
		FileName=name;
	}
	public String getFileName(){
		return FileName;
	}
	public void setJobType(String name){
		JobType=name;
	}
	public String getJobType(){
		return JobType;
	}
	public void setAreaType(String name){
		AreaType=name;
	}
	public String getAreaType(){
		return AreaType;
	}
	public void setSnagID(String name){
		SnagID=name;
	}
	public String getSnagID(){
		return SnagID;
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
	public void setFloorID(String name){
		FloorID=name;
	}
	public String getFloorID(){
		return FloorID;
	}
	public void setBuildingID(String name){
		BuildingID=name;
	}
	public String getBuildingID(){
		return BuildingID;
	}
	public void setProjectID(String name){
		ProjectID=name;
	}
	public String getProjectID(){
		return ProjectID;
	}
	public void setAttachmentID(String name){
		AttachmentID=name;
	}
	public String getAttachmentID(){
		return AttachmentID;
	}
	
	
	
	


}
