package com.snagreporter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.UUID;

import android.support.v4.view.ViewPager.LayoutParams;
import org.afree.chart.block.Arrangement;
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
import com.snagreporter.listitems.HeaderWithAdd;
import com.snagreporter.listitems.Item;
import com.snagreporter.listitems.ListItem;
import com.snagreporter.listitems.ListItemAddDefect;
import com.snagreporter.listitems.ListItemAddDefectPhoto;
import com.snagreporter.listitems.ListItemAddDependencyAddSave;
import com.snagreporter.listitems.ListItemAddedDependency;
import com.snagreporter.listitems.ListItemBlank;
import com.snagreporter.listitems.ListItemParent;
import com.snagreporter.menuhandler.MenuHandler;



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
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.InputType;
import android.text.method.DateTimeKeyListener;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.RemoteViews.ActionException;
import android.widget.TextView;

import com.snagreporter.AddDefect.DownloadDataFromWeb;
import com.snagreporter.EditDefect.DownloadImagesInBackGround;
import com.snagreporter.ProjectListPage.StartDownloadSnags;
import com.snagreporter.database.*;
import com.snagreporter.entity.ApartmentDetails;
import com.snagreporter.entity.ApartmentMaster;
import com.snagreporter.entity.BuildingMaster;
import com.snagreporter.entity.FaultType;
import com.snagreporter.entity.FloorMaster;
import com.snagreporter.entity.Inspector;
import com.snagreporter.entity.JobType;
import com.snagreporter.entity.ProjectMaster;
import com.snagreporter.entity.Registration;
import com.snagreporter.entity.SnagMaster;
import com.snagreporter.entity.SnagMasterDependency;
import com.snagreporter.entity.StdApartmentAreas;
import com.snagreporter.entity.StdFloorAreas;

import android.app.DatePickerDialog;


public class AddDependency extends Activity implements OnScrollListener
{
	String RegUserID="";
	String[] strSetValues;
	String[] strGetValues;
 	ListView list;
 	ListView list2;
	ImageButton imgVw1,imgVw2,imgVw3;
	int cnt=1;
	List<Item> items;
	List<Item> items2;
	String sample;
	BuildingMaster CurrentBuilding;
	FloorMaster CurrentFloor;
	ProjectMaster CurrentProject;
	ApartmentMaster CurrentAPT;
	String SelectedArea,SelectedAreaID,SelectedJobType,SelectedJobDetails,SelectedFaultType,SelectedInspector,selectedCost="",costTo="",snagPriority="";
	JobType arrJobType[];
	FaultType arrFaultType[];
	private static final int CAMERA_PIC_REQUEST = 1337;
	
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
	private int year, month, day;
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
	
	JobType[] AddedSnagType;
	FaultType[] AddedFaultType;
	SnagMaster NewSnag;
	SnagMaster[] arrSnagM;
	int selectedSnagIndex=0;
	String SelectedSnag="";
	int SelectSnagFrom=0;//1=floor,2=Building,3=Project
	ArrayList<String> AddedDepen;
	ArrayList<SnagMasterDependency> AddedDepen2;
	String ShowSnagIn="";
	

