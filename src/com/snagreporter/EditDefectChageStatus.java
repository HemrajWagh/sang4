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
import com.snagreporter.listitems.Header2;
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
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
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
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.TextView;

import com.snagreporter.database.*;
import com.snagreporter.entity.ApartmentMaster;
import com.snagreporter.entity.BuildingMaster;
import com.snagreporter.entity.FaultType;
import com.snagreporter.entity.FloorMaster;
import com.snagreporter.entity.JobType;
import com.snagreporter.entity.ProjectMaster;
import com.snagreporter.entity.QCC;
import com.snagreporter.entity.Registration;
import com.snagreporter.entity.SnagMaster;
import com.snagreporter.entity.StdApartmentAreas;
import com.snagreporter.entity.StdFloorAreas;
public class EditDefectChageStatus extends Activity// implements OnScrollListener
{
	
	
	
	
	
	
	boolean isExtArea,isExtFloorArea;
	ListView list;
	//ListView list2;
	//ListView list3;
	//ImageButton imgVw1,imgVw2,imgVw3;
	
	List<Item> items;
	//List<Item> items2;
	//List<Item> items3;
	FloorMaster curFloor;
	ProjectMaster curProject;
	BuildingMaster curBuilding;
	String[] strSetValues;
	String[] strGetValues;
 	ApartmentMaster CurrentAPT;
 	String strContractorStatus="";
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
	String AreaList[];
	String RemovedPhotoURL;
	boolean isAptmt;
	StdFloorAreas CurrentSFA;
	String Status[];
	String SelectedStatus;
	String ResolveDate="";
	String ExpectedDate="";
	int TAG=0;
	static View AllViews[];
	TwoTextArrayAdapter adapter;
	ImageView CurrentIMG;
	String oldStatus;
	
	static final int DATE_DIALOG_ID = 1;
	private int year, month, day;
	String SelectedDate="";
	String SelectedReinspDate="";
	
	int CurrentDate;
	boolean isOnline=false;
	ProgressDialog mProgressDialog2;
	
	private File dir, destImage,f;
	private String cameraFile = null;
	QCC[] arrQCC;
	String SelectedQCC;
	String strLoginType="";
	String RegUserID="";
	String arrContStatus[];
	
	View TopMenu;
	boolean isMenuVisible=false;
	MenuHandler menuhandler;
	SnagMaster UpdatedSnag;
	
	int index_Area=1;
	int index_Trades=2;
	int index_SubTrades=3;
	//int index_TradeDesc=4;
	int index_Comments=4;
	//int index_AllocatedTo=6;
	int index_Inspetor=5;
	int index_ReportedDate=6;
	int index_ExpInsDate=7;
	int index_Cost=8; 
	int index_CostTo=9;
	int index_SnagPriority=10;
	int index_SnagPhotos=11;
	int index_CntrStatus=12;
	int index_Status=13;
	int index_ReinspectionDate=14;
	int index_UnresolvedPhotos=15;
	int index_QualityCheckControl=16;
	Button btnUpdate;
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        try{
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.edit_defect_changestatus);
        
        menuhandler=new MenuHandler(EditDefectChageStatus.this);
        TopMenu=new View(this);
        RelativeLayout.LayoutParams rlp=new RelativeLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
        TopMenu.setLayoutParams(rlp);
        LayoutInflater inflater= LayoutInflater.from(this);
        TopMenu=(View) inflater.inflate(R.layout.popup_menu, null);
        this.addContentView(TopMenu, rlp);
        TopMenu.requestLayout();
        TopMenu.setVisibility(View.INVISIBLE);
        
        SharedPreferences SP = getSharedPreferences("AppDelegate",MODE_PRIVATE);
        RegUserID=SP.getString("RegUserID", "");
        FMDBDatabaseAccess obj=new FMDBDatabaseAccess(getApplicationContext());
        Registration REG=obj.getRegistration();
        strLoginType=REG.getType();
        btnUpdate=(Button)findViewById(R.id.editdfctchngstts_updBtn);
        isExtArea=getIntent().getBooleanExtra("isFromExtArea",false);
        isExtFloorArea=getIntent().getBooleanExtra("isFromFloorArea",false);
        mProgressDialog2 = new ProgressDialog(EditDefectChageStatus.this);
        isAptmt=getIntent().getBooleanExtra("isAptmt",false);
        AllViews=new View[19];
        curBuilding=(BuildingMaster)getIntent().getExtras().get("Building");
        curProject=(ProjectMaster)getIntent().getExtras().get("Project");
        if(!isExtArea)
        	curFloor=(FloorMaster)getIntent().getExtras().get("Floor");
        
        
        if(isAptmt){
        	CurrentAPT=(ApartmentMaster)getIntent().getExtras().get("Apartment");
        	
        }
        else
        {
        	CurrentSFA=(StdFloorAreas)getIntent().getExtras().get("SFA");
        }
       // CurrentAPT=(ApartmentMaster)getIntent().getExtras().get("Apartment");
        CurrentSnag=(SnagMaster)getIntent().getExtras().get("Snag");
      //  CurrentSnag.setSnagStatus("Reinspected & Unresolved");
       	//CurrentSnag.setReInspectedUnresolvedDatePictureURL2("download");
       // CurrentSnag.setReInspectedUnresolvedDate("123");
        oldStatus=CurrentSnag.getSnagStatus();
        if(CurrentSnag.getContractorStatus()!=null && !(CurrentSnag.getContractorStatus().equalsIgnoreCase("null")) && CurrentSnag.getContractorStatus().length()>0)
        {
        	strContractorStatus=CurrentSnag.getContractorStatus();
        }
        else
        {
        	strContractorStatus="Pending";
        }
        if(oldStatus.equalsIgnoreCase("Resolved"))
        {
        	if(btnUpdate!=null)
        		btnUpdate.setText("Ok");
        }
       strContractorStatus="Ended";
      
    // oldStatus="Reinspected & Unresolved";
   
     //CurrentSnag.setReInspectedUnresolvedDate("123");
    
     Object objNot = getLastNonConfigurationInstance();
        if(objNot!=null)
        {
        	strGetValues=(String[])objNot;
        }
       // populateCureentSnag();
        continueprocess();
        //SelectedArea=getIntent().getExtras().getString("SelectedArea");
       
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
    
