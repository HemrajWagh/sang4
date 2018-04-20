package com.snagreporter;

import java.io.ByteArrayOutputStream;
import java.io.File;
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
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
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
import com.snagreporter.entity.StdApartmentAreas;
import com.snagreporter.entity.StdFloorAreas;

import android.app.DatePickerDialog;


public class AddDefect extends Activity
{
	String[] strValues;
 	ListView list;
	ImageButton imgVw1,imgVw2,imgVw3;
	int cnt=1;
	List<Item> items;
	String sample;
	BuildingMaster CurrentBuilding;
	FloorMaster CurrentFloor;
	ProjectMaster CurrentProject;
	ApartmentMaster CurrentAPT;
	String SelectedArea,SelectedAreaID,SelectedJobType,SelectedJobDetails,SelectedFaultType;
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
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.add_defect);
       
        CurrentProject=(ProjectMaster) getIntent().getExtras().get("Project");
        CurrentBuilding=(BuildingMaster)getIntent().getExtras().get("Building");
        CurrentFloor=(FloorMaster)getIntent().getExtras().get("Floor");
        isAptmt=getIntent().getExtras().getBoolean("isAptmt");
        
        Button btn=(Button)findViewById(R.id.adddefectSavebtn);
        btn.setBackgroundDrawable(getResources().getDrawable(R.drawable.back_blue_button));
        
        
        SharedPreferences SP = getSharedPreferences("AppDelegate",MODE_PRIVATE);
        FMDBDatabaseAccess obj=new FMDBDatabaseAccess(getApplicationContext());
        arrInspetor=new Registration[1];
        arrInspetor[0]=obj.getRegistrationDetailForID(SP.getString("RegUserID", ""));
        Object objNot = getLastNonConfigurationInstance();
        if(objNot!=null)
        {
        	adapter2=(TwoTextArrayAdapter)objNot;
        }
        
        if(isAptmt){
        	CurrentAPT=(ApartmentMaster)getIntent().getExtras().get("Apartment");
        	
        }
        else{
        	CurrentSFA=(StdFloorAreas)getIntent().getExtras().get("SFA");
        }
        
        //SelectedArea=getIntent().getExtras().getString("SelectedArea");
        if(isAptmt){
        
		/*StdApartmentAreas arrSAA[]=obj.getSAA();
		int tot=0;
		if(CurrentAPT!=null){
			if(arrSAA!=null && arrSAA.length>0){
				tot=CurrentAPT.getNoOfRooms()+CurrentAPT.getNoOfToilets()+arrSAA.length;
			}
			else{
				tot=CurrentAPT.getNoOfRooms()+CurrentAPT.getNoOfToilets();
			}
			
		}
		AreaList=new String[tot];
		int index=0;
        if(CurrentAPT!=null){
			for(int i=0;i<CurrentAPT.getNoOfRooms();i++){
				AreaList[index]="Room "+(i+1);
				index++;
			}
			
			for(int i=0;i<CurrentAPT.getNoOfToilets();i++){
				AreaList[index]="Toilet "+(i+1);
				index++;
			}
		}
		
        
		if(arrSAA!=null && arrSAA.length>0){
			for(int i=0;i<arrSAA.length;i++){
				AreaList[index]=arrSAA[i].getAreaName();
				index++;
			}
		}*/
    }
        else{
        	//AreaList=new String[1];
        	//AreaList[0]=CurrentSFA.getAreaName();
        }
       /* if(savedInstanceState!=null)
        {
        	SelectedArea=savedInstanceState.getString("SelectedArea");
        	Log.d("SelectedArea",""+SelectedArea);
        	SelectedFaultType=savedInstanceState.getString("SelectedFaultType");
        	Log.d("SelectedFaultType",SelectedFaultType);
        	SelectedJobDetails=savedInstanceState.getString("SelectedJobDetails");
        	Log.d("SelectedJobDetails", SelectedJobDetails);
        }*/
        
        FMDBDatabaseAccess db=new FMDBDatabaseAccess(getApplicationContext());
        AreaList=db.getApartmentDetails(CurrentAPT);
        imgVw1=(ImageButton)findViewById(R.id.row_cell_addDefectPhoto_img1);
        imgVw2=(ImageButton)findViewById(R.id.row_cell_addDefectPhoto_img2);
        imgVw3=(ImageButton)findViewById(R.id.row_cell_addDefectPhoto_img3);
        list=(ListView)findViewById(R.id.android_add_defect_list);
       
        if(AreaList==null || AreaList.length==0){
        	DownloadProfileImage task=new DownloadProfileImage();
			task.execute(10);
        	//getApartmentDetailsFromWeb();
        	
        }
        else
        {
        	
        	ContinueProcess();
        	
        }
       // Toast.makeText(getApplicationContext(), "Came in OnCreate "+cnt++, Toast.LENGTH_LONG).show();
        
    }
    
   /*@Override
	public void onConfigurationChanged(Configuration newConfig){
		
		super.onConfigurationChanged(newConfig);
		Log.d("area", ""+SelectedArea);
		Log.d("job details",""+SelectedJobDetails);
		Log.d("faulttype",""+SelectedFaultType);
		int wantedPosition =1; // Whatever position you're looking for
		int firstPosition = list.getFirstVisiblePosition() - list.getHeaderViewsCount(); // This is the same as child #0
			int wantedChild = wantedPosition - firstPosition;
		final View v=list.getChildAt(wantedChild);
		    TextView t=(TextView)v.findViewById(R.id.row_cell_addDefect_text);
		    t.setText("Jaldip");
		    //Toast.makeText(getApplicationContext(), "Changed", Toast.LENGTH_LONG).show();
		    //v=list.
		//if
		    Toast.makeText(getApplicationContext(), "Came in Config "+cnt++, Toast.LENGTH_LONG).show();
		 
	}*/
   /* @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
      // Save UI state changes to the savedInstanceState.
      // This bundle will be passed to onCreate if the process is
      // killed and restarted.
     savedInstanceState.putString("SelectedArea", SelectedArea);
     savedInstanceState.putString("SelectedFaultType", SelectedFaultType);
     savedInstanceState.putString("SelectedJobDetails",SelectedJobDetails);
     //savedInstanceState.putInt("MyInt", 1);
     //savedInstanceState.putString("MyString", "Welcome back to Android");
     // etc.
     super.onSaveInstanceState(savedInstanceState);
     }*/
    @Override
    public Object onRetainNonConfigurationInstance()
    {
        strValues=new String[8];
        strValues[0]="";
        
    	return adapter;
    }

   @Override
    public void onResume(){
    	super.onResume();
    	//Toast.makeText(getApplicationContext(), "Came in Resume "+cnt++, Toast.LENGTH_LONG).show();
        //else{
        	
        	
       // }
        
    }
  
    ///////////Date
    protected class DownloadProfileImage extends AsyncTask<Integer , Integer, Void> {
    	ProgressDialog mProgressDialog = new ProgressDialog(AddDefect.this);
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
    		items = new ArrayList<Item>();
  	       items.add(new Header(null, "ADD DEFECT",false));
  	       
  	       populateData();
  	       if(AreaList!=null && AreaList.length>0)
  	    	   SelectedArea=AreaList[0].getAptAreaName();
  	       else 
  	    	 SelectedArea="";
  	     //SelectedAreaID=AreaList[0].getaptare
  	       SelectedJobType=arrJobType[0].getJobType();
  	       SelectedJobDetails=arrJobType[0].getJobDetails();
  	       
  	       if(arrFaultType!=null && arrFaultType.length!=0){
  	    	   SelectedFaultType=arrFaultType[0].getFaultType();
  	       }
  	       
  	       
  	       
  	       
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
  	      //}
  	        
  	        
  	        
  	        list.setOnItemClickListener(new OnItemClickListener() {

  				@Override
  				public void onItemClick(AdapterView<?> arg0, View arg1, int position,
  						long arg3) {
  					// TODO Auto-generated method stub
  					if(position!=0 && position!=6 && position!=7 && position!=4){
  						//Toast.makeText(getApplicationContext(), "list position="+position, Toast.LENGTH_LONG).show();;
  						//View v=arg1;//ListItemAddDefect
  						//SelTextInList=(TextView)v.findViewById(R.id.row_cell_addDefect_text);
  						registerForContextMenu(arg0);
  			    		as_index=position-1;
  			    		openContextMenu(arg0);
  			    		unregisterForContextMenu(arg0);
  					}
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
  					else if(position==4){
  						AlertDialog.Builder alert = new AlertDialog.Builder(AddDefect.this);

  						alert.setTitle("Comments");
  						//alert.setMessage("Message");

  						// Set an EditText view to get user input 
  						final EditText input = new EditText(AddDefect.this);
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
    		FMDBDatabaseAccess obj=new FMDBDatabaseAccess(getApplicationContext());
    		arrJobType=obj.getJobType();
    		
    		if(arrJobType==null || arrJobType.length==0){
    			//getJobTypeFromWeb();
    			arrJobType=obj.getJobType();
    		}
    		
    		arrFaultType=obj.getFaultType(arrJobType[0].getID());
    		if(AreaList!=null && AreaList.length>0)
    			items.add(new ListItemAddDefect(null, "Area",""+AreaList[0].getAptAreaName()));
    		else
    			items.add(new ListItemAddDefect(null, "Area",""+""));
    		items.add(new ListItemAddDefect(null, "SnagType",""+arrJobType[0].getJobType()));
    	   if(arrFaultType!=null && arrFaultType.length>0)//
    			items.add(new ListItemAddDefect(null, "FaultType",""+arrFaultType[0].getFaultType()));
    		else 
    			items.add(new ListItemAddDefect(null, "FaultType",""));
    		items.add(new ListItemAddDefect(null, "Comments",""+arrJobType[0].getJobDetails()));
    		
    		if(arrInspetor.length>0)
    			items.add(new ListItemAddDefect(null, "Inspector",""+arrInspetor[0].getFirstName()+" "+arrInspetor[0].getLastName()));
    		else
    			items.add(new ListItemAddDefect(null, "Inspector","-No Inspector found-"));
    		
    		
    		final Calendar cal = Calendar.getInstance();
            year = cal.get(Calendar.YEAR);
            month = cal.get(Calendar.MONTH);
            day = cal.get(Calendar.DAY_OF_MONTH);
            ReportedDate=GetDate();
            ExpectedDate=GetDate();
    		items.add(new ListItemAddDefect(null, "Reported Date",""+ReportedDate));
    		items.add(new ListItemAddDefect(null, "Expected Inspection Date",""+ExpectedDate));
    		items.add(new ListItemAddDefectPhoto(null, "Snag Photos",null,null,null,1,false,"","",""));
    		
    		
    		
    		
    		obj=null;
    		
            
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
		menu.setHeaderTitle("Select Area");
		for(int i=0;i<AreaList.length;i++){
			menu.add(0, v.getId(), 0, ""+AreaList[i].getAptAreaName());
		}
	}
	else if(as_index==1){
		menu.setHeaderTitle("Select SnagType");
		for(int i=0;i<arrJobType.length;i++){
			menu.add(0, v.getId(), 0, ""+arrJobType[i].getJobType());
		}
	}
	else if(as_index==2){
		menu.setHeaderTitle("Select FaultTYpe");
		if(arrFaultType!=null && arrFaultType.length>0){
		for(int i=0;i<arrFaultType.length;i++){
			menu.add(0, v.getId(), 0, ""+arrFaultType[i].getFaultType());
		}
		}
		else{
			
		}
	}
	else if(as_index==4){//Inspector
		menu.setHeaderTitle("Select Inspector");
		for(int i=0;i<arrInspetor.length;i++){
			menu.add(0, v.getId(), 0, ""+arrInspetor[0].getFirstName()+" "+arrInspetor[0].getLastName());
		}
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
		 Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
			
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
		
		}
		
		

	}
	else if(as_index==2){
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
		SelectedFaultType=item.getTitle().toString();
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
               
            BtnYourImg = BitmapFactory.decodeFile(filePath);
            
//           float f=BtnYourImg.getHeight();
//           //float f2=BtnYourImg.getWidth();
//           if(f>100){
//        	   Bitmap temp=scaleDownBitmap(BtnYourImg, 100, getApplicationContext());
//        	   BtnYourImg=temp;
//           }
//           f=BtnYourImg.getHeight();
            //showImage();
          
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
           // startActivity(i);
          
           startActivityForResult(i, 2);
           break;
           
        }
    case 2:
    {
   	 
   	 if(resultCode==10 && imageReturnedIntent!=null)
   	 {
   	// Log.d("strFromImgvw", "strFromImgvw");
   		//Toast.makeText(getApplicationContext(), "Came here 10", Toast.LENGTH_LONG).show();
   	 strFromImgvw=imageReturnedIntent.getExtras().getString("strFromImgvw");
   	 strFilePath=imageReturnedIntent.getExtras().getString("strFilePath");
   	 String retUUID=imageReturnedIntent.getExtras().getString("UUID");
   	//BtnImageBmp=(Bitmap)getIntent().getParcelableExtra("btnBitmap");
   	 BtnImageBmp=BitmapFactory.decodeFile(strFilePath);
   	 if(strFromImgvw.equalsIgnoreCase("img1"))
   	 {
   		int wantedPosition = 8; // Whatever position you're looking for
		int firstPosition = list.getFirstVisiblePosition() - list.getHeaderViewsCount(); // This is the same as child #0
		int wantedChild = wantedPosition - firstPosition;
		// Say, first visible position is 8, you want position 10, wantedChild will now be 2
		// So that means your view is child #2 in the ViewGroup:
		if (wantedChild < 0 || wantedChild >= list.getChildCount()) {
		  Log.d("Child Not Available", "");
		}
		else{
			View v=list.getChildAt(wantedChild);
			PhotoURl1=retUUID;
			ImageButton btnI=new ImageButton(this);
			btnI.setImageBitmap(BtnImageBmp);
			imgVw1=(ImageButton)v.findViewById(R.id.row_cell_addDefectPhoto_img1);
			imgVw1.setBackgroundDrawable(btnI.getDrawable());
			//imgVw1.setBackgroundColor(getResources().getColor(R.color.transparent));
			//imgVw1.setImageBitmap(BtnImageBmp);
			isImageSetFor1=true;
		}
   	 }
   	 else if(strFromImgvw.equalsIgnoreCase("img2"))
   	 {
   		int wantedPosition = 8; // Whatever position you're looking for
		int firstPosition = list.getFirstVisiblePosition() - list.getHeaderViewsCount(); // This is the same as child #0
		int wantedChild = wantedPosition - firstPosition;
		// Say, first visible position is 8, you want position 10, wantedChild will now be 2
		// So that means your view is child #2 in the ViewGroup:
		if (wantedChild < 0 || wantedChild >= list.getChildCount()) {
		  Log.d("Child Not Available", "");
		}
		else{
			View v=list.getChildAt(wantedChild);
   		PhotoURl2=retUUID;
   		ImageButton btnI=new ImageButton(this);
		btnI.setImageBitmap(BtnImageBmp);
		imgVw2=(ImageButton)v.findViewById(R.id.row_cell_addDefectPhoto_img2);
   		 imgVw2.setBackgroundDrawable(btnI.getDrawable());
		//imgVw2.setBackgroundColor(getResources().getColor(R.color.transparent));
		//imgVw2.setImageBitmap(BtnImageBmp);
   		isImageSetFor2=true;
		}
   	 }
   	 else if(strFromImgvw.equalsIgnoreCase("img3"))
   	 {
   		int wantedPosition = 8; // Whatever position you're looking for
		int firstPosition = list.getFirstVisiblePosition() - list.getHeaderViewsCount(); // This is the same as child #0
		int wantedChild = wantedPosition - firstPosition;
		// Say, first visible position is 8, you want position 10, wantedChild will now be 2
		// So that means your view is child #2 in the ViewGroup:
		if (wantedChild < 0 || wantedChild >= list.getChildCount()) {
		  Log.d("Child Not Available", "");
		}
		else{
			View v=list.getChildAt(wantedChild);
   		PhotoURl3=retUUID;
   		ImageButton btnI=new ImageButton(this);
		btnI.setImageBitmap(BtnImageBmp);
		imgVw3=(ImageButton)v.findViewById(R.id.row_cell_addDefectPhoto_img3);
   		 imgVw3.setBackgroundDrawable(btnI.getDrawable());
		//imgVw2.setBackgroundColor(getResources().getColor(R.color.transparent));
   		//imgVw3.setImageBitmap(BtnImageBmp);
   		isImageSetFor3=true;
		}
   	 }
   	 //Log.d("strFromImgvw",""+strFromImgvw);
   	 }
   	 break;
    }
    case CAMERA_PIC_REQUEST:
    {
    	
    	//Toast.makeText(getApplicationContext(), "Came here", Toast.LENGTH_LONG).show();
    	//Log.d("Came here","");
   	 mBitmap = (Bitmap) imageReturnedIntent.getExtras().get("data");
   	Uri uri = (Uri) imageReturnedIntent.getExtras().get("data");
	// set the imageview image via uri 
	//_previewImage.setImageURI(uri);
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
//         file.createNewFile();
//         FileOutputStream ostream = new FileOutputStream(file);
//       //  bmImg.compress(CompressFormat.JPEG, 100, ostream);
//         bitmap.compress(CompressFormat.JPEG, 100, ostream);
//        
//         ostream.close();
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
        i.putExtra("BitmapImage", mBitmap);
        i.putExtra("strFromImg", strFromImg);
        startActivityForResult(i, 2);
    }
    
    
    
    }
	 }
	 catch(Exception e){
		 Log.d("Error=", ""+e.getMessage());
		// Toast.makeText(getApplicationContext(), "Error="+e.getMessage(), Toast.LENGTH_LONG).show();
	 }
}

