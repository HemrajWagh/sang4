package com.snagreporter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.xml.datatype.Duration;

import org.w3c.dom.Text;

import com.snagreporter.database.FMDBDatabaseAccess;
import com.snagreporter.entity.ApartmentMaster;
import com.snagreporter.entity.BuildingMaster;
import com.snagreporter.entity.FloorMaster;
import com.snagreporter.entity.ProjectMaster;
import com.snagreporter.entity.SnagMaster;

import android.R.color;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.TableRow.LayoutParams;
import android.widget.Toast;
public class ReportTable extends Activity
{
	List<String[]> addresses;
	 int colCount;
	 TableLayout tbl,tblhdr;
	 int index=0;
	 int noOfRows=10;
	 int noOfCols=6;
	 SnagMaster[] arr;
	 String []strColValue;
	 String []strValue2;
	 String []strValue3;
	 String []set;
	 boolean isFromContractor=false;
	 ProjectMaster currentproject;
	 ApartmentMaster currentapt;
	 BuildingMaster currentbuilding;
	 FloorMaster currentfloor;
	 Boolean isDesc=false;
	 SQLiteDatabase mydb;
	 String prjName="",buildName="",strSngType="",sngSts="",strInspct="";
	 String AllocByMe="",ChckdByMe="";
	TextView txtData;	
	 private static String DBNAME = "SnagReporter.db";

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // making it full screen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.reporttable);
     //    tbl=(TableLayout)findViewById(R.id.maintable);
        tbl=(TableLayout)findViewById(R.id.reporttable_maintable);
        txtData=(TextView)findViewById(R.id.reporttable_txtnodata);
       
        isFromContractor=getIntent().getBooleanExtra("fromContractor",false);
       
       if(isFromContractor)
       {
    	   
       }
       else
       {
    	   prjName=getIntent().getExtras().getString("project");
           buildName=getIntent().getExtras().getString("building");
           strSngType=getIntent().getExtras().getString("snagType");
           sngSts=getIntent().getExtras().getString("snagStatus");
           strInspct=getIntent().getExtras().getString("inspector");
           AllocByMe=getIntent().getExtras().getString("AllocBy");
           ChckdByMe=getIntent().getExtras().getString("ChckBy");
   	    
       }
	  
