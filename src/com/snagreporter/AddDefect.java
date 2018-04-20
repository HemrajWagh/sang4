package com.snagreporter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.UUID;

import org.json.JSONArray;
import org.json.JSONObject;
import org.kobjects.base64.Base64;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.MarshalBase64;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;



import com.snagreporter.R;
import com.snagreporter.listitems.Header;
import com.snagreporter.listitems.Item;
import com.snagreporter.listitems.ListItemAddDefect;
import com.snagreporter.listitems.ListItemAddDefectPhoto;
import com.snagreporter.menuhandler.MenuHandler;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.view.ViewPager.LayoutParams;
import android.text.InputType;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Display;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.snagreporter.database.*;
import com.snagreporter.entity.ApartmentDetails;
import com.snagreporter.entity.ApartmentMaster;
import com.snagreporter.entity.BuildingMaster;
import com.snagreporter.entity.Employee;
import com.snagreporter.entity.FaultType;
import com.snagreporter.entity.FloorMaster;
import com.snagreporter.entity.JobMaster;
import com.snagreporter.entity.JobType;
import com.snagreporter.entity.ProjectMaster;
import com.snagreporter.entity.Registration;
import com.snagreporter.entity.SnagChecklistEntries;
import com.snagreporter.entity.SnagMaster;
import com.snagreporter.entity.StdFloorAreas;
import com.snagreporter.entity.TradeDetails;
import com.snagreporter.entity.TradeMaster;

import android.app.DatePickerDialog;


public class AddDefect extends Activity// implements OnScrollListener
{
	
	FloorMaster[] florList;
	String[] strSetValues;
	String[] strGetValues;
	String strCntrctrStatus="";
 	ListView list;
	ImageButton imgVw1,imgVw2,imgVw3;
	int cnt=1;
	List<Item> items;
	String sample;
	BuildingMaster CurrentBuilding;
	FloorMaster CurrentFloor;
	ProjectMaster CurrentProject;
	ApartmentMaster CurrentAPT;
	Boolean isExtArea=false;
	BuildingMaster[] extArea;
	String SelectedArea="",SelectedAreaID="",SelectedTrade="",SelectedSubTrade="",SelectedJobDetails="",SelectedInspector="",selectedCost="",costTo="",snagPriority="";//SelectedFaultType/SelectedJobType
	JobType arrJobType[];
	FaultType arrFaultType[];
	private static final int CAMERA_PIC_REQUEST = 1337;
	String LoginType="";
	int as_index=-1;
	int as_index_selected=0;
	boolean isImageSetFor1=false;
	boolean isImageSetFor2=false;
	boolean isImageSetFor3=false;
	String PhotoURl1="",PhotoURl2="",PhotoURl3="";
	Bitmap BtnImageBmp,BtnYourImg;
	String strFromImg,strFromImgvw,strFilePath,strMenuType;
	private Bitmap mBitmap;
	int SelJobTypeIndex=0;
	ApartmentDetails AreaList[];
	TextView SelTextInList;
	String RemovedPhotoURL;
	boolean isAptmt;
	StdFloorAreas CurrentSFA;
	//Inspector arrInspetor[];
	Registration arrInspetor[];
	static final int DATE_DIALOG_ID = 1;
	private int year, month, day,year2, month2, day2;
	String ReportedDate="";
	String ExpectedDate="";
	int selectedInspectorIndex=0;
	int CurrentDate=-1;
	boolean isFirstTime;
	TwoTextArrayAdapter adapter;
	TwoTextArrayAdapter adapter2;
	
	boolean isOnline=false;
	ProgressDialog mProgressDialog2;
	int image=0;
	
	private File dir, destImage,f;
	private String cameraFile = null;
	SnagMaster NewSnag;
	JobType[] AddedSnagType;
	FaultType[] AddedFaultType;
	Employee[] arrEmployee;
	int SelectedEmployeeIndex=-1;
	String strLoginType="";
	String RegUserID="";
	
	View TopMenu;
	boolean isMenuVisible=false;
	MenuHandler menuhandler;
	String SelectedAreaTypeParent="";
	String AreaType="";
	String AreaTypeID="";
	TradeMaster arrTradeMaster[];
	String SelectedTradeMasterID="";
	TradeDetails[]arrTDDistinctTrade;
	String SelectedTradeType="";
	String SelectedInspectionGroup="";
	String[]arrTDDistinctInspectionGroup;
	TradeDetails[]arrTDDistinctInspectionDescription;
	String selectedInscpectionDescription="";
	SnagChecklistEntries []arrCheckList;
	boolean isRotated=false;
	boolean isFloorExtArea;
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        try{
        setContentView(R.layout.add_defect);
       
        SharedPreferences SP = getSharedPreferences("AppDelegate",MODE_PRIVATE);
        RegUserID=SP.getString("RegUserID", "");
        LoginType=SP.getString("LoginType", "");
        
        menuhandler=new MenuHandler(AddDefect.this);
        TopMenu=new View(this);
        RelativeLayout.LayoutParams rlp=new RelativeLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
        TopMenu.setLayoutParams(rlp);
        LayoutInflater inflater= LayoutInflater.from(this);
        TopMenu=(View) inflater.inflate(R.layout.popup_menu, null);
        this.addContentView(TopMenu, rlp);
        TopMenu.requestLayout();
        TopMenu.setVisibility(View.INVISIBLE);
        
        FMDBDatabaseAccess obj=new FMDBDatabaseAccess(getApplicationContext());
        Registration REG=obj.getRegistration();
        strLoginType=REG.getType();
        mProgressDialog2 = new ProgressDialog(AddDefect.this);
        CurrentProject=(ProjectMaster) getIntent().getExtras().get("Project");
        CurrentBuilding=(BuildingMaster)getIntent().getExtras().get("Building");
        isExtArea=getIntent().getBooleanExtra("isFromExtArea",false);
        isFloorExtArea=getIntent().getBooleanExtra("isFromFloorArea",false);
        if(!isExtArea)
        {
        	CurrentFloor=(FloorMaster)getIntent().getExtras().get("Floor");
        }
        
        isAptmt=getIntent().getBooleanExtra("isAptmt",false);
        
        Button btn=(Button)findViewById(R.id.adddefectSavebtn);
        btn.setBackgroundDrawable(getResources().getDrawable(R.drawable.back_blue_button));
        
        
        
        
        arrInspetor=new Registration[1];
        arrInspetor[0]=obj.getRegistrationDetailForID(RegUserID);
        Object objNot = getLastNonConfigurationInstance();
        if(objNot!=null)
        {
        	Intent i=(Intent)objNot;
        	strGetValues=(String[])i.getExtras().get("strSetValues");
        	AddedFaultType=(FaultType[])i.getExtras().get("AddedFaultType");
        	AddedSnagType=(JobType[])i.getExtras().get("AddedSnagType");
        	arrJobType=(JobType[])i.getExtras().get("arrJobType");
        	arrFaultType=(FaultType[])i.getExtras().get("arrFaultType");
        	
        	
        	//arrTradeAptAreaDetail=(TradeAptAreaDetail[])i.getExtras().get("arrTradeAptAreaDetail");
        	arrTDDistinctInspectionDescription=(TradeDetails[])i.getExtras().get("arrTDDistinctInspectionDescription");
        	isRotated=(boolean)i.getExtras().getBoolean("isRotated");
        }
        
        if(isAptmt){
        	CurrentAPT=(ApartmentMaster)getIntent().getExtras().get("Apartment");
        	
        }
        else{
        	CurrentSFA=(StdFloorAreas)getIntent().getExtras().get("SFA");
        }
        
        
       
        
        FMDBDatabaseAccess db=new FMDBDatabaseAccess(getApplicationContext());
        if(isExtArea)
        {
        	extArea=db.getExtBuildingDetails(CurrentProject.getID());
        }
        else if(isFloorExtArea)
        {
        	
        }
        else
        {
        	AreaList=db.getApartmentDetails(CurrentAPT);
        }
        
        imgVw1=(ImageButton)findViewById(R.id.row_cell_addDefectPhoto_img1);
        imgVw2=(ImageButton)findViewById(R.id.row_cell_addDefectPhoto_img2);
        imgVw3=(ImageButton)findViewById(R.id.row_cell_addDefectPhoto_img3);
        list=(ListView)findViewById(R.id.android_add_defect_list);
       
        if(AreaList==null || AreaList.length==0)
        {
        	//DownloadProfileImage task=new DownloadProfileImage();
			//task.execute(10);
        	//getApartmentDetailsFromWeb();
        	ContinueProcess();
        }
        else
        {
        	
        	ContinueProcess();
        	
        }
       // Toast.makeText(getApplicationContext(), "Came in OnCreate "+cnt++, Toast.LENGTH_LONG).show();
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
		catch(Exception e)
		{
			Log.d("Exception", ""+e.getMessage());
		}
        
    }
  //@@@@@@@MenuHandlers
    
