package com.snagreporter.database;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Locale;

import org.apache.http.util.ByteArrayBuffer;
import org.json.JSONArray;
import org.json.JSONObject;
import org.kobjects.base64.Base64;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.MarshalBase64;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import com.snagreporter.ProjectListPage;
import com.snagreporter.R;

import com.snagreporter.entity.ApartmentAreaType;
import com.snagreporter.entity.ApartmentDetails;
import com.snagreporter.entity.ApartmentMaster;
import com.snagreporter.entity.AttachmentDetails;
import com.snagreporter.entity.BuildingMaster;
import com.snagreporter.entity.FaultType;
import com.snagreporter.entity.FloorMaster;
import com.snagreporter.entity.InspectionDetails;
import com.snagreporter.entity.InspectionList;
import com.snagreporter.entity.JobType;
import com.snagreporter.entity.ProjectMaster;
import com.snagreporter.entity.SnagChecklistEntries;
import com.snagreporter.entity.SnagChecklistMaster;
import com.snagreporter.entity.SnagMaster;
import com.snagreporter.entity.SnagMasterDependency;
import com.snagreporter.entity.TimeStampMaster;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.CompressFormat;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.SystemClock;
//import android.provider.Settings.System;
import android.util.Log;

public class ParseSyncData extends Activity
{
	SQLiteDatabase mydb;
	private static String DBNAME = "SnagReporter.db";
	int Sequence=1;
	boolean isAllDone=false;
	int index=0;
	Context context;
	ProgressDialog mProgressDialog2;
	public String RegUserID="";
	String LoginType="";
	String NAMESPACE = "http://tempuri.org/";
	String URL ="";
	
	String METHOD_NAME_SaveNewDataToTheDataBase = "SaveNewDataToTheDataBase";
	String SOAP_ACTION_SaveNewDataToTheDataBase = "http://tempuri.org/SaveNewDataToTheDataBase";
	String METHOD_NAME_SyncTableData="SyncTableData";
	String SOAP_ACTION_SyncTableData = "http://tempuri.org/SyncTableData";
	String METHOD_NAME_UpdateDataToTheDataBase = "UpdateDataToTheDataBase";
	String SOAP_ACTION_UpdateDataToTheDataBase = "http://tempuri.org/UpdateDataToTheDataBase";
	ArrayList<String> ALS=new ArrayList<String>();
	int TotalImages=0;
	int i=0;
	ArrayList<String> arrD_URL;
	int TotalImagesToDownload=0;
	public ParseSyncData(Context con)
	{
		context=con;
		mProgressDialog2 = new ProgressDialog(context);
		URL=""+context.getResources().getString(R.string.WS_URL).toString()+"SnagReporter_WebS.asmx";
		SharedPreferences SP = context.getSharedPreferences("AppDelegate",MODE_PRIVATE);
        RegUserID=SP.getString("RegUserID", "");
        LoginType=SP.getString("LoginType", "");
	}
	public void start(){
		try{
		StartSyncProgress task=new StartSyncProgress();
		task.execute(10);
		//StartDownloadData task=new StartDownloadData();
		//task.execute(10);
		//StartUploadImages task=new StartUploadImages();
		//task.execute(10);
		}
    	catch(Exception e)
    	{
    		Log.d("Exception",""+e.getMessage());
    	}
	}
//	protected class Temp extends AsyncTask<Integer , Integer, Void> {
//
//		@Override
//        protected void onPreExecute() {
//			if(!mProgressDialog2.isShowing()){
//			mProgressDialog2.setCancelable(false);
//    		mProgressDialog2.setMessage("Synchronizing...");
//    		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
//    		mProgressDialog2.show();
//			}
//    		i++;
//		}
//		@Override
//		protected Void doInBackground(Integer... arg0) {
//			// TODO Auto-generated method stub
//			return null;
//		}
//		 @Override
//	     protected void onPostExecute(Void result1){
//			 //SystemClock.sleep(5000);
//			 if(i<500){
//				 Temp task=new Temp();
//					task.execute(10);
//			 }
//			 else
//			 mProgressDialog2.dismiss();
//		 }
//		
//	}
	
