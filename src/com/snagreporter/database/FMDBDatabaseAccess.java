package com.snagreporter.database;



import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.apache.http.client.CircularRedirectException;
import org.apache.http.util.ByteArrayBuffer;
import org.json.JSONObject;

import android.R.integer;
import android.app.Activity;
import android.bluetooth.BluetoothClass.Device;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.snagreporter.entity.*;


public class FMDBDatabaseAccess  extends Activity {
	SQLiteDatabase mydb;
	Context context;
	String strLoginType;
	String RegUserID;
	private static String DBNAME = "SnagReporter.db";

	
	public FMDBDatabaseAccess(Context con){
		context=con;
		DBNAME = "SnagReporter.db";
		 SharedPreferences SP = context.getSharedPreferences("AppDelegate",MODE_PRIVATE);
	        RegUserID=SP.getString("RegUserID", "");
	        strLoginType=SP.getString("LoginType", "");
	}
	public BuildingMaster[] getBuildings(String ProjectID)
	{
		BuildingMaster arr[]=null;
		try{
			mydb=context.openOrCreateDatabase(DBNAME, Context.MODE_PRIVATE, null);
			String strQuery;
	        strQuery = "Select * from BuildingMaster where ProjectID='"+ProjectID+"' Order by ID";
	        Cursor cursor=mydb.rawQuery(strQuery, null);
	        arr=new BuildingMaster[cursor.getCount()];
	        int i=0;
	        if(cursor.moveToFirst()){
	        	do{
	        		BuildingMaster obj=new BuildingMaster();
	        		obj.setID(cursor.getString(cursor.getColumnIndex("ID")));
	        		obj.setBuildingName(cursor.getString(cursor.getColumnIndex("BuildingName")));
	        		obj.setNoOfElevators(cursor.getInt(cursor.getColumnIndex("NoOfElevators")));
	        		obj.setNoOfFloors(cursor.getInt(cursor.getColumnIndex("NoOfFloors")));
	        		obj.setProjectID(cursor.getString(cursor.getColumnIndex("ProjectID")));
	        		obj.setProjectName(cursor.getString(cursor.getColumnIndex("ProjectName")));
	        		obj.setImageName(cursor.getString(cursor.getColumnIndex("ImageName")));
	        		obj.setBuildingType(cursor.getString(cursor.getColumnIndex("BuildingType")));
	        		
	        		arr[i]=obj;
	        		
	        		i++;
	        	}
	        	while(cursor.moveToNext());
	        }
	        cursor.close();
	        mydb.close();
		}
		catch(Exception e){
			Log.d("Error=", ""+e.getMessage());
		}
		
		
		return arr;
	}
	
	public ProjectMaster[] getProjects()
	{
		ProjectMaster arr[]=null;
		try{
			mydb=context.openOrCreateDatabase(DBNAME, Context.MODE_PRIVATE, null);
			String strQuery;
	        strQuery = "Select * from ProjectMaster Order by ID";
	        Cursor cursor=mydb.rawQuery(strQuery, null);
	        arr=new ProjectMaster[cursor.getCount()];
	        int i=0;
	        if(cursor.moveToFirst()){
	        	do{
	        		ProjectMaster obj=new ProjectMaster();
	        		obj.setID(cursor.getString(cursor.getColumnIndex("ID")));
	        		obj.setProjectName(cursor.getString(cursor.getColumnIndex("ProjectName")));
	        		obj.setNoOfBuildings(cursor.getInt(cursor.getColumnIndex("NoOfBuildings")));
	        		obj.setLocation(cursor.getString(cursor.getColumnIndex("Location")));
	        		obj.setAddress1(cursor.getString(cursor.getColumnIndex("Address1")));
	        		obj.setAddress2(cursor.getString(cursor.getColumnIndex("Address2")));
	        		obj.setPincode(cursor.getString(cursor.getColumnIndex("Pincode")));
	        		obj.setCity(cursor.getString(cursor.getColumnIndex("City")));
	        		obj.setState(cursor.getString(cursor.getColumnIndex("State")));
	        		obj.setImageName(cursor.getString(cursor.getColumnIndex("ImageName")));
	        		obj.setAbout(cursor.getString(cursor.getColumnIndex("About")));
	        		obj.setBuilderID(cursor.getString(cursor.getColumnIndex("BuilderID")));
					obj.setBuilderName(cursor.getString(cursor.getColumnIndex("BuilderName")));
	        		arr[i]=obj;
	        		
	        		i++;
	        	}
	        	while(cursor.moveToNext());
	        }
	        cursor.close();
	        mydb.close();
		}
		catch(Exception e){
			
		}
		
		
		return arr;
	}
	
	public boolean insertORUpdateJobType(JobType obj)
	{
		
		try{
			mydb=context.openOrCreateDatabase(DBNAME, Context.MODE_PRIVATE, null);
			String strQuery;
			strQuery="Select * from JobType where ID='"+obj.getID()+"';";
			Cursor cursor=mydb.rawQuery(strQuery, null);
			if(cursor.moveToFirst()){
				strQuery="UPDATE JobType SET JobType='"+obj.getJobType()+"',JobDetails='"+obj.getJobDetails()+"',IsSyncedToWeb='"+obj.getIsSyncedToWeb()+"'  where ID='"+obj.getID()+"';";
			}
			else{
				strQuery = "Insert into JobType(ID,JobType,JobDetails,IsSyncedToWeb) values('"+obj.getID()+"','"+obj.getJobType()+"','"+obj.getJobDetails()+"','"+obj.getIsSyncedToWeb()+"');";
			}
	        mydb.execSQL(strQuery);
	        cursor.close();
	        mydb.close();
	        
		}
		catch(Exception e){
			
		}
		
		
		return true;
	}
	
	public boolean insertORUpdateFaultType(FaultType obj)
	{
		
		try{
			mydb=context.openOrCreateDatabase(DBNAME, Context.MODE_PRIVATE, null);
			String strQuery;
			strQuery="Select * from FaultType where ID='"+obj.getID()+"' and JobTypeID='"+obj.getJobTypeID()+"';";
			Cursor cursor=mydb.rawQuery(strQuery, null);
			if(cursor.moveToFirst()){
				strQuery="UPDATE FaultType SET FaultType='"+obj.getFaultType()+"',FaultDetails='"+obj.getFaultDetails()+"',JobType='"+obj.getJobType()+"',IsSyncedToWeb='"+obj.getIsSyncedToWeb()+"'  where ID='"+obj.getID()+"' and JobTypeID='"+obj.getJobTypeID()+"';";
			}
			else{
				strQuery = "Insert into FaultType(ID,FaultType,FaultDetails,JobTypeID,JobType,IsSyncedToWeb) values('"+obj.getID()+"','"+obj.getFaultType()+"','"+obj.getFaultDetails()+"','"+obj.getJobTypeID()+"','"+obj.getJobType()+"','"+obj.getIsSyncedToWeb()+"');";
			}
	        mydb.execSQL(strQuery);
	        cursor.close();
	        mydb.close();
	        
		}
		catch(Exception e){
			
		}
		
		
		return true;
	}
	
	
	public boolean insertORUpdateProject(ProjectMaster obj)
	{
		
		try{
			mydb=context.openOrCreateDatabase(DBNAME, Context.MODE_PRIVATE, null);
			String strQuery;
			strQuery="Select * from ProjectMaster where ID='"+obj.getID()+"';";
			Cursor cursor=mydb.rawQuery(strQuery, null);
			if(cursor.moveToFirst()){
				strQuery="UPDATE ProjectMaster SET ProjectName='"+obj.getProjectName()+"',NoOfBuildings='"+obj.getNoOfBuildings()+"',Location='"+obj.getLocation()+"',Address1='"+obj.getAddress1()+"',Address2='"+obj.getAddress2()+"',Pincode='"+obj.getPincode()+"',City='"+obj.getCity()+"',State='"+obj.getState()+"',BuilderID='"+obj.getBuilderID()+"',BuilderName='"+obj.getBuilderName()+"',ImageName='"+obj.getImageName()+"',About='"+obj.getAbout()+"',IsSyncedToWeb='"+obj.getisDataSyncToWeb()+"',StatusForUpload='"+obj.getStatusForUpload()+"'  where ID='"+obj.getID()+"';";
			}
			else{
				strQuery = "Insert into ProjectMaster(ID,ProjectName,NoOfBuildings,Location,Address1,Address2,Pincode,City,State,BuilderID,BuilderName,ImageName,About,IsSyncedToWeb,StatusForUpload) values('"+obj.getID()+"','"+obj.getProjectName()+"','"+obj.getNoOfBuildings()+"','"+obj.getLocation()+"','"+obj.getAddress1()+"','"+obj.getAddress2()+"','"+obj.getPincode()+"','"+obj.getCity()+"','"+obj.getState()+"','"+obj.getBuilderID()+"','"+obj.getBuilderName()+"','"+obj.getImageName()+"','"+obj.getAbout()+"','"+obj.getisDataSyncToWeb()+"','"+obj.getStatusForUpload()+"');";
			}
	        mydb.execSQL(strQuery);
	        cursor.close();
	        mydb.close();
	        
		}
		catch(Exception e){
			Log.d("Error insertORUpdateProject=", e.getMessage());
		}
		
		
		return true;
	}
	
	public boolean insertORUpdateBuilding(BuildingMaster obj)
	{
		
		try{
			mydb=context.openOrCreateDatabase(DBNAME, Context.MODE_PRIVATE, null);
			String strQuery;
			strQuery="Select * from BuildingMaster where ID='"+obj.getID()+"' and ProjectID='"+obj.getProjectID()+"'";
			Cursor cursor=mydb.rawQuery(strQuery, null);
			if(cursor.moveToFirst()){
				strQuery="UPDATE BuildingMaster SET BuildingName='"+obj.getBuildingName()+"',NoOfFloors='"+obj.getNoOfFloors()+"',ProjectName='"+obj.getProjectName()+"',NoOfElevators='"+obj.getNoOfElevators()+"',ImageName='"+obj.getImageName()+"',IsSyncedToWeb='"+obj.getisDataSyncToWeb()+"',StatusForUpload='"+obj.getStatusForUpload()+"',BuildingType='"+obj.getBuildingType()+"'  where ID='"+obj.getID()+"' and ProjectID='"+obj.getProjectID()+"';";
			}
			else{
	        strQuery = "Insert into BuildingMaster(ID,BuildingName,NoOfFloors,ProjectID,ProjectName,NoOfElevators,ImageName,IsSyncedToWeb,StatusForUpload,BuildingType) values('"+obj.getID()+"','"+obj.getBuildingName()+"','"+obj.getNoOfFloors()+"','"+obj.getProjectID()+"','"+obj.getProjectName()+"','"+obj.getNoOfElevators()+"','"+obj.getImageName()+"','"+obj.getisDataSyncToWeb()+"','"+obj.getStatusForUpload()+"','"+obj.getBuildingType()+"');";
			}
	        mydb.execSQL(strQuery);
	        cursor.close();
	        mydb.close();
	        
		}
		catch(Exception e){
			
		}
		
		
		return true;
	}
	
	public boolean insertORUpdateFloor(FloorMaster obj)
	{
		
		try{
			mydb=context.openOrCreateDatabase(DBNAME, Context.MODE_PRIVATE, null);
			String strQuery;
			strQuery="Select * from FloorMaster where ID='"+obj.getID()+"' and BuildingID='"+obj.getBuildingID()+"' and ProjectID='"+obj.getProjectID()+"' ";
			Cursor cursor=mydb.rawQuery(strQuery, null);
			if(cursor.moveToFirst()){
				strQuery="UPDATE FloorMaster SET Floor='"+obj.getFloor()+"',NoOfApartments='"+obj.getNoOfApartments()+"',BuildingName='"+obj.getBuildingName()+"',ProjectID='"+obj.getProjectID()+"',ProjectName='"+obj.getProjectName()+"',ImageName='"+obj.getImageName()+"',IsSyncedToWeb='"+obj.getisDataSyncToWeb()+"',StatusForUpload='"+obj.getStatusForUpload()+"' where ID='"+obj.getID()+"' and BuildingID='"+obj.getBuildingID()+"' and ProjectID='"+obj.getProjectID()+"';";
			}
			else{
				strQuery = "Insert into FloorMaster(ID,Floor,NoOfApartments,BuildingID,BuildingName,ProjectID,ProjectName,ImageName,IsSyncedToWeb,StatusForUpload) values('"+obj.getID()+"','"+obj.getFloor()+"','"+obj.getNoOfApartments()+"','"+obj.getBuildingID()+"','"+obj.getBuildingName()+"','"+obj.getProjectID()+"','"+obj.getProjectName()+"','"+obj.getImageName()+"','"+obj.getisDataSyncToWeb()+"','"+obj.getStatusForUpload()+"');";
		        
		        
			}
			mydb.execSQL(strQuery);
			cursor.close();
	        mydb.close();
	        
		}
		catch(Exception e){
			
		}
		
		
		return true;
	}
	
	public boolean insertORUpdateApartment(ApartmentMaster obj)
	{
		
		try{
			mydb=context.openOrCreateDatabase(DBNAME, Context.MODE_PRIVATE, null);
			String strQuery;
			strQuery="Select * from ApartmentMaster where ID='"+obj.getID()+"' and FloorID='"+obj.getFloorID()+"' and BuildingID='"+obj.getBuildingID()+"' and ProjectID='"+obj.getProjectID()+"'";
			Cursor cursor=mydb.rawQuery(strQuery, null);
			if(cursor.moveToFirst()){
				strQuery="UPDATE ApartmentMaster SET ApartmentNo='"+obj.getApartmentNo()+"',Floor='"+obj.getFloor()+"',ProjectID='"+obj.getProjectID()+"',ProjectName='"+obj.getProjectName()+"',BuildingID='"+obj.getBuildingID()+"',BuildingName='"+obj.getBuildingName()+"',AptPlanID='"+obj.getAptPlanID()+"',AptPlanName='"+obj.getAptPlanName()+"',AptType='"+obj.getAptType()+"',ImageName='"+obj.getImageName()+"',IsSyncedToWeb='"+obj.getisDataSyncToWeb()+"',StatusForUpload='"+obj.getStatusForUpload()+"' where ID='"+obj.getID()+"' and FloorID='"+obj.getFloorID()+"';";
			}
			else{
				strQuery = "Insert into ApartmentMaster(ID,ApartmentNo,FloorID,Floor,BuildingID,BuildingName,ProjectID,ProjectName,AptPlanID,AptPlanName,AptType,ImageName,IsSyncedToWeb,StatusForUpload) values('"+obj.getID()+"','"+obj.getApartmentNo()+"','"+obj.getFloorID()+"','"+obj.getFloor()+"','"+obj.getBuildingID()+"','"+obj.getBuildingName()+"','"+obj.getProjectID()+"','"+obj.getProjectName()+"','"+obj.getAptPlanID()+"','"+obj.getAptPlanName()+"','"+obj.getAptType()+"','"+obj.getImageName()+"','"+obj.getisDataSyncToWeb()+"','"+obj.getStatusForUpload()+"');";
			}
			
	        mydb.execSQL(strQuery);
	        cursor.close();
	        mydb.close();
	        
		}
		catch(Exception e){
			Log.d("Error insertORUpdateApartment=", ""+e.getMessage());
		}
		
		
		return true;
	}
	
	public boolean insertORUpdateApartmentDetails(ApartmentDetails obj)
	{
		
		try{
			mydb=context.openOrCreateDatabase(DBNAME, Context.MODE_PRIVATE, null);
			String strQuery;
			strQuery="Select * from ApartmentDetails where ID='"+obj.getID()+"' and ApartmentID='"+obj.getApartmentID()+"' and FloorID='"+obj.getFloorID()+"' and BuildingID='"+obj.getBuildingID()+"' and ProjectID='"+obj.getProjectID()+"'";
			Cursor cursor=mydb.rawQuery(strQuery, null);
			if(cursor.moveToFirst()){
				strQuery="UPDATE ApartmentDetails SET Apartment='"+obj.getApartment()+"',FloorID='"+obj.getFloorID()+"',Floor='"+obj.getFloor()+"',ProjectID='"+obj.getProjectID()+"',ProjectName='"+obj.getProjectName()+"',BuildingID='"+obj.getBuildingID()+"',BuildingName='"+obj.getBuildingName()+"',AptAreaType='"+obj.getAptAreaType()+"',AptAreaName='"+obj.getAptAreaName()+"',SubSerial='"+obj.getSubSerial()+"',ImageName='"+obj.getImageName()+"' where ID='"+obj.getID()+"' and ApartmentID='"+obj.getApartmentID()+"';";
			}
			else{
				strQuery = "Insert into ApartmentDetails(ID,ApartmentID,Apartment,FloorID,Floor,BuildingID,BuildingName,ProjectID,ProjectName,AptAreaType,AptAreaName,SubSerial,ImageName) values('"+obj.getID()+"','"+obj.getApartmentID()+"','"+obj.getApartment()+"','"+obj.getFloorID()+"','"+obj.getFloor()+"','"+obj.getBuildingID()+"','"+obj.getBuildingName()+"','"+obj.getProjectID()+"','"+obj.getProjectName()+"','"+obj.getAptAreaType()+"','"+obj.getAptAreaName()+"','"+obj.getSubSerial()+"','"+obj.getImageName()+"');";
			}
			
	        mydb.execSQL(strQuery);
	        cursor.close();
	        mydb.close();
	        
		}
		catch(Exception e){
			
		}
		
		
		return true;
	}
	public Boolean insertOrUpdateSnagMasterFromWeb(SnagMaster obj)
	{
		try
		{
			mydb=context.openOrCreateDatabase(DBNAME, MODE_PRIVATE, null);
			String strQury;
			strQury="Select * from Snagmaster where ID='"+obj.getID()+"' and ApartmentID='"+obj.getApartmentID()+"' and BuildingID='"+obj.getBuildingID()+"' and ProjectID='"+obj.getProjectID()+"' and FloorID='"+obj.getFloorID()+"'";
			Cursor cursor=mydb.rawQuery(strQury, null);
			if(cursor.moveToFirst())
			{
				strQury="Update into SnagMaster SET SnagStatus='"+obj.getSnagStatus()+"',AptAreaName='"+obj.getAptAreaName()+"',AptAreaID='"+obj.getAptAreaID()+"',IsSyncedToWeb='"+obj.getIsDataSyncToWeb()+"',StatusForUpload='"+obj.getStatusForUpload()+"' where ID='"+obj.getID()+"' and ProjectID='"+obj.getProjectID()+"' and BuildingID='"+obj.getBuildingID()+"' and FloorID='"+obj.getFloorID()+"' and ApartmentID='"+obj.getApartmentID()+"';";
			}
			else
			{
				strQury="Insert into SnagMaster(ID,SnagStatus,AptAreaName,ApartmentID,BuildingID,ProjectID,FloorID,AptAreaID,IsSyncedToWeb,StatusForUpload) values('"+obj.getID()+"','"+obj.getSnagStatus()+"','"+obj.getAptAreaName()+"','"+obj.getApartmentID()+"','"+obj.getBuildingID()+"','"+obj.getProjectID()+"','"+obj.getFloorID()+"','"+obj.getAptAreaID()+"','"+obj.getIsDataSyncToWeb()+"','"+obj.getStatusForUpload()+"')";
			}
			cursor.close();
			mydb.close();
		}
		catch (Exception e)
		{
			Log.d("insertSnagMasterFromWeb fmdb", ""+e.getMessage());
		}
		return true;
	}
	
	public boolean insertORUpdateSnagMaster(SnagMaster obj)
	{
		
		try{
			mydb=context.openOrCreateDatabase(DBNAME, Context.MODE_PRIVATE, null);
			String strQuery;
			strQuery="Select * from SnagMaster where ID='"+obj.getID()+"' and ApartmentID='"+obj.getApartmentID()+"' and BuildingID='"+obj.getBuildingID()+"' and ProjectID='"+obj.getProjectID()+"' and FloorID='"+obj.getFloorID()+"'";
			Cursor cursor=mydb.rawQuery(strQuery, null);
			if(cursor.moveToFirst()){
				strQuery="UPDATE SnagMaster SET SnagType='"+obj.getSnagType()+"',SnagDetails='"+obj.getSnagDetails()+"',PictureURL1='"+obj.getPictureURL1()+"',PictureURL2='"+obj.getPictureURL2()+"',PictureURL3='"+obj.getPictureURL3()+"',ProjectName='"+obj.getProjectName()+"',BuildingName='"+obj.getBuildingName()+"',Floor='"+obj.getFloor()+"',Apartment='"+obj.getApartment()+"',AptAreaName='"+obj.getAptAreaName()+"',AptAreaID='"+obj.getAptAreaID()+"',ReportDate='"+obj.getReportDate()+"',SnagStatus='"+obj.getSnagStatus()+"',ResolveDate='"+obj.getResolveDate()+"',InspectorID='"+obj.getInspectorID()+"',InspectorName='"+obj.getInspectorName()+"',ExpectedInspectionDate='"+obj.getExpectedInspectionDate()+"',FaultType='"+obj.getFaultType()+"',Cost='"+obj.getCost()+"',CostTo='"+obj.getCostTo()+"',PriorityLevel='"+obj.getSnagPriority()+"',IsSyncedToWeb='"+obj.getIsDataSyncToWeb()+"',StatusForUpload='"+obj.getStatusForUpload()+"' where ID='"+obj.getID()+"' and ProjectID='"+obj.getProjectID()+"' and BuildingID='"+obj.getBuildingID()+"' and FloorID='"+obj.getFloorID()+"' and ApartmentID='"+obj.getApartmentID()+"';";
			}
			else{
				strQuery = "Insert into SnagMaster(ID,SnagType,SnagDetails,PictureURL1,PictureURL2,PictureURL3,ProjectID,ProjectName,BuildingID,BuildingName,FloorID,Floor,ApartmentID,Apartment,AptAreaName,AptAreaID,ReportDate,SnagStatus,ResolveDate,InspectorID,InspectorName,ExpectedInspectionDate,FaultType,Cost,CostTo,PriorityLevel,IsSyncedToWeb,StatusForUpload) values('"+obj.getID()+"','"+obj.getSnagType()+"','"+obj.getSnagDetails()+"','"+obj.getPictureURL1()+"','"+obj.getPictureURL2()+"','"+obj.getPictureURL3()+"','"+obj.getProjectID()+"','"+obj.getProjectName()+"','"+obj.getBuildingID()+"','"+obj.getBuildingName()+"','"+obj.getFloorID()+"','"+obj.getFloor()+"','"+obj.getApartmentID()+"','"+obj.getApartment()+"','"+obj.getAptAreaName()+"','"+obj.getAptAreaID()+"','"+obj.getReportDate()+"','"+obj.getSnagStatus()+"','"+obj.getResolveDate()+"','"+obj.getInspectorID()+"','"+obj.getInspectorName()+"','"+obj.getExpectedInspectionDate()+"','"+obj.getFaultType()+"','"+obj.getCost()+"','"+obj.getCostTo()+"','"+obj.getSnagPriority()+"','"+obj.getIsDataSyncToWeb()+"','"+obj.getStatusForUpload()+"');";
			}
			
	        mydb.execSQL(strQuery);
	        cursor.close();
	        mydb.close();
	        
		}
		catch(Exception e){
			
		}
		
		
		return true;
	}
	
	
	public Registration getRegistrationDetailForID(String ReguserID){
		Registration obj=null;
		try{
			mydb=context.openOrCreateDatabase(DBNAME, Context.MODE_PRIVATE, null);
			String strQuery;
			
			//strQuery="Update Registration set Firstname='Tarun',LastName='Parihar' where ID='ISP-000006';";
			//mydb.execSQL(strQuery);
			
			
	        strQuery = "Select * from Registration where ID='"+ReguserID+"';";
	        Cursor cursor=mydb.rawQuery(strQuery, null);
	        
	        
	        if(cursor.moveToFirst()){
	        	do{
	        		obj=new Registration();
	        		obj.setID(cursor.getString(cursor.getColumnIndex("ID")));
	        		obj.setFirstName(cursor.getString(cursor.getColumnIndex("FirstName")));
	        		obj.setLastName(cursor.getString(cursor.getColumnIndex("LastName")));
	        		
	        	}
	        	while(cursor.moveToNext());
	        }
	        cursor.close();
	        mydb.close();
		}
		catch(Exception e){
			Log.d("Error getRegistrationDetailForID=", ""+e.getMessage());
		}
		return obj;
	}
	
	public FloorMaster[] getFloors(BuildingMaster Bldg){
		FloorMaster[] arr=null;
		try{
			mydb=context.openOrCreateDatabase(DBNAME, Context.MODE_PRIVATE, null);
			String strQuery;
	        strQuery = "Select * from FloorMaster where BuildingID='"+Bldg.getID()+"' and ProjectID='"+Bldg.getProjectID()+"' Order by ID";
	        Cursor cursor=mydb.rawQuery(strQuery, null);
	        arr=new FloorMaster[cursor.getCount()];
	        int i=0;
	        if(cursor.moveToFirst()){
	        	do{
	        		FloorMaster obj=new FloorMaster();
	        		obj.setID(cursor.getString(cursor.getColumnIndex("ID")));
	        		obj.setNoOfApartments(cursor.getInt(cursor.getColumnIndex("NoOfApartments")));
	        		obj.setFloor(cursor.getString(cursor.getColumnIndex("Floor")));
	        		obj.setBuildingID(cursor.getString(cursor.getColumnIndex("BuildingID")));
	        		obj.setBuildingName(cursor.getString(cursor.getColumnIndex("BuildingName")));
	        		obj.setProjectID(cursor.getString(cursor.getColumnIndex("ProjectID")));
	        		obj.setProjectName(cursor.getString(cursor.getColumnIndex("ProjectName")));
	        		obj.setImageName(cursor.getString(cursor.getColumnIndex("ImageName")));
	        		obj.setFloorPlanImage(cursor.getString(cursor.getColumnIndex("FloorPlanImage")));
	        		arr[i]=obj;
	        		
	        		i++;
	        	}
	        	while(cursor.moveToNext());
	        }
	        cursor.close();
	        mydb.close();
		}
		catch(Exception e){
			Log.d("Error=", ""+e.getMessage());
		}
		return arr;
	}
	
	public ApartmentMaster[] getApartments(FloorMaster floor){
		ApartmentMaster[] arr=null;
		try{
			mydb=context.openOrCreateDatabase(DBNAME, Context.MODE_PRIVATE, null);
			String strQuery;
	        strQuery = "Select * from ApartmentMaster where FloorID='"+floor.getID()+"' and BuildingID='"+floor.getBuildingID()+"' and ProjectID='"+floor.getProjectID()+"' Order by ID";
	        Cursor cursor=mydb.rawQuery(strQuery, null);
	        arr=new ApartmentMaster[cursor.getCount()];
	        int i=0;
	        if(cursor.moveToFirst()){
	        	do{
	        		ApartmentMaster obj=new ApartmentMaster();
	        		obj.setID(cursor.getString(cursor.getColumnIndex("ID")));
	        		obj.setApartmentNo(cursor.getString(cursor.getColumnIndex("ApartmentNo")));
	        		
	        		obj.setFloorID(cursor.getString(cursor.getColumnIndex("FloorID")));
	        		obj.setFloor(cursor.getString(cursor.getColumnIndex("Floor")));
	        		obj.setBuildingID(cursor.getString(cursor.getColumnIndex("BuildingID")));
	        		obj.setBuildingName(cursor.getString(cursor.getColumnIndex("BuildingName")));
	        		obj.setProjectID(cursor.getString(cursor.getColumnIndex("ProjectID")));
	        		obj.setProjectName(cursor.getString(cursor.getColumnIndex("ProjectName")));
	        		obj.setAptPlanID(cursor.getString(cursor.getColumnIndex("AptPlanID")));
	        		obj.setAptPlanName(cursor.getString(cursor.getColumnIndex("AptPlanName")));
	        		obj.setAptType(cursor.getString(cursor.getColumnIndex("AptType")));
	        		obj.setImageName(cursor.getString(cursor.getColumnIndex("ImageName")));
	        		arr[i]=obj;
	        		
	        		i++;
	        	}
	        	while(cursor.moveToNext());
	        }
	        cursor.close();
	        mydb.close();
		}
		catch(Exception e){
			Log.d(" getApartments=", ""+e.getMessage());
		}
		return arr;
	}
	
	public ApartmentDetails[] getApartmentDetails(ApartmentMaster ApartmentD){
		ApartmentDetails[] arr=null;
		try{
			mydb=context.openOrCreateDatabase(DBNAME, Context.MODE_PRIVATE, null);
			String strQuery;
	        strQuery = "Select * from ApartmentDetails where ApartmentID='"+ApartmentD.getID()+"' and FloorID='"+ApartmentD.getFloorID()+"' and BuildingID='"+ApartmentD.getBuildingID()+"' and ProjectID='"+ApartmentD.getProjectID()+"' order by ID;";
	        Cursor cursor=mydb.rawQuery(strQuery, null);
	        int c=cursor.getCount();
	        arr=new ApartmentDetails[cursor.getCount()];
	        int i=0;
	        if(cursor.moveToFirst()){
	        	do{
	        		ApartmentDetails obj=new ApartmentDetails();
	        		obj.setID(cursor.getString(cursor.getColumnIndex("ID")));
	        		obj.setApartment(cursor.getString(cursor.getColumnIndex("Apartment")));
	        		obj.setApartmentID(cursor.getString(cursor.getColumnIndex("ApartmentID")));
	        		
	        		obj.setFloorID(cursor.getString(cursor.getColumnIndex("FloorID")));
	        		obj.setFloor(cursor.getString(cursor.getColumnIndex("Floor")));
	        		obj.setBuildingID(cursor.getString(cursor.getColumnIndex("BuildingID")));
	        		obj.setBuildingName(cursor.getString(cursor.getColumnIndex("BuildingName")));
	        		obj.setProjectID(cursor.getString(cursor.getColumnIndex("ProjectID")));
	        		obj.setProjectName(cursor.getString(cursor.getColumnIndex("ProjectName")));
	        		obj.setAptAreaType(cursor.getString(cursor.getColumnIndex("AptAreaType")));
	        		obj.setAptAreaName(cursor.getString(cursor.getColumnIndex("AptAreaName")));
	        		obj.setSubSerial(cursor.getString(cursor.getColumnIndex("SubSerial")));
	        		obj.setImageName(cursor.getString(cursor.getColumnIndex("ImageName")));
	        		arr[i]=obj;
	        		
	        		i++;
	        	}
	        	while(cursor.moveToNext());
	        }
	        cursor.close();
	        mydb.close();
		}
		catch(Exception e){
			Log.d(" getApartments=", ""+e.getMessage());
		}
		return arr;
	}
	
	public ApartmentDetails getApartmentDetailsForArea(ApartmentMaster ApartmentD,String AptAreaName){
		ApartmentDetails arr=null;
		try{
			mydb=context.openOrCreateDatabase(DBNAME, Context.MODE_PRIVATE, null);
			String strQuery;
	        strQuery = "Select * from ApartmentDetails where AptAreaName='"+AptAreaName+"' and ApartmentID='"+ApartmentD.getID()+"' and FloorID='"+ApartmentD.getFloorID()+"' and BuildingID='"+ApartmentD.getBuildingID()+"' and ProjectID='"+ApartmentD.getProjectID()+"' order by ID;";
	        Cursor cursor=mydb.rawQuery(strQuery, null);
	        int c=cursor.getCount();
	       
	        int i=0;
	        if(cursor.moveToFirst()){
	        	do{
	        		arr=new ApartmentDetails();
	        		ApartmentDetails obj=new ApartmentDetails();
	        		obj.setID(cursor.getString(cursor.getColumnIndex("ID")));
	        		obj.setApartment(cursor.getString(cursor.getColumnIndex("Apartment")));
	        		obj.setApartmentID(cursor.getString(cursor.getColumnIndex("ApartmentID")));
	        		
	        		obj.setFloorID(cursor.getString(cursor.getColumnIndex("FloorID")));
	        		obj.setFloor(cursor.getString(cursor.getColumnIndex("Floor")));
	        		obj.setBuildingID(cursor.getString(cursor.getColumnIndex("BuildingID")));
	        		obj.setBuildingName(cursor.getString(cursor.getColumnIndex("BuildingName")));
	        		obj.setProjectID(cursor.getString(cursor.getColumnIndex("ProjectID")));
	        		obj.setProjectName(cursor.getString(cursor.getColumnIndex("ProjectName")));
	        		obj.setAptAreaType(cursor.getString(cursor.getColumnIndex("AptAreaType")));
	        		obj.setAptAreaName(cursor.getString(cursor.getColumnIndex("AptAreaName")));
	        		obj.setSubSerial(cursor.getString(cursor.getColumnIndex("SubSerial")));
	        		obj.setImageName(cursor.getString(cursor.getColumnIndex("ImageName")));
	        		arr=obj;
	        		
	        		i++;
	        	}
	        	while(cursor.moveToNext());
	        }
	        cursor.close();
	        mydb.close();
		}
		catch(Exception e){
			Log.d(" getApartments=", ""+e.getMessage());
		}
		return arr;
	}
	
	public StdFloorAreas[] getSFA(){
		StdFloorAreas arr[]=null;
		try{
			mydb=context.openOrCreateDatabase(DBNAME, Context.MODE_PRIVATE, null);
			String strQuery;
	        strQuery = "Select * from StdFloorAreas Order by ID";
	        Cursor cursor=mydb.rawQuery(strQuery, null);
	        arr=new StdFloorAreas[cursor.getCount()];
	        int i=0;
	        if(cursor.moveToFirst()){
	        	do{
	        		StdFloorAreas obj=new StdFloorAreas();
	        		obj.setID(cursor.getString(cursor.getColumnIndex("ID")));
	        		
	        		obj.setAreaName(cursor.getString(cursor.getColumnIndex("AreaName")));
	        		arr[i]=obj;
	        		
	        		i++;
	        	}
	        	while(cursor.moveToNext());
	        }
	        cursor.close();
	        mydb.close();
		}
		catch(Exception e){
			Log.d("Error getSFA=", ""+e.getMessage());
		}
		return arr;
	}
	public StdApartmentAreas[] getSAA(){
		StdApartmentAreas arr[]=null;
		try{
			mydb=context.openOrCreateDatabase(DBNAME, Context.MODE_PRIVATE, null);
			String strQuery;
	        strQuery = "Select * from StdApartmentAreas Order by ID";
	        Cursor cursor=mydb.rawQuery(strQuery, null);
	        arr=new StdApartmentAreas[cursor.getCount()];
	        int i=0;
	        if(cursor.moveToFirst()){
	        	do{
	        		StdApartmentAreas obj=new StdApartmentAreas();
	        		obj.setID(cursor.getString(cursor.getColumnIndex("ID")));
	        		
	        		obj.setAreaName(cursor.getString(cursor.getColumnIndex("AreaName")));
	        		arr[i]=obj;
	        		
	        		i++;
	        	}
	        	while(cursor.moveToNext());
	        }
	        cursor.close();
	        mydb.close();
		}
		catch(Exception e){
			Log.d("Error getSAA=", ""+e.getMessage());
		}
		return arr;
	}
	
	public JobType[] getJobType(){
		JobType arr[]=null;
		try{
			mydb=context.openOrCreateDatabase(DBNAME, Context.MODE_PRIVATE, null);
			String strQuery;
	        strQuery = "Select * from JobType Order by SeqNo";
	        Cursor cursor=mydb.rawQuery(strQuery, null);
	        arr=new JobType[cursor.getCount()];
	        int i=0;
	        if(cursor.moveToFirst()){
	        	do{
	        		JobType obj=new JobType();
	        		obj.setID(cursor.getString(cursor.getColumnIndex("ID")));
	        		obj.setJobType(cursor.getString(cursor.getColumnIndex("JobType")));
	        		obj.setJobDetails(cursor.getString(cursor.getColumnIndex("JobDetails")));
	        		obj.setParentID(cursor.getString(cursor.getColumnIndex("ParentID")));
	        		obj.setParentJob(cursor.getString(cursor.getColumnIndex("ParentJob")));
	        		obj.setSeqNo(cursor.getString(cursor.getColumnIndex("SeqNo")));
	        		String str=cursor.getString(cursor.getColumnIndex("IsSyncedToWeb"));
	        		if(str!=null && str.equalsIgnoreCase("true"))
	        			obj.setIsSyncedToWeb(true);
	        		else
	        			obj.setIsSyncedToWeb(false);
	        		arr[i]=obj;
	        		
	        		i++;
	        	}
	        	while(cursor.moveToNext());
	        }
	        cursor.close();
	        mydb.close();
		}
		catch(Exception e){
			Log.d("Error getJobType=", ""+e.getMessage());
		}
		return arr;
	}
	
	public FaultType[] getFaultType(String jobtype){
		FaultType arr[]=null;
		try{
			mydb=context.openOrCreateDatabase(DBNAME, Context.MODE_PRIVATE, null);
			String strQuery;
	        strQuery = "Select * from FaultType where JobTypeID='"+jobtype+"'";
	        Cursor cursor=mydb.rawQuery(strQuery, null);
	        arr=new FaultType[cursor.getCount()];
	        int i=0;
	        if(cursor.moveToFirst()){
	        	do{
	        		FaultType obj=new FaultType();
	        		obj.setID(cursor.getString(cursor.getColumnIndex("ID")));
	        		obj.setJobType(cursor.getString(cursor.getColumnIndex("JobType")));
	        		obj.setJobTypeID(cursor.getString(cursor.getColumnIndex("JobTypeID")));
	        		obj.setFaultType(cursor.getString(cursor.getColumnIndex("FaultType")));
	        		obj.setFaultDetails(cursor.getString(cursor.getColumnIndex("FaultDetails")));
	        		String str=cursor.getString(cursor.getColumnIndex("IsSyncedToWeb"));
	        		if(str!=null && str.equalsIgnoreCase("true"))
	        		obj.setIsSyncedToWeb(true);
	        		else
	        			obj.setIsSyncedToWeb(false);
	        		arr[i]=obj;
	        		
	        		i++;
	        	}
	        	while(cursor.moveToNext());
	        }
	        cursor.close();
	        mydb.close();
		}
		catch(Exception e){
			Log.d("Error getFaultType=", ""+e.getMessage());
		}
		return arr;
	}
	
	public FaultType[] getAllFaultType(){
		FaultType arr[]=null;
		try{
			mydb=context.openOrCreateDatabase(DBNAME, Context.MODE_PRIVATE, null);
			String strQuery;
	        strQuery = "Select * from FaultType";
	        Cursor cursor=mydb.rawQuery(strQuery, null);
	        arr=new FaultType[cursor.getCount()];
	        int i=0;
	        if(cursor.moveToFirst()){
	        	do{
	        		FaultType obj=new FaultType();
	        		obj.setID(cursor.getString(cursor.getColumnIndex("ID")));
	        		obj.setJobType(cursor.getString(cursor.getColumnIndex("JobType")));
	        		obj.setJobTypeID(cursor.getString(cursor.getColumnIndex("JobTypeID")));
	        		obj.setFaultType(cursor.getString(cursor.getColumnIndex("FaultType")));
	        		obj.setFaultDetails(cursor.getString(cursor.getColumnIndex("FaultDetails")));
	        		String str=cursor.getString(cursor.getColumnIndex("IsSyncedToWeb"));
	        		if(str!=null && str.equalsIgnoreCase("true"))
	        			obj.setIsSyncedToWeb(true);
	        		else
	        			obj.setIsSyncedToWeb(false);
	        		arr[i]=obj;
	        		
	        		i++;
	        	}
	        	while(cursor.moveToNext());
	        }
	        cursor.close();
	        mydb.close();
		}
		catch(Exception e){
			Log.d("Error getAllFaultType=", ""+e.getMessage());
		}
		return arr;
	}
	
	public Inspector[] getInspector(){
		Inspector arr[]=null;
		try{
			mydb=context.openOrCreateDatabase(DBNAME, Context.MODE_PRIVATE, null);
			String strQuery;
	        strQuery = "Select * from Inspector Order by ID";
	        Cursor cursor=mydb.rawQuery(strQuery, null);
	        arr=new Inspector[cursor.getCount()];
	        int i=0;
	        if(cursor.moveToFirst()){
	        	do{
	        		Inspector obj=new Inspector();
	        		obj.setID(cursor.getString(cursor.getColumnIndex("ID")));
	        		obj.setInspectorName(cursor.getString(cursor.getColumnIndex("InspectorName")));
	        		arr[i]=obj;
	        		
	        		i++;
	        	}
	        	while(cursor.moveToNext());
	        }
	        cursor.close();
	        mydb.close();
		}
		catch(Exception e){
			Log.d("Error getInspector=", ""+e.getMessage());
		}
		return arr;
	}
	
	
//	public boolean InsertIntoSnagMaster(SnagMaster obj){
//		boolean success=false;
//		try{
//			mydb=context.openOrCreateDatabase(DBNAME, Context.MODE_PRIVATE, null);
//			String strQuery;
//	        strQuery = "Insert into SnagMaster(ID,SnagType,SnagDetails,PictureURL1,PictureURL2,PictureURL3,ProjectID,ProjectName,BuildingID,BuildingName,FloorID,Floor,ApartmentID,Apartment,AptAreaName,AptAreaID,ReportDate,SnagStatus,ResolveDate,InspectorID,InspectorName,ExpectedInspectionDate,FaultType) values('"+obj.getID()+"','"+obj.getSnagType()+"','"+obj.getSnagDetails()+"','"+obj.getPictureURL1()+"','"+obj.getPictureURL2()+"','"+obj.getPictureURL3()+"','"+obj.getProjectID()+"','"+obj.getProjectName()+"','"+obj.getBuildingID()+"','"+obj.getBuildingName()+"','"+obj.getFloorID()+"','"+obj.getFloor()+"','"+obj.getApartmentID()+"','"+obj.getApartment()+"','"+obj.getAptAreaName()+"','"+obj.getAptAreaID()+"','"+obj.getReportDate()+"','"+obj.getSnagStatus()+"','"+obj.getResolveDate()+"','"+obj.getInspectorID()+"','"+obj.getInspectorName()+"','"+obj.getExpectedInspectionDate()+"','"+obj.getFaultType()+"');";
//	        mydb.execSQL(strQuery);
//	        mydb.close();
//		}
//		catch(Exception e){
//			Log.d("Error InsertIntoSnagMaster=", ""+e.getMessage());
//		}
//		return true;
//	}
	
//	public boolean InsertIntoSnagMaster(SnagMaster obj){
//		boolean success=false;
//		try{
//			mydb=context.openOrCreateDatabase(DBNAME, Context.MODE_PRIVATE, null);
//			String strQuery;
//	        strQuery = "Insert into SnagMaster(ID,SnagType,SnagDetails,PictureURL1,PictureURL2,PictureURL3,ProjectID,ProjectName,BuildingID,BuildingName,FloorID,Floor,ApartmentID,Apartment,AptAreaName,AptAreaID,ReportDate,SnagStatus,ResolveDate,InspectorID,InspectorName,ExpectedInspectionDate,FaultType,Cost,CostTo,PriorityLevel,IsDataSyncToWeb,StatusForUpload,AllocatedTo,AllocatedToName) values('"+obj.getID()+"','"+obj.getSnagType()+"','"+obj.getSnagDetails()+"','"+obj.getPictureURL1()+"','"+obj.getPictureURL2()+"','"+obj.getPictureURL3()+"','"+obj.getProjectID()+"','"+obj.getProjectName()+"','"+obj.getBuildingID()+"','"+obj.getBuildingName()+"','"+obj.getFloorID()+"','"+obj.getFloor()+"','"+obj.getApartmentID()+"','"+obj.getApartment()+"','"+obj.getAptAreaName()+"','"+obj.getAptAreaID()+"','"+obj.getReportDate()+"','"+obj.getSnagStatus()+"','"+obj.getResolveDate()+"','"+obj.getInspectorID()+"','"+obj.getInspectorName()+"','"+obj.getExpectedInspectionDate()+"','"+obj.getFaultType()+"',"+obj.getCost()+",'"+obj.getCostTo()+"','"+obj.getSnagPriority()+"','"+obj.getIsDataSyncToWeb()+"','"+obj.getStatusForUpload()+"','"+obj.getAllocatedTo()+"','"+obj.getAllocatedToName()+"');";
//	        mydb.execSQL(strQuery);
//	        mydb.close();
//		}
//		catch(Exception e){
//			Log.d("Error InsertIntoSnagMaster=", ""+e.getMessage());
//		}
//		return true;
//	}
	
	public boolean InsertIntoSnagMaster(SnagMaster obj){
		boolean success=false;
		try{
			mydb=context.openOrCreateDatabase(DBNAME, Context.MODE_PRIVATE, null);
			String strQuery;
	        strQuery = "Insert into SnagMaster(ID,SnagType,SnagDetails,PictureURL1,PictureURL2,PictureURL3,ProjectID,ProjectName,BuildingID,BuildingName,FloorID,Floor,ApartmentID,Apartment,AptAreaName,AptAreaID,ReportDate,SnagStatus,ResolveDate,InspectorID,InspectorName,ExpectedInspectionDate,FaultType,Cost,CostTo,PriorityLevel,IsSyncedToWeb,StatusForUpload,AllocatedTo,AllocatedToName,XValue,YValue,ContractorID,ContractorName,SubContractorID,SubContractorName,SubSubContractorID,SubSubContractorName,ContractorStatus) values('"+obj.getID()+"','"+obj.getSnagType()+"','"+obj.getSnagDetails()+"','"+obj.getPictureURL1()+"','"+obj.getPictureURL2()+"','"+obj.getPictureURL3()+"','"+obj.getProjectID()+"','"+obj.getProjectName()+"','"+obj.getBuildingID()+"','"+obj.getBuildingName()+"','"+obj.getFloorID()+"','"+obj.getFloor()+"','"+obj.getApartmentID()+"','"+obj.getApartment()+"','"+obj.getAptAreaName()+"','"+obj.getAptAreaID()+"','"+obj.getReportDate()+"','"+obj.getSnagStatus()+"','"+obj.getResolveDate()+"','"+obj.getInspectorID()+"','"+obj.getInspectorName()+"','"+obj.getExpectedInspectionDate()+"','"+obj.getFaultType()+"',"+obj.getCost()+",'"+obj.getCostTo()+"','"+obj.getSnagPriority()+"','"+obj.getIsDataSyncToWeb()+"','"+obj.getStatusForUpload()+"','"+obj.getAllocatedTo()+"','"+obj.getAllocatedToName()+"',"+obj.getXValue()+","+obj.getYValue()+",'"+obj.getContractorID()+"','"+obj.getContractorName()+"','"+obj.getSubContractorID()+"','"+obj.getSubContractorName()+"','"+obj.getSubSubContractorID()+"','"+obj.getSubSubContractorName()+"','"+obj.getContractorStatus()+"');";
	        mydb.execSQL(strQuery);
	        mydb.close();
		}
		catch(Exception e){
			Log.d("Error InsertIntoSnagMaster=", ""+e.getMessage());
		}
		return true;
	}
	
	public boolean insertORUpdateRegistration(Registration obj){
		try{
			mydb=context.openOrCreateDatabase(DBNAME, Context.MODE_PRIVATE, null);
			String strQuery;
			strQuery="Select * from Registration where ID='"+obj.getID()+"';";
			Cursor cursor=mydb.rawQuery(strQuery, null);
			if(cursor.moveToFirst()){
				strQuery="UPDATE Registration SET FirstName='"+obj.getFirstName()+"',LastName='"+obj.getLastName()+"',Email='"+obj.getEmail()+"',MobileNo='"+obj.getMobileNo()+"',Address='"+obj.getAddress()+"',Type='"+obj.getType()+"',IsLoggedIn='"+obj.getIsLoggedIn()+"',ImageName='"+obj.getImageName()+"',BuilderID='"+obj.getBuilderID()+"',BuilderName='"+obj.getBuilderName()+"' where ID='"+obj.getID()+"';";
			}
			else{
	        strQuery = "Insert into Registration(ID,FirstName,LastName,Email,MobileNo,Address,Type,DefaultProjectID,DefaultProjectname,CurrentLocationInProjectID,CurrentLocationInProjectName,IsLoggedIn,ImageName,BuilderID,BuilderName) values('"+obj.getID()+"','"+obj.getFirstName()+"','"+obj.getLastName()+"','"+obj.getEmail()+"','"+obj.getMobileNo()+"','"+obj.getAddress()+"','"+obj.getType()+"','"+obj.getDefaultProjectID()+"','"+obj.getDefaultProjectname()+"','"+obj.getCurrentLocationInProjectID()+"','"+obj.getCurrentLocationInProjectName()+"','"+obj.getIsLoggedIn()+"','"+obj.getImageName()+"','"+obj.getBuilderID()+"','"+obj.getBuilderName()+"');";
			}
	        mydb.execSQL(strQuery);
	        cursor.close();
	        mydb.close();
		}
		catch(Exception e){
			Log.d("Error insertRegistration=", ""+e.getMessage());
		}
		return true;
		
	}
	public Registration getRegistration(){
		Registration obj=null;
		try{
			mydb=context.openOrCreateDatabase(DBNAME, Context.MODE_PRIVATE, null);
			
			//String str="update jobmaster set projectid='PRJ-000002',projectname='THE WORLD TOWERS' where contractorid='CTR-000001'";
//			String str="update registration set id='CTR-000010' where id='CTR-000001'";
//			String str="update snagmaster set contractorid='CTR-000010', contractorname='PATEL ENGINEERING LIMITED',subcontractorid='SCR-000011',subcontractorname='VIVEK ENTERPRISES' where snagtype='RCC'";
//			mydb.execSQL(str);
			
			
			String strQuery="Select * from Registration where IsLoggedIn='true' or IsLoggedIn='1'";
			Cursor cursor=mydb.rawQuery(strQuery, null);
			if(cursor.moveToFirst()){
				obj=new Registration();
				obj.setID(cursor.getString(cursor.getColumnIndex("ID")));
				obj.setFirstName(cursor.getString(cursor.getColumnIndex("FirstName")));
				obj.setLastName(cursor.getString(cursor.getColumnIndex("LastName")));
				obj.setEmail(cursor.getString(cursor.getColumnIndex("Email")));
				obj.setMobileNo(cursor.getString(cursor.getColumnIndex("MobileNo")));
				obj.setAddress(cursor.getString(cursor.getColumnIndex("Address")));
				obj.setType(cursor.getString(cursor.getColumnIndex("Type")));
				obj.setDefaultProjectID(cursor.getString(cursor.getColumnIndex("DefaultProjectID")));
				obj.setDefaultProjectname(cursor.getString(cursor.getColumnIndex("DefaultProjectname")));
				obj.setCurrentLocationInProjectID(cursor.getString(cursor.getColumnIndex("CurrentLocationInProjectID")));
				obj.setCurrentLocationInProjectName(cursor.getString(cursor.getColumnIndex("CurrentLocationInProjectName")));
				obj.setImageName(cursor.getString(cursor.getColumnIndex("ImageName")));
				obj.setBuilderID(cursor.getString(cursor.getColumnIndex("BuilderID")));
				obj.setBuilderName(cursor.getString(cursor.getColumnIndex("BuilderName")));
				String login=cursor.getString(cursor.getColumnIndex("IsLoggedIn"));
//				if(login.equalsIgnoreCase("true")){
//					obj.setIsLoggedIn(true);
//				}
//				else{
//					obj.setIsLoggedIn(false);
//				}
				
				
			}
			cursor.close();
	        mydb.close();
		}
		catch(Exception e){
			Log.d("Error getRegistration=", ""+e.getMessage());
		}
		return obj;
	}
	public ProjectMaster getDefaultProject(String ID){
		ProjectMaster obj=null;
		try{
			mydb=context.openOrCreateDatabase(DBNAME, Context.MODE_PRIVATE, null);
			String strQuery="Select * from ProjectMaster where ID='"+ID+"';";
			Cursor cursor=mydb.rawQuery(strQuery, null);
			if(cursor.moveToFirst()){
				obj=new ProjectMaster();
				obj.setID(cursor.getString(cursor.getColumnIndex("ID")));
				obj.setProjectName(cursor.getString(cursor.getColumnIndex("ProjectName")));
				obj.setNoOfBuildings(cursor.getInt(cursor.getColumnIndex("NoOfBuildings")));
				obj.setLocation(cursor.getString(cursor.getColumnIndex("Location")));
				obj.setAddress1(cursor.getString(cursor.getColumnIndex("Address1")));
				obj.setAddress2(cursor.getString(cursor.getColumnIndex("Address2")));
				obj.setPincode(cursor.getString(cursor.getColumnIndex("Pincode")));
				obj.setCity(cursor.getString(cursor.getColumnIndex("City")));
				obj.setState(cursor.getString(cursor.getColumnIndex("State")));
				obj.setImageName(cursor.getString(cursor.getColumnIndex("ImageName")));
				obj.setAbout(cursor.getString(cursor.getColumnIndex("About")));
				obj.setBuilderID(cursor.getString(cursor.getColumnIndex("BuilderID")));
				obj.setBuilderName(cursor.getString(cursor.getColumnIndex("BuilderName")));
				
				
			}
			cursor.close();
	        mydb.close();
		}
		catch(Exception e){
			Log.d("Error getDefaultProject=", ""+e.getMessage());
		}
		return obj;
	}
	
	public BuildingMaster getBuildingWithID(String prjID,String ID){
		BuildingMaster obj=null;
		try{
			mydb=context.openOrCreateDatabase(DBNAME, Context.MODE_PRIVATE, null);
			String strQuery="Select * from BuildingMaster where ID='"+ID+"' and ProjectID='"+prjID+"';";
			Cursor cursor=mydb.rawQuery(strQuery, null);
			if(cursor.moveToFirst()){
				obj=new BuildingMaster();
				obj.setID(cursor.getString(cursor.getColumnIndex("ID")));
        		obj.setBuildingName(cursor.getString(cursor.getColumnIndex("BuildingName")));
        		obj.setNoOfElevators(cursor.getInt(cursor.getColumnIndex("NoOfElevators")));
        		obj.setNoOfFloors(cursor.getInt(cursor.getColumnIndex("NoOfFloors")));
        		obj.setProjectID(cursor.getString(cursor.getColumnIndex("ProjectID")));
        		obj.setProjectName(cursor.getString(cursor.getColumnIndex("ProjectName")));
        		obj.setImageName(cursor.getString(cursor.getColumnIndex("ImageName")));
				
				
				
			}
			cursor.close();
	        mydb.close();
		}
		catch(Exception e){
			Log.d("Error getBuildingWithID=", ""+e.getMessage());
		}
		return obj;
	}
	
	public FloorMaster getFloorWithID(String prjID,String bldgID,String ID){
		FloorMaster obj=null;
		try{
			mydb=context.openOrCreateDatabase(DBNAME, Context.MODE_PRIVATE, null);
			String strQuery="Select * from FloorMaster where ID='"+ID+"' and BuildingID='"+bldgID+"' and ProjectID='"+prjID+"';";
			Cursor cursor=mydb.rawQuery(strQuery, null);
			if(cursor.moveToFirst()){
				obj=new FloorMaster();
				obj.setID(cursor.getString(cursor.getColumnIndex("ID")));
        		obj.setNoOfApartments(cursor.getInt(cursor.getColumnIndex("NoOfApartments")));
        		obj.setFloor(cursor.getString(cursor.getColumnIndex("Floor")));
        		obj.setBuildingID(cursor.getString(cursor.getColumnIndex("BuildingID")));
        		obj.setBuildingName(cursor.getString(cursor.getColumnIndex("BuildingName")));
        		
        		obj.setProjectID(cursor.getString(cursor.getColumnIndex("ProjectID")));
        		obj.setProjectName(cursor.getString(cursor.getColumnIndex("ProjectName")));
        		obj.setImageName(cursor.getString(cursor.getColumnIndex("ImageName")));
        		obj.setFloorPlanImage(cursor.getString(cursor.getColumnIndex("FloorPlanImage")));
				
				
			}
			cursor.close();
	        mydb.close();
		}
		catch(Exception e){
			Log.d("Error getFloorWithID=", ""+e.getMessage());
		}
		return obj;
	}
	
	public boolean setCurrentPositionInProject(String LocationID,String LocationName,String RegUserID){ 
		try{
			//SharedPreferences SP = getSharedPreferences("AppDelegate",MODE_PRIVATE);
			mydb=context.openOrCreateDatabase(DBNAME, Context.MODE_PRIVATE, null);
			String strQuery;
	        strQuery = "UPDATE Registration SET CurrentLocationInProjectID='"+LocationID+"',CurrentLocationInProjectName='"+LocationName+"' where ID='"+RegUserID+"';";
	        mydb.execSQL(strQuery);
	        mydb.close();
		}
		catch(Exception e){
			Log.d("Error setCurrentPositionInProject=", ""+e.getMessage());
		}
		return true;
	}
	
	public boolean setDefaultProject(String DefaultProject,String DefaultProjectname,String RegUserID){ 
		try{
			//SharedPreferences SP = getSharedPreferences("AppDelegate",MODE_PRIVATE);
			mydb=context.openOrCreateDatabase(DBNAME, Context.MODE_PRIVATE, null);
			String strQuery;
	        strQuery = "UPDATE Registration SET DefaultProjectID='"+DefaultProject+"',DefaultProjectname='"+DefaultProjectname+"' where ID='"+RegUserID+"';";
	        mydb.execSQL(strQuery);
	        mydb.close();
		}
		catch(Exception e){
			Log.d("Error setDefaultProject=", ""+e.getMessage());
		}
		return true;
	}
	public boolean UpdateIntoSnagMaster(SnagMaster obj){
		boolean success=false;
		try{
			mydb=context.openOrCreateDatabase(DBNAME, Context.MODE_PRIVATE, null);
			String strQuery;
	        strQuery = "UPDATE SnagMaster SET SnagType='"+obj.getSnagType()+"',SnagDetails='"+obj.getSnagDetails()+"',PictureURL1='"+obj.getPictureURL1()+"',PictureURL2='"+obj.getPictureURL2()+"',PictureURL3='"+obj.getPictureURL3()+"',AptAreaName='"+obj.getAptAreaName()+"',AptAreaID='"+obj.getAptAreaID()+"',ReportDate='"+obj.getReportDate()+"',SnagStatus='"+obj.getSnagStatus()+"',ResolveDate='"+obj.getResolveDate()+"',ResolveDatePictureURL1='"+obj.getResolveDatePictureURL1()+"',ResolveDatePictureURL2='"+obj.getResolveDatePictureURL2()+"',ResolveDatePictureURL3='"+obj.getResolveDatePictureURL3()+"',ReInspectedUnresolvedDate='"+obj.getReInspectedUnresolvedDate()+"',ReInspectedUnresolvedDatePictureURL1='"+obj.getReInspectedUnresolvedDatePictureURL1()+"',ReInspectedUnresolvedDatePictureURL2='"+obj.getReInspectedUnresolvedDatePictureURL2()+"',ReInspectedUnresolvedDatePictureURL3='"+obj.getReInspectedUnresolvedDatePictureURL3()+"' ,ExpectedInspectionDate='"+obj.getExpectedInspectionDate()+"',FaultType='"+obj.getFaultType()+"',Cost="+obj.getCost()+",CostTo='"+obj.getCostTo()+"',PriorityLevel='"+obj.getSnagPriority()+"',IsSyncedToWeb='"+obj.getIsDataSyncToWeb()+"',StatusForUpload='"+obj.getStatusForUpload()+"',QCC='"+obj.getQCC()+"',PercentageCompleted='"+obj.getPercentageCompleted()+"' WHERE (ID='"+obj.getID()+"' AND ApartmentID='"+obj.getApartmentID()+"');";
	        mydb.execSQL(strQuery);
	        mydb.close();
		}
		catch(Exception e){
			Toast.makeText(context, "Error Occured", Toast.LENGTH_LONG).show();
			Log.d("Error UpdateIntoSnagMaster=", ""+e.getMessage());
		}
		return true;
	}
	
	public void UpdateIsSyncedToWebSnagMaster(SnagMaster obj){
		try {
			mydb=context.openOrCreateDatabase(DBNAME, Context.MODE_PRIVATE, null);
			String strQuery="Update SnagMaster set IsSyncedToWeb='"+obj.getIsDataSyncToWeb()+"' where ID='"+obj.getID()+"'";
			mydb.execSQL(strQuery);
			mydb.close();
		} catch (Exception e) {
			// TODO: handle exception
			Log.d("Error UpdateIsSyncedToWebSnagMaster=", ""+e.getMessage());
		}
	}
	public boolean UpdateIntoSnagMasterForContractor(SnagMaster obj){
		boolean success=false;
		try{
			mydb=context.openOrCreateDatabase(DBNAME, Context.MODE_PRIVATE, null);
			String strQuery;
	        strQuery = "UPDATE SnagMaster SET ContractorStatus='"+obj.getContractorStatus()+"',ContractorExpectedBeginDate='"+obj.getContractorExpectedBeginDate()+"',ContractorRemarks='"+obj.getContractorRemarks()+"',ContractorExpectedEndDate='"+obj.getContractorExpectedEndDate()+"',ContractorActualEndDate='"+obj.getContractorActualEndDate()+"',ContractorActualBeginDate='"+obj.getContractorActualBeginDate()+"',ConCost='"+obj.getConCost()+"',ConManHours='"+obj.getConManHours()+"',ConNoOfResources='"+obj.getConNoOfResources()+"',IsSyncedToWeb='"+obj.getIsDataSyncToWeb()+"',StatusForUpload='"+obj.getStatusForUpload()+"'  WHERE (ID='"+obj.getID()+"' AND ApartmentID='"+obj.getApartmentID()+"');";
	        mydb.execSQL(strQuery);
	        mydb.close();
		}
		catch(Exception e){
			Toast.makeText(context, "Error Occured", Toast.LENGTH_LONG).show();
			Log.d("Error UpdateIntoSnagMaster=", ""+e.getMessage());
		}
		return true;
	}
//	public boolean UpdateIntoSnagMaster(SnagMaster obj){
//		boolean success=false;
//		try{
//			mydb=context.openOrCreateDatabase(DBNAME, Context.MODE_PRIVATE, null);
//			String strQuery;
//	        strQuery = "UPDATE SnagMaster SET SnagType='"+obj.getSnagType()+"',SnagDetails='"+obj.getSnagDetails()+"',PictureURL1='"+obj.getPictureURL1()+"',PictureURL2='"+obj.getPictureURL2()+"',PictureURL3='"+obj.getPictureURL3()+"',AptAreaName='"+obj.getAptAreaName()+"',AptAreaID='"+obj.getAptAreaID()+"',ReportDate='"+obj.getReportDate()+"',SnagStatus='"+obj.getSnagStatus()+"',ResolveDate='"+obj.getResolveDate()+"',ResolveDatePictureURL1='"+obj.getResolveDatePictureURL1()+"',ResolveDatePictureURL2='"+obj.getResolveDatePictureURL2()+"',ResolveDatePictureURL3='"+obj.getResolveDatePictureURL3()+"',ReInspectedUnresolvedDate='"+obj.getReInspectedUnresolvedDate()+"',ReInspectedUnresolvedDatePictureURL1='"+obj.getReInspectedUnresolvedDatePictureURL1()+"',ReInspectedUnresolvedDatePictureURL2='"+obj.getReInspectedUnresolvedDatePictureURL2()+"',ReInspectedUnresolvedDatePictureURL3='"+obj.getReInspectedUnresolvedDatePictureURL3()+"' ,ExpectedInspectionDate='"+obj.getExpectedInspectionDate()+"',FaultType='"+obj.getFaultType()+"' WHERE (ID='"+obj.getID()+"' AND ApartmentID='"+obj.getApartmentID()+"');";
//	        mydb.execSQL(strQuery);
//	        mydb.close();
//		}
//		catch(Exception e){
//			Toast.makeText(context, "Error Occured", Toast.LENGTH_LONG).show();
//			Log.d("Error UpdateIntoSnagMaster=", ""+e.getMessage());
//		}
//		return true;
//	}
	
	public SnagMaster[] getSnags(String prjID,String bldgID,String flrID,String aptmtID){
		SnagMaster arr[]=null;
		try{
			mydb=context.openOrCreateDatabase(DBNAME, Context.MODE_PRIVATE, null);
			String strQuery;
	        strQuery = "Select * from SnagMaster where (ProjectID='"+prjID+"' and BuildingID='"+bldgID+"' and FloorID='"+flrID+"' and ApartmentID='"+aptmtID+"')  Order by ReportDate desc";
	        Cursor cursor=mydb.rawQuery(strQuery, null);
	        arr=new SnagMaster[cursor.getCount()];
	        int i=0;
	        if(cursor.moveToFirst()){
	        	do{
	        		SnagMaster obj=new SnagMaster();
	        		obj.setID(cursor.getString(cursor.getColumnIndex("ID")));
	        		obj.setSnagType(cursor.getString(cursor.getColumnIndex("SnagType")));
	        		obj.setSnagDetails(cursor.getString(cursor.getColumnIndex("SnagDetails")));
	        		obj.setPictureURL1(cursor.getString(cursor.getColumnIndex("PictureURL1")));
	        		obj.setPictureURL2(cursor.getString(cursor.getColumnIndex("PictureURL2")));
	        		obj.setPictureURL3(cursor.getString(cursor.getColumnIndex("PictureURL3")));
	        		obj.setProjectID(cursor.getString(cursor.getColumnIndex("ProjectID")));
	        		obj.setProjectName(cursor.getString(cursor.getColumnIndex("ProjectName")));
	        		obj.setBuildingID(cursor.getString(cursor.getColumnIndex("BuildingID")));
	        		obj.setBuildingName(cursor.getString(cursor.getColumnIndex("BuildingName")));
	        		obj.setFloorID(cursor.getString(cursor.getColumnIndex("FloorID")));
	        		obj.setFloor(cursor.getString(cursor.getColumnIndex("Floor")));
	        		obj.setApartmentID(cursor.getString(cursor.getColumnIndex("ApartmentID")));
	        		obj.setApartment(cursor.getString(cursor.getColumnIndex("Apartment")));
	        		obj.setAptAreaName(cursor.getString(cursor.getColumnIndex("AptAreaName")));
	        		obj.setAptAreaID(cursor.getString(cursor.getColumnIndex("AptAreaID")));
	        		obj.setReportDate(cursor.getString(cursor.getColumnIndex("ReportDate")));
	        		obj.setSnagStatus(cursor.getString(cursor.getColumnIndex("SnagStatus")));
	        		obj.setResolveDate(cursor.getString(cursor.getColumnIndex("ResolveDate")));
	        		obj.setInspectorID(cursor.getString(cursor.getColumnIndex("InspectorID")));
	        		obj.setInspectorName(cursor.getString(cursor.getColumnIndex("InspectorName")));
	        		obj.setResolveDatePictureURL1(cursor.getString(cursor.getColumnIndex("ResolveDatePictureURL1")));
	        		obj.setResolveDatePictureURL2(cursor.getString(cursor.getColumnIndex("ResolveDatePictureURL2")));
	        		obj.setResolveDatePictureURL3(cursor.getString(cursor.getColumnIndex("ResolveDatePictureURL3")));
	        		obj.setReInspectedUnresolvedDate(cursor.getString(cursor.getColumnIndex("ReinspectedUnresolvedDate")));
	        		obj.setReInspectedUnresolvedDatePictureURL1(cursor.getString(cursor.getColumnIndex("ReinspectedUnresolvedDatePictureURL1")));
	        		obj.setReInspectedUnresolvedDatePictureURL2(cursor.getString(cursor.getColumnIndex("ReinspectedUnresolvedDatePictureURL2")));
	        		obj.setReInspectedUnresolvedDatePictureURL3(cursor.getString(cursor.getColumnIndex("ReinspectedUnresolvedDatePictureURL3")));
	        		
	        		obj.setExpectedInspectionDate(cursor.getString(cursor.getColumnIndex("ExpectedInspectionDate")));
	        		obj.setFaultType(cursor.getString(cursor.getColumnIndex("FaultType")));
	        		obj.setQCC(cursor.getString(cursor.getColumnIndex("QCC")));
	        		if(cursor.getString(cursor.getColumnIndex("IsSyncedToWeb")).equalsIgnoreCase("true"))
	        			obj.setIsDataSyncToWeb(true);
	        		else{
	        			obj.setIsDataSyncToWeb(false);
	        		}
	        		obj.setStatusForUpload(cursor.getString(cursor.getColumnIndex("StatusForUpload")));
	        		
	        		//String str=cursor.getString(cursor.getColumnIndex("Cost"));
	        		String str1=cursor.getString(cursor.getColumnIndex("Cost"));
	    			Double d=0.0;
	    			if(str1!=null && !str1.equalsIgnoreCase("null") && str1.length()>0)
	    				d=Double.parseDouble(str1);
	    			
	    			obj.setCost(d);
	            		
	            		obj.setCostTo(cursor.getString(cursor.getColumnIndex("CostTo")));
	            		obj.setSnagPriority(cursor.getString(cursor.getColumnIndex("PriorityLevel")));
	        		
	            		obj.setAllocatedTo(cursor.getString(cursor.getColumnIndex("AllocatedTo")));
                		obj.setAllocatedToName(cursor.getString(cursor.getColumnIndex("AllocatedToName")));
                		obj.setQCC(cursor.getString(cursor.getColumnIndex("QCC")));
                		obj.setContractorStatus(cursor.getString(cursor.getColumnIndex("ContractorStatus")));
            			obj.setContractorExpectedBeginDate(cursor.getString(cursor.getColumnIndex("ContractorExpectedBeginDate")));
            			obj.setContractorExpectedEndDate(cursor.getString(cursor.getColumnIndex("ContractorExpectedEndDate")));
            			obj.setContractorActualBeginDate(cursor.getString(cursor.getColumnIndex("ContractorActualBeginDate")));
            			obj.setContractorActualEndDate(cursor.getString(cursor.getColumnIndex("ContractorActualEndDate")));
            			if(cursor.getString(cursor.getColumnIndex("ConManHours"))!=null)
            			{
            				String manHrs=cursor.getString(cursor.getColumnIndex("ConManHours"));
            				Double dbMnHrs=0.0;
            				if(manHrs!=null && !manHrs.equalsIgnoreCase("null") && manHrs.length()>0 )
            					dbMnHrs=Double.parseDouble(manHrs);
            				obj.setConManHours(dbMnHrs);
            			}
            			else
            			{
            				obj.setConManHours(0.0);
            			}
            			if(cursor.getString(cursor.getColumnIndex("ConNoOfResources")) != null)
            			{
            				String noOfrsr=cursor.getString(cursor.getColumnIndex("ConNoOfResources"));
            				int rs=0;
            				if(noOfrsr!=null  &&!noOfrsr.equalsIgnoreCase("null") && noOfrsr.length()>0)
            					rs=Integer.parseInt(noOfrsr);
            				obj.setConNoOfResources(rs);
            			}
            			else
            			{
            				obj.setConNoOfResources(0);
            			}
            			if(cursor.getString(cursor.getColumnIndex("ConCost"))==null || (cursor.getString(cursor.getColumnIndex("ConCost"))!=null && cursor.getString(cursor.getColumnIndex("ConCost")).length()==0))
    	            		obj.setConCost(0.0);
    	            	else{
    	            		String str=cursor.getString(cursor.getColumnIndex("ConCost"));
    	            		if(str!=null && !str.equalsIgnoreCase("null") && str.length()>0)
    	            			obj.setConCost(Double.parseDouble(str));
    	            		else 
    	            			obj.setConCost(0.0);
    	            		}
    	            		
            			
                       
            			obj.setContractorRemarks(cursor.getString(cursor.getColumnIndex("ContractorRemarks")));
	            		
	        		arr[i]=obj;
	        		
	        		i++;
	        	}
	        	while(cursor.moveToNext());
	        }
	        cursor.close();
	        mydb.close();
	        
		}
		catch(Exception e)
		{
			arr=null;
			Log.d("Error getSnags=",""+e.getMessage());
		}
		return arr;
	}
	public SnagMaster[] getSnagsOfSnagTypeANDORFaultType(String prjID,String bldgID,String flrID,String aptmtID,String snagType,String FaultType,int SnagFor){//0=apartment 1=floor 2=building 3=project
		SnagMaster arr[]=null;
		try{
			mydb=context.openOrCreateDatabase(DBNAME, Context.MODE_PRIVATE, null);
			String strQuery="";
			if(SnagFor==0){
			if(FaultType==null || FaultType.length()==0)
	        strQuery = "Select * from SnagMaster where (ProjectID='"+prjID+"' and BuildingID='"+bldgID+"' and FloorID='"+flrID+"' and ApartmentID='"+aptmtID+"' and SnagType='"+snagType+"')  Order by ID desc";
			else
				strQuery = "Select * from SnagMaster where (ProjectID='"+prjID+"' and BuildingID='"+bldgID+"' and FloorID='"+flrID+"' and ApartmentID='"+aptmtID+"' and SnagType='"+snagType+"' and FaultType='"+FaultType+"')  Order by ID desc";
			}
			else if(SnagFor==1){
				if(FaultType==null || FaultType.length()==0)
			        strQuery = "Select * from SnagMaster where (ProjectID='"+prjID+"' and BuildingID='"+bldgID+"' and FloorID='"+flrID+"'  and SnagType='"+snagType+"')  Order by ID desc";
					else
						strQuery = "Select * from SnagMaster where (ProjectID='"+prjID+"' and BuildingID='"+bldgID+"' and FloorID='"+flrID+"'  and SnagType='"+snagType+"' and FaultType='"+FaultType+"')  Order by ID desc";
			}
			else if(SnagFor==2){
				if(FaultType==null || FaultType.length()==0)
			        strQuery = "Select * from SnagMaster where (ProjectID='"+prjID+"' and BuildingID='"+bldgID+"'   and SnagType='"+snagType+"')  Order by ID desc";
					else
						strQuery = "Select * from SnagMaster where (ProjectID='"+prjID+"' and BuildingID='"+bldgID+"'   and SnagType='"+snagType+"' and FaultType='"+FaultType+"')  Order by ID desc";
			}
			else if(SnagFor==3){
				if(FaultType==null || FaultType.length()==0)
			        strQuery = "Select * from SnagMaster where (ProjectID='"+prjID+"'    and SnagType='"+snagType+"')  Order by ID desc";
					else
						strQuery = "Select * from SnagMaster where (ProjectID='"+prjID+"'    and SnagType='"+snagType+"' and FaultType='"+FaultType+"')  Order by ID desc";
			}
	        Cursor cursor=mydb.rawQuery(strQuery, null);
	        arr=new SnagMaster[cursor.getCount()];
	        int i=0;
	        if(cursor.moveToFirst()){
	        	do{
	        		SnagMaster obj=new SnagMaster();
	        		obj.setID(cursor.getString(cursor.getColumnIndex("ID")));
	        		obj.setSnagType(cursor.getString(cursor.getColumnIndex("SnagType")));
	        		obj.setSnagDetails(cursor.getString(cursor.getColumnIndex("SnagDetails")));
	        		obj.setPictureURL1(cursor.getString(cursor.getColumnIndex("PictureURL1")));
	        		obj.setPictureURL2(cursor.getString(cursor.getColumnIndex("PictureURL2")));
	        		obj.setPictureURL3(cursor.getString(cursor.getColumnIndex("PictureURL3")));
	        		obj.setProjectID(cursor.getString(cursor.getColumnIndex("ProjectID")));
	        		obj.setProjectName(cursor.getString(cursor.getColumnIndex("ProjectName")));
	        		obj.setBuildingID(cursor.getString(cursor.getColumnIndex("BuildingID")));
	        		obj.setBuildingName(cursor.getString(cursor.getColumnIndex("BuildingName")));
	        		obj.setFloorID(cursor.getString(cursor.getColumnIndex("FloorID")));
	        		obj.setFloor(cursor.getString(cursor.getColumnIndex("Floor")));
	        		obj.setApartmentID(cursor.getString(cursor.getColumnIndex("ApartmentID")));
	        		obj.setApartment(cursor.getString(cursor.getColumnIndex("Apartment")));
	        		obj.setAptAreaName(cursor.getString(cursor.getColumnIndex("AptAreaName")));
	        		obj.setAptAreaID(cursor.getString(cursor.getColumnIndex("AptAreaID")));
	        		obj.setReportDate(cursor.getString(cursor.getColumnIndex("ReportDate")));
	        		obj.setSnagStatus(cursor.getString(cursor.getColumnIndex("SnagStatus")));
	        		obj.setResolveDate(cursor.getString(cursor.getColumnIndex("ResolveDate")));
	        		obj.setInspectorID(cursor.getString(cursor.getColumnIndex("InspectorID")));
	        		obj.setInspectorName(cursor.getString(cursor.getColumnIndex("InspectorName")));
	        		obj.setResolveDatePictureURL1(cursor.getString(cursor.getColumnIndex("ResolveDatePictureURL1")));
	        		obj.setResolveDatePictureURL2(cursor.getString(cursor.getColumnIndex("ResolveDatePictureURL2")));
	        		obj.setResolveDatePictureURL3(cursor.getString(cursor.getColumnIndex("ResolveDatePictureURL3")));
	        		obj.setReInspectedUnresolvedDate(cursor.getString(cursor.getColumnIndex("ReinspectedUnresolvedDate")));
	        		obj.setReInspectedUnresolvedDatePictureURL1(cursor.getString(cursor.getColumnIndex("ReinspectedUnresolvedDatePictureURL1")));
	        		obj.setReInspectedUnresolvedDatePictureURL2(cursor.getString(cursor.getColumnIndex("ReinspectedUnresolvedDatePictureURL2")));
	        		obj.setReInspectedUnresolvedDatePictureURL3(cursor.getString(cursor.getColumnIndex("ReinspectedUnresolvedDatePictureURL3")));
	        		obj.setExpectedInspectionDate(cursor.getString(cursor.getColumnIndex("ExpectedInspectionDate")));
	        		obj.setFaultType(cursor.getString(cursor.getColumnIndex("FaultType")));
	        		if(cursor.getString(cursor.getColumnIndex("IsSyncedToWeb")).equalsIgnoreCase("true"))
	        			obj.setIsDataSyncToWeb(true);
	        		else{
	        			obj.setIsDataSyncToWeb(false);
	        		}
	        		obj.setStatusForUpload(cursor.getString(cursor.getColumnIndex("StatusForUpload")));
	        		
	        		String str=cursor.getString(cursor.getColumnIndex("Cost"));
	        		Double d=0.0;
	        		if(str!=null && str.length()>0)
	        			d=Double.parseDouble(str);
	            	obj.setCost(d);
	            		
	            		
	            		obj.setCostTo(cursor.getString(cursor.getColumnIndex("CostTo")));
	            		obj.setSnagPriority(cursor.getString(cursor.getColumnIndex("PriorityLevel")));
	        		
	            		obj.setAllocatedTo(cursor.getString(cursor.getColumnIndex("AllocatedTo")));
                		obj.setAllocatedToName(cursor.getString(cursor.getColumnIndex("AllocatedToName")));
                		obj.setQCC(cursor.getString(cursor.getColumnIndex("QCC")));
                		obj.setContractorStatus(cursor.getString(cursor.getColumnIndex("ContractorStatus")));
            			obj.setContractorExpectedBeginDate(cursor.getString(cursor.getColumnIndex("ContractorExpectedBeginDate")));
            			obj.setContractorExpectedEndDate(cursor.getString(cursor.getColumnIndex("ContractorExpectedEndDate")));
            			obj.setContractorActualBeginDate(cursor.getString(cursor.getColumnIndex("ContractorActualBeginDate")));
            			obj.setContractorActualEndDate(cursor.getString(cursor.getColumnIndex("ContractorActualEndDate")));
            			String str1=cursor.getString(cursor.getColumnIndex("ConManHours"));
            			d=0.0;
            			if(str1!=null && str1.length()>0)
            				d=Double.parseDouble(str1);
            			obj.setConManHours(d);
            			obj.setConNoOfResources(cursor.getInt(cursor.getColumnIndex("ConNoOfResources")));
            			
            			str1=cursor.getString(cursor.getColumnIndex("ConCost"));
            			d=0.0;
            			if(str1!=null && str1.length()>0)
            				d=Double.parseDouble(str1);
            			obj.setConCost(d);
            			
            			obj.setContractorRemarks(cursor.getString(cursor.getColumnIndex("ContractorRemarks")));
            			d=0.0;
            			String dstr=cursor.getString(cursor.getColumnIndex("ContractorRemarks"));
            			if(dstr!=null && dstr.length()>0)
            				d=Double.parseDouble(dstr);
            			obj.setPercentageCompleted(d);
	        		arr[i]=obj;
	        		
	        		i++;
	        	}
	        	while(cursor.moveToNext());
	        }
	        cursor.close();
	        mydb.close();
	        
		}
		catch(Exception e)
		{
			Log.d("Error getSnags=",""+e.getMessage());
		}
		return arr;
	}
	
	public void downloadImage(String imageUrl)
    {
    	
    	try
    	{	
    		String path=Environment.getExternalStorageDirectory()+"/SnagReporter/Pictures/"+imageUrl;
    		File filechk=new File(path);
    		
    		if(!(filechk.exists()))
    		{
    			String downloadUrl="http://snag.itakeon.com/uploadimage/"+imageUrl;
    			URL url=new URL(downloadUrl);
    			File file=new File(path);
    			long startTime = System.currentTimeMillis();
    			Log.d("ImageManager", "download begining");
    			Log.d("ImageManager", "download url:" + url);
    			Log.d("ImageManager", "downloaded file name:" + imageUrl);
    			URLConnection con=url.openConnection();
    			InputStream is=con.getInputStream();
    			BufferedInputStream bis = new BufferedInputStream(is);
    			ByteArrayBuffer baf = new ByteArrayBuffer(1024);
    			int current = 0;
    			while ((current = bis.read()) != -1)
    			{
                     baf.append((byte) current);
    			}
    			FileOutputStream fos = new FileOutputStream(file);
    			fos.write(baf.toByteArray());
    			fos.close();
    			Log.d("ImageManager", "download ready in"
                          + ((System.currentTimeMillis() - startTime) / 1000)
                             + " sec");
    		}
    		else
    		{
    			Log.d("file exists",""+path);
    		}
    		
    	}
    	catch (Exception e)
    	{
			Log.d("error dowanload image", ""+e.getMessage());
		}
    }
	
	public SnagMaster getSnagByID(String ID)
	{
		SnagMaster obj=null;
		try
		{
			
			String strQury;
			strQury="Select * from SnagMaster where ID='"+ID+"'";
			mydb=context.openOrCreateDatabase(DBNAME, MODE_PRIVATE, null);
			Cursor cursor=mydb.rawQuery(strQury, null);
			
			if(cursor.moveToFirst())
			{
				obj=new SnagMaster();
				obj.setID(cursor.getString(cursor.getColumnIndex("ID")));
        		obj.setSnagType(cursor.getString(cursor.getColumnIndex("SnagType")));
        		obj.setSnagDetails(cursor.getString(cursor.getColumnIndex("SnagDetails")));
        		obj.setPictureURL1(cursor.getString(cursor.getColumnIndex("PictureURL1")));
        		obj.setPictureURL2(cursor.getString(cursor.getColumnIndex("PictureURL2")));
        		obj.setPictureURL3(cursor.getString(cursor.getColumnIndex("PictureURL3")));
        		obj.setProjectID(cursor.getString(cursor.getColumnIndex("ProjectID")));
        		obj.setProjectName(cursor.getString(cursor.getColumnIndex("ProjectName")));
        		obj.setBuildingID(cursor.getString(cursor.getColumnIndex("BuildingID")));
        		obj.setBuildingName(cursor.getString(cursor.getColumnIndex("BuildingName")));
        		obj.setFloorID(cursor.getString(cursor.getColumnIndex("FloorID")));
        		obj.setFloor(cursor.getString(cursor.getColumnIndex("Floor")));
        		obj.setApartmentID(cursor.getString(cursor.getColumnIndex("ApartmentID")));
        		obj.setApartment(cursor.getString(cursor.getColumnIndex("Apartment")));
        		obj.setAptAreaName(cursor.getString(cursor.getColumnIndex("AptAreaName")));
        		obj.setAptAreaID(cursor.getString(cursor.getColumnIndex("AptAreaID")));
        		obj.setReportDate(cursor.getString(cursor.getColumnIndex("ReportDate")));
        		obj.setSnagStatus(cursor.getString(cursor.getColumnIndex("SnagStatus")));
        		obj.setResolveDate(cursor.getString(cursor.getColumnIndex("ResolveDate")));
        		obj.setInspectorID(cursor.getString(cursor.getColumnIndex("InspectorID")));
        		obj.setInspectorName(cursor.getString(cursor.getColumnIndex("InspectorName")));
        		obj.setResolveDatePictureURL1(cursor.getString(cursor.getColumnIndex("ResolveDatePictureURL1")));
        		obj.setResolveDatePictureURL2(cursor.getString(cursor.getColumnIndex("ResolveDatePictureURL2")));
        		obj.setResolveDatePictureURL3(cursor.getString(cursor.getColumnIndex("ResolveDatePictureURL3")));
        		obj.setReInspectedUnresolvedDate(cursor.getString(cursor.getColumnIndex("ReinspectedUnresolvedDate")));
        		obj.setReInspectedUnresolvedDatePictureURL1(cursor.getString(cursor.getColumnIndex("ReinspectedUnresolvedDatePictureURL1")));
        		obj.setReInspectedUnresolvedDatePictureURL2(cursor.getString(cursor.getColumnIndex("ReinspectedUnresolvedDatePictureURL2")));
        		obj.setReInspectedUnresolvedDatePictureURL3(cursor.getString(cursor.getColumnIndex("ReinspectedUnresolvedDatePictureURL3")));
        		obj.setExpectedInspectionDate(cursor.getString(cursor.getColumnIndex("ExpectedInspectionDate")));
        		obj.setFaultType(cursor.getString(cursor.getColumnIndex("FaultType")));
        		if(cursor.getString(cursor.getColumnIndex("IsSyncedToWeb")).equalsIgnoreCase("true"))
        			obj.setIsDataSyncToWeb(true);
        		else{
        			obj.setIsDataSyncToWeb(false);
        		}
        		obj.setStatusForUpload(cursor.getString(cursor.getColumnIndex("StatusForUpload")));
        		
        		Double d=0.0;
        		String str=cursor.getString(cursor.getColumnIndex("Cost"));
        		if(str!=null && str.length()>0)
        			d=Double.parseDouble(str);
        		obj.setCost(d);
        			
            		
            		obj.setCostTo(cursor.getString(cursor.getColumnIndex("CostTo")));
            		obj.setSnagPriority(cursor.getString(cursor.getColumnIndex("PriorityLevel")));
            		obj.setAllocatedTo(cursor.getString(cursor.getColumnIndex("AllocatedTo")));
            		obj.setAllocatedToName(cursor.getString(cursor.getColumnIndex("AllocatedToName")));
            		obj.setQCC(cursor.getString(cursor.getColumnIndex("QCC")));
            		obj.setContractorStatus(cursor.getString(cursor.getColumnIndex("ContractorStatus")));
        			obj.setContractorExpectedBeginDate(cursor.getString(cursor.getColumnIndex("ContractorExpectedBeginDate")));
        			obj.setContractorExpectedEndDate(cursor.getString(cursor.getColumnIndex("ContractorExpectedEndDate")));
        			obj.setContractorActualBeginDate(cursor.getString(cursor.getColumnIndex("ContractorActualBeginDate")));
        			obj.setContractorActualEndDate(cursor.getString(cursor.getColumnIndex("ContractorActualEndDate")));
        			
        			String str1=cursor.getString(cursor.getColumnIndex("ConManHours"));
        			d=0.0;
        			if(str1!=null && str1.length()>0)
        				d=Double.parseDouble(str1);
        			obj.setConManHours(d);
        			
        			str1=cursor.getString(cursor.getColumnIndex("ConCost"));
        			d=0.0;
        			if(str1!=null && str1.length()>0)
        				d=Double.parseDouble(str1);
        			obj.setConCost(d);
        			
        			obj.setContractorRemarks(cursor.getString(cursor.getColumnIndex("ContractorRemarks")));
        			d=0.0;
        			String dstr=cursor.getString(cursor.getColumnIndex("ContractorRemarks"));
        			if(dstr!=null && dstr.length()>0)
        				d=Double.parseDouble(dstr);
        			obj.setPercentageCompleted(d);
        			
				cursor.close();
				mydb.close();
				
			}
			
		}
		catch (Exception e) 
		{
			Log.d("getsnagbyid fmdb",""+e.getMessage());
		}
		return obj;
	}

	public int getResolvedSnagsByProject(String ProjectID,String InspectorID){
		int Count=0;
		try{
			String strQury;
			strQury="Select count(*) as iCount from SnagMaster where ProjectID='"+ProjectID+"'  and SnagStatus='Resolved'";//and InspectorID='"+InspectorID+"'
			mydb=context.openOrCreateDatabase(DBNAME, MODE_PRIVATE, null);
			Cursor cursor=mydb.rawQuery(strQury, null);
			if(cursor.moveToFirst()){
				Count=cursor.getInt(0);
			}
			cursor.close();
			mydb.close();
		}
		catch(Exception e){
			Log.d("Error getResolvedAndUnresolvedSnagsByProject fmdb",""+e.getMessage());
		}
		return Count;
		
	}
	public int getUnresolvedSnagsByProject(String ProjectID,String InspectorID){
		int Count=0;
		try{
			String strQury;
			strQury="Select count(*) as iCount from SnagMaster where ProjectID='"+ProjectID+"'  and SnagStatus<>'Resolved'";//and InspectorID='"+InspectorID+"'
			mydb=context.openOrCreateDatabase(DBNAME, MODE_PRIVATE, null);
			Cursor cursor=mydb.rawQuery(strQury, null);
			if(cursor.moveToFirst()){
				Count=cursor.getInt(0);
			}
			cursor.close();
			mydb.close();
		}
		catch(Exception e){
			Log.d("Error getResolvedAndUnresolvedSnagsByProject fmdb",""+e.getMessage());
		}
		return Count;
		
	}
	
	public int getResolvedSnagsByBuilding(String ProjectID,String BuildingID,String InspectorID){
		int Count=0;
		try{
			String strQury;
			strQury="Select count(*) as iCount from SnagMaster where BuildingID='"+BuildingID+"' and ProjectID='"+ProjectID+"'  and SnagStatus='Resolved'";//and InspectorID='"+InspectorID+"'
			mydb=context.openOrCreateDatabase(DBNAME, MODE_PRIVATE, null);
			Cursor cursor=mydb.rawQuery(strQury, null);
			if(cursor.moveToFirst()){
				Count=cursor.getInt(0);
			}
			cursor.close();
			mydb.close();
		}
		catch(Exception e){
			Log.d("Error getResolvedAndUnresolvedSnagsByBuilding fmdb",""+e.getMessage());
		}
		return Count;
		
	}
	public int getUnresolvedSnagsByBuilding(String ProjectID,String BuildingID,String InspectorID){
		int Count=0;
		try{
			String strQury;
			strQury="Select count(*) as iCount from SnagMaster where BuildingID='"+BuildingID+"' and ProjectID='"+ProjectID+"'  and SnagStatus<>'Resolved'";//and InspectorID='"+InspectorID+"'
			mydb=context.openOrCreateDatabase(DBNAME, MODE_PRIVATE, null);
			Cursor cursor=mydb.rawQuery(strQury, null);
			if(cursor.moveToFirst()){
				Count=cursor.getInt(0);
			}
			cursor.close();
			mydb.close();
		}
		catch(Exception e){
			Log.d("Error getResolvedAndUnresolvedSnagsByBuilding fmdb",""+e.getMessage());
		}
		return Count;
		
	}
	
	public int getResolvedSnagsByFloor(String ProjectID,String BuildingID,String FloorID,String InspectorID){
		int Count=0;
		try{
			String strQury;
			strQury="Select count(*) as iCount from SnagMaster where FloorID='"+FloorID+"' and BuildingID='"+BuildingID+"' and ProjectID='"+ProjectID+"'  and SnagStatus='Resolved'";//and InspectorID='"+InspectorID+"'
			mydb=context.openOrCreateDatabase(DBNAME, MODE_PRIVATE, null);
			Cursor cursor=mydb.rawQuery(strQury, null);
			if(cursor.moveToFirst()){
				Count=cursor.getInt(0);
			}
			cursor.close();
			mydb.close();
		}
		catch(Exception e){
			Log.d("Error getResolvedAndUnresolvedSnagsByBuilding fmdb",""+e.getMessage());
		}
		return Count;
		
	}
	public int getUnresolvedSnagsByFloor(String ProjectID,String BuildingID,String FloorID,String InspectorID){
		int Count=0;
		try{
			String strQury;
			strQury="Select count(*) as iCount from SnagMaster where FloorID='"+FloorID+"' and BuildingID='"+BuildingID+"' and ProjectID='"+ProjectID+"'  and SnagStatus<>'Resolved'";//and InspectorID='"+InspectorID+"'
			mydb=context.openOrCreateDatabase(DBNAME, MODE_PRIVATE, null);
			Cursor cursor=mydb.rawQuery(strQury, null);
			if(cursor.moveToFirst()){
				Count=cursor.getInt(0);
			}
			cursor.close();
			mydb.close();
		}
		catch(Exception e){
			Log.d("Error getResolvedAndUnresolvedSnagsByBuilding fmdb",""+e.getMessage());
		}
		return Count;
		
	}
	
	public int getResolvedSnagsByApt(String ProjectID,String BuildingID,String FloorID,String AptID,String InspectorID){
		int Count=0;
		try{
			String strQury;
			strQury="Select count(*) as iCount from SnagMaster where ApartmentID='"+AptID+"' and FloorID='"+FloorID+"' and BuildingID='"+BuildingID+"' and ProjectID='"+ProjectID+"'  and SnagStatus='Resolved'";//and InspectorID='"+InspectorID+"'
			mydb=context.openOrCreateDatabase(DBNAME, MODE_PRIVATE, null);
			Cursor cursor=mydb.rawQuery(strQury, null);
			if(cursor.moveToFirst()){
				Count=cursor.getInt(0);
			}
			cursor.close();
			mydb.close();
		}
		catch(Exception e){
			Log.d("Error getResolvedAndUnresolvedSnagsByBuilding fmdb",""+e.getMessage());
		}
		return Count;
		
	}
	public int getUnresolvedSnagsByApt(String ProjectID,String BuildingID,String FloorID,String AptID,String InspectorID){
		int Count=0;
		try{
			String strQury;
			strQury="Select count(*) as iCount from SnagMaster where ApartmentID='"+AptID+"' and FloorID='"+FloorID+"' and BuildingID='"+BuildingID+"' and ProjectID='"+ProjectID+"'  and SnagStatus<>'Resolved'";//and InspectorID='"+InspectorID+"'
			mydb=context.openOrCreateDatabase(DBNAME, MODE_PRIVATE, null);
			Cursor cursor=mydb.rawQuery(strQury, null);
			if(cursor.moveToFirst()){
				Count=cursor.getInt(0);
			}
			cursor.close();
			mydb.close();
		}
		catch(Exception e){
			Log.d("Error getResolvedAndUnresolvedSnagsByBuilding fmdb",""+e.getMessage());
		}
		return Count;
		
	}
	
	
	public void UpdateIsSyncedSnagMaster(String ID){
		try{
			mydb=context.openOrCreateDatabase(DBNAME, Context.MODE_PRIVATE, null);
			String strQuery;
	        strQuery = "UPDATE SnagMaster SET IsSyncedToWeb='true' WHERE (ID='"+ID+"');";
	        mydb.execSQL(strQuery);
	        mydb.close();
		}
		catch(Exception e){
			Log.d("Error UpdateIsSyncedSnagMaster fmdb",""+e.getMessage());
		}
	}

	public SnagMaster getUnSyncedModifiedSnag()
	{
		SnagMaster obj=null;
		try
		{
			String strQuery;
			strQuery="Select * from SnagMaster where IsSyncedToWeb='false' and StatusForUpload='Modified'";
			mydb=context.openOrCreateDatabase(DBNAME,MODE_PRIVATE,null);
			Cursor cursor=mydb.rawQuery(strQuery, null);
			if(cursor.moveToFirst())
			{
				obj=new SnagMaster();
				obj.setID(cursor.getString(cursor.getColumnIndex("ID")));
        		obj.setSnagType(cursor.getString(cursor.getColumnIndex("SnagType")));
        		obj.setSnagDetails(cursor.getString(cursor.getColumnIndex("SnagDetails")));
        		obj.setPictureURL1(cursor.getString(cursor.getColumnIndex("PictureURL1")));
        		obj.setPictureURL2(cursor.getString(cursor.getColumnIndex("PictureURL2")));
        		obj.setPictureURL3(cursor.getString(cursor.getColumnIndex("PictureURL3")));
        		obj.setProjectID(cursor.getString(cursor.getColumnIndex("ProjectID")));
        		obj.setProjectName(cursor.getString(cursor.getColumnIndex("ProjectName")));
        		obj.setBuildingID(cursor.getString(cursor.getColumnIndex("BuildingID")));
        		obj.setBuildingName(cursor.getString(cursor.getColumnIndex("BuildingName")));
        		obj.setFloorID(cursor.getString(cursor.getColumnIndex("FloorID")));
        		obj.setFloor(cursor.getString(cursor.getColumnIndex("Floor")));
        		obj.setApartmentID(cursor.getString(cursor.getColumnIndex("ApartmentID")));
        		obj.setApartment(cursor.getString(cursor.getColumnIndex("Apartment")));
        		obj.setAptAreaName(cursor.getString(cursor.getColumnIndex("AptAreaName")));
        		obj.setAptAreaID(cursor.getString(cursor.getColumnIndex("AptAreaID")));
        		obj.setReportDate(cursor.getString(cursor.getColumnIndex("ReportDate")));
        		obj.setSnagStatus(cursor.getString(cursor.getColumnIndex("SnagStatus")));
        		obj.setResolveDate(cursor.getString(cursor.getColumnIndex("ResolveDate")));
        		obj.setInspectorID(cursor.getString(cursor.getColumnIndex("InspectorID")));
        		obj.setInspectorName(cursor.getString(cursor.getColumnIndex("InspectorName")));
        		obj.setResolveDatePictureURL1(cursor.getString(cursor.getColumnIndex("ResolveDatePictureURL1")));
        		obj.setResolveDatePictureURL2(cursor.getString(cursor.getColumnIndex("ResolveDatePictureURL2")));
        		obj.setResolveDatePictureURL3(cursor.getString(cursor.getColumnIndex("ResolveDatePictureURL3")));
        		obj.setReInspectedUnresolvedDate(cursor.getString(cursor.getColumnIndex("ReinspectedUnresolvedDate")));
        		obj.setReInspectedUnresolvedDatePictureURL1(cursor.getString(cursor.getColumnIndex("ReinspectedUnresolvedDatePictureURL1")));
        		obj.setReInspectedUnresolvedDatePictureURL2(cursor.getString(cursor.getColumnIndex("ReinspectedUnresolvedDatePictureURL2")));
        		obj.setReInspectedUnresolvedDatePictureURL3(cursor.getString(cursor.getColumnIndex("ReinspectedUnresolvedDatePictureURL3")));
        		
        		
        		obj.setExpectedInspectionDate(cursor.getString(cursor.getColumnIndex("ExpectedInspectionDate")));
        		obj.setFaultType(cursor.getString(cursor.getColumnIndex("FaultType")));
        		
        		String str1=cursor.getString(cursor.getColumnIndex("Cost"));
    			Double d=0.0;
    			if(str1!=null && str1.length()>0)
    				d=Double.parseDouble(str1);
    			obj.setCost(d);
        		
        		obj.setCostTo(cursor.getString(cursor.getColumnIndex("CostTo")));
        		obj.setSnagPriority(cursor.getString(cursor.getColumnIndex("PriorityLevel")));
        		 d=0.0;
    			String dstr=cursor.getString(cursor.getColumnIndex("ContractorRemarks"));
    			if(dstr!=null && dstr.length()>0)
    				d=Double.parseDouble(dstr);
    			obj.setPercentageCompleted(d);
			}
			cursor.close();
			mydb.close();
		}
		catch (Exception e) 
		{
			Log.d("Error unSynchModified",""+e.getMessage());
		}
		return obj;
	}
	public SnagMaster getUnSyncedNewSnag()
	{
		SnagMaster obj=null;
		try
		{
			String strQuery;
			strQuery="Select * from SnagMaster where IsSyncedToWeb='false' and StatusForUpload='Inserted'";
			mydb=context.openOrCreateDatabase(DBNAME,MODE_PRIVATE,null);
			Cursor cursor=mydb.rawQuery(strQuery, null);
			if(cursor.moveToFirst())
			{
				obj=new SnagMaster();
				obj.setID(cursor.getString(cursor.getColumnIndex("ID")));
        		obj.setSnagType(cursor.getString(cursor.getColumnIndex("SnagType")));
        		obj.setSnagDetails(cursor.getString(cursor.getColumnIndex("SnagDetails")));
        		obj.setPictureURL1(cursor.getString(cursor.getColumnIndex("PictureURL1")));
        		obj.setPictureURL2(cursor.getString(cursor.getColumnIndex("PictureURL2")));
        		obj.setPictureURL3(cursor.getString(cursor.getColumnIndex("PictureURL3")));
        		obj.setProjectID(cursor.getString(cursor.getColumnIndex("ProjectID")));
        		obj.setProjectName(cursor.getString(cursor.getColumnIndex("ProjectName")));
        		obj.setBuildingID(cursor.getString(cursor.getColumnIndex("BuildingID")));
        		obj.setBuildingName(cursor.getString(cursor.getColumnIndex("BuildingName")));
        		obj.setFloorID(cursor.getString(cursor.getColumnIndex("FloorID")));
        		obj.setFloor(cursor.getString(cursor.getColumnIndex("Floor")));
        		obj.setApartmentID(cursor.getString(cursor.getColumnIndex("ApartmentID")));
        		obj.setApartment(cursor.getString(cursor.getColumnIndex("Apartment")));
        		obj.setAptAreaName(cursor.getString(cursor.getColumnIndex("AptAreaName")));
        		obj.setAptAreaID(cursor.getString(cursor.getColumnIndex("AptAreaID")));
        		obj.setReportDate(cursor.getString(cursor.getColumnIndex("ReportDate")));
        		obj.setSnagStatus(cursor.getString(cursor.getColumnIndex("SnagStatus")));
        		obj.setResolveDate(cursor.getString(cursor.getColumnIndex("ResolveDate")));
        		obj.setInspectorID(cursor.getString(cursor.getColumnIndex("InspectorID")));
        		obj.setInspectorName(cursor.getString(cursor.getColumnIndex("InspectorName")));
        		obj.setResolveDatePictureURL1(cursor.getString(cursor.getColumnIndex("ResolveDatePictureURL1")));
        		obj.setResolveDatePictureURL2(cursor.getString(cursor.getColumnIndex("ResolveDatePictureURL2")));
        		obj.setResolveDatePictureURL3(cursor.getString(cursor.getColumnIndex("ResolveDatePictureURL3")));
        		obj.setReInspectedUnresolvedDate(cursor.getString(cursor.getColumnIndex("ReinspectedUnresolvedDate")));
        		obj.setReInspectedUnresolvedDatePictureURL1(cursor.getString(cursor.getColumnIndex("ReinspectedUnresolvedDatePictureURL1")));
        		obj.setReInspectedUnresolvedDatePictureURL2(cursor.getString(cursor.getColumnIndex("ReinspectedUnresolvedDatePictureURL2")));
        		obj.setReInspectedUnresolvedDatePictureURL3(cursor.getString(cursor.getColumnIndex("ReinspectedUnresolvedDatePictureURL3")));
        		obj.setExpectedInspectionDate(cursor.getString(cursor.getColumnIndex("ExpectedInspectionDate")));
        		obj.setFaultType(cursor.getString(cursor.getColumnIndex("FaultType")));
        		
        		String str1=cursor.getString(cursor.getColumnIndex("Cost"));
    			Double d=0.0;
    			if(str1!=null && str1.length()>0)
    				d=Double.parseDouble(str1);
    			obj.setCost(d);
            		
            		obj.setCostTo(cursor.getString(cursor.getColumnIndex("CostTo")));
            		obj.setSnagPriority(cursor.getString(cursor.getColumnIndex("PriorityLevel")));
            		 d=0.0;
        			String dstr=cursor.getString(cursor.getColumnIndex("ContractorRemarks"));
        			if(dstr!=null && dstr.length()>0)
        				d=Double.parseDouble(dstr);
        			obj.setPercentageCompleted(d);
			}
			cursor.close();
			mydb.close();
		}
		catch (Exception e)
		{
			Log.d("error unSynchNew fmdb",""+e.getMessage());
		}
		return obj;
	}
	
	public void performLogout(String ID){
		try{
			String strQuery;
			strQuery="Update Registration set IsLoggedIn='false' where ID='"+ID+"'";
			mydb=context.openOrCreateDatabase(DBNAME,MODE_PRIVATE,null);
			mydb.execSQL(strQuery);
	        
	        mydb.close();
		}
		catch(Exception e){
			Log.d("Error=", ""+e.getMessage());
		}
	}
	
	public void insertJobTypes1(JobType[] arr){
		try{
			mydb=context.openOrCreateDatabase(DBNAME, Context.MODE_PRIVATE, null);
			String strQuery;
			for(int i=0;i<arr.length;i++){
			strQuery="Select ID from JobType where ID='"+arr[i].getID()+"';";
			Cursor cursor=mydb.rawQuery(strQuery, null);
			if(cursor.moveToFirst()){
				strQuery="UPDATE JobType SET JobType='"+arr[i].getJobType()+"',JobDetails='"+arr[i].getJobDetails()+"' where ID='"+arr[i].getID()+"';";
			}
			else{
	        strQuery = "Insert into JobType(ID,JobType,JobDetails) values('"+arr[i].getID()+"','"+arr[i].getJobType()+"','"+arr[i].getJobDetails()+"');";
			}
	        mydb.execSQL(strQuery);
	        cursor.close();
			}
	        mydb.close();
		}
		catch(Exception e){
			Log.d("Error insertJobType=",""+e.getMessage());
		}
	}
	
	public void insertFaultTypes1(FaultType[] arr){
		try{
			mydb=context.openOrCreateDatabase(DBNAME, Context.MODE_PRIVATE, null);
			String strQuery;
			for(int i=0;i<arr.length;i++){
			strQuery="Select ID from FaultType where ID='"+arr[i].getID()+"';";
			Cursor cursor=mydb.rawQuery(strQuery, null);
			if(cursor.moveToFirst()){
				strQuery="UPDATE FaultType SET FaultType='"+arr[i].getFaultType()+"',FaultDetails='"+arr[i].getFaultDetails()+"',JobTypeID='"+arr[i].getJobTypeID()+"',JobType='"+arr[i].getJobType()+"' where ID='"+arr[i].getID()+"';";
			}
			else{
	        strQuery = "Insert into FaultType(ID,FaultType,FaultDetails,JobTypeID,JobType) values('"+arr[i].getID()+"','"+arr[i].getFaultType()+"','"+arr[i].getFaultDetails()+"','"+arr[i].getJobTypeID()+"','"+arr[i].getJobType()+"');";
			}
	        mydb.execSQL(strQuery);
	        cursor.close();
			}
	        mydb.close();
		}
		catch(Exception e){
			Log.d("Error insertFaultTypes=",""+e.getMessage());
		}
	}
///@@@@@@@@@@@@@ Sync functions
	public ProjectMaster getProjectsNotSynced(int st){
    	ProjectMaster arr=null;
    	try{
    		
    		
    		
    			String strQuery;
    			if(st==0)
    				strQuery="Select * from ProjectMaster where IsSyncedToWeb='false' and StatusForUpload='Inserted'";
    			else
    				strQuery="Select * from ProjectMaster where IsSyncedToWeb='false' and StatusForUpload='Modified'";
    			
    			mydb=context.openOrCreateDatabase(DBNAME,MODE_PRIVATE,null);
    			Cursor cursor=mydb.rawQuery(strQuery, null);
    			
    			
    	        if(cursor.moveToFirst()){
    	        		arr=new ProjectMaster();
    	        		ProjectMaster obj=new ProjectMaster();
    	        		obj.setID(cursor.getString(cursor.getColumnIndex("ID")));
    	        		obj.setProjectName(cursor.getString(cursor.getColumnIndex("ProjectName")));
    	        		obj.setNoOfBuildings(cursor.getInt(cursor.getColumnIndex("NoOfBuildings")));
    	        		obj.setLocation(cursor.getString(cursor.getColumnIndex("Location")));
    	        		obj.setAddress1(cursor.getString(cursor.getColumnIndex("Address1")));
    	        		obj.setAddress2(cursor.getString(cursor.getColumnIndex("Address2")));
    	        		obj.setPincode(cursor.getString(cursor.getColumnIndex("Pincode")));
    	        		obj.setCity(cursor.getString(cursor.getColumnIndex("City")));
    	        		obj.setState(cursor.getString(cursor.getColumnIndex("State")));
    	        		obj.setImageName(cursor.getString(cursor.getColumnIndex("ImageName")));
    	        		obj.setAbout(cursor.getString(cursor.getColumnIndex("About")));
    	        		obj.setBuilderID(cursor.getString(cursor.getColumnIndex("BuilderID")));
    					obj.setBuilderName(cursor.getString(cursor.getColumnIndex("BuilderName")));
    	        		arr=obj;
    	        		
    	        		
    	        	
    	        }
    	        cursor.close();
    	        mydb.close();
    	}
    	catch(Exception e){
    		
    	}
    	return arr;
    }
	public void updateProjectSynced(String  ID){
    	try{
    		
    			
    			String strQuery;
    			strQuery="Update ProjectMaster set IsSyncedToWeb='true' where ID='"+ID+"'";
    			mydb=context.openOrCreateDatabase(DBNAME,MODE_PRIVATE,null);
    			mydb.execSQL(strQuery);
    	        mydb.close();
    	}
    	catch(Exception e){
    		Log.d("Error", ""+e.getMessage());
    	}
    	
    }
	public BuildingMaster getBuildingsNotSynced(int st){
    	BuildingMaster arr=null;
    	try{
    		
    		
    			String strQuery;
    			if(st==0)
    			strQuery="Select * from BuildingMaster where IsSyncedToWeb='false' and StatusForUpload='Inserted'";
    			else
    				strQuery="Select * from BuildingMaster where IsSyncedToWeb='false' and StatusForUpload='Modified'";
    			mydb=context.openOrCreateDatabase(DBNAME,MODE_PRIVATE,null);
    			Cursor cursor=mydb.rawQuery(strQuery, null);
    			
    			
    	        if(cursor.moveToFirst()){
    	        	
    	        		arr=new BuildingMaster();
    	        		BuildingMaster obj=new BuildingMaster();
    	        		obj.setID(cursor.getString(cursor.getColumnIndex("ID")));
    	        		obj.setBuildingName(cursor.getString(cursor.getColumnIndex("BuildingName")));
    	        		obj.setNoOfElevators(cursor.getInt(cursor.getColumnIndex("NoOfElevators")));
    	        		obj.setNoOfFloors(cursor.getInt(cursor.getColumnIndex("NoOfFloors")));
    	        		obj.setProjectID(cursor.getString(cursor.getColumnIndex("ProjectID")));
    	        		obj.setProjectName(cursor.getString(cursor.getColumnIndex("ProjectName")));
    	        		obj.setImageName(cursor.getString(cursor.getColumnIndex("ImageName")));
    	        		
    	        		arr=obj;
    	        		
    	        		
    	        	
    	        }
    	        cursor.close();
    	        mydb.close();
    	}
    	catch(Exception e){
    		
    	}
    	return arr;
    }
	public void updateBuildingSynced(String  ID){
    	try{
    		
    		
    			String strQuery;
    			strQuery="Update BuildingMaster set IsSyncedToWeb='true' where ID='"+ID+"'";
    			mydb=context.openOrCreateDatabase(DBNAME,MODE_PRIVATE,null);
    			mydb.execSQL(strQuery);
    	        mydb.close();
    	}
    	catch(Exception e){
    		Log.d("Error", ""+e.getMessage());
    	}
    	
    }
	public FloorMaster getFloorsNotSynced( int st){
    	FloorMaster arr=null;
    	try{
    		
    		
    			String strQuery;
    			if(st==0)
    			strQuery="Select * from FloorMaster where IsSyncedToWeb='false' and StatusForUpload='Inserted'";
    			else
    			strQuery="Select * from FloorMaster where IsSyncedToWeb='false' and StatusForUpload='Modified'";
    			mydb=context.openOrCreateDatabase(DBNAME,MODE_PRIVATE,null);
    			Cursor cursor=mydb.rawQuery(strQuery, null);
    			
    			
    	        if(cursor.moveToFirst()){
    	        	
    	        		arr=new FloorMaster();
    	        		FloorMaster obj=new FloorMaster();
    	        		obj.setID(cursor.getString(cursor.getColumnIndex("ID")));
    	        		obj.setNoOfApartments(cursor.getInt(cursor.getColumnIndex("NoOfApartments")));
    	        		obj.setFloor(cursor.getString(cursor.getColumnIndex("Floor")));
    	        		obj.setBuildingID(cursor.getString(cursor.getColumnIndex("BuildingID")));
    	        		obj.setBuildingName(cursor.getString(cursor.getColumnIndex("BuildingName")));
    	        		obj.setProjectID(cursor.getString(cursor.getColumnIndex("ProjectID")));
    	        		obj.setProjectName(cursor.getString(cursor.getColumnIndex("ProjectName")));
    	        		obj.setImageName(cursor.getString(cursor.getColumnIndex("ImageName")));
    	        		obj.setFloorPlanImage(cursor.getString(cursor.getColumnIndex("FloorPlanImage")));
    	        		arr=obj;
    	        		
    	        	
    	        }
    	        cursor.close();
    	        mydb.close();
    	}
    	catch(Exception e){
    		
    	}
    	return arr;
    }
	public void updateFloorSynced(String  ID){
    	try{
    		
    		
    			String strQuery;
    			strQuery="Update FloorMaster set IsSyncedToWeb='true' where ID='"+ID+"'";
    			mydb=context.openOrCreateDatabase(DBNAME,MODE_PRIVATE,null);
    			mydb.execSQL(strQuery);
    	        mydb.close();
    	}
    	catch(Exception e){
    		Log.d("Error", ""+e.getMessage());
    	}
    	
    }
	public ApartmentMaster getAptsNotSynced(int st){
    	ApartmentMaster arr=null;
    	try{
    		
    		
    			String strQuery;
    			if(st==0)
    				strQuery="Select * from ApartmentMaster where IsSyncedToWeb='false' and StatusForUpload='Inserted'";
    			else
    				strQuery="Select * from ApartmentMaster where IsSyncedToWeb='false' and StatusForUpload='Modified'";
    			mydb=context.openOrCreateDatabase(DBNAME,MODE_PRIVATE,null);
    			Cursor cursor=mydb.rawQuery(strQuery, null);
    			
    			
    	        if(cursor.moveToFirst()){
    	        	
    	        		arr=new ApartmentMaster();
    	        		ApartmentMaster obj=new ApartmentMaster();
    	        		obj.setID(cursor.getString(cursor.getColumnIndex("ID")));
    	        		obj.setApartmentNo(cursor.getString(cursor.getColumnIndex("ApartmentNo")));
    	        		
    	        		obj.setFloorID(cursor.getString(cursor.getColumnIndex("FloorID")));
    	        		obj.setFloor(cursor.getString(cursor.getColumnIndex("Floor")));
    	        		obj.setBuildingID(cursor.getString(cursor.getColumnIndex("BuildingID")));
    	        		obj.setBuildingName(cursor.getString(cursor.getColumnIndex("BuildingName")));
    	        		obj.setProjectID(cursor.getString(cursor.getColumnIndex("ProjectID")));
    	        		obj.setProjectName(cursor.getString(cursor.getColumnIndex("ProjectName")));
    	        		obj.setAptPlanID(cursor.getString(cursor.getColumnIndex("AptPlanID")));
    	        		obj.setAptPlanName(cursor.getString(cursor.getColumnIndex("AptPlanName")));
    	        		obj.setAptType(cursor.getString(cursor.getColumnIndex("AptType")));
    	        		obj.setImageName(cursor.getString(cursor.getColumnIndex("ImageName")));
    	        		arr=obj;
    	        		
    	        	
    	        }
    	        cursor.close();
    	        mydb.close();
    	}
    	catch(Exception e){
    		
    	}
    	return arr;
    }
	public void updateAptSynced(String  ID){
    	try{
    		
    		
    			String strQuery;
    			strQuery="Update ApartmentMaster set IsSyncedToWeb='true' where ID='"+ID+"'";
    			mydb=context.openOrCreateDatabase(DBNAME,MODE_PRIVATE,null);
    			mydb.execSQL(strQuery);
    	        mydb.close();
    	}
    	catch(Exception e){
    		Log.d("Error", ""+e.getMessage());
    	}
    	
    }
	public FaultType getFaultTypeNotSynced(){
		FaultType arr=null;
		try{
			mydb=context.openOrCreateDatabase(DBNAME, Context.MODE_PRIVATE, null);
			String strQuery;
	        strQuery = "Select * from FaultType where IsSyncedToWeb='false'";
	        Cursor cursor=mydb.rawQuery(strQuery, null);
	        
	        
	        if(cursor.moveToFirst()){
	        	arr=new FaultType();
	        		FaultType obj=new FaultType();
	        		obj.setID(cursor.getString(cursor.getColumnIndex("ID")));
	        		obj.setJobType(cursor.getString(cursor.getColumnIndex("JobType")));
	        		obj.setJobTypeID(cursor.getString(cursor.getColumnIndex("JobTypeID")));
	        		obj.setFaultType(cursor.getString(cursor.getColumnIndex("FaultType")));
	        		obj.setFaultDetails(cursor.getString(cursor.getColumnIndex("FaultDetails")));
	        		//String str=cursor.getString(cursor.getColumnIndex("IsSyncedToWeb"));
//	        		if(str.equalsIgnoreCase("true"))
//	        		obj.setIsSyncedToWeb(true);
//	        		else
//	        			obj.setIsSyncedToWeb(false);
	        		arr=obj;
	        		
	        	
	        }
	        cursor.close();
	        mydb.close();
		}
		catch(Exception e){
			Log.d("Error getFaultType=", ""+e.getMessage());
		}
		return arr;
	}
	public void updateFaultTypeSynced(String ID){
		try{
			
    		
    			String strQuery;
    			strQuery="Update FaultType set IsSyncedToWeb='true' where ID='"+ID+"'";
    			mydb=context.openOrCreateDatabase(DBNAME,MODE_PRIVATE,null);
    			mydb.execSQL(strQuery);
    	        mydb.close();
		}
		catch(Exception e){
			Log.d("Error UpdateIsSyncedSnagMaster fmdb",""+e.getMessage());
		}
	}
	public JobType getJobTypeNotSynced(){
		JobType arr=null;
		try{
			mydb=context.openOrCreateDatabase(DBNAME, Context.MODE_PRIVATE, null);
			String strQuery;
	        strQuery = "Select * from JobType where IsSyncedToWeb='false'";
	        Cursor cursor=mydb.rawQuery(strQuery, null);
	       
	        
	        if(cursor.moveToFirst()){
	        	
	        		 arr=new JobType();
	        		JobType obj=new JobType();
	        		obj.setID(cursor.getString(cursor.getColumnIndex("ID")));
	        		obj.setJobType(cursor.getString(cursor.getColumnIndex("JobType")));
	        		obj.setJobDetails(cursor.getString(cursor.getColumnIndex("JobDetails")));
//	        		String str=cursor.getString(cursor.getColumnIndex("IsSyncedToWeb"));
//	        		if(str.equalsIgnoreCase("true"))
//	        			obj.setIsSyncedToWeb(true);
//	        		else
//	        			obj.setIsSyncedToWeb(false);
	        		arr=obj;
	        		
	        	
	        }
	        cursor.close();
	        mydb.close();
		}
		catch(Exception e){
			Log.d("Error getJobType=", ""+e.getMessage());
		}
		return arr;
	}
	
	public void updateJobTypeSynced(String ID){
		try{
			
    		
    			String strQuery;
    			strQuery="Update JobType set IsSyncedToWeb='true' where ID='"+ID+"'";
    			mydb=context.openOrCreateDatabase(DBNAME,MODE_PRIVATE,null);
    			mydb.execSQL(strQuery);
    	        mydb.close();
		}
		catch(Exception e){
			Log.d("Error UpdateIsSyncedSnagMaster fmdb",""+e.getMessage());
		}
	}
	
	public SnagMaster getSnagsNotSynced(int st){
    	SnagMaster arr=null;
    	try{
    		
    			String strQuery;
    			if(st==0)
    				strQuery="Select * from SnagMaster where IsSyncedToWeb='false' and StatusForUpload='Inserted'";
    			else
    				strQuery="Select * from SnagMaster where IsSyncedToWeb='false' and StatusForUpload='Modified'";
    			mydb=context.openOrCreateDatabase(DBNAME,MODE_PRIVATE,null);
    			Cursor cursor=mydb.rawQuery(strQuery, null);
    			
    			
    	        if(cursor.moveToFirst()){
    	        	
    	        		arr=new SnagMaster();
    	        		SnagMaster obj=new SnagMaster();
    					obj.setID(cursor.getString(cursor.getColumnIndex("ID")));
    	        		obj.setSnagType(cursor.getString(cursor.getColumnIndex("SnagType")));
    	        		obj.setSnagDetails(cursor.getString(cursor.getColumnIndex("SnagDetails")));
    	        		obj.setPictureURL1(cursor.getString(cursor.getColumnIndex("PictureURL1")));
    	        		obj.setPictureURL2(cursor.getString(cursor.getColumnIndex("PictureURL2")));
    	        		obj.setPictureURL3(cursor.getString(cursor.getColumnIndex("PictureURL3")));
    	        		obj.setProjectID(cursor.getString(cursor.getColumnIndex("ProjectID")));
    	        		obj.setProjectName(cursor.getString(cursor.getColumnIndex("ProjectName")));
    	        		obj.setBuildingID(cursor.getString(cursor.getColumnIndex("BuildingID")));
    	        		obj.setBuildingName(cursor.getString(cursor.getColumnIndex("BuildingName")));
    	        		obj.setFloorID(cursor.getString(cursor.getColumnIndex("FloorID")));
    	        		obj.setFloor(cursor.getString(cursor.getColumnIndex("Floor")));
    	        		obj.setApartmentID(cursor.getString(cursor.getColumnIndex("ApartmentID")));
    	        		obj.setApartment(cursor.getString(cursor.getColumnIndex("Apartment")));
    	        		obj.setAptAreaName(cursor.getString(cursor.getColumnIndex("AptAreaName")));
    	        		obj.setAptAreaID(cursor.getString(cursor.getColumnIndex("AptAreaID")));
    	        		obj.setReportDate(cursor.getString(cursor.getColumnIndex("ReportDate")));
    	        		obj.setSnagStatus(cursor.getString(cursor.getColumnIndex("SnagStatus")));
    	        		obj.setResolveDate(cursor.getString(cursor.getColumnIndex("ResolveDate")));
    	        		obj.setInspectorID(cursor.getString(cursor.getColumnIndex("InspectorID")));
    	        		obj.setInspectorName(cursor.getString(cursor.getColumnIndex("InspectorName")));
    	        		obj.setResolveDatePictureURL1(cursor.getString(cursor.getColumnIndex("ResolveDatePictureURL1")));
    	        		obj.setResolveDatePictureURL2(cursor.getString(cursor.getColumnIndex("ResolveDatePictureURL2")));
    	        		obj.setResolveDatePictureURL3(cursor.getString(cursor.getColumnIndex("ResolveDatePictureURL3")));
    	        		obj.setReInspectedUnresolvedDate(cursor.getString(cursor.getColumnIndex("ReinspectedUnresolvedDate")));
    	        		obj.setReInspectedUnresolvedDatePictureURL1(cursor.getString(cursor.getColumnIndex("ReinspectedUnresolvedDatePictureURL1")));
    	        		obj.setReInspectedUnresolvedDatePictureURL2(cursor.getString(cursor.getColumnIndex("ReinspectedUnresolvedDatePictureURL2")));
    	        		obj.setReInspectedUnresolvedDatePictureURL3(cursor.getString(cursor.getColumnIndex("ReinspectedUnresolvedDatePictureURL3")));
    	        		obj.setExpectedInspectionDate(cursor.getString(cursor.getColumnIndex("ExpectedInspectionDate")));
    	        		obj.setFaultType(cursor.getString(cursor.getColumnIndex("FaultType")));
    	        		double d=0.0;
            			String dstr=cursor.getString(cursor.getColumnIndex("ContractorRemarks"));
            			if(dstr!=null && dstr.length()>0)
            				d=Double.parseDouble(dstr);
            			obj.setPercentageCompleted(d);
            			
            			
            			obj.setAllocatedTo(cursor.getString(cursor.getColumnIndex("AllocatedTo")));
            			obj.setAllocatedToName(cursor.getString(cursor.getColumnIndex("AllocatedToName")));
            			obj.setQccRemarks(cursor.getString(cursor.getColumnIndex("QCCRemarks")));
            			obj.setQCC(cursor.getString(cursor.getColumnIndex("QCC")));
            			obj.setContractorID(cursor.getString(cursor.getColumnIndex("ContractorID")));
            			obj.setContractorName(cursor.getString(cursor.getColumnIndex("ContractorName")));
            			obj.setSubContractorID(cursor.getString(cursor.getColumnIndex("SubContractorID")));
            			obj.setSubContractorName(cursor.getString(cursor.getColumnIndex("SubContractorName")));
            			obj.setSubSubContractorID(cursor.getString(cursor.getColumnIndex("SUbSubContractorID")));
            			obj.setSubSubContractorName(cursor.getString(cursor.getColumnIndex("SubSubContractorName")));
            			obj.setContractorStatus(cursor.getString(cursor.getColumnIndex("ContractorStatus")));
            			obj.setContractorRemarks(cursor.getString(cursor.getColumnIndex("ContractorRemarks")));
            			obj.setContractorExpectedBeginDate(cursor.getString(cursor.getColumnIndex("ContractorExpectedBeginDate")));
            			obj.setContractorExpectedEndDate(cursor.getString(cursor.getColumnIndex("ContractorExpectedEndDate")));
            			obj.setContractorActualEndDate(cursor.getString(cursor.getColumnIndex("ContractorActualEndDate")));
            			obj.setContractorActualBeginDate(cursor.getString(cursor.getColumnIndex("ContractorActualBeginDate")));
            			String str=cursor.getString(cursor.getColumnIndex("PercentageCompleted"));
            			d=0.0;
            			if(str!=null && str.length()>0 ){
            				d=Double.parseDouble(str);
            			}
            			obj.setPercentageCompleted(d);
            			str=cursor.getString(cursor.getColumnIndex("ConCost"));
            			d=0.0;
            			if(str!=null && str.length()>0){
            				d=Double.parseDouble(str);
            			}
            			obj.setConCost(d);
            			d=0.0;
            			str=cursor.getString(cursor.getColumnIndex("ConManHours"));
            			if(str!=null && str.length()>0){
            				d=Double.parseDouble(str);
            			}
            			obj.setConManHours(d);
            			int I=0;
            			str=cursor.getString(cursor.getColumnIndex("ConNoOfResources"));
            			if(str!=null && str.length()>0){
            				I=Integer.parseInt(str);
            			}
            			obj.setConNoOfResources(I);
            			float F=0;
            			str=cursor.getString(cursor.getColumnIndex("XValue"));
            			if(str!=null && str.length()>0){
            				F=Float.parseFloat(str);
            			}
            			obj.setXValue(F);
            			F=0;
            			str=cursor.getString(cursor.getColumnIndex("YValue"));
            			if(str!=null && str.length()>0){
            				F=Float.parseFloat(str);
            			}
            			obj.setYValue(F);
    	        		arr=obj;
    	        		
    	        	
    	        }
    	        cursor.close();
    	        mydb.close();
    	}
    	catch(Exception e){
    		
    	}
    	return arr;
    }
	public SnagMasterDependency getSnagsDependencyNotSynced(int st){
		SnagMasterDependency arr=null;
    	try{
    		
    			String strQuery;
    			//if(st==0)
    				strQuery="Select * from SnagMasterDependency where IsSyncedToWeb='false'";
    			//else
    			//	strQuery="Select * from SnagMasterDependency where IsSyncedToWeb='false' and StatusForUpload='Modified'";
    			mydb=context.openOrCreateDatabase(DBNAME,MODE_PRIVATE,null);
    			Cursor cursor=mydb.rawQuery(strQuery, null);
    			
    			
    	        if(cursor.moveToFirst()){
    	        	
    	        		arr=new SnagMasterDependency();
    	        		SnagMasterDependency obj=new SnagMasterDependency();
    					obj.setDId(cursor.getString(cursor.getColumnIndex("DId")));
    					obj.setParentSnagId(cursor.getString(cursor.getColumnIndex("ParentSnagId")));
    					obj.setSnagId(cursor.getString(cursor.getColumnIndex("SnagId")));
    					obj.setJobType(cursor.getString(cursor.getColumnIndex("JobType")));
    					obj.setProjectId(cursor.getString(cursor.getColumnIndex("ProjectId")));
    					obj.setBuildingId(cursor.getString(cursor.getColumnIndex("BuildingId")));
    					obj.setFloorId(cursor.getString(cursor.getColumnIndex("FloorId")));
    					obj.setApartmentID(cursor.getString(cursor.getColumnIndex("ApartmentID")));
    					//obj.setDependencyLevel(cursor.getString(cursor.getColumnIndex("DependencyLevel")));
    	        		
    	        		
    	        		arr=obj;
    	        		
    	        	
    	        }
    	        cursor.close();
    	        mydb.close();
    	}
    	catch(Exception e){
    		
    	}
    	return arr;
    }
	public void updateSnagSynced(String ID){
		try{
			
    		
    			String strQuery;
    			strQuery="Update SnagMaster set IsSyncedToWeb='true' where ID='"+ID+"'";
    			mydb=context.openOrCreateDatabase(DBNAME,MODE_PRIVATE,null);
    			mydb.execSQL(strQuery);
    	        mydb.close();
		}
		catch(Exception e){
			Log.d("Error UpdateIsSyncedSnagMaster fmdb",""+e.getMessage());
		}
	}
	public void updateSnagDependencySynced(String ID){
		try{
			
    		
    			String strQuery;
    			strQuery="Update SnagMasterDependency set IsSyncedToWeb='true' where DId='"+ID+"'";
    			mydb=context.openOrCreateDatabase(DBNAME,MODE_PRIVATE,null);
    			mydb.execSQL(strQuery);
    	        mydb.close();
		}
		catch(Exception e){
			Log.d("Error UpdateIsSyncedSnagMaster fmdb",""+e.getMessage());
		}
	}
	//@@@@@@@@@@@@
	
	//@@@@@@@ Timestamp
	public TimeStampMaster getTimeStampForSequence(int TS){
		TimeStampMaster arr=null;
    	try{
    		
    			String strQuery;
    			
    				strQuery="Select * from TimeStampMaster where SequenceNo='"+TS+"'";
    			
    			mydb=context.openOrCreateDatabase(DBNAME,MODE_PRIVATE,null);
    			Cursor cursor=mydb.rawQuery(strQuery, null);
    			
    			
    	        if(cursor.moveToFirst()){
    	        	
    	        		arr=new TimeStampMaster();
    	        		arr.setTableName(cursor.getString(cursor.getColumnIndex("TableName")));
    	        		arr.setTimeStampValue(cursor.getString(cursor.getColumnIndex("TimeStampValue")));
    	        		arr.setCaption(cursor.getString(cursor.getColumnIndex("Caption")));
    	        		arr.setSequenceNo(Integer.parseInt(cursor.getString(cursor.getColumnIndex("SequenceNo")).toString()));
    	        		arr.setFromRowNo(Integer.parseInt(cursor.getString(cursor.getColumnIndex("RowNo")).toString()));
    	        	
    	        }
    	        cursor.close();
    	        mydb.close();
    	}
    	catch(Exception e){
    		
    	}
    	return arr;
	}
	public QCC[] getQCC(){
		QCC[] arr=null;
    	try{
    		
    			String strQuery;
    			
    				strQuery="Select * from QCC order by ID";
    			
    			mydb=context.openOrCreateDatabase(DBNAME,MODE_PRIVATE,null);
    			Cursor cursor=mydb.rawQuery(strQuery, null);
    			arr=new QCC[cursor.getCount()];
    			int count=0;
    	        if(cursor.moveToFirst()){
    	        	do{
    	        		QCC obj=new QCC();
    	        		
    	        		obj.setID(cursor.getString(cursor.getColumnIndex("ID")));
    	        		obj.setQCCStatus(cursor.getString(cursor.getColumnIndex("QCCStatus")));
    	        		obj.setDescription(cursor.getString(cursor.getColumnIndex("Description")));
    	        		//obj.setSequenceNo(Integer.parseInt(cursor.getString(cursor.getColumnIndex("SequenceNo")).toString()));
    	        		arr[count]=obj;
    	        		count++;
    	        	}
    	        	while(cursor.moveToNext());
    	        	
    	        }
    	        cursor.close();
    	        mydb.close();
    	}
    	catch(Exception e){
    		
    	}
    	return arr;
	}
	public Employee[] getEmployee(){
		Employee[] arr=null;
    	try{
    		
    			String strQuery;
    			
    				strQuery="Select * from Employee order by EmpName";
    			
    			mydb=context.openOrCreateDatabase(DBNAME,MODE_PRIVATE,null);
    			Cursor cursor=mydb.rawQuery(strQuery, null);
    			arr=new Employee[cursor.getCount()];
    			int count=0;
    	        if(cursor.moveToFirst()){
    	        	do{
    	        		Employee obj=new Employee();
    	        		
    	        		obj.setEmpCode(cursor.getString(cursor.getColumnIndex("EmpCode")));
    	        		obj.setSalutation(cursor.getString(cursor.getColumnIndex("Salutation")));
    	        		obj.setEmpName(cursor.getString(cursor.getColumnIndex("EmpName")));
    	        		obj.setEmpLastName(cursor.getString(cursor.getColumnIndex("EmpLastName")));
    	        		obj.setGender(cursor.getString(cursor.getColumnIndex("Gender")));
    	        		obj.setDesignation(cursor.getString(cursor.getColumnIndex("Designation")));
    	        		obj.setDepartment(cursor.getString(cursor.getColumnIndex("Department")));
    	        		arr[count]=obj;
    	        		count++;
    	        	}
    	        	while(cursor.moveToNext());
    	        	
    	        }
    	        cursor.close();
    	        mydb.close();
    	}
    	catch(Exception e){
    		
    	}
    	return arr;
	}
	public boolean insertORUpdateEmployee(Employee obj)
	{
		
		try{
			mydb=context.openOrCreateDatabase(DBNAME, Context.MODE_PRIVATE, null);
			String strQuery;
			strQuery="Select * from Employee where EmpCode='"+obj.getEmpCode()+"';";
			Cursor cursor=mydb.rawQuery(strQuery, null);
			if(cursor.moveToFirst()){
				strQuery="UPDATE Employee SET Salutation='"+obj.getSalutation()+"',EmpName='"+obj.getEmpName()+"',EmpLastName='"+obj.getEmpLastName()+"',Gender='"+obj.getGender()+"',Designation='"+obj.getDesignation()+"',Department='"+obj.getDepartment()+"'  where EmpCode='"+obj.getEmpCode()+"';";
			}
			else{
				strQuery = "Insert into Employee(EmpCode,Salutation,EmpName,EmpLastName,Gender,Designation,Department) values('"+obj.getEmpCode()+"','"+obj.getSalutation()+"','"+obj.getEmpName()+"','"+obj.getEmpLastName()+"','"+obj.getGender()+"','"+obj.getDesignation()+"','"+obj.getDepartment()+"');";
			}
	        mydb.execSQL(strQuery);
	        cursor.close();
	        mydb.close();
	        
		}
		catch(Exception e){
			
		}
		
		
		return true;
	}
	public LoginData[] getLoginData(String RegUserID){
		LoginData[] arr=null;
    	try{
    		
    			String strQuery;
    			
    				strQuery="Select * from LoginData where ID<>'"+RegUserID+"'";
    			
    			mydb=context.openOrCreateDatabase(DBNAME,MODE_PRIVATE,null);
    			Cursor cursor=mydb.rawQuery(strQuery, null);
    			arr=new LoginData[cursor.getCount()];
    			int count=0;
    	        if(cursor.moveToFirst()){
    	        	do{
    	        		LoginData obj=new LoginData();
    	        		obj.setID(cursor.getString(cursor.getColumnIndex("ID")));
    	        		obj.setFirstName(cursor.getString(cursor.getColumnIndex("FirstName")));
    	        		obj.setLastName(cursor.getString(cursor.getColumnIndex("LastName")));
    	        		obj.setUserName(cursor.getString(cursor.getColumnIndex("UserName")));
    	        		obj.setCreatedBy(cursor.getString(cursor.getColumnIndex("CreatedBy")));
    	        		obj.setCreatedDate(cursor.getString(cursor.getColumnIndex("CreatedDate")));
    	        		obj.setModifiedBy(cursor.getString(cursor.getColumnIndex("ModifiedBy")));
    	        		obj.setModifiedDate(cursor.getString(cursor.getColumnIndex("ModifiedDate")));
    	        		obj.setEmailID(cursor.getString(cursor.getColumnIndex("EmailID")));
    	        		obj.setDesignation(cursor.getString(cursor.getColumnIndex("Designation")));
    	        		arr[count]=obj;
    	        		count++;
    	        	}
    	        	while(cursor.moveToNext());
    	        	
    	        }
    	        cursor.close();
    	        mydb.close();
    	}
    	catch(Exception e){
    		
    	}
    	return arr;
	}
	public boolean insertORUpdateLoginData(LoginData obj)
	{
		
		try{
			mydb=context.openOrCreateDatabase(DBNAME, Context.MODE_PRIVATE, null);
			String strQuery;
			strQuery="Select * from LoginData where ID='"+obj.getID()+"';";
			Cursor cursor=mydb.rawQuery(strQuery, null);
			if(cursor.moveToFirst()){
				strQuery="UPDATE LoginData SET FirstName='"+obj.getFirstName()+"',LastName='"+obj.getLastName()+"',UserName='"+obj.getUserName()+"',CreatedBy='"+obj.getCreatedBy()+"',CreatedDate='"+obj.getCreatedDate()+"',ModifiedBy='"+obj.getModifiedBy()+"',ModifiedDate='"+obj.getModifiedDate()+"',EmailID='"+obj.getEmailID()+"',Designation='"+obj.getDesignation()+"'  where ID='"+obj.getID()+"';";
			}
			else{
				strQuery = "Insert into LoginData(ID,FirstName,LastName,UserName,CreatedBy,CreatedDate,ModifiedBy,ModifiedDate,EmailID,Designation) values('"+obj.getID()+"','"+obj.getFirstName()+"','"+obj.getLastName()+"','"+obj.getUserName()+"','"+obj.getCreatedBy()+"','"+obj.getCreatedDate()+"','"+obj.getModifiedBy()+"','"+obj.getModifiedDate()+"','"+obj.getEmailID()+"','"+obj.getDesignation()+"');";
			}
	        mydb.execSQL(strQuery);
	        cursor.close();
	        mydb.close();
	        
		}
		catch(Exception e){
			
		}
		
		
		return true;
	}
	public int getMaxTimeStamp(){
		int arr=0;
    	try{
    		
    			String strQuery;
    			
    				strQuery="Select count(*) as iCount from TimeStampMaster";
    			
    			mydb=context.openOrCreateDatabase(DBNAME,MODE_PRIVATE,null);
    			Cursor cursor=mydb.rawQuery(strQuery, null);
    			
    			
    	        if(cursor.moveToFirst()){
    	        	
    	        		
    	        		arr=Integer.parseInt(cursor.getString(0).toString());
    	        		
    	        		
    	        	
    	        }
    	        cursor.close();
    	        mydb.close();
    	}
    	catch(Exception e){
    		
    	}
    	return arr;
	}
	public void updateFromRowNoForSequence(TimeStampMaster TS){
		try{
			String strQuery;
			
			strQuery="Update TimeStampMaster set RowNo='"+TS.getFromRowNo()+"' where SequenceNo='"+TS.getSequenceNo()+"'";
			mydb=context.openOrCreateDatabase(DBNAME,MODE_PRIVATE,null);
			mydb.execSQL(strQuery);
	        mydb.close();
		}
		catch(Exception e){
			Log.d("Error=", ""+e.getMessage());
		}
	}
	public void updateTimeStampForSequence(TimeStampMaster TS){
		try{
			String strQuery;
			String dateString = TS.getTimeStampValue();
			
			SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss aa");
		    Date convertedDate = new Date();
		    try {
		        convertedDate = dateFormat.parse(dateString);
		        convertedDate.setSeconds(convertedDate.getSeconds()+1);
		        Log.d("Value date", ""+convertedDate +" " +dateFormat.format(convertedDate));
		    } catch (Exception e) {
		        // TODO Auto-generated catch block
		        e.printStackTrace();
		    }
		    System.out.println(convertedDate);
			strQuery="Update TimeStampMaster set TimeStampValue='"+dateFormat.format(convertedDate)+"',RowNo='0' where SequenceNo='"+TS.getSequenceNo()+"'";
				
			//strQuery="Update TimeStampMaster set TimeStampValue='"+TS.getTimeStampValue()+"' where SequenceNo='"+TS.getSequenceNo()+"'";
			mydb=context.openOrCreateDatabase(DBNAME,MODE_PRIVATE,null);
			mydb.execSQL(strQuery);
	        mydb.close();
		}
		catch(Exception e){
			Log.d("Error=", ""+e.getMessage());
		}
	}
	//@@@@@@@
	
	public void insertDependency(SnagMasterDependency[] arr){
		try{
			mydb=context.openOrCreateDatabase(DBNAME, Context.MODE_PRIVATE, null);
			String strQuery;
			for(int i=0;i<arr.length;i++){
			strQuery="Select DId from SnagMasterDependency where DId='"+arr[i].getDId()+"';";
			Cursor cursor=mydb.rawQuery(strQuery, null);
			if(cursor.moveToFirst()){
				strQuery="UPDATE SnagMasterDependency SET ParentSnagId='"+arr[i].getParentSnagId()+"',SnagId='"+arr[i].getSnagId()+"',JobType='"+arr[i].getJobType()+"',ProjectId='"+arr[i].getProjectId()+"',BuildingId='"+arr[i].getBuildingId()+"',FloorId='"+arr[i].getFloorId()+"',ApartmentID='"+arr[i].getApartmentID()+"',IsSyncedToWeb='"+arr[i].getIsDataSyncToWeb()+"' where DId='"+arr[i].getDId()+"';";
			}
			else{
	        strQuery = "Insert into SnagMasterDependency(DId,ParentSnagId,SnagId,JobType,ProjectId,BuildingId,FloorId,ApartmentID,IsSyncedToWeb) values('"+arr[i].getDId()+"','"+arr[i].getParentSnagId()+"','"+arr[i].getSnagId()+"','"+arr[i].getJobType()+"','"+arr[i].getProjectId()+"','"+arr[i].getBuildingId()+"','"+arr[i].getFloorId()+"','"+arr[i].getApartmentID()+"','"+arr[i].getIsDataSyncToWeb()+"');";
			}
	        mydb.execSQL(strQuery);
	        cursor.close();
			}
	        mydb.close();
		}
		catch(Exception e){
			Log.d("Error insertJobType=",""+e.getMessage());
		}
	}
	
	public  List<String[]> getSnagsByAsc(String prjName,String bldgName,String strSngType,String sngSts,String strInspct,String strChckBy,String strAllocByMe,String strBy,String sortColumn)
	{
		//SnagMaster arr[]=null;
		 List<String[]> addresses=null;
		try{
			mydb=context.openOrCreateDatabase(DBNAME,MODE_PRIVATE, null);
			String strQuery="";
			if(prjName.equalsIgnoreCase("All"))
			{
				prjName="%";
			}
			
			if(bldgName.equalsIgnoreCase("All"))
			{
				bldgName="%";
			}
			
			if(strSngType.equalsIgnoreCase("All"))
			{
				strSngType="%";
			}
		
			if(sngSts.equalsIgnoreCase("All"))
			{
				sngSts="%";
			}
			if(strInspct.equalsIgnoreCase("All"))
			{
				strInspct="%";
			}
			if(strChckBy.equalsIgnoreCase("All"))
			{
				strChckBy="%";
			}
			if(strAllocByMe.equalsIgnoreCase("All"))
			{
				//strAllocByMe="%";
				strAllocByMe= "AllocatedTo LIKE '%' or AllocatedTo isNull";
			}
			else
			{
				strAllocByMe="AllocatedTo Like '"+strAllocByMe+"'";
			}
			//query for contractor
			
			String qry="Select * from SnagMaster ";
			//
			if(strBy.equalsIgnoreCase("desc"))
			{
				if(strLoginType.equalsIgnoreCase("Contractor"))
				{
					strQuery = "Select ProjectName,BuildingName,Floor,Apartment,AptAreaName,SnagType,FaultType,SnagStatus,ReportDate,InspectorName,Cost from SnagMaster where ContractorID='"+RegUserID+"' Order by "+sortColumn+" desc";
				}
				else
				{
					if(sngSts.equalsIgnoreCase("Pending"))
					{
						//costTo,priorityLevel
						strQuery = "Select ProjectName,BuildingName,Floor,Apartment,AptAreaName,SnagType,FaultType,SnagStatus,ReportDate,InspectorName,Cost from SnagMaster where (ProjectID LIKE '"+prjName+"' and BuildingID LIKE '"+bldgName+"' and SnagType LIKE '"+strSngType+"' and SnagStatus LIKE '"+sngSts+"' and InspectorName LIKE '"+strInspct+"' and InspectorID LIKE '"+strChckBy+"' and ("+strAllocByMe+"))  Order by "+sortColumn+" desc";
					}
					else
					{
						//costTo,priorityLevel,Resolveddate,ReinpectdUnresolvedDate
						strQuery = "Select ProjectName,BuildingName,Floor,Apartment,AptAreaName,SnagType,FaultType,SnagStatus,ReportDate,InspectorName,Cost from SnagMaster where (ProjectID LIKE '"+prjName+"' and BuildingID LIKE '"+bldgName+"' and SnagType LIKE '"+strSngType+"' and SnagStatus LIKE '"+sngSts+"' and InspectorName LIKE '"+strInspct+"' and InspectorID LIKE '"+strChckBy+"' and ("+strAllocByMe+"))  Order by "+sortColumn+" desc";
					}
				}
					
					
			}
			else
			{
				if(strLoginType.equalsIgnoreCase("Contractor"))
				{
					strQuery = "Select ProjectName,BuildingName,Floor,Apartment,AptAreaName,SnagType,FaultType,SnagStatus,ReportDate,InspectorName,Cost from SnagMaster where ContractorID='"+RegUserID+"' Order by "+sortColumn+" asc";
				}
				else
				{
					if(sngSts.equalsIgnoreCase("Pending"))
					{
						//costTo,priorityLevel
						strQuery = "Select ProjectName,BuildingName,Floor,Apartment,AptAreaName,SnagType,FaultType,SnagStatus,ReportDate,InspectorName,Cost from SnagMaster where (ProjectID LIKE '"+prjName+"' and BuildingID LIKE '"+bldgName+"' and SnagType LIKE '"+strSngType+"' and SnagStatus LIKE '"+sngSts+"' and InspectorName LIKE '"+strInspct+"' and InspectorID LIKE '"+strChckBy+"' and ("+strAllocByMe+"))  Order by "+sortColumn+" asc";
					}
					else
					{
						strQuery = "Select ProjectName,BuildingName,Floor,Apartment,AptAreaName,SnagType,FaultType,SnagStatus,ReportDate,InspectorName,Cost from SnagMaster where (ProjectID LIKE '"+prjName+"' and BuildingID LIKE '"+bldgName+"' and SnagType LIKE '"+strSngType+"' and SnagStatus LIKE '"+sngSts+"' and InspectorName LIKE '"+strInspct+"' and InspectorID LIKE '"+strChckBy+"' and ("+strAllocByMe+"))  Order by "+sortColumn+" asc";
					}
				}
				
//				
//				
			}
			Cursor cursor=mydb.rawQuery(strQuery, null);
			int colCount=cursor.getColumnCount();
			String[] strColValue=cursor.getColumnNames();
	        addresses = new ArrayList<String[]>();
	        

	        int i=0;
	        if(cursor.moveToFirst()){
	        	do{
	        		
	        		String[] addressesArr  = new String[colCount];
	        		addressesArr[0]=cursor.getString(cursor.getColumnIndex("ProjectName"));
	        		addressesArr[1]=cursor.getString(cursor.getColumnIndex("BuildingName"));
	        		addressesArr[2]=cursor.getString(cursor.getColumnIndex("Floor"));
	        		addressesArr[3]=cursor.getString(cursor.getColumnIndex("Apartment"));
	        		addressesArr[4]=cursor.getString(cursor.getColumnIndex("AptAreaName"));
	        		addressesArr[5]=cursor.getString(cursor.getColumnIndex("SnagType"));
	        		addressesArr[6]=cursor.getString(cursor.getColumnIndex("FaultType"));
	        		addressesArr[7]=cursor.getString(cursor.getColumnIndex("SnagStatus"));
	        		//addressesArr[8]=cursor.getString(cursor.getColumnIndex("PriorityLevel"));
	        		addressesArr[8]=cursor.getString(cursor.getColumnIndex("ReportDate"));
	        		addressesArr[9]=cursor.getString(cursor.getColumnIndex("InspectorName"));
	        		if((sngSts.equalsIgnoreCase("Pending")))
	        		{
	        			addressesArr[10]=cursor.getString(cursor.getColumnIndex("Cost"));
		        		//addressesArr[12]=cursor.getString(cursor.getColumnIndex("CostTo"));
		        	
	        			
	        		}
	        		else
	        		{
	        			//addressesArr[11]=cursor.getString(cursor.getColumnIndex("ReInspectedUnresolvedDate"));
	        			//addressesArr[12]=cursor.getString(cursor.getColumnIndex("ResolveDate"));
	        			addressesArr[10]=cursor.getString(cursor.getColumnIndex("Cost"));
	        			//addressesArr[14]=cursor.getString(cursor.getColumnIndex("CostTo"));
	        		}	
	        		addresses.add(addressesArr);
	        		i++;
	        	}
	        	while(cursor.moveToNext());
	        	addresses.add(strColValue);
	        }
	        cursor.close();
	        mydb.close();
	        
		}
		catch(Exception e)
		{
			Log.d("Error getSnags=",""+e.getMessage());
		//	cursor.close();
	        mydb.close();
		}
		return addresses;
	}
	public BuildingMaster[] getBuildingsByprj(String ProjectID)
	{
		BuildingMaster arr[]=null;
		try{
			mydb=context.openOrCreateDatabase(DBNAME, Context.MODE_PRIVATE, null);
			String strQuery;
	        strQuery = "Select * from BuildingMaster where ProjectID='"+ProjectID+"' Order by ID";
	        Cursor cursor=mydb.rawQuery(strQuery, null);
	        arr=new BuildingMaster[cursor.getCount()];
	        int i=0;
	        if(cursor.moveToFirst()){
	        	do{
	        		BuildingMaster obj=new BuildingMaster();
	        		obj.setID(cursor.getString(cursor.getColumnIndex("ID")));
	        		obj.setBuildingName(cursor.getString(cursor.getColumnIndex("BuildingName")));
	        		obj.setNoOfElevators(cursor.getInt(cursor.getColumnIndex("NoOfElevators")));
	        		obj.setNoOfFloors(cursor.getInt(cursor.getColumnIndex("NoOfFloors")));
	        		obj.setProjectID(cursor.getString(cursor.getColumnIndex("ProjectID")));
	        		obj.setProjectName(cursor.getString(cursor.getColumnIndex("ProjectName")));
	        		obj.setImageName(cursor.getString(cursor.getColumnIndex("ImageName")));
	        		
	        		arr[i]=obj;
	        		
	        		i++;
	        	}
	        	while(cursor.moveToNext());
	        }
	        cursor.close();
	        mydb.close();
		}
		catch(Exception e){
			Log.d("Error=", ""+e.getMessage());
		}
		
		
		return arr;
	}
	
	public boolean insertORUpdateAttachmentDetails(AttachmentDetails obj)
	{
		
		try{
			mydb=context.openOrCreateDatabase(DBNAME, Context.MODE_PRIVATE, null);
			String strQuery;
			strQuery="Select * from AttachmentDetails where AttachmentID='"+obj.getAttachmentID()+"';";
			Cursor cursor=mydb.rawQuery(strQuery, null);
			if(cursor.moveToFirst()){
				strQuery="UPDATE AttachmentDetails SET ProjectID='"+obj.getProjectID()+"',BuildingID='"+obj.getBuildingID()+"',FloorID='"+obj.getFloorID()+"',ApartmentID='"+obj.getApartmentID()+"',AptAreaID='"+obj.getAptAreaID()+"',SnagID='"+obj.getSnagID()+"',AreaType='"+obj.getAreaType()+"',JobType='"+obj.getJobType()+"',FileName='"+obj.getFileName()+"',LocalFilePath='"+obj.getLocalFilePath()+"',CreatedBy='"+obj.getCreatedBy()+"',CreatedDate='"+obj.getCreatedDate()+"',ModifiedBy='"+obj.getModifiedBy()+"'ModifiedDate='"+obj.getModifiedDate()+"',IsUploadedToWeb='"+obj.getIsUploadedToWeb()+"',IsSyncedToWeb='"+obj.getIsSyncToWeb()+"'  where AttachmentID='"+obj.getAttachmentID()+"';";
			}
			else{
				strQuery = "Insert into AttachmentDetails(AttachmentID,ProjectID,BuildingID,FloorID,ApartmentID,AptAreaID,SnagID,AreaType,JobType,FileName,LocalFilePath,CreatedBy,CreatedDate,ModifiedBy,ModifiedDate,IsUploadedToWeb,IsSyncedToWeb) values('"+obj.getAttachmentID()+"','"+obj.getProjectID()+"','"+obj.getBuildingID()+"','"+obj.getFloorID()+"','"+obj.getApartmentID()+"','"+obj.getAptAreaID()+"','"+obj.getSnagID()+"','"+obj.getAreaType()+"','"+obj.getJobType()+"','"+obj.getFileName()+"','"+obj.getLocalFilePath()+"','"+obj.getCreatedBy()+"','"+obj.getCreatedDate()+"','"+obj.getModifiedBy()+"','"+obj.getModifiedDate()+"','"+obj.getIsUploadedToWeb()+"','"+obj.getIsSyncToWeb()+"');";
			}
	        mydb.execSQL(strQuery);
	        cursor.close();
	        mydb.close();
	        
		}
		catch(Exception e){
			
		}
		
		
		return true;
	}
	
	public AttachmentDetails getAttachmentDetailsNotSynced(){
		AttachmentDetails arr=null;
    	try{
    		
    		
    		
    			String strQuery;
    			
    				strQuery="Select * from AttachmentDetails where IsSyncedToWeb='false' or IsUploadedToWeb='false'";
    			
    			
    			mydb=context.openOrCreateDatabase(DBNAME,MODE_PRIVATE,null);
    			Cursor cursor=mydb.rawQuery(strQuery, null);
    			
    			
    	        if(cursor.moveToFirst()){
    	        		arr=new AttachmentDetails();
    	        		AttachmentDetails obj=new AttachmentDetails();
    	        		obj.setAttachmentID(cursor.getString(cursor.getColumnIndex("AttachmentID")));
    	        		obj.setProjectID(cursor.getString(cursor.getColumnIndex("ProjectID")));
    	        		obj.setBuildingID(cursor.getString(cursor.getColumnIndex("BuildingID")));
    	        		obj.setFloorID(cursor.getString(cursor.getColumnIndex("FloorID")));
    	        		obj.setApartmentID(cursor.getString(cursor.getColumnIndex("ApartmentID")));
    	        		obj.setAptAreaID(cursor.getString(cursor.getColumnIndex("AptAreaID")));
    	        		obj.setSnagID(cursor.getString(cursor.getColumnIndex("SnagID")));
    	        		obj.setAreaType(cursor.getString(cursor.getColumnIndex("AreaType")));
    	        		obj.setJobType(cursor.getString(cursor.getColumnIndex("JobType")));
    	        		obj.setFileName(cursor.getString(cursor.getColumnIndex("FileName")));
    	        		obj.setCreatedBy(cursor.getString(cursor.getColumnIndex("CreatedBy")));
    	        		obj.setCreatedDate(cursor.getString(cursor.getColumnIndex("CreatedDate")));
    	        		obj.setModifiedBy(cursor.getString(cursor.getColumnIndex("ModifiedBy")));
    	        		obj.setLocalFilePath(cursor.getString(cursor.getColumnIndex("LocalFilePath")));
    	        		String IsSyncToWeb=cursor.getString(cursor.getColumnIndex("IsSyncedToWeb"));
    	        		if(IsSyncToWeb.equalsIgnoreCase("true")){
    	        			obj.setIsSyncToWeb(true);	
    	        		}
    	        		else{
    	        			obj.setIsSyncToWeb(false);
    	        		}
    	        		String IsUploadedToWeb=cursor.getString(cursor.getColumnIndex("IsUploadedToWeb"));
    	        		if(IsUploadedToWeb.equalsIgnoreCase("true")){
    	        			obj.setIsUploadedToWeb(true);	
    	        		}
    	        		else{
    	        			obj.setIsUploadedToWeb(false);
    	        		}
    	        		arr=obj;
    	        		
    	        		
    	        	
    	        }
    	        cursor.close();
    	        mydb.close();
    	}
    	catch(Exception e){
    		
    	}
    	return arr;
    }
	public void UpdateIsSyncedAttachmentDetails(AttachmentDetails obj){
		try{
			mydb=context.openOrCreateDatabase(DBNAME, Context.MODE_PRIVATE, null);
			String strQuery;
	        strQuery = "UPDATE AttachmentDetails SET IsSyncedToWeb='"+obj.getIsSyncToWeb()+"',IsUploadedToWeb='"+obj.getIsUploadedToWeb()+"' WHERE (ID='"+obj.getAttachmentID()+"');";
	        mydb.execSQL(strQuery);
	        mydb.close();
		}
		catch(Exception e){
			Log.d("Error UpdateIsSyncedSnagMaster fmdb",""+e.getMessage());
		}
	}
	public InspectionDetails getInspectionDetailsNotSynced(int status){
		InspectionDetails arr=null;
    	try{
    		
    		
    		
    			String strQuery="";
    			if(status==0)
    				strQuery="Select * from InspectionDetails where IsSyncedToWeb='false' and StatusForUpload='Inserted'";
    			else
    				strQuery="Select * from InspectionDetails where IsSyncedToWeb='false' and StatusForUpload='Modified'";
    			
    			
    			mydb=context.openOrCreateDatabase(DBNAME,MODE_PRIVATE,null);
    			Cursor cursor=mydb.rawQuery(strQuery, null);
    			
    			
    	        if(cursor.moveToFirst()){
    	        		arr=new InspectionDetails();
    	        		InspectionDetails obj=new InspectionDetails();
    	        		obj.setInspectionDetailID(cursor.getString(cursor.getColumnIndex("InspectionDetailID")));
    	        		obj.setInspectionMasterID(cursor.getString(cursor.getColumnIndex("InspectionMasterID")));
    	        		obj.setChecklistID(cursor.getString(cursor.getColumnIndex("ChecklistID")));
    	        		obj.setChecklistDescription(cursor.getString(cursor.getColumnIndex("ChecklistDescription")));
    	        		obj.setChecklistEntry(cursor.getString(cursor.getColumnIndex("ChecklistEntry")));
    	        		obj.setCreatedBy(cursor.getString(cursor.getColumnIndex("CreatedBy")));
    	        		obj.setCreatedDate(cursor.getString(cursor.getColumnIndex("CreatedDate")));
    	        		obj.setModifiedBy(cursor.getString(cursor.getColumnIndex("ModifiedBy")));
    	        		obj.setModifiedDate(cursor.getString(cursor.getColumnIndex("ModifiedDate")));
    	        		obj.setEnteredOnMachineID(cursor.getString(cursor.getColumnIndex("EnteredOnMachineID")));
    	        		
    	        		arr=obj;
    	        		
    	        		
    	        	
    	        }
    	        cursor.close();
    	        mydb.close();
    	}
    	catch(Exception e){
    		
    	}
    	return arr;
    }
	public void UpdateIsSyncedInspectionDetails(InspectionDetails obj){
		try{
			mydb=context.openOrCreateDatabase(DBNAME, Context.MODE_PRIVATE, null);
			String strQuery;
	        strQuery = "UPDATE InspectionDetails SET IsSyncedToWeb='true' WHERE (InspectionDetailID='"+obj.getInspectionDetailID()+"' and InspectionMasterID='"+obj.getInspectionMasterID()+"');";
	        mydb.execSQL(strQuery);
	        mydb.close();
		}
		catch(Exception e){
			Log.d("Error UpdateIsSyncedSnagMaster fmdb",""+e.getMessage());
		}
	}
	
	///CHAT PAGE CODE///
	public void insertChat(Chat obj){
		try{
			mydb=context.openOrCreateDatabase(DBNAME, Context.MODE_PRIVATE, null);
			String strQuery;
			strQuery="insert into Chat(RegUserId,ChatWithID,ChatWithName,ChatSentBy,ChatCommet,ChatSyncDateTime,ChatID,ISChatSync,ChatCreatedDateTime,Unread) values('" + obj._regUserID + "','" + obj._chatWithID + "','" + obj._chatWithName + "','" + obj._chatSentBy + "','" + obj._chatCommet + "','" + obj._chatSyncDateTime + "','" + obj._chatID + "','" + obj._isChatSync + "','" + obj._chatCreatedDateTime + "','" + obj._unread +  "');";
			Cursor cursor=mydb.rawQuery(strQuery, null);
			mydb.execSQL(strQuery);
	        cursor.close();
	        mydb.close();
		}
		catch(Exception e){
			Log.d("Error Insert Chat = ",""+e.getMessage());
		}
	}
	
	public void updateChat(Chat obj)
	{
		
		try{
			mydb=context.openOrCreateDatabase(DBNAME, Context.MODE_PRIVATE, null);
			String strQuery;
			strQuery="update Chat set ISChatSync='true' where ChatID='" + obj._chatID + "'";
			Cursor cursor=mydb.rawQuery(strQuery, null);
			
	        mydb.execSQL(strQuery);
	        cursor.close();
	        mydb.close();
	        
		}
		catch(Exception e){
			
		}
		
			
		
	}
	
	public void updateRead(Chat obj)
	{
		
		try{
			mydb=context.openOrCreateDatabase(DBNAME, Context.MODE_PRIVATE, null);
			String strQuery;
			strQuery="update Chat set Unread='false' where ChatID='" + obj._chatID + "'";
			Cursor cursor=mydb.rawQuery(strQuery, null);
			
	        mydb.execSQL(strQuery);
	        cursor.close();
	        mydb.close();
	        
		}
		catch(Exception e){
			
		}
		
			
		
	}
	
	public void updateDelivered(Chat obj)
	{
		
		try{
			mydb=context.openOrCreateDatabase(DBNAME, Context.MODE_PRIVATE, null);
			String strQuery;
			strQuery="update Chat set ISChatDelivered=true where ChatID='" + obj._chatID + "'";
			Cursor cursor=mydb.rawQuery(strQuery, null);
			
	        mydb.execSQL(strQuery);
	        cursor.close();
	        mydb.close();
	        
		}
		catch(Exception e){
			
		}

	}
	
	public void updateIsRead(Chat obj)
	{
		
		try{
			mydb=context.openOrCreateDatabase(DBNAME, Context.MODE_PRIVATE, null);
			String strQuery;
			strQuery="update Chat set ISChatRead=true where ChatID='" + obj._chatID + "'";
			Cursor cursor=mydb.rawQuery(strQuery, null);
			
	        mydb.execSQL(strQuery);
	        cursor.close();
	        mydb.close();
	        
		}
		catch(Exception e){
			
		}

	}
	public Chat[] getChat(Chat obj)
	{
		//Chat obj=null;
		Chat arr[]=null;
		try{
			mydb=context.openOrCreateDatabase(DBNAME, Context.MODE_PRIVATE, null);
			String strQuery;
			strQuery="select * from Chat where (RegUserID='"+obj._regUserID+"' or RegUserID='"+obj._chatWithID+"') and (ChatWithID='"+obj._chatWithID+"'  or ChatWithID='"+obj._regUserID+"') order by ChatCreatedDateTime";
			Cursor cursor=mydb.rawQuery(strQuery, null);
			
			arr=new Chat[cursor.getCount()];
	        int i=0;
	        if(cursor.moveToFirst()){
	        	do{
	        		Chat obj2=new Chat();
	        		obj2._regUserID=(cursor.getString(cursor.getColumnIndex("RegUserId")));
	        		obj2._chatCommet=(cursor.getString(cursor.getColumnIndex("ChatCommet")));
	        		obj2._chatCreatedDateTime=(cursor.getString(cursor.getColumnIndex("ChatCreatedDateTime")));
	        		obj2._chatID=(cursor.getString(cursor.getColumnIndex("ChatID")));
	        		
	        		obj2._chatWithID=(cursor.getString(cursor.getColumnIndex("ChatWithID")));
	        		
	        		
	        		arr[i]=obj2;
	        		
	        		i++;
	        	}
	        	while(cursor.moveToNext());
	        }
	        cursor.close();
	        mydb.close();
	        
		}
		catch(Exception e){
			
		}
		return arr;
	}


/////Inspection Details Functions
	public InspectionDetails getInspectionDetails(String mainID,String ChecklistID)
	{
		InspectionDetails arr=null;
		try{
			mydb=context.openOrCreateDatabase(DBNAME, Context.MODE_PRIVATE, null);
			String strQuery;
	        strQuery = "Select * from InspectionDetails where InspectionMasterID='"+mainID+"' and ChecklistID='"+ChecklistID+"'";// where ProjectID='"+ProjectID+"' Order by ID";
	        Cursor cursor=mydb.rawQuery(strQuery, null);
	        
	       // int i=0;
	        if(cursor.moveToFirst()){
	        	//do{
	        		arr=new InspectionDetails();
	        		InspectionDetails obj=new InspectionDetails();
	        		obj.setInspectionDetailID(cursor.getString(cursor.getColumnIndex("InspectionDetailID")));
	        		obj.setInspectionMasterID(cursor.getString(cursor.getColumnIndex("InspectionMasterID")));
	        		obj.setChecklistID(cursor.getString(cursor.getColumnIndex("ChecklistID")));
	        		obj.setChecklistDescription(cursor.getString(cursor.getColumnIndex("ChecklistDescription")));
	        		obj.setChecklistEntry(cursor.getString(cursor.getColumnIndex("ChecklistEntry")));
	        		obj.setCreatedBy(cursor.getString(cursor.getColumnIndex("CreatedBy")));
	        		obj.setCreatedDate(cursor.getString(cursor.getColumnIndex("CreatedDate")));
	        		obj.setModifiedBy(cursor.getString(cursor.getColumnIndex("ModifiedBy")));
	        		obj.setModifiedDate(cursor.getString(cursor.getColumnIndex("ModifiedDate")));
	        		obj.setEnteredOnMachineID(cursor.getString(cursor.getColumnIndex("EnteredOnMachineID")));
	        		arr=obj;
	        		
	        		//i++;
	        	//}
	        	//while(cursor.moveToNext());
	        }
	        cursor.close();
	        mydb.close();
		}
		catch(Exception e){
			Log.d("Error=", ""+e.getMessage());
		}
		
		
		return arr;
	}
	public InspectionDetails[] getInspectionDetailsForCheck(String mainID)
	{
		InspectionDetails[] arr=null;
		try{
			mydb=context.openOrCreateDatabase(DBNAME, Context.MODE_PRIVATE, null);
			String strQuery;
	        strQuery = "Select * from InspectionDetails where InspectionMasterID='"+mainID+"'";// where ProjectID='"+ProjectID+"' Order by ID";
	        Cursor cursor=mydb.rawQuery(strQuery, null);
	        arr=new InspectionDetails[cursor.getCount()];
	        int i=0;
	        if(cursor.moveToFirst()){
	        	do{
	        		
	        		InspectionDetails obj=new InspectionDetails();
	        		obj.setInspectionDetailID(cursor.getString(cursor.getColumnIndex("InspectionDetailID")));
	        		obj.setInspectionMasterID(cursor.getString(cursor.getColumnIndex("InspectionMasterID")));
	        		obj.setChecklistID(cursor.getString(cursor.getColumnIndex("ChecklistID")));
	        		obj.setChecklistDescription(cursor.getString(cursor.getColumnIndex("ChecklistDescription")));
	        		obj.setChecklistEntry(cursor.getString(cursor.getColumnIndex("ChecklistEntry")));
	        		obj.setCreatedBy(cursor.getString(cursor.getColumnIndex("CreatedBy")));
	        		obj.setCreatedDate(cursor.getString(cursor.getColumnIndex("CreatedDate")));
	        		obj.setModifiedBy(cursor.getString(cursor.getColumnIndex("ModifiedBy")));
	        		obj.setModifiedDate(cursor.getString(cursor.getColumnIndex("ModifiedDate")));
	        		obj.setEnteredOnMachineID(cursor.getString(cursor.getColumnIndex("EnteredOnMachineID")));
	        		arr[i]=obj;
	        		
	        		i++;
	        	}
	        	while(cursor.moveToNext());
	        }
	        cursor.close();
	        mydb.close();
		}
		catch(Exception e){
			Log.d("Error=", ""+e.getMessage());
		}
		
		
		return arr;
	}
	public InspectionList[] getInspectionListByAreaTypeAndjobType(String AreaType,String JobType, String AreaName)
	{
		InspectionList arr[]=null;
		try{
			mydb=context.openOrCreateDatabase(DBNAME, Context.MODE_PRIVATE, null);
			String strQuery;
	        strQuery = "Select * from InspectionList where AreaType='"+AreaType+"' and JobType='"+JobType+"' and AreaName='"+AreaName+"' Order by ChecklistDescription";
	        Cursor cursor=mydb.rawQuery(strQuery, null);
	        arr=new InspectionList[cursor.getCount()];
	        int i=0;
	        if(cursor.moveToFirst()){
	        	do{
	        		InspectionList obj=new InspectionList();
	        		obj.setID(cursor.getString(cursor.getColumnIndex("ID")));
	        		obj.setChecklistDescription(cursor.getString(cursor.getColumnIndex("ChecklistDescription")));
	        		obj.setAreaID(cursor.getString(cursor.getColumnIndex("AreaID")));
	        		obj.setAreaType(cursor.getString(cursor.getColumnIndex("AreaType")));
	        		obj.setJobType(cursor.getString(cursor.getColumnIndex("JobType")));
	        		obj.setAreaName(cursor.getString(cursor.getColumnIndex("AreaName")));
	        		obj.setCreatedBy(cursor.getString(cursor.getColumnIndex("CreatedBy")));
	        		obj.setCreatedDate(cursor.getString(cursor.getColumnIndex("CreatedDate")));
	        		obj.setModifiedBy(cursor.getString(cursor.getColumnIndex("ModifiedBy")));
	        		obj.setModifiedDate(cursor.getString(cursor.getColumnIndex("ModifiedDate")));
	        		obj.setEnteredOnMachineID(cursor.getString(cursor.getColumnIndex("EnteredOnMachineID")));
	        		arr[i]=obj;
	        		
	        		i++;
	        	}
	        	while(cursor.moveToNext());
	        }
	        cursor.close();
	        mydb.close();
		}
		catch(Exception e){
			Log.d("Error=", ""+e.getMessage());
		}
		
		
		return arr;
	}
	public String[] getAreas(String AreaType)
	{
		String arr[]=null;
		try{
			mydb=context.openOrCreateDatabase(DBNAME, Context.MODE_PRIVATE, null);
			String strQuery;
	        strQuery = "Select distinct AreaName from InspectionList where AreaType='"+AreaType+"' Order by ChecklistDescription";
	        Cursor cursor=mydb.rawQuery(strQuery, null);
	        arr=new String[cursor.getCount()];
	        int i=0;
	        if(cursor.moveToFirst()){
	        	do{
	        		InspectionList obj=new InspectionList();
	        		
	        		obj.setAreaName(cursor.getString(cursor.getColumnIndex("AreaName")));
	        		
	        		arr[i]=obj.getAreaName();
	        		
	        		i++;
	        	}
	        	while(cursor.moveToNext());
	        }
	        cursor.close();
	        mydb.close();
		}
		catch(Exception e){
			Log.d("Error=", ""+e.getMessage());
		}
		
		
		return arr;
	}
	public String[] getJobTypes(String AreaType)
	{
		String arr[]=null;
		try{
			mydb=context.openOrCreateDatabase(DBNAME, Context.MODE_PRIVATE, null);
			String strQuery;
	        strQuery = "Select distinct JobType from InspectionList where AreaType='"+AreaType+"' Order by ChecklistDescription";
	        Cursor cursor=mydb.rawQuery(strQuery, null);
	        arr=new String[cursor.getCount()];
	        int i=0;
	        if(cursor.moveToFirst()){
	        	do{
	        		InspectionList obj=new InspectionList();
	        		
	        		obj.setJobType(cursor.getString(cursor.getColumnIndex("JobType")));
	        		
	        		arr[i]=obj.getJobType();
	        		
	        		i++;
	        	}
	        	while(cursor.moveToNext());
	        }
	        cursor.close();
	        mydb.close();
		}
		catch(Exception e){
			Log.d("Error=", ""+e.getMessage());
		}
		
		
		return arr;
	}
	
	public void insertOrUpdateInspectionDetails(InspectionDetails obj){
		try{
			mydb=context.openOrCreateDatabase(DBNAME, Context.MODE_PRIVATE, null);
			String strQuery;
			strQuery="Select * from InspectionDetails where InspectionDetailID='"+obj.getInspectionDetailID()+"' and InspectionMasterID='"+obj.getInspectionMasterID()+"';";
			Cursor cursor=mydb.rawQuery(strQuery, null);
			if(cursor.moveToFirst()){
				strQuery="UPDATE InspectionDetails SET ChecklistID='"+obj.getChecklistID()+"',ChecklistDescription='"+obj.getChecklistDescription()+"',ChecklistEntry='"+obj.getChecklistEntry()+"',CreatedBy='"+obj.getCreatedBy()+"',CreatedDate='"+obj.getCreatedDate()+"',ModifiedBy='"+obj.getModifiedBy()+"',ModifiedDate='"+obj.getModifiedDate()+"',EnteredOnMachineID='"+obj.getEnteredOnMachineID()+"',IsSyncedToWeb='"+obj.getIsSyncedToWeb()+"',StatusForUpload='"+obj.getStatusForUpload()+"'  where InspectionDetailID='"+obj.getInspectionDetailID()+"' and InspectionMasterID='"+obj.getInspectionMasterID()+"';";
			}
			else{
				strQuery = "Insert into InspectionDetails(InspectionDetailID,InspectionMasterID,ChecklistID,ChecklistDescription,ChecklistEntry,CreatedBy,CreatedDate,ModifiedBy,ModifiedDate,EnteredOnMachineID,IsSyncedToWeb,StatusForUpload) values('"+obj.getInspectionDetailID()+"','"+obj.getInspectionMasterID()+"','"+obj.getChecklistID()+"','"+obj.getChecklistDescription()+"','"+obj.getChecklistEntry()+"','"+obj.getCreatedBy()+"','"+obj.getCreatedDate()+"','"+obj.getModifiedBy()+"','"+obj.getModifiedDate()+"','"+obj.getEnteredOnMachineID()+"','"+obj.getIsSyncedToWeb()+"','"+obj.getStatusForUpload()+"');";
			}
	        mydb.execSQL(strQuery);
	        cursor.close();
	        mydb.close();
		}
		catch(Exception e){
			
		}
	}
	
	public InspectionMaster getInspectionMaster(String prjID,String bldgID,String flrID,String AptID,String AreaType,String JobType)
	{
		InspectionMaster arr=null;
		try{
			mydb=context.openOrCreateDatabase(DBNAME, Context.MODE_PRIVATE, null);
			String strQuery;
	        strQuery = "Select * from InspectionMaster where ProjectID='"+prjID+"' and BuildingID='"+bldgID+"' and FloorID='"+flrID+"' and ApartmentID='"+AptID+"' and AreaType='"+AreaType+"' and JobType='"+JobType+"'";// where ProjectID='"+ProjectID+"' Order by ID";
	        Cursor cursor=mydb.rawQuery(strQuery, null);
	        
	        int i=0;
	        if(cursor.moveToFirst()){
	        	//do{
	        		arr=new InspectionMaster();
	        		InspectionMaster obj=new InspectionMaster();
	        		obj.setID(cursor.getString(cursor.getColumnIndex("ID")));
	        		obj.setProjectID(cursor.getString(cursor.getColumnIndex("ProjectID")));
	        		obj.setBuildingID(cursor.getString(cursor.getColumnIndex("BuildingID")));
	        		obj.setFloorID(cursor.getString(cursor.getColumnIndex("FloorID")));
	        		obj.setApartmentID(cursor.getString(cursor.getColumnIndex("ApartmentID")));
	        		obj.setAptAreaID(cursor.getString(cursor.getColumnIndex("AptAreaID")));
	        		obj.setAreaType(cursor.getString(cursor.getColumnIndex("AreaType")));
	        		obj.setJobType(cursor.getString(cursor.getColumnIndex("JobType")));
	        		obj.setCreatedBy(cursor.getString(cursor.getColumnIndex("CreatedBy")));
	        		obj.setCreatedDate(cursor.getString(cursor.getColumnIndex("CreatedDate")));
	        		obj.setModifiedBy(cursor.getString(cursor.getColumnIndex("ModifiedBy")));
	        		obj.setModifiedDate(cursor.getString(cursor.getColumnIndex("ModifiedDate")));
	        		obj.setEnteredOnMachineID(cursor.getString(cursor.getColumnIndex("EnteredOnMachineID")));
	        		arr=obj;
	        		
	        		i++;
	        	//}
	        	//while(cursor.moveToNext());
	        }
	        cursor.close();
	        mydb.close();
		}
		catch(Exception e){
			Log.d("Error=", ""+e.getMessage());
		}
		
		
		return arr;
	}
	public JobType[] getJobTypeAccordingToInspectionMaster(String prjID,String bldgID,String flrID,String AptID,String AreaType)
	{
		InspectionMaster arr[]=null;
		JobType arrJob[]=null;
		ArrayList<JobType> arrJobP=null;
		try{
			mydb=context.openOrCreateDatabase(DBNAME, Context.MODE_PRIVATE, null);
			String strQuery;
	        strQuery = "Select * from InspectionMaster where ProjectID='"+prjID+"' and BuildingID='"+bldgID+"' and FloorID='"+flrID+"' and ApartmentID='"+AptID+"' and AreaType='"+AreaType+"'";
	        Cursor cursor=mydb.rawQuery(strQuery, null);
	        arr=new InspectionMaster[cursor.getCount()];
	        int i=0;
	        if(cursor.moveToFirst()){
	        	do{
	        		
	        		InspectionMaster obj=new InspectionMaster();
//	        		obj.setID(cursor.getString(cursor.getColumnIndex("ID")));
//	        		obj.setProjectID(cursor.getString(cursor.getColumnIndex("ProjectID")));
//	        		obj.setBuildingID(cursor.getString(cursor.getColumnIndex("BuildingID")));
//	        		obj.setFloorID(cursor.getString(cursor.getColumnIndex("FloorID")));
//	        		obj.setApartmentID(cursor.getString(cursor.getColumnIndex("ApartmentID")));
//	        		obj.setAptAreaID(cursor.getString(cursor.getColumnIndex("AptAreaID")));
//	        		obj.setAreaType(cursor.getString(cursor.getColumnIndex("AreaType")));
	        		obj.setJobType(cursor.getString(cursor.getColumnIndex("JobType")));
//	        		obj.setCreatedBy(cursor.getString(cursor.getColumnIndex("CreatedBy")));
//	        		obj.setCreatedDate(cursor.getString(cursor.getColumnIndex("CreatedDate")));
//	        		obj.setModifiedBy(cursor.getString(cursor.getColumnIndex("ModifiedBy")));
//	        		obj.setModifiedDate(cursor.getString(cursor.getColumnIndex("ModifiedDate")));
//	        		obj.setEnteredOnMachineID(cursor.getString(cursor.getColumnIndex("EnteredOnMachineID")));
	        		double d=0.0;
	        		String str=cursor.getString(cursor.getColumnIndex("PercentageComplete"))==null?"":cursor.getString(cursor.getColumnIndex("PercentageComplete"));
	        		if(str!=null && str.length()>0){
	        			d=Double.parseDouble(str);
	        		}
	        		obj.setPercentageComplete(d);
	        		arr[i]=obj;
	        		
	        		i++;
	        	}
	        	while(cursor.moveToNext());
	        }
	        //cursor.close();
	        if(arr!=null && arr.length>0){
	        	strQuery="Select * from JobType where ";
	        	for(int k=0;k<arr.length;k++){
	        		String temp="JobType='"+arr[k].getJobType()+"' ";
	        		strQuery=strQuery.concat(temp);
	        		if(k<=arr.length-2)
	        			strQuery=strQuery.concat(" or ");
	        		else
	        			strQuery=strQuery.concat(" order by SeqNo;");
	        			
	        	}
	        	cursor=mydb.rawQuery(strQuery, null);
	        	//arrJob=new JobType[cursor.getCount()];
	        	i=0;
	        	if(cursor.moveToFirst()){
	        		do{
	        			JobType obj=new JobType();
	        			obj.setID(cursor.getString(cursor.getColumnIndex("ID")));
	        			obj.setJobType(cursor.getString(cursor.getColumnIndex("JobType")));
	        			obj.setJobDetails(cursor.getString(cursor.getColumnIndex("JobDetails")));
	        			obj.setParentID(cursor.getString(cursor.getColumnIndex("ParentID")));
	        			obj.setParentJob(cursor.getString(cursor.getColumnIndex("ParentJob")));
	        			obj.setSeqNo(cursor.getString(cursor.getColumnIndex("SeqNo")));
//	        			obj.setID(cursor.getString(cursor.getColumnIndex("ImageName")));
//	        			obj.setID(cursor.getString(cursor.getColumnIndex("CreatedBy")));
//	        			obj.setID(cursor.getString(cursor.getColumnIndex("CreatedDate")));
//	        			obj.setID(cursor.getString(cursor.getColumnIndex("ModifiedBy")));
//	        			obj.setModifiedDate(cursor.getString(cursor.getColumnIndex("ModifiedDate")));
	        			boolean isCompleted=false;
	        			if(obj.getParentID().equals("-1"))
	        				isCompleted=true;
	        			else {
	        				for(int j=0;j<arr.length;j++){
	        					if(obj.getParentJob().equalsIgnoreCase(arr[j].getJobType())){
	        						if(arr[j].getPercentageComplete()==100.0)
	        							isCompleted=true;
	        						else
	        							isCompleted=false;
	        						break;
	        					}
	        				}
	        			}
	        			if(isCompleted){
	        				if(arrJobP==null || arrJobP.size()==0)
	        					arrJobP=new ArrayList<JobType>();
	        				//arrJob[i]=obj;
	        				arrJobP.add(obj);
	        				i++;
	        			}
	        		}
	        		while(cursor.moveToNext());
	        	}
	        }
	        cursor.close();
	        mydb.close();
		}
		catch(Exception e){
			Log.d("Error=", ""+e.getMessage());
		}
		if(arrJobP!=null && arrJobP.size()>0)
			arrJob=new JobType[arrJobP.size()];
			for(int i=0;i<arrJobP.size();i++)
				arrJob[i]=arrJobP.get(i);
		
		return arrJob;
	}
	public ApartmentAreaType[] getApartmentAreaType()
	{
		ApartmentAreaType arr[]=null;
		try{
			mydb=context.openOrCreateDatabase(DBNAME, Context.MODE_PRIVATE, null);
			String strQuery;
	        strQuery = "Select * from ApartmentAreaType";// where ProjectID='"+ProjectID+"' Order by ID";
	        Cursor cursor=mydb.rawQuery(strQuery, null);
	        arr=new ApartmentAreaType[cursor.getCount()];
	        int i=0;
	        if(cursor.moveToFirst()){
	        	do{
	        		ApartmentAreaType obj=new ApartmentAreaType();
	        		obj.setID(cursor.getString(cursor.getColumnIndex("ID")));
	        		obj.setApartmentAreaType(cursor.getString(cursor.getColumnIndex("ApartmentAreaType")));
	        		obj.setAreaType(cursor.getString(cursor.getColumnIndex("AreaType")));
	        		obj.setCreatedBy(cursor.getString(cursor.getColumnIndex("CreatedBy")));
	        		obj.setCreatedDate(cursor.getString(cursor.getColumnIndex("CreatedDate")));
	        		obj.setModifiedBy(cursor.getString(cursor.getColumnIndex("ModifiedBy")));
	        		obj.setModifiedDate(cursor.getString(cursor.getColumnIndex("ModifiedDate")));
	        		obj.setEnteredOnMachineID(cursor.getString(cursor.getColumnIndex("EnteredOnMachineID")));
	        		arr[i]=obj;
	        		
	        		i++;
	        	}
	        	while(cursor.moveToNext());
	        }
	        cursor.close();
	        mydb.close();
		}
		catch(Exception e){
			Log.d("Error=", ""+e.getMessage());
		}
		
		
		return arr;
	}
	public void insertOrUpdateInspectionList(InspectionList obj){
		try{
			mydb=context.openOrCreateDatabase(DBNAME, Context.MODE_PRIVATE, null);
			String strQuery;
			strQuery="Select * from InspectionList where ID='"+obj.getID()+"';";
			Cursor cursor=mydb.rawQuery(strQuery, null);
			if(cursor.moveToFirst()){
				strQuery="UPDATE InspectionList SET ChecklistDescription='"+obj.getChecklistDescription()+"',AreaID='"+obj.getAreaID()+"',AreaType='"+obj.getAreaType()+"',AreaName='"+obj.getAreaName()+"',JobType='"+obj.getJobType()+"',CreatedBy='"+obj.getCreatedBy()+"',CreatedDate='"+obj.getCreatedDate()+"',ModifiedBy='"+obj.getModifiedBy()+"',ModifiedDate='"+obj.getModifiedDate()+"',EnteredOnMachineID='"+obj.getEnteredOnMachineID()+"'  where ID='"+obj.getID()+"';";
			}
			else{
				strQuery = "Insert into InspectionList(ID,ChecklistDescription,AreaID,AreaType,AreaName,JobType,CreatedBy,CreatedDate,ModifiedBy,ModifiedDate,EnteredOnMachineID) values('"+obj.getID()+"','"+obj.getChecklistDescription()+"','"+obj.getAreaID()+"','"+obj.getAreaType()+"','"+obj.getAreaName()+"','"+obj.getJobType()+"','"+obj.getCreatedBy()+"','"+obj.getCreatedDate()+"','"+obj.getModifiedBy()+"','"+obj.getModifiedDate()+"','"+obj.getEnteredOnMachineID()+"');";
			}
	        mydb.execSQL(strQuery);
	        cursor.close();
	        mydb.close();
		}
		catch(Exception e){
			
		}
	}
	//Close
	public String[] getAreaType(String strAptAreaType)
	{
		String result[]=null;
		try
		{
			mydb=context.openOrCreateDatabase(DBNAME, Context.MODE_PRIVATE, null);
			String strQuery;
			strQuery="Select * from ApartmentAreaType where ApartmentAreaType='"+strAptAreaType+"';";
			Cursor cursor=mydb.rawQuery(strQuery, null);
			if(cursor.moveToFirst())
			{
				result=new String[2];
				result[0]=cursor.getString(cursor.getColumnIndex("AreaType"));
				result[1]=cursor.getString(cursor.getColumnIndex("ID"));
			}
			cursor.close();
			mydb.close();
		}
		catch (Exception e)
		{
			Log.d("Exception fmdb getAreaType", ""+e.getMessage());
		}
		return result;
	}
	
	public void InsertOrUpdateEmployee(Employee obj)
	{
		
		try{
			
			mydb=context.openOrCreateDatabase(DBNAME, Context.MODE_PRIVATE, null);
			String strQuery;
			strQuery="Select * from Employee where EmpCode='"+obj.getEmpCode()+"';";
			Cursor cursor=mydb.rawQuery(strQuery, null);
			if(cursor.moveToFirst()){
				strQuery="UPDATE Employee SET Salution='"+obj.getSalutation()+"',EmpName='"+obj.getEmpName()+"',EmpLastName='"+obj.getEmpLastName()+"',Gender='"+obj.getGender()+"',Designation='"+obj.getDesignation()+"',Department='"+obj.getDepartment()+"'  where EmpCode='"+obj.getEmpCode()+"';";
			}
			else{
				strQuery = "Insert into Employee(EmpCode,Salution,EmpName,EmpLastName,Gender,Designation,Department) values('"+obj.getEmpCode()+"','"+obj.getSalutation()+"','"+obj.getEmpName()+"','"+obj.getEmpLastName()+"','"+obj.getGender()+"','"+obj.getDesignation()+"','"+obj.getDepartment()+"');";
			}
	        mydb.execSQL(strQuery);
	        cursor.close();
	        mydb.close();
		}
		catch(Exception e){
			
		}

	}
	
	
	public Employee[] getEmployees()
	{
		
		Employee arr[]=null;
		try{
			mydb=context.openOrCreateDatabase(DBNAME, Context.MODE_PRIVATE, null);
			String strQuery;
	        strQuery = "Select * from Employee";
	        Cursor cursor=mydb.rawQuery(strQuery, null);
	        arr=new Employee[cursor.getCount()];
	        int i=0;
	        if(cursor.moveToFirst()){
	        	do{
	        		Employee obj=new Employee();
	        		obj.setEmpCode(cursor.getString(cursor.getColumnIndex("Empcode")));
	        		obj.setSalutation(cursor.getString(cursor.getColumnIndex("Salution")));
	        		obj.setEmpName(cursor.getString(cursor.getColumnIndex("EmpName")));
	        		obj.setEmpLastName(cursor.getString(cursor.getColumnIndex("EmpLastName")));
	        		obj.setGender(cursor.getString(cursor.getColumnIndex("Gender")));
	        		obj.setDesignation(cursor.getString(cursor.getColumnIndex("Designation")));
	        		obj.setDepartment(cursor.getString(cursor.getColumnIndex("Department")));
	        		
	        		arr[i]=obj;
	        		
	        		i++;
	        	}
	        	while(cursor.moveToNext());
	        }
	        cursor.close();
	        mydb.close();
		}
		catch(Exception e){
			Log.d("Error=", ""+e.getMessage());
		}
		
		
		return arr;
	}
	
	public boolean insertORUpdateInspectionMaster(InspectionMaster obj)
	{
		
		try{
			mydb=context.openOrCreateDatabase(DBNAME, Context.MODE_PRIVATE, null);
			String strQuery;
			strQuery="Select * from InspectionMaster where ID='"+obj.getID()+"';";
			Cursor cursor=mydb.rawQuery(strQuery, null);
			if(cursor.moveToFirst()){
				strQuery="UPDATE InspectionMaster SET ProjectID='"+obj.getProjectID()+"',BuildingID='"+obj.getBuildingID()+"',FloorID='"+obj.getFloorID()+"',ApartmentID='"+obj.getApartmentID()+"',AptAreaID='"+obj.getAptAreaID()+"',JobType='"+obj.getJobType()+"',AreaType='"+obj.getAreaType()+"',CreatedBy='"+obj.getCreatedBy()+"',CreatedDate='"+obj.getCreatedDate()+"',ModifiedBy='"+obj.getModifiedBy()+"',ModifiedDate='"+obj.getModifiedDate()+"',EnteredOnMachineID='"+obj.getEnteredOnMachineID()+"',PercentageComplete='"+obj.getPercentageComplete()+"'  where ID='"+obj.getID()+"';";
			}
			else{
				strQuery = "Insert into InspectionMaster(ID,ProjectID,BuildingID,FloorID,ApartmentID,AptAreaID,JobType,AreaType,CreatedBy,CreatedDate,ModifiedBy,ModifiedDate,EnteredOnMachineID,PercentageComplete) values('"+obj.getID()+"','"+obj.getProjectID()+"','"+obj.getBuildingID()+"','"+obj.getFloorID()+"','"+obj.getApartmentID()+"','"+obj.getAptAreaID()+"','"+obj.getJobType()+"','"+obj.getAreaType()+"','"+obj.getCreatedBy()+"','"+obj.getCreatedDate()+"','"+obj.getModifiedBy()+"','"+obj.getModifiedDate()+"','"+obj.getEnteredOnMachineID()+"','"+obj.getPercentageComplete()+"');";
			}
	        mydb.execSQL(strQuery);
	        cursor.close();
	        mydb.close();
	        
		}
		catch(Exception e){
			Log.d("Error insertORUpdateInspectionMaster=", ""+e.getMessage());
		}
		
		
		return true;
	}
	public SnagChecklistMaster[] getSnagChecklistMasterForType(String JobType,String FaultType)
	{
		SnagChecklistMaster arr[]=null;
		try{
			mydb=context.openOrCreateDatabase(DBNAME, Context.MODE_PRIVATE, null);
			String strQuery;
	        strQuery = "Select * from SnagChecklistMaster where JobType='"+JobType+"' and FaultType='"+FaultType+"'";
	        Cursor cursor=mydb.rawQuery(strQuery, null);
	        arr=new SnagChecklistMaster[cursor.getCount()];
	        int i=0;
	        if(cursor.moveToFirst()){
	        	do{
	        		SnagChecklistMaster obj=new SnagChecklistMaster();
	        		obj.setID(cursor.getString(cursor.getColumnIndex("ID")));
	        		obj.setJobType(cursor.getString(cursor.getColumnIndex("JobType")));
	        		obj.setFaultType(cursor.getString(cursor.getColumnIndex("FaultType")));
	        		obj.setChecklistDescription(cursor.getString(cursor.getColumnIndex("ChecklistDescription")));
	        		obj.setCreatedBy(cursor.getString(cursor.getColumnIndex("CreatedBy")));
	        		obj.setCreatedDate(cursor.getString(cursor.getColumnIndex("CreatedDate")));
	        		obj.setModifiedBy(cursor.getString(cursor.getColumnIndex("ModifiedBy")));
	        		obj.setModifiedDate(cursor.getString(cursor.getColumnIndex("ModifiedDate")));
	        		obj.setEnteredOnMachineID(cursor.getString(cursor.getColumnIndex("EnteredOnMachineID")));
	        		String dstr=cursor.getString(cursor.getColumnIndex("PercentageComplete"));
	        		double d=0.0;
	        		if(dstr!=null && dstr.length()>0)
	        			d=Double.parseDouble(dstr);
	        		obj.setPercentageComplete(d);
	        		arr[i]=obj;
	        		
	        		i++;
	        	}
	        	while(cursor.moveToNext());
	        }
	        cursor.close();
	        mydb.close();
		}
		catch(Exception e){
			Log.d("Error=", ""+e.getMessage());
		}
		
		
		return arr;
	}
	
	public boolean insertORUpdateSnagChecklistEntry(SnagChecklistEntries obj)
	{
		
		try{
			mydb=context.openOrCreateDatabase(DBNAME, Context.MODE_PRIVATE, null);
			String strQuery;
			strQuery="Select * from SnagChecklistEntries where ID='"+obj.ID+"';";
			Cursor cursor=mydb.rawQuery(strQuery, null);
			if(cursor.moveToFirst()){
				strQuery="UPDATE SnagChecklistEntries SET ProjectID='"+obj.ProjectID+"',BuildingID='"+obj.BuildingID+"',FloorID='"+obj.FloorID+"',ApartmentID='"+obj.ApartmentID+"',AptAreaID='"+obj.AptAreaID+"',SnagID='"+obj.SnagID+"',ChecklistID='"+obj.ChecklistID+"',CheckListGroup='"+obj.CheckListGroup+"',ChecklistDescription='"+obj.ChecklistDescription+"',ChecklistEntry='"+obj.CheckListEntry+"',CreatedBy='"+obj.CreatedBy+"',CreatedDate='"+obj.CreatedDate+"',ModifiedBy='"+obj.ModifiedBy+"',ModifiedDate='"+obj.ModifiedDate+"',EnteredOnMachineID='"+obj.EnteredOnMachineID+"'  where ID='"+obj.ID+"';";
			}
			else{
				strQuery = "Insert into SnagChecklistEntries(ID,ProjectID,BuildingID,FloorID,ApartmentID,AptAreaID,SnagID,ChecklistID,CheckListGroup,ChecklistDescription,ChecklistEntry,CreatedBy,CreatedDate,ModifiedBy,ModifiedDate,EnteredOnMachineID) values('"+obj.ID+"','"+obj.ProjectID+"','"+obj.BuildingID+"','"+obj.FloorID+"','"+obj.ApartmentID+"','"+obj.AptAreaID+"','"+obj.SnagID+"','"+obj.ChecklistID+"','"+obj.CheckListGroup+"','"+obj.ChecklistDescription+"','"+obj.CheckListEntry+"','"+obj.CreatedBy+"','"+obj.CreatedDate+"','"+obj.ModifiedBy+"','"+obj.ModifiedDate+"','"+obj.EnteredOnMachineID+"');";
			}
	        mydb.execSQL(strQuery);
	        cursor.close();
	        mydb.close();
	        
		}
		catch(Exception e){
			Log.d("Error insertORUpdateSnagChecklistEntry=", ""+e.getMessage());
		}
		
		
		return true;
	}
	
	public SnagChecklistEntries[] getSnagChecklistEnteries(String ID)
	{
		SnagChecklistEntries arr[]=null;
		try{
			mydb=context.openOrCreateDatabase(DBNAME, Context.MODE_PRIVATE, null);
			String strQuery;
	        strQuery = "Select * from SnagChecklistEnteries where ID='"+ID+"' Order by ID";
	        Cursor cursor=mydb.rawQuery(strQuery, null);
	        arr=new SnagChecklistEntries[cursor.getCount()];
	        int i=0;
	        if(cursor.moveToFirst()){
	        	do{
	        		SnagChecklistEntries obj=new SnagChecklistEntries();
	        		obj.ID=(String)(cursor.getString(cursor.getColumnIndex("ID")));
	        		obj.ProjectID=(String)(cursor.getString(cursor.getColumnIndex("ProjectID")));
	        		obj.BuildingID=(String)(cursor.getString(cursor.getColumnIndex("BuildingID")));
	        		obj.FloorID=(String)(cursor.getString(cursor.getColumnIndex("FloorID")));
	        		obj.ApartmentID=(String)(cursor.getString(cursor.getColumnIndex("ApartmentID")));
	        		obj.AptAreaID=(String)(cursor.getString(cursor.getColumnIndex("AptAreaID")));
	        		obj.SnagID=(String)(cursor.getString(cursor.getColumnIndex("SnagID")));
	        		obj.ChecklistID=(String)(cursor.getString(cursor.getColumnIndex("ChecklistID")));
	        		obj.ChecklistDescription=(String)(cursor.getString(cursor.getColumnIndex("ChecklistDescription")));
	        		obj.CheckListEntry=(String)(cursor.getString(cursor.getColumnIndex("ChecklistEntry")));
	        		obj.CreatedBy=(String)(cursor.getString(cursor.getColumnIndex("CreatedBy")));
	        		obj.CreatedDate=(String)(cursor.getString(cursor.getColumnIndex("CreatedDate")));
	        		obj.ModifiedBy=(String)(cursor.getString(cursor.getColumnIndex("ModifiedBy")));
	        		obj.ModifiedDate=(String)(cursor.getString(cursor.getColumnIndex("ModifiedDate")));
	        		obj.EnteredOnMachineID=(String)(cursor.getString(cursor.getColumnIndex("EnteredOnMachineID")));
	        		obj.CheckListGroup=(String)(cursor.getString(cursor.getColumnIndex("CheckListGroup")));
	        		
	        		arr[i]=obj;
	        		
	        		i++;
	        	}
	        	while(cursor.moveToNext());
	        }
	        cursor.close();
	        mydb.close();
		}
		catch(Exception e){
			Log.d("Error=", ""+e.getMessage());
		}
		return arr;
}
	public SnagMaster[] getSnagsXYvalue(String strfloorID)
	{
		SnagMaster[] arrValue=null;
		try
		{
		mydb=context.openOrCreateDatabase(DBNAME, Context.MODE_PRIVATE, null);
		String strQuery;
		String strArr[]=strfloorID.split("~");
        strQuery = "Select * from SnagMaster where FloorID='"+strArr[0]+"' and BuildingID='"+strArr[1]+"' and ProjectID='"+strArr[2]+"';";
        Cursor cursor=mydb.rawQuery(strQuery, null);
        arrValue=new SnagMaster[cursor.getCount()];
        int i=0;
        if(cursor.moveToFirst())
        {
        	do
        	{
        		SnagMaster obj=new SnagMaster();
        		
        		obj.setID(cursor.getString(cursor.getColumnIndex("ID")));
        		if(cursor.getString(cursor.getColumnIndex("XValue"))!=null)
        		{
        			String str=cursor.getString(cursor.getColumnIndex("XValue"));
        			float db=0;
        			if(str!=null && str.length()>0)
        				db=Float.parseFloat(str);
        			obj.setXValue(db);
        		}
        		else
        		{
        			obj.setXValue(0);
        		}
        		if(cursor.getString(cursor.getColumnIndex("YValue"))!=null)
        		{
        			float db=0;
        			String str=cursor.getString(cursor.getColumnIndex("YValue"));
        			if(str!=null && str.length()>0)
        				db=Float.parseFloat(str);
        			obj.setYValue(db);
        		}
        		else
        		{
        			obj.setYValue(0);
        		}
        		obj.setID(cursor.getString(cursor.getColumnIndex("ID")));
        		obj.setSnagType(cursor.getString(cursor.getColumnIndex("SnagType")));
        		if(cursor.getString(cursor.getColumnIndex("SnagDetails"))==null)
        		{
        			obj.setSnagDetails("");
        		}
        		else
        		{
        			obj.setSnagDetails(cursor.getString(cursor.getColumnIndex("SnagDetails")));
            		
        		}
        		obj.setPictureURL1(cursor.getString(cursor.getColumnIndex("PictureURL1")));
        		obj.setPictureURL2(cursor.getString(cursor.getColumnIndex("PictureURL2")));
        		obj.setPictureURL3(cursor.getString(cursor.getColumnIndex("PictureURL3")));
        		obj.setProjectID(cursor.getString(cursor.getColumnIndex("ProjectID")));
        		obj.setProjectName(cursor.getString(cursor.getColumnIndex("ProjectName")));
        		obj.setBuildingID(cursor.getString(cursor.getColumnIndex("BuildingID")));
        		obj.setBuildingName(cursor.getString(cursor.getColumnIndex("BuildingName")));
        		obj.setFloorID(cursor.getString(cursor.getColumnIndex("FloorID")));
        		obj.setFloor(cursor.getString(cursor.getColumnIndex("Floor")));
        		obj.setApartmentID(cursor.getString(cursor.getColumnIndex("ApartmentID")));
        		obj.setApartment(cursor.getString(cursor.getColumnIndex("Apartment")));
        		obj.setAptAreaName(cursor.getString(cursor.getColumnIndex("AptAreaName")));
        		obj.setAptAreaID(cursor.getString(cursor.getColumnIndex("AptAreaID")));
        		obj.setReportDate(cursor.getString(cursor.getColumnIndex("ReportDate")));
        		obj.setSnagStatus(cursor.getString(cursor.getColumnIndex("SnagStatus")));
        		obj.setResolveDate(cursor.getString(cursor.getColumnIndex("ResolveDate")));
        		obj.setInspectorID(cursor.getString(cursor.getColumnIndex("InspectorID")));
        		if(cursor.getString(cursor.getColumnIndex("InspectorName"))==null)
        		{
        			obj.setInspectorName("");
        		}
        		else
        		{
        			obj.setInspectorName(cursor.getString(cursor.getColumnIndex("InspectorName")));
        		}
        	    obj.setResolveDatePictureURL1(cursor.getString(cursor.getColumnIndex("ResolveDatePictureURL1")));
        		obj.setResolveDatePictureURL2(cursor.getString(cursor.getColumnIndex("ResolveDatePictureURL2")));
        		obj.setResolveDatePictureURL3(cursor.getString(cursor.getColumnIndex("ResolveDatePictureURL3")));
        		obj.setReInspectedUnresolvedDate(cursor.getString(cursor.getColumnIndex("ReinspectedUnresolvedDate")));
        		obj.setReInspectedUnresolvedDatePictureURL1(cursor.getString(cursor.getColumnIndex("ReinspectedUnresolvedDatePictureURL1")));
        		obj.setReInspectedUnresolvedDatePictureURL2(cursor.getString(cursor.getColumnIndex("ReinspectedUnresolvedDatePictureURL2")));
        		obj.setReInspectedUnresolvedDatePictureURL3(cursor.getString(cursor.getColumnIndex("ReinspectedUnresolvedDatePictureURL3")));
        		obj.setExpectedInspectionDate(cursor.getString(cursor.getColumnIndex("ExpectedInspectionDate")));
        		obj.setFaultType(cursor.getString(cursor.getColumnIndex("FaultType")));
        		obj.setQCC(cursor.getString(cursor.getColumnIndex("QCC")));
        		if(cursor.getString(cursor.getColumnIndex("IsSyncedToWeb")).equalsIgnoreCase("true"))
        			obj.setIsDataSyncToWeb(true);
        		else{
        			obj.setIsDataSyncToWeb(false);
        		}
        		obj.setStatusForUpload(cursor.getString(cursor.getColumnIndex("StatusForUpload")));
        		
        		//String str=cursor.getString(cursor.getColumnIndex("Cost"));
        		String str1=cursor.getString(cursor.getColumnIndex("Cost"));
    			Double d=0.0;
    			if(str1!=null && !(str1.equalsIgnoreCase("null")) && str1.length()>0)
    				d=Double.parseDouble(str1);
    			obj.setCost(d);
            		if(cursor.getString(cursor.getColumnIndex("CostTo"))==null)
            		{
            			obj.setCostTo("");
            		}
            		else
            		{
            			obj.setCostTo(cursor.getString(cursor.getColumnIndex("CostTo")));
            		}
            		
            		obj.setSnagPriority(cursor.getString(cursor.getColumnIndex("PriorityLevel")));
        		
            		obj.setAllocatedTo(cursor.getString(cursor.getColumnIndex("AllocatedTo")));
            		obj.setAllocatedToName(cursor.getString(cursor.getColumnIndex("AllocatedToName")));
            		obj.setQCC(cursor.getString(cursor.getColumnIndex("QCC")));
            		if(cursor.getString(cursor.getColumnIndex("ContractorStatus"))==null)
            		{
            			obj.setContractorStatus("");
            			
            		}
            		else
            		{
            			obj.setContractorStatus(cursor.getString(cursor.getColumnIndex("ContractorStatus")));
            		}
        			obj.setContractorExpectedBeginDate(cursor.getString(cursor.getColumnIndex("ContractorExpectedBeginDate")));
        			obj.setContractorExpectedEndDate(cursor.getString(cursor.getColumnIndex("ContractorExpectedEndDate")));
        			obj.setContractorActualBeginDate(cursor.getString(cursor.getColumnIndex("ContractorActualBeginDate")));
        			obj.setContractorActualEndDate(cursor.getString(cursor.getColumnIndex("ContractorActualEndDate")));
        			
        			String str=cursor.getString(cursor.getColumnIndex("ConManHours"));
        			d=0.0;
        			if(str!=null && !(str.equalsIgnoreCase("null")) && str.length()>0)
        				d=Double.parseDouble(str);
        			
        			obj.setConManHours(d);
        			
        			if(cursor.getString(cursor.getColumnIndex("ConNoOfResources")) != null)
        			{
        				String noOfrsr=cursor.getString(cursor.getColumnIndex("ConNoOfResources"));
        				int rs=0;
        				if(noOfrsr!=null && !(noOfrsr.equalsIgnoreCase("null")) && noOfrsr.length()>0)
        					rs=Integer.parseInt(noOfrsr);
        				obj.setConNoOfResources(rs);
        			}
        			else
        			{
        				obj.setConNoOfResources(0);
        			}
        			str=cursor.getString(cursor.getColumnIndex("ConCost"));
        			d=0.0;
        			if(str!=null && !(str.equalsIgnoreCase("null")) && str.length()>0){
        				d=Double.parseDouble(str);
        			}
        			obj.setConCost(d);
	            		
        			
                   
        			obj.setContractorRemarks(cursor.getString(cursor.getColumnIndex("ContractorRemarks")));
            	    if(cursor.getString(cursor.getColumnIndex("ContractorName"))==null)
            	    {
            	    	obj.setContractorName("");
            	    }
            	    else
            	    {
            	    	obj.setContractorName(cursor.getString(cursor.getColumnIndex("ContractorName")));
            	    }
            	    
            	    if(cursor.getString(cursor.getColumnIndex("SubContractorName"))==null)
            	    {
            	    	obj.setSubContractorName("");
            	    }
            	    else
            	    {
            	    	obj.setSubContractorName(cursor.getString(cursor.getColumnIndex("SubContractorName")));
            	    }
//        		if(cursor.getString(cursor.getColumnIndex("SnagDetails"))!=null && cursor.getString(cursor.getColumnIndex("SnagDetails")).length()>0)
//        		{
//        			obj.setSnagDetails(cursor.getString(cursor.getColumnIndex("SnagDetails")));
//        		}
//        		else
//        		{
//        			obj.setSnagDetails("");
//        		}
        		arrValue[i]=obj;
        		i++;
        	}
        	while(cursor.moveToNext());
        }
        cursor.close();
        mydb.close();
        
		}
		catch (Exception e)
		{
		Log.d("Expection fmdb getSnagsXYvalue",""+e.getMessage());
		mydb.close();
		}
		return arrValue;
	}
	
	public boolean insertORUpdateSnagChecklistMaster(SnagChecklistMaster obj)
	{
		
		try{
			mydb=context.openOrCreateDatabase(DBNAME, Context.MODE_PRIVATE, null);
			String strQuery;
			strQuery="Select * from SnagChecklistMaster where ID='"+obj.getID()+"';";
			Cursor cursor=mydb.rawQuery(strQuery, null);
			if(cursor.moveToFirst()){
				strQuery="UPDATE SnagChecklistMaster SET JobType='"+obj.getJobType()+"',FaultType='"+obj.getFaultType()+"',ChecklistDescription='"+obj.getChecklistDescription()+"',CreatedBy='"+obj.getCreatedBy()+"',CreatedDate='"+obj.getCreatedDate()+"',ModifiedBy='"+obj.getModifiedBy()+"',ModifiedDate='"+obj.getModifiedDate()+"',EnteredOnMachineID='"+obj.getEnteredOnMachineID()+"'  where ID='"+obj.getID()+"';";
			}
			else{
				strQuery = "Insert into SnagChecklistMaster(ID,JObType,FaultType,ChecklistDescription,CreatedBy,CreatedDate,ModifiedBy,ModofiedDate,EnteredOnMachineID) values('"+obj.getID()+"','"+obj.getJobType()+"','"+obj.getFaultType()+"','"+obj.getChecklistDescription()+"','"+obj.getCreatedBy()+"','"+obj.getCreatedDate()+"','"+obj.getModifiedBy()+"','"+obj.getModifiedDate()+"','"+obj.getEnteredOnMachineID()+"');";
			}
	        mydb.execSQL(strQuery);
	        cursor.close();
	        mydb.close();
	        
		}
		catch(Exception e){
			
		}
		
		
		return true;
	}

	public JobMaster getContractorDetailFromJobMaster(String Job,String PrjID){
		JobMaster obj=null;
		try{

			mydb=context.openOrCreateDatabase(DBNAME, Context.MODE_PRIVATE, null);
			String strQuery;
	        strQuery = "Select * from JobMaster where Job='"+Job+"' and ProjectID='"+PrjID+"'";
	        Cursor cursor=mydb.rawQuery(strQuery, null);
	       
	        
	        if(cursor.moveToFirst()){
	        	
	        		obj=new JobMaster();
	        		obj.setID(cursor.getString(cursor.getColumnIndex("ID")));
	        		obj.setJob(cursor.getString(cursor.getColumnIndex("Job")));
	        		obj.setJobDetails(cursor.getString(cursor.getColumnIndex("JobDetails")));
	        		obj.setProjectID(cursor.getString(cursor.getColumnIndex("ProjectID")));
	        		obj.setProjectName(cursor.getString(cursor.getColumnIndex("ProjectName")));
	        		obj.setProjectID(cursor.getString(cursor.getColumnIndex("ProjectID")));
	        		obj.setContractorID(cursor.getString(cursor.getColumnIndex("ContractorID")));
	        		obj.setContractorName(cursor.getString(cursor.getColumnIndex("ContractorName")));
	        		obj.setSubContractorID(cursor.getString(cursor.getColumnIndex("SubContractorID")));
	        		obj.setSubContractorName(cursor.getString(cursor.getColumnIndex("SubContractorName")));
	        		obj.setCreatedBy(cursor.getString(cursor.getColumnIndex("CreatedBy")));
	        		obj.setCreatedDate(cursor.getString(cursor.getColumnIndex("CreatedDate")));
	        		obj.setModifiedBy(cursor.getString(cursor.getColumnIndex("ModifiedBy")));
	        		obj.setModifiedDate(cursor.getString(cursor.getColumnIndex("ModifiedDate")));
	        		obj.setSubSubContractorID(cursor.getString(cursor.getColumnIndex("SubSubContractorID")));
	        		obj.setSubSubContractorName(cursor.getString(cursor.getColumnIndex("SubSubContractorName")));
	        		
	        		
	        		
	        	
	        }
	        cursor.close();
	        mydb.close();
		
		}
		catch(Exception e){
			
		}
		return obj;
	}
	//
	public String[] getProjectIDByContractor(String conId)
	{
		String[] strID=null;
		try
		{
			mydb=context.openOrCreateDatabase(DBNAME, Context.MODE_PRIVATE, null);
			String strQuery;
			strQuery="Select ProjectID from JobMaster where ContractorID='"+conId+"';";
			Cursor cursor=mydb.rawQuery(strQuery, null);
			strID=new String[cursor.getCount()];
			int i=0;
			if(cursor.moveToFirst())
			{
				do
				{
					String res=cursor.getString(cursor.getColumnIndex("ProjectID"));
					strID[i]=res;
					i++;
					
				}while(cursor.moveToNext());
			}
			cursor.close();
			mydb.close();
		}
		catch (Exception e)
		{
			Log.d("Exception getProjectIDByContractor", e.getMessage());
		}
		return strID;
	}
	
	public ProjectMaster getProjectByID(String strProjectID)
	{
		ProjectMaster obj=null;
		try
		{
			mydb=context.openOrCreateDatabase(DBNAME, Context.MODE_PRIVATE, null);
			String strQuery;
			strQuery="Select * from ProjectMaster where ID='"+strProjectID+"';";
			Cursor cursor=mydb.rawQuery(strQuery, null);
			if(cursor.moveToFirst())
			{
				obj=new ProjectMaster();
				obj.setID(cursor.getString(cursor.getColumnIndex("ID")));
				obj.setProjectName(cursor.getString(cursor.getColumnIndex("ProjectName")));
				obj.setNoOfBuildings(cursor.getInt(cursor.getColumnIndex("NoOfBuildings")));
				obj.setLocation(cursor.getString(cursor.getColumnIndex("Location")));
				obj.setAddress1(cursor.getString(cursor.getColumnIndex("Address1")));
				obj.setAddress2(cursor.getString(cursor.getColumnIndex("Address2")));
				obj.setPincode(cursor.getString(cursor.getColumnIndex("Pincode")));
				obj.setCity(cursor.getString(cursor.getColumnIndex("City")));
				obj.setState(cursor.getString(cursor.getColumnIndex("State")));
				obj.setImageName(cursor.getString(cursor.getColumnIndex("ImageName")));
				obj.setAbout(cursor.getString(cursor.getColumnIndex("About")));
				obj.setBuilderID(cursor.getString(cursor.getColumnIndex("BuilderID")));
				obj.setBuilderName(cursor.getString(cursor.getColumnIndex("BuilderName")));
			}
			cursor.close();
			mydb.close();
		}
		catch (Exception e)
		{
			Log.d("Exception getProjectByID",e.getMessage());
		}
		return obj;
	}
	public SnagMaster[] getSnagsByContractorID(String strPrjID,String strBldID,String strConID)
	{
		SnagMaster[] arrSnag=null;
		try
		{
			mydb=context.openOrCreateDatabase(DBNAME, Context.MODE_PRIVATE, null);
			String strQuery;
			strQuery = "Select * from SnagMaster where (ProjectID='"+strPrjID+"' and BuildingID='"+strBldID+"' and ContractorID='"+strConID+"' )  Order by ID desc";
			Cursor cursor=mydb.rawQuery(strQuery, null);
		    arrSnag=new SnagMaster[cursor.getCount()];
		    int i=0;
		    if(cursor.moveToFirst())
		    {
	        	do{
	        		SnagMaster obj=new SnagMaster();
	        		obj.setID(cursor.getString(cursor.getColumnIndex("ID")));
	        		obj.setSnagType(cursor.getString(cursor.getColumnIndex("SnagType")));
	        		obj.setSnagDetails(cursor.getString(cursor.getColumnIndex("SnagDetails")));
	        		obj.setPictureURL1(cursor.getString(cursor.getColumnIndex("PictureURL1")));
	        		obj.setPictureURL2(cursor.getString(cursor.getColumnIndex("PictureURL2")));
	        		obj.setPictureURL3(cursor.getString(cursor.getColumnIndex("PictureURL3")));
	        		obj.setProjectID(cursor.getString(cursor.getColumnIndex("ProjectID")));
	        		obj.setProjectName(cursor.getString(cursor.getColumnIndex("ProjectName")));
	        		obj.setBuildingID(cursor.getString(cursor.getColumnIndex("BuildingID")));
	        		obj.setBuildingName(cursor.getString(cursor.getColumnIndex("BuildingName")));
	        		obj.setFloorID(cursor.getString(cursor.getColumnIndex("FloorID")));
	        		obj.setFloor(cursor.getString(cursor.getColumnIndex("Floor")));
	        		obj.setApartmentID(cursor.getString(cursor.getColumnIndex("ApartmentID")));
	        		obj.setApartment(cursor.getString(cursor.getColumnIndex("Apartment")));
	        		obj.setAptAreaName(cursor.getString(cursor.getColumnIndex("AptAreaName")));
	        		obj.setAptAreaID(cursor.getString(cursor.getColumnIndex("AptAreaID")));
	        		obj.setReportDate(cursor.getString(cursor.getColumnIndex("ReportDate")));
	        		obj.setSnagStatus(cursor.getString(cursor.getColumnIndex("SnagStatus")));
	        		obj.setResolveDate(cursor.getString(cursor.getColumnIndex("ResolveDate")));
	        		obj.setInspectorID(cursor.getString(cursor.getColumnIndex("InspectorID")));
	        		obj.setInspectorName(cursor.getString(cursor.getColumnIndex("InspectorName")));
	        		obj.setResolveDatePictureURL1(cursor.getString(cursor.getColumnIndex("ResolveDatePictureURL1")));
	        		obj.setResolveDatePictureURL2(cursor.getString(cursor.getColumnIndex("ResolveDatePictureURL2")));
	        		obj.setResolveDatePictureURL3(cursor.getString(cursor.getColumnIndex("ResolveDatePictureURL3")));
	        		obj.setReInspectedUnresolvedDate(cursor.getString(cursor.getColumnIndex("ReinspectedUnresolvedDate")));
	        		obj.setReInspectedUnresolvedDatePictureURL1(cursor.getString(cursor.getColumnIndex("ReinspectedUnresolvedDatePictureURL1")));
	        		obj.setReInspectedUnresolvedDatePictureURL2(cursor.getString(cursor.getColumnIndex("ReinspectedUnresolvedDatePictureURL2")));
	        		obj.setReInspectedUnresolvedDatePictureURL3(cursor.getString(cursor.getColumnIndex("ReinspectedUnresolvedDatePictureURL3")));
	        		obj.setExpectedInspectionDate(cursor.getString(cursor.getColumnIndex("ExpectedInspectionDate")));
	        		obj.setFaultType(cursor.getString(cursor.getColumnIndex("FaultType")));
	        		obj.setQCC(cursor.getString(cursor.getColumnIndex("QCC")));
	        		if(cursor.getString(cursor.getColumnIndex("IsSyncedToWeb")).equalsIgnoreCase("true"))
	        			obj.setIsDataSyncToWeb(true);
	        		else{
	        			obj.setIsDataSyncToWeb(false);
	        		}
	        		obj.setStatusForUpload(cursor.getString(cursor.getColumnIndex("StatusForUpload")));
	        		
	        		//String str=cursor.getString(cursor.getColumnIndex("Cost"));
	        		String str1=cursor.getString(cursor.getColumnIndex("Cost"));
	    			Double d=0.0;
	    			if(str1!=null && !str1.equalsIgnoreCase("null") && str1.length()>0)
	    				d=Double.parseDouble(str1);
	    			obj.setCost(d);
	            		
	            		obj.setCostTo(cursor.getString(cursor.getColumnIndex("CostTo")));
	            		obj.setSnagPriority(cursor.getString(cursor.getColumnIndex("PriorityLevel")));
	        		
	            		obj.setAllocatedTo(cursor.getString(cursor.getColumnIndex("AllocatedTo")));
                		obj.setAllocatedToName(cursor.getString(cursor.getColumnIndex("AllocatedToName")));
                		obj.setQCC(cursor.getString(cursor.getColumnIndex("QCC")));
                		obj.setContractorStatus(cursor.getString(cursor.getColumnIndex("ContractorStatus")));
            			obj.setContractorExpectedBeginDate(cursor.getString(cursor.getColumnIndex("ContractorExpectedBeginDate")));
            			obj.setContractorExpectedEndDate(cursor.getString(cursor.getColumnIndex("ContractorExpectedEndDate")));
            			obj.setContractorActualBeginDate(cursor.getString(cursor.getColumnIndex("ContractorActualBeginDate")));
            			obj.setContractorActualEndDate(cursor.getString(cursor.getColumnIndex("ContractorActualEndDate")));
            			String str=cursor.getString(cursor.getColumnIndex("ConManHours"));
            			d=0.0;
            			if(str!=null && !str.equalsIgnoreCase("null") && str.length()>0)
            				d=Double.parseDouble(str);
            			
            			obj.setConManHours(d);
            			if(cursor.getString(cursor.getColumnIndex("ConNoOfResources")) != null)
            			{
            				String noOfrsr=cursor.getString(cursor.getColumnIndex("ConNoOfResources"));
            				int rs=0;
            				if(noOfrsr!=null && !noOfrsr.equalsIgnoreCase("null") && noOfrsr.length()>0)
            				rs=Integer.parseInt(noOfrsr);
            				obj.setConNoOfResources(rs);
            			}
            			else
            			{
            				obj.setConNoOfResources(0);
            			}
            			str=cursor.getString(cursor.getColumnIndex("ConCost"));
            			d=0.0;
            			if(str!=null && !str.equalsIgnoreCase("null") && str.length()>0){
            				d=Double.parseDouble(str);
            			}
            			obj.setConCost(d);
    	            		
            			
                       
            			obj.setContractorRemarks(cursor.getString(cursor.getColumnIndex("ContractorRemarks")));
	            		
	        		arrSnag[i]=obj;
	        		
	        		i++;
	        	}
	        	while(cursor.moveToNext());
	        }
	        cursor.close();
	        mydb.close();

		}
		catch (Exception e)
		{
			Log.d("Exception getSnagsByContractorID",e.getMessage());
		}
		return arrSnag;
	}
	public boolean getIsParentDone(String MainIMID,String AreaType,String SelectedAreaTypeParent,String jobtype){
		boolean isParentDone=true;
		try{
			
		}
		catch(Exception e){
			Log.d("Error getIsParentDone= ",""+e.getMessage());
		}
		return isParentDone;
	}
	
	public BuildingMaster[] getExtBuildingDetails(String strPrjID)
	{
		BuildingMaster[] arr=null;
		try
		{
			mydb=context.openOrCreateDatabase(DBNAME, Context.MODE_PRIVATE, null);
			String strQuery;
			strQuery="Select * from BuildingMaster where ProjectID='"+strPrjID+"' and BuildingType ='EXTERNAL AREA' ;";
			Cursor cursor=mydb.rawQuery(strQuery, null);
			arr=new BuildingMaster[cursor.getCount()];
			int i=0;
		    if(cursor.moveToFirst())
		    {
		    	do
		    	{
		    		BuildingMaster obj=new BuildingMaster();
	        		obj.setID(cursor.getString(cursor.getColumnIndex("ID")));
	        		obj.setBuildingName(cursor.getString(cursor.getColumnIndex("BuildingName")));
	        		obj.setNoOfElevators(cursor.getInt(cursor.getColumnIndex("NoOfElevators")));
	        		obj.setNoOfFloors(cursor.getInt(cursor.getColumnIndex("NoOfFloors")));
	        		obj.setProjectID(cursor.getString(cursor.getColumnIndex("ProjectID")));
	        		obj.setProjectName(cursor.getString(cursor.getColumnIndex("ProjectName")));
	        		obj.setImageName(cursor.getString(cursor.getColumnIndex("ImageName")));
	        		obj.setBuildingType(cursor.getString(cursor.getColumnIndex("BuildingType")));
	        		
	        		arr[i]=obj;
	        		
	        		i++;
		    	}while(cursor.moveToNext());
		    	
		    }
		    cursor.close();
		    mydb.close();
		}
		catch (Exception e)
		{
			Log.d("Exception fmdb getExtBuildingDetails",""+e.getMessage());
		}
		return arr;
	}
	
	public SnagMaster[] getExternalAreaSnags(String strPrjID,String strBuildID)
	{
		SnagMaster[] arr=null;
		try
		{
			mydb=context.openOrCreateDatabase(DBNAME, Context.MODE_PRIVATE, null);
			String strQuery;
			strQuery="Select * from SnagMaster where ProjectID='"+strPrjID+"' and BuildingID='"+strBuildID+"' Order by ReportDate Desc;";
			Cursor cursor=mydb.rawQuery(strQuery, null);
		    arr=new SnagMaster[cursor.getCount()];
		    int i=0;
		    if(cursor.moveToFirst())
		    {
	        	do{
	        		SnagMaster obj=new SnagMaster();
	        		obj.setID(cursor.getString(cursor.getColumnIndex("ID")));
	        		obj.setSnagType(cursor.getString(cursor.getColumnIndex("SnagType")));
	        		obj.setSnagDetails(cursor.getString(cursor.getColumnIndex("SnagDetails")));
	        		obj.setPictureURL1(cursor.getString(cursor.getColumnIndex("PictureURL1")));
	        		obj.setPictureURL2(cursor.getString(cursor.getColumnIndex("PictureURL2")));
	        		obj.setPictureURL3(cursor.getString(cursor.getColumnIndex("PictureURL3")));
	        		obj.setProjectID(cursor.getString(cursor.getColumnIndex("ProjectID")));
	        		obj.setProjectName(cursor.getString(cursor.getColumnIndex("ProjectName")));
	        		obj.setBuildingID(cursor.getString(cursor.getColumnIndex("BuildingID")));
	        		obj.setBuildingName(cursor.getString(cursor.getColumnIndex("BuildingName")));
	        		obj.setFloorID(cursor.getString(cursor.getColumnIndex("FloorID")));
	        		obj.setFloor(cursor.getString(cursor.getColumnIndex("Floor")));
	        		obj.setApartmentID(cursor.getString(cursor.getColumnIndex("ApartmentID")));
	        		obj.setApartment(cursor.getString(cursor.getColumnIndex("Apartment")));
	        		obj.setAptAreaName(cursor.getString(cursor.getColumnIndex("AptAreaName")));
	        		obj.setAptAreaID(cursor.getString(cursor.getColumnIndex("AptAreaID")));
	        		obj.setReportDate(cursor.getString(cursor.getColumnIndex("ReportDate")));
	        		obj.setSnagStatus(cursor.getString(cursor.getColumnIndex("SnagStatus")));
	        		obj.setResolveDate(cursor.getString(cursor.getColumnIndex("ResolveDate")));
	        		obj.setInspectorID(cursor.getString(cursor.getColumnIndex("InspectorID")));
	        		obj.setInspectorName(cursor.getString(cursor.getColumnIndex("InspectorName")));
	        		obj.setResolveDatePictureURL1(cursor.getString(cursor.getColumnIndex("ResolveDatePictureURL1")));
	        		obj.setResolveDatePictureURL2(cursor.getString(cursor.getColumnIndex("ResolveDatePictureURL2")));
	        		obj.setResolveDatePictureURL3(cursor.getString(cursor.getColumnIndex("ResolveDatePictureURL3")));
	        		obj.setReInspectedUnresolvedDate(cursor.getString(cursor.getColumnIndex("ReinspectedUnresolvedDate")));
	        		obj.setReInspectedUnresolvedDatePictureURL1(cursor.getString(cursor.getColumnIndex("ReinspectedUnresolvedDatePictureURL1")));
	        		obj.setReInspectedUnresolvedDatePictureURL2(cursor.getString(cursor.getColumnIndex("ReinspectedUnresolvedDatePictureURL2")));
	        		obj.setReInspectedUnresolvedDatePictureURL3(cursor.getString(cursor.getColumnIndex("ReinspectedUnresolvedDatePictureURL3")));
	        		obj.setExpectedInspectionDate(cursor.getString(cursor.getColumnIndex("ExpectedInspectionDate")));
	        		obj.setFaultType(cursor.getString(cursor.getColumnIndex("FaultType")));
	        		obj.setQCC(cursor.getString(cursor.getColumnIndex("QCC")));
	        		if(cursor.getString(cursor.getColumnIndex("IsSyncedToWeb")).equalsIgnoreCase("true"))
	        			obj.setIsDataSyncToWeb(true);
	        		else{
	        			obj.setIsDataSyncToWeb(false);
	        		}
	        		obj.setStatusForUpload(cursor.getString(cursor.getColumnIndex("StatusForUpload")));
	        		
	        		//String str=cursor.getString(cursor.getColumnIndex("Cost"));
	        		String str1=cursor.getString(cursor.getColumnIndex("Cost"));
	    			Double d=0.0;
	    			if(str1!=null && str1.length()>0)
	    				d=Double.parseDouble(str1);
	    			obj.setCost(d);
	            		
	            		obj.setCostTo(cursor.getString(cursor.getColumnIndex("CostTo")));
	            		obj.setSnagPriority(cursor.getString(cursor.getColumnIndex("PriorityLevel")));
	        		
	            		obj.setAllocatedTo(cursor.getString(cursor.getColumnIndex("AllocatedTo")));
                		obj.setAllocatedToName(cursor.getString(cursor.getColumnIndex("AllocatedToName")));
                		obj.setQCC(cursor.getString(cursor.getColumnIndex("QCC")));
                		obj.setContractorStatus(cursor.getString(cursor.getColumnIndex("ContractorStatus")));
            			obj.setContractorExpectedBeginDate(cursor.getString(cursor.getColumnIndex("ContractorExpectedBeginDate")));
            			obj.setContractorExpectedEndDate(cursor.getString(cursor.getColumnIndex("ContractorExpectedEndDate")));
            			obj.setContractorActualBeginDate(cursor.getString(cursor.getColumnIndex("ContractorActualBeginDate")));
            			obj.setContractorActualEndDate(cursor.getString(cursor.getColumnIndex("ContractorActualEndDate")));
            			String str=cursor.getString(cursor.getColumnIndex("ConManHours"));
            			d=0.0;
            			if(str!=null && str.length()>0)
            				d=Double.parseDouble(str);
            			
            			obj.setConManHours(d);
            			if(cursor.getString(cursor.getColumnIndex("ConNoOfResources")) != null)
            			{
            				String noOfrsr=cursor.getString(cursor.getColumnIndex("ConNoOfResources"));
            				int rs=Integer.parseInt(noOfrsr);
            				obj.setConNoOfResources(rs);
            			}
            			else
            			{
            				obj.setConNoOfResources(0);
            			}
            			str=cursor.getString(cursor.getColumnIndex("ConCost"));
            			d=0.0;
            			if(str!=null && str.length()>0){
            				d=Double.parseDouble(str);
            			}
            			obj.setConCost(d);
    	            		
            			//obj.setApartmentID(cursor.getString(cursor.getColumnIndex("AptAreaID")));
                       
            			obj.setContractorRemarks(cursor.getString(cursor.getColumnIndex("ContractorRemarks")));
	            		
	        		arr[i]=obj;
	        		
	        		i++;
	        	}
	        	while(cursor.moveToNext());
	        }
	        cursor.close();
	        mydb.close();
		}
		catch (Exception e)
		{
			Log.d("Exception getExternalAreaSnags", ""+e.getMessage());
		}
		return arr;
	}

	public boolean InsertOrUpdateIntoDailyAttendance(DailyAttendance obj){
		try{
			mydb=context.openOrCreateDatabase(DBNAME, Context.MODE_PRIVATE, null);
			String strQuery;
			strQuery="Select * from DailyAttendance where ID='"+obj.ID+"';";
			Cursor cursor=mydb.rawQuery(strQuery, null);
			if(cursor.moveToFirst()){
				strQuery="UPDATE DailyAttendance SET InspectorID='"+obj.InspectorID+"',InspectorName='"+obj.InspectorName+"',AttendanceDate='"+obj.AttendanceDate+"',NoOfSkilledEmp='"+obj.NoOfSkilledEmp+"',NoOfUnskilledEmp='"+obj.NoOfUnskilledEmp+"',CreatedBy='"+obj.CreatedBy+"',CreatedDate='"+obj.CreatedDate+"',ModifiedBy='"+obj.ModifiedBy+"',ModifiedDate='"+obj.ModifiedDate+"',EnteredOnMachineID='"+obj.EnteredOnMachineID+"',IsSyncedToWeb='"+obj.IsSyncedToWeb+"',ContractorName='"+obj.ContractorName+"',ContractorID='"+obj.ContractorID+"'  where ID='"+obj.ID+"';";
			}
			else{
				strQuery = "Insert into DailyAttendance(ID,InspectorID,InspectorName,AttendanceDate,NoOfSkilledEmp,NoOfUnskilledEmp,CreatedBy,CreatedDate,ModifiedBy,ModifiedDate,EnteredOnMachineID,IsSyncedToWeb,ContractorName,ContractorID) values('"+obj.ID+"','"+obj.InspectorID+"','"+obj.InspectorName+"','"+obj.AttendanceDate+"','"+obj.NoOfSkilledEmp+"','"+obj.NoOfUnskilledEmp+"','"+obj.CreatedBy+"','"+obj.CreatedDate+"','"+obj.ModifiedBy+"','"+obj.ModifiedDate+"','"+obj.EnteredOnMachineID+"','"+obj.IsSyncedToWeb+"','"+obj.ContractorName+"','"+obj.ContractorID+"');";
			}
	        mydb.execSQL(strQuery);
	        cursor.close();
	        mydb.close();
		}
		catch(Exception e){
			Log.d("Error InsertOrUpdateIntoDailyAttendance=", ""+e.getMessage());
		}
		return true;
	}
	
	public boolean insertORUpdatetradeAptAreaMaster(TradeAptAreaMaster obj){
		try{
			mydb=context.openOrCreateDatabase(DBNAME, Context.MODE_PRIVATE, null);
			String strQuery;
			strQuery="Select * from TradeAptAreaMaster where ID='"+obj.ID+"';";
			Cursor cursor=mydb.rawQuery(strQuery, null);
			if(cursor.moveToFirst()){
				strQuery="UPDATE TradeAptAreaMaster SET TradeID='"+obj.TradeID+"',TradeType='"+obj.TradeType+"',PercentageComplete='"+obj.PercentageComplete+"',CreatedBy='"+obj.CreatedBy+"',CreatedDate='"+obj.CreatedDate+"',ModifiedBy='"+obj.ModifiedBy+"',ModifiedDate='"+obj.ModifiedDate+"',EnteredOnMachineID='"+obj.EnteredOnMachineID+"',IsSyncedToWeb='"+obj.IsSyncedToWeb+"',StatusForUpload='"+obj.StatusForUpload+"',ContractorID='"+obj.ContractorID+"',ContractorName='"+obj.ContractorName+"'  where ID='"+obj.ID+"';";
			}
			else{
				strQuery = "Insert into TradeAptAreaMaster(ID,ProjectID,BuildingID,FloorID,ApartmentID,AptAreaID,AptAreaTypeID,AptAreaType,TradeID,TradeType,PercentageComplete,CreatedBy,CreatedDate,ModifiedBy,ModifiedDate,EnteredOnMachineID,IsSyncedToWeb,StatusForUpload,ContractorID,ContractorName) values('"+obj.ID+"','"+obj.ProjectID+"','"+obj.BuildingID+"','"+obj.FloorID+"','"+obj.ApartmentID+"','"+obj.AptAreaID+"','"+obj.AptAreaTypeID+"','"+obj.AptAreaType+"','"+obj.TradeID+"','"+obj.TradeType+"','"+obj.PercentageComplete+"','"+obj.CreatedBy+"','"+obj.CreatedDate+"','"+obj.ModifiedBy+"','"+obj.ModifiedDate+"','"+obj.EnteredOnMachineID+"','"+obj.IsSyncedToWeb+"','"+obj.StatusForUpload+"','"+obj.ContractorID+"','"+obj.ContractorName+"');";
			}
	        mydb.execSQL(strQuery);
	        cursor.close();
	        mydb.close();
		}
		catch(Exception e){
			Log.d("Error InsertOrUpdateIntoDailyAttendance=", ""+e.getMessage());
		}
		return true;
	}
	
	public boolean insertORupdateTradeAptAreaDetail(TradeAptAreaDetail obj){
		try{
			mydb=context.openOrCreateDatabase(DBNAME, Context.MODE_PRIVATE, null);
			String strQuery;
			strQuery="Select * from TradeAptAreaDetail where ID='"+obj.ID+"';";
			Cursor cursor=mydb.rawQuery(strQuery, null);
			if(cursor.moveToFirst()){
				strQuery="UPDATE TradeAptAreaDetail SET TradeAptAreaMasterID='"+obj.TradeAptAreaMasterID+"',TradeDetailID='"+obj.TradeDetailID+"',InspectionGroup='"+obj.InspectionGroup+"',InspectionDescription='"+obj.InspectionDescription+"',InspectionEntry='"+obj.InspectionEntry+"',CreatedBy='"+obj.CreatedBy+"',CreatedDate='"+obj.CreatedDate+"',ModifiedBy='"+obj.ModifiedBy+"',ModifiedDate='"+obj.ModifiedDate+"',EnteredOnMachineID='"+obj.EnteredOnMachineID+"',StatusForUpload='"+obj.StatusForUpload+"',IsSyncedToWeb='"+obj.IsSyncedToWeb+"'  where ID='"+obj.ID+"';";
			}
			else{
				strQuery = "Insert into TradeAptAreaDetail(ID,TradeAptAreaMasterID,TradeDetailID,InspectionGroup,InspectionDescription,InspectionEntry,CreatedBy,CreatedDate,ModifiedBy,ModifiedDate,EnteredOnMachineID,StatusForUpload,IsSyncedToWeb) values('"+obj.ID+"','"+obj.TradeAptAreaMasterID+"','"+obj.TradeDetailID+"','"+obj.InspectionGroup+"','"+obj.InspectionDescription+"','"+obj.InspectionEntry+"','"+obj.CreatedBy+"','"+obj.CreatedDate+"','"+obj.ModifiedBy+"','"+obj.ModifiedDate+"','"+obj.EnteredOnMachineID+"','"+obj.StatusForUpload+"','"+obj.IsSyncedToWeb+"');";
			}
	        mydb.execSQL(strQuery);
	        cursor.close();
	        mydb.close();
		}
		catch(Exception e){
			Log.d("Error InsertOrUpdateIntoDailyAttendance=", ""+e.getMessage());
		}
		return true;
	}
	
	public TradeMaster[] getTradeMaster(String areatype,String areatypeParent)
	{
		
		
		TradeMaster[] arr=null;
		try
		{
			mydb=context.openOrCreateDatabase(DBNAME, Context.MODE_PRIVATE, null);
			String strQuery;
			strQuery="select * from trademaster where id in (select tradeid from tradeareamaster where buildingareatype='"+areatype+"' and aptareatype='"+areatypeParent+"' )order by SeqNo ;";
			Cursor cursor=mydb.rawQuery(strQuery, null);
			arr=new TradeMaster[cursor.getCount()];
			int i=0;
		    if(cursor.moveToFirst())
		    {
		    	do
		    	{
		    		TradeMaster obj=new TradeMaster();
	        		obj.ID=(String)(cursor.getString(cursor.getColumnIndex("ID")));
	        		obj.TradeType=(String)(cursor.getString(cursor.getColumnIndex("TradeType")));
	        		obj.TradeDescription=(String)(cursor.getString(cursor.getColumnIndex("TradeDescription")));
	        		obj.ParentID=(String)(cursor.getString(cursor.getColumnIndex("ParentID")));
	        		obj.ParentJob=(String)(cursor.getString(cursor.getColumnIndex("ParentJob")));
	        		obj.SeqNo=(String)(cursor.getString(cursor.getColumnIndex("SeqNo")));
	        		obj.ImageName=(String)(cursor.getString(cursor.getColumnIndex("ImageName")));
	        		obj.CreatedBy=(String)(cursor.getString(cursor.getColumnIndex("CreatedBy")));
	        		obj.CreatedDate=(String)(cursor.getString(cursor.getColumnIndex("CreatedDate")));
	        		obj.ModifiedBy=(String)(cursor.getString(cursor.getColumnIndex("ModifiedBy")));
	        		obj.ModifiedDate=(String)(cursor.getString(cursor.getColumnIndex("ModifiedDate")));
	        		obj.EnteredOnMachineID=(String)(cursor.getString(cursor.getColumnIndex("EnteredOnMachineID")));
	        		obj.StatusForUpload=(String)(cursor.getString(cursor.getColumnIndex("StatusForUpload")));
	        		obj.DependencyCount=(int)(cursor.getInt(cursor.getColumnIndex("DependencyCount")));
	        		String str=(String)(cursor.getString(cursor.getColumnIndex("IsParentCompulsory")));
	        		if(str!=null && str.equalsIgnoreCase("true"))
	        			obj.IsParentCompulsory=true;
	        		else
	        			obj.IsParentCompulsory=false;
	        		str=(String)(cursor.getString(cursor.getColumnIndex("IsSyncedToWeb")));
	        		if(str!=null && str.equalsIgnoreCase("true")){
	        			obj.IsSyncedToWeb=true;
	        		}
	        		else
	        			obj.IsSyncedToWeb=false;
	        		
	        		arr[i]=obj;
	        		
	        		i++;
		    	}while(cursor.moveToNext());
		    	
		    }
		    cursor.close();
		    mydb.close();
		}
		catch (Exception e)
		{
			Log.d("Exception fmdb getExtBuildingDetails",""+e.getMessage());
		}
		return arr;
	}
	public TradeDetails[] getTradeDetailsByTradeMasterID(String tradeID)
	{
		
		
		TradeDetails[] arr=null;
		try
		{
			mydb=context.openOrCreateDatabase(DBNAME, Context.MODE_PRIVATE, null);
			String strQuery;
			strQuery="Select * from TradeDetail where TradeMasterID='"+tradeID+"' order by SeqNo;";
			Cursor cursor=mydb.rawQuery(strQuery, null);
			arr=new TradeDetails[cursor.getCount()];
			int i=0;
		    if(cursor.moveToFirst())
		    {
		    	do
		    	{
		    		TradeDetails obj=new TradeDetails();
	        		obj.ID=(String)(cursor.getString(cursor.getColumnIndex("ID")));
	        		obj.TradeType=(String)(cursor.getString(cursor.getColumnIndex("TradeType")));
	        		obj.TradeMasterID=(String)(cursor.getString(cursor.getColumnIndex("TradeMasterID")));
	        		obj.InspectionGroup=(String)(cursor.getString(cursor.getColumnIndex("InspectionGroup")));
	        		obj.InspectionDescription=(String)(cursor.getString(cursor.getColumnIndex("InspectionDescription")));
	        		obj.SeqNo=(String)(cursor.getString(cursor.getColumnIndex("SeqNo")));
	        		
	        		obj.CreatedBy=(String)(cursor.getString(cursor.getColumnIndex("CreatedBy")));
	        		obj.CreatedDate=(String)(cursor.getString(cursor.getColumnIndex("CreatedDate")));
	        		obj.ModifiedBy=(String)(cursor.getString(cursor.getColumnIndex("ModifiedBy")));
	        		obj.ModifiedDate=(String)(cursor.getString(cursor.getColumnIndex("ModifiedDate")));
	        		obj.EnteredOnMachineID=(String)(cursor.getString(cursor.getColumnIndex("EnteredOnMachineID")));
	        		obj.StatusForUpload=(String)(cursor.getString(cursor.getColumnIndex("StatusForUpload")));
	        		String str=(String)(cursor.getString(cursor.getColumnIndex("IsSyncedToWeb")));
	        		if(str!=null && str.equalsIgnoreCase("true")){
	        			obj.IsSyncedToWeb=true;
	        		}
	        		else
	        			obj.IsSyncedToWeb=false;
	        		
	        		arr[i]=obj;
	        		
	        		i++;
		    	}while(cursor.moveToNext());
		    	
		    }
		    cursor.close();
		    mydb.close();
		}
		catch (Exception e)
		{
			Log.d("Exception fmdb getExtBuildingDetails",""+e.getMessage());
		}
		return arr;
	}
	public TradeDetails[] getTradeDetailsByInspectionGroup(String tradeID,String InspectionGroup)
	{
		
		
		TradeDetails[] arr=null;
		try
		{
			mydb=context.openOrCreateDatabase(DBNAME, Context.MODE_PRIVATE, null);
			String strQuery;
			strQuery="Select * from TradeDetail where TradeMasterID='"+tradeID+"' and InspectionGroup='"+InspectionGroup+"' order by SeqNo;";
			Cursor cursor=mydb.rawQuery(strQuery, null);
			arr=new TradeDetails[cursor.getCount()];
			int i=0;
		    if(cursor.moveToFirst())
		    {
		    	do
		    	{
		    		TradeDetails obj=new TradeDetails();
	        		obj.ID=(String)(cursor.getString(cursor.getColumnIndex("ID")));
	        		obj.TradeType=(String)(cursor.getString(cursor.getColumnIndex("TradeType")));
	        		obj.TradeMasterID=(String)(cursor.getString(cursor.getColumnIndex("TradeMasterID")));
	        		obj.InspectionGroup=(String)(cursor.getString(cursor.getColumnIndex("InspectionGroup")));
	        		obj.InspectionDescription=(String)(cursor.getString(cursor.getColumnIndex("InspectionDescription")));
	        		obj.SeqNo=(String)(cursor.getString(cursor.getColumnIndex("SeqNo")));
	        		
	        		obj.CreatedBy=(String)(cursor.getString(cursor.getColumnIndex("CreatedBy")));
	        		obj.CreatedDate=(String)(cursor.getString(cursor.getColumnIndex("CreatedDate")));
	        		obj.ModifiedBy=(String)(cursor.getString(cursor.getColumnIndex("ModifiedBy")));
	        		obj.ModifiedDate=(String)(cursor.getString(cursor.getColumnIndex("ModifiedDate")));
	        		obj.EnteredOnMachineID=(String)(cursor.getString(cursor.getColumnIndex("EnteredOnMachineID")));
	        		obj.StatusForUpload=(String)(cursor.getString(cursor.getColumnIndex("StatusForUpload")));
	        		String str=(String)(cursor.getString(cursor.getColumnIndex("IsSyncedToWeb")));
	        		if(str!=null && str.equalsIgnoreCase("true")){
	        			obj.IsSyncedToWeb=true;
	        		}
	        		else
	        			obj.IsSyncedToWeb=false;
	        		
	        		arr[i]=obj;
	        		
	        		i++;
		    	}while(cursor.moveToNext());
		    	
		    }
		    cursor.close();
		    mydb.close();
		}
		catch (Exception e)
		{
			Log.d("Exception fmdb getExtBuildingDetails",""+e.getMessage());
		}
		return arr;
	}
	public String[] getTradeDetailsByTradeType(String tradeID)
	{
		
		
		String[] arr=null;
		try
		{
			mydb=context.openOrCreateDatabase(DBNAME, Context.MODE_PRIVATE, null);
			String strQuery;
			strQuery="Select distinct inspectiongroup from TradeDetail where TradeMasterID='"+tradeID+"' order by SeqNo;";
			Cursor cursor=mydb.rawQuery(strQuery, null);
			arr=new String[cursor.getCount()];
			int i=0;
		    if(cursor.moveToFirst())
		    {
		    	do
		    	{
		    		String obj=new String();
	        		//obj.ID=(String)(cursor.getString(cursor.getColumnIndex("ID")));
	        		obj=(String)(cursor.getString(0));
//	        		obj.TradeMasterID=(String)(cursor.getString(cursor.getColumnIndex("TradeMasterID")));
//	        		obj.InspectionGroup=(String)(cursor.getString(cursor.getColumnIndex("InspectionGroup")));
//	        		obj.InspectionDescription=(String)(cursor.getString(cursor.getColumnIndex("InspectionDescription")));
	        		//int a=(int)(cursor.getInt(cursor.getColumnIndex("SeqNo")));
//	        		
//	        		obj.CreatedBy=(String)(cursor.getString(cursor.getColumnIndex("CreatedBy")));
//	        		obj.CreatedDate=(String)(cursor.getString(cursor.getColumnIndex("CreatedDate")));
//	        		obj.ModifiedBy=(String)(cursor.getString(cursor.getColumnIndex("ModifiedBy")));
//	        		obj.ModifiedDate=(String)(cursor.getString(cursor.getColumnIndex("ModifiedDate")));
//	        		obj.EnteredOnMachineID=(String)(cursor.getString(cursor.getColumnIndex("EnteredOnMachineID")));
//	        		obj.StatusForUpload=(String)(cursor.getString(cursor.getColumnIndex("StatusForUpload")));
//	        		String str=(String)(cursor.getString(cursor.getColumnIndex("IsSyncedToWeb")));
//	        		if(str!=null && str.equalsIgnoreCase("true")){
//	        			obj.IsSyncedToWeb=true;
//	        		}
//	        		else
//	        			obj.IsSyncedToWeb=false;
	        		
	        		arr[i]=obj;
	        		
	        		i++;
		    	}while(cursor.moveToNext());
		    	
		    }
		    cursor.close();
		    mydb.close();
		}
		catch (Exception e)
		{
			Log.d("Exception fmdb getExtBuildingDetails",""+e.getMessage());
		}
		return arr;
	}
	public TradeAptAreaDetail[] getTradeAptAreaDetail(String TradeAptAreaMasterID,String TradeDetailID, String InspectionGroup)
	{
		TradeAptAreaDetail arr[]=null;
		try{
			mydb=context.openOrCreateDatabase(DBNAME, Context.MODE_PRIVATE, null);
			String strQuery;
	        strQuery = "Select * from TradeAptAreaDetail where TradeAptAreaMasterID='"+TradeAptAreaMasterID+"' and InspectionGroup='"+InspectionGroup+"'";// and InspectionGroup='"+InspectionGroup+"'";
	        Cursor cursor=mydb.rawQuery(strQuery, null);
	        arr=new TradeAptAreaDetail[cursor.getCount()];
	        int i=0;
	        if(cursor.moveToFirst()){
	        	do{
	        		TradeAptAreaDetail obj=new TradeAptAreaDetail();
	        		obj.ID=(String)(cursor.getString(cursor.getColumnIndex("ID")));
	        		obj.TradeAptAreaMasterID=(String)(cursor.getString(cursor.getColumnIndex("TradeAptAreaMasterID")));
	        		obj.TradeAptAreaMasterID=(String)(cursor.getString(cursor.getColumnIndex("TradeAptAreaMasterID")));
	        		obj.InspectionGroup=(String)(cursor.getString(cursor.getColumnIndex("InspectionGroup")));
	        		obj.InspectionDescription=(String)(cursor.getString(cursor.getColumnIndex("InspectionDescription")));
	        		obj.InspectionEntry=(String)(cursor.getString(cursor.getColumnIndex("InspectionEntry")));
	        		obj.CreatedBy=(String)(cursor.getString(cursor.getColumnIndex("CreatedBy")));
	        		obj.CreatedDate=(String)(cursor.getString(cursor.getColumnIndex("CreatedDate")));
	        		obj.ModifiedBy=(String)(cursor.getString(cursor.getColumnIndex("ModifiedBy")));
	        		obj.ModifiedDate=(String)(cursor.getString(cursor.getColumnIndex("ModifiedDate")));
	        		obj.EnteredOnMachineID=(String)(cursor.getString(cursor.getColumnIndex("EnteredOnMachineID")));
	        		obj.StatusForUpload=(String)(cursor.getString(cursor.getColumnIndex("StatusForUpload")));
	        		String str=(String)(cursor.getString(cursor.getColumnIndex("IsSyncedToWeb")));
	        		if(str!=null && str.equalsIgnoreCase("true"))
	        		obj.IsSyncedToWeb=true;
	        		else
	        			obj.IsSyncedToWeb=false;
	        		arr[i]=obj;
	        		
	        		i++;
	        	}
	        	while(cursor.moveToNext());
	        }
	        cursor.close();
	        mydb.close();
		}
		catch(Exception e){
			Log.d("Error=", ""+e.getMessage());
		}
		
		
		return arr;
	}
	public TradeAptAreaMaster getTradeAptAreaMaster(String prjID,String bldgID,String flrID,String AptID,String AreaType,String tradeID,String AptAreaID)
	{
		TradeAptAreaMaster arr=null;
		try{
			mydb=context.openOrCreateDatabase(DBNAME, Context.MODE_PRIVATE, null);
			String strQuery;
	        strQuery = "Select * from TradeAptAreaMaster where ProjectID='"+prjID+"' and BuildingID='"+bldgID+"' and FloorID='"+flrID+"' and ApartmentID='"+AptID+"' and AptAreaType='"+AreaType+"' and TradeType='"+tradeID+"' and AptAreaID='"+AptAreaID+"'";// where ProjectID='"+ProjectID+"' Order by ID";
	        Cursor cursor=mydb.rawQuery(strQuery, null);
	        
	        int i=0;
	        if(cursor.moveToFirst()){
	        	//do{
	        		arr=new TradeAptAreaMaster();
	        		TradeAptAreaMaster obj=new TradeAptAreaMaster();
	        		obj.ID=(String)(cursor.getString(cursor.getColumnIndex("ID")));
	        		obj.ProjectID=(String)(cursor.getString(cursor.getColumnIndex("ProjectID")));
	        		obj.BuildingID=(String)(cursor.getString(cursor.getColumnIndex("BuildingID")));
	        		obj.FloorID=(String)(cursor.getString(cursor.getColumnIndex("FloorID")));
	        		obj.ApartmentID=(String)(cursor.getString(cursor.getColumnIndex("ApartmentID")));
	        		obj.AptAreaID=(String)(cursor.getString(cursor.getColumnIndex("AptAreaID")));
	        		obj.AptAreaTypeID=(String)(cursor.getString(cursor.getColumnIndex("AptAreaTypeID")));
	        		obj.AptAreaType=(String)(cursor.getString(cursor.getColumnIndex("AptAreaType")));
	        		obj.TradeID=(String)(cursor.getString(cursor.getColumnIndex("TradeID")));
	        		obj.TradeType=(String)(cursor.getString(cursor.getColumnIndex("TradeType")));
	        		String str2=(String)(cursor.getString(cursor.getColumnIndex("PercentageComplete")));
	        		double d=0.0;
	        		if(str2!=null)
	        			d=Double.parseDouble(str2);
	        		obj.PercentageComplete=d;
	        		obj.CreatedBy=(String)(cursor.getString(cursor.getColumnIndex("CreatedBy")));
	        		obj.CreatedDate=(String)(cursor.getString(cursor.getColumnIndex("CreatedDate")));
	        		obj.ModifiedBy=(String)(cursor.getString(cursor.getColumnIndex("ModifiedBy")));
	        		obj.ModifiedDate=(String)(cursor.getString(cursor.getColumnIndex("ModifiedDate")));
	        		obj.EnteredOnMachineID=(String)(cursor.getString(cursor.getColumnIndex("EnteredOnMachineID")));
	        		
	        		obj.StatusForUpload=(String)(cursor.getString(cursor.getColumnIndex("StatusForUpload")));
	        		String str=(String)(cursor.getString(cursor.getColumnIndex("IsSyncedToWeb")));
	        		if(str!=null && str.equalsIgnoreCase("true"))
	        			obj.IsSyncedToWeb=true;
	        		else
	        			obj.IsSyncedToWeb=false;
	        		arr=obj;
	        		
	        		i++;
	        	//}
	        	//while(cursor.moveToNext());
	        }
	        cursor.close();
	        mydb.close();
		}
		catch(Exception e){
			Log.d("Error=", ""+e.getMessage());
		}
		
		
		return arr;
	}
	public TradeMaster[] getTradeMasterTradeAptAreaMaster(String prjID,String bldgID,String flrID,String AptID,String AreaType,String AptAreaId)
	{
		TradeMaster arr[]=null;
		try{
			mydb=context.openOrCreateDatabase(DBNAME, Context.MODE_PRIVATE, null);
			String strQuery;
	        strQuery = "select * from trademaster where id in (Select TradeID from TradeAptAreaMaster where ProjectID='"+prjID+"' and BuildingID='"+bldgID+"' and FloorID='"+flrID+"' and ApartmentID='"+AptID+"' and AptAreaID='"+AptAreaId+"') order by seqno;";
	        
	        Cursor cursor=mydb.rawQuery(strQuery, null);
	        arr=new TradeMaster[cursor.getCount()];
	        int i=0;
	        if(cursor.moveToFirst())
		    {
		    	do
		    	{
		    		TradeMaster obj=new TradeMaster();
	        		obj.ID=(String)(cursor.getString(cursor.getColumnIndex("ID")));
	        		obj.TradeType=(String)(cursor.getString(cursor.getColumnIndex("TradeType")));
	        		obj.TradeDescription=(String)(cursor.getString(cursor.getColumnIndex("TradeDescription")));
	        		obj.ParentID=(String)(cursor.getString(cursor.getColumnIndex("ParentID")));
	        		obj.ParentJob=(String)(cursor.getString(cursor.getColumnIndex("ParentJob")));
	        		obj.SeqNo=(String)(cursor.getString(cursor.getColumnIndex("SeqNo")));
	        		obj.ImageName=(String)(cursor.getString(cursor.getColumnIndex("ImageName")));
	        		obj.CreatedBy=(String)(cursor.getString(cursor.getColumnIndex("CreatedBy")));
	        		obj.CreatedDate=(String)(cursor.getString(cursor.getColumnIndex("CreatedDate")));
	        		obj.ModifiedBy=(String)(cursor.getString(cursor.getColumnIndex("ModifiedBy")));
	        		obj.ModifiedDate=(String)(cursor.getString(cursor.getColumnIndex("ModifiedDate")));
	        		obj.EnteredOnMachineID=(String)(cursor.getString(cursor.getColumnIndex("EnteredOnMachineID")));
	        		obj.StatusForUpload=(String)(cursor.getString(cursor.getColumnIndex("StatusForUpload")));
	        		obj.DependencyCount=(int)(cursor.getInt(cursor.getColumnIndex("DependencyCount")));
	        		String str=(String)(cursor.getString(cursor.getColumnIndex("IsParentCompulsory")));
	        		if(str!=null && str.equalsIgnoreCase("true"))
	        			obj.IsParentCompulsory=true;
	        		else
	        			obj.IsParentCompulsory=false;
	        		str=(String)(cursor.getString(cursor.getColumnIndex("IsSyncedToWeb")));
	        		if(str!=null && str.equalsIgnoreCase("true")){
	        			obj.IsSyncedToWeb=true;
	        		}
	        		else
	        			obj.IsSyncedToWeb=false;
	        		
	        		arr[i]=obj;
	        		
	        		i++;
		    	}while(cursor.moveToNext());
		    	
		    }
	        cursor.close();
	        mydb.close();
		}
		catch(Exception e){
			Log.d("Error=", ""+e.getMessage());
		}
		
		
		return arr;
	}
	public TradeDependency[] getDependencyForTradeID(String ID){
		TradeDependency arr[]=null;
		try{

			mydb=context.openOrCreateDatabase(DBNAME, Context.MODE_PRIVATE, null);
			String strQuery;
	        strQuery = "Select * from TradeDependency where TradeID='"+ID+"';";
	        Cursor cursor=mydb.rawQuery(strQuery, null);
	        arr=new TradeDependency[cursor.getCount()];
	        int i=0;
	        if(cursor.moveToFirst()){
	        	do{
	        		
	        		TradeDependency obj=new TradeDependency();
	        		obj.ID=(String)(cursor.getString(cursor.getColumnIndex("ID")));
	        		obj.TradeID=(String)(cursor.getString(cursor.getColumnIndex("TradeID")));
	        		obj.TradeName=(String)(cursor.getString(cursor.getColumnIndex("TradeName")));
	        		obj.DependencyID=(String)(cursor.getString(cursor.getColumnIndex("DependencyID")));
	        		obj.DependencyTradeName=(String)(cursor.getString(cursor.getColumnIndex("DependencyTradeName")));
	        		obj.DependencyType=(String)(cursor.getString(cursor.getColumnIndex("DependencyType")));
	        		obj.DependencyValueComplete=(String)(cursor.getString(cursor.getColumnIndex("DependencyValueComplete")));
	        		obj.CreatedBy=(String)(cursor.getString(cursor.getColumnIndex("CreatedBy")));
	        		obj.CreatedDate=(String)(cursor.getString(cursor.getColumnIndex("CreatedDate")));
	        		obj.ModifiedBy=(String)(cursor.getString(cursor.getColumnIndex("ModifiedBy")));
	        		obj.ModifiedDate=(String)(cursor.getString(cursor.getColumnIndex("ModifiedDate")));
	        		obj.EnteredOnMachineID=(String)(cursor.getString(cursor.getColumnIndex("EnteredOnMachineID")));
	        		obj.StatusForUpload=(String)(cursor.getString(cursor.getColumnIndex("StatusForUpload")));
	        		String str=(String)(cursor.getString(cursor.getColumnIndex("IsSyncedToWeb")));
	        		if(str!=null && str.equalsIgnoreCase("true")){
	        			obj.IsSyncedToWeb=true;
	        		}
	        		else
	        			obj.IsSyncedToWeb=false;
	        		
	        		arr[i]=obj;
	        		
	        		i++;
	        	}
	        	while(cursor.moveToNext());
	        }
	        cursor.close();
	        mydb.close();
		
		}
		catch(Exception e){
			Log.d("Error getDependencyForTradeID=", ""+e.getMessage());
		}
		return arr;
	}
	
	public boolean insertORUpdateTradeAreaMaster(TradeAreaMaster obj){
		try{			
			mydb=context.openOrCreateDatabase(DBNAME, Context.MODE_PRIVATE, null);
			String strQuery;
			strQuery="Select * from TradeAreaMaster where ID='" +obj.ID + "';";
			Cursor cursor=mydb.rawQuery(strQuery, null);
			if(cursor.moveToFirst()){
				strQuery="UPDATE TradeAreaMaster SET BuildingAreaType='" + obj.BuildingAreaType  + "', AptAreaTypeID='" + obj.AptAreaTypeID + "', AptAreaType='" + obj.AptAreaType + "', TradeID'" + obj.TradeID + "', TradeType='" + obj.TradeType  + "', CreatedBy='" + obj.CreatedBy + "', CreatedDate='" + obj.CreatedDate + "', ModifiedBy='" + obj.ModifiedBy + "', ModifiedDate='" + obj.ModifiedDate + "', EnteredOnMachineID='" + obj.EnteredOnMachineID + "', StatusForUpload='" + obj.StatusForUpload  + "', IsSyncedToWeb='" + obj.IsSyncedToWeb + "'  where ID='" + obj.ID + "';";
			}
			else {
				strQuery= "Insert into TradeAreaMaster(ID,BuildingAreaType,AptAreaTypeID,AptAreaType,TradeID,TradeType,CreatedBy,CreatedDate,ModifiedBy,ModifiedDate,EnteredOnMachineID,StatusForUpload,IsSyncedToWeb) values('" + obj.ID + "','" + obj.BuildingAreaType  + "','" + obj.AptAreaTypeID + "','" + obj.AptAreaType + "','" + obj.TradeID + "','" + obj.TradeType  + "','" + obj.CreatedBy + "','" + obj.CreatedDate + "','" + obj.ModifiedBy + "','" + obj.ModifiedDate + "','" + obj.EnteredOnMachineID + "','" + obj.StatusForUpload  + "','" + obj.IsSyncedToWeb + "');";
			}
	        mydb.execSQL(strQuery);
	        cursor.close();
	        mydb.close();
		}
		catch(Exception e){
			Log.d("Error InsertOrUpdateIntoDailyAttendance=", ""+e.getMessage());
	}
		return true;
	
}
	public ContractorMaster[] getAllWorkingContractor()
	{
		ContractorMaster []tmpCM = null;
		try {			
			mydb=context.openOrCreateDatabase(DBNAME, Context.MODE_PRIVATE, null);
			String strQuery;
			strQuery="SELECT * FROM ContractorMaster WHERE ID IN (SELECT DISTINCT ContractorID FROM TradeAptAreaMaster)";
			Cursor cursor=mydb.rawQuery(strQuery, null);
			tmpCM = new ContractorMaster[cursor.getCount()];
			
			int i=0;
			if(cursor.moveToFirst()) {
				do {
					ContractorMaster obj = new ContractorMaster();
			
					obj.ID = (String)(cursor.getString(cursor.getColumnIndex("ID")));
					obj.ContractorName = (String)(cursor.getString(cursor.getColumnIndex("ContractorName")));
					obj.Address1 = (String)(cursor.getString(cursor.getColumnIndex("Address1")));
					obj.Address2 = (String)(cursor.getString(cursor.getColumnIndex("Address2")));
					obj.Pincode = (String)(cursor.getString(cursor.getColumnIndex("Pincode")));
					obj.City = (String)(cursor.getString(cursor.getColumnIndex("City")));
					obj.State = (String)(cursor.getString(cursor.getColumnIndex("State")));
					obj.Contact1 = (String)(cursor.getString(cursor.getColumnIndex("Contact1")));
					obj.Contact2 = (String)(cursor.getString(cursor.getColumnIndex("Contact2")));
					obj.ContactPerson1 = (String)(cursor.getString(cursor.getColumnIndex("ContactPerson1")));
					obj.Mobile1 = (String)(cursor.getString(cursor.getColumnIndex("Mobile1")));
					obj.Email1 = (String)(cursor.getString(cursor.getColumnIndex("Email1")));
					obj.ContactPerson2 = (String)(cursor.getString(cursor.getColumnIndex("ContactPerson2")));
					obj.Mobile2 = (String)(cursor.getString(cursor.getColumnIndex("Mobile2")));
					obj.Email2 = (String)(cursor.getString(cursor.getColumnIndex("Email2")));
					obj.TypeOfJob1 = (String)(cursor.getString(cursor.getColumnIndex("TypeOfJob1")));
					obj.ImageName = (String)(cursor.getString(cursor.getColumnIndex("ImageName")));			
					obj.CreatedBy = (String)(cursor.getString(cursor.getColumnIndex("CreatedBy")));
					obj.CreatedDate = (String)(cursor.getString(cursor.getColumnIndex("CreatedDate")));
					obj.ModifiedBy = (String)(cursor.getString(cursor.getColumnIndex("ModifiedBy")));
					obj.ModifiedDate = (String)(cursor.getString(cursor.getColumnIndex("ModifiedDate")));
					//obj.EnteredOnMachineID = (String)(cursor.getString(cursor.getColumnIndex("EnteredOnMachineID")));    		
					obj.StatusForUpload = (String)(cursor.getString(cursor.getColumnIndex("StatusForUpload")));
    		
					String str = (String)(cursor.getString(cursor.getColumnIndex("IsSyncedToWeb")));
					if(str!=null && str.equalsIgnoreCase("true")) obj.IsSyncedToWeb=true;
					else obj.IsSyncedToWeb=false;
    		
					tmpCM[i]=obj;    		
					i++;
				} while(cursor.moveToNext());
			}
			cursor.close();
			mydb.close();
		}
		catch(Exception e) {
			Log.d("Error InsertOrUpdateIntoDailyAttendance=", ""+e.getMessage());
		}
		return tmpCM;
	}
	
	//@@@@@@@@@@@@@@@@@
	public TradeAptAreaMaster getActiveData(String strPrjID,String strBuilID,String strFloorID,String strAptID,String strFrom)
	{
		TradeAptAreaMaster obj=null;
		try
		{
			mydb=context.openOrCreateDatabase(DBNAME, Context.MODE_PRIVATE, null);
			String strQuery;
			if(strFrom.equalsIgnoreCase("floorlist"))
			{
				strQuery="Select * from TradeAptAreaMaster where ProjectID='"+strPrjID+"' and BuildingID='"+strBuilID+"' order by ModifiedDate DESC;";
			}
			else if(strFrom.equalsIgnoreCase("apartmentlist"))
			{
				strQuery="Select * from TradeAptAreaMaster where ProjectID='"+strPrjID+"' and BuildingID='"+strBuilID+"' and FloorID='"+strFloorID+"' order by ModifiedDate DESC;";
			}
			else // if(strFrom.equalsIgnoreCase("defectlist"))
			{
				strQuery="Select * from TradeAptAreaMaster where ProjectID='"+strPrjID+"' and BuildingID='"+strBuilID+"' and FloorID='"+strFloorID+"' and ApartmentID='"+strAptID+"' order by ModifiedDate DESC;";
			}
				
				
			
			Cursor cursor=mydb.rawQuery(strQuery, null);
			
			if(cursor.moveToFirst())
			{
				obj=new TradeAptAreaMaster();
				obj.ProjectID=cursor.getString(cursor.getColumnIndex("ProjectID"));
				obj.FloorID=cursor.getString(cursor.getColumnIndex("FloorID"));
				obj.ApartmentID=cursor.getString(cursor.getColumnIndex("ApartmentID"));
				obj.AptAreaID=cursor.getString(cursor.getColumnIndex("AptAreaID"));
				obj.BuildingID=cursor.getString(cursor.getColumnIndex("BuildingID"));
				obj.AptAreaType=cursor.getString(cursor.getColumnIndex("AptAreaType"));
				//obj.AptAreaType=cursor.getString(cursor.getColumnIndex("AptAreaType"));
				
				if(cursor.getString(cursor.getColumnIndexOrThrow("PercentageComplete"))!=null)
				{
					String prcntg=cursor.getString(cursor.getColumnIndexOrThrow("PercentageComplete"));
					Double per=0.0;
					if(prcntg!=null)
					per=Double.parseDouble(prcntg);
					obj.PercentageComplete=per;
				}
				else
				{
					obj.PercentageComplete=0.0;
				}
				obj.TradeType=cursor.getString(cursor.getColumnIndex("TradeType"));
			}
			cursor.close();
			mydb.close();
		}
		catch (Exception e)
		{
			Log.d("Exception fmdb TradeAptAreaMaster",e.getMessage());
		}
		return obj;
	}
	public int getActiveSnag(String strPrjID,String strBuildID,String strFloorID,String strAptID,String strFrom)
	{
		int count=0;
		try
		{
			mydb=context.openOrCreateDatabase(DBNAME, Context.MODE_PRIVATE, null);
			String strQuery;
			if(strFrom.equalsIgnoreCase("floorlist"))
			{
				strQuery="Select count(*) As SnagCount from SnagMaster where ProjectID='"+strPrjID+"' and BuildingID='"+strBuildID+"' and (SnagStatus LIKE 'pending' or SnagStatus LIKE 'Reinspected & Unresolved');";
			}
			else if(strFrom.equalsIgnoreCase("apartmentlist"))
			{
				strQuery="Select count(*) As SnagCount from SnagMaster where ProjectID='"+strPrjID+"' and BuildingID='"+strBuildID+"' and FloorID='"+strFloorID+"' and (SnagStatus LIKE 'pending' or SnagStatus LIKE 'Reinspected & Unresolved');";
			}
			else
			{
				strQuery="Select count(*) As SnagCount from SnagMaster where ProjectID='"+strPrjID+"' and BuildingID='"+strBuildID+"' and FloorID='"+strFloorID+"' and ApartmentID='"+strAptID+"' and (SnagStatus LIKE 'pending' or SnagStatus LIKE 'Reinspected & Unresolved');";
			}
			
			
			Cursor cursor=mydb.rawQuery(strQuery, null);
			if(cursor.moveToFirst())
			{
				count=cursor.getInt(cursor.getColumnIndex("SnagCount"));
			}
			cursor.close();
			mydb.close();
			return count;
		}
		catch (Exception e)
		{
			Log.d("Exception fmdb getActiveSnag",e.getMessage());
		}
		return count;
	}
	/*public int getResolvedSnag(String strPrjID,String strBuildID,String strFloorID,String strAptID,String strFrom)
	{
		int cnt=0;
		try
		{
			mydb=context.openOrCreateDatabase(DBNAME, Context.MODE_PRIVATE, null);
			String strQuery;
			if(strFrom.equalsIgnoreCase("floorlist"))
			{
				strQuery="Select count(*) As SnagCount from SnagMaster where ProjectID='"+strPrjID+"' and BuildingID='"+strBuildID+"' and SnagStatus LIKE 'resolved';";
			}
			else if(strFrom.equalsIgnoreCase("apartmentlist"))
			{
				strQuery="Select count(*) As SnagCount from SnagMaster where ProjectID='"+strPrjID+"' and BuildingID='"+strBuildID+"' and FloorID='"+strFloorID+"' and SnagStatus LIKE 'resolved';";
			}
			else
			{
				strQuery="Select count(*) As SnagCount from SnagMaster where ProjectID='"+strPrjID+"' and BuildingID='"+strBuildID+"' and FloorID='"+strFloorID+"' and ApartmentID='"+strAptID+"' and SnagStatus LIKE 'resolved';";
			}
			
			
			Cursor cursor=mydb.rawQuery(strQuery, null);
			if(cursor.moveToFirst())
			{
				cnt=cursor.getInt(cursor.getColumnIndex("SnagCount"));
			}
			cursor.close();
			mydb.close();
			return cnt;
		}
		catch (Exception e)
		{
			Log.d("Exception",""+e.getMessage());
		}
		return cnt;
	}*/
	public String getApartMentNameByID(String strPrjID,String strBuildID,String strFloorID,String strAptID)
	{
		String name="";
		try
		{
			mydb=context.openOrCreateDatabase(DBNAME, Context.MODE_PRIVATE, null);
			String strQuery;
			strQuery="Select ApartmentNo from ApartmentMaster where ProjectID='"+strPrjID+"' and BuildingID='"+strBuildID+"' and FloorID='"+strFloorID+"' and ID='"+strAptID+"';";
			Cursor cursor=mydb.rawQuery(strQuery, null);
			if(cursor.moveToFirst())
			{
				name=cursor.getString(cursor.getColumnIndex("ApartmentNo"));
			}
			cursor.close();
			mydb.close();
		}
		catch (Exception e)
		{
			Log.d("Exception fmdb getApartMentNameByID",e.getMessage());
		}
		return name;
	}
	//@@@@@@@@@@@@@@@@@@
	public ContractorMaster getContractorByTradeType(String TradeType){
		ContractorMaster Con=null;
		try{
			mydb=context.openOrCreateDatabase(DBNAME, Context.MODE_PRIVATE, null);
			String strQuery;
			strQuery="Select * from ContractorMaster where TypeOfJob1='"+TradeType+"';";
			Cursor cursor=mydb.rawQuery(strQuery, null);
			if(cursor.moveToFirst())
			{
				Con=new ContractorMaster();
				Con.ID=cursor.getString(cursor.getColumnIndex("ID"));
				Con.ContractorName=cursor.getString(cursor.getColumnIndex("ContractorName"));
			}
			cursor.close();
			mydb.close();
		}
		catch(Exception e){
			Log.d("Error=",""+e.getMessage());
		}
		return Con;
	}
	//@@@@@@@@@@@@@MANISH
	
	public Boolean syncAllLocalDataToServer(String...strOptionalTableName) {		
		String []strTableNameList;
		
		if(strOptionalTableName.length>0) {
			strTableNameList = new String[strOptionalTableName.length];
			int temp=0;
			for (String strTemp : strOptionalTableName) {
				System.out.println(strTemp);
				strTableNameList[temp] = strTemp;
				temp++;
			}
		}
		else {
			strTableNameList = getAllTableName();
		}
		
		mydb = context.openOrCreateDatabase(DBNAME, Context.MODE_PRIVATE, null);
		
		try {	
		for(int i=0;i<strTableNameList.length;i++) {
			String strQuery = "SELECT * FROM " + strTableNameList[i] + " WHERE UPPER(IsSyncedToWeb) = 'FALSE'";
			Cursor cursor = mydb.rawQuery(strQuery, null);	
			
			//Insert For Prepare String
			String strInsColNames = "";
			String strInsColValues = "";
			
			//Update For Prepare String
			String strUpdColNames = "";
			String strUpdColValues = "";
			String strKeyColNames = "";
			String strKeyColValues = "";
			String strKeyColDataTypes = "";			

			String strTableName = strTableNameList[i]; //Current Table Name
			
			if(cursor.getCount()>0) {
				String strQueryForColumnName = "PRAGMA table_info(" + strTableName + ")";
				Cursor cursorColumnName = mydb.rawQuery(strQueryForColumnName, null);	
				
				if(cursorColumnName.moveToFirst()) {
					do {
						//Getting Current Column Name Info
						String strCurrColName = cursorColumnName.getString(cursorColumnName.getColumnIndex("name"));
						int strCurrColKey = cursorColumnName.getInt(cursorColumnName.getColumnIndex("pk"));
						
						if(!(strCurrColName.equalsIgnoreCase("StatusForUpload") || strCurrColName.equalsIgnoreCase("IsSyncedToWeb"))) {
							if(strInsColNames=="") strInsColNames = strCurrColName;
							else strInsColNames += "~" + strCurrColName;
							
							if(!(strCurrColKey>0)) {
								if(strUpdColNames=="") strUpdColNames = strCurrColName;
								else strUpdColNames += "~" + strCurrColName;
							}
							else {
								if(strKeyColNames=="") strKeyColNames = strCurrColName;
								else strKeyColNames += "~" + strCurrColName;
							
								if(strKeyColDataTypes=="") strKeyColDataTypes = "String";
								else strKeyColDataTypes += "~String";
							}
						}
					} while(cursorColumnName.moveToNext());
				}
			}
				
			if(cursor.moveToFirst()) {
				do {					
					String strCurrColValue = null;
					
					String []tempArr = strInsColNames.split("~");
					for(int j=0;j<tempArr.length;j++) {
						strCurrColValue = cursor.getString(cursor.getColumnIndex(tempArr[j]));
						if(strInsColValues=="") strInsColValues = strCurrColValue;
						else strInsColValues += "~" + strCurrColValue;
					}
					
					String []tempKeyColNameArr = strKeyColNames.split("~");					
					for(int j=0;j<tempKeyColNameArr.length;j++) {
						strCurrColValue = cursor.getString(cursor.getColumnIndex(tempKeyColNameArr[j]));
						
						if(strKeyColValues=="") strKeyColValues = strCurrColValue;
						else strKeyColValues += "~" + strCurrColValue;
					}
					
					String []tempColNameArr = strUpdColNames.split("~");		
					for(int j=0;j<tempColNameArr.length;j++) {
						strCurrColValue = cursor.getString(cursor.getColumnIndex(tempColNameArr[j]));
						
						if(strUpdColValues=="") strUpdColValues = strCurrColValue;
						else strUpdColValues += "~" + strCurrColValue;
					}
					
					Boolean result = null;
					String tempStatusColName = strUpdColNames + "~IsSyncedToWeb";					
					String tempStatusColValue = strUpdColValues + "~TRUE";
					
					if((cursor.getString(cursor.getColumnIndex("StatusForUpload"))).equalsIgnoreCase("INSERTED")) {						
						result = saveNewDataToTheDatabaseServer(strInsColNames,strInsColValues,strTableName);
						if(result) updateDatabase(tempStatusColName, tempStatusColValue, strTableName, strKeyColNames, strKeyColValues, strKeyColDataTypes);
					}
					else if((cursor.getString(cursor.getColumnIndex("StatusForUpload"))).equalsIgnoreCase("MODIFIED")) {
						result = updateDataToTheDatabaseServer(strUpdColNames,strUpdColValues,strTableName,strKeyColNames,strKeyColValues,strKeyColDataTypes);
						if(result) updateDatabase(tempStatusColName, tempStatusColValue, strTableName, strKeyColNames, strKeyColValues, strKeyColDataTypes);
					}
				} while(cursor.moveToNext());
			}
		}
	}
	catch (Exception e) {
		Log.d("Exception", e.getMessage());
		e.printStackTrace();
	}
	return null;
	}
	
	public String[] getAllTableName() {
		String []strTablNameArr = null;

		String strGetAllTableNameInLocalDB = "SELECT DISTINCT TableName FROM TimeStampMaster";
		
		mydb = context.openOrCreateDatabase(DBNAME, Context.MODE_PRIVATE, null);
		Cursor cursor = mydb.rawQuery(strGetAllTableNameInLocalDB, null);	
		
		strTablNameArr = new String[cursor.getCount()];
		int i = 0;
		try {
		if(cursor.moveToFirst()) {
			do {				
				strTablNameArr[i] = cursor.getString(cursor.getColumnIndex("TableName"));
				i++;
			} while(cursor.moveToNext());
		}
		}
		catch (Exception e) {
			Log.d("getAllTableName ", e.getMessage());
			e.printStackTrace();
		}
		mydb.close();
		 return strTablNameArr;
	}
	//Generic Function
	public Boolean updateDatabase(String strColNames,String strColValues,String strTableName,String strKeyColName,String strKeyColValue,String strKeyColDataType) {
		String strQuery;
		String[] strColNamesArr = strColNames.split("~");
		String[] strColValuesArr = strColValues.split("~");
		String[] strKeyColNameArr = strKeyColName.split("~");
		String[] strKeyColValueArr = strKeyColValue.split("~");
		
		strQuery = "UPDATE " + strTableName + " SET ";
		
		try {
			for(int i=0;i<(strColNamesArr.length-1);i++)
				strQuery += strColNamesArr[i] + "='" + strColValuesArr[i] + "', ";
				strQuery += strColNamesArr[(strColNamesArr.length-1)] + "='" + strColValuesArr[(strColNamesArr.length-1)] + "' WHERE ";
			
			if(strKeyColDataType.equalsIgnoreCase("") || strKeyColDataType == null)
			{
				for(int i=0;i<(strKeyColNameArr.length-1);i++)
					strQuery += strKeyColNameArr[i] + "='" + strKeyColValueArr[i] + "', AND ";
					strQuery += strKeyColNameArr[(strKeyColNameArr.length-1)] + "='" + strKeyColValueArr[(strKeyColNameArr.length-1)] + "'";
			}	
			
			Log.d("UPDATE STMT GENERIC FUNCTION : ", strQuery);
		
			mydb=context.openOrCreateDatabase(DBNAME, Context.MODE_PRIVATE, null);
			Cursor cursor = mydb.rawQuery(strQuery, null);	
			
			mydb.execSQL(strQuery);
			cursor.close();
	        mydb.close();
	        return true;
		}
		catch(Exception e) {
			e.printStackTrace();
			return false;
		}
	}	
	
	public Boolean saveNewDataToTheDatabaseServer(String strColNames,String strColValues,String strTableName) {
		Log.d("saveNewDataToTheDatabaseServer strColNames",strColNames);
		Log.d("saveNewDataToTheDatabaseServer strColValues",strColValues);
		Log.d("saveNewDataToTheDatabaseServer strTableName",strTableName);
		Boolean result=true;
		
		return result;
	}
	public Boolean updateDataToTheDatabaseServer(String strColNames,String strColValues,String strTableName,String strKeyColName,String strKeyColValue,String strKeyColDataType) {
		Log.d("updateDataToTheDatabaseServer strColNames",strColNames);
		Log.d("updateDataToTheDatabaseServer strColValues",strColValues);
		Log.d("updateDataToTheDatabaseServer strTableName",strTableName);
		Log.d("updateDataToTheDatabaseServer strKeyColName",strKeyColName);
		Log.d("updateDataToTheDatabaseServer strKeyColValue",strKeyColValue);
		Log.d("updateDataToTheDatabaseServer strKeyColDataType",strKeyColDataType);
		Boolean result=true;
		
		return result;
	}
	/*public Boolean parseDataTable(JSONObject jsonColValues,String strTableName) {		///without update only insert

		String strQuery = "PRAGMA table_info(" + strTableName + ")";		
		String strInsertQuery = "INSERT INTO " + strTableName;
		String strColNames = "";
		String strColValues = "";		
		Boolean success = null;
		
		Log.d("TABLE NAME : " , strTableName);
		
		mydb = context.openOrCreateDatabase(DBNAME, Context.MODE_PRIVATE, null);
		Cursor cursor = mydb.rawQuery(strQuery, null);	
		
		try {		
	
			if(cursor.moveToFirst()) {
				
				do {
					try{
					String strCurrColName = cursor.getString(1).toUpperCase(Locale.ENGLISH);
					String strCurrColValue = "";
					
					if(strTableName.equalsIgnoreCase("tradeareamaster") && strCurrColName.equalsIgnoreCase("TradeType")){
						String str=jsonColValues.getString(strCurrColName);
						if(str.equalsIgnoreCase("BLOCK") || str.equalsIgnoreCase("FRAMING") || str.equalsIgnoreCase("SLAB")  ){
							Log.d("dfdgf","");
						}
					}
					
					if(jsonColValues.has(strCurrColName)==true)
					{
						if(strColNames=="") {
							strColNames = strCurrColName;
							strCurrColValue = jsonColValues.getString(strCurrColName);
							strColValues = "'" + jsonColValues.getString(strCurrColName);
//							if(strCurrColName.equalsIgnoreCase("ID"))
//							{
//								strCurrColValue=strCurrColValue.toLowerCase();
//							}
						}
						else {
							strColNames += "," + strCurrColName;
							strCurrColValue = jsonColValues.getString(strCurrColName);
							strColValues += "','" + jsonColValues.getString(strCurrColName);
//							if(strCurrColName.equalsIgnoreCase("ID"))
//							{
//								strCurrColValue=strCurrColValue.toLowerCase();
//							}
						}
						
						
					}
					else 
					{
						if(strColNames=="") {
							strColNames = strCurrColName;
							if(strColNames.endsWith("ISDATASYNCTOWEB") || strColNames.endsWith("ISSYNCTOWEB")|| strColNames.endsWith("ISSYNCEDTOWEB")) strColValues = "'INSERTED";  //not sure true or inserted
							else if(strColNames.endsWith("STATUSFORUPLOAD")) strColValues = "'TRUE";
							else  strColValues = "'";
							strCurrColValue = "DEFAULT VALUE : BCOZ COL NOT EXISTS AT SERVER";
						}
						else {
							strColNames += "," + strCurrColName;
							if(strColNames.endsWith("ISDATASYNCTOWEB") || strColNames.endsWith("ISSYNCTOWEB")|| strColNames.endsWith("ISSYNCEDTOWEB")) strColValues += "','INSERTED";
							else if(strColNames.endsWith("STATUSFORUPLOAD")) strColValues += "','TRUE";
							else  strColValues +=  "','";
							strCurrColValue = "DEFAULT VALUE : BCOZ COL NOT EXISTS AT SERVER";
						}
					}
					System.out.println("ColNameValue : " + strCurrColName + " :|: " + strCurrColValue);
					}
					catch (Exception e) {
						// TODO: handle exception
					}
				}
				while (cursor.moveToNext());;
			}		
			
			strColValues += "'"; 
			strInsertQuery += " (" + strColNames + ") VALUES(" + strColValues + ")";
		
			mydb.execSQL(strInsertQuery);			
			success = true;
			cursor.close();
			mydb.close();  
		}
		catch (Exception e) {
			success = false;
			cursor.close();
			mydb.close();  
			Log.d("Exception parseDataTable ", e.getMessage());
		}
		cursor.close();
		mydb.close();   
		return success;	
	}*/
	
	//@@@@@@@@@@@@@/MANISH
	
	//@@@@@@Pavan
	
	public Boolean parseDataTable(JSONObject jsonColValues,String strTableName) {	////WithUpdate	

		String strQuery = "PRAGMA table_info(" + strTableName + ")";		
		String strInsertQuery = "INSERT INTO " + strTableName;
		String strUpDtQuery="UPDATE "+strTableName+" SET ";
		String strColNames = "";
		String strUpDtColName = "";
		String strColValues = "";		
		Boolean success = null;
		
		Log.d("TABLE NAME : " , strTableName);
		
		mydb = context.openOrCreateDatabase(DBNAME, Context.MODE_PRIVATE, null);
		Cursor cursor = mydb.rawQuery(strQuery, null);	
		
		try {		
	
			if(cursor.moveToFirst()) {
				
				do {
					try{
					String strCurrColName = cursor.getString(1).toUpperCase(Locale.ENGLISH);
					String strCurrColValue = "";
					
					if(strTableName.equalsIgnoreCase("tradeareamaster") && strCurrColName.equalsIgnoreCase("TradeType")){
						String str=jsonColValues.getString(strCurrColName);
						if(str.equalsIgnoreCase("BLOCK") || str.equalsIgnoreCase("FRAMING") || str.equalsIgnoreCase("SLAB")  ){
							Log.d("dfdgf","");
						}
					}
					
					if(jsonColValues.has(strCurrColName)==true)
					{
						if(strColNames=="") {
							strColNames = strCurrColName;
							strCurrColValue = jsonColValues.getString(strCurrColName);
							strColValues = "'" + jsonColValues.getString(strCurrColName);
//							if(strCurrColName.equalsIgnoreCase("ID"))
//							{
//								strCurrColValue=strCurrColValue.toLowerCase();
//							}
						}
						else {
							strColNames += "," + strCurrColName;
							strCurrColValue = jsonColValues.getString(strCurrColName);
							strColValues += "','" + jsonColValues.getString(strCurrColName);
//							if(strCurrColName.equalsIgnoreCase("ID"))
//							{
//								strCurrColValue=strCurrColValue.toLowerCase();
//							}
						}
						
						
					////for updateprocess
						if(strUpDtColName=="") {
							strUpDtColName = strCurrColName + "='" + jsonColValues.getString(strCurrColName) + "'";
						}
						else
						{
							strUpDtColName += "," + strCurrColName + "='" + jsonColValues.getString(strCurrColName) + "'";
						}
					}
					else 
					{
						if(strColNames=="") {
							strColNames = strCurrColName;
							if(strColNames.endsWith("ISDATASYNCTOWEB") || strColNames.endsWith("ISSYNCTOWEB")|| strColNames.endsWith("ISSYNCEDTOWEB")) strColValues = "'TRUE";  //not sure true or inserted
							else if(strColNames.endsWith("STATUSFORUPLOAD")) strColValues = "'INSERTED";
							else  strColValues = "'";
							strCurrColValue = "DEFAULT VALUE : BCOZ COL NOT EXISTS AT SERVER";
						}
						else {
							strColNames += "," + strCurrColName;
							if(strColNames.endsWith("ISDATASYNCTOWEB") || strColNames.endsWith("ISSYNCTOWEB")|| strColNames.endsWith("ISSYNCEDTOWEB")) strColValues += "','TRUE";
							else if(strColNames.endsWith("STATUSFORUPLOAD")) strColValues += "','INSERTED";
							else  strColValues +=  "','";
							strCurrColValue = "DEFAULT VALUE : BCOZ COL NOT EXISTS AT SERVER";
						}
						if(strUpDtColName=="")
						{
							//strUpDtColName = strCurrColName + "='" + jsonColValues.getString(strCurrColName) + "'";
							if(strColNames.endsWith("ISDATASYNCTOWEB") || strColNames.endsWith("ISSYNCTOWEB")|| strColNames.endsWith("ISSYNCEDTOWEB"))
								strUpDtColName = strCurrColName + "='TRUE'";
							else if(strColNames.endsWith("STATUSFORUPLOAD"))
								strUpDtColName = strCurrColName + "='MODIFIED'";
						}
						else
						{
							if(strColNames.endsWith("ISDATASYNCTOWEB") || strColNames.endsWith("ISSYNCTOWEB")|| strColNames.endsWith("ISSYNCEDTOWEB"))
								strUpDtColName +=","+ strCurrColName + "='TRUE'";
							else if(strColNames.endsWith("STATUSFORUPLOAD"))
								strUpDtColName +=","+ strCurrColName + "='MODIFIED'";
						}
					}
					System.out.println("ColNameValue : " + strCurrColName + " :|: " + strCurrColValue);
					}
					catch (Exception e) {
						// TODO: handle exception
					}
				}
				while (cursor.moveToNext());;
			}		
			
			String strSelctQuery="";
			//boolean isUdt=false;
			String strWherCnd="";
			
			if(strTableName.equalsIgnoreCase("ApartmentAreaType"))
			{
				strWherCnd=" where ID='"+jsonColValues.getString("ID")+"'";
				strSelctQuery="Select * from ApartmentAreaType "+strWherCnd;
				strUpDtQuery=strUpDtQuery+strUpDtColName+strWherCnd;
			}
			else if(strTableName.equalsIgnoreCase("ApartmentDetails"))
			{
				strWherCnd=" where ProjectID='"+jsonColValues.getString("PROJECTID")+"' and BuildingID='"+jsonColValues.getString("BUILDINGID")+"' and FloorID='"+jsonColValues.getString("FLOORID")+"' and ApartmentID='"+jsonColValues.getString("APARTMENTID")+"' and ID='"+jsonColValues.getString("ID")+"'";
				strSelctQuery="Select * from ApartmentDetails "+strWherCnd;
				strUpDtQuery=strUpDtQuery+strUpDtColName+strWherCnd;
			}
			else if(strTableName.equalsIgnoreCase("ApartmentMaster"))
			{
				strWherCnd=" where ProjectID='"+jsonColValues.getString("PROJECTID")+"' and BuildingID='"+jsonColValues.getString("BUILDINGID")+"' and FloorID='"+jsonColValues.getString("FLOORID")+"' and ID='"+jsonColValues.getString("ID")+"' ";
				strSelctQuery="Select * from ApartmentMaster "+strWherCnd;
				strUpDtQuery=strUpDtQuery+strUpDtColName+strWherCnd;
			}
			else if(strTableName.equalsIgnoreCase("AttachmentDetails"))
			{
				strWherCnd=" where AttachmentID='"+jsonColValues.getString("ATTACHMENTID")+"'";
				strSelctQuery="Select * from AttachmentDetails "+strWherCnd;
				strUpDtQuery=strUpDtQuery+strUpDtColName+strWherCnd;
			}
			else if(strTableName.equalsIgnoreCase("BuilderMaster"))
			{
				strWherCnd=" where ID='"+jsonColValues.getString("ID")+"'";
				strSelctQuery="Select * from BuilderMaster "+strWherCnd;
				strUpDtQuery=strUpDtQuery+strUpDtColName+strWherCnd;
			}
			else if(strTableName.equalsIgnoreCase("BuildingMaster"))
			{
				strWherCnd=" where ID='"+jsonColValues.getString("ID")+"' and ProjectID='"+jsonColValues.getString("PROJECTID")+"'";
				strSelctQuery="Select * from BuildingMaster "+strWherCnd;
				strUpDtQuery=strUpDtQuery+strUpDtColName+strWherCnd;
			}
			else if(strTableName.equalsIgnoreCase("ContractorMaster"))
			{
				strWherCnd=" where ID='"+jsonColValues.getString("ID")+"' ";
				strSelctQuery="Select * from ContractorMaster "+strWherCnd;
				strUpDtQuery=strUpDtQuery+strUpDtColName+strWherCnd;
			}
			else if(strTableName.equalsIgnoreCase("DailyAttendance"))
			{
				strWherCnd=" where ID='"+jsonColValues.getString("ID")+"' ";
				strSelctQuery="Select * from DailyAttendance "+strWherCnd;
				strUpDtQuery=strUpDtQuery+strUpDtColName+strWherCnd;
			}
			else if(strTableName.equalsIgnoreCase("Employee"))
			{
				strWherCnd=" where EmpCode='"+jsonColValues.getString("EMPCODE")+"' ";
				strSelctQuery="Select * from Employee "+strWherCnd;
				strUpDtQuery=strUpDtQuery+strUpDtColName+strWherCnd;
			}
			else if(strTableName.equalsIgnoreCase("FloorMaster"))
			{
				strWherCnd=" where ID='"+jsonColValues.getString("ID")+"' and ProjectID='"+jsonColValues.getString("PROJECTID")+"' and BuildingID='"+jsonColValues.getString("BUILDINGID")+"'";
				strSelctQuery="Select * from FloorMaster "+strWherCnd;
				strUpDtQuery=strUpDtQuery+strUpDtColName+strWherCnd;
			}
			else if(strTableName.equalsIgnoreCase("Inspector"))
			{
				strWherCnd=" where ID='"+jsonColValues.getString("ID")+"' ";
				strSelctQuery="Select * from Inspector "+strWherCnd;
				strUpDtQuery=strUpDtQuery+strUpDtColName+strWherCnd;
			}
			else if(strTableName.equalsIgnoreCase("JobMaster"))
			{
				strWherCnd=" where ID='"+jsonColValues.getString("ID")+"' and ProjectID='"+jsonColValues.getString("PROJECTID")+"'";
				strSelctQuery="Select * from JobMaster "+strWherCnd;
				strUpDtQuery=strUpDtQuery+strUpDtColName+strWherCnd;
			}
			else if(strTableName.equalsIgnoreCase("ProjectMaster"))
			{
				strWherCnd=" where ID='"+jsonColValues.getString("ID")+"' ";
				strSelctQuery="Select * from ProjectMaster "+strWherCnd;
				strUpDtQuery=strUpDtQuery+strUpDtColName+strWherCnd;
			}
			else if(strTableName.equalsIgnoreCase("SnagChecklistEntries"))
			{
				strWherCnd=" where ID='"+jsonColValues.getString("ID")+"' ";
				strSelctQuery="Select * from SnagChecklistEntries "+strWherCnd;
				strUpDtQuery=strUpDtQuery+strUpDtColName+strWherCnd;
			}
			else if(strTableName.equalsIgnoreCase("QCC"))
			{
				strWherCnd=" where ID='"+jsonColValues.getString("ID")+"' ";
				strSelctQuery="Select * from QCC "+strWherCnd;
				strUpDtQuery=strUpDtQuery+strUpDtColName+strWherCnd;
			}
			else if(strTableName.equalsIgnoreCase("SnagMaster"))
			{
				strWherCnd=" where ID='"+jsonColValues.getString("ID")+"' ";
				strSelctQuery="Select * from SnagMaster "+strWherCnd;
				strUpDtQuery=strUpDtQuery+strUpDtColName+strWherCnd;
			}
			else if(strTableName.equalsIgnoreCase("SnagStatus"))
			{
				strWherCnd=" where ID='"+jsonColValues.getString("ID")+"' ";
				strSelctQuery="Select * from SnagStatus "+strWherCnd;
				strUpDtQuery=strUpDtQuery+strUpDtColName+strWherCnd;
			}
			else if(strTableName.equalsIgnoreCase("SubContractorMaster"))
			{
				strWherCnd=" where ContractorID='"+jsonColValues.getString("CONTRACTORID")+"' and ID='"+jsonColValues.getString("ID")+"'";
				strSelctQuery="Select * from SubContractorMaster "+strWherCnd;
				strUpDtQuery=strUpDtQuery+strUpDtColName+strWherCnd;
			}
			else if(strTableName.equalsIgnoreCase("SubSubContractor"))
			{
				strWherCnd=" where ID='"+jsonColValues.getString("ID")+"' and ContractorID='"+jsonColValues.getString("CONTRACTORID")+"'";
				strSelctQuery="Select * from SubSubContractor "+strWherCnd;
				strUpDtQuery=strUpDtQuery+strUpDtColName+strWherCnd;
			}
			else if(strTableName.equalsIgnoreCase("TradeAptAreaDetail"))
			{
				strWherCnd=" where ID='"+jsonColValues.getString("ID")+"' ";
				strSelctQuery="Select * from TradeAptAreaDetail "+strWherCnd;
				strUpDtQuery=strUpDtQuery+strUpDtColName+strWherCnd;
			}
			else if(strTableName.equalsIgnoreCase("TradeAptAreaMaster"))
			{
				strWherCnd=" where ID='"+jsonColValues.getString("ID")+"' ";
				strSelctQuery="Select * from TradeAptAreaMaster "+strWherCnd;
				strUpDtQuery=strUpDtQuery+strUpDtColName+strWherCnd;
			}
			else if(strTableName.equalsIgnoreCase("TradeAreaMaster"))
			{
				strWherCnd=" where ID='"+jsonColValues.getString("ID")+"' ";
				strSelctQuery="Select * from TradeAreaMaster "+strWherCnd;
				strUpDtQuery=strUpDtQuery+strUpDtColName+strWherCnd;
			}
			else if(strTableName.equalsIgnoreCase("TradeDependency"))
			{
				strWherCnd=" where ID='"+jsonColValues.getString("ID")+"' ";
				strSelctQuery="Select * from TradeDependency "+strWherCnd;
				strUpDtQuery=strUpDtQuery+strUpDtColName+strWherCnd;
			}
			else if(strTableName.equalsIgnoreCase("TradeDetail"))
			{
				strWherCnd=" where ID='"+jsonColValues.getString("ID")+"' ";
				strSelctQuery="Select * from TradeDetail "+strWherCnd;
				strUpDtQuery=strUpDtQuery+strUpDtColName+strWherCnd;
			}
			else if(strTableName.equalsIgnoreCase("TradeFloorDetail"))
			{
				strWherCnd=" where ID='"+jsonColValues.getString("ID")+"' ";
				strSelctQuery="Select * from TradeFloorDetail "+strWherCnd;
				strUpDtQuery=strUpDtQuery+strUpDtColName+strWherCnd;
			}
			else if(strTableName.equalsIgnoreCase("TradeMaster"))
			{
				strWherCnd=" where ID='"+jsonColValues.getString("ID")+"' ";
				strSelctQuery="Select * from TradeMaster "+strWherCnd;
				strUpDtQuery=strUpDtQuery+strUpDtColName+strWherCnd;
			}
			
			
			strColValues += "'"; 
			strInsertQuery += " (" + strColNames + ") VALUES(" + strColValues + ")";
		
			Cursor selCursor=mydb.rawQuery(strSelctQuery, null);
			if(selCursor.moveToFirst())
			{
				mydb.execSQL(strUpDtQuery);
				//successint=1;
				Log.d("Updated parseDataTable ", "");
			}
			else
			{
				mydb.execSQL(strInsertQuery);	
				//successint=0;
				Log.d("Inserted parseDataTable ", "");
			}
			//mydb.execSQL(strInsertQuery);	
			selCursor.close();
			success = true;
			cursor.close();
			mydb.close();  
		}
		catch (Exception e) {
			success = false;
			cursor.close();
			mydb.close();  
			Log.d("Exception parseDataTable ", e.getMessage());
		}
		cursor.close();
		mydb.close();   
		return success;	
	}
	
	
	
	
	public TradeMaster[] getExtTradeMasterTradeAptAreaMaster(String prjID,String bldgID,String AreaType)
	{
		TradeMaster arr[]=null;
		try{
			mydb=context.openOrCreateDatabase(DBNAME, Context.MODE_PRIVATE, null);
			String strQuery;
	        strQuery = "select * from trademaster where id in (Select TradeID from TradeAptAreaMaster where ProjectID='"+prjID+"' and BuildingID='"+bldgID+"' and AptAreaType='"+AreaType+"') order by seqno;";
	        
	        Cursor cursor=mydb.rawQuery(strQuery, null);
	        arr=new TradeMaster[cursor.getCount()];
	        int i=0;
	        if(cursor.moveToFirst())
		    {
		    	do
		    	{
		    		TradeMaster obj=new TradeMaster();
	        		obj.ID=(String)(cursor.getString(cursor.getColumnIndex("ID")));
	        		obj.TradeType=(String)(cursor.getString(cursor.getColumnIndex("TradeType")));
	        		obj.TradeDescription=(String)(cursor.getString(cursor.getColumnIndex("TradeDescription")));
	        		obj.ParentID=(String)(cursor.getString(cursor.getColumnIndex("ParentID")));
	        		obj.ParentJob=(String)(cursor.getString(cursor.getColumnIndex("ParentJob")));
	        		obj.SeqNo=(String)(cursor.getString(cursor.getColumnIndex("SeqNo")));
	        		obj.ImageName=(String)(cursor.getString(cursor.getColumnIndex("ImageName")));
	        		obj.CreatedBy=(String)(cursor.getString(cursor.getColumnIndex("CreatedBy")));
	        		obj.CreatedDate=(String)(cursor.getString(cursor.getColumnIndex("CreatedDate")));
	        		obj.ModifiedBy=(String)(cursor.getString(cursor.getColumnIndex("ModifiedBy")));
	        		obj.ModifiedDate=(String)(cursor.getString(cursor.getColumnIndex("ModifiedDate")));
	        		obj.EnteredOnMachineID=(String)(cursor.getString(cursor.getColumnIndex("EnteredOnMachineID")));
	        		obj.StatusForUpload=(String)(cursor.getString(cursor.getColumnIndex("StatusForUpload")));
	        		obj.DependencyCount=(int)(cursor.getInt(cursor.getColumnIndex("DependencyCount")));
	        		String str=(String)(cursor.getString(cursor.getColumnIndex("IsParentCompulsory")));
	        		if(str!=null && str.equalsIgnoreCase("true"))
	        			obj.IsParentCompulsory=true;
	        		else
	        			obj.IsParentCompulsory=false;
	        		str=(String)(cursor.getString(cursor.getColumnIndex("IsSyncedToWeb")));
	        		if(str!=null && str.equalsIgnoreCase("true")){
	        			obj.IsSyncedToWeb=true;
	        		}
	        		else
	        			obj.IsSyncedToWeb=false;
	        		
	        		arr[i]=obj;
	        		
	        		i++;
		    	}while(cursor.moveToNext());
		    	
		    }
	        cursor.close();
	        mydb.close();
		}
		catch(Exception e){
			Log.d("Error=", ""+e.getMessage());
		}
		
		
		return arr;
	}
	public TradeAptAreaMaster getExtTradeAptAreaMaster(String prjID,String bldgID,String AreaType,String tradeID)
	{
		TradeAptAreaMaster arr=null;
		try{
			mydb=context.openOrCreateDatabase(DBNAME, Context.MODE_PRIVATE, null);
			String strQuery;
	        strQuery = "Select * from TradeAptAreaMaster where ProjectID='"+prjID+"' and BuildingID='"+bldgID+"' and AptAreaType='"+AreaType+"' and TradeType='"+tradeID+"'";// where ProjectID='"+ProjectID+"' Order by ID";
	        Cursor cursor=mydb.rawQuery(strQuery, null);
	        
	        int i=0;
	        if(cursor.moveToFirst()){
	        	//do{
	        		arr=new TradeAptAreaMaster();
	        		TradeAptAreaMaster obj=new TradeAptAreaMaster();
	        		obj.ID=(String)(cursor.getString(cursor.getColumnIndex("ID")));
	        		obj.ProjectID=(String)(cursor.getString(cursor.getColumnIndex("ProjectID")));
	        		obj.BuildingID=(String)(cursor.getString(cursor.getColumnIndex("BuildingID")));
	        		//obj.FloorID=(String)(cursor.getString(cursor.getColumnIndex("FloorID")));
	        		//obj.ApartmentID=(String)(cursor.getString(cursor.getColumnIndex("ApartmentID")));
	        		//obj.AptAreaID=(String)(cursor.getString(cursor.getColumnIndex("AptAreaID")));
	        		//obj.AptAreaTypeID=(String)(cursor.getString(cursor.getColumnIndex("AptAreaTypeID")));
	        		//obj.AptAreaType=(String)(cursor.getString(cursor.getColumnIndex("AptAreaType")));
	        		obj.TradeID=(String)(cursor.getString(cursor.getColumnIndex("TradeID")));
	        		obj.TradeType=(String)(cursor.getString(cursor.getColumnIndex("TradeType")));
	        		String str2=(String)(cursor.getString(cursor.getColumnIndex("PercentageComplete")));
	        		double d=0.0;
	        		if(str2!=null)
	        			d=Double.parseDouble(str2);
	        		obj.PercentageComplete=d;
	        		obj.CreatedBy=(String)(cursor.getString(cursor.getColumnIndex("CreatedBy")));
	        		obj.CreatedDate=(String)(cursor.getString(cursor.getColumnIndex("CreatedDate")));
	        		obj.ModifiedBy=(String)(cursor.getString(cursor.getColumnIndex("ModifiedBy")));
	        		obj.ModifiedDate=(String)(cursor.getString(cursor.getColumnIndex("ModifiedDate")));
	        		obj.EnteredOnMachineID=(String)(cursor.getString(cursor.getColumnIndex("EnteredOnMachineID")));
	        		
	        		obj.StatusForUpload=(String)(cursor.getString(cursor.getColumnIndex("StatusForUpload")));
	        		String str=(String)(cursor.getString(cursor.getColumnIndex("IsSyncedToWeb")));
	        		if(str!=null && str.equalsIgnoreCase("true"))
	        			obj.IsSyncedToWeb=true;
	        		else
	        			obj.IsSyncedToWeb=false;
	        		arr=obj;
	        		
	        		i++;
	        	//}
	        	//while(cursor.moveToNext());
	        }
	        cursor.close();
	        mydb.close();
		}
		catch(Exception e){
			Log.d("Error=", ""+e.getMessage());
		}
		
		
		return arr;
	}

	//@@@@@@@@Pavan Close
	
	
	////@@@@@27/2/14 pavan
	
	public ProjectMaster[] getProjectsWithSngCnt()
	{
		ProjectMaster arr[]=null;
		try{
			mydb=context.openOrCreateDatabase(DBNAME, Context.MODE_PRIVATE, null);
			String strQuery;
	        strQuery = "Select * from ProjectMaster Order by ID";
	        Cursor cursor=mydb.rawQuery(strQuery, null);
	        arr=new ProjectMaster[cursor.getCount()];
	        int i=0;
	        if(cursor.moveToFirst()){
	        	do{
	        		ProjectMaster obj=new ProjectMaster();
	        		obj.setID(cursor.getString(cursor.getColumnIndex("ID")));
	        		obj.setProjectName(cursor.getString(cursor.getColumnIndex("ProjectName")));
	        		obj.setNoOfBuildings(cursor.getInt(cursor.getColumnIndex("NoOfBuildings")));
	        		obj.setLocation(cursor.getString(cursor.getColumnIndex("Location")));
	        		obj.setAddress1(cursor.getString(cursor.getColumnIndex("Address1")));
	        		obj.setAddress2(cursor.getString(cursor.getColumnIndex("Address2")));
	        		obj.setPincode(cursor.getString(cursor.getColumnIndex("Pincode")));
	        		obj.setCity(cursor.getString(cursor.getColumnIndex("City")));
	        		obj.setState(cursor.getString(cursor.getColumnIndex("State")));
	        		obj.setImageName(cursor.getString(cursor.getColumnIndex("ImageName")));
	        		obj.setAbout(cursor.getString(cursor.getColumnIndex("About")));
	        		obj.setBuilderID(cursor.getString(cursor.getColumnIndex("BuilderID")));
					obj.setBuilderName(cursor.getString(cursor.getColumnIndex("BuilderName")));
					String strQuery2="Select count(*) from SnagMaster Where ProjectID='"+obj.getID()+"'";
					Cursor cursor2=mydb.rawQuery(strQuery2, null);
					if(cursor2.moveToFirst())
						obj.SnagCount=cursor2.getInt(0);
					else
						obj.SnagCount=0;
					
					cursor2.close();
					
					String strQury4="Select * from TradeAptAreaMaster where ProjectID='"+obj.getID()+"'";
					Cursor cursor3=mydb.rawQuery(strQury4, null);
					if(cursor3.moveToFirst())
						obj.isInspStarted=true;
					else
						obj.isInspStarted=false;
					cursor3.close();
	        		arr[i]=obj;
	        		
	        		i++;
	        	}
	        	while(cursor.moveToNext());
	        }
	        cursor.close();
	        mydb.close();
		}
		catch(Exception e){
			
		}
		
		
		return arr;
	}
	
	public BuildingMaster[] getBuildingsWthSngCount(String ProjectID)
	{
		BuildingMaster arr[]=null;
		try{
			mydb=context.openOrCreateDatabase(DBNAME, Context.MODE_PRIVATE, null);
			String strQuery;
	        strQuery = "Select * from BuildingMaster where ProjectID='"+ProjectID+"' Order by ID";
	        Cursor cursor=mydb.rawQuery(strQuery, null);
	        arr=new BuildingMaster[cursor.getCount()];
	        int i=0;
	        if(cursor.moveToFirst()){
	        	do{
	        		BuildingMaster obj=new BuildingMaster();
	        		obj.setID(cursor.getString(cursor.getColumnIndex("ID")));
	        		obj.setBuildingName(cursor.getString(cursor.getColumnIndex("BuildingName")));
	        		obj.setNoOfElevators(cursor.getInt(cursor.getColumnIndex("NoOfElevators")));
	        		obj.setNoOfFloors(cursor.getInt(cursor.getColumnIndex("NoOfFloors")));
	        		obj.setProjectID(cursor.getString(cursor.getColumnIndex("ProjectID")));
	        		obj.setProjectName(cursor.getString(cursor.getColumnIndex("ProjectName")));
	        		obj.setImageName(cursor.getString(cursor.getColumnIndex("ImageName")));
	        		obj.setBuildingType(cursor.getString(cursor.getColumnIndex("BuildingType")));
	        		
	        		String strQuery2="Select count(*) from SnagMaster Where ProjectID='"+obj.getProjectID()+"' and BuildingID='"+obj.getID()+"'";
					Cursor cursor2=mydb.rawQuery(strQuery2, null);
					if(cursor2.moveToFirst())
						obj.SnagCount=cursor2.getInt(0);
					else
						obj.SnagCount=0;
					
					cursor2.close();
					
					String strQury4="Select * from TradeAptAreaMaster where ProjectID='"+obj.getProjectID()+"' and BuildingID='"+obj.getID()+"'";
					Cursor cursor3=mydb.rawQuery(strQury4, null);
					if(cursor3.moveToFirst())
						obj.isInspStarted=true;
					else
						obj.isInspStarted=false;
					cursor3.close();
	        		
	        		arr[i]=obj;
	        		
	        		i++;
	        	}
	        	while(cursor.moveToNext());
	        }
	        cursor.close();
	        mydb.close();
		}
		catch(Exception e){
			Log.d("Error=", ""+e.getMessage());
		}
		
		
		return arr;
	}
	
	
	
	public FloorMaster[] getFloorsWithSngCnt(BuildingMaster Bldg){
		FloorMaster[] arr=null;
		try{
			mydb=context.openOrCreateDatabase(DBNAME, Context.MODE_PRIVATE, null);
			String strQuery;
	        strQuery = "Select * from FloorMaster where BuildingID='"+Bldg.getID()+"' and ProjectID='"+Bldg.getProjectID()+"' Order by ID";
	        Cursor cursor=mydb.rawQuery(strQuery, null);
	        arr=new FloorMaster[cursor.getCount()];
	        int i=0;
	        if(cursor.moveToFirst()){
	        	do{
	        		FloorMaster obj=new FloorMaster();
	        		obj.setID(cursor.getString(cursor.getColumnIndex("ID")));
	        		obj.setNoOfApartments(cursor.getInt(cursor.getColumnIndex("NoOfApartments")));
	        		obj.setFloor(cursor.getString(cursor.getColumnIndex("Floor")));
	        		obj.setBuildingID(cursor.getString(cursor.getColumnIndex("BuildingID")));
	        		obj.setBuildingName(cursor.getString(cursor.getColumnIndex("BuildingName")));
	        		obj.setProjectID(cursor.getString(cursor.getColumnIndex("ProjectID")));
	        		obj.setProjectName(cursor.getString(cursor.getColumnIndex("ProjectName")));
	        		obj.setImageName(cursor.getString(cursor.getColumnIndex("ImageName")));
	        		obj.setFloorPlanImage(cursor.getString(cursor.getColumnIndex("FloorPlanImage")));
	        		try{
	        			obj.FloorType=cursor.getString(cursor.getColumnIndex("FloorType"));
	        		}
	        		catch(Exception e)
	        		{
	        			Log.d("Exception",""+e.getMessage());
	        		}
	        		
	        		
	        		String strQuery2="Select count(*) from SnagMaster Where ProjectID='"+obj.getProjectID()+"' and BuildingID='"+obj.getBuildingID()+"' and FloorID='"+obj.getID()+"'";
					Cursor cursor2=mydb.rawQuery(strQuery2, null);
					if(cursor2.moveToFirst())
						obj.SnagCount=cursor2.getInt(0);
					else
						obj.SnagCount=0;
					
					cursor2.close();
					
					String strQury4="Select * from TradeAptAreaMaster where ProjectID='"+obj.getProjectID()+"' and BuildingID='"+obj.getBuildingID()+"' and FloorID='"+obj.getID()+"'";
					Cursor cursor3=mydb.rawQuery(strQury4, null);
					if(cursor3.moveToFirst())
						obj.isInspStarted=true;
					else
						obj.isInspStarted=false;
					cursor3.close();
					
	        		arr[i]=obj;
	        		
	        		i++;
	        	}
	        	while(cursor.moveToNext());
	        }
	        cursor.close();
	        mydb.close();
		}
		catch(Exception e){
			Log.d("Error=", ""+e.getMessage());
		}
		return arr;
	}
	
	public ApartmentMaster[] getApartmentsWithSnag(FloorMaster floor){
		ApartmentMaster[] arr=null;
		try{
			mydb=context.openOrCreateDatabase(DBNAME, Context.MODE_PRIVATE, null);
			String strQuery;
	        strQuery = "Select * from ApartmentMaster where FloorID='"+floor.getID()+"' and BuildingID='"+floor.getBuildingID()+"' and ProjectID='"+floor.getProjectID()+"' Order by ID";
	        Cursor cursor=mydb.rawQuery(strQuery, null);
	        arr=new ApartmentMaster[cursor.getCount()];
	        int i=0;
	        if(cursor.moveToFirst()){
	        	do{
	        		ApartmentMaster obj=new ApartmentMaster();
	        		obj.setID(cursor.getString(cursor.getColumnIndex("ID")));
	        		obj.setApartmentNo(cursor.getString(cursor.getColumnIndex("ApartmentNo")));
	        		
	        		obj.setFloorID(cursor.getString(cursor.getColumnIndex("FloorID")));
	        		obj.setFloor(cursor.getString(cursor.getColumnIndex("Floor")));
	        		obj.setBuildingID(cursor.getString(cursor.getColumnIndex("BuildingID")));
	        		obj.setBuildingName(cursor.getString(cursor.getColumnIndex("BuildingName")));
	        		obj.setProjectID(cursor.getString(cursor.getColumnIndex("ProjectID")));
	        		obj.setProjectName(cursor.getString(cursor.getColumnIndex("ProjectName")));
	        		obj.setAptPlanID(cursor.getString(cursor.getColumnIndex("AptPlanID")));
	        		obj.setAptPlanName(cursor.getString(cursor.getColumnIndex("AptPlanName")));
	        		obj.setAptType(cursor.getString(cursor.getColumnIndex("AptType")));
	        		obj.setImageName(cursor.getString(cursor.getColumnIndex("ImageName")));
	        		
	        		String strQuery2="Select count(*) from SnagMaster Where ProjectID='"+obj.getProjectID()+"' and BuildingID='"+obj.getBuildingID()+"' and FloorID='"+obj.getFloorID()+"' and ApartmentID='"+obj.getID()+"'";
					Cursor cursor2=mydb.rawQuery(strQuery2, null);
					if(cursor2.moveToFirst())
						obj.SnagCount=cursor2.getInt(0);
					else
						obj.SnagCount=0;
					
					cursor2.close();
					
					String strQury4="Select * from TradeAptAreaMaster where ProjectID='"+obj.getProjectID()+"' and BuildingID='"+obj.getBuildingID()+"' and FloorID='"+obj.getFloorID()+"' and ApartmentID='"+obj.getID()+"'";
					Cursor cursor3=mydb.rawQuery(strQury4, null);
					if(cursor3.moveToFirst())
						obj.isInspStarted=true;
					else
						obj.isInspStarted=false;
					cursor3.close();
	        		
	        		arr[i]=obj;
	        		
	        		i++;
	        	}
	        	while(cursor.moveToNext());
	        }
	        cursor.close();
	        mydb.close();
		}
		catch(Exception e){
			Log.d(" getApartments=", ""+e.getMessage());
		}
		return arr;
	}
	public String[] getTradeDetailsByTradeTypeNew(String tradeID,ApartmentMaster currFlr,String AptareaID,String strTrdmstID)
	{
		
		
		String[] arr=null;
		try
		{
			mydb=context.openOrCreateDatabase(DBNAME, Context.MODE_PRIVATE, null);
			String strQuery;
			String strFltr="";
			if(AptareaID!=null && AptareaID.length()>0)
				strFltr="and AptAreaID='"+AptareaID+"'";
			strQuery="Select distinct inspectiongroup from TradeDetail where ID in(Select DISTINCT TradeDetailID from TradeAptAreaDetail where TradeAptAreaMasterID IN(Select DISTINCT ID from TradeAptAreaMaster where ProjectID='"+currFlr.getProjectID()+"' and BuildingID='"+currFlr.getBuildingID()+"' and FloorID='"+currFlr.getFloorID()+"' and ApartmentID='"+currFlr.getID()+"' "+strFltr+")) and TradeMasterID='"+strTrdmstID+"'  order by SeqNo;";
			Cursor cursor=mydb.rawQuery(strQuery, null);
			arr=new String[cursor.getCount()];
			int i=0;
		    if(cursor.moveToFirst())
		    {
		    	do
		    	{
		    		String obj=new String();
	        		//obj.ID=(String)(cursor.getString(cursor.getColumnIndex("ID")));
	        		obj=(String)(cursor.getString(0));
//	        		obj.TradeMasterID=(String)(cursor.getString(cursor.getColumnIndex("TradeMasterID")));
//	        		obj.InspectionGroup=(String)(cursor.getString(cursor.getColumnIndex("InspectionGroup")));
//	        		obj.InspectionDescription=(String)(cursor.getString(cursor.getColumnIndex("InspectionDescription")));
	        		//int a=(int)(cursor.getInt(cursor.getColumnIndex("SeqNo")));
//	        		
//	        		obj.CreatedBy=(String)(cursor.getString(cursor.getColumnIndex("CreatedBy")));
//	        		obj.CreatedDate=(String)(cursor.getString(cursor.getColumnIndex("CreatedDate")));
//	        		obj.ModifiedBy=(String)(cursor.getString(cursor.getColumnIndex("ModifiedBy")));
//	        		obj.ModifiedDate=(String)(cursor.getString(cursor.getColumnIndex("ModifiedDate")));
//	        		obj.EnteredOnMachineID=(String)(cursor.getString(cursor.getColumnIndex("EnteredOnMachineID")));
//	        		obj.StatusForUpload=(String)(cursor.getString(cursor.getColumnIndex("StatusForUpload")));
//	        		String str=(String)(cursor.getString(cursor.getColumnIndex("IsSyncedToWeb")));
//	        		if(str!=null && str.equalsIgnoreCase("true")){
//	        			obj.IsSyncedToWeb=true;
//	        		}
//	        		else
//	        			obj.IsSyncedToWeb=false;
	        		
	        		arr[i]=obj;
	        		
	        		i++;
		    	}while(cursor.moveToNext());
		    	
		    }
		    cursor.close();
		    mydb.close();
		}
		catch (Exception e)
		{
			Log.d("Exception fmdb getExtBuildingDetails",""+e.getMessage());
		}
		return arr;
	}
	
	
	/////@@@@pavan 01/03/14
	public SnagMaster[] getExternalAreaFloorSnags(String strPrjID,String strBuildID,String strFloorID)
	{
		SnagMaster[] arr=null;
		try
		{
			mydb=context.openOrCreateDatabase(DBNAME, Context.MODE_PRIVATE, null);
			String strQuery;
			strQuery="Select * from SnagMaster where ProjectID='"+strPrjID+"' and BuildingID='"+strBuildID+"' and FloorID='"+strFloorID+"' Order by ReportDate Desc;";
			Cursor cursor=mydb.rawQuery(strQuery, null);
		    arr=new SnagMaster[cursor.getCount()];
		    int i=0;
		    if(cursor.moveToFirst())
		    {
	        	do{
	        		SnagMaster obj=new SnagMaster();
	        		obj.setID(cursor.getString(cursor.getColumnIndex("ID")));
	        		obj.setSnagType(cursor.getString(cursor.getColumnIndex("SnagType")));
	        		obj.setSnagDetails(cursor.getString(cursor.getColumnIndex("SnagDetails")));
	        		obj.setPictureURL1(cursor.getString(cursor.getColumnIndex("PictureURL1")));
	        		obj.setPictureURL2(cursor.getString(cursor.getColumnIndex("PictureURL2")));
	        		obj.setPictureURL3(cursor.getString(cursor.getColumnIndex("PictureURL3")));
	        		obj.setProjectID(cursor.getString(cursor.getColumnIndex("ProjectID")));
	        		obj.setProjectName(cursor.getString(cursor.getColumnIndex("ProjectName")));
	        		obj.setBuildingID(cursor.getString(cursor.getColumnIndex("BuildingID")));
	        		obj.setBuildingName(cursor.getString(cursor.getColumnIndex("BuildingName")));
	        		obj.setFloorID(cursor.getString(cursor.getColumnIndex("FloorID")));
	        		obj.setFloor(cursor.getString(cursor.getColumnIndex("Floor")));
	        		obj.setApartmentID(cursor.getString(cursor.getColumnIndex("ApartmentID")));
	        		obj.setApartment(cursor.getString(cursor.getColumnIndex("Apartment")));
	        		obj.setAptAreaName(cursor.getString(cursor.getColumnIndex("AptAreaName")));
	        		obj.setAptAreaID(cursor.getString(cursor.getColumnIndex("AptAreaID")));
	        		obj.setReportDate(cursor.getString(cursor.getColumnIndex("ReportDate")));
	        		obj.setSnagStatus(cursor.getString(cursor.getColumnIndex("SnagStatus")));
	        		obj.setResolveDate(cursor.getString(cursor.getColumnIndex("ResolveDate")));
	        		obj.setInspectorID(cursor.getString(cursor.getColumnIndex("InspectorID")));
	        		obj.setInspectorName(cursor.getString(cursor.getColumnIndex("InspectorName")));
	        		obj.setResolveDatePictureURL1(cursor.getString(cursor.getColumnIndex("ResolveDatePictureURL1")));
	        		obj.setResolveDatePictureURL2(cursor.getString(cursor.getColumnIndex("ResolveDatePictureURL2")));
	        		obj.setResolveDatePictureURL3(cursor.getString(cursor.getColumnIndex("ResolveDatePictureURL3")));
	        		obj.setReInspectedUnresolvedDate(cursor.getString(cursor.getColumnIndex("ReinspectedUnresolvedDate")));
	        		obj.setReInspectedUnresolvedDatePictureURL1(cursor.getString(cursor.getColumnIndex("ReinspectedUnresolvedDatePictureURL1")));
	        		obj.setReInspectedUnresolvedDatePictureURL2(cursor.getString(cursor.getColumnIndex("ReinspectedUnresolvedDatePictureURL2")));
	        		obj.setReInspectedUnresolvedDatePictureURL3(cursor.getString(cursor.getColumnIndex("ReinspectedUnresolvedDatePictureURL3")));
	        		obj.setExpectedInspectionDate(cursor.getString(cursor.getColumnIndex("ExpectedInspectionDate")));
	        		obj.setFaultType(cursor.getString(cursor.getColumnIndex("FaultType")));
	        		obj.setQCC(cursor.getString(cursor.getColumnIndex("QCC")));
	        		//obj.setApartmentID(cursor.getString(cursor.getColumnIndex("AptAreaID")));
	        		if(cursor.getString(cursor.getColumnIndex("IsSyncedToWeb")).equalsIgnoreCase("true"))
	        			obj.setIsDataSyncToWeb(true);
	        		else{
	        			obj.setIsDataSyncToWeb(false);
	        		}
	        		obj.setStatusForUpload(cursor.getString(cursor.getColumnIndex("StatusForUpload")));
	        		
	        		//String str=cursor.getString(cursor.getColumnIndex("Cost"));
	        		String str1=cursor.getString(cursor.getColumnIndex("Cost"));
	    			Double d=0.0;
	    			if(str1!=null && str1.length()>0)
	    				d=Double.parseDouble(str1);
	    			obj.setCost(d);
	            		
	            		obj.setCostTo(cursor.getString(cursor.getColumnIndex("CostTo")));
	            		obj.setSnagPriority(cursor.getString(cursor.getColumnIndex("PriorityLevel")));
	        		
	            		obj.setAllocatedTo(cursor.getString(cursor.getColumnIndex("AllocatedTo")));
                		obj.setAllocatedToName(cursor.getString(cursor.getColumnIndex("AllocatedToName")));
                		obj.setQCC(cursor.getString(cursor.getColumnIndex("QCC")));
                		obj.setContractorStatus(cursor.getString(cursor.getColumnIndex("ContractorStatus")));
            			obj.setContractorExpectedBeginDate(cursor.getString(cursor.getColumnIndex("ContractorExpectedBeginDate")));
            			obj.setContractorExpectedEndDate(cursor.getString(cursor.getColumnIndex("ContractorExpectedEndDate")));
            			obj.setContractorActualBeginDate(cursor.getString(cursor.getColumnIndex("ContractorActualBeginDate")));
            			obj.setContractorActualEndDate(cursor.getString(cursor.getColumnIndex("ContractorActualEndDate")));
            			String str=cursor.getString(cursor.getColumnIndex("ConManHours"));
            			d=0.0;
            			if(str!=null && str.length()>0)
            				d=Double.parseDouble(str);
            			
            			obj.setConManHours(d);
            			if(cursor.getString(cursor.getColumnIndex("ConNoOfResources")) != null)
            			{
            				String noOfrsr=cursor.getString(cursor.getColumnIndex("ConNoOfResources"));
            				int rs=Integer.parseInt(noOfrsr);
            				obj.setConNoOfResources(rs);
            			}
            			else
            			{
            				obj.setConNoOfResources(0);
            			}
            			str=cursor.getString(cursor.getColumnIndex("ConCost"));
            			d=0.0;
            			if(str!=null && str.length()>0){
            				d=Double.parseDouble(str);
            			}
            			obj.setConCost(d);
    	            		
            			
                       
            			obj.setContractorRemarks(cursor.getString(cursor.getColumnIndex("ContractorRemarks")));
	            		
	        		arr[i]=obj;
	        		
	        		i++;
	        	}
	        	while(cursor.moveToNext());
	        }
	        cursor.close();
	        mydb.close();
		}
		catch (Exception e)
		{
			Log.d("Exception getExternalAreaSnags", ""+e.getMessage());
		}
		return arr;
	}
	
	public TradeMaster[] getExtTradeMasterTradeAptAreaMasterFloor(String prjID,String bldgID,String strFlrID,String AreaType)
	{
		TradeMaster arr[]=null;
		try{
			mydb=context.openOrCreateDatabase(DBNAME, Context.MODE_PRIVATE, null);
			String strQuery;
	        strQuery = "select * from trademaster where id in (Select TradeID from TradeAptAreaMaster where ProjectID='"+prjID+"' and BuildingID='"+bldgID+"' and AptAreaType='"+AreaType+"' and FloorID='"+strFlrID+"') order by seqno;";
	        
	        Cursor cursor=mydb.rawQuery(strQuery, null);
	        arr=new TradeMaster[cursor.getCount()];
	        int i=0;
	        if(cursor.moveToFirst())
		    {
		    	do
		    	{
		    		TradeMaster obj=new TradeMaster();
	        		obj.ID=(String)(cursor.getString(cursor.getColumnIndex("ID")));
	        		obj.TradeType=(String)(cursor.getString(cursor.getColumnIndex("TradeType")));
	        		obj.TradeDescription=(String)(cursor.getString(cursor.getColumnIndex("TradeDescription")));
	        		obj.ParentID=(String)(cursor.getString(cursor.getColumnIndex("ParentID")));
	        		obj.ParentJob=(String)(cursor.getString(cursor.getColumnIndex("ParentJob")));
	        		obj.SeqNo=(String)(cursor.getString(cursor.getColumnIndex("SeqNo")));
	        		obj.ImageName=(String)(cursor.getString(cursor.getColumnIndex("ImageName")));
	        		obj.CreatedBy=(String)(cursor.getString(cursor.getColumnIndex("CreatedBy")));
	        		obj.CreatedDate=(String)(cursor.getString(cursor.getColumnIndex("CreatedDate")));
	        		obj.ModifiedBy=(String)(cursor.getString(cursor.getColumnIndex("ModifiedBy")));
	        		obj.ModifiedDate=(String)(cursor.getString(cursor.getColumnIndex("ModifiedDate")));
	        		obj.EnteredOnMachineID=(String)(cursor.getString(cursor.getColumnIndex("EnteredOnMachineID")));
	        		obj.StatusForUpload=(String)(cursor.getString(cursor.getColumnIndex("StatusForUpload")));
	        		obj.DependencyCount=(int)(cursor.getInt(cursor.getColumnIndex("DependencyCount")));
	        		String str=(String)(cursor.getString(cursor.getColumnIndex("IsParentCompulsory")));
	        		if(str!=null && str.equalsIgnoreCase("true"))
	        			obj.IsParentCompulsory=true;
	        		else
	        			obj.IsParentCompulsory=false;
	        		str=(String)(cursor.getString(cursor.getColumnIndex("IsSyncedToWeb")));
	        		if(str!=null && str.equalsIgnoreCase("true")){
	        			obj.IsSyncedToWeb=true;
	        		}
	        		else
	        			obj.IsSyncedToWeb=false;
	        		
	        		arr[i]=obj;
	        		
	        		i++;
		    	}while(cursor.moveToNext());
		    	
		    }
	        cursor.close();
	        mydb.close();
		}
		catch(Exception e){
			Log.d("Error=", ""+e.getMessage());
		}
		
		
		return arr;
	}
	
	
	
	
	
	public TradeAptAreaMaster getExtTradeAptAreaMasterFloor(String prjID,String bldgID,String strfloorID,String AreaType,String tradeID)
	{
		TradeAptAreaMaster arr=null;
		try{
			mydb=context.openOrCreateDatabase(DBNAME, Context.MODE_PRIVATE, null);
			String strQuery;
	        strQuery = "Select * from TradeAptAreaMaster where ProjectID='"+prjID+"' and BuildingID='"+bldgID+"' and FloorID='"+strfloorID+"' and AptAreaType='"+AreaType+"' and TradeType='"+tradeID+"'";// where ProjectID='"+ProjectID+"' Order by ID";
	        Cursor cursor=mydb.rawQuery(strQuery, null);
	        
	        int i=0;
	        if(cursor.moveToFirst()){
	        	//do{
	        		arr=new TradeAptAreaMaster();
	        		TradeAptAreaMaster obj=new TradeAptAreaMaster();
	        		obj.ID=(String)(cursor.getString(cursor.getColumnIndex("ID")));
	        		obj.ProjectID=(String)(cursor.getString(cursor.getColumnIndex("ProjectID")));
	        		obj.BuildingID=(String)(cursor.getString(cursor.getColumnIndex("BuildingID")));
	        		//obj.FloorID=(String)(cursor.getString(cursor.getColumnIndex("FloorID")));
	        		//obj.ApartmentID=(String)(cursor.getString(cursor.getColumnIndex("ApartmentID")));
	        		//obj.AptAreaID=(String)(cursor.getString(cursor.getColumnIndex("AptAreaID")));
	        		//obj.AptAreaTypeID=(String)(cursor.getString(cursor.getColumnIndex("AptAreaTypeID")));
	        		//obj.AptAreaType=(String)(cursor.getString(cursor.getColumnIndex("AptAreaType")));
	        		obj.TradeID=(String)(cursor.getString(cursor.getColumnIndex("TradeID")));
	        		obj.TradeType=(String)(cursor.getString(cursor.getColumnIndex("TradeType")));
	        		String str2=(String)(cursor.getString(cursor.getColumnIndex("PercentageComplete")));
	        		double d=0.0;
	        		if(str2!=null)
	        			d=Double.parseDouble(str2);
	        		obj.PercentageComplete=d;
	        		obj.CreatedBy=(String)(cursor.getString(cursor.getColumnIndex("CreatedBy")));
	        		obj.CreatedDate=(String)(cursor.getString(cursor.getColumnIndex("CreatedDate")));
	        		obj.ModifiedBy=(String)(cursor.getString(cursor.getColumnIndex("ModifiedBy")));
	        		obj.ModifiedDate=(String)(cursor.getString(cursor.getColumnIndex("ModifiedDate")));
	        		obj.EnteredOnMachineID=(String)(cursor.getString(cursor.getColumnIndex("EnteredOnMachineID")));
	        		
	        		obj.StatusForUpload=(String)(cursor.getString(cursor.getColumnIndex("StatusForUpload")));
	        		String str=(String)(cursor.getString(cursor.getColumnIndex("IsSyncedToWeb")));
	        		if(str!=null && str.equalsIgnoreCase("true"))
	        			obj.IsSyncedToWeb=true;
	        		else
	        			obj.IsSyncedToWeb=false;
	        		arr=obj;
	        		
	        		i++;
	        	//}
	        	//while(cursor.moveToNext());
	        }
	        cursor.close();
	        mydb.close();
		}
		catch(Exception e){
			Log.d("Error=", ""+e.getMessage());
		}
		
		
		return arr;
	}
	
	
	public String[] getTradeDetailsByTradeTypeNewExtArea(String tradeID,BuildingMaster currFlr,String AptareaID,String strTrdmstID)
	{
		
		
		String[] arr=null;
		try
		{
			mydb=context.openOrCreateDatabase(DBNAME, Context.MODE_PRIVATE, null);
			String strQuery;
			String strFltr="";
			if(AptareaID!=null && AptareaID.length()>0)
				strFltr="and AptAreaID='"+AptareaID+"'";
			strQuery="Select distinct inspectiongroup from TradeDetail where ID in(Select DISTINCT TradeDetailID from TradeAptAreaDetail where TradeAptAreaMasterID IN(Select DISTINCT ID from TradeAptAreaMaster where ProjectID='"+currFlr.getProjectID()+"' and BuildingID='"+currFlr.getID()+"' )) and TradeMasterID='"+strTrdmstID+"'  order by SeqNo;";
			Cursor cursor=mydb.rawQuery(strQuery, null);
			arr=new String[cursor.getCount()];
			int i=0;
		    if(cursor.moveToFirst())
		    {
		    	do
		    	{
		    		String obj=new String();
	        		//obj.ID=(String)(cursor.getString(cursor.getColumnIndex("ID")));
	        		obj=(String)(cursor.getString(0));
//	        		obj.TradeMasterID=(String)(cursor.getString(cursor.getColumnIndex("TradeMasterID")));
//	        		obj.InspectionGroup=(String)(cursor.getString(cursor.getColumnIndex("InspectionGroup")));
//	        		obj.InspectionDescription=(String)(cursor.getString(cursor.getColumnIndex("InspectionDescription")));
	        		//int a=(int)(cursor.getInt(cursor.getColumnIndex("SeqNo")));
//	        		
//	        		obj.CreatedBy=(String)(cursor.getString(cursor.getColumnIndex("CreatedBy")));
//	        		obj.CreatedDate=(String)(cursor.getString(cursor.getColumnIndex("CreatedDate")));
//	        		obj.ModifiedBy=(String)(cursor.getString(cursor.getColumnIndex("ModifiedBy")));
//	        		obj.ModifiedDate=(String)(cursor.getString(cursor.getColumnIndex("ModifiedDate")));
//	        		obj.EnteredOnMachineID=(String)(cursor.getString(cursor.getColumnIndex("EnteredOnMachineID")));
//	        		obj.StatusForUpload=(String)(cursor.getString(cursor.getColumnIndex("StatusForUpload")));
//	        		String str=(String)(cursor.getString(cursor.getColumnIndex("IsSyncedToWeb")));
//	        		if(str!=null && str.equalsIgnoreCase("true")){
//	        			obj.IsSyncedToWeb=true;
//	        		}
//	        		else
//	        			obj.IsSyncedToWeb=false;
	        		
	        		arr[i]=obj;
	        		
	        		i++;
		    	}while(cursor.moveToNext());
		    	
		    }
		    cursor.close();
		    mydb.close();
		}
		catch (Exception e)
		{
			Log.d("Exception fmdb getExtBuildingDetails",""+e.getMessage());
		}
		return arr;
	}
	
	public String[] getTradeDetailsByTradeTypeNewFloorArea(String tradeID,FloorMaster currFlr,String AptareaID,String strTrdmstID)
	{
		
		
		String[] arr=null;
		try
		{
			mydb=context.openOrCreateDatabase(DBNAME, Context.MODE_PRIVATE, null);
			String strQuery;
			String strFltr="";
			
			if(AptareaID!=null && AptareaID.length()>0)
				strFltr="and AptAreaID='"+AptareaID+"'";
			strQuery="Select distinct inspectiongroup from TradeDetail where ID in(Select DISTINCT TradeDetailID from TradeAptAreaDetail where TradeAptAreaMasterID IN(Select DISTINCT ID from TradeAptAreaMaster where ProjectID='"+currFlr.getProjectID()+"' and BuildingID='"+currFlr.getBuildingID()+"' and FloorID='"+currFlr.getID()+"')) and TradeMasterID='"+strTrdmstID+"'  order by SeqNo;";
			Cursor cursor=mydb.rawQuery(strQuery, null);
			arr=new String[cursor.getCount()];
			int i=0;
		    if(cursor.moveToFirst())
		    {
		    	do
		    	{
		    		String obj=new String();
	        		//obj.ID=(String)(cursor.getString(cursor.getColumnIndex("ID")));
	        		obj=(String)(cursor.getString(0));
//	        		obj.TradeMasterID=(String)(cursor.getString(cursor.getColumnIndex("TradeMasterID")));
//	        		obj.InspectionGroup=(String)(cursor.getString(cursor.getColumnIndex("InspectionGroup")));
//	        		obj.InspectionDescription=(String)(cursor.getString(cursor.getColumnIndex("InspectionDescription")));
	        		//int a=(int)(cursor.getInt(cursor.getColumnIndex("SeqNo")));
//	        		
//	        		obj.CreatedBy=(String)(cursor.getString(cursor.getColumnIndex("CreatedBy")));
//	        		obj.CreatedDate=(String)(cursor.getString(cursor.getColumnIndex("CreatedDate")));
//	        		obj.ModifiedBy=(String)(cursor.getString(cursor.getColumnIndex("ModifiedBy")));
//	        		obj.ModifiedDate=(String)(cursor.getString(cursor.getColumnIndex("ModifiedDate")));
//	        		obj.EnteredOnMachineID=(String)(cursor.getString(cursor.getColumnIndex("EnteredOnMachineID")));
//	        		obj.StatusForUpload=(String)(cursor.getString(cursor.getColumnIndex("StatusForUpload")));
//	        		String str=(String)(cursor.getString(cursor.getColumnIndex("IsSyncedToWeb")));
//	        		if(str!=null && str.equalsIgnoreCase("true")){
//	        			obj.IsSyncedToWeb=true;
//	        		}
//	        		else
//	        			obj.IsSyncedToWeb=false;
	        		
	        		arr[i]=obj;
	        		
	        		i++;
		    	}while(cursor.moveToNext());
		    	
		    }
		    cursor.close();
		    mydb.close();
		}
		catch (Exception e)
		{
			Log.d("Exception fmdb getExtBuildingDetails",""+e.getMessage());
		}
		return arr;
	}
	
	public SnagChecklistEntries[] checkGetSnagchckLst(String strSngID)
	{
		SnagChecklistEntries objChlstArr[]=null;
		try
		{
			String strQuery="Select * from SnagChecklistEntries where SnagID='"+strSngID+"'";
			mydb=context.openOrCreateDatabase(DBNAME, Context.MODE_PRIVATE, null);
			Cursor cursor=mydb.rawQuery(strQuery, null);
			int i=0;
			objChlstArr=new SnagChecklistEntries[cursor.getCount()];
			if(cursor.moveToFirst())
			{
				do{
				SnagChecklistEntries objChlst=new SnagChecklistEntries();
				//,,,,,,,,,,,,,,,IsSyncedToWeb,,
				objChlst.ID=cursor.getString(cursor.getColumnIndex("ID"));
				objChlst.ProjectID=cursor.getString(cursor.getColumnIndex("ProjectID"));
				objChlst.BuildingID=cursor.getString(cursor.getColumnIndex("BuildingID"));
				objChlst.FloorID=cursor.getString(cursor.getColumnIndex("FloorID"));
				objChlst.ApartmentID=cursor.getString(cursor.getColumnIndex("ApartmentID"));
				objChlst.AptAreaID=cursor.getString(cursor.getColumnIndex("AptAreaID"));
				objChlst.SnagID=cursor.getString(cursor.getColumnIndex("SnagID"));
				objChlst.ChecklistID=cursor.getString(cursor.getColumnIndex("CheckListID"));
				objChlst.ChecklistDescription=cursor.getString(cursor.getColumnIndex("ChecklistDescription"));
				objChlst.CheckListEntry=cursor.getString(cursor.getColumnIndex("ChecklistEntry"));
				objChlst.CreatedBy=cursor.getString(cursor.getColumnIndex("CreatedBy"));
				objChlst.CreatedDate=cursor.getString(cursor.getColumnIndex("CreatedDate"));
				objChlst.ModifiedDate=cursor.getString(cursor.getColumnIndex("ModifiedDate"));
				objChlst.ModifiedBy=cursor.getString(cursor.getColumnIndex("ModifiedBy"));
				objChlst.EnteredOnMachineID=cursor.getString(cursor.getColumnIndex("EnteredOnMachineID"));
				objChlst.StatusForUpload=cursor.getString(cursor.getColumnIndex("StatusForUpload"));
				objChlst.CheckListGroup=cursor.getString(cursor.getColumnIndex("ChecklistGroup"));
				
				objChlstArr[i]=objChlst;
				i++;
				}
				while(cursor.moveToNext());
			}
			
			cursor.close();
			mydb.close();
		}
		catch(Exception e)
		{
			Log.d("Exception",""+e.getMessage());
		}
		return objChlstArr;
	}
	public void insrtUpdateSnglstChklst(SnagChecklistEntries obj)
	{
		try
		{
			String strQuery="Select * from SnagChecklistEntries where ID='"+obj.ID+"'";
			mydb=context.openOrCreateDatabase(DBNAME, Context.MODE_PRIVATE, null);
			Cursor cursor=mydb.rawQuery(strQuery, null);
			if(cursor.moveToFirst()){
				strQuery="UPDATE SnagChecklistEntries SET ProjectID='"+obj.ProjectID+"',BuildingID='"+obj.BuildingID+"',FloorID='"+obj.FloorID+"',ApartmentID='"+obj.ApartmentID+"',AptAreaID='"+obj.AptAreaID+"',SnagID='"+obj.SnagID+"',ChecklistID='"+obj.ChecklistID+"',CheckListGroup='"+obj.CheckListGroup+"',ChecklistDescription='"+obj.ChecklistDescription+"',ChecklistEntry='"+obj.CheckListEntry+"',CreatedBy='"+obj.CreatedBy+"',CreatedDate='"+obj.CreatedDate+"',ModifiedBy='"+obj.ModifiedBy+"',ModifiedDate='"+obj.ModifiedDate+"',EnteredOnMachineID='"+obj.EnteredOnMachineID+"'  where ID='"+obj.ID+"';";
			}
			else{
				strQuery = "Insert into SnagChecklistEntries(ID,ProjectID,BuildingID,FloorID,ApartmentID,AptAreaID,SnagID,ChecklistID,CheckListGroup,ChecklistDescription,ChecklistEntry,CreatedBy,CreatedDate,ModifiedBy,ModifiedDate,EnteredOnMachineID) values('"+obj.ID+"','"+obj.ProjectID+"','"+obj.BuildingID+"','"+obj.FloorID+"','"+obj.ApartmentID+"','"+obj.AptAreaID+"','"+obj.SnagID+"','"+obj.ChecklistID+"','"+obj.CheckListGroup+"','"+obj.ChecklistDescription+"','"+obj.CheckListEntry+"','"+obj.CreatedBy+"','"+obj.CreatedDate+"','"+obj.ModifiedBy+"','"+obj.ModifiedDate+"','"+obj.EnteredOnMachineID+"');";
			}
			
			mydb.rawQuery(strQuery, null);
			cursor.close();
			mydb.close();
		}
		catch(Exception e)
		{
			Log.d("Exception",""+e.getMessage());
		}
	}
	
	
	
	/////new functions
	public boolean EditPageUpdateIntoSnagMaster(SnagMaster obj){
		boolean success=false;
		try{
			mydb=context.openOrCreateDatabase(DBNAME, Context.MODE_PRIVATE, null);
			String strQuery;
	        strQuery = "UPDATE SnagMaster SET SnagType='"+obj.getSnagType()+"',SnagDetails='"+obj.getSnagDetails()+"',PictureURL1='"+obj.getPictureURL1()+"',PictureURL2='"+obj.getPictureURL2()+"',PictureURL3='"+obj.getPictureURL3()+"',AptAreaName='"+obj.getAptAreaName()+"',AptAreaID='"+obj.getAptAreaID()+"',ReportDate='"+obj.getReportDate()+"',SnagStatus='"+obj.getSnagStatus()+"',ResolveDate='"+obj.getResolveDate()+"',ResolveDatePictureURL1='"+obj.getResolveDatePictureURL1()+"',ResolveDatePictureURL2='"+obj.getResolveDatePictureURL2()+"',ResolveDatePictureURL3='"+obj.getResolveDatePictureURL3()+"',ReInspectedUnresolvedDate='"+obj.getReInspectedUnresolvedDate()+"',ReInspectedUnresolvedDatePictureURL1='"+obj.getReInspectedUnresolvedDatePictureURL1()+"',ReInspectedUnresolvedDatePictureURL2='"+obj.getReInspectedUnresolvedDatePictureURL2()+"',ReInspectedUnresolvedDatePictureURL3='"+obj.getReInspectedUnresolvedDatePictureURL3()+"' ,ExpectedInspectionDate='"+obj.getExpectedInspectionDate()+"',FaultType='"+obj.getFaultType()+"',Cost="+obj.getCost()+",CostTo='"+obj.getCostTo()+"',PriorityLevel='"+obj.getSnagPriority()+"',IsSyncedToWeb='"+obj.getIsDataSyncToWeb()+"',StatusForUpload='"+obj.getStatusForUpload()+"',QCC='"+obj.getQCC()+"',PercentageCompleted='"+obj.getPercentageCompleted()+"',AllocatedTo='"+obj.getAllocatedTo()+"', AllocatedToName='"+obj.getAllocatedToName()+"'  WHERE (ID='"+obj.getID()+"' );";
	        mydb.execSQL(strQuery);
	        mydb.close();
		}
		catch(Exception e){
			Toast.makeText(context, "Error Occured", Toast.LENGTH_LONG).show();
			Log.d("Error UpdateIntoSnagMaster=", ""+e.getMessage());
		}
		return true;
	}
	
}