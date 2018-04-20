package com.snagreporter.entity;

import java.io.Serializable;

public class InspectionDetails implements Serializable {
	String InspectionDetailID;
	String InspectionMasterID;
	String ChecklistID;
	String ChecklistDescription;
	String ChecklistEntry;
	
	String CreatedBy;
	String CreatedDate;
	String ModifiedBy;
	String ModifiedDate;
	String EnteredOnMachineID;
	boolean IsSyncedToWeb;
	String StatusForUpload;
	
	public void setStatusForUpload(String name){
		StatusForUpload=name;
	}
	public String getStatusForUpload(){
		return StatusForUpload;
	}
	
	public void setIsSyncedToWeb(boolean name){
		IsSyncedToWeb=name;
	}
	public boolean getIsSyncedToWeb(){
		return IsSyncedToWeb;
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
	public void setCreatedDate(String name){
		CreatedDate=name;
	}
	public String getCreatedDate(){
		return CreatedDate;
	}
	public void setInspectionDetailID(String name){
		InspectionDetailID=name;
	}
	public String getInspectionDetailID(){
		return InspectionDetailID;
	}
	
	
	
	public void setChecklistDescription(String name){
		ChecklistDescription=name;
	}
	public String getChecklistDescription(){
		return ChecklistDescription;
	}

	
	public void setInspectionMasterID(String id){
		InspectionMasterID=id;
	}
	public String getInspectionMasterID(){
		return InspectionMasterID;
	}
	
	public void setChecklistID(String id){
		ChecklistID=id;
	}
	public String getChecklistID(){
		return ChecklistID;
	}
	
	public void setChecklistEntry(String id){
		ChecklistEntry=id;
	}
	public String getChecklistEntry(){
		return ChecklistEntry;
	}
	


}
