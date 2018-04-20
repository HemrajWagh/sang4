package com.snagreporter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.UUID;

import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import com.snagreporter.database.FMDBDatabaseAccess;
import com.snagreporter.database.ParseSyncData;


import com.snagreporter.entity.ApartmentDetails;
import com.snagreporter.entity.ApartmentMaster;
import com.snagreporter.entity.BuildingMaster;
import com.snagreporter.entity.ContractorMaster;
import com.snagreporter.entity.FloorMaster;
import com.snagreporter.entity.ProjectMaster;
import com.snagreporter.entity.Registration;
import com.snagreporter.entity.StdFloorAreas;
import com.snagreporter.entity.TradeAptAreaDetail;
import com.snagreporter.entity.TradeAptAreaMaster;
import com.snagreporter.entity.TradeDependency;
import com.snagreporter.entity.TradeDetails;
import com.snagreporter.entity.TradeMaster;
import com.snagreporter.listitems.Header;
import com.snagreporter.listitems.Item;
import com.snagreporter.listitems.ListItemAddDefect;
import com.snagreporter.menuhandler.MenuHandler;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.ViewPager.LayoutParams;
import android.util.Log;
import android.view.ContextMenu;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;

public class AddUpdateInspectionList extends Activity implements OnScrollListener
{
	
	
	boolean isFromFloorExtArea;
	String NAMESPACE = "http://tempuri.org/";
	String URL="";// =""+getResources().getString(R.string.WS_URL).toString()+"SnagReporter_WebS.asmx";
	boolean isParentCheckOn=true;
	String METHOD_NAME_SaveNewDataToTheDataBase = "SaveNewDataToTheDataBase";
	String SOAP_ACTION_SaveNewDataToTheDataBase = "http://tempuri.org/SaveNewDataToTheDataBase";
	String METHOD_NAME_UpdateDataToTheDataBase = "UpdateDataToTheDataBase";
	String SOAP_ACTION_UpdateDataToTheDataBase = "http://tempuri.org/UpdateDataToTheDataBase";
	
	String[] strSetValues;
	String[] strGetValues;
 	ListView list;
	//ImageButton imgVw1,imgVw2,imgVw3;
	int cnt=1;
	List<Item> items;
	//String sample;
	BuildingMaster CurrentBuilding;
	FloorMaster CurrentFloor;
	ProjectMaster CurrentProject;
	ApartmentMaster CurrentAPT;
	String SelectedArea="",SelectedAreaID="",SelectedJobType="",SelectedJobDetails="",SelectedFaultType="",SelectedInspector="",selectedCost="",costTo="",snagPriority="";
	//JobType arrJobType[];
	//FaultType arrFaultType[];
	String AreaType="";
	String AreaTypeID="";
	private static final int CAMERA_PIC_REQUEST = 1337;
	//InspectionMaster MainIM;
	int as_index=-1;
	int as_index_selected=0;
	//boolean isImageSetFor1=false;
	//boolean isImageSetFor2=false;
	//boolean isImageSetFor3=false;
	//String PhotoURl1="",PhotoURl2="",PhotoURl3="";
	//Bitmap BtnImageBmp,BtnYourImg;
	//String strFromImg,strFromImgvw,strFilePath,strMenuType;
	//private Bitmap mBitmap;
	//int SelJobTypeIndex=0;
	ApartmentDetails AreaList[];
	//TextView SelTextInList;
	//String RemovedPhotoURL;
	boolean isAptmt;
	StdFloorAreas CurrentSFA;
	//Inspector arrInspetor[];
	//Registration arrInspetor[];
	static final int DATE_DIALOG_ID = 1;
	private int year, month, day,year2, month2, day2;
	//String ReportedDate="";
	//String ExpectedDate="";
	//int selectedInspectorIndex=-1;
	//int CurrentDate=-1;
	//boolean isFirstTime;
	TwoTextArrayAdapter adapter;
	TwoTextArrayAdapter adapter2;
	
	boolean isOnline=false;
	ProgressDialog mProgressDialog2;
	int image=0;
	
	//private File dir, destImage,f;
	//private String cameraFile = null;
	//SnagMaster NewSnag;
	//JobType[] AddedSnagType;
	//FaultType[] AddedFaultType;
	//Employee[] arrEmployee;
	//int SelectedEmployeeIndex=-1;
	String strLoginType="";
	String RegUserID="";
	String LoginType="";
	View TopMenu;
	boolean isMenuVisible=false;
	MenuHandler menuhandler;
	//InspectionList[] arrInspectionList;
	//InspectionDetails[] arrInspectionDetails;
	
	//String SelectedProject="",SelectedBuilding="",SelectedFloor="",SelectedApt="";
	
	int intAreaType=0;
	//String[] areas;
	//String[] jobtype;
	