	View TopMenu;
		boolean isMenuVisible=false;
		MenuHandler menuhandler; 
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.add_dependencies);
       
        SharedPreferences SP = getSharedPreferences("AppDelegate",MODE_PRIVATE);
        RegUserID=SP.getString("RegUserID", "");
        
        menuhandler=new MenuHandler(AddDependency.this);
        TopMenu=new View(this);
        RelativeLayout.LayoutParams rlp=new RelativeLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
        TopMenu.setLayoutParams(rlp);
        LayoutInflater inflater= LayoutInflater.from(this);
        TopMenu=(View) inflater.inflate(R.layout.popup_menu, null);
        this.addContentView(TopMenu, rlp);
        TopMenu.requestLayout();
        TopMenu.setVisibility(View.INVISIBLE);
        
        mProgressDialog2 = new ProgressDialog(AddDependency.this);
        CurrentProject=(ProjectMaster) getIntent().getExtras().get("Project");
        CurrentBuilding=(BuildingMaster)getIntent().getExtras().get("Building");
        CurrentFloor=(FloorMaster)getIntent().getExtras().get("Floor");
        isAptmt=getIntent().getExtras().getBoolean("isAptmt");
        NewSnag=(SnagMaster) getIntent().getExtras().get("NewSnag");
        
        Button btn=(Button)findViewById(R.id.adddefectSavebtn);
        btn.setBackgroundDrawable(getResources().getDrawable(R.drawable.back_blue_button));
        
        
        
        FMDBDatabaseAccess obj=new FMDBDatabaseAccess(getApplicationContext());
        arrInspetor=new Registration[1];
        arrInspetor[0]=obj.getRegistrationDetailForID(SP.getString("RegUserID", ""));
        Object objNot = getLastNonConfigurationInstance();
        if(objNot!=null)
        {
        	Intent i=(Intent)objNot;
        	strGetValues=(String[])i.getExtras().get("strSetValues");
        	AddedFaultType=(FaultType[])i.getExtras().get("AddedFaultType");
        	AddedSnagType=(JobType[])i.getExtras().get("AddedSnagType");
        	arrJobType=(JobType[])i.getExtras().get("arrJobType");
        	arrFaultType=(FaultType[])i.getExtras().get("arrFaultType");
        	AddedDepen=(ArrayList<String>) i.getExtras().get("AddedDepen");
        	AddedDepen2=(ArrayList<SnagMasterDependency>) i.getExtras().get("AddedDepen2");
        	arrSnagM=(SnagMaster[])i.getExtras().get("arrSnagM");
        	selectedSnagIndex=i.getExtras().getInt("selectedSnagIndex");
        	SelectedSnag=(String)i.getExtras().get("SelectedSnag");
        	SelectSnagFrom=i.getExtras().getInt("SelectSnagFrom");
        	ShowSnagIn=(String) i.getExtras().get("ShowSnagIn");
           
        }
        
        if(isAptmt){
        	CurrentAPT=(ApartmentMaster)getIntent().getExtras().get("Apartment");
        	
        }
        else{
        	CurrentSFA=(StdFloorAreas)getIntent().getExtras().get("SFA");
        }
        
        
       
        
        FMDBDatabaseAccess db=new FMDBDatabaseAccess(getApplicationContext());
        AreaList=db.getApartmentDetails(CurrentAPT);
        imgVw1=(ImageButton)findViewById(R.id.row_cell_addDefectPhoto_img1);
        imgVw2=(ImageButton)findViewById(R.id.row_cell_addDefectPhoto_img2);
        imgVw3=(ImageButton)findViewById(R.id.row_cell_addDefectPhoto_img3);
        list=(ListView)findViewById(R.id.android_add_defect_list);
        list2=(ListView)findViewById(R.id.android_added_dependencies_list);
       
        if(AreaList==null || AreaList.length==0){
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
        strSetValues=new String[15];
        strSetValues[0]=SelectedArea;
        strSetValues[1]=SelectedJobType;
        strSetValues[2]=SelectedFaultType;
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
        
        Intent i=new Intent();
        i.putExtra("strSetValues", strSetValues);
        i.putExtra("AddedSnagType", AddedSnagType);
        i.putExtra("AddedFaultType", AddedFaultType);
        i.putExtra("arrJobType", arrJobType);
        i.putExtra("arrFaultType", arrFaultType);
        i.putExtra("AddedDepen", AddedDepen);
        i.putExtra("AddedDepen2", AddedDepen2);
        i.putExtra("arrSnagM", arrSnagM);
        i.putExtra("selectedSnagIndex", selectedSnagIndex);
        i.putExtra("SelectedSnag", SelectedSnag);
        i.putExtra("SelectSnagFrom", SelectSnagFrom);
        i.putExtra("ShowSnagIn", ShowSnagIn);
    	
        
        
        return i;
    }

   @Override
    public void onResume(){
    	super.onResume();
    	
    	SharedPreferences sharedPref = getSharedPreferences("AppDelegate",MODE_PRIVATE);
        Boolean id=sharedPref.getBoolean("isOnline", false);
        isOnline=id;
    }
  
    ///////////Date
    protected class DownloadProfileImage extends AsyncTask<Integer , Integer, Void> {
    	ProgressDialog mProgressDialog = new ProgressDialog(AddDependency.this);
    	JSONObject jObject;
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
			    		 FMDBDatabaseAccess obj=new FMDBDatabaseAccess(AddDependency.this);
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
    		    SelectedJobType=strGetValues[1];
    		    SelectedFaultType=strGetValues[2];
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
    			
    		}
    		items = new ArrayList<Item>();
  	       items.add(new HeaderWithAdd(null, ""+CurrentProject.getProjectName()+"-"+CurrentBuilding.getBuildingName()+"-"+CurrentFloor.getFloor()+"-"+CurrentAPT.getApartmentNo()+"-"+NewSnag.getAptAreaName()+"-"+NewSnag.getSnagType()+"-"+NewSnag.getFaultType(),"",true));
  	   
  	     FMDBDatabaseAccess obj=new FMDBDatabaseAccess(getApplicationContext());
  	     
 		//arrJobType=obj.getJobType();
 		
 		if(arrJobType==null || arrJobType.length==0){
 			arrJobType=obj.getJobType();
 		}
 		
 		if(arrFaultType==null || arrFaultType.length==0){
 			arrFaultType=obj.getFaultType(arrJobType[0].getID());
 		}
 		
  	       if(AreaList!=null && AreaList.length>0)
  	    	   if(SelectedArea==null || SelectedArea.length()==0)
  	    		   SelectedArea=AreaList[0].getAptAreaName();
	     //  else 
	    	// SelectedArea="";
	     //SelectedAreaID=AreaList[0].getaptare
  	     if(SelectedJobType==null || SelectedJobType.length()==0)
	       SelectedJobType=arrJobType[0].getJobType();
  	     
  	   if(SelectedJobDetails==null || SelectedJobDetails.length()==0)
	       SelectedJobDetails=arrJobType[0].getJobDetails();
	       
	       if(arrFaultType!=null && arrFaultType.length!=0){
	    	   if(SelectedFaultType==null || SelectedFaultType.length()==0)
	    		   SelectedFaultType=arrFaultType[0].getFaultType();
	    		   //SelectedFaultType="";
	    	   
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
	    	   ExpectedDate=GetDate();
	       
	       
	       SelectedArea=NewSnag.getApartment();
	       
	       if(arrSnagM==null || arrSnagM.length==0){
	       if(isAptmt)
	    	   //arrSnagM=obj.getSnags(CurrentProject.getID(),CurrentBuilding.getID(),CurrentFloor.getID(),CurrentAPT.getID());
	    	   arrSnagM=obj.getSnagsOfSnagTypeANDORFaultType(CurrentProject.getID(),CurrentBuilding.getID(), CurrentFloor.getID(), CurrentAPT.getID(), SelectedJobType, "", 0);
   			else
   				//arrSnagM=obj.getSnags(CurrentProject.getID(),CurrentBuilding.getID(),CurrentFloor.getID(),CurrentSFA.getID());
   				arrSnagM=obj.getSnagsOfSnagTypeANDORFaultType(CurrentProject.getID(),CurrentBuilding.getID(), CurrentFloor.getID(), CurrentSFA.getID(), SelectedJobType, "", 0);
	       }
//	       if(arrSnagM!=null && arrSnagM.length!=0){
//	    	   if(SelectedSnag==null || SelectedSnag.length()==0){
//	    		   SelectedSnag="All";
//	    	   }
//	       }
	       
	       if(ShowSnagIn!=null && ShowSnagIn.length()>0){
	    	   
	       }
	       else{
	    	  ShowSnagIn="Apartment"; 
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
  					// TODO Auto-generated method stub
  					if(position!=0 && position!=6 && position!=7  && position!=8  ){
  						//Toast.makeText(getApplicationContext(), "list position="+position, Toast.LENGTH_LONG).show();;
  						//View v=arg1;//ListItemAddDefect
  						//SelTextInList=(TextView)v.findViewById(R.id.row_cell_addDefect_text);
  						if(position==4 && arrSnagM!=null && arrSnagM.length>0){
  						registerForContextMenu(arg0);
  			    		as_index=position-1;
  			    		
  			    		openContextMenu(arg0);
  			    		unregisterForContextMenu(arg0);
  						}
  			    		else{
  			    			registerForContextMenu(arg0);
  	  			    		as_index=position-1;
  	  			    		openContextMenu(arg0);
  	  			    		unregisterForContextMenu(arg0);
  			    		}
  					}
  					/*else if(position==2){
  						AlertDialog.Builder alert = new AlertDialog.Builder(AddDependency.this);

  						alert.setTitle("Select SnagType");
  						final String[] GENRES = new String[] {
  	  					        "Action", "Adventure", "Animation", "Children", "Comedy", "Documentary", "Drama",
  	  					        "Foreign", "History", "Independent", "Romance", "Sci-Fi", "Television", "Thriller"
  	  					    };
  	  						final ListView listview=new ListView(AddDependency.this);
  	  						listview.setBackgroundColor(Color.WHITE);
  	  						listview.setScrollingCacheEnabled(false);
  	  					listview.setVerticalFadingEdgeEnabled(false);
  	  				listview.setFadingEdgeLength(0);
  	  						listview.setAdapter(new ArrayAdapter<String>(AddDependency.this,
  	  				                android.R.layout.simple_list_item_multiple_choice, GENRES));
  	  						
  	  						listview.setItemsCanFocus(false);
  	  						listview.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
  	  						alert.setView(listview);
  	  							alert.setPositiveButton("Done", new DialogInterface.OnClickListener() {
  	  	  						public void onClick(DialogInterface dialog, int whichButton) {
  	  	  							
  	  	  						}
  	  	  						});

  	  	  						alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
  	  	  						  public void onClick(DialogInterface dialog, int whichButton) {
  	  	  						    // Canceled.
  	  	  						  }
  	  	  						});

  	  	  						alert.show();
  					}*/
  					else if(position==6){
  						final View v=arg1;
  						TextView t=(TextView)v.findViewById(R.id.row_cell_addDefect_text);
  						String date=t.getText().toString();
  						String arr[]=date.split("-");
  						//month=Integer.parseInt(arr[0].toString());
  						//day=Integer.parseInt(arr[1].toString());
  						//year=Integer.parseInt(arr[2].toString());
  						CurrentDate=6;
  						showDialog(DATE_DIALOG_ID);
  					}
  					else if(position==7){
  						final View v=arg1;
  						TextView t=(TextView)v.findViewById(R.id.row_cell_addDefect_text); 
  						String date=t.getText().toString();
  						String arr[]=date.split("-");
  						//month=Integer.parseInt(arr[0].toString());
  						//day=Integer.parseInt(arr[1].toString());
  						//year=Integer.parseInt(arr[2].toString());
  						CurrentDate=7;
  						showDialog(DATE_DIALOG_ID);
  					}
  					else if(position==8)
  					{
  						AlertDialog.Builder alert = new AlertDialog.Builder(AddDependency.this);

  						alert.setTitle("Cost");
  						
  						final EditText input = new EditText(AddDependency.this);
  						
  						input.setInputType(InputType.TYPE_CLASS_NUMBER);
  						int wantedPosition = 8; // Whatever position you're looking for    
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
  	  						  String value = input.getText().toString();
  	  						  int wantedPosition = 8; // Whatever position you're looking for
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
  					else if(position==4){
  						AlertDialog.Builder alert = new AlertDialog.Builder(AddDependency.this);

  						alert.setTitle("Comments");
  						//alert.setMessage("Message");

  						// Set an EditText view to get user input 
  						final EditText input = new EditText(AddDependency.this);
  						int wantedPosition = 4; // Whatever position you're looking for    
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
  						  String value = input.getText().toString();
  						  int wantedPosition = 4; // Whatever position you're looking for
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
  						//
  				}
  			});
  	        
  	        reloadList();
    	}
    	catch(Exception e){
    		
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
        	
            return new DatePickerDialog(this, mDateSetListener, year, month, day);
        }
        return null;
    }



    // the call back received when the user "sets" the date in the dialog
    private DatePickerDialog.OnDateSetListener mDateSetListener =
        new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int myear, int monthOfYear, int dayOfMonth) {
                year = myear;
                month = monthOfYear;
                day = dayOfMonth;
                updateDate();
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
		String[] months={"01","02","03","04","05","06","07","08","09","10","11","12"};
		int wantedPosition=6;
		if(CurrentDate==6){
			wantedPosition = 6;
			
		}
		else if(CurrentDate==7)
			wantedPosition=7;
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
		
		
		t.setText(new StringBuilder().append(day).append('-')
				.append(months[month]).append('-').append(year));
		if(CurrentDate==6)
			ReportedDate=t.getText().toString();
		else 
			ExpectedDate=t.getText().toString();
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
    		
    		//if(AreaList!=null && AreaList.length>0)
    			//items.add(new ListItemAddDefect(null, "Current Snag",""+NewSnag.getApartment()+"-"+NewSnag.getSnagType()+"-"+NewSnag.getFaultType(),false));
    		//else
    		//	items.add(new ListItemAddDefect(null, "Area",""+CurrentAPT.getApartmentNo(),false));
    		
    		
    		items.add(new ListItemAddDefect(null, "SnagType",""+SelectedJobType,false));
    		
    	   if(arrFaultType!=null && arrFaultType.length>0){
    		   if(SelectedFaultType!=null && SelectedFaultType.length()>0){
    			   items.add(new ListItemAddDefect(null, "FaultType",""+SelectedFaultType,false));
    		   }
    		   else{
    			   items.add(new ListItemAddDefect(null, "FaultType","",false));
    		   }
    	   }
    		else 
    			items.add(new ListItemAddDefect(null, "FaultType","",false));
    	   
    	   
    	   
    	   items.add(new ListItemAddDefect(null, "Show Snags In",""+ShowSnagIn,false));
    	   
    	   if(arrSnagM!=null && arrSnagM.length>0)
    		   items.add(new ListItemAddDefect(null, "Snag",""+arrSnagM[selectedSnagIndex].getApartment()+"-"+arrSnagM[selectedSnagIndex].getSnagType()+"-"+arrSnagM[selectedSnagIndex].getFaultType(),false));//""+arrSnagM[0].getApartment()+"-"+arrSnagM[0].getSnagType()+"-"+arrSnagM[0].getFaultType()
    	   else
    		   items.add(new ListItemAddDefect(null, "Snag","",false));
    	   
    	   
    	   if(AddedDepen!=null && AddedDepen.size()>0){
    		   LinearLayout l=(LinearLayout)findViewById(R.id.add_depen_header);
    	    	l.setVisibility(View.VISIBLE);
    		   if(items2==null)
    	    		items2 = new ArrayList<Item>();
    		   for(int i=0;i<AddedDepen.size();i++){
    	    		items2.add(new ListItemAddedDependency(null, ""+AddedDepen.get(i),"",false));
    	    	}
    	    	
    	    	list2.setDivider(getResources().getDrawable(R.color.transparent));
    		       list2.setDividerHeight(1);
    		       
    		      adapter2 = new TwoTextArrayAdapter(this, items2);
    		     
    		    	 list2.setAdapter(adapter2); 
    	   }
    		
    		
            
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
    		as_index=10;
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
    		as_index=10;
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
    		as_index=10;
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
	
	if(as_index==10){
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
	else if(as_index==0){
//		menu.setHeaderTitle("Select Area");
//		for(int i=0;i<AreaList.length;i++){
//			menu.add(0, v.getId(), 0, ""+AreaList[i].getAptAreaName());
//		}
		menu.setHeaderTitle("Select SnagType");
		for(int i=0;i<arrJobType.length;i++){
			menu.add(0, v.getId(), 0, ""+arrJobType[i].getJobType());
		}
	}
	else if(as_index==1){
		
		//menu.add(0, v.getId(), 0, "Add More");

		if(arrFaultType!=null && arrFaultType.length>0){
		menu.setHeaderTitle("Select FaultType");
		//menu.add(0, v.getId(), 0, "All");
		if(arrFaultType!=null && arrFaultType.length>0){
		for(int i=0;i<arrFaultType.length;i++){
			menu.add(0, v.getId(), 0, ""+arrFaultType[i].getFaultType());
		}
		
		}
		else{
			
		}
		//menu.add(0, v.getId(), 0, "Add More");
		}
		else{


			AlertDialog.Builder alert = new AlertDialog.Builder(AddDependency.this);
			alert.setTitle("Add Fault Type");
			final EditText input = new EditText(AddDependency.this);
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
				

			
		
	
		}
	
	}
	else if(as_index==2){
		menu.setHeaderTitle("Show Snag In");
		//for(int i=0;i<arrInspetor.length;i++){
			menu.add(0, v.getId(), 0, "Apartment");
			menu.add(0, v.getId(), 0, "Floor");
			menu.add(0, v.getId(), 0, "Building");
			menu.add(0, v.getId(), 0, "Project");
	//}
	
	}
	else if(as_index==3){
		
		menu.setHeaderTitle("Select Snag");
		//if(arrSnagM.length>1)
		//	menu.add(0, v.getId(), 0, "All");
		for(int i=0;i<arrSnagM.length;i++){
			menu.add(0, v.getId(), 0, ""+arrSnagM[i].getApartment()+"-"+arrSnagM[i].getSnagType()+"-"+arrSnagM[i].getFaultType());
		}
	}
	else if(as_index==4){//Inspector
		menu.setHeaderTitle("Select Inspector");
		for(int i=0;i<arrInspetor.length;i++){
			menu.add(0, v.getId(), 0, ""+arrInspetor[0].getFirstName()+" "+arrInspetor[0].getLastName());
		}
	}
	else if(as_index==8)
	{
		menu.setHeaderTitle("Select CostTo");
		menu.add(0, v.getId(), 0,"Client");
		menu.add(0, v.getId(), 0,"Builder");
		menu.add(0, v.getId(), 0,"Contractor");
	}
	else if(as_index==9)
	{
		menu.setHeaderTitle("Select SnagPriority");
		menu.add(0, v.getId(), 0,"1");
		menu.add(0, v.getId(), 0,"2");
		menu.add(0, v.getId(), 0,"3");
		menu.add(0, v.getId(), 0,"4");
		menu.add(0, v.getId(), 0,"5");
	}
	else if(as_index==4){//Date
		
	}
	    
	}  
@Override
	public boolean onContextItemSelected(MenuItem item)
	{
	if(as_index==10){
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
		 if(strFromImg.equalsIgnoreCase("img1")){
			 RemovedPhotoURL=PhotoURl1;
			 imgVw1.setBackgroundDrawable(getResources().getDrawable(R.drawable.image_add));
			 PhotoURl1="";
			 isImageSetFor1=false;
		 }
		 else if(strFromImg.equalsIgnoreCase("img2")){
			 RemovedPhotoURL=PhotoURl2;
			 imgVw2.setBackgroundDrawable(getResources().getDrawable(R.drawable.image_add));
			 PhotoURl2="";
			 isImageSetFor2=false;
		 }
		 else if(strFromImg.equalsIgnoreCase("img3")){
			 RemovedPhotoURL=PhotoURl3;
			 imgVw3.setBackgroundDrawable(getResources().getDrawable(R.drawable.image_add));
			 PhotoURl3="";
			 isImageSetFor3=false;
		 }
		 return true;
	 }
	 else if(item.getTitle()=="View Image"){
		 
		 Intent i=new Intent(AddDependency.this,com.snagreporter.ViewImagePage.class);
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
//		int wantedPosition =1; // Whatever position you're looking for
//		int firstPosition = list.getFirstVisiblePosition() - list.getHeaderViewsCount(); // This is the same as child #0
//		int wantedChild = wantedPosition - firstPosition;
//		// Say, first visible position is 8, you want position 10, wantedChild will now be 2
//		// So that means your view is child #2 in the ViewGroup:
//		if (wantedChild < 0 || wantedChild >= list.getChildCount()) {
//		  Log.d("Child Not Available", "");
//		}
//		else{
//		final View v=list.getChildAt(wantedChild);
//		TextView t=(TextView)v.findViewById(R.id.row_cell_addDefect_text);
//		t.setText(""+item.getTitle().toString());
//		SelectedArea=item.getTitle().toString();
//		}

		int wantedPosition =1; // Whatever position you're looking for
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
			wantedPosition =2; // Whatever position you're looking for
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
			t2.setText(""+arrFaultType[0].getFaultType());//+arrFaultType[0].getFaultType()
			SelectedFaultType=arrFaultType[0].getFaultType();
			}
		}
		//SelectedFaultType="";
		
		{
			arrSnagM=db.getSnagsOfSnagTypeANDORFaultType(CurrentProject.getID(),CurrentBuilding.getID(), CurrentFloor.getID(), CurrentAPT.getID(), SelectedJobType, "", SelectSnagFrom);
		
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
			
			if(arrSnagM!=null && arrSnagM.length>0){
//				if(arrSnagM.length==1){
					t2.setText(""+arrSnagM[0].getApartment()+"-"+arrSnagM[0].getSnagType()+"-"+arrSnagM[0].getFaultType());
//				}
//				else{
					//t2.setText("All");
				//}
			}
			else{
				t2.setText("");
			}
			
			}
		}
		
		}
			
		
		}
		
		
		
		

	
		
	}
	else if(as_index==8)
	{
		int wantedPosition =9; // Whatever position you're looking for
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
		}
	}
	else if(as_index==9)
	{
		int wantedPosition =10; // Whatever position you're looking for
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
		}
	}
	else if(as_index==1){

		int wantedPosition =2; // Whatever position you're looking for
		int firstPosition = list.getFirstVisiblePosition() - list.getHeaderViewsCount(); // This is the same as child #0
		int wantedChild = wantedPosition - firstPosition;
		// Say, first visible position is 8, you want position 10, wantedChild will now be 2
		// So that means your view is child #2 in the ViewGroup:
		if (wantedChild < 0 || wantedChild >= list.getChildCount()) {
		  Log.d("Child Not Available", "");
		}
		else{
			if(!item.getTitle().toString().equalsIgnoreCase("All")){
				final View v=list.getChildAt(wantedChild);
				TextView t=(TextView)v.findViewById(R.id.row_cell_addDefect_text);
				t.setText(""+item.getTitle().toString());
				SelectedFaultType=item.getTitle().toString();
			}
			else{
				final View v=list.getChildAt(wantedChild);
				TextView t=(TextView)v.findViewById(R.id.row_cell_addDefect_text);
				t.setText(""+item.getTitle().toString());
				//SelectedFaultType=item.getTitle().toString();
			}
			{
				FMDBDatabaseAccess db=new FMDBDatabaseAccess(getApplicationContext());
				arrSnagM=db.getSnagsOfSnagTypeANDORFaultType(CurrentProject.getID(),CurrentBuilding.getID(), CurrentFloor.getID(), CurrentAPT.getID(), SelectedJobType, SelectedFaultType, SelectSnagFrom);
			
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
				
				if(arrSnagM!=null && arrSnagM.length>0){
					//if(arrSnagM.length==1){
						t2.setText(""+arrSnagM[0].getApartment()+"-"+arrSnagM[0].getSnagType()+"-"+arrSnagM[0].getFaultType());
//					}
//					else{
//						t2.setText("All");
//					}
				}
				else{
					t2.setText("");
				}
				
				}
			}
		}
	
	}
	else if(as_index==2){

		int wantedPosition =3;
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
//		for(int i=0;i<arrInspetor.length;i++){
//			if(t.getText().toString().equalsIgnoreCase(""+arrInspetor[0].getFirstName()+" "+arrInspetor[0].getLastName())){
//				selectedInspectorIndex=i;
//				break;
//			}
//				
//		}
		if(item.getTitle().toString().equals("Apartment"))
			SelectSnagFrom=0;
		else if(item.getTitle().toString().equals("Floor"))
			SelectSnagFrom=1;
		else if(item.getTitle().toString().equals("Building"))
			SelectSnagFrom=2;
		else if(item.getTitle().toString().equals("Project"))
			SelectSnagFrom=3;
		ShowSnagIn=item.getTitle().toString();
		SelectSnagIn();
		}
		// Whatever position you're looking for
		
	
	}
	else if(as_index==3){
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
		for(int i=0;i<arrSnagM.length;i++){
			if(t.getText().toString().equalsIgnoreCase(""+arrSnagM[i].getApartment()+"-"+arrSnagM[i].getSnagType()+"-"+arrSnagM[i].getFaultType())){
				selectedSnagIndex=i;
				break;
			}
				
		}
		}
	}
	else if(as_index==4){
		int wantedPosition =5; // Whatever position you're looking for
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
		for(int i=0;i<arrInspetor.length;i++){
			if(t.getText().toString().equalsIgnoreCase(""+arrInspetor[0].getFirstName()+" "+arrInspetor[0].getLastName())){
				selectedInspectorIndex=i;
				break;
			}
				
		}
		}
	}
	 
	
	 return true;
	}
