package com.snagreporter;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import android.support.v4.view.ViewPager.LayoutParams;

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
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;


import com.snagreporter.R;
import com.snagreporter.listitems.Header;
import com.snagreporter.listitems.HeaderWithAdd;
import com.snagreporter.listitems.Item;
import com.snagreporter.listitems.ListItem;
import com.snagreporter.listitems.ListItemBlank;
import com.snagreporter.listitems.ListItemParent;
import com.snagreporter.menuhandler.MenuHandler;


import android.R.array;
import android.app.Activity;
import android.app.AlertDialog;
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
import android.graphics.Color;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.ContextMenu;
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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.RemoteViews.ActionException;
import android.widget.TextView;


import com.snagreporter.FloorListPage.StartSyncProgress;
import com.snagreporter.ProjectListPage.DownloadDataFromWeb;
import com.snagreporter.ProjectListPage.StartDownloadSnags;
import com.snagreporter.database.*;
import com.snagreporter.entity.ActiveFields;
import com.snagreporter.entity.ActiveFields2;
import com.snagreporter.entity.ApartmentMaster;
import com.snagreporter.entity.BuildingMaster;
import com.snagreporter.entity.FaultType;
import com.snagreporter.entity.FloorMaster;
import com.snagreporter.entity.JobType;
import com.snagreporter.entity.ProjectMaster;
import com.snagreporter.entity.SnagMaster;
import com.snagreporter.entity.StdApartmentAreas;
import com.snagreporter.entity.StdFloorAreas;
import com.snagreporter.entity.TradeAptAreaMaster;
import com.snagreporter.griditems.GridArrayAdapter;
import com.snagreporter.griditems.GridViewItem;
import com.snagreporter.griditems.GridViewListItem;
public class ApartmentListPage extends Activity
{
	ListView list;
	ListView list_parent;
	
	List<Item> items;
	List<Item> itemsParent;
	
	GridView grid;
	List<GridViewItem> Grid_items;
	String ActiveSnag,ActiveTrade,ActiveApt,perComplete;
	BuildingMaster CurrentBuilding;
	FloorMaster CurrentFloor;
	ProjectMaster CurrentProject;
	String CurrentProjectID;
	ApartmentMaster arrAptmt[];
	StdFloorAreas arrSFA[];
	boolean CameFromStartPage=false;
	boolean isOnline=false;
	ProgressDialog mProgressDialog2;
	String LoginType="";
	String RegUserID="";
	View TopMenu;
	boolean isRefresh=false;
	boolean isMenuVisible=false;
	MenuHandler menuhandler;
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        try{
        setContentView(R.layout.apartment_list_page);
        
        SharedPreferences SP = getSharedPreferences("AppDelegate",MODE_PRIVATE);
        RegUserID=SP.getString("RegUserID", "");
        LoginType=SP.getString("LoginType", "");
        
        menuhandler=new MenuHandler(ApartmentListPage.this);
        TopMenu=new View(this);
        RelativeLayout.LayoutParams rlp=new RelativeLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
        TopMenu.setLayoutParams(rlp);
        LayoutInflater inflater= LayoutInflater.from(this);
        TopMenu=(View) inflater.inflate(R.layout.popup_menu, null);
        this.addContentView(TopMenu, rlp);
        TopMenu.requestLayout();
        TopMenu.setVisibility(View.INVISIBLE);
        
        mProgressDialog2 = new ProgressDialog(ApartmentListPage.this);
        CurrentProject=(ProjectMaster) getIntent().getExtras().get("Project");
        CurrentProjectID=CurrentProject.getID();
        CurrentBuilding=(BuildingMaster)getIntent().getExtras().get("Building");
        CurrentFloor=(FloorMaster)getIntent().getExtras().get("Floor");
        
        CameFromStartPage=getIntent().getExtras().getBoolean("CameFromStartPage");
        
        list=(ListView)findViewById(R.id.android_list_apt_apt);
        list_parent=(ListView)findViewById(R.id.android_list_apt_floor);
        
        
        
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
		menuhandler.MenuGraphClick("apartment",CurrentFloor);
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
		menuhandler.MenuAttachClick();
	}
	
