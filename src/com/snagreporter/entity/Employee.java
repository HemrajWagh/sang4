package com.snagreporter.entity;

import java.io.Serializable;

public class Employee implements Serializable {
	String EmpCode;
	String Salutation;
	String EmpName;
	String EmpLastName;
	String Gender;
	String Designation;
	String Department;
	
	
	
	public void setEmpCode(String name){
		EmpCode=name;
	}
	public String getEmpCode(){
		return EmpCode;
	}
	
	public void setSalutation(String name){
		Salutation=name;
	}
	public String getSalutation(){
		return Salutation;
	}
	
	public void setEmpName(String name){
		EmpName=name;
	}
	public String getEmpName(){
		return EmpName;
	}
	
	public void setEmpLastName(String name){
		EmpLastName=name;
	}
	public String getEmpLastName(){
		return EmpLastName;
	}
	
	public void setGender(String name){
		Gender=name;
	}
	public String getGender(){
		return Gender;
	}
	
	public void setDesignation(String name){
		Designation=name;
	}
	public String getDesignation(){
		return Designation;
	}
	
	public void setDepartment(String name){
		Department=name;
	}
	public String getDepartment(){
		return Department;
	}
	
	
	
}