    public void MenuLegendsClick(View v)
	{
		try
		{
			HideTopMenu();
			menuhandler.MenuLegendsClick();
		}
		catch(Exception e)
		{
			Log.d("Exception",""+e.getMessage());
		}
	}
    public void MenuAboutClick(View v)
	{
		try{
			HideTopMenu();
			menuhandler.MenuAboutusClick();
		}
		catch(Exception e)
		{
			Log.d("Exception",""+e.getMessage());
		}
	}
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
		menuhandler.MenuRoomsheetClick(CurrentFloor);
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
		//menuhandler.MenuAttachClick();
	}
	
	//@@@@@@@MenuHandlers
    @Override
    public Object onRetainNonConfigurationInstance()
    {
        strSetValues=new String[23];
        strSetValues[0]=SelectedArea;
        strSetValues[1]=SelectedTrade;
        strSetValues[2]=SelectedSubTrade;
        strSetValues[3]=SelectedJobDetails;
        strSetValues[4]=""+selectedInspectorIndex;
        strSetValues[5]=ReportedDate;
        strSetValues[6]=ExpectedDate;
        strSetValues[7]=PhotoURl1;
        strSetValues[8]=PhotoURl2;
        strSetValues[9]=PhotoURl3;
        strSetValues[10]=selectedCost;
        strSetValues[11]=costTo;
        strSetValues[12]=snagPriority;
        strSetValues[13]=strFromImg;
        if(f!=null)
        	strSetValues[14]=f.getAbsolutePath().toString();
        strSetValues[15]=""+SelectedEmployeeIndex;
        
        strSetValues[16]=SelectedArea;
        strSetValues[17]=SelectedAreaTypeParent;
        strSetValues[18]=SelectedTradeMasterID;
        strSetValues[19]=SelectedTradeType;
       // strSetValues[20]=SelectedTradeID;
        strSetValues[21]=SelectedInspectionGroup;
        strSetValues[22]=selectedInscpectionDescription;
        
        Intent i=new Intent();
        i.putExtra("strSetValues", strSetValues);
        i.putExtra("AddedSnagType", AddedSnagType);
        i.putExtra("AddedFaultType", AddedFaultType);
        i.putExtra("arrJobType", arrJobType);
        i.putExtra("arrFaultType", arrFaultType);
        
        i.putExtra("strSetValues", strSetValues);
       //i.putExtra("arrTradeAptAreaDetail", arrTradeAptAreaDetail);
        i.putExtra("arrTDDistinctInspectionDescription", arrTDDistinctInspectionDescription);
        i.putExtra("isRotated", true);
        
        
        return i;
    }

   @Override
    public void onResume(){
    	super.onResume();
    	try{
    	//Toast.makeText(getApplicationContext(), "Came in Resume "+cnt++, Toast.LENGTH_LONG).show();
        //else{
        	
        	
       // }
    	SharedPreferences sharedPref = getSharedPreferences("AppDelegate",MODE_PRIVATE);
        Boolean id=sharedPref.getBoolean("isOnline", false);
        isOnline=id;
    	}
		catch(Exception e)
		{
			Log.d("Exception", ""+e.getMessage());
		}
    }
  
    ///////////Date
    protected class DownloadProfileImage extends AsyncTask<Integer , Integer, Void> {
    	ProgressDialog mProgressDialog = new ProgressDialog(AddDefect.this);
    	JSONObject jObject;
        @Override
        protected void onPreExecute() {  
        	try{
        	mProgressDialog.setCancelable(false);
        	mProgressDialog.setMessage("Loading...");
            mProgressDialog.show();
        	}
    		catch(Exception e)
    		{
    			Log.d("Exception", ""+e.getMessage());
    		}
        	
        }      
        @Override
        protected Void doInBackground(Integer... params) {
        	
        	
        	
        	try
    		{
    			try{
    	    		String METHOD_NAME = "GetDataTableFilter";
    	    		String NAMESPACE = "http://tempuri.org/";
    	    		String URL = ""+getResources().getString(R.string.WS_URL).toString()+"SnagReporter_WebS.asmx";//SnagReporter_WCF.svc
    	    		String SOAP_ACTION = "http://tempuri.org/GetDataTableFilter";//
    	    		String res = "";
    	    		try {
    	    		    SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
    	    		    
    	    		    SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
    	    		    envelope.dotNet = true;
    	    		    envelope.setOutputSoapObject(request);
    	    		    
    	    		    request.addProperty("_strTableName","ApartmentDetails");
    	    		    request.addProperty("_strProjectID",CurrentProject.getID());
    	    		    request.addProperty("_strBuildingID",CurrentBuilding.getID());
    	    		    request.addProperty("_strFloorID",CurrentFloor.getID());
    	    		    request.addProperty("_strApartmentID",CurrentAPT.getID());
    	        		//
    	    		    
    	    		    HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
    	    		    androidHttpTransport.call(SOAP_ACTION, envelope);
    	    		    Object resonse=envelope.getResponse();
    	    		    String resultData = resonse.toString();
    	    		    
    	    		    
    	    			
    					jObject = new JSONObject(resultData);
    					
    					//mProgressDialog.dismiss();
    	    		    res = resultData;
    	    		    // 0 is the first object of data
    	    		} catch (Exception e) {
    	    		    res = e.getMessage();
    	    		    Log.d("Error=", ""+e.getMessage());
    	    		    //mProgressDialog.dismiss();
    	    		}
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
        	runOnUiThread(new Runnable() {
			     public void run() {

			//stuff that updates ui
			    	 try{
			    		 if(jObject!=null){
			    		 JSONArray arr = jObject.getJSONArray("Data");
						if(arr!=null){
							for(int i=0;i<arr.length();i++){
								JSONObject geometry = arr.getJSONObject(i);
								parseData(geometry);
								
								//parseFriends(geometry);
							}
						}
			    		 }
			    		 FMDBDatabaseAccess obj=new FMDBDatabaseAccess(AddDefect.this);
			    		 AreaList=obj.getApartmentDetails(CurrentAPT);
			    		 ContinueProcess();
			    	 }
			    	 catch(Exception e){
			    		 Log.d("Error=", ""+e.getMessage());
			    	 }

			    }
			});
            
        }
         
}
    
    
    public void ContinueProcess(){
    	//Toast.makeText(getApplicationContext(), "Came in Continue Process "+cnt++, Toast.LENGTH_LONG).show();
    	try{
    		
    		
    		if(strGetValues!=null)
    		{
    			SelectedArea=strGetValues[0];
    			SelectedTrade=strGetValues[1];
    			SelectedSubTrade=strGetValues[2];
    		    SelectedJobDetails=strGetValues[3];
    		    selectedInspectorIndex=Integer.parseInt(strGetValues[4]);
    		    ReportedDate=strGetValues[5];
    		    ExpectedDate=strGetValues[6];
    		    PhotoURl1=strGetValues[7];
    		    PhotoURl2=strGetValues[8];
    		    PhotoURl3=strGetValues[9];
    		    selectedCost=strGetValues[10];
    		    costTo=strGetValues[11];
    		    snagPriority=strGetValues[12];
    		    SelectedEmployeeIndex=Integer.parseInt(strGetValues[15]);
    		    
    		    
    		    SelectedArea=strGetValues[16];
    			SelectedAreaTypeParent=strGetValues[17];
    			SelectedTradeMasterID=strGetValues[18];
    			SelectedTradeType=strGetValues[19];
    			//SelectedTradeID=strGetValues[20];
    			SelectedInspectionGroup=strGetValues[21];
    			selectedInscpectionDescription=strGetValues[22];
    			
    		}
    		items = new ArrayList<Item>();
    		if(LoginType.equals(getResources().getString(R.string.strInspector).toString()))
    	        
  	       items.add(new Header(null, "ADD SNAG",false,false,true,true));
    		else
    			items.add(new Header(null, "ADD SNAG",false,false,true,false));
  	   
  	     FMDBDatabaseAccess obj=new FMDBDatabaseAccess(getApplicationContext());
  	     
 		//arrJobType=obj.getJobType();
 		
 		if(arrJobType==null || arrJobType.length==0){
 			//getJobTypeFromWeb();
 			arrJobType=obj.getJobType();
 		}
 		
 		if(arrFaultType==null || arrFaultType.length==0){
 			arrFaultType=obj.getFaultType(arrJobType[0].getID());
 		}
 		
  	    if(isExtArea)
  	    {
  	    	if(extArea!=null && extArea.length>0)
  	    		if(SelectedArea==null || SelectedArea.length()==0){
  	    			SelectedArea=CurrentBuilding.getBuildingName();//extArea[0].getBuildingName();
  	    			SelectedAreaID=CurrentBuilding.getID();
  	    			SelectedAreaTypeParent=CurrentBuilding.getBuildingType(); //extArea[0].getBuildingType();
  	    		}
  	    			
  	    }
  	    else if(isFloorExtArea)
  	    {
  	    	if(SelectedArea==null || SelectedArea.length()==0){
	    			SelectedArea=CurrentFloor.getFloor();//extArea[0].getBuildingName();
	    			SelectedAreaID=CurrentFloor.getID();
	    			SelectedAreaTypeParent=CurrentFloor.FloorType; //extArea[0].getBuildingType();
	    		}
  	    }
  	    else
  	    {
  	    	if(AreaList!=null && AreaList.length>0){
   	    	   if(SelectedArea==null || SelectedArea.length()==0){
   	    		   SelectedArea=AreaList[0].getAptAreaName();
   	    		SelectedAreaID=AreaList[0].getID();
   	    		   SelectedAreaTypeParent=AreaList[0].getAptAreaType();
   	    	   }
  	    	}
  	    	
  	    		else{
  	    	    	if(SelectedArea==null || SelectedArea.length()==0){
  	  	    		   SelectedArea=CurrentAPT.getApartmentNo();
  	  	    		SelectedAreaID=CurrentAPT.getID();
  	  	    		   SelectedAreaTypeParent=CurrentAPT.getApartmentNo();
  	  	    	   }
  	    	     }
  	    	
  	    		
  	    }
 		
//  	  if(AreaList==null || AreaList.length==0){
//	     	AreaType="FLOOR";
//	     	
//	     }
//	     else{
//	     	AreaType="APARTMENT";
//	     	
//	     }
  	 
  	if(isExtArea)
  	{
  		AreaType=CurrentBuilding.getBuildingType();
  		AreaTypeID="";
  	}
  	else if(isFloorExtArea)
  	{
  		AreaType=CurrentFloor.FloorType;
  		AreaTypeID="";
  	}
  	else
  	{
  		 String arr[]=obj.getAreaType(SelectedAreaTypeParent);
  		 if(arr!=null)
  		 {
  			 AreaType=arr[0];
  			 AreaTypeID=arr[1];
  		 }	 
  	}
  	 
  	  
  	
  	 if(isExtArea)
  	{
  		arrTradeMaster=obj.getExtTradeMasterTradeAptAreaMaster(CurrentProject.getID(),CurrentBuilding.getID(),CurrentBuilding.getBuildingName());
  	}
  	else if(isFloorExtArea)
  	{
  		arrTradeMaster=obj.getExtTradeMasterTradeAptAreaMasterFloor(CurrentProject.getID(), CurrentBuilding.getID(),CurrentFloor.getID(), CurrentFloor.getFloor());
  	}
  	else
  	{
  		arrTradeMaster=obj.getTradeMasterTradeAptAreaMaster(CurrentProject.getID(), CurrentBuilding.getID(),CurrentFloor.getID(), CurrentAPT.getID(),SelectedAreaTypeParent,SelectedAreaID);
  	}
  	if(arrTradeMaster!=null && arrTradeMaster.length>0){
  		if(SelectedTradeMasterID!=null && SelectedTradeMasterID.length()==0){
  		SelectedTradeMasterID=arrTradeMaster[0].ID;
  		SelectedTradeType=arrTradeMaster[0].TradeType;
  		}
  	}
  	else{
  		SelectedTradeMasterID="";
  		SelectedTradeType="";
  	}
  	if(SelectedTradeMasterID.length()>0)
  		arrTDDistinctTrade=obj.getTradeDetailsByTradeMasterID(SelectedTradeMasterID);
  	else
  		arrTDDistinctTrade=null;
  	
  	if(arrTDDistinctTrade!=null && arrTDDistinctTrade.length>0){
  		if(SelectedInspectionGroup==null || SelectedInspectionGroup.length()==0)
  			SelectedInspectionGroup=arrTDDistinctTrade[0].InspectionGroup;
  	}
  	else{
  		
  		SelectedInspectionGroup="";
  	}
  	
  	if(SelectedInspectionGroup!=null && SelectedInspectionGroup.length()>0)
  	{
  		String strFl="";
  		if(isValidText(SelectedAreaID))
  			strFl=SelectedAreaID;
  		
  		if(isExtArea)
  		{
  			arrTDDistinctInspectionGroup=obj.getTradeDetailsByTradeTypeNewExtArea(SelectedTradeMasterID, CurrentBuilding, strFl, SelectedTradeMasterID);
  		}
  		else if(isFloorExtArea)
  		{
  			arrTDDistinctInspectionGroup=obj.getTradeDetailsByTradeTypeNewFloorArea(SelectedTradeMasterID, CurrentFloor, strFl, SelectedTradeMasterID);
  		}
  		else
  			arrTDDistinctInspectionGroup=obj.getTradeDetailsByTradeTypeNew(SelectedTradeMasterID, CurrentAPT,strFl,SelectedTradeMasterID);//getTradeDetailsByTradeType(SelectedTradeMasterID);//////usd
  	}
  	else
  		arrTDDistinctInspectionGroup=null;
  	//new
  	if(arrTDDistinctInspectionGroup!=null && arrTDDistinctInspectionGroup.length>0)
  		SelectedInspectionGroup=arrTDDistinctInspectionGroup[0];
  	else
  		SelectedInspectionGroup="";
  	//new close
//  	if(arrTDDistinctInspectionGroup!=null && arrTDDistinctInspectionGroup.length>0)
//  		arrTDDistinctInspectionDescription=obj.getTradeDetailsByInspectionGroup(SelectedTradeMasterID, arrTDDistinctInspectionGroup[0]);
  	if(arrTDDistinctInspectionGroup!=null && arrTDDistinctInspectionGroup.length>0){
  		arrTDDistinctInspectionDescription=obj.getTradeDetailsByInspectionGroup(SelectedTradeMasterID, arrTDDistinctInspectionGroup[0]);
  	}
  	else{
  		arrTDDistinctInspectionDescription=null;
  	}
  	if(arrTDDistinctInspectionDescription!=null && arrTDDistinctInspectionDescription.length>0){
  		arrCheckList=new SnagChecklistEntries[arrTDDistinctInspectionDescription.length];
  		for(int i=0;i<arrCheckList.length;i++){
  			arrCheckList[i]=new SnagChecklistEntries();
  			arrCheckList[i].ChecklistID=arrTDDistinctInspectionDescription[i].ID;
  			arrCheckList[i].CheckListGroup=arrTDDistinctInspectionDescription[i].InspectionGroup;
  			arrCheckList[i].CheckListEntry="Not Done";
  			arrCheckList[i].ChecklistDescription=arrTDDistinctInspectionDescription[i].InspectionDescription;
  			
  		}
  	}
  	
  	

	       
	       final Calendar cal = Calendar.getInstance();
	       if(year==0)
	    	   year = cal.get(Calendar.YEAR);
	       if(month==0)
	    	   month = cal.get(Calendar.MONTH);
	       if(day==0)
	    	   day = cal.get(Calendar.DAY_OF_MONTH);
	       if(ReportedDate==null || ReportedDate.length()==0)
	    	   ReportedDate=GetDate();
	       if(ExpectedDate==null || ExpectedDate.length()==0)
	    	   ExpectedDate=GetPlusDate();
	       
	       
	       if(arrEmployee==null || arrEmployee.length==0){
	    	   arrEmployee=obj.getEmployee();
	       }
	       if(arrEmployee!=null && arrEmployee.length>0 && SelectedEmployeeIndex!=-1 )
	    	   SelectedEmployeeIndex=0;
  	       populateData();
  	       
  	       
  	       
  	       
  	       
  	       list.setDivider(getResources().getDrawable(R.color.transparent));
  	       list.setDividerHeight(1);
  	       
  	      adapter = new TwoTextArrayAdapter(this, items);
  	      //if(adapter2!=null)
  	     // {
  	    	//list.setAdapter(adapter2);
  	      //} 
  	     // else
  	      //{
  	    	 list.setAdapter(adapter); 
  	    	 
  	    	 //@@list.setOnScrollListener(this);
  	      //}
  	        
  	        
  	        
  	        list.setOnItemClickListener(new OnItemClickListener() {

  				
  				public void onItemClick(AdapterView<?> arg0, View arg1, int position,
  						long arg3) {
  					// TODO Auto-generated method stub
  					try{
  					if(position!=0 && position!=8 && position!=9 && position!=5 && position!=10){
  						//Toast.makeText(getApplicationContext(), "list position="+position, Toast.LENGTH_LONG).show();;
  						//View v=arg1;//ListItemAddDefect
  						//SelTextInList=(TextView)v.findViewById(R.id.row_cell_addDefect_text);
  						registerForContextMenu(arg0);
  			    		as_index=position-1;
  			    		
  			    		openContextMenu(arg0);
  			    		unregisterForContextMenu(arg0);
  					}
  					else if(position==8){
  						final View v=arg1;
  						TextView t=(TextView)v.findViewById(R.id.row_cell_addDefect_text);
  						String date=t.getText().toString();
  						String arr[]=date.split("-");
  						//month=Integer.parseInt(arr[0].toString());
  						//day=Integer.parseInt(arr[1].toString());
  						//year=Integer.parseInt(arr[2].toString());
  						CurrentDate=8;
  						showDialog(DATE_DIALOG_ID);
  					}
  					else if(position==9){
  						final View v=arg1;
  						TextView t=(TextView)v.findViewById(R.id.row_cell_addDefect_text); 
  						String date=t.getText().toString();
  						String arr[]=date.split("-");
  						//month=Integer.parseInt(arr[0].toString());
  						//day=Integer.parseInt(arr[1].toString());
  						//year=Integer.parseInt(arr[2].toString());
  						CurrentDate=9;
  						showDialog(DATE_DIALOG_ID);
  					}
  					else if(position==10)
  					{
  						AlertDialog.Builder alert = new AlertDialog.Builder(AddDefect.this);

  						alert.setTitle("Cost");
  						
  						final EditText input = new EditText(AddDefect.this);
  						
  						input.setInputType(InputType.TYPE_CLASS_NUMBER);
  						int wantedPosition = 10; // Whatever position you're looking for    
  						int firstPosition = list.getFirstVisiblePosition() - list.getHeaderViewsCount(); // This is the same as child #0
  						int wantedChild = wantedPosition - firstPosition;
  						if (wantedChild < 0 || wantedChild >= list.getChildCount()) {
    						  Log.d("Child Not Available", "");
    						}
  						else{
  	  						final View v=list.getChildAt(wantedChild);
  	  						TextView t=(TextView)v.findViewById(R.id.row_cell_addDefect_text);
  	  					
  	  						input.setText(t.getText().toString());
  	  						alert.setView(input);
  	  						alert.setPositiveButton("Done", new DialogInterface.OnClickListener() {
  	  						public void onClick(DialogInterface dialog, int whichButton) {
  	  							
  	  							try{
  	  						  String value = input.getText().toString();
  	  						  int wantedPosition = 10; // Whatever position you're looking for
  	  							int firstPosition = list.getFirstVisiblePosition() - list.getHeaderViewsCount(); // This is the same as child #0
  	  							int wantedChild = wantedPosition - firstPosition;
  	  							// Say, first visible position is 8, you want position 10, wantedChild will now be 2
  	  							// So that means your view is child #2 in the ViewGroup:
  	  							if (wantedChild < 0 || wantedChild >= list.getChildCount()) {
  	  							  Log.d("Child Not Available", "");
  	  							}
  	  							else{
  	  						  View v2=list.getChildAt(wantedChild);
  	  							TextView t2=(TextView)v2.findViewById(R.id.row_cell_addDefect_text);
  	  						//t2.setInputType(InputType.TYPE_CLASS_NUMBER);
  	  							
  	  							t2.setText(""+value);
  	  							selectedCost=value;
  	  							ListItemAddDefect itemi=(ListItemAddDefect) items.get(wantedPosition);
  	  							items.set(wantedPosition,new ListItemAddDefect(null, "Cost",""+selectedCost,itemi.isDisabled));
  	  							adapter.notifyDataSetChanged();
  	  							}
  	  						  // Do something with value!
  	  						}
  	  						catch(Exception e)
  	  						{
  	  							Log.d("Exception", ""+e.getMessage());
  	  						}
  	  						  }
  	  						});

  	  						alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
  	  						  public void onClick(DialogInterface dialog, int whichButton) {
  	  						    // Canceled.
  	  						  }
  	  						});

  	  						alert.show();
  	  						}

  						
  					}
  					else if(position==5){
  						AlertDialog.Builder alert = new AlertDialog.Builder(AddDefect.this);

  						alert.setTitle("Comments");
  						//alert.setMessage("Message");

  						// Set an EditText view to get user input 
  						final EditText input = new EditText(AddDefect.this);
  						int wantedPosition = 5; // Whatever position you're looking for    
  						int firstPosition = list.getFirstVisiblePosition() - list.getHeaderViewsCount(); // This is the same as child #0
  						int wantedChild = wantedPosition - firstPosition;
  						// Say, first visible position is 8, you want position 10, wantedChild will now be 2
  						// So that means your view is child #2 in the ViewGroup:
  						if (wantedChild < 0 || wantedChild >= list.getChildCount()) {
  						  Log.d("Child Not Available", "");
  						}
  						else{
  						final View v=list.getChildAt(wantedChild);
  						TextView t=(TextView)v.findViewById(R.id.row_cell_addDefect_text);
  						input.setText(t.getText().toString());
  						alert.setView(input);
  						alert.setPositiveButton("Done", new DialogInterface.OnClickListener() {
  						public void onClick(DialogInterface dialog, int whichButton) {
  							
  						try{	
  						  String value = input.getText().toString();
  						  int wantedPosition = 5; // Whatever position you're looking for
  							int firstPosition = list.getFirstVisiblePosition() - list.getHeaderViewsCount(); // This is the same as child #0
  							int wantedChild = wantedPosition - firstPosition;
  							// Say, first visible position is 8, you want position 10, wantedChild will now be 2
  							// So that means your view is child #2 in the ViewGroup:
  							if (wantedChild < 0 || wantedChild >= list.getChildCount()) {
  							  Log.d("Child Not Available", "");
  							}
  							else{
  						  View v2=list.getChildAt(wantedChild);
  							TextView t2=(TextView)v2.findViewById(R.id.row_cell_addDefect_text);
  							
  							
  							t2.setText(""+value);
  							SelectedJobDetails=value;
  							ListItemAddDefect itemi=(ListItemAddDefect) items.get(wantedPosition);
  							items.set(wantedPosition,new ListItemAddDefect(null, "Comments",""+SelectedJobDetails,itemi.isDisabled));
  							adapter.notifyDataSetChanged();
  							}
  						  // Do something with value!
  						}
  						catch(Exception e)
  						{
  							Log.d("Exception", ""+e.getMessage());
  						}
  						  }
  						});

  						alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
  						  public void onClick(DialogInterface dialog, int whichButton) {
  						    // Canceled.
  						  }
  						});

  						alert.show();
  						}
  					}
  						//
  				}
  				catch(Exception e)
  				{
  					Log.d("Exception", ""+e.getMessage());
  				}
  				}
  			});
  	        
  	        reloadList();
    	}
    	catch(Exception e){
    		Log.d("Exception",e.getMessage());
    		
    	}
    }
    public void reloadList(){
    	
    }
    
    private Bitmap decodeFile(String url){//This is the whole url of the image
		File f=new File(url);
		Display display = getWindowManager().getDefaultDisplay();
	    Bitmap b = null;
	    //int IMAGE_MAX_SIZE=200;
	    int maxWidth=200;
	    int maxHeight=200;
	    try {
	        //Decode image size
	    	
	    	//@@@@@
	    	int angle = 0;
	    	 Matrix mat = new Matrix();
	        try {
	            File f1 = new File(url);
	            ExifInterface exif = new ExifInterface(f1.getPath());
	            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

	            

	            if (orientation == ExifInterface.ORIENTATION_ROTATE_90) {
	                angle = 90;
	            } 
	            else if (orientation == ExifInterface.ORIENTATION_ROTATE_180) {
	                angle = 180;
	            } 
	            else if (orientation == ExifInterface.ORIENTATION_ROTATE_270) {
	                angle = 270;
	            }
	            
	           
	            if(angle>0){
	            	mat.postRotate(angle);
	            	if(angle==90 || angle==270){
	            	int temp=maxHeight;
	            	maxHeight=maxWidth;
	            	maxWidth=temp;
	            	}
	            	
	            	
	            }
	                
	        }
	        catch (IOException e) {
	            Log.w("TAG", "-- Error in setting image");
	        }   
	        catch(OutOfMemoryError oom) {
	            Log.w("TAG", "-- OOM Error in setting image");
	        }
	        
	        //@@@@@
	    	
	    	
	        BitmapFactory.Options o = new BitmapFactory.Options();
	        o.inJustDecodeBounds = true;
	        
	        

	        FileInputStream fis = new FileInputStream(f);
	        BitmapFactory.decodeStream(fis, null, o);
	        
	        fis.close();
	        


	        
	        int scale = 1;
	        if (o.outHeight > maxHeight && o.outWidth > maxWidth) {
	        	if(o.outHeight>o.outWidth){
	        		scale = (int)Math.pow(2, (int) Math.round(Math.log(maxHeight / (double) Math.max(o.outHeight, o.outWidth)) / Math.log(0.5)));
	        	}
	        	else{
	        		scale = (int)Math.pow(2, (int) Math.round(Math.log(maxWidth / (double) Math.max(o.outHeight, o.outWidth)) / Math.log(0.5)));
	        	}
	        }
	        else if (o.outHeight > maxHeight || o.outWidth > maxWidth) {
	        	if(o.outHeight>maxHeight)
	        		scale = (int)Math.pow(2, (int) Math.round(Math.log(maxHeight / (double) Math.max(o.outHeight, o.outWidth)) / Math.log(0.5)));
	        	else
	        		scale = (int)Math.pow(2, (int) Math.round(Math.log(maxWidth / (double) Math.max(o.outHeight, o.outWidth)) / Math.log(0.5)));
	        }

	        //Decode with inSampleSize
	        BitmapFactory.Options o2 = new BitmapFactory.Options();
	        o2.inSampleSize = scale;
	        
	        
	        fis = new FileInputStream(f);
	        b = BitmapFactory.decodeStream(fis, null, o2);
	        try{
	        float wd=b.getWidth();
	        float ht=b.getHeight();

	        
	        int space=44;
	        if(scale>1){
	        if((maxWidth>wd  && maxHeight>ht) || (maxWidth==wd && ht>maxHeight) || (maxHeight==ht && wd>maxWidth)){
	        	float tempWD,tempHT;
	        	tempWD=wd;
	        	tempHT=ht;
	        	if(tempWD>tempHT){
	        		float diff=maxWidth-tempWD;
	        		float pc=diff/tempWD;
	        		tempWD=maxWidth;
	        		float cc=ht*pc;
	        		tempHT+=cc;
	        	}
	        	else{
	        		float diff=maxHeight-space-tempHT;
	        		float pc=diff/tempHT;
	        		tempHT=maxHeight-space;
	        		float cc=wd*pc;
	        		tempWD+=cc;
	        	}
	        	
	        	if(tempHT>maxHeight-space){
	        		float diff=maxHeight-space-tempHT;
	        		float pc=diff/tempHT;
	        		tempHT=maxHeight-space;
	        		float cc=wd*pc;
	        		tempWD+=cc;
	        	}
	        	else if(tempWD>maxWidth){
	        		float diff=maxWidth-tempWD;
	        		float pc=diff/tempWD;
	        		tempWD=maxWidth;
	        		float cc=ht*pc;
	        		tempHT+=cc;
	        	}
	        wd=tempWD;
	        ht=tempHT;
	       
	        	
	        
	        	b=Bitmap.createScaledBitmap(b, (int)wd,(int) ht, true);
	        	
	        	
	        
	        }
	        
	        }
	        }
	        catch(Exception e){
	        	b = BitmapFactory.decodeStream(fis, null, o2);
	        }
	        
	        
	        if(angle>0){
        		b= Bitmap.createBitmap(b, 0, 0, b.getWidth(), b.getHeight(), mat, true);
        	} 
	        
	        
	       // Log.d("", "");
	        //byte[] arr=getBytesFromBitmap(b);
	        //int i=arr.length;
	       // double d=b.getRowBytes()*b.getHeight();
	        fis.close();
	        //Toast.makeText(getApplicationContext(), "Resized To="+b.getHeight()+"X"+b.getWidth(), Toast.LENGTH_LONG).show();
	    } catch (IOException e) {
	    }
	    return b;
	}
    
    
    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
        case DATE_DIALOG_ID:
        	DatePickerDialog dd;
        	if(CurrentDate==7)
        	{
        		dd=new DatePickerDialog(this, mDateSetListener, year, month, day);
        		
        	}
        	else{
        		dd=new DatePickerDialog(this, mDateSetListener, year2, month2, day2);
        	}
        	return dd;
        }
        return null;
    }



    // the call back received when the user "sets" the date in the dialog
    private DatePickerDialog.OnDateSetListener mDateSetListener =
        new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int myear, int monthOfYear, int dayOfMonth) {
            	if(CurrentDate==8){
            		year = myear;
                    month = monthOfYear;
                    day = dayOfMonth;
            	}
            	else{
            		year2 = myear;
                    month2 = monthOfYear;
                    day2 = dayOfMonth;
            	}
                
                updateDate();
            }
    };
    private DatePickerDialog.OnDateSetListener mDateSetListener2 =
        new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int myear, int monthOfYear, int dayOfMonth) {
//            	if(CurrentDate==6){
//            		year = myear;
//                    month = monthOfYear;
//                    day = dayOfMonth;
//            	}
//            	else{
            		year2 = myear;
                    month2 = monthOfYear;
                    day2 = dayOfMonth;
            	//}
                
                updateDate();
            }
    };
   private String GetDate(){
//	   String[] months={"01","02","03","04","05","06","07","08","09","10","11","12"};
//	   StringBuilder date=new StringBuilder().append(day).append('-')
//				.append(months[month]).append('-').append(year);
//		String dt=date.toString();
	   String dt = "";  // Start date
	   SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
	   Calendar c = Calendar.getInstance();
	  
	   c.setTime(new Date());
	  // c.add(Calendar.DATE, 2);
	   
	   dt = sdf.format(c.getTime());
		return dt;
   }
   private String GetPlusDate(){
//	   Calendar calendar = Calendar.getInstance();
//	   Date now = new Date();
//	   calendar.setTime(now);
//	   calendar.add(Calendar.DATE,2);
//	   Date date=calendar.getTime();
//	   

	   
	   String dt = "";  // Start date
	   SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
	   Calendar c = Calendar.getInstance();
	  
	   c.setTime(new Date());
	   c.add(Calendar.DATE, 2);
	   int dtt=c.get(Calendar.DATE);
	   day2=dtt;
	   int mon=c.get(Calendar.MONTH);
	   month2=mon;
	   int yr=c.get(Calendar.YEAR);
	   year2=yr;
	   dt = sdf.format(c.getTime());
	   
	   
//	   String[] months={"01","02","03","04","05","06","07","08","09","10","11","12"};
//	   StringBuilder datestr=new StringBuilder().append(date.getDate()).append('-')
//				.append(date.getMonth()).append('-').append(date.getYear());
//		 dt=datestr.toString();
		return dt;
   }
    private void updateDate() {
		String[] months={"01","02","03","04","05","06","07","08","09","10","11","12"};
		int wantedPosition=8;
		if(CurrentDate==8){
			wantedPosition = 8;
			
		}
		else if(CurrentDate==9)
			wantedPosition=9;
		// Whatever position you're looking for
		int firstPosition = list.getFirstVisiblePosition() - list.getHeaderViewsCount(); // This is the same as child #0
		int wantedChild = wantedPosition - firstPosition;
		// Say, first visible position is 8, you want position 10, wantedChild will now be 2
		// So that means your view is child #2 in the ViewGroup:
		if (wantedChild < 0 || wantedChild >= list.getChildCount()) {
		  Log.d("Child Not Available", "");
		}
		else{
		final View v=list.getChildAt(wantedChild);
		TextView t=(TextView)v.findViewById(R.id.row_cell_addDefect_text);
		
		if(CurrentDate==8){
			String strDT=""+day+"-"+(month+1)+"-"+year;
			SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
			Calendar c = Calendar.getInstance();
			try {
				c.setTime(sdf.parse(strDT));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			strDT = sdf.format(c.getTime());
		t.setText(strDT);
		ListItemAddDefect itemi=(ListItemAddDefect)items.get(wantedPosition);
		items.set(wantedPosition,new ListItemAddDefect(null, "Reported Date",""+strDT,itemi.isDisabled));
		ReportedDate=t.getText().toString();
		adapter.notifyDataSetChanged();
		}
		else{
			String strDT=""+day2+"-"+(month2+1)+"-"+year2;
			SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
			Calendar c = Calendar.getInstance();
			try {
				c.setTime(sdf.parse(strDT));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			strDT = sdf.format(c.getTime());
			t.setText(strDT);
			ExpectedDate=t.getText().toString();
			
			ListItemAddDefect itemi=(ListItemAddDefect)items.get(wantedPosition);
			items.set(wantedPosition,new ListItemAddDefect(null, "Expected Inspection Date",""+strDT,itemi.isDisabled));
			adapter.notifyDataSetChanged();
		}
		
			
		}
		//dateDisplay.setTextColor(Color.BLACK);
		
		//udate.concat((String) t.getText());	
	}
//////////////Date
    public static class TwoTextArrayAdapter extends ArrayAdapter<Item> {
    	private LayoutInflater mInflater;

        public enum RowType {
            LIST_ITEM, HEADER_ITEM
        }

        private List<Item> items;

        public TwoTextArrayAdapter(Context context, List<Item> items) {
            super(context, 0, items);
            this.items = items;
            mInflater = LayoutInflater.from(context);
        }

        @Override
        public int getViewTypeCount() {
            return RowType.values().length;

        }

        @Override
        public int getItemViewType(int position) {
            return items.get(position).getViewType();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
        	
        		//View v=items.get(position).getView(mInflater, convertView);
        		
        		return items.get(position).getView(mInflater, null);
        	
        }
    }
    
    
    public void populateData()
    {
    	try{
    		if(isExtArea)
    		{
    			if(extArea!=null && extArea.length>0)
    				items.add(new ListItemAddDefect(null, "Area",""+SelectedArea,false));
    			else
    				items.add(new ListItemAddDefect(null, "Area",""+CurrentBuilding.getBuildingName(),false));
    				
    			
    		}
    		else if(isFloorExtArea)
    		{
    			if(SelectedArea!=null && SelectedArea.length()>0)
    				items.add(new ListItemAddDefect(null, "Area",""+SelectedArea,false));
    			else
    				items.add(new ListItemAddDefect(null, "Area",""+CurrentFloor.getFloor(),false));
    		}
    		else
    		{
    			//if(AreaList!=null && AreaList.length>0)
        			items.add(new ListItemAddDefect(null, "Area",""+SelectedArea,false));
        		//else
        		//	items.add(new ListItemAddDefect(null, "Area",""+CurrentAPT.getApartmentNo(),false));
        		
        		
    		}
    		items.add(new ListItemAddDefect(null, "Trades",""+SelectedTradeType,false));//SelectedJobType
    	   if(arrTDDistinctInspectionGroup!=null && arrTDDistinctInspectionGroup.length>0)//
    			items.add(new ListItemAddDefect(null, "Sub Trade",""+SelectedInspectionGroup,false));
    		else 
    			items.add(new ListItemAddDefect(null, "Sub Trade","",false));
    	   
    	   if(selectedInscpectionDescription==null || selectedInscpectionDescription.length()==0)
    		   if(arrTDDistinctInspectionDescription!=null && arrTDDistinctInspectionDescription.length>0)
    			   selectedInscpectionDescription=arrTDDistinctInspectionDescription[0].InspectionDescription;
    		   else
    			   selectedInscpectionDescription="";
//    	   if(arrTDDistinctInspectionDescription!=null && arrTDDistinctInspectionDescription.length>0)
//    		   items.add(new ListItemAddDefect(null, "Trade Description",arrTDDistinctInspectionDescription[0].InspectionDescription,false));
//    	   else
    		   items.add(new ListItemAddDefect(null, "Trade Description",selectedInscpectionDescription,false));
    	   
    		items.add(new ListItemAddDefect(null, "Comments",""+SelectedJobDetails,false));
    		
    		if(SelectedEmployeeIndex==-1)
    		{
    			items.add(new ListItemAddDefect(null, "Allocated To","",false));
    		}
    		else
    		{
    			items.add(new ListItemAddDefect(null, "Allocated To",""+arrEmployee[SelectedEmployeeIndex].getEmpName()+" "+arrEmployee[SelectedEmployeeIndex].getEmpLastName(),false));
    		}
    		if(arrInspetor.length>0){
    			if(selectedInspectorIndex!=-1)
    			{
    				
    				items.add(new ListItemAddDefect(null, "Inspector",""+arrInspetor[selectedInspectorIndex].getFirstName()+" "+arrInspetor[selectedInspectorIndex].getLastName(),false));
    			}
    			else
    			{
    				items.add(new ListItemAddDefect(null, "Inspector","",false));
    			}
    				//items.add(new ListItemAddDefect(null, "Inspector",""+SelectedInspector));
    		}
    		else
    			items.add(new ListItemAddDefect(null, "Inspector","-No Inspector found-",false));
    		
    		
    		
    		items.add(new ListItemAddDefect(null, "Reported Date",""+ReportedDate,false));
    		items.add(new ListItemAddDefect(null, "Expected Inspection Date",""+ExpectedDate,false));
    		items.add(new ListItemAddDefect(null, "Cost",""+selectedCost,false));
    		items.add(new ListItemAddDefect(null, "CostTo",""+costTo,false));
    		items.add(new ListItemAddDefect(null, "SnagPriority",""+snagPriority,false));
    		
    		ImageButton btn1=null,btn2=null,btn3=null;
    		if(PhotoURl1!=null && PhotoURl1.length()>0){
    			String FilePath1=Environment.getExternalStorageDirectory()+File.separator+"SnagReporter/Pictures/"+PhotoURl1+".jpg";
    			File fooo = new File(FilePath1);
    			if(fooo.exists()){
    			Bitmap Img1 = decodeFile(FilePath1);
    			btn1=new ImageButton(this);
    			btn1.setImageBitmap(Img1);
    			//imgVw1.setBackgroundDrawable(btnI.getDrawable());
    	   		isImageSetFor1=true;
    			}
    			else
    			{
//    				DownloadImagesInBackGround task=new DownloadImagesInBackGround();
//    				task.imageUrl=PhotoURl1;
//    				task.tag=1;
//    				task.execute(10);
    				btn1=null;
    			}
    			
    		}
    		
    		if(PhotoURl2!=null && PhotoURl2.length()>0){
    			String FilePath2=Environment.getExternalStorageDirectory()+File.separator+"SnagReporter/Pictures/"+PhotoURl2+".jpg";
    			File fooo = new File(FilePath2);
    			if(fooo.exists())
    			{
    			Bitmap Img2 = decodeFile(FilePath2);
    			 btn2=new ImageButton(this);
    			btn2.setImageBitmap(Img2);
    			//imgVw2.setBackgroundDrawable(btnI.getDrawable());
    	   		isImageSetFor2=true;
    			}
    			else
    			{
    				btn2=null;
//    				DownloadImagesInBackGround task=new DownloadImagesInBackGround();
//    				task.imageUrl=PhotoURl2;
//    				task.tag=2;
//    				task.execute(10);
    			}
    			
    		}
    		
    		if(PhotoURl3!=null && PhotoURl3.length()>0){
    			String FilePath2=Environment.getExternalStorageDirectory()+File.separator+"SnagReporter/Pictures/"+PhotoURl3+".jpg";
    			File fooo = new File(FilePath2);
    			if(fooo.exists())
    			{
    			Bitmap Img2 = decodeFile(FilePath2);
    			 btn3=new ImageButton(this);
    			btn3.setImageBitmap(Img2);
    			//imgVw3.setBackgroundDrawable(btnI.getDrawable());
    	   		isImageSetFor3=true;
    			}
    			else
    			{
    				btn3=null;
//    				DownloadImagesInBackGround task=new DownloadImagesInBackGround();
//    				task.imageUrl=PhotoURl3;
//    				task.tag=3;
//    				task.execute(10);
    			}
    			
    		}
    		
    		
    		items.add(new ListItemAddDefectPhoto(null, "Snag Photos",btn1,btn2,btn3,1,false,"","","",false));
    		
    		
    		
    		
    		//obj=null;
    		
            
    	}
    	catch(Exception e){
    	 Log.d("Error=", ""+e.getMessage());
    	}
    }
    
    
    public void BackClick(View v){
    	finish();
    }
    
    
    
    
    public void Photo1Clicked(View v){
    	try{
    		registerForContextMenu(v);
    		as_index=12;
    		strFromImg="img1";
    		
    		if(!isImageSetFor1)
    		{
    			as_index_selected=0;
    		}
    		else
    		{
    			as_index_selected=1;
    			
    		}
    			openContextMenu(v);
    			unregisterForContextMenu(v);
    	}
    	catch(Exception e){
    		Log.d("Error Photo1Clicked=",""+e.getMessage());
    	}
    }
    public void Photo2Clicked(View v){
    	try{
    		registerForContextMenu(v);
    		as_index=12;
    		strFromImg="img2";
    		
    		if(!isImageSetFor2)
    		{
    			as_index_selected=0;
    		}
    		else
    		{
    			as_index_selected=1;
    			
    		}
    			openContextMenu(v);
    			unregisterForContextMenu(v);
    	}
    	catch(Exception e){
    		Log.d("Error Photo2Clicked=",""+e.getMessage());
    	}
    }
    public void Photo3Clicked(View v){
    	try{
    		registerForContextMenu(v);
    		as_index=12;
    		strFromImg="img3";
    		
    		if(!isImageSetFor3)
    		{
    			as_index_selected=0;
    		}
    		else
    		{
    			as_index_selected=1;
    			
    		}
    			openContextMenu(v);
    			unregisterForContextMenu(v);
    	}
    	catch(Exception e){
    		Log.d("Error Photo3Clicked=",""+e.getMessage());
    	}
    }
    
    @Override  
	   public void onCreateContextMenu(ContextMenu menu, View v,ContextMenuInfo menuInfo)
 {  
	super.onCreateContextMenu(menu, v, menuInfo);  
	
	try{
	if(as_index==12){
	    menu.setHeaderTitle("Choose Photo");  
	    if(as_index_selected==0)
	    {
	    	menu.add(0, v.getId(), 0, "Choose from Library");
	    	menu.add(0, v.getId(), 0, "Take with Camera");
	    	
	    }
	    else
	    {
	    	 menu.add(0, v.getId(), 0, "Remove Photo"); 
	    	 menu.add(0, v.getId(), 0, "View Image");
	    	menu.add(0, v.getId(), 0, "Choose from Library");  
	  	    menu.add(0, v.getId(), 0, "Take with Camera");
	    }
	}
	else if(as_index==0)
	{
		menu.setHeaderTitle("Select Area");
		if(isExtArea)
		{
//			for(int i=0;i<extArea.length;i++)
//			{
//				menu.add(0, v.getId(), 0, ""+extArea[i].getBuildingName());
//			}
		}
		else
		{
			for(int i=0;i<AreaList.length;i++)
			{
				menu.add(0, v.getId(), 0, ""+AreaList[i].getAptAreaName());
			}
		}
		
	}
	else if(as_index==1){
		if(arrTradeMaster!=null){
		menu.setHeaderTitle("Select Trades");
//		for(int i=0;i<arrJobType.length;i++){
//			menu.add(0, v.getId(), 0, ""+arrJobType[i].getJobType());
//		}
		
		for(int i=0;i<arrTradeMaster.length;i++){
			menu.add(0, v.getId(), 0, ""+arrTradeMaster[i].TradeType);
		}
		}
		//menu.add(0, v.getId(), 0, "Add More");
	}
	else if(as_index==2){
		if(arrTDDistinctInspectionGroup!=null){
		menu.setHeaderTitle("Select InspectionGroup");
		for(int i=0;i<arrTDDistinctInspectionGroup.length;i++){
			//if(SelectedTradeType.equalsIgnoreCase(arrTDDistinctInspectionGroup[i]))
				menu.add(0, v.getId(), 0, ""+arrTDDistinctInspectionGroup[i]);
		}
			
		}
		
		/*
		if(arrFaultType!=null && arrFaultType.length>0){
		menu.setHeaderTitle("Select FaultType");
		if(arrFaultType!=null && arrFaultType.length>0){
		for(int i=0;i<arrFaultType.length;i++){
			menu.add(0, v.getId(), 0, ""+arrFaultType[i].getFaultType());
		}
		
		}
		else{
			
		}
		menu.add(0, v.getId(), 0, "Add More");
		}
		else{


			AlertDialog.Builder alert = new AlertDialog.Builder(AddDefect.this);
			alert.setTitle("Add Fault Type");
			final EditText input = new EditText(AddDefect.this);
			alert.setView(input);
			alert.setPositiveButton("Done", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int whichButton) {
				  String value = input.getText().toString().trim();
				  if(value.length()>0){
					  
					  int wantedPosition =3; // Whatever position you're looking for
					  int firstPosition = list.getFirstVisiblePosition() - list.getHeaderViewsCount(); // This is the same as child #0
					  int wantedChild = wantedPosition - firstPosition;
					// Say, first visible position is 8, you want position 10, wantedChild will now be 2
					// So that means your view is child #2 in the ViewGroup:
					  if (wantedChild < 0 || wantedChild >= list.getChildCount()) {
						  Log.d("Child Not Available", "");
					  }
					  else{
						  View v2=list.getChildAt(wantedChild);
						  TextView t2=(TextView)v2.findViewById(R.id.row_cell_addDefect_text);
						  //t2.setInputType(InputType.TYPE_CLASS_NUMBER);
					
						  t2.setText(""+value);
						  UUID id=UUID.randomUUID();
						  FaultType nft=new FaultType();
						  nft.setID(id.toString());
						  nft.setFaultType(""+value);
						  nft.setFaultDetails("");
						  
						  nft.setIsSyncedToWeb(false);
						  int index=0;
						  for(int i=0;i<arrJobType.length;i++){
							  if(SelectedJobType.equalsIgnoreCase(arrJobType[i].getJobType())){
								  index=i;
								  break;
							  }
						  }
						  nft.setJobTypeID(arrJobType[index].getID());
						  nft.setJobType(""+SelectedJobType);
						  
						  
						  arrFaultType=new FaultType[1];
						  arrFaultType[0]=nft;
						  
						  if(AddedFaultType==null || AddedFaultType.length==0){
							  AddedFaultType=new FaultType[1];
							  AddedFaultType[0]=nft;
						  }
						  else{
							  FaultType[] temp=AddedFaultType;
							  AddedFaultType=new FaultType[temp.length+1];
							  for(int i=0;i<temp.length;i++){
								  AddedFaultType[i]=temp[i];
								  if(i==temp.length-1){
									  AddedFaultType[i+1]=nft;
								  }
							  }
						  }
						  SelectedFaultType=""+value;
					}
				  
				  }
				}
				});

				alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
				  public void onClick(DialogInterface dialog, int whichButton) {
				    // Canceled.
				  }
				});

				alert.show();
				

			
		
	
		}*/
	}
	else if(as_index==3){
		menu.setHeaderTitle("Select InspectionDescription");
		if(arrTDDistinctInspectionDescription!=null)
		for(int i=0;i<arrTDDistinctInspectionDescription.length;i++){
			//if(SelectedTradeType.equalsIgnoreCase(arrTDDistinctInspectionGroup[i]))
				menu.add(0, v.getId(), 0, ""+arrTDDistinctInspectionDescription[i].InspectionDescription);
			
			
		}
	}
	else if(as_index==5){//Allocated To
		if(arrEmployee!=null && arrEmployee.length>0){
		menu.setHeaderTitle("Allocate To");
		for(int i=0;i<arrEmployee.length;i++){
			menu.add(0, v.getId(), 0, ""+arrEmployee[i].getEmpName()+" "+arrEmployee[i].getEmpLastName());
		}
		}
	}
	else if(as_index==6){//Inspector
		menu.setHeaderTitle("Select Inspector");
		for(int i=0;i<arrInspetor.length;i++){
			menu.add(0, v.getId(), 0, ""+arrInspetor[0].getFirstName()+" "+arrInspetor[0].getLastName());
		}
	}
	else if(as_index==10)
	{
		menu.setHeaderTitle("Select CostTo");
		menu.add(0, v.getId(), 0,"Client");
		menu.add(0, v.getId(), 0,"Builder");
		menu.add(0, v.getId(), 0,"Contractor");
	}
	else if(as_index==11)
	{
		menu.setHeaderTitle("Select SnagPriority");
		menu.add(0, v.getId(), 0,"1");
		menu.add(0, v.getId(), 0,"2");
		menu.add(0, v.getId(), 0,"3");
		menu.add(0, v.getId(), 0,"4");
		menu.add(0, v.getId(), 0,"5");
	}
	}
	catch(Exception e)
	{
		Log.d("Exception",""+e.getMessage());
	}
	    
	}  
@Override
	public boolean onContextItemSelected(MenuItem item)
	{
	if(as_index==12){
	 if(item.getTitle()=="Choose from Library")
	 {
		 Intent intent=new Intent();
		 intent.setType("image/*");
		 intent.setAction(Intent.ACTION_GET_CONTENT);
		 startActivityForResult(intent,0);
		 
		 return true;
	 }
	 else if(item.getTitle()=="Take with Camera"){
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
	 else if(item.getTitle()=="Remove Photo"){
		 int wantedPosition=13;
		 if(strFromImg.equalsIgnoreCase("img1")){
			
			 RemovedPhotoURL=PhotoURl1;
			 imgVw1.setBackgroundDrawable(getResources().getDrawable(R.drawable.image_add));
			 PhotoURl1="";
			 isImageSetFor1=false;
			 
			 ImageButton btnI=new ImageButton(this);
			 btnI.setImageResource(R.drawable.image_add);
			 ListItemAddDefectPhoto itemi=(ListItemAddDefectPhoto)items.get(wantedPosition);
			 itemi.url1=btnI;
			 items.set(wantedPosition,itemi);
			 
		 }
		 else if(strFromImg.equalsIgnoreCase("img2")){
			 RemovedPhotoURL=PhotoURl2;
			 imgVw2.setBackgroundDrawable(getResources().getDrawable(R.drawable.image_add));
			 PhotoURl2="";
			 isImageSetFor2=false;
			 
			 ImageButton btnI=new ImageButton(this);
			 btnI.setImageResource(R.drawable.image_add);
			 ListItemAddDefectPhoto itemi=(ListItemAddDefectPhoto)items.get(wantedPosition);
			 itemi.url2=btnI;
			 items.set(wantedPosition,itemi);
		 }
		 else if(strFromImg.equalsIgnoreCase("img3")){
			 RemovedPhotoURL=PhotoURl3;
			 imgVw3.setBackgroundDrawable(getResources().getDrawable(R.drawable.image_add));
			 PhotoURl3="";
			 isImageSetFor3=false;
			 
			 ImageButton btnI=new ImageButton(this);
			 btnI.setImageResource(R.drawable.image_add);
			 ListItemAddDefectPhoto itemi=(ListItemAddDefectPhoto)items.get(wantedPosition);
			 itemi.url3=btnI;
			 items.set(wantedPosition,itemi);
		 }
		 adapter.notifyDataSetChanged();
		 return true;
	 }
	 else if(item.getTitle()=="View Image"){
		 
		 Intent i=new Intent(AddDefect.this,com.snagreporter.ViewImagePage.class);
		 if(strFromImg.equalsIgnoreCase("img1")){
			 i.putExtra("PhotoURL", PhotoURl1);
		 }
		 else if(strFromImg.equalsIgnoreCase("img2")){
			 i.putExtra("PhotoURL", PhotoURl2);
		 }
		 else if(strFromImg.equalsIgnoreCase("img3")){
			 i.putExtra("PhotoURL", PhotoURl3);
		 }
		 
		 startActivity(i);
		 return true;
	 }
	}
	else if(as_index==0){
		int wantedPosition =1; // Whatever position you're looking for
		int firstPosition = list.getFirstVisiblePosition() - list.getHeaderViewsCount(); // This is the same as child #0
		int wantedChild = wantedPosition - firstPosition;
		// Say, first visible position is 8, you want position 10, wantedChild will now be 2
		// So that means your view is child #2 in the ViewGroup:
		if (wantedChild < 0 || wantedChild >= list.getChildCount()) {
		  Log.d("Child Not Available", "");
		}
		else{
		final View v=list.getChildAt(wantedChild);
		TextView t=(TextView)v.findViewById(R.id.row_cell_addDefect_text);
		t.setText(""+item.getTitle().toString());
		SelectedArea=item.getTitle().toString();
		ListItemAddDefect itemi=(ListItemAddDefect)items.get(wantedPosition);
		items.set(wantedPosition,new ListItemAddDefect(null, "Area",""+SelectedArea,itemi.isDisabled));
		
		if(isExtArea)
  	    {
//  	    	if(extArea!=null && extArea.length>0)
//  	    		if(SelectedArea==null || SelectedArea.length()==0){
//  	    			SelectedArea=CurrentBuilding.getBuildingName();//extArea[0].getBuildingName();
//  	    			SelectedAreaID=CurrentBuilding.getID();
//  	    			SelectedAreaTypeParent=CurrentBuilding.getBuildingType(); //extArea[0].getBuildingType();
//  	    		}
  	    }
  	    else
  	    {
  	    	if(AreaList!=null && AreaList.length>0){
   	    	   //if(SelectedArea==null || SelectedArea.length()==0){
  	    		for(int i=0;i<AreaList.length;i++)
  	    			if(SelectedArea.equalsIgnoreCase(""+AreaList[i].getAptAreaName())){
  	    				SelectedAreaID=AreaList[i].getID();
  	   	    		   	SelectedAreaTypeParent=AreaList[i].getAptAreaType();
  	   	    		   	break;
  	    			}
   	    		   
   	    		   
   	    	  // }
  	    	}
  	    	
  	    }
		
		
		//@@@@@@@
		FMDBDatabaseAccess obj=new FMDBDatabaseAccess(AddDefect.this);
		if(!isExtArea && !isFloorExtArea)
	  	{
	  		arrTradeMaster=obj.getTradeMasterTradeAptAreaMaster(CurrentProject.getID(), CurrentBuilding.getID(),CurrentFloor.getID(), CurrentAPT.getID(),SelectedAreaTypeParent,SelectedAreaID);
	  	}
	  	else if(isExtArea)
	  	{
	  		arrTradeMaster=obj.getExtTradeMasterTradeAptAreaMaster(CurrentProject.getID(),CurrentBuilding.getID(),CurrentBuilding.getBuildingName());
	  	}
	  	else
	  	{
	  		arrTradeMaster=obj.getExtTradeMasterTradeAptAreaMasterFloor(CurrentProject.getID(), CurrentBuilding.getID(),CurrentFloor.getID(), CurrentFloor.getFloor());
	  	}
	  	if(arrTradeMaster!=null && arrTradeMaster.length>0){
	  		//if(SelectedTradeMasterID!=null && SelectedTradeMasterID.length()==0){
	  		SelectedTradeMasterID=arrTradeMaster[0].ID;
	  		SelectedTradeType=arrTradeMaster[0].TradeType;
	  		//}
	  	}
	  	else{
	  		SelectedTradeMasterID="";
	  		SelectedTradeType="";
	  	}
	  	if(SelectedTradeMasterID.length()>0)
	  		arrTDDistinctTrade=obj.getTradeDetailsByTradeMasterID(SelectedTradeMasterID);
	  	else
	  		arrTDDistinctTrade=null;
	  	
	  	if(arrTDDistinctTrade!=null && arrTDDistinctTrade.length>0){
	  		//if(SelectedInspectionGroup!=null && SelectedInspectionGroup.length()==0)
	  			SelectedInspectionGroup=arrTDDistinctTrade[0].InspectionGroup;
	  	}
	  	else{
	  		
	  		SelectedInspectionGroup="";
	  	}
	  	
	  	if(SelectedInspectionGroup!=null && SelectedInspectionGroup.length()>0)
	  	{
	  		String strFl="";
	  		if(isValidText(SelectedAreaID))
	  			strFl=SelectedAreaID;
	  		if(isExtArea)
	  		{
	  			arrTDDistinctInspectionGroup=obj.getTradeDetailsByTradeTypeNewExtArea(SelectedTradeMasterID, CurrentBuilding, strFl, SelectedTradeMasterID);
	  		}
	  		else if(isFloorExtArea)
	  		{
	  			arrTDDistinctInspectionGroup=obj.getTradeDetailsByTradeTypeNewFloorArea(SelectedTradeMasterID, CurrentFloor, strFl, SelectedTradeMasterID);
	  		}
	  		else
	  		{
	  			arrTDDistinctInspectionGroup=obj.getTradeDetailsByTradeTypeNew(SelectedTradeMasterID, CurrentAPT,strFl,SelectedTradeMasterID);//getTradeDetailsByTradeType(SelectedTradeMasterID);//getTradeDetailsByTradeTypeNew(SelectedTradeMasterID, CurrentAPT);////usd
	  		}
	  		
	  	}
	  	else
	  		arrTDDistinctInspectionGroup=null;
	  	
	  //new
	  	if(arrTDDistinctInspectionGroup!=null && arrTDDistinctInspectionGroup.length>0)
	  		SelectedInspectionGroup=arrTDDistinctInspectionGroup[0];
	  	else
	  		SelectedInspectionGroup="";
	  	//new close
	  	
//	  	if(arrTDDistinctInspectionGroup!=null && arrTDDistinctInspectionGroup.length>0)
//	  		arrTDDistinctInspectionDescription=obj.getTradeDetailsByInspectionGroup(SelectedTradeMasterID, arrTDDistinctInspectionGroup[0]);
	  	if(arrTDDistinctInspectionGroup!=null && arrTDDistinctInspectionGroup.length>0){
	  		arrTDDistinctInspectionDescription=obj.getTradeDetailsByInspectionGroup(SelectedTradeMasterID, arrTDDistinctInspectionGroup[0]);
	  	}
	  	else{
	  		arrTDDistinctInspectionDescription=null;
	  	}
	  	if(arrTDDistinctInspectionDescription!=null && arrTDDistinctInspectionDescription.length>0){
	  		arrCheckList=new SnagChecklistEntries[arrTDDistinctInspectionDescription.length];
	  		for(int i=0;i<arrCheckList.length;i++){
	  			arrCheckList[i]=new SnagChecklistEntries();
	  			arrCheckList[i].ChecklistID=arrTDDistinctInspectionDescription[i].ID;
	  			arrCheckList[i].CheckListGroup=arrTDDistinctInspectionDescription[i].InspectionGroup;
	  			arrCheckList[i].CheckListEntry="Not Done";
	  			arrCheckList[i].ChecklistDescription=arrTDDistinctInspectionDescription[i].InspectionDescription;
	  			
	  		}
	  	}
	  	else{
	  		arrCheckList=null;
	  	}
	  	
	  	wantedPosition =2; // Whatever position you're looking for
		 firstPosition = list.getFirstVisiblePosition() - list.getHeaderViewsCount(); // This is the same as child #0
		 wantedChild = wantedPosition - firstPosition;
		if (wantedChild < 0 || wantedChild >= list.getChildCount()) {
		  Log.d("Child Not Available", "");
		}
		else{
			final View v2=list.getChildAt(wantedChild);
			TextView t2=(TextView)v2.findViewById(R.id.row_cell_addDefect_text);
			t2.setText(""+SelectedTradeType);
			itemi=(ListItemAddDefect)items.get(wantedPosition);
			items.set(wantedPosition,new ListItemAddDefect(null, "Trades",""+SelectedTradeType,itemi.isDisabled));
		}
	  	
	  	wantedPosition =3; // Whatever position you're looking for
		 firstPosition = list.getFirstVisiblePosition() - list.getHeaderViewsCount(); // This is the same as child #0
		 wantedChild = wantedPosition - firstPosition;
		if (wantedChild < 0 || wantedChild >= list.getChildCount()) {
		  Log.d("Child Not Available", "");
		}
		else{
			final View v2=list.getChildAt(wantedChild);
			TextView t2=(TextView)v2.findViewById(R.id.row_cell_addDefect_text);
			t2.setText(""+SelectedInspectionGroup);
			itemi=(ListItemAddDefect)items.get(wantedPosition);
			items.set(wantedPosition,new ListItemAddDefect(null, "Sub Trade",""+SelectedInspectionGroup,itemi.isDisabled));
		}
		
		
		
		if(arrTDDistinctInspectionDescription!=null && arrTDDistinctInspectionDescription.length>0){
	  		selectedInscpectionDescription=arrTDDistinctInspectionDescription[0].InspectionDescription;
	  	}
	  	else{
	  		selectedInscpectionDescription="";
	  	}
		 
		
		wantedPosition =4; // Whatever position you're looking for
		 firstPosition = list.getFirstVisiblePosition() - list.getHeaderViewsCount(); // This is the same as child #0
		 wantedChild = wantedPosition - firstPosition;
		// Say, first visible position is 8, you want position 10, wantedChild will now be 2
		// So that means your view is child #2 in the ViewGroup:
		if (wantedChild < 0 || wantedChild >= list.getChildCount()) {
		  Log.d("Child Not Available", "");
		}
		else{
			
		final View v2=list.getChildAt(wantedChild);
		TextView t2=(TextView)v2.findViewById(R.id.row_cell_addDefect_text);
		t2.setText(""+selectedInscpectionDescription);
		itemi=(ListItemAddDefect)items.get(wantedPosition);
		items.set(wantedPosition,new ListItemAddDefect(null, "Trade Description",selectedInscpectionDescription,itemi.isDisabled));
		}
		//@@@@@@@
		
		}
		adapter.notifyDataSetChanged();
	}
	else if(as_index==10)
	{
		int wantedPosition =11; // Whatever position you're looking for
		int firstPosition = list.getFirstVisiblePosition() - list.getHeaderViewsCount(); // This is the same as child #0
		int wantedChild = wantedPosition - firstPosition;
		// Say, first visible position is 8, you want position 10, wantedChild will now be 2
		// So that means your view is child #2 in the ViewGroup:
		if (wantedChild < 0 || wantedChild >= list.getChildCount()) {
		  Log.d("Child Not Available", "");
		}
		else{
		final View v=list.getChildAt(wantedChild);
		TextView t=(TextView)v.findViewById(R.id.row_cell_addDefect_text);
		t.setText(""+item.getTitle().toString());
		costTo=item.getTitle().toString();
		ListItemAddDefect itemi=(ListItemAddDefect)items.get(wantedPosition);
		items.set(wantedPosition,new ListItemAddDefect(null, "CostTo",""+costTo,itemi.isDisabled));
		adapter.notifyDataSetChanged();
		}
	}
	else if(as_index==11)
	{
		int wantedPosition =12; // Whatever position you're looking for
		int firstPosition = list.getFirstVisiblePosition() - list.getHeaderViewsCount(); // This is the same as child #0
		int wantedChild = wantedPosition - firstPosition;
		// Say, first visible position is 8, you want position 10, wantedChild will now be 2
		// So that means your view is child #2 in the ViewGroup:
		if (wantedChild < 0 || wantedChild >= list.getChildCount()) {
		  Log.d("Child Not Available", "");
		}
		else{
		final View v=list.getChildAt(wantedChild);
		TextView t=(TextView)v.findViewById(R.id.row_cell_addDefect_text);
		t.setText(""+item.getTitle().toString());
		snagPriority=item.getTitle().toString();
		ListItemAddDefect itemi=(ListItemAddDefect)items.get(wantedPosition);
		items.set(wantedPosition,new ListItemAddDefect(null, "SnagPriority",""+snagPriority,itemi.isDisabled));
		adapter.notifyDataSetChanged();
		}
	}
	else if(as_index==1){
		
		int wantedPosition =2; // Whatever position you're looking for
		int firstPosition = list.getFirstVisiblePosition() - list.getHeaderViewsCount(); // This is the same as child #0
		int wantedChild = wantedPosition - firstPosition;
		if (wantedChild < 0 || wantedChild >= list.getChildCount()) {
		  Log.d("Child Not Available", "");
		}
		else{
			
		final View v=list.getChildAt(wantedChild);
		TextView t=(TextView)v.findViewById(R.id.row_cell_addDefect_text);
		t.setText(""+item.getTitle().toString());
		ListItemAddDefect itemi=(ListItemAddDefect)items.get(wantedPosition);
		items.set(wantedPosition,new ListItemAddDefect(null, "Trades",""+item.getTitle().toString(),itemi.isDisabled));
		String SelectedTradeMaster=item.getTitle().toString();
		for(int i=0;i<arrTradeMaster.length;i++){
			String temp=arrTradeMaster[i].TradeType;
			if(SelectedTradeMaster.equalsIgnoreCase(temp)){
				SelectedTradeMasterID=arrTradeMaster[i].ID;
				SelectedTradeType=arrTradeMaster[i].TradeType;
				break;
			}
		}
		
		FMDBDatabaseAccess db=new FMDBDatabaseAccess(AddDefect.this);
		
		arrTDDistinctTrade=db.getTradeDetailsByTradeMasterID(SelectedTradeMasterID);
	  	
	  	if(arrTDDistinctTrade!=null && arrTDDistinctTrade.length>0){
	  		
	  		SelectedInspectionGroup=arrTDDistinctTrade[0].InspectionGroup;
	  	}
	  	else{
	  		SelectedInspectionGroup="";
	  	}
		
	  	if(SelectedInspectionGroup!=null && SelectedInspectionGroup.length()>0)
	  	{
	  		String strFl="";
	  		if(isValidText(SelectedAreaID))
	  			strFl=SelectedAreaID;
	  		
	  		if(isExtArea)
	  		{
	  			arrTDDistinctInspectionGroup=db.getTradeDetailsByTradeTypeNewExtArea(SelectedTradeMasterID, CurrentBuilding, strFl, SelectedTradeMasterID);
	  		}
	  		else if(isFloorExtArea)
	  		{
	  			arrTDDistinctInspectionGroup=db.getTradeDetailsByTradeTypeNewFloorArea(SelectedTradeMasterID, CurrentFloor, strFl, SelectedTradeMasterID);
	  		}
	  		else
	  			arrTDDistinctInspectionGroup=db.getTradeDetailsByTradeTypeNew(SelectedTradeMasterID, CurrentAPT,strFl,SelectedTradeMasterID);//getTradeDetailsByTradeType(SelectedTradeMasterID);//getTradeDetailsByTradeTypeNew(SelectedTradeMaster,CurrentAPT);////usd
	  	}
	  	else
	  		arrTDDistinctInspectionGroup=null;
	  	
	  //new
	  	if(arrTDDistinctInspectionGroup!=null && arrTDDistinctInspectionGroup.length>0)
	  		SelectedInspectionGroup=arrTDDistinctInspectionGroup[0];
	  	else
	  		SelectedInspectionGroup="";
	  	//new close
		
	  	if(arrTDDistinctInspectionGroup!=null && arrTDDistinctInspectionGroup.length>0)
	  		arrTDDistinctInspectionDescription=db.getTradeDetailsByInspectionGroup(SelectedTradeMasterID, arrTDDistinctInspectionGroup[0]);
	  	else
	  		arrTDDistinctInspectionDescription=null;
	  	
	  	if(arrTDDistinctInspectionDescription!=null && arrTDDistinctInspectionDescription.length>0){
	  		arrCheckList=new SnagChecklistEntries[arrTDDistinctInspectionDescription.length];
	  		for(int i=0;i<arrCheckList.length;i++){
	  			arrCheckList[i]=new SnagChecklistEntries();
	  			arrCheckList[i].ChecklistID=arrTDDistinctInspectionDescription[i].ID;
	  			arrCheckList[i].CheckListGroup=arrTDDistinctInspectionDescription[i].InspectionGroup;
	  			arrCheckList[i].CheckListEntry="Not Done";
	  			arrCheckList[i].ChecklistDescription=arrTDDistinctInspectionDescription[i].InspectionDescription;
	  			
	  		}
	  	}
	  	else{
	  		arrCheckList=null;
	  	}
	
			 wantedPosition =3; // Whatever position you're looking for
			 firstPosition = list.getFirstVisiblePosition() - list.getHeaderViewsCount(); // This is the same as child #0
			 wantedChild = wantedPosition - firstPosition;
			if (wantedChild < 0 || wantedChild >= list.getChildCount()) {
			  Log.d("Child Not Available", "");
			}
			else{
				final View v2=list.getChildAt(wantedChild);
				TextView t2=(TextView)v2.findViewById(R.id.row_cell_addDefect_text);
				t2.setText(""+SelectedInspectionGroup);
				itemi=(ListItemAddDefect)items.get(wantedPosition);
				items.set(wantedPosition,new ListItemAddDefect(null, "Sub Trade",""+SelectedInspectionGroup,itemi.isDisabled));
			}
			
			
			
			if(arrTDDistinctInspectionDescription!=null && arrTDDistinctInspectionDescription.length>0){
		  		selectedInscpectionDescription=arrTDDistinctInspectionDescription[0].InspectionDescription;
		  	}
		  	else{
		  		selectedInscpectionDescription="";
		  	}
			 
			
			wantedPosition =4; // Whatever position you're looking for
			 firstPosition = list.getFirstVisiblePosition() - list.getHeaderViewsCount(); // This is the same as child #0
			 wantedChild = wantedPosition - firstPosition;
			// Say, first visible position is 8, you want position 10, wantedChild will now be 2
			// So that means your view is child #2 in the ViewGroup:
			if (wantedChild < 0 || wantedChild >= list.getChildCount()) {
			  Log.d("Child Not Available", "");
			}
			else{
				
			final View v2=list.getChildAt(wantedChild);
			TextView t2=(TextView)v2.findViewById(R.id.row_cell_addDefect_text);
			t2.setText(""+selectedInscpectionDescription);
			itemi=(ListItemAddDefect)items.get(wantedPosition);
			items.set(wantedPosition,new ListItemAddDefect(null, "Trade Description",selectedInscpectionDescription,itemi.isDisabled));
			}
			adapter.notifyDataSetChanged();
	}
		
		
		
		
		

	}
	else if(as_index==2){

		
		



		//boolean isPreviosInspected=false;
	int wantedPosition =3; // Whatever position you're looking for
	int firstPosition = list.getFirstVisiblePosition() - list.getHeaderViewsCount(); // This is the same as child #0
	int wantedChild = wantedPosition - firstPosition;
	// Say, first visible position is 8, you want position 10, wantedChild will now be 2
	// So that means your view is child #2 in the ViewGroup:
	if (wantedChild < 0 || wantedChild >= list.getChildCount()) {
	  Log.d("Child Not Available", "");
	}
	else{
		
	final View v=list.getChildAt(wantedChild);
	TextView t=(TextView)v.findViewById(R.id.row_cell_addDefect_text);
	t.setText(""+item.getTitle().toString());
	SelectedInspectionGroup=item.getTitle().toString();
	
	ListItemAddDefect itemi=(ListItemAddDefect)items.get(wantedPosition);
	items.set(wantedPosition,new ListItemAddDefect(null, "Sub Trade",""+SelectedInspectionGroup,itemi.isDisabled));
	
	
	FMDBDatabaseAccess db=new FMDBDatabaseAccess(AddDefect.this);
	
	
  	if(arrTDDistinctInspectionGroup!=null && arrTDDistinctInspectionGroup.length>0)
  		arrTDDistinctInspectionDescription=db.getTradeDetailsByInspectionGroup(SelectedTradeMasterID, SelectedInspectionGroup);
  	
  	if(arrTDDistinctInspectionDescription!=null && arrTDDistinctInspectionDescription.length>0){
  		arrCheckList=new SnagChecklistEntries[arrTDDistinctInspectionDescription.length];
  		for(int i=0;i<arrCheckList.length;i++){
  			arrCheckList[i]=new SnagChecklistEntries();
  			arrCheckList[i].ChecklistID=arrTDDistinctInspectionDescription[i].ID;
  			arrCheckList[i].CheckListGroup=arrTDDistinctInspectionDescription[i].InspectionGroup;
  			arrCheckList[i].CheckListEntry="Not Done";
  			arrCheckList[i].ChecklistDescription=arrTDDistinctInspectionDescription[i].InspectionDescription;
  			
  		}
  	}
  	
  	if(arrTDDistinctInspectionDescription!=null && arrTDDistinctInspectionDescription.length>0){
  		selectedInscpectionDescription=arrTDDistinctInspectionDescription[0].InspectionDescription;
  	}
  	else{
  		selectedInscpectionDescription="";
  	}
	 wantedPosition =4; // Whatever position you're looking for
	 firstPosition = list.getFirstVisiblePosition() - list.getHeaderViewsCount(); // This is the same as child #0
	 wantedChild = wantedPosition - firstPosition;
	// Say, first visible position is 8, you want position 10, wantedChild will now be 2
	// So that means your view is child #2 in the ViewGroup:
	if (wantedChild < 0 || wantedChild >= list.getChildCount()) {
	  Log.d("Child Not Available", "");
	}
	else{
		
	final View v2=list.getChildAt(wantedChild);
	TextView t2=(TextView)v2.findViewById(R.id.row_cell_addDefect_text);
	t2.setText(""+selectedInscpectionDescription);
	itemi=(ListItemAddDefect)items.get(wantedPosition);
	items.set(wantedPosition,new ListItemAddDefect(null, "Trade Description",selectedInscpectionDescription,itemi.isDisabled));
	}
  	
  	//@@
  	if(arrCheckList!=null && arrCheckList.length>0){
  		Intent in=new Intent(AddDefect.this,com.snagreporter.CheckListDescription.class);
  		in.putExtra("Count", arrCheckList.length);
  		for(int i=0;i<arrCheckList.length;i++)
  			in.putExtra("CheckList"+i, arrCheckList[i]);
  		startActivityForResult(in, 12345);
  		return true;
  	}
  	
adapter.notifyDataSetChanged();

}




	
	
		
	}
	else if(as_index==3)
	{
		int wantedPosition =4; // Whatever position you're looking for
		int firstPosition = list.getFirstVisiblePosition() - list.getHeaderViewsCount(); // This is the same as child #0
		int wantedChild = wantedPosition - firstPosition;
		// Say, first visible position is 8, you want position 10, wantedChild will now be 2
		// So that means your view is child #2 in the ViewGroup:
		if (wantedChild < 0 || wantedChild >= list.getChildCount()) {
		  Log.d("Child Not Available", "");
		}
		else{
			
		final View v=list.getChildAt(wantedChild);
		TextView t=(TextView)v.findViewById(R.id.row_cell_addDefect_text);
		t.setText(""+item.getTitle().toString());
		selectedInscpectionDescription=""+item.getTitle().toString();
		ListItemAddDefect itemi=(ListItemAddDefect)items.get(wantedPosition);
		items.set(wantedPosition,new ListItemAddDefect(null, "Trade Description",selectedInscpectionDescription,itemi.isDisabled));
		adapter.notifyDataSetChanged();
		}
	}
	else if(as_index==5)
	{
		int wantedPosition =6; // Whatever position you're looking for
		int firstPosition = list.getFirstVisiblePosition() - list.getHeaderViewsCount(); // This is the same as child #0
		int wantedChild = wantedPosition - firstPosition;
		// Say, first visible position is 8, you want position 10, wantedChild will now be 2
		// So that means your view is child #2 in the ViewGroup:
		if (wantedChild < 0 || wantedChild >= list.getChildCount()) {
		  Log.d("Child Not Available", "");
		}
		else{
		final View v=list.getChildAt(wantedChild);
		TextView t=(TextView)v.findViewById(R.id.row_cell_addDefect_text);
		t.setText(""+item.getTitle().toString());
		ListItemAddDefect itemi=(ListItemAddDefect)items.get(wantedPosition);
		items.set(wantedPosition,new ListItemAddDefect(null, "Allocated To",""+item.getTitle().toString(),itemi.isDisabled));
		for(int i=0;i<arrEmployee.length;i++){
			if(t.getText().toString().equalsIgnoreCase(""+arrEmployee[i].getEmpName()+" "+arrEmployee[i].getEmpLastName())){
				SelectedEmployeeIndex=i;
				break;
			}
				
		}
		adapter.notifyDataSetChanged();
		}
	}
	else if(as_index==6){
		int wantedPosition =7; // Whatever position you're looking for
		int firstPosition = list.getFirstVisiblePosition() - list.getHeaderViewsCount(); // This is the same as child #0
		int wantedChild = wantedPosition - firstPosition;
		// Say, first visible position is 8, you want position 10, wantedChild will now be 2
		// So that means your view is child #2 in the ViewGroup:
		if (wantedChild < 0 || wantedChild >= list.getChildCount()) {
		  Log.d("Child Not Available", "");
		}
		else{
		final View v=list.getChildAt(wantedChild);
		TextView t=(TextView)v.findViewById(R.id.row_cell_addDefect_text);
		t.setText(""+item.getTitle().toString());
		ListItemAddDefect itemi=(ListItemAddDefect)items.get(wantedPosition);
		items.set(wantedPosition,new ListItemAddDefect(null, "Inspector",""+item.getTitle().toString(),itemi.isDisabled));
		for(int i=0;i<arrInspetor.length;i++){
			if(t.getText().toString().equalsIgnoreCase(""+arrInspetor[i].getFirstName()+" "+arrInspetor[i].getLastName())){
				selectedInspectorIndex=i;
				break;
			}
				
		}
		adapter.notifyDataSetChanged();
		}
	}
	 
	
	 return true;
	}
	
protected class SyncJobtype extends AsyncTask<Integer , Integer, Void> {
	JobType objJob=null;
	boolean isAlldone=false;
    @Override
    protected void onPreExecute() {
    	try{
    	FMDBDatabaseAccess db=new FMDBDatabaseAccess(AddDefect.this);
    	objJob=db.getJobTypeNotSynced();
    	if(objJob==null){
    		isAlldone=true;
    	}
    	}
		catch(Exception e)
		{
			Log.d("Exception", ""+e.getMessage());
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
    	FMDBDatabaseAccess db=new FMDBDatabaseAccess(AddDefect.this);
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
			Log.d("Exception", ""+e.getMessage());
		}
    	
    	
        
    }
     
}
protected class SyncFaulttype extends AsyncTask<Integer , Integer, Void> {
	FaultType objFlt=null;
	boolean isAlldone=false;
    @Override
    protected void onPreExecute() { 
    	try{
    	FMDBDatabaseAccess db=new FMDBDatabaseAccess(AddDefect.this);
    	objFlt=db.getFaultTypeNotSynced();
    	if(objFlt==null){
    		isAlldone=true;
    	}
    	}
		catch(Exception e)
		{
			Log.d("Exception", ""+e.getMessage());
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
    	FMDBDatabaseAccess db=new FMDBDatabaseAccess(AddDefect.this);
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
			Log.d("Exception", ""+e.getMessage());
		}
        
    }
     
}

protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) 
{
	 super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
	 try{
		 
//		 if(requestCode==101 && resultCode==101 && imageReturnedIntent!=null){
// 	  		String FilePath=imageReturnedIntent.getExtras().getString("FilePath");
// 	  		String FileName=imageReturnedIntent.getExtras().getString("FileName");
// 	  		menuhandler.SaveAttachment(CurrentProject.getID(),CurrentBuilding.getID(), CurrentFloor.getID(), CurrentAPT.getID(), curr, "", "", "", FileName, FilePath);
// 	  		//Toast.makeText(ProjectListPage.this, "File123="+FilePath, Toast.LENGTH_LONG).show();
// 	  	 }
		 
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
               
            //BtnYourImg = BitmapFactory.decodeFile(filePath);
            
            if(strFromImg==null || strFromImg.length()==0){
            	if(strGetValues!=null)
            		strFromImg=strGetValues[13];
            }
          
            Intent i=new Intent(this,com.snagreporter.PhotoView.class);
            i.putExtra("filePath", filePath);
            //i.putExtra("BitmapImage", BtnYourImg);
           i.putExtra("strFromImg", strFromImg);
           if(strFromImg.equalsIgnoreCase("img1") && PhotoURl1!=null && PhotoURl1.length()>0){
        	   i.putExtra("PhotoUrl", PhotoURl1);
           }
           else if(strFromImg.equalsIgnoreCase("img2") && PhotoURl2!=null && PhotoURl1.length()>0){
        	   i.putExtra("PhotoUrl", PhotoURl2);
           }
           else if(strFromImg.equalsIgnoreCase("img3") && PhotoURl3!=null && PhotoURl1.length()>0){
        	   i.putExtra("PhotoUrl", PhotoURl3);
           }
           else if(RemovedPhotoURL!=null && RemovedPhotoURL.length()>0){
        	   i.putExtra("PhotoUrl", RemovedPhotoURL);
           }
           
          
           startActivityForResult(i, 2);
           break;
           
        }
    case 2:
    {
   	 
   	 if(resultCode==10 && imageReturnedIntent!=null)
   	 {
   	 strFromImgvw=imageReturnedIntent.getExtras().getString("strFromImgvw");
   	 strFilePath=imageReturnedIntent.getExtras().getString("strFilePath");
   	 String retUUID=imageReturnedIntent.getExtras().getString("UUID");
   	//BtnImageBmp=(Bitmap)getIntent().getParcelableExtra("btnBitmap");
   	 BtnImageBmp=decodeFile(strFilePath);
   	 if(strFromImgvw.equalsIgnoreCase("img1"))
   	 {
   		int wantedPosition = 13;
   		 //int wantedPosition = 8; // Whatever position you're looking for
		int firstPosition = list.getFirstVisiblePosition() - list.getHeaderViewsCount(); // This is the same as child #0
		int wantedChild = wantedPosition - firstPosition;
		// Say, first visible position is 8, you want position 10, wantedChild will now be 2
		// So that means your view is child #2 in the ViewGroup:
		PhotoURl1=retUUID;
		isImageSetFor1=true;
		if (wantedChild < 0 || wantedChild >= list.getChildCount()) {
		  Log.d("Child Not Available", "");
		}
		else{
			View v=list.getChildAt(wantedChild);
			
			ImageButton btnI=new ImageButton(this);
			btnI.setImageBitmap(BtnImageBmp);
			imgVw1=(ImageButton)v.findViewById(R.id.row_cell_addDefectPhoto_img1);
			imgVw1.setBackgroundDrawable(btnI.getDrawable());
			//imgVw1.setBackgroundColor(getResources().getColor(R.color.transparent));
			//imgVw1.setImageBitmap(BtnImageBmp);
			ListItemAddDefectPhoto itemi=(ListItemAddDefectPhoto)items.get(wantedPosition);
			itemi.url1=btnI;
			items.set(wantedPosition,itemi);
			adapter.notifyDataSetChanged();
		}
		
   	 }
   	 else if(strFromImgvw.equalsIgnoreCase("img2"))
   	 {
   		int wantedPosition = 13;
   		 //int wantedPosition = 8; // Whatever position you're looking for
		int firstPosition = list.getFirstVisiblePosition() - list.getHeaderViewsCount(); // This is the same as child #0
		int wantedChild = wantedPosition - firstPosition;
		// Say, first visible position is 8, you want position 10, wantedChild will now be 2
		// So that means your view is child #2 in the ViewGroup:
		PhotoURl2=retUUID;
		isImageSetFor2=true;
		if (wantedChild < 0 || wantedChild >= list.getChildCount()) {
		  Log.d("Child Not Available", "");
		}
		else{
			View v=list.getChildAt(wantedChild);
   		
   		ImageButton btnI=new ImageButton(this);
		btnI.setImageBitmap(BtnImageBmp);
		imgVw2=(ImageButton)v.findViewById(R.id.row_cell_addDefectPhoto_img2);
   		 imgVw2.setBackgroundDrawable(btnI.getDrawable());
		//imgVw2.setBackgroundColor(getResources().getColor(R.color.transparent));
		//imgVw2.setImageBitmap(BtnImageBmp);
   		ListItemAddDefectPhoto itemi=(ListItemAddDefectPhoto)items.get(wantedPosition);
		itemi.url2=btnI;
		items.set(wantedPosition,itemi);
		adapter.notifyDataSetChanged();
		}
   	 }
   	 else if(strFromImgvw.equalsIgnoreCase("img3"))
   	 {
   		int wantedPosition = 13;
   		 //int wantedPosition = 8; // Whatever position you're looking for
		int firstPosition = list.getFirstVisiblePosition() - list.getHeaderViewsCount(); // This is the same as child #0
		int wantedChild = wantedPosition - firstPosition;
		// Say, first visible position is 8, you want position 10, wantedChild will now be 2
		// So that means your view is child #2 in the ViewGroup:
		PhotoURl3=retUUID;
		isImageSetFor3=true;
		if (wantedChild < 0 || wantedChild >= list.getChildCount()) {
		  Log.d("Child Not Available", "");
		}
		else{
			View v=list.getChildAt(wantedChild);
   		
   		ImageButton btnI=new ImageButton(this);
		btnI.setImageBitmap(BtnImageBmp);
		imgVw3=(ImageButton)v.findViewById(R.id.row_cell_addDefectPhoto_img3);
   		 imgVw3.setBackgroundDrawable(btnI.getDrawable());
		//imgVw2.setBackgroundColor(getResources().getColor(R.color.transparent));
   		//imgVw3.setImageBitmap(BtnImageBmp);
   		ListItemAddDefectPhoto itemi=(ListItemAddDefectPhoto)items.get(wantedPosition);
		itemi.url3=btnI;
		items.set(wantedPosition,itemi);
		adapter.notifyDataSetChanged();
		}
   	 }
   	 //adapter.notifyDataSetChanged();
   	 //Handler handler = new Handler();
   	// handler.postDelayed(timedTask, 1000);
   	 //Log.d("strFromImgvw",""+strFromImgvw);
   	 }
   	 break;
    }
    case CAMERA_PIC_REQUEST:
    {
    	if(resultCode!=0){
    	if(f==null){
            if(cameraFile!=null)
                f = new File(cameraFile);
            else
                Log.e("check", "camera file object null line no 279");
        }else
            Log.e("check", f.getAbsolutePath());
    	
    	if(f==null){
    		if(strGetValues!=null){
    			strFromImg=strGetValues[13];
    			f = new File(strGetValues[14]);
    		}
    	}
    	
   	 String PhotoURL="";
   	if(strFromImg.equalsIgnoreCase("img1") && PhotoURl1!=null && PhotoURl1.length()>0){
   		PhotoURL=PhotoURl1;
    }
    else if(strFromImg.equalsIgnoreCase("img2") && PhotoURl2!=null && PhotoURl1.length()>0){
    	PhotoURL=PhotoURl2;
    }
    else if(strFromImg.equalsIgnoreCase("img3") && PhotoURl3!=null && PhotoURl1.length()>0){
    	PhotoURL=PhotoURl3;
    }	
    else if(RemovedPhotoURL!=null && RemovedPhotoURL.length()>0){
    	PhotoURL=RemovedPhotoURL;
    }
   
	   strFilePath=f.getAbsolutePath();
	Log.d("file path", ""+strFilePath);
	 try 
     {

         
     } 
	 
     catch (Exception e) 
     {
        Log.d("Exception btnSaveClick", e.getMessage());
     }
   	 //if(mBitmap!=null){
   	 Intent i=new Intent(this,com.snagreporter.PhotoView.class);
   	i.putExtra("filePath", strFilePath);
        i.putExtra("PhotoUrl", PhotoURL);
        //i.putExtra("BitmapImage", mBitmap);
        i.putExtra("strFromImg", strFromImg);
        startActivityForResult(i, 2);
   	 //}

    	}
  /* // if(imageReturnedIntent.getExtras().get("data")!=null){	
   	 mBitmap = (Bitmap) imageReturnedIntent.getExtras().get("data");
   	
   	 String PhotoURL="";
   	 try{
   	if(strFromImg.equalsIgnoreCase("img1") && PhotoURl1!=null && PhotoURl1.length()>0){
   		PhotoURL=PhotoURl1;
    }
    else if(strFromImg.equalsIgnoreCase("img2") && PhotoURl2!=null && PhotoURl1.length()>0){
    	PhotoURL=PhotoURl2;
    }
    else if(strFromImg.equalsIgnoreCase("img3") && PhotoURl3!=null && PhotoURl1.length()>0){
    	PhotoURL=PhotoURl3;
    }	
    else if(RemovedPhotoURL!=null && RemovedPhotoURL.length()>0){
    	PhotoURL=RemovedPhotoURL;
    }
   	 }
   	 catch(Exception e){
   		PhotoURL="";
   	 }
   	
   	
   String	FileName="";
	if(PhotoURL!=null && PhotoURL.length()>0){
		FileName=PhotoURL;
	}
	else{
		UUID uniqueKey = UUID.randomUUID();
		FileName=uniqueKey.toString();
	}
	File file=new File(Environment.getExternalStorageDirectory()+File.separator+"SnagReporter/Pictures/"+FileName+".jpg");
	strFilePath=file.toString();
	Log.d("file path", ""+strFilePath);
	 try 
     {

		 Bitmap bitmap;
			
			bitmap = Bitmap.createBitmap(mBitmap);
		 OutputStream outStream = null;
		 outStream = new FileOutputStream(file);
		 
		 bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outStream);
 	     outStream.flush();
 	     outStream.close();
         
     } 
	 
     catch (Exception e) 
     {
        Log.d("Exception btnSaveClick", e.getMessage());
     }
   	 
   	 Intent i=new Intent(this,com.snagreporter.PhotoView.class);
   	i.putExtra("filePath", strFilePath);
        i.putExtra("PhotoUrl", PhotoURL);
        //i.putExtra("BitmapImage", mBitmap);
        i.putExtra("strFromImg", strFromImg);
        startActivityForResult(i, 2);
    */
    	break;
    	}
    case 12345:
    	try{
    		if(resultCode==RESULT_OK && imageReturnedIntent!=null)
    		{
    			 int count=(int )imageReturnedIntent.getExtras().getInt("Count");
    		       arrCheckList=new SnagChecklistEntries[count];
    		       //arrCheckList=(SnagChecklistEntries[])imageReturnedIntent.getExtras().get("CheckList");
    		       for(int i=0;i<count;i++){
    		    	   arrCheckList[i]=new SnagChecklistEntries();
    		    	   arrCheckList[i]=(SnagChecklistEntries)imageReturnedIntent.getExtras().get("CheckList"+i);
    		       }
    		       
    		       
    		       Log.d("Exception", "");
    		}
    	}
    	catch (Exception e) {
			Log.d("Exception", ""+e.getMessage());
		}
    	break;
    
    //}
    
    
    
    }
	 if(resultCode==10001){
			setResult(10001);
			finish();
		}
	 
	 if(resultCode==10002){
			//setResult(10001);
			finish();
		}
//	 if(requestCode==12345 && resultCode==12345){
//		 if(imageReturnedIntent!=null){
//			 arrCheckList=(SnagChecklistEntries[])imageReturnedIntent.getExtras().get("CheckList");
//		 }
//	 }
	 }
	 
	 
	 catch(Exception e){
		 Log.d("Error=", ""+e.getMessage());
		
	 }
}