	//@@@@@@@MenuHandlers
    
    @Override
	public void onConfigurationChanged(Configuration newConfig){
		super.onConfigurationChanged(newConfig);
	}
    @Override
	public void onResume(){
    	super.onResume();
    	try{
    	if(itemsParent!=null && itemsParent.size()>0)
    		itemsParent.clear();
    	itemsParent = new ArrayList<Item>();
        
    	populateCurrentFloor();
        
        
        list_parent.setDivider(getResources().getDrawable(R.color.transparent));
        list_parent.setDividerHeight(1);
       //list.setSelector(getResources().getDrawable(R.color.transparent));

       TwoTextArrayAdapter adapterParent = new TwoTextArrayAdapter(this, itemsParent);
       list_parent.setAdapter(adapterParent);
       
       list_parent.setOnItemClickListener(new OnItemClickListener() {

			
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				// TODO Auto-generated method stub
				
					//Toast.makeText(getApplicationContext(), "list_project position="+position, Toast.LENGTH_LONG).show();;
			}
		});
       
       items = new ArrayList<Item>();
       //items.add(new Header(null, "APARTMENTS/AREA LIST",false));
       if(LoginType.equals(getResources().getString(R.string.strInspector).toString()))
    	   items.add(new HeaderWithAdd(null, "APARTMENTS/AREA LIST","Add New Apartment",true));
       else
    	   items.add(new HeaderWithAdd(null, "APARTMENTS/AREA LIST","Add New Apartment",false));
       
       grid=(GridView)findViewById(R.id.aptmt_gridview);
       Grid_items = new ArrayList<GridViewItem>();
       
       
       list.setDivider(getResources().getDrawable(R.color.transparent));
       list.setDividerHeight(1);
       
        TwoTextArrayAdapter adapter = new TwoTextArrayAdapter(this, items);
        list.setAdapter(adapter);
        
        list.setOnItemClickListener(new OnItemClickListener() {

			
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				
			}
		});
    	
    	
    	
    	if(Grid_items.size()==0){
    	populateAptmts();
        
    	}
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
    
