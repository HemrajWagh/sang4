package com.snagreporter.entity;

import java.io.Serializable;

public class LoginData implements Serializable {
	String ID;
	String UserName;
	String Password;
	String CreatedBy;
	String CreatedDate;
	String ModifiedBy;
	String ModifiedDate;
	String EmailID;
	String Designation;
	String FirstName;
	String LastName;
	
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
	
	public void setID(String name){
		ID=name;
	}
	public String getID(){
		return ID;
	}
	
	public void setEmailID(String name){
		EmailID=name;
	}
	public String getEmailID(){
		return EmailID;
	}
	
	public void setUserName(String name){
		UserName=name;
	}
	public String getUserName(){
		return UserName;
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
	
	public void setModifiedBy(String name){
		ModifiedBy=name;
	}
	public String getModifiedBy(){
		return ModifiedBy;
	}
	
	public void setDesignation(String name){
		Designation=name;
	}
	public String getDesignation(){
		return Designation;
	}
	
	public void setModifiedDate(String name){
		ModifiedDate=name;
	}
	public String getModifiedDate(){
		return ModifiedDate;
	}
	
	
	
}
