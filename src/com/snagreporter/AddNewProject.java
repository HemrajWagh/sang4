package com.snagreporter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Currency;
import java.util.StringTokenizer;
import java.util.UUID;
import android.support.v4.view.ViewPager.LayoutParams;
import org.json.JSONArray;
import org.json.JSONObject;
import org.kobjects.base64.Base64;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.MarshalBase64;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import com.snagreporter.ProjectListPage.StartDownloadSnags;
import com.snagreporter.database.FMDBDatabaseAccess;
import com.snagreporter.database.ParseSyncData;
import com.snagreporter.entity.ApartmentMaster;
import com.snagreporter.entity.BuildingMaster;
import com.snagreporter.entity.FaultType;
import com.snagreporter.entity.FloorMaster;
import com.snagreporter.entity.JobType;
import com.snagreporter.entity.ProjectMaster;
import com.snagreporter.entity.SnagMaster;
import com.snagreporter.menuhandler.MenuHandler;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class AddNewProject extends Activity 
{
	private static final int CAMERA_PIC_REQUEST = 1337;
	EditText txtPrjName,txtNoOfbld,txtLoc,txtAdd1,txtAdd2,txtPin,txtCity,txtState,txtBldrName,txtabout;
	Bitmap myBmp;
	ImageView myImg;
	ProjectMaster objPrj;
	String imageName;
	boolean isOnline=false;
	private File dir, destImage,f;
	private String cameraFile = null;
	String bName,bID;
	ProgressDialog mProgressDialog2;
	

	View TopMenu;
		boolean isMenuVisible=false;
		MenuHandler menuhandler;
	@Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        setContentView(R.layout.add_new_project);
        
        menuhandler=new MenuHandler(AddNewProject.this);
        TopMenu=new View(this);
        RelativeLayout.LayoutParams rlp=new RelativeLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
        TopMenu.setLayoutParams(rlp);
        LayoutInflater inflater= LayoutInflater.from(this);
        TopMenu=(View) inflater.inflate(R.layout.popup_menu, null);
        this.addContentView(TopMenu, rlp);
        TopMenu.requestLayout();
        TopMenu.setVisibility(View.INVISIBLE);
        
        mProgressDialog2 = new ProgressDialog(AddNewProject.this);
        txtBldrName=(EditText)findViewById(R.id.addPrj_bldrname);
        txtPrjName=(EditText)findViewById(R.id.addPrj_prjname);
        txtAdd1=(EditText)findViewById(R.id.addPrj_addrss1);
        txtAdd2=(EditText)findViewById(R.id.addPrj_addrss2);
        txtCity=(EditText)findViewById(R.id.addPrj_city);
        txtLoc=(EditText)findViewById(R.id.addPrj_loc);
        txtabout=(EditText)findViewById(R.id.addPrj_aboutprj);
       // txtNoOfbld=(EditText)findViewById(R.id.addPrj_bldNo);
        txtPin=(EditText)findViewById(R.id.addPrj_pin);
        txtState=(EditText)findViewById(R.id.addPrj_state);
        myImg=(ImageView)findViewById(R.id.addPrj_imgAdd);
         bName=getIntent().getExtras().getString("BuilderName");
         bID=getIntent().getExtras().getString("BuilderID");
        txtBldrName.setText(bName);
        
        SharedPreferences sharedPref = getSharedPreferences("AppDelegate",MODE_PRIVATE);
        Boolean id=sharedPref.getBoolean("isOnline", false);
        isOnline=id;
        
        Button b=(Button)findViewById(R.id.topbar_statusbtn);
		ImageView i=(ImageView)findViewById(R.id.topbar_statusimage);
		if(isOnline){
			b.setText("Go Offline");
			i.setBackgroundDrawable(getResources().getDrawable(R.drawable.green));
    	}
    	else{
    		b.setText("Go Online");
    		i.setBackgroundDrawable(getResources().getDrawable(R.drawable.status_online_icon));
    	}
    }  
	
	//@@@@@@@MenuHandlers
	public void ShowTopMenu(){
		TopMenu.setVisibility(View.VISIBLE);
		isMenuVisible=true;
	}
	public void HideTopMenu(){
		TopMenu.setVisibility(View.INVISIBLE);
		isMenuVisible=false;
	}
	public void TopMenuClick(View v){
		if(isMenuVisible){
			HideTopMenu();
		}
		else{
			ShowTopMenu();
		}
	}
	public void TopMenuBGClick(View v){
		HideTopMenu();
		
	}
	public void MenuRoomsheetClick(View v){
		HideTopMenu();
		//menuhandler.MenuRoomsheetClick();
	}
	public void MenuReportsClick(View v){
		HideTopMenu();
		menuhandler.MenuReportsClick();
	}
	public void MenuSortFilterClick(View v){
		HideTopMenu();
		menuhandler.MenuSortFilterClick();
	}
	public void MenuChatClick(View v){
		HideTopMenu();
		menuhandler.MenuChatClick();
	}
	public void MenuAttendanceClick(View v){
		HideTopMenu();
		
		menuhandler.MenuAttendanceClick();
	}
	
	public void MenuGraphClick(View v){
		HideTopMenu();
		//menuhandler.MenuGraphClick("project");
	}
	public void MenuChartClick(View v){
		HideTopMenu();
		menuhandler.MenuChartClick();
	}
	public void MenuAttachClick(View v){
		HideTopMenu();
		menuhandler.MenuAttachClick();
	}
	
	//@@@@@@@MenuHandlers
	public Boolean istextValid()
	{
		Boolean res=false;
		String name,loc,add1,pin,city,state;
		name=txtPrjName.getText().toString();
		loc=txtLoc.getText().toString();
		add1=txtAdd1.getText().toString();
		pin=txtPin.getText().toString();
		city=txtCity.getText().toString();
		state=txtState.getText().toString();
		if(name!=null && name.length()>0 && loc!=null && loc.length()>0 && add1!=null && add1.length()>0 && pin!=null && pin.length()>0 && city!=null && city.length()>0 && state!=null && state.length()>0)
		{
			res=true;
		}
		else
		{
			res=false;
		}
		return res;
		
		
	}
	 protected class SaveDataToWeb extends AsyncTask<Integer , Integer, Void> 
	 {
		 ProgressDialog mProgressDialog = new ProgressDialog(AddNewProject.this);
			String output="";
			JSONObject jsobj;
			JSONArray jaRR;
			String value;
		  @Override
	        protected void onPreExecute() 
		  {  
			  if(isOnline){
	        /*	mProgressDialog.setCancelable(false);
	        	mProgressDialog.setMessage("Loading...");
	            mProgressDialog.show();*/
			  }
	        }   
		@Override
		protected Void doInBackground(Integer... params)
		{
			
			if(isOnline){
			String METHOD_NAME = "SaveNewDataToTheDataBase";
			String NAMESPACE = "http://tempuri.org/";
			String URL = ""+getResources().getString(R.string.WS_URL).toString()+"SnagReporter_WebS.asmx";
			String SOAP_ACTION = "http://tempuri.org/SaveNewDataToTheDataBase";
			try
			{
				SoapObject request=new SoapObject(NAMESPACE, METHOD_NAME);
				SoapSerializationEnvelope envelope=new SoapSerializationEnvelope(SoapEnvelope.VER11);
				envelope.dotNet=true;
				envelope.setOutputSoapObject(request);
				
				String CoumnNames="ID~ProjectName~Location~Address1~Address2~Pincode~City~State~ImageName~BuilderID~BuilderName";
				  String Values=""+objPrj.getID()+"~"+objPrj.getProjectName()+"~"+objPrj.getLocation()+"~"+objPrj.getAddress1()+"~"+objPrj.getAddress2()+"~"+objPrj.getPincode()+"~"+objPrj.getCity()+"~"+objPrj.getState()+"~"+objPrj.getImageName()+"~"+objPrj.getBuilderID()+"~"+objPrj.getBuilderName();
				  String TableName="ProjectMaster";
				request.addProperty("_strCoumnNames", CoumnNames);
				request.addProperty("_strValues", Values);
				request.addProperty("_strTableName", TableName);
				
				HttpTransportSE httptransport=new HttpTransportSE(URL);
				httptransport.call(SOAP_ACTION, envelope);
				Object response=envelope.getResponse();
				output=response.toString();
				if(output!=null)
				{
					StringTokenizer token=new StringTokenizer(output,":");
					String[] str=new String[3];
					str[0]=token.nextToken();
					str[1]=token.nextToken();
					str[2]=token.nextToken();
					value=str[2];
				}
				
			}
			catch (Exception e)
			{
				Log.d("SyncToWeb", ""+e.getMessage());
			}
			
			}
			
			return null;
		}
		@Override
        protected void onPostExecute(Void result)
		{
			objPrj.setStatusForUpload("Inserted");
			if(isOnline){
			//mProgressDialog.dismiss();
			if(value.equalsIgnoreCase("true}}"))
			{
				//JSONArray
				objPrj.setisDataSyncToWeb(true);
				FMDBDatabaseAccess fmdb=new FMDBDatabaseAccess(AddNewProject.this);
				fmdb.insertORUpdateProject(objPrj);
				if(imageName!=null && imageName.length()>0)
				{
					UploadImageToweb task=new UploadImageToweb();
					task.url=imageName;
					task.execute(10);
				}
				//finish();
			}
			else
			{
				 new AlertDialog.Builder(AddNewProject.this)
		    	    .setTitle("Update Failed")
		    	    .setMessage("Update Failed.")
		    	    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
		    	        public void onClick(DialogInterface dialog, int which) { 
		    	            // continue with delete
		    	        }
		    	     })
		    	    .show();
			}
			}
			else{
				objPrj.setisDataSyncToWeb(false);
				FMDBDatabaseAccess fmdb=new FMDBDatabaseAccess(AddNewProject.this);
				fmdb.insertORUpdateProject(objPrj);
				//finish();
			}
		}
	 }
	public void SubmitClick(View v)
	{
			
		if(istextValid())
		{
			objPrj=new ProjectMaster();
			objPrj.setProjectName(txtPrjName.getText().toString());
			//objPrj.setNoOfBuildings(Integer.parseInt(txtNoOfbld.getText().toString()));
			objPrj.setLocation(txtLoc.getText().toString());
			objPrj.setAddress1(txtAdd1.getText().toString());
			if(txtAdd2.getText()!=null && txtAdd2.getText().toString().length()>0)
			{
				objPrj.setAddress2(txtAdd2.getText().toString());
			}
			else
			{
				objPrj.setAddress2("");
			}
			objPrj.setPincode(txtPin.getText().toString());
			objPrj.setState(txtState.getText().toString());
			objPrj.setCity(txtCity.getText().toString());
			UUID uuid=UUID.randomUUID();
			objPrj.setID(uuid.toString());
			objPrj.setAbout(txtabout.getText().toString().trim());
			if(imageName!=null && imageName.length()>0)
			{
				objPrj.setImageName(imageName);
			}
			else
			{
				objPrj.setImageName("");
			}
			objPrj.setBuilderID(""+bID);
			objPrj.setBuilderName(""+bName);
			SaveDataToWeb task=new SaveDataToWeb();
			task.execute(10);
			finish();
		}	
		else
		{
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
	public void CancelClick(View v)
	{
		finish();
		
	}
	public void BackClick(View v){
    	finish();
    }
	public void OnImageClick(View v)
	{
		registerForContextMenu(v);
		openContextMenu(v);
		unregisterForContextMenu(v);
	}
	
	 @Override  
	  public void onCreateContextMenu(ContextMenu menu, View v,ContextMenuInfo menuInfo)
	  {
		 super.onCreateContextMenu(menu, v, menuInfo);
		 menu.setHeaderTitle("Choose Photo");
		 menu.add(0, v.getId(), 0, "Choose from Library");  
	     menu.add(0, v.getId(), 0, "Take with Camera");
		 
	  }
	 @Override
	  public boolean onContextItemSelected(MenuItem item)
	 {
		 if(item.getTitle()=="Choose from Library")
		 {
			 Intent intent=new Intent();
			 intent.setType("image/*");
			 intent.setAction(Intent.ACTION_GET_CONTENT);
			 startActivityForResult(intent,0);
			 
			 return true;
		 }
		 else if(item.getTitle()=="Take with Camera")
		 {
			 String FileName="";
			 UUID uniqueKey = UUID.randomUUID();
			FileName=uniqueKey.toString();
			 destImage=new File(Environment.getExternalStorageDirectory()+File.separator+"SnagReporter/Pictures/"+FileName+".jpg");
			  cameraFile=destImage.getAbsolutePath();
			  try{
				    if(!destImage.createNewFile())
				        Log.e("check", "unable to create empty file");

				}catch(IOException ex){
				    ex.printStackTrace();
				}
				f = new File(destImage.getAbsolutePath());
			 Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
			 cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(destImage));
				startActivityForResult(cameraIntent, CAMERA_PIC_REQUEST);
			 return true;
		 }
		 return true;
	 }
	 protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) 
	 {
	 	 super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
	 	try
	 	{
	 		if(requestCode==101 && resultCode==101 && imageReturnedIntent!=null){
    	  		String FilePath=imageReturnedIntent.getExtras().getString("FilePath");
    	  		String FileName=imageReturnedIntent.getExtras().getString("FileName");
    	  		menuhandler.SaveAttachment("","","", "", "", "", "", "", FileName, FilePath);
    	  		//Toast.makeText(ProjectListPage.this, "File123="+FilePath, Toast.LENGTH_LONG).show();
    	  	 }
	 		
	 		 switch(requestCode)
	 		 {
	 		 	case 0:
	 		 		if(resultCode == RESULT_OK && imageReturnedIntent!=null)
	 		        { 
	 		 			 Uri selectedImage = imageReturnedIntent.getData();
	 		            String[] filePathColumn = {MediaStore.Images.Media.DATA};

	 		            Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
	 		            cursor.moveToFirst();

	 		            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
	 		            String filePath = cursor.getString(columnIndex);
	 		            cursor.close();
	 		 			myBmp=BitmapFactory.decodeFile(filePath);
	 		 			Drawable draw=new BitmapDrawable(myBmp);
	 		 			myImg.setBackgroundDrawable(draw);
	 		 			//myImg.setImageBitmap(myBmp);
	 		 			File f=new File(filePath);
	 		 			if(f.exists())
	 		 			{
	 		 				imageName=f.getName();
	 		 			}
	 		 			else
	 		 			{	
	 		 				imageName="";
	 		 			}
	 		        }
	 		 		break;
	 		 	case CAMERA_PIC_REQUEST:
//	 		 		 Bitmap mBitmap = (Bitmap) imageReturnedIntent.getExtras().get("data");
//	 		 	   	Uri uri = (Uri) imageReturnedIntent.getExtras().get("data");
	 		 		try
	 		 		{
//	 		 			UUID uniqueKey = UUID.randomUUID();
//	 		 			String filename=uniqueKey.toString();
//	 		 			File file=new File(Environment.getExternalStorageDirectory()+File.separator+"SnagReporter/Pictures/"+filename+".jpg");
//	 		 			String filepath=file.toString();
//	 		 			 Bitmap bitmap;
//	 					
//	 					bitmap = Bitmap.createBitmap(mBitmap);
//	 				 OutputStream outStream = null;
//	 				 outStream = new FileOutputStream(file);
//	 				 bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outStream);
//	 		 	     outStream.flush();
//	 		 	     outStream.close();
	 		 			
	 		 			if(f==null){
	 		 	            if(cameraFile!=null)
	 		 	                f = new File(cameraFile);
	 		 	            else
	 		 	                Log.e("check", "camera file object null line no 279");
	 		 	        }else
	 		 	            Log.e("check", f.getAbsolutePath());
	 		 	    	
	 		 			Bitmap mBitmap = BitmapFactory.decodeFile(f.getAbsolutePath());
	 		 			if(mBitmap!=null){
	 		 				Drawable draw=new BitmapDrawable(mBitmap);
	 		 				myImg.setBackgroundDrawable(draw);
	 		 	   	 		//String PhotoURL="";
	 		 				//strFilePath=f.getAbsolutePath();
	 		 			imageName=f.getName();
	 		 			}
		 		 	   	
	 		 		
	 		         
	 		 		}
	 		 		catch (Exception e)
	 		 		{
						Log.d("exception",e.getMessage());
					}
	 		 		break;
	 		    
	 		 }
	 	}
	 	catch (Exception e) 
	 	{
			Log.d("Activity result", ""+e.getMessage());
		}
	 	 
	 		
	 }
	 protected class UploadImageToweb extends AsyncTask<Integer , Integer, Void> 
	 {
		 String url;
		@Override
		protected void onPreExecute() 
		{
			
		};
		 @Override
		protected Void doInBackground(Integer... params)
		 {
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
				    
				    
						String FileName=url;
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
			return null;
		}
		 
		 @Override
		protected void onPostExecute(Void result)
		{
			 
		}
		 
	 }
	
	 public void ChangeStatusClick(View v){
	    	try{	
	    		Button b=(Button)findViewById(R.id.topbar_statusbtn);
	    		ImageView i=(ImageView)findViewById(R.id.topbar_statusimage);
	    		if(isOnline){
	    			
	    			b.setText("Go Online");
	    			i.setBackgroundDrawable(getResources().getDrawable(R.drawable.status_online_icon));
	        		isOnline=false;
	        		SharedPreferences sharedPref = getSharedPreferences("AppDelegate",MODE_PRIVATE);
	                SharedPreferences.Editor prefEditor = sharedPref.edit();
	                prefEditor.putBoolean("isOnline",false);
	                prefEditor.commit();
	        	}
	        	else{
	        		b.setText("Go Offline");
	        		i.setBackgroundDrawable(getResources().getDrawable(R.drawable.green));
	        		isOnline=true;
	        		SharedPreferences sharedPref = getSharedPreferences("AppDelegate",MODE_PRIVATE);
	                SharedPreferences.Editor prefEditor = sharedPref.edit();
	                prefEditor.putBoolean("isOnline",true);
	                prefEditor.commit();
	        	}
	    	}
	    	catch(Exception e){
	    		Log.d("Error=", ""+e.getMessage());
	    	}
	    	}
	    	public void SyncClick(View v){
	    		try{	
	    			if(isOnline){
	            		SyncData();
	            	}
	    			else{
	    				new AlertDialog.Builder(AddNewProject.this)
	    	    	    
	    	    	    .setMessage("Please go online to Sync.")
	    	    	    .setPositiveButton("GoOnline", new DialogInterface.OnClickListener() {
	    	    	        public void onClick(DialogInterface dialog, int which) { 
	    	    	        	Button b=(Button)findViewById(R.id.topbar_statusbtn);
	    	    	    		ImageView i=(ImageView)findViewById(R.id.topbar_statusimage);
	    	    	    		b.setText("Go Offline");
	    	    	        	i.setBackgroundDrawable(getResources().getDrawable(R.drawable.green));
	    	    	        	isOnline=true;
	    	    	        	SharedPreferences sharedPref = getSharedPreferences("AppDelegate",MODE_PRIVATE);
	    	    	            SharedPreferences.Editor prefEditor = sharedPref.edit();
	    	    	            prefEditor.putBoolean("isOnline",true);
	    	    	            prefEditor.commit();
	    	    	            
	    	    	            SyncData();
	    	    	        	
	    	    	        }
	    	    	     })
	    	    	     .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
	    	    	        public void onClick(DialogInterface dialog, int which) { 
	    	    	            
	    	    	        }
	    	    	     })
	    	    	    .show();
	    			}
	    		}
	    		catch(Exception e){
	    			Log.d("Error=", ""+e.getMessage());
	    		}
	    	}
	    	public void ShowGraphClick(View v){
	    		try{	
	    			//openChart();
	    		}
	    		catch(Exception e){
	    			Log.d("Error=", ""+e.getMessage());
	    		}
	    	}
	    	public void ShowChartClick(View v){
	    		try{	
	    			Intent i=new Intent(AddNewProject.this,com.snagreporter.GraphWebView.class);
	            	startActivity(i);
	    		}
	    		catch(Exception e){
	    			Log.d("Error=", ""+e.getMessage());
	    		}
	    	}
	    	public void ReportsClick(View v){
	    		try{	
	    			
	    		}
	    		catch(Exception e){
	    			Log.d("Error=", ""+e.getMessage());
	    		}
	    	}
	    	
	    	//@@@@@@ Sync Process
	    	public void SyncData(){
	        	//StartSyncProgress task=new StartSyncProgress();
	        	//task.execute(10);
	    		ParseSyncData parser=new ParseSyncData(AddNewProject.this);
	    		parser.start();
	        }
	        protected class StartSyncProgress extends AsyncTask<Integer , Integer, Void> {
	        	ProjectMaster objPrjI=null,objPrjM=null;
	        	BuildingMaster objBldgI=null,objBldgM=null;
	        	FloorMaster objFlrI=null,objFlrM=null;
	        	ApartmentMaster objAptI=null,objAptM=null;
	        	SnagMaster objSnagI=null,objSnagM=null;
	        	FaultType objFlt=null;
	        	JobType objJob=null;
	        	boolean isprojectsDone=false;
	        	boolean isBuildingDone=false;
	        	boolean isFloorDone=false;
	        	boolean isAptDone=false;
	        	boolean isFaultDone=false;
	        	boolean isJobDone=false;
	        	boolean isSnagDone=false;
	        	boolean isAllDone=false;
	        	boolean result=false;
	            	
	                @Override
	                protected void onPreExecute() {  
	                	if(!mProgressDialog2.isShowing()){
	                		mProgressDialog2.setCancelable(false);
	                		mProgressDialog2.setMessage("Synchronizing...");
	                		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
	                		mProgressDialog2.show();
	                	}
	                	FMDBDatabaseAccess db=new FMDBDatabaseAccess(AddNewProject.this);
	                	objPrjI=db.getProjectsNotSynced(0);
	                	if(objPrjI==null){
	                		objPrjM=db.getProjectsNotSynced(1);
	                		if(objPrjM==null){
	                			isprojectsDone=true;
	                			objBldgI=db.getBuildingsNotSynced(0);
	                			if(objBldgI==null){
	                				objBldgM=db.getBuildingsNotSynced(1);
	                				if(objBldgM==null){
	                					isBuildingDone=true;
	                					objFlrI=db.getFloorsNotSynced(0);
	                					if(objFlrI==null){
	                						objFlrM=db.getFloorsNotSynced(1);
	                						if(objFlrM==null){
	                							isFloorDone=true;
	                							objAptI=db.getAptsNotSynced(0);
	                        					if(objAptI==null){
	                        						objAptM=db.getAptsNotSynced(1);
	                        						if(objAptM==null){
	                        							isAptDone=true;
	                        							objJob=db.getJobTypeNotSynced();
	                        							if(objJob==null){
	                        								isJobDone=true;
	                        								objFlt=db.getFaultTypeNotSynced();
	                        								if(objFlt==null){
	                        									isFaultDone=true;
	                        									objSnagI=db.getSnagsNotSynced(0);
	                                							if(objSnagI==null){
	                                								objSnagM=db.getSnagsNotSynced(1);
	                                								if(objSnagM==null){
	                                									isSnagDone=true;
	                                									isAllDone=true;
	                                								}
	                                								else{
	                                									mProgressDialog2.setMessage("Synchronizing Snags...");
	                                								}
	                                							}
	                                							else{
	                                								mProgressDialog2.setMessage("Synchronizing Snags...");
	                                							}
	                        								}
	                        							}
	                        							
	                        						}
	                        						else{
	                        							mProgressDialog2.setMessage("Synchronizing Apartments...");
	                        						}
	                        					}
	                        					else{
	                        						mProgressDialog2.setMessage("Synchronizing Apartments...");
	                        					}
	                						}
	                						else{
	                							mProgressDialog2.setMessage("Synchronizing Floors...");
	                						}
	                					}
	                					else{
	                						mProgressDialog2.setMessage("Synchronizing Floors...");
	                					}
	                				}
	                				else{
	                					mProgressDialog2.setMessage("Synchronizing Buildings...");
	                				}
	                			}
	                			else{
	                				mProgressDialog2.setMessage("Synchronizing Buildings...");
	                			}
	                		}
	                		else{
	                			mProgressDialog2.setMessage("Synchronizing Projects...");
	                		}
	                	}
	                	else{
	                		mProgressDialog2.setMessage("Synchronizing Projects...");
	                	}
	                }      
	                @Override
	                protected Void doInBackground(Integer... params) {
	                	
	                	
	                	if(objPrjI!=null){//NEW Project Sync

	            				//mProgressDialog2.setMessage("Synchronizing Projects...");
	            			
	            				String METHOD_NAME = "SaveNewDataToTheDataBase";
	            				String NAMESPACE = "http://tempuri.org/";
	            				String URL = ""+getResources().getString(R.string.WS_URL).toString()+"SnagReporter_WebS.asmx";
	            				String SOAP_ACTION = "http://tempuri.org/SaveNewDataToTheDataBase";
	            				try
	            				{
	            					SoapObject request=new SoapObject(NAMESPACE, METHOD_NAME);
	            					SoapSerializationEnvelope envelope=new SoapSerializationEnvelope(SoapEnvelope.VER11);
	            					envelope.dotNet=true;
	            					envelope.setOutputSoapObject(request);
	            					
	            					  String CoumnNames="ID~ProjectName~Location~Address1~Address2~Pincode~City~State~ImageName~BuilderID~BuilderName";
	            					  String Values=""+objPrjI.getID()+"~"+objPrjI.getProjectName()+"~"+objPrjI.getLocation()+"~"+objPrjI.getAddress1()+"~"+objPrjI.getAddress2()+"~"+objPrjI.getPincode()+"~"+objPrjI.getCity()+"~"+objPrjI.getState()+"~"+objPrjI.getImageName()+"~"+objPrjI.getBuilderID()+"~"+objPrjI.getBuilderName();
	            					  String TableName="ProjectMaster";
	            					request.addProperty("_strCoumnNames", CoumnNames);
	            					request.addProperty("_strValues", Values);
	            					request.addProperty("_strTableName", TableName);
	            					
	            					HttpTransportSE httptransport=new HttpTransportSE(URL);
	            					httptransport.call(SOAP_ACTION, envelope);
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
	            					
	            					
	            					uploadImage(objPrjI.getImageName());
	            				}
	            				catch (Exception e)
	            				{
	            					Log.d("SyncToWeb", ""+e.getMessage());
	            				}
	            			}
	                	else{
	                		
	                		if(objPrjM!=null){//Modified Project Sync

	                			
	                			
	                				String METHOD_NAME = "UpdateDataToTheDataBase";
	                	    		String NAMESPACE = "http://tempuri.org/";
	                	    		String URL = ""+getResources().getString(R.string.WS_URL).toString()+"SnagReporter_WebS.asmx";//SnagReporter_WCF.svc
	                	    		String SOAP_ACTION = "http://tempuri.org/UpdateDataToTheDataBase";//
	                	    		
	                	    		try {
	                	    		    SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
	                	    		    
	                	    		    SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
	                	    		    envelope.dotNet = true;
	                	    		    envelope.setOutputSoapObject(request);
	                	    		    
	                	    		    
	                	    		    String CoumnNames="ProjectName~Location~Address1~Address2~Pincode~City~State~ImageName";
	                	    		    String Values=""+objPrjM.getProjectName()+"~"+objPrjM.getLocation()+"~"+objPrjM.getAddress1()+"~"+objPrjM.getAddress2()+"~"+objPrjM.getPincode()+"~"+objPrjM.getCity()+"~"+objPrjM.getState()+"~"+objPrjM.getImageName();
	                	    		    String TableName="ProjectMaster";
	                	    		    String KeyColName="ID";
	                	    		    String ColDataType="String";
	                	    		    String ColValue=""+objPrjM.getID();
	                	    		    
	                	    		    request.addProperty("_strCoumnNames",CoumnNames);
	                	    		    request.addProperty("_strValues",Values);
	                	    		    request.addProperty("_strTableName",TableName);
	                	    		    request.addProperty("_strKeyColName",KeyColName);
	                	    		    request.addProperty("_strKeyColDataType",ColDataType);
	                	    		    request.addProperty("_keyColValue",ColValue);
	                	    		    
	                	    		    
	                	    		    HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
	                	    		    androidHttpTransport.call(SOAP_ACTION, envelope);
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
	                	    		    
	                	    		    
	                	    		    uploadImage(objPrjM.getImageName());
	                	    			
	                					//jObject = new JSONObject(resultData);
	                	    		}
	                	    		 catch(Exception e){
	                	    			 Log.d("Error=", ""+e.getMessage()); 
	                	    		 }
	                			
	                		
	                		}
	                		else{
	                			isprojectsDone=true;
	                			if(objBldgI!=null){//New BUilding Sync

	                				String METHOD_NAME = "SaveNewDataToTheDataBase";
	                				String NAMESPACE = "http://tempuri.org/";
	                				String URL = ""+getResources().getString(R.string.WS_URL).toString()+"SnagReporter_WebS.asmx";
	                				String SOAP_ACTION = "http://tempuri.org/SaveNewDataToTheDataBase";
	                				
	                				try
	                				{
	                					SoapObject request=new SoapObject(NAMESPACE, METHOD_NAME);
	                					SoapSerializationEnvelope envelope=new SoapSerializationEnvelope(SoapEnvelope.VER11);
	                					envelope.dotNet=true;
	                					envelope.setOutputSoapObject(request);
	                					
	                					  String CoumnNames="ID~BuildingName~ProjectID~ProjectName~ImageName";
	                					  String Values=""+objBldgI.getID()+"~"+objBldgI.getBuildingName()+"~"+objBldgI.getProjectID()+"~"+objBldgI.getProjectName()+"~"+objBldgI.getImageName()+"";
	                					  String TableName="BuildingMaster";
	                					request.addProperty("_strCoumnNames", CoumnNames);
	                					request.addProperty("_strValues", Values);
	                					request.addProperty("_strTableName", TableName);
	                					
	                					HttpTransportSE httptransport=new HttpTransportSE(URL);
	                					httptransport.call(SOAP_ACTION, envelope);
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
	                					
	                					uploadImage(objBldgI.getImageName());
	                				}
	                				catch (Exception e)
	                				{
	                					Log.d("SyncToWeb", ""+e.getMessage());
	                				}
	                			
	                			}
	                			else{
	                				if(objBldgM!=null){//Modified Building Sync

	                    				String METHOD_NAME = "UpdateDataToTheDataBase";
	                    	    		String NAMESPACE = "http://tempuri.org/";
	                    	    		String URL = ""+getResources().getString(R.string.WS_URL).toString()+"SnagReporter_WebS.asmx";//SnagReporter_WCF.svc
	                    	    		String SOAP_ACTION = "http://tempuri.org/UpdateDataToTheDataBase";//
	                    	    		
	                    	    		try {
	                    	    		    SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
	                    	    		    
	                    	    		    SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
	                    	    		    envelope.dotNet = true;
	                    	    		    envelope.bodyOut=request;
	                    	    		    envelope.setOutputSoapObject(request);
	                    	    		    
	                    	    		    
	                    	    		    String CoumnNames="BuildingName~ImageName";
	                    	    		    String Values=""+objBldgM.getBuildingName()+"~"+objBldgM.getImageName()+"";
	                    	    		    String TableName="BuildingMaster";
	                    	    		    String KeyColName="ID~ProjectID";
	                    	    		    String ColDataType="String~String";
	                    	    		    String ColValue=""+objBldgM.getID()+"~"+objBldgM.getProjectID()+"";
	                    	    		    
	                    	    		    request.addProperty("_strCoumnNames",CoumnNames);
	                    	    		    request.addProperty("_strValues",Values);
	                    	    		    request.addProperty("_strTableName",TableName);
	                    	    		    request.addProperty("_strKeyColName",KeyColName);
	                    	    		    request.addProperty("_strKeyColDataType",ColDataType);
	                    	    		    request.addProperty("_keyColValue",ColValue);
	                    	    		    
	                    	    		    
	                    	    		    HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
	                    	    		    androidHttpTransport.call(SOAP_ACTION, envelope);
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
	                    	    		   
	                    	    			uploadImage(objBldgM.getImageName());
	                    					
	                    	    		}
	                    	    		 catch(Exception e){
	                    	    			 Log.d("Error=", ""+e.getMessage()); 
	                    	    		 }
	                    			
	                				}
	                				else{
	                					isBuildingDone=true;
	                					if(objFlrI!=null){//new Floor Sync

	                	    				String METHOD_NAME = "SaveNewDataToTheDataBase";
	                	    				String NAMESPACE = "http://tempuri.org/";
	                	    				String URL = ""+getResources().getString(R.string.WS_URL).toString()+"SnagReporter_WebS.asmx";
	                	    				//String URL="http://snag.itakeon.com/SnagReporter_WebS.asmx";
	                	    				String SOAP_ACTION = "http://tempuri.org/SaveNewDataToTheDataBase";
	                	    				String colName="ID~ProjectName~ProjectID~Floor~BuildingID~BuildingName~ImageName~NoOfApartments";
	                	    				String colvalues=""+objFlrI.getID()+"~"+objFlrI.getProjectName()+"~"+objFlrI.getProjectID()+"~"+objFlrI.getFloor()+"~"+objFlrI.getBuildingID()+"~"+objFlrI.getBuildingName()+"~"+objFlrI.getImageName()+"~"+objFlrI.getNoOfApartments();
	                	    				String tablename="FloorMaster";
	                	    				try
	                	    				{
	                	    					SoapObject request=new SoapObject(NAMESPACE, METHOD_NAME);
	                	    					SoapSerializationEnvelope envelope=new SoapSerializationEnvelope(SoapEnvelope.VER11);
	                	    					envelope.dotNet=true;
	                	    					envelope.bodyOut=request;
	                	    					envelope.setOutputSoapObject(request);
	                	    					
	                	    					request.addProperty("_strCoumnNames",colName);
	                	    					request.addProperty("_strValues",colvalues);
	                	    					request.addProperty("_strTableName",tablename);
	                	    					
	                	    					HttpTransportSE httptransport=new HttpTransportSE(URL);
	                	    					try
	                	    					{
	                	    					httptransport.call(SOAP_ACTION, envelope);
	                	    					Object response=envelope.getResponse();
	                	    					Log.d("response",""+response.toString());
	                	    					
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
	                	    					
	                	    					
	                	    					uploadImage(objFlrI.getImageName());
	                	    					}
	                	    					catch (Exception e)
	                	    					{
	                	    						Log.d("Exception ",""+e.getMessage());
	                	    					}
	                	    					
	                	    				}
	                	    				catch (Exception e)
	                	    				{
	                	    					Log.d("SyncToWeb", ""+e.getMessage());
	                	    				}
	                	    			
	                					}
	                					else{
	                						if(objFlrM!=null){//Modified Floor Sync

	                		    				String METHOD_NAME = "UpdateDataToTheDataBase";
	                		    	    		String NAMESPACE = "http://tempuri.org/";
	                		    	    		String URL = ""+getResources().getString(R.string.WS_URL).toString()+"SnagReporter_WebS.asmx";//SnagReporter_WCF.svc
	                		    	    		String SOAP_ACTION = "http://tempuri.org/UpdateDataToTheDataBase";//
	                		    	    		
	                		    	    		try {
	                		    	    		    SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
	                		    	    		    
	                		    	    		    SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
	                		    	    		    envelope.dotNet = true;
	                		    	    		    envelope.setOutputSoapObject(request);
	                		    	    		    
	                		    	    		    
	                		    	    		    String CoumnNames="Floor~ImageName";
	                		    	    		    String Values=""+objFlrM.getFloor()+"~"+objFlrM.getImageName();
	                		    	    		    String TableName="FloorMaster";
	                		    	    		    String KeyColName="ID~ProjectID~BuildingID";
	                		    	    		    String ColDataType="String~String~String";
	                		    	    		    String ColValue=""+objFlrM.getID()+"~"+objFlrM.getProjectID()+"~"+objFlrM.getBuildingID();
	                		    	    		    
	                		    	    		    request.addProperty("_strCoumnNames",CoumnNames);
	                		    	    		    request.addProperty("_strValues",Values);
	                		    	    		    request.addProperty("_strTableName",TableName);
	                		    	    		    request.addProperty("_strKeyColName",KeyColName);
	                		    	    		    request.addProperty("_strKeyColDataType",ColDataType);
	                		    	    		    request.addProperty("_keyColValue",ColValue);
	                		    	    		    
	                		    	    		    
	                		    	    		    HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
	                		    	    		    androidHttpTransport.call(SOAP_ACTION, envelope);
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
	                		    	    		    
	                		    	    		    uploadImage(objFlrM.getImageName());
	                		    	    			
	                		    					
	                		    	    		}
	                		    	    		 catch(Exception e){
	                		    	    			 Log.d("Error=", ""+e.getMessage()); 
	                		    	    		 }
	                		    			
	                						}
	                						else{
	                							isFloorDone=true;
	                							if(objAptI!=null){//New ApartMEnt Sync

	                			    				String METHOD_NAME = "SaveNewDataToTheDataBase";
	                			    				String NAMESPACE = "http://tempuri.org/";
	                			    				String URL = ""+getResources().getString(R.string.WS_URL).toString()+"SnagReporter_WebS.asmx";
	                			    				String SOAP_ACTION = "http://tempuri.org/SaveNewDataToTheDataBase";
	                			    				
	                			    				try
	                			    				{
	                			    					SoapObject request=new SoapObject(NAMESPACE, METHOD_NAME);
	                			    					SoapSerializationEnvelope envelope=new SoapSerializationEnvelope(SoapEnvelope.VER11);
	                			    					envelope.dotNet=true;
	                			    					envelope.setOutputSoapObject(request);
	                			    					
	                			    					  String CoumnNames="ID~BuildingName~ProjectID~ProjectName~ImageName~ApartmentNo~BuildingID~Floor~FloorID";
	                			    					  String Values=""+objAptI.getID()+"~"+objAptI.getBuildingName()+"~"+objAptI.getProjectID()+"~"+objAptI.getProjectName()+"~"+objAptI.getImageName()+"~"+objAptI.getApartmentNo()+"~"+objAptI.getBuildingID()+"~"+objAptI.getFloor()+"~"+objAptI.getFloorID();
	                			    					  String TableName="ApartmentMaster";
	                			    					request.addProperty("_strCoumnNames", CoumnNames);
	                			    					request.addProperty("_strValues", Values);
	                			    					request.addProperty("_strTableName", TableName);
	                			    					
	                			    					HttpTransportSE httptransport=new HttpTransportSE(URL);
	                			    					httptransport.call(SOAP_ACTION, envelope);
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
	                			    					
	                			    					
	                			    					uploadImage(objAptI.getImageName());
	                			    					
	                			    				}
	                			    				catch (Exception e)
	                			    				{
	                			    					Log.d("SyncToWeb", ""+e.getMessage());
	                			    				}
	                			    			
	                							}
	                							else{
	                								if(objAptM!=null){//Modified Apartment Sync

	                				    				String METHOD_NAME = "UpdateDataToTheDataBase";
	                				    	    		String NAMESPACE = "http://tempuri.org/";
	                				    	    		String URL = ""+getResources().getString(R.string.WS_URL).toString()+"SnagReporter_WebS.asmx";//SnagReporter_WCF.svc
	                				    	    		String SOAP_ACTION = "http://tempuri.org/UpdateDataToTheDataBase";//
	                				    	    		
	                				    	    		try {
	                				    	    		    SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
	                				    	    		    
	                				    	    		    SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
	                				    	    		    envelope.dotNet = true;
	                				    	    		    envelope.setOutputSoapObject(request);
	                				    	    		    
	                				    	    		    
	                				    	    		    String CoumnNames="ApartmentNo~ImageName";
	                				    	    		    String Values=""+objAptM.getApartmentNo()+"~"+objAptM.getImageName();
	                				    	    		    String TableName="ApartmentMaster";
	                				    	    		    String KeyColName="ID~ProjectID~BuildingID~FloorID";
	                				    	    		    String ColDataType="String~String~String~String";
	                				    	    		    String ColValue=""+objAptM.getID()+"~"+objAptM.getProjectID()+"~"+objAptM.getBuildingID()+"~"+objAptM.getFloorID();
	                				    	    		    
	                				    	    		    request.addProperty("_strCoumnNames",CoumnNames);
	                				    	    		    request.addProperty("_strValues",Values);
	                				    	    		    request.addProperty("_strTableName",TableName);
	                				    	    		    request.addProperty("_strKeyColName",KeyColName);
	                				    	    		    request.addProperty("_strKeyColDataType",ColDataType);
	                				    	    		    request.addProperty("_keyColValue",ColValue);
	                				    	    		    
	                				    	    		    
	                				    	    		    HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
	                				    	    		    androidHttpTransport.call(SOAP_ACTION, envelope);
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
	                				    	    		    
	                				    	    		   
	                				    	    		    uploadImage(objAptM.getImageName());
	                				    	    		    
	                				    	    		}
	                				    	    		 catch(Exception e){
	                				    	    			 Log.d("Error=", ""+e.getMessage()); 
	                				    	    		 }
	                				    			
	                										
	                								}
	                								else{
	                									isAptDone=true;
	                									if(objJob!=null){//New JobTYpe Sync
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
	        							    	    		
	        							    	    		    if(output!=null){
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
	        							    	    			 Log.d("Error=", ""+e.getMessage()); 
	        							    	    		 }
	                									}
	                									else{
	                										isJobDone=true;
	                										if(objFlt!=null){//New Fault Type Sync
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
	            							    	    		
	            							    	    		    if(output!=null){
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
	            							    	    			 Log.d("Error=", ""+e.getMessage()); 
	            							    	    		 }
	                										}
	                										else{
	                											isFaultDone=true;
	                											if(objSnagI!=null){//New Snag Sync

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
	                							    	    		    
	                							    	    		    
	                							    	    		    String CoumnNames="ID~SnagType~SnagDetails~PictureURL1~PictureURL2~PictureURL3~ProjectID~ProjectName~BuildingID~BuildingName~FloorID~Floor~ApartmentID~Apartment~AptAreaName~SnagStatus~ResolveDate~ExpectedInspectionDate~FaultType~InspectorID~InspectorName~Cost~CostTo~PriorityLevel";
	                							    	    		    String Values=""+objSnagI.getID()+"~"+objSnagI.getSnagType()+"~"+objSnagI.getSnagDetails()+"~"+objSnagI.getPictureURL1()+"~"+objSnagI.getPictureURL2()+"~"+objSnagI.getPictureURL3()+"~"+objSnagI.getProjectID()+"~"+objSnagI.getProjectName()+"~"+objSnagI.getBuildingID()+"~"+objSnagI.getBuildingName()+"~"+objSnagI.getFloorID()+"~"+objSnagI.getFloor()+"~"+objSnagI.getApartmentID()+"~"+objSnagI.getApartment()+"~"+objSnagI.getAptAreaName()+"~"+objSnagI.getSnagStatus()+"~"+objSnagI.getResolveDate()+"~"+objSnagI.getExpectedInspectionDate()+"~"+objSnagI.getFaultType()+"~"+objSnagI.getInspectorID()+"~"+objSnagI.getInspectorName()+"~"+objSnagI.getCost()+"~"+objSnagI.getCostTo()+"~"+objSnagI.getSnagPriority();
	                							    	    		    String TableName="SnagMaster";
	                							    	    		    
	                							    	    		    request.addProperty("_strCoumnNames",CoumnNames);
	                							    	    		    request.addProperty("_strValues",Values);
	                							    	    		    request.addProperty("_strTableName",TableName);
	                							    	    		    
	                							    	    		    
	                							    	    		    HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
	                							    	    		    androidHttpTransport.call(SOAP_ACTION, envelope);
	                							    	    		    Object resonse=envelope.getResponse();
	                							    	    		    String output = resonse.toString();
	                							    	    		
	                							    	    		    uploadImage(objSnagI.getPictureURL1());
	                							    	        		uploadImage(objSnagI.getPictureURL2());
	                							    	        		uploadImage(objSnagI.getPictureURL3());
	                							    	    		    
	                							    	        		if(output!=null){
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
	                							    	    			 Log.d("Error=", ""+e.getMessage()); 
	                							    	    		 }
	                							    			
	                											}
	                											else{
	                												if(objSnagM!=null){//Modified Snag Sync

	                								    				String METHOD_NAME = "UpdateDataToTheDataBase";
	                								    	    		String NAMESPACE = "http://tempuri.org/";
	                								    	    		String URL = ""+getResources().getString(R.string.WS_URL).toString()+"SnagReporter_WebS.asmx";//SnagReporter_WCF.svc
	                								    	    		String SOAP_ACTION = "http://tempuri.org/UpdateDataToTheDataBase";//
	                								    	    		String res = "";
	                								    	    		try {
	                								    	    		    SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
	                								    	    		    
	                								    	    		    SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
	                								    	    		    envelope.dotNet = true;
	                								    	    		    envelope.setOutputSoapObject(request);
	                								    	    		    
	                								    	    		    
	                								    	    		    String CoumnNames="SnagType~SnagDetails~FaultType~ExpectedInspectionDate~PictureURL1~PictureURL2~PictureURL3~ApartmentID~Apartment~AptAreaName~ReportDate~SnagStatus~ReInspectedUnresolvedDate~ReInspectedUnresolvedDatePictureURL1~ReInspectedUnresolvedDatePictureURL2~ReInspectedUnresolvedDatePictureURL3~ResolveDate~ResolveDatePictureURL1~ResolveDatePictureURL2~ResolveDatePictureURL3~Cost~CostTo~PriorityLevel";
	                								    	    		    String Values=""+objSnagM.getSnagType()+"~"+objSnagM.getSnagDetails()+"~"+objSnagM.getFaultType()+"~"+objSnagM.getExpectedInspectionDate()+"~"+objSnagM.getPictureURL1()+"~"+objSnagM.getPictureURL2()+"~"+objSnagM.getPictureURL3()+"~"+objSnagM.getApartmentID()+"~"+objSnagM.getApartment()+"~"+objSnagM.getAptAreaName()+"~"+objSnagM.getReportDate()+"~"+objSnagM.getSnagStatus()+"~"+objSnagM.getReInspectedUnresolvedDate()+"~"+objSnagM.getReInspectedUnresolvedDatePictureURL1()+"~"+objSnagM.getReInspectedUnresolvedDatePictureURL2()+"~"+objSnagM.getReInspectedUnresolvedDatePictureURL3()+"~"+objSnagM.getResolveDate()+"~"+objSnagM.getResolveDatePictureURL1()+"~"+objSnagM.getResolveDatePictureURL2()+"~"+objSnagM.getResolveDatePictureURL3()+"~"+objSnagM.getCost()+"~"+objSnagM.getCostTo()+"~"+objSnagM.getSnagPriority();
	                								    	    		    String TableName="SnagMaster";
	                								    	    		    String KeyColName="ID";
	                								    	    		    String ColDataType="String";
	                								    	    		    String ColValue=""+objSnagM.getID();
	                								    	    		    
	                								    	    		    request.addProperty("_strCoumnNames",CoumnNames);
	                								    	    		    request.addProperty("_strValues",Values);
	                								    	    		    request.addProperty("_strTableName",TableName);
	                								    	    		    request.addProperty("_strKeyColName",KeyColName);
	                								    	    		    request.addProperty("_strKeyColDataType",ColDataType);
	                								    	    		    request.addProperty("_keyColValue",ColValue);
	                								    	    		    
	                								    	    		    
	                								    	    		    HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
	                								    	    		    androidHttpTransport.call(SOAP_ACTION, envelope);
	                								    	    		    Object resonse=envelope.getResponse();
	                								    	    		   String  output = resonse.toString();
	                								    	    		    
	                								    	    		    uploadImage(objSnagM.getPictureURL1());
	                								    	    			uploadImage(objSnagM.getPictureURL2());
	                								    	    			uploadImage(objSnagM.getPictureURL3());
	                								    	    			uploadImage(objSnagM.getReInspectedUnresolvedDatePictureURL1());
	                								    	    			uploadImage(objSnagM.getReInspectedUnresolvedDatePictureURL2());
	                								    	    			uploadImage(objSnagM.getReInspectedUnresolvedDatePictureURL3());
	                								    	    			uploadImage(objSnagM.getResolveDatePictureURL1());
	                								    	    			uploadImage(objSnagM.getResolveDatePictureURL2());
	                								    	    			uploadImage(objSnagM.getResolveDatePictureURL3());
	                								    	    			
	                								    	    			if(output!=null){
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
	                								    	    			 Log.d("Error=", ""+e.getMessage()); 
	                								    	    		 }
	                								    			
	                												}
	                												else{
	                													isSnagDone=true;
	                													isAllDone=true;
	                												}
	                											}
	                										}
	                									}
	                								}
	                							}
	                						}
	                					}
	                				}
	                			}
	                		}
	                	}
	            		
	                    return null;
	                }
	                @Override
	                protected void onPostExecute(Void result1) {
	                	FMDBDatabaseAccess db=new FMDBDatabaseAccess(AddNewProject.this);
	                	if(!isprojectsDone){
	                		if(objPrjI!=null && result){
	                			db.updateProjectSynced(objPrjI.getID());
	                		}
	                		if(objPrjM!=null && result){
	                			db.updateProjectSynced(objPrjM.getID());
	                		}
	                	}
	                	else{
	                		if(!isBuildingDone){
	                			if(objBldgI!=null && result){
	                    			db.updateBuildingSynced(objBldgI.getID());
	                    		}
	                    		if(objBldgM!=null && result){
	                    			db.updateBuildingSynced(objBldgM.getID());
	                    		}
	                		}
	                		else{
	                			if(!isFloorDone){
	                				if(objFlrI!=null && result){
	                        			db.updateFloorSynced(objFlrI.getID());
	                        		}
	                        		if(objFlrM!=null && result){
	                        			db.updateFloorSynced(objFlrM.getID());
	                        		}
	                			}
	                			else{
	                				if(!isAptDone){
	                					if(objAptI!=null && result){
	                						db.updateAptSynced(objAptI.getID());
	                            		}
	                            		if(objAptM!=null && result){
	                            			db.updateAptSynced(objAptM.getID());
	                            		}
	                				}
	                				else{
	                					if(!isJobDone){
	                						if(objJob!=null && result){
	                							db.updateJobTypeSynced(objJob.getID());
	                						}
	                					}
	                					else{
	                						if(!isFaultDone){
	                							if(objFlt!=null && result){
	                								db.updateFaultTypeSynced(objFlt.getID());
	                							}
	                						}
	                						else{
	                							if(!isSnagDone){
	                								if(objSnagI!=null && result){
	                									db.updateSnagSynced(objSnagI.getID());
	                	                    		}
	                	                    		if(objSnagM!=null && result){
	                	                    			db.updateSnagSynced(objSnagM.getID());
	                	                    		}
	                							}
	                						}
	                					}
	                				}
	                			}
	                		}
	                	}
	                	if(!isAllDone){
	                		StartSyncProgress task=new StartSyncProgress();
	                		task.execute(10);
	                	}
	                	else{
//	                		mProgressDialog2.setMessage("Synchronization Complete...");
//	                		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
//	                		mProgressDialog2.dismiss();
	                		StartDownloadSnags task=new StartDownloadSnags();
	                		task.execute(10);
	                	}
	                }
	                 
	        }

	        protected class StartDownloadSnags extends AsyncTask<Integer , Integer, Void> {
	        	
	        	
	        	boolean result=false;
	        	String resultData="";
	                @Override
	                protected void onPreExecute() {  
	                	if(!mProgressDialog2.isShowing()){
	                		mProgressDialog2.setCancelable(false);
	                		mProgressDialog2.setMessage("Downloading Snags...");
	                		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
	                		mProgressDialog2.show();
	                	}
	                	
	                	
	                }      
	                @Override
	                protected Void doInBackground(Integer... params) {
	                	String METHOD_NAME = "GetDataTable";
	            		String NAMESPACE = "http://tempuri.org/";
	            		String URL = ""+getResources().getString(R.string.WS_URL).toString()+"SnagReporter_WebS.asmx";//SnagReporter_WCF.svc
	            		String SOAP_ACTION = "http://tempuri.org/GetDataTable";
	            		String res = "";
	            		try {
	            		    SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
	            		    
	            		    SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
	            		    envelope.bodyOut=request;
	            		    envelope.dotNet = true;
	            		    envelope.setOutputSoapObject(request);
	            		    
	            		    request.addProperty("_strTableName","SnagMaster");
	            		    
	            		    HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
	            		    androidHttpTransport.call(SOAP_ACTION, envelope);
	            		    Object resonse=envelope.getResponse();
	            		    resultData = resonse.toString();
	            		    
	            		    
	            		    
	            		}
	            		catch(Exception e){
	            			
	            		}
	                    return null;
	            		
	                }
	                @Override
	                protected void onPostExecute(Void result1) {
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
	            		mProgressDialog2.setMessage("Synchronization Complete...");
	            		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
	            		mProgressDialog2.dismiss();
	                }
	                 
	        }
	        public void parseSnagData(JSONObject obj1){
	        	try{
	        		String ID=obj1.getString("ID");
	        		String ApartmentID=obj1.getString("ApartmentID");
	        		String Apartment=obj1.getString("Apartment");
	        		String FloorID=obj1.getString("FloorID");
	        		String Floor=obj1.getString("Floor");
	        		String BuildingID=obj1.getString("BuildingID");
	        		String BuildingName=obj1.getString("BuildingName");
	        		String ProjectID=obj1.getString("ProjectID");
	        		String ProjectName=obj1.getString("ProjectName");
	        		String SnagType=obj1.getString("SnagType");
	        		String SnagDetails=obj1.getString("SnagDetails");
	        		String PictureURL1=obj1.getString("PictureURL1");
	        		String PictureURL2=obj1.getString("PictureURL2");
	        		String PictureURL3=obj1.getString("PictureURL3");
	        		String AptAreaName=obj1.getString("AptAreaName");
	        		String AptAreaID=obj1.getString("AptAreaID");
	        		String ReportDate=obj1.getString("ReportDate");
	        		String SnagStatus=obj1.getString("SnagStatus");
	        		String ResolveDate=obj1.getString("ResolveDate");
	        		String InspectorID=obj1.getString("InspectorID");
	        		String InspectorName=obj1.getString("InspectorName");
	        		String ResolveDatePictureURL1=obj1.getString("ResolveDatePictureURL1");
	        		String ResolveDatePictureURL2=obj1.getString("ResolveDatePictureURL2");
	        		String ResolveDatePictureURL3=obj1.getString("ResolveDatePictureURL3");
	        		
	        		String ReInspectedUnresolvedDate="";
	        		try{
	        			ReInspectedUnresolvedDate=obj1.getString("ReInspectedUnresolvedDate");
	        		}
	        		catch(Exception e){
	        			
	        		}
	        		String ReInspectedUnresolvedDatePictureURL1="";
	        		try{
	        			ReInspectedUnresolvedDatePictureURL1=obj1.getString("ReInspectedUnresolvedDatePictureURL1");
	        		}
	        		catch(Exception e){
	        			ReInspectedUnresolvedDatePictureURL1="";
	        		}
	        		
	        		String ReInspectedUnresolvedDatePictureURL2="";
	        		try{
	        			ReInspectedUnresolvedDatePictureURL2=obj1.getString("ReInspectedUnresolvedDatePictureURL2");
	        		}
	        		catch(Exception e){
	        			ReInspectedUnresolvedDatePictureURL2="";
	        		}
	        		
	        		String ReInspectedUnresolvedDatePictureURL3="";
	        		try{
	        			ReInspectedUnresolvedDatePictureURL3=obj1.getString("ReInspectedUnresolvedDatePictureURL3");
	        		}
	        		catch(Exception e){
	        			ReInspectedUnresolvedDatePictureURL3="";
	        		}
	        		
	        		String ExpectedInspectionDate="";
	        		try{
	        			ExpectedInspectionDate=obj1.getString("ExpectedInspectionDate");
	        		}
	        		catch(Exception e){
	        			ExpectedInspectionDate="";
	        		}
	        		String FaultType="";
	        		try{
	        			FaultType=obj1.getString("FaultType");
	        		}
	        		catch(Exception e){
	        			FaultType="";
	        		}
	        		
	        		String Cost="";
	        		try{
	        			Cost=obj1.getString("Cost");
	        		}
	        		catch(Exception e){
	        			Cost="";
	        		}
	        		
	        		String CostTo="";
	        		try{
	        			CostTo=obj1.getString("CostTo");
	        		}
	        		catch(Exception e){
	        			CostTo="";
	        		}
	        		if(CostTo==null || CostTo.equalsIgnoreCase("null")){
	        			CostTo="";
	        		}
	        		
	        		
	        		String PriorityLevel="";
	        		try{
	        			PriorityLevel=obj1.getString("PriorityLevel");
	        		}
	        		catch(Exception e){
	        			PriorityLevel="";
	        		}
	        		if(PriorityLevel==null || PriorityLevel.equalsIgnoreCase("null")){
	        			PriorityLevel="";
	        		}
	        		
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
	        		obj.setReInspectedUnresolvedDate(ReInspectedUnresolvedDate);
	        		obj.setReInspectedUnresolvedDatePictureURL1(ReInspectedUnresolvedDatePictureURL1);
	        		obj.setReInspectedUnresolvedDatePictureURL2(ReInspectedUnresolvedDatePictureURL2);
	        		obj.setReInspectedUnresolvedDatePictureURL3(ReInspectedUnresolvedDatePictureURL3);
	        		obj.setExpectedInspectionDate(ExpectedInspectionDate);
	        		obj.setFaultType(FaultType);
	        		obj.setSnagPriority(PriorityLevel);
	        		if(Cost.length()!=0)
	        			obj.setCost(Double.parseDouble(Cost));
	        		else
	        			obj.setCost(0.0);
	        		
	        		obj.setCostTo(CostTo);
	        		obj.setStatusForUpload("Inserted");
	            	obj.setIsDataSyncToWeb(true);
	            	FMDBDatabaseAccess db=new FMDBDatabaseAccess(AddNewProject.this);
	        		db.insertORUpdateSnagMaster(obj);
	        	}
	        	catch(Exception e){
	        		Log.d("Error=", ""+e.getMessage());
	        	}
	        }

	            
	          //@@@@@@$$$$$$$$$$@@@@@@@@@@@
	        
	        public void uploadImage(String url)
	        {
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
	        public byte[] getBytesFromBitmap(Bitmap bitmap) {
	            ByteArrayOutputStream stream = new ByteArrayOutputStream();
	            bitmap.compress(CompressFormat.JPEG, 70, stream);
	            return stream.toByteArray();
	        }
	        
	        //@@@@@@$$$$$$$$$$@@@@@@@@@@@

}
