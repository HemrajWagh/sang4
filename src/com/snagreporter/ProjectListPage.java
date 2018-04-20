package com.snagreporter;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;


import java.net.URL;
import java.net.URLConnection;

import java.util.ArrayList;


import java.util.List;





import org.achartengine.ChartFactory;
import org.achartengine.chart.BarChart.Type;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;
import org.apache.http.util.ByteArrayBuffer;
import org.json.JSONArray;
import org.json.JSONObject;
import org.kobjects.base64.Base64;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.MarshalBase64;
import org.ksoap2.serialization.SoapObject;

import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;



import com.snagreporter.database.FMDBDatabaseAccess;
import com.snagreporter.database.ParseSyncData;
import com.snagreporter.entity.ApartmentMaster;
import com.snagreporter.entity.BuildingMaster;
import com.snagreporter.entity.FaultType;
import com.snagreporter.entity.FloorMaster;
import com.snagreporter.entity.JobType;
import com.snagreporter.entity.ProjectMaster;
import com.snagreporter.entity.SnagMaster;
import com.snagreporter.listitems.Header;
import com.snagreporter.listitems.Item;

import com.snagreporter.menuhandler.MenuHandler;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.Bitmap.CompressFormat;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;

import android.support.v4.view.ViewPager.LayoutParams;
import android.util.DisplayMetrics;
import android.util.Log;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;


import android.view.View;
import android.view.ViewGroup;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import android.widget.AdapterView.OnItemClickListener;
import com.snagreporter.griditems.*;