public void SelectSnagIn(){

	//Toast.makeText(getApplicationContext(), "1", Toast.LENGTH_LONG).show();
	
	{
		FMDBDatabaseAccess db=new FMDBDatabaseAccess(getApplicationContext());
		arrSnagM=db.getSnagsOfSnagTypeANDORFaultType(CurrentProject.getID(),CurrentBuilding.getID(), CurrentFloor.getID(), CurrentAPT.getID(), SelectedJobType, SelectedFaultType, SelectSnagFrom);
	
		int wantedPosition =4; // Whatever position you're looking for
		int firstPosition = list.getFirstVisiblePosition() - list.getHeaderViewsCount(); // This is the same as child #0
		int wantedChild = wantedPosition - firstPosition;
		// Say, first visible position is 8, you want position 10, wantedChild will now be 2
		// So that means your view is child #2 in the ViewGroup:
		if (wantedChild < 0 || wantedChild >= list.getChildCount()) {
		  Log.d("Child Not Available", "");
		}
		else{
			
		final View v2=list.getChildAt(wantedChild);
		TextView t2=(TextView)v2.findViewById(R.id.row_cell_addDefect_text);
		
		if(arrSnagM!=null && arrSnagM.length>0){
//			if(arrSnagM.length==1){
				t2.setText(""+arrSnagM[0].getApartment()+"-"+arrSnagM[0].getSnagType()+"-"+arrSnagM[0].getFaultType());
				selectedSnagIndex=0;
//			}
//			else{
				//t2.setText("All");
			//}
		}
		else{
			t2.setText("");
		}
		
		}
	}

}
	public void Radio1Click(View v){
		//Toast.makeText(getApplicationContext(), "1", Toast.LENGTH_LONG).show();
		SelectSnagFrom=1;
		{
			FMDBDatabaseAccess db=new FMDBDatabaseAccess(getApplicationContext());
			arrSnagM=db.getSnagsOfSnagTypeANDORFaultType(CurrentProject.getID(),CurrentBuilding.getID(), CurrentFloor.getID(), CurrentAPT.getID(), SelectedJobType, SelectedFaultType, SelectSnagFrom);
		
			int wantedPosition =4; // Whatever position you're looking for
			int firstPosition = list.getFirstVisiblePosition() - list.getHeaderViewsCount(); // This is the same as child #0
			int wantedChild = wantedPosition - firstPosition;
			// Say, first visible position is 8, you want position 10, wantedChild will now be 2
			// So that means your view is child #2 in the ViewGroup:
			if (wantedChild < 0 || wantedChild >= list.getChildCount()) {
			  Log.d("Child Not Available", "");
			}
			else{
				
			final View v2=list.getChildAt(wantedChild);
			TextView t2=(TextView)v2.findViewById(R.id.row_cell_addDefect_text);
			
			if(arrSnagM!=null && arrSnagM.length>0){
//				if(arrSnagM.length==1){
//					t2.setText(""+arrSnagM[0].getApartment()+"-"+arrSnagM[0].getSnagType()+"-"+arrSnagM[0].getFaultType());
//				}
//				else{
					t2.setText("All");
				//}
			}
			else{
				t2.setText("");
			}
			
			}
		}
	}
	public void Radio2Click(View v){
		//Toast.makeText(getApplicationContext(), "2", Toast.LENGTH_LONG).show();
		SelectSnagFrom=2;
		{
			FMDBDatabaseAccess db=new FMDBDatabaseAccess(getApplicationContext());
			arrSnagM=db.getSnagsOfSnagTypeANDORFaultType(CurrentProject.getID(),CurrentBuilding.getID(), CurrentFloor.getID(), CurrentAPT.getID(), SelectedJobType, SelectedFaultType, SelectSnagFrom);
		
			int wantedPosition =4; // Whatever position you're looking for
			int firstPosition = list.getFirstVisiblePosition() - list.getHeaderViewsCount(); // This is the same as child #0
			int wantedChild = wantedPosition - firstPosition;
			// Say, first visible position is 8, you want position 10, wantedChild will now be 2
			// So that means your view is child #2 in the ViewGroup:
			if (wantedChild < 0 || wantedChild >= list.getChildCount()) {
			  Log.d("Child Not Available", "");
			}
			else{
				
			final View v2=list.getChildAt(wantedChild);
			TextView t2=(TextView)v2.findViewById(R.id.row_cell_addDefect_text);
			
			if(arrSnagM!=null && arrSnagM.length>0){
//				if(arrSnagM.length==1){
//					t2.setText(""+arrSnagM[0].getApartment()+"-"+arrSnagM[0].getSnagType()+"-"+arrSnagM[0].getFaultType());
//				}
//				else{
					t2.setText("All");
				//}
			}
			else{
				t2.setText("");
			}
			
			}
		}
	}
	public void Radio3Click(View v){
		//Toast.makeText(getApplicationContext(), "3", Toast.LENGTH_LONG).show();
		SelectSnagFrom=3;
		{
			FMDBDatabaseAccess db=new FMDBDatabaseAccess(getApplicationContext());
			arrSnagM=db.getSnagsOfSnagTypeANDORFaultType(CurrentProject.getID(),CurrentBuilding.getID(), CurrentFloor.getID(), CurrentAPT.getID(), SelectedJobType, SelectedFaultType, SelectSnagFrom);
		
			int wantedPosition =4; // Whatever position you're looking for
			int firstPosition = list.getFirstVisiblePosition() - list.getHeaderViewsCount(); // This is the same as child #0
			int wantedChild = wantedPosition - firstPosition;
			// Say, first visible position is 8, you want position 10, wantedChild will now be 2
			// So that means your view is child #2 in the ViewGroup:
			if (wantedChild < 0 || wantedChild >= list.getChildCount()) {
			  Log.d("Child Not Available", "");
			}
			else{
				
			final View v2=list.getChildAt(wantedChild);
			TextView t2=(TextView)v2.findViewById(R.id.row_cell_addDefect_text);
			
			if(arrSnagM!=null && arrSnagM.length>0){
//				if(arrSnagM.length==1){
//					t2.setText(""+arrSnagM[0].getApartment()+"-"+arrSnagM[0].getSnagType()+"-"+arrSnagM[0].getFaultType());
//				}
//				else{
					t2.setText("All");
				//}
			}
			else{
				t2.setText("");
			}
			
			}
		}
	}
