package com.snagreporter.entity;

import java.io.Serializable;

public class ApartmentDetails implements Serializable {
	String ApartmentID;
	String ID;
	String FloorID;
	String Floor;
	String ProjectID;
	String ProjectName;
	String BuildingID;
	String BuildingName;
	String Apartment;
	String AptAreaName;
	String AptAreaType;
	String SubSerial;
	String ImageName;
	
	public void setImageName(String id){
		ImageName=id;
	}
	public String getImageName(){
		return ImageName;
	}
	public void setSubSerial(String id){
		SubSerial=id;
	}
	public String getSubSerial(){
		return SubSerial;
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
	
	public void setApartment(String name){
		Apartment=name;
	}
	public String getApartment(){
		return Apartment;
	}
	
	public void setAptAreaName(String name){
		AptAreaName=name;
	}
	public String getAptAreaName(){
		return AptAreaName;
	}
	
	public void setAptAreaType(String name){
		AptAreaType=name;
	}
	public String getAptAreaType(){
		return AptAreaType;
	}
	
	public void setApartmentID(String name){
		ApartmentID=name;
	}
	public String getApartmentID(){
		return ApartmentID;
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
