package com.snagreporter;

import com.snagreporter.database.FMDBDatabaseAccess;
import com.snagreporter.entity.BuildingMaster;
import com.snagreporter.entity.Inspector;
import com.snagreporter.entity.JobType;
import com.snagreporter.entity.ProjectMaster;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.Button;
import android.widget.CheckBox;

public class SelectReportPage extends Activity 
{
	Button btnPrj,btnBuild,btnSnType,btnSnSts,btnInsp;
	ProjectMaster[] arrPrj;
	String currentProject;
	String currentBuilding;
	String currentSnagType,currentSnagStatus,currentInspector;
	String RegUserID;
	String AllocByMe,ChckByMe;
	JobType[] arrJob;
	Inspector[] arrInsp;
	
	BuildingMaster[] arrBuild;
	int index;
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // making it full screen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.select_report);
        btnPrj=(Button)findViewById(R.id.select_report_prjname);
        btnBuild=(Button)findViewById(R.id.select_report_buildingname);
        btnSnSts=(Button)findViewById(R.id.select_report_snagStatus);
        btnSnType=(Button)findViewById(R.id.select_report_snagtype);
        //btnInsp=(Button)findViewById(R.id.select_report_inspctr);
        SharedPreferences SP = getSharedPreferences("AppDelegate",MODE_PRIVATE);
        RegUserID=SP.getString("RegUserID", "");
        currentProject="All";
        currentBuilding="All";
        currentSnagType="All";
        currentSnagStatus="All";
        currentInspector="All";
        AllocByMe="All";
        ChckByMe="All";
        btnPrj.setText("All");
        btnBuild.setText("All");
        btnSnSts.setText("All");
        btnSnType.setText("All");
        //btnInsp.setText("All");
        populateproject();
        
	}
	public void populateproject()
	{
		FMDBDatabaseAccess fmdb=new FMDBDatabaseAccess(this);
		arrPrj=fmdb.getProjects();
	}
	public void projectNameClick(View v)
	{
		index=0;
		registerForContextMenu(v);
		openContextMenu(v);
		unregisterForContextMenu(v);
	}
	public void buildingNameClick(View v)
	{
		index=1;
		registerForContextMenu(v);
		openContextMenu(v);
		unregisterForContextMenu(v);
		
	}
	public void snagTypeClick(View v)
	{
		index=2;
		registerForContextMenu(v);
		openContextMenu(v);
		unregisterForContextMenu(v);
		
	}
	public void snagStatusClick(View v)
	{
		index=3;
		registerForContextMenu(v);
		openContextMenu(v);
		unregisterForContextMenu(v);
		
	}
	public void selectInspectorClick(View v)
	{
		index=4;
		registerForContextMenu(v);
		openContextMenu(v);
		unregisterForContextMenu(v);
	}
	public void selectBuilderClick(View v)
	{
		index=5;
		registerForContextMenu(v);
		openContextMenu(v);
		unregisterForContextMenu(v);
	}
	public void CancelClick(View v)
	{
		finish();
	}
	@Override  
	  public void onCreateContextMenu(ContextMenu menu, View v,ContextMenuInfo menuInfo)
	  {
		 super.onCreateContextMenu(menu, v, menuInfo);
		 if(index==0)
		 {
			 menu.setHeaderTitle("Select Project");
			 menu.add(0, 0, 0,"All");
			
			 for(int i=0;i<arrPrj.length;i++)
			 {
				 menu.add(0,(i+1), 0,arrPrj[i].getProjectName());
			 }
		 }
		 else if(index==1)
		 {
			 if(currentProject!=null)
			 {
				 FMDBDatabaseAccess fdb=new FMDBDatabaseAccess(this);
				 arrBuild=fdb.getBuildingsByprj(currentProject);
				 menu.setHeaderTitle("Select Building");
				 menu.add(0, 0, 0,"All");
				 for(int i=0;i<arrBuild.length;i++)
				 {
					 menu.add(0,(i+1), 0,arrBuild[i].getBuildingName());
						
				 }
				 
			 }
		 }
		 else if(index==2)
		 {
			 FMDBDatabaseAccess fdb=new FMDBDatabaseAccess(this);
			 arrJob=fdb.getJobType();
			 
			 menu.setHeaderTitle("Select SnagType");
			 menu.add(0, v.getId(), 0,"All");
			 for(int i=0;i<arrJob.length;i++)
			 {
				 menu.add(0, v.getId(), 0,arrJob[i].getJobType());
					
			 }
			 
		 }
		 else if(index==3)
		 {
			 menu.setHeaderTitle("Select SnagStatus");
			 menu.add(0, v.getId(), 0,"All");
			 menu.add(0, v.getId(), 0,"PENDING");
			 menu.add(0, v.getId(), 0,"REINSPECTED & UNRESOLVED");
			 menu.add(0, v.getId(), 0,"RESOLVED");
		 }
		 else if(index==4)
		 {
			 FMDBDatabaseAccess fm=new FMDBDatabaseAccess(this);
			 arrInsp=fm.getInspector();
			 menu.setHeaderTitle("Select Inspector");
			 menu.add(0, v.getId(), 0,"All");
			 for(int i=0;i<arrInsp.length;i++)
			 {
				 menu.add(0,v.getId(),0,arrInsp[i].getInspectorName());
			 }
			 
		 }
		 else if(index==5)
		 {
			 
		 }
		
	  }
	@Override
	  public boolean onContextItemSelected(MenuItem item)
	 {
		///AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();

		if(index==0)
		{
			int  iID= item.getItemId();
			if(iID==0)
			{
				currentProject="All";
			}
			else
			{
				currentProject=arrPrj[(iID-1)].getID();
			}
			
			String strTxt=item.getTitle().toString();
			btnPrj.setText(strTxt);
			currentBuilding="All";
			btnBuild.setText("All");
			
		}
		else if(index==1)
		{
			int  iID= item.getItemId();
			if(iID==0)
			{
				currentBuilding="All";
			}
			else
			{
				currentBuilding=arrBuild[(iID-1)].getID();
			}
			String strTxt=item.getTitle().toString();
			btnBuild.setText(strTxt);
		}
		else if(index==2)
		{
			currentSnagType=item.getTitle().toString();
			btnSnType.setText(currentSnagType);
		}
		else if(index==3)
		{
			currentSnagStatus=item.getTitle().toString();
			btnSnSts.setText(currentSnagStatus);
		}
		else if(index==4)
		{
			//currentInspector=item.getTitle().toString();
			//btnInsp.setText(currentInspector);
			
		}
		return true;
	 }
	public void onCheckClick(View v)
	{
		 boolean checked = ((CheckBox)v).isChecked();
		switch (v.getId())
		{
		case R.id.select_report_allocByme:
				if(checked)
				{
					AllocByMe=RegUserID;
				}
				else
				{
					AllocByMe="All";
				}
			break;
		case R.id.select_report_chckdByme:
				if(checked)
				{
					ChckByMe=RegUserID;
				}
				else
				{
					ChckByMe="All";
				}
			
			break;

		default:
			break;
		}
	}
	public void SubmitClick(View v)
	{
		Intent i=new Intent(this,com.snagreporter.ReportTable.class);
		i.putExtra("project", currentProject);
		i.putExtra("building",currentBuilding);
		i.putExtra("snagType",currentSnagType);
		i.putExtra("snagStatus",currentSnagStatus);
		i.putExtra("inspector",currentInspector);
		i.putExtra("AllocBy",AllocByMe);
		i.putExtra("ChckBy", ChckByMe);
		startActivityForResult(i, 10001);
	}
	
	///@@optionsMenu
	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
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
	        	new AlertDialog.Builder(SelectReportPage.this)
	    	    
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
	        	new AlertDialog.Builder(SelectReportPage.this)
	    	    
	    	    .setMessage("Are you sure you want to Logout?")
	    	    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
	    	        public void onClick(DialogInterface dialog, int which) { 
	    	            /*FMDBDatabaseAccess db=new FMDBDatabaseAccess(SelectReportPage.this);
	    	            SharedPreferences SP = getSharedPreferences("AppDelegate",MODE_PRIVATE);
	    				db.performLogout(SP.getString("RegUserID", ""));
	    				Intent i=new Intent(SelectReportPage.this,com.snagreporter.Login_page.class);
	    				i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	    				startActivity(i);
	    				
	    				finish();*/
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
	 @Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
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
		    	
		super.onActivityResult(requestCode, resultCode, data);
	}
}
