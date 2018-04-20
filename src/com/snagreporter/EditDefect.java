package com.snagreporter;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.UUID;
import android.support.v4.view.ViewPager.LayoutParams;
import org.apache.http.util.ByteArrayBuffer;
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
import android.app.DatePickerDialog;
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
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
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
import android.view.WindowManager;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.snagreporter.database.*;
import com.snagreporter.entity.ApartmentDetails;
import com.snagreporter.entity.ApartmentMaster;
import com.snagreporter.entity.BuildingMaster;
import com.snagreporter.entity.Employee;
import com.snagreporter.entity.FaultType;
import com.snagreporter.entity.FloorMaster;
import com.snagreporter.entity.JobType;
import com.snagreporter.entity.ProjectMaster;
import com.snagreporter.entity.SnagChecklistEntries;
import com.snagreporter.entity.SnagMaster;
import com.snagreporter.entity.StdFloorAreas;
import com.snagreporter.entity.TradeDetails;
import com.snagreporter.entity.TradeMaster;
public class EditDefect extends Activity implements OnScrollListener
{
	ImageButton imgVw1,imgVw2,imgVw3;
	ImageButton imgVw21,imgVw22,imgVw23;
	ImageButton imgVw31,imgVw32,imgVw33;
	
	//public String strSelectedTradeDescrptn;
	
	
	int iAreaIndx,iTradeIndex,isbTradeIndex,iCmntindx,iAllctdindx,iInspctrindx,irePortdateIndx;
	int iExpectedInspectionIndx,iCostindex,iCosttoindx,iSnagPriorityindx,isngPenPicIndx,iReInspIndxDt;
	int iUnResPicIndx,iReslvPicIndx,iReslvdtIndx,iTrdDescrptionindx,SelectedEmployeeIndex;
	
	TradeMaster arrTradeMaster[];
	String SelectedTradeMasterID="";
	TradeDetails[]arrTDDistinctTrade;
	String SelectedTradeType="";
	
	String[]arrTDDistinctInspectionGroup;
	TradeDetails[]arrTDDistinctInspectionDescription;
	String selectedInscpectionDescription="";
	SnagChecklistEntries []arrCheckList;
	public String SelectedAreaID,SelectedAreaTypeParent;
	public String AreaType,AreaTypeID,SelectedInspectionGroup;
	FMDBDatabaseAccess fmAccess;
	
	BuildingMaster curBuilding;
	FloorMaster curFloorMaster;
	ProjectMaster currProject;
	ApartmentMaster currAptMaster;
	
	
	boolean isExtArea;
	boolean isFloorExtArea;
	
	
	ListView list;
	//ListView list2;
	//ListView list3;
	//ImageButton imgVw1,imgVw2,imgVw3;
	
	List<Item> items;
	//List<Item> items2;
	//List<Item> items3;
	
	String[] strSetValues;
	String[] strGetValues;
	ApartmentMaster CurrentAPT;
	SnagMaster CurrentSnag;
	String SelectedArea,SelectedJobType,SelectedJobDetails,SelectedFaultType,selectedCost,costTo,SnagPriority;
	JobType arrJobType[];
	FaultType arrFaultType[];
	private static final int CAMERA_PIC_REQUEST = 1337;
	int as_index=-1;
	int as_index_selected=0;
	boolean isImageSetFor1=false;
	boolean isImageSetFor2=false;
	boolean isImageSetFor3=false;
	boolean isImageSetFor21=false;
	boolean isImageSetFor22=false;
	boolean isImageSetFor23=false;
	boolean isImageSetFor31=false;
	boolean isImageSetFor32=false;
	boolean isImageSetFor33=false;
	String PhotoURl1="",PhotoURl2="",PhotoURl3="";
	String PhotoURl21="",PhotoURl22="",PhotoURl23="";
	String PhotoURl31="",PhotoURl32="",PhotoURl33="";
	Bitmap BtnImageBmp,BtnYourImg;
	String strFromImg,strFromImgvw,strFilePath,strMenuType;
	private Bitmap mBitmap;
	int SelJobTypeIndex=0;
	ApartmentDetails AreaList[];
	String RemovedPhotoURL;
	boolean isAptmt;
	StdFloorAreas CurrentSFA;
	String Status[];
	String SelectedStatus;
	String ResolveDate="";
	String ExpectedDate="";
	int TAG=0;
	//static View AllViews[];
	TwoTextArrayAdapter adapter;
	ImageView CurrentIMG;
	
	static final int DATE_DIALOG_ID = 1;
	private int year, month, day;
	String SelectedDate="";
	int CurrentDate;
	boolean isOnline=false;
	ProgressDialog mProgressDialog2;
	private File dir, destImage,f;
	private String cameraFile = null;
	JobType[] AddedSnagType;
	FaultType[] AddedFaultType;
	Employee[] arrEmployee;
	String SelectedEmployee="";
	String RegUserID="";
	String LoginType="";
	View TopMenu;
	boolean isMenuVisible=false;
	MenuHandler menuhandler;
	Button btnUpdate;
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        
        try
        {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.edit_defect);
        
        SharedPreferences SP = getSharedPreferences("AppDelegate",MODE_PRIVATE);
        RegUserID=SP.getString("RegUserID", "");
        LoginType=SP.getString("LoginType", "");
        SelectedEmployeeIndex=-1;
        menuhandler=new MenuHandler(EditDefect.this);
        TopMenu=new View(this);
        
        selectedInscpectionDescription="";
        RelativeLayout.LayoutParams rlp=new RelativeLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
        TopMenu.setLayoutParams(rlp);
        LayoutInflater inflater= LayoutInflater.from(this);
        TopMenu=(View) inflater.inflate(R.layout.popup_menu, null);
        this.addContentView(TopMenu, rlp);
        TopMenu.requestLayout();
        TopMenu.setVisibility(View.INVISIBLE);
        fmAccess=new FMDBDatabaseAccess(EditDefect.this);
        SharedPreferences sharedPref = getSharedPreferences("AppDelegate",MODE_PRIVATE);
        Boolean id=sharedPref.getBoolean("isOnline", false);
        isOnline=id;
        setindexex();
        Button b=(Button)findViewById(R.id.topbar_statusbtn);
		ImageView i=(ImageView)findViewById(R.id.topbar_statusimage);
		btnUpdate=(Button)findViewById(R.id.edt_defectUpdtBtn);
		if(isOnline){
			b.setText("Go Offline");
			i.setBackgroundDrawable(getResources().getDrawable(R.drawable.green));
    	}
    	else{
    		b.setText("Go Online");
    		i.setBackgroundDrawable(getResources().getDrawable(R.drawable.status_online_icon));
    	}
        
        mProgressDialog2 = new ProgressDialog(EditDefect.this);
        isAptmt=getIntent().getBooleanExtra("isAptmt",false);
        isExtArea=getIntent().getBooleanExtra("isFromExtArea",false);
        isFloorExtArea=getIntent().getBooleanExtra("isFromFloorArea",false);
        //AllViews=new View[12];
        
        curBuilding=(BuildingMaster)getIntent().getExtras().get("Building");
        currProject=(ProjectMaster)getIntent().getExtras().get("Project");
        if(!isExtArea)
        {
        	curFloorMaster=(FloorMaster)getIntent().getExtras().get("Floor");
        }
        if(isAptmt || (!isExtArea && !isFloorExtArea)){
        	CurrentAPT=(ApartmentMaster)getIntent().getExtras().get("Apartment");
        	
        }
        else
        {
        	CurrentSFA=(StdFloorAreas)getIntent().getExtras().get("SFA");
        }
       // CurrentAPT=(ApartmentMaster)getIntent().getExtras().get("Apartment");
        CurrentSnag=(SnagMaster)getIntent().getExtras().get("Snag");
        //SelectedArea=getIntent().getExtras().getString("SelectedArea");
        Object objNot = getLastNonConfigurationInstance();
        if(objNot!=null)
        {
        	Intent i2=(Intent)objNot;
        	strGetValues=(String[])i2.getExtras().get("strSetValues");
        	AddedFaultType=(FaultType[])i2.getExtras().get("AddedFaultType");
        	AddedSnagType=(JobType[])i2.getExtras().get("AddedSnagType");
        	arrJobType=(JobType[])i2.getExtras().get("arrJobType");
        	arrFaultType=(FaultType[])i2.getExtras().get("arrFaultType");
        }
       
       // populateCureentSnag();
        
        populatetradeStuff();
        continueprocess();
        

        
        		
        SharedPreferences sharedPref2 = getSharedPreferences("AppDelegate",MODE_PRIVATE);
        Boolean id2=sharedPref2.getBoolean("isOnline", false);
        isOnline=id2;
        }
        catch(Exception e)
        {
        	Log.d("Exception", ""+e.getMessage());
        }
    }
    public void setindexex()
    {
    	try{
    		iAreaIndx=1;
    		iTradeIndex=2;
    		isbTradeIndex=3;
    		iTrdDescrptionindx=4;
    		iCmntindx=5;
    		iAllctdindx=6;
    		iInspctrindx=7;
    		irePortdateIndx=8;
    		iExpectedInspectionIndx=9;
    		iCostindex=10;
    		iCosttoindx=11;
    		iSnagPriorityindx=12;
    		isngPenPicIndx=13;
    		iReInspIndxDt=14;
    		iUnResPicIndx=15;
    		iReslvdtIndx=16;
    		iReslvPicIndx=17;
    	}
    	catch(Exception e)
    	{
    		Log.d("Exception",""+e.getMessage());
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
		//menuhandler.MenuRoomsheetClick(curr);
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
	
	public void populatetradeStuff()
	{
		try
		{
			
			/////
			FMDBDatabaseAccess obj=new FMDBDatabaseAccess(EditDefect.this);
		       if(arrJobType==null || arrJobType.length==0)
		    	   arrJobType=obj.getJobType();
				if(SelectedArea==null || SelectedArea.equals("")){
					if(!CurrentSnag.getAptAreaName().toString().equalsIgnoreCase(""))
					SelectedArea=CurrentSnag.getAptAreaName().toString();
					else
						SelectedArea=CurrentAPT.getApartmentNo();
				}
				if(SelectedAreaID==null || SelectedAreaID.equals("")){
					if(!CurrentSnag.getAptAreaName().toString().equalsIgnoreCase(""))
						SelectedAreaID=CurrentSnag.getAptAreaID().toString();
					else
						SelectedAreaID="";
				}
				if(SelectedJobDetails==null || SelectedJobDetails.equals(""))
					SelectedJobDetails=CurrentSnag.getSnagDetails().toString();
				if(SelectedJobType==null || SelectedJobType.equals(""))
					SelectedJobType=CurrentSnag.getSnagType().toString();
				if(SelectedFaultType==null || SelectedFaultType.equals(""))
					SelectedFaultType=CurrentSnag.getFaultType().toString();
				
				if(SelectedAreaTypeParent==null || SelectedAreaTypeParent.length()==0)
				{
					if(CurrentSnag!=null && CurrentSnag.getApartment()!=null)
						SelectedAreaTypeParent=CurrentSnag.getApartment();
				}
					
				//func
				int index=0;
				for(int i=0;i<arrJobType.length;i++){
					if(SelectedJobType.equalsIgnoreCase(arrJobType[i].getJobType())){
						index=i;
						break;
					}
				}
				FMDBDatabaseAccess fdb=new FMDBDatabaseAccess(EditDefect.this);
				
				if(arrEmployee==null || arrEmployee.length==0){
					arrEmployee=fdb.getEmployee();
				}
				
				
				if(arrFaultType==null || arrFaultType.length==0)
					arrFaultType=fdb.getFaultType(arrJobType[index].getID());
			
			
			/////
			
			  if(isExtArea)
		        {
		        	//extArea=db.getExtBuildingDetails(CurrentProject.getID());
		        }
		        else if(isFloorExtArea)
		        {
		        	
		        }
		        else
		        {
		        	AreaList=fmAccess.getApartmentDetails(CurrentAPT);
		        }
			  if(isExtArea)
		  	    {
		  	    	
		  	    		if(SelectedArea==null || SelectedArea.length()==0){
		  	    			SelectedArea=curBuilding.getBuildingName();//extArea[0].getBuildingName();
		  	    			SelectedAreaID=curBuilding.getID();
		  	    			SelectedAreaTypeParent=curBuilding.getBuildingType(); //extArea[0].getBuildingType();
		  	    		}
		  	    			
		  	    }
		  	    else if(isFloorExtArea)
		  	    {
		  	    	if(SelectedArea==null || SelectedArea.length()==0){
			    			SelectedArea=curFloorMaster.getFloor();//extArea[0].getBuildingName();
			    			SelectedAreaID=curFloorMaster.getID();
			    			SelectedAreaTypeParent=curFloorMaster.FloorType; //extArea[0].getBuildingType();
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
			  
			
			  if(isExtArea)
			  	{
			  		AreaType=curBuilding.getBuildingType();
			  		AreaTypeID="";
			  	}
			  	else if(isFloorExtArea)
			  	{
			  		AreaType=curFloorMaster.FloorType;
			  		AreaTypeID="";
			  	}
			  	else
			  	{
			  		 String arr[]=fmAccess.getAreaType(SelectedAreaTypeParent);
			  		 if(arr!=null)
			  		 {
			  			 AreaType=arr[0];
			  			 AreaTypeID=arr[1];
			  		 }	 
			  	}
			  
			  
			  
			  
			  
			  if(isExtArea)
			  	{
			  		arrTradeMaster=fmAccess.getExtTradeMasterTradeAptAreaMaster(currProject.getID(),curBuilding.getID(),curBuilding.getBuildingName());
			  	}
			  	else if(isFloorExtArea)
			  	{
			  		arrTradeMaster=fmAccess.getExtTradeMasterTradeAptAreaMasterFloor(currProject.getID(), curBuilding.getID(),curFloorMaster.getID(), curFloorMaster.getFloor());
			  	}
			  	else
			  	{
			  		arrTradeMaster=fmAccess.getTradeMasterTradeAptAreaMaster(currProject.getID(), curBuilding.getID(),curFloorMaster.getID(), CurrentAPT.getID(),SelectedAreaTypeParent,SelectedAreaID);
			  	}
			  	if(arrTradeMaster!=null && arrTradeMaster.length>0){
			  		if(SelectedTradeMasterID!=null && SelectedTradeMasterID.length()==0){
			  		for(int id=0;id<arrTradeMaster.length;id++)
			  		{
			  			if(arrTradeMaster[id].TradeType.equalsIgnoreCase(SelectedJobType))
			  			{
			  				SelectedTradeMasterID=arrTradeMaster[id].ID;
					  		SelectedTradeType=arrTradeMaster[id].TradeType;
					  		break;
			  			}
			  			
			  		}
			  		
			  		}
			  	}
			  	else{
			  		SelectedTradeMasterID="";
			  		SelectedTradeType="";
			  	}
			  	if(SelectedTradeMasterID.length()>0)
			  		arrTDDistinctTrade=fmAccess.getTradeDetailsByTradeMasterID(SelectedTradeMasterID);
			  	else
			  		arrTDDistinctTrade=null;
			  	
			  	SelectedInspectionGroup=SelectedFaultType;
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
			  			arrTDDistinctInspectionGroup=fmAccess.getTradeDetailsByTradeTypeNewExtArea(SelectedTradeMasterID, curBuilding, strFl, SelectedTradeMasterID);
			  		}
			  		else if(isFloorExtArea)
			  		{
			  			arrTDDistinctInspectionGroup=fmAccess.getTradeDetailsByTradeTypeNewFloorArea(SelectedTradeMasterID, curFloorMaster, strFl, SelectedTradeMasterID);
			  		}
			  		else
			  			arrTDDistinctInspectionGroup=fmAccess.getTradeDetailsByTradeTypeNew(SelectedTradeMasterID, CurrentAPT,strFl,SelectedTradeMasterID);//getTradeDetailsByTradeType(SelectedTradeMasterID);//////usd
			  	}
			  	else
			  		arrTDDistinctInspectionGroup=null;
			  	//new
			  	if(SelectedInspectionGroup==null || SelectedInspectionGroup.length()==0)
			  	{
			  		if(arrTDDistinctInspectionGroup!=null && arrTDDistinctInspectionGroup.length>0)
				  		SelectedInspectionGroup=arrTDDistinctInspectionGroup[0];
				  	else
				  		SelectedInspectionGroup="";
			  	}
			  
			  	//new close
//			  	if(arrTDDistinctInspectionGroup!=null && arrTDDistinctInspectionGroup.length>0)
//			  		arrTDDistinctInspectionDescription=obj.getTradeDetailsByInspectionGroup(SelectedTradeMasterID, arrTDDistinctInspectionGroup[0]);
			  	if(arrTDDistinctInspectionGroup!=null && arrTDDistinctInspectionGroup.length>0){
			  		arrTDDistinctInspectionDescription=fmAccess.getTradeDetailsByInspectionGroup(SelectedTradeMasterID, SelectedInspectionGroup);//arrTDDistinctInspectionGroup[0]);
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
		}
		catch(Exception e)
		{
			Log.d("Exception",""+e.getMessage());
		}
	}
    public void continueprocess()
    {
    	try{
    	Status=new String[3];
		Status[0]="Pending";
		Status[1]="Reinspected & Unresolved";
		Status[2]="Resolved";
		

		if(strGetValues!=null)
		{
				SelectedArea=strGetValues[0];
		        SelectedJobType=strGetValues[1];
		        SelectedFaultType=strGetValues[2];
		        SelectedJobDetails=strGetValues[3];
		        ExpectedDate=strGetValues[4];
		        PhotoURl1=strGetValues[5];
		        PhotoURl2=strGetValues[6];
		        PhotoURl3=strGetValues[7];
		        selectedCost=strGetValues[8];
		        costTo=strGetValues[9];
		        SnagPriority=strGetValues[10];
		        PhotoURl21=strGetValues[14];
		        PhotoURl22=strGetValues[15];
		        PhotoURl23=strGetValues[16];
		        PhotoURl31=strGetValues[17];
		        PhotoURl32=strGetValues[18];
		        PhotoURl33=strGetValues[19];
		        SelectedEmployee=strGetValues[20];
		}
		if(selectedCost==null)
		{
			if(CurrentSnag.getCost()!=null)
			{
				selectedCost=CurrentSnag.getCost().toString();
			}
			else
			{
				selectedCost="0";
			}
		}
		if(costTo==null)
		{
			if(CurrentSnag.getCostTo()!=null)
			{
				costTo=CurrentSnag.getCostTo().toString();
			}
			else
			{
				costTo="";
			}
		}
		if(SnagPriority==null)
		{
			if(CurrentSnag.getSnagPriority()!=null)
			{
				SnagPriority=CurrentSnag.getSnagPriority().toString();
			}
			else
			{
				SnagPriority="";
			}
		}
		if(SelectedEmployee==null || SelectedEmployee.length()==0){
			
			if(CurrentSnag.getAllocatedToName()!=null){
				
				SelectedEmployee=""+CurrentSnag.getAllocatedToName();
			}
			
		}
		else{
			SelectedEmployee="";
		}
		FMDBDatabaseAccess db=new FMDBDatabaseAccess(EditDefect.this);
     //   AreaList=db.getApartmentDetails(CurrentAPT);
        //imgVw1=(ImageButton)findViewById(R.id.row_cell_addDefectPhoto_img1);
        //imgVw2=(ImageButton)findViewById(R.id.row_cell_addDefectPhoto_img2);
        //imgVw3=(ImageButton)findViewById(R.id.row_cell_addDefectPhoto_img3);
        
        
        
        list=(ListView)findViewById(R.id.android_edit_defect_list);
        //list2=(ListView)findViewById(R.id.android_edit_defect_list2);
        //list3=(ListView)findViewById(R.id.android_edit_defect_list3);
        
        final Calendar cal = Calendar.getInstance();
        year = cal.get(Calendar.YEAR);
        month = cal.get(Calendar.MONTH);
        day = cal.get(Calendar.DAY_OF_MONTH);
        //SelectedDate=GetDate();
        
       
       items = new ArrayList<Item>();
       if(LoginType.equals(getResources().getString(R.string.strInspector).toString()))
    	   items.add(new Header(null, "EDIT SNAG",false,false,true,true));
       else
    	   items.add(new Header(null, "EDIT SNAG",false,false,true,false));
       
       
	
		if(ExpectedDate==null || ExpectedDate.equals(""))
			ExpectedDate=CurrentSnag.getExpectedInspectionDate();
		if(ExpectedDate==null || ExpectedDate.equals(""))
			ExpectedDate=GetDate();
	
       
       populateData();
       
       list.setDivider(getResources().getDrawable(R.color.transparent));
       list.setDividerHeight(1);
       
        adapter = new TwoTextArrayAdapter(this, items);
        list.setAdapter(adapter);
        list.setOnScrollListener(this);
        
        list.setOnItemClickListener(new OnItemClickListener() {

			
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				
				try{
				// TODO Auto-generated method stub
					
				if(!SelectedStatus.equalsIgnoreCase(Status[2]))	
				{
					if(SelectedStatus.equalsIgnoreCase(Status[0])){
						if(position!=0 && position!=iCmntindx && position!=iExpectedInspectionIndx && position!=iCostindex){
							//Toast.makeText(EditDefect.this, "list position="+position, Toast.LENGTH_LONG).show();;
							//View v=arg1;//ListItemAddDefect
							//SelTextInList=(TextView)v.findViewById(R.id.row_cell_addDefect_text);
							registerForContextMenu(arg0);
				    		as_index=position;
				    		openContextMenu(arg0);
				    		unregisterForContextMenu(arg0);
						}
						else if(position==iCostindex)
							{
								AlertDialog.Builder alert = new AlertDialog.Builder(EditDefect.this);

								alert.setTitle("Cost");
								final EditText input = new EditText(EditDefect.this);
								input.setInputType(InputType.TYPE_CLASS_NUMBER);
								int wantedPosition =iCostindex; // Whatever position you're looking for    
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
			  						  int wantedPosition = iCostindex; // Whatever position you're looking for
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
			  							ListItemAddDefect itemi=(ListItemAddDefect)items.get(wantedPosition);
			  							items.set(wantedPosition,new ListItemAddDefect(null, "Cost",""+selectedCost,itemi.isDisabled));
			  							adapter.notifyDataSetChanged();
			  							}
			  						 }
			  						 catch(Exception e)
			  						 {
			  							 Log.d("Exception",""+e.getMessage());
			  						 }
			  						  // Do something with value!
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

						else if(position==iCmntindx){
							AlertDialog.Builder alert = new AlertDialog.Builder(EditDefect.this);

							alert.setTitle("Comments");
							//alert.setMessage("Message");

							// Set an EditText view to get user input 
							final EditText input = new EditText(EditDefect.this);
							
							int wantedPosition = iCmntindx; // Whatever position you're looking for
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
								int wantedPosition = iCmntindx; // Whatever position you're looking for
								int firstPosition = list.getFirstVisiblePosition() - list.getHeaderViewsCount(); // This is the same as child #0
								int wantedChild = wantedPosition - firstPosition;
								// Say, first visible position is 8, you want position 10, wantedChild will now be 2
								// So that means your view is child #2 in the ViewGroup:
								if (wantedChild < 0 || wantedChild >= list.getChildCount()) {
								  Log.d("Child Not Available", "");
								}
								else{
									String value = input.getText().toString();
									  View v2=list.getChildAt(wantedChild);
										TextView t2=(TextView)v2.findViewById(R.id.row_cell_addDefect_text);
										
										
										t2.setText(""+value);
										SelectedJobDetails=value;
										ListItemAddDefect itemi=(ListItemAddDefect)items.get(wantedPosition);
										items.set(wantedPosition,new ListItemAddDefect(null, "Comments",""+SelectedJobDetails,itemi.isDisabled));
								}
								}
		 						 catch(Exception e)
		 						 {
		 							 Log.d("Exception",""+e.getMessage());
		 						 }
							  // Do something with value!
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
						else if(position==iExpectedInspectionIndx){// || position==10){
							CurrentDate=position;
							showDialog(DATE_DIALOG_ID);
						}
							//
					}
						else if(SelectedStatus.equalsIgnoreCase(Status[1])){
							if(position==iExpectedInspectionIndx || position==13){
								CurrentDate=position;
								showDialog(DATE_DIALOG_ID);
							}
//							else if(position==13){
//								registerForContextMenu(arg0);
//					    		as_index=position;
//					    		openContextMenu(arg0);
//					    		unregisterForContextMenu(arg0);
//							}
							
						}
						else if(SelectedStatus.equalsIgnoreCase(Status[2])){
							if(CurrentSnag.getReInspectedUnresolvedDate()==null || CurrentSnag.getReInspectedUnresolvedDate().equals("")){
							if(position==iExpectedInspectionIndx || position==13){
								CurrentDate=position;
								showDialog(DATE_DIALOG_ID);
							}
							}
							else{
								if(position==iExpectedInspectionIndx || position==15){
									CurrentDate=position;
									showDialog(DATE_DIALOG_ID);
								}
							}
						}

				}
			 }
	        catch(Exception e)
	        {
	        	Log.d("Exception", ""+e.getMessage());
	        }
			}
			
		});

    	 }
        catch(Exception e)
        {
        	Log.d("Exception", ""+e.getMessage());
        }

    }
/*  @Override
public void onConfigurationChanged(Configuration newConfig){
		super.onConfigurationChanged(newConfig);
	}*/
    public Object onRetainNonConfigurationInstance()
    {
        strSetValues=new String[21];
        strSetValues[0]=SelectedArea;
        strSetValues[1]=SelectedJobType;
        strSetValues[2]=SelectedFaultType;
        strSetValues[3]=SelectedJobDetails;
        strSetValues[4]=ExpectedDate;
        strSetValues[5]=PhotoURl1;
        strSetValues[6]=PhotoURl2;
        strSetValues[7]=PhotoURl3;
        strSetValues[8]=selectedCost;
        strSetValues[9]=costTo;
        strSetValues[10]=SnagPriority;
        strSetValues[11]=strFromImg;
        if(f!=null)
        	strSetValues[12]=f.getAbsolutePath();
        strSetValues[13]=""+TAG;
        strSetValues[14]=PhotoURl21;
        strSetValues[15]=PhotoURl22;
        strSetValues[16]=PhotoURl23;
        strSetValues[17]=PhotoURl31;
        strSetValues[18]=PhotoURl32;
        strSetValues[19]=PhotoURl33;
        strSetValues[20]=SelectedEmployee;
        
        Intent i=new Intent();
        i.putExtra("strSetValues", strSetValues);
        i.putExtra("AddedSnagType", AddedSnagType);
        i.putExtra("AddedFaultType", AddedFaultType);
        i.putExtra("arrJobType", arrJobType);
        i.putExtra("arrFaultType", arrFaultType);
        
        return i;
    }

///////////Date
    
    
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
      return new DatePickerDialog(this, mDateSetListener, year, month, day);
  }
  return null;
}



