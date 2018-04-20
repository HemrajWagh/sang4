/*
 * Copyright (C) 2011 Scott Lund
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


package com.snagreporter.imagemap;

import java.io.ByteArrayOutputStream;
import java.io.File;

import org.json.JSONObject;
import org.kobjects.base64.Base64;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.MarshalBase64;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.CompressFormat;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.Toast;

import com.snagreporter.AddDefect;
import com.snagreporter.R;
import com.snagreporter.R.string;



import com.snagreporter.database.FMDBDatabaseAccess;
import com.snagreporter.entity.FaultType;
import com.snagreporter.entity.JobType;
import com.snagreporter.entity.SnagMaster;

public class ImageMapActivity extends Activity {
 ImageMap mImageMap;
	SnagMaster currentsng;
	boolean isOnline=false;
	String  RegUserID;
     public static Float currentXp=0.0f,currentYp=0.0f;
     ImageMap objMap;
     JobType[] AddedSnagType;
     FaultType[] AddedFaultType;
     String PhotoURl1,PhotoURl2,PhotoURl3;
     String[] photo;
     String ImageSource="";
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_map);
        
        // find the image map in the view
        try{
        mImageMap = (ImageMap)findViewById(R.id.map); 
        ImageSource=getIntent().getExtras().getString("ImageName");
        String FilePath=Environment.getExternalStorageDirectory()+"/SnagReporter/Pictures/"+ImageSource;
		Bitmap bp=BitmapFactory.decodeFile(FilePath);
		mImageMap.setImageBitmap(bp);
        currentsng=(SnagMaster)getIntent().getExtras().get("currentsnag");
        AddedSnagType=(JobType[])getIntent().getExtras().get("AddedSnagType");
        AddedFaultType=(FaultType[])getIntent().getExtras().get("AddedFaultType");
        photo=(String[])getIntent().getExtras().get("photo");
        SharedPreferences SP = getSharedPreferences("AppDelegate",MODE_PRIVATE);
        RegUserID=SP.getString("RegUserID", "");
        Boolean id=SP.getBoolean("isOnline", false);
        isOnline=id;
        }
		catch(Exception e)
		{
			Log.d("Exception",""+e.getMessage());
		}
        Log.d("status ",""+isOnline);
        
    }
  
   
	public void SaveClick(View v)
    {
		
		try{
		//Log.d("x y",""+currentXp+" "+currentYp);
		//objMap=new ImageMap(getApplicationContext());
		//currentYp=objMap.currentY;
		//currentXp=objMap.currentX;
		Display display = getWindowManager().getDefaultDisplay(); 
		float screenWidth = display.getWidth();
		float screenHeight = display.getHeight();
		PhotoURl1=photo[0];
		PhotoURl2=photo[1];
		PhotoURl3=photo[2];
		
		float xv=(currentXp/screenWidth);
		float yv=(currentYp/screenHeight);
//		double x=(double)xv;
//		double y=(double)yv;
		currentsng.setXValue(xv);
		currentsng.setYValue(yv);
		DownloadDataFromWeb task=new DownloadDataFromWeb();
		task.execute(10);
		setResult(10002);
		finish();
    	//Toast.makeText(this,"You click "+currentXp+" "+currentYp,Toast.LENGTH_SHORT).show();
		}
		catch(Exception e)
		{
			Log.d("Exception",""+e.getMessage());
		}
    	
    }
    protected class DownloadDataFromWeb extends AsyncTask<Integer , Integer, Void> {
		ProgressDialog mProgressDialog = new ProgressDialog(ImageMapActivity.this);
    	JSONObject jObject;
    	String output="";
    	SnagMaster obj=currentsng;
    	
        @Override
        protected void onPreExecute() { 
        	if(isOnline)
        	{
        	/*mProgressDialog.setCancelable(false);
        	mProgressDialog.setMessage("Loading...");
            mProgressDialog.show();*/
        	}
        }      
        @Override
        protected Void doInBackground(Integer... params) {
        	
        	
        	
        	try
    		{

        		
        		
        		
        		if(isOnline){
        			if(AddedSnagType!=null && AddedSnagType.length>0){
        				SyncJobtype task=new SyncJobtype();
        				task.execute(10);
        			}
        			if(AddedFaultType!=null && AddedFaultType.length>0){
        				SyncFaulttype task1=new SyncFaulttype();
        				task1.execute(10);
        			}
        		
        		String METHOD_NAME = "SaveNewDataToTheDataBase";
	    		String NAMESPACE = "http://tempuri.org/";
	    		String URL = ""+getResources().getString(R.string.WS_URL).toString()+"SnagReporter_WebS.asmx";//SnagReporter_WCF.svc
	    		String SOAP_ACTION = "http://tempuri.org/SaveNewDataToTheDataBase";//
	    		String res = "";
	    		try {
	    		    SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
	    		    
	    		    SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
	    		    envelope.dotNet = true;
	    		    envelope.setOutputSoapObject(request);
	    		    
	    		    
	    			 
	    		    String CoumnNames="ID~SnagType~SnagDetails~PictureURL1~PictureURL2~PictureURL3~ProjectID~ProjectName~BuildingID~BuildingName~FloorID~Floor~ApartmentID~Apartment~AptAreaName~SnagStatus~ResolveDate~ExpectedInspectionDate~FaultType~InspectorID~InspectorName~Cost~CostTo~PriorityLevel~AllocatedTo~AllocatedToName~XValue~YValue~ContractorID~ContractorName~SubContractorID~SubContractorName~SubSubContractorID~SubSubContractorName~ReportDate~CreatedBy~ContractorStatus~AptAreaID";
	    		    String Values=""+obj.getID()+"~"+obj.getSnagType()+"~"+obj.getSnagDetails()+"~"+obj.getPictureURL1()+"~"+obj.getPictureURL2()+"~"+obj.getPictureURL3()+"~"+obj.getProjectID()+"~"+obj.getProjectName()+"~"+obj.getBuildingID()+"~"+obj.getBuildingName()+"~"+obj.getFloorID()+"~"+obj.getFloor()+"~"+obj.getApartmentID()+"~"+obj.getApartment()+"~"+obj.getAptAreaName()+"~"+obj.getSnagStatus()+"~"+obj.getResolveDate()+"~"+obj.getExpectedInspectionDate()+"~"+obj.getFaultType()+"~"+obj.getInspectorID()+"~"+obj.getInspectorName()+"~"+obj.getCost()+"~"+obj.getCostTo()+"~"+obj.getSnagPriority()+"~"+obj.getAllocatedTo()+"~"+obj.getAllocatedToName()+"~"+obj.getXValue()+"~"+obj.getYValue()+"~"+obj.getContractorID()+"~"+obj.getContractorName()+"~"+obj.getSubContractorID()+"~"+obj.getSubContractorName()+"~"+obj.getSubSubContractorID()+"~"+obj.getSubSubContractorName()+"~"+obj.getReportDate()+"~"+RegUserID+"~"+obj.getContractorStatus()+"~"+obj.getAptAreaID();
	    		    String TableName="SnagMaster";
	    		    
	    		    request.addProperty("_strCoumnNames",CoumnNames);
	    		    request.addProperty("_strValues",Values);
	    		    request.addProperty("_strTableName",TableName);
	    		    
	    		    
	    		    HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
	    		    androidHttpTransport.call(SOAP_ACTION, envelope);
	    		    Object resonse=envelope.getResponse();
	    		    output = resonse.toString();
	    		    
	    		    
	    		    
	    			
					//jObject = new JSONObject(resultData);
	    		}
	    		 catch(Exception e){
	    			 Log.d("Error=", ""+e.getMessage()); 
	    		 }
    			
        		}
    			
    		}
    		catch (Exception e){
                Log.d("Error=", ""+e.getMessage()); 
    		}
            return null;
        }
        @Override
        protected void onPostExecute(Void result) {
        	
        	try{
  		
        	if(isOnline)
        	{
    		uploadImage(PhotoURl1);
    		uploadImage(PhotoURl2);
    		uploadImage(PhotoURl3);
    		
        	}
        	obj.setStatusForUpload("Inserted");
        	if(isOnline)
        		obj.setIsDataSyncToWeb(true);
        	else
        		obj.setIsDataSyncToWeb(false);
    		FMDBDatabaseAccess db=new FMDBDatabaseAccess(ImageMapActivity.this);
    		db.InsertIntoSnagMaster(obj);                                               
    		
    		//if(mProgressDialog.isShowing())
    		//	mProgressDialog.dismiss();
        	
    		
    		db=null;
    		//setResult(10002);
    		//finish();
        	
        	}
    		catch(Exception e)
    		{
    			Log.d("Exception",""+e.getMessage());
    		}
        	
            
        }
         
}
    protected class SyncJobtype extends AsyncTask<Integer , Integer, Void> {
    	JobType objJob=null;
    	boolean isAlldone=false;
        @Override
        protected void onPreExecute() {
        	try{
        	FMDBDatabaseAccess db=new FMDBDatabaseAccess(ImageMapActivity.this);
        	objJob=db.getJobTypeNotSynced();
        	if(objJob==null){
        		isAlldone=true;
        	}
        	}
    		catch(Exception e)
    		{
    			Log.d("Exception",""+e.getMessage());
    		}
        }      
        @Override
        protected Void doInBackground(Integer... params) {
        	if(objJob!=null){
        		String METHOD_NAME = "SaveNewDataToTheDataBase";
        		String NAMESPACE = "http://tempuri.org/";
        		String URL = ""+getResources().getString(R.string.WS_URL).toString()+"SnagReporter_WebS.asmx";//SnagReporter_WCF.svc
        		String SOAP_ACTION = "http://tempuri.org/SaveNewDataToTheDataBase";//
        		String res = "";
        		try {
        		    SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        		    
        		    SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        		    envelope.dotNet = true;
        		    envelope.setOutputSoapObject(request);
        		    
        		    
        		    String CoumnNames="ID~JobType~JobDetails";
        		    String Values=""+objJob.getID()+"~"+objJob.getJobType()+"~"+objJob.getJobDetails();
        		    String TableName="JobType";
        		    
        		    request.addProperty("_strCoumnNames",CoumnNames);
        		    request.addProperty("_strValues",Values);
        		    request.addProperty("_strTableName",TableName);
        		    
        		    
        		    HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
        		    androidHttpTransport.call(SOAP_ACTION, envelope);
        		    Object resonse=envelope.getResponse();
        		    String output = resonse.toString();
        		
        		    
        		    
        		    
        			
    				
        		}
        		 catch(Exception e){
        			 Log.d("Error=", ""+e.getMessage()); 
        		 }
        	}
        	
        	
        	
            return null;
        }
        @Override
        protected void onPostExecute(Void result) {
        	try{
        	FMDBDatabaseAccess db=new FMDBDatabaseAccess(ImageMapActivity.this);
        	if(objJob!=null){
        		db.updateJobTypeSynced(objJob.getID());
        	}
        	if(!isAlldone){
        		SyncJobtype task=new SyncJobtype();
        		task.execute(10);
        	}
        	
        	}
    		catch(Exception e)
    		{
    			Log.d("Exception",""+e.getMessage());
    		}
        	
            
        }
         
    }
    protected class SyncFaulttype extends AsyncTask<Integer , Integer, Void> {
    	FaultType objFlt=null;
    	boolean isAlldone=false;
        @Override
        protected void onPreExecute() { 
        	try{
        	FMDBDatabaseAccess db=new FMDBDatabaseAccess(ImageMapActivity.this);
        	objFlt=db.getFaultTypeNotSynced();
        	if(objFlt==null){
        		isAlldone=true;
        	}
        	}
    		catch(Exception e)
    		{
    			Log.d("Exception",""+e.getMessage());
    		}
        }      
        @Override
        protected Void doInBackground(Integer... params) {
        	if(objFlt!=null){
        		String METHOD_NAME = "SaveNewDataToTheDataBase";
        		String NAMESPACE = "http://tempuri.org/";
        		String URL = ""+getResources().getString(R.string.WS_URL).toString()+"SnagReporter_WebS.asmx";//SnagReporter_WCF.svc
        		String SOAP_ACTION = "http://tempuri.org/SaveNewDataToTheDataBase";//
        		String res = "";
        		try {
        		    SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        		    
        		    SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        		    envelope.dotNet = true;
        		    envelope.setOutputSoapObject(request);
        		    
        		    
        		    String CoumnNames="ID~FaultType~FaultDetails~JobTypeID~JobType";
        		    String Values=""+objFlt.getID()+"~"+objFlt.getFaultType()+"~"+objFlt.getFaultDetails()+"~"+objFlt.getJobTypeID()+"~"+objFlt.getJobType();
        		    String TableName="FaultType";
        		    
        		    request.addProperty("_strCoumnNames",CoumnNames);
        		    request.addProperty("_strValues",Values);
        		    request.addProperty("_strTableName",TableName);
        		    
        		    
        		    HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
        		    androidHttpTransport.call(SOAP_ACTION, envelope);
        		    Object resonse=envelope.getResponse();
        		    String output = resonse.toString();
        		
        		    
        		    
        		    
        			
    				
        		}
        		 catch(Exception e){
        			 Log.d("Error=", ""+e.getMessage()); 
        		 }
        	}
        	
        	
        	
            return null;
        }
        @Override
        protected void onPostExecute(Void result) {
        	
        	try{
        	FMDBDatabaseAccess db=new FMDBDatabaseAccess(ImageMapActivity.this);
        	if(objFlt!=null){
        		db.updateFaultTypeSynced(objFlt.getID());
        	}
        	if(!isAlldone){
        		SyncFaulttype task=new SyncFaulttype();
        		task.execute(10);
        	}
        	}
    		catch(Exception e)
    		{
    			Log.d("Exception",""+e.getMessage());
    		}
            
        }
         
    }
    public void uploadImage(String url)
    {
    	try{
    	if(url!=null && url.length()!=0 && !url.equals(""))
    	{
    		String METHOD_NAME = "UploadFileDataParam";
    		String NAMESPACE = "http://tempuri.org/";
    		String URL = ""+getResources().getString(R.string.WS_URL).toString()+"SnagReporter_WebS.asmx";//SnagReporter_WCF.svc
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
		catch(Exception e)
		{
			Log.d("Exception",""+e.getMessage());
		}
    }
    public byte[] getBytesFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(CompressFormat.JPEG, 70, stream);
        return stream.toByteArray();
    }
    
}