private Runnable timedTask = new Runnable(){

	  
	  public void run() {
	   // TODO Auto-generated method stub
		synchronized(this){
			try{
			//adapter.notifyDataSetChanged();
			//reloadList();
			if(strFromImgvw.equalsIgnoreCase("img1"))
		   	 {
		   		int wantedPosition = 13;
		   		 //int wantedPosition = 8; // Whatever position you're looking for
				int firstPosition = list.getFirstVisiblePosition() - list.getHeaderViewsCount(); // This is the same as child #0
				int wantedChild = wantedPosition - firstPosition;
				// Say, first visible position is 8, you want position 10, wantedChild will now be 2
				// So that means your view is child #2 in the ViewGroup:
				
				
				if (wantedChild < 0 || wantedChild >= list.getChildCount()) {
				  Log.d("Child Not Available", "");
				}
				else{
					View v=list.getChildAt(wantedChild);
					
					ImageButton btnI=new ImageButton(AddDefect.this);
					btnI.setImageBitmap(BtnImageBmp);
					imgVw1=(ImageButton)v.findViewById(R.id.row_cell_addDefectPhoto_img1);
					imgVw1.setBackgroundDrawable(btnI.getDrawable());
					//imgVw1.setBackgroundColor(getResources().getColor(R.color.transparent));
					//imgVw1.setImageBitmap(BtnImageBmp);
					
				}
				
		   	 }
		   	 else if(strFromImgvw.equalsIgnoreCase("img2"))
		   	 {
		   		int wantedPosition = 13;
		   		 //int wantedPosition = 8; // Whatever position you're looking for
				int firstPosition = list.getFirstVisiblePosition() - list.getHeaderViewsCount(); // This is the same as child #0
				int wantedChild = wantedPosition - firstPosition;
				// Say, first visible position is 8, you want position 10, wantedChild will now be 2
				// So that means your view is child #2 in the ViewGroup:
				
				if (wantedChild < 0 || wantedChild >= list.getChildCount()) {
				  Log.d("Child Not Available", "");
				}
				else{
					View v=list.getChildAt(wantedChild);
		   		
		   		ImageButton btnI=new ImageButton(AddDefect.this);
				btnI.setImageBitmap(BtnImageBmp);
				imgVw2=(ImageButton)v.findViewById(R.id.row_cell_addDefectPhoto_img2);
		   		 imgVw2.setBackgroundDrawable(btnI.getDrawable());
				//imgVw2.setBackgroundColor(getResources().getColor(R.color.transparent));
				//imgVw2.setImageBitmap(BtnImageBmp);
		   		
				}
		   	 }
		   	 else if(strFromImgvw.equalsIgnoreCase("img3"))
		   	 {
		   		int wantedPosition = 13;
		   		 //int wantedPosition = 8; // Whatever position you're looking for
				int firstPosition = list.getFirstVisiblePosition() - list.getHeaderViewsCount(); // This is the same as child #0
				int wantedChild = wantedPosition - firstPosition;
				// Say, first visible position is 8, you want position 10, wantedChild will now be 2
				// So that means your view is child #2 in the ViewGroup:
				
				if (wantedChild < 0 || wantedChild >= list.getChildCount()) {
				  Log.d("Child Not Available", "");
				}
				else{
					View v=list.getChildAt(wantedChild);
		   		
		   		ImageButton btnI=new ImageButton(AddDefect.this);
				btnI.setImageBitmap(BtnImageBmp);
				imgVw3=(ImageButton)v.findViewById(R.id.row_cell_addDefectPhoto_img3);
		   		 imgVw3.setBackgroundDrawable(btnI.getDrawable());
				//imgVw2.setBackgroundColor(getResources().getColor(R.color.transparent));
		   		//imgVw3.setImageBitmap(BtnImageBmp);
		   		
				}
		   	 }
		}
		catch(Exception e)
		{
			Log.d("Exception", ""+e.getMessage());
		}
		}
	  }
};


