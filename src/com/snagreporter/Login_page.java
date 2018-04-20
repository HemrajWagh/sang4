package com.snagreporter;

import java.io.File;
import java.sql.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.UUID;

import org.json.JSONArray;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;


import com.snagreporter.R;
import com.snagreporter.listitems.Header;
import com.snagreporter.listitems.Item;
import com.snagreporter.listitems.ListItem;
import com.snagreporter.listitems.ListItemAddDefect;
import com.snagreporter.listitems.ListItemAddDefectPhoto;
import com.snagreporter.listitems.ListItemBlank;
import com.snagreporter.listitems.ListItemParent;


import android.R.array;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.RemoteViews.ActionException;
import android.widget.TextView;
import com.snagreporter.database.*;
import com.snagreporter.entity.ApartmentMaster;
import com.snagreporter.entity.BuildingMaster;
import com.snagreporter.entity.FloorMaster;
import com.snagreporter.entity.Inspector;
import com.snagreporter.entity.JobType;
import com.snagreporter.entity.ProjectMaster;
import com.snagreporter.entity.Registration;
import com.snagreporter.entity.SnagMaster;
import com.snagreporter.entity.StdApartmentAreas;
import com.snagreporter.entity.StdFloorAreas;

import android.app.DatePickerDialog;


public class Login_page extends Activity
{
	//ProjectMaster arrPrj[];
	EditText email,pwd;
	String resultData;
	//int SelectedProjectIndex=-1;
	//boolean isProjectSelected=false;
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_page);
        
        //String str=getIntent().getExtras().getString("Email");
        
       
        
        
        email=(EditText)findViewById(R.id.reg_email);
        pwd=(EditText)findViewById(R.id.reg_pwd);
        
        
        
        
        
    }
    @Override
	public void onConfigurationChanged(Configuration newConfig){
		super.onConfigurationChanged(newConfig);
	}
    public void CancelClick(View v){
    	try{
//    		Intent i = new Intent(Intent.ACTION_SEND);
//    		i.setType("message/rfc822");
//    		i.putExtra(Intent.EXTRA_EMAIL  , new String[]{"jaldip.katre@gmail.com"});
//    		i.putExtra(Intent.EXTRA_SUBJECT, "subject of email");
//    		i.putExtra(Intent.EXTRA_TEXT   , "body of email");
//    		try {
//    		    startActivity(Intent.createChooser(i, "Send mail..."));
//    		} catch (android.content.ActivityNotFoundException ex) {
//    		    Toast.makeText(Login_page.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
//    		}
    		//Intent i=new Intent(Login_page.this,com.snagreporter.Registration_page.class);
			//i.putExtra("Email", email.getText().toString());
			//startActivity(i);
			finish();
    	}
    	catch(Exception e){
    		
    	}
    }
    
    public void SubmitClick(View v){
    	if(isTextFieldValid()){
//    		Registration obj=new Registration();
//    		UUID id=UUID.randomUUID();
//    		
//    		obj.setID(id.toString());
//    		
//    		obj.setEmail(email.getText().toString());
//    		obj.setMobileNo(pwd.getText().toString());
//
//    			obj.setDefaultProjectID("");
//    			obj.setDefaultProjectname("");
//    			obj.setCurrentLocationInProjectID("");
//    			obj.setCurrentLocationInProjectName("");
//
//    		obj.setIsLoggedIn(true);
//    		obj.setType("Inspector");
//    		obj.setAddress("");
//    		
//    		FMDBDatabaseAccess db=new FMDBDatabaseAccess(getApplicationContext());
//    		db.insertRegistration(obj);
//    		
//    		SharedPreferences sharedPref = getSharedPreferences("AppDelegate",MODE_PRIVATE);
//            SharedPreferences.Editor prefEditor = sharedPref.edit();
//            prefEditor.putString("RegUserID",obj.getID());
//            
//            prefEditor.commit();
//    		
//
//    			Intent i=new Intent(Login_page.this,com.snagreporter.ProjectListPage.class);
//    			startActivity(i);
//    		
//    		finish();
    		InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(email.getWindowToken(), 0);
			imm.hideSoftInputFromWindow(pwd.getWindowToken(), 0);
    		
    		AuthenticateLogin task=new AuthenticateLogin();
    		task.execute(10);
    		
    	}
    	else{
    		new AlertDialog.Builder(this)
    	    .setTitle("Empty Field")
    	    .setMessage("Fields can not be left blank.")
    	    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
    	        public void onClick(DialogInterface dialog, int which) { 
    	            // continue with delete
    	        }
    	     })
    	    .show();
    	}
    	
    }
    protected class AuthenticateLogin extends AsyncTask<Integer , Integer, Void> 
    {
    	ProgressDialog mProgressDialog = new ProgressDialog(Login_page.this);
    	//JSONObject jObject;
        @Override
        protected void onPreExecute() 
        {  
        	mProgressDialog.setCancelable(false);
        	mProgressDialog.setMessage("Loading...");
            mProgressDialog.show();
        	
        }   
        @Override
        protected Void doInBackground(Integer... params)
        {
        	String METHOD_NAME = "VerifyIfEmailIDExist";
        	String NAMESPACE = "http://tempuri.org/";
        	String URL = ""+getResources().getString(R.string.WS_URL).toString()+"SnagReporter_WebS.asmx";
        	String SOAP_ACTION = "http://tempuri.org/VerifyIfEmailIDExist";
        	try
        	{
        		SoapObject request=new SoapObject(NAMESPACE,METHOD_NAME);
        		SoapSerializationEnvelope envelope=new SoapSerializationEnvelope(SoapEnvelope.VER11);
        		envelope.dotNet=true;
        		envelope.bodyOut=request;
        		envelope.setOutputSoapObject(request);
   		 
        		request.addProperty("_strEmailID", email.getText().toString());
        		request.addProperty("_strPassword",pwd.getText().toString());
   		 
        		HttpTransportSE htttptransport=new HttpTransportSE(URL);
        		htttptransport.call(SOAP_ACTION, envelope);
        		Object response=envelope.getResponse();
        		resultData=response.toString();
        	}
        	catch (Exception e)
        	{
        		Log.d("Exceptopn AuthenticateLogin", ""+e.getMessage());
        	}
        	return null;
        }
        @Override
        protected void onPostExecute(Void result) 
        {
        	mProgressDialog.dismiss();
        	 if(resultData!=null)
    		 {
        		 if(resultData.equalsIgnoreCase("false") || resultData.equalsIgnoreCase("{\"Data\" : []}"))
        		 {
        			 Log.d("Authentication Failed","");
        			 runOnUiThread(new Runnable() {
          			     public void run() {
            		new AlertDialog.Builder(Login_page.this)
            	    .setTitle("Invalid Login")
            	    .setMessage("Invalid Email or Password.")
            	    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
            	        public void onClick(DialogInterface dialog, int which) { 
            	            // continue with delete
            	        }
            	     })
            	    .show();
          			     }
            		});
        		 }
        		 
        		 else
        		 {
        			 try
        			 {
        				 JSONObject jsObj;
        				 jsObj=new JSONObject(resultData);
        				 JSONArray jsArr=jsObj.getJSONArray("Data");
        				 if(jsArr!=null)
        				 {
        					 String ID="";
        					 String LoginType="";
        					 String BlrID="";
        					 for(int i=0;i<jsArr.length();i++)
        					 {
        						
        						 /*JSONObject obj = jsArr.getJSONObject(i);
        						 Registration objReg=new Registration();
        						 objReg.setID(obj.getString("ID"));
        						 objReg.setFirstName(obj.getString("FirstName"));
        						 objReg.setLastName(obj.getString("LastName"));
        						 objReg.setEmail(obj.getString("Email"));
        						 objReg.setMobileNo(obj.getString("MobileNo"));
        						 objReg.setAddress(obj.getString("Address"));
        						 objReg.setImageName(obj.getString("ImageName"));
        						 objReg.setType(obj.getString("Type"));
        						 
        						 objReg.setDefaultProjectID(obj.getString("DefaultProjectID"));
        						 objReg.setDefaultProjectname(obj.getString("DefaultProjectName"));
        						 objReg.setCurrentLocationInProjectID(obj.getString("CurrecntLocationInProjectID"));
        						 objReg.setCurrentLocationInProjectName(obj.getString("CurrentLocationInProjectName"));
        						 objReg.setIsLoggedIn(true);
        						 objReg.setBuilderID(obj.getString("BuilderID"));
        						 objReg.setBuilderName(obj.getString("BuilderName"));*/
        						 
        						 JSONObject obj = jsArr.getJSONObject(i);
        						 Registration objReg=new Registration();
        						 objReg.setID(obj.getString("ID"));
        						 objReg.setFirstName(obj.getString("FirstName"));
        						 objReg.setLastName(obj.getString("LastName"));
        						 objReg.setEmail(obj.getString("EmailID"));
        						 objReg.setMobileNo("");
        						 objReg.setAddress("");
        						 objReg.setImageName("");
        						 objReg.setType(obj.getString("Designation"));
        						 
        						 objReg.setDefaultProjectID("PRJ-000001");
        						 objReg.setDefaultProjectname("Project-01");
        						 objReg.setCurrentLocationInProjectID("");
        						 objReg.setCurrentLocationInProjectName("");
        						 objReg.setIsLoggedIn(true);
        						 objReg.setBuilderID("BLR-000001");
        						 objReg.setBuilderName("LODHA GROUP");
        						 
        						 
        						 FMDBDatabaseAccess fmdb=new FMDBDatabaseAccess(Login_page.this);
        						 fmdb.insertORUpdateRegistration(objReg);
        						 ID=objReg.getID();
        						 BlrID=objReg.getBuilderID();
        						 LoginType=objReg.getType();
        					 }
        					 	SharedPreferences sharedPref = getSharedPreferences("AppDelegate",MODE_PRIVATE);
        			            SharedPreferences.Editor prefEditor = sharedPref.edit();
        			            prefEditor.putString("RegUserID",ID);
        			            prefEditor.putString("LoginType",LoginType);
        			            prefEditor.commit();
        			           
        			            if(LoginType.equalsIgnoreCase("Contractor"))
        			            {
        			            	//i=new Intent(Login_page.this,com.snagreporter.BuildingListPage.class);
        			            	//i.putExtra("RegObj",)
        			            	FMDBDatabaseAccess fmdb=new FMDBDatabaseAccess(Login_page.this);
        			            	String[] conID=fmdb.getProjectIDByContractor(ID);
        			            	if(conID!=null)
        			            	{
        			            		Intent conI;
//        			            		if(conID.length==1)
//        			            		{
//        			            			String strId=conID[0];
//        			            			ProjectMaster objProj=fmdb.getProjectByID(strId);
//        			            			conI=new Intent(Login_page.this,com.snagreporter.BuildingListPage.class);
//        			            			conI.putExtra("Project",objProj);
//        			            			
//        			            			
//        			            		}
//        			            		else
        			            		{
        			            			conI=new Intent(Login_page.this,com.snagreporter.ProjectListPage.class);
        			            			conI.putExtra("ContractorID",conID);
        			            		}
        			            		startActivity(conI);
        			            	}
        			            	
        			            	

        			            }
        			            else
        			            {
        			            	Intent i;
        			            	i=new Intent(Login_page.this,com.snagreporter.ProjectListPage.class);
            			    		i.putExtra("EnteredApplication", true);
            			    		i.putExtra("BuilderID", BlrID);
            			    		startActivity(i);  
        			            }
        			                 			    		
        			    		finish();
        					 
        				 }
        			 }
        			 catch (Exception e)
        			 {
        				 Log.d("AuthenticateLogin", ""+e.getMessage());
        				 runOnUiThread(new Runnable() {
              			     public void run() {
                		new AlertDialog.Builder(Login_page.this)
                	    .setTitle("Error")
                	    .setMessage("Login Failed.")
                	    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                	        public void onClick(DialogInterface dialog, int which) { 
                	            // continue with delete
                	        }
                	     })
                	    .show();
              			     }
                		});
        			 }
        		 } 
    			 
    		 }
        	 else{
        		 runOnUiThread(new Runnable() {
      			     public void run() {
        		new AlertDialog.Builder(Login_page.this)
        	    .setTitle("Login Failed")
        	    .setMessage("Please Retry.")
        	    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
        	        public void onClick(DialogInterface dialog, int which) { 
        	            // continue with delete
        	        }
        	     })
        	    .show();
      			     }
        		});
        	 }
        }	
    }
   
    
    public boolean isTextFieldValid(){
    	
    	String str2=email.getText().toString().trim();
    	String str3=pwd.getText().toString().trim();
    	boolean isValid;
    	if((str2!=null && str2.length()!=0) && (str3!=null && str3.length()!=0)){
    		isValid=true;
    	}
    	else{
    		isValid=false;
    	}
    	
    	
    	return isValid;
    }
   public void SignupClick(View v){
	   Intent i=new Intent(Login_page.this,com.snagreporter.Registration_page.class);
		
		startActivity(i);
   }
   
   @Override
	public boolean dispatchTouchEvent(MotionEvent event) {
	    View view = getCurrentFocus();
	    boolean ret = super.dispatchTouchEvent(event);

	    if (view instanceof EditText) {
	        View w = getCurrentFocus();
	        int scrcoords[] = new int[2];
	        w.getLocationOnScreen(scrcoords);
	        float x = event.getRawX() + w.getLeft() - scrcoords[0];
	        float y = event.getRawY() + w.getTop() - scrcoords[1];
	        
	        if (event.getAction() == MotionEvent.ACTION_UP 
	 && (x < w.getLeft() || x >= w.getRight() 
	 || y < w.getTop() || y > w.getBottom()) ) { 
	            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
	            imm.hideSoftInputFromWindow(getWindow().getCurrentFocus().getWindowToken(), 0);
	        }
	    }
	 return ret;
	}
}