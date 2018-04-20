package com.snagreporter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

import org.json.JSONObject;
import org.kobjects.base64.Base64;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.MarshalBase64;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import com.snagreporter.AddDefect.DownloadDataFromWeb;
import com.snagreporter.AddDefect.TwoTextArrayAdapter;
import com.snagreporter.AddDefect.TwoTextArrayAdapter.RowType;
import com.snagreporter.database.FMDBDatabaseAccess;
import com.snagreporter.entity.ApartmentDetails;
import com.snagreporter.entity.ApartmentMaster;
import com.snagreporter.entity.BuildingMaster;
import com.snagreporter.entity.Employee;
import com.snagreporter.entity.FaultType;
import com.snagreporter.entity.FloorMaster;
import com.snagreporter.entity.InspectionDetails;
import com.snagreporter.entity.InspectionList;
import com.snagreporter.entity.InspectionMaster;
import com.snagreporter.entity.JobType;
import com.snagreporter.entity.ProjectMaster;
import com.snagreporter.entity.Registration;
import com.snagreporter.entity.SnagChecklistEntries;
import com.snagreporter.entity.SnagChecklistMaster;
import com.snagreporter.entity.SnagMaster;
import com.snagreporter.entity.StdFloorAreas;
import com.snagreporter.listitems.Header;
import com.snagreporter.listitems.Header2;
import com.snagreporter.listitems.Item;
import com.snagreporter.listitems.ListItemAddDefect;
import com.snagreporter.listitems.ListItemAddDefectPhoto;
import com.snagreporter.listitems.ListItemAddUpdateInspection;
import com.snagreporter.menuhandler.MenuHandler;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.CompressFormat;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.view.ViewPager.LayoutParams;
import android.text.InputType;
import android.util.Log;
import android.view.ContextMenu;
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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;

public class AddSnagCheckList extends Activity implements OnScrollListener
{
	String NAMESPACE = "http://tempuri.org/";
	String URL ="";
	
	String METHOD_NAME_SaveNewDataToTheDataBase = "SaveNewDataToTheDataBase";
	String SOAP_ACTION_SaveNewDataToTheDataBase = "http://tempuri.org/SaveNewDataToTheDataBase";
	String METHOD_NAME_UpdateDataToTheDataBase = "UpdateDataToTheDataBase";
	String SOAP_ACTION_UpdateDataToTheDataBase = "http://tempuri.org/UpdateDataToTheDataBase";
	
	String[] strSetValues;
	String[] strGetValues;
 	ListView list;
	ImageButton imgVw1,imgVw2,imgVw3;
	int cnt=1;
	List<Item> items;
	String sample;
	BuildingMaster CurrentBuilding;
	FloorMaster CurrentFloor;
	ProjectMaster CurrentProject;
	ApartmentMaster CurrentAPT;
	String SelectedAreaType,SelectedAreaID,SelectedJobType,SelectedJobDetails,SelectedFaultType,SelectedInspector,selectedCost="",costTo="",snagPriority="";
	JobType arrJobType[];
	FaultType arrFaultType[];
	String AreaType="";
	private static final int CAMERA_PIC_REQUEST = 1337;
	
	int as_index=-1;
	int as_index_selected=0;
	boolean isImageSetFor1=false;
	boolean isImageSetFor2=false;
	boolean isImageSetFor3=false;
	//String PhotoURl1="",PhotoURl2="",PhotoURl3="";
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
	int selectedInspectorIndex=-1;
	int CurrentDate=-1;
	boolean isFirstTime;
	TwoTextArrayAdapter adapter;
	TwoTextArrayAdapter adapter2;
	
	boolean isOnline=false;
	ProgressDialog mProgressDialog2;
	int image=0;
	
	private File dir, destImage,f;
	private String cameraFile = null;
	SnagMaster UpdatedSnag;
	JobType[] AddedSnagType;
	FaultType[] AddedFaultType;
	Employee[] arrEmployee;
	int SelectedEmployeeIndex=-1;
	String strLoginType="";
	String RegUserID="";
	String LoginType="";
	View TopMenu;
	boolean isMenuVisible=false;
	MenuHandler menuhandler;
	SnagChecklistMaster[] arrSnagChecklist;
	