public static Bitmap scaleDownBitmap(Bitmap photo, int newHeight, Context context) {

	 final float densityMultiplier = context.getResources().getDisplayMetrics().density;        

	 int h= (int) (newHeight*densityMultiplier);
	 int w= (int) (h * photo.getWidth()/((double) photo.getHeight()));

	 photo=Bitmap.createScaledBitmap(photo, w, h, true);

	 return photo;
	 }
	public void SaveClick(View v){
	try{
		if(arrTradeMaster!=null && arrTradeMaster.length>0)
		{
		FMDBDatabaseAccess db=new FMDBDatabaseAccess(AddDefect.this);
//		if(AddedSnagType!=null && AddedSnagType.length>0){
//			for(int i=0;i<AddedSnagType.length;i++)
//				db.insertORUpdateJobType(AddedSnagType[i]);
//		}
//		if(AddedFaultType!=null && AddedFaultType.length>0){
//			for(int i=0;i<AddedFaultType.length;i++)
//				db.insertORUpdateFaultType(AddedFaultType[i]);
//		}
		
		NewSnag =new SnagMaster();
		UUID uuid=UUID.randomUUID();
		String strid=uuid.toString().toUpperCase();//.toLowerCase();
	
		NewSnag.setID(strid);
		NewSnag.setSnagType(SelectedTradeType);
		NewSnag.setSnagDetails(SelectedJobDetails);
		NewSnag.setPictureURL1(PhotoURl1);
		NewSnag.setPictureURL2(PhotoURl2);
		NewSnag.setPictureURL3(PhotoURl3);
		NewSnag.setProjectID(CurrentProject.getID());
		NewSnag.setProjectName(CurrentProject.getProjectName());
		NewSnag.setBuildingID(CurrentBuilding.getID());
		NewSnag.setBuildingName(CurrentBuilding.getBuildingName());
		NewSnag.setContractorStatus("Pending");
		if(!isExtArea && !isFloorExtArea)
		{
			NewSnag.setFloorID(CurrentFloor.getID());
			NewSnag.setFloor(CurrentFloor.getFloor());
			
			if(isAptmt)
			{
				NewSnag.setApartmentID(CurrentAPT.getID());
				NewSnag.setApartment(CurrentAPT.getApartmentNo());
			}
			else
			{
				NewSnag.setApartmentID(CurrentSFA.getID());
				NewSnag.setApartment(CurrentSFA.getAreaName());
			}
		}
		else if(isFloorExtArea)
		{
			NewSnag.setFloorID(CurrentFloor.getID());
			NewSnag.setFloor(CurrentFloor.getFloor());
			NewSnag.setApartmentID("");
			NewSnag.setApartment("");
		}
		else
		{
			NewSnag.setFloorID("");
			NewSnag.setFloor("");
			NewSnag.setApartmentID("");
			NewSnag.setApartment("");
		}
		
		if(selectedCost.length()!=0)
			NewSnag.setCost(Double.parseDouble(selectedCost));
		else
			NewSnag.setCost(0.0);
		NewSnag.setCostTo(costTo);
		NewSnag.setSnagPriority(snagPriority);
		
		NewSnag.setAptAreaName(SelectedArea);
		NewSnag.setAptAreaID(SelectedAreaID);
		NewSnag.setReportDate(""+ReportedDate);
		NewSnag.setSnagStatus("Pending");
		NewSnag.setResolveDate("");
		//String str=SelectedFaultType;
		NewSnag.setExpectedInspectionDate(ExpectedDate);
		NewSnag.setFaultType(SelectedInspectionGroup);
		
		if(selectedInspectorIndex!=-1)
		{
		 NewSnag.setInspectorID(arrInspetor[selectedInspectorIndex].getID());
		 NewSnag.setInspectorName(arrInspetor[selectedInspectorIndex].getFirstName()+" "+arrInspetor[selectedInspectorIndex].getLastName());
		}
		else
		{
			NewSnag.setInspectorID("");
			NewSnag.setInspectorName("");
				
		}
		
		if(SelectedEmployeeIndex!=-1)
		{
			NewSnag.setAllocatedTo(arrEmployee[SelectedEmployeeIndex].getEmpCode());
			NewSnag.setAllocatedToName(arrEmployee[SelectedEmployeeIndex].getEmpName()+" "+arrEmployee[SelectedEmployeeIndex].getEmpLastName());
			}
			else
			{
				NewSnag.setAllocatedTo("");
				NewSnag.setAllocatedToName("");
					
			}
		
		JobMaster objJob=db.getContractorDetailFromJobMaster(NewSnag.getSnagType(), NewSnag.getProjectID());
		if(objJob!=null)
		{
			NewSnag.setContractorID(objJob.getContractorID());
			NewSnag.setContractorName(objJob.getContractorName());
			NewSnag.setSubContractorID(objJob.getSubContractorID());
			NewSnag.setSubContractorName(objJob.getSubContractorName());
			NewSnag.setSubSubContractorID(objJob.getSubSubContractorID());
			NewSnag.setSubSubContractorName(objJob.getSubSubContractorName());
		}
		else
		{
			NewSnag.setContractorID("");
			NewSnag.setContractorName("");
			NewSnag.setSubContractorID("");
			NewSnag.setSubContractorName("");
			NewSnag.setSubSubContractorID("");
			NewSnag.setSubSubContractorName("");
		}
		
		
//		if(SelectedTradeType.equalsIgnoreCase("RCC")){
//			NewSnag.setContractorID("CTR-000001");
//		}
		if(arrCheckList!=null && arrCheckList.length>0){
		for(int i=0;i<arrCheckList.length;i++){
			
		UUID id=UUID.randomUUID();
		arrCheckList[i].ID=id.toString();
		arrCheckList[i].ProjectID=NewSnag.getProjectID();
		arrCheckList[i].BuildingID=NewSnag.getBuildingID();
		if(!isExtArea && !isFloorExtArea)
		{
			arrCheckList[i].FloorID=NewSnag.getFloorID();
			arrCheckList[i].ApartmentID=NewSnag.getApartmentID();
		}
		else if(isFloorExtArea)
		{
			arrCheckList[i].FloorID=NewSnag.getFloorID();
			arrCheckList[i].ApartmentID="";
		}
		else
		{
			arrCheckList[i].FloorID="";
			arrCheckList[i].ApartmentID="";
		}
		
		arrCheckList[i].AptAreaID=NewSnag.getAptAreaID();
		arrCheckList[i].SnagID=NewSnag.getID();
		//obj.ChecklistID="";
		//obj.ChecklistID="";
		//obj.ChecklistDescription="";
		//obj.CheckListEntry="";
		arrCheckList[i].CreatedBy=RegUserID;
		arrCheckList[i].CreatedDate="";
		arrCheckList[i].ModifiedBy=RegUserID;
		arrCheckList[i].ModifiedDate="";
		arrCheckList[i].EnteredOnMachineID="";
		FMDBDatabaseAccess d=new FMDBDatabaseAccess(AddDefect.this);
		d.insertORUpdateSnagChecklistEntry(arrCheckList[i]);
		}
		}
			
		
		
		
		if(!isExtArea)
		{
//		new AlertDialog.Builder(AddDefect.this)
//	    
//	    .setMessage("Do you want to add dependency?")
//	    .setPositiveButton("YES", new DialogInterface.OnClickListener() {
//	        public void onClick(DialogInterface dialog, int which) { 
//	            Intent i=new Intent(AddDefect.this,com.snagreporter.AddDependency.class);
//	            i.putExtra("Floor", CurrentFloor);
//	    		i.putExtra("Project", CurrentProject);
//	    		i.putExtra("Building", CurrentBuilding);
//	    		if(isAptmt){
//	    			i.putExtra("isAptmt", isAptmt);
//	    			i.putExtra("Apartment", CurrentAPT);
//	            }
//	            else{
//	            	i.putExtra("isAptmt", isAptmt);
//	    			i.putExtra("SFA", CurrentSFA);
//	            }
//	    		i.putExtra("NewSnag", NewSnag);
//	            startActivityForResult(i, 10002);
//	        	
//	        }
//	     })
	     //.setNegativeButton("NO", new DialogInterface.OnClickListener() {
	        //public void onClick(DialogInterface dialog, int which) { 
	        	//DownloadDataFromWeb task=new DownloadDataFromWeb();
	    		//task.execute(10);//ltt
	        	String photo[]={PhotoURl1,PhotoURl2,PhotoURl3};
	        	Intent i=new Intent(AddDefect.this,com.snagreporter.imagemap.ImageMapActivity.class);
	        	i.putExtra("ImageName", CurrentFloor.getFloorPlanImage());
	        	i.putExtra("currentsnag",NewSnag);
	        	i.putExtra("AddedSnagType",AddedSnagType);
	        	i.putExtra("AddedFaultType",AddedFaultType);
	        	i.putExtra("photo",photo);
	        	if(CurrentFloor.getFloorPlanImage()!=null && CurrentFloor.getFloorPlanImage().length()>0)
	        	{
	        		startActivityForResult(i, 10002);
	        	}
	        	else
	        	{
	        		DownloadDataFromWeb task=new DownloadDataFromWeb();
	        		task.execute(10);
	        		finish();
	        	}
	        	
	       // }
	    // })
	   // .show();
		
		}
		else
		{
			DownloadDataFromWeb task=new DownloadDataFromWeb();
    		task.execute(10);
    		finish();
		}
	}
	else{
		new AlertDialog.Builder(AddDefect.this)
	    
	    .setMessage("No Trade found")
	    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
	        public void onClick(DialogInterface dialog, int which) { 
	        	
	        	//android.os.Process.killProcess(android.os.Process.myPid());
	        }
	     })
	    .show();
	}
		
	}
	catch(Exception e){
		Log.d("Error=", ""+e.getMessage());
	}
	
}
	protected class DownloadDataFromWeb extends AsyncTask<Integer , Integer, Void> {
		ProgressDialog mProgressDialog = new ProgressDialog(AddDefect.this);
    	JSONObject jObject;
    	String output="";
    	SnagMaster obj=NewSnag;
    	
        @Override
        protected void onPreExecute() { 
        	if(isOnline){
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
	    		    
	    		    
	    		    String CoumnNames="ID~SnagType~SnagDetails~PictureURL1~PictureURL2~PictureURL3~ProjectID~ProjectName~BuildingID~BuildingName~FloorID~Floor~ApartmentID~Apartment~AptAreaName~SnagStatus~ResolveDate~ExpectedInspectionDate~FaultType~InspectorID~InspectorName~Cost~CostTo~PriorityLevel~AllocatedTo~AllocatedToName~ContractorID~ContractorName~SubContractorID~SubContractorName~SubSubContractorID~SubSubContractorName~ReportDate~CreatedBy~ContractorStatus~AptAreaID";
	    		    String Values=""+obj.getID()+"~"+obj.getSnagType()+"~"+obj.getSnagDetails()+"~"+obj.getPictureURL1()+"~"+obj.getPictureURL2()+"~"+obj.getPictureURL3()+"~"+obj.getProjectID()+"~"+obj.getProjectName()+"~"+obj.getBuildingID()+"~"+obj.getBuildingName()+"~"+obj.getFloorID()+"~"+obj.getFloor()+"~"+obj.getApartmentID()+"~"+obj.getApartment()+"~"+obj.getAptAreaName()+"~"+obj.getSnagStatus()+"~"+obj.getResolveDate()+"~"+obj.getExpectedInspectionDate()+"~"+obj.getFaultType()+"~"+obj.getInspectorID()+"~"+obj.getInspectorName()+"~"+obj.getCost()+"~"+obj.getCostTo()+"~"+obj.getSnagPriority()+"~"+obj.getAllocatedTo()+"~"+obj.getAllocatedToName()+"~"+obj.getContractorID()+"~"+obj.getContractorName()+"~"+obj.getSubContractorID()+"~"+obj.getSubContractorName()+"~"+obj.getSubSubContractorID()+"~"+obj.getSubSubContractorName()+"~"+obj.getReportDate()+"~"+RegUserID+"~"+obj.getContractorStatus()+"~"+obj.getAptAreaID();
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
  		
        	if(isOnline){
    		uploadImage(PhotoURl1);
    		uploadImage(PhotoURl2);
    		uploadImage(PhotoURl3);
    		
        	}
        	obj.setStatusForUpload("Inserted");
        	if(isOnline)
        		obj.setIsDataSyncToWeb(true);
        	else
        		obj.setIsDataSyncToWeb(false);
    		FMDBDatabaseAccess db=new FMDBDatabaseAccess(AddDefect.this);
    		db.InsertIntoSnagMaster(obj);
    		
    		//if(mProgressDialog.isShowing())
    		//	mProgressDialog.dismiss();
        	
    		
    		db=null;
        	}
    		catch(Exception e)
    		{
    			Log.d("Exception", ""+e.getMessage());
    		}
    		//finish();
        	
        	
        	
            
        }
         
}
public void uploadImage2(String url)
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
		Log.d("Exception", ""+e.getMessage());
	}
}
public byte[] getBytesFromBitmap2(Bitmap bitmap) {
    ByteArrayOutputStream stream = new ByteArrayOutputStream();
    bitmap.compress(CompressFormat.JPEG, 70, stream);
    return stream.toByteArray();
}
	public String GetUTCdateAsString()
	{
		 final String DATEFORMAT = "yyyy-MM-dd";

	    final SimpleDateFormat sdf = new SimpleDateFormat(DATEFORMAT);
	    sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
	   String utcTime = sdf.format(new Date());

	    return utcTime;
	}
	
	public void  getApartmentDetailsFromWeb(){
    	ProgressDialog mProgressDialog;
    	mProgressDialog = new ProgressDialog(this);
    	try{
    		mProgressDialog.setMessage("Loading...");
            mProgressDialog.show(); 
            String METHOD_NAME = "GetDataTable";
    		String NAMESPACE = "http://tempuri.org/";
    		//SharedPreferences sharedPref1 = getSharedPreferences("AppDelegate",MODE_PRIVATE);
    		String URL = ""+getResources().getString(R.string.WS_URL).toString()+"SnagReporter_WebS.asmx";//SnagReporter_WCF.svc
    		String SOAP_ACTION = "http://tempuri.org/GetDataTable";//
    		//URL="http://betsall2.computact.org/Bets4All_Authentication.asmx";
    		String res = "";
    		try {
    		    SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
    		    
    		    SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
    		    envelope.dotNet = true;
    		    envelope.setOutputSoapObject(request);
    		    
    		    request.addProperty("_strTableName","ApartmentDetails");
    		   // request.addProperty("ProjectID",""+CurrentProject.getID());
    		    //request.addProperty("BuildingID",""+CurrentBuilding.getID());
    		    //request.addProperty("FloorID",""+CurrentFloor.getID());
    		    //request.addProperty("ApartmentID","");
        		
    		    
    		    HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
    		    androidHttpTransport.call(SOAP_ACTION, envelope);
    		    //SoapPrimitive result = (SoapPrimitive) envelope.getResponse();
    		    // to get the data
    		    Object resonse=envelope.getResponse();
    		    String resultData = resonse.toString();
    		    
    		    JSONObject jObject;
    			
				jObject = new JSONObject(resultData);
				JSONArray arr = jObject.getJSONArray("Data");
				if(arr!=null){
					for(int i=0;i<arr.length();i++){
						JSONObject geometry = arr.getJSONObject(i);
						parseData(geometry);
						
						//parseFriends(geometry);
					}
				}
				mProgressDialog.dismiss();
    		    res = resultData;
    		    // 0 is the first object of data
    		} catch (Exception e) {
    		    res = e.getMessage();
    		    Log.d("Error=", ""+e.getMessage());
    		    mProgressDialog.dismiss();
    		}
    	}
    	catch(Exception e){
    		Log.d("Error=", ""+e.getMessage());
    		 mProgressDialog.dismiss();
    	}
    }
    public void parseData(JSONObject obj){
    	try{
    		String ID=obj.getString("ID");
    		String ApartmentID=obj.getString("ApartmentID");
    		String Apartment=obj.getString("Apartment");
    		
    		
    		String FloorID=obj.getString("FloorID");
    		String Floor=obj.getString("Floor");
    		String BuildingID=obj.getString("BuildingID");
    		String BuildingName=obj.getString("BuildingName");
    		String ProjectID=obj.getString("ProjectID");
    		String ProjectName=obj.getString("ProjectName");
    		String AptAreaType=obj.getString("AptAreaType");
    		String AptAreaName=obj.getString("AptAreaName");
    		String SubSerial=obj.getString("SubSerial");
    		
    		ApartmentDetails prj=new ApartmentDetails();
    		prj.setID(ID);
    		prj.setApartmentID(ApartmentID);
    		prj.setApartment(Apartment);
    		prj.setFloorID(FloorID);
    		prj.setFloor(Floor);
    		prj.setBuildingID(BuildingID);
    		prj.setBuildingName(BuildingName);
    		prj.setProjectID(ProjectID);
    		prj.setProjectName(ProjectName);
    		prj.setAptAreaType(AptAreaType);
    		prj.setAptAreaName(AptAreaName);
    		prj.setSubSerial(SubSerial);
    		
    		String ImageName=obj.getString("ImageName");
    		prj.setImageName(ImageName);
    		
    		FMDBDatabaseAccess db=new FMDBDatabaseAccess(getApplicationContext());
    		db.insertORUpdateApartmentDetails(prj);
    	}
    	catch(Exception e){
    		Log.d("Error=", ""+e.getMessage());
    	}
    }
    
    
   
    
    
  //@@@@@@@@@@@@@@@@@@@@@@@@@@
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater menuInflater = getMenuInflater();
        //if(isOnline)
        	menuInflater.inflate(R.layout.menu, menu);
        //else
        //	menuInflater.inflate(R.layout.menuoff, menu);
        return true;
    }
   
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
 
        switch (item.getItemId())
        {
        /*case R.id.menu_showlist:
            // Single menu item is selected do something
            // Ex: launching new activity/screen or show alert message
            //Toast.makeText(ProjectListPage.this, "Bookmark is Selected", Toast.LENGTH_SHORT).show();
        	//openChart();
            return true;
        case R.id.menu_showlistinWeb:
        	Intent i=new Intent(AddDefect.this,com.snagreporter.GraphWebView.class);
        	startActivity(i);
            return true;
        case R.id.menu_OnlineOfline:
        	//Intent i=new Intent(ProjectListPage.this,com.snagreporter.GraphWebView.class);
        	//startActivity(i);
        	if(isOnline){
        		item.setTitle("GoOnline");
        		isOnline=false;
        		SharedPreferences sharedPref = getSharedPreferences("AppDelegate",MODE_PRIVATE);
                SharedPreferences.Editor prefEditor = sharedPref.edit();
                prefEditor.putBoolean("isOnline",false);
                prefEditor.commit();
        	}
        	else{
        		item.setTitle("GoOffline");
        		isOnline=true;
        		SharedPreferences sharedPref = getSharedPreferences("AppDelegate",MODE_PRIVATE);
                SharedPreferences.Editor prefEditor = sharedPref.edit();
                prefEditor.putBoolean("isOnline",true);
                prefEditor.commit();
        	}
            return true;
        case R.id.menu_sync:
        	//Intent i=new Intent(ProjectListPage.this,com.snagreporter.GraphWebView.class);
        	//startActivity(i);
        	if(isOnline){
        		SyncData();
        	}
            return true;
            */
        case R.id.menuBtn_exit:
        	new AlertDialog.Builder(AddDefect.this)
    	    
    	    .setMessage("Are you sure you want to Exit?")
    	    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
    	        public void onClick(DialogInterface dialog, int which) { 
    	        	try{
    	        	setResult(10001);
    	    		finish();
    	        	}
    	    		catch(Exception e)
    	    		{
    	    			Log.d("Exception", ""+e.getMessage());
    	    		}
    	        	//android.os.Process.killProcess(android.os.Process.myPid());
    	        }
    	     })
    	     .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
    	        public void onClick(DialogInterface dialog, int which) { 
    	            
    	        }
    	     })
    	    .show();
        	return true;
        case R.id.menuBtn_logout:
        	new AlertDialog.Builder(AddDefect.this)
    	    
    	    .setMessage("Are you sure you want to Logout?")
    	    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
    	        public void onClick(DialogInterface dialog, int which) { 
    	        	try{
    	            FMDBDatabaseAccess db=new FMDBDatabaseAccess(AddDefect.this);
    	            SharedPreferences SP = getSharedPreferences("AppDelegate",MODE_PRIVATE);
    				db.performLogout(SP.getString("RegUserID", ""));
    				Intent i=new Intent(AddDefect.this,com.snagreporter.Login_page.class);
    				
    				startActivity(i);
    				
    				finish();
    	        	}
    	    		catch(Exception e)
    	    		{
    	    			Log.d("Exception", ""+e.getMessage());
    	    		}
    	        	
    	        }
    	     })
    	     .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
    	        public void onClick(DialogInterface dialog, int which) { 
    	            
    	        	
    	        }
    	     })
    	    .show();
        	return true;
 
        
 
        default:
            return super.onOptionsItemSelected(item);
        }
    }
    