	public String uploadFile(String FileName,String FilePath)
    { String Result="false";
    	if(FilePath!=null && FilePath.length()!=0 && !FilePath.equals(""))
    	{
    		String METHOD_NAME = "UploadFileDataParam";
    		String NAMESPACE = "http://tempuri.org/";
    		String URL = ""+context.getResources().getString(R.string.WS_URL).toString()+"SnagReporter_WebS.asmx";//SnagReporter_WCF.svc
    		String SOAP_ACTION = "http://tempuri.org/UploadFileDataParam";//
    		String res = "";
    		try 
    		{
    			SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
    	    
    			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
    			new MarshalBase64().register(envelope);
    			envelope.dotNet = true;
    			envelope.setOutputSoapObject(request);
    	    
    	    
    			//String FileName=""+url+".jpg";
    			File file=new File(FilePath);
    			//strFilePath=file.toString();
    			//String FilePath1=file.toString();
    			//Bitmap myImg=BitmapFactory.decodeFile(FilePath1);
    			byte[] arr=getBytesFromFile(file);
    			String FilePosition="0";
     	    
    			String ba1=Base64.encode(arr);
    	    
    			request.addProperty("FileName",FileName);
    			request.addProperty("BufferData",ba1);
    			request.addProperty("FilePosition",FilePosition);
    	    
    	    
    			HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
    			androidHttpTransport.call(SOAP_ACTION, envelope);
    			Object resonse=envelope.getResponse();
    			String output = resonse.toString();
    			Result=output;
    			Log.d("Result=", ""+output);
    	    
    		
    			//jObject = new JSONObject(resultData);
    	}
    	 catch(Exception e)
    	 {
    		 String str=e.getMessage().toString();
    		 Log.d("Error=", ""+e.getMessage()); 
    	 }
    	}
    	return Result;
    }
	public byte[] getBytesFromFile(File file) {
		byte[] bytes =null;
		try{
		FileInputStream fis = new FileInputStream(file);
        //System.out.println(file.exists() + "!!");
        //InputStream in = resource.openStream();
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] buf = new byte[1024];
        try {
            for (int readNum; (readNum = fis.read(buf)) != -1;) {
                bos.write(buf, 0, readNum); //no doubt here is 0
                //Writes len bytes from the specified byte array starting at offset off to this byte array output stream.
                System.out.println("read " + readNum + " bytes,");
            }
        } catch (IOException ex) {
            //Logger.getLogger(genJpeg.class.getName()).log(Level.SEVERE, null, ex);
        	Log.d("IOException=", ""+ex.getMessage());
        }
         bytes = bos.toByteArray();
		}
		catch(Exception e){
			Log.d("Exception=", ""+e.getMessage());
		}
        return bytes;
    }
	protected class StartDownloadImages extends AsyncTask<Integer , Integer, Void> {
		boolean isAllDone=false;
		@Override
        protected void onPreExecute() {  
			int current=TotalImages-ALS.size()+1;
			 mProgressDialog2.setMessage("Downloading "+current+" of "+TotalImages+" photos...");
		}
		@Override
		protected Void doInBackground(Integer... arg0) {
			// TODO Auto-generated method stub
			try{
			if(ALS!=null && ALS.size()>0){
				downloadImage(ALS.get(0));
				ALS.remove(0);
				if((ALS!=null && ALS.size()==0) || ALS==null){
					isAllDone=true;
				}
			}
			else{
				isAllDone=true;
			}
			
			}
			catch(Exception e){
				Log.d("Exception =",""+e.getMessage());
			}
			
			
			return null;
		}
		 @Override
	        protected void onPostExecute(Void result1) {
			 
			 if(isAllDone){
			 mProgressDialog2.setMessage("Synchronization Complete...");
				setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
				//SystemClock.sleep(2000);
				runOnUiThread(new Runnable() {
      			  public void run() {
      			    
      			    mProgressDialog2.dismiss();
      			  }
      			});
				//mProgressDialog2.dismiss();
			 }
			 else{
				 StartDownloadImages task=new StartDownloadImages();
				 task.execute(10);
			 }
		 }
	}
	public void uploadImage(String url)
    {
    	if(url!=null && url.length()!=0 && !url.equals(""))
    	{
    		String METHOD_NAME = "UploadFileDataParam";
    		String NAMESPACE = "http://tempuri.org/";
    		String URL = ""+context.getResources().getString(R.string.WS_URL).toString()+"SnagReporter_WebS.asmx";//SnagReporter_WCF.svc
    		String SOAP_ACTION = "http://tempuri.org/UploadFileDataParam";//
    		String res = "";
    		try 
    		{
    			SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
    	    
    			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
    			new MarshalBase64().register(envelope);
    			envelope.dotNet = true;
    			envelope.setOutputSoapObject(request);
    	    
    	    
    			String FileName=""+url+".jpg";
    			File file=new File(Environment.getExternalStorageDirectory()+File.separator+"SnagReporter/Pictures/"+FileName);
    			//strFilePath=file.toString();
    			String FilePath=file.toString();
    			Bitmap myImg=BitmapFactory.decodeFile(FilePath);
    			byte[] arr=getBytesFromBitmap(myImg);
    			String FilePosition="0";
     	    
    			String ba1=Base64.encode(arr);
    	    
    			request.addProperty("FileName",FileName);
    			request.addProperty("BufferData",ba1);
    			request.addProperty("FilePosition",FilePosition);
    	    
    	    
    			HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
    			androidHttpTransport.call(SOAP_ACTION, envelope);
    			Object resonse=envelope.getResponse();
    			String output = resonse.toString();
    	    
    	    
    	    
    		
    			//jObject = new JSONObject(resultData);
    	}
    	 catch(Exception e)
    	 {
    		 String str=e.getMessage().toString();
    		 Log.d("Error=", ""+e.getMessage()); 
    	 }
    	}
    }
	public byte[] getBytesFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(CompressFormat.JPEG, 70, stream);
        return stream.toByteArray();
    }
	public void getAllDataFromWebAfterDate(String RegUserID,String TableName,String ModifiedDate){
		try{
			
    		String resultData="";
    		try {
    		    SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME_SyncTableData);
    		    
    		    SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
    		    envelope.bodyOut=request;
    		    envelope.dotNet = true;
    		    envelope.setOutputSoapObject(request);
    		    
    		    if(ModifiedDate!=null && ModifiedDate.length()==0){
    		    	
    		    }
    		    else{
    		    	
    		    }
    		    
    		    request.addProperty("_strTableName",TableName);
    		    request.addProperty("_strModifiedDate","4/13/2013 1:16:37 AM");
    		    request.addProperty("_strModifiedBy",RegUserID);
    		    request.addProperty("_strNoRows",10);
    		    
    		    HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
    		    androidHttpTransport.call(SOAP_ACTION_SyncTableData, envelope);
    		    Object resonse=envelope.getResponse();
    		    resultData = resonse.toString();
    		    
    		    
    		    try{
    	        	JSONObject jObject;
    				String lastModifiedDate="";
    				jObject = new JSONObject(resultData);
    				JSONArray arr = jObject.getJSONArray("Data");
    				if(arr!=null){
    					for(int i=0;i<arr.length();i++){
    						JSONObject geometry = arr.getJSONObject(i);
    						if(TableName.equalsIgnoreCase("ProjectMaster"))
    							parseProjectMasterData(geometry);
    						else if(TableName.equalsIgnoreCase("BuildingMaster"))
    							parseBuildingMasterData(geometry);
    						else if(TableName.equalsIgnoreCase("FloorMaster"))
    							parseFloorData(geometry);
    						else if(TableName.equalsIgnoreCase("ApartmentMaster"))
    							parseAptMasterData(geometry);
    						else if(TableName.equalsIgnoreCase("ApartmentDetails"))
    							parseApartmentDetails(geometry);
    						else if(TableName.equalsIgnoreCase("SnagMaster"))
    							parseSnagData(geometry);
    						lastModifiedDate=geometry.getString("ModifiedDate");
    						
    					}
    				}
    				if(arr!=null && arr.length()==10){
    					getAllDataFromWebAfterDate(RegUserID,TableName,lastModifiedDate);
    				}
    				else{
    					if(TableName.equalsIgnoreCase("ProjectMaster")){
    						getAllDataFromWebAfterDate(RegUserID,"BuildingMaster","");
    					}
    					else if(TableName.equalsIgnoreCase("BuildingMaster")){
    						getAllDataFromWebAfterDate(RegUserID,"FloorMaster","");
    					}
    					else if(TableName.equalsIgnoreCase("FloorMaster")){
    						getAllDataFromWebAfterDate(RegUserID,"ApartmentMaster","");
    					}
    					else if(TableName.equalsIgnoreCase("ApartmentMaster")){
    						getAllDataFromWebAfterDate(RegUserID,"ApartmentDetails","");
    					}
    					else if(TableName.equalsIgnoreCase("ApartmentDetails")){
    						
    					}
    				}
    	        	}
    	        	catch(Exception e){
    	        		Log.d("Error=", ""+e.getMessage());
    	        	}
    		    
    		    
    		}
    		catch(Exception e){
    			Log.d("Error=", ""+e.getMessage());
    		}
		}
		catch(Exception e){
			Log.d("Error getProjectsFromWebAfterDate", e.getMessage());
		}
		
		
	}
	
	
	public boolean getSnagsFromWebAfterDate(String RegUserID){
		try{
			
    		String resultData="";
    		try {
    		    SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME_SyncTableData);
    		    
    		    SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
    		    envelope.bodyOut=request;
    		    envelope.dotNet = true;
    		    envelope.setOutputSoapObject(request);
    		    
    		    request.addProperty("_strTableName","SnagMaster");
    		    request.addProperty("_strModifiedDate","4/13/2013 1:16:37 AM");
    		    request.addProperty("_strModifiedBy",RegUserID);
    		    request.addProperty("_strNoRows",10);
    		    
    		    HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
    		    androidHttpTransport.call(SOAP_ACTION_SyncTableData, envelope);
    		    Object resonse=envelope.getResponse();
    		    resultData = resonse.toString();
    		    
    		    
    		    try{
    	        	JSONObject jObject;
    				
    				jObject = new JSONObject(resultData);
    				JSONArray arr = jObject.getJSONArray("Data");
    				if(arr!=null){
    					for(int i=0;i<arr.length();i++){
    						JSONObject geometry = arr.getJSONObject(i);
    						parseSnagData(geometry);
    						
    						
    					}
    				}
    	        	}
    	        	catch(Exception e){
    	        		
    	        	}
    		    
    		    
    		}
    		catch(Exception e){
    			
    		}
		}
		catch(Exception e){
			Log.d("Error getSnagsFromWebAfterDate", e.getMessage());
		}
		return true;
	}
	
	
	public void getInspectionListFromWeb(){
		try{
			String METHOD_NAME = "GetDataTable";
    		String NAMESPACE = "http://tempuri.org/";
    		String URL = ""+context.getResources().getString(R.string.WS_URLLocal).toString()+"SnagReporter_WebS.asmx";//SnagReporter_WCF.svc
    		String SOAP_ACTION = "http://tempuri.org/GetDataTable";//
    		String res = "";
    		try {
    		    SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
    		    SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
    		    envelope.bodyOut=request;
    		    envelope.dotNet = true;
    		    envelope.setOutputSoapObject(request);
    		    
    		    request.addProperty("_strTableName","InspectionList");
    		    
    		    
    		    HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
    		    androidHttpTransport.call(SOAP_ACTION, envelope);
    		    Object resonse=envelope.getResponse();
    		    String resultData = resonse.toString();
    		    
    		    
    		    JSONObject jObject;
    			
    				jObject = new JSONObject(resultData);
    				JSONArray arr = jObject.getJSONArray("Data");
    				if(arr!=null){
    					for(int i=0;i<arr.length();i++){
    						JSONObject geometry = arr.getJSONObject(i);
    						parseInspectionList(geometry);
    						
    					}
    				}
    		    res = resultData;
		}
		catch(Exception e){
			
		}
		
		
	}
		catch(Exception e){
			
		}
	}
	
	 public void parseSnagData(JSONObject jsObj)
	 {
		 try
		 {
			 
			 	String ID=jsObj.getString("ID");
	    		String ApartmentID=jsObj.getString("ApartmentID");
	    		String Apartment=jsObj.getString("Apartment");
	    		
	    		
	    		String FloorID=jsObj.getString("FloorID");
	    		String Floor=jsObj.getString("Floor");
	    		String BuildingID=jsObj.getString("BuildingID");
	    		String BuildingName=jsObj.getString("BuildingName");
	    		String ProjectID=jsObj.getString("ProjectID");
	    		String ProjectName=jsObj.getString("ProjectName");
	    		
	    		String SnagType=jsObj.getString("SnagType");
	    		String SnagDetails=jsObj.getString("SnagDetails");
	    		String PictureURL1=jsObj.getString("PictureURL1");
	    		String PictureURL2=jsObj.getString("PictureURL2");
	    		String PictureURL3=jsObj.getString("PictureURL3");
	    		String AptAreaName=jsObj.getString("AptAreaName");
	    		String AptAreaID=jsObj.getString("AptAreaID");
	    		String ReportDate=jsObj.getString("ReportDate");
	    		String SnagStatus=jsObj.getString("SnagStatus");
	    		String ResolveDate=jsObj.getString("ResolveDate");
	    		String InspectorID=jsObj.getString("InspectorID");
	    		String InspectorName=jsObj.getString("InspectorName");
	    		String ResolveDatePictureURL1=jsObj.getString("ResolveDatePictureURL1");
	    		String ResolveDatePictureURL2=jsObj.getString("ResolveDatePictureURL2");
	    		String ResolveDatePictureURL3=jsObj.getString("ResolveDatePictureURL3");
	    		String ReInspectedUnresolvedDate="";
	    		try{
	    			ReInspectedUnresolvedDate=jsObj.getString("ReInspectedUnresolvedDate");
	    		}
	    		catch(Exception e){
	    			
	    		}
	    		String ReInspectedUnresolvedDatePictureURL1="";
	    		try{
	    			ReInspectedUnresolvedDatePictureURL1=jsObj.getString("ReInspectedUnresolvedDatePictureURL1");
	    		}
	    		catch(Exception e){
	    			ReInspectedUnresolvedDatePictureURL1="";
	    		}
	    		
	    		String ReInspectedUnresolvedDatePictureURL2="";
	    		try{
	    			ReInspectedUnresolvedDatePictureURL2=jsObj.getString("ReInspectedUnresolvedDatePictureURL2");
	    		}
	    		catch(Exception e){
	    			ReInspectedUnresolvedDatePictureURL2="";
	    		}
	    		
	    		String ReInspectedUnresolvedDatePictureURL3="";
	    		try{
	    			ReInspectedUnresolvedDatePictureURL3=jsObj.getString("ReInspectedUnresolvedDatePictureURL3");
	    		}
	    		catch(Exception e){
	    			ReInspectedUnresolvedDatePictureURL3="";
	    		}
	    		
	    		String ExpectedInspectionDate="";
	    		try{
	    			ExpectedInspectionDate=jsObj.getString("ExpectedInspectionDate");
	    		}
	    		catch(Exception e){
	    			ExpectedInspectionDate="";
	    		}
	    		String FaultType="";
	    		try{
	    			FaultType=jsObj.getString("FaultType");
	    		}
	    		catch(Exception e){
	    			FaultType="";
	    		}
	    		
	    		String Cost="";
	    		try{
	    			Cost=jsObj.getString("Cost");
	    		}
	    		catch(Exception e){
	    			Cost="";
	    		}
	    		
	    		String CostTo="";
	    		try{
	    			CostTo=jsObj.getString("CostTo");
	    		}
	    		catch(Exception e){
	    			CostTo="";
	    		}
	    		if(CostTo==null || CostTo.equalsIgnoreCase("null")){
	    			CostTo="";
	    		}
	    		
	    		
	    		String PriorityLevel="";
	    		try{
	    			PriorityLevel=jsObj.getString("PriorityLevel");
	    		}
	    		catch(Exception e){
	    			PriorityLevel="";
	    		}
	    		if(PriorityLevel==null || PriorityLevel.equalsIgnoreCase("null")){
	    			PriorityLevel="";
	    		}
	    		
	    		String QCC=jsObj.getString("QCC")==null?"":jsObj.getString("QCC");
	    		String QCCRemarks=jsObj.getString("QCCRemarks")==null?"":jsObj.getString("QCCRemarks");
	    		String ContractorStatus=jsObj.getString("ContractorStatus")==null?"":jsObj.getString("ContractorStatus");
	    		String ContractorRemarks=jsObj.getString("ContractorRemarks")==null?"":jsObj.getString("ContractorRemarks");
	    		String ContractorExpectedBeginDate=jsObj.getString("ContractorExpectedBeginDate")==null?"":jsObj.getString("ContractorExpectedBeginDate");
	    		String ContractorExpectedEndDate=jsObj.getString("ContractorExpectedEndDate")==null?"":jsObj.getString("ContractorExpectedEndDate");
	    		String ContractorActualBeginDate=jsObj.getString("ContractorActualBeginDate")==null?"":jsObj.getString("ContractorActualBeginDate");
	    		String ContractorActualEndDate=jsObj.getString("ContractorActualEndDate")==null?"":jsObj.getString("ContractorActualEndDate");
	    		String PercentageCompleted=jsObj.getString("PercentageCompleted")==null?"":jsObj.getString("PercentageCompleted");
	    		String AllocatedTo=jsObj.getString("AllocatedTo")==null?"":jsObj.getString("AllocatedTo");
	    		String AllocatedToName=jsObj.getString("AllocatedToName")==null?"":jsObj.getString("AllocatedToName");
	    		String ConNoOfResources=jsObj.getString("ConNoOfResources")==null?"":jsObj.getString("ConNoOfResources");
	    		String ConManHours=jsObj.getString("ConManHours")==null?"":jsObj.getString("ConManHours");
	    		String ConCost=jsObj.getString("ConCost")==null?"":jsObj.getString("ConCost");
	    		String XValue=jsObj.getString("XValue")==null?"":jsObj.getString("XValue");
	    		String YValue=jsObj.getString("YValue")==null?"":jsObj.getString("YValue");
	    		
			   
			    
			    
			    SnagMaster obj=new SnagMaster();
			obj.setID(ID);
			obj.setSnagType(SnagType);
     		obj.setSnagDetails(SnagDetails);
     		obj.setPictureURL1(PictureURL1);
     		obj.setPictureURL2(PictureURL2);
     		obj.setPictureURL3(PictureURL3);
     		obj.setProjectID(ProjectID);
     		obj.setProjectName(ProjectName);
     		obj.setBuildingID(BuildingID);
     		obj.setBuildingName(BuildingName);
     		obj.setFloorID(FloorID);
     		obj.setFloor(Floor);
     		obj.setApartmentID(ApartmentID);
     		obj.setApartment(Apartment);
     		obj.setAptAreaName(AptAreaName);
     		obj.setAptAreaID(AptAreaID);
     		obj.setReportDate(ReportDate);
     		obj.setSnagStatus(SnagStatus);
     		obj.setResolveDate(ResolveDate);
     		obj.setInspectorID(InspectorID);
     		obj.setInspectorName(InspectorName);
     		obj.setResolveDatePictureURL1(ResolveDatePictureURL1);
     		obj.setResolveDatePictureURL2(ResolveDatePictureURL2);
     		obj.setResolveDatePictureURL3(ResolveDatePictureURL3);
     		obj.setReInspectedUnresolvedDate(ReInspectedUnresolvedDate);//ReinspectedUnresolvedDate
     		obj.setReInspectedUnresolvedDatePictureURL1(ReInspectedUnresolvedDatePictureURL1);
     		obj.setReInspectedUnresolvedDatePictureURL2(ReInspectedUnresolvedDatePictureURL2);
     		obj.setReInspectedUnresolvedDatePictureURL3(ReInspectedUnresolvedDatePictureURL3);
     		if(Cost.length()!=0)
    			obj.setCost(Double.parseDouble(Cost));
    		else
    			obj.setCost(0.0);
     		obj.setCostTo(CostTo);
     		obj.setSnagPriority(PriorityLevel);
     		obj.setExpectedInspectionDate(ExpectedInspectionDate);
     		obj.setFaultType(FaultType);
     		
     		
     		
     		//obj.setContractorID(ContractorID);
     		//obj.setContractorName(ContractorName);
     		//obj.setSubContractorID(SubContractorID);
     		//obj.setSubContractorName(SubContractorName);
     		obj.setStatusForUpload("Inserted");
     		obj.setIsDataSyncToWeb(true);
     		
     		if(PictureURL1!=null && PictureURL1.length()>0){
    			ALS.add(""+PictureURL1);
    		}
     		if(PictureURL2!=null && PictureURL2.length()>0){
    			ALS.add(""+PictureURL2);
    		}
     		if(PictureURL3!=null && PictureURL3.length()>0){
    			ALS.add(""+PictureURL3);
    		}
     		if(ResolveDatePictureURL1!=null && ResolveDatePictureURL1.length()>0){
    			ALS.add(""+ResolveDatePictureURL1);
    		}
     		if(ResolveDatePictureURL2!=null && ResolveDatePictureURL2.length()>0){
    			ALS.add(""+ResolveDatePictureURL2);
    		}
     		if(ResolveDatePictureURL3!=null && ResolveDatePictureURL3.length()>0){
    			ALS.add(""+ResolveDatePictureURL3);
    		}
     		if(ReInspectedUnresolvedDatePictureURL1!=null && ReInspectedUnresolvedDatePictureURL1.length()>0){
    			ALS.add(""+ReInspectedUnresolvedDatePictureURL1);
    		}
     		if(ReInspectedUnresolvedDatePictureURL2!=null && ReInspectedUnresolvedDatePictureURL2.length()>0){
    			ALS.add(""+ReInspectedUnresolvedDatePictureURL2);
    		}
     		if(ReInspectedUnresolvedDatePictureURL3!=null && ReInspectedUnresolvedDatePictureURL3.length()>0){
    			ALS.add(""+ReInspectedUnresolvedDatePictureURL3);
    		}
     		
     		obj.setQCC(QCC);
     		obj.setQccRemarks(QCCRemarks);
     		obj.setContractorStatus(ContractorStatus);
     		obj.setContractorRemarks(ContractorRemarks);
     		obj.setContractorExpectedBeginDate(ContractorExpectedBeginDate);
     		obj.setContractorExpectedEndDate(ContractorExpectedEndDate);
     		obj.setContractorActualBeginDate(ContractorActualBeginDate);
     		obj.setContractorActualEndDate(ContractorActualEndDate);
     		double d=0.0;
     		if(PercentageCompleted!=null && PercentageCompleted.length()>0)
     			d=Double.parseDouble(PercentageCompleted);
     		obj.setPercentageCompleted(d);
     		obj.setAllocatedTo(AllocatedTo);
     		obj.setAllocatedToName(AllocatedToName);
     		int I=0;
     		if(ConNoOfResources!=null && ConNoOfResources.length()>0)
     			I=Integer.parseInt(ConNoOfResources);
     		obj.setConNoOfResources(I);
     		d=0.0;
     		if(ConManHours!=null && ConManHours.length()>0)
     			d=Double.parseDouble(ConManHours);
     		obj.setConManHours(d);
     		d=0.0;
     		if(ConCost!=null && ConCost.length()>0)
     			d=Double.parseDouble(ConCost);
     		obj.setConCost(d);
     		float F=0;
     		if(XValue!=null && XValue.length()>0)
     			F=Float.parseFloat(XValue);
     		obj.setXValue(F);
     		F=0;
     		if(YValue!=null && YValue.length()>0)
     			F=Float.parseFloat(YValue);
     		obj.setYValue(F);
     		
     		
     		
     		
     		
     		
     		
     		FMDBDatabaseAccess fmdb=new FMDBDatabaseAccess(context);
     		fmdb.insertORUpdateSnagMaster(obj);
     		
		 }
		 catch (Exception e)
		 {
			Log.d("Error parseSnagData ", ""+e.getMessage());
		 }	
	 }
	 
	 public void parseSnagDepencdencyData(JSONObject jsObj)
	 {
		 try
		 {
			 
			 	String DId=jsObj.getString("DId");
	    		String ParentSnagId=jsObj.getString("ParentSnagId");
	    		String SnagId=jsObj.getString("SnagId");
	    		
	    		
	    		String JobType=jsObj.getString("JobType");
	    		String ProjectId=jsObj.getString("ProjectId");
	    		String BuildingId=jsObj.getString("BuildingId");
	    		String FloorId=jsObj.getString("FloorId");
	    		String ApartmentID=jsObj.getString("ApartmentID");
	    		
	    		
	    		
			    
			    
			    SnagMasterDependency obj=new SnagMasterDependency();
			obj.setDId(DId);
			obj.setParentSnagId(ParentSnagId);
     		obj.setSnagId(SnagId);
     		obj.setJobType(JobType);
     		obj.setProjectId(ProjectId);
     		obj.setBuildingId(BuildingId);
     		obj.setFloorId(FloorId);
     		obj.setApartmentID(ApartmentID);
     		
     		obj.setIsDataSyncToWeb(true);
     		
     		
     		
     		FMDBDatabaseAccess fmdb=new FMDBDatabaseAccess(context);
     		SnagMasterDependency[] arr=new SnagMasterDependency[1];
     		arr[0]=obj;
     		fmdb.insertDependency(arr);
     		
		 }
		 catch (Exception e)
		 {
			Log.d("Error parseSnagData ", ""+e.getMessage());
		 }	
	 }
	 public void parseApartmentDetails(JSONObject jsobj)
	 {
		 try
		 {
			 	String ID=jsobj.getString("ID");
	    		String ApartmentID=jsobj.getString("ApartmentID");
	    		String Apartment=jsobj.getString("Apartment");
	    		
	    		
	    		String FloorID=jsobj.getString("FloorID");
	    		String Floor=jsobj.getString("Floor");
	    		String BuildingID=jsobj.getString("BuildingID");
	    		String BuildingName=jsobj.getString("BuildingName");
	    		String ProjectID=jsobj.getString("ProjectID");
	    		String ProjectName=jsobj.getString("ProjectName");
	    		String AptAreaType=jsobj.getString("AptAreaType");
	    		String AptAreaName=jsobj.getString("AptAreaName");
	    		String SubSerial=jsobj.getString("SubSerial");
	    		String ImageName=jsobj.getString("ImageName");
	    		
	    		ApartmentDetails objApt=new ApartmentDetails();
	    		objApt.setID(ID);
	    		objApt.setApartmentID(ApartmentID);
	    		objApt.setApartment(Apartment);
	    		objApt.setFloorID(FloorID);
	    		objApt.setFloor(Floor);
	    		objApt.setBuildingID(BuildingID);
	    		objApt.setBuildingName(BuildingName);
	    		objApt.setProjectID(ProjectID);
	    		objApt.setProjectName(ProjectName);
	    		objApt.setAptAreaType(AptAreaType);
	    		objApt.setAptAreaName(AptAreaName);
	    		objApt.setSubSerial(SubSerial);
	    		objApt.setImageName(ImageName);
	    		
	    		if(ImageName!=null && ImageName.length()>0){
	    			ALS.add(""+ImageName);
	    		}
	    		
	    		
	    		
	    		FMDBDatabaseAccess fmdb=new FMDBDatabaseAccess(context);
	    		fmdb.insertORUpdateApartmentDetails(objApt);
		 }
		 catch (Exception e) 
		 {
			Log.d("Error  parseApartmentDetails",""+e.getMessage());
		}
	 }
	
	  public void parseBuildingMasterData(JSONObject jsobj)
	  {
	    	try
	    	{
	    		String ID=jsobj.getString("ID");
	    		String BuildingName=jsobj.getString("BuildingName");
	    		
	    		int NoOfFloors=jsobj.getInt("NoOfFloors");
	    		int NoOfElevators=jsobj.getInt("NoOfElevators");
	    		String ProjectID=jsobj.getString("ProjectID");
	    		String ProjectName=jsobj.getString("ProjectName");
	    		String ImageName=jsobj.getString("ImageName");
	    		
	    		BuildingMaster objbldg=new BuildingMaster();
	    		objbldg.setID(ID);
	    		objbldg.setBuildingName(BuildingName);
	    		objbldg.setNoOfFloors(NoOfFloors);
	    		objbldg.setNoOfElevators(NoOfElevators);
	    		objbldg.setProjectID(ProjectID);
	    		objbldg.setProjectName(ProjectName);
	    		objbldg.setImageName(ImageName);
	    		
	    		if(ImageName!=null && ImageName.length()>0){
	    			ALS.add(""+ImageName);
	    		}
	    		objbldg.setisDataSyncToWeb(true);
	    		objbldg.setStatusForUpload("Inserted");
	    		
	    		FMDBDatabaseAccess fmdb=new FMDBDatabaseAccess(context);
	    		fmdb.insertORUpdateBuilding(objbldg);
	    	}
	    	catch (Exception e) 
	    	{
				Log.d("Error parseBuildingMasterData",""+e.getMessage());
			}
	  } 	
	    
	  public void parseFloorData(JSONObject jsobj)
	  {
	    	try
	    	{
	    		String ID=jsobj.getString("ID");
	    		String Floor=jsobj.getString("Floor");
	    		
	    		int NoOfApartments=jsobj.getInt("NoOfApartments");
	    		String BuildingID=jsobj.getString("BuildingID");
	    		String BuildingName=jsobj.getString("BuildingName");
	    		String ProjectID=jsobj.getString("ProjectID");
	    		String ProjectName=jsobj.getString("ProjectName");
	    		String ImageName=jsobj.getString("ImageName");
	    		
	    		FloorMaster objflr=new FloorMaster();
	    		objflr.setID(ID);
	    		objflr.setFloor(Floor);
	    		objflr.setNoOfApartments(NoOfApartments);
	    		objflr.setBuildingID(BuildingID);
	    		objflr.setBuildingName(BuildingName);
	    		objflr.setProjectID(ProjectID);
	    		objflr.setProjectName(ProjectName);
	    		objflr.setImageName(ImageName);
	    		
	    		if(ImageName!=null && ImageName.length()>0){
	    			ALS.add(""+ImageName);
	    		}
	    		
	    		objflr.setisDataSyncToWeb(true);
	    		objflr.setStatusForUpload("Inserted");
	    		
	    		FMDBDatabaseAccess fmdb=new FMDBDatabaseAccess(context);
	    		fmdb.insertORUpdateFloor(objflr);
	    		
	    		
	    	}
	    	catch (Exception e)
	    	{
				Log.d("Error parseFloorData",""+e.getMessage());
			}
	  }
	  public void parseProjectMasterData(JSONObject jsobj)
	  {
	    	try
	    	{
	    		String ID=jsobj.getString("ID");
	    		String ProjectName=jsobj.getString("ProjectName");
	    		
	    		int NoOfBuildings=jsobj.getInt("NoOfBuildings");
	    		String Location=jsobj.getString("Location");
	    		String Address1=jsobj.getString("Address1");
	    		String Address2=jsobj.getString("Address2");
	    		String Pincode=jsobj.getString("Pincode");
	    		String City=jsobj.getString("City");
	    		String State=jsobj.getString("State");
	    		String BuilderID=jsobj.getString("BuilderID");
	    		String BuilderName=jsobj.getString("BuilderName");
	    		String ImageName=jsobj.getString("ImageName");
	    		String About="";
	    		try{
	    			About=jsobj.getString("About");
	    		}
	    		catch(Exception e){
	    			About="";
	    		}
	    		 
	    		String str=About.replace("'", " ");
	    		About=str;
	    		
	    		ProjectMaster objprj=new ProjectMaster();
	    		objprj.setID(ID);
	    		objprj.setProjectName(ProjectName);
	    		objprj.setNoOfBuildings(NoOfBuildings);
	    		objprj.setLocation(Location);
	    		objprj.setAddress1(Address1);
	    		objprj.setAddress2(Address2);
	    		objprj.setPincode(Pincode);
	    		objprj.setCity(City);
	    		objprj.setState(State);
	    		objprj.setBuilderID(BuilderID);
	    		objprj.setBuilderName(BuilderName);
	    		objprj.setImageName(ImageName);
	    		objprj.setAbout(About);
	    		
	    		if(ImageName!=null && ImageName.length()>0){
	    			ALS.add(""+ImageName);
	    		}
	    		objprj.setisDataSyncToWeb(true);
	    		objprj.setStatusForUpload("Inserted");
	    		
	    		FMDBDatabaseAccess fmdb=new FMDBDatabaseAccess(context);
	    		fmdb.insertORUpdateProject(objprj);
	    		
	    	}
	    	catch (Exception e)
	    	{
				Log.d("Error parseProjectMasterData",""+e.getMessage());
			}
	    	
	  }
	  public void parseJobTypeData(JSONObject jsobj)
	  {
	    	try
	    	{
	    		String ID=jsobj.getString("ID");
	    		String JobType=jsobj.getString("JobType");
	    		
	    		String JobDetails=jsobj.getString("JobDetails");
	    		
	    		JobType objJob=new JobType();
	    		objJob.setID(ID);
	    		objJob.setJobType(JobType);
	    		objJob.setJobDetails(JobDetails);
	    		
	    		FMDBDatabaseAccess fmdb=new FMDBDatabaseAccess(context);
	    		fmdb.insertORUpdateJobType(objJob);
	    	}
	    	catch (Exception e)
	    	{
				Log.d("Error parseJobTypeData", ""+e.getMessage());
			}
	  }
	  public void parseFaultTypeData(JSONObject jsobj)
	  {
	    	try
	    	{
	    		
	    		String ID=jsobj.getString("ID");
	    		String FaultType=jsobj.getString("FaultType");
	    		
	    		String FaultDetails=jsobj.getString("FaultDetails");
	    		String JobTypeID=jsobj.getString("JobTypeID");
	    		String JobType=jsobj.getString("JobType");
	    		
	    		FaultType objFault=new FaultType();
	    		objFault.setID(ID);
	    		objFault.setJobType(JobType);
	    		objFault.setJobType(JobTypeID);
	    		objFault.setFaultType(FaultType);
	    		objFault.setFaultDetails(FaultDetails);
	    		
	    		FMDBDatabaseAccess fmdb=new FMDBDatabaseAccess(context);
	    		fmdb.insertORUpdateFaultType(objFault);
	    	}
	    	catch (Exception e)
	    	{
				Log.d("Error parseFaultTypeData",""+e.getMessage());
			}
	  }
	  
	  public void parseAptMasterData(JSONObject jsobj)
	  {
		  try
		  {
			  	String ID=jsobj.getString("ID");
	    		String ApartmentNo=jsobj.getString("ApartmentNo");
	    		
	    		
	    		String FloorID=jsobj.getString("FloorID");
	    		String Floor=jsobj.getString("Floor");
	    		String BuildingID=jsobj.getString("BuildingID");
	    		String BuildingName=jsobj.getString("BuildingName");
	    		String ProjectID=jsobj.getString("ProjectID");
	    		String ProjectName=jsobj.getString("ProjectName");
	    		String AptPlanID=jsobj.getString("AptPlanID");
	    		String AptPlanName=jsobj.getString("AptPlanName");
	    		String AptType=jsobj.getString("AptType");
	    		String ImageName=jsobj.getString("ImageName");
	    		ApartmentMaster objApt=new ApartmentMaster();
	    		objApt.setID(ID);
	    		objApt.setApartmentNo(ApartmentNo);
	    		objApt.setFloorID(FloorID);
	    		objApt.setFloor(Floor);
	    		objApt.setBuildingID(BuildingID);
	    		objApt.setBuildingName(BuildingName);
	    		objApt.setProjectID(ProjectID);
	    		objApt.setProjectName(ProjectName);
	    		objApt.setAptPlanID(AptPlanID);
	    		objApt.setAptPlanName(AptPlanName);
	    		objApt.setImageName(ImageName);
	    		objApt.setAptType(AptType);
	    		
	    		if(ImageName!=null && ImageName.length()>0){
	    			ALS.add(""+ImageName);
	    		}
	    		
	    		FMDBDatabaseAccess fmdb=new FMDBDatabaseAccess(context);
	    		fmdb.insertORUpdateApartment(objApt);
	    		
		  }
		  catch (Exception e) 
		  {
			Log.d("Error parseAptMasterData", ""+e.getMessage());
		  }
	  }
	  
	  public void parseApartmentAreaType(JSONObject jsobj)
	  {
	    	try
	    	{
	    		String ID=jsobj.getString("ID");
	    		String ApartmentAreaType=jsobj.getString("ApartmentAreaType");
	    		String CreatedBy=jsobj.getString("CreatedBy");
	    		String CreatedDate=jsobj.getString("CreatedDate");
	    		String ModifiedBy=jsobj.getString("ModifiedBy");
	    		String ModifiedDate=jsobj.getString("ModifiedDate");
	    		String AreaType=jsobj.getString("AreaType");
	    		String EnteredOnMachineID=jsobj.getString("EnteredOnMachineID");
	    		
	    		
	    		ApartmentAreaType objprj=new ApartmentAreaType();
	    		objprj.setID(ID);
	    		objprj.setApartmentAreaType(ApartmentAreaType);
	    		objprj.setCreatedBy(CreatedBy);
	    		objprj.setCreatedDate(CreatedDate);
	    		objprj.setModifiedBy(ModifiedBy);
	    		objprj.setModifiedDate(ModifiedDate);
	    		objprj.setAreaType(AreaType);
	    		objprj.setEnteredOnMachineID(EnteredOnMachineID);
	    		
	    		
	    		
	    		FMDBDatabaseAccess fmdb=new FMDBDatabaseAccess(context);
	    		//fmdb.insertOrUpdateApartmentAreaType(objprj);
	    		
	    	}
	    	catch (Exception e)
	    	{
				Log.d("Error parseApartmentAreaType",""+e.getMessage());
			}
	    	
	  }
	  
	  public void parseInspectionList(JSONObject jsobj)
	  {
	    	try
	    	{
	    		String ID=jsobj.getString("ID");
	    		String ChecklistDescription=jsobj.getString("ChecklistDescription");
	    		String AreaID=jsobj.getString("AreaID");
	    		String AreaType=jsobj.getString("AreaType");
	    		String AreaName=jsobj.getString("AreaName");
	    		String JobType=jsobj.getString("JobType");
	    		String CreatedBy=jsobj.getString("CreatedBy");
	    		String CreatedDate=jsobj.getString("CreatedDate");
	    		String ModifiedBy=jsobj.getString("ModifiedBy");
	    		String ModifiedDate=jsobj.getString("ModifiedDate");
	    		String EnteredOnMachineID=jsobj.getString("EnteredOnMachineID");
	    		
	    		
	    		InspectionList objprj=new InspectionList();
	    		objprj.setID(ID);
	    		objprj.setChecklistDescription(ChecklistDescription);
	    		objprj.setAreaID(AreaID);
	    		objprj.setAreaName(AreaName);
	    		objprj.setJobType(JobType);
	    		objprj.setAreaType(AreaType);
	    		objprj.setCreatedBy(CreatedBy);
	    		objprj.setCreatedDate(CreatedDate);
	    		objprj.setModifiedBy(ModifiedBy);
	    		objprj.setModifiedDate(ModifiedDate);
	    		objprj.setEnteredOnMachineID(EnteredOnMachineID);
	    		
	    		
	    		
	    		FMDBDatabaseAccess fmdb=new FMDBDatabaseAccess(context);
	    		fmdb.insertOrUpdateInspectionList(objprj);
	    		
	    	}
	    	catch (Exception e)
	    	{
				Log.d("Error parseApartmentAreaType",""+e.getMessage());
			}
	    	
	  }
	  public void downloadImage(String imageUrl){
	    	try{
	    		if(imageUrl!=null && imageUrl.length()>0){
	    		String path=Environment.getExternalStorageDirectory()+"/SnagReporter/Pictures/"+imageUrl+"";
	    		File filechk=new File(path);
	    		
	    		if(!(filechk.exists()))
	    		{
	    			//String downloadUrl="http://snag.itakeon.com/uploadimage/"+imageUrl+"";
	    			String downloadUrl=context.getResources().getString(R.string.WS_URL)+"uploadimage/"+imageUrl+"";
	    			URL url=new URL(downloadUrl);
	    			File file=new File(path);
	    			long startTime = System.currentTimeMillis();
	    			Log.d("ImageManager", "download begining");
	    			Log.d("ImageManager", "download url:" + url);
	    			Log.d("ImageManager", "downloaded file name:" + imageUrl);
	    			URLConnection con=url.openConnection();
	    			int length=con.getContentLength();
	    			Log.d("length",""+length);
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
	    	}
	    	catch(Exception e){
	    		Log.d("Error", ""+e.getMessage());
	    	}
	    	
	    }
	  
	  public void parseSnagChecklistEnteries(JSONObject jsObj)
		 {
			 try
			 {
				 
				 	String ID=jsObj.getString("ID");
		    		String ProjectID=jsObj.getString("ParentID");
		    		String BuildingID=jsObj.getString("BuildingID");
		    		
		    		
		    		String FloorID=jsObj.getString("FloorID");
		    		String ApartmentID=jsObj.getString("ApartmentID");
		    		String AptAreaID=jsObj.getString("AptAreaID");
		    		String SnagID=jsObj.getString("SnagID");
		    		String ChecklistID=jsObj.getString("ChecklistID");
		    		String ChecklistDescription=jsObj.getString("ChecklistDescription");
		    		String ChecklistEntry=jsObj.getString("ChecklistEntry");
		    		String CreatedBy=jsObj.getString("CreatedBy");
		    		String CreatedDate=jsObj.getString("CreatedDate");
		    		String ModifiedDate=jsObj.getString("ModifiedDate");
		    		String ModifiedBy=jsObj.getString("ModifiedBy");
		    		String EnteredOnMachineID=jsObj.getString("EnteredOnMachineID");
		    	
		    		SnagChecklistEntries obj=new SnagChecklistEntries();
				obj.ID=(String)(ID);
				obj.ProjectID=(String)(ProjectID);
	     		obj.BuildingID=(String)(BuildingID);
	     		obj.FloorID=(String)(FloorID);
	     		obj.ApartmentID=(String)(ApartmentID);
	     		obj.AptAreaID=(String)(AptAreaID);
	     		obj.SnagID=(String)(SnagID);
	     		obj.ChecklistID=(String)(ChecklistID);
	     		obj.ChecklistDescription=(String)(ChecklistDescription);
	     		obj.CheckListEntry=(String)(ChecklistEntry);
	     		obj.CreatedBy=(String)(CreatedBy);
	     		obj.CreatedDate=(String)(CreatedDate);
	     		obj.ModifiedDate=(String)(ModifiedDate);
	     		obj.ModifiedBy=(String)(ModifiedBy);
	     		obj.EnteredOnMachineID=(String)(EnteredOnMachineID);
	     		
	     		
	     		
	     		FMDBDatabaseAccess fmdb=new FMDBDatabaseAccess(context);
	     		SnagChecklistEntries[] arr=new SnagChecklistEntries[1];
	     		arr[0]=obj;
	     		fmdb.insertORUpdateSnagChecklistEntry(arr[0]);
	     		
			 }
			 catch (Exception e)
			 {
				Log.d("Error parseSnagData ", ""+e.getMessage());
			 }	
		 }
	  
	  
	  public void parseSnagChecklistMaster(JSONObject jsObj)
		 {
			 try
			 {
				 
				 	String ID=jsObj.getString("ID");
		    		String JobType=jsObj.getString("Jobtype");
		    		String FaultType=jsObj.getString("FaultType");
		 		
		    		String ChecklistDescription=jsObj.getString("ChecklistDescription");
		    		
		    		String CreatedBy=jsObj.getString("CreatedBy");
		    		String CreatedDate=jsObj.getString("CreatedDate");
		    		String ModifiedDate=jsObj.getString("ModifiedDate");
		    		String ModifiedBy=jsObj.getString("ModifiedBy");
		    		String EnteredOnMachineID=jsObj.getString("EnteredOnMachineID");
		    	
		    		SnagChecklistMaster obj=new SnagChecklistMaster();
				obj.setID(ID);
				obj.setJobType(JobType);
	     		obj.setFaultType(FaultType);
	     		obj.setChecklistDescription(ChecklistDescription);
	     		obj.setCreatedBy(CreatedBy);
	     		obj.setCreatedDate(CreatedDate);
	     		obj.setModifiedDate(ModifiedDate);
	     		obj.setModifiedBy(ModifiedBy);
	     		obj.setEnteredOnMachineID(EnteredOnMachineID);
	     		
	     		
	     		
	     		FMDBDatabaseAccess fmdb=new FMDBDatabaseAccess(context);
	     		SnagChecklistMaster[] arr=new SnagChecklistMaster[1];
	     		arr[0]=obj;
	     		fmdb.insertORUpdateSnagChecklistMaster(arr[0]);
	     		
			 }
			 catch (Exception e)
			 {
				Log.d("Error parseSnagData ", ""+e.getMessage());
			 }	
		 }
	  
	  
	  
	  ////All Sync Code
	  
	  protected class StartSyncProgress extends AsyncTask<Integer , Integer, Void> {
			
	    	@Override
	        protected void onPreExecute() {  
	    		try{
	        	if(!mProgressDialog2.isShowing()){
	        		mProgressDialog2.setCancelable(false);
	        		mProgressDialog2.setMessage("Synchronizing...");
	        		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
	        		mProgressDialog2.show();
	        	}
	    		}
	        	catch(Exception e)
	        	{
	        		Log.d("Exception",""+e.getMessage());
	        	}
	        }      
	        @Override
	        protected Void doInBackground(Integer... params) {
	        	
	        	try{
	        	syncAllLocalDataToServer();
	        	}
	        	catch(Exception e)
	        	{
	        		Log.d("Exception",""+e.getMessage());
	        	}
	    		
	            return null;
	        }
	        @Override
	        protected void onPostExecute(Void result1) {
	        	try{
	        	if(arrD_URL!=null && arrD_URL.size()>0)
	        		TotalImagesToDownload=arrD_URL.size();
	        	StartUploadImages task=new StartUploadImages();
	        		task.execute(10);
	        	}
	        	catch(Exception e)
	        	{
	        		Log.d("Exception",""+e.getMessage());
	        	}
	         
	}
	}
	  protected class StartUploadImages extends AsyncTask<Integer , Integer, Void> {
		  String url="";
		  boolean isAllDone=false;
		  @Override
	        protected void onPreExecute() {  
			  try{
	        	if(!mProgressDialog2.isShowing()){
	        		mProgressDialog2.setCancelable(false);
	        		
	        		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
	        		mProgressDialog2.show();
	        	}
	        	if(arrD_URL!=null && arrD_URL.size()>0){
	        		int current=TotalImagesToDownload-arrD_URL.size()+1;
		        	mProgressDialog2.setMessage("Uploading "+current+" of "+TotalImagesToDownload+" images ...");
		        	
	        		url=arrD_URL.get(0);
	        	}
	        	else
	        		isAllDone=true;
			  }
	        	catch(Exception e)
	        	{
	        		Log.d("Exception",""+e.getMessage());
	        	}
	        }      
	        @Override
	        protected Void doInBackground(Integer... params) {
	        	
	        	try{
	        		//url="7ccd37d0-d042-44e0-939a-891a5fd5d5bb";
	            	if(url!=null && url.length()!=0 && !url.equals(""))
	            	{
	            		String METHOD_NAME = "UploadFileDataParam";
	            		String NAMESPACE = "http://tempuri.org/";
	            		String URL = ""+context.getResources().getString(R.string.WS_URL).toString()+"SnagReporter_WebS.asmx";//SnagReporter_WCF.svc
	            		String SOAP_ACTION = "http://tempuri.org/UploadFileDataParam";//
	            		String res = "";
	            		try 
	            		{
	            			SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
	            	    
	            			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
	            			new MarshalBase64().register(envelope);
	            			envelope.dotNet = true;
	            			envelope.setOutputSoapObject(request);
	            	    
	            	    
	            			String FileName=""+url+".jpg";
	            			File file=new File(Environment.getExternalStorageDirectory()+File.separator+"SnagReporter/Pictures/"+FileName);
	            			//strFilePath=file.toString();
	            			String FilePath=file.toString();
	            			Bitmap myImg=BitmapFactory.decodeFile(FilePath);
	            			byte[] arr=getBytesFromBitmap(myImg);
	            			String FilePosition="0";
	             	    
	            			String ba1=Base64.encode(arr);
	            	    
	            			request.addProperty("FileName",FileName);
	            			request.addProperty("BufferData",ba1);
	            			request.addProperty("FilePosition",FilePosition);
	            	    
	            	    
	            			HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
	            			androidHttpTransport.call(SOAP_ACTION, envelope);
	            			Object resonse=envelope.getResponse();
	            			String output = resonse.toString();
	            	    
	            	    
	            	    
	            		
	            			//jObject = new JSONObject(resultData);
	            	}
	            	 catch(Exception e)
	            	 {
	            		 String str=e.getMessage().toString();
	            		 Log.d("Error=", ""+e.getMessage()); 
	            	 }
	            	}
	            	if(!isAllDone){
	            	if(arrD_URL!=null && arrD_URL.size()>0){
	            		arrD_URL.remove(0);
	            	}
	            	if(arrD_URL==null || (arrD_URL!=null && arrD_URL.size()==0)){
	            		isAllDone=true;
	            	}
	            	}
	        	}
	        	catch(Exception e){
	        		
	        	}
	        	
	    		
	            return null;
	        }
	        @Override
	        protected void onPostExecute(Void result1) {
	        	try{
	        	if(isAllDone){
	        		StartDownloadData task=new StartDownloadData();
	        		task.execute(10);
	        	}
	        	else{
	        		StartUploadImages task=new StartUploadImages();
	        		task.execute(10);
	        	}
	        	}
	        	catch(Exception e)
	        	{
	        		Log.d("Exception",""+e.getMessage());
	        	}
	         
	}
	  }
	
	
	int FromRowNumber = 0;
	protected class StartDownloadData extends AsyncTask<Integer , Integer, Void> {
		int max = 0;
		boolean result = false;
		String resultData = "";
		TimeStampMaster TS = null;
		int NUMBEROFROW = 50;
		
		@Override
		protected void onPreExecute() {  
			try{
			if(!mProgressDialog2.isShowing()) {
				mProgressDialog2.setCancelable(false);
				mProgressDialog2.setMessage("Importing...");
				setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
				mProgressDialog2.show();
			}
			
			FMDBDatabaseAccess db = new FMDBDatabaseAccess(context);
			max =db.getMaxTimeStamp();
			TS = db.getTimeStampForSequence(Sequence);//
			if(Sequence==1){
				Log.d("24", "24");
			}
			mProgressDialog2.setMessage("Importing " + TS.getCaption().toString() + " ...");
			}
			catch(Exception e)
			{
				Log.d("Exception", ""+e.getMessage());
			}
		}      
	    
		@Override
		protected Void doInBackground(Integer... params) {
			try {
				SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME_SyncTableData);
				SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
				
//				if(TS.getFromRowNo()==0)
//					TS.setFromRowNo(TS.getFromRowNo()+600);
				
				envelope.bodyOut = request;
				envelope.dotNet = true;
				envelope.setOutputSoapObject(request);
				Log.d("tablename", ""+TS.getTableName());
				request.addProperty("_strTableName",TS.getTableName());
				request.addProperty("_strModifiedDate",TS.getTimeStampValue());
				if(TS.getTableName().equalsIgnoreCase("ApartmentDetails"))
					
					request.addProperty("_strModifiedBy","");
				else
					request.addProperty("_strModifiedBy","");//RegUserID);
				request.addProperty("_strNoRows",NUMBEROFROW);
				request.addProperty("_strFromRowNumber",TS.getFromRowNo());
				
				Log.d("DATA of = ", TS.getCaption());
	        	
				HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
				androidHttpTransport.call(SOAP_ACTION_SyncTableData, envelope);
				
				Object response=envelope.getResponse();
				resultData = response.toString().toUpperCase(Locale.ENGLISH);

				if(TS.getTableName().equalsIgnoreCase("TradeDetail")){
					//resultData.replaceAll(""+"\n", "");
					resultData.replaceAll("'","");
				}
				
				
				Log.d("DATA of = ", TS.getTableName() + " : " + resultData);
			}
			catch(Exception e) {
				Log.d("Exception startDownloadData doInBackground = ", e.getMessage());
			}
			return null;
		}
	    
		@Override
		protected void onPostExecute(Void result1) {
			try{
			JSONObject jObject=null;
			JSONArray arr=null;
			
			try {
				if(!resultData.equalsIgnoreCase("ANYTYPE{}")) {
					
					jObject = new JSONObject(resultData);
  				arr = jObject.getJSONArray("DATA");
  				
  				if(arr!=null) {
  					for(int i=0;i<arr.length();i++) {
  						JSONObject geometry = arr.getJSONObject(i);
  						
  						FMDBDatabaseAccess db = new FMDBDatabaseAccess(context);
  						Boolean succ = db.parseDataTable(geometry,TS.getTableName());
  		        		
  						if(geometry.has("IMAGENAME")){ 
  							String str=geometry.getString("IMAGENAME");
  							if(str!=null && !str.equalsIgnoreCase("null") && str.length()>0)
  								ALS.add(geometry.getString("IMAGENAME"));
  						
  						}
  						
  						Log.d("Inserted in TABLE : " + TS.getTableName(), succ.toString() + " FromRowNumber : " + FromRowNumber);
  		        		
  						try{
  						int rowNo = Integer.parseInt(geometry.getString("RN"));
  						//TS.setTimeStampValue(lastModifiedDate);
  						TS.setFromRowNo(rowNo);
  						}
  						catch(Exception e){
  							//if(i==arr.length()-1)
  							//	TS.setFromRowNo(TS.getFromRowNo()+arr.length());
  						}
  					}
  				}
				}
			}
			catch(Exception e) {
				Log.d("Exception startDownloadData onPostExecute = ", e.getMessage());
				//TS.setFromRowNo(FromRowNumber);
			}
			
			if(arr!=null && arr.length()>0) {
				FMDBDatabaseAccess db = new FMDBDatabaseAccess(context);
				db.updateFromRowNoForSequence(TS);
				db = null;
			}
			
			if(arr!=null && arr.length()==NUMBEROFROW) {
				FromRowNumber += NUMBEROFROW;
				
				StartDownloadData task = new StartDownloadData();
				task.execute(10);
			}
			else if(arr!=null && arr.length()<NUMBEROFROW && arr.length()!=0) {
				if(Sequence!=max) {
					try {
					JSONObject jsnLastData = arr.getJSONObject(arr.length()-1);
					TS.setTimeStampValue(jsnLastData.getString("MODIFIEDDATE"));
					
					FMDBDatabaseAccess db = new FMDBDatabaseAccess(context);
					db.updateTimeStampForSequence(TS);
					db = null;
					
					Sequence++;
					TS.setFromRowNo(0);
					StartDownloadData task = new StartDownloadData();
					task.execute(10);
					}
					catch (Exception e) {
						e.printStackTrace();
					}
				}
				else {
					if(ALS!=null && ALS.size()>0) {
						TotalImages = ALS.size();
						StartDownloadImages task = new StartDownloadImages();
						task.execute(10);
					}
					else {
						mProgressDialog2.setMessage("Synchronization Complete...");
						setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
						mProgressDialog2.dismiss();
					}
				}
			}
			else if(arr!=null && arr.length()==0)
			{
				if(Sequence!=max) {
				Sequence++;
				FromRowNumber = 0;
				StartDownloadData task = new StartDownloadData();
				task.execute(10);
				}
				else{
					if(ALS!=null && ALS.size()>0) {
						TotalImages = ALS.size();
						StartDownloadImages task = new StartDownloadImages();
						task.execute(10);
					}
					else {
						mProgressDialog2.setMessage("Synchronization Complete...");
						setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
						mProgressDialog2.dismiss();
					}
				}
			}
			else{
				if(Sequence!=max){
					Sequence++;
					FromRowNumber = 0;
					StartDownloadData task = new StartDownloadData();
					task.execute(10);
				}
				else{
					if(ALS!=null && ALS.size()>0) {
						TotalImages = ALS.size();
						StartDownloadImages task = new StartDownloadImages();
						task.execute(10);
					}
					else {
						mProgressDialog2.setMessage("Synchronization Complete...");
						setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
						mProgressDialog2.dismiss();
					}
				}
			}
			}
			catch(Exception e)
			{
				Log.d("Exception",""+e.getMessage());
			}
			//
		}   
	}
	  
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
			
			
			
			try {	
			for(int i=0;i<strTableNameList.length;i++) {
				mydb.close();
				mydb = context.openOrCreateDatabase(DBNAME, Context.MODE_PRIVATE, null);
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
						cursorColumnName.close();
					}
				}
					
				if(cursor.moveToFirst()) {
					do {	
						
						 strInsColValues = "";
						 
						 strUpdColValues = "";
						 
						 strKeyColValues = "";
						 
						
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
						String str=cursor.getString(cursor.getColumnIndex("StatusForUpload"));
						
						if(strTableName.equalsIgnoreCase("TradeAptAreaMaster")){
							Log.d("", "");
						}
						
						if((cursor.getString(cursor.getColumnIndex("StatusForUpload")))!=null && (cursor.getString(cursor.getColumnIndex("StatusForUpload"))).equalsIgnoreCase("INSERTED")) {						
							result = saveNewDataToTheDatabaseServer(strInsColNames,strInsColValues,strTableName);
							
							if(result) updateDatabase(tempStatusColName, tempStatusColValue, strTableName, strKeyColNames, strKeyColValues, strKeyColDataTypes);
							
						}
						else if((cursor.getString(cursor.getColumnIndex("StatusForUpload")))!=null && (cursor.getString(cursor.getColumnIndex("StatusForUpload"))).equalsIgnoreCase("MODIFIED")) {
							result = updateDataToTheDatabaseServer(strUpdColNames,strUpdColValues,strTableName,strKeyColNames,strKeyColValues,strKeyColDataTypes);
							if(result) updateDatabase(tempStatusColName, tempStatusColValue, strTableName, strKeyColNames, strKeyColValues, strKeyColDataTypes);
						}
						
						if(str.equalsIgnoreCase("Inserted") || str.equalsIgnoreCase("Modified")){
							String[] strColNamesArr = tempStatusColName.split("~");
							String[] strColValuesArr = tempStatusColValue.split("~");
							if(strColNamesArr!=null && strColNamesArr.length>0){
								
								for(int k=0;k<strColNamesArr.length;k++){
									if(strColNamesArr[k]!=null && strColNamesArr[k].length()>0){
									if(strColNamesArr[k].equalsIgnoreCase("ImageName") || strColNamesArr[k].equalsIgnoreCase("PictureURl1")
											|| strColNamesArr[k].equalsIgnoreCase("PictureURl2")|| strColNamesArr[k].equalsIgnoreCase("PictureURl3")
											||strColNamesArr[k].equalsIgnoreCase("ResolveDatePictureURL1") ||strColNamesArr[k].equalsIgnoreCase("ResolveDatePictureURL2")
											|| strColNamesArr[k].equalsIgnoreCase("ResolveDatePictureURL3") ||strColNamesArr[k].equalsIgnoreCase("ReinspectedUnresolvedDatePictureURL1")
											||strColNamesArr[k].equalsIgnoreCase("ReinspectedUnresolvedDatePictureURL2")||strColNamesArr[k].equalsIgnoreCase("ReinspectedUnresolvedDatePictureURL3") ){
										String val=strColValuesArr[k];
										if(val!=null && val.length()>0 && !val.equalsIgnoreCase("null"))
										{
											if(arrD_URL==null || arrD_URL.size()==0)
												arrD_URL=new ArrayList<String>();
											arrD_URL.add(val);
											
										}
										
									}
									}
								}
							}
						}
						
						
						
					} while(cursor.moveToNext());
				}
				cursor.close();
				mydb.close();
			}
		}
		catch (Exception e) {
			Log.d("Exception", ""+e.getMessage());
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
				Log.d("getAllTableName ", ""+e.getMessage());
				e.printStackTrace();
			}
			mydb.close();
			 return strTablNameArr;
		}
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
				
				if(!strKeyColDataType.equalsIgnoreCase(""))
				{
					for(int i=0;i<(strKeyColNameArr.length-1);i++)
						strQuery += strKeyColNameArr[i] + "='" + strKeyColValueArr[i] + "', AND ";
						strQuery += strKeyColNameArr[(strKeyColNameArr.length-1)] + "='" + strKeyColValueArr[(strKeyColNameArr.length-1)] + "'";
				}	
				
				Log.d("UPDATE STMT GENERIC FUNCTION : ", ""+strQuery);
			
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
			Log.d("saveNewDataToTheDatabaseServer strColNames",""+strColNames);
			Log.d("saveNewDataToTheDatabaseServer strColValues",""+strColValues);
			Log.d("saveNewDataToTheDatabaseServer strTableName",""+strTableName);
			Boolean result=true;
			try{
			SoapObject request=new SoapObject(NAMESPACE, METHOD_NAME_SaveNewDataToTheDataBase);
			SoapSerializationEnvelope envelope=new SoapSerializationEnvelope(SoapEnvelope.VER11);
			envelope.dotNet=true;
			envelope.setOutputSoapObject(request);
			
			  String CoumnNames=strColNames;
			  String Values=strColValues;
			  String TableName=strTableName;
			request.addProperty("_strCoumnNames", CoumnNames);
			request.addProperty("_strValues", Values);
			request.addProperty("_strTableName", TableName);
			
			HttpTransportSE httptransport=new HttpTransportSE(URL);
			httptransport.call(SOAP_ACTION_SaveNewDataToTheDataBase, envelope);
			Object response=envelope.getResponse();
			String output=response.toString();
			
			
			if(output!=null)
			{
				try{
    		    	JSONObject jObject = new JSONObject(output);
			        jObject=jObject.getJSONObject("Data");
			        String Value=jObject.getString("Value");
			        if(Value.equalsIgnoreCase("true")){
			        	result=true;
			        }
			        else{
			        	result=false;
			        }
    		    	}
    		    	catch(Exception e){
    		    		Log.d("Error parsing result", "");
    		    	}
			}
			}
			catch(Exception e){
				return result;
			}
			
			
			return result;
		}
		public Boolean updateDataToTheDatabaseServer(String strColNames,String strColValues,String strTableName,String strKeyColName,String strKeyColValue,String strKeyColDataType) {
			Log.d("updateDataToTheDatabaseServer strColNames",""+strColNames);
			Log.d("updateDataToTheDatabaseServer strColValues",""+strColValues);
			Log.d("updateDataToTheDatabaseServer strTableName",""+strTableName);
			Log.d("updateDataToTheDatabaseServer strKeyColName",""+strKeyColName);
			Log.d("updateDataToTheDatabaseServer strKeyColValue",""+strKeyColValue);
			Log.d("updateDataToTheDatabaseServer strKeyColDataType",""+strKeyColDataType);
			Boolean result=true;
			try {
    		    SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME_UpdateDataToTheDataBase);
    		    
    		    SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
    		    envelope.dotNet = true;
    		    envelope.setOutputSoapObject(request);
    		    
    		    
    		    String CoumnNames=strColNames;
    		    String Values=strColValues;
    		    String TableName=strTableName;
    		    String KeyColName=strKeyColName;
    		    String ColDataType=strKeyColDataType;
    		    String ColValue=strKeyColValue;
    		    
    		    request.addProperty("_strCoumnNames",CoumnNames);
    		    request.addProperty("_strValues",Values);
    		    request.addProperty("_strTableName",TableName);
    		    request.addProperty("_strKeyColName",KeyColName);
    		    request.addProperty("_strKeyColDataType",ColDataType);
    		    request.addProperty("_keyColValue",ColValue);
    		    
    		    
    		    HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
    		    androidHttpTransport.call(SOAP_ACTION_UpdateDataToTheDataBase, envelope);
    		    Object resonse=envelope.getResponse();
    		    String output = resonse.toString();
    		    if(output!=null)
				{
    		    	try{
	    		    	JSONObject jObject = new JSONObject(output);
				        jObject=jObject.getJSONObject("Data");
				        String Value=jObject.getString("Value");
				        if(Value.equalsIgnoreCase("true")){
				        	result=true;
				        }
				        else{
				        	result=false;
				        }
	    		    	}
	    		    	catch(Exception e){
	    		    		Log.d("Error parsing result", "");
	    		    	}
				}
    		    
    		    
    		    
    		}
    		 catch(Exception e){
    			 Log.d("Error Modified Project Sync=", ""+e.getMessage()); 
    			 return result;
    		 }
			return result;
		}
		
		public Boolean parseDataTable(JSONObject jsonColValues,String strTableName) {		

			String strQuery = "PRAGMA table_info(" + strTableName + ")";		
			String strInsertQuery = "INSERT INTO " + strTableName;
			String strColNames = "";
			String strColValues = "";		
			Boolean success = null;
			
			Log.d("TABLE NAME : " , ""+strTableName);
			
			mydb = context.openOrCreateDatabase(DBNAME, Context.MODE_PRIVATE, null);
			Cursor cursor = mydb.rawQuery(strQuery, null);	
			
			try {		
		
				if(cursor.moveToFirst()) {
					
					do {
						try{
						String strCurrColName = cursor.getString(1).toUpperCase(Locale.ENGLISH);
						String strCurrColValue = "";
						
						if(jsonColValues.has(strCurrColName)==true)
						{
							if(strColNames=="") {
								strColNames = strCurrColName;
								strCurrColValue = jsonColValues.getString(strCurrColName);
								strColValues = "'" + jsonColValues.getString(strCurrColName);					
							}
							else {
								strColNames += "," + strCurrColName;
								strCurrColValue = jsonColValues.getString(strCurrColName);
								strColValues += "','" + jsonColValues.getString(strCurrColName);
							}
						}
						else 
						{
							if(strColNames=="") {
								strColNames = strCurrColName;
								if(strColNames.endsWith("ISDATASYNCTOWEB") || strColNames.endsWith("ISSYNCTOWEB")|| strColNames.endsWith("ISSYNCEDTOWEB")) strColValues = "'INSERTED";
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
					while (cursor.moveToNext());	
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
				Log.d("Exception parseDataTable ",""+ e.getMessage());
			}
			cursor.close();
			mydb.close();   
			return success;	
		}
	  ////All Sync Code Close
}