	@Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_snag_inspection_list);
        
        SharedPreferences SP = getSharedPreferences("AppDelegate",MODE_PRIVATE);
        RegUserID=SP.getString("RegUserID", "");
        LoginType=SP.getString("LoginType", "");
       URL =""+getResources().getString(R.string.WS_URL).toString()+"SnagReporter_WebS.asmx";
        SharedPreferences sharedPref = getSharedPreferences("AppDelegate",MODE_PRIVATE);
        Boolean id=sharedPref.getBoolean("isOnline", false);
        isOnline=id;
        
        menuhandler=new MenuHandler(AddSnagCheckList.this);
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
        mProgressDialog2 = new ProgressDialog(AddSnagCheckList.this);
        UpdatedSnag=(SnagMaster) getIntent().getExtras().get("UpdatedSnag");
        CurrentAPT=(ApartmentMaster)getIntent().getExtras().get("Apt");
//        CurrentBuilding=(BuildingMaster)getIntent().getExtras().get("Building");
//        CurrentFloor=(FloorMaster)getIntent().getExtras().get("Floor");
//        isAptmt=getIntent().getExtras().getBoolean("isAptmt");
        
        Button btn=(Button)findViewById(R.id.adddefectSavebtn);
        btn.setBackgroundDrawable(getResources().getDrawable(R.drawable.back_blue_button));
        
        Object objNot = getLastNonConfigurationInstance();
        if(objNot!=null)
        {
        	Intent i=(Intent)objNot;
        	strGetValues=(String[])i.getExtras().get("strSetValues");
        	AddedFaultType=(FaultType[])i.getExtras().get("AddedFaultType");
        	AddedSnagType=(JobType[])i.getExtras().get("AddedSnagType");
        	arrJobType=(JobType[])i.getExtras().get("arrJobType");
        	arrFaultType=(FaultType[])i.getExtras().get("arrFaultType");
        }