	String SelectedAreaTypeParent="";
	String []Status;
	TradeDetails[]arrTDDistinctTrade;
	String[]arrTDDistinctInspectionGroup;
	TradeDetails[]arrTDDistinctInspectionDescription;
	String SelectedInspectionGroup="";
	TradeMaster arrTradeMaster[];
	String SelectedTradeType="";
	String SelectedTradeID="";
	Boolean isExtArea=false;
	String SelectedTradeMasterID="";
	String arrarrTradeDetailsDescription[];
	TradeAptAreaMaster  MainIMtradeAptAreaMaster;
	TradeAptAreaDetail arrTradeAptAreaDetail[];
	String SelectedAptAreaID="";
	ApartmentDetails CurrsentApartmentArea;
	boolean isRotated=false;
	int TotalToUpload=0;
	int Current=0;
	@Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        try{
        setContentView(R.layout.add_update_inspection_list);
        
        SharedPreferences SP = getSharedPreferences("AppDelegate",MODE_PRIVATE);
        RegUserID=SP.getString("RegUserID", "");
        LoginType=SP.getString("LoginType", "");
        
        Status=new String[4];
        Status[0]="Not Done";
        Status[1]="Done";
        Status[2]="Override";
        Status[3]="NA";
        
        SharedPreferences sharedPref = getSharedPreferences("AppDelegate",MODE_PRIVATE);
        Boolean id=sharedPref.getBoolean("isOnline", false);
        isOnline=id;
        
        URL =""+getResources().getString(R.string.WS_URL).toString()+"SnagReporter_WebS.asmx";
        
        menuhandler=new MenuHandler(AddUpdateInspectionList.this);
        TopMenu=new View(this);
        RelativeLayout.LayoutParams rlp=new RelativeLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
        TopMenu.setLayoutParams(rlp);
        LayoutInflater inflater= LayoutInflater.from(this);
        TopMenu=(View) inflater.inflate(R.layout.popup_menu, null);
        this.addContentView(TopMenu, rlp);
        TopMenu.requestLayout();
        TopMenu.setVisibility(View.INVISIBLE);
        
        RelativeLayout RL=(RelativeLayout)TopMenu.findViewById(R.id.parentCheckView);
        if(RL!=null){
        	RL.setVisibility(View.VISIBLE);
        }
        
        
        FMDBDatabaseAccess obj=new FMDBDatabaseAccess(getApplicationContext());
        Registration REG=obj.getRegistration();
        strLoginType=REG.getType();
        mProgressDialog2 = new ProgressDialog(AddUpdateInspectionList.this);
        isExtArea=getIntent().getBooleanExtra("isFromExtArea",false);
        isFromFloorExtArea=getIntent().getBooleanExtra("isFromFloorArea",false);
        CurrentProject=(ProjectMaster) getIntent().getExtras().get("Project");
        CurrentBuilding=(BuildingMaster)getIntent().getExtras().get("Building");
        if(!isExtArea)
        {
        	CurrentFloor=(FloorMaster)getIntent().getExtras().get("Floor");
        }
        isAptmt=getIntent().getBooleanExtra("isAptmt",false);
        
        Button btn=(Button)findViewById(R.id.adddefectSavebtn);
        btn.setBackgroundDrawable(getResources().getDrawable(R.drawable.back_blue_button));
        
        Object objNot = getLastNonConfigurationInstance();
        if(objNot!=null)
        {
        	Intent i=(Intent)objNot;
        	strGetValues=(String[])i.getExtras().get("strSetValues");
        	arrTradeAptAreaDetail=(TradeAptAreaDetail[])i.getExtras().get("arrTradeAptAreaDetail");
        	arrTDDistinctInspectionDescription=(TradeDetails[])i.getExtras().get("arrTDDistinctInspectionDescription");
        	isRotated=(boolean)i.getExtras().getBoolean("isRotated");
        	
        }
        
        
        if(isAptmt){
        	CurrentAPT=(ApartmentMaster)getIntent().getExtras().get("Apartment");
        	
        }
        else{
        	CurrentSFA=(StdFloorAreas)getIntent().getExtras().get("SFA");
        }
        
       
        
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
		
        
        
        list=(ListView)findViewById(R.id.android_add_defect_list);
        
        ContinueProcess();
        }
        catch(Exception e)
        {
        	Log.d("Exception",e.getMessage());
        }
    }
	
	public void ParentCheckClick(View v){
		try{
			HideTopMenu();
			TextView t=(TextView)TopMenu.findViewById(R.id.parentcheckText);
			if(isParentCheckOn){
				isParentCheckOn=false;
				if(t!=null)
					t.setText("ParentCheck Off");
			}
			else{
				if(t!=null)
					t.setText("ParentCheck On");
				isParentCheckOn=true;
			}
				
		}
		catch(Exception e){
			
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
	public void MenuGraphClick(View v){
		HideTopMenu();
		//menuhandler.MenuGraphClick("project");
	}
	public void MenuAttendanceClick(View v){
		HideTopMenu();
		
		menuhandler.MenuAttendanceClick();
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
        strSetValues=new String[6];
        strSetValues[0]=SelectedArea;
        strSetValues[1]=SelectedAreaTypeParent;
        strSetValues[2]=SelectedTradeMasterID;
        strSetValues[3]=SelectedTradeType;
        strSetValues[4]=SelectedTradeID;
        strSetValues[5]=SelectedInspectionGroup;
        
        Intent i=new Intent();
        i.putExtra("strSetValues", strSetValues);
        i.putExtra("arrTradeAptAreaDetail", arrTradeAptAreaDetail);
        i.putExtra("arrTDDistinctInspectionDescription", arrTDDistinctInspectionDescription);
        i.putExtra("isRotated", true);
        
       
        
        
        return i;
    }
	
	public void ContinueProcess(){
    	//Toast.makeText(getApplicationContext(), "Came in Continue Process "+cnt++, Toast.LENGTH_LONG).show();
    	try{
    		 
    		
    		if(strGetValues!=null)
    		{
    			SelectedArea=strGetValues[0];
    			SelectedAreaTypeParent=strGetValues[1];
    			SelectedTradeMasterID=strGetValues[2];
    			SelectedTradeType=strGetValues[3];
    			SelectedTradeID=strGetValues[4];
    			SelectedInspectionGroup=strGetValues[5];
    		    
    		    
    		    //@con
    			
    		}
    		items = new ArrayList<Item>();
    		if(LoginType.equals(getResources().getString(R.string.strInspector).toString()))
    			items.add(new Header(null, "INSPECTION LIST",false,false,true,true));
    		else
    			items.add(new Header(null, "INSPECTION LIST",false,false,true,false));
  	   
  	     FMDBDatabaseAccess obj=new FMDBDatabaseAccess(getApplicationContext());
  	     if(isExtArea)
  	     {
  	    	 //AreaList=CurrentBuilding;
  	    	 SelectedArea=CurrentBuilding.getBuildingName();
  	    	SelectedAreaID=CurrentBuilding.getID();
  	    	SelectedAreaTypeParent=CurrentBuilding.getBuildingName();
  	     }
  	     else if(isFromFloorExtArea)
  	     {
  	    	 SelectedArea=CurrentFloor.getFloor();
   	    	SelectedAreaID=CurrentFloor.getID();
   	    	SelectedAreaTypeParent=CurrentFloor.getFloor();
  	     }
  	     else
  	     {
  	    	 AreaList=obj.getApartmentDetails(CurrentAPT);
  	     }
  	     if(AreaList!=null && AreaList.length>0)
  	     {
	    	   if(SelectedArea==null || SelectedArea.length()==0)
	    	   {
	    		   SelectedArea=AreaList[0].getAptAreaName();
	    		   SelectedAreaID=AreaList[0].getID();
	    		   SelectedAreaTypeParent=AreaList[0].getAptAreaType();
	    	   }
  	     }
  	     else if(isFromFloorExtArea)
  	     {
  	    	if(SelectedArea==null || SelectedArea.length()==0)
  	    	{
	    		   SelectedArea=CurrentFloor.getFloor();
	    		   SelectedAreaID=CurrentFloor.getID();
	    		   SelectedAreaTypeParent=CurrentFloor.getFloor();
	    	}
  	     }
  	     else
  	     {
  	    	if(SelectedArea==null || SelectedArea.length()==0)
  	    	{
	    		   SelectedArea=CurrentAPT.getApartmentNo();
	    		   SelectedAreaID=CurrentAPT.getID();
	    		   SelectedAreaTypeParent=CurrentAPT.getApartmentNo();
	    	}
  	     }
  	   FMDBDatabaseAccess db=new FMDBDatabaseAccess(AddUpdateInspectionList.this);
  	   
  	  
  	  if(isExtArea)
  	   {
  		   AreaType=CurrentBuilding.getBuildingType();
  		 AreaTypeID="";
  		   
  	   }
  	  else if(isFromFloorExtArea)
  	  {
  		  AreaType=CurrentFloor.FloorType;
   		 AreaTypeID="";
  	  }
  	  else
  	  {
  		CurrsentApartmentArea=db.getApartmentDetailsForArea(CurrentAPT,SelectedArea);
		  String arr[]=obj.getAreaType(SelectedAreaTypeParent);
	  	   if(arr!=null && arr.length>0)
	  	   {
	  		 AreaType=arr[0];
	  	  	AreaTypeID=arr[1];
	  	   }
  	  }
  	   
  	   
  	     
  	     
//  	   if(AreaList==null || AreaList.length==0){
//  	     	AreaType="FLOOR";
//
//  	     }
//  	     else{
//  	     	AreaType="APARTMENT";
//  	     	
//  	     }
  	 
  	 
  	   
  	 
  	arrTradeMaster=db.getTradeMaster(AreaType,SelectedAreaTypeParent);
  	
  	if(arrTradeMaster!=null && arrTradeMaster.length>0){
  		if(SelectedTradeMasterID==null || SelectedTradeMasterID.length()==0){
  		SelectedTradeMasterID=arrTradeMaster[0].ID;
  		SelectedTradeType=arrTradeMaster[0].TradeType;
  		SelectedTradeID=arrTradeMaster[0].ID;
  		}
  	}
  	else{
  		SelectedTradeMasterID="";
  		SelectedTradeType="";
  		SelectedTradeID="";
  	}
  	//SelectedTradeMasterID="D8DB3469-3190-40D4-849D-9E7CAAA1C12F";
  	arrTDDistinctTrade=db.getTradeDetailsByTradeMasterID(SelectedTradeMasterID);
  	
  	if(arrTDDistinctTrade!=null && arrTDDistinctTrade.length>0){
  		
  		//SelectedInspectionGroup=arrTDDistinctTrade[0].InspectionGroup;
  	}
  	else{
  		
  		//SelectedInspectionGroup="";
  	}
  	 
  	if(SelectedTradeMasterID!=null && SelectedTradeMasterID.length()>0)
  		arrTDDistinctInspectionGroup=db.getTradeDetailsByTradeType(SelectedTradeMasterID);
  	else
  		arrTDDistinctInspectionGroup=null;
  	
  	if(arrTDDistinctInspectionGroup!=null && arrTDDistinctInspectionGroup.length>0){
  		if(SelectedInspectionGroup==null || SelectedInspectionGroup.length()==0)
  			SelectedInspectionGroup=arrTDDistinctInspectionGroup[0];
  	}
  	else{
  		SelectedInspectionGroup="";
  	}
  	
  	
  	if(arrTDDistinctInspectionGroup!=null && arrTDDistinctInspectionGroup.length>0){
  		if(arrTDDistinctInspectionDescription==null || arrTDDistinctInspectionDescription.length==0)
  			arrTDDistinctInspectionDescription=db.getTradeDetailsByInspectionGroup(SelectedTradeMasterID, arrTDDistinctInspectionGroup[0]);
  	}
  	else{
  		arrTDDistinctInspectionDescription=null;
  	}
  	   
	   
  	   
  	   
  	   
  	   
	       
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

  					registerForContextMenu(arg0);
			    		as_index=position;
			    		openContextMenu(arg0);
			    		unregisterForContextMenu(arg0);
  					}
  			        catch(Exception e)
  			        {
  			        	Log.d("Exception",e.getMessage());
  			        }
  				}
  			});
  	        
  	        
    	}
    	catch(Exception e){
    		
    	}
    }
	@Override  
	   public void onCreateContextMenu(ContextMenu menu, View v,ContextMenuInfo menuInfo)
	{  
		super.onCreateContextMenu(menu, v, menuInfo);
		if(as_index==1)
		{
			if(!isExtArea)
			{
				if(AreaList!=null && AreaList.length>0)
				{
					menu.setHeaderTitle("Select Area Type");
					for(int i=0;i<AreaList.length;i++)
					{
						menu.add(0, v.getId(), 0, ""+AreaList[i].getAptAreaName());
					}
				}
			}
		}
		else if(as_index==2)
		{
			if(arrTradeMaster!=null && arrTradeMaster.length>0)
			{
				menu.setHeaderTitle("Select Trade");
				for(int i=0;i<arrTradeMaster.length;i++)
				{
				
					menu.add(0, v.getId(), 0, ""+arrTradeMaster[i].TradeType);
				
				
				}
			}
			//menu.add(0, v.getId(), 0, "Add More");
		}
		else if(as_index==3)
		{
			if(arrTDDistinctInspectionGroup!=null && arrTDDistinctInspectionGroup.length>0)
			{
				menu.setHeaderTitle("Select InspectionGroup");
				for(int i=0;i<arrTDDistinctInspectionGroup.length;i++)
				{
				//if(SelectedTradeType.equalsIgnoreCase(arrTDDistinctInspectionGroup[i]))
					menu.add(0, v.getId(), 0, ""+arrTDDistinctInspectionGroup[i]);
				
				
				}
			}
			//menu.add(0, v.getId(), 0, "Add More");
		}
		else if(as_index>=4)
		{
			
			menu.setHeaderTitle("Select Status");
			for(int i=0;i<Status.length;i++)
			{
				menu.add(0, v.getId(), 0, ""+Status[i]);
			}
		}
	}
	
	@Override
	public boolean onContextItemSelected(MenuItem item)
	{
		try{
		if(as_index==1){
	
		int wantedPosition =1; // Whatever position you're looking for
		int firstPosition = list.getFirstVisiblePosition() - list.getHeaderViewsCount(); // This is the same as child #0
		int wantedChild = wantedPosition - firstPosition;
		if (wantedChild < 0 || wantedChild >= list.getChildCount()) {
		  Log.d("Child Not Available", "");
		}
		else{
		final View v=list.getChildAt(wantedChild);
		TextView t=(TextView)v.findViewById(R.id.row_cell_addDefect_text);
		t.setText(""+item.getTitle().toString());
		SelectedArea=item.getTitle().toString();  //@con
		for(int i=0;i<AreaList.length;i++){
			if(AreaList[i].getAptAreaName().equalsIgnoreCase(SelectedArea)){
				SelectedAreaID=AreaList[i].getID();
				SelectedAreaTypeParent=AreaList[i].getAptAreaType();
				break;
			}
		}
		
		
		
		
		FMDBDatabaseAccess fmdb=new FMDBDatabaseAccess(AddUpdateInspectionList.this);
	  	   
	  	   CurrsentApartmentArea=fmdb.getApartmentDetailsForArea(CurrentAPT,SelectedArea);
	  	     
	  	     
//	  	   if(AreaList==null || AreaList.length==0){
//	  	     	AreaType="FLOOR";
	//
//	  	     }
//	  	     else{
//	  	     	AreaType="APARTMENT";
//	  	     	
//	  	     }
	  	   String arr[]=fmdb.getAreaType(SelectedAreaTypeParent);
	  	   if(arr!=null && arr.length>0)
	  	   {
	  		 AreaType=arr[0];
	  	  	AreaTypeID=arr[1];
	  	   }
		
		
		FMDBDatabaseAccess db=new FMDBDatabaseAccess(AddUpdateInspectionList.this);
	  	arrTradeMaster=db.getTradeMaster(AreaType,SelectedAreaTypeParent);
	  	
	  	if(arrTradeMaster!=null && arrTradeMaster.length>0){
	  		SelectedTradeMasterID=arrTradeMaster[0].ID;
	  		SelectedTradeType=arrTradeMaster[0].TradeType;
	  		SelectedTradeID=arrTradeMaster[0].ID;
	  	}
	  	else{
	  		SelectedTradeMasterID="";
	  		SelectedTradeType="";
	  		SelectedTradeID="";
	  	}
	  	
	  	arrTDDistinctTrade=db.getTradeDetailsByTradeMasterID(SelectedTradeMasterID);
	  	
	  	if(SelectedTradeType==null || SelectedTradeType.length()==0){ //check
	  		
	  		SelectedInspectionGroup="";
	  		
	  	}
	  	else{
	  		SelectedInspectionGroup=arrTDDistinctTrade[0].InspectionGroup;
	  		
	  	}
	  	if(SelectedInspectionGroup!=null && SelectedInspectionGroup.length()>0)
	  		arrTDDistinctInspectionGroup=db.getTradeDetailsByTradeType(SelectedTradeMasterID);
	  	else
	  		arrTDDistinctInspectionGroup=null;
	  	
	  	if(arrTDDistinctInspectionGroup==null || arrTDDistinctInspectionGroup.length==0){
	  		SelectedInspectionGroup="";
	  		
	  	}
	  	else{
	  		
	  		SelectedInspectionGroup=arrTDDistinctInspectionGroup[0];
	  	}
	  	
	  	if(arrTDDistinctInspectionGroup!=null && arrTDDistinctInspectionGroup.length>0){
	  		arrTDDistinctInspectionDescription=db.getTradeDetailsByInspectionGroup(SelectedTradeMasterID, arrTDDistinctInspectionGroup[0]);
	  	}
	  	else{
	  		arrTDDistinctInspectionDescription=null;
	  	}
	  	
		}
		
		
		items.clear();
		items = new ArrayList<Item>();
		if(LoginType.equals(getResources().getString(R.string.strInspector).toString()))
			items.add(new Header(null, "INSPECTION LIST",false,false,true,true));
		else
			items.add(new Header(null, "INSPECTION LIST",false,false,true,false));
	    populateData();
	    adapter = new TwoTextArrayAdapter(this, items);
        list.setAdapter(adapter);
		}
		else  if(as_index==2)
		{
			boolean isParentDone=false;
			String backSelectedTradeMasterID=SelectedTradeMasterID;
			String backSelectedTradeType=SelectedTradeType;
			String backSelectedTradeID=SelectedTradeID;
			int wantedPosition =2; // Whatever position you're looking for
			int firstPosition = list.getFirstVisiblePosition() - list.getHeaderViewsCount(); // This is the same as child #0
			int wantedChild = wantedPosition - firstPosition;
			if (wantedChild < 0 || wantedChild >= list.getChildCount()) 
			{
			  Log.d("Child Not Available", "");
			}
			else
			{
				if(!item.getTitle().toString().equalsIgnoreCase("add more")){
			final View v=list.getChildAt(wantedChild);
			TextView t=(TextView)v.findViewById(R.id.row_cell_addDefect_text);
			
			String SelectedTradeMaster=item.getTitle().toString();
			int pos=-1;
			for(int i=0;i<arrTradeMaster.length;i++){
				String temp=arrTradeMaster[i].TradeType;
				if(SelectedTradeMaster.equalsIgnoreCase(temp)){
					SelectedTradeMasterID=arrTradeMaster[i].ID;
					SelectedTradeType=arrTradeMaster[i].TradeType;
					SelectedTradeID=arrTradeMaster[i].ID;
					pos=i;
					break;
				}
			}
			TradeDependency TDarr[]=null;
			if(pos!=-1){
				if(arrTradeMaster[pos].DependencyCount>0){
					FMDBDatabaseAccess db=new FMDBDatabaseAccess(AddUpdateInspectionList.this);
					TDarr=db.getDependencyForTradeID(SelectedTradeMasterID);
					int yescount=0;
					if(TDarr!=null && TDarr.length>0){
						for(int i=0;i<TDarr.length;i++){
							boolean ispresent=false;
							String parentID=TDarr[i].DependencyID;
							for(int j=0;j<arrTradeMaster.length;j++){
								if(arrTradeMaster[j].ID.equalsIgnoreCase(parentID)){
									ispresent=true;
									break;
								}
							}
							
							
							if(ispresent){
							TradeAptAreaMaster obj=null;
							if(!isExtArea)
							{
								obj=db.getTradeAptAreaMaster(CurrentProject.getID(),CurrentBuilding.getID(), CurrentFloor.getID(), CurrentAPT.getID(), SelectedAreaTypeParent, TDarr[i].DependencyTradeName,SelectedAreaID);
							}
							else
							{
								 obj=db.getExtTradeAptAreaMaster(CurrentProject.getID(),CurrentBuilding.getID(),SelectedAreaTypeParent, TDarr[i].DependencyTradeName);
							}
							if(obj!=null &&( obj.PercentageComplete==100.0 || obj.PercentageComplete==100)){
								yescount++;
							}
							}
							else
								yescount++;
						}
						if(yescount==TDarr.length)
							isParentDone=true;
						else{
							isParentDone=false;
						}
					}
					else{
						isParentDone=false;
					}
					
				}
				else{
					isParentDone=true;
					
				}
//				if(!isParentDone && arrTradeMaster[pos].DependencyCount>0){
//					if(TDarr!=null && TDarr.length>0){
//					boolean  ispresent=false;
//					for(int i=0;i<TDarr.length;i++){
//						String parentID=TDarr[i].DependencyID;
//						for(int j=0;j<arrTradeMaster.length;j++){
//							if(arrTradeMaster[j].ID.equalsIgnoreCase(parentID)){
//								ispresent=true;
//								break;
//							}
//						}
//						if(ispresent)
//							break;
//					}
//					if(!ispresent)
//						isParentDone=true;
//					}
//				}
			}
			
			if(!isParentCheckOn)
				isParentDone=true;
			
			if(isParentDone){
			FMDBDatabaseAccess db=new FMDBDatabaseAccess(AddUpdateInspectionList.this);
			
			arrTDDistinctTrade=db.getTradeDetailsByTradeMasterID(SelectedTradeMasterID);
		  	
		  	if(arrTDDistinctTrade!=null && arrTDDistinctTrade.length>0){
		  		
		  		SelectedInspectionGroup=arrTDDistinctTrade[0].InspectionGroup;
		  	}
		  	else{
		  		SelectedInspectionGroup="";
		  	}
			
		  	if(SelectedInspectionGroup!=null && SelectedInspectionGroup.length()>0)
		  		arrTDDistinctInspectionGroup=db.getTradeDetailsByTradeType(SelectedTradeMasterID);
		  	else
		  		arrTDDistinctInspectionGroup=null;
		  	
		  	if(arrTDDistinctInspectionGroup!=null && arrTDDistinctInspectionGroup.length>0){
		  		SelectedInspectionGroup=arrTDDistinctInspectionGroup[0];
		  	}
		  	else{
		  		SelectedInspectionGroup="";
		  	}
		  		
			
		  	if(arrTDDistinctInspectionGroup!=null && arrTDDistinctInspectionGroup.length>0)
		  		arrTDDistinctInspectionDescription=db.getTradeDetailsByInspectionGroup(SelectedTradeMasterID, arrTDDistinctInspectionGroup[0]);
		  	else
		  		arrTDDistinctInspectionDescription=null;
		  	
		  	
		  	
		  		t.setText(""+item.getTitle().toString());
			}

		}
		}
			if(isParentDone){
			
			items.clear();
			items = new ArrayList<Item>();
			if(LoginType.equals(getResources().getString(R.string.strInspector).toString()))
				items.add(new Header(null, "INSPECTION LIST",false,false,true,true));
			else
				items.add(new Header(null, "INSPECTION LIST",false,false,true,false));
		    populateData();
		    adapter = new TwoTextArrayAdapter(this, items);
	        list.setAdapter(adapter);
			}
			else{
				SelectedTradeMasterID=backSelectedTradeMasterID==null?"":backSelectedTradeMasterID;
				SelectedTradeType=backSelectedTradeType==null?"":backSelectedTradeType;
				SelectedTradeID=backSelectedTradeID==null?"":backSelectedTradeID;
				new AlertDialog.Builder(AddUpdateInspectionList.this)
	    	    
	    	    .setMessage("Parent Job Inspection is not Complete.")
	    	    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
	    	        public void onClick(DialogInterface dialog, int which) { 
	    	        	
	    	        }
	    	     })
	    	     
	    	    .show();
			}

		}
		else if(as_index==3){

			//boolean isPreviosInspected=false;
		int wantedPosition =as_index; // Whatever position you're looking for
		int firstPosition = list.getFirstVisiblePosition() - list.getHeaderViewsCount(); // This is the same as child #0
		int wantedChild = wantedPosition - firstPosition;
		if (wantedChild < 0 || wantedChild >= list.getChildCount()) {
		  Log.d("Child Not Available", "");
		}
		else{
			
		final View v=list.getChildAt(wantedChild);
		TextView t=(TextView)v.findViewById(R.id.row_cell_addDefect_text);
		t.setText(""+item.getTitle().toString());
		SelectedInspectionGroup=item.getTitle().toString();
		
		
		FMDBDatabaseAccess db=new FMDBDatabaseAccess(AddUpdateInspectionList.this);
		
		
	  	if(arrTDDistinctInspectionGroup!=null && arrTDDistinctInspectionGroup.length>0)
	  		arrTDDistinctInspectionDescription=db.getTradeDetailsByInspectionGroup(SelectedTradeMasterID, SelectedInspectionGroup);
	  	

	
	}
		
		items.clear();
		items = new ArrayList<Item>();
		if(LoginType.equals(getResources().getString(R.string.strInspector).toString()))
			items.add(new Header(null, "INSPECTION LIST",false,false,true,true));
		else
			items.add(new Header(null, "INSPECTION LIST",false,false,true,false));
	    populateData();
	    adapter = new TwoTextArrayAdapter(this, items);
        list.setAdapter(adapter);

	
		}
		else if(as_index>=4){
			int wantedPosition =as_index; // Whatever position you're looking for
			int firstPosition = list.getFirstVisiblePosition() - list.getHeaderViewsCount(); // This is the same as child #0
			int wantedChild = wantedPosition - firstPosition;
			if (wantedChild < 0 || wantedChild >= list.getChildCount()) {
			  Log.d("Child Not Available", "");
			}
			else{
			final View v=list.getChildAt(wantedChild);
			TextView t=(TextView)v.findViewById(R.id.row_cell_addDefect_text);
			t.setText(""+item.getTitle().toString());
			arrTradeAptAreaDetail[as_index-4].InspectionEntry=""+item.getTitle().toString();
			Log.d("", "");
			}
			
			
		}
	}
	catch(Exception e)
	{
		Log.d("Exception",""+e.getMessage());
	}
		 return true;
	}
	
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
	
	public void BackClick(View v){
    	finish();
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
    				new AlertDialog.Builder(AddUpdateInspectionList.this)
    	    	    
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
    	    	        		Log.d("Exception",""+e.getMessage());
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
    	public void SyncData(){
        	//StartSyncProgress task=new StartSyncProgress();
        	//task.execute(10);
    		try{
    		ParseSyncData parser=new ParseSyncData(AddUpdateInspectionList.this);
    			    		parser.start();
    		}
        	catch(Exception e)
        	{
        		Log.d("Exception",""+e.getMessage());
        	}
        }
	public void populateData()
    {
    	try{
    		
    		FMDBDatabaseAccess db=new FMDBDatabaseAccess(getApplicationContext());
    		
    		
    		
    		//if(AreaList!=null && AreaList.length>0)
    			items.add(new ListItemAddDefect(null, "Area",""+SelectedArea,false));
    		//else
    		//	items.add(new ListItemAddDefect(null, "Area",""+CurrentAPT.getAptType(),false));
    		
    		FMDBDatabaseAccess obj=new FMDBDatabaseAccess(AddUpdateInspectionList.this);
    		//@ext
    		
    		
    		if(isExtArea)
    		{
    			MainIMtradeAptAreaMaster=obj.getExtTradeAptAreaMaster(CurrentProject.getID(),CurrentBuilding.getID(),SelectedAreaTypeParent, SelectedTradeType);
    		}
    		else if(isFromFloorExtArea)
    		{
    			MainIMtradeAptAreaMaster=obj.getExtTradeAptAreaMasterFloor(CurrentProject.getID(),CurrentBuilding.getID(),CurrentFloor.getID(),SelectedAreaTypeParent, SelectedTradeType);
    		}
    		else
    		{
    			MainIMtradeAptAreaMaster=obj.getTradeAptAreaMaster(CurrentProject.getID(), CurrentBuilding.getID(),CurrentFloor.getID(), CurrentAPT.getID(), SelectedAreaTypeParent, SelectedTradeType,SelectedAreaID);
    		}

//    		if(areas!=null && areas.length>0)
//    			items.add(new ListItemAddDefect(null, "Area",""+SelectedArea,false));
//    		else
//    			items.add(new ListItemAddDefect(null, "Area","",false));
//    		
    		items.add(new ListItemAddDefect(null, "Trade",""+SelectedTradeType,false));
    		
    		items.add(new ListItemAddDefect(null, "InspectionGroup",""+SelectedInspectionGroup,false));
    		
    		if(!isRotated){
    		if(SelectedAreaTypeParent!=null && SelectedAreaTypeParent.length()>0 && SelectedTradeType!=null && SelectedTradeType.length()>0)
    		{
    			//FMDBDatabaseAccess db=new FMDBDatabaseAccess(AddUpdateInspectionList.this);
    			
    			if(MainIMtradeAptAreaMaster!=null)
    			{
    				arrTradeAptAreaDetail=db.getTradeAptAreaDetail(MainIMtradeAptAreaMaster.ID, MainIMtradeAptAreaMaster.TradeID,SelectedInspectionGroup);
    				if(arrTradeAptAreaDetail!=null && arrTradeAptAreaDetail.length>0){
    				
    			}
    			else
    			{
    				if(arrTDDistinctInspectionDescription!=null && arrTDDistinctInspectionDescription.length>0)
    				{
    					arrTradeAptAreaDetail=new TradeAptAreaDetail[arrTDDistinctInspectionDescription.length];
    					for(int i=0;i<arrTradeAptAreaDetail.length;i++)
    					{
    						arrTradeAptAreaDetail[i]=new TradeAptAreaDetail();
    						arrTradeAptAreaDetail[i].InspectionEntry="Not Done";
    					}
    				}
    			}
    			}
    			else
    			{
    				if(arrTDDistinctInspectionDescription!=null && arrTDDistinctInspectionDescription.length>0)
    				{
    					arrTradeAptAreaDetail=new TradeAptAreaDetail[arrTDDistinctInspectionDescription.length];
    					for(int i=0;i<arrTradeAptAreaDetail.length;i++)
    					{
    						arrTradeAptAreaDetail[i]=new TradeAptAreaDetail();
    						arrTradeAptAreaDetail[i].InspectionEntry="Not Done";
    					}
    				}
    			}
    			
    			
    		}
    		}
    		isRotated=false;
    		
//    		if(arrInspectionList!=null && arrInspectionList.length>0){
//    			for(int i=0;i<arrInspectionList.length;i++)
//    				//items.add(new ListItemAddUpdateInspection(null, ""+arrInspectionList[i].getChecklistDescription(),"",false,i,arrInspectionList[i].getIsSelected()));
//    				items.add(new ListItemAddDefect(null, ""+arrInspectionList[i].getChecklistDescription(),""+arrInspectionDetails[i].getChecklistEntry(),false));
//    		}
    		if(arrTDDistinctInspectionDescription!=null && arrTDDistinctInspectionDescription.length>0)
    		{
    			for(int i=0;i<arrTDDistinctInspectionDescription.length;i++)
    				//if(arrTDDistinctInspectionDescription[i].TradeType.equalsIgnoreCase(SelectedTradeType) && arrTradeDetails[i].InspectionGroup.equalsIgnoreCase(SelectedInspectionGroup))
    					items.add(new ListItemAddDefect(null, ""+arrTDDistinctInspectionDescription[i].InspectionDescription,""+arrTradeAptAreaDetail[i].InspectionEntry,false));//+arrInspectionDetails[i].getChecklistEntry()
    		}
    		

    		
    		
    		
            
    	}
    	catch(Exception e){
    	 Log.d("Error=", ""+e.getMessage());
    	}
    }
	public void SaveClick(View v){
		try{
			FMDBDatabaseAccess db=new FMDBDatabaseAccess(AddUpdateInspectionList.this);
			ContractorMaster Con=db.getContractorByTradeType(SelectedTradeType);
    		if(MainIMtradeAptAreaMaster==null){
			MainIMtradeAptAreaMaster=new TradeAptAreaMaster();
			UUID id=UUID.randomUUID();
			MainIMtradeAptAreaMaster.ID=id.toString();
			MainIMtradeAptAreaMaster.ProjectID=CurrentProject.getID();
			MainIMtradeAptAreaMaster.BuildingID=CurrentBuilding.getID();
			
			
			
			if(isExtArea)
			{
				MainIMtradeAptAreaMaster.FloorID="";
				MainIMtradeAptAreaMaster.ApartmentID="";
				MainIMtradeAptAreaMaster.AptAreaID="";
			}
			else if(isFromFloorExtArea)
			{
				MainIMtradeAptAreaMaster.FloorID=CurrentFloor.getID();
				MainIMtradeAptAreaMaster.ApartmentID="";
				MainIMtradeAptAreaMaster.AptAreaID="";
			}
			else
			{

				MainIMtradeAptAreaMaster.FloorID=CurrentFloor.getID();
				MainIMtradeAptAreaMaster.ApartmentID=CurrentAPT.getID();
				try{
				MainIMtradeAptAreaMaster.AptAreaID=SelectedAreaID;//CurrsentApartmentArea.getID();
				}
				catch(Exception e){
					MainIMtradeAptAreaMaster.AptAreaID="";
				}
			}
			MainIMtradeAptAreaMaster.AptAreaTypeID=AreaTypeID;
			MainIMtradeAptAreaMaster.AptAreaType=SelectedAreaTypeParent;
			MainIMtradeAptAreaMaster.TradeID=SelectedTradeID;
			MainIMtradeAptAreaMaster.TradeType=SelectedTradeType;
			MainIMtradeAptAreaMaster.PercentageComplete=0.0;
			MainIMtradeAptAreaMaster.CreatedBy=RegUserID;
			MainIMtradeAptAreaMaster.CreatedDate=GetUTCdateTimeAsString();
			MainIMtradeAptAreaMaster.ModifiedBy=RegUserID;
			MainIMtradeAptAreaMaster.ModifiedDate=GetUTCdateTimeAsString();
			MainIMtradeAptAreaMaster.StatusForUpload="Inserted";
			
			
			
		}
    		else
    			MainIMtradeAptAreaMaster.StatusForUpload="Modified";
		int completecount=0;
		for(int i=0;i<arrTradeAptAreaDetail.length;i++){
			if(arrTradeAptAreaDetail[i].InspectionEntry.equalsIgnoreCase("Done") ||
					arrTradeAptAreaDetail[i].InspectionEntry.equalsIgnoreCase("Override")
					||arrTradeAptAreaDetail[i].InspectionEntry.equalsIgnoreCase("NA")){
				completecount++;
			}
		}
		
		double per=0.0;
		if(completecount>0)
			per=(double)((double)completecount/(double)arrTradeAptAreaDetail.length)*100;
		MainIMtradeAptAreaMaster.PercentageComplete=per;
		
		MainIMtradeAptAreaMaster.IsSyncedToWeb=false;
		
		if(Con!=null)
		{
			MainIMtradeAptAreaMaster.ContractorID=Con.ID;
			MainIMtradeAptAreaMaster.ContractorName=Con.ContractorName;
		}
		else
		{
			MainIMtradeAptAreaMaster.ContractorID="";
			MainIMtradeAptAreaMaster.ContractorName="";
		}
		
		//SyncTradeAptAreaMaster(MainIMtradeAptAreaMaster);
		
		for(int i=0;i<arrTradeAptAreaDetail.length;i++){
			UUID id=UUID.randomUUID();
			if(arrTradeAptAreaDetail[i].ID==null || arrTradeAptAreaDetail[i].ID.length()==0)
				arrTradeAptAreaDetail[i].ID=id.toString();
			arrTradeAptAreaDetail[i].TradeAptAreaMasterID=MainIMtradeAptAreaMaster.ID;
			arrTradeAptAreaDetail[i].TradeDetailID=arrTDDistinctInspectionDescription[i].ID;
			arrTradeAptAreaDetail[i].InspectionGroup=arrTDDistinctInspectionDescription[i].InspectionGroup;
			arrTradeAptAreaDetail[i].InspectionDescription=arrTDDistinctInspectionDescription[i].InspectionDescription;
			arrTradeAptAreaDetail[i].CreatedBy=RegUserID;
			arrTradeAptAreaDetail[i].CreatedDate=GetUTCdateTimeAsString();
			arrTradeAptAreaDetail[i].ModifiedBy=RegUserID;
			arrTradeAptAreaDetail[i].ModifiedDate=GetUTCdateTimeAsString();
			arrTradeAptAreaDetail[i].EnteredOnMachineID="";
			arrTradeAptAreaDetail[i].StatusForUpload="Inserted";
			arrTradeAptAreaDetail[i].IsSyncedToWeb=false;
				
			//SyncTradeAptAreaDetail(arrTradeAptAreaDetail[i]);                     
			
		}
		if(arrTradeAptAreaDetail!=null && arrTradeAptAreaDetail.length>0){
			TotalToUpload=arrTradeAptAreaDetail.length;
			
		}
		else
			TotalToUpload=0;
//			for(int i=0;i<arrTradeAptAreaDetail.length;i++){
//				
//				
//				db.insertORupdateTradeAptAreaDetail(arrTradeAptAreaDetail[i]);
//			}
		StartMasterSync task=new StartMasterSync();
		task.execute(10);
		//if(!isOnline)
			Toast.makeText(getApplicationContext(), "Inspection Saved.", Toast.LENGTH_LONG).show();	
		finish();	
			
		}
		catch(Exception e){
			Log.d("Error=", ""+e.getMessage());
		}
		
	}
	
	protected class StartMasterSync extends AsyncTask<Integer , Integer, Void> {
		boolean result=false;
    	@Override
        protected void onPreExecute() {  
        	/*if(!mProgressDialog2.isShowing() && isOnline){
        		mProgressDialog2.setCancelable(false);
        		mProgressDialog2.setMessage("Synchronizing...");
        		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
        		mProgressDialog2.show();
        	}*/
        	
        }      
        @Override
        protected Void doInBackground(Integer... params) {
        	
        	
    		try
    		{
    			//Log.d("StartMasterSync first call","from save");
    			if(isOnline){
    			SoapObject request=new SoapObject(NAMESPACE, METHOD_NAME_SaveNewDataToTheDataBase);
    			SoapSerializationEnvelope envelope=new SoapSerializationEnvelope(SoapEnvelope.VER11);
    			envelope.dotNet=true;
    			envelope.setOutputSoapObject(request);
    			
    			  String CoumnNames="ID~ProjectID~BuildingID~FloorID~ApartmentID~AptAreaID~AptAreaTypeID~AptAreaType~TradeID~TradeType~PercentageComplete~CreatedBy~CreatedDate~ModifiedBy~ModifiedDate~EnteredOnMachineID~ContractorID~ContractorName";
    			  String Values=""+MainIMtradeAptAreaMaster.ID+"~"+MainIMtradeAptAreaMaster.ProjectID+"~"+MainIMtradeAptAreaMaster.BuildingID+"~"+MainIMtradeAptAreaMaster.FloorID+"~"+MainIMtradeAptAreaMaster.ApartmentID+"~"+MainIMtradeAptAreaMaster.AptAreaID+"~"+MainIMtradeAptAreaMaster.AptAreaTypeID+"~"+MainIMtradeAptAreaMaster.AptAreaType+"~"+MainIMtradeAptAreaMaster.TradeID+"~"+MainIMtradeAptAreaMaster.TradeType+"~"+MainIMtradeAptAreaMaster.PercentageComplete+"~"+MainIMtradeAptAreaMaster.CreatedBy+"~"+MainIMtradeAptAreaMaster.CreatedDate+"~"+MainIMtradeAptAreaMaster.ModifiedBy+"~"+MainIMtradeAptAreaMaster.ModifiedDate+"~"+MainIMtradeAptAreaMaster.EnteredOnMachineID+"~"+MainIMtradeAptAreaMaster.ContractorID+"~"+MainIMtradeAptAreaMaster.ContractorName;
    			  String TableName="TradeAptAreaMaster";
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
    			
    			
    			
    			
    		}
    		catch (Exception e)
    		{
    			Log.d("SyncToWeb", ""+e.getMessage());
    		}
        	
    		
            return null;
        }
        @Override
        protected void onPostExecute(Void result1) {
        	
        	try{
        	if(result)
				MainIMtradeAptAreaMaster.IsSyncedToWeb=true;
			else
				MainIMtradeAptAreaMaster.IsSyncedToWeb=false;
			MainIMtradeAptAreaMaster.StatusForUpload="Inserted";
				
			FMDBDatabaseAccess db=new FMDBDatabaseAccess(AddUpdateInspectionList.this);
			db.insertORUpdatetradeAptAreaMaster(MainIMtradeAptAreaMaster);
			//Log.d("StartChildSync first call","from first");
			StartChildSync task=new StartChildSync();
			task.execute(10);
        	}
        	catch(Exception e)
        	{
        		Log.d("Exception",""+e.getMessage());
        	}
}
}
protected class StartChildSync extends AsyncTask<Integer , Integer, Void> {
	boolean result=false;
	TradeAptAreaDetail obj=null;
    	@Override
        protected void onPreExecute() {  
//        	if(!mProgressDialog2.isShowing()){
//        		mProgressDialog2.setCancelable(false);
//        		mProgressDialog2.setMessage("Synchronizing...");
//        		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
//        		mProgressDialog2.show();
//        	}
    		try{
        	if(TotalToUpload!=0 && Current<TotalToUpload){
        		obj=arrTradeAptAreaDetail[Current];
        	}
    		}
        	catch(Exception e)
        	{
        		Log.d("Exception",""+e.getMessage());
        	}
        }      
        @Override
        protected Void doInBackground(Integer... params) {
        	
        	
    		try
    		{
    			
    			if(obj!=null){
    			if(isOnline){
    			SoapObject request=new SoapObject(NAMESPACE, METHOD_NAME_SaveNewDataToTheDataBase);
    			SoapSerializationEnvelope envelope=new SoapSerializationEnvelope(SoapEnvelope.VER11);
    			envelope.dotNet=true;
    			envelope.setOutputSoapObject(request);
    			
    			  String CoumnNames="ID~TradeAptAreaMasterID~TradeDetailID~InspectionGroup~InspectionDescription~InspectionEntry~CreatedBy~CreatedDate~ModifiedBy~ModifiedDate~EnteredOnMachineID";
    			  String Values=""+obj.ID+"~"+obj.TradeAptAreaMasterID+"~"+obj.TradeDetailID+"~"+obj.InspectionGroup+"~"+obj.InspectionDescription+"~"+obj.InspectionEntry+"~"+obj.CreatedBy+"~"+obj.CreatedDate+"~"+obj.ModifiedBy+"~"+obj.ModifiedDate+"~"+obj.EnteredOnMachineID;
    			  String TableName="TradeAptAreaDetail";
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
    			}
    			
    		}
    		catch (Exception e)
    		{
    			Log.d("SyncToWeb", ""+e.getMessage());
    		}
        	
    		
            return null;
        }
        @Override
        protected void onPostExecute(Void result1) {
        	try{
        	if(obj!=null){
        	FMDBDatabaseAccess db=new FMDBDatabaseAccess(AddUpdateInspectionList.this);
			if(result)
				obj.IsSyncedToWeb=true;
			else
				obj.IsSyncedToWeb=false;
			//obj.StatusForUpload="Inserted";
			db.insertORupdateTradeAptAreaDetail(obj);
        	}
        	
        	if(Current<TotalToUpload){
        		Current++;
        		StartChildSync task=new StartChildSync();
        		task.execute(10);
        		//Log.d("StartChildSync onpost call",""+Current);
        	}
        	else{
        		//mProgressDialog2.dismiss();
               // finish();
        	}
        	}
        	catch(Exception e)
        	{
        		Log.d("Exception",""+e.getMessage());
        	}
}
}
	public String GetUTCdateTimeAsString()
	{
		final String DATEFORMAT = "yyyy-MM-dd HH:mm:ss aaa";

	    final SimpleDateFormat sdf = new SimpleDateFormat(DATEFORMAT);
	    sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
	   String utcTime = sdf.format(new Date());

	    return utcTime;
	}
	public void onScroll(AbsListView lw, final int firstVisibleItem,
            final int visibleItemCount, final int totalItemCount) {

		try{
			switch(lw.getId())
			{
				case R.id.android_add_defect_list:     
					//@scroll
					int j=0;
					final int lastItem = firstVisibleItem + visibleItemCount;
					Log.d("firstVisibleItem  visibleItemCount", ""+firstVisibleItem+" "+visibleItemCount);
					//Toast.makeText(AddUpdateInspectionList.this, "Toatalcount "+totalItemCount, Toast.LENGTH_SHORT).show();
					//if(lastItem == totalItemCount) 
					//{ 
					
					
//						if(firstVisibleItem>=4)
//						{
							for(int i=firstVisibleItem;i<firstVisibleItem+visibleItemCount;i++)
							{
								if(firstVisibleItem>=4){
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
									final View v=list.getChildAt(wantedChild);
									TextView t=(TextView)v.findViewById(R.id.row_cell_addDefect_text);
									t.setText(""+arrTradeAptAreaDetail[i-4].InspectionEntry);
									j++;
									Log.d("", "");
								}
								}
								
							}
					//}
//						}
//						if(lastItem == totalItemCount) 
//						{	
//							int wantedPosition =lastItem-1; // Whatever position you're looking for
//						
//							int firstPosition = list.getFirstVisiblePosition() - list.getHeaderViewsCount(); // This is the same as child #0
//							int wantedChild = wantedPosition - firstPosition;
//							if (wantedChild < 0 || wantedChild >= list.getChildCount())
//							{
//									Log.d("Child Not Available", "");
//							}
//							else
//							{
//									final View v=list.getChildAt(wantedChild);
//									TextView t=(TextView)v.findViewById(R.id.row_cell_addDefect_text);
//									t.setText(""+arrTradeAptAreaDetail[lastItem-4-1].InspectionEntry);
//												
//									Log.d("", "");
//							}
//						}
			}
		}
    	catch(Exception e)
    	{
    		Log.d("Exception",""+e.getMessage());
    	}
	}

	public void onScrollStateChanged(AbsListView lw, int scrollState) {
		// TODO Auto-generated method stub
		
	}
	
	
	
	public void SyncTradeAptAreaMaster(TradeAptAreaMaster obj){
		boolean result=false;
		try
		{
			if(isOnline){
			SoapObject request=new SoapObject(NAMESPACE, METHOD_NAME_SaveNewDataToTheDataBase);
			SoapSerializationEnvelope envelope=new SoapSerializationEnvelope(SoapEnvelope.VER11);
			envelope.dotNet=true;
			envelope.setOutputSoapObject(request);
			
			  String CoumnNames="ID~ProjectID~BuildingID~FloorID~ApartmentID~AptAreaID~AptAreaTypeID~AptAreaType~TradeID~TradeType~PercentageComplete~CreatedBy~CreatedDate~ModifiedBy~ModifiedDate~EnteredOnMachineID~ContractorID~ContractorName";
			  String Values=""+obj.ID+"~"+obj.ProjectID+"~"+obj.BuildingID+"~"+obj.FloorID+"~"+obj.ApartmentID+"~"+obj.AptAreaID+"~"+obj.AptAreaTypeID+"~"+obj.AptAreaType+"~"+obj.TradeID+"~"+obj.TradeType+"~"+obj.PercentageComplete+"~"+obj.CreatedBy+"~"+obj.CreatedDate+"~"+obj.ModifiedBy+"~"+obj.ModifiedDate+"~"+obj.EnteredOnMachineID+"~"+obj.ContractorID+"~"+obj.ContractorName;
			  String TableName="TradeAptAreaMaster";
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
			
			if(result)
				MainIMtradeAptAreaMaster.IsSyncedToWeb=true;
			else
				MainIMtradeAptAreaMaster.IsSyncedToWeb=false;
			MainIMtradeAptAreaMaster.StatusForUpload="Inserted";
				
			FMDBDatabaseAccess db=new FMDBDatabaseAccess(AddUpdateInspectionList.this);
			db.insertORUpdatetradeAptAreaMaster(MainIMtradeAptAreaMaster);
			
			
		}
		catch (Exception e)
		{
			Log.d("SyncToWeb", ""+e.getMessage());
		}
	}
	public void SyncTradeAptAreaDetail(TradeAptAreaDetail obj){
		boolean result=false;
		try
		{
			if(isOnline){
			SoapObject request=new SoapObject(NAMESPACE, METHOD_NAME_SaveNewDataToTheDataBase);
			SoapSerializationEnvelope envelope=new SoapSerializationEnvelope(SoapEnvelope.VER11);
			envelope.dotNet=true;
			envelope.setOutputSoapObject(request);
			
			  String CoumnNames="ID~TradeAptAreaMasterID~TradeDetailID~InspectionGroup~InspectionDescription~InspectionEntry~CreatedBy~CreatedDate~ModifiedBy~ModifiedDate~EnteredOnMachineID";
			  String Values=""+obj.ID+"~"+obj.TradeAptAreaMasterID+"~"+obj.TradeDetailID+"~"+obj.InspectionGroup+"~"+obj.InspectionDescription+"~"+obj.InspectionEntry+"~"+obj.CreatedBy+"~"+obj.CreatedDate+"~"+obj.ModifiedBy+"~"+obj.ModifiedDate+"~"+obj.EnteredOnMachineID;
			  String TableName="TradeAptAreaDetail";
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
			FMDBDatabaseAccess db=new FMDBDatabaseAccess(AddUpdateInspectionList.this);
			if(result)
				obj.IsSyncedToWeb=true;
			else
				obj.IsSyncedToWeb=false;
			//obj.StatusForUpload="Inserted";
			db.insertORupdateTradeAptAreaDetail(obj);
		}
		catch (Exception e)
		{
			Log.d("SyncToWeb", ""+e.getMessage());
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
        
        case R.id.menuBtn_exit:
        	new AlertDialog.Builder(AddUpdateInspectionList.this)
    	    
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
        	new AlertDialog.Builder(AddUpdateInspectionList.this)
    	    
    	    .setMessage("Are you sure you want to Logout?")
    	    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
    	        public void onClick(DialogInterface dialog, int which) { 
    	        	try{
    	            FMDBDatabaseAccess db=new FMDBDatabaseAccess(AddUpdateInspectionList.this);
    	            SharedPreferences SP = getSharedPreferences("AppDelegate",MODE_PRIVATE);
    				db.performLogout(SP.getString("RegUserID", ""));
    				Intent i=new Intent(AddUpdateInspectionList.this,com.snagreporter.Login_page.class);
    				
    				startActivity(i);
    				
    				finish();
    	        	}
    	        	catch(Exception e)
    	        	{
    	        		Log.d("Exception",""+e.getMessage());
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
}