public class ProjectListPage extends Activity{
	ListView list;
	List<Item> items;
	private static final int projectReqst=1000;
	GridView grid;
	List<GridViewItem> Grid_items;
	GridArrayAdapter adapter2;
	ProjectMaster arrProjects[];
	String CurrentBuilderID="",CurrentBuilderName="";
	ProgressDialog mProgressDialog2;
	boolean isOnline=false;
	boolean EnteredApplication=false;
	String RegUserID="";
	boolean isRefresh=false;
	String LoginType="";
	View TopMenu;
	String[] contrID;
	boolean isMenuVisible=false;
	MenuHandler menuhandler;
	@Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        
      try{
        setContentView(R.layout.main);
        
        //Display display = getWindowManager().getDefaultDisplay();
//       String android_id = Secure.getString(ProjectListPage.this.getContentResolver(),
//                Secure.ANDROID_ID); 
        
		//ParseSyncData obj=new ParseSyncData(ProjectListPage.this);
		//obj.getInspectionListFromWeb();
        
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        //Toast.makeText(getApplicationContext(), ""+display.getHeight()+"X"+display.getWidth()+" density="+dm.densityDpi, Toast.LENGTH_LONG).show();
        
        SharedPreferences SP = getSharedPreferences("AppDelegate",MODE_PRIVATE);
        RegUserID=SP.getString("RegUserID", "");
        LoginType=SP.getString("LoginType", "");
        
        menuhandler=new MenuHandler(ProjectListPage.this);
        TopMenu=new View(this);
        RelativeLayout.LayoutParams rlp=new RelativeLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
        TopMenu.setLayoutParams(rlp);
        LayoutInflater inflater= LayoutInflater.from(this);
        TopMenu=(View) inflater.inflate(R.layout.popup_menu, null);
        this.addContentView(TopMenu, rlp);
        TopMenu.requestLayout();
        TopMenu.setVisibility(View.INVISIBLE);
		
        
        EnteredApplication=getIntent().getExtras().getBoolean("EnteredApplication");
//        if(EnteredApplication){
//        	DownloadCompleteDataFromWeb task=new DownloadCompleteDataFromWeb();
//        	task.execute(10);
//        }
        
        
        mProgressDialog2 = new ProgressDialog(ProjectListPage.this);
        
        CurrentBuilderID=getIntent().getExtras().getString("BuilderID");
        CurrentBuilderName=getIntent().getExtras().getString("BuilderName");
        if(LoginType.equalsIgnoreCase("Contractor"))
        {
        	contrID=(String[])getIntent().getExtras().get("ContractorID");
        	if(contrID!=null){
        		if(contrID.length==1){
        			FMDBDatabaseAccess fmdb=new FMDBDatabaseAccess(ProjectListPage.this);
        			ProjectMaster objProj=fmdb.getProjectByID(contrID[0]);
        			Intent i=new Intent(ProjectListPage.this,com.snagreporter.BuildingListPage.class);
        			i.putExtra("Project",objProj);
        			startActivityForResult(i, 10001);
        		}
        	}
        }
        
        
        
        
        list=(ListView)findViewById(R.id.android_list_projects);
        //int noOfCol=display.getWidth()/dm.densityDpi;
        
        items = new ArrayList<Item>();
        if(LoginType.equals(getResources().getString(R.string.strInspector).toString()))
        	items.add(new Header(null, "PROJECTS LIST",true,true,false,true));
        else
        	items.add(new Header(null, "PROJECTS LIST",true,true,false,false));
        
        grid=(GridView)findViewById(R.id.main_gridview);
        
        //grid.setNumColumns(noOfCol);
        
        Grid_items = new ArrayList<GridViewItem>();
        
        
       list.setDivider(getResources().getDrawable(R.color.transparent));
       list.setDividerHeight(1);
       //list.setSelector(getResources().getDrawable(R.color.transparent));

        TwoTextArrayAdapter adapter = new TwoTextArrayAdapter(this, items);
        list.setAdapter(adapter);
        
        list.setOnItemClickListener(new OnItemClickListener() {

			
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				// TODO Auto-generated method stub
				//Toast.makeText(getApplicationContext(), "position="+position, Toast.LENGTH_SHORT).show();;

				
			}
		});
        
        
      }
      catch(Exception e)
      {
    	  Log.d("Exception",""+e.getMessage());
      }
    }
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater menuInflater = getMenuInflater();
        //if(isOnline)
        	menuInflater.inflate(R.layout.menu, menu);
        //else
        //	menuInflater.inflate(R.layout.menuoff, menu);
        return true;
		//return super.onCreateOptionsMenu(menu);
	}
	 @Override
	    public boolean onOptionsItemSelected(MenuItem item)
	    {
	 
		 switch (item.getItemId())
	        {
	        
	        case R.id.menuBtn_exit:
	        	new AlertDialog.Builder(ProjectListPage.this)
	    	    
	    	    .setMessage("Are you sure you want to Exit?")
	    	    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
	    	        public void onClick(DialogInterface dialog, int which) { 
	    	        	
	    	        	try{
	    	        	setResult(10001);
	    	    		finish();
	    	        	}
	    	            catch(Exception e)
	    	            {
	    	          	  Log.d("Exception",""+e.getMessage());
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
	        	new AlertDialog.Builder(ProjectListPage.this)
	    	    
	    	    .setMessage("Are you sure you want to Logout?")
	    	    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
	    	        public void onClick(DialogInterface dialog, int which) { 
	    	            try{
	    	        	FMDBDatabaseAccess db=new FMDBDatabaseAccess(ProjectListPage.this);
	    	            SharedPreferences SP = getSharedPreferences("AppDelegate",MODE_PRIVATE);
	    				db.performLogout(SP.getString("RegUserID", ""));
	    				Intent i=new Intent(ProjectListPage.this,com.snagreporter.Login_page.class);
	    				i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
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
	public void MenuGraphClick(View v){
		HideTopMenu();
		menuhandler.MenuGraphClick("project");
	}
	
	public void MenuChartClick(View v){
		HideTopMenu();
		menuhandler.MenuChartClick();
	}
	public void MenuAttachClick(View v){
		HideTopMenu();
		menuhandler.MenuAttachClick();
	}
	public void MenuAttendanceClick(View v){
		HideTopMenu();
		
		menuhandler.MenuAttendanceClick();
	}
	//@@@@@@@MenuHandlers
	public void BackClick(View v){
		finish();
	}
	
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
	@Override
	public void onConfigurationChanged(Configuration newConfig){
		super.onConfigurationChanged(newConfig);
	}
	
	public void RefreshClick(View v){
		try{
			if(isOnline){
				isRefresh=true;
			DownloadDataFromWeb task=new DownloadDataFromWeb();
			task.execute(10);
			}
			else{
				new AlertDialog.Builder(ProjectListPage.this)
	    	    
	    	    .setMessage("Please go online to Refresh.")
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
	    	            isRefresh=true;
	    	            DownloadDataFromWeb task=new DownloadDataFromWeb();
	    				task.execute(10);
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
			Log.d("Error=",""+e.getMessage());
		}
	}
	
	@Override
	public void onResume(){
		super.onResume();
//		Grid_items.size()
//		Grid_items.removeAll(Grid_items);
//        grid.setAdapter(null);
//        adapter2.notifyDataSetChanged();
		//if(Grid_items.size()==0){
		try{
		populateProjects();
        
		//}
		
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
	    	  Log.d("Exception",""+e.getMessage());
	      }
	}
	private boolean isNetworkConnected() {
		  ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		  NetworkInfo ni = cm.getActiveNetworkInfo();
		  if (ni == null) {
		   // There are no active networks.
		   return false;
		  } else
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
        	
        	
        		return items.get(position).getView(mInflater, convertView);
        	
        }
    }
	
	
	
    public void populateProjects()
    {
    	try{
    		FMDBDatabaseAccess obj=new FMDBDatabaseAccess(ProjectListPage.this);
    		arrProjects=null;
    		if(LoginType.equalsIgnoreCase("Contractor"))
            {
    			ProjectMaster[] arr=new ProjectMaster[contrID.length];
    			for(int i=0;i<contrID.length;i++)
    			{
    				ProjectMaster prjobj=new ProjectMaster();
    				prjobj=obj.getProjectByID(contrID[i]);
    				arr[i]=prjobj;
    			}
    			arrProjects=arr;
            }
    		else
    		{
    			arrProjects=obj.getProjectsWithSngCnt();//getProjects();
    			if(arrProjects==null || arrProjects.length==0)
        		{
    				if(isOnline)
    				{
    					isRefresh=false;
            			DownloadDataFromWeb task=new DownloadDataFromWeb();
            			task.execute(10);
            			
    				}
    				//getProjectFromWeb();
        			
        		}
    		}
    		//getProjectFromWeb();
    		
    	
    		
    			ContinueProcess();
    	}
    	catch(Exception e){
    	 Log.d("Error=", ""+e.getMessage());
    	}
    }
    
    protected class DownloadDataFromWeb extends AsyncTask<Integer , Integer, Void> {
    	ProgressDialog mProgressDialog = new ProgressDialog(ProjectListPage.this);
    	JSONObject jObject;
        @Override
        protected void onPreExecute() {  
        	try{
        	mProgressDialog.setCancelable(false);
        	mProgressDialog.setMessage("Loading...");
        	if(isRefresh)
        	{
        		mProgressDialog.show();
        	}
            //
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
    			try{
    	    		String METHOD_NAME = "GetDataTableFilter";
    	    		String NAMESPACE = "http://tempuri.org/";
    	    		String URL = ""+getResources().getString(R.string.WS_URL).toString()+"SnagReporter_WebS.asmx";//SnagReporter_WCF.svc
    	    		String SOAP_ACTION = "http://tempuri.org/GetDataTableFilter";//
    	    		
    	    		try {
    	    		    SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
    	    		    
    	    		    SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
    	    		    envelope.dotNet = true;
    	    		    envelope.setOutputSoapObject(request);
    	    		    
    	    		    request.addProperty("_strTableName","ProjectMaster");
    	    		    request.addProperty("_strBuilderID",CurrentBuilderID);
    	    		    //request.addProperty("_strProjectID","");
    	    		    //request.addProperty("_strBuildingID","");
    	    		    //request.addProperty("_strFloorID","");
    	    		   // request.addProperty("_strApartmentID","");
    	        		//
    	    		    
    	    		    HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
    	    		    androidHttpTransport.call(SOAP_ACTION, envelope);
    	    		    Object resonse=envelope.getResponse();
    	    		    String resultData = resonse.toString();
    	    		    
    	    		    
    	    			
    					jObject = new JSONObject(resultData);
    					
    					//mProgressDialog.dismiss();
    	    		    
    	    		    // 0 is the first object of data
    	    		} catch (Exception e) {
    	    		    
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
        	if(isRefresh)
        	{
        		mProgressDialog.dismiss();
        	}
        	//
        	runOnUiThread(new Runnable() {
			     public void run() {

			//stuff that updates ui
			    	 try{
			    		 if(jObject!=null){
			    		 JSONArray arr = jObject.getJSONArray("Data");
						if(arr!=null){
							for(int i=0;i<arr.length();i++){
								JSONObject geometry = arr.getJSONObject(i);
								ParseSyncData parser=new ParseSyncData(ProjectListPage.this);
	    						parser.parseProjectMasterData(geometry);
								
								//parseFriends(geometry);
							}
						}
			    		 }
			    		 
			    		 FMDBDatabaseAccess obj=new FMDBDatabaseAccess(ProjectListPage.this);
			    		 arrProjects=obj.getProjectsWithSngCnt();//getProjects();
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
    	try{
    	for(int i=Grid_items.size()-1;i>=0;i--){
			//int j=items.size();
			Grid_items.remove(i);
		}
    	if(arrProjects!=null && arrProjects.length>0){
    		Typeface font = Typeface.createFromAsset(getAssets(), "PRISTINA.TTF");
    		Typeface font2 = Typeface.createFromAsset(getAssets(), "LHANDW.TTF");
    		
			for(int i=0;i<arrProjects.length;i++){
				String FilePath=Environment.getExternalStorageDirectory()+"/SnagReporter/Pictures/"+arrProjects[i].getImageName();
				Bitmap bp=null;
				try{
				 bp=BitmapFactory.decodeFile(FilePath);
				}
				catch(OutOfMemoryError e)
				{
					Log.d("ErrorMemory","outofmemory");
				}
				Grid_items.add(new GridViewListItemProject(null, ""+arrProjects[i].getProjectName(),0,bp,arrProjects[i].getAbout(),font,i,font2,arrProjects[i].SnagCount,arrProjects[i].isInspStarted));
			}
			TextView t=(TextView)findViewById(R.id.noDataText);
			t.setVisibility(View.INVISIBLE);
		}
		else{
			
			TextView t=(TextView)findViewById(R.id.noDataText);
			t.setVisibility(View.VISIBLE);
		}
    	
    	reloadList();
    	}
        catch(Exception e)
        {
      	  Log.d("Exception",""+e.getMessage());
        }
    }
    public void reloadList()
    {
    	try{
    	adapter2 = new GridArrayAdapter(this, Grid_items);
        grid.setAdapter(adapter2);
        adapter2.notifyDataSetChanged();
        grid.setOnItemClickListener(new OnItemClickListener() {

			
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3)
			{
				// TODO Auto-generated method stub
				//Toast.makeText(getApplicationContext(), "position="+position, Toast.LENGTH_SHORT).show();;
				try{
				if(LoginType.equalsIgnoreCase("Contractor"))
	            {
					Intent i=new Intent(ProjectListPage.this,com.snagreporter.BuildingListPage.class);
					i.putExtra("Project", arrProjects[position]);
					startActivityForResult(i,10001);
	            }
				else
				{
					if(arrProjects!=null && arrProjects.length>0)
					{
						Intent i=new Intent(ProjectListPage.this,com.snagreporter.BuildingListPage.class);
						i.putExtra("Project", arrProjects[position]);
				
						FMDBDatabaseAccess db=new FMDBDatabaseAccess(getApplicationContext());
						SharedPreferences SP = getSharedPreferences("AppDelegate",MODE_PRIVATE);
						db.setCurrentPositionInProject(arrProjects[position].getID(), "Project",SP.getString("RegUserID", ""));
				
						db.setDefaultProject(arrProjects[position].getID(),arrProjects[position].getProjectName(),SP.getString("RegUserID", ""));
						startActivityForResult(i,10001);
					}
				}
				}
			      catch(Exception e)
			      {
			    	  Log.d("Exception",""+e.getMessage());
			      }
			}
		});
        
        FMDBDatabaseAccess obj=new FMDBDatabaseAccess(getApplicationContext());
		JobType[] arrJobType=obj.getJobType();
		if(arrJobType==null || arrJobType.length==0){
			getJobTypeFromWeb();
		}
		
		FaultType[] arr2=obj.getAllFaultType();
		if(arr2!=null && arr2.length==0){
			getFaultTypeFromWeb();
		}
    	}
        catch(Exception e)
        {
      	  Log.d("Exception",""+e.getMessage());
        }
    }
    public void  getProjectFromWeb(){
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
    		    envelope.bodyOut=request;
    		    envelope.dotNet = true;
    		    envelope.setOutputSoapObject(request);
    		    
    		    request.addProperty("_strTableName","ProjectMaster");
    		    //request.addProperty("ProjectID","");
    		    //request.addProperty("BuildingID","");
    		    //request.addProperty("FloorID","");
    		   // request.addProperty("ApartmentID","");
        		
    		    
    		    HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
    		    androidHttpTransport.call(SOAP_ACTION, envelope);
    		    Object resonse=envelope.getResponse();
    		    String resultData = resonse.toString();
    		    //String str=resonse.toString();
    		    //SoapPrimitive result = (SoapPrimitive) envelope.getResponse();
    		    // to get the data
    		    
    		    
    		    JSONObject jObject;
    			
    				jObject = new JSONObject(resultData);
    				JSONArray arr = jObject.getJSONArray("Data");
    				if(arr!=null){
    					for(int i=0;i<arr.length();i++){
    						JSONObject geometry = arr.getJSONObject(i);
    						ParseSyncData parser=new ParseSyncData(ProjectListPage.this);
    						parser.parseProjectMasterData(geometry);
    						//parseData(geometry);
    						
    						//parseFriends(geometry);
    					}
    				}
    		    res = resultData;
    		    
    		    
    		    
    		    
    		    mProgressDialog.dismiss();
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
//    public void parseData(JSONObject obj){
//    	try{
//    		String ID=obj.getString("ID");
//    		String ProjectName=obj.getString("ProjectName");
//    		
//    		int NoOfBuildings=obj.getInt("NoOfBuildings");
//    		String Location=obj.getString("Location");
//    		String Address1=obj.getString("Address1");
//    		String Address2=obj.getString("Address2");
//    		String Pincode=obj.getString("Pincode");
//    		String City=obj.getString("City");
//    		String State=obj.getString("State");
//    		String BuilderID=obj.getString("BuilderID");
//    		String BuilderName=obj.getString("BuilderName");
//    		String ImageName=obj.getString("ImageName");
//    		String About="";
//    		try{
//    			About=obj.getString("About");
//    		}
//    		catch(Exception e){
//    			About="";
//    		}
//    		 
//    		String str=About.replace("'", " ");
//    		About=str;
//    		
//    		ProjectMaster prj=new ProjectMaster();
//    		prj.setID(ID);
//    		prj.setProjectName(ProjectName);
//    		prj.setNoOfBuildings(NoOfBuildings);
//    		prj.setLocation(Location);
//    		prj.setAddress1(Address1);
//    		prj.setAddress2(Address2);
//    		prj.setPincode(Pincode);
//    		prj.setCity(City);
//    		prj.setState(State);
//    		prj.setBuilderID(BuilderID);
//    		prj.setBuilderName(BuilderName);
//    		prj.setImageName(ImageName);
//    		prj.setAbout(About);
//    		if(ImageName!=null && ImageName.length()!=0){
//    			downloadImage(ImageName);
//    		}
//    		FMDBDatabaseAccess db=new FMDBDatabaseAccess(ProjectListPage.this);
//    		db.insertORUpdateProject(prj);
//    	}
//    	catch(Exception e){
//    		Log.d("Error=", ""+e.getMessage());
//    	}
//    }
    
    public void  getJobTypeFromWeb(){
    	//ProgressDialog mProgressDialog;
    	//mProgressDialog = new ProgressDialog(this);
    	try{
    		
    		
        	//mProgressDialog.setMessage("Loading...");
           // mProgressDialog.show(); 
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
    		    envelope.bodyOut=request;
    		    envelope.dotNet = true;
    		    envelope.setOutputSoapObject(request);
    		    
    		    request.addProperty("_strTableName","JobType");
    		    //request.addProperty("ProjectID","");
    		    //request.addProperty("BuildingID","");
    		    //request.addProperty("FloorID","");
    		   // request.addProperty("ApartmentID","");
        		
    		    
    		    HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
    		    androidHttpTransport.call(SOAP_ACTION, envelope);
    		    Object resonse=envelope.getResponse();
    		    String resultData = resonse.toString();
    		    //String str=resonse.toString();
    		    //SoapPrimitive result = (SoapPrimitive) envelope.getResponse();
    		    // to get the data
    		    
    		    
    		    JSONObject jObject;
    			
    				jObject = new JSONObject(resultData);
    				JSONArray arr = jObject.getJSONArray("Data");
    				if(arr!=null){
    					for(int i=0;i<arr.length();i++){
    						JSONObject geometry = arr.getJSONObject(i);
    						parseData2(geometry);
    						
    						//parseFriends(geometry);
    					}
    				}
    		    res = resultData;
    		    //mProgressDialog.dismiss();
    		    // 0 is the first object of data
    		} catch (Exception e) {
    		    res = e.getMessage();
    		    Log.d("Error=", ""+e.getMessage());
    		   // mProgressDialog.dismiss();
    		}
    	}
    	catch(Exception e){
    		Log.d("Error=", ""+e.getMessage());
    		 //mProgressDialog.dismiss();
    	}
    }
    public void parseData2(JSONObject obj){
    	try{
    		String ID=obj.getString("ID");
    		String JobType=obj.getString("JobType");
    		
    		String JobDetails=obj.getString("JobDetails");
    		
    		JobType prj=new JobType();
    		prj.setID(ID);
    		prj.setJobType(JobType);
    		prj.setJobDetails(JobDetails);
    		prj.setIsSyncedToWeb(true);
    		FMDBDatabaseAccess db=new FMDBDatabaseAccess(getApplicationContext());
    		db.insertORUpdateJobType(prj);
    	}
    	catch(Exception e){
    		Log.d("Error=", ""+e.getMessage());
    	}
    }
    
    public void  getFaultTypeFromWeb(){
    	//ProgressDialog mProgressDialog;
    	//mProgressDialog = new ProgressDialog(this);
    	try{
    		
    		
        	//mProgressDialog.setMessage("Loading...");
           // mProgressDialog.show(); 
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
    		    envelope.bodyOut=request;
    		    envelope.dotNet = true;
    		    envelope.setOutputSoapObject(request);
    		    
    		    request.addProperty("_strTableName","FaultType");
    		    //request.addProperty("ProjectID","");
    		    //request.addProperty("BuildingID","");
    		    //request.addProperty("FloorID","");
    		   // request.addProperty("ApartmentID","");
        		
    		    
    		    HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
    		    androidHttpTransport.call(SOAP_ACTION, envelope);
    		    Object resonse=envelope.getResponse();
    		    String resultData = resonse.toString();
    		    //String str=resonse.toString();
    		    //SoapPrimitive result = (SoapPrimitive) envelope.getResponse();
    		    // to get the data
    		    
    		    
    		    JSONObject jObject;
    			
    				jObject = new JSONObject(resultData);
    				JSONArray arr = jObject.getJSONArray("Data");
    				if(arr!=null){
    					for(int i=0;i<arr.length();i++){
    						JSONObject geometry = arr.getJSONObject(i);
    						parseData3(geometry);
    						
    						//parseFriends(geometry);
    					}
    				}
    		    res = resultData;
    		    //mProgressDialog.dismiss();
    		    // 0 is the first object of data
    		} catch (Exception e) {
    		    res = e.getMessage();
    		    Log.d("Error=", ""+e.getMessage());
    		  // mProgressDialog.dismiss();
    		}
    	}
    	catch(Exception e){
    		Log.d("Error=", ""+e.getMessage());
    		// mProgressDialog.dismiss();
    	}
    }
    public void parseData3(JSONObject obj){
    	try{
    		String ID=obj.getString("ID");
    		String FaultType=obj.getString("FaultType");
    		
    		String FaultDetails=obj.getString("FaultDetails");
    		String JobTypeID=obj.getString("JobTypeID");
    		String JobType=obj.getString("JobType");
    		
    		FaultType prj=new FaultType();
    		prj.setID(ID);
    		prj.setJobType(JobType);
    		prj.setJobTypeID(JobTypeID);
    		prj.setFaultType(FaultType);
    		prj.setFaultDetails(FaultDetails);
    		prj.setIsSyncedToWeb(true);
    		FMDBDatabaseAccess db=new FMDBDatabaseAccess(getApplicationContext());
    		db.insertORUpdateFaultType(prj);
    	}
    	catch(Exception e){
    		Log.d("Error=", ""+e.getMessage());
    	}
    }
    
    public void downloadImage(String imageUrl){
    	try{
    		/*URL url1 = new URL("http://snag.itakeon.com/uploadimage/"+url);
  		  HttpURLConnection urlConnection = (HttpURLConnection) url1.openConnection();
  		  urlConnection.setRequestMethod("GET");
  		  urlConnection.setDoOutput(true);                   
  		  urlConnection.connect();                  
  		  //File SDCardRoot = Environment.getExternalStorageDirectory().getAbsoluteFile();
  		  //String extStorageDirectory = Environment.getExternalStorageDirectory().toString();
  		  String filename=Environment.getExternalStorageDirectory()+"/SnagReporter/Pictures/"+url;  
  		 // Log.i("Local filename:",""+filename);
  		  File file = new File(filename);
  		  if(file.createNewFile())
  		  {
  		    file.createNewFile();
  		  }                 
  		  FileOutputStream fileOutput = new FileOutputStream(file);
  		  InputStream inputStream = null;
  		//inputStream.reset();
  		inputStream = urlConnection.getInputStream();
  		  int totalSize = urlConnection.getContentLength();
  		  int downloadedSize = 0;   
  		  byte[] buffer = new byte[1024];
  		  int bufferLength = 0;
  		  while ( (bufferLength = inputStream.read(buffer)) > 0 ) 
  		  {                 
  		    fileOutput.write(buffer, 0, bufferLength);                  
  		    downloadedSize += bufferLength;                 
  		    Log.i("Progress:","downloadedSize:"+downloadedSize+"totalSize:"+ totalSize) ;
  		  }             
  		  fileOutput.close();*/
    		String path=Environment.getExternalStorageDirectory()+"/SnagReporter/Pictures/"+imageUrl;
    		File filechk=new File(path);
    		
    		if(!(filechk.exists()))
    		{
    			String downloadUrl="http://snag.itakeon.com/uploadimage/"+imageUrl;
    			String str=downloadUrl.replace(" ", "%20");
    			downloadUrl=str;
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
    	catch(Exception e){
    		Log.d("Error", ""+e.getMessage());
    	}
    	
    }
    //public void btnAddNewClick(View v)
    //{
    //	Intent i=new Intent(ProjectListPage.this,com.snagreporter.AddNewProject.class);
    //	startActivity(i);
    	
   // }
    public void EditItemClick(View v)
    {
    	try{
    	Intent i=new Intent(ProjectListPage.this,com.snagreporter.AddNewProject.class);
    	i.putExtra("BuilderName", CurrentBuilderName);
    	i.putExtra("BuilderID", CurrentBuilderID);
    	startActivity(i);
    	}
        catch(Exception e)
        {
      	  Log.d("Exception",""+e.getMessage());
        }
    }
    
    
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) 
    {
    	 super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
    	 
    	 try{
    	 if(requestCode==101 && resultCode==101 && imageReturnedIntent!=null){
    		String FilePath=imageReturnedIntent.getExtras().getString("FilePath");
    		String FileName=imageReturnedIntent.getExtras().getString("FileName");
    		menuhandler.SaveAttachment("", "", "", "", "", "", "", "", FileName, FilePath);
    		//Toast.makeText(ProjectListPage.this, "File123="+FilePath, Toast.LENGTH_LONG).show();
    	 }
    	 
    	if(resultCode==10001){
    		setResult(10001);
    		finish();
    	}
    	else if(resultCode==10002){
    		FMDBDatabaseAccess db=new FMDBDatabaseAccess(ProjectListPage.this);
            SharedPreferences SP = getSharedPreferences("AppDelegate",MODE_PRIVATE);
			db.performLogout(SP.getString("RegUserID", ""));
			Intent i=new Intent(ProjectListPage.this,com.snagreporter.Login_page.class);
			i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(i);
			
			finish();
    	}
    	 }
         catch(Exception e)
         {
       	  Log.d("Exception",""+e.getMessage());
         }
    }
    
   
    private String[] mProjects;
//    = new String[] {
//			"Jan", "Feb" , "Mar", "Apr", "May", "Jun",
//			"Jul", "Aug" , "Sep", "Oct", "Nov", "Dec"
//		};
    
    private void openChart(){
    	try{
    	SharedPreferences SP = getSharedPreferences("AppDelegate",MODE_PRIVATE);
    	String id=SP.getString("RegUserID", "");
    	
    	
    	mProjects=new String[arrProjects.length];
    	for(int i=0;i<arrProjects.length;i++){
    		mProjects[i]=arrProjects[i].getProjectName();
    	}
    	
    	int[] x=new int[arrProjects.length];
    	for(int i=0;i<arrProjects.length;i++){
    		x[i]=i;
    	}
    	FMDBDatabaseAccess db=new FMDBDatabaseAccess(ProjectListPage.this);
    	
    	int[] resolved = new int[arrProjects.length];//{ 2000,2500,2700,3000,2800,3500,3700,3800};
    	for(int i=0;i<arrProjects.length;i++){
    		resolved[i]=db.getResolvedSnagsByProject(arrProjects[i].getID(), id);
    	}
    	
    	int[] expense = new int[arrProjects.length];//{2200, 2700, 2900, 2800, 2600, 3000, 3300, 3400 };
    			for(int i=0;i<arrProjects.length;i++){
    				expense[i]=db.getUnresolvedSnagsByProject(arrProjects[i].getID(), id);
    	}
    	
    	
    	
    	// Creating an  XYSeries for Income
    	//CategorySeries incomeSeries = new CategorySeries("Income");
    	XYSeries incomeSeries = new XYSeries("Resolved");
    	// Creating an  XYSeries for Income
    	XYSeries expenseSeries = new XYSeries("UnResolved");
    	// Adding data to Income and Expense Series
    	for(int i=0;i<x.length;i++){    		
    		incomeSeries.add(i,resolved[i]);
    		expenseSeries.add(i,expense[i]);
    	}
    	
    	
    	// Creating a dataset to hold each series
    	XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
    	// Adding Income Series to the dataset
    	dataset.addSeries(incomeSeries);
    	// Adding Expense Series to dataset
    	dataset.addSeries(expenseSeries);    	
    	
    	
    	// Creating XYSeriesRenderer to customize incomeSeries
    	XYSeriesRenderer incomeRenderer = new XYSeriesRenderer();
    	incomeRenderer.setColor(Color.rgb(130, 130, 230));
    	incomeRenderer.setFillPoints(true);
    	incomeRenderer.setLineWidth(2);
    	incomeRenderer.setDisplayChartValues(true);
    	
    	// Creating XYSeriesRenderer to customize expenseSeries
    	XYSeriesRenderer expenseRenderer = new XYSeriesRenderer();
    	expenseRenderer.setColor(Color.rgb(220, 80, 80));
    	expenseRenderer.setFillPoints(true);
    	expenseRenderer.setLineWidth(2);
    	expenseRenderer.setDisplayChartValues(true);   
    	
    	
    	
    	// Creating a XYMultipleSeriesRenderer to customize the whole chart
    	XYMultipleSeriesRenderer multiRenderer = new XYMultipleSeriesRenderer();
    	multiRenderer.setXLabels(0);
    	multiRenderer.setChartTitle("Resolved vs UnResolved Chart");
    	multiRenderer.setXTitle("Projects");
    	multiRenderer.setYTitle("Number of Snags");
    	multiRenderer.setZoomButtonsVisible(true);
    	
    	for(int i=0; i< x.length;i++){
    		multiRenderer.addXTextLabel(i, mProjects[i]);    		
    	}    	

    	
    	//multiRenderer.setYAxisMin(0);
    	//multiRenderer.setYAxisMax(max);
    	
    	//multiRenderer.setZoomRate(0.5f);
    	
    	// Adding incomeRenderer and expenseRenderer to multipleRenderer
    	// Note: The order of adding dataseries to dataset and renderers to multipleRenderer
    	// should be same
    	multiRenderer.addSeriesRenderer(incomeRenderer);
    	multiRenderer.addSeriesRenderer(expenseRenderer);
    	
    	
    	
    	// Creating an intent to plot bar chart using dataset and multipleRenderer    	
    	Intent intent = ChartFactory.getBarChartIntent(getBaseContext(), dataset, multiRenderer, Type.DEFAULT);
    	intent.setSourceBounds(new Rect(50, 100, 100, 100));
    	
    	// Start Activity
    	startActivity(intent);
    	
    	}
        catch(Exception e)
        {
      	  Log.d("Exception",""+e.getMessage());
        }
    	
    	
    }
    public void SyncData(){
    	try{
    	//SyncAllDataToWeb task=new SyncAllDataToWeb();
    	//task.execute(10);
    	//StartSyncProgress task=new StartSyncProgress();
    	//task.execute(10);
    	//SyncAllData();
    	ParseSyncData parser=new ParseSyncData(ProjectListPage.this);
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
        	FMDBDatabaseAccess db=new FMDBDatabaseAccess(ProjectListPage.this);
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
          	  Log.d("Exception",""+e.getMessage());
            }
        }      
        @Override
        protected Void doInBackground(Integer... params) {
        	try{
        	
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
        	}
            catch(Exception e)
            {
          	  Log.d("Exception",""+e.getMessage());
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void result1) {
        	try{
        	FMDBDatabaseAccess db=new FMDBDatabaseAccess(ProjectListPage.this);
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
        		mProgressDialog2.setMessage("Synchronization Complete...");
        		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
        		mProgressDialog2.dismiss();
//        		StartDownloadSnags task=new StartDownloadSnags();
//        		task.execute(10);
        	}
        	}
            catch(Exception e)
            {
          	  Log.d("Exception",""+e.getMessage());
            }
        }
         
}

protected class StartDownloadSnags extends AsyncTask<Integer , Integer, Void> {
	
	ParseSyncData parser;
	boolean result=false;
	String resultData="";
        @Override
        protected void onPreExecute() {  
        	
        	try{
        	if(!mProgressDialog2.isShowing()){
        		mProgressDialog2.setCancelable(false);
        		mProgressDialog2.setMessage("Synchronizing...");
        		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
        		//mProgressDialog2.show();
        	}
        	
        	parser=new ParseSyncData(ProjectListPage.this);
        	}
            catch(Exception e)
            {
          	  Log.d("Exception",""+e.getMessage());
            }
        }      
        @Override
        protected Void doInBackground(Integer... params) {
//        	String METHOD_NAME = "SyncTableData";
//    		String NAMESPACE = "http://tempuri.org/";
//    		String URL = ""+getResources().getString(R.string.WS_URLLocal).toString()+"SnagReporter_WebS.asmx";//SnagReporter_WCF.svc
//    		String SOAP_ACTION = "http://tempuri.org/SyncTableData";
    		
    		try {
//    		    SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
//    		    
//    		    SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
//    		    envelope.bodyOut=request;
//    		    envelope.dotNet = true;
//    		    envelope.setOutputSoapObject(request);
//    		    
//    		    request.addProperty("_strTableName","ProjectMaster");
//    		    request.addProperty("_strModifiedDate","4/13/2013 1:16:37 AM");
//    		    request.addProperty("_strModifiedBy",RegUserID);
//    		    request.addProperty("_strNoRows",10);
//    		    
//    		    HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
//    		    androidHttpTransport.call(SOAP_ACTION, envelope);
//    		    Object resonse=envelope.getResponse();
//    		    resultData = resonse.toString();
    			
    			//parser.getAllDataFromWebAfterDate(RegUserID, "ProjectMaster", "");
    			parser.start();
    		    
    		}
    		catch(Exception e){
    			Log.d("Error=", ""+e.getMessage());
    		}
            return null;
    		
        }
        @Override
        protected void onPostExecute(Void result1) {
        	try{
//        	JSONObject jObject;
//			
//			jObject = new JSONObject(resultData);
//			JSONArray arr = jObject.getJSONArray("Data");
//			if(arr!=null){
//				for(int i=0;i<arr.length();i++){
//					JSONObject geometry = arr.getJSONObject(i);
//					ParseSyncData parser=new ParseSyncData(ProjectListPage.this);
//					//parser.parseSnagData(geometry);
//					//parseSnagData(geometry);
//					
//				}
//			}
        	}
        	catch(Exception e){
        		
        	}
    		mProgressDialog2.setMessage("Synchronization Complete...");
    		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
    		mProgressDialog2.dismiss();
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
      	  Log.d("Exception",""+e.getMessage());
        }
    }
    public byte[] getBytesFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(CompressFormat.JPEG, 70, stream);
        return stream.toByteArray();
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
    				new AlertDialog.Builder(ProjectListPage.this)
    	    	    
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
    			openChart2();
    			
    			
    		}
    		catch(Exception e){
    			Log.d("Error=", ""+e.getMessage());
    		}
    	}
    	public void ShowChartClick(View v){
    		try{	
    			Intent i=new Intent(ProjectListPage.this,com.snagreporter.GraphWebView.class);
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
    	public void openChart2(){
    		try{
    			Intent i=new Intent(ProjectListPage.this,com.snagreporter.BarChartActivity.class);
    			i.putExtra("project", true);
    			
    			//i.putExtra("building", true);
    			//i.putExtra("currentprojectID", CurrentProject.getID());
    			
    			//i.putExtra("floor", true);
    			//i.putExtra("currentBuilding", CurrentBuilding.getID());
    			
    			//i.putExtra("apartment", true);
    			//i.putExtra("currentfloor", CurrentFloor.getID());
            	startActivity(i);
    		}
    		catch(Exception e){
    			
    		}
    	}
    	
    	protected class DownloadCompleteDataFromWeb extends AsyncTask<Integer , Integer, Void> {
        	ProgressDialog mProgressDialogN = new ProgressDialog(ProjectListPage.this);
        	JSONObject jObject;
        	boolean isProjectDone=false;
        	boolean isBuildingDone=false;
        	boolean isFloorDone=false;
        	boolean isAptDone=false;
        	boolean isAptDtlsDone=false;
        	boolean isSnagDone=false;
        	boolean isAllDone=false;
            @Override
            protected void onPreExecute() { 
            	try{
            	mProgressDialogN.setCancelable(false);
            	mProgressDialogN.setMessage("Loading...");
            	mProgressDialogN.show();
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
            		
        		}
        		catch (Exception e){
                    Log.d("Error DownloadCompleteDataFromWeb=", ""+e.getMessage()); 
        		}
                return null;
            }
            @Override
            protected void onPostExecute(Void result) {
            	try{
            	mProgressDialogN.dismiss();
            	}
                catch(Exception e)
                {
              	  Log.d("Exception",""+e.getMessage());
                }
            }
    	}
    	public boolean onKeyDown(int keyCode, KeyEvent event)
    	{
    		try{
            if (keyCode == KeyEvent.KEYCODE_BACK)
            {
            	
            	if(isMenuVisible)
            	{
            		HideTopMenu();
            		return false;
            	}
            	else
            	{
            		setResult(10001);
    	    		finish();
            	}
            	
            	//Intent i=new Intent();
            	//setResult(RESULT_OK,i);
            	//setResult(projectReqst, i);
            	// finish();
            	//Toast.makeText(ProjectListPage.this,"BackButtonPressed",Toast.LENGTH_SHORT).show();
            }
    		}
    	      catch(Exception e)
    	      {
    	    	  Log.d("Exception",""+e.getMessage());
    	      }
            return super.onKeyDown(keyCode,event);  
        }
}