//        if(isAptmt){
//        	CurrentAPT=(ApartmentMaster)getIntent().getExtras().get("Apartment");
//        	
//        }
//        else{
//        	CurrentSFA=(StdFloorAreas)getIntent().getExtras().get("SFA");
//        }
        
       
        
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
		FMDBDatabaseAccess db=new FMDBDatabaseAccess(getApplicationContext());
        AreaList=db.getApartmentDetails(CurrentAPT);
        imgVw1=(ImageButton)findViewById(R.id.row_cell_addDefectPhoto_img1);
        imgVw2=(ImageButton)findViewById(R.id.row_cell_addDefectPhoto_img2);
        imgVw3=(ImageButton)findViewById(R.id.row_cell_addDefectPhoto_img3);
        list=(ListView)findViewById(R.id.android_add_defect_list);
        
        ContinueProcess();
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
        strSetValues=new String[16];
        strSetValues[0]=SelectedAreaType;
        strSetValues[1]=SelectedJobType;
        strSetValues[2]=SelectedFaultType;
        strSetValues[3]=SelectedJobDetails;
        strSetValues[4]=""+selectedInspectorIndex;
        strSetValues[5]=ReportedDate;
        strSetValues[6]=ExpectedDate;
        strSetValues[7]=UpdatedSnag.getPictureURL1();
        strSetValues[8]=UpdatedSnag.getPictureURL2();
        strSetValues[9]=UpdatedSnag.getPictureURL3();
        strSetValues[10]=selectedCost;
        strSetValues[11]=costTo;
        strSetValues[12]=snagPriority;
        strSetValues[13]=strFromImg;
        if(f!=null)
        	strSetValues[14]=f.getAbsolutePath().toString();
        strSetValues[15]=""+SelectedEmployeeIndex;
        
        Intent i=new Intent();
        i.putExtra("strSetValues", strSetValues);
        i.putExtra("AddedSnagType", AddedSnagType);
        i.putExtra("AddedFaultType", AddedFaultType);
        i.putExtra("arrJobType", arrJobType);
        i.putExtra("arrFaultType", arrFaultType);
        
        
        return i;
    }
	
	public void ContinueProcess(){
    	//Toast.makeText(getApplicationContext(), "Came in Continue Process "+cnt++, Toast.LENGTH_LONG).show();
    	try{
    		
    		
    		if(strGetValues!=null)
    		{
    			SelectedAreaType=strGetValues[0];
    		    SelectedJobType=strGetValues[1];
    		    SelectedFaultType=strGetValues[2];
    		    SelectedJobDetails=strGetValues[3];
    		    selectedInspectorIndex=Integer.parseInt(strGetValues[4]);
    		    ReportedDate=strGetValues[5];
    		    ExpectedDate=strGetValues[6];
//    		    PhotoURl1=strGetValues[7];
//    		    PhotoURl2=strGetValues[8];
//    		    PhotoURl3=strGetValues[9];
    		    selectedCost=strGetValues[10];
    		    costTo=strGetValues[11];
    		    snagPriority=strGetValues[12];
    		    SelectedEmployeeIndex=Integer.parseInt(strGetValues[15]);
    		    
    			
    		}
    		items = new ArrayList<Item>();
    		if(LoginType.equals(getResources().getString(R.string.strInspector).toString()))
  	       items.add(new Header(null, "INSPECTION LIST",false,false,true,true));
    		else
    			items.add(new Header(null, "INSPECTION LIST",false,false,true,false));
  	   
  	     FMDBDatabaseAccess obj=new FMDBDatabaseAccess(getApplicationContext());
  	     if(AreaList!=null && AreaList.length>0)
	    	   if(SelectedAreaType==null || SelectedAreaType.length()==0)
	    		   SelectedAreaType=AreaList[0].getAptAreaType();
  	     
  	     
  	   String arr[]=obj.getAreaType(SelectedAreaType);
    	 AreaType=arr[0];
  	     
  	   if(arrJobType==null || arrJobType.length==0){
			//getJobTypeFromWeb();
			arrJobType=obj.getJobType();
		}
  	   
  	   if(SelectedJobType==null || SelectedJobType.length()==0)
	       SelectedJobType=arrJobType[0].getJobType();
	       
  	       populateData();
  	       
  	       
  	       
  	       
  	       
  	       list.setDivider(getResources().getDrawable(R.color.transparent));
  	       list.setDividerHeight(1);
  	       
  	      adapter = new TwoTextArrayAdapter(this, items);
  	      
  	    	 list.setAdapter(adapter); 
  	    	 
  	    	 list.setOnScrollListener(this);
  	       
  	        list.setOnItemClickListener(new OnItemClickListener() {

  				
  				public void onItemClick(AdapterView<?> arg0, View arg1, int position,
  						long arg3) {
//  					Toast.makeText(getApplicationContext(), ""+position, Toast.LENGTH_LONG).show();
//  					int wantedPosition =position; // Whatever position you're looking for
//					  int firstPosition = list.getFirstVisiblePosition() - list.getHeaderViewsCount(); // This is the same as child #0
//					  int wantedChild = wantedPosition - firstPosition;
//					// Say, first visible position is 8, you want position 10, wantedChild will now be 2
//					// So that means your view is child #2 in the ViewGroup:
//					  if (wantedChild < 0 || wantedChild >= list.getChildCount()) {
//						  Log.d("Child Not Available", "");
//					  }
//					  else{
//						  View v2=list.getChildAt(wantedChild);
//						  //TextView t2=(TextView)v2.findViewById(R.id.row_cell_addDefect_text);
//					  }
  					registerForContextMenu(arg0);
			    		as_index=position;
			    		openContextMenu(arg0);
			    		unregisterForContextMenu(arg0);
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
//		if(as_index==1){
//			menu.setHeaderTitle("Select Area Type");
//			for(int i=0;i<AreaList.length;i++){
//				menu.add(0, v.getId(), 0, ""+AreaList[i].getAptAreaType());
//			}
//		}
//		else
			if(as_index==1){
			menu.setHeaderTitle("Select JobType");
			for(int i=0;i<arrJobType.length;i++){
				menu.add(0, v.getId(), 0, ""+arrJobType[i].getJobType());
			}
			//menu.add(0, v.getId(), 0, "Add More");
		}
	}
	
	@Override
	public boolean onContextItemSelected(MenuItem item)
	{
//		if(as_index==0){
//	
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
//		SelectedAreaType=item.getTitle().toString();
//		}
//		}
//		else 
			if(as_index==1){
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
		 return true;
	}
	public void CheckBoxButtonClick(View v1){
		try{
			boolean checked=false;
			int tag=Integer.parseInt(v1.getTag().toString());
			if(arrSnagChecklist!=null && arrSnagChecklist.length>0){
				if(arrSnagChecklist[tag].getIsSelected()){
					arrSnagChecklist[tag].setIsSelected(false);
					checked=false;
				}
				else{
					arrSnagChecklist[tag].setIsSelected(true);
					checked=true;
				}
				int wantedPosition =tag+1; // Whatever position you're looking for
				int firstPosition = list.getFirstVisiblePosition() - list.getHeaderViewsCount(); // This is the same as child #0
				int wantedChild = wantedPosition - firstPosition;
				// Say, first visible position is 8, you want position 10, wantedChild will now be 2
				// So that means your view is child #2 in the ViewGroup:
				if (wantedChild < 0 || wantedChild >= list.getChildCount()) {
				  Log.d("Child Not Available", "");
				}
				else{
					
				final View v=list.getChildAt(wantedChild);
				CheckBox t=(CheckBox)v.findViewById(R.id.check_box);
				t.setChecked(checked);
				
			
			}
			}
		
		
		}
		catch(Exception e){
			Log.d("Error=", ""+e.getMessage());
		}
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
	public void populateData()
    {
    	try{
    		
    		//if(AreaList!=null && AreaList.length>0)
    		//	items.add(new ListItemAddDefect(null, "Area",""+SelectedAreaType,false));
//    		else
//    			items.add(new ListItemAddDefect(null, "Area",""+CurrentAPT.getAptType(),false));
//    		
    		//items.add(new ListItemAddDefect(null, "JobType",""+SelectedJobType,false));
    		
    		
    		if(UpdatedSnag!=null){
    			FMDBDatabaseAccess db=new FMDBDatabaseAccess(AddSnagCheckList.this);
    			arrSnagChecklist=db.getSnagChecklistMasterForType(UpdatedSnag.getSnagType(), UpdatedSnag.getFaultType());
    			if(arrSnagChecklist!=null && arrSnagChecklist.length>0){
    				for(int i=0;i<arrSnagChecklist.length;i++){
    					arrSnagChecklist[i].setIsSelected(false);
    				}
    			}
    		}
    		
    		if(arrSnagChecklist!=null && arrSnagChecklist.length>0){
    			for(int i=0;i<arrSnagChecklist.length;i++)
    				items.add(new ListItemAddUpdateInspection(null, ""+arrSnagChecklist[i].getChecklistDescription(),"",false,i,arrSnagChecklist[i].getIsSelected()));
    		}
    		else{
    			new AlertDialog.Builder(AddSnagCheckList.this)
        	    
        	    .setMessage("No Check List Found!")
        	    .setPositiveButton("Continue", new DialogInterface.OnClickListener() {
        	        public void onClick(DialogInterface dialog, int which) { 
        	        	SaveClick(null);
        	        }
        	     })
        	     .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
        	        public void onClick(DialogInterface dialog, int which) { 
        	            finish();
        	        }
        	     })
        	    .show();
    		}
    		

    		
    		
    		
            
    	}
    	catch(Exception e){
    	 Log.d("Error=", ""+e.getMessage());
    	}
    }
	public void SaveClick(View v){
		try{
			FMDBDatabaseAccess db=new FMDBDatabaseAccess(AddSnagCheckList.this);
			double per=0.0;
		
		
		if(arrSnagChecklist!=null && arrSnagChecklist.length>0){
			SnagChecklistEntries[] arr=new SnagChecklistEntries[arrSnagChecklist.length];
			
		for(int i=0;i<arrSnagChecklist.length;i++){
			
			SnagChecklistEntries obj2=new SnagChecklistEntries();
				UUID id2=UUID.randomUUID();
				obj2.ID=(String)(id2.toString());
				obj2.ProjectID=(String)(UpdatedSnag.getProjectID());
				obj2.BuildingID=(String)(UpdatedSnag.getBuildingID());
				obj2.FloorID=(String)(UpdatedSnag.getFloorID());
				obj2.ApartmentID=(String)(UpdatedSnag.getApartmentID());
				obj2.AptAreaID=(String)(UpdatedSnag.getAptAreaID());
				obj2.SnagID=(String)(UpdatedSnag.getID());
				obj2.ChecklistID=(String)(arrSnagChecklist[i].getID());
				obj2.ChecklistDescription=(String)(arrSnagChecklist[i].getChecklistDescription());
				if(arrSnagChecklist[i].getIsSelected())
					obj2.CheckListEntry=(String)("YES");
				else
					obj2.CheckListEntry=(String)("NO");
				obj2.CreatedBy=(String)(RegUserID);
				obj2.CreatedDate=(String)("");
				obj2.ModifiedBy=(String)(RegUserID);
				obj2.ModifiedDate=(String)("");
				obj2.EnteredOnMachineID=(String)("");
				arr[i]=obj2;
				db.insertORUpdateSnagChecklistEntry(obj2);
		}
		int completecount=0;
		for(int i=0;i<arrSnagChecklist.length;i++){
			if(arrSnagChecklist[i].getIsSelected())
				completecount++;
		}
		
		
		if(completecount>0)
			per=(completecount/arrSnagChecklist.length)*100;
		
		if(isOnline){	
			//UploadinsertORUpdateSnagChecklistEntry(obj);
			for(int i=0;i<arrSnagChecklist.length;i++){
				UploadinsertORUpdateSnagChecklistEntry(arr[i]);
			}
		}
		
		}
		UpdatedSnag.setPercentageCompleted(per);
		
			
		//finish();
		UploadDataToWeb task=new UploadDataToWeb();
		task.execute(10);
			
		}
		catch(Exception e){
			Log.d("Error=", ""+e.getMessage());
		}
		
	}
	public void onScroll(AbsListView arg0, int arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub
		
	}
public void UploadinsertORUpdateSnagChecklistEntry(SnagChecklistEntries obj){
	boolean result;
	try
	{
		SoapObject request=new SoapObject(NAMESPACE, METHOD_NAME_SaveNewDataToTheDataBase);
		SoapSerializationEnvelope envelope=new SoapSerializationEnvelope(SoapEnvelope.VER11);
		envelope.dotNet=true;
		envelope.setOutputSoapObject(request);
		
		  String CoumnNames="ID~ProjectID~BuildingID~FloorID~ApartmentID~AptAreaID~SnagID~ChecklistID~ChecklistDescription~ChecklistEntry~CreatedBy~CreatedDate~ModifiedBy~ModifiedDate~EnteredOnMachineID";
		  String Values=""+obj.ID+"~"+obj.ProjectID+"~"+obj.BuildingID+"~"+obj.FloorID+"~"+obj.ApartmentID+"~"+obj.AptAreaID+"~"+obj.SnagID+"~"+obj.ChecklistID+"~"+obj.ChecklistDescription+"~"+obj.CheckListEntry+"~"+obj.CreatedBy+"~"+obj.CreatedDate+"~"+obj.ModifiedBy+"~"+obj.ModifiedDate+"~"+obj.EnteredOnMachineID;
		  String TableName="SnagChecklistEnteries";
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
	catch (Exception e)
	{
		Log.d("SyncToWeb", ""+e.getMessage());
	}
}
protected class UploadDataToWeb extends AsyncTask<Integer , Integer, Void> {
	ProgressDialog mProgressDialog = new ProgressDialog(AddSnagCheckList.this);
	JSONObject jObject;
	String output="";
	SnagMaster obj=new SnagMaster();
    @Override
    protected void onPreExecute() {  
    	if(isOnline){
    	mProgressDialog.setCancelable(false);
    	mProgressDialog.setMessage("Loading...");
        mProgressDialog.show();
    	}
    }      
    @Override
    protected Void doInBackground(Integer... params) {
    	
    	
    	
    	try
		{
    		
    		
//    		obj.setID(UpdatedSnag.getID());
//    		obj.setSnagType(UpdatedSnag.getSnagType());
//    		obj.setSnagDetails(UpdatedSnag.getSnagDetails());
//    		obj.setFaultType(UpdatedSnag.getFaultType());
//    		obj.setExpectedInspectionDate(""+UpdatedSnag.getExpectedInspectionDate());
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
//    			
//    			obj.setReInspectedUnresolvedDate(""+SelectedReinspDate);
//    			obj.setReInspectedUnresolvedDatePictureURL1(""+PhotoURl21);
//    			obj.setReInspectedUnresolvedDatePictureURL2(""+PhotoURl22);
//    			obj.setReInspectedUnresolvedDatePictureURL3(""+PhotoURl23);
//    			obj.setResolveDate(""+SelectedDate);
//    			obj.setResolveDatePictureURL1(""+PhotoURl31);
//    			obj.setResolveDatePictureURL2(""+PhotoURl32);
//    			obj.setResolveDatePictureURL3(""+PhotoURl33);
//    			obj.setCost(Double.parseDouble(selectedCost));
//    			obj.setCostTo(costTo);
//    			obj.setSnagPriority(SnagPriority);
//    			
//    			obj.setQCC(SelectedQCC);
    		
    		
    		obj=UpdatedSnag;
    		
    		
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
    		    
    		    
    		    String CoumnNames="SnagType~SnagDetails~FaultType~ExpectedInspectionDate~PictureURL1~PictureURL2~PictureURL3~ApartmentID~Apartment~AptAreaName~ReportDate~SnagStatus~ReInspectedUnresolvedDate~ReInspectedUnresolvedDatePictureURL1~ReInspectedUnresolvedDatePictureURL2~ReInspectedUnresolvedDatePictureURL3~ResolveDate~ResolveDatePictureURL1~ResolveDatePictureURL2~ResolveDatePictureURL3~Cost~CostTo~PriorityLevel~QCC~PercentageCompleted";
    		    String Values=""+obj.getSnagType()+"~"+obj.getSnagDetails()+"~"+obj.getFaultType()+"~"+obj.getExpectedInspectionDate()+"~"+obj.getPictureURL1()+"~"+obj.getPictureURL2()+"~"+obj.getPictureURL3()+"~"+obj.getApartmentID()+"~"+obj.getApartment()+"~"+obj.getAptAreaName()+"~"+obj.getReportDate()+"~"+obj.getSnagStatus()+"~"+obj.getReInspectedUnresolvedDate()+"~"+obj.getReInspectedUnresolvedDatePictureURL1()+"~"+obj.getReInspectedUnresolvedDatePictureURL2()+"~"+obj.getReInspectedUnresolvedDatePictureURL3()+"~"+obj.getResolveDate()+"~"+obj.getResolveDatePictureURL1()+"~"+obj.getResolveDatePictureURL2()+"~"+obj.getResolveDatePictureURL3()+"~"+obj.getCost()+"~"+obj.getCostTo()+"~"+obj.getSnagPriority()+"~"+obj.getQCC()+"~"+obj.getPercentageCompleted();
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
    	if(mProgressDialog.isShowing())
    		mProgressDialog.dismiss();
//    	SnagMaster obj=new SnagMaster();
//		
//		obj.setID(CurrentSnag.getID());
//		obj.setSnagType(SelectedJobType);
//		obj.setSnagDetails(SelectedJobDetails);
//		obj.setFaultType(SelectedFaultType);
//		obj.setExpectedInspectionDate(""+ExpectedDate);
//		
//		obj.setPictureURL1(PhotoURl1);
//		obj.setPictureURL2(PhotoURl2);
//		obj.setPictureURL3(PhotoURl3);
//
//		if(isAptmt){
//			obj.setApartmentID(CurrentAPT.getID());
//			obj.setApartment(CurrentAPT.getApartmentNo());
//		}
//		else{
//			obj.setApartmentID(CurrentSFA.getID());
//			obj.setApartment(CurrentSFA.getAreaName());
//		}
//		
//		
//		
//		obj.setAptAreaName(SelectedArea);
//		obj.setReportDate(CurrentSnag.getReportDate().toString());
//		obj.setSnagStatus(SelectedStatus);
//			
//			obj.setReInspectedUnresolvedDate(""+SelectedDate);
//			obj.setReInspectedUnresolvedDatePictureURL1(""+PhotoURl21);
//			obj.setReInspectedUnresolvedDatePictureURL2(""+PhotoURl22);
//			obj.setReInspectedUnresolvedDatePictureURL3(""+PhotoURl23);
//			obj.setResolveDate(""+SelectedDate);
//			obj.setResolveDatePictureURL1(""+PhotoURl31);
//			obj.setResolveDatePictureURL2(""+PhotoURl32);
//			obj.setResolveDatePictureURL3(""+PhotoURl33);
			if(isOnline){
			uploadImage(obj.getPictureURL1());
			uploadImage(obj.getPictureURL2());
			uploadImage(obj.getPictureURL3());
			uploadImage(obj.getReInspectedUnresolvedDatePictureURL1());
			uploadImage(obj.getReInspectedUnresolvedDatePictureURL2());
			uploadImage(obj.getReInspectedUnresolvedDatePictureURL3());
			uploadImage(obj.getResolveDatePictureURL1());
			uploadImage(obj.getResolveDatePictureURL1());
			uploadImage(obj.getResolveDatePictureURL1());
			}
		
		FMDBDatabaseAccess db=new FMDBDatabaseAccess(AddSnagCheckList.this);
		obj.setStatusForUpload("Inserted");
    	if(isOnline)
    		obj.setIsDataSyncToWeb(true);
    	else
    		obj.setIsDataSyncToWeb(false);
		db.UpdateIntoSnagMaster(obj);
		db=null;
		
		setResult(10005);//Finish current and previous only
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
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		// TODO Auto-generated method stub
		
	}
	public void SyncInspectionMaster(InspectionMaster obj){
		boolean result=false;
		try
		{
			SoapObject request=new SoapObject(NAMESPACE, METHOD_NAME_SaveNewDataToTheDataBase);
			SoapSerializationEnvelope envelope=new SoapSerializationEnvelope(SoapEnvelope.VER11);
			envelope.dotNet=true;
			envelope.setOutputSoapObject(request);
			
			  String CoumnNames="ID~ProjectID~BuildingID~FloorID~ApartmentID~AptAreaID~Jobtype~AreaType~CreatedBy~CreatedDate~ModifiedDate~EnteredOnMachineID";
			  String Values=""+obj.getID()+"~"+obj.getProjectID()+"~"+obj.getBuildingID()+"~"+obj.getFloorID()+"~"+obj.getApartmentID()+"~"+obj.getAptAreaID()+"~"+obj.getJobType()+"~"+obj.getAreaType()+"~"+obj.getCreatedBy()+"~"+obj.getCreatedDate()+"~"+obj.getModifiedBy()+"~"+obj.getModifiedDate()+"~"+obj.getEnteredOnMachineID();
			  String TableName="InspectionMaster";
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
		catch (Exception e)
		{
			Log.d("SyncToWeb", ""+e.getMessage());
		}
	}
	public void SyncInspectionDetails(InspectionDetails obj){
		boolean result=false;
		try
		{
			SoapObject request=new SoapObject(NAMESPACE, METHOD_NAME_SaveNewDataToTheDataBase);
			SoapSerializationEnvelope envelope=new SoapSerializationEnvelope(SoapEnvelope.VER11);
			envelope.dotNet=true;
			envelope.setOutputSoapObject(request);
			
			  String CoumnNames="InspectionDetailID~InspectionMasterID~ChecklistID~ChecklistDescription~ChecklistEntry~CreatedBy~CreatedDate~ModifiedBy~ModifiedDate~EnteredOnMachineID";
			  String Values=""+obj.getInspectionDetailID()+"~"+obj.getInspectionMasterID()+"~"+obj.getChecklistID()+"~"+obj.getChecklistDescription()+"~"+obj.getChecklistEntry()+"~"+obj.getCreatedBy()+"~"+obj.getCreatedDate()+"~"+obj.getModifiedBy()+"~"+obj.getModifiedDate()+"~"+obj.getEnteredOnMachineID();
			  String TableName="InspectionDetails";
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
        	new AlertDialog.Builder(AddSnagCheckList.this)
    	    
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
        	new AlertDialog.Builder(AddSnagCheckList.this)
    	    
    	    .setMessage("Are you sure you want to Logout?")
    	    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
    	        public void onClick(DialogInterface dialog, int which) { 
    	            FMDBDatabaseAccess db=new FMDBDatabaseAccess(AddSnagCheckList.this);
    	            SharedPreferences SP = getSharedPreferences("AppDelegate",MODE_PRIVATE);
    				db.performLogout(SP.getString("RegUserID", ""));
    				Intent i=new Intent(AddSnagCheckList.this,com.snagreporter.Login_page.class);
    				
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
}