// the call back received when the user "sets" the date in the dialog
private DatePickerDialog.OnDateSetListener mDateSetListener =
  new DatePickerDialog.OnDateSetListener() {
      public void onDateSet(DatePicker view, int myear, int monthOfYear, int dayOfMonth) {
    	  try{
          year = myear;
          month = monthOfYear;
          day = dayOfMonth;
          updateDate();
    		 }
          catch(Exception e)
          {
          	Log.d("Exception", ""+e.getMessage());
          }
      }
};
private String GetDate(){
 String[] months={"01","02","03","04","05","06","07","08","09","10","11","12"};
 StringBuilder date=new StringBuilder().append(day).append('-')
			.append(months[month]).append('-').append(year);
	String dt=date.toString();
	return dt;
}
private void updateDate() {
	try{
	String[] months={"01","02","03","04","05","06","07","08","09","10","11","12"};
	
	int wantedPosition =CurrentDate;
//	if(CurrentDate==11)
//		wantedPosition = 11;
//	else
//		wantedPosition = 8;
	
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
		
		
		t.setText(new StringBuilder().append(day).append('-')
				.append(months[month]).append('-').append(year));
		if(CurrentDate==13 || CurrentDate==15){
			SelectedDate=t.getText().toString();
			ListItemAddDefect itemi=(ListItemAddDefect)items.get(wantedPosition);
			itemi.str2=SelectedDate;
			items.set(wantedPosition,itemi);
			adapter.notifyDataSetChanged();
		}
		else{
			ExpectedDate=t.getText().toString();
			ListItemAddDefect itemi=(ListItemAddDefect)items.get(wantedPosition);
			itemi.str2=ExpectedDate;
			items.set(wantedPosition,itemi);
			adapter.notifyDataSetChanged();
		}
	}
	
	//dateDisplay.setTextColor(Color.BLACK);
	
	//udate.concat((String) t.getText());	
}
catch(Exception e)
{
	Log.d("Exception", ""+e.getMessage());
}
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
        	
        		View v=items.get(position).getView(mInflater, null);
        		//AllViews[position]=v;
        		return v;
        	
        }
    }
    
    
    public void populateData()
    {
    	try{
    		
    		SelectedStatus=CurrentSnag.getSnagStatus().toString();
			if(SelectedStatus==null || SelectedStatus.length()==0 || SelectedStatus.equals("null"))
				SelectedStatus="Pending";
    		boolean isDisabled=false;
    		boolean isllDisAbled=false;
			if(SelectedStatus.equalsIgnoreCase(Status[0])){
				isDisabled=false;
				isllDisAbled=false;
			}
			else if(SelectedStatus.equalsIgnoreCase(Status[1])){
				isDisabled=true;
				isllDisAbled=false;
			}
			else if(SelectedStatus.equalsIgnoreCase(Status[2])){
				
				isDisabled=true;
				isllDisAbled=true;
				if(btnUpdate!=null)
					btnUpdate.setText("Ok");
			}
    		
    		
    		items.add(new ListItemAddDefect(null, "Area",""+SelectedArea,isDisabled));
    		items.add(new ListItemAddDefect(null, "Trades",""+SelectedJobType,true));//isDisabled
    		if(SelectedFaultType!=null && !SelectedFaultType.equals("null") && !SelectedFaultType.equals(""))
    			items.add(new ListItemAddDefect(null, "Sub Trade",""+SelectedFaultType,true));
    		else
    			items.add(new ListItemAddDefect(null, "Sub Trade","",true));
    		
    		if(selectedInscpectionDescription==null || selectedInscpectionDescription.length()==0)
    			if(arrTDDistinctInspectionDescription!=null)
    				selectedInscpectionDescription=arrTDDistinctInspectionDescription[0].InspectionDescription;
    		items.add(new ListItemAddDefect(null, "Trade Description",""+selectedInscpectionDescription,true));
    		items.add(new ListItemAddDefect(null, "Comments",""+SelectedJobDetails,isDisabled));
    		items.add(new ListItemAddDefect(null, "Allocated To",""+SelectedEmployee,isDisabled));
    		items.add(new ListItemAddDefect(null, "Inspector",""+CurrentSnag.getInspectorName(),true));
    		items.add(new ListItemAddDefect(null, "Report Date",CurrentSnag.getReportDate(),true));
    		items.add(new ListItemAddDefect(null, "Expected Inspection Date",""+ExpectedDate,isllDisAbled));
    		
    		items.add(new ListItemAddDefect(null, "Cost",""+selectedCost,isDisabled));
    		items.add(new ListItemAddDefect(null, "CostTo",""+costTo,isDisabled));
    		items.add(new ListItemAddDefect(null, "SnagPriority",""+SnagPriority,isDisabled));
    		
    		
    		
    		if((PhotoURl1==null || PhotoURl1.equals("")) && CurrentSnag.getPictureURL1()!=null)
    			PhotoURl1=CurrentSnag.getPictureURL1().toString();
    		if((PhotoURl2==null || PhotoURl2.equals("")) && CurrentSnag.getPictureURL2()!=null)
    			PhotoURl2=CurrentSnag.getPictureURL2().toString();
    		if((PhotoURl3==null || PhotoURl3.equals("")) && CurrentSnag.getPictureURL3()!=null)
    			PhotoURl3=CurrentSnag.getPictureURL3().toString();
    		ImageButton btn1=null,btn2=null,btn3=null;
    		
    		if(PhotoURl1!=null && PhotoURl1.length()>0){
    			String FilePath1=Environment.getExternalStorageDirectory()+File.separator+"SnagReporter/Pictures/"+PhotoURl1+".jpg";
    			File fooo = new File(FilePath1);
    			if(fooo.exists()){
    			
    				Bitmap Img1=null;
    				try{
    			 Img1 = decodeFile(FilePath1);
    				}
    				catch(OutOfMemoryError e)
    				{
    					Log.d("Exception",""+e.getMessage());
    				}
    			btn1=new ImageButton(this);
    			btn1.setImageBitmap(Img1);
    			imgVw1=new ImageButton(this);
    			imgVw1.setImageBitmap(Img1);
    			//imgVw1.setBackgroundDrawable(btnI.getDrawable());
    	   		isImageSetFor1=true;
    			}
    			else
    			{
    				DownloadImagesInBackGround task=new DownloadImagesInBackGround();
    				task.imageUrl=PhotoURl1;
    				task.tag=1;
    				task.execute(10);
    			}
    			
    		}
    		
    		if(PhotoURl2!=null && PhotoURl2.length()>0){
    			String FilePath2=Environment.getExternalStorageDirectory()+File.separator+"SnagReporter/Pictures/"+PhotoURl2+".jpg";
    			File fooo = new File(FilePath2);
    			if(fooo.exists())
    			{
    				Bitmap Img2 =null;
    				try{
    			 Img2 = decodeFile(FilePath2);
    				}
    				catch(OutOfMemoryError e)
    				{
    					Log.d("Exception", ""+e.getMessage());
    				}
    			 btn2=new ImageButton(this);
    			btn2.setImageBitmap(Img2);
    			imgVw2=new ImageButton(this);
    			imgVw2.setImageBitmap(Img2);
    			//imgVw2.setBackgroundDrawable(btnI.getDrawable());
    	   		isImageSetFor2=true;
    			}
    			else
    			{
    				DownloadImagesInBackGround task=new DownloadImagesInBackGround();
    				task.imageUrl=PhotoURl2;
    				task.tag=2;
    				task.execute(10);
    			}
    			
    		}
    		
    		if(PhotoURl3!=null && PhotoURl3.length()>0){
    			String FilePath2=Environment.getExternalStorageDirectory()+File.separator+"SnagReporter/Pictures/"+PhotoURl3+".jpg";
    			File fooo = new File(FilePath2);
    			if(fooo.exists())
    			{
    				Bitmap Img2 =null;
    				try{
    			Img2 = decodeFile(FilePath2);
    				}
    				catch(OutOfMemoryError e)
    				{
    					Log.d("Exception",""+e.getMessage());
    				}
    			 btn3=new ImageButton(this);
    			btn3.setImageBitmap(Img2);
    			imgVw3=new ImageButton(this);
    			imgVw3.setImageBitmap(Img2);
    			//imgVw3.setBackgroundDrawable(btnI.getDrawable());
    	   		isImageSetFor3=true;
    			}
    			else
    			{
    				DownloadImagesInBackGround task=new DownloadImagesInBackGround();
    				task.imageUrl=PhotoURl3;
    				task.tag=3;
    				task.execute(10);
    			}
    			
    		}
    		
    		items.add(new ListItemAddDefectPhoto(null, "Snag Photos",btn1,btn2,btn3,1,true,PhotoURl1,PhotoURl2,PhotoURl3,isDisabled));
    		//if(SelectedStatus==null || SelectedStatus.equals(""))
    			
    		
    		//items.add(new ListItemAddDefect(null, "Status",SelectedStatus));
    		
    		btn1=null;btn2=null;btn3=null;
    		
    		
    		
    		if(SelectedStatus.equalsIgnoreCase(Status[1])){
    			if(SelectedDate==null || SelectedDate.equals(""))
    				SelectedDate=CurrentSnag.getReInspectedUnresolvedDate();
    			if(SelectedDate==null || SelectedDate.equals(""))
    				SelectedDate=GetDate();
    			
    			if((PhotoURl21==null || PhotoURl21.equals("")) && CurrentSnag.getReInspectedUnresolvedDatePictureURL1()!=null)
    				PhotoURl21=CurrentSnag.getReInspectedUnresolvedDatePictureURL1().toString();
    			if((PhotoURl22==null || PhotoURl22.equals("")) &&  CurrentSnag.getReInspectedUnresolvedDatePictureURL2()!=null)
    				PhotoURl22=CurrentSnag.getReInspectedUnresolvedDatePictureURL2().toString();
    			if((PhotoURl23==null || PhotoURl23.equals("")) && CurrentSnag.getReInspectedUnresolvedDatePictureURL3()!=null)
    				PhotoURl23=CurrentSnag.getReInspectedUnresolvedDatePictureURL3().toString();
        		
        		if(PhotoURl21!=null && PhotoURl21.length()>0){
        			String FilePath1=Environment.getExternalStorageDirectory()+File.separator+"SnagReporter/Pictures/"+PhotoURl21+".jpg";
        			File fooo = new File(FilePath1);
        			if(fooo.exists()){
        				Bitmap Img1 =null;
        				try{
        			Img1 = decodeFile(FilePath1);
        				}
        				catch(OutOfMemoryError e)
        				{
        					Log.d("Exception",""+e.getMessage());
        				}
        			btn1=new ImageButton(this);
        			btn1.setImageBitmap(Img1);
        			imgVw21=new ImageButton(this);
        			imgVw2.setImageBitmap(Img1);
        			//imgVw1.setBackgroundDrawable(btnI.getDrawable());
        	   		isImageSetFor21=true;
        			}
        		}
        		
        		if(PhotoURl22!=null && PhotoURl22.length()>0){
        			String FilePath2=Environment.getExternalStorageDirectory()+File.separator+"SnagReporter/Pictures/"+PhotoURl22+".jpg";
        			File fooo = new File(FilePath2);
        			if(fooo.exists()){
        				Bitmap Img2 =null;
        				try{
        					Img2 = decodeFile(FilePath2);
        				}
        				catch(OutOfMemoryError e)
        				{
        					Log.d("Exception",""+e.getMessage());
        				}
        			 btn2=new ImageButton(this);
        			btn2.setImageBitmap(Img2);
        			imgVw22=new ImageButton(this);
        			imgVw22.setImageBitmap(Img2);
        			//imgVw2.setBackgroundDrawable(btnI.getDrawable());
        	   		isImageSetFor22=true;
        			}
        		}
        		
        		if(PhotoURl23!=null && PhotoURl23.length()>0){
        			String FilePath2=Environment.getExternalStorageDirectory()+File.separator+"SnagReporter/Pictures/"+PhotoURl23+".jpg";
        			File fooo = new File(FilePath2);
        			if(fooo.exists()){
        				Bitmap Img2 = null;
        				try{
        			 Img2 = decodeFile(FilePath2);
        				}
        				catch(OutOfMemoryError e)
        				{
        					Log.d("Exception",""+e.getMessage());
        				}
        			 btn3=new ImageButton(this);
        			btn3.setImageBitmap(Img2);
        			imgVw23=new ImageButton(this);
        			imgVw23.setImageBitmap(Img2);
        			//imgVw3.setBackgroundDrawable(btnI.getDrawable());
        	   		isImageSetFor23=true;
        			}
        		}
    			
    		items.add(new ListItemAddDefect(null, "ReinspectionDate",SelectedDate,isllDisAbled));
    		items.add(new ListItemAddDefectPhoto(null, "Unresolved Photos",btn1,btn2,btn3,2,true,PhotoURl21,PhotoURl22,PhotoURl23,isllDisAbled));
    		btn1=null;btn2=null;btn3=null;
    		}
    		else if(SelectedStatus.equalsIgnoreCase(Status[2])){
    			if(CurrentSnag.getReInspectedUnresolvedDate()!=null && !CurrentSnag.getReInspectedUnresolvedDate().equalsIgnoreCase("")){
    				if(SelectedDate==null || SelectedDate.equals(""))
        				SelectedDate=CurrentSnag.getReInspectedUnresolvedDate();
        			if(SelectedDate==null || SelectedDate.equals(""))
        				SelectedDate=GetDate();
        			
        			if((PhotoURl21==null || PhotoURl21.equals("")) && CurrentSnag.getReInspectedUnresolvedDatePictureURL1()!=null)
        				PhotoURl21=CurrentSnag.getReInspectedUnresolvedDatePictureURL1().toString();
        			if((PhotoURl22==null || PhotoURl22.equals("")) &&  CurrentSnag.getReInspectedUnresolvedDatePictureURL2()!=null)
        				PhotoURl22=CurrentSnag.getReInspectedUnresolvedDatePictureURL2().toString();
        			if((PhotoURl23==null || PhotoURl23.equals("")) && CurrentSnag.getReInspectedUnresolvedDatePictureURL3()!=null)
        				PhotoURl23=CurrentSnag.getReInspectedUnresolvedDatePictureURL3().toString();
            		
            		if(PhotoURl21!=null && PhotoURl21.length()>0){
            			String FilePath1=Environment.getExternalStorageDirectory()+File.separator+"SnagReporter/Pictures/"+PhotoURl21+".jpg";
            			File fooo = new File(FilePath1);
            			if(fooo.exists()){
            				Bitmap Img1 = null;
            				try{
            			Img1 = decodeFile(FilePath1);
            				}
            				catch(OutOfMemoryError e)
            				{
            					Log.d("Exception",""+e.getMessage());
            				}
            			btn1=new ImageButton(this);
            			btn1.setImageBitmap(Img1);
            			imgVw21=new ImageButton(this);
            			imgVw21.setImageBitmap(Img1);
            			//imgVw1.setBackgroundDrawable(btnI.getDrawable());
            	   		isImageSetFor21=true;
            			}
            		}
            		
            		if(PhotoURl22!=null && PhotoURl22.length()>0){
            			String FilePath2=Environment.getExternalStorageDirectory()+File.separator+"SnagReporter/Pictures/"+PhotoURl22+".jpg";
            			File fooo = new File(FilePath2);
            			if(fooo.exists()){
            				Bitmap Img2 = null;
            				try{
            			Img2 = decodeFile(FilePath2);
            				}
            				catch(OutOfMemoryError e)
            				{
            					Log.d("Exception",""+e.getMessage());
            				}
            				
            			 btn2=new ImageButton(this);
            			btn2.setImageBitmap(Img2);
            			imgVw22=new ImageButton(this);
            			imgVw22.setImageBitmap(Img2);
            			//imgVw2.setBackgroundDrawable(btnI.getDrawable());
            	   		isImageSetFor22=true;
            			}
            		}
            		
            		if(PhotoURl23!=null && PhotoURl23.length()>0){
            			String FilePath2=Environment.getExternalStorageDirectory()+File.separator+"SnagReporter/Pictures/"+PhotoURl23+".jpg";
            			File fooo = new File(FilePath2);
            			if(fooo.exists()){
            				Bitmap Img2 = null;
            				try{
            			 Img2 = decodeFile(FilePath2);
            				}
            				catch(OutOfMemoryError e)
            				{
            					Log.d("Exception",""+e.getMessage());
            				}
            			 btn3=new ImageButton(this);
            			btn3.setImageBitmap(Img2);
            			imgVw23=new ImageButton(this);
            			imgVw23.setImageBitmap(Img2);
            			//imgVw3.setBackgroundDrawable(btnI.getDrawable());
            	   		isImageSetFor23=true;
            			}
            		}
        			
        		items.add(new ListItemAddDefect(null, "ReinspectionDate",SelectedDate,isllDisAbled));
        		items.add(new ListItemAddDefectPhoto(null, "Unresolved Photos",btn1,btn2,btn3,2,true,PhotoURl21,PhotoURl22,PhotoURl23,isllDisAbled));
        		btn1=null;btn2=null;btn3=null;
    			}
    			
    			
    			
    			if(SelectedDate==null || SelectedDate.equals(""))
    				SelectedDate=CurrentSnag.getResolveDate();
    			if(SelectedDate==null || SelectedDate.equals(""))
    				SelectedDate=GetDate();
    			
    			if((PhotoURl31==null || PhotoURl31.equals("") ) && CurrentSnag.getResolveDatePictureURL1()!=null)
    				PhotoURl31=CurrentSnag.getResolveDatePictureURL1().toString();
    			if((PhotoURl32==null || PhotoURl32.equals("") )&& CurrentSnag.getResolveDatePictureURL2()!=null)
    				PhotoURl32=CurrentSnag.getResolveDatePictureURL2().toString();
    			if((PhotoURl33==null || PhotoURl33.equals(""))&& CurrentSnag.getResolveDatePictureURL3()!=null)
    				PhotoURl33=CurrentSnag.getResolveDatePictureURL3().toString();
        		
        		if(PhotoURl31!=null && PhotoURl31.length()>0){
        			String FilePath1=Environment.getExternalStorageDirectory()+File.separator+"SnagReporter/Pictures/"+PhotoURl31+".jpg";
        			File fooo = new File(FilePath1);
        			if(fooo.exists()){
        				Bitmap Img1 =null;	
        				try{
        			Img1 = decodeFile(FilePath1);
        				}
        				catch(OutOfMemoryError e)
        				{
        					Log.d("Exception",""+e.getMessage());
        				}
        			btn1=new ImageButton(this);
        			btn1.setImageBitmap(Img1);
        			imgVw31=new ImageButton(this);
        			imgVw31.setImageBitmap(Img1);
        			//imgVw1.setBackgroundDrawable(btnI.getDrawable());
        	   		isImageSetFor31=true;
        			}
        		}
        		
        		if(PhotoURl32!=null && PhotoURl32.length()>0){
        			String FilePath2=Environment.getExternalStorageDirectory()+File.separator+"SnagReporter/Pictures/"+PhotoURl32+".jpg";
        			File fooo = new File(FilePath2);
        			if(fooo.exists()){
        				Bitmap Img2 = null;
        				try{
        			Img2 = decodeFile(FilePath2);
        				}
        				catch(OutOfMemoryError e)
        				{
        					Log.d("Exception", ""+e.getMessage());
        				}
        			
        			 btn2=new ImageButton(this);
        			btn2.setImageBitmap(Img2);
        			imgVw32=new ImageButton(this);
        			imgVw32.setImageBitmap(Img2);
        			//imgVw2.setBackgroundDrawable(btnI.getDrawable());
        	   		isImageSetFor32=true;
        			}
        		}
        		
        		if(PhotoURl33!=null && PhotoURl33.length()>0){
        			String FilePath2=Environment.getExternalStorageDirectory()+File.separator+"SnagReporter/Pictures/"+PhotoURl33+".jpg";
        			File fooo = new File(FilePath2);
        			if(fooo.exists()){
        				Bitmap Img2 = null;
        				try{
        			 Img2 = decodeFile(FilePath2);
        				}
        				catch(OutOfMemoryError e)
        				{
        					Log.d("Exception",""+e.getMessage());
        				}
        			 btn3=new ImageButton(this);
        			btn3.setImageBitmap(Img2);
        			imgVw33=new ImageButton(this);
        			imgVw33.setImageBitmap(Img2);
        			//imgVw3.setBackgroundDrawable(btnI.getDrawable());
        	   		isImageSetFor33=true;
        			}
        		}
    			
    			
    		items.add(new ListItemAddDefect(null, "Resolve Date",SelectedDate,isllDisAbled));
    		items.add(new ListItemAddDefectPhoto(null, "Resolved Photos",btn1,btn2,btn3,3,true,PhotoURl31,PhotoURl32,PhotoURl33,isllDisAbled));
    		}
    		
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
    		
    		if(!SelectedStatus.equalsIgnoreCase(Status[2]))
    		{
    			registerForContextMenu(v);
        		strFromImg="img1";
        		CurrentIMG=(ImageView)v;
        		TAG=Integer.parseInt(v.getTag().toString());
        		if(TAG==1){
        			if(SelectedStatus.equalsIgnoreCase(Status[0])){
        		as_index=isngPenPicIndx;
        		
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
        		}
        		else if(TAG==2){
        			if(SelectedStatus.equalsIgnoreCase(Status[1])){
        			as_index=isngPenPicIndx;
            		
            		if(!isImageSetFor21)
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
        		}
        		else if(TAG==3){
        			as_index=iUnResPicIndx;
            		
            		if(!isImageSetFor31)
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
    		}
    		
    			
    	}
    	catch(Exception e){
    		Log.d("Error Photo1Clicked=",""+e.getMessage());
    	}
    }
    public void Photo2Clicked(View v){
    	try{
    		if(!SelectedStatus.equalsIgnoreCase(Status[2]))
    		{
    			registerForContextMenu(v);
        		strFromImg="img2";
        		CurrentIMG=(ImageView)v;
        		TAG=Integer.parseInt(v.getTag().toString());
        		if(TAG==1){
        			if(SelectedStatus.equalsIgnoreCase(Status[0])){
        		as_index=isngPenPicIndx;
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
        			
        		}
        		else if(TAG==2){
        			if(SelectedStatus.equalsIgnoreCase(Status[1])){
            		as_index=isngPenPicIndx;
            		if(!isImageSetFor22)
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
        			
        		}
        		else{
        			as_index=iUnResPicIndx;
            		if(!isImageSetFor32)
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
    		}
    		
    			
    	}
    	catch(Exception e){
    		Log.d("Error Photo2Clicked=",""+e.getMessage());
    	}
    }
    public void Photo3Clicked(View v){
    	try{
    		
    		if(!SelectedStatus.equalsIgnoreCase(Status[2]))
    		{
    			registerForContextMenu(v);
        		strFromImg="img3";
        		CurrentIMG=(ImageView)v;
        		TAG=Integer.parseInt(v.getTag().toString());
        		if(TAG==1){
        			if(SelectedStatus.equalsIgnoreCase(Status[0])){
        		as_index=isngPenPicIndx;
        		
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
        		}
        		else if(TAG==2){
        			if(SelectedStatus.equalsIgnoreCase(Status[1])){
            		as_index=isngPenPicIndx;
            		
            		if(!isImageSetFor23)
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
            	}
        		else{
        			as_index=iUnResPicIndx;
            		
            		if(!isImageSetFor33)
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
    		}
    		
    			
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
	if( as_index==isngPenPicIndx || as_index==iUnResPicIndx)//as_index==8 ||
	{
	      
	    if(as_index_selected==0)
	    {
	    	menu.setHeaderTitle("Choose Photo");
	    	menu.add(0, v.getId(), 0, "Choose from Library");  
	    	menu.add(0, v.getId(), 0, "Take with Camera");
	    	
	    }
	    else
	    {
	    	menu.setHeaderTitle("Choose Option");
	    	 menu.add(0, v.getId(), 0, "Remove Photo"); 
	    	 menu.add(0, v.getId(), 0, "View Image");
	    	menu.add(0, v.getId(), 0, "Choose from Library");  
	  	    menu.add(0, v.getId(), 0, "Take with Camera");
	    }
	}
	else if(as_index==iAreaIndx){
		menu.setHeaderTitle("Select Area");
		for(int i=0;i<AreaList.length;i++){
			menu.add(0, v.getId(), 0, ""+AreaList[i].getAptAreaName());
		}
	}
	else if(as_index==iTradeIndex)
	{/*
		if(arrTradeMaster!=null){
			menu.setHeaderTitle("Select Trades");
//			for(int i=0;i<arrJobType.length;i++){
//				menu.add(0, v.getId(), 0, ""+arrJobType[i].getJobType());
//			}
			
			for(int i=0;i<arrTradeMaster.length;i++){
				menu.add(0, v.getId(), 0, ""+arrTradeMaster[i].TradeType);
			}
			}
		*/
	}
	else if(as_index==iSnagPriorityindx)
	{
		menu.setHeaderTitle("Select SnagPriority");
		menu.add(0, v.getId(), 0,"1");
		menu.add(0, v.getId(), 0,"2");
		menu.add(0, v.getId(), 0,"3");
		menu.add(0, v.getId(), 0,"4");
		menu.add(0, v.getId(), 0,"5");
	
	}
	else if(as_index==iCosttoindx)
	{
		menu.setHeaderTitle("Select CostTo");
		menu.add(0, v.getId(), 0,"Client");
		menu.add(0, v.getId(), 0,"Builder");
		menu.add(0, v.getId(), 0,"Contractor");
	}
	else if(as_index==isbTradeIndex)
	{
		/*if(arrTDDistinctInspectionGroup!=null){
			menu.setHeaderTitle("Select InspectionGroup");
			for(int i=0;i<arrTDDistinctInspectionGroup.length;i++){
				//if(SelectedTradeType.equalsIgnoreCase(arrTDDistinctInspectionGroup[i]))
					menu.add(0, v.getId(), 0, ""+arrTDDistinctInspectionGroup[i]);
			}
				
			}*/
		
		
	}
	else if(as_index==iTrdDescrptionindx)
	{
		try
		{
			
		}
		catch(Exception e)
		{
			Log.d("Exception",""+e.getMessage());
		}
	}
	else if(as_index==iAllctdindx)
	{
		try
		{
			if(arrEmployee!=null && arrEmployee.length>0){
				menu.setHeaderTitle("Allocate To");
				for(int i=0;i<arrEmployee.length;i++){
					menu.add(0, v.getId(), 0, ""+arrEmployee[i].getEmpName()+" "+arrEmployee[i].getEmpLastName());
				}
				}
		}
		catch(Exception e)
		{
			Log.d("Exception",""+e.getMessage());
		}
	}
	else if(as_index==iInspctrindx)
	{
		try
		{
			
		}
		catch(Exception e)
		{
			Log.d("Exception",""+e.getMessage());
		}
	}
	/*else if(as_index==9)
	{
		menu.setHeaderTitle("Select Status");
		for(int i=0;i<Status.length;i++)
		{
			menu.add(0, v.getId(), 0, ""+Status[i]);
		}

	}*/
 }
    catch(Exception e)
    {
    	Log.d("Exception", ""+e.getMessage());
    }
	    
	}  
@Override
	public boolean onContextItemSelected(MenuItem item)
	{
	if(as_index==iCostindex || as_index==isngPenPicIndx || as_index==iUnResPicIndx){
	 if(item.getTitle()=="Choose from Library")
	 {
		 try{
		 Intent intent=new Intent();
		 intent.setType("image/*");
		 intent.setAction(Intent.ACTION_GET_CONTENT);
		 intent.addCategory(Intent.CATEGORY_OPENABLE);
		 startActivityForResult(intent,0);
		 }
	        catch(Exception e)
	        {
	        	Log.d("Exception", ""+e.getMessage());
	        }
		 return true;
	 }
	 else if(item.getTitle()=="Take with Camera"){
		 try{
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
		 }
	        catch(Exception e)
	        {
	        	Log.d("Exception", ""+e.getMessage());
	        }
		 return true;
	 }
	 else if(item.getTitle()=="Remove Photo"){
		 try{
		 if(strFromImg.equalsIgnoreCase("img1")){
			 View v=null;
			 if(TAG==1){
				 int wantedPosition = isngPenPicIndx; // Whatever position you're looking for
					int firstPosition = list.getFirstVisiblePosition() - list.getHeaderViewsCount(); // This is the same as child #0
					int wantedChild = wantedPosition - firstPosition;
					// Say, first visible position is 8, you want position 10, wantedChild will now be 2
					// So that means your view is child #2 in the ViewGroup:
					if (wantedChild < 0 || wantedChild >= list.getChildCount()) {
					  Log.d("Child Not Available", "");
					}
					else{
						RemovedPhotoURL=PhotoURl1;
						 v=list.getChildAt(wantedChild);
						 PhotoURl1="";
						 isImageSetFor1=false;
					}
				 
			 }
			 else if(TAG==2){
				 int wantedPosition = isngPenPicIndx; // Whatever position you're looking for
					int firstPosition = list.getFirstVisiblePosition() - list.getHeaderViewsCount(); // This is the same as child #0
					int wantedChild = wantedPosition - firstPosition;
					// Say, first visible position is 8, you want position 10, wantedChild will now be 2
					// So that means your view is child #2 in the ViewGroup:
					if (wantedChild < 0 || wantedChild >= list.getChildCount()) {
					  Log.d("Child Not Available", "");
					}
					else{
						RemovedPhotoURL=PhotoURl21;
						 v=list.getChildAt(wantedChild);
						 PhotoURl21="";
						 isImageSetFor21=false;
					}
				 
				 }
			 else if(TAG==3){
				 	int wantedPosition = isngPenPicIndx; // Whatever position you're looking for
					int firstPosition = list.getFirstVisiblePosition() - list.getHeaderViewsCount(); // This is the same as child #0
					int wantedChild = wantedPosition - firstPosition;
					// Say, first visible position is 8, you want position 10, wantedChild will now be 2
					// So that means your view is child #2 in the ViewGroup:
					if (wantedChild < 0 || wantedChild >= list.getChildCount()) {
					  Log.d("Child Not Available", "");
					}
					else{
						RemovedPhotoURL=PhotoURl31;
						 v=list.getChildAt(wantedChild);
						 PhotoURl31="";
						 isImageSetFor31=false;
					}
					 
					 
			 }
			 
			 
			 if(v!=null){
			 ImageButton i1=(ImageButton)v.findViewById(R.id.row_cell_addDefectPhoto_img1);
			 i1.setBackgroundDrawable(getResources().getDrawable(R.drawable.image_add));
			 }
		 }
		 else if(strFromImg.equalsIgnoreCase("img2")){
			 View v=null;
			 if(TAG==1){
				 int wantedPosition = isngPenPicIndx; // Whatever position you're looking for
					int firstPosition = list.getFirstVisiblePosition() - list.getHeaderViewsCount(); // This is the same as child #0
					int wantedChild = wantedPosition - firstPosition;
					// Say, first visible position is 8, you want position 10, wantedChild will now be 2
					// So that means your view is child #2 in the ViewGroup:
					if (wantedChild < 0 || wantedChild >= list.getChildCount()) {
					  Log.d("Child Not Available", "");
					}
					else{
						RemovedPhotoURL=PhotoURl2;
						 v=list.getChildAt(wantedChild);
						 PhotoURl2="";
						 isImageSetFor2=false;
					}
				 
			 }
			 else if(TAG==2){
				 int wantedPosition = isngPenPicIndx; // Whatever position you're looking for
					int firstPosition = list.getFirstVisiblePosition() - list.getHeaderViewsCount(); // This is the same as child #0
					int wantedChild = wantedPosition - firstPosition;
					// Say, first visible position is 8, you want position 10, wantedChild will now be 2
					// So that means your view is child #2 in the ViewGroup:
					if (wantedChild < 0 || wantedChild >= list.getChildCount()) {
					  Log.d("Child Not Available", "");
					}
					else{
						RemovedPhotoURL=PhotoURl22;
						 v=list.getChildAt(wantedChild);
						 PhotoURl22="";
						 isImageSetFor22=false;
					}
				 
			 }
			 else if(TAG==3){
				 int wantedPosition = isngPenPicIndx; // Whatever position you're looking for
					int firstPosition = list.getFirstVisiblePosition() - list.getHeaderViewsCount(); // This is the same as child #0
					int wantedChild = wantedPosition - firstPosition;
					// Say, first visible position is 8, you want position 10, wantedChild will now be 2
					// So that means your view is child #2 in the ViewGroup:
					if (wantedChild < 0 || wantedChild >= list.getChildCount()) {
					  Log.d("Child Not Available", "");
					}
					else{
						RemovedPhotoURL=PhotoURl32;
						 v=list.getChildAt(wantedChild);
						 PhotoURl32="";
						 isImageSetFor32=false;
					}
				 
			 }
			 
			 
			 if(v!=null){
			 ImageButton i2=(ImageButton)v.findViewById(R.id.row_cell_addDefectPhoto_img2);
			 i2.setBackgroundDrawable(getResources().getDrawable(R.drawable.image_add));
			 }
		 }
		 else if(strFromImg.equalsIgnoreCase("img3")){
			 View v=null;
			 if(TAG==1){
				 
				 int wantedPosition = isngPenPicIndx; // Whatever position you're looking for
					int firstPosition = list.getFirstVisiblePosition() - list.getHeaderViewsCount(); // This is the same as child #0
					int wantedChild = wantedPosition - firstPosition;
					// Say, first visible position is 8, you want position 10, wantedChild will now be 2
					// So that means your view is child #2 in the ViewGroup:
					if (wantedChild < 0 || wantedChild >= list.getChildCount()) {
					  Log.d("Child Not Available", "");
					}
					else{
						RemovedPhotoURL=PhotoURl3;
						v=list.getChildAt(wantedChild);
						 PhotoURl3="";
						 isImageSetFor3=false;
					}
				 
			 }
			 else if(TAG==2){
				 
				 	int wantedPosition = isngPenPicIndx; // Whatever position you're looking for
					int firstPosition = list.getFirstVisiblePosition() - list.getHeaderViewsCount(); // This is the same as child #0
					int wantedChild = wantedPosition - firstPosition;
					// Say, first visible position is 8, you want position 10, wantedChild will now be 2
					// So that means your view is child #2 in the ViewGroup:
					if (wantedChild < 0 || wantedChild >= list.getChildCount()) {
					  Log.d("Child Not Available", "");
					}
					else{
						RemovedPhotoURL=PhotoURl23;
						v=list.getChildAt(wantedChild);
						 PhotoURl23="";
						 isImageSetFor23=false;
					}
				 
			 }
			 else if(TAG==3){
				 int wantedPosition = isngPenPicIndx; // Whatever position you're looking for
					int firstPosition = list.getFirstVisiblePosition() - list.getHeaderViewsCount(); // This is the same as child #0
					int wantedChild = wantedPosition - firstPosition;
					// Say, first visible position is 8, you want position 10, wantedChild will now be 2
					// So that means your view is child #2 in the ViewGroup:
					if (wantedChild < 0 || wantedChild >= list.getChildCount()) {
					  Log.d("Child Not Available", "");
					}
					else{
						RemovedPhotoURL=PhotoURl33;
						 v=list.getChildAt(wantedChild);
						 PhotoURl33="";
						 isImageSetFor33=false;
					}
				 
			 }
			 if(v!=null){
			 ImageButton i3=(ImageButton)v.findViewById(R.id.row_cell_addDefectPhoto_img3);
			 i3.setBackgroundDrawable(getResources().getDrawable(R.drawable.image_add));
			 }
		 }
		 
		 String FilePath=Environment.getExternalStorageDirectory()+File.separator+"SnagReporter/Pictures/"+RemovedPhotoURL+".jpg";
		 File file = new File(FilePath);
		 //file.delete();
		 
		 RemovedPhotoURL="";
	 }
     catch(Exception e)
     {
     	Log.d("Exception", ""+e.getMessage());
     }
		 return true;
	 }
	 else if(item.getTitle()=="View Image"){
		 
		 try{
		 Intent i=new Intent(EditDefect.this,com.snagreporter.ViewImagePage.class);
		 if(strFromImg.equalsIgnoreCase("img1")){
			 if(TAG==1)
				 i.putExtra("PhotoURL", PhotoURl1);
			 else if(TAG==2)
				 i.putExtra("PhotoURL", PhotoURl21);
			 else if(TAG==3)
				 i.putExtra("PhotoURL", PhotoURl31);
		 }
		 else if(strFromImg.equalsIgnoreCase("img2")){
			 if(TAG==1)
				 i.putExtra("PhotoURL", PhotoURl2);
			 else if(TAG==2)
				 i.putExtra("PhotoURL", PhotoURl22);
			 else if(TAG==3)
				 i.putExtra("PhotoURL", PhotoURl32);
		 }
		 else if(strFromImg.equalsIgnoreCase("img3")){
			 if(TAG==1)
				 i.putExtra("PhotoURL", PhotoURl3);
			 else if(TAG==2)
				 i.putExtra("PhotoURL", PhotoURl23);
			 else if(TAG==3)
				 i.putExtra("PhotoURL", PhotoURl33);
		 }
		 
		 startActivity(i);
		 }
	        catch(Exception e)
	        {
	        	Log.d("Exception", ""+e.getMessage());
	        }
		 return true;
	 }
	}
	else if(as_index==iAreaIndx){
		
		try{
		int wantedPosition = iAreaIndx; // Whatever position you're looking for
		int firstPosition = list.getFirstVisiblePosition() - list.getHeaderViewsCount(); // This is the same as child #0
		int wantedChild = wantedPosition - firstPosition;
		// Say, first visible position is 8, you want position 10, wantedChild will now be 2
		// So that means your view is child #2 in the ViewGroup:
		if (wantedChild < 0 || wantedChild >= list.getChildCount()) {
		  Log.d("Child Not Available", "");
		}
		else{
			final View v=list.getChildAt(wantedChild);
			if(v!=null){
			TextView t=(TextView)v.findViewById(R.id.row_cell_addDefect_text);
			t.setText(""+item.getTitle().toString());
			SelectedArea=item.getTitle().toString();
		}
		
		}
		 }
        catch(Exception e)
        {
        	Log.d("Exception", ""+e.getMessage());
        }
	}
	else if(as_index==iCosttoindx)
	{
		try{
		int wantedPosition = iCosttoindx; // Whatever position you're looking for
		int firstPosition = list.getFirstVisiblePosition() - list.getHeaderViewsCount(); // This is the same as child #0
		int wantedChild = wantedPosition - firstPosition;
		// Say, first visible position is 8, you want position 10, wantedChild will now be 2
		// So that means your view is child #2 in the ViewGroup:
		if (wantedChild < 0 || wantedChild >= list.getChildCount()) {
		  Log.d("Child Not Available", "");
		}
		else{
			final View v=list.getChildAt(wantedChild);
			if(v!=null){
			TextView t=(TextView)v.findViewById(R.id.row_cell_addDefect_text);
			t.setText(""+item.getTitle().toString());
			costTo=item.getTitle().toString();
		}
		
		}
		 }
        catch(Exception e)
        {
        	Log.d("Exception", ""+e.getMessage());
        }
	}
	else if(as_index==iSnagPriorityindx)
	{
		try{
		int wantedPosition = iSnagPriorityindx; // Whatever position you're looking for
		int firstPosition = list.getFirstVisiblePosition() - list.getHeaderViewsCount(); // This is the same as child #0
		int wantedChild = wantedPosition - firstPosition;
		// Say, first visible position is 8, you want position 10, wantedChild will now be 2
		// So that means your view is child #2 in the ViewGroup:
		if (wantedChild < 0 || wantedChild >= list.getChildCount()) {
		  Log.d("Child Not Available", "");
		}
		else{
			final View v=list.getChildAt(wantedChild);
			if(v!=null){
			TextView t=(TextView)v.findViewById(R.id.row_cell_addDefect_text);
			t.setText(""+item.getTitle().toString());
			SnagPriority=item.getTitle().toString();
		}
		
		}
		 }
        catch(Exception e)
        {
        	Log.d("Exception", ""+e.getMessage());
        }
		
	}
	else if(as_index==iTradeIndex){
		
		try{

			
			int wantedPosition =iTradeIndex; // Whatever position you're looking for
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
			
		//	FMDBDatabaseAccess db=new FMDBDatabaseAccess(AddDefect.this);
			
			arrTDDistinctTrade=fmAccess.getTradeDetailsByTradeMasterID(SelectedTradeMasterID);
		  	
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
		  			arrTDDistinctInspectionGroup=fmAccess.getTradeDetailsByTradeTypeNewExtArea(SelectedTradeMasterID, curBuilding, strFl, SelectedTradeMasterID);
		  		}
		  		else if(isFloorExtArea)
		  		{
		  			arrTDDistinctInspectionGroup=fmAccess.getTradeDetailsByTradeTypeNewFloorArea(SelectedTradeMasterID, curFloorMaster, strFl, SelectedTradeMasterID);
		  		}
		  		else
		  			arrTDDistinctInspectionGroup=fmAccess.getTradeDetailsByTradeTypeNew(SelectedTradeMasterID, CurrentAPT,strFl,SelectedTradeMasterID);//getTradeDetailsByTradeType(SelectedTradeMasterID);//getTradeDetailsByTradeTypeNew(SelectedTradeMaster,CurrentAPT);////usd
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
		  		arrTDDistinctInspectionDescription=fmAccess.getTradeDetailsByInspectionGroup(SelectedTradeMasterID, arrTDDistinctInspectionGroup[0]);
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
		
				 wantedPosition =isbTradeIndex; // Whatever position you're looking for
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
				 
				
				wantedPosition =iTrdDescrptionindx; // Whatever position you're looking for
				 firstPosition = list.getFirstVisiblePosition() - list.getHeaderViewsCount(); // This is the same as child #0
				 wantedChild = wantedPosition - firstPosition;
				// Say, first visible position is Ä, you want position 10, wantedChild will now be 2
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
		catch(Exception e)
		{
			Log.d("Exception",""+e.getMessage());
		}
			
			/*
		int wantedPosition =2; // Whatever position you're looking for
		int firstPosition = list.getFirstVisiblePosition() - list.getHeaderViewsCount(); // This is the same as child #0
		int wantedChild = wantedPosition - firstPosition;
		// Say, first visible position is 8, you want position 10, wantedChild will now be 2
		// So that means your view is child #2 in the ViewGroup:
		if (wantedChild < 0 || wantedChild >= list.getChildCount()) {
		  Log.d("Child Not Available", "");
		}
		else{
			if(!item.getTitle().toString().equalsIgnoreCase("add more")){
		final View v=list.getChildAt(wantedChild);
		TextView t=(TextView)v.findViewById(R.id.row_cell_addDefect_text);
		t.setText(""+item.getTitle().toString());
		SelectedJobType=item.getTitle().toString();
		
		int index=0;
		for(int i=0;i<arrJobType.length;i++){
			if(SelectedJobType.equalsIgnoreCase(arrJobType[i].getJobType())){
				index=i;
				break;
			}
		}
		FMDBDatabaseAccess db=new FMDBDatabaseAccess(getApplicationContext());
		arrFaultType=db.getFaultType(arrJobType[index].getID());
		
		//@@@@
		if(AddedFaultType!=null && AddedFaultType.length>0){
			for(int i=0;i<AddedFaultType.length;i++){
				if(AddedFaultType[i].getJobTypeID().equalsIgnoreCase(arrJobType[index].getID())){
					if(arrFaultType==null || arrFaultType.length==0){
						arrFaultType=new FaultType[1];
						arrFaultType[0]=AddedFaultType[i];
					}
					else{
						FaultType[] temp=arrFaultType;
						arrFaultType=new FaultType[temp.length+1];
						for(int j=0;j<temp.length;j++){
							arrFaultType[j]=temp[j];
							if(j==temp.length-1){
								arrFaultType[j+1]=AddedFaultType[i];
							}
						}
					}
				}
			}
		}
		//@@@@
		
		
		if(arrFaultType!=null && arrFaultType.length>0){
			wantedPosition =3; // Whatever position you're looking for
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
			t2.setText(""+arrFaultType[0].getFaultType());
			SelectedFaultType=arrFaultType[0].getFaultType();
			}
		}
		else{
			if(AddedFaultType!=null && AddedFaultType.length>0){
				for(int i=0;i<AddedFaultType.length;i++){
					if(arrJobType[index].getID().equalsIgnoreCase(AddedFaultType[i].getJobTypeID())){
						if(arrFaultType==null || arrFaultType.length==0){
							arrFaultType=new FaultType[1];
							arrFaultType[0]=AddedFaultType[i];
						}
						else{
							FaultType[] temp=arrFaultType;
							arrFaultType=new FaultType[temp.length+1];
							for(int j=0;j<temp.length;j++){
								arrFaultType[j]=temp[j];
								if(j==temp.length-1){
									arrFaultType[j+1]=AddedFaultType[i];
								}
							}
						}
					}
				}
				
				if(arrFaultType==null || arrFaultType.length==0){
					wantedPosition =3; // Whatever position you're looking for
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
						t2.setText("Add more");
						SelectedFaultType="";
					}
				}
				else{
					wantedPosition =3; // Whatever position you're looking for
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
					t2.setText(""+arrFaultType[0].getFaultType());
					SelectedJobType=arrFaultType[0].getFaultType();
					}
				}
			}
			else{
				wantedPosition =3; // Whatever position you're looking for
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
					t2.setText("Add more");
					SelectedFaultType="";
				}
			}
		}
		
		}
			else{

					AlertDialog.Builder alert = new AlertDialog.Builder(EditDefect.this);
					alert.setTitle("Add Snag Type");
					final EditText input = new EditText(EditDefect.this);
					alert.setView(input);
					alert.setPositiveButton("Done", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int whichButton) {
							try{
						  String value = input.getText().toString().trim();
						  if(value.length()>0){
							  
							  int wantedPosition =2; // Whatever position you're looking for
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
								  JobType njt=new JobType();
								  njt.setID(id.toString());
								  njt.setJobType(""+value);
								  njt.setJobDetails("");
								  njt.setIsSyncedToWeb(false);
								  JobType[] tempjt=arrJobType;
								  arrJobType=new JobType[tempjt.length+1];
								  for(int i=0;i<tempjt.length;i++){
									  arrJobType[i]=tempjt[i];
									  if(i==tempjt.length-1){
										  arrJobType[i+1]=njt;
									  }
								  }
								  if(AddedSnagType==null || AddedSnagType.length==0){
									  AddedSnagType=new JobType[1];
									  AddedSnagType[0]=njt;
								  }
								  else{
									  JobType[] tempajt=AddedSnagType;
									  AddedSnagType=new JobType[tempajt.length+1];
									  for(int i=0;i<tempajt.length;i++){
										  AddedSnagType[i]=tempajt[i];
										  if(i==tempajt.length-1){
											  AddedSnagType[i+1]=njt;
										  }
									  }
								  }
								  
								  SelectedJobType=""+value;
								  arrFaultType=null;

									wantedPosition =3; // Whatever position you're looking for
									firstPosition = list.getFirstVisiblePosition() - list.getHeaderViewsCount(); // This is the same as child #0
									wantedChild = wantedPosition - firstPosition;
									// Say, first visible position is 8, you want position 10, wantedChild will now be 2
									// So that means your view is child #2 in the ViewGroup:
									if (wantedChild < 0 || wantedChild >= list.getChildCount()) {
									  Log.d("Child Not Available", "");
									}
									else{
									final View v3=list.getChildAt(wantedChild);
									TextView t3=(TextView)v3.findViewById(R.id.row_cell_addDefect_text);
									t3.setText("Add More");
									
									}
								
								  SelectedFaultType="";
								  
								  
							  }
						  
						  }
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
		
	
		 }
        catch(Exception e)
        {
        	Log.d("Exception", ""+e.getMessage());
        }	*/

	}
	else if(as_index==isbTradeIndex){
		try{
			
			//boolean isPreviosInspected=false;
			int wantedPosition =isbTradeIndex; // Whatever position you're looking for
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
			
			
			//FMDBDatabaseAccess db=new FMDBDatabaseAccess(AddDefect.this);
			
			
		  	if(arrTDDistinctInspectionGroup!=null && arrTDDistinctInspectionGroup.length>0)
		  		arrTDDistinctInspectionDescription=fmAccess.getTradeDetailsByInspectionGroup(SelectedTradeMasterID, SelectedInspectionGroup);
		  	
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
			wantedPosition =iTrdDescrptionindx; // Whatever position you're looking for
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
		  	/*if(arrCheckList!=null && arrCheckList.length>0){
		  		Intent in=new Intent(EditDefect.this,com.snagreporter.CheckListDescription.class);
		  		in.putExtra("Count", arrCheckList.length);
		  		for(int i=0;i<arrCheckList.length;i++)
		  			in.putExtra("CheckList"+i, arrCheckList[i]);
		  		startActivityForResult(in, 12345);
		  		return true;
		  	}*/
		  	
		adapter.notifyDataSetChanged();
			}
		/*int wantedPosition =3; // Whatever position you're looking for
		int firstPosition = list.getFirstVisiblePosition() - list.getHeaderViewsCount(); // This is the same as child #0
		int wantedChild = wantedPosition - firstPosition;
		// Say, first visible position is 8, you want position 10, wantedChild will now be 2
		// So that means your view is child #2 in the ViewGroup:
		if (wantedChild < 0 || wantedChild >= list.getChildCount()) {
		  Log.d("Child Not Available", "");
		}
		else{
			if(!item.getTitle().toString().equalsIgnoreCase("add more")){
		final View v=list.getChildAt(wantedChild);
		TextView t=(TextView)v.findViewById(R.id.row_cell_addDefect_text);
		t.setText(""+item.getTitle().toString());
		SelectedFaultType=item.getTitle().toString();
			}
			else{



				AlertDialog.Builder alert = new AlertDialog.Builder(EditDefect.this);
				alert.setTitle("Add Fault Type");
				final EditText input = new EditText(EditDefect.this);
				alert.setView(input);
				alert.setPositiveButton("Done", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						try{
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
							  nft.setJobType(SelectedJobType);
							  
							  if(arrFaultType==null || arrFaultType.length==0)
							  {  	arrFaultType=new FaultType[1];
							  		arrFaultType[0]=nft;
							  }
							  else{
								  FaultType[] temp=arrFaultType;
								  arrFaultType=new FaultType[temp.length+1];
								  for(int i=0;i<temp.length;i++){
									  arrFaultType[i]=temp[i];
									  if(i==temp.length-1){
										  arrFaultType[i+1]=nft;
									  }
								  }
							  }
							  
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
		}*/
	 }
    catch(Exception e)
    {
    	Log.d("Exception", ""+e.getMessage());
    }
	}
	else if(as_index==iTrdDescrptionindx)
	{
		try
		{
			
		}
		catch(Exception e)
		{
			Log.d("Exception",""+e.getMessage());
		}
	}
	else if(as_index==iAllctdindx)
	{
		try
		{

			int wantedPosition =iAllctdindx; // Whatever position you're looking for
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
		catch(Exception e)
		{
			Log.d("Exception",""+e.getMessage());
		}
	}
	else if(as_index==iInspctrindx)
	{
		try
		{
			
		}
		catch(Exception e)
		{
			Log.d("Exception",""+e.getMessage());
		}
	}
	else if(as_index==10){
		try{
		View v;
		int wantedPosition = 10; // Whatever position you're looking for
		int firstPosition = list.getFirstVisiblePosition() - list.getHeaderViewsCount(); // This is the same as child #0
		int wantedChild = wantedPosition - firstPosition;
		// Say, first visible position is 8, you want position 10, wantedChild will now be 2
		// So that means your view is child #2 in the ViewGroup:
		if (wantedChild < 0 || wantedChild >= list.getChildCount()) {
		  Log.d("Child Not Available", "");
		}
		else{
			v=list.getChildAt(wantedChild);
		
		
		TextView t=(TextView)v.findViewById(R.id.row_cell_addDefect_text);
		t.setText(""+item.getTitle().toString());
		SelectedStatus=item.getTitle().toString();
		//CurrentSnag.setSnagStatus(SelectedStatus);
		
		if(SelectedStatus.equalsIgnoreCase(Status[0])){
			if(list.getCount()>9){
				
				items.clear();
				items = new ArrayList<Item>();
				if(LoginType.equals(getResources().getString(R.string.strInspector).toString()))
					items.add(new Header(null, "EDIT SNAG",false,false,true,true));
				else
					items.add(new Header(null, "EDIT SNAG",false,false,true,false));
				populateData();
			    
				adapter = new TwoTextArrayAdapter(this, items);
		        list.setAdapter(adapter);
		        
		        list.setSelection(list.getAdapter().getCount() - 1);
			}
			

		}
		else if(SelectedStatus.equalsIgnoreCase(Status[1])){
			if(list.getCount()>10){
				items.remove(11);
				items.remove(11);
				
				
			}
			
			items.clear();
			items = new ArrayList<Item>();
			if(LoginType.equals(getResources().getString(R.string.strInspector).toString()))
				items.add(new Header(null, "EDIT SNAG",false,false,true,true));
			else
				items.add(new Header(null, "EDIT SNAG",false,false,true,false));
			populateData();
			
			/*ImageButton btn1=null,btn2=null,btn3=null;
			if(PhotoURl21!=null && PhotoURl21.length()>0){
    			String FilePath1=Environment.getExternalStorageDirectory()+File.separator+"SnagReporter/Pictures/"+PhotoURl21+".jpg";
    			File fooo = new File(FilePath1);
    			if(fooo.exists()){
    			Bitmap Img1 = BitmapFactory.decodeFile(FilePath1);
    			btn1=new ImageButton(this);
    			btn1.setImageBitmap(Img1);
    			//imgVw1.setBackgroundDrawable(btnI.getDrawable());
    	   		isImageSetFor21=true;
    			}
    		}
    		
    		if(PhotoURl22!=null && PhotoURl22.length()>0){
    			String FilePath2=Environment.getExternalStorageDirectory()+File.separator+"SnagReporter/Pictures/"+PhotoURl22+".jpg";
    			File fooo = new File(FilePath2);
    			if(fooo.exists()){
    			Bitmap Img2 = BitmapFactory.decodeFile(FilePath2);
    			 btn2=new ImageButton(this);
    			btn2.setImageBitmap(Img2);
    			//imgVw2.setBackgroundDrawable(btnI.getDrawable());
    	   		isImageSetFor22=true;
    			}
    		}
    		
    		if(PhotoURl23!=null && PhotoURl23.length()>0){
    			String FilePath2=Environment.getExternalStorageDirectory()+File.separator+"SnagReporter/Pictures/"+PhotoURl23+".jpg";
    			File fooo = new File(FilePath2);
    			if(fooo.exists()){
    			Bitmap Img2 = BitmapFactory.decodeFile(FilePath2);
    			 btn3=new ImageButton(this);
    			btn3.setImageBitmap(Img2);
    			//imgVw3.setBackgroundDrawable(btnI.getDrawable());
    	   		isImageSetFor23=true;
    			}
    		}
			
			
			items.add(new ListItemAddDefect(null, "Reinspection Date",SelectedDate));
    		items.add(new ListItemAddDefectPhoto(null, "Unresolved Photos",btn1,btn2,btn3,2));*/
			adapter = new TwoTextArrayAdapter(this, items);
	        list.setAdapter(adapter);
	        list.setSelection(list.getAdapter().getCount() - 1);

		}
		else{
			if(list.getCount()>10){
				//View v1=(View)items.get(8);
				items.remove(11);
				//int countw=items.size();
				
				items.remove(11);
				
			}
			
			items.clear();
			items = new ArrayList<Item>();
			if(LoginType.equals(getResources().getString(R.string.strInspector).toString()))
				items.add(new Header(null, "EDIT SNAG",false,false,true,true));
			else
				items.add(new Header(null, "EDIT SNAG",false,false,true,false));
			populateData();
			
			/*ImageButton btn1=null,btn2=null,btn3=null;
			if(PhotoURl31!=null && PhotoURl31.length()>0){
    			String FilePath1=Environment.getExternalStorageDirectory()+File.separator+"SnagReporter/Pictures/"+PhotoURl31+".jpg";
    			File fooo = new File(FilePath1);
    			if(fooo.exists()){
    			Bitmap Img1 = BitmapFactory.decodeFile(FilePath1);
    			btn1=new ImageButton(this);
    			btn1.setImageBitmap(Img1);
    			//imgVw1.setBackgroundDrawable(btnI.getDrawable());
    	   		isImageSetFor31=true;
    			}
    		}
    		
    		if(PhotoURl32!=null && PhotoURl32.length()>0){
    			String FilePath2=Environment.getExternalStorageDirectory()+File.separator+"SnagReporter/Pictures/"+PhotoURl32+".jpg";
    			File fooo = new File(FilePath2);
    			if(fooo.exists()){
    			Bitmap Img2 = BitmapFactory.decodeFile(FilePath2);
    			 btn2=new ImageButton(this);
    			btn2.setImageBitmap(Img2);
    			//imgVw2.setBackgroundDrawable(btnI.getDrawable());
    	   		isImageSetFor32=true;
    			}
    		}
    		
    		if(PhotoURl33!=null && PhotoURl33.length()>0){
    			String FilePath2=Environment.getExternalStorageDirectory()+File.separator+"SnagReporter/Pictures/"+PhotoURl33+".jpg";
    			File fooo = new File(FilePath2);
    			if(fooo.exists()){
    			Bitmap Img2 = BitmapFactory.decodeFile(FilePath2);
    			 btn3=new ImageButton(this);
    			btn3.setImageBitmap(Img2);
    			//imgVw3.setBackgroundDrawable(btnI.getDrawable());
    	   		isImageSetFor33=true;
    			}
    		}
			
			
			items.add(new ListItemAddDefect(null, "Resolve Date",SelectedDate));
    		items.add(new ListItemAddDefectPhoto(null, "Resolved Photos",null,null,null,3));*/
			adapter = new TwoTextArrayAdapter(this, items);
	        list.setAdapter(adapter);
	        
	        list.setSelection(list.getAdapter().getCount() - 1);

		}
	
		}
	 }
    catch(Exception e)
    {
    	Log.d("Exception", ""+e.getMessage());
    }
	}
	 
	
	 return true;
	}
	
protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) 
{
	 super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
	 try{
		 if(requestCode==101 && resultCode==101 && imageReturnedIntent!=null){
 	  		String FilePath=imageReturnedIntent.getExtras().getString("FilePath");
 	  		String FileName=imageReturnedIntent.getExtras().getString("FileName");
 	  		menuhandler.SaveAttachment(CurrentSnag.getProjectID(),CurrentSnag.getBuildingID(),CurrentSnag.getFloorID(), CurrentSnag.getApartmentID(),  CurrentSnag.getAptAreaID(), CurrentSnag.getID(),"", CurrentSnag.getSnagType(), FileName, FilePath);
 	  		//Toast.makeText(ProjectListPage.this, "File123="+FilePath, Toast.LENGTH_LONG).show();
 	  	 }
	 switch(requestCode)
	 {
    case 0:
        if(resultCode == RESULT_OK && imageReturnedIntent!=null)
        { 
        	try{
            Uri selectedImage = imageReturnedIntent.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String filePath = cursor.getString(columnIndex);
            cursor.close();
               
            if(strFromImg==null || strFromImg.length()==0){
            	if(strGetValues!=null)
            		strFromImg=strGetValues[11];
            }
            
            //BtnYourImg = BitmapFactory.decodeFile(filePath);
            //showImage();
          
            Intent i=new Intent(this,com.snagreporter.PhotoView.class);
            i.putExtra("filePath", filePath);
           // i.putExtra("BitmapImage", BtnYourImg);
            
//            if(strFromImg==null || strFromImg.length()==0){
//            	if(strGetValues!=null){
//        			strFromImg=strGetValues[11];
//        			//f = new File(strGetValues[12]);
//        			TAG=Integer.parseInt(strGetValues[13]);
//        		}
//            }
            
           i.putExtra("strFromImg", strFromImg);
           if(strFromImg.equalsIgnoreCase("img1")){
        	   if(TAG==1 && PhotoURl1!=null && PhotoURl1.length()>0)
        		   i.putExtra("PhotoUrl", PhotoURl1);
        	   else if(TAG==2 && PhotoURl21!=null && PhotoURl21.length()>0)
        		   i.putExtra("PhotoUrl", PhotoURl21);
        	   else if(TAG==3 && PhotoURl31!=null && PhotoURl31.length()>0)
        		   i.putExtra("PhotoUrl", PhotoURl31);
           }
           else if(strFromImg.equalsIgnoreCase("img2")){
        	   if(TAG==1 && PhotoURl2!=null && PhotoURl2.length()>0)
        		   i.putExtra("PhotoUrl", PhotoURl2);
        	   else if(TAG==2 && PhotoURl22!=null && PhotoURl22.length()>0)
        		   i.putExtra("PhotoUrl", PhotoURl22);
        	   else if(TAG==3 && PhotoURl32!=null && PhotoURl32.length()>0)
        		   i.putExtra("PhotoUrl", PhotoURl32);
           }
           else if(strFromImg.equalsIgnoreCase("img3")){
        	   if(TAG==1 && PhotoURl3!=null && PhotoURl3.length()>0)
        		   i.putExtra("PhotoUrl", PhotoURl3);
        	   else if(TAG==2 && PhotoURl23!=null && PhotoURl23.length()>0)
        		   i.putExtra("PhotoUrl", PhotoURl23);
        	   else if(TAG==3 && PhotoURl33!=null && PhotoURl33.length()>0)
        		   i.putExtra("PhotoUrl", PhotoURl33);
        	   
           }
           else if(RemovedPhotoURL!=null && RemovedPhotoURL.length()>0){
        	   i.putExtra("PhotoUrl", RemovedPhotoURL);
           }
           // startActivity(i);
          
           startActivityForResult(i, 2);
           break;
   	 }
        catch(Exception e)
        {
        	Log.d("Exception", ""+e.getMessage());
        }
        }
    case 2:
    {
   	 
   	 if(resultCode==10 && imageReturnedIntent!=null)
   	 {
   		 try{
   	// Log.d("strFromImgvw", "strFromImgvw");
   		//Toast.makeText(EditDefect.this, "Came here 10", Toast.LENGTH_LONG).show();
   	 strFromImgvw=imageReturnedIntent.getExtras().getString("strFromImgvw");
   	 strFilePath=imageReturnedIntent.getExtras().getString("strFilePath");
   	 String retUUID=imageReturnedIntent.getExtras().getString("UUID");
   	//BtnImageBmp=(Bitmap)getIntent().getParcelableExtra("btnBitmap");
   	 try{
   	 BtnImageBmp=decodeFile(strFilePath);
   	 }
   	 catch(OutOfMemoryError e)
   	 {
   		 Log.d("Exception",""+e.getMessage());
   	 }
   	 if(TAG==0){
   		 if(strGetValues!=null){
   			 TAG=Integer.parseInt(strGetValues[13].toString());
   		 }
   	 }
   	 
   	 if(strFromImgvw.equalsIgnoreCase("img1"))
   	 {
   		
   		
   		View v=null;
   		if(TAG==1){
   			int wantedPosition = isngPenPicIndx; // Whatever position you're looking for
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
				
				v=list.getChildAt(wantedChild);
				ImageButton btnI=new ImageButton(this);
				btnI.setImageBitmap(BtnImageBmp);
				// ImageButton i1=(ImageButton)v.findViewById(R.id.row_cell_addDefectPhoto_img1);
		   		// i1.setBackgroundDrawable(btnI.getDrawable());
		   		//ListItemAddDefectPhoto item=(ListItemAddDefectPhoto) items.get(wantedPosition);
   				items.set(wantedPosition,new ListItemAddDefectPhoto(null, "Snag Photos",btnI,imgVw2,imgVw3,1,true,PhotoURl1,PhotoURl2,PhotoURl3,false));
				adapter.notifyDataSetChanged();
			}
   			
   		}
   		else if(TAG==2){
   			int wantedPosition = iUnResPicIndx; // Whatever position you're looking for
			int firstPosition = list.getFirstVisiblePosition() - list.getHeaderViewsCount(); // This is the same as child #0
			int wantedChild = wantedPosition - firstPosition;
			// Say, first visible position is 8, you want position 10, wantedChild will now be 2
			// So that means your view is child #2 in the ViewGroup:
			PhotoURl21=retUUID;
			isImageSetFor21=true;
			if (wantedChild < 0 || wantedChild >= list.getChildCount()) {
			  Log.d("Child Not Available", "");
			}
			else{
				
				v=list.getChildAt(wantedChild);
				ImageButton btnI=new ImageButton(this);
				btnI.setImageBitmap(BtnImageBmp);
				 //ImageButton i1=(ImageButton)v.findViewById(R.id.row_cell_addDefectPhoto_img1);
		   		// i1.setBackgroundDrawable(btnI.getDrawable());
			//	ListItemAddDefectPhoto item=(ListItemAddDefectPhoto) items.get(wantedPosition);
   				items.set(wantedPosition,new ListItemAddDefectPhoto(null, "Unresolved Photos",btnI,imgVw22,imgVw23,2,true,PhotoURl21,PhotoURl22,PhotoURl23,false));
				adapter.notifyDataSetChanged();
			}
   			
   		}
   		else if(TAG==3){
   			int wantedPosition = iUnResPicIndx; // Whatever position you're looking for
			int firstPosition = list.getFirstVisiblePosition() - list.getHeaderViewsCount(); // This is the same as child #0
			int wantedChild = wantedPosition - firstPosition;
			// Say, first visible position is 8, you want position 10, wantedChild will now be 2
			// So that means your view is child #2 in the ViewGroup:
			PhotoURl31=retUUID;
			isImageSetFor31=true;
			if (wantedChild < 0 || wantedChild >= list.getChildCount()) {
			  Log.d("Child Not Available", "");
			}
			else{
				
				v=list.getChildAt(wantedChild);
				ImageButton btnI=new ImageButton(this);
				btnI.setImageBitmap(BtnImageBmp);
				// ImageButton i1=(ImageButton)v.findViewById(R.id.row_cell_addDefectPhoto_img1);
		   		// i1.setBackgroundDrawable(btnI.getDrawable());
				ListItemAddDefectPhoto item=(ListItemAddDefectPhoto) items.get(wantedPosition);
   				items.set(wantedPosition,new ListItemAddDefectPhoto(null, "Resolved Photos",btnI,imgVw32,imgVw33,3,true,PhotoURl31,PhotoURl32,PhotoURl33,false));
   				
				adapter.notifyDataSetChanged();
			}
   			
   		}
		
		
   		
   	 }
   	 else if(strFromImgvw.equalsIgnoreCase("img2"))
   	 {
   		View v=null;
   		if(TAG==1){
   			int wantedPosition = isngPenPicIndx; // Whatever position you're looking for
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
   			
   			v=list.getChildAt(wantedChild);
   			ImageButton btnI=new ImageButton(this);
   			btnI.setImageBitmap(BtnImageBmp);
   			// ImageButton i2=(ImageButton)v.findViewById(R.id.row_cell_addDefectPhoto_img2);
   	   		// i2.setBackgroundDrawable(btnI.getDrawable());
   			ListItemAddDefectPhoto item=(ListItemAddDefectPhoto) items.get(wantedPosition);
  			items.set(wantedPosition,new ListItemAddDefectPhoto(null, "Snag Photos",imgVw1,btnI,imgVw3,1,true,PhotoURl1,PhotoURl2,PhotoURl3,false));
   			adapter.notifyDataSetChanged();
			}
   		}
   		else if(TAG==2){
   			int wantedPosition = iUnResPicIndx; // Whatever position you're looking for
			int firstPosition = list.getFirstVisiblePosition() - list.getHeaderViewsCount(); // This is the same as child #0
			int wantedChild = wantedPosition - firstPosition;
			// Say, first visible position is 8, you want position 10, wantedChild will now be 2
			// So that means your view is child #2 in the ViewGroup:
			 PhotoURl22=retUUID;
			 isImageSetFor22=true;
			if (wantedChild < 0 || wantedChild >= list.getChildCount()) {
			  Log.d("Child Not Available", "");
			}
			else{
      		
        		v=list.getChildAt(wantedChild);
        		ImageButton btnI=new ImageButton(this);
        		btnI.setImageBitmap(BtnImageBmp);
        		 //ImageButton i2=(ImageButton)v.findViewById(R.id.row_cell_addDefectPhoto_img2);
           		// i2.setBackgroundDrawable(btnI.getDrawable());
        		ListItemAddDefectPhoto item=(ListItemAddDefectPhoto) items.get(wantedPosition);
           		items.set(wantedPosition,new ListItemAddDefectPhoto(null, "Unresolved Photos",imgVw21,btnI,imgVw23,2,true,PhotoURl21,PhotoURl22,PhotoURl23,false));
        		adapter.notifyDataSetChanged();
			}
        }
   		else if(TAG==3){
   			int wantedPosition = iUnResPicIndx; // Whatever position you're looking for
			int firstPosition = list.getFirstVisiblePosition() - list.getHeaderViewsCount(); // This is the same as child #0
			int wantedChild = wantedPosition - firstPosition;
			// Say, first visible position is 8, you want position 10, wantedChild will now be 2
			// So that means your view is child #2 in the ViewGroup:
			 PhotoURl32=retUUID;
			 isImageSetFor32=true;
			if (wantedChild < 0 || wantedChild >= list.getChildCount()) {
			  Log.d("Child Not Available", "");
			}
			else{
     		
       		v=list.getChildAt(wantedChild);
       		ImageButton btnI=new ImageButton(this);
    		btnI.setImageBitmap(BtnImageBmp);
    		ListItemAddDefectPhoto item=(ListItemAddDefectPhoto) items.get(wantedPosition);
       		items.set(wantedPosition,new ListItemAddDefectPhoto(null, "Resolved Photos",imgVw31,btnI,imgVw33,3,true,PhotoURl31,PhotoURl32,PhotoURl33,false));
       		
    		adapter.notifyDataSetChanged();
    		 //ImageButton i2=(ImageButton)v.findViewById(R.id.row_cell_addDefectPhoto_img2);
       		// i2.setBackgroundDrawable(btnI.getDrawable());
			}
       }
   		
		
   		
   	 }
   	 else if(strFromImgvw.equalsIgnoreCase("img3"))
   	 {
   		 View v=null;
   		 if(TAG==1){
   			int wantedPosition = isngPenPicIndx; // Whatever position you're looking for
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
   			
   			 v=list.getChildAt(wantedChild);
   			ImageButton btnI=new ImageButton(this);
   			btnI.setImageBitmap(BtnImageBmp);
   			// ImageButton i3=(ImageButton)v.findViewById(R.id.row_cell_addDefectPhoto_img3);
   	   		// i3.setBackgroundDrawable(btnI.getDrawable());
   			ListItemAddDefectPhoto item=(ListItemAddDefectPhoto) items.get(wantedPosition);
  			items.set(wantedPosition,new ListItemAddDefectPhoto(null, "Snag Photos",imgVw1,imgVw2,btnI,1,true,PhotoURl1,PhotoURl2,PhotoURl3,false));
   			adapter.notifyDataSetChanged();
			}
   		 }
   		 else if(TAG==2){
   			int wantedPosition = iUnResPicIndx; // Whatever position you're looking for
			int firstPosition = list.getFirstVisiblePosition() - list.getHeaderViewsCount(); // This is the same as child #0
			int wantedChild = wantedPosition - firstPosition;
			// Say, first visible position is 8, you want position 10, wantedChild will now be 2
			// So that means your view is child #2 in the ViewGroup:
			PhotoURl23=retUUID;
			isImageSetFor23=true;
			if (wantedChild < 0 || wantedChild >= list.getChildCount()) {
			  Log.d("Child Not Available", "");
			}
			else{
   			
  			 v=list.getChildAt(wantedChild);
  			ImageButton btnI=new ImageButton(this);
  			btnI.setImageBitmap(BtnImageBmp);
  			ListItemAddDefectPhoto item=(ListItemAddDefectPhoto) items.get(wantedPosition);
 			items.set(wantedPosition,new ListItemAddDefectPhoto(null, "Unresolved Photos",imgVw21,imgVw22,btnI,2,true,PhotoURl21,PhotoURl22,PhotoURl23,false));
  			adapter.notifyDataSetChanged();
  			// ImageButton i3=(ImageButton)v.findViewById(R.id.row_cell_addDefectPhoto_img3);
  	   		 //i3.setBackgroundDrawable(btnI.getDrawable());
			}
   		 }
   		 else if(TAG==3){
   			int wantedPosition = iUnResPicIndx; // Whatever position you're looking for
			int firstPosition = list.getFirstVisiblePosition() - list.getHeaderViewsCount(); // This is the same as child #0
			int wantedChild = wantedPosition - firstPosition;
			// Say, first visible position is 8, you want position 10, wantedChild will now be 2
			// So that means your view is child #2 in the ViewGroup:
			PhotoURl33=retUUID;
			isImageSetFor33=true;
			if (wantedChild < 0 || wantedChild >= list.getChildCount()) {
			  Log.d("Child Not Available", "");
			}
			else{
   			
  			 v=list.getChildAt(wantedChild);
  			ImageButton btnI=new ImageButton(this);
  			btnI.setImageBitmap(BtnImageBmp);
  			ListItemAddDefectPhoto item=(ListItemAddDefectPhoto) items.get(wantedPosition);
 			items.set(wantedPosition,new ListItemAddDefectPhoto(null, "Resolved Photos",imgVw31,imgVw32,btnI,3,true,PhotoURl31,PhotoURl32,PhotoURl33,false));
 			
 			adapter.notifyDataSetChanged();
  			// ImageButton i3=(ImageButton)v.findViewById(R.id.row_cell_addDefectPhoto_img3);
  	   		// i3.setBackgroundDrawable(btnI.getDrawable());
  			 
			}
   		 }
   			 
		
   		
   	 }
   	 //Log.d("strFromImgvw",""+strFromImgvw);
   	Handler handler = new Handler();
  	 handler.postDelayed(timedTask, 1000);
	 }
     catch(Exception e)
     {
     	Log.d("Exception", ""+e.getMessage());
     }
   	 }
   	 break;
    }
    case CAMERA_PIC_REQUEST:
    {
    	
    	try{
    	if(resultCode!=0){
        	if(f==null){
                if(cameraFile!=null)
                    f = new File(cameraFile);
                else
                    Log.e("check", "camera file object null line no 279");
            }else
                Log.e("check", f.getAbsolutePath());
        	
        	 // mBitmap = BitmapFactory.decodeFile(f.getAbsolutePath());
       	
        	if(f==null){
        		if(strGetValues!=null){
        			strFromImg=strGetValues[11];
        			f = new File(strGetValues[12]);
        			TAG=Integer.parseInt(strGetValues[13]);
        		}
        	}
        	
       	 String PhotoURL="";
       	if(strFromImg.equalsIgnoreCase("img1") && PhotoURl1!=null && PhotoURl1.length()>0){
       		PhotoURL=PhotoURl1;
        }
        else if(strFromImg.equalsIgnoreCase("img2") && PhotoURl2!=null && PhotoURl2.length()>0){
        	PhotoURL=PhotoURl2;
        }
        else if(strFromImg.equalsIgnoreCase("img3") && PhotoURl3!=null && PhotoURl3.length()>0){
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

   	 }
        catch(Exception e)
        {
        	Log.d("Exception", ""+e.getMessage());
        }
    /*	
   	 mBitmap = (Bitmap) imageReturnedIntent.getExtras().get("data");
   	
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
        startActivityForResult(i, 2);*/
    
    }
    
    
    
    }
	 if(resultCode==10001){
			setResult(10001);
			finish();
	}
	 else if(resultCode==10002){
		setResult(10002);
		finish();
	}
	 }
	 catch(Exception e){
		 Log.d("Error=", ""+e.getMessage());
		 //Toast.makeText(EditDefect.this, "Error="+e.getMessage(), Toast.LENGTH_LONG).show();
	 }
}


private Runnable timedTask = new Runnable(){

	  
	  public void run() {
	   // TODO Auto-generated method stub
		synchronized(this){
			try{
			if(strFromImgvw.equalsIgnoreCase("img1"))
		   	 {
		   		
		   		
		   		View v=null;
		   		if(TAG==1){
		   			int wantedPosition = isngPenPicIndx; // Whatever position you're looking for
					int firstPosition = list.getFirstVisiblePosition() - list.getHeaderViewsCount(); // This is the same as child #0
					int wantedChild = wantedPosition - firstPosition;
					// Say, first visible position is 8, you want position 10, wantedChild will now be 2
					// So that means your view is child #2 in the ViewGroup:
					
					if (wantedChild < 0 || wantedChild >= list.getChildCount()) {
					  Log.d("Child Not Available", "");
					}
					else{
						
						v=list.getChildAt(wantedChild);
						ImageButton btnI=new ImageButton(EditDefect.this);
						btnI.setImageBitmap(BtnImageBmp);
						 ImageButton i1=(ImageButton)v.findViewById(R.id.row_cell_addDefectPhoto_img1);
				   		 i1.setBackgroundDrawable(btnI.getDrawable());
					}
		   			
		   		}
		   		else if(TAG==2){
		   			int wantedPosition = iUnResPicIndx; // Whatever position you're looking for
					int firstPosition = list.getFirstVisiblePosition() - list.getHeaderViewsCount(); // This is the same as child #0
					int wantedChild = wantedPosition - firstPosition;
					// Say, first visible position is 8, you want position 10, wantedChild will now be 2
					// So that means your view is child #2 in the ViewGroup:
					
					if (wantedChild < 0 || wantedChild >= list.getChildCount()) {
					  Log.d("Child Not Available", "");
					}
					else{
						
						v=list.getChildAt(wantedChild);
						ImageButton btnI=new ImageButton(EditDefect.this);
						btnI.setImageBitmap(BtnImageBmp);
						 ImageButton i1=(ImageButton)v.findViewById(R.id.row_cell_addDefectPhoto_img1);
				   		 i1.setBackgroundDrawable(btnI.getDrawable());
					}
		   			
		   		}
		   		else if(TAG==3){
		   			int wantedPosition;
		   			if(CurrentSnag.getReInspectedUnresolvedDate()!=null && !CurrentSnag.getReInspectedUnresolvedDate().equalsIgnoreCase(""))
		   			 wantedPosition = iReslvPicIndx; // Whatever position you're looking for
		   			else
		   				wantedPosition = iUnResPicIndx;
					int firstPosition = list.getFirstVisiblePosition() - list.getHeaderViewsCount(); // This is the same as child #0
					int wantedChild = wantedPosition - firstPosition;
					// Say, first visible position is 8, you want position 10, wantedChild will now be 2
					// So that means your view is child #2 in the ViewGroup:
					
					if (wantedChild < 0 || wantedChild >= list.getChildCount()) {
					  Log.d("Child Not Available", "");
					}
					else{
						
						v=list.getChildAt(wantedChild);
						ImageButton btnI=new ImageButton(EditDefect.this);
						btnI.setImageBitmap(BtnImageBmp);
						 ImageButton i1=(ImageButton)v.findViewById(R.id.row_cell_addDefectPhoto_img1);
				   		 i1.setBackgroundDrawable(btnI.getDrawable());
					}
		   			
		   		}
				
				
		   		
		   	 }
		   	 else if(strFromImgvw.equalsIgnoreCase("img2"))
		   	 {
		   		View v=null;
		   		if(TAG==1){
		   			int wantedPosition = isngPenPicIndx; // Whatever position you're looking for
					int firstPosition = list.getFirstVisiblePosition() - list.getHeaderViewsCount(); // This is the same as child #0
					int wantedChild = wantedPosition - firstPosition;
					// Say, first visible position is 8, you want position 10, wantedChild will now be 2
					// So that means your view is child #2 in the ViewGroup:
					
					if (wantedChild < 0 || wantedChild >= list.getChildCount()) {
					  Log.d("Child Not Available", "");
					}
					else{
		   			
		   			v=list.getChildAt(wantedChild);
		   			ImageButton btnI=new ImageButton(EditDefect.this);
					btnI.setImageBitmap(BtnImageBmp);
					 ImageButton i2=(ImageButton)v.findViewById(R.id.row_cell_addDefectPhoto_img2);
			   		 i2.setBackgroundDrawable(btnI.getDrawable());
					}
		   		}
		   		else if(TAG==2){
		   			int wantedPosition = iUnResPicIndx; // Whatever position you're looking for
					int firstPosition = list.getFirstVisiblePosition() - list.getHeaderViewsCount(); // This is the same as child #0
					int wantedChild = wantedPosition - firstPosition;
					// Say, first visible position is 8, you want position 10, wantedChild will now be 2
					// So that means your view is child #2 in the ViewGroup:
					 
					if (wantedChild < 0 || wantedChild >= list.getChildCount()) {
					  Log.d("Child Not Available", "");
					}
					else{
		      		
		        		v=list.getChildAt(wantedChild);
		        		ImageButton btnI=new ImageButton(EditDefect.this);
						btnI.setImageBitmap(BtnImageBmp);
						 ImageButton i2=(ImageButton)v.findViewById(R.id.row_cell_addDefectPhoto_img2);
				   		 i2.setBackgroundDrawable(btnI.getDrawable());
					}
		        }
		   		else if(TAG==3){
		   			int wantedPosition;
		   			if(CurrentSnag.getReInspectedUnresolvedDate()!=null && !CurrentSnag.getReInspectedUnresolvedDate().equalsIgnoreCase(""))
		   			 wantedPosition = iReslvPicIndx; // Whatever position you're looking for
		   			else
		   				wantedPosition = iUnResPicIndx;
					int firstPosition = list.getFirstVisiblePosition() - list.getHeaderViewsCount(); // This is the same as child #0
					int wantedChild = wantedPosition - firstPosition;
					// Say, first visible position is 8, you want position 10, wantedChild will now be 2
					// So that means your view is child #2 in the ViewGroup:
					
					if (wantedChild < 0 || wantedChild >= list.getChildCount()) {
					  Log.d("Child Not Available", "");
					}
					else{
		     		
		       		v=list.getChildAt(wantedChild);
		       		ImageButton btnI=new ImageButton(EditDefect.this);
					btnI.setImageBitmap(BtnImageBmp);
					 ImageButton i2=(ImageButton)v.findViewById(R.id.row_cell_addDefectPhoto_img2);
			   		 i2.setBackgroundDrawable(btnI.getDrawable());
					}
		       }
		   		
				
		   		
		   	 }
		   	 else if(strFromImgvw.equalsIgnoreCase("img3"))
		   	 {
		   		 View v=null;
		   		 if(TAG==1){
		   			int wantedPosition = isngPenPicIndx; // Whatever position you're looking for
					int firstPosition = list.getFirstVisiblePosition() - list.getHeaderViewsCount(); // This is the same as child #0
					int wantedChild = wantedPosition - firstPosition;
					// Say, first visible position is 8, you want position 10, wantedChild will now be 2
					// So that means your view is child #2 in the ViewGroup:
					 
					if (wantedChild < 0 || wantedChild >= list.getChildCount()) {
					  Log.d("Child Not Available", "");
					}
					else{
		   			
		   			 v=list.getChildAt(wantedChild);
		   			ImageButton btnI=new ImageButton(EditDefect.this);
					btnI.setImageBitmap(BtnImageBmp);
					 ImageButton i3=(ImageButton)v.findViewById(R.id.row_cell_addDefectPhoto_img3);
			   		 i3.setBackgroundDrawable(btnI.getDrawable());
					}
		   		 }
		   		 else if(TAG==2){
		   			int wantedPosition = iUnResPicIndx; // Whatever position you're looking for
					int firstPosition = list.getFirstVisiblePosition() - list.getHeaderViewsCount(); // This is the same as child #0
					int wantedChild = wantedPosition - firstPosition;
					// Say, first visible position is 8, you want position 10, wantedChild will now be 2
					// So that means your view is child #2 in the ViewGroup:
					
					if (wantedChild < 0 || wantedChild >= list.getChildCount()) {
					  Log.d("Child Not Available", "");
					}
					else{
		   			
		  			 v=list.getChildAt(wantedChild);
		  			ImageButton btnI=new ImageButton(EditDefect.this);
					btnI.setImageBitmap(BtnImageBmp);
					 ImageButton i3=(ImageButton)v.findViewById(R.id.row_cell_addDefectPhoto_img3);
			   		 i3.setBackgroundDrawable(btnI.getDrawable());
					}
		   		 }
		   		 else if(TAG==3){
		   			int wantedPosition;
		   			if(CurrentSnag.getReInspectedUnresolvedDate()!=null && !CurrentSnag.getReInspectedUnresolvedDate().equalsIgnoreCase(""))
		   			 wantedPosition = iReslvPicIndx; // Whatever position you're looking for
		   			else
		   				wantedPosition = iUnResPicIndx;
					int firstPosition = list.getFirstVisiblePosition() - list.getHeaderViewsCount(); // This is the same as child #0
					int wantedChild = wantedPosition - firstPosition;
					// Say, first visible position is 8, you want position 10, wantedChild will now be 2
					// So that means your view is child #2 in the ViewGroup:
					
					if (wantedChild < 0 || wantedChild >= list.getChildCount()) {
					  Log.d("Child Not Available", "");
					}
					else{
		   			
		  			 v=list.getChildAt(wantedChild);
		  			ImageButton btnI=new ImageButton(EditDefect.this);
					btnI.setImageBitmap(BtnImageBmp);
					 ImageButton i3=(ImageButton)v.findViewById(R.id.row_cell_addDefectPhoto_img3);
			   		 i3.setBackgroundDrawable(btnI.getDrawable());
					}
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

	public void UpdateClick(View v){
	try{
		
		if(!SelectedStatus.equalsIgnoreCase(Status[2]))
		{
			FMDBDatabaseAccess db=new FMDBDatabaseAccess(EditDefect.this);
//			if(AddedSnagType!=null && AddedSnagType.length>0){
//				for(int i=0;i<AddedSnagType.length;i++)
//					db.insertORUpdateJobType(AddedSnagType[i]);
//			}
//			if(AddedFaultType!=null && AddedFaultType.length>0){
//				for(int i=0;i<AddedFaultType.length;i++)
//					db.insertORUpdateFaultType(AddedFaultType[i]);
//			}
			//SnagMaster obj=new SnagMaster();
			//obj.setID(CurrentSnag.getID());
			CurrentSnag.setSnagType(SelectedTradeType);//SelectedJobType
			CurrentSnag.setSnagDetails(SelectedJobDetails);
			CurrentSnag.setFaultType(SelectedInspectionGroup);//(SelectedFaultType);
			CurrentSnag.setExpectedInspectionDate(""+ExpectedDate);
			//obj.setAptAreaID(CurrentSnag.getAptAreaID());
			CurrentSnag.setPictureURL1(PhotoURl1);
			CurrentSnag.setPictureURL2(PhotoURl2);
			CurrentSnag.setPictureURL3(PhotoURl3);

			if(isAptmt){
				CurrentSnag.setApartmentID(CurrentAPT.getID());
				CurrentSnag.setApartment(CurrentAPT.getApartmentNo());
			}
			else{
				if(CurrentSFA!=null)
				{
					CurrentSnag.setApartmentID(CurrentSFA.getID());
					CurrentSnag.setApartment(CurrentSFA.getAreaName());
					
				}
				else
				{
					CurrentSnag.setApartmentID("");
					CurrentSnag.setApartment("");
				}
				
			}
			CurrentSnag.setAptAreaName(SelectedArea);
			CurrentSnag.setAptAreaID(SelectedAreaID);
			CurrentSnag.setReportDate(CurrentSnag.getReportDate().toString());
			CurrentSnag.setSnagStatus(SelectedStatus);
			CurrentSnag.setReInspectedUnresolvedDate(""+SelectedDate);
			CurrentSnag.setReInspectedUnresolvedDatePictureURL1(""+PhotoURl21);
			CurrentSnag.setReInspectedUnresolvedDatePictureURL2(""+PhotoURl22);
			CurrentSnag.setReInspectedUnresolvedDatePictureURL3(""+PhotoURl23);
			CurrentSnag.setResolveDate(""+SelectedDate);
			CurrentSnag.setResolveDatePictureURL1(""+PhotoURl31);
			CurrentSnag.setResolveDatePictureURL2(""+PhotoURl32);
			CurrentSnag.setResolveDatePictureURL3(""+PhotoURl33);
			CurrentSnag.setCost(Double.parseDouble(selectedCost));
			CurrentSnag.setCostTo(costTo);
			CurrentSnag.setSnagPriority(SnagPriority);
			CurrentSnag.setPercentageCompleted(CurrentSnag.getPercentageCompleted());
			
			if(CurrentSnag.getStatusForUpload().equalsIgnoreCase("Inserted") && !CurrentSnag.getIsDataSyncToWeb())
				CurrentSnag.setStatusForUpload("Inserted");
			else	
				CurrentSnag.setStatusForUpload("Modified");
			
			CurrentSnag.setIsDataSyncToWeb(false);
			
			if(SelectedEmployeeIndex!=-1)
			{
				CurrentSnag.setAllocatedTo(arrEmployee[SelectedEmployeeIndex].getEmpCode());
				CurrentSnag.setAllocatedToName(arrEmployee[SelectedEmployeeIndex].getEmpName()+" "+arrEmployee[SelectedEmployeeIndex].getEmpLastName());
				}
				else
				{
					CurrentSnag.setAllocatedTo(""+CurrentSnag.getAllocatedTo().toString());
					CurrentSnag.setAllocatedToName(""+CurrentSnag.getAllocatedToName().toString());
						
				}
			db.EditPageUpdateIntoSnagMaster(CurrentSnag);
			
			if(CurrentSnag.getStatusForUpload().equalsIgnoreCase("modified")){
				UpdateDataToTheDataBase task=new UpdateDataToTheDataBase();
				task.obj=CurrentSnag;
				task.execute(10);
			}
			else{
				SaveNewDataToTheDataBase task=new SaveNewDataToTheDataBase();
				task.obj=CurrentSnag;
				task.execute(0);
			}
				
			finish();
		}
		else
		{
			finish();
			
		}
		
	}
	catch(Exception e){
		Log.d("Error=", ""+e.getMessage());
	}
	
}
	public String GetUTCdatetimeAsString()
	{
		 final String DATEFORMAT = "dd-MM-yyyy";

	    final SimpleDateFormat sdf = new SimpleDateFormat(DATEFORMAT);
	    sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
	   String utcTime = sdf.format(new Date());

	    return utcTime;
	}
	
	public void uploadImage2(String url){
		try{
		if(url!=null && url.length()!=0 && !url.equals("")){
		String METHOD_NAME = "UploadFileDataParam";
		String NAMESPACE = "http://tempuri.org/";
		String URL = ""+getResources().getString(R.string.WS_URL).toString()+"SnagReporter_WebS.asmx";//SnagReporter_WCF.svc
		String SOAP_ACTION = "http://tempuri.org/UploadFileDataParam";//
		String res = "";
		try {
		    SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
		    
		    SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		    new MarshalBase64().register(envelope);
		    envelope.dotNet = true;
		    envelope.setOutputSoapObject(request);
		    
		    
		    String FileName=""+url+".jpg";
		    File file=new File(Environment.getExternalStorageDirectory()+File.separator+"SnagReporter/Pictures/"+FileName);
			//strFilePath=file.toString();
		    String FilePath=file.toString();
		   Bitmap myImg=decodeFile(FilePath);
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
		 catch(Exception e){
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
	
	
	protected class UpdateDataToTheDataBase extends AsyncTask<Integer , Integer, Void> {
    	ProgressDialog mProgressDialog = new ProgressDialog(EditDefect.this);
    	JSONObject jObject;
    	String output="";
    	SnagMaster obj=null;
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
	    		    
	    		    
	    		    String CoumnNames="SnagType~SnagDetails~FaultType~ExpectedInspectionDate~PictureURL1~PictureURL2~PictureURL3~ApartmentID~Apartment~AptAreaName~ReportDate~SnagStatus~ReInspectedUnresolvedDate~ReInspectedUnresolvedDatePictureURL1~ReInspectedUnresolvedDatePictureURL2~ReInspectedUnresolvedDatePictureURL3~ResolveDate~ResolveDatePictureURL1~ResolveDatePictureURL2~ResolveDatePictureURL3~Cost~CostTo~PriorityLevel~AllocatedTo~AllocatedToName";
	    		    String Values=""+obj.getSnagType()+"~"+obj.getSnagDetails()+"~"+obj.getFaultType()+"~"+obj.getExpectedInspectionDate()+"~"+obj.getPictureURL1()+"~"+obj.getPictureURL2()+"~"+obj.getPictureURL3()+"~"+obj.getApartmentID()+"~"+obj.getApartment()+"~"+obj.getAptAreaName()+"~"+obj.getReportDate()+"~"+obj.getSnagStatus()+"~"+obj.getReInspectedUnresolvedDate()+"~"+obj.getReInspectedUnresolvedDatePictureURL1()+"~"+obj.getReInspectedUnresolvedDatePictureURL2()+"~"+obj.getReInspectedUnresolvedDatePictureURL3()+"~"+obj.getResolveDate()+"~"+obj.getResolveDatePictureURL1()+"~"+obj.getResolveDatePictureURL2()+"~"+obj.getResolveDatePictureURL3()+"~"+obj.getCost()+"~"+obj.getCostTo()+"~"+obj.getSnagPriority()+"~"+obj.getAllocatedTo()+"~"+obj.getAllocatedToName();
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

    			
    		FMDBDatabaseAccess db=new FMDBDatabaseAccess(EditDefect.this);
    		//obj.setStatusForUpload("Modified");
        	if(isOnline)
        		obj.setIsDataSyncToWeb(true);
        	else
        		obj.setIsDataSyncToWeb(false);
        	
    		db.UpdateIsSyncedToWebSnagMaster(obj);
    		
    		
    		db=null;
        	
        	//finish();
        	
       	 }
            catch(Exception e)
            {
            	Log.d("Exception", ""+e.getMessage());
            }
        	
            
        }
         
}
	
	protected class SaveNewDataToTheDataBase extends AsyncTask<Integer , Integer, Void> {
		ProgressDialog mProgressDialog = new ProgressDialog(EditDefect.this);
    	JSONObject jObject;
    	String output="";
    	SnagMaster obj=null;
    	
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
        	//obj.setStatusForUpload("Inserted");
        	if(isOnline)
        		obj.setIsDataSyncToWeb(true);
        	else
        		obj.setIsDataSyncToWeb(false);
    		FMDBDatabaseAccess db=new FMDBDatabaseAccess(EditDefect.this);
    		db.UpdateIsSyncedToWebSnagMaster(obj);
    		
    		db=null;
  		 }
        catch(Exception e)
        {
        	Log.d("Exception", ""+e.getMessage());
        }
    		//finish();
        	
        	
        	
            
        }
         
}
	protected class SyncJobtype extends AsyncTask<Integer , Integer, Void> {
		JobType objJob=null;
		boolean isAlldone=false;
	    @Override
	    protected void onPreExecute() {
	    	try{
	    	FMDBDatabaseAccess db=new FMDBDatabaseAccess(EditDefect.this);
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
	    		    String TableName="Trades";
	    		    
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
	    	FMDBDatabaseAccess db=new FMDBDatabaseAccess(EditDefect.this);
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
	    	FMDBDatabaseAccess db=new FMDBDatabaseAccess(EditDefect.this);
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
	    	FMDBDatabaseAccess db=new FMDBDatabaseAccess(EditDefect.this);
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
	
	 protected class DownloadSnagInBackground extends AsyncTask<Integer , Integer, Void> 
	 {
		 ProgressDialog mProgressDialog = new ProgressDialog(EditDefect.this);
	    	
			String output="";
			JSONObject jObject;
	        @Override
	        protected void onPreExecute()
	        {  
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
	        protected Void doInBackground(Integer... params)
	        {
	        	try
	        	{
	        		String METHOD_NAME = "GetSnagDetails";
    	    		String NAMESPACE = "http://tempuri.org/";
    	    		//String URL = ""+getResources().getString(R.string.WS_URL).toString()+"SnagReporter_WebS.asmx";//SnagReporter_WCF.svc
    	    		String URL ="http://20.10.1.44:4442/SnagReporter_WebS.asmx"; 
    	    		String SOAP_ACTION = "http://tempuri.org/GetSnagDetails";//
    	    		String res = "";
    	    		try
    	    		{
    	    			SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
    	    		    
    	    		    SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
    	    		    envelope.dotNet = true;
    	    		    envelope.setOutputSoapObject(request);
    	    		    
    	    		 request.addProperty("_strSnagID",CurrentSnag.getID());
    	    		    
    	    		    HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
    	    		    androidHttpTransport.call(SOAP_ACTION, envelope);
    	    		    Object resonse=envelope.getResponse();
    	    		    String resultData = resonse.toString();
    	    		    
    	    		    
    	    			
    					jObject = new JSONObject(resultData);
    					
    					//mProgressDialog.dismiss();
    	    		    res = resultData;
 
    	    			
    	    		}
    	    		catch (Exception e)
    	    		{
						Log.d("error", ""+e.getMessage());
					}
	        	}
	        	catch (Exception e)
	        	{
					Log.d("Download error", ""+e.getMessage());
				}
	        	
	        	return null;
	        }
	        @Override
            protected void onPostExecute(Void result)
       	 {
            	  
            	mProgressDialog.dismiss();
            	try
            	{
            		 if(jObject!=null)
		    		 {
		    			 JSONArray arr = jObject.getJSONArray("Data");
		    			 if(arr!=null)
		    			 {
		    				 for(int i=0;i<arr.length();i++)
		    				 {
		    					 JSONObject geometry = arr.getJSONObject(i);
		    					 parseData(geometry);
							
							//parseFriends(geometry);
		    				 }
		    			 }
		    		 }
            		 FMDBDatabaseAccess fdb=new FMDBDatabaseAccess(EditDefect.this);
            		 SnagMaster obj=new SnagMaster();
            		 
            		 obj=fdb.getSnagByID(CurrentSnag.getID());
            		CurrentSnag=obj;
            		continueprocess();
            		
            	}
            	catch (Exception e)
            	{
					Log.d("error dowanloaddate", ""+e.getMessage());
				}
       	 }
	        
	 }
	 public void parseData(JSONObject jsObj)
	 {
		 try
		 {
			 SnagMaster obj=new SnagMaster();
			 obj.setID(jsObj.getString("ID"));
			 obj.setSnagType(jsObj.getString("SnagType"));
     		obj.setSnagDetails(jsObj.getString("SnagDetails"));
     		obj.setPictureURL1(jsObj.getString("PictureURL1"));
     		obj.setPictureURL2(jsObj.getString("PictureURL2"));
     		obj.setPictureURL3(jsObj.getString("PictureURL3"));
     		obj.setProjectID(jsObj.getString("ProjectID"));
     		obj.setProjectName(jsObj.getString("ProjectName"));
     		obj.setBuildingID(jsObj.getString("BuildingID"));
     		obj.setBuildingName(jsObj.getString("BuildingName"));
     		obj.setFloorID(jsObj.getString("FloorID"));
     		obj.setFloor(jsObj.getString("Floor"));
     		obj.setApartmentID(jsObj.getString("ApartmentID"));
     		obj.setApartment(jsObj.getString("Apartment"));
     		obj.setAptAreaName(jsObj.getString("AptAreaName"));
     		obj.setAptAreaID(jsObj.getString("AptAreaID"));
     		obj.setReportDate(jsObj.getString("ReportDate"));
     		obj.setSnagStatus(jsObj.getString("SnagStatus"));
     		obj.setResolveDate(jsObj.getString("ResolveDate"));
     		obj.setInspectorID(jsObj.getString("InspectorID"));
     		obj.setInspectorName(jsObj.getString("InspectorName"));
     		obj.setResolveDatePictureURL1(jsObj.getString("ResolveDatePictureURL1"));
     		obj.setResolveDatePictureURL2(jsObj.getString("ResolveDatePictureURL2"));
     		obj.setResolveDatePictureURL3(jsObj.getString("ResolveDatePictureURL3"));
     		obj.setReInspectedUnresolvedDate(jsObj.getString("ReinspectedUnresolvedDate"));
     		obj.setReInspectedUnresolvedDatePictureURL1(jsObj.getString("ReinspectedUnresolvedDatePictureURL1"));
     		obj.setReInspectedUnresolvedDatePictureURL2(jsObj.getString("ReinspectedUnresolvedDatePictureURL2"));
     		obj.setReInspectedUnresolvedDatePictureURL3(jsObj.getString("ReinspectedUnresolvedDatePictureURL3"));
     		
     		obj.setExpectedInspectionDate(jsObj.getString("ExpectedInspectionDate"));
     		obj.setFaultType(jsObj.getString("FaultType"));
     		FMDBDatabaseAccess fdb=new FMDBDatabaseAccess(EditDefect.this);
     		
     		obj.setStatusForUpload("Modified");
        	
     		if(isOnline)
        		obj.setIsDataSyncToWeb(true);
        	else
        		obj.setIsDataSyncToWeb(false);
     		
     		fdb.insertORUpdateSnagMaster(obj);
     	
		 }
		 catch (Exception e)
		 {
			Log.d("parseData ",""+e.getMessage());
		}
	 }
	 public void populateCureentSnag()
	 {
		 try{
		 SnagMaster objSnag=new SnagMaster();
		 FMDBDatabaseAccess fmdb=new FMDBDatabaseAccess(EditDefect.this);
		
		 objSnag=fmdb.getSnagByID(CurrentSnag.getID());
		 if(objSnag!=null && objSnag.getReportDate()!=null && objSnag.getReportDate().length()>0 && !(objSnag.getReportDate().equalsIgnoreCase("null")))
		 {
			 CurrentSnag=objSnag;
			 continueprocess();
		 }
		 else
		 {
			 DownloadSnagInBackground task=new DownloadSnagInBackground();
			 task.execute(10);
			 
			 
		 }
		 }
	        catch(Exception e)
	        {
	        	Log.d("Exception", ""+e.getMessage());
	        }
	 }
	 protected class DownloadImagesInBackGround extends AsyncTask<Integer, Integer,Void>
	 {
		 
		 String imageUrl;
		 int tag;
		 String filepath;
		Bitmap bmp;
		@Override
		protected void onPreExecute()
		{
			   
		}
		 @Override
		protected Void doInBackground(Integer... params) 
		 {
			
			 
			 try{
		    		if(imageUrl!=null && imageUrl.length()>0){
		    		String path=Environment.getExternalStorageDirectory()+"/SnagReporter/Pictures/"+imageUrl+".jpg";
		    		File filechk=new File(path);
		    		filepath=path;
		    		if(!(filechk.exists()))
		    		{
		    			//String downloadUrl="http://snag.itakeon.com/uploadimage/"+imageUrl+".jpg";
		    			String downloadUrl="http://5.1.113.117:1105/uploadimage/"+imageUrl+".jpg";
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
		    	

			return null;
		}
		 @Override
		protected void onPostExecute(Void result)
		 {
			runOnUiThread(new Runnable() {
				public void run()
				{
					try{
					View v;
					 int wantedPosition = isngPenPicIndx; // Whatever position you're looking for
						int firstPosition = list.getFirstVisiblePosition() - list.getHeaderViewsCount(); // This is the same as child #0
						int wantedChild = wantedPosition - firstPosition;
					
					 if(tag==1)
					 {
							// Say, first visible position is 8, you want position 10, wantedChild will now be 2
							// So that means your view is child #2 in the ViewGroup:
							if(! (wantedChild < 0 || wantedChild >= list.getChildCount())) {
						
								//PhotoURl1=retUUID;
								v=list.getChildAt(wantedChild);
								ImageButton i1=(ImageButton)v.findViewById(R.id.row_cell_addDefectPhoto_img1);
								ProgressBar pbar=(ProgressBar)v.findViewById(R.id.row_cell_progress);
								try{
								bmp=decodeFile(filepath);
								}
								catch(OutOfMemoryError e)
								{
									Log.d("Exception",""+e.getMessage());
								}
								//i1.setImageBitmap(bmp);
								Drawable draw=new BitmapDrawable(bmp);
								i1.setBackgroundDrawable(draw);
								if(pbar!=null)
								pbar.setVisibility(View.INVISIBLE);
								
								i1.requestLayout();
								i1.setVisibility(View.VISIBLE);
								imgVw1=i1;
								//ListItemAddDefectPhoto item=(ListItemAddDefectPhoto) items.get(wantedPosition);
								//items.set(wantedPosition,new ListItemAddDefectPhoto(null, "Snag Photos",imgVw1,imgVw2,imgVw3,1,true,PhotoURl1,PhotoURl2,PhotoURl3,false));
								//adapter.notifyDataSetChanged();
								isImageSetFor1=true;
								//adapter.notifyDataSetChanged();
							}

						 
					 }
					 else if(tag==2)
					 {
						 if(! (wantedChild < 0 || wantedChild >= list.getChildCount()))
						 {
								//PhotoURl1=retUUID;
								v=list.getChildAt(wantedChild);
								ImageButton i1=(ImageButton)v.findViewById(R.id.row_cell_addDefectPhoto_img2);
								ProgressBar pbar2=(ProgressBar)v.findViewById(R.id.row_cell_progress2);
								try{
								bmp=decodeFile(filepath);
								}
								catch(OutOfMemoryError  e)
								{
									Log.d("Error ",""+e.getMessage());
								}
								Drawable draw=new BitmapDrawable(bmp);
								i1.setBackgroundDrawable(draw);
								if(pbar2!=null)
								pbar2.setVisibility(View.INVISIBLE);
								i1.requestLayout();
								i1.setVisibility(View.VISIBLE);
								imgVw2=i1;
								//ListItemAddDefectPhoto item=(ListItemAddDefectPhoto) items.get(wantedPosition);
								//items.set(wantedPosition,new ListItemAddDefectPhoto(null, "Snag Photos",imgVw1,imgVw2,imgVw3,1,true,PhotoURl1,PhotoURl2,PhotoURl3,false));
								//adapter.notifyDataSetChanged();
								
								isImageSetFor1=true;
							}
					 }
					 else if(tag==3)
					 {
						 if(! (wantedChild < 0 || wantedChild >= list.getChildCount()))
							{
								//PhotoURl1=retUUID;
								v=list.getChildAt(wantedChild);
								ImageButton i1=(ImageButton)v.findViewById(R.id.row_cell_addDefectPhoto_img3);
								ProgressBar pbar3=(ProgressBar)v.findViewById(R.id.row_cell_progress3);
								bmp=decodeFile(filepath);
								if(pbar3!=null)
									pbar3.setVisibility(View.INVISIBLE);
								Drawable draw=new BitmapDrawable(bmp);
								i1.setBackgroundDrawable(draw);
							    i1.setVisibility(View.VISIBLE);
								i1.requestLayout();
								isImageSetFor1=true;
								imgVw3=i1;
								//ListItemAddDefectPhoto item=(ListItemAddDefectPhoto) items.get(wantedPosition);
							//	items.set(wantedPosition,new ListItemAddDefectPhoto(null, "Snag Photos",imgVw1,imgVw2,imgVw3,1,true,PhotoURl1,PhotoURl2,PhotoURl3,false));
								//isImageSetFor1=true;
								//adapter.notifyDataSetChanged();
							}
					 }
				 }
		        catch(Exception e)
		        {
		        	Log.d("Exception", ""+e.getMessage());
		        }
				}
			});
			 				
			
		}
	 }
	 public void downloadImage(String imageUrl)
	 {
	    		   
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
	        
	        case R.id.menuBtn_exit:
	        	new AlertDialog.Builder(EditDefect.this)
	    	    
	    	    .setMessage("Are you sure you want to Exit?")
	    	    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
	    	        public void onClick(DialogInterface dialog, int which) { 
	    	        	setResult(10001);
	    	    		finish();
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
	        	new AlertDialog.Builder(EditDefect.this)
	    	    
	    	    .setMessage("Are you sure you want to Logout?")
	    	    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
	    	        public void onClick(DialogInterface dialog, int which) { 
//	    	            FMDBDatabaseAccess db=new FMDBDatabaseAccess(EditDefect.this);
//	    	            SharedPreferences SP = getSharedPreferences("AppDelegate",MODE_PRIVATE);
//	    				db.performLogout(SP.getString("RegUserID", ""));
//	    				Intent i=new Intent(EditDefect.this,com.snagreporter.Login_page.class);
//	    				i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//	    				startActivity(i);
//	    				
//	    				finish();
	    	        	setResult(10002);
	    	    		finish();
	    	        	
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
	    
//	    public void SyncData(){
//	    	DownloadDataFromWeb2 task=new DownloadDataFromWeb2();
//	    	task.execute(10);
//	    	
//	    }
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

	    		
	    		FMDBDatabaseAccess db=new FMDBDatabaseAccess(EditDefect.this);
	    		
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
	    
		public void onScroll(AbsListView lw, final int firstVisibleItem,
		                 final int visibleItemCount, final int totalItemCount) {

		    switch(lw.getId()) {
		        case R.id.android_edit_defect_list:     
		        	try{
		            // Make your calculation stuff here. You have all your
		            // needed info from the parameters of this function.

		            // Sample calculation to determine if the last 
		            // item is fully visible.
		            final int lastItem = firstVisibleItem + visibleItemCount;
		            if(lastItem == totalItemCount) {
		                // Last item is fully visible.
		            	
		            	//ImageButton btn1=null,btn2=null,btn3=null;
		            	//ImageButton btn1=null,btn2=null,btn3=null;
		        		if(PhotoURl1!=null && PhotoURl1.length()>0){
		        			String FilePath1=Environment.getExternalStorageDirectory()+File.separator+"SnagReporter/Pictures/"+PhotoURl1+".jpg";
		        			File fooo = new File(FilePath1);
		        			if(fooo.exists()){
		        			Bitmap Img1 = decodeFile(FilePath1);
		        			int wantedPosition = isngPenPicIndx; // Whatever position you're looking for
		        			int firstPosition = list.getFirstVisiblePosition() - list.getHeaderViewsCount(); // This is the same as child #0
		        			int wantedChild = wantedPosition - firstPosition;
		        			// Say, first visible position is 8, you want position 10, wantedChild will now be 2
		        			// So that means your view is child #2 in the ViewGroup:
		        			
		        			if (wantedChild < 0 || wantedChild >= list.getChildCount()) {
		        			  Log.d("Child Not Available", "");
		        			}
		        			else{
		        				
		        				View v=list.getChildAt(wantedChild);
		        				ImageButton btnI=new ImageButton(this);
		        				btnI.setImageBitmap(Img1);
		        				 ImageButton i1=(ImageButton)v.findViewById(R.id.row_cell_addDefectPhoto_img1);
		        		   		 i1.setBackgroundDrawable(btnI.getDrawable());
		        			}
		        			}
		        			else
		        			{
		        				DownloadImagesInBackGround task=new DownloadImagesInBackGround();
		        				task.imageUrl=PhotoURl1;
		        				task.tag=1;
		        				task.execute(10);
		        			}
		        			
		        		}
		        		
		        		if(PhotoURl2!=null && PhotoURl2.length()>0){
		        			String FilePath2=Environment.getExternalStorageDirectory()+File.separator+"SnagReporter/Pictures/"+PhotoURl2+".jpg";
		        			File fooo = new File(FilePath2);
		        			if(fooo.exists())
		        			{
		        			Bitmap Img2 = decodeFile(FilePath2);
		        			int wantedPosition = isngPenPicIndx; // Whatever position you're looking for
		        			int firstPosition = list.getFirstVisiblePosition() - list.getHeaderViewsCount(); // This is the same as child #0
		        			int wantedChild = wantedPosition - firstPosition;
		        			// Say, first visible position is 8, you want position 10, wantedChild will now be 2
		        			// So that means your view is child #2 in the ViewGroup:
		        			
		        			if (wantedChild < 0 || wantedChild >= list.getChildCount()) {
		        			  Log.d("Child Not Available", "");
		        			}
		        			else{
		           			
		           			View v=list.getChildAt(wantedChild);
		           			ImageButton btnI=new ImageButton(this);
		           			btnI.setImageBitmap(Img2);
		           			 ImageButton i2=(ImageButton)v.findViewById(R.id.row_cell_addDefectPhoto_img2);
		           	   		 i2.setBackgroundDrawable(btnI.getDrawable());
		        			}
		        			}
		        			else
		        			{
		        				DownloadImagesInBackGround task=new DownloadImagesInBackGround();
		        				task.imageUrl=PhotoURl2;
		        				task.tag=2;
		        				task.execute(10);
		        			}
		        			
		        		}
		        		
		        		if(PhotoURl3!=null && PhotoURl3.length()>0){
		        			String FilePath2=Environment.getExternalStorageDirectory()+File.separator+"SnagReporter/Pictures/"+PhotoURl3+".jpg";
		        			File fooo = new File(FilePath2);
		        			if(fooo.exists())
		        			{
		        			Bitmap Img2 = decodeFile(FilePath2);
		        			int wantedPosition = isngPenPicIndx; // Whatever position you're looking for
		        			int firstPosition = list.getFirstVisiblePosition() - list.getHeaderViewsCount(); // This is the same as child #0
		        			int wantedChild = wantedPosition - firstPosition;
		        			// Say, first visible position is 8, you want position 10, wantedChild will now be 2
		        			// So that means your view is child #2 in the ViewGroup:
		        			 
		        			if (wantedChild < 0 || wantedChild >= list.getChildCount()) {
		        			  Log.d("Child Not Available", "");
		        			}
		        			else{
		           			
		           			 View v=list.getChildAt(wantedChild);
		           			ImageButton btnI=new ImageButton(this);
		           			btnI.setImageBitmap(Img2);
		           			 ImageButton i3=(ImageButton)v.findViewById(R.id.row_cell_addDefectPhoto_img3);
		           	   		 i3.setBackgroundDrawable(btnI.getDrawable());
		        			}
		        			}
		        			else
		        			{
		        				DownloadImagesInBackGround task=new DownloadImagesInBackGround();
		        				task.imageUrl=PhotoURl3;
		        				task.tag=3;
		        				task.execute(10);
		        			}
		        			
		        		}
		        		
		        		///second
		        		if(PhotoURl21!=null && PhotoURl21.length()>0){
		        			String FilePath1=Environment.getExternalStorageDirectory()+File.separator+"SnagReporter/Pictures/"+PhotoURl21+".jpg";
		        			File fooo = new File(FilePath1);
		        			if(fooo.exists()){
		        			Bitmap Img1 = decodeFile(FilePath1);
		        			int wantedPosition = iUnResPicIndx; // Whatever position you're looking for
		        			int firstPosition = list.getFirstVisiblePosition() - list.getHeaderViewsCount(); // This is the same as child #0
		        			int wantedChild = wantedPosition - firstPosition;
		        			// Say, first visible position is 8, you want position 10, wantedChild will now be 2
		        			// So that means your view is child #2 in the ViewGroup:
		        			
		        			if (wantedChild < 0 || wantedChild >= list.getChildCount()) {
		        			  Log.d("Child Not Available", "");
		        			}
		        			else{
		        				
		        				View v=list.getChildAt(wantedChild);
		        				ImageButton btnI=new ImageButton(this);
		        				btnI.setImageBitmap(Img1);
		        				 ImageButton i1=(ImageButton)v.findViewById(R.id.row_cell_addDefectPhoto_img1);
		        		   		 i1.setBackgroundDrawable(btnI.getDrawable());
		        			}
		        			}
		        			else
		        			{
		        				DownloadImagesInBackGround task=new DownloadImagesInBackGround();
		        				task.imageUrl=PhotoURl21;
		        				task.tag=1;
		        				task.execute(10);
		        			}
		        			
		        		}
		        		
		        		if(PhotoURl22!=null && PhotoURl22.length()>0){
		        			String FilePath2=Environment.getExternalStorageDirectory()+File.separator+"SnagReporter/Pictures/"+PhotoURl22+".jpg";
		        			File fooo = new File(FilePath2);
		        			if(fooo.exists())
		        			{
		        			Bitmap Img2 = decodeFile(FilePath2);
		        			int wantedPosition = iUnResPicIndx; // Whatever position you're looking for
		        			int firstPosition = list.getFirstVisiblePosition() - list.getHeaderViewsCount(); // This is the same as child #0
		        			int wantedChild = wantedPosition - firstPosition;
		        			// Say, first visible position is 8, you want position 10, wantedChild will now be 2
		        			// So that means your view is child #2 in the ViewGroup:
		        			
		        			if (wantedChild < 0 || wantedChild >= list.getChildCount()) {
		        			  Log.d("Child Not Available", "");
		        			}
		        			else{
		           			
		           			View v=list.getChildAt(wantedChild);
		           			ImageButton btnI=new ImageButton(this);
		           			btnI.setImageBitmap(Img2);
		           			 ImageButton i2=(ImageButton)v.findViewById(R.id.row_cell_addDefectPhoto_img2);
		           	   		 i2.setBackgroundDrawable(btnI.getDrawable());
		        			}
		        			}
		        			else
		        			{
		        				DownloadImagesInBackGround task=new DownloadImagesInBackGround();
		        				task.imageUrl=PhotoURl22;
		        				task.tag=2;
		        				task.execute(10);
		        			}
		        			
		        		}
		        		
		        		if(PhotoURl23!=null && PhotoURl23.length()>0){
		        			String FilePath2=Environment.getExternalStorageDirectory()+File.separator+"SnagReporter/Pictures/"+PhotoURl23+".jpg";
		        			File fooo = new File(FilePath2);
		        			if(fooo.exists())
		        			{
		        			Bitmap Img2 = decodeFile(FilePath2);
		        			int wantedPosition = iUnResPicIndx; // Whatever position you're looking for
		        			int firstPosition = list.getFirstVisiblePosition() - list.getHeaderViewsCount(); // This is the same as child #0
		        			int wantedChild = wantedPosition - firstPosition;
		        			// Say, first visible position is 8, you want position 10, wantedChild will now be 2
		        			// So that means your view is child #2 in the ViewGroup:
		        			 
		        			if (wantedChild < 0 || wantedChild >= list.getChildCount()) {
		        			  Log.d("Child Not Available", "");
		        			}
		        			else{
		           			
		           			 View v=list.getChildAt(wantedChild);
		           			ImageButton btnI=new ImageButton(this);
		           			btnI.setImageBitmap(Img2);
		           			 ImageButton i3=(ImageButton)v.findViewById(R.id.row_cell_addDefectPhoto_img3);
		           	   		 i3.setBackgroundDrawable(btnI.getDrawable());
		        			}
		        			}
		        			else
		        			{
		        				DownloadImagesInBackGround task=new DownloadImagesInBackGround();
		        				task.imageUrl=PhotoURl23;
		        				task.tag=3;
		        				task.execute(10);
		        			}
		        			
		        		}
		        		//third
		        		if(PhotoURl31!=null && PhotoURl31.length()>0){
		        			String FilePath1=Environment.getExternalStorageDirectory()+File.separator+"SnagReporter/Pictures/"+PhotoURl31+".jpg";
		        			File fooo = new File(FilePath1);
		        			if(fooo.exists()){
		        			Bitmap Img1 = decodeFile(FilePath1);
		        			int wantedPosition;
		        			if(CurrentSnag.getReInspectedUnresolvedDate()!=null && !CurrentSnag.getReInspectedUnresolvedDate().equalsIgnoreCase(""))
		        			wantedPosition = 15; // Whatever position you're looking for
		        			else
		        				wantedPosition = iUnResPicIndx;
		        			int firstPosition = list.getFirstVisiblePosition() - list.getHeaderViewsCount(); // This is the same as child #0
		        			int wantedChild = wantedPosition - firstPosition;
		        			// Say, first visible position is 8, you want position 10, wantedChild will now be 2
		        			// So that means your view is child #2 in the ViewGroup:
		        			
		        			if (wantedChild < 0 || wantedChild >= list.getChildCount()) {
		        			  Log.d("Child Not Available", "");
		        			}
		        			else{
		        				
		        				View v=list.getChildAt(wantedChild);
		        				ImageButton btnI=new ImageButton(this);
		        				btnI.setImageBitmap(Img1);
		        				 ImageButton i1=(ImageButton)v.findViewById(R.id.row_cell_addDefectPhoto_img1);
		        		   		 i1.setBackgroundDrawable(btnI.getDrawable());
		        			}
		        			}
		        			else
		        			{
		        				DownloadImagesInBackGround task=new DownloadImagesInBackGround();
		        				task.imageUrl=PhotoURl31;
		        				task.tag=1;
		        				task.execute(10);
		        			}
		        			
		        		}
		        		
		        		if(PhotoURl32!=null && PhotoURl32.length()>0){
		        			String FilePath2=Environment.getExternalStorageDirectory()+File.separator+"SnagReporter/Pictures/"+PhotoURl32+".jpg";
		        			File fooo = new File(FilePath2);
		        			if(fooo.exists())
		        			{
		        			Bitmap Img2 = decodeFile(FilePath2);
		        			int wantedPosition;
		        			if(CurrentSnag.getReInspectedUnresolvedDate()!=null && !CurrentSnag.getReInspectedUnresolvedDate().equalsIgnoreCase(""))
		        			 wantedPosition = iReslvPicIndx; // Whatever position you're looking for
		        			else
		        				wantedPosition = iUnResPicIndx;
		        			int firstPosition = list.getFirstVisiblePosition() - list.getHeaderViewsCount(); // This is the same as child #0
		        			int wantedChild = wantedPosition - firstPosition;
		        			// Say, first visible position is 8, you want position 10, wantedChild will now be 2
		        			// So that means your view is child #2 in the ViewGroup:
		        			
		        			if (wantedChild < 0 || wantedChild >= list.getChildCount()) {
		        			  Log.d("Child Not Available", "");
		        			}
		        			else{
		           			
		           			View v=list.getChildAt(wantedChild);
		           			ImageButton btnI=new ImageButton(this);
		           			btnI.setImageBitmap(Img2);
		           			 ImageButton i2=(ImageButton)v.findViewById(R.id.row_cell_addDefectPhoto_img2);
		           	   		 i2.setBackgroundDrawable(btnI.getDrawable());
		        			}
		        			}
		        			else
		        			{
		        				DownloadImagesInBackGround task=new DownloadImagesInBackGround();
		        				task.imageUrl=PhotoURl32;
		        				task.tag=2;
		        				task.execute(10);
		        			}
		        			
		        		}
		        		
		        		if(PhotoURl33!=null && PhotoURl33.length()>0){
		        			String FilePath2=Environment.getExternalStorageDirectory()+File.separator+"SnagReporter/Pictures/"+PhotoURl33+".jpg";
		        			File fooo = new File(FilePath2);
		        			if(fooo.exists())
		        			{
		        			Bitmap Img2 = decodeFile(FilePath2);
		        			int wantedPosition;
		        			if(CurrentSnag.getReInspectedUnresolvedDate()!=null && !CurrentSnag.getReInspectedUnresolvedDate().equalsIgnoreCase(""))
		        			 wantedPosition = iReslvPicIndx; 
		        			else
		        				wantedPosition = iUnResPicIndx;// Whatever position you're looking for
		        			int firstPosition = list.getFirstVisiblePosition() - list.getHeaderViewsCount(); // This is the same as child #0
		        			int wantedChild = wantedPosition - firstPosition;
		        			// Say, first visible position is 8, you want position 10, wantedChild will now be 2
		        			// So that means your view is child #2 in the ViewGroup:
		        			 
		        			if (wantedChild < 0 || wantedChild >= list.getChildCount()) {
		        			  Log.d("Child Not Available", "");
		        			}
		        			else{
		           			
		           			 View v=list.getChildAt(wantedChild);
		           			ImageButton btnI=new ImageButton(this);
		           			btnI.setImageBitmap(Img2);
		           			 ImageButton i3=(ImageButton)v.findViewById(R.id.row_cell_addDefectPhoto_img3);
		           	   		 i3.setBackgroundDrawable(btnI.getDrawable());
		        			}
		        			}
		        			else
		        			{
		        				DownloadImagesInBackGround task=new DownloadImagesInBackGround();
		        				task.imageUrl=PhotoURl33;
		        				task.tag=3;
		        				task.execute(10);
		        			}
		        			
		        		}
		            }
		       	 }
		            catch(Exception e)
		            {
		            	Log.d("Exception", ""+e.getMessage());
		            }
		            
		    }
		}
		
		public void onScrollStateChanged(AbsListView arg0, int arg1) {
			// TODO Auto-generated method stub
			
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
	    				new AlertDialog.Builder(EditDefect.this)
	    	    	    
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
	    			Intent i=new Intent(EditDefect.this,com.snagreporter.GraphWebView.class);
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
	    		ParseSyncData parser=new ParseSyncData(EditDefect.this);
	    		parser.start();
	    		 }
	            catch(Exception e)
	            {
	            	Log.d("Exception", ""+e.getMessage());
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
	                	FMDBDatabaseAccess db=new FMDBDatabaseAccess(EditDefect.this);
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
	                	FMDBDatabaseAccess db=new FMDBDatabaseAccess(EditDefect.this);
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
	            	FMDBDatabaseAccess db=new FMDBDatabaseAccess(EditDefect.this);
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
}