protected class SyncJobtype extends AsyncTask<Integer , Integer, Void> {
	JobType objJob=null;
	boolean isAlldone=false;
    @Override
    protected void onPreExecute() {
    	FMDBDatabaseAccess db=new FMDBDatabaseAccess(AddDependency.this);
    	objJob=db.getJobTypeNotSynced();
    	if(objJob==null){
    		isAlldone=true;
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
    	FMDBDatabaseAccess db=new FMDBDatabaseAccess(AddDependency.this);
    	if(objJob!=null){
    		db.updateJobTypeSynced(objJob.getID());
    	}
    	if(!isAlldone){
    		SyncJobtype task=new SyncJobtype();
    		task.execute(10);
    	}
    	
    	
    	
        
    }
     
}
protected class SyncFaulttype extends AsyncTask<Integer , Integer, Void> {
	FaultType objFlt=null;
	boolean isAlldone=false;
    @Override
    protected void onPreExecute() { 
    	FMDBDatabaseAccess db=new FMDBDatabaseAccess(AddDependency.this);
    	objFlt=db.getFaultTypeNotSynced();
    	if(objFlt==null){
    		isAlldone=true;
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
    	
    	FMDBDatabaseAccess db=new FMDBDatabaseAccess(AddDependency.this);
    	if(objFlt!=null){
    		db.updateFaultTypeSynced(objFlt.getID());
    	}
    	if(!isAlldone){
    		SyncFaulttype task=new SyncFaulttype();
    		task.execute(10);
    	}
    	
        
    }
     
}

protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) 
{
	 super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
	 try{
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
   		int wantedPosition = 11;
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
			
		}
		
   	 }
   	 else if(strFromImgvw.equalsIgnoreCase("img2"))
   	 {
   		int wantedPosition = 11;
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
   		
		}
   	 }
   	 else if(strFromImgvw.equalsIgnoreCase("img3"))
   	 {
   		int wantedPosition = 11;
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
   		
		}
   	 }
   	 //adapter.notifyDataSetChanged();
   	 Handler handler = new Handler();
   	 handler.postDelayed(timedTask, 1000);
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
    	}
    
    //}
    
    
    
    }
	 if(resultCode==10001){
			setResult(10001);
			finish();
		}
	 if(resultCode==10002){
			setResult(10002);
			finish();
		}
	 }
	 
	 catch(Exception e){
		 Log.d("Error=", ""+e.getMessage());
		
	 }
}