    public void continueprocess()
    {
    	
    	try{
    	// if(isAptmt){
    	        FMDBDatabaseAccess obj=new FMDBDatabaseAccess(EditDefectChageStatus.this);
    			StdApartmentAreas arrSAA[]=obj.getSAA();
    			int tot=0;
    			
    			//if(strLoginType.equals("Inspector")){
    			if(strContractorStatus.equalsIgnoreCase("Ended"))
    			{
    				Status=new String[3];
    			}
    			else
    			{
    				Status=new String[2];
    			}
    			Status[0]="Pending";
    			Status[1]="Reinspected & Unresolved";
    			if(strContractorStatus.equalsIgnoreCase("Ended"))
    			{
    				Status[2]="Resolved";
    			}
//    			}
//    			else if(strLoginType.equals("Contractor")){
//    				Status=new String[4];
//        			Status[0]="Accepted";
//        			Status[1]="Started";
//        			Status[2]="Ended";
//        			Status[3]="Not Accepted";
//    			}
    			
    			
    			
    			
    			
    			/*if(CurrentAPT!=null){
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
    	  //  }
    	        //else
    	        //{
    	       // 	AreaList=new String[1];
    	       // 	AreaList[0]=CurrentSFA.getAreaName();
    	      //  }
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
    	       items.add(new Header2(null, "SNAG DETAILS",false,false,false,true));
    	       if(strGetValues!=null)
    	       {
    	    	  ExpectedDate=strGetValues[0];
    	          SelectedStatus=strGetValues[1];
    	          PhotoURl21=strGetValues[2];
    	          PhotoURl22=strGetValues[3];
    	          PhotoURl23=strGetValues[4];
    	          PhotoURl31=strGetValues[5];
    	          PhotoURl32=strGetValues[6];
    	          PhotoURl33=strGetValues[7];
    	          SelectedDate=strGetValues[8];
    	          SelectedQCC=strGetValues[10];
    	          SelectedReinspDate=strGetValues[11];
    	          CurrentSnag.setSnagStatus(SelectedStatus);
    	       }
    	       
    	      
    	       if(selectedCost==null || selectedCost.equalsIgnoreCase(""))
    				if(CurrentSnag.getCost()!=null)
    				selectedCost=CurrentSnag.getCost().toString();
    				else
    				selectedCost="0";
    			if(costTo==null || costTo.equalsIgnoreCase(""))
    				if(CurrentSnag.getCostTo()!=null)
    				costTo=CurrentSnag.getCostTo();
    				else
    				costTo="";
    			
    			if(SnagPriority==null || SnagPriority.equalsIgnoreCase(""))
    				if(CurrentSnag.getSnagPriority()!=null)
    				SnagPriority=CurrentSnag.getSnagPriority();
    				else
    				SnagPriority="";
    			
    			if(SelectedQCC==null || SelectedQCC.length()==0){
    				if(CurrentSnag.getQCC()!=null){
    					SelectedQCC=CurrentSnag.getQCC();
    				}
    				else{
    					SelectedQCC="";
    				}
    			}
    	       
    	       populateData();
    	       
    	       list.setDivider(getResources().getDrawable(R.color.transparent));
    	       list.setDividerHeight(1);
    	       
    	        adapter = new TwoTextArrayAdapter(this, items);
    	        list.setAdapter(adapter);
    	        //list.setOnScrollListener(this);
    	        
    	        
    	        list.setOnItemClickListener(new OnItemClickListener() {

    				
    				public void onItemClick(AdapterView<?> arg0, View arg1, int position,
    						long arg3) {
    					try{
    					// TODO Auto-generated method stub
    					if(!(oldStatus.equalsIgnoreCase("Resolved")))
    					{	
    						if(!SelectedStatus.equalsIgnoreCase(Status[0]) && position==list.getCount()-1){
        						registerForContextMenu(arg0);
    							as_index=101;
    							openContextMenu(arg0);
    							unregisterForContextMenu(arg0);
        					} //else if(position!=0  && position!=4 && position!=7 && position!=13 && position!=15 && position>6) 
    						else if(position!=0  && position!=4 && position!=7 && position!=14 && position!=16 && position>6) 
    						{
    						//Toast.makeText(EditDefectChageStatus.this, "list position="+position, Toast.LENGTH_LONG).show();;
    						//View v=arg1;//ListItemAddDefect
    						//SelTextInList=(TextView)v.findViewById(R.id.row_cell_addDefect_text);
    							
    								registerForContextMenu(arg0);
    								as_index=position;
    								openContextMenu(arg0);
    								unregisterForContextMenu(arg0);
    							
    						}
    					
    					
    					else if(position==7 || position==14 || position==16)  //(position==7 || position==13 || position==15)
    					{
    					
    						    	CurrentDate=position;
    						    	showDialog(DATE_DIALOG_ID);
    						    
							
    					}
    						//
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
//    @Override
//	public void onConfigurationChanged(Configuration newConfig){
//		super.onConfigurationChanged(newConfig);
//	}
    @Override
    public Object onRetainNonConfigurationInstance()
    {
    	strSetValues=new String[14];
    	strSetValues[0]=ExpectedDate;
        strSetValues[1]=SelectedStatus;
        strSetValues[2]=PhotoURl21;
        strSetValues[3]=PhotoURl22;
        strSetValues[4]=PhotoURl23;
        strSetValues[5]=PhotoURl31;
        strSetValues[6]=PhotoURl32;
        strSetValues[7]=PhotoURl33;
        strSetValues[8]=SelectedDate;
        strSetValues[9]=strFromImg;
        strSetValues[10]=SelectedQCC;
        strSetValues[11]=SelectedReinspDate;
        if(f!=null)
        	strSetValues[12]=f.getAbsolutePath().toString();
        strSetValues[13]=""+TAG;
    	return strSetValues;
    }
///////////Date
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
         	Log.d("Exception",""+e.getMessage());
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
	
	int wantedPosition = 7;
	if(CurrentDate==10)
		wantedPosition = 10;
	else if(CurrentDate==14)
		wantedPosition=14;
	else if(CurrentDate==16)
		wantedPosition=16;
	else
		wantedPosition = 7;
	
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
		if(CurrentDate==10)
		{	
			if(SelectedStatus.equals("Resolved")){
				SelectedDate=t.getText().toString();
				ListItemAddDefect itemi=(ListItemAddDefect)items.get(wantedPosition);
				itemi.str2=SelectedDate;
				items.set(wantedPosition,itemi);
				adapter.notifyDataSetChanged();
			}
			else if(SelectedStatus.equals(Status[1])){
				SelectedReinspDate=t.getText().toString();
				ListItemAddDefect itemi=(ListItemAddDefect)items.get(wantedPosition);
				itemi.str2=SelectedReinspDate;
				items.set(wantedPosition,itemi);
				adapter.notifyDataSetChanged();
			}
		}
		else{
			ExpectedDate=t.getText().toString();
			ListItemAddDefect itemi=(ListItemAddDefect)items.get(wantedPosition);
			itemi.str2=ExpectedDate;
			items.set(wantedPosition,itemi);
			adapter.notifyDataSetChanged();
		}
	}
	}
    catch(Exception e)
    {
    	Log.d("Exception",""+e.getMessage());
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
        	
        		View v=items.get(position).getView(mInflater, null);
        		AllViews[position]=v;
        		return v;
        	
        }
    }
    
    
    public void populateData()
    {
    	try{
    		FMDBDatabaseAccess obj=new FMDBDatabaseAccess(EditDefectChageStatus.this);
    		arrJobType=obj.getJobType();
    		
    		arrQCC=obj.getQCC();
    		
    		if(SelectedArea==null || SelectedArea.equals(""))
    			SelectedArea=CurrentSnag.getAptAreaName().toString();
    		if(SelectedJobDetails==null || SelectedJobDetails.equals(""))
    			SelectedJobDetails=CurrentSnag.getSnagDetails().toString();
    		if(SelectedJobType==null || SelectedJobType.equals(""))
    			SelectedJobType=CurrentSnag.getSnagType().toString();
    		if(SelectedFaultType==null || SelectedFaultType.equals(""))
    			SelectedFaultType=CurrentSnag.getFaultType().toString();
    		
    		if(ExpectedDate==null || ExpectedDate.equals(""))
    			ExpectedDate=CurrentSnag.getExpectedInspectionDate();
			if(ExpectedDate==null || ExpectedDate.equals(""))
				ExpectedDate=GetDate();
    		
    		
			items.add(new ListItemAddDefect(null, "Area",""+SelectedArea,true));
    		items.add(new ListItemAddDefect(null, "Trades",""+SelectedJobType,true));
    		if(SelectedFaultType!=null && !SelectedFaultType.equals("null") && !SelectedFaultType.equals(""))
    			items.add(new ListItemAddDefect(null, "Sub Trade",""+SelectedFaultType,true));
    		else
    			items.add(new ListItemAddDefect(null, "Sub Trade","",true));
    		items.add(new ListItemAddDefect(null, "Comments",""+SelectedJobDetails,true));
    		items.add(new ListItemAddDefect(null, "Inspector",""+CurrentSnag.getInspectorName(),true));
    		items.add(new ListItemAddDefect(null, "Report Date",CurrentSnag.getReportDate(),true));
    		if(!(oldStatus.equalsIgnoreCase("Resolved")))
    		items.add(new ListItemAddDefect(null, "Expected Inspection Date",""+ExpectedDate,false));
    		else{
    			items.add(new ListItemAddDefect(null, "Expected Inspection Date",""+ExpectedDate,true));
    		}
    		
  		items.add(new ListItemAddDefect(null, "Cost",""+selectedCost,true));
  	    items.add(new ListItemAddDefect(null, "CostTo",""+costTo,true));
  		items.add(new ListItemAddDefect(null, "SnagPriority",""+SnagPriority,true));
    		
    		
    		
    		if((PhotoURl1==null || PhotoURl1.equals("")) && CurrentSnag.getPictureURL1()!=null)
    			PhotoURl1=CurrentSnag.getPictureURL1().toString();
    		if((PhotoURl2==null || PhotoURl2.equals("")) && CurrentSnag.getPictureURL2()!=null)
    			PhotoURl2=CurrentSnag.getPictureURL2().toString();
    		if((PhotoURl3==null || PhotoURl3.equals("")) && CurrentSnag.getPictureURL3()!=null)
    			PhotoURl3=CurrentSnag.getPictureURL3().toString();
    		ImageButton btn1=null,btn2=null,btn3=null;
    		if(PhotoURl1!=null && PhotoURl1.length()>0)
    		{
    			String FilePath1=Environment.getExternalStorageDirectory()+File.separator+"SnagReporter/Pictures/"+PhotoURl1+".jpg";
    			File fooo = new File(FilePath1);
    			if(fooo.exists())
    			{
    			Bitmap Img1 = decodeFile(FilePath1);
    			btn1=new ImageButton(this);
    			btn1.setImageBitmap(Img1);
    			//imgVw1.setBackgroundDrawable(btnI.getDrawable());
    	   		isImageSetFor1=true;
    			}
    			else
    			{
    				DownloadImagesInBackGround task=new DownloadImagesInBackGround();
    				task.tag=1;
    				task.imageUrl=PhotoURl1;
    				task.fromImg=1;
    				task.execute(10);
    			}
    			
    		}
    		
    		if(PhotoURl2!=null && PhotoURl2.length()>0){
    			String FilePath2=Environment.getExternalStorageDirectory()+File.separator+"SnagReporter/Pictures/"+PhotoURl2+".jpg";
    			File fooo = new File(FilePath2);
    			if(fooo.exists()){
    			Bitmap Img2 = decodeFile(FilePath2);
    			 btn2=new ImageButton(this);
    			btn2.setImageBitmap(Img2);
    			//imgVw2.setBackgroundDrawable(btnI.getDrawable());
    	   		isImageSetFor2=true;
    			}
    			else
    			{
    				DownloadImagesInBackGround task=new DownloadImagesInBackGround();
    				task.tag=2;
    				task.imageUrl=PhotoURl2;
    				task.fromImg=1;
    				task.execute(10);
    			}
    			
    		}
    		
    		if(PhotoURl3!=null && PhotoURl3.length()>0){
    			String FilePath2=Environment.getExternalStorageDirectory()+File.separator+"SnagReporter/Pictures/"+PhotoURl3+".jpg";
    			File fooo = new File(FilePath2);
    			if(fooo.exists()){
    			Bitmap Img2 = decodeFile(FilePath2);
    			 btn3=new ImageButton(this);
    			btn3.setImageBitmap(Img2);
    			//imgVw3.setBackgroundDrawable(btnI.getDrawable());
    	   		isImageSetFor3=true;
    			}
    			else
    			{
    				DownloadImagesInBackGround task=new DownloadImagesInBackGround();
    				task.tag=3;
    				task.imageUrl=PhotoURl3;
    				task.fromImg=1;
    				task.execute(10);
    			}
    			
    		}
    		
    		items.add(new ListItemAddDefectPhoto(null, "Snag Photos",btn1,btn2,btn3,1,true,PhotoURl1,PhotoURl2,PhotoURl3,true));
    		//if(SelectedStatus==null || SelectedStatus.equals(""))
    		items.add(new ListItemAddDefect(null, "ContractorStatus",strContractorStatus,true));
    			SelectedStatus=CurrentSnag.getSnagStatus().toString();
    			if(!(oldStatus.equalsIgnoreCase("Resolved")))
    		items.add(new ListItemAddDefect(null, "Status",SelectedStatus,false));
    			else
    				items.add(new ListItemAddDefect(null, "Status",SelectedStatus,true));
    		
    		
    		
    		
    		btn1=null;btn2=null;btn3=null;
    		
    		//@@@
    	if(oldStatus.equalsIgnoreCase(Status[0]))
    	{
    		if(SelectedStatus.equalsIgnoreCase(Status[1]))
    		{
    			if(SelectedReinspDate==null || SelectedReinspDate.equals(""))
    				SelectedReinspDate=CurrentSnag.getReInspectedUnresolvedDate();
    			if(SelectedReinspDate==null || SelectedReinspDate.equals(""))
    				SelectedReinspDate=GetDate();
    			
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
        			Bitmap Img1 = decodeFile(FilePath1);
        			btn1=new ImageButton(this);
        			btn1.setImageBitmap(Img1);
        			//imgVw1.setBackgroundDrawable(btnI.getDrawable());
        	   		isImageSetFor21=true;
        			}
        			else
        			{
        				DownloadImagesInBackGround task=new DownloadImagesInBackGround();
        				task.tag=1;
        				task.imageUrl=PhotoURl21;
        				task.fromImg=2;
        				task.execute(10);
        			}
        			
        			
        		
        		}
        		
        		if(PhotoURl22!=null && PhotoURl22.length()>0)
        		{
        			String FilePath2=Environment.getExternalStorageDirectory()+File.separator+"SnagReporter/Pictures/"+PhotoURl22+".jpg";
        			File fooo = new File(FilePath2);
        			if(fooo.exists()){
        			Bitmap Img2 = decodeFile(FilePath2);
        			 btn2=new ImageButton(this);
        			btn2.setImageBitmap(Img2);
        			//imgVw2.setBackgroundDrawable(btnI.getDrawable());
        	   		isImageSetFor22=true;
        			}
        			else
        			{
        				DownloadImagesInBackGround task=new DownloadImagesInBackGround();
        				task.tag=2;
        				task.imageUrl=PhotoURl22;
        				task.fromImg=2;
        				task.execute(10);
        			}
        			
        		}
        		
        		if(PhotoURl23!=null && PhotoURl23.length()>0)
        		{
        			String FilePath2=Environment.getExternalStorageDirectory()+File.separator+"SnagReporter/Pictures/"+PhotoURl23+".jpg";
        			File fooo = new File(FilePath2);
        			if(fooo.exists()){
        			Bitmap Img2 = decodeFile(FilePath2);
        			 btn3=new ImageButton(this);
        			btn3.setImageBitmap(Img2);
        			//imgVw3.setBackgroundDrawable(btnI.getDrawable());
        	   		isImageSetFor23=true;
        			}
        			else
        			{
        				DownloadImagesInBackGround task=new DownloadImagesInBackGround();
        				task.tag=3;
        				task.imageUrl=PhotoURl23;
        				task.fromImg=2;
        				task.execute(10);
        			}
        			
        		}
    			
        		items.add(new ListItemAddDefect(null, "ReinspectionDate",SelectedReinspDate,false));
        		items.add(new ListItemAddDefectPhoto(null, "Unresolved Photos",btn1,btn2,btn3,2,true,PhotoURl21,PhotoURl22,PhotoURl23,false));
        		btn1=null;btn2=null;btn3=null;
    		}
    		else if(SelectedStatus.equalsIgnoreCase("Resolved"))
    		{	btn1=null;btn2=null;btn3=null;
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
        			Bitmap Img1 = decodeFile(FilePath1);
        			btn1=new ImageButton(this);
        			btn1.setImageBitmap(Img1);
        			//imgVw1.setBackgroundDrawable(btnI.getDrawable());
        	   		isImageSetFor31=true;
        			}
        			else
        			{
        				DownloadImagesInBackGround task=new DownloadImagesInBackGround();
        				task.tag=1;
        				task.imageUrl=PhotoURl31;
        				task.fromImg=3;
        				task.execute(10);
        			}
        		
        		}
        		
        		if(PhotoURl32!=null && PhotoURl32.length()>0){
        			String FilePath2=Environment.getExternalStorageDirectory()+File.separator+"SnagReporter/Pictures/"+PhotoURl32+".jpg";
        			File fooo = new File(FilePath2);
        			if(fooo.exists()){
        			Bitmap Img2 = decodeFile(FilePath2);
        			 btn2=new ImageButton(this);
        			btn2.setImageBitmap(Img2);
        			//imgVw2.setBackgroundDrawable(btnI.getDrawable());
        	   		isImageSetFor32=true;
        			}
        			else
        			{
        				DownloadImagesInBackGround task=new DownloadImagesInBackGround();
        				task.tag=2;
        				task.imageUrl=PhotoURl32;
        				task.fromImg=3;
        				task.execute(10);
        			}
        			
        		}
        		
        		if(PhotoURl33!=null && PhotoURl33.length()>0){
        			String FilePath2=Environment.getExternalStorageDirectory()+File.separator+"SnagReporter/Pictures/"+PhotoURl33+".jpg";
        			File fooo = new File(FilePath2);
        			if(fooo.exists()){
        			Bitmap Img2 = decodeFile(FilePath2);
        			 btn3=new ImageButton(this);
        			btn3.setImageBitmap(Img2);
        			//imgVw3.setBackgroundDrawable(btnI.getDrawable());
        	   		isImageSetFor33=true;
        			}
        			else
        			{
        				DownloadImagesInBackGround task=new DownloadImagesInBackGround();
        				task.tag=3;
        				task.imageUrl=PhotoURl33;
        				task.fromImg=3;
        				task.execute(10);
        			}
        			
        		}
    			
    			
    		items.add(new ListItemAddDefect(null, "Resolve Date",SelectedDate,false));
    		//items.add(new ListItemAddDefectPhoto(null, "Resolved Photos",btn1,btn2,btn3,3,true));
    		items.add(new ListItemAddDefectPhoto(null, "Resolved Photos",btn1,btn2,btn3,3,true,PhotoURl31,PhotoURl32,PhotoURl33,false));
    		btn1=null;btn2=null;btn3=null;
    		}
    	}
    	else if(oldStatus.equalsIgnoreCase(Status[1]))
    	{
    		btn1=null;btn2=null;btn3=null;
    		
    		if(SelectedReinspDate==null || SelectedReinspDate.equals(""))
    			SelectedReinspDate=CurrentSnag.getReInspectedUnresolvedDate();
			if(SelectedReinspDate==null || SelectedReinspDate.equals(""))
				SelectedReinspDate=GetDate();
			
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
    			Bitmap Img1 = decodeFile(FilePath1);
    			btn1=new ImageButton(this);
    			btn1.setImageBitmap(Img1);
    			//imgVw1.setBackgroundDrawable(btnI.getDrawable());
    	   		isImageSetFor21=true;
    			}
    			else
    			{
    				DownloadImagesInBackGround task=new DownloadImagesInBackGround();
    				task.tag=1;
    				task.imageUrl=PhotoURl21;
    				task.fromImg=2;
    				task.execute(10);
    			}
    			
    			
    		
    		}
    		
    		if(PhotoURl22!=null && PhotoURl22.length()>0)
    		{
    			String FilePath2=Environment.getExternalStorageDirectory()+File.separator+"SnagReporter/Pictures/"+PhotoURl22+".jpg";
    			File fooo = new File(FilePath2);
    			if(fooo.exists()){
    			Bitmap Img2 = decodeFile(FilePath2);
    			 btn2=new ImageButton(this);
    			btn2.setImageBitmap(Img2);
    			//imgVw2.setBackgroundDrawable(btnI.getDrawable());
    	   		isImageSetFor22=true;
    			}
    			else
    			{
    				DownloadImagesInBackGround task=new DownloadImagesInBackGround();
    				task.tag=2;
    				task.imageUrl=PhotoURl22;
    				task.fromImg=2;
    				task.execute(10);
    			}
    			
    		}
    		
    		if(PhotoURl23!=null && PhotoURl23.length()>0)
    		{
    			String FilePath2=Environment.getExternalStorageDirectory()+File.separator+"SnagReporter/Pictures/"+PhotoURl23+".jpg";
    			File fooo = new File(FilePath2);
    			if(fooo.exists()){
    			Bitmap Img2 = decodeFile(FilePath2);
    			 btn3=new ImageButton(this);
    			btn3.setImageBitmap(Img2);
    			//imgVw3.setBackgroundDrawable(btnI.getDrawable());
    	   		isImageSetFor23=true;
    			}
    			else
    			{
    				DownloadImagesInBackGround task=new DownloadImagesInBackGround();
    				task.tag=3;
    				task.imageUrl=PhotoURl23;
    				task.fromImg=2;
    				task.execute(10);
    			}
    			
    		}
			
    		items.add(new ListItemAddDefect(null, "ReinspectionDate",SelectedReinspDate,false));
    		items.add(new ListItemAddDefectPhoto(null, "Unresolved Photos",btn1,btn2,btn3,2,true,PhotoURl21,PhotoURl22,PhotoURl23,false));
    		btn1=null;btn2=null;btn3=null;
    		if(SelectedStatus.equalsIgnoreCase("Resolved"))
    		{
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
        			Bitmap Img1 = decodeFile(FilePath1);
        			btn1=new ImageButton(this);
        			btn1.setImageBitmap(Img1);
        			//imgVw1.setBackgroundDrawable(btnI.getDrawable());
        	   		isImageSetFor31=true;
        			}
        			else
        			{
        				DownloadImagesInBackGround task=new DownloadImagesInBackGround();
        				task.tag=1;
        				task.imageUrl=PhotoURl31;
        				task.fromImg=4;  //lastchange
        				task.execute(10);
        			}
        		
        		}
        		
        		if(PhotoURl32!=null && PhotoURl32.length()>0){
        			String FilePath2=Environment.getExternalStorageDirectory()+File.separator+"SnagReporter/Pictures/"+PhotoURl32+".jpg";
        			File fooo = new File(FilePath2);
        			if(fooo.exists()){
        			Bitmap Img2 = decodeFile(FilePath2);
        			 btn2=new ImageButton(this);
        			btn2.setImageBitmap(Img2);
        			//imgVw2.setBackgroundDrawable(btnI.getDrawable());
        	   		isImageSetFor32=true;
        			}
        			else
        			{
        				DownloadImagesInBackGround task=new DownloadImagesInBackGround();
        				task.tag=2;
        				task.imageUrl=PhotoURl32;
        				task.fromImg=4;
        				task.execute(10);
        			}
        			
        		}
        		
        		if(PhotoURl33!=null && PhotoURl33.length()>0){
        			String FilePath2=Environment.getExternalStorageDirectory()+File.separator+"SnagReporter/Pictures/"+PhotoURl33+".jpg";
        			File fooo = new File(FilePath2);
        			if(fooo.exists()){
        			Bitmap Img2 = decodeFile(FilePath2);
        			 btn3=new ImageButton(this);
        			btn3.setImageBitmap(Img2);
        			//imgVw3.setBackgroundDrawable(btnI.getDrawable());
        	   		isImageSetFor33=true;
        			}
        			else
        			{
        				DownloadImagesInBackGround task=new DownloadImagesInBackGround();
        				task.tag=3;
        				task.imageUrl=PhotoURl33;
        				task.fromImg=4;
        				task.execute(10);
        			}
        			
        		}
    			
    			
    		items.add(new ListItemAddDefect(null, "Resolve Date",SelectedDate,false));
    		//items.add(new ListItemAddDefectPhoto(null, "Resolved Photos",btn1,btn2,btn3,3,true));
    		items.add(new ListItemAddDefectPhoto(null, "Resolved Photos",btn1,btn2,btn3,3,true,PhotoURl31,PhotoURl32,PhotoURl33,false));
    		btn1=null;btn2=null;btn3=null;
    		}
    		
    	}
    	else if(oldStatus.equalsIgnoreCase("Resolved"))
    	{
    		if(CurrentSnag.getReInspectedUnresolvedDate()==null || CurrentSnag.getReInspectedUnresolvedDate().length()==0)
    		{
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
        			Bitmap Img1 = decodeFile(FilePath1);
        			btn1=new ImageButton(this);
        			btn1.setImageBitmap(Img1);
        			//imgVw1.setBackgroundDrawable(btnI.getDrawable());
        	   		isImageSetFor31=true;
        			}
        			else
        			{
        				DownloadImagesInBackGround task=new DownloadImagesInBackGround();
        				task.tag=1;
        				task.imageUrl=PhotoURl31;
        				task.fromImg=3;    //last1
        				task.execute(10);
        			}
        		
        		}
        		
        		if(PhotoURl32!=null && PhotoURl32.length()>0){
        			String FilePath2=Environment.getExternalStorageDirectory()+File.separator+"SnagReporter/Pictures/"+PhotoURl32+".jpg";
        			File fooo = new File(FilePath2);
        			if(fooo.exists()){
        			Bitmap Img2 = decodeFile(FilePath2);
        			 btn2=new ImageButton(this);
        			btn2.setImageBitmap(Img2);
        			//imgVw2.setBackgroundDrawable(btnI.getDrawable());
        	   		isImageSetFor32=true;
        			}
        			else
        			{
        				DownloadImagesInBackGround task=new DownloadImagesInBackGround();
        				task.tag=2;
        				task.imageUrl=PhotoURl32;
        				task.fromImg=3;
        				task.execute(10);
        			}
        			
        		}
        		
        		if(PhotoURl33!=null && PhotoURl33.length()>0){
        			String FilePath2=Environment.getExternalStorageDirectory()+File.separator+"SnagReporter/Pictures/"+PhotoURl33+".jpg";
        			File fooo = new File(FilePath2);
        			if(fooo.exists()){
        			Bitmap Img2 = decodeFile(FilePath2);
        			 btn3=new ImageButton(this);
        			btn3.setImageBitmap(Img2);
        			//imgVw3.setBackgroundDrawable(btnI.getDrawable());
        	   		isImageSetFor33=true;
        			}
        			else
        			{
        				DownloadImagesInBackGround task=new DownloadImagesInBackGround();
        				task.tag=3;
        				task.imageUrl=PhotoURl33;
        				task.fromImg=3;
        				task.execute(10);
        			}
        			
        		}
    			
    			
    		items.add(new ListItemAddDefect(null, "Resolve Date",SelectedDate,true));
    		//items.add(new ListItemAddDefectPhoto(null, "Resolved Photos",btn1,btn2,btn3,3,true));
    		items.add(new ListItemAddDefectPhoto(null, "Resolved Photos",btn1,btn2,btn3,3,true,PhotoURl31,PhotoURl32,PhotoURl33,true));
    		btn1=null;btn2=null;btn3=null;
    			
    		}
    		else
    		{
    			
    			if(SelectedReinspDate==null || SelectedReinspDate.equals(""))
    				SelectedReinspDate=CurrentSnag.getReInspectedUnresolvedDate();
    			if(SelectedReinspDate==null || SelectedReinspDate.equals(""))
    				SelectedReinspDate=GetDate();
    			
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
        			Bitmap Img1 = decodeFile(FilePath1);
        			btn1=new ImageButton(this);
        			btn1.setImageBitmap(Img1);
        			//imgVw1.setBackgroundDrawable(btnI.getDrawable());
        	   		isImageSetFor21=true;
        			}
        			else
        			{ //lttt
        				DownloadImagesInBackGround task=new DownloadImagesInBackGround();
        				task.tag=1;
        				task.imageUrl=PhotoURl21;
        				task.fromImg=2;
        				task.vwNo=13;
        				task.execute(10);
        			}
        			
        			
        		
        		}
        		
        		if(PhotoURl22!=null && PhotoURl22.length()>0)
        		{
        			String FilePath2=Environment.getExternalStorageDirectory()+File.separator+"SnagReporter/Pictures/"+PhotoURl22+".jpg";
        			File fooo = new File(FilePath2);
        			if(fooo.exists()){
        			Bitmap Img2 = decodeFile(FilePath2);
        			 btn2=new ImageButton(this);
        			btn2.setImageBitmap(Img2);
        			//imgVw2.setBackgroundDrawable(btnI.getDrawable());
        	   		isImageSetFor22=true;
        			}
        			else
        			{
        				DownloadImagesInBackGround task=new DownloadImagesInBackGround();
        				task.tag=2;
        				task.imageUrl=PhotoURl22;
        				task.fromImg=2;
        				task.execute(10);
        			}
        			
        		}
        		
        		if(PhotoURl23!=null && PhotoURl23.length()>0)
        		{
        			String FilePath2=Environment.getExternalStorageDirectory()+File.separator+"SnagReporter/Pictures/"+PhotoURl23+".jpg";
        			File fooo = new File(FilePath2);
        			if(fooo.exists()){
        			Bitmap Img2 = decodeFile(FilePath2);
        			 btn3=new ImageButton(this);
        			btn3.setImageBitmap(Img2);
        			//imgVw3.setBackgroundDrawable(btnI.getDrawable());
        	   		isImageSetFor23=true;
        			}
        			else
        			{
        				DownloadImagesInBackGround task=new DownloadImagesInBackGround();
        				task.tag=3;
        				task.imageUrl=PhotoURl23;
        				task.fromImg=2;
        				task.execute(10);
        			}
        			
        		}
        		//if(!(oldStatus.equalsIgnoreCase("Resolved")))
        		//items.add(new ListItemAddDefect(null, "ReinspectionDate",SelectedDate,false));
        		//else
        			items.add(new ListItemAddDefect(null, "ReinspectionDate",SelectedReinspDate,true));
        		items.add(new ListItemAddDefectPhoto(null, "Unresolved Photos",btn1,btn2,btn3,2,true,PhotoURl21,PhotoURl22,PhotoURl23,true));
        		
        		btn1=null;btn2=null;btn3=null;
        		//for resolved
        		
        		
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
        			Bitmap Img1 = decodeFile(FilePath1);
        			btn1=new ImageButton(this);
        			btn1.setImageBitmap(Img1);
        			//imgVw1.setBackgroundDrawable(btnI.getDrawable());
        	   		isImageSetFor31=true;
        			}
        			else
        			{
        				DownloadImagesInBackGround task=new DownloadImagesInBackGround();
        				task.tag=1;
        				task.imageUrl=PhotoURl31;  //last33
        				task.fromImg=4;
        				task.execute(10);
        			}
        		
        		}
        		
        		if(PhotoURl32!=null && PhotoURl32.length()>0){
        			String FilePath2=Environment.getExternalStorageDirectory()+File.separator+"SnagReporter/Pictures/"+PhotoURl32+".jpg";
        			File fooo = new File(FilePath2);
        			if(fooo.exists()){
        			Bitmap Img2 = decodeFile(FilePath2);
        			 btn2=new ImageButton(this);
        			btn2.setImageBitmap(Img2);
        			//imgVw2.setBackgroundDrawable(btnI.getDrawable());
        	   		isImageSetFor32=true;
        			}
        			else
        			{
        				DownloadImagesInBackGround task=new DownloadImagesInBackGround();
        				task.tag=2;
        				task.imageUrl=PhotoURl32;
        				task.fromImg=4;
        				task.execute(10);
        			}
        			
        		}
        		
        		if(PhotoURl33!=null && PhotoURl33.length()>0){
        			String FilePath2=Environment.getExternalStorageDirectory()+File.separator+"SnagReporter/Pictures/"+PhotoURl33+".jpg";
        			File fooo = new File(FilePath2);
        			if(fooo.exists()){
        			Bitmap Img2 = decodeFile(FilePath2);
        			 btn3=new ImageButton(this);
        			btn3.setImageBitmap(Img2);
        			//imgVw3.setBackgroundDrawable(btnI.getDrawable());
        	   		isImageSetFor33=true;
        			}
        			else
        			{
        				DownloadImagesInBackGround task=new DownloadImagesInBackGround();
        				task.tag=3;
        				task.imageUrl=PhotoURl33;
        				task.fromImg=4;
        				task.execute(10);
        			}
        			
        		}
    			
        		//if(!(oldStatus.equalsIgnoreCase("Resolved")))	
    		//items.add(new ListItemAddDefect(null, "Resolve Date",SelectedDate,false));
        		//else
        			items.add(new ListItemAddDefect(null, "Resolve Date",SelectedDate,true));
    		//items.add(new ListItemAddDefectPhoto(null, "Resolved Photos",btn1,btn2,btn3,3,true));
    		items.add(new ListItemAddDefectPhoto(null, "Resolved Photos",btn1,btn2,btn3,3,true,PhotoURl31,PhotoURl32,PhotoURl33,true));
    		
    		
    		btn1=null;btn2=null;btn3=null;
        		
    			
    			
    			
    		}
    		
    	}
    	if(SelectedStatus.equals(Status[1])|| SelectedStatus.equals("Resolved")){   //let123
    		boolean temp=false;
    		if(oldStatus.equals("Resolved"))
    			temp=true;
    		if(SelectedQCC!=null && SelectedQCC.length()>0 && !(SelectedQCC.equalsIgnoreCase("null")) && !(SelectedQCC.equalsIgnoreCase("(null)"))){
    			items.add(new ListItemAddDefect(null, "Quality Check Control",SelectedQCC,temp));
    		}
    		else{
    			items.add(new ListItemAddDefect(null, "Quality Check Control","",temp));
    		}
    		}
    	else{
    		SelectedQCC="";
    	}
    		obj=null;
    		
            
    	}
    	catch(Exception e){
    	 Log.d("Error=", ""+e.getMessage());
    	}
    }
    
    
    public void BackClick(View v){
    	finish();
    }
    
    
    
    
    public void Photo1Clicked(View v)
    {
    	try{
    		if(!(oldStatus.equalsIgnoreCase("Resolved")))
			{
    			registerForContextMenu(v);
    			strFromImg="img1";
    			CurrentIMG=(ImageView)v;
    			TAG=Integer.parseInt(v.getTag().toString());
    			if(TAG==1){
    		/*as_index=8;
    		
    		if(!isImageSetFor1)
    		{
    			as_index_selected=0;
    		}
    		else
    		{
    			as_index_selected=1;
    			
    		}*/
    			}
    			else if(TAG==2)
    			{
    			
    				as_index=11;
    				if(!(oldStatus.equalsIgnoreCase("Reinspected & Unresolved") && SelectedStatus.equalsIgnoreCase("Resolved")))
            		{
            		
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
    		else if(TAG==3)
    		{
    			
    			as_index=23;  
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
    		registerForContextMenu(v);
    		strFromImg="img2";
    		CurrentIMG=(ImageView)v;
    		TAG=Integer.parseInt(v.getTag().toString());
    		if(!(oldStatus.equalsIgnoreCase("Resolved")))
			{
    			if(TAG==1){
    		/*as_index=8;
    		if(!isImageSetFor2)
    		{
    			as_index_selected=0;
    		}
    		else
    		{
    			as_index_selected=1;
    			
    		}*/
    			}
    			else if(TAG==2)
    			{
    			
    				as_index=11;
    				if(!(oldStatus.equalsIgnoreCase("Reinspected & Unresolved") && SelectedStatus.equalsIgnoreCase("Resolved")))
            		{
            		
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
    			as_index=23;
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
    		registerForContextMenu(v);
    		strFromImg="img3";
    		CurrentIMG=(ImageView)v;
    		TAG=Integer.parseInt(v.getTag().toString());
    		if(!(oldStatus.equalsIgnoreCase("Resolved")))
			{
    			if(TAG==1){
    		/*as_index=8;
    		
    		if(!isImageSetFor3)
    		{
    			as_index_selected=0;
    		}
    		else
    		{
    			as_index_selected=1;
    			
    		}*/
    			}
    			else if(TAG==2)
    			{
        		
    			
    				as_index=11;
    				if(!(oldStatus.equalsIgnoreCase("Reinspected & Unresolved") && SelectedStatus.equalsIgnoreCase("Resolved")))
            		{
            		
        		
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
    			else
    			{
    				as_index=23;
        		
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
	if( as_index==11 || as_index==23)
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

	else if(as_index==1){
		menu.setHeaderTitle("Select Area");
		for(int i=0;i<AreaList.length;i++){
			menu.add(0, v.getId(), 0, ""+AreaList[i]);
		}
	}
	else if(as_index==2){
		menu.setHeaderTitle("Select Trades");
		for(int i=0;i<arrJobType.length;i++){
			menu.add(0, v.getId(), 0, ""+arrJobType[i].getJobType());
		}
	}
	else if(as_index==3){
		if(arrFaultType!=null && arrFaultType.length>0){
		menu.setHeaderTitle("Select InspectionGroup");
		for(int i=0;i<arrFaultType.length;i++){
			menu.add(0, v.getId(), 0, ""+arrFaultType[i].getJobType());
		}
		}
	}
	else if(as_index==13)
	{
		list.setSelection(8);
		
		if(oldStatus.equalsIgnoreCase("pending"))
		{
			menu.setHeaderTitle("Select Status");
			for(int i=0;i<Status.length;i++)
			{
				menu.add(0, v.getId(), 0, ""+Status[i]);
			}
		}
		else if(oldStatus.equalsIgnoreCase("Reinspected & Unresolved"))
		{
			menu.setHeaderTitle("Select Status");
			for(int i=1;i<Status.length;i++)
			{
				menu.add(0, v.getId(), 0, ""+Status[i]);
			}
			
			
		}

	}
	else if(as_index==101){
		menu.setHeaderTitle("Select QCC");
		for(int i=0;i<arrQCC.length;i++){
			menu.add(0, v.getId(), 0, ""+arrQCC[i].getQCCStatus());
		}
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
	
	try{
	if( as_index==11 || as_index==23)
	{
	 if(item.getTitle()=="Choose from Library")
	 {
		 Intent intent=new Intent();
		 intent.setType("image/*");
		 intent.setAction(Intent.ACTION_GET_CONTENT);
		 intent.addCategory(Intent.CATEGORY_OPENABLE);
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
	 }
	 else if(item.getTitle()=="Remove Photo"){
		 if(strFromImg.equalsIgnoreCase("img1")){
			 View v=null;
			 int wantedPosition=0;
			 if(TAG==1)
			 {
				  wantedPosition = 8; // Whatever position you're looking for
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
			 else if(TAG==2)
			 {
				  wantedPosition = 15; // Whatever position you're looking for 14
					int firstPosition = list.getFirstVisiblePosition() - list.getHeaderViewsCount(); // This is the same as child #0
					int wantedChild = wantedPosition - firstPosition;
					// Say, first visible position is 8, you want position 10, wantedChild will now be 2
					// So that means your view is child #2 in the ViewGroup:
					if (wantedChild < 0 || wantedChild >= list.getChildCount()) {
					  Log.d("Child Not Available", "");
					}
					else
					{
						RemovedPhotoURL=PhotoURl21;
						 v=list.getChildAt(wantedChild);
						 PhotoURl21="";
						 isImageSetFor21=false;
					}
				 
				 }
			 else if(TAG==3)
			 {
				 	
				 	 wantedPosition = 0; // Whatever position you're looking for
				 	if(oldStatus.equalsIgnoreCase("pending"))
				 	{
				 		 wantedPosition = 15; //14
				 	}
				 	else
				 	{
				 		wantedPosition = 17;  //16
				 	}
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
			 
			 ImageButton btnI=new ImageButton(this);
			 btnI.setImageResource(R.drawable.image_add);
			 ListItemAddDefectPhoto itemi=(ListItemAddDefectPhoto)items.get(wantedPosition);
			 itemi.url1=btnI;
			 items.set(wantedPosition,itemi);
			 adapter.notifyDataSetChanged();
			 }
		 }
		 else if(strFromImg.equalsIgnoreCase("img2")){
			 View v=null;
			 int wantedPosition=0;
			 if(TAG==1){
				  wantedPosition = 8; // Whatever position you're looking for
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
			 else if(TAG==2)
			 {
				  wantedPosition = 15; // Whatever position you're looking for 15
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
				  wantedPosition = 0; // Whatever position you're looking for
				 	if(oldStatus.equalsIgnoreCase("pending"))
				 	{
				 		 wantedPosition = 15; //14
				 	}
				 	else
				 	{
				 		wantedPosition = 17;  //16
				 	}
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
			 
			 ImageButton btnI=new ImageButton(this);
			 btnI.setImageResource(R.drawable.image_add);
			 ListItemAddDefectPhoto itemi=(ListItemAddDefectPhoto)items.get(wantedPosition);
			 itemi.url2=btnI;
			 items.set(wantedPosition,itemi);
			 adapter.notifyDataSetChanged();
			 }
		 }
		 else if(strFromImg.equalsIgnoreCase("img3")){
			 View v=null;
			 int wantedPosition=0;
			 if(TAG==1){
				 
				  wantedPosition = 8; // Whatever position you're looking for
					int firstPosition = list.getFirstVisiblePosition() - list.getHeaderViewsCount(); // This is the same as child #0
					int wantedChild = wantedPosition - firstPosition;
					// Say, first visible position is 8, you want position 10, wantedChild will now be 2
					// So that means your view is child #2 in the ViewGroup:
					if (wantedChild < 0 || wantedChild >= list.getChildCount()) {
					  Log.d("Child Not Available", "");
					}
					else
					{
						RemovedPhotoURL=PhotoURl3;
						v=list.getChildAt(wantedChild);
						 PhotoURl3="";
						 isImageSetFor3=false;
					}
				 
			 }
			 else if(TAG==2){
				 
				 	 wantedPosition = 15; // Whatever position you're looking for 15
					int firstPosition = list.getFirstVisiblePosition() - list.getHeaderViewsCount(); // This is the same as child #0
					int wantedChild = wantedPosition - firstPosition;
					// Say, first visible position is 8, you want position 10, wantedChild will now be 2
					// So that means your view is child #2 in the ViewGroup:
					if (wantedChild < 0 || wantedChild >= list.getChildCount()) {
					  Log.d("Child Not Available", "");
					}
					else
					{
						RemovedPhotoURL=PhotoURl23;
						v=list.getChildAt(wantedChild);
						 PhotoURl23="";
						 isImageSetFor23=false;
					}
				 
			 }
			 else if(TAG==3){
				 
				  wantedPosition = 0; // Whatever position you're looking for
				 	if(oldStatus.equalsIgnoreCase("pending"))
				 	{
				 		 wantedPosition = 15;  //14
				 	}
				 	else
				 	{
				 		wantedPosition = 17;   //16
				 	}
					int firstPosition = list.getFirstVisiblePosition() - list.getHeaderViewsCount(); // This is the same as child #0
					int wantedChild = wantedPosition - firstPosition;
					// Say, first visible position is 8, you want position 10, wantedChild will now be 2
					// So that means your view is child #2 in the ViewGroup:
					if (wantedChild < 0 || wantedChild >= list.getChildCount()) {
					  Log.d("Child Not Available", "");
					}
					else
					{
						RemovedPhotoURL=PhotoURl33;
						 v=list.getChildAt(wantedChild);
						 PhotoURl33="";
						 isImageSetFor33=false;
					}
				 
			 }
			 if(v!=null){
			 ImageButton i3=(ImageButton)v.findViewById(R.id.row_cell_addDefectPhoto_img3);
			 i3.setBackgroundDrawable(getResources().getDrawable(R.drawable.image_add));
			 
			 ImageButton btnI=new ImageButton(this);
			 btnI.setImageResource(R.drawable.image_add);
			 ListItemAddDefectPhoto itemi=(ListItemAddDefectPhoto)items.get(wantedPosition);
			 itemi.url3=btnI;
			 items.set(wantedPosition,itemi);
			 adapter.notifyDataSetChanged();
			 }
		 }
		 
		 String FilePath=Environment.getExternalStorageDirectory()+File.separator+"SnagReporter/Pictures/"+RemovedPhotoURL+".jpg";
		 File file = new File(FilePath);
		// file.delete();
		 
		 RemovedPhotoURL="";
		 
		 return true;
	 }
	 else if(item.getTitle()=="View Image"){
		 Intent i=new Intent(EditDefectChageStatus.this,com.snagreporter.ViewImagePage.class);
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
		 
		 return true;
	 }
	}
	else if(as_index==1){
		int wantedPosition = 1; // Whatever position you're looking for
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
	else if(as_index==2){
		int wantedPosition = 2; // Whatever position you're looking for
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
			SelectedJobType=item.getTitle().toString();
			
			int index=0;
			for(int i=0;i<arrJobType.length;i++){
				if(SelectedJobType.equalsIgnoreCase(arrJobType[i].getJobType())){
					index=i;
					break;
				}
			}
			FMDBDatabaseAccess db=new FMDBDatabaseAccess(EditDefectChageStatus.this);
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

	}
	else if(as_index==3){
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
	else if(as_index==13)
	{
	
	   
		
//		
		// int x=list.getChildCount();
	    
//		list.post(new Runnable(){
//			  public void run() {
//			    list.setSelection(list.getCount() - 1);
//			  }});
		
		View v;
		list.setSelection(9);
		int wantedPosition = 9; // Whatever position you're looking for
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
		CurrentSnag.setSnagStatus(SelectedStatus);
		
		if(SelectedStatus.equalsIgnoreCase(Status[0])){
			if(list.getCount()>9){
				
				items.clear();
				items = new ArrayList<Item>();
			    items.add(new Header2(null, "SNAG DETAILS",false,false,false,true));
			    
				populateData();
			    
				adapter = new TwoTextArrayAdapter(this, items);
		        list.setAdapter(adapter);
		        
		        list.setSelection(list.getAdapter().getCount() - 1);
			}
			

		}
		else if(SelectedStatus.equalsIgnoreCase(Status[1])){
			if(list.getCount()>10){
				items.remove(10);
				items.remove(10);
				
				
			}
			
			items.clear();
			items = new ArrayList<Item>();
		    items.add(new Header2(null, "SNAG DETAILS",false,false,false,true));
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
				items.remove(10);  //modified
				//int countw=items.size();
				
				items.remove(10);
				
			}
			
			items.clear();
			items = new ArrayList<Item>();
		    items.add(new Header2(null, "SNAG DETAILS",false,false,false,true));
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
	else if(as_index==101){

		int wantedPosition = list.getCount()-1; // Whatever position you're looking for
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
			SelectedQCC=item.getTitle().toString();
			
			ListItemAddDefect itemi=(ListItemAddDefect)items.get(wantedPosition);
			itemi.str2=SelectedQCC;
			items.set(wantedPosition,itemi);
			adapter.notifyDataSetChanged();
		}
		
		}
	
	}
	 
	}
catch(Exception e)
{
	Log.d("Exception",""+e.getMessage());
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
               
            //BtnYourImg = BitmapFactory.decodeFile(filePath);
            //showImage();
            if(strFromImg==null || strFromImg.length()==0){
            	if(strGetValues!=null)
            		strFromImg=strGetValues[9];
            }

          
            Intent i=new Intent(this,com.snagreporter.PhotoView.class);
            i.putExtra("filePath", filePath);
           // i.putExtra("BitmapImage", BtnYourImg);
           i.putExtra("strFromImg", strFromImg);
           if(strFromImg.equalsIgnoreCase("img1"))
           {
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
        }
        catch(Exception e)
        {
        	Log.d("Exception",""+e.getMessage());
        }
           break;
           
        }
    case 2:
    {
   	 
   	 if(resultCode==10 && imageReturnedIntent!=null)
   	 {
   		 try{
   	// Log.d("strFromImgvw", "strFromImgvw");
   		//Toast.makeText(EditDefectChageStatus.this, "Came here 10", Toast.LENGTH_LONG).show();
   	 strFromImgvw=imageReturnedIntent.getExtras().getString("strFromImgvw");
   	 strFilePath=imageReturnedIntent.getExtras().getString("strFilePath");
   	 String retUUID=imageReturnedIntent.getExtras().getString("UUID");
   	//BtnImageBmp=(Bitmap)getIntent().getParcelableExtra("btnBitmap");
   	 BtnImageBmp=decodeFile(strFilePath);
   	if(TAG==0){
 		 if(strGetValues!=null){
 			 TAG=Integer.parseInt(strGetValues[13].toString());
 		 }
 	 }

   	 if(strFromImgvw.equalsIgnoreCase("img1"))
   	 {
   		
   		
   		View v=null;
   		if(TAG==1){
   			int wantedPosition = 8; // Whatever position you're looking for
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
				 ImageButton i1=(ImageButton)v.findViewById(R.id.row_cell_addDefectPhoto_img1);
		   		 i1.setBackgroundDrawable(btnI.getDrawable());
		   		 
		   		ListItemAddDefectPhoto itemi=(ListItemAddDefectPhoto)items.get(wantedPosition);
				itemi.url1=btnI;
				items.set(wantedPosition,itemi);
				adapter.notifyDataSetChanged();
			}
   			
   		}
   		else if(TAG==2){
   			int wantedPosition = 15; // Whatever position you're looking for 14
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
				 ImageButton i1=(ImageButton)v.findViewById(R.id.row_cell_addDefectPhoto_img1);
		   		 i1.setBackgroundDrawable(btnI.getDrawable());
		   		 
		   		ListItemAddDefectPhoto itemi=(ListItemAddDefectPhoto)items.get(wantedPosition);
				itemi.url1=btnI;
				items.set(wantedPosition,itemi);
				adapter.notifyDataSetChanged();
			}
   			
   		}
   		else if(TAG==3)
   		{
   			int wantedPosition=0;
   			if(oldStatus.equalsIgnoreCase("pending"))
   			{
   				 wantedPosition = 15; // Whatever position you're looking for 14
   			}
   			else
   			{
   				wantedPosition = 17;  //16
   			}
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
				 ImageButton i1=(ImageButton)v.findViewById(R.id.row_cell_addDefectPhoto_img1);
		   		 i1.setBackgroundDrawable(btnI.getDrawable());
		   		 
		   		ListItemAddDefectPhoto itemi=(ListItemAddDefectPhoto)items.get(wantedPosition);
				itemi.url1=btnI;
				items.set(wantedPosition,itemi);
				adapter.notifyDataSetChanged();
			}
   			
   		}
		
		
   		
   	 }
   	 else if(strFromImgvw.equalsIgnoreCase("img2"))
   	 {
   		View v=null;
   		if(TAG==1){
   			int wantedPosition = 8; // Whatever position you're looking for
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
   			 ImageButton i2=(ImageButton)v.findViewById(R.id.row_cell_addDefectPhoto_img2);
   	   		 i2.setBackgroundDrawable(btnI.getDrawable());
   	   		 
   	   	ListItemAddDefectPhoto itemi=(ListItemAddDefectPhoto)items.get(wantedPosition);
		itemi.url2=btnI;
		items.set(wantedPosition,itemi);
		adapter.notifyDataSetChanged();
			}
   		}
   		else if(TAG==2){
   			int wantedPosition = 15; // Whatever position you're looking for 14
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
        		 ImageButton i2=(ImageButton)v.findViewById(R.id.row_cell_addDefectPhoto_img2);
           		 i2.setBackgroundDrawable(btnI.getDrawable());
           		 
           		ListItemAddDefectPhoto itemi=(ListItemAddDefectPhoto)items.get(wantedPosition);
        		itemi.url2=btnI;
        		items.set(wantedPosition,itemi);
        		adapter.notifyDataSetChanged();
			}
        }
   		else if(TAG==3){
   			int wantedPosition=0;
   			if(oldStatus.equalsIgnoreCase("pending"))
   			{
   				 wantedPosition = 15; // Whatever position you're looking for 14
   			}
   			else
   			{
   				wantedPosition = 17;   //16
   			}
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
    		 ImageButton i2=(ImageButton)v.findViewById(R.id.row_cell_addDefectPhoto_img2);
       		 i2.setBackgroundDrawable(btnI.getDrawable());
       		 
       		ListItemAddDefectPhoto itemi=(ListItemAddDefectPhoto)items.get(wantedPosition);
    		itemi.url2=btnI;
    		items.set(wantedPosition,itemi);
    		adapter.notifyDataSetChanged();
			}
       }
   		
		
   		
   	 }
   	 else if(strFromImgvw.equalsIgnoreCase("img3"))
   	 {
   		 View v=null;
   		 if(TAG==1){
   			int wantedPosition = 8; // Whatever position you're looking for
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
   			 ImageButton i3=(ImageButton)v.findViewById(R.id.row_cell_addDefectPhoto_img3);
   	   		 i3.setBackgroundDrawable(btnI.getDrawable());
   	   		 
   	   	ListItemAddDefectPhoto itemi=(ListItemAddDefectPhoto)items.get(wantedPosition);
		itemi.url3=btnI;
		items.set(wantedPosition,itemi);
		adapter.notifyDataSetChanged();
			}
   		 }
   		 else if(TAG==2){
   			int wantedPosition = 15; // Whatever position you're looking for 14
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
  			 ImageButton i3=(ImageButton)v.findViewById(R.id.row_cell_addDefectPhoto_img3);
  	   		 i3.setBackgroundDrawable(btnI.getDrawable());
  	   		 
  	   	ListItemAddDefectPhoto itemi=(ListItemAddDefectPhoto)items.get(wantedPosition);
		itemi.url3=btnI;
		items.set(wantedPosition,itemi);
		adapter.notifyDataSetChanged();
			}
   		 }
   		 else if(TAG==3){
   			int wantedPosition=0;
   			if(oldStatus.equalsIgnoreCase("pending"))
   			{
   				 wantedPosition = 15; // Whatever position you're looking for 14
   			}
   			else
   			{
   				wantedPosition = 17;  //16
   			}
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
  			 ImageButton i3=(ImageButton)v.findViewById(R.id.row_cell_addDefectPhoto_img3);
  	   		 i3.setBackgroundDrawable(btnI.getDrawable());
  	   		 
  	   	ListItemAddDefectPhoto itemi=(ListItemAddDefectPhoto)items.get(wantedPosition);
		itemi.url3=btnI;
		items.set(wantedPosition,itemi);
		adapter.notifyDataSetChanged();
			}
   		 }
   			 
		
   		
   	 }
   	//Handler handler = new Handler();
 	 //handler.postDelayed(timedTask, 1000);
   	 //Log.d("strFromImgvw",""+strFromImgvw);
   	}
     catch(Exception e)
     {
     	Log.d("Exception",""+e.getMessage());
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
        			strFromImg=strGetValues[9];
        			f = new File(strGetValues[12]);
        			TAG=Integer.parseInt(strGetValues[13].toString());
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
        	Log.d("Exception",""+e.getMessage());
        }
   	/* mBitmap = (Bitmap) imageReturnedIntent.getExtras().get("data");
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
	 else if(resultCode==10005){
	 		
	 		finish();
	 	}
	 }
	 catch(Exception e){
		 Log.d("Error=", ""+e.getMessage());
		 Toast.makeText(EditDefectChageStatus.this, "Error="+e.getMessage(), Toast.LENGTH_LONG).show();
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
		   			int wantedPosition = 8; // Whatever position you're looking for
					int firstPosition = list.getFirstVisiblePosition() - list.getHeaderViewsCount(); // This is the same as child #0
					int wantedChild = wantedPosition - firstPosition;
					// Say, first visible position is 8, you want position 10, wantedChild will now be 2
					// So that means your view is child #2 in the ViewGroup:
					if (wantedChild < 0 || wantedChild >= list.getChildCount()) {
					  Log.d("Child Not Available", "");
					}
					else{
						
						v=list.getChildAt(wantedChild);
						ImageButton btnI=new ImageButton(EditDefectChageStatus.this);
						btnI.setImageBitmap(BtnImageBmp);
						 ImageButton i1=(ImageButton)v.findViewById(R.id.row_cell_addDefectPhoto_img1);
				   		 i1.setBackgroundDrawable(btnI.getDrawable());
					}
		   			
		   		}
		   		else if(TAG==2)
		   		{
		   			int wantedPosition = 15; // Whatever position you're looking for 14
					int firstPosition = list.getFirstVisiblePosition() - list.getHeaderViewsCount(); // This is the same as child #0
					int wantedChild = wantedPosition - firstPosition;
					// Say, first visible position is 8, you want position 10, wantedChild will now be 2
					// So that means your view is child #2 in the ViewGroup:
					if (wantedChild < 0 || wantedChild >= list.getChildCount()) {
					  Log.d("Child Not Available", "");
					}
					else{
						
						v=list.getChildAt(wantedChild);
						ImageButton btnI=new ImageButton(EditDefectChageStatus.this);
						btnI.setImageBitmap(BtnImageBmp);
						 ImageButton i1=(ImageButton)v.findViewById(R.id.row_cell_addDefectPhoto_img1);
				   		 i1.setBackgroundDrawable(btnI.getDrawable());
					}
		   			
		   		}
		   		else if(TAG==3)
		   		{
		   			int wantedPosition =0;  // Whatever position you're looking for
		   			
		   			if(oldStatus.equalsIgnoreCase("pending"))
		   			{
		   				wantedPosition = 15; //14
		   			}
		   			else
		   			{
		   				wantedPosition = 17;  //16
		   			}
		   			
					int firstPosition = list.getFirstVisiblePosition() - list.getHeaderViewsCount(); // This is the same as child #0
					int wantedChild = wantedPosition - firstPosition;
					// Say, first visible position is 8, you want position 10, wantedChild will now be 2
					// So that means your view is child #2 in the ViewGroup:
					if (wantedChild < 0 || wantedChild >= list.getChildCount()) {
					  Log.d("Child Not Available", "");
					}
					else{
						
						v=list.getChildAt(wantedChild);
						ImageButton btnI=new ImageButton(EditDefectChageStatus.this);
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
		   			int wantedPosition = 8; // Whatever position you're looking for
					int firstPosition = list.getFirstVisiblePosition() - list.getHeaderViewsCount(); // This is the same as child #0
					int wantedChild = wantedPosition - firstPosition;
					// Say, first visible position is 8, you want position 10, wantedChild will now be 2
					// So that means your view is child #2 in the ViewGroup:
					if (wantedChild < 0 || wantedChild >= list.getChildCount()) {
					  Log.d("Child Not Available", "");
					}
					else{
		   			
		   			v=list.getChildAt(wantedChild);
		   			ImageButton btnI=new ImageButton(EditDefectChageStatus.this);
					btnI.setImageBitmap(BtnImageBmp);
					 ImageButton i2=(ImageButton)v.findViewById(R.id.row_cell_addDefectPhoto_img2);
			   		 i2.setBackgroundDrawable(btnI.getDrawable());
					}
		   		}
		   		else if(TAG==2){
		   			int wantedPosition = 15; // Whatever position you're looking for 14
					int firstPosition = list.getFirstVisiblePosition() - list.getHeaderViewsCount(); // This is the same as child #0
					int wantedChild = wantedPosition - firstPosition;
					// Say, first visible position is 8, you want position 10, wantedChild will now be 2
					// So that means your view is child #2 in the ViewGroup:
					if (wantedChild < 0 || wantedChild >= list.getChildCount()) {
					  Log.d("Child Not Available", "");
					}
					else{
		      		
		        		v=list.getChildAt(wantedChild);
		        		ImageButton btnI=new ImageButton(EditDefectChageStatus.this);
						btnI.setImageBitmap(BtnImageBmp);
						 ImageButton i2=(ImageButton)v.findViewById(R.id.row_cell_addDefectPhoto_img2);
				   		 i2.setBackgroundDrawable(btnI.getDrawable());
					}
		        }
		   		else if(TAG==3)
		   		{
		   			int wantedPosition =0;  // Whatever position you're looking for
		   			
		   			if(oldStatus.equalsIgnoreCase("pending"))
		   			{
		   				wantedPosition = 15; //14
		   			}
		   			else
		   			{
		   				wantedPosition = 17;  //16
		   			}
					int firstPosition = list.getFirstVisiblePosition() - list.getHeaderViewsCount(); // This is the same as child #0
					int wantedChild = wantedPosition - firstPosition;
					// Say, first visible position is 8, you want position 10, wantedChild will now be 2
					// So that means your view is child #2 in the ViewGroup:
					if (wantedChild < 0 || wantedChild >= list.getChildCount()) {
					  Log.d("Child Not Available", "");
					}
					else{
		     		 
		       		v=list.getChildAt(wantedChild);
		       		ImageButton btnI=new ImageButton(EditDefectChageStatus.this);
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
		   			int wantedPosition = 8; // Whatever position you're looking for
					int firstPosition = list.getFirstVisiblePosition() - list.getHeaderViewsCount(); // This is the same as child #0
					int wantedChild = wantedPosition - firstPosition;
					// Say, first visible position is 8, you want position 10, wantedChild will now be 2
					// So that means your view is child #2 in the ViewGroup:
					if (wantedChild < 0 || wantedChild >= list.getChildCount()) {
					  Log.d("Child Not Available", "");
					}
					else{
		   			 
		   			 v=list.getChildAt(wantedChild);
		   			ImageButton btnI=new ImageButton(EditDefectChageStatus.this);
					btnI.setImageBitmap(BtnImageBmp);
					 ImageButton i3=(ImageButton)v.findViewById(R.id.row_cell_addDefectPhoto_img3);
			   		 i3.setBackgroundDrawable(btnI.getDrawable());
					}
		   		 }
		   		 else if(TAG==2){
		   			int wantedPosition = 15; // Whatever position you're looking for 14
					int firstPosition = list.getFirstVisiblePosition() - list.getHeaderViewsCount(); // This is the same as child #0
					int wantedChild = wantedPosition - firstPosition;
					// Say, first visible position is 8, you want position 10, wantedChild will now be 2
					// So that means your view is child #2 in the ViewGroup:
					if (wantedChild < 0 || wantedChild >= list.getChildCount()) {
					  Log.d("Child Not Available", "");
					}
					else{
		   			
		  			 v=list.getChildAt(wantedChild);
		  			ImageButton btnI=new ImageButton(EditDefectChageStatus.this);
					btnI.setImageBitmap(BtnImageBmp);
					 ImageButton i3=(ImageButton)v.findViewById(R.id.row_cell_addDefectPhoto_img3);
			   		 i3.setBackgroundDrawable(btnI.getDrawable());
					}
		   		 }
		   		 else if(TAG==3){
		   			 
		   			 int wantedPosition =0;  // Whatever position you're looking for
		   			
		   			if(oldStatus.equalsIgnoreCase("pending"))
		   			{
		   				wantedPosition = 15; //14
		   			}
		   			else
		   			{
		   				wantedPosition = 17;  //16
		   			}
					int firstPosition = list.getFirstVisiblePosition() - list.getHeaderViewsCount(); // This is the same as child #0
					int wantedChild = wantedPosition - firstPosition;
					// Say, first visible position is 8, you want position 10, wantedChild will now be 2
					// So that means your view is child #2 in the ViewGroup:
					if (wantedChild < 0 || wantedChild >= list.getChildCount()) {
					  Log.d("Child Not Available", "");
					}
					else{
		   			
		  			 v=list.getChildAt(wantedChild);
		  			ImageButton btnI=new ImageButton(EditDefectChageStatus.this);
					btnI.setImageBitmap(BtnImageBmp);
					 ImageButton i3=(ImageButton)v.findViewById(R.id.row_cell_addDefectPhoto_img3);
			   		 i3.setBackgroundDrawable(btnI.getDrawable());
					}
		   		 }
		   			 
				
		   		
		   	 }
		}
        catch(Exception e)
        {
        	Log.d("Exception",""+e.getMessage());
        }
		}
	  }
};


	public void UpdateClick(View v){
	try{
		
		
		 if(!oldStatus.equalsIgnoreCase("Resolved"))
		 {
			 SnagMaster obj=new SnagMaster();
				obj.setID(CurrentSnag.getID());
				obj.setSnagType(SelectedJobType);
				obj.setSnagDetails(SelectedJobDetails);
				obj.setFaultType(SelectedFaultType);
				obj.setExpectedInspectionDate(""+ExpectedDate);
				
				obj.setPictureURL1(PhotoURl1);
				obj.setPictureURL2(PhotoURl2);
				obj.setPictureURL3(PhotoURl3);

				if(isAptmt){
					obj.setApartmentID(CurrentAPT.getID());
					obj.setApartment(CurrentAPT.getApartmentNo());
				}
				else if(isExtArea)
				{
					obj.setApartmentID("");
					obj.setApartment("");
				}
				else if(isExtFloorArea)
				{
					obj.setApartmentID("");
					obj.setApartment("");
				}
				else{
					obj.setApartmentID(CurrentSFA.getID());
					obj.setApartment(CurrentSFA.getAreaName());
				}
				
				
				
				obj.setAptAreaName(SelectedArea);
				obj.setReportDate(CurrentSnag.getReportDate().toString());
				obj.setSnagStatus(SelectedStatus);
					
					if(!obj.getSnagStatus().equalsIgnoreCase(""+Status[0])){
						
						if(obj.getSnagStatus().equalsIgnoreCase(""+Status[1]) ||   oldStatus.equalsIgnoreCase(""+Status[1])){
						obj.setReInspectedUnresolvedDate(""+SelectedReinspDate);
						obj.setReInspectedUnresolvedDatePictureURL1(""+PhotoURl21);
						obj.setReInspectedUnresolvedDatePictureURL2(""+PhotoURl22);
						obj.setReInspectedUnresolvedDatePictureURL3(""+PhotoURl23);
						}
						else{
							obj.setReInspectedUnresolvedDate("");
							obj.setReInspectedUnresolvedDatePictureURL1("");
							obj.setReInspectedUnresolvedDatePictureURL2("");
							obj.setReInspectedUnresolvedDatePictureURL3("");
						}
						
						if(obj.getSnagStatus().equalsIgnoreCase("Resolved")){
						obj.setResolveDate(""+SelectedDate);
						obj.setResolveDatePictureURL1(""+PhotoURl31);
						obj.setResolveDatePictureURL2(""+PhotoURl32);
						obj.setResolveDatePictureURL3(""+PhotoURl33);
						}
						else{
							obj.setResolveDate("");
							obj.setResolveDatePictureURL1("");
							obj.setResolveDatePictureURL2("");
							obj.setResolveDatePictureURL3("");
						}
					}
					else{
						obj.setReInspectedUnresolvedDate("");
						obj.setReInspectedUnresolvedDatePictureURL1("");
						obj.setReInspectedUnresolvedDatePictureURL2("");
						obj.setReInspectedUnresolvedDatePictureURL3("");
						obj.setResolveDate("");
						obj.setResolveDatePictureURL1("");
						obj.setResolveDatePictureURL2("");
						obj.setResolveDatePictureURL3("");
					}
					
					obj.setCost(Double.parseDouble(selectedCost));
					obj.setCostTo(costTo);
					obj.setSnagPriority(SnagPriority);
					
					obj.setQCC(SelectedQCC);
					
					UpdatedSnag=obj;
					
					
//					if(UpdatedSnag.getSnagStatus().equals(Status[1]) || UpdatedSnag.getSnagStatus().equals(Status[2])){
//						Intent i=new Intent(EditDefectChageStatus.this,com.snagreporter.AddSnagCheckList.class);
//						i.putExtra("UpdatedSnag", UpdatedSnag);
//						//i.putExtra("Floor", CurrentAPT);
//						i.putExtra("Apt", CurrentAPT);
//						startActivityForResult(i,10002);
		//
//					}
//					else
//					{
						UploadDataToWeb task=new UploadDataToWeb();
						task.execute(10);
						finish();
					//}
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
	protected class UploadDataToWeb extends AsyncTask<Integer , Integer, Void> {
    	ProgressDialog mProgressDialog = new ProgressDialog(EditDefectChageStatus.this);
    	JSONObject jObject;
    	String output="";
    	SnagMaster obj=new SnagMaster();
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
        		
        		
        		obj.setID(CurrentSnag.getID());
        		obj.setSnagType(SelectedJobType);
        		obj.setSnagDetails(SelectedJobDetails);
        		obj.setFaultType(SelectedFaultType);
        		obj.setExpectedInspectionDate(""+ExpectedDate);
        		
        		obj.setPictureURL1(PhotoURl1);
        		obj.setPictureURL2(PhotoURl2);
        		obj.setPictureURL3(PhotoURl3);

        		if(isAptmt){
        			obj.setApartmentID(CurrentAPT.getID());
        			obj.setApartment(CurrentAPT.getApartmentNo());
        		}
        		else{
        			obj.setApartmentID(CurrentSFA.getID());
        			obj.setApartment(CurrentSFA.getAreaName());
        		}
        		
        		
        		
        		obj.setAptAreaName(SelectedArea);
        		obj.setReportDate(CurrentSnag.getReportDate().toString());
        		obj.setSnagStatus(SelectedStatus);
        			
        			
        			obj.setReInspectedUnresolvedDate(""+SelectedReinspDate);
        			obj.setReInspectedUnresolvedDatePictureURL1(""+PhotoURl21);
        			obj.setReInspectedUnresolvedDatePictureURL2(""+PhotoURl22);
        			obj.setReInspectedUnresolvedDatePictureURL3(""+PhotoURl23);
        			obj.setResolveDate(""+SelectedDate);
        			obj.setResolveDatePictureURL1(""+PhotoURl31);
        			obj.setResolveDatePictureURL2(""+PhotoURl32);
        			obj.setResolveDatePictureURL3(""+PhotoURl33);
        			obj.setCost(Double.parseDouble(selectedCost));
        			obj.setCostTo(costTo);
        			obj.setSnagPriority(SnagPriority);
        			
        			obj.setQCC(SelectedQCC);
        		
        		
        		
        		
        		
        		if(isOnline){
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
	    		    
	    		    
	    		    String CoumnNames="SnagType~SnagDetails~FaultType~ExpectedInspectionDate~PictureURL1~PictureURL2~PictureURL3~ApartmentID~Apartment~AptAreaName~ReportDate~SnagStatus~ReInspectedUnresolvedDate~ReInspectedUnresolvedDatePictureURL1~ReInspectedUnresolvedDatePictureURL2~ReInspectedUnresolvedDatePictureURL3~ResolveDate~ResolveDatePictureURL1~ResolveDatePictureURL2~ResolveDatePictureURL3~Cost~CostTo~PriorityLevel~QCC";
	    		    String Values=""+obj.getSnagType()+"~"+obj.getSnagDetails()+"~"+obj.getFaultType()+"~"+obj.getExpectedInspectionDate()+"~"+obj.getPictureURL1()+"~"+obj.getPictureURL2()+"~"+obj.getPictureURL3()+"~"+obj.getApartmentID()+"~"+obj.getApartment()+"~"+obj.getAptAreaName()+"~"+obj.getReportDate()+"~"+obj.getSnagStatus()+"~"+obj.getReInspectedUnresolvedDate()+"~"+obj.getReInspectedUnresolvedDatePictureURL1()+"~"+obj.getReInspectedUnresolvedDatePictureURL2()+"~"+obj.getReInspectedUnresolvedDatePictureURL3()+"~"+obj.getResolveDate()+"~"+obj.getResolveDatePictureURL1()+"~"+obj.getResolveDatePictureURL2()+"~"+obj.getResolveDatePictureURL3()+"~"+obj.getCost()+"~"+obj.getCostTo()+"~"+obj.getSnagPriority()+"~"+obj.getQCC();
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
        //	if(mProgressDialog.isShowing())
        //		mProgressDialog.dismiss();
//        	SnagMaster obj=new SnagMaster();
//    		
//    		obj.setID(CurrentSnag.getID());
//    		obj.setSnagType(SelectedJobType);
//    		obj.setSnagDetails(SelectedJobDetails);
//    		obj.setFaultType(SelectedFaultType);
//    		obj.setExpectedInspectionDate(""+ExpectedDate);
//    		
//    		obj.setPictureURL1(PhotoURl1);
//    		obj.setPictureURL2(PhotoURl2);
//    		obj.setPictureURL3(PhotoURl3);
//
//    		if(isAptmt){
//    			obj.setApartmentID(CurrentAPT.getID());
//    			obj.setApartment(CurrentAPT.getApartmentNo());
//    		}
//    		else{
//    			obj.setApartmentID(CurrentSFA.getID());
//    			obj.setApartment(CurrentSFA.getAreaName());
//    		}
//    		
//    		
//    		
//    		obj.setAptAreaName(SelectedArea);
//    		obj.setReportDate(CurrentSnag.getReportDate().toString());
//    		obj.setSnagStatus(SelectedStatus);
//    			
//    			obj.setReInspectedUnresolvedDate(""+SelectedDate);
//    			obj.setReInspectedUnresolvedDatePictureURL1(""+PhotoURl21);
//    			obj.setReInspectedUnresolvedDatePictureURL2(""+PhotoURl22);
//    			obj.setReInspectedUnresolvedDatePictureURL3(""+PhotoURl23);
//    			obj.setResolveDate(""+SelectedDate);
//    			obj.setResolveDatePictureURL1(""+PhotoURl31);
//    			obj.setResolveDatePictureURL2(""+PhotoURl32);
//    			obj.setResolveDatePictureURL3(""+PhotoURl33);
    			if(isOnline){
    			uploadImage(PhotoURl1);
    			uploadImage(PhotoURl2);
    			uploadImage(PhotoURl3);
    			uploadImage(PhotoURl21);
    			uploadImage(PhotoURl22);
    			uploadImage(PhotoURl23);
    			uploadImage(PhotoURl31);
    			uploadImage(PhotoURl32);
    			uploadImage(PhotoURl33);
    			}
    		
    		FMDBDatabaseAccess db=new FMDBDatabaseAccess(EditDefectChageStatus.this);
    		obj.setStatusForUpload("Inserted");
        	if(isOnline)
        		obj.setIsDataSyncToWeb(true);
        	else
        		obj.setIsDataSyncToWeb(false);
        	obj.setPercentageCompleted(CurrentSnag.getPercentageCompleted());
    		db.UpdateIntoSnagMaster(obj);
    		db=null;
    		//finish();
        	
        	}
            catch(Exception e)
            {
            	Log.d("Exception",""+e.getMessage());
            }
        	
            
        }
         
}
	
	 public void populateCureentSnag()
	 {
		 try{
		 SnagMaster objSnag=new SnagMaster();
		 FMDBDatabaseAccess fmdb=new FMDBDatabaseAccess(EditDefectChageStatus.this);
		
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
	        	Log.d("Exception",""+e.getMessage());
	        }
	 }
	 protected class DownloadSnagInBackground extends AsyncTask<Integer , Integer, Void> 
	 {
		 ProgressDialog mProgressDialog = new ProgressDialog(EditDefectChageStatus.this);
	    	
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
	            	Log.d("Exception",""+e.getMessage());
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
    	    		    
    	    		  request.addProperty("_strSnagID", CurrentSnag.getID());
    	        		
    	    		    
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
            		 FMDBDatabaseAccess fdb=new FMDBDatabaseAccess(EditDefectChageStatus.this);
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
     		obj.setReInspectedUnresolvedDate(jsObj.getString("ReinspectedUnresolvedDate"));//ReinspectedUnresolvedDate
     		obj.setReInspectedUnresolvedDatePictureURL1(jsObj.getString("ReinspectedUnresolvedDatePictureURL1"));
     		obj.setReInspectedUnresolvedDatePictureURL2(jsObj.getString("ReinspectedUnresolvedDatePictureURL2"));
     		obj.setReInspectedUnresolvedDatePictureURL3(jsObj.getString("ReinspectedUnresolvedDatePictureURL3"));
     		
     		obj.setExpectedInspectionDate(jsObj.getString("ExpectedInspectionDate"));
     		obj.setFaultType(jsObj.getString("FaultType"));
     		FMDBDatabaseAccess fdb=new FMDBDatabaseAccess(EditDefectChageStatus.this);
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
	 protected class DownloadImagesInBackGround extends AsyncTask<Integer, Integer,Void>
	 {
		 int tag;
		 String filepath;
		 String imageUrl;
		 Bitmap bmp;
		 int fromImg;
		 int vwNo;
		 String ErrChck;
		 
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
		    		ErrChck="1";
		    		if(!(filechk.exists()))
		    		{
		    			//String downloadUrl="http://snag.itakeon.com/uploadimage/"+imageUrl+".jpg";
		    			String downloadUrl=getResources().getString(R.string.WS_URL)+"uploadimage/"+imageUrl+".jpg";
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
		    	catch(Exception e)
		    	{
		    		ErrChck=null;
		    		Log.d("filepath",""+filepath);
		    		
		    		Log.d("Error", ""+e.getMessage());
		    		
		    		
		    	}
		    	

			return null;
		}
		 @Override
		protected void onPostExecute(Void result)
		 {
			 try{
			runOnUiThread(new Runnable() {
				public void run() 
				{
					try{
					View v;
					int wantedPosition;
					int firstPosition;
					int wantedChild;
					 Log.d("listcount",""+list.getChildCount());
					switch (fromImg)
					{
					case 1:
						 wantedPosition = 11; // Whatever position you're looking for
						 firstPosition = list.getFirstVisiblePosition() - list.getHeaderViewsCount(); // This is the same as child #0
						 wantedChild = wantedPosition - firstPosition;
						 Log.d("listcount",""+list.getChildCount());
						 if(tag==1)
						 {
								// Say, first visible position is 8, you want position 10, wantedChild will now be 2
								// So that means your view is child #2 in the ViewGroup:
								if(! (wantedChild < 0 || wantedChild >= list.getChildCount())) {
							
									//PhotoURl1=retUUID;
									v=list.getChildAt(wantedChild);
									ImageButton i1=(ImageButton)v.findViewById(R.id.row_cell_addDefectPhoto_img1);
									ProgressBar pbar=(ProgressBar)v.findViewById(R.id.row_cell_progress);
									if(ErrChck!=null)
									{
										bmp=decodeFile(filepath);
										//i1.setImageBitmap(bmp);
										Drawable draw=new BitmapDrawable(bmp);
										i1.setBackgroundDrawable(draw);
										if(pbar!=null)
										pbar.setVisibility(View.INVISIBLE);
									
										i1.requestLayout();
										i1.setVisibility(View.VISIBLE);
										
										
									}
									else
									{
										i1.setBackgroundResource(R.drawable.image_add);
										if(pbar!=null)
										pbar.setVisibility(View.INVISIBLE);
										i1.setVisibility(View.VISIBLE);
										
									}
									ListItemAddDefectPhoto itemi=(ListItemAddDefectPhoto)items.get(wantedPosition);
									itemi.url1=i1;
									items.set(wantedPosition,itemi);
									adapter.notifyDataSetChanged();
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
									if(ErrChck!=null)
									{
										bmp=decodeFile(filepath);
										Drawable draw=new BitmapDrawable(bmp);
										i1.setBackgroundDrawable(draw);
										if(pbar2!=null)
										pbar2.setVisibility(View.INVISIBLE);
										i1.requestLayout();
										i1.setVisibility(View.VISIBLE);
									}
									else
									{
										i1.setBackgroundResource(R.drawable.image_add);
										if(pbar2!=null)
										pbar2.setVisibility(View.INVISIBLE);
										i1.setVisibility(View.VISIBLE);
									}
									
									ListItemAddDefectPhoto itemi=(ListItemAddDefectPhoto)items.get(wantedPosition);
									itemi.url2=i1;
									items.set(wantedPosition,itemi);
									adapter.notifyDataSetChanged();
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
									if(ErrChck!=null)
									{
										bmp=decodeFile(filepath);
										if(pbar3!=null)
										pbar3.setVisibility(View.INVISIBLE);
										Drawable draw=new BitmapDrawable(bmp);
										i1.setBackgroundDrawable(draw);
										i1.setVisibility(View.VISIBLE);
										i1.requestLayout();
									}
									else
									{
										i1.setBackgroundResource(R.drawable.image_add);
										if(pbar3!=null)
										pbar3.setVisibility(View.INVISIBLE);
										i1.setVisibility(View.VISIBLE);
									}
									ListItemAddDefectPhoto itemi=(ListItemAddDefectPhoto)items.get(wantedPosition);
									itemi.url3=i1;
									items.set(wantedPosition,itemi);
									adapter.notifyDataSetChanged();
									isImageSetFor1=true;
								}
						 }

						
						break;
					case 2:
						wantedPosition = 15; // Whatever position you're looking for 14
						 firstPosition = list.getFirstVisiblePosition() - list.getHeaderViewsCount(); // This is the same as child #0
						 wantedChild = wantedPosition - firstPosition;
						 Log.d("listcount",""+list.getChildCount());
						 if(tag==1)
						 {
								// Say, first visible position is 8, you want position 10, wantedChild will now be 2
								// So that means your view is child #2 in the ViewGroup:
								if(! (wantedChild < 0 || wantedChild >= list.getChildCount())) 
								{
							
									//PhotoURl1=retUUID;
									v=list.getChildAt(wantedChild);
									ImageButton i1=(ImageButton)v.findViewById(R.id.row_cell_addDefectPhoto_img1);
									ProgressBar pbar=(ProgressBar)v.findViewById(R.id.row_cell_progress);
									if(ErrChck!=null)
									{
									
										bmp=decodeFile(filepath);
										Log.d("filepath",""+filepath);
										//i1.setImageBitmap(bmp);
										Drawable draw=new BitmapDrawable(bmp);
										i1.setBackgroundDrawable(draw);
										if(pbar!=null)
										pbar.setVisibility(View.INVISIBLE);
									
										i1.requestLayout();
										i1.setVisibility(View.VISIBLE);
									}
									else
									{
										i1.setBackgroundResource(R.drawable.image_add);
										if(pbar!=null)
										pbar.setVisibility(View.INVISIBLE);
										i1.setVisibility(View.VISIBLE);
									}
									
									ListItemAddDefectPhoto itemi=(ListItemAddDefectPhoto)items.get(wantedPosition);
									itemi.url1=i1;
									items.set(wantedPosition,itemi);
									adapter.notifyDataSetChanged();
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
									
									if(ErrChck!=null)
									{
									
										bmp=decodeFile(filepath);
										Drawable draw=new BitmapDrawable(bmp);
										i1.setBackgroundDrawable(draw);
										if(pbar2!=null)
										pbar2.setVisibility(View.INVISIBLE);
										i1.requestLayout();
										i1.setVisibility(View.VISIBLE);
									}
									else
									{
										i1.setBackgroundResource(R.drawable.image_add);
										if(pbar2!=null)
										pbar2.setVisibility(View.INVISIBLE);
										i1.setVisibility(View.VISIBLE);
									}
									ListItemAddDefectPhoto itemi=(ListItemAddDefectPhoto)items.get(wantedPosition);
									itemi.url2=i1;
									items.set(wantedPosition,itemi);
									adapter.notifyDataSetChanged();
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
									
									if(ErrChck!=null)
									{
									
										bmp=decodeFile(filepath);
										if(pbar3!=null)
										pbar3.setVisibility(View.INVISIBLE);
										Drawable draw=new BitmapDrawable(bmp);
										i1.setBackgroundDrawable(draw);
										i1.setVisibility(View.VISIBLE);
										i1.requestLayout();
									}
									else
									{
										i1.setBackgroundResource(R.drawable.image_add);
										if(pbar3!=null)
										pbar3.setVisibility(View.INVISIBLE);
										i1.setVisibility(View.VISIBLE);
									}
									ListItemAddDefectPhoto itemi=(ListItemAddDefectPhoto)items.get(wantedPosition);
									itemi.url3=i1;
									items.set(wantedPosition,itemi);
									adapter.notifyDataSetChanged();
									isImageSetFor1=true;
								}
						 }

						
						break;
					case 3:
						wantedPosition = 15; // Whatever position you're looking for 14
						 firstPosition = list.getFirstVisiblePosition() - list.getHeaderViewsCount(); // This is the same as child #0
						 wantedChild = wantedPosition - firstPosition;
						 Log.d("listcount",""+list.getChildCount());
						 if(tag==1)
						 {
								// Say, first visible position is 8, you want position 10, wantedChild will now be 2
								// So that means your view is child #2 in the ViewGroup:
								if(! (wantedChild < 0 || wantedChild >= list.getChildCount())) {
							
									//PhotoURl1=retUUID;
									v=list.getChildAt(wantedChild);
									ImageButton i1=(ImageButton)v.findViewById(R.id.row_cell_addDefectPhoto_img1);
									ProgressBar pbar=(ProgressBar)v.findViewById(R.id.row_cell_progress);
									
									if(ErrChck!=null)
									{
									
										bmp=decodeFile(filepath);
										//i1.setImageBitmap(bmp);
										Drawable draw=new BitmapDrawable(bmp);
										i1.setBackgroundDrawable(draw);
										if(pbar!=null)
										pbar.setVisibility(View.INVISIBLE);
									
										i1.requestLayout();
										i1.setVisibility(View.VISIBLE);
									}
									else
									{
										i1.setBackgroundResource(R.drawable.image_add);
										if(pbar!=null)
										pbar.setVisibility(View.INVISIBLE);
										i1.setVisibility(View.VISIBLE);
									}
									ListItemAddDefectPhoto itemi=(ListItemAddDefectPhoto)items.get(wantedPosition);
									itemi.url1=i1;
									items.set(wantedPosition,itemi);
									adapter.notifyDataSetChanged();
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
									if(ErrChck!=null)
									{
										bmp=decodeFile(filepath);
										Drawable draw=new BitmapDrawable(bmp);
										i1.setBackgroundDrawable(draw);
										if(pbar2!=null)
										pbar2.setVisibility(View.INVISIBLE);
										i1.requestLayout();
										i1.setVisibility(View.VISIBLE);
									}
									else
									{
										i1.setBackgroundResource(R.drawable.image_add);
										if(pbar2!=null)
										pbar2.setVisibility(View.INVISIBLE);
										i1.setVisibility(View.VISIBLE);

									}
									ListItemAddDefectPhoto itemi=(ListItemAddDefectPhoto)items.get(wantedPosition);
									itemi.url2=i1;
									items.set(wantedPosition,itemi);
									adapter.notifyDataSetChanged();
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
									if(ErrChck!=null)
									{
										bmp=decodeFile(filepath);
										if(pbar3!=null)
										pbar3.setVisibility(View.INVISIBLE);
										Drawable draw=new BitmapDrawable(bmp);
										i1.setBackgroundDrawable(draw);
										i1.setVisibility(View.VISIBLE);
										i1.requestLayout();
									}
									else
									{
										i1.setBackgroundResource(R.drawable.image_add);
										if(pbar3!=null)
										pbar3.setVisibility(View.INVISIBLE);
										i1.setVisibility(View.VISIBLE);
									}
									ListItemAddDefectPhoto itemi=(ListItemAddDefectPhoto)items.get(wantedPosition);
									itemi.url3=i1;
									items.set(wantedPosition,itemi);
									adapter.notifyDataSetChanged();
									isImageSetFor1=true;
								}
						 }

						break;
						case 4:
							wantedPosition = 17; // Whatever position you're looking for 16
							 firstPosition = list.getFirstVisiblePosition() - list.getHeaderViewsCount(); // This is the same as child #0
							 wantedChild = wantedPosition - firstPosition;
							 Log.d("listcount",""+list.getChildCount());
							 if(tag==1)
							 {
									// Say, first visible position is 8, you want position 10, wantedChild will now be 2
									// So that means your view is child #2 in the ViewGroup:
									if(! (wantedChild < 0 || wantedChild >= list.getChildCount())) {
								
										//PhotoURl1=retUUID;
										v=list.getChildAt(wantedChild);
										ImageButton i1=(ImageButton)v.findViewById(R.id.row_cell_addDefectPhoto_img1);
										ProgressBar pbar=(ProgressBar)v.findViewById(R.id.row_cell_progress);
										if(ErrChck!=null)
										{
											bmp=decodeFile(filepath);
											//i1.setImageBitmap(bmp);
											Drawable draw=new BitmapDrawable(bmp);
											i1.setBackgroundDrawable(draw);
											if(pbar!=null)
											pbar.setVisibility(View.INVISIBLE);
										
											i1.requestLayout();
											i1.setVisibility(View.VISIBLE);
										}
										else
										{
											i1.setBackgroundResource(R.drawable.image_add);
											if(pbar!=null)
											pbar.setVisibility(View.INVISIBLE);
											i1.setVisibility(View.VISIBLE);
										}
										ListItemAddDefectPhoto itemi=(ListItemAddDefectPhoto)items.get(wantedPosition);
										itemi.url1=i1;
										items.set(wantedPosition,itemi);
										adapter.notifyDataSetChanged();
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
										if(ErrChck!=null)
										{
											bmp=decodeFile(filepath);
											Drawable draw=new BitmapDrawable(bmp);
											i1.setBackgroundDrawable(draw);
											if(pbar2!=null)
											pbar2.setVisibility(View.INVISIBLE);
											i1.requestLayout();
											i1.setVisibility(View.VISIBLE);
										}
										else
										{
											i1.setBackgroundResource(R.drawable.image_add);
											if(pbar2!=null)
											pbar2.setVisibility(View.INVISIBLE);
											i1.setVisibility(View.VISIBLE);
										}
										ListItemAddDefectPhoto itemi=(ListItemAddDefectPhoto)items.get(wantedPosition);
										itemi.url2=i1;
										items.set(wantedPosition,itemi);
										adapter.notifyDataSetChanged();
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
										if(ErrChck!=null)
										{
										
											bmp=decodeFile(filepath);
											if(pbar3!=null)
											pbar3.setVisibility(View.INVISIBLE);
											Drawable draw=new BitmapDrawable(bmp);
											i1.setBackgroundDrawable(draw);
											i1.setVisibility(View.VISIBLE);
											i1.requestLayout();
										}
										else
										{
											i1.setBackgroundResource(R.drawable.image_add);
											if(pbar3!=null)
											pbar3.setVisibility(View.INVISIBLE);
											i1.setVisibility(View.VISIBLE);
										}
										ListItemAddDefectPhoto itemi=(ListItemAddDefectPhoto)items.get(wantedPosition);
										itemi.url3=i1;
										items.set(wantedPosition,itemi);
										adapter.notifyDataSetChanged();
										isImageSetFor1=true;
									}
							 }

							break;
					default:
						break;
					}
					//snagphts8
					//unreslvd11
					//reslvd11
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
	        	new AlertDialog.Builder(EditDefectChageStatus.this)
	    	    
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
	        	new AlertDialog.Builder(EditDefectChageStatus.this)
	    	    
	    	    .setMessage("Are you sure you want to Logout?")
	    	    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
	    	        public void onClick(DialogInterface dialog, int which) { 
//	    	            FMDBDatabaseAccess db=new FMDBDatabaseAccess(EditDefectChageStatus.this);
//	    	            SharedPreferences SP = getSharedPreferences("AppDelegate",MODE_PRIVATE);
//	    				db.performLogout(SP.getString("RegUserID", ""));
//	    				Intent i=new Intent(EditDefectChageStatus.this,com.snagreporter.Login_page.class);
//	    				i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//	    				startActivity(i);
//	    				
//	    				finish();
	    	        	try{
	    	        	setResult(10002);
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
	            	Log.d("Exception",""+e.getMessage());
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

	    		
	    		FMDBDatabaseAccess db=new FMDBDatabaseAccess(EditDefectChageStatus.this);
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
	            	Log.d("Exception",""+e.getMessage());
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
		
		public void onScroll(AbsListView Lview, int firstVisibleItem,
				int visibleItemCount, int totalItemCount) 
		{
			// TODO Auto-generated method stub
			try{
			switch (Lview.getId())
			{
			case R.id.android_edit_defect_list:
	            int lastItem = firstVisibleItem + visibleItemCount;
	            if(!SelectedStatus.equals(Status[0]))
	            	lastItem--;
	            ImageButton imgVw1;
	            if(lastItem == totalItemCount-1)
	            {
	            	if(PhotoURl1!=null && PhotoURl1.length()>0)
	            	{
	            		String FilePath1=Environment.getExternalStorageDirectory()+File.separator+"SnagReporter/Pictures/"+PhotoURl1+".jpg";
	            		File fooo = new File(FilePath1);
	            		if(fooo.exists())
	            		{
	            			Bitmap Img1 = decodeFile(FilePath1);
	            			int wantedPosition = 11;
	            			int firstPosition = list.getFirstVisiblePosition() - list.getHeaderViewsCount(); 
	            			int wantedChild = wantedPosition - firstPosition;
	            			if (wantedChild < 0 || wantedChild >= list.getChildCount())
	            			{
	      	   				  Log.d("Child Not Available", "");
	      	   				}
	      	   				else
	      	   				{
	      	   					View v=list.getChildAt(wantedChild);
	      	   					
	      	   					ImageButton btnI=new ImageButton(EditDefectChageStatus.this);
	      	   					btnI.setImageBitmap(Img1);
	      	   					imgVw1=(ImageButton)v.findViewById(R.id.row_cell_addDefectPhoto_img1);
	      	   					imgVw1.setBackgroundDrawable(btnI.getDrawable());
	      	   				}
	            		}
	            	}
	            	
	            	if(PhotoURl2!=null && PhotoURl2.length()>0)
	            	{
	            		String FilePath1=Environment.getExternalStorageDirectory()+File.separator+"SnagReporter/Pictures/"+PhotoURl2+".jpg";
	            		File fooo = new File(FilePath1);
	            		if(fooo.exists())
	            		{
	            			Bitmap Img1 = decodeFile(FilePath1);
	            			int wantedPosition = 11;
	            			int firstPosition = list.getFirstVisiblePosition() - list.getHeaderViewsCount(); 
	            			int wantedChild = wantedPosition - firstPosition;
	            			
	            			if (wantedChild < 0 || wantedChild >= list.getChildCount())
	            			{
	      	   				  Log.d("Child Not Available", "");
	      	   				}
	            			else
	      	   				{
	      	   					View v=list.getChildAt(wantedChild);
	      	   					
	      	   					ImageButton btnI=new ImageButton(EditDefectChageStatus.this);
	      	   					btnI.setImageBitmap(Img1);
	      	   					imgVw1=(ImageButton)v.findViewById(R.id.row_cell_addDefectPhoto_img2);
	      	   					imgVw1.setBackgroundDrawable(btnI.getDrawable());
	      	   				}
	            			
	            		}
	            		
	            	}
	            	if(PhotoURl3!=null && PhotoURl3.length()>0)
	            	{
	            		String FilePath1=Environment.getExternalStorageDirectory()+File.separator+"SnagReporter/Pictures/"+PhotoURl3+".jpg";
	            		File fooo = new File(FilePath1);
	            		if(fooo.exists())
	            		{
	            			Bitmap Img1 = decodeFile(FilePath1);
	            			int wantedPosition = 11;
	            			int firstPosition = list.getFirstVisiblePosition() - list.getHeaderViewsCount(); 
	            			int wantedChild = wantedPosition - firstPosition;
	            			if (wantedChild < 0 || wantedChild >= list.getChildCount())
	            			{
	      	   				  Log.d("Child Not Available", "");
	      	   				}
	            			else
	      	   				{
	      	   					View v=list.getChildAt(wantedChild);
	      	   					
	      	   					ImageButton btnI=new ImageButton(EditDefectChageStatus.this);
	      	   					btnI.setImageBitmap(Img1);
	      	   					imgVw1=(ImageButton)v.findViewById(R.id.row_cell_addDefectPhoto_img3);
	      	   					imgVw1.setBackgroundDrawable(btnI.getDrawable());
	      	   				}
	            		}
	            	}
	            	//Checking old pending
	            	if(oldStatus.equalsIgnoreCase("Pending"))
	            	{
	            		if(SelectedStatus.equalsIgnoreCase("Reinspected & Unresolved"))
	            		{
	            			if(PhotoURl21!=null && PhotoURl21.length()>0)
	    	            	{
	            				String FilePath1=Environment.getExternalStorageDirectory()+File.separator+"SnagReporter/Pictures/"+PhotoURl21+".jpg";
	    	            		File fooo = new File(FilePath1);
	    	            		if(fooo.exists())
	    	            		{
	    	            			Bitmap Img1 = decodeFile(FilePath1);
	    	            			int wantedPosition = 15; //14
	    	            			int firstPosition = list.getFirstVisiblePosition() - list.getHeaderViewsCount(); 
	    	            			int wantedChild = wantedPosition - firstPosition;
	    	            			if (wantedChild < 0 || wantedChild >= list.getChildCount())
	    	            			{
	    	      	   				  Log.d("Child Not Available", "");
	    	      	   				}
	    	            			else
	    	            			{
	    	            				View v=list.getChildAt(wantedChild);
	    	      	   					
	    	      	   					ImageButton btnI=new ImageButton(EditDefectChageStatus.this);
	    	      	   					btnI.setImageBitmap(Img1);
	    	      	   					imgVw1=(ImageButton)v.findViewById(R.id.row_cell_addDefectPhoto_img1);
	    	      	   					imgVw1.setBackgroundDrawable(btnI.getDrawable());
	    	      	   					ProgressBar pbar=(ProgressBar)v.findViewById(R.id.row_cell_progress);
	    	      	   				if(pbar!=null)
	    	      	   					pbar.setVisibility(View.INVISIBLE);
	    	      	   					imgVw1.setVisibility(View.VISIBLE);
	    	            			}
	    	            		}
	    	            		
	            				
	    	            	}
	            			if(PhotoURl22!=null && PhotoURl22.length()>0)
	    	            	{
	            				String FilePath1=Environment.getExternalStorageDirectory()+File.separator+"SnagReporter/Pictures/"+PhotoURl22+".jpg";
	    	            		File fooo = new File(FilePath1);
	    	            		if(fooo.exists())
	    	            		{
	    	            			Bitmap Img1 = decodeFile(FilePath1);
	    	            			int wantedPosition = 15; //14
	    	            			int firstPosition = list.getFirstVisiblePosition() - list.getHeaderViewsCount(); 
	    	            			int wantedChild = wantedPosition - firstPosition;
	    	            			if (wantedChild < 0 || wantedChild >= list.getChildCount())
	    	            			{
	    	      	   				  Log.d("Child Not Available", "");
	    	      	   				}
	    	            			else
	    	            			{
	    	            					View v=list.getChildAt(wantedChild);
	    	      	   					
	    	      	   					ImageButton btnI=new ImageButton(EditDefectChageStatus.this);
	    	      	   					btnI.setImageBitmap(Img1);
	    	      	   					imgVw1=(ImageButton)v.findViewById(R.id.row_cell_addDefectPhoto_img2);
	    	      	   					imgVw1.setBackgroundDrawable(btnI.getDrawable());
	    	      	   					ProgressBar pbar=(ProgressBar)v.findViewById(R.id.row_cell_progress2);
	    	      	   					if(pbar!=null)
	    	      	   					pbar.setVisibility(View.INVISIBLE);
	    	      	   					imgVw1.setVisibility(View.VISIBLE);
	    	            			}
	    	            			
	    	            		}
	    	            	}
	            			if(PhotoURl23!=null && PhotoURl23.length()>0)
	    	            	{
	            				String FilePath1=Environment.getExternalStorageDirectory()+File.separator+"SnagReporter/Pictures/"+PhotoURl23+".jpg";
	    	            		File fooo = new File(FilePath1);
	    	            		if(fooo.exists())
	    	            		{
	    	            			Bitmap Img1 = decodeFile(FilePath1);
	    	            			int wantedPosition = 15; //14
	    	            			int firstPosition = list.getFirstVisiblePosition() - list.getHeaderViewsCount(); 
	    	            			int wantedChild = wantedPosition - firstPosition;
	    	            			if (wantedChild < 0 || wantedChild >= list.getChildCount())
	    	            			{
	    	      	   				  Log.d("Child Not Available", "");
	    	      	   				}
	    	            			else
	    	            			{
	    	            				View v=list.getChildAt(wantedChild);
	    	      	   					
	    	      	   					ImageButton btnI=new ImageButton(EditDefectChageStatus.this);
	    	      	   					btnI.setImageBitmap(Img1);
	    	      	   					imgVw1=(ImageButton)v.findViewById(R.id.row_cell_addDefectPhoto_img3);
	    	      	   					imgVw1.setBackgroundDrawable(btnI.getDrawable());
	    	      	   					ProgressBar pbar=(ProgressBar)v.findViewById(R.id.row_cell_progress3);
	    	      	   					if(pbar!=null)
	    	      	   					pbar.setVisibility(View.INVISIBLE);
	    	      	   					imgVw1.setVisibility(View.VISIBLE);
	    	            			}
	    	            		}
	    	            	}
	            			}
	            			else if(SelectedStatus.equalsIgnoreCase("Resolved"))
	            			{
	            				
		            			
		            			
	            				if(PhotoURl31!=null && PhotoURl31.length()>0)
		    	            	{
		            				String FilePath1=Environment.getExternalStorageDirectory()+File.separator+"SnagReporter/Pictures/"+PhotoURl31+".jpg";
		    	            		File fooo = new File(FilePath1);
		    	            		if(fooo.exists())
		    	            		{
		    	            			Bitmap Img1 = decodeFile(FilePath1);
		    	            			int wantedPosition = 15; //14
		    	            			int firstPosition = list.getFirstVisiblePosition() - list.getHeaderViewsCount(); 
		    	            			int wantedChild = wantedPosition - firstPosition;
		    	            			if (wantedChild < 0 || wantedChild >= list.getChildCount())
		    	            			{
		    	      	   				  Log.d("Child Not Available", "");
		    	      	   				}
		    	            			else
		    	            			{
		    	            				View v=list.getChildAt(wantedChild);
		    	      	   					
		    	      	   					ImageButton btnI=new ImageButton(EditDefectChageStatus.this);
		    	      	   					btnI.setImageBitmap(Img1);
		    	      	   					imgVw1=(ImageButton)v.findViewById(R.id.row_cell_addDefectPhoto_img1);
		    	      	   					imgVw1.setBackgroundDrawable(btnI.getDrawable());
		    	      	   					ProgressBar pbar=(ProgressBar)v.findViewById(R.id.row_cell_progress);
		    	      	   				if(pbar!=null)
		    	      	   					pbar.setVisibility(View.INVISIBLE);
		    	      	   					imgVw1.setVisibility(View.VISIBLE);
		    	            			}
		    	            		}
		    	            		
		            				
		    	            	}
		            			if(PhotoURl32!=null && PhotoURl32.length()>0)
		    	            	{
		            				String FilePath1=Environment.getExternalStorageDirectory()+File.separator+"SnagReporter/Pictures/"+PhotoURl32+".jpg";
		    	            		File fooo = new File(FilePath1);
		    	            		if(fooo.exists())
		    	            		{
		    	            			Bitmap Img1 = decodeFile(FilePath1);
		    	            			int wantedPosition = 15; //14
		    	            			int firstPosition = list.getFirstVisiblePosition() - list.getHeaderViewsCount(); 
		    	            			int wantedChild = wantedPosition - firstPosition;
		    	            			if (wantedChild < 0 || wantedChild >= list.getChildCount())
		    	            			{
		    	      	   				  Log.d("Child Not Available", "");
		    	      	   				}
		    	            			else
		    	            			{
		    	            					View v=list.getChildAt(wantedChild);
		    	      	   					
		    	      	   					ImageButton btnI=new ImageButton(EditDefectChageStatus.this);
		    	      	   					btnI.setImageBitmap(Img1);
		    	      	   					imgVw1=(ImageButton)v.findViewById(R.id.row_cell_addDefectPhoto_img2);
		    	      	   					imgVw1.setBackgroundDrawable(btnI.getDrawable());
		    	      	   					ProgressBar pbar=(ProgressBar)v.findViewById(R.id.row_cell_progress2);
		    	      	   				if(pbar!=null)
		    	      	   					pbar.setVisibility(View.INVISIBLE);
		    	      	   					imgVw1.setVisibility(View.VISIBLE);
		    	            			}
		    	            			
		    	            		}
		    	            	}
		            			if(PhotoURl33!=null && PhotoURl33.length()>0)
		    	            	{
		            				String FilePath1=Environment.getExternalStorageDirectory()+File.separator+"SnagReporter/Pictures/"+PhotoURl33+".jpg";
		    	            		File fooo = new File(FilePath1);
		    	            		if(fooo.exists())
		    	            		{
		    	            			Bitmap Img1 = decodeFile(FilePath1);
		    	            			int wantedPosition = 15;  //14
		    	            			int firstPosition = list.getFirstVisiblePosition() - list.getHeaderViewsCount(); 
		    	            			int wantedChild = wantedPosition - firstPosition;
		    	            			if (wantedChild < 0 || wantedChild >= list.getChildCount())
		    	            			{
		    	      	   				  Log.d("Child Not Available", "");
		    	      	   				}
		    	            			else
		    	            			{
		    	            				View v=list.getChildAt(wantedChild);
		    	      	   					
		    	      	   					ImageButton btnI=new ImageButton(EditDefectChageStatus.this);
		    	      	   					btnI.setImageBitmap(Img1);
		    	      	   					imgVw1=(ImageButton)v.findViewById(R.id.row_cell_addDefectPhoto_img3);
		    	      	   					imgVw1.setBackgroundDrawable(btnI.getDrawable());
		    	      	   					ProgressBar pbar=(ProgressBar)v.findViewById(R.id.row_cell_progress3);
		    	      	   				if(pbar!=null)
		    	      	   					pbar.setVisibility(View.INVISIBLE);
		    	      	   					imgVw1.setVisibility(View.VISIBLE);
		    	            			}
		    	            		}
		    	            	}
	            				
	            			}
	            	}
	            	//Close
	            	if(oldStatus.equalsIgnoreCase("Reinspected & Unresolved"))
	            	{
	            		if(SelectedStatus.equalsIgnoreCase("Reinspected & Unresolved"))
	            		{
	            			if(PhotoURl21!=null && PhotoURl21.length()>0)
	    	            	{
	            				String FilePath1=Environment.getExternalStorageDirectory()+File.separator+"SnagReporter/Pictures/"+PhotoURl21+".jpg";
	    	            		File fooo = new File(FilePath1);
	    	            		if(fooo.exists())
	    	            		{
	    	            			Bitmap Img1 = decodeFile(FilePath1);
	    	            			int wantedPosition = 15; //14
	    	            			int firstPosition = list.getFirstVisiblePosition() - list.getHeaderViewsCount(); 
	    	            			int wantedChild = wantedPosition - firstPosition;
	    	            			if (wantedChild < 0 || wantedChild >= list.getChildCount())
	    	            			{
	    	      	   				  Log.d("Child Not Available", "");
	    	      	   				}
	    	            			else
	    	            			{
	    	            				View v=list.getChildAt(wantedChild);
	    	      	   					
	    	      	   					ImageButton btnI=new ImageButton(EditDefectChageStatus.this);
	    	      	   					btnI.setImageBitmap(Img1);
	    	      	   					imgVw1=(ImageButton)v.findViewById(R.id.row_cell_addDefectPhoto_img1);
	    	      	   					imgVw1.setBackgroundDrawable(btnI.getDrawable());
	    	      	   					ProgressBar pbar=(ProgressBar)v.findViewById(R.id.row_cell_progress);
	    	      	   				if(pbar!=null)
	    	      	   					pbar.setVisibility(View.INVISIBLE);
	    	      	   					imgVw1.setVisibility(View.VISIBLE);
	    	            			}
	    	            		}
	    	            		
	            				
	    	            	}
	            			if(PhotoURl22!=null && PhotoURl22.length()>0)
	    	            	{
	            				String FilePath1=Environment.getExternalStorageDirectory()+File.separator+"SnagReporter/Pictures/"+PhotoURl22+".jpg";
	    	            		File fooo = new File(FilePath1);
	    	            		if(fooo.exists())
	    	            		{
	    	            			Bitmap Img1 = decodeFile(FilePath1);
	    	            			int wantedPosition = 15;  //14
	    	            			int firstPosition = list.getFirstVisiblePosition() - list.getHeaderViewsCount(); 
	    	            			int wantedChild = wantedPosition - firstPosition;
	    	            			if (wantedChild < 0 || wantedChild >= list.getChildCount())
	    	            			{
	    	      	   				  Log.d("Child Not Available", "");
	    	      	   				}
	    	            			else
	    	            			{
	    	            					View v=list.getChildAt(wantedChild);
	    	      	   					
	    	      	   					ImageButton btnI=new ImageButton(EditDefectChageStatus.this);
	    	      	   					btnI.setImageBitmap(Img1);
	    	      	   					imgVw1=(ImageButton)v.findViewById(R.id.row_cell_addDefectPhoto_img2);
	    	      	   					imgVw1.setBackgroundDrawable(btnI.getDrawable());
	    	      	   					ProgressBar pbar=(ProgressBar)v.findViewById(R.id.row_cell_progress2);
	    	      	   					if(pbar!=null)
	    	      	   					pbar.setVisibility(View.INVISIBLE);
	    	      	   					imgVw1.setVisibility(View.VISIBLE);
	    	            			}
	    	            			
	    	            		}
	    	            	}
	            			if(PhotoURl23!=null && PhotoURl23.length()>0)
	    	            	{
	            				String FilePath1=Environment.getExternalStorageDirectory()+File.separator+"SnagReporter/Pictures/"+PhotoURl23+".jpg";
	    	            		File fooo = new File(FilePath1);
	    	            		if(fooo.exists())
	    	            		{
	    	            			Bitmap Img1 = decodeFile(FilePath1);
	    	            			int wantedPosition = 15; //14
	    	            			int firstPosition = list.getFirstVisiblePosition() - list.getHeaderViewsCount(); 
	    	            			int wantedChild = wantedPosition - firstPosition;
	    	            			if (wantedChild < 0 || wantedChild >= list.getChildCount())
	    	            			{
	    	      	   				  Log.d("Child Not Available", "");
	    	      	   				}
	    	            			else
	    	            			{
	    	            				View v=list.getChildAt(wantedChild);
	    	      	   					
	    	      	   					ImageButton btnI=new ImageButton(EditDefectChageStatus.this);
	    	      	   					btnI.setImageBitmap(Img1);
	    	      	   					imgVw1=(ImageButton)v.findViewById(R.id.row_cell_addDefectPhoto_img3);
	    	      	   					imgVw1.setBackgroundDrawable(btnI.getDrawable());
	    	      	   					ProgressBar pbar=(ProgressBar)v.findViewById(R.id.row_cell_progress3);
	    	      	   					if(pbar!=null)
	    	      	   					pbar.setVisibility(View.INVISIBLE);
	    	      	   					imgVw1.setVisibility(View.VISIBLE);
	    	            			}
	    	            		}
	    	            	}
	            			}
	            			else if(SelectedStatus.equalsIgnoreCase("Resolved"))
	            			{
	            				if(PhotoURl21!=null && PhotoURl21.length()>0)
		    	            	{
		            				String FilePath1=Environment.getExternalStorageDirectory()+File.separator+"SnagReporter/Pictures/"+PhotoURl21+".jpg";
		    	            		File fooo = new File(FilePath1);
		    	            		if(fooo.exists())
		    	            		{
		    	            			Bitmap Img1 = decodeFile(FilePath1);
		    	            			int wantedPosition = 15; //14
		    	            			int firstPosition = list.getFirstVisiblePosition() - list.getHeaderViewsCount(); 
		    	            			int wantedChild = wantedPosition - firstPosition;
		    	            			if (wantedChild < 0 || wantedChild >= list.getChildCount())
		    	            			{
		    	      	   				  Log.d("Child Not Available", "");
		    	      	   				}
		    	            			else
		    	            			{
		    	            				View v=list.getChildAt(wantedChild);
		    	      	   					
		    	      	   					ImageButton btnI=new ImageButton(EditDefectChageStatus.this);
		    	      	   					btnI.setImageBitmap(Img1);
		    	      	   					imgVw1=(ImageButton)v.findViewById(R.id.row_cell_addDefectPhoto_img1);
		    	      	   					imgVw1.setBackgroundDrawable(btnI.getDrawable());
		    	      	   					ProgressBar pbar=(ProgressBar)v.findViewById(R.id.row_cell_progress);
		    	      	   					if(pbar!=null)
		    	      	   					pbar.setVisibility(View.INVISIBLE);
		    	      	   					imgVw1.setVisibility(View.VISIBLE);
		    	            			}
		    	            		}
		    	            		
		            				
		    	            	}
		            			if(PhotoURl22!=null && PhotoURl22.length()>0)
		    	            	{
		            				String FilePath1=Environment.getExternalStorageDirectory()+File.separator+"SnagReporter/Pictures/"+PhotoURl22+".jpg";
		    	            		File fooo = new File(FilePath1);
		    	            		if(fooo.exists())
		    	            		{
		    	            			Bitmap Img1 = decodeFile(FilePath1);
		    	            			int wantedPosition = 15; //14
		    	            			int firstPosition = list.getFirstVisiblePosition() - list.getHeaderViewsCount(); 
		    	            			int wantedChild = wantedPosition - firstPosition;
		    	            			if (wantedChild < 0 || wantedChild >= list.getChildCount())
		    	            			{
		    	      	   				  Log.d("Child Not Available", "");
		    	      	   				}
		    	            			else
		    	            			{
		    	            					View v=list.getChildAt(wantedChild);
		    	      	   					
		    	      	   					ImageButton btnI=new ImageButton(EditDefectChageStatus.this);
		    	      	   					btnI.setImageBitmap(Img1);
		    	      	   					imgVw1=(ImageButton)v.findViewById(R.id.row_cell_addDefectPhoto_img2);
		    	      	   					imgVw1.setBackgroundDrawable(btnI.getDrawable());
		    	      	   					ProgressBar pbar=(ProgressBar)v.findViewById(R.id.row_cell_progress2);
		    	      	   					if(pbar!=null)
		    	      	   					pbar.setVisibility(View.INVISIBLE);
		    	      	   					imgVw1.setVisibility(View.VISIBLE);
		    	            			}
		    	            			
		    	            		}
		    	            	}
		            			if(PhotoURl23!=null && PhotoURl23.length()>0)
		    	            	{
		            				String FilePath1=Environment.getExternalStorageDirectory()+File.separator+"SnagReporter/Pictures/"+PhotoURl23+".jpg";
		    	            		File fooo = new File(FilePath1);
		    	            		if(fooo.exists())
		    	            		{
		    	            			Bitmap Img1 = decodeFile(FilePath1);
		    	            			int wantedPosition = 15; //14
		    	            			int firstPosition = list.getFirstVisiblePosition() - list.getHeaderViewsCount(); 
		    	            			int wantedChild = wantedPosition - firstPosition;
		    	            			if (wantedChild < 0 || wantedChild >= list.getChildCount())
		    	            			{
		    	      	   				  Log.d("Child Not Available", "");
		    	      	   				}
		    	            			else
		    	            			{
		    	            				View v=list.getChildAt(wantedChild);
		    	      	   					
		    	      	   					ImageButton btnI=new ImageButton(EditDefectChageStatus.this);
		    	      	   					btnI.setImageBitmap(Img1);
		    	      	   					imgVw1=(ImageButton)v.findViewById(R.id.row_cell_addDefectPhoto_img3);
		    	      	   					imgVw1.setBackgroundDrawable(btnI.getDrawable());
		    	      	   					ProgressBar pbar=(ProgressBar)v.findViewById(R.id.row_cell_progress3);
		    	      	   					if(pbar!=null)
		    	      	   					pbar.setVisibility(View.INVISIBLE);
		    	      	   					imgVw1.setVisibility(View.VISIBLE);
		    	            			}
		    	            		}
		    	            	}
	            				if(PhotoURl31!=null && PhotoURl31.length()>0)
		    	            	{
		            				String FilePath1=Environment.getExternalStorageDirectory()+File.separator+"SnagReporter/Pictures/"+PhotoURl31+".jpg";
		    	            		File fooo = new File(FilePath1);
		    	            		if(fooo.exists())
		    	            		{
		    	            			Bitmap Img1 = decodeFile(FilePath1);
		    	            			int wantedPosition = 17; //16
		    	            			int firstPosition = list.getFirstVisiblePosition() - list.getHeaderViewsCount(); 
		    	            			int wantedChild = wantedPosition - firstPosition;
		    	            			if (wantedChild < 0 || wantedChild >= list.getChildCount())
		    	            			{
		    	      	   				  Log.d("Child Not Available", "");
		    	      	   				}
		    	            			else
		    	            			{
		    	            				View v=list.getChildAt(wantedChild);
		    	      	   					
		    	      	   					ImageButton btnI=new ImageButton(EditDefectChageStatus.this);
		    	      	   					btnI.setImageBitmap(Img1);
		    	      	   					imgVw1=(ImageButton)v.findViewById(R.id.row_cell_addDefectPhoto_img1);
		    	      	   					imgVw1.setBackgroundDrawable(btnI.getDrawable());
		    	      	   					ProgressBar pbar=(ProgressBar)v.findViewById(R.id.row_cell_progress);
		    	      	   				if(pbar!=null)
		    	      	   					pbar.setVisibility(View.INVISIBLE);
		    	      	   					imgVw1.setVisibility(View.VISIBLE);
		    	            			}
		    	            		}
		    	            		
		            				
		    	            	}
		            			if(PhotoURl32!=null && PhotoURl32.length()>0)
		    	            	{
		            				String FilePath1=Environment.getExternalStorageDirectory()+File.separator+"SnagReporter/Pictures/"+PhotoURl32+".jpg";
		    	            		File fooo = new File(FilePath1);
		    	            		if(fooo.exists())
		    	            		{
		    	            			Bitmap Img1 = decodeFile(FilePath1);
		    	            			int wantedPosition = 17;  //16
		    	            			int firstPosition = list.getFirstVisiblePosition() - list.getHeaderViewsCount(); 
		    	            			int wantedChild = wantedPosition - firstPosition;
		    	            			if (wantedChild < 0 || wantedChild >= list.getChildCount())
		    	            			{
		    	      	   				  Log.d("Child Not Available", "");
		    	      	   				}
		    	            			else
		    	            			{
		    	            					View v=list.getChildAt(wantedChild);
		    	      	   					
		    	      	   					ImageButton btnI=new ImageButton(EditDefectChageStatus.this);
		    	      	   					btnI.setImageBitmap(Img1);
		    	      	   					imgVw1=(ImageButton)v.findViewById(R.id.row_cell_addDefectPhoto_img2);
		    	      	   					imgVw1.setBackgroundDrawable(btnI.getDrawable());
		    	      	   					ProgressBar pbar=(ProgressBar)v.findViewById(R.id.row_cell_progress2);
		    	      	   				if(pbar!=null)
		    	      	   					pbar.setVisibility(View.INVISIBLE);
		    	      	   					imgVw1.setVisibility(View.VISIBLE);
		    	            			}
		    	            			
		    	            		}
		    	            	}
		            			if(PhotoURl33!=null && PhotoURl33.length()>0)
		    	            	{
		            				String FilePath1=Environment.getExternalStorageDirectory()+File.separator+"SnagReporter/Pictures/"+PhotoURl33+".jpg";
		    	            		File fooo = new File(FilePath1);
		    	            		if(fooo.exists())
		    	            		{
		    	            			Bitmap Img1 = decodeFile(FilePath1);
		    	            			int wantedPosition = 17; //16
		    	            			int firstPosition = list.getFirstVisiblePosition() - list.getHeaderViewsCount(); 
		    	            			int wantedChild = wantedPosition - firstPosition;
		    	            			if (wantedChild < 0 || wantedChild >= list.getChildCount())
		    	            			{
		    	      	   				  Log.d("Child Not Available", "");
		    	      	   				}
		    	            			else
		    	            			{
		    	            				View v=list.getChildAt(wantedChild);
		    	      	   					
		    	      	   					ImageButton btnI=new ImageButton(EditDefectChageStatus.this);
		    	      	   					btnI.setImageBitmap(Img1);
		    	      	   					imgVw1=(ImageButton)v.findViewById(R.id.row_cell_addDefectPhoto_img3);
		    	      	   					imgVw1.setBackgroundDrawable(btnI.getDrawable());
		    	      	   					ProgressBar pbar=(ProgressBar)v.findViewById(R.id.row_cell_progress3);
		    	      	   				if(pbar!=null)
		    	      	   					pbar.setVisibility(View.INVISIBLE);
		    	      	   					imgVw1.setVisibility(View.VISIBLE);
		    	            			}
		    	            		}
		    	            	}
	            				
	            			}
	            	}
	            	if(oldStatus.equalsIgnoreCase("Resolved"))
	            	{
	            		if(CurrentSnag.getReInspectedUnresolvedDate()==null)
	            		{
	            			if(PhotoURl31!=null && PhotoURl31.length()>0)
	    	            	{
	            				String FilePath1=Environment.getExternalStorageDirectory()+File.separator+"SnagReporter/Pictures/"+PhotoURl31+".jpg";
	    	            		File fooo = new File(FilePath1);
	    	            		if(fooo.exists())
	    	            		{
	    	            			Bitmap Img1 = decodeFile(FilePath1);
	    	            			int wantedPosition = 15; //14
	    	            			int firstPosition = list.getFirstVisiblePosition() - list.getHeaderViewsCount(); 
	    	            			int wantedChild = wantedPosition - firstPosition;
	    	            			if (wantedChild < 0 || wantedChild >= list.getChildCount())
	    	            			{
	    	      	   				  Log.d("Child Not Available", "");
	    	      	   				}
	    	            			else
	    	            			{
	    	            				View v=list.getChildAt(wantedChild);
	    	      	   					
	    	      	   					ImageButton btnI=new ImageButton(EditDefectChageStatus.this);
	    	      	   					btnI.setImageBitmap(Img1);
	    	      	   					imgVw1=(ImageButton)v.findViewById(R.id.row_cell_addDefectPhoto_img1);
	    	      	   					imgVw1.setBackgroundDrawable(btnI.getDrawable());
	    	      	   					ProgressBar pbar=(ProgressBar)v.findViewById(R.id.row_cell_progress);
	    	      	   				if(pbar!=null)
	    	      	   					pbar.setVisibility(View.INVISIBLE);
	    	      	   					imgVw1.setVisibility(View.VISIBLE);
	    	            			}
	    	            		}
	    	            		
	            				
	    	            	}
	            			if(PhotoURl32!=null && PhotoURl32.length()>0)
	    	            	{
	            				String FilePath1=Environment.getExternalStorageDirectory()+File.separator+"SnagReporter/Pictures/"+PhotoURl32+".jpg";
	    	            		File fooo = new File(FilePath1);
	    	            		if(fooo.exists())
	    	            		{
	    	            			Bitmap Img1 = decodeFile(FilePath1);
	    	            			int wantedPosition = 15; //14
	    	            			int firstPosition = list.getFirstVisiblePosition() - list.getHeaderViewsCount(); 
	    	            			int wantedChild = wantedPosition - firstPosition;
	    	            			if (wantedChild < 0 || wantedChild >= list.getChildCount())
	    	            			{
	    	      	   				  Log.d("Child Not Available", "");
	    	      	   				}
	    	            			else
	    	            			{
	    	            					View v=list.getChildAt(wantedChild);
	    	      	   					
	    	      	   					ImageButton btnI=new ImageButton(EditDefectChageStatus.this);
	    	      	   					btnI.setImageBitmap(Img1);
	    	      	   					imgVw1=(ImageButton)v.findViewById(R.id.row_cell_addDefectPhoto_img2);
	    	      	   					imgVw1.setBackgroundDrawable(btnI.getDrawable());
	    	      	   					ProgressBar pbar=(ProgressBar)v.findViewById(R.id.row_cell_progress2);
	    	      	   				if(pbar!=null)
	    	      	   					pbar.setVisibility(View.INVISIBLE);
	    	      	   					imgVw1.setVisibility(View.VISIBLE);
	    	            			}
	    	            			
	    	            		}
	    	            	}
	            			if(PhotoURl33!=null && PhotoURl33.length()>0)
	    	            	{
	            				String FilePath1=Environment.getExternalStorageDirectory()+File.separator+"SnagReporter/Pictures/"+PhotoURl33+".jpg";
	    	            		File fooo = new File(FilePath1);
	    	            		if(fooo.exists())
	    	            		{
	    	            			Bitmap Img1 = decodeFile(FilePath1);
	    	            			int wantedPosition = 15; //14
	    	            			int firstPosition = list.getFirstVisiblePosition() - list.getHeaderViewsCount(); 
	    	            			int wantedChild = wantedPosition - firstPosition;
	    	            			if (wantedChild < 0 || wantedChild >= list.getChildCount())
	    	            			{
	    	      	   				  Log.d("Child Not Available", "");
	    	      	   				}
	    	            			else
	    	            			{
	    	            				View v=list.getChildAt(wantedChild);
	    	      	   					
	    	      	   					ImageButton btnI=new ImageButton(EditDefectChageStatus.this);
	    	      	   					btnI.setImageBitmap(Img1);
	    	      	   					imgVw1=(ImageButton)v.findViewById(R.id.row_cell_addDefectPhoto_img3);
	    	      	   					imgVw1.setBackgroundDrawable(btnI.getDrawable());
	    	      	   					ProgressBar pbar=(ProgressBar)v.findViewById(R.id.row_cell_progress3);
	    	      	   				if(pbar!=null)
	    	      	   					pbar.setVisibility(View.INVISIBLE);
	    	      	   					imgVw1.setVisibility(View.VISIBLE);
	    	            			}
	    	            		}
	    	            	}
	            		}
	            		else
	            		{
	            			if(PhotoURl21!=null && PhotoURl21.length()>0)
	    	            	{
	            				String FilePath1=Environment.getExternalStorageDirectory()+File.separator+"SnagReporter/Pictures/"+PhotoURl21+".jpg";
	    	            		File fooo = new File(FilePath1);
	    	            		if(fooo.exists())
	    	            		{
	    	            			Bitmap Img1 = decodeFile(FilePath1);
	    	            			int wantedPosition = 15; //14
	    	            			int firstPosition = list.getFirstVisiblePosition() - list.getHeaderViewsCount(); 
	    	            			int wantedChild = wantedPosition - firstPosition;
	    	            			if (wantedChild < 0 || wantedChild >= list.getChildCount())
	    	            			{
	    	      	   				  Log.d("Child Not Available", "");
	    	      	   				}
	    	            			else
	    	            			{
	    	            				View v=list.getChildAt(wantedChild);
	    	      	   					
	    	      	   					ImageButton btnI=new ImageButton(EditDefectChageStatus.this);
	    	      	   					btnI.setImageBitmap(Img1);
	    	      	   					imgVw1=(ImageButton)v.findViewById(R.id.row_cell_addDefectPhoto_img1);
	    	      	   					imgVw1.setBackgroundDrawable(btnI.getDrawable());
	    	      	   					ProgressBar pbar=(ProgressBar)v.findViewById(R.id.row_cell_progress);
	    	      	   				if(pbar!=null)
	    	      	   					pbar.setVisibility(View.INVISIBLE);
	    	      	   					imgVw1.setVisibility(View.VISIBLE);
	    	            			}
	    	            		}
	    	            		
	            				
	    	            	}
	            			if(PhotoURl22!=null && PhotoURl22.length()>0)
	    	            	{
	            				String FilePath1=Environment.getExternalStorageDirectory()+File.separator+"SnagReporter/Pictures/"+PhotoURl22+".jpg";
	    	            		File fooo = new File(FilePath1);
	    	            		if(fooo.exists())
	    	            		{
	    	            			Bitmap Img1 = decodeFile(FilePath1);
	    	            			int wantedPosition = 15;  // 14
	    	            			int firstPosition = list.getFirstVisiblePosition() - list.getHeaderViewsCount(); 
	    	            			int wantedChild = wantedPosition - firstPosition;
	    	            			if (wantedChild < 0 || wantedChild >= list.getChildCount())
	    	            			{
	    	      	   				  Log.d("Child Not Available", "");
	    	      	   				}
	    	            			else
	    	            			{
	    	            					View v=list.getChildAt(wantedChild);
	    	      	   					
	    	      	   					ImageButton btnI=new ImageButton(EditDefectChageStatus.this);
	    	      	   					btnI.setImageBitmap(Img1);
	    	      	   					imgVw1=(ImageButton)v.findViewById(R.id.row_cell_addDefectPhoto_img2);
	    	      	   					imgVw1.setBackgroundDrawable(btnI.getDrawable());
	    	      	   					ProgressBar pbar=(ProgressBar)v.findViewById(R.id.row_cell_progress2);
	    	      	   				if(pbar!=null)
	    	      	   					pbar.setVisibility(View.INVISIBLE);
	    	      	   					imgVw1.setVisibility(View.VISIBLE);
	    	            			}
	    	            			
	    	            		}
	    	            	}
	            			if(PhotoURl23!=null && PhotoURl23.length()>0)
	    	            	{
	            				String FilePath1=Environment.getExternalStorageDirectory()+File.separator+"SnagReporter/Pictures/"+PhotoURl23+".jpg";
	    	            		File fooo = new File(FilePath1);
	    	            		if(fooo.exists())
	    	            		{
	    	            			Bitmap Img1 = decodeFile(FilePath1);
	    	            			int wantedPosition = 15; //14
	    	            			int firstPosition = list.getFirstVisiblePosition() - list.getHeaderViewsCount(); 
	    	            			int wantedChild = wantedPosition - firstPosition;
	    	            			if (wantedChild < 0 || wantedChild >= list.getChildCount())
	    	            			{
	    	      	   				  Log.d("Child Not Available", "");
	    	      	   				}
	    	            			else
	    	            			{
	    	            				View v=list.getChildAt(wantedChild);
	    	      	   					
	    	      	   					ImageButton btnI=new ImageButton(EditDefectChageStatus.this);
	    	      	   					btnI.setImageBitmap(Img1);
	    	      	   					imgVw1=(ImageButton)v.findViewById(R.id.row_cell_addDefectPhoto_img3);
	    	      	   					imgVw1.setBackgroundDrawable(btnI.getDrawable());
	    	      	   					ProgressBar pbar=(ProgressBar)v.findViewById(R.id.row_cell_progress3);
	    	      	   				if(pbar!=null)
	    	      	   					pbar.setVisibility(View.INVISIBLE);
	    	      	   					imgVw1.setVisibility(View.VISIBLE);
	    	            			}
	    	            		}
	    	            	}
	            			
	            			if(PhotoURl31!=null && PhotoURl31.length()>0)
	    	            	{
	            				String FilePath1=Environment.getExternalStorageDirectory()+File.separator+"SnagReporter/Pictures/"+PhotoURl31+".jpg";
	    	            		File fooo = new File(FilePath1);
	    	            		if(fooo.exists())
	    	            		{
	    	            			Bitmap Img1 = decodeFile(FilePath1);
	    	            			int wantedPosition = 17; //16
	    	            			int firstPosition = list.getFirstVisiblePosition() - list.getHeaderViewsCount(); 
	    	            			int wantedChild = wantedPosition - firstPosition;
	    	            			if (wantedChild < 0 || wantedChild >= list.getChildCount())
	    	            			{
	    	      	   				  Log.d("Child Not Available", "");
	    	      	   				}
	    	            			else
	    	            			{
	    	            				View v=list.getChildAt(wantedChild);
	    	      	   					
	    	      	   					ImageButton btnI=new ImageButton(EditDefectChageStatus.this);
	    	      	   					btnI.setImageBitmap(Img1);
	    	      	   					imgVw1=(ImageButton)v.findViewById(R.id.row_cell_addDefectPhoto_img1);
	    	      	   					imgVw1.setBackgroundDrawable(btnI.getDrawable());
	    	      	   					ProgressBar pbar=(ProgressBar)v.findViewById(R.id.row_cell_progress);
	    	      	   				if(pbar!=null)
	    	      	   					pbar.setVisibility(View.INVISIBLE);
	    	      	   					imgVw1.setVisibility(View.VISIBLE);
	    	            			}
	    	            		}
	    	            		
	            				
	    	            	}
	            			if(PhotoURl32!=null && PhotoURl32.length()>0)
	    	            	{
	            				String FilePath1=Environment.getExternalStorageDirectory()+File.separator+"SnagReporter/Pictures/"+PhotoURl32+".jpg";
	    	            		File fooo = new File(FilePath1);
	    	            		if(fooo.exists())
	    	            		{
	    	            			Bitmap Img1 = decodeFile(FilePath1);
	    	            			int wantedPosition = 17; //16
	    	            			int firstPosition = list.getFirstVisiblePosition() - list.getHeaderViewsCount(); 
	    	            			int wantedChild = wantedPosition - firstPosition;
	    	            			if (wantedChild < 0 || wantedChild >= list.getChildCount())
	    	            			{
	    	      	   				  Log.d("Child Not Available", "");
	    	      	   				}
	    	            			else
	    	            			{
	    	            					View v=list.getChildAt(wantedChild);
	    	      	   					
	    	      	   					ImageButton btnI=new ImageButton(EditDefectChageStatus.this);
	    	      	   					btnI.setImageBitmap(Img1);
	    	      	   					imgVw1=(ImageButton)v.findViewById(R.id.row_cell_addDefectPhoto_img2);
	    	      	   					imgVw1.setBackgroundDrawable(btnI.getDrawable());
	    	      	   					ProgressBar pbar=(ProgressBar)v.findViewById(R.id.row_cell_progress2);
	    	      	   				if(pbar!=null)
	    	      	   					pbar.setVisibility(View.INVISIBLE);
	    	      	   					imgVw1.setVisibility(View.VISIBLE);
	    	            			}
	    	            			
	    	            		}
	    	            	}
	            			if(PhotoURl33!=null && PhotoURl33.length()>0)
	    	            	{
	            				String FilePath1=Environment.getExternalStorageDirectory()+File.separator+"SnagReporter/Pictures/"+PhotoURl33+".jpg";
	    	            		File fooo = new File(FilePath1);
	    	            		if(fooo.exists())
	    	            		{
	    	            			Bitmap Img1 = decodeFile(FilePath1);
	    	            			int wantedPosition = 17; //16
	    	            			int firstPosition = list.getFirstVisiblePosition() - list.getHeaderViewsCount(); 
	    	            			int wantedChild = wantedPosition - firstPosition;
	    	            			if (wantedChild < 0 || wantedChild >= list.getChildCount())
	    	            			{
	    	      	   				  Log.d("Child Not Available", "");
	    	      	   				}
	    	            			else
	    	            			{
	    	            				View v=list.getChildAt(wantedChild);
	    	      	   					
	    	      	   					ImageButton btnI=new ImageButton(EditDefectChageStatus.this);
	    	      	   					btnI.setImageBitmap(Img1);
	    	      	   					imgVw1=(ImageButton)v.findViewById(R.id.row_cell_addDefectPhoto_img3);
	    	      	   					imgVw1.setBackgroundDrawable(btnI.getDrawable());
	    	      	   					ProgressBar pbar=(ProgressBar)v.findViewById(R.id.row_cell_progress3);
	    	      	   				if(pbar!=null)
	    	      	   					pbar.setVisibility(View.INVISIBLE);
	    	      	   					imgVw1.setVisibility(View.VISIBLE);
	    	            			}
	    	            		}
	    	            	}
	            		}
	            	}
	            }
	 	           

				
				break;

			default:
				break;
			}
		}
        catch(Exception e)
        {
        	Log.d("Exception",""+e.getMessage());
        }
		}
		
		public void onScrollStateChanged(AbsListView view, int scrollState)
		{
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
	    				new AlertDialog.Builder(EditDefectChageStatus.this)
	    	    	    
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
	    			Intent i=new Intent(EditDefectChageStatus.this,com.snagreporter.GraphWebView.class);
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
	    		ParseSyncData parser=new ParseSyncData(EditDefectChageStatus.this);
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
	                	FMDBDatabaseAccess db=new FMDBDatabaseAccess(EditDefectChageStatus.this);
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
	                	FMDBDatabaseAccess db=new FMDBDatabaseAccess(EditDefectChageStatus.this);
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
	            	FMDBDatabaseAccess db=new FMDBDatabaseAccess(EditDefectChageStatus.this);
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
	            	Log.d("Exception",""+e.getMessage());
	            }
	        }
	        public byte[] getBytesFromBitmap(Bitmap bitmap) {
	            ByteArrayOutputStream stream = new ByteArrayOutputStream();
	            bitmap.compress(CompressFormat.JPEG, 70, stream);
	            return stream.toByteArray();
	        }
	        
	        //@@@@@@$$$$$$$$$$@@@@@@@@@@@
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
	        public void EditItemClick(View v){
	        	try{
	        		
	        		if(isExtArea)
	        		{
	        			Intent itt=new Intent(EditDefectChageStatus.this,com.snagreporter.EditDefect.class);
						itt.putExtra("isFromExtArea",true);
						itt.putExtra("Project",curProject);
						itt.putExtra("Building",curBuilding);
						itt.putExtra("Snag", CurrentSnag);
						startActivityForResult(itt,10001);
	        		}
	        		else if(isExtFloorArea)
	        		{
	        			Intent itt=new Intent(EditDefectChageStatus.this,com.snagreporter.EditDefect.class);
	        			itt.putExtra("isFromFloorArea", true);
						itt.putExtra("Project",curProject);
						itt.putExtra("Building",curBuilding);
						itt.putExtra("Floor",curFloor);
						itt.putExtra("Snag", CurrentSnag);
						startActivityForResult(itt,10001);
	        		}
	        		else
	        		{
	        			 Intent i=new Intent(EditDefectChageStatus.this,com.snagreporter.EditDefect.class);
	 	 				
	 	 				if(isAptmt){
	 	 					i.putExtra("isAptmt", isAptmt);
	 	 					i.putExtra("Apartment", CurrentAPT);
	 	 		        }
	 	 		        else{
	 	 		        	i.putExtra("isAptmt", isAptmt);
	 	 					i.putExtra("SFA", CurrentSFA);
	 	 		        }
	 	 				i.putExtra("Snag", CurrentSnag);
	 	 				i.putExtra("Project",curProject);
	 	 				i.putExtra("Building",curBuilding);
	 	 				i.putExtra("Floor",curFloor);
	 	 				//i.putExtra("SelectedArea", SelectedArea);
	 	 				startActivityForResult(i,10001);
	        		}
	        		
	        	}
	        	catch(Exception e){
	        		
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





