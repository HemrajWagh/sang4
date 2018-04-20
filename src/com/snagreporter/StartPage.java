package com.snagreporter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.json.JSONArray;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import com.snagreporter.database.*;
import com.snagreporter.entity.*;
import com.snagreporter.R;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.database.SQLException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.TextView;

public class StartPage extends Activity {
	String extStorageDirectory;
	private static final int projectReqst=1000;
	private static final String namespace = "http://tempuri.org/";
	  String url ;//="http://Snag2.itakeon.com/SnagReporter_WebS.asmx?WSDL";
	private static final String getchat = "GetMyAllChatMessage";
	private static final String getchat_action = "http://tempuri.org/GetMyAllChatMessage";
	ProgressDialog mProgress;
	JSONObject jObject;
	Handler handler=new Handler();
	String RegUserID="";
	Chat object=new Chat();
	boolean isInChat=false;
	TextView txtPW;
	@Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.start_page);
        //mProgress=new ProgressDialog(StartPage.this);
        //mProgress.setMessage("Loading Please wait...");
        //mProgress.setCancelable(false);
        url=getResources().getString(R.string.WS_URL)+"SnagReporter_WebS.asmx";
        txtPW=(TextView)findViewById(R.id.txt_plzwait);
        DataBaseHelper myDbHelper = new DataBaseHelper(this);
        myDbHelper = new DataBaseHelper(this);
 
        try 
        {
        	boolean dbexists1=myDbHelper.CheckIfDBExists();
        	if(!dbexists1){
        		txtPW.setText("Please Wait...");
        		addShortcut();
        	}
        	boolean dbexists=myDbHelper.createDataBase();
        	
        		
        } 
        catch (IOException ioe) 
        {
 
 		//throw new Error("Unable to create database");
        	Log.d("Error Main_Start_Page onCreate IOException=", ""+ioe.getMessage());
        	
        }
 
        try 
        {
 
 		myDbHelper.openDataBase();
 		myDbHelper.close();
 
        }
        catch(SQLException sqle){
 
 		//throw sqle;
        	Log.d("Error Main_Start_Page onCreate SQLException=", ""+sqle.getMessage());
 
        }
        
        
        
        
        
        
        checkAndCreateDirectory();
        ContinurInBg task=new ContinurInBg();
        task.execute(10);
        
        //addShortcut();
        
      
        
    }
	
	public void CopyDatabaseAsBackup(){
		try {
			try{
        	DataBaseHelper2 myDbHelper2 = new DataBaseHelper2(StartPage.this);
            myDbHelper2 = new DataBaseHelper2(StartPage.this);
     
            try 
            {
     
            	myDbHelper2.createDataBase();
     
            } 
            catch (IOException ioe) 
            {
     
     		//throw new Error("Unable to create database");
            	Log.d("Error Main_Start_Page onCreate IOException=", ""+ioe.getMessage());
            	
            }
     
            try 
            {
     
            	myDbHelper2.openDataBase();
            	myDbHelper2.close();
     
            }
            catch(SQLException sqle){
     
     		//throw sqle;
            	Log.d("Error Main_Start_Page onCreate SQLException=", ""+sqle.getMessage());
     
            }
        }
        catch(Exception e){
        	
        }
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	public void ContinueProcess()
	{
		
		  copyAssets();
	        CopyDatabaseAsBackup();
	        
	        
	       
	        
	}
	public void completeProcess()
	{
		
		 SharedPreferences sharedPref1 = getSharedPreferences("AppDelegate",MODE_PRIVATE);
	        SharedPreferences.Editor prefEditor1 = sharedPref1.edit();
	        prefEditor1.putString("WS_URL","http://120.63.128.108:4442/");
	        
	        prefEditor1.commit();
	        FMDBDatabaseAccess db=new FMDBDatabaseAccess(StartPage.this);
	        
	        Registration obj=db.getRegistration();
	        if(obj!=null){
	        	
	        	SharedPreferences sharedPref = getSharedPreferences("AppDelegate",MODE_PRIVATE);
	            SharedPreferences.Editor prefEditor = sharedPref.edit();
	            prefEditor.putString("RegUserID",obj.getID());
	            RegUserID=obj.getID();
	            prefEditor.putString("LoginType",obj.getType());
	            prefEditor.putBoolean("isInChat",isInChat);
	            
	            
	            prefEditor.commit();
	            
	            Intent i;
	            if(obj.getType().equalsIgnoreCase("Contractor"))
	            {
	         	   FMDBDatabaseAccess fmdb=new FMDBDatabaseAccess(StartPage.this);
	            	  String[] conID=fmdb.getProjectIDByContractor(obj.getID());
	            	  //if(conID!=null)
	            	 // {
//	            		 if(conID.length==1)
//	            		 {
//	            			String strId=conID[0];
//	            			ProjectMaster objProj=fmdb.getProjectByID(strId);
//	            			i=new Intent(StartPage.this,com.snagreporter.BuildingListPage.class);
//	            			i.putExtra("Project",objProj);
//	            			i.putExtra("isContractor", true);
//	            			i.putExtra("CameFromStartPage", true);
//	            		 }
//	            		 else
//	            		 {
	            			i=new Intent(StartPage.this,com.snagreporter.ProjectListPage.class);
	            			i.putExtra("BuilderID", obj.getBuilderID());
	            			i.putExtra("BuilderName", obj.getBuilderName());
	            			i.putExtra("EnteredApplication", true);
	            			i.putExtra("ContractorID",conID);
	            		// }
	            		
	            	  // }
	            	   
	            	  
	         	   
	            }
	            else
	            {
	         	if(obj.getCurrentLocationInProjectName().equalsIgnoreCase("Project"))
	         	{
	         		ProjectMaster prj=db.getDefaultProject(obj.getCurrentLocationInProjectID().toString());
	         		i=new Intent(this,com.snagreporter.BuildingListPage.class);
	         		i.putExtra("CameFromStartPage", true);
	         		i.putExtra("Project",prj);
	         		i.putExtra("GOTO", "BUILDING");
	         	}
	         	else if(obj.getCurrentLocationInProjectName().equalsIgnoreCase("Building"))
	         	{
	         		i=new Intent(this,com.snagreporter.FloorListPage.class);
	         		i.putExtra("CameFromStartPage", true);
	         		String[] str=obj.getCurrentLocationInProjectID().split("~");
	         		BuildingMaster bldg=db.getBuildingWithID(str[0],str[1]);
	         		ProjectMaster prj=db.getDefaultProject(str[0]);
	         		i.putExtra("Building", bldg);
	 				i.putExtra("Project", prj);
	         	}
	         	else if(obj.getCurrentLocationInProjectName().equalsIgnoreCase("Floor"))
	         	{
	         		i=new Intent(this,com.snagreporter.ApartmentListPage.class);
	         		i.putExtra("CameFromStartPage", true);
	         		String[] str=obj.getCurrentLocationInProjectID().split("~");
	         		FloorMaster flr=db.getFloorWithID(str[0],str[1],str[2]);
	         		
	         		BuildingMaster bldg=db.getBuildingWithID(str[0],str[1]);
	         		ProjectMaster prj=db.getDefaultProject(str[0]);
	         		i.putExtra("Floor", flr);
	 				i.putExtra("Project", prj);
	 				i.putExtra("Building", bldg);
	         	}
	         	else
	         	{
	         		i=new Intent(this,com.snagreporter.ProjectListPage.class);
	         		i.putExtra("EnteredApplication", true);
	         		i.putExtra("BuilderID", obj.getBuilderID());
	         		i.putExtra("BuilderName", obj.getBuilderName());
	         	}
	         	
	         	
	            
	           }
	        	
	        	startActivityForResult(i, projectReqst);
	           // startActivity(i);
	           // finish(); //@
	        }
	        else{
	        	Intent i=new Intent(this,com.snagreporter.Login_page.class);
	        
	            startActivity(i);
	            finish();
	           //finish(); //@
	        }
	        
	        //PopulateChat getchat=new PopulateChat();
	        //getchat.execute(10);
	        //finish();
	        
	}
	public void addShortcut()
	{
		 Intent shortcutIntent = new Intent(getApplicationContext(),StartPage.class);
		 shortcutIntent.setAction(Intent.ACTION_MAIN);
		 Intent addIntent = new Intent();
		 addIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
		 addIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, R.string.app_name);
		 addIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE,Intent.ShortcutIconResource.fromContext(getApplicationContext(),R.drawable.snagreporter));
		 addIntent.putExtra("duplicate", false);
		 
		 addIntent.setAction("com.android.launcher.action.INSTALL_SHORTCUT");
		 
		 getApplicationContext().sendBroadcast(addIntent);
		 
	}  
	public void checkAndCreateDirectory(){
		try{
		extStorageDirectory = Environment.getExternalStorageDirectory().toString();
       	 File fooo = new File(extStorageDirectory+"/SnagReporter/Pictures");
       	 if(fooo.exists() && fooo.isDirectory()){
       	
       		 //extStorageDirectory=extStorageDirectory+"/DingLing/Pictures";
       		 Log.d("", "/SnagReporter/Pictures Directory Exists");
       	 }
       	 else{
       		 boolean success = fooo.mkdirs();
       		 
       		 if(success)
       			Log.d("", "/SnagReporter/Pictures Directory Created");
       		 else{
       			Log.d("", "/SnagReporter/Pictures Directory Not Created");
       			//Toast.makeText(getApplicationContext(), "1", Toast.LENGTH_LONG).show();
       		 }
       		
       			// extStorageDirectory=extStorageDirectory+"/DingLing/Pictures";
       	 }
       	 
       	
//      	String state = Environment.getExternalStorageState();
//      	String res;
//      	if (Environment.MEDIA_MOUNTED.equals(state)) {
//      	    Log.d("Test", "sdcard mounted and writable");
//      	    res="sdcard mounted and writable";
//      	}
//      	else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
//      	    Log.d("Test", "sdcard mounted readonly");
//      	    res="sdcard mounted readonly";
//      	}
//      	else {
//      		res=""+state;
//      	    Log.d("Test", "sdcard state: " + state);
//      	}
//      	boolean can_write = fooo.canWrite();
      	//Toast.makeText(getApplicationContext(), "can_write="+can_write+ "  State="+state, Toast.LENGTH_LONG).show();
		}
		catch(Exception e){
			
		}
	}
	
	private void copyAssets() {
	    AssetManager assetManager = getAssets();
	    String[] files = null;
	    try {
	        files = assetManager.list("");
	    } catch (IOException e) {
	        Log.e("tag", e.getMessage());
	    }
	    for(String filename : files) {
	        InputStream in = null;
	        OutputStream out = null;
	        try {
	        	if(!filename.equalsIgnoreCase("SnagReporter.db") && !filename.equalsIgnoreCase("SnagReporter1.db") && !filename.equalsIgnoreCase("LHANDW.TTF")&& !filename.equalsIgnoreCase("PRISTINA.TTF")){
	        		File f=new File(Environment.getExternalStorageDirectory()+"/SnagReporter/Pictures/" + filename);
	  	          if(!f.exists()){
	        		in = assetManager.open(filename);
	          
	        		out = new FileOutputStream(Environment.getExternalStorageDirectory()+"/SnagReporter/Pictures/" + filename);
	          
	        		copyFile(in, out);
	        		in.close();
	        		in = null;
	        		out.flush();
	        		out.close();
	        		out = null;
	  	          }
	  	          f=null;
	        	}
	        } catch(Exception e) {
	            Log.e("tag", ""+e.getMessage());
	        }       
	    }
	}
	private void copyFile(InputStream in, OutputStream out) throws IOException {
	    byte[] buffer = new byte[1024];
	    int read;
	    while((read = in.read(buffer)) != -1){
	      out.write(buffer, 0, read);
	    }
	}
	protected class ContinurInBg extends AsyncTask<Integer , Integer, Void> 
	{
		@Override
		protected void onPreExecute()
		{
			//mProgress.show();
			super.onPreExecute();
		}
		@Override
		protected Void doInBackground(Integer... params) {
			ContinueProcess();
			return null;
		}
		@Override
		protected void onPostExecute(Void result) {
			//mProgress.dismiss();
			completeProcess();
			super.onPostExecute(result);
		}
	}
	
	protected class PopulateChat extends AsyncTask<Integer , Integer, Void> {
    	FMDBDatabaseAccess fdb=new FMDBDatabaseAccess(StartPage.this);
    	 protected Void doInBackground(Integer... params) {
    		 try {
    			 SharedPreferences sharedPref = getSharedPreferences("AppDelegate",MODE_PRIVATE);
    		        Boolean id=sharedPref.getBoolean("isInChat", false);
    		        isInChat=id;
    		        Log.d("Came Out Chat", "");
    			 if(!isInChat){
    				 Log.d("Came In Chat", "");
	    		    SoapObject request = new SoapObject(namespace, getchat);
	    		    
	    		    SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
	    		    envelope.dotNet = true;
	    		    envelope.setOutputSoapObject(request);
	    		    
	    		    request.addProperty("strUserGuid",RegUserID);
	    		    request.addProperty("strNotFromUserGuid","");
	    		    
	    		    
	    		    HttpTransportSE androidHttpTransport = new HttpTransportSE(url);
	    		    androidHttpTransport.call(getchat_action, envelope);
	    		    
	    		    Object resonse=envelope.getResponse();
	    		    String resultdata = resonse.toString();
	    		    
	    		    
	    			
					
					if(!resultdata.equalsIgnoreCase("anyType{}")){
						
			              jObject = new JSONObject(resultdata.toLowerCase());
			              if(jObject!=null){
					    		JSONArray arr = jObject.getJSONArray("data");
								if(arr!=null){
									for(int i=0;i<arr.length();i++){
										JSONObject obj = arr.getJSONObject(i);
										
							              object._chatWithID=obj.getString("touser_id");
							              object._chatCommet=obj.getString("message");
							              object._regUserID=obj.getString("fromuser_id");
							              object._chatSyncDateTime=obj.getString("messagereaddatetime");
									      object._chatCreatedDateTime=obj.getString("createddatetime");
									      object._chatID=obj.getString("message_id");
									      object._chatSentBy="frnd";
									      object._isChatSync=true;
									      object._chatWithName="abc";
									      object._unread=false;
									      fdb.insertChat(object);
									     // object._chatID=obj.getString("message_id");
									      
											
										
										 
//			 							String isread=obj.getString("status");
//										if(isread.equalsIgnoreCase("read"))
//										{
//											fdb.updateIsRead(object);
//										}
//										else if(isread.equalsIgnoreCase("delv") ||isread.equalsIgnoreCase("new"))
//										{
//											
//											fdb.updateDelivered(object);
//											
//											
//									
//										}
										}
										}	
									}
								}
    			 }
					
	    		} catch (Exception e) {
	    		    
	    		    Log.d("Error=", ""+e.getMessage());
	    		    //mProgressDialog.dismiss();
	    		}
			return null;
    		 
    	 }
    	 
    	 protected void onPostExecute(Void result) {
    		 PopulateChat getchat=new PopulateChat();
    	        getchat.execute(10);
    	 }
    }

	protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) 
	{
		 super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
		 try
		 {
			 switch(requestCode)
			 {
			 	case projectReqst:
			 		
			 			finish();
			 		
			 		break;
			 }
			 if(resultCode==10001)
			 {
				 finish();
			 }
		 }
		 catch (Exception e)
		 {
			Log.d("Exception onActivityResult",""+e.getMessage());
		 }
	}
		
}
