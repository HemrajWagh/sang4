package com.snagreporter.menuhandler;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import org.json.JSONArray;
import org.json.JSONObject;
import org.kobjects.base64.Base64;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.MarshalBase64;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import com.snagreporter.Login_page;
import com.snagreporter.ProjectListPage;
import com.snagreporter.R;
import com.snagreporter.database.FMDBDatabaseAccess;
import com.snagreporter.entity.ApartmentMaster;
import com.snagreporter.entity.AttachmentDetails;
import com.snagreporter.entity.BuildingMaster;
import com.snagreporter.entity.FloorMaster;
import com.snagreporter.entity.Registration;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.CompressFormat;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class MenuHandler extends Activity {
	Context context;
	String FilePath="";
	String NAMESPACE = "http://tempuri.org/";
	String METHOD_NAME_SaveNewDataToTheDataBase = "SaveNewDataToTheDataBase";
	String SOAP_ACTION_SaveNewDataToTheDataBase = "http://tempuri.org/SaveNewDataToTheDataBase";
	String URL ="";
	final int REQUEST_FILE=101;
	String RegUserID="";
	String LoginType="";
	boolean isOnline=false;
	AttachmentDetails AtmtObj;
	public  MenuHandler(Context con){
		context=con;
		SharedPreferences sharedPref = context.getSharedPreferences("AppDelegate",MODE_PRIVATE);
        Boolean id=sharedPref.getBoolean("isOnline", false);
        isOnline=id;
        RegUserID=sharedPref.getString("RegUserID", "");
        LoginType=sharedPref.getString("LoginType", "");
        URL=""+context.getResources().getString(R.string.WS_URL).toString()+"SnagReporter_WebS.asmx";
	}
	
	
	public void MenuRoomsheetClick(FloorMaster obj){
		
		String str=obj.getFloorPlanImage();
		if(str!=null && str.length()>0){
		Intent i=new Intent(context,com.snagreporter.SnagImageMapping.class);
		i.putExtra("Floor", obj);
		
		i.putExtra("ImageName", obj.getFloorPlanImage());
		context.startActivity(i);
		}
		else{
			Toast.makeText(context, "No Floor Plan Image Found!!!", Toast.LENGTH_LONG).show();
		}
	}
	public void MenuReportsClick(){
		Intent i;
		if(LoginType.equalsIgnoreCase("Contractor"))
		{
			
			i=new Intent(context,com.snagreporter.ReportTable.class);
			i.putExtra("fromContractor",true);
		}
		else
		{
			i=new Intent(context,com.snagreporter.SelectReportPage.class);
		}
		((Activity)context).startActivityForResult(i,10001);
	}
	public void MenuSortFilterClick(){
		
	}
	public void MenuAttendanceClick(){
		if(LoginType.equalsIgnoreCase("Inspector")){
			Intent i=new Intent(context,com.snagreporter.Attendance.class);
			context.startActivity(i);
		}
	}
	public void MenuLegendsClick()
	{
		try
		{
			Intent itt=new Intent(context, com.snagreporter.IconDesc.class);
			context.startActivity(itt);
		}
		catch(Exception e)
		{
			Log.d("Exception",""+e.getMessage());
		}
	}
	public void MenuAboutusClick()
	{
		try
		{
			Intent itt=new Intent(context, com.snagreporter.AboutProject.class);
			context.startActivity(itt);
		}
		catch(Exception e)
		{
			Log.d("Exception",""+e.getMessage());
		}
	}
	public void MenuChatClick(){
		Intent i=new Intent(context,com.snagreporter.ListItemNames.class);
		//context.startActivity(i);
		((Activity)context).startActivityForResult(i,10001);
	}
	public void MenuGraphClick(String str){
		Intent i=new Intent(context,com.snagreporter.BarChartActivity.class);
		i.putExtra(""+str, true);
		context.startActivity(i);
	}
	public void MenuGraphClick(String str,ApartmentMaster apt){
		Intent i=new Intent(context,com.snagreporter.BarChartActivity.class);
		i.putExtra(""+str, true);
		
		i.putExtra("CurrentAPT", apt);
		context.startActivity(i);
	}
	public void MenuGraphClick(String str,FloorMaster flr){
		Intent i=new Intent(context,com.snagreporter.BarChartActivity.class);
		i.putExtra(""+str, true);
		
		i.putExtra("currentfloor", flr);
		context.startActivity(i);
	}
	public void MenuGraphClick(String str,BuildingMaster bldg){
		Intent i=new Intent(context,com.snagreporter.BarChartActivity.class);
		i.putExtra(""+str, true);
		
		i.putExtra("currentBuilding", bldg);
		context.startActivity(i);
	}
	public void MenuGraphClick(String str,String str2){
		Intent i=new Intent(context,com.snagreporter.BarChartActivity.class);
		i.putExtra(""+str, true);
		
		i.putExtra("currentprojectID", str2);
		context.startActivity(i);
	}
	public void MenuChartClick(){
		Intent i=new Intent(context,com.snagreporter.GraphWebView.class);
		context.startActivity(i);
	}
	public void MenuAttachClick(){
		Intent i=new Intent(context,com.snagreporter.fileexplorer.FileExplorerActivity.class);
		((Activity)context).startActivityForResult(i, REQUEST_FILE);
	}
//	@Override
//	 protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) 
//	    {
//	    	 super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
//	    	 switch(requestCode){
//	    	 	case REQUEST_FILE:
//	    	 			if(imageReturnedIntent!=null && resultCode==REQUEST_FILE){
//	    	 				FilePath=imageReturnedIntent.getExtras().getString("FilePath");
//	    	 				Toast.makeText(context, "File111="+FilePath, Toast.LENGTH_LONG).show();
//	    	 			}
//	    	 			break;
//	    	 }
//	    }
	public void SaveAttachment(String ProjectID,String BuildingID,String FloorID,String AptID,String AptAreaID,String SnagID,String AreaType,String JobType,String FileName,String FilePath){
		try{
			SharedPreferences SP = context.getSharedPreferences("AppDelegate",MODE_PRIVATE);
	        RegUserID=SP.getString("RegUserID", "");
			AttachmentDetails obj=new AttachmentDetails();
			UUID id=UUID.randomUUID();
			obj.setAttachmentID(id.toString());
			obj.setProjectID(ProjectID);
			obj.setBuildingID(BuildingID);
			obj.setFloorID(FloorID);
			obj.setApartmentID(AptID);
			obj.setAptAreaID(AptAreaID);
			obj.setSnagID(SnagID);
			obj.setAreaType(AreaType);
			obj.setJobType(JobType);
			obj.setFileName(FileName);
			obj.setLocalFilePath(FilePath);
			obj.setCreatedBy(RegUserID);
			Calendar c = Calendar.getInstance();
	        SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss aaa");
			String date=df.format(c.getTime());
			obj.setCreatedDate(date);
			obj.setModifiedBy(RegUserID);
			obj.setModifiedDate(date);
			
			AtmtObj=obj;
			SyncDataAndUploadFile task=new SyncDataAndUploadFile();
			task.execute(10);
		}
		catch(Exception e){
			
		}
	}
	 protected class SyncDataAndUploadFile extends AsyncTask<Integer , Integer, Void> 
	    {
		 	String FileUploadResult="";
		 	String Result="";
	    	ProgressDialog mProgressDialog = new ProgressDialog(context);
	    	//JSONObject jObject;
	        @Override
	        protected void onPreExecute() 
	        {  
	        	if(!mProgressDialog.isShowing() && isOnline){
	        		mProgressDialog.setCancelable(false);
	        		mProgressDialog.setMessage("Uploading File...");
	        		mProgressDialog.show();
	        	}
	        	
	        }   
	        @Override
	        protected Void doInBackground(Integer... params)
	        {
	        	if(isOnline){
	        		try{
	        			FileUploadResult=uploadFile(AtmtObj.getFileName(), AtmtObj.getLocalFilePath());
	        			
	        			if(FileUploadResult.equalsIgnoreCase("true"))
	    	        		AtmtObj.setIsUploadedToWeb(true);
	    	        	else
	    	        		AtmtObj.setIsUploadedToWeb(false);
	        			
	        		SoapObject request=new SoapObject(NAMESPACE, METHOD_NAME_SaveNewDataToTheDataBase);
					SoapSerializationEnvelope envelope=new SoapSerializationEnvelope(SoapEnvelope.VER11);
					envelope.dotNet=true;
					envelope.setOutputSoapObject(request);
					
					  String CoumnNames="AttachmentID~ProjectID~BuildingID~FloorID~ApartmentID~AptAreaID~SnagID~AreaType~JobType~FileName~CreatedBy~IsUploadedToWeb";
					  String Values=""+AtmtObj.getAttachmentID()+"~"+AtmtObj.getProjectID()+"~"+AtmtObj.getBuildingID()+"~"+AtmtObj.getFloorID()+"~"+AtmtObj.getApartmentID()+"~"+AtmtObj.getAptAreaID()+"~"+AtmtObj.getSnagID()+"~"+AtmtObj.getAreaType()+"~"+AtmtObj.getJobType()+"~"+AtmtObj.getFileName()+"~"+AtmtObj.getCreatedBy()+"~"+AtmtObj.getIsUploadedToWeb();
					  String TableName="AttachmentDetails";
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
    				        	Result="true";
    				        }
    				        else{
    				        	Result="false";
    				        }
    	    		    	}
    	    		    	catch(Exception e){
    	    		    		Log.d("Error parsing result", "");
    	    		    	}
					}
					
	        		}
	        		catch(Exception e){
	        			Log.d("Error:", ""+e.getMessage());
	        		}
	        	}
	        	
	        	return null;
	        }
	        @Override
	        protected void onPostExecute(Void result) 
	        {
	        	mProgressDialog.dismiss();
//	        	if(FileUploadResult.equalsIgnoreCase("true"))
//	        		AtmtObj.setIsUploadedToWeb(true);
//	        	else
//	        		AtmtObj.setIsUploadedToWeb(false);
	        	
	        	if(Result.equalsIgnoreCase("true"))
	        		AtmtObj.setIsSyncToWeb(true);
	        	else
	        		AtmtObj.setIsSyncToWeb(false);
	        	FMDBDatabaseAccess db=new FMDBDatabaseAccess(context);
	        	db.insertORUpdateAttachmentDetails(AtmtObj);
	        }	
	    }
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
}
