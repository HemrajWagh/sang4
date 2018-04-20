package com.snagreporter.entity;

import java.io.Serializable;

public class StdApartmentAreas implements Serializable {
	String AreaName;
	String ID;
	
	
	public void setAreaName(String name){
		AreaName=name;
	}
	public String getAreaName(){
		return AreaName;
	}
	

	
	public void setID(String id){
		ID=id;
	}
	public String getID(){
		return ID;
	}
	


}
