package com.snagreporter;

import java.util.ArrayList;
import java.util.List;

import com.snagreporter.DefectListPage.TwoTextArrayAdapter;
import com.snagreporter.DefectListPage.TwoTextArrayAdapter.RowType;
import com.snagreporter.database.FMDBDatabaseAccess;
import com.snagreporter.database.ParseSyncData;
import com.snagreporter.entity.BuildingMaster;
import com.snagreporter.entity.FloorMaster;
import com.snagreporter.entity.ProjectMaster;
import com.snagreporter.entity.SnagMaster;
import com.snagreporter.listitems.Header;
import com.snagreporter.listitems.HeaderWithAddAndInspect;
import com.snagreporter.listitems.Item;
import com.snagreporter.listitems.ListItemBlank;
import com.snagreporter.listitems.ListItemParent;
import com.snagreporter.listitems.ListItemWithEdit;
import com.snagreporter.menuhandler.MenuHandler;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.view.ViewPager.LayoutParams;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class ContractorDefectList extends Activity 
{
	SnagMaster arrSnagM[];
	String RegUserID="";
	String LoginType="";
	View TopMenu;
	ProjectMaster currentProject;
	BuildingMaster currentBuilding;
	boolean isMenuVisible=false;
	MenuHandler menuhandler;
	ListView list;
	ListView list_parent;
	boolean isOnline=false;
	
	
	List<Item> items;
	List<Item> itemsParent;
	 @Override
	    public void onCreate(Bundle savedInstanceState)
	    {
		  super.onCreate(savedInstanceState);
		  
		  try{
	      requestWindowFeature(Window.FEATURE_NO_TITLE);
	      getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
	      setContentView(R.layout.defect_list_page);
	      
	      currentBuilding=(BuildingMaster)getIntent().getExtras().get("Building");
	      currentProject=(ProjectMaster)getIntent().getExtras().get("Project");
          SharedPreferences SP = getSharedPreferences("AppDelegate",MODE_PRIVATE);
	      RegUserID=SP.getString("RegUserID", "");
	      LoginType=SP.getString("LoginType", "");
	      
	      
	      
	         menuhandler=new MenuHandler(ContractorDefectList.this);
	         TopMenu=new View(this);
	         RelativeLayout.LayoutParams rlp=new RelativeLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
	         TopMenu.setLayoutParams(rlp);
	         LayoutInflater inflater= LayoutInflater.from(this);
	         TopMenu=(View) inflater.inflate(R.layout.popup_menu, null);
	         this.addContentView(TopMenu, rlp);
	         TopMenu.requestLayout();
	         TopMenu.setVisibility(View.INVISIBLE);
	         
	      
	      list=(ListView)findViewById(R.id.android_currentAPT_child);
	      list_parent=(ListView)findViewById(R.id.android_currentAPT_parent);
	      itemsParent = new ArrayList<Item>();
	      
	      populateCurrentBuilding();
	      
	      list_parent.setDivider(getResources().getDrawable(R.color.transparent));
	        list_parent.setDividerHeight(1);
	        
	        TwoTextArrayAdapter adapterParent = new TwoTextArrayAdapter(this, itemsParent);
	        list_parent.setAdapter(adapterParent);
	        
	        list_parent.setOnItemClickListener(new OnItemClickListener() {

	 			
	 			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
	 					long arg3) {
	 				// TODO Auto-generated method stub
	 				
	 					//Toast.makeText(getApplicationContext(), "list_project position="+position, Toast.LENGTH_LONG).show();;
	 			}
	 		});
		  }
			catch(Exception e)
			{
				Log.d("Exception", ""+e.getMessage());
			}
	    }
	 
	 @Override
	    public void onResume()
	   {
	    	super.onResume();
	    	try{
	    	list.setDivider(getResources().getDrawable(R.color.transparent));
	        list.setDividerHeight(1);
	        items = new ArrayList<Item>();
	        items.add(new HeaderWithAddAndInspect(null, "SNAG LIST","",false));
	        populateDefectsData();
	        
	        SharedPreferences sharedPref = getSharedPreferences("AppDelegate",MODE_PRIVATE);
	        Boolean id=sharedPref.getBoolean("isOnline", false);
	        isOnline=id;
	        
	        Button b=(Button)findViewById(R.id.topbar_statusbtn);
			ImageView i=(ImageView)findViewById(R.id.topbar_statusimage);
			if(isOnline)
			{
				b.setText("Go Offline");
				i.setBackgroundDrawable(getResources().getDrawable(R.drawable.green));
	    	}
	    	else
	    	{
	    		b.setText("Go Online");
	    		i.setBackgroundDrawable(getResources().getDrawable(R.drawable.status_online_icon));
	    	}
	    	}
			catch(Exception e)
			{
				Log.d("Exception", ""+e.getMessage());
			}
	   }
	    	
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
	        	new AlertDialog.Builder(ContractorDefectList.this)
	    	    
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
	        	new AlertDialog.Builder(ContractorDefectList.this)
	    	    
	    	    .setMessage("Are you sure you want to Logout?")
	    	    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
	    	        public void onClick(DialogInterface dialog, int which) { 
//	    	            FMDBDatabaseAccess db=new FMDBDatabaseAccess(BuildingListPage.this);
//	    	            SharedPreferences SP = getSharedPreferences("AppDelegate",MODE_PRIVATE);
//	    				db.performLogout(SP.getString("RegUserID", ""));
//	    				Intent i=new Intent(BuildingListPage.this,com.snagreporter.Login_page.class);
//	    				i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//	    				startActivity(i);
//	    				
//	    				finish();
	    	        	//if(!CameFromStartPage){
	    	        			setResult(10002);
	    	        			finish();
	    	          	//	}
	    	          		/*else{
	    	        			FMDBDatabaseAccess db=new FMDBDatabaseAccess(ContractorDefectList.this);
	    	                    SharedPreferences SP = getSharedPreferences("AppDelegate",MODE_PRIVATE);
	    	        			db.performLogout(SP.getString("RegUserID", ""));
	    	        			Intent i=new Intent(ContractorDefectList.this,com.snagreporter.Login_page.class);
	    	        			i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	    	        			startActivity(i);
	    	        			
	    	        			finish();
	    	        		}*/
	    	        	
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
	 public void ShowTopMenu()
	 {
			TopMenu.setVisibility(View.VISIBLE);
			isMenuVisible=true;
	 }
	 public void HideTopMenu()
	 {
			TopMenu.setVisibility(View.INVISIBLE);
			isMenuVisible=false;
	 }
	 public void TopMenuClick(View v)
	 {
			if(isMenuVisible)
			{
				HideTopMenu();
			}
			else
			{
				ShowTopMenu();
			}
	 }
	 public void TopMenuBGClick(View v)
	 {
			HideTopMenu();
			
	 }
	 public void MenuRoomsheetClick(View v)
	 {
			HideTopMenu();
			//menuhandler.MenuRoomsheetClick(CurrentFloor);
	 }
	 public void MenuReportsClick(View v)
	 {
			HideTopMenu();
			menuhandler.MenuReportsClick();
	 }
	 public void MenuSortFilterClick(View v)
	 {
			HideTopMenu();
			menuhandler.MenuSortFilterClick();
	 }
	 public void MenuChatClick(View v)
	 {
			HideTopMenu();
			menuhandler.MenuChatClick();
	 }
	 public void MenuAttendanceClick(View v){
			HideTopMenu();
			
			menuhandler.MenuAttendanceClick();
		}
	 public void MenuGraphClick(View v)
	 {
			HideTopMenu();
			//menuhandler.MenuGraphClick("Oneapartment",CurrentAPT);
	 }
	 public void MenuChartClick(View v)
	 {
			HideTopMenu();
			menuhandler.MenuChartClick();
	 }
	 public void MenuAttachClick(View v)
	 {
			HideTopMenu();
			menuhandler.MenuAttachClick();
	 }
	 
	 public void populateCurrentBuilding()
	 {
	 		try
	 		{
	 			itemsParent.add(new Header(null, "BUILDING",true,false,false,false));
	 			itemsParent.add(new ListItemParent(null, "Building",""+currentProject.getProjectName()+" - "+currentBuilding.getBuildingName()));
	 		}
	 		catch (Exception e)
	 		{
				Log.d("Exception populateCurrentBuilding",e.getMessage());
			}
	 }
	 	
	 	 public static class TwoTextArrayAdapter extends ArrayAdapter<Item>
	 	 {
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

	 	public void populateDefectsData()
	    {
	 		try
	 		{
	 			FMDBDatabaseAccess fmobj=new FMDBDatabaseAccess(ContractorDefectList.this);
	    		arrSnagM=null;
	 			arrSnagM=fmobj.getSnagsByContractorID(currentProject.getID(), currentBuilding.getID(), RegUserID);
	 			ContinueProcess();
	 			
	 		}
	 		catch (Exception e)
	 		{
				Log.d("exception populateDefectsData", e.getMessage());
			}
	    }
	 	public void ContinueProcess()
	 	{
	 	 try
	 	 {
	 		if(arrSnagM!=null && arrSnagM.length>0)
    		{
	 			for(int i=items.size()-1;i>0;i--)
    			{
    				//int j=items.size();
    				items.remove(i);
    			}
	 			for(int i=0;i<arrSnagM.length;i++)
	 			{
	 				int j=0;
	 				if(arrSnagM[i].getContractorStatus()!=null && arrSnagM[i].getContractorStatus().equalsIgnoreCase("Accepted"))
	 				{
	 					j=1;
	 				}
	 				else if(arrSnagM[i].getContractorStatus()!=null && arrSnagM[i].getContractorStatus().equalsIgnoreCase("Started"))
	 				{
	 					j=2;
	 				}
	 				else if(arrSnagM[i].getContractorStatus()!=null && arrSnagM[i].getContractorStatus().equalsIgnoreCase("Ended"))
	 				{
	 					j=3;
	 				}
	 				else if(arrSnagM[i].getContractorStatus()!=null && arrSnagM[i].getContractorStatus().equalsIgnoreCase("Not Accepted"))
	 				{
	 					j=4;
	 				}
	 				else  //@@for pending contractor status
	 				{
	 					j=5;
	 				}
	 				
	 				/*if(arrSnagM[i].getSnagStatus().equalsIgnoreCase("Pending"))
	 				{
    					j=1;
    				}
	 				else if(arrSnagM[i].getSnagStatus().equalsIgnoreCase("Reinspected & Unresolved"))
	 				{
    					j=2;
    				}
    				else if(arrSnagM[i].getSnagStatus().equalsIgnoreCase("Resolved"))
    				{
    					j=3;
    				}*/
	 				String strName;
	 				strName=""+arrSnagM[i].getFloor()+"-"+arrSnagM[i].getApartment();
	 				if(arrSnagM[i].getAptAreaName()!=null && arrSnagM[i].getAptAreaName().length()>0)
	 				{
	 					strName=strName+"-"+arrSnagM[i].getAptAreaName();
	 				}
	 				items.add(new ListItemWithEdit(null,""+strName,i,j,false,true));
	 			}
    		}
	 		else
	 		{
    			if(items.size()<=1)
    				items.add(new ListItemBlank(null, "No defects found"));
    		}
	 		reloadList();
	 	 }
	 	 catch (Exception e)
	 	 {
			Log.d("Exception",""+e.getMessage());
		 }
	 	 
	 	}

	 	 public void reloadList()
	 	 {
	 		 try{
	 		 TwoTextArrayAdapter adapter = new TwoTextArrayAdapter(this, items);
	         list.setAdapter(adapter);
	         list.setOnItemClickListener(new OnItemClickListener() {

				public void onItemClick(AdapterView<?> arg0, View arg1,
						int position, long arg3) 
				{
					
				//	Toast.makeText(ContractorDefectList.this,"post"+" "+position,Toast.LENGTH_SHORT).show();
//					if(arrSnagM!=null && arrSnagM.length>0)
//					{
//						Intent i;
//						 i=new Intent(ContractorDefectList.this,com.snagreporter.EditDefectChageContractorStatus.class);
//						 i.putExtra("isAptmt", true);
//						 i.putExtra("Snag", arrSnagM[position]);
//						 startActivity(i);
//					}
					
					// TODO Auto-generated method stub
					
				}
			});
	 		}
	 		catch(Exception e)
	 		{
	 			Log.d("Exception", ""+e.getMessage());
	 		}
	 		 
	 	 }
	 	 public void EditCellTextClick(View v)
	 	 {
	 		//Toast.makeText(ContractorDefectList.this,"post"+" Edit cell text",Toast.LENGTH_SHORT).show();
	 		 try{
	 		int index=Integer.parseInt(v.getTag().toString());
	 		if(arrSnagM!=null && arrSnagM.length>0)
				{
	 				 Intent i;
					 i=new Intent(ContractorDefectList.this,com.snagreporter.EditDefectChageContractorStatus.class);
					 i.putExtra("isAptmt", true);
					 i.putExtra("Snag", arrSnagM[index]);
					 startActivity(i);
				}
	 		}
	 		catch(Exception e)
	 		{
	 			Log.d("Exception", ""+e.getMessage());
	 		}
	 	 }
	 	 
	 	public void BackClick(View v)
	 	{
	    	finish();
	    }
	 	public void ChangeStatusClick(View v)
	 	{
	 		try{	
	    		Button b=(Button)findViewById(R.id.topbar_statusbtn);
	    		ImageView i=(ImageView)findViewById(R.id.topbar_statusimage);
	    		if(isOnline)
	    		{
	    			
	    			b.setText("Go Online");
	    			i.setBackgroundDrawable(getResources().getDrawable(R.drawable.status_online_icon));
	        		isOnline=false;
	        		SharedPreferences sharedPref = getSharedPreferences("AppDelegate",MODE_PRIVATE);
	                SharedPreferences.Editor prefEditor = sharedPref.edit();
	                prefEditor.putBoolean("isOnline",false);
	                prefEditor.commit();
	        	}
	        	else
	        	{
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
	 	    
	 	public void SyncClick(View v)
	 	{
	 		try
	 		{
	 			if(isOnline)
	 			{
            		SyncData();
            	}
	 			else
	 			{
                     new AlertDialog.Builder(ContractorDefectList.this)
    	    	    
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
	 		catch (Exception e)
	 		{
				Log.d("Exception SyncClick",e.getMessage());
			}
	 	}
	 	
	 	public void SyncData()
	 	{
	 		try{
	 		ParseSyncData parser=new ParseSyncData(ContractorDefectList.this);
    		parser.start();
	 		}
			catch(Exception e)
			{
				Log.d("Exception", ""+e.getMessage());
			}
	 	}
	 	
	 	
	 	 @Override
	 	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	 		 try{
	 		 if(resultCode==10001)
	 		 {
	 	    		setResult(10001);
	 	    		finish();
	 	     }
	 		 else if(resultCode==10002)
	 		 {
	 			 setResult(10002);
	  			finish();
	 		 }
	 		}
	 		catch(Exception e)
	 		{
	 			Log.d("Exception", ""+e.getMessage());
	 		}	
	 		super.onActivityResult(requestCode, resultCode, data);
	 	}
}
