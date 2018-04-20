package com.snagreporter.entity;

import java.io.Serializable;

public class ProjectMaster implements Serializable {
String ID;
String ProjectName;
int NoOfBuildings;
String Location;
String Address1;

String Address2;
String Pincode;
String City;
String State;


String BuilderID;
String BuilderName;
String ImageName;
String About;

Boolean isDataSyncToWeb;
public int SnagCount;
public boolean isInspStarted;
public void setisDataSyncToWeb(boolean id){
	isDataSyncToWeb=id;
}
public boolean getisDataSyncToWeb(){
	return isDataSyncToWeb;
}


String StatusForUpload;
public void setStatusForUpload(String id){
	StatusForUpload=id;
}
public String getStatusForUpload(){
	return StatusForUpload;
}
public void setAbout(String id){
	About=id;
}
public String getAbout(){
	return About;
}

public void setImageName(String id){
	ImageName=id;
}
public String getImageName(){
	return ImageName;
}

public void setBuilderID(String id)
{
	BuilderID=id;
}
public String getBuilderID(){
	return BuilderID;
}

public void setBuilderName(String id)
{
	BuilderName=id;
}
public String getBuilderName(){
	return BuilderName;
}

public void setID(String id)
{
	ID=id;
}
public String getID(){
	return ID;
}

public void setProjectName(String Name)
{
	ProjectName=Name;
}
public String getProjectName(){
	return ProjectName;
}

public void setNoOfBuildings(int no)
{
	NoOfBuildings=no;
}
public int getNoOfBuildings(){
	return NoOfBuildings;
}

public void setLocation(String Name)
{
	Location=Name;
}
public String getLocation(){
	return Location;
}

public void setAddress1(String Name)
{
	Address1=Name;
}
public String getAddress1(){
	return Address1;
}

public void setAddress2(String Name)
{
	Address2=Name;
}
public String getAddress2(){
	return Address2;
}

public void setPincode(String Name)
{
	Pincode=Name;
}
public String getPincode(){
	return Pincode;
}

public void setCity(String Name)
{
	City=Name;
}
public String getCity(){
	return City;
}

public void setState(String Name)
{
	State=Name;
}
public String getState(){
	return State;
}
}
