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
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
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


public class Registration_page extends Activity
{
	//ProjectMaster arrPrj[];
	EditText fname,lname,email,mobno,project,pwd,cnfpwd;
	UUID uuid;
	//int SelectedProjectIndex=-1;
	//boolean isProjectSelected=false;
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.registration_page);
        
        //FMDBDatabaseAccess obj=new FMDBDatabaseAccess(getApplicationContext());
        //arrPrj=obj.getProjects();
        
        fname=(EditText)findViewById(R.id.reg_fname);
        lname=(EditText)findViewById(R.id.reg_lname);
        email=(EditText)findViewById(R.id.reg_email);
        mobno=(EditText)findViewById(R.id.reg_mobno);
        pwd=(EditText)findViewById(R.id.reg_pwd);
        cnfpwd=(EditText)findViewById(R.id.reg_cnfpwd);
       // project=(EditText)findViewById(R.id.reg_project);
        
        
//        if(arrPrj!=null && arrPrj.length>0){
//        	project.setText(arrPrj[0].getProjectName());
//        	SelectedProjectIndex=0;
//        }
        
        
        
        
    }
    @Override
	public void onConfigurationChanged(Configuration newConfig){
		super.onConfigurationChanged(newConfig);
	}
    public void CancelClick(View v){
    	try{
//    		Intent i=new Intent(Registration_page.this,com.snagreporter.Login_page.class);
//			i.putExtra("Email", email.getText().toString());
//			startActivity(i);
			finish();
    	}
    	catch(Exception e){
    		
    	}
    }
    
    public void SubmitClick(View v){
    	if(isTextFieldValid()){
    		
    		if(pwd.getText().toString().trim().equals(""+cnfpwd.getText().toString().trim())){
    			InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
    			imm.hideSoftInputFromWindow(fname.getWindowToken(), 0);
    			imm.hideSoftInputFromWindow(lname.getWindowToken(), 0);
    			imm.hideSoftInputFromWindow(email.getWindowToken(), 0);
    			imm.hideSoftInputFromWindow(mobno.getWindowToken(), 0);
    			imm.hideSoftInputFromWindow(pwd.getWindowToken(), 0);
    			imm.hideSoftInputFromWindow(cnfpwd.getWindowToken(), 0);
    			DownloadDataFromWeb task=new DownloadDataFromWeb();
    			task.execute(10);
    		}
    		else{
    			new AlertDialog.Builder(this)
        	    .setTitle("Password Mismatch")
        	    .setMessage("Password and confirm password do not match.")
        	    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
        	        public void onClick(DialogInterface dialog, int which) { 
        	            // continue with delete
        	        }
        	     })
        	    .show();
    		}
    		
    		
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
    
    protected class DownloadDataFromWeb extends AsyncTask<Integer , Integer, Void> {
    	ProgressDialog mProgressDialog = new ProgressDialog(Registration_page.this);
    	JSONObject jObject;
    	String output="";
        @Override
        protected void onPreExecute() {  
        	mProgressDialog.setCancelable(false);
        	mProgressDialog.setMessage("Loading...");
            mProgressDialog.show();
        	
        }      
        @Override
        protected Void doInBackground(Integer... params) {
        	
        	
        	
        	try
    		{
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
	    		    uuid=UUID.randomUUID();
	    		    
	    		    String CoumnNames="ID~FirstName~LastName~InspectorName~Email~MobileNo~Address~ImageName~Type~Password";
	    		    String Values=""+uuid.toString()+"~"+fname.getText().toString()+"~"+lname.getText().toString()+"~"+fname.getText().toString()+" "+lname.getText().toString()+"~"+email.getText().toString()+"~"+mobno.getText().toString()+"~~~"+"Inspector"+"~"+pwd.getText().toString();
	    		    String TableName="Inspector";
	    		    
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
    		catch (Exception e){
                Log.d("Error=", ""+e.getMessage()); 
    		}
            return null;
        }
        @Override
        protected void onPostExecute(Void result) {
        	mProgressDialog.dismiss();
        	
        	if(output!=null && !output.equals("")){
        		runOnUiThread(new Runnable() {
   			     public void run() {

   			//stuff that updates ui
   			    	 
   			    	 
   			    	 
   			    	 Registration obj=new Registration();
   			    		
   			    		
   			    		obj.setID(uuid.toString());
   			    		obj.setFirstName(fname.getText().toString());
   			    		obj.setLastName(lname.getText().toString());
   			    		obj.setEmail(email.getText().toString());
   			    		obj.setMobileNo(mobno.getText().toString());

   			    			obj.setDefaultProjectID("");
   			    			obj.setDefaultProjectname("");
   			    			obj.setCurrentLocationInProjectID("");
   			    			obj.setCurrentLocationInProjectName("");
   			    		
   			    		obj.setIsLoggedIn(true);
   			    		obj.setType("Inspector");
   			    		obj.setAddress("");
   			    		
   			    		FMDBDatabaseAccess db=new FMDBDatabaseAccess(Registration_page.this);
   			    		db.insertORUpdateRegistration(obj);
   			    		
   			    		SharedPreferences sharedPref = getSharedPreferences("AppDelegate",MODE_PRIVATE);
   			            SharedPreferences.Editor prefEditor = sharedPref.edit();
   			            prefEditor.putString("RegUserID",obj.getID());
   			         prefEditor.putString("LoginType",obj.getType());
   			            prefEditor.commit();
   			    		Intent i=new Intent(Registration_page.this,com.snagreporter.ProjectListPage.class);
   			    		i.putExtra("EnteredApplication", true);
   			    		startActivity(i);
   			    		
   			    		finish();
   			    	 
   			    	 

   			    }
   			});
	    	 }
        	else{
        		runOnUiThread(new Runnable() {
      			     public void run() {
        		new AlertDialog.Builder(Registration_page.this)
        	    .setTitle("Registration failed")
        	    .setMessage("Failed to register. Please retry.")
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
    
    public void project_Click(View v){
    	registerForContextMenu(v);
		
			openContextMenu(v);
			unregisterForContextMenu(v);
    }
    
    @Override  
	public void onCreateContextMenu(ContextMenu menu, View v,ContextMenuInfo menuInfo)
    {  
    	super.onCreateContextMenu(menu, v, menuInfo);  
    	menu.setHeaderTitle("Select Project");
//    	menu.add(0, v.getId(), 0, "");
//    	for(int i=0;i<arrPrj.length;i++){
//    		menu.add(0, v.getId(), 0, ""+arrPrj[i].getProjectName());
//    	}
    	
	
	    
	}  
    
    @Override
	public boolean onContextItemSelected(MenuItem item)
	{
    	String str=item.getTitle().toString();
    	//if(str!=null && str.length()>0){
    	//project.setText(item.getTitle().toString());
//    	isProjectSelected=true;
//    	for(int i=0;i<arrPrj.length;i++){
//    		if(item.getTitle().toString().equalsIgnoreCase(arrPrj[i].getProjectName().toString())){
//    			SelectedProjectIndex=i;
//    			break;
//    		}
//    	}
//    	}
//    	else{
//    		project.setText(str);
//    		SelectedProjectIndex=-1;
//    		isProjectSelected=false;
//    	}
	
	 return true;
	}
    
    public boolean isTextFieldValid(){
    	String str=fname.getText().toString().trim();
    	String str2=lname.getText().toString().trim();
    	String str3=email.getText().toString().trim();
    	String str4=pwd.getText().toString().trim();
    	String str5=cnfpwd.getText().toString().trim();
    	boolean isValid;
    	if((str!=null && str.length()!=0) && (str2!=null && str2.length()!=0) && (str3!=null && str3.length()!=0) && (str4!=null && str4.length()!=0) && (str5!=null && str5.length()!=0)){
    		isValid=true;
    	}
    	else{
    		isValid=false;
    	}
    	
    	
    	return isValid;
    }
   
}