    public void RefreshClick(View v){
		try{
			if(isOnline){
				isRefresh=true;
			DownloadDataFromWeb task=new DownloadDataFromWeb();
			task.execute(10);
			}
			else{
				new AlertDialog.Builder(ApartmentListPage.this)
	    	    
	    	    .setMessage("Please go online to Refresh.")
	    	    .setPositiveButton("GoOnline", new DialogInterface.OnClickListener() {
	    	        public void onClick(DialogInterface dialog, int which) { 
	    	        	try{
	    	        	Button b=(Button)findViewById(R.id.topbar_statusbtn);
	    	    		ImageView i=(ImageView)findViewById(R.id.topbar_statusimage);
	    	    		b.setText("GoOffline");
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
    public void BackClick(View v){
    	try{
   		 SharedPreferences SP = getSharedPreferences("AppDelegate",MODE_PRIVATE);
				FMDBDatabaseAccess db=new FMDBDatabaseAccess(getApplicationContext());
				String str=CurrentBuilding.getProjectID()+"~"+CurrentBuilding.getID();
				db.setCurrentPositionInProject(str, "Building",SP.getString("RegUserID", ""));
	    		if(CameFromStartPage){
	    			Intent i=new Intent(ApartmentListPage.this,com.snagreporter.FloorListPage.class);
	    			i.putExtra("Project", CurrentProject);
	    			i.putExtra("Building", CurrentBuilding);
	    			i.putExtra("CameFromStartPage", CameFromStartPage);
	    			
	    			
	    			
	    			startActivity(i);
	    			
	    		}
	    		finish();
	    	}
	    	catch(Exception e){
	    		 Log.d("Error=", ""+e.getMessage());
	    	}
    }
    @Override
    public boolean onKeyDown(int keycode, KeyEvent event ) {
    	
    	try{
     if(keycode == KeyEvent.KEYCODE_BACK){
    	 try{
    		 
    		 
    		 if(isMenuVisible)
         	{
         		HideTopMenu();
         		return false;
         	}
         	else
         	{
    		 SharedPreferences SP = getSharedPreferences("AppDelegate",MODE_PRIVATE);
				FMDBDatabaseAccess db=new FMDBDatabaseAccess(getApplicationContext());
				String str=CurrentBuilding.getProjectID()+"~"+CurrentBuilding.getID();
				db.setCurrentPositionInProject(str, "Building",SP.getString("RegUserID", ""));
	    		if(CameFromStartPage){
	    			Intent i=new Intent(ApartmentListPage.this,com.snagreporter.FloorListPage.class);
	    			i.putExtra("Project", CurrentProject);
	    			i.putExtra("Building", CurrentBuilding);
	    			i.putExtra("CameFromStartPage", CameFromStartPage);
	    			
	    			
	    			
	    			startActivity(i);
	    			finish();
	    		}
         	}
	    	}
	    	catch(Exception e){
	    		 Log.d("Error=", ""+e.getMessage());
	    	}
     }
    	}
        catch(Exception e)
        {
        	Log.d("Exception",""+e.getMessage());
        }
     return super.onKeyDown(keycode,event);  
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
    
    
    public void populateAptmts()
    {
    	try{
    		FMDBDatabaseAccess obj=new FMDBDatabaseAccess(getApplicationContext());
    		arrAptmt=null;
    		arrAptmt=obj.getApartmentsWithSnag(CurrentFloor);//getApartments(CurrentFloor);
    		
    		if(arrAptmt==null || arrAptmt.length==0){
    			
    			if(isOnline)
    			{
    				isRefresh=false;
        			DownloadDataFromWeb task=new DownloadDataFromWeb();
        			task.execute(10);
        			
    			}
    			//getApartmentsFromWeb();
    			
    		}
    		else{
    			ContinueProcess();
    		}
    		
    		
    		obj=null;
    		
            
    	}
    	catch(Exception e){
    	 Log.d("Error=", ""+e.getMessage());
    	}
    }
    
    protected class DownloadDataFromWeb extends AsyncTask<Integer , Integer, Void> {
    	ProgressDialog mProgressDialog = new ProgressDialog(ApartmentListPage.this);
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
    	    		String res = "";
    	    		try {
    	    		    SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
    	    		    
    	    		    SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
    	    		    envelope.dotNet = true;
    	    		    envelope.setOutputSoapObject(request);
    	    		    
    	    		    request.addProperty("_strTableName","ApartmentMaster");
    	    		    request.addProperty("_strBuilderID","");
    	    		    request.addProperty("_strProjectID",CurrentProject.getID());
    	    		    request.addProperty("_strBuildingID",CurrentBuilding.getID());
    	    		    request.addProperty("_strFloorID",CurrentFloor.getID());
    	    		    request.addProperty("_strApartmentID","");
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
								parseData(geometry);
								
								//parseFriends(geometry);
							}
						}
			    		 }
			    		 FMDBDatabaseAccess obj=new FMDBDatabaseAccess(ApartmentListPage.this);
			    		 arrAptmt=obj.getApartmentsWithSnag(CurrentFloor);//getApartments(CurrentFloor);
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
    	
    	if(arrAptmt!=null && arrAptmt.length>0 ){
			for(int i=0;i<arrAptmt.length;i++){
				//items.add(new ListItem(null, ""+arrAptmt[i].getApartmentNo()));
				String FilePath=Environment.getExternalStorageDirectory()+"/SnagReporter/Pictures/"+arrAptmt[i].getImageName();
				Bitmap bp=BitmapFactory.decodeFile(FilePath);
				Grid_items.add(new GridViewListItem(null, ""+arrAptmt[i].getApartmentNo(),3,bp,arrAptmt[i].SnagCount,arrAptmt[i].isInspStarted));
			}
		}
		
		
//		arrSFA=obj.getSFA();
//		if(arrSFA!=null && arrSFA.length>0){
//			for(int i=0;i<arrSFA.length;i++){
//				//items.add(new ListItem(null, ""+arrSFA[i].getAreaName()));
//				Grid_items.add(new GridViewListItem(null, ""+arrSFA[i].getAreaName(),4));
//			}
//		}
		
		if( arrAptmt==null || arrAptmt.length==0){//arrSFA==null &&
			//items.add(new ListItemBlank(null, "No items found"));
			TextView t=(TextView)findViewById(R.id.noDataText);
			t.setVisibility(View.VISIBLE);
		}
		else{
			TextView t=(TextView)findViewById(R.id.noDataText);
			t.setVisibility(View.INVISIBLE);
		}
		
		reloadList();
    	}
        catch(Exception e)
        {
        	Log.d("Exception",""+e.getMessage());
        }
    }
    public void reloadList(){
    	try{
    	GridArrayAdapter adapter2 = new GridArrayAdapter(this, Grid_items);
        grid.setAdapter(adapter2);
        
        grid.setOnItemClickListener(new OnItemClickListener() {

			
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				// TODO Auto-generated method stub
				//Toast.makeText(getApplicationContext(), "position="+position, Toast.LENGTH_SHORT).show();;
				
				try{
				if(arrAptmt!=null && arrAptmt.length>0 && position<=arrAptmt.length){
					Intent i=new Intent(ApartmentListPage.this,com.snagreporter.DefectListPage.class);
					i.putExtra("Floor", CurrentFloor);
					i.putExtra("Project", CurrentProject);
					i.putExtra("Building", CurrentBuilding);
					i.putExtra("isAptmt", true);
					i.putExtra("Apartment", arrAptmt[position]);
					startActivityForResult(i,10001);
				}
				else if(arrAptmt!=null && arrAptmt.length>0 && position>arrAptmt.length){
					int pos=position-arrAptmt.length;
					Intent i=new Intent(ApartmentListPage.this,com.snagreporter.DefectListPage.class);
					i.putExtra("Floor", CurrentFloor);
					i.putExtra("Project", CurrentProject);
					i.putExtra("Building", CurrentBuilding);
					i.putExtra("isAptmt", false);
					i.putExtra("SFA", arrSFA[pos]);
					startActivityForResult(i,10001);
				}
				else if((arrAptmt==null || arrAptmt.length==0) && arrSFA.length>0){
					//int o=position-1;
					Intent i=new Intent(ApartmentListPage.this,com.snagreporter.DefectListPage.class);
					i.putExtra("Floor", CurrentFloor);
					i.putExtra("Project", CurrentProject);
					i.putExtra("Building", CurrentBuilding);
					i.putExtra("isAptmt", false);
					i.putExtra("SFA", arrSFA[position]);
					startActivityForResult(i,10001);
				}
				}
		        catch(Exception e)
		        {
		        	Log.d("Exception",""+e.getMessage());
		        }
			}
		});
    	}
        catch(Exception e)
        {
        	Log.d("Exception",""+e.getMessage());
        }
    }
    public void populateCurrentFloor()
    {
    	try{
    		
            FMDBDatabaseAccess fmdb=new FMDBDatabaseAccess(ApartmentListPage.this);
    		
    		TradeAptAreaMaster objTrade=fmdb.getActiveData(CurrentProject.getID(),CurrentBuilding.getID(),CurrentFloor.getID(),"","apartmentlist");
    		
    		int actvsn=fmdb.getActiveSnag(CurrentProject.getID(),CurrentBuilding.getID(),CurrentFloor.getID(),"","apartmentlist");
    		ActiveSnag=""+actvsn;
    		
    		
    		if(LoginType.equals(getResources().getString(R.string.strInspector).toString()))
    				itemsParent.add(new Header(null, "FLOOR",true,false,false,true));
    		else
    				itemsParent.add(new Header(null, "FLOOR",true,false,false,false));
    				//itemsParent.add(new ListItemParent(null, "Project",""+CurrentProject.getProjectName()));
    				
    		
    		       itemsParent.add(new ActiveFields(null, ""+CurrentProject.getProjectName()+"  -  "+CurrentBuilding.getBuildingName()+"  -  "+CurrentFloor.getFloor(),false,""));
    		       // itemsParent.add(new ListItemParent(null, "Building",""+CurrentProject.getProjectName()+" - "+CurrentBuilding.getBuildingName()));
    				//itemsParent.add(new ListItemParent(null, "Floor",""+CurrentFloor.getFloor()));
    				//itemsParent.add(new ListItemParent(null, "ActiveSnag", ActiveSnag));
					
    				if(objTrade!=null)
    				{
    					ActiveTrade=objTrade.TradeType;
    					perComplete=""+objTrade.PercentageComplete;
    					ActiveApt=fmdb.getApartMentNameByID(objTrade.ProjectID, objTrade.BuildingID, objTrade.FloorID,objTrade.ApartmentID);
    					
    					//@@old
    					//itemsParent.add(new ActiveFields2(null, "Snags "+ActiveSnag,true,"Completed "+perComplete+"%"));
    					//itemsParent.add(new ActiveFields2(null,""+ActiveApt+" - "+ActiveTrade,false,""));
    					//oldend
    					
    					
    					//@@newcode
    					itemsParent.add(new ActiveFields2(null, "Snags "+ActiveSnag,false,""));
    					itemsParent.add(new ActiveFields2(null,""+ActiveApt+" - "+ActiveTrade,true,"Completed "+perComplete+"%"));
    					
    					//@@newend
//    					itemsParent.add(new ListItemParent(null, "ActiveTrade",ActiveTrade));
//    					itemsParent.add(new ListItemParent(null, "% Complete",perComplete));
//    					itemsParent.add(new ListItemParent(null, "ActiveApartment",ActiveApt));
    				}
    		
    		
            
    	}
    	catch(Exception e){
    	 Log.d("Error=", ""+e.getMessage());
    	}
    }
//    public void BackClick(View v){
//    	finish();
//    }
//    
    public void  getApartmentsFromWeb(){
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
    		    
    		    request.addProperty("_strTableName","ApartmentMaster");
    		   // request.addProperty("ProjectID",""+CurrentProject.getID());jaldip
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
    public void parseData(JSONObject obj){
    	try{
    		String ID=obj.getString("ID");
    		String ApartmentNo=obj.getString("ApartmentNo");
    		
    		
    		String FloorID=obj.getString("FloorID")==null?"":obj.getString("FloorID");
    		String Floor=obj.getString("Floor")==null?"":obj.getString("Floor");
    		String BuildingID=obj.getString("BuildingID")==null?"":obj.getString("BuildingID");
    		String BuildingName=obj.getString("BuildingName")==null?"":obj.getString("BuildingName");
    		String ProjectID=obj.getString("ProjectID")==null?"":obj.getString("ProjectID");
    		String ProjectName=obj.getString("ProjectName")==null?"":obj.getString("ProjectName");
    		String AptPlanID=obj.getString("AptPlanID")==null?"":obj.getString("AptPlanID");
    		String AptPlanName=obj.getString("AptPlanName")==null?"":obj.getString("AptPlanName");
    		String AptType=obj.getString("AptType")==null?"":obj.getString("AptType");
    		ApartmentMaster prj=new ApartmentMaster();
    		prj.setID(ID);
    		prj.setApartmentNo(ApartmentNo);
    		prj.setFloorID(FloorID);
    		prj.setFloor(Floor);
    		prj.setBuildingID(BuildingID);
    		prj.setBuildingName(BuildingName);
    		prj.setProjectID(ProjectID);
    		prj.setProjectName(ProjectName);
    		prj.setAptPlanID(AptPlanID);
    		prj.setAptPlanName(AptPlanName);
    		String ImageName=obj.getString("ImageName")==null?"":obj.getString("ImageName");
    		if(ImageName!=null && ImageName.length()!=0){
    			downloadImage(ImageName);
    		}
    		prj.setImageName(ImageName);
    		prj.setAptType(AptType);
    		prj.setisDataSyncToWeb(true);
    		prj.setStatusForUpload("Inserted");
    		FMDBDatabaseAccess db=new FMDBDatabaseAccess(getApplicationContext());
    		db.insertORUpdateApartment(prj);
    	}
    	catch(Exception e){
    		Log.d("Error=", ""+e.getMessage());
    	}
    }
    public void downloadImage(String imageUrl){
    	try{
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
    public void AddDefectClick(View v)
    {
    	try{
    	Intent i=new Intent(ApartmentListPage.this,com.snagreporter.AddNewApartment.class);
    	i.putExtra("project", CurrentProject);
    	i.putExtra("floor", CurrentFloor);
    	i.putExtra("building",CurrentBuilding);
    	
    	startActivity(i);
    	}
        catch(Exception e)
        {
        	Log.d("Exception",""+e.getMessage());
        }
    	
    }
    public void EditItemClick(View v)
    {
    	try{
    	Intent i=new Intent(ApartmentListPage.this,com.snagreporter.EditFloor.class);
    	i.putExtra("project",CurrentProject);
    	i.putExtra("building",CurrentBuilding);
    	i.putExtra("floor",CurrentFloor);
    	startActivity(i);
    	}
        catch(Exception e)
        {
        	Log.d("Exception",""+e.getMessage());
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
   
    
    private String[] mApts;
//  = new String[] {
//			"Jan", "Feb" , "Mar", "Apr", "May", "Jun",
//			"Jul", "Aug" , "Sep", "Oct", "Nov", "Dec"
//		};
  
  private void openChart(){
	  try{
  	SharedPreferences SP = getSharedPreferences("AppDelegate",MODE_PRIVATE);
  	String id=SP.getString("RegUserID", "");
  	
  	
  	mApts=new String[arrAptmt.length];
  	for(int i=0;i<arrAptmt.length;i++){
  		mApts[i]=arrAptmt[i].getApartmentNo();
  	}
  	
  	int[] x=new int[arrAptmt.length];
  	for(int i=0;i<arrAptmt.length;i++){
  		x[i]=i;
  	}
  	FMDBDatabaseAccess db=new FMDBDatabaseAccess(ApartmentListPage.this);
  	
  	int[] resolved = new int[arrAptmt.length];//{ 2000,2500,2700,3000,2800,3500,3700,3800};
  	for(int i=0;i<arrAptmt.length;i++){
  		resolved[i]=db.getResolvedSnagsByApt(CurrentProject.getID(), CurrentBuilding.getID(), CurrentFloor.getID(), arrAptmt[i].getID(), id);
  	}
  	
  	int[] expense = new int[arrAptmt.length];//{2200, 2700, 2900, 2800, 2600, 3000, 3300, 3400 };
  			for(int i=0;i<arrAptmt.length;i++){
  				expense[i]=db.getUnresolvedSnagsByApt(CurrentProject.getID(), CurrentBuilding.getID(), CurrentFloor.getID(), arrAptmt[i].getID(), id);
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
  	incomeRenderer.setLineWidth(1);
  	incomeRenderer.setDisplayChartValues(true);
  	
  	// Creating XYSeriesRenderer to customize expenseSeries
  	XYSeriesRenderer expenseRenderer = new XYSeriesRenderer();
  	expenseRenderer.setColor(Color.rgb(220, 80, 80));
  	expenseRenderer.setFillPoints(true);
  	expenseRenderer.setLineWidth(1);
  	expenseRenderer.setDisplayChartValues(true);    	
  	
  	// Creating a XYMultipleSeriesRenderer to customize the whole chart
  	XYMultipleSeriesRenderer multiRenderer = new XYMultipleSeriesRenderer();
  	multiRenderer.setXLabels(0);
  	multiRenderer.setChartTitle(""+CurrentProject.getProjectName()+" - "+CurrentBuilding.getBuildingName()+" - "+CurrentFloor.getFloor());
  	multiRenderer.setXTitle("Apartments/Areas");
  	multiRenderer.setYTitle("Number of Snags");
  	multiRenderer.setZoomButtonsVisible(true);    	    	
  	for(int i=0; i< x.length;i++){
  		multiRenderer.addXTextLabel(i, mApts[i]);    		
  	}    	
  	
  	
  	// Adding incomeRenderer and expenseRenderer to multipleRenderer
  	// Note: The order of adding dataseries to dataset and renderers to multipleRenderer
  	// should be same
  	multiRenderer.addSeriesRenderer(incomeRenderer);
  	multiRenderer.addSeriesRenderer(expenseRenderer);
  	
  	// Creating an intent to plot bar chart using dataset and multipleRenderer    	
  	Intent intent = ChartFactory.getBarChartIntent(getBaseContext(), dataset, multiRenderer, Type.DEFAULT);
  	
  	// Start Activity
  	startActivity(intent);
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
  		menuhandler.SaveAttachment(CurrentProject.getID(),CurrentBuilding.getID(), CurrentFloor.getID(), "", "", "", "", "", FileName, FilePath);
  		//Toast.makeText(ProjectListPage.this, "File123="+FilePath, Toast.LENGTH_LONG).show();
  	 }
  	 
  	if(resultCode==10001){
  		setResult(10001);
  		finish();
  	}
  	else if(resultCode==10002){
  		if(!CameFromStartPage){
		setResult(10002);
		finish();
  		}
  		else{
			FMDBDatabaseAccess db=new FMDBDatabaseAccess(ApartmentListPage.this);
            SharedPreferences SP = getSharedPreferences("AppDelegate",MODE_PRIVATE);
			db.performLogout(SP.getString("RegUserID", ""));
			Intent i=new Intent(ApartmentListPage.this,com.snagreporter.Login_page.class);
			i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(i);
			
			finish();
		}
	}
  	}
     catch(Exception e)
     {
     	Log.d("Exception",""+e.getMessage());
     }
  }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
 
        switch (item.getItemId())
        {
       /* case R.id.menu_showlist:
            // Single menu item is selected do something
            // Ex: launching new activity/screen or show alert message
            //Toast.makeText(ProjectListPage.this, "Bookmark is Selected", Toast.LENGTH_SHORT).show();
        	openChart();
            return true;
        case R.id.menu_showlistinWeb:
        	Intent i=new Intent(ApartmentListPage.this,com.snagreporter.GraphWebView.class);
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
            return true;*/
        case R.id.menuBtn_exit:
        	new AlertDialog.Builder(ApartmentListPage.this)
    	    
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
        	new AlertDialog.Builder(ApartmentListPage.this)
    	    
    	    .setMessage("Are you sure you want to Logout?")
    	    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
    	        public void onClick(DialogInterface dialog, int which) { 
//    	            FMDBDatabaseAccess db=new FMDBDatabaseAccess(ApartmentListPage.this);
//    	            SharedPreferences SP = getSharedPreferences("AppDelegate",MODE_PRIVATE);
//    				db.performLogout(SP.getString("RegUserID", ""));
//    				Intent i=new Intent(ApartmentListPage.this,com.snagreporter.Login_page.class);
//    				i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//    				startActivity(i);
//    				
//    				finish();
    	        	try{
    	        	if(!CameFromStartPage){
    	        		setResult(10002);
    	        		finish();
    	          		}
    	          		else{
    	        			FMDBDatabaseAccess db=new FMDBDatabaseAccess(ApartmentListPage.this);
    	                    SharedPreferences SP = getSharedPreferences("AppDelegate",MODE_PRIVATE);
    	        			db.performLogout(SP.getString("RegUserID", ""));
    	        			Intent i=new Intent(ApartmentListPage.this,com.snagreporter.Login_page.class);
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
    
    public void SyncData(){
    	//StartSyncProgress task=new StartSyncProgress();
    	//task.execute(10);
    	ParseSyncData parser=new ParseSyncData(ApartmentListPage.this);
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
            	try{
            	if(!mProgressDialog2.isShowing()){
            		mProgressDialog2.setCancelable(false);
            		mProgressDialog2.setMessage("Synchronizing...");
            		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
            		mProgressDialog2.show();
            	}
            	FMDBDatabaseAccess db=new FMDBDatabaseAccess(ApartmentListPage.this);
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
            	FMDBDatabaseAccess db=new FMDBDatabaseAccess(ApartmentListPage.this);
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
//            		mProgressDialog2.setMessage("Synchronization Complete...");
//            		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
//            		mProgressDialog2.dismiss();
            		StartDownloadSnags task=new StartDownloadSnags();
            		task.execute(10);
            	}
            	}
                catch(Exception e)
                {
                	Log.d("Exception",""+e.getMessage());
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
                	Log.d("Exception",""+e.getMessage());
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
        	FMDBDatabaseAccess db=new FMDBDatabaseAccess(ApartmentListPage.this);
    		db.insertORUpdateSnagMaster(obj);
    	}
    	catch(Exception e){
    		Log.d("Error=", ""+e.getMessage());
    	}
    }

        
      //@@@@@@$$$$$$$$$$@@@@@@@@@@@
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
        	catch(Exception e){
        		
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

    		
    		FMDBDatabaseAccess db=new FMDBDatabaseAccess(ApartmentListPage.this);
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
        	catch(Exception e){
        		
        	}
        }
         
}
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
    	catch(Exception e){
    		
    	}
    }
    public byte[] getBytesFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(CompressFormat.JPEG, 70, stream);
        return stream.toByteArray();
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
        		
        		if(cursor.getString(cursor.getColumnIndex("Cost"))==null || (cursor.getString(cursor.getColumnIndex("Cost"))!=null && cursor.getString(cursor.getColumnIndex("Cost")).length()==0))
            		obj.setCost(0.0);
            		else{
            			obj.setCost(Double.parseDouble(cursor.getString(cursor.getColumnIndex("Cost"))));
            		}
            		
            		obj.setCostTo(cursor.getString(cursor.getColumnIndex("CostTo")));
            		obj.setSnagPriority(cursor.getString(cursor.getColumnIndex("PriorityLevel")));
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
        		
        		if(cursor.getString(cursor.getColumnIndex("Cost"))==null || (cursor.getString(cursor.getColumnIndex("Cost"))!=null && cursor.getString(cursor.getColumnIndex("Cost")).length()==0))
            		obj.setCost(0.0);
            		else{
            			obj.setCost(Double.parseDouble(cursor.getString(cursor.getColumnIndex("Cost"))));
            		}
            		
            		obj.setCostTo(cursor.getString(cursor.getColumnIndex("CostTo")));
            		obj.setSnagPriority(cursor.getString(cursor.getColumnIndex("PriorityLevel")));
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
    				new AlertDialog.Builder(ApartmentListPage.this)
    	    	    
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
    	                	catch(Exception e){
    	                		
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
    			openChart2();
    		}
    		catch(Exception e){
    			Log.d("Error=", ""+e.getMessage());
    		}
    	}
    	public void ShowChartClick(View v){
    		try{	
    			Intent i=new Intent(ApartmentListPage.this,com.snagreporter.GraphWebView.class);
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
    			Intent i=new Intent(ApartmentListPage.this,com.snagreporter.BarChartActivity.class);
    			//i.putExtra("project", true);
    			
//    			i.putExtra("building", true);
//    			i.putExtra("currentprojectID", CurrentProject.getID());
    			
    			//i.putExtra("floor", true);
    			//i.putExtra("currentBuilding", CurrentBuilding.getID());
    			
    			i.putExtra("apartment", true);
    			i.putExtra("currentfloor", CurrentFloor);
            	startActivity(i);
    		}
    		catch(Exception e){
    			
    		}
    	}
}