private Runnable timedTask = new Runnable(){

	  
	  public void run() {
	   // TODO Auto-generated method stub
		synchronized(this){
			//adapter.notifyDataSetChanged();
			//reloadList();
			if(strFromImgvw.equalsIgnoreCase("img1"))
		   	 {
		   		int wantedPosition = 11;
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
					
					ImageButton btnI=new ImageButton(AddDependency.this);
					btnI.setImageBitmap(BtnImageBmp);
					imgVw1=(ImageButton)v.findViewById(R.id.row_cell_addDefectPhoto_img1);
					imgVw1.setBackgroundDrawable(btnI.getDrawable());
					//imgVw1.setBackgroundColor(getResources().getColor(R.color.transparent));
					//imgVw1.setImageBitmap(BtnImageBmp);
					
				}
				
		   	 }
		   	 else if(strFromImgvw.equalsIgnoreCase("img2"))
		   	 {
		   		int wantedPosition = 11;
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
		   		
		   		ImageButton btnI=new ImageButton(AddDependency.this);
				btnI.setImageBitmap(BtnImageBmp);
				imgVw2=(ImageButton)v.findViewById(R.id.row_cell_addDefectPhoto_img2);
		   		 imgVw2.setBackgroundDrawable(btnI.getDrawable());
				//imgVw2.setBackgroundColor(getResources().getColor(R.color.transparent));
				//imgVw2.setImageBitmap(BtnImageBmp);
		   		
				}
		   	 }
		   	 else if(strFromImgvw.equalsIgnoreCase("img3"))
		   	 {
		   		int wantedPosition = 11;
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
		   		
		   		ImageButton btnI=new ImageButton(AddDependency.this);
				btnI.setImageBitmap(BtnImageBmp);
				imgVw3=(ImageButton)v.findViewById(R.id.row_cell_addDefectPhoto_img3);
		   		 imgVw3.setBackgroundDrawable(btnI.getDrawable());
				//imgVw2.setBackgroundColor(getResources().getColor(R.color.transparent));
		   		//imgVw3.setImageBitmap(BtnImageBmp);
		   		
				}
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
		FMDBDatabaseAccess db=new FMDBDatabaseAccess(AddDependency.this);
		if(AddedSnagType!=null && AddedSnagType.length>0){
			for(int i=0;i<AddedSnagType.length;i++)
				db.insertORUpdateJobType(AddedSnagType[i]);
		}
		if(AddedFaultType!=null && AddedFaultType.length>0){
			for(int i=0;i<AddedFaultType.length;i++)
				db.insertORUpdateFaultType(AddedFaultType[i]);
		}
		
		//DownloadDataFromWeb task=new DownloadDataFromWeb();
		//task.execute(10);
		String photo[]={PhotoURl1,PhotoURl2,PhotoURl3};
    	Intent i=new Intent(AddDependency.this,com.snagreporter.imagemap.ImageMapActivity.class);
    	i.putExtra("ImageName", CurrentFloor.getFloorPlanImage());
    	i.putExtra("currentsnag",NewSnag);
    	i.putExtra("AddedSnagType",AddedSnagType);
    	i.putExtra("AddedFaultType",AddedFaultType);
    	i.putExtra("photo",photo);
    	if(CurrentFloor.getFloorPlanImage()!=null && CurrentFloor.getFloorPlanImage().length()>0)
    		startActivityForResult(i, 10002);
    	else{
    		DownloadDataFromWeb task=new DownloadDataFromWeb();
    		task.execute(10);
    		setResult(10002);
    		finish();
    	}
    	
		
	}
	catch(Exception e){
		Log.d("Error=", ""+e.getMessage());
	}
	
}
	protected class DownloadDataFromWeb extends AsyncTask<Integer , Integer, Void> {
		ProgressDialog mProgressDialog = new ProgressDialog(AddDependency.this);
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
	    		    
	    		    
	    		    String CoumnNames="ID~SnagType~SnagDetails~PictureURL1~PictureURL2~PictureURL3~ProjectID~ProjectName~BuildingID~BuildingName~FloorID~Floor~ApartmentID~Apartment~AptAreaName~SnagStatus~ResolveDate~ExpectedInspectionDate~FaultType~InspectorID~InspectorName~Cost~CostTo~PriorityLevel~AllocatedTo~AllocatedToName~ContractorID~ContractorName~SubContractorID~SubContractorName~SubSubContractorID~SubSubContractorName~ReportDate~CreatedBy";
	    		    String Values=""+obj.getID()+"~"+obj.getSnagType()+"~"+obj.getSnagDetails()+"~"+obj.getPictureURL1()+"~"+obj.getPictureURL2()+"~"+obj.getPictureURL3()+"~"+obj.getProjectID()+"~"+obj.getProjectName()+"~"+obj.getBuildingID()+"~"+obj.getBuildingName()+"~"+obj.getFloorID()+"~"+obj.getFloor()+"~"+obj.getApartmentID()+"~"+obj.getApartment()+"~"+obj.getAptAreaName()+"~"+obj.getSnagStatus()+"~"+obj.getResolveDate()+"~"+obj.getExpectedInspectionDate()+"~"+obj.getFaultType()+"~"+obj.getInspectorID()+"~"+obj.getInspectorName()+"~"+obj.getCost()+"~"+obj.getCostTo()+"~"+obj.getSnagPriority()+"~"+obj.getAllocatedTo()+"~"+obj.getAllocatedToName()+"~"+obj.getContractorID()+"~"+obj.getContractorName()+"~"+obj.getSubContractorID()+"~"+obj.getSubContractorName()+"~"+obj.getSubSubContractorID()+"~"+obj.getSubSubContractorName()+"~"+obj.getReportDate()+"~"+RegUserID;
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
        	

        	FMDBDatabaseAccess db=new FMDBDatabaseAccess(AddDependency.this);
        	
        	
        	
        	
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
    		
    		db.InsertIntoSnagMaster(obj);
    		
    		
        	
    		if(isOnline){
        		if(AddedDepen2!=null && AddedDepen2.size()>0){
        	for(int i=0;i<AddedDepen2.size();i++){
        		SnagMasterDependency obj=AddedDepen2.get(i);
    			saveNewDependency(obj);
    			
    		}
        		}
        	}
        	if(AddedDepen2!=null && AddedDepen2.size()>0){
        		SnagMasterDependency[] arr=new SnagMasterDependency[AddedDepen2.size()];
        		for(int i=0;i<AddedDepen2.size();i++){
        			SnagMasterDependency obj=AddedDepen2.get(i);
        			if(isOnline){
        				obj.setIsDataSyncToWeb(true);
        			}
        			arr[i]=obj;
        		}
        		db.insertDependency(arr);
        		
        	}
        	//if(mProgressDialog.isShowing())
    		//	mProgressDialog.dismiss();
    		
    		db=null;
    		//setResult(10002);
    		//finish();
        	
        	
        	
            
        }
         
}
	public void saveNewDependency(SnagMasterDependency obj){
		try{
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
    		    
    		    
    		    String CoumnNames="DId~ParentSnagId~SnagId~JobType~ProjectId~BuildingId~FloorId~ApartmentID";
    		    String Values=""+obj.getDId()+"~"+obj.getParentSnagId()+"~"+obj.getSnagId()+"~"+obj.getJobType()+"~"+obj.getProjectId()+"~"+obj.getBuildingId()+"~"+obj.getFloorId()+"~"+obj.getApartmentID();
    		    String TableName="SnagMasterDependency";
    		    
    		    request.addProperty("_strCoumnNames",CoumnNames);
    		    request.addProperty("_strValues",Values);
    		    request.addProperty("_strTableName",TableName);
    		    
    		    
    		    HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
    		    androidHttpTransport.call(SOAP_ACTION, envelope);
    		    Object resonse=envelope.getResponse();
    		    String output = resonse.toString();
    		    Log.d("SnagID=", ""+NewSnag.getID());
    		    Log.d("DID=", ""+obj.getDId());
    		    Log.d("Result=", ""+output);
    		}
    		catch(Exception e){
    			
    		}
		}
    		
		catch(Exception e){
			
		}
	}
	
