package com.snagreporter;

import java.util.ArrayList;
import java.util.List;

import com.snagreporter.AddDefect.TwoTextArrayAdapter;
import com.snagreporter.AddDefect.TwoTextArrayAdapter.RowType;
import com.snagreporter.database.FMDBDatabaseAccess;
import com.snagreporter.entity.Employee;
import com.snagreporter.entity.LoginData;
import com.snagreporter.listitems.ChatItem;
import com.snagreporter.listitems.Header;
import com.snagreporter.listitems.Item;
import com.snagreporter.menuhandler.MenuHandler;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class ListItemNames extends Activity 
{
	ListView list;
	List<Item> items;
	TwoTextArrayAdapter adapter;
	boolean isOnline=false;
	
	boolean isMenuVisible=false;
	MenuHandler menuhandler;
	String RegUserID="";
	String Fname,LName;
	LoginData[] arrLoginData;
	@Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listitemnames);
        
        SharedPreferences SP = getSharedPreferences("AppDelegate",MODE_PRIVATE);
        RegUserID=SP.getString("RegUserID", "");
        Boolean id=SP.getBoolean("isOnline", false);
        isOnline=id;
        
        list=(ListView)findViewById(R.id.android_list_item_names);
        
        continueprocess();
        
    }    
	public void continueprocess()
	{
		
		FMDBDatabaseAccess fmdb=new FMDBDatabaseAccess(this);
		arrLoginData=fmdb.getLoginData(RegUserID);
		items = new ArrayList<Item>();
		//if(LoginType.equals(getResources().getString(R.string.strInspector).toString()))
		items.add(new Header(null, "ChatList",false,false,true,false));
		if(arrLoginData!=null)
		{
			populatelist();
		}
		adapter = new TwoTextArrayAdapter(this, items);
	     list.setAdapter(adapter);  
	     
	    list.setOnItemClickListener(new OnItemClickListener()
	    {

			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3)
			{
				int location=position-1;
				String chatId=arrLoginData[location].getID();
				Intent i=new Intent(ListItemNames.this,com.snagreporter.chat.MessageActivity.class);
				i.putExtra("chatID",chatId);
				startActivityForResult(i,10001);
				//Log.d("position",""+position);
				//Toast.makeText(ListItemNames.this,""+position+" "+chatId,Toast.LENGTH_SHORT).show();
				
			}
		});
	}
	public void populatelist()
	{
		 
		for(int i=0;i<arrLoginData.length;i++)
		{
			if(arrLoginData[i].getFirstName()!=null && arrLoginData[i].getFirstName().length()>0)
			{
				Fname=arrLoginData[i].getFirstName();
			}
			else
			{
				Fname="";
			}
			if(arrLoginData[i].getLastName()!=null && arrLoginData[i].getLastName().length()>0)
			{
				LName=arrLoginData[i].getLastName();
			}
			else
			{
				LName="";
			}
			String strName=Fname+" "+LName;
			items.add(new ChatItem(null,strName));
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
	        	
	        		//View v=items.get(position).getView(mInflater, convertView);
	        		
	        		return items.get(position).getView(mInflater, null);
	        	
	        }
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
		        	new AlertDialog.Builder(ListItemNames.this)
		    	    
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
		        	new AlertDialog.Builder(ListItemNames.this)
		    	    
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