//    public void SyncData(){
//    	DownloadDataFromWeb2 task=new DownloadDataFromWeb2();
//    	task.execute(10);
//    	
//    }
    protected class DownloadDataFromWeb2 extends AsyncTask<Integer , Integer, Void> {
    	
    	JSONObject jObject;
    	String output="";
    	SnagMaster obj=new SnagMaster();
    	boolean isNewComplete=false;
    	boolean isModifiedComplete=false;
    	boolean isAllDone=false;
    	
        @Override
        protected void onPreExecute() {  
        	try{
        	if(!mProgressDialog2.isShowing()){
        	mProgressDialog2.setCancelable(false);
        	mProgressDialog2.setMessage("Synchronizing...");
        	mProgressDialog2.show();
        	}
        	}
    		catch(Exception e)
    		{
    			Log.d("Exception", ""+e.getMessage());
    		}
        }      
        @Override
        protected Void doInBackground(Integer... params) {
        	
        	
        	
        	try
    		{
        		
        		//FMDBDatabaseAccess db=new FMDBDatabaseAccess(DefectListPage.this);
        		obj=getUnSyncedNewSnag();
        		if(obj!=null)
        		{
        			isNewComplete=false;
        			isModifiedComplete=false;
        			isAllDone=false;
        		
        		
        		
        		
        		
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
	    		    String Values=""+obj.getID()+"~"+obj.getSnagType()+"~"+obj.getSnagDetails()+"~"+obj.getPictureURL1()+"~"+obj.getPictureURL2()+"~"+obj.getPictureURL3()+"~"+obj.getProjectID()+"~"+obj.getProjectName()+"~"+obj.getBuildingID()+"~"+obj.getBuildingName()+"~"+obj.getFloorID()+"~"+obj.getFloor()+"~"+obj.getApartmentID()+"~"+obj.getApartment()+"~"+obj.getAptAreaName()+"~"+obj.getSnagStatus()+"~"+obj.getResolveDate()+"~"+obj.getExpectedInspectionDate()+"~"+obj.getFaultType()+"~"+obj.getInspectorID()+"~"+obj.getInspectorName()+"~"+obj.getCost()+"~"+obj.getCostTo()+"~"+obj.getSnagPriority();
	    		    String TableName="SnagMaster";
	    		    
	    		    request.addProperty("_strCoumnNames",CoumnNames);
	    		    request.addProperty("_strValues",Values);
	    		    request.addProperty("_strTableName",TableName);
	    		    
	    		    
	    		    HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
	    		    androidHttpTransport.call(SOAP_ACTION, envelope);
	    		    Object resonse=envelope.getResponse();
	    		    output = resonse.toString();
	    		
	    		    uploadImage(obj.getPictureURL1());
	        		uploadImage(obj.getPictureURL2());
	        		uploadImage(obj.getPictureURL3());
	    		    
	    		    
	    			
					//jObject = new JSONObject(resultData);
	    		}
	    		 catch(Exception e){
	    			 Log.d("Error=", ""+e.getMessage()); 
	    		 }
        		}
        		else{
        			
        			isNewComplete=true;
        			isModifiedComplete=false;
        			isAllDone=false;
        			obj=getUnSyncedModifiedSnag();
        			if(obj!=null){
        				isModifiedComplete=false;
        				isAllDone=false;
        				
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
        	    		    String Values=""+obj.getSnagType()+"~"+obj.getSnagDetails()+"~"+obj.getFaultType()+"~"+obj.getExpectedInspectionDate()+"~"+obj.getPictureURL1()+"~"+obj.getPictureURL2()+"~"+obj.getPictureURL3()+"~"+obj.getApartmentID()+"~"+obj.getApartment()+"~"+obj.getAptAreaName()+"~"+obj.getReportDate()+"~"+obj.getSnagStatus()+"~"+obj.getReInspectedUnresolvedDate()+"~"+obj.getReInspectedUnresolvedDatePictureURL1()+"~"+obj.getReInspectedUnresolvedDatePictureURL2()+"~"+obj.getReInspectedUnresolvedDatePictureURL3()+"~"+obj.getResolveDate()+"~"+obj.getResolveDatePictureURL1()+"~"+obj.getResolveDatePictureURL2()+"~"+obj.getResolveDatePictureURL3()+"~"+obj.getCost()+"~"+obj.getCostTo()+"~"+obj.getSnagPriority();
        	    		    String TableName="SnagMaster";
        	    		    String KeyColName="ID";
        	    		    String ColDataType="String";
        	    		    String ColValue=""+obj.getID();
        	    		    
        	    		    request.addProperty("_strCoumnNames",CoumnNames);
        	    		    request.addProperty("_strValues",Values);
        	    		    request.addProperty("_strTableName",TableName);
        	    		    request.addProperty("_strKeyColName",KeyColName);
        	    		    request.addProperty("_strKeyColDataType",ColDataType);
        	    		    request.addProperty("_keyColValue",ColValue);
        	    		    
        	    		    
        	    		    HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
        	    		    androidHttpTransport.call(SOAP_ACTION, envelope);
        	    		    Object resonse=envelope.getResponse();
        	    		    output = resonse.toString();
        	    		    
        	    		    uploadImage(obj.getPictureURL1());
        	    			uploadImage(obj.getPictureURL2());
        	    			uploadImage(obj.getPictureURL3());
        	    			uploadImage(obj.getReInspectedUnresolvedDatePictureURL1());
        	    			uploadImage(obj.getReInspectedUnresolvedDatePictureURL2());
        	    			uploadImage(obj.getReInspectedUnresolvedDatePictureURL3());
        	    			uploadImage(obj.getResolveDatePictureURL1());
        	    			uploadImage(obj.getResolveDatePictureURL2());
        	    			uploadImage(obj.getResolveDatePictureURL3());
        	    		    
        	    			
        					//jObject = new JSONObject(resultData);
        	    		}
        	    		 catch(Exception e){
        	    			 Log.d("Error=", ""+e.getMessage()); 
        	    		 }
        			}
        			else{
        				isModifiedComplete=true;
        				isAllDone=true;
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
        	if(obj!=null){
        	//mProgressDialog.dismiss();

    		
    		FMDBDatabaseAccess db=new FMDBDatabaseAccess(AddDefect.this);
    		db.UpdateIsSyncedSnagMaster(obj.getID());
    		db=null;
    		
        	
        	}
        	else{
        		
        	}
        	
        	if(isAllDone){
        		mProgressDialog2.dismiss();
        		Log.d("All Data Synced", "");
        	}
        	else{
        		SyncData();
        	}
        	
        	}
    		catch(Exception e)
    		{
    			Log.d("Exception", ""+e.getMessage());
    		}
            
        }
         
}
    
    
    public SnagMaster getUnSyncedNewSnag()
	{
		SnagMaster obj=null;
		try
		{
			SQLiteDatabase mydb;
		Context context=this;
		String DBNAME = "SnagReporter.db";
			String strQuery;
			strQuery="Select * from SnagMaster where IsDataSyncToWeb='false' and StatusForUpload='Inserted'";
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
        		obj.setReInspectedUnresolvedDate(cursor.getString(cursor.getColumnIndex("ReInspectedUnresolvedDate")));
        		obj.setReInspectedUnresolvedDatePictureURL1(cursor.getString(cursor.getColumnIndex("ReInspectedUnresolvedDatePictureURL1")));
        		obj.setReInspectedUnresolvedDatePictureURL2(cursor.getString(cursor.getColumnIndex("ReInspectedUnresolvedDatePictureURL2")));
        		obj.setReInspectedUnresolvedDatePictureURL3(cursor.getString(cursor.getColumnIndex("ReInspectedUnresolvedDatePictureURL3")));
        		
        		obj.setExpectedInspectionDate(cursor.getString(cursor.getColumnIndex("ExpectedInspectionDate")));
        		obj.setFaultType(cursor.getString(cursor.getColumnIndex("FaultType")));
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
    public SnagMaster getUnSyncedModifiedSnag()
	{
		SnagMaster obj=null;
		try
		{SQLiteDatabase mydb;
		Context context=this;
		String DBNAME = "SnagReporter.db";
			String strQuery;
			strQuery="Select * from SnagMaster where IsDataSyncToWeb='false' and StatusForUpload='Modified'";
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
        		obj.setReInspectedUnresolvedDate(cursor.getString(cursor.getColumnIndex("ReInspectedUnresolvedDate")));
        		obj.setReInspectedUnresolvedDatePictureURL1(cursor.getString(cursor.getColumnIndex("ReInspectedUnresolvedDatePictureURL1")));
        		obj.setReInspectedUnresolvedDatePictureURL2(cursor.getString(cursor.getColumnIndex("ReInspectedUnresolvedDatePictureURL2")));
        		obj.setReInspectedUnresolvedDatePictureURL3(cursor.getString(cursor.getColumnIndex("ReInspectedUnresolvedDatePictureURL3")));
        		
        		obj.setExpectedInspectionDate(cursor.getString(cursor.getColumnIndex("ExpectedInspectionDate")));
        		obj.setFaultType(cursor.getString(cursor.getColumnIndex("FaultType")));
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
    //@@@@@@@@@@@@@@@@@@@@@@

	
	public void onScrollStateChanged(AbsListView arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}
	
	public void onScroll(AbsListView lw, final int firstVisibleItem,
	                 final int visibleItemCount, final int totalItemCount) {

		try{
	    switch(lw.getId()) {
	        case R.id.android_add_defect_list:     

	            // Make your calculation stuff here. You have all your
	            // needed info from the parameters of this function.

	            // Sample calculation to determine if the last 
	            // item is fully visible.
	            final int lastItem = firstVisibleItem + visibleItemCount;
	            if(lastItem == totalItemCount) {
	                // Last item is fully visible.
	            	
	            	//ImageButton btn1=null,btn2=null,btn3=null;
	        		if(PhotoURl1!=null && PhotoURl1.length()>0){
	        			String FilePath1=Environment.getExternalStorageDirectory()+File.separator+"SnagReporter/Pictures/"+PhotoURl1+".jpg";
	        			File fooo = new File(FilePath1);
	        			if(fooo.exists()){
	        			Bitmap Img1 = decodeFile(FilePath1);
	        			
	        			//imgVw1.setBackgroundDrawable(btnI.getDrawable());
	        			int wantedPosition = 13;
	   		   		 //int wantedPosition = 8; // Whatever position you're looking for
	   				int firstPosition = list.getFirstVisiblePosition() - list.getHeaderViewsCount(); // This is the same as child #0
	   				int wantedChild = wantedPosition - firstPosition;
	   				// Say, first visible position is 8, you want position 10, wantedChild will now be 2
	   				// So that means your view is child #2 in the ViewGroup:
	   				
	   				
	   				if (wantedChild < 0 || wantedChild >= list.getChildCount()) {
	   				  Log.d("Child Not Available", "");
	   				}
	   				else{
	   					View v=list.getChildAt(wantedChild);
	   					
	   					ImageButton btnI=new ImageButton(AddDefect.this);
	   					btnI.setImageBitmap(Img1);
	   					imgVw1=(ImageButton)v.findViewById(R.id.row_cell_addDefectPhoto_img1);
	   					imgVw1.setBackgroundDrawable(btnI.getDrawable());
	   					//imgVw1.setBackgroundColor(getResources().getColor(R.color.transparent));
	   					//imgVw1.setImageBitmap(BtnImageBmp);
	   					
	   				}
	        			}
	        			else
	        			{

	        			}
	        			
	        		}
	        		
	        		if(PhotoURl2!=null && PhotoURl2.length()>0){
	        			String FilePath2=Environment.getExternalStorageDirectory()+File.separator+"SnagReporter/Pictures/"+PhotoURl2+".jpg";
	        			File fooo = new File(FilePath2);
	        			if(fooo.exists())
	        			{
	        			Bitmap Img2 = decodeFile(FilePath2);
	        			
	        			//imgVw2.setBackgroundDrawable(btnI.getDrawable());
	        	   		
	        			int wantedPosition = 13;
	   		   		 //int wantedPosition = 8; // Whatever position you're looking for
	   				int firstPosition = list.getFirstVisiblePosition() - list.getHeaderViewsCount(); // This is the same as child #0
	   				int wantedChild = wantedPosition - firstPosition;
	   				// Say, first visible position is 8, you want position 10, wantedChild will now be 2
	   				// So that means your view is child #2 in the ViewGroup:
	   				
	   				if (wantedChild < 0 || wantedChild >= list.getChildCount()) {
	   				  Log.d("Child Not Available", "");
	   				}
	   				else{
	   					View v=list.getChildAt(wantedChild);
	   		   		
	   		   		ImageButton btnI=new ImageButton(AddDefect.this);
	   				btnI.setImageBitmap(Img2);
	   				imgVw2=(ImageButton)v.findViewById(R.id.row_cell_addDefectPhoto_img2);
	   		   		 imgVw2.setBackgroundDrawable(btnI.getDrawable());
	   				
	   		   		
	   				}
	        			
	        			}
	        			else
	        			{
	        				
	        			}
	        			
	        		}
	        		
	        		if(PhotoURl3!=null && PhotoURl3.length()>0){
	        			String FilePath2=Environment.getExternalStorageDirectory()+File.separator+"SnagReporter/Pictures/"+PhotoURl3+".jpg";
	        			File fooo = new File(FilePath2);
	        			if(fooo.exists())
	        			{
	        			Bitmap Img2 = decodeFile(FilePath2);
	        			 
	        			//imgVw3.setBackgroundDrawable(btnI.getDrawable());
	        			int wantedPosition = 13;
	   		   		 //int wantedPosition = 8; // Whatever position you're looking for
	   				int firstPosition = list.getFirstVisiblePosition() - list.getHeaderViewsCount(); // This is the same as child #0
	   				int wantedChild = wantedPosition - firstPosition;
	   				// Say, first visible position is 8, you want position 10, wantedChild will now be 2
	   				// So that means your view is child #2 in the ViewGroup:
	   				
	   				if (wantedChild < 0 || wantedChild >= list.getChildCount()) {
	   				  Log.d("Child Not Available", "");
	   				}
	   				else{
	   					View v=list.getChildAt(wantedChild);
	   		   		
	   		   		ImageButton btnI=new ImageButton(AddDefect.this);
	   				btnI.setImageBitmap(Img2);
	   				imgVw3=(ImageButton)v.findViewById(R.id.row_cell_addDefectPhoto_img3);
	   		   		 imgVw3.setBackgroundDrawable(btnI.getDrawable());
	   				}
	        			}
	        			else
	        			{
	        				//btn3=null;
//	        				DownloadImagesInBackGround task=new DownloadImagesInBackGround();
//	        				task.imageUrl=PhotoURl3;
//	        				task.tag=3;
//	        				task.execute(10);
	        			}
	        			
	        		}
	        		
	        		//@@for scroll value
	        		
	        		
	        		
	        		/*
	        		String[] strScrValue=new String[12];
	        		strScrValue[0]=SelectedArea;
	        		strScrValue[1]=SelectedTradeType;
	        		strScrValue[2]=SelectedInspectionGroup;
	        		strScrValue[3]=selectedInscpectionDescription;
	        		strScrValue[4]=SelectedJobDetails;
	        		if(SelectedEmployeeIndex!=-1)
	        		{
	        			strScrValue[5]=arrEmployee[SelectedEmployeeIndex].getEmpName();
	        		}
	        		else
	        		{
	        			strScrValue[5]="";
	        		}
	        		if(selectedInspectorIndex!=-1)
	        		{
	        			strScrValue[6]=arrInspetor[selectedInspectorIndex].getFirstName();
	        		}
	        		else
	        		{
	        			strScrValue[6]="";
	        		}
	        		strScrValue[7]=ReportedDate;
	        		strScrValue[8]=ExpectedDate;
	        		strScrValue[9]=selectedCost;
	        		strScrValue[10]=costTo;
	        		strScrValue[11]=snagPriority;
	        		
	        		int j=0;
	        		for(int i=firstVisibleItem;i<((firstVisibleItem+visibleItemCount)-1);i++)
					{
						if(firstVisibleItem>=1){
						int wantedPosition =i; // Whatever position you're looking for
						int firstPosition = list.getFirstVisiblePosition() - list.getHeaderViewsCount(); // This is the same as child #0
						int wantedChild = wantedPosition - firstPosition;
						if (wantedChild < 0 || wantedChild >= list.getChildCount())
						{
							Log.d("Child Not Available", "");
							j++;
						}
						else
						{
							View v=list.getChildAt(wantedChild);
		   					TextView t=(TextView)v.findViewById(R.id.row_cell_addDefect_text);
		   					t.setText(""+strScrValue[i-1]);
		   					Log.d("i",""+(i-1));
						}
						}
						
					}*/
	        		///@@for value end
	            }
	    }
	}
	catch(Exception e)
	{
		Log.d("Exception", ""+e.getMessage());
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
    			else
    			{
    				new AlertDialog.Builder(AddDefect.this)
    	    	    
    	    	    .setMessage("Please go online to Sync.")
    	    	    .setPositiveButton("GoOnline", new DialogInterface.OnClickListener() {
    	    	        public void onClick(DialogInterface dialog, int which) { 
    	    	        	try{
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
    	    	    		catch(Exception e)
    	    	    		{
    	    	    			Log.d("Exception", ""+e.getMessage());
    	    	    		}
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
    			Intent i=new Intent(AddDefect.this,com.snagreporter.GraphWebView.class);
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
    		try{
    			
    		ParseSyncData parser=new ParseSyncData(AddDefect.this);
    			    		parser.start();
    		}
    		catch(Exception e)
    		{
    			Log.d("Exception",""+e.getMessage());
    		}
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
                	try{
                	if(!mProgressDialog2.isShowing()){
                		mProgressDialog2.setCancelable(false);
                		mProgressDialog2.setMessage("Synchronizing...");
                		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
                		mProgressDialog2.show();
                	}
                	FMDBDatabaseAccess db=new FMDBDatabaseAccess(AddDefect.this);
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
            		catch(Exception e)
            		{
            			Log.d("Exception", ""+e.getMessage());
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
                	
                	try{
                	FMDBDatabaseAccess db=new FMDBDatabaseAccess(AddDefect.this);
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
//                		mProgressDialog2.setMessage("Synchronization Complete...");
//                		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
//                		mProgressDialog2.dismiss();
                		StartDownloadSnags task=new StartDownloadSnags();
                		task.execute(10);
                	}
                	}
            		catch(Exception e)
            		{
            			Log.d("Exception", ""+e.getMessage());
            		}
                }
                 
        }

        protected class StartDownloadSnags extends AsyncTask<Integer , Integer, Void> {
        	
        	
        	boolean result=false;
        	String resultData="";
                @Override
                protected void onPreExecute() {  
                	try{
                	if(!mProgressDialog2.isShowing()){
                		mProgressDialog2.setCancelable(false);
                		mProgressDialog2.setMessage("Downloading Snags...");
                		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
                		mProgressDialog2.show();
                	}
                	}
            		catch(Exception e)
            		{
            			Log.d("Exception", ""+e.getMessage());
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
            	FMDBDatabaseAccess db=new FMDBDatabaseAccess(AddDefect.this);
        		db.insertORUpdateSnagMaster(obj);
        	}
        	catch(Exception e){
        		Log.d("Error=", ""+e.getMessage());
        	}
        }

            
          //@@@@@@$$$$$$$$$$@@@@@@@@@@@
        
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
    			Log.d("Exception", ""+e.getMessage());
    		}
        }
        public byte[] getBytesFromBitmap(Bitmap bitmap) {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(CompressFormat.JPEG, 70, stream);
            return stream.toByteArray();
        }
        
        //@@@@@@$$$$$$$$$$@@@@@@@@@@@
    
        @Override
        public boolean onKeyDown(int keyCode, KeyEvent event) {
        	// TODO Auto-generated method stub
        	try{
        		if(isMenuVisible)
             	{
             		HideTopMenu();
             		return false;
             	}
             	else
             	{
             		finish();
             	}
        	}
        	catch(Exception e)
        	{
        		Log.d("Exception", ""+e.getMessage());
        	}
        	return super.onKeyDown(keyCode, event);
        }
        public boolean isValidText(String str)
        {
        	boolean res=false;;
        	try{
        		if(str!=null && str.trim().length()>0 && !str.equalsIgnoreCase("null") && !str.equalsIgnoreCase("(null)"))
        			res=true;
        		else
        			res=false;
        	}
        	catch(Exception e)
        	{
        		Log.d("Exception",""+e.getMessage());
        	}
        	return res;
        }
        
        
        public void syncSnagCheckListEntrtries()
        {
        	try{
        		
        	}
        	catch(Exception e)
        	{
        		Log.d("Exception",""+e.getMessage());
        	}
        }
}