public static Bitmap scaleDownBitmap(Bitmap photo, int newHeight, Context context) {

	 final float densityMultiplier = context.getResources().getDisplayMetrics().density;        

	 int h= (int) (newHeight*densityMultiplier);
	 int w= (int) (h * photo.getWidth()/((double) photo.getHeight()));

	 photo=Bitmap.createScaledBitmap(photo, w, h, true);

	 return photo;
	 }
	public void SaveClick(View v){
	try{
		DownloadDataFromWeb task=new DownloadDataFromWeb();
		task.execute(10);
		
	}
	catch(Exception e){
		Log.d("Error=", ""+e.getMessage());
	}
	
}
	protected class DownloadDataFromWeb extends AsyncTask<Integer , Integer, Void> {
    	ProgressDialog mProgressDialog = new ProgressDialog(AddDefect.this);
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
        		SnagMaster obj=new SnagMaster();
        		UUID uuid=UUID.randomUUID();
        		
        		obj.setID(uuid.toString());
        		obj.setSnagType(SelectedJobType);
        		obj.setSnagDetails(SelectedJobDetails);
        		obj.setPictureURL1(PhotoURl1);
        		obj.setPictureURL2(PhotoURl2);
        		obj.setPictureURL3(PhotoURl3);
        		obj.setProjectID(CurrentProject.getID());
        		obj.setProjectName(CurrentProject.getProjectName());
        		obj.setBuildingID(CurrentBuilding.getID());
        		obj.setBuildingName(CurrentBuilding.getBuildingName());
        		obj.setFloorID(CurrentFloor.getID());
        		obj.setFloor(CurrentFloor.getFloor());
        		if(isAptmt){
        		obj.setApartmentID(CurrentAPT.getID());
        		obj.setApartment(CurrentAPT.getApartmentNo());
        		}
        		else{
        			obj.setApartmentID(CurrentSFA.getID());
        			obj.setApartment(CurrentSFA.getAreaName());
        		}
        		obj.setAptAreaName(SelectedArea);
        		obj.setReportDate(""+ReportedDate);
        		obj.setSnagStatus("Pending");
        		obj.setResolveDate("");
        		//String str=SelectedFaultType;
        		obj.setExpectedInspectionDate(ExpectedDate);
        		obj.setFaultType(SelectedFaultType);
        		
        		obj.setInspectorID(arrInspetor[selectedInspectorIndex].getID());
        		obj.setInspectorName(arrInspetor[selectedInspectorIndex].getFirstName()+" "+arrInspetor[selectedInspectorIndex].getLastName());
        		
        		
        		
        		
//        		FMDBDatabaseAccess db=new FMDBDatabaseAccess(AddDefect.this);
//        		db.InsertIntoSnagMaster(obj);
//        		db=null;
        		
        		
        		
        		
        		
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
	    		    
	    		    
	    		    String CoumnNames="ID~SnagType~SnagDetails~PictureURL1~PictureURL2~PictureURL3~ProjectID~ProjectName~BuildingID~BuildingName~FloorID~Floor~ApartmentID~Apartment~AptAreaName~SnagStatus~ResolveDate~ExpectedInspectionDate~FaultType~InspectorID~InspectorName";
	    		    String Values=""+obj.getID()+"~"+obj.getSnagType()+"~"+obj.getSnagDetails()+"~"+obj.getPictureURL1()+"~"+obj.getPictureURL2()+"~"+obj.getPictureURL3()+"~"+obj.getProjectID()+"~"+obj.getProjectName()+"~"+obj.getBuildingID()+"~"+obj.getBuildingName()+"~"+obj.getFloorID()+"~"+obj.getFloor()+"~"+obj.getApartmentID()+"~"+obj.getApartment()+"~"+obj.getAptAreaName()+"~"+obj.getSnagStatus()+"~"+obj.getResolveDate()+"~"+obj.getExpectedInspectionDate()+"~"+obj.getFaultType()+"~"+obj.getInspectorID()+"~"+obj.getInspectorName();
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
    		catch (Exception e){
                Log.d("Error=", ""+e.getMessage()); 
    		}
            return null;
        }
        @Override
        protected void onPostExecute(Void result) {
        	mProgressDialog.dismiss();
        	SnagMaster obj=new SnagMaster();
    		UUID uuid=UUID.randomUUID();
    		
    		obj.setID(uuid.toString());
    		obj.setSnagType(SelectedJobType);
    		obj.setSnagDetails(SelectedJobDetails);
    		obj.setPictureURL1(PhotoURl1);
    		obj.setPictureURL2(PhotoURl2);
    		obj.setPictureURL3(PhotoURl3);
    		obj.setProjectID(CurrentProject.getID());
    		obj.setProjectName(CurrentProject.getProjectName());
    		obj.setBuildingID(CurrentBuilding.getID());
    		obj.setBuildingName(CurrentBuilding.getBuildingName());
    		obj.setFloorID(CurrentFloor.getID());
    		obj.setFloor(CurrentFloor.getFloor());
    		if(isAptmt){
    			obj.setApartmentID(CurrentAPT.getID());
    			obj.setApartment(CurrentAPT.getApartmentNo());
    		}
    		else{
    			obj.setApartmentID(CurrentSFA.getID());
    			obj.setApartment(CurrentSFA.getAreaName());
    		}
    		obj.setAptAreaName(SelectedArea);
    		obj.setReportDate(""+ReportedDate);
    		obj.setSnagStatus("Pending");
    		obj.setResolveDate("");
    		
    		obj.setExpectedInspectionDate(ExpectedDate);
    		obj.setFaultType(SelectedFaultType);
    		
    		obj.setInspectorID(arrInspetor[selectedInspectorIndex].getID());
    		obj.setInspectorName(arrInspetor[selectedInspectorIndex].getFirstName()+" "+arrInspetor[selectedInspectorIndex].getLastName());
    		
    		uploadImage(PhotoURl1);
    		uploadImage(PhotoURl2);
    		uploadImage(PhotoURl3);
    		
    		
    		FMDBDatabaseAccess db=new FMDBDatabaseAccess(AddDefect.this);
    		db.InsertIntoSnagMaster(obj);
    		db=null;
        	
        	finish();
        	
        	
        	
            
        }
         
}
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
    
    
    
    
}