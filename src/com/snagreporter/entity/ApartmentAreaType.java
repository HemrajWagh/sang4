package com.snagreporter.entity;

import java.io.Serializable;

public class ApartmentAreaType implements Serializable {
	String ApartmentAreaType;
	String ID;
	String AreaType;
	String CreatedBy;
	String CreatedDate;
	String ModifiedBy;
	String ModifiedDate;
	String EnteredOnMachineID;
	
	public void setApartmentAreaType(String name){
		ApartmentAreaType=name;
	}
	public String getApartmentAreaType(){
		return ApartmentAreaType;
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
	
	
	
	
	

	
	public void setID(String id){
		ID=id;
	}
	public String getID(){
		return ID;
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
