package com.snagreporter.entity;

import java.io.Serializable;

public class Inspector implements Serializable {
	String InspectorName;
	String ID;
	
	
	public void setInspectorName(String name){
		InspectorName=name;
	}
	public String getInspectorName(){
		return InspectorName;
	}
	
	public void setID(String id){
		ID=id;
	}
	public String getID(){
		return ID;
	}
	
	
}