public void uploadImage2(String url)
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
        	new AlertDialog.Builder(AddDependency.this)
    	    
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
        	new AlertDialog.Builder(AddDependency.this)
    	    
    	    .setMessage("Are you sure you want to Logout?")
    	    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
    	        public void onClick(DialogInterface dialog, int which) { 
    	            FMDBDatabaseAccess db=new FMDBDatabaseAccess(AddDependency.this);
    	            SharedPreferences SP = getSharedPreferences("AppDelegate",MODE_PRIVATE);
    				db.performLogout(SP.getString("RegUserID", ""));
    				Intent i=new Intent(AddDependency.this,com.snagreporter.Login_page.class);
    				
    				startActivity(i);
    				
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
        	if(!mProgressDialog2.isShowing()){
        	mProgressDialog2.setCancelable(false);
        	mProgressDialog2.setMessage("Synchronizing...");
        	mProgressDialog2.show();
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
        	if(obj!=null){
        	//mProgressDialog.dismiss();

    		
    		FMDBDatabaseAccess db=new FMDBDatabaseAccess(AddDependency.this);
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
	        			int wantedPosition = 11;
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
	   					
	   					ImageButton btnI=new ImageButton(AddDependency.this);
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
	        	   		
	        			int wantedPosition = 11;
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
	   		   		
	   		   		ImageButton btnI=new ImageButton(AddDependency.this);
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
	        			int wantedPosition = 11;
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
	   		   		
	   		   		ImageButton btnI=new ImageButton(AddDependency.this);
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
	            }
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
    				new AlertDialog.Builder(AddDependency.this)
    	    	    
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
    			Intent i=new Intent(AddDependency.this,com.snagreporter.GraphWebView.class);
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
    		ParseSyncData parser=new ParseSyncData(AddDependency.this);
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
                	FMDBDatabaseAccess db=new FMDBDatabaseAccess(AddDependency.this);
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
                	FMDBDatabaseAccess db=new FMDBDatabaseAccess(AddDependency.this);
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
            	FMDBDatabaseAccess db=new FMDBDatabaseAccess(AddDependency.this);
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
    public void CellAddNewClick(View v){
    	
    }
    public void AddDefectClick(View v){
    	LinearLayout l=(LinearLayout)findViewById(R.id.add_depen_header);
    	l.setVisibility(View.VISIBLE);
    	if(items2==null)
    		items2 = new ArrayList<Item>();
    	
    	if(AddedDepen==null)
    		AddedDepen=new ArrayList<String>();
    	
    	if(AddedDepen2==null)
    		AddedDepen2=new ArrayList<SnagMasterDependency>();
    	String str="";
    	UUID uuid=UUID.randomUUID();
    	SnagMasterDependency obj=new SnagMasterDependency();
    	obj.setDId(uuid.toString());
    	obj.setParentSnagId(NewSnag.getID());
    	if(arrSnagM!=null && arrSnagM.length>0){
    		if(selectedSnagIndex!=-1)
    			obj.setSnagId(arrSnagM[selectedSnagIndex].getID());
    	}
    	else
    		obj.setSnagId("");
    	
    	obj.setIsDataSyncToWeb(false);
    	//obj.getDId()+"~"+obj.getParentSnagId()+"~"+obj.getSnagId()+"~"+obj.getJobType()+"~"+obj.getProjectId()+"~"+obj.getBuildingId()+"~"+obj.getFloorId()+"~"+obj.getApartmentID();
    	switch(SelectSnagFrom){
    	case 0:
    		obj.setProjectId("");
    		obj.setBuildingId("");
    		obj.setFloorId("");
    		obj.setApartmentID(NewSnag.getApartmentID());
    		obj.setJobType(SelectedJobType);
    		str=""+NewSnag.getApartment()+"-"+SelectedJobType+"-"+SelectedFaultType;
    		break;
    	case 1:
    		obj.setProjectId("");
    		obj.setBuildingId("");
    		obj.setFloorId(NewSnag.getFloorID());
    		obj.setApartmentID(NewSnag.getApartmentID());
    		obj.setJobType(SelectedJobType);
    		str=""+NewSnag.getFloor()+"-"+NewSnag.getApartment()+"-"+SelectedJobType+"-"+SelectedFaultType;
    		break;
    	case 2:
    		obj.setProjectId("");
    		obj.setBuildingId(NewSnag.getBuildingID());
    		obj.setFloorId(NewSnag.getFloorID());
    		obj.setApartmentID(NewSnag.getApartmentID());
    		obj.setJobType(SelectedJobType);
    		str=""+NewSnag.getBuildingName()+"-"+NewSnag.getFloor()+"-"+NewSnag.getApartment()+"-"+SelectedJobType+"-"+SelectedFaultType;
    		break;
    	case 3:
    		obj.setProjectId(NewSnag.getProjectID());
    		obj.setBuildingId(NewSnag.getBuildingID());
    		obj.setFloorId(NewSnag.getFloorID());
    		obj.setApartmentID(NewSnag.getApartmentID());
    		obj.setJobType(SelectedJobType);
    		str=""+NewSnag.getProjectName()+"-"+NewSnag.getBuildingName()+"-"+NewSnag.getFloor()+"-"+NewSnag.getApartment()+"-"+SelectedJobType+"-"+SelectedFaultType;
    		break;
    	}
    	
    	AddedDepen.add(str);
    	AddedDepen2.add(obj);
    	if(items2.size()>0)
    	{
    		for(int i=0;i<items2.size();i++){
    			items2.remove(0);
    		}
    	}
    	
    	for(int i=0;i<AddedDepen.size();i++){
    		items2.add(new ListItemAddedDependency(null, ""+AddedDepen.get(i),"",false));
    	}
    	
    	list2.setDivider(getResources().getDrawable(R.color.transparent));
	       list2.setDividerHeight(1);
	       
	      adapter2 = new TwoTextArrayAdapter(this, items2);
	     
	    	 list2.setAdapter(adapter2); 
	    	 
	    	 //list2.setOnScrollListener(this);
    }
    public void AddSaveTextClick(View v){
    	registerForContextMenu(v);
    		as_index=1;
    		openContextMenu(v);
    		unregisterForContextMenu(v);
    }
    
}