        FMDBDatabaseAccess fdb=new FMDBDatabaseAccess(this);
	   addresses=fdb.getSnagsByAsc(prjName,buildName,strSngType,sngSts,strInspct,ChckdByMe,AllocByMe,"asc", "ID");
	   if(addresses!=null && addresses.size()>1)
	   {
		   int size=addresses.size();
		   strColValue=addresses.get(size-1);
		   colCount=strColValue.length;
		   txtData.setText("Snag Report");
		   txtData.setVisibility(View.VISIBLE);
			populateheader();
	    	populatetable();
	   }
	   else
	   {
			txtData.setVisibility(View.VISIBLE);
	   }
	   // arr=fdb.getSnags(currentproject.getID(), currentbuilding.getID(),currentfloor.getID(),currentapt.getID());
	   // arr=getSnagsByAsc("PRJ-000002","BLD-000003","BDD-000013","FLD-000019","","ID");
	    // noOfRows=arr.length;
	    //noOfCols=6;
//	    if(addresses.size()<2)
//	    {
//	    
//	    	
//	    }
//	    else
//	    {
//	    
//	    }
	}
	public void populateheader()
	{
		
		
	  
	  tblhdr=(TableLayout)findViewById(R.id.reporttable_mainHeader);
	  TableRow tr1=new TableRow(this);
	  TableLayout.LayoutParams tableRowParams2 = new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT);
      tr1.setLayoutParams(tableRowParams2);
      
      for(int i=0;i<colCount;i++)
      {
    	  
    	  View vh = new View(this);
	     	  vh.setLayoutParams(new TableRow.LayoutParams(1,TableRow.LayoutParams.FILL_PARENT));
	     	  vh.setBackgroundColor(Color.rgb(51, 51, 51));
	     	  
    	  
    	  TextView txt1 = new TextView(this);
    	  txt1.setText(strColValue[i]);
    	  txt1.setGravity(Gravity.CENTER);
    	  txt1.setId(i);
    	  txt1.setTextColor(Color.BLACK);
    	  txt1.setTextSize(15);//@@
    	  txt1.setTypeface(null, Typeface.BOLD);
    	  txt1.setLayoutParams(new LayoutParams(120,50));
    	  ImageView img=new ImageView(this);
    	  img.setBackgroundResource(R.drawable.arrowup);
    	  img.setId(i+100);
    	  img.setLayoutParams(new LayoutParams(20,20));
    	  txt1.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v)
			{
				View myV=v;
				int id=myV.getId();
				String sortBy=strColValue[id];
				FMDBDatabaseAccess fdb=new FMDBDatabaseAccess(ReportTable.this);
				 
				if(isDesc)
				{
					isDesc=false;
					tbl.removeAllViews();
					// arr=getSnagsByAsc("PRJ-000002","BLD-000003","BDD-000013","FLD-000019","asc",sortBy);
					   addresses=fdb.getSnagsByAsc(prjName,buildName,strSngType,sngSts,strInspct,ChckdByMe,AllocByMe,"asc",sortBy);
//					   if(addresses!=null)
//					   {
//						   int size=addresses.size();
//						   strColValue=addresses.get(size-1);
//						   colCount=strColValue.length;
//					   }
					   for(int i=0;i<colCount;i++)
					   {
						   TextView txt=(TextView)findViewById(i);
						   if(i==id)
						   {
							   txt.setTextColor(Color.WHITE);
						   }
						   else
						   {
							   txt.setTextColor(Color.BLACK);
						   }
						   ImageView myImg=(ImageView)findViewById(i+100);
						   myImg.setBackgroundResource(R.drawable.arrowup);
					   }
					
					 populatetable();
					
				}
				else
				{
					isDesc=true;
					tbl.removeAllViews();
					// arr=getSnagsByAsc("PRJ-000002","BLD-000003","BDD-000013","FLD-000019","desc",sortBy);
					addresses=fdb.getSnagsByAsc(prjName,buildName,strSngType,sngSts,strInspct,ChckdByMe,AllocByMe,"desc",sortBy);
					 for(int i=0;i<colCount;i++)
					   {
						   TextView txt=(TextView)findViewById(i);
						   if(i==id)
						   {
							   txt.setTextColor(Color.WHITE);
						   }
						   else
						   {
							   txt.setTextColor(Color.BLACK);
						   }
						   ImageView myImg=(ImageView)findViewById(i+100);
						   myImg.setBackgroundResource(R.drawable.arrowdown);
					   }
				
					populatetable();
				}
				
			}
		});
    	  if(i==0)
    	  {
    		  View vhd1 = new View(this);
  	     	  vhd1.setLayoutParams(new TableRow.LayoutParams(1,TableRow.LayoutParams.FILL_PARENT));
  	     	  vhd1.setBackgroundColor(Color.rgb(51, 51, 51));
  	     	  tr1.addView(vhd1);
    	  }
    	  tr1.addView(txt1);
    	  tr1.addView(img);
    	  tr1.addView(vh);
    	  
      }
   	  
      tblhdr.addView(tr1);
	 
		  
	}
	public String[] populaterowdata(int i)
	{
		String[] str=null;
		//Log.d("adreesssize",""+addresses.size());
		if(i<addresses.size())
		{
			str=addresses.get(i);
		}
		return str;
	}
	public void populatetable()
	{
         for(int i=0;i<(addresses.size()-1);i++) //for row values
         {
        		
       	  View v = new View(this);
       	  v.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, 1));
       	  v.setBackgroundColor(Color.rgb(51, 51, 51));

        	TableRow tr=new TableRow(this);
        	TableLayout.LayoutParams tableRowParams = new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT);
           // tableRowParams.setMargins(3, 3,2,10);
        	tr.setLayoutParams(tableRowParams);
           // tr.setBackgroundResource(R.drawable.row_border);
            	//set=strValue2;
             if(i==0)
             {
               	 View v3 = new View(this);
                	  v3.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, 1));
                   	  v3.setBackgroundColor(Color.rgb(51, 51, 51));
                   	  tbl.addView(v3);
             }
             String strrw[]=populaterowdata(i);
             for(int j=0;j<colCount;j++)
             {
            	 
            	 View vh1= new View(this);
   	     	  	 vh1.setLayoutParams(new TableRow.LayoutParams(1,TableRow.LayoutParams.FILL_PARENT));
   	     	  	 vh1.setBackgroundColor(Color.rgb(51, 51, 51));
   	     	  	 if(j==0)
   	     	  	 {
   	     	  		 View vh2= new View(this);
   	     	  		 vh2.setLayoutParams(new TableRow.LayoutParams(1,TableRow.LayoutParams.FILL_PARENT));
   	     	  		 vh2.setBackgroundColor(Color.rgb(51, 51, 51));
   	     	  		 tr.addView(vh2);
   	     	  	 }
            	 TextView txt1 = new TextView(this);
            	 txt1.setText(strrw[j]);
            	 txt1.setTextSize(15);
            	 txt1.setGravity(Gravity.CENTER);
            	 txt1.setTextColor(Color.BLACK);
            	 txt1.setLayoutParams(new LayoutParams(140,50));
            	 tr.addView(txt1);
            	 tr.addView(vh1);
            	 
             }
          
             tbl.addView(tr);//,new TableLayout.LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT));
             tbl.addView(v);
             
         }
         
         
         
        
		
		
		
		
		
		
	}
	/*public SnagMaster[] getSnagsByAsc(String prjID,String bldgID,String flrID,String aptmtID,String strBy,String sortColumn)
	{
		SnagMaster arr[]=null;
		try{
			mydb=openOrCreateDatabase(DBNAME, Context.MODE_PRIVATE, null);
			String strQuery;
			if(strBy.equalsIgnoreCase("desc"))
			{
				strQuery = "Select ID,SnagType,Floor,AptAreaName,FaultType from SnagMaster where (ProjectID='"+prjID+"' and BuildingID='"+bldgID+"' and FloorID='"+flrID+"' and ApartmentID='"+aptmtID+"')  Order by "+sortColumn+" desc";
		    }
			else
			{
				strQuery = "Select ID,SnagType,Floor,AptAreaName,FaultType from SnagMaster where (ProjectID='"+prjID+"' and BuildingID='"+bldgID+"' and FloorID='"+flrID+"' and ApartmentID='"+aptmtID+"')  Order by "+sortColumn+" asc";
			}
			Cursor cursor=mydb.rawQuery(strQuery, null);
			colCount=cursor.getColumnCount();
			strColValue=cursor.getColumnNames();
	        arr=new SnagMaster[cursor.getCount()];
	        addresses = new ArrayList<String[]>();
	        

	        int i=0;
	        if(cursor.moveToFirst()){
	        	do{
	        		SnagMaster obj=new SnagMaster();
	        		String[] addressesArr  = new String[colCount];
	        		addressesArr[0]=cursor.getString(cursor.getColumnIndex("ID"));
	        		addressesArr[1]=cursor.getString(cursor.getColumnIndex("SnagType"));
	        		addressesArr[2]=cursor.getString(cursor.getColumnIndex("Floor"));
	        		addressesArr[3]=cursor.getString(cursor.getColumnIndex("AptAreaName"));
	        		addressesArr[4]=cursor.getString(cursor.getColumnIndex("FaultType"));
	        		
	        		obj.setID(cursor.getString(cursor.getColumnIndex("ID")));
	        		obj.setSnagType(cursor.getString(cursor.getColumnIndex("SnagType")));
	        		obj.setFloor(cursor.getString(cursor.getColumnIndex("Floor")));
	        		obj.setAptAreaName(cursor.getString(cursor.getColumnIndex("AptAreaName")));
//	        		
	        		arr[i]=obj;
	        		addresses.add(addressesArr);
	        		i++;
	        	}
	        	while(cursor.moveToNext());
	        }
	        cursor.close();
	        mydb.close();
	        
		}
		catch(Exception e)
		{
			Log.d("Error getSnags=",""+e.getMessage());
		}
		return arr;
	}*/

	
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
	        	new AlertDialog.Builder(ReportTable.this)
	    	    
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
	        	new AlertDialog.Builder(ReportTable.this)
	    	    
	    	    .setMessage("Are you sure you want to Logout?")
	    	    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
	    	        public void onClick(DialogInterface dialog, int which) { 
	    	           /* FMDBDatabaseAccess db=new FMDBDatabaseAccess(ReportTable.this);
	    	            SharedPreferences SP = getSharedPreferences("AppDelegate",MODE_PRIVATE);
	    				db.performLogout(SP.getString("RegUserID", ""));
	    				Intent i=new Intent(ReportTable.this,com.snagreporter.Login_page.class);
	    				i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	    				startActivity(i);*/
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
}

