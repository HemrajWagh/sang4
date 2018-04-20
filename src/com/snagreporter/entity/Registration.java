package com.snagreporter.entity;

import java.io.Serializable;

public class Registration implements Serializable {
	String FirstName;
	String ID;
	String LastName;
	String Email;
	String MobileNo;
	String Address;
	String Type;
	String DefaultProjectID;
	String DefaultProjectname;
	String CurrentLocationInProjectID;
	String CurrentLocationInProjectName;
	boolean IsLoggedIn;
	String BuilderID;
	String BuilderName;
	
String ImageName;
	
	public void setImageName(String id){
		ImageName=id;
	}
	public String getImageName(){
		return ImageName;
	}
	
	public void setBuilderID(String id){
		BuilderID=id;
	}
	public String getBuilderID(){
		return BuilderID;
	}
	
	public void setBuilderName(String id){
		BuilderName=id;
	}
	public String getBuilderName(){
		return BuilderName;
	}
	
	

	
	public void setID(String id){
		ID=id;
	}
	public String getID(){
		return ID;
	}
	
	public void setFirstName(String name){
		FirstName=name;
	}
	public String getFirstName(){
		return FirstName;
	}
	
	public void setLastName(String name){
		LastName=name;
	}
	public String getLastName(){
		return LastName;
	}
	
	public void setEmail(String name){
		Email=name;
	}
	public String getEmail(){
		return Email;
	}
	
	public void setMobileNo(String name){
		MobileNo=name;
	}
	public String getMobileNo(){
		return MobileNo;
	}
	
	public void setAddress(String name){
		Address=name;
	}
	public String getAddress(){
		return Address;
	}
	
	public void setType(String name){
		Type=name;
	}
	public String getType(){
		return Type;
	}
	
	public void setDefaultProjectID(String name){
		DefaultProjectID=name;
	}
	public String getDefaultProjectID(){
		return DefaultProjectID;
	}
	
	public void setDefaultProjectname(String name){
		DefaultProjectname=name;
	}
	public String getDefaultProjectname(){
		return DefaultProjectname;
	}
	
	public void setCurrentLocationInProjectID(String name){
		CurrentLocationInProjectID=name;
	}
	public String getCurrentLocationInProjectID(){
		return CurrentLocationInProjectID;
	}
	
	public void setCurrentLocationInProjectName(String name){
		CurrentLocationInProjectName=name;
	}
	public String getCurrentLocationInProjectName(){
		return CurrentLocationInProjectName;
	}
	
	public void setIsLoggedIn(boolean name){
		IsLoggedIn=name;
	}
	public boolean getIsLoggedIn(){
		return IsLoggedIn;
	}

}
