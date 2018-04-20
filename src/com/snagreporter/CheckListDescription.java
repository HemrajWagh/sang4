package com.snagreporter;

import java.util.ArrayList;
import java.util.List;

import com.snagreporter.ListItemNames.TwoTextArrayAdapter;
import com.snagreporter.ListItemNames.TwoTextArrayAdapter.RowType;
import com.snagreporter.database.FMDBDatabaseAccess;
import com.snagreporter.entity.BuildingMaster;
import com.snagreporter.entity.ProjectMaster;
import com.snagreporter.entity.SnagChecklistEntries;
import com.snagreporter.listitems.ChatItem;
import com.snagreporter.listitems.Header;
import com.snagreporter.listitems.Item;
import com.snagreporter.listitems.ListItemAddDefect;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class CheckListDescription extends Activity {
	SnagChecklistEntries []obj;
	ListView list;
	List<Item> items;
	TwoTextArrayAdapter adapter;
	int as_index=-1;
	String Status[];
	@Override
    public void onCreate(Bundle savedInstanceState)
    {
		super.onCreate(savedInstanceState);
		try{
       setContentView(R.layout.listitemnameschecklist);
       list=(ListView)findViewById(R.id.android_list_item_names);
       Status=new String[4];
       Status[0]="Not Done";
       Status[1]="Done";
       Status[2]="Override";
       Status[3]="NA";
       
       int count=(int )getIntent().getExtras().getInt("Count");
       obj=new SnagChecklistEntries[count];
       for(int i=0;i<count;i++){
    	   obj[i]=new SnagChecklistEntries();
    	   obj[i]=(SnagChecklistEntries)getIntent().getExtras().get("CheckList"+i);
       }
       continueprocess();
		}
		catch(Exception e)
		{
			Log.d("Exception",""+e.getMessage());
		}
    }
	public void continueprocess()
	{
		try{
		
		items = new ArrayList<Item>();
		//if(LoginType.equals(getResources().getString(R.string.strInspector).toString()))
		items.add(new Header(null, "CheckList",false,false,true,false));
		
			populatelist();
		
		adapter = new TwoTextArrayAdapter(this, items);
	     list.setAdapter(adapter);  
	     
	    list.setOnItemClickListener(new OnItemClickListener()
	    {

			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3)
			{
				try{
					registerForContextMenu(arg0);
		    		as_index=position;
		    		openContextMenu(arg0);
		    		unregisterForContextMenu(arg0);
				}
				catch(Exception e)
				{
					Log.d("Exception", ""+e.getMessage());
				}
			}
		});
		}
		catch(Exception e)
		{
			Log.d("Exception", ""+e.getMessage());
		}
	}
	 @Override  
	   public void onCreateContextMenu(ContextMenu menu, View v,ContextMenuInfo menuInfo)
{  
	super.onCreateContextMenu(menu, v, menuInfo);  
	
	
	    menu.setHeaderTitle("Select InspectionDescription");  
	    for(int i=0;i<Status.length;i++)
	    	menu.add(0, v.getId(), 0, ""+Status[i]);
	    	
	    	
	   
	
}
	public void populatelist()
	{
		 try{
		for(int i=0;i<obj.length;i++)
			items.add(new ListItemAddDefect(null, ""+obj[i].ChecklistDescription,""+obj[i].CheckListEntry,false));
			
		 }
			catch(Exception e)
			{
				Log.d("Exception", ""+e.getMessage());
			}
		 
	}
	@Override
	public boolean onContextItemSelected(MenuItem item)
	{
		int wantedPosition =as_index; // Whatever position you're looking for
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
		obj[as_index-1].CheckListEntry=""+item.getTitle().toString();
		}
		return true;
	
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
	public void SaveClick(View v){
		try{
			Intent in=new Intent();
			in.putExtra("Count", obj.length);
	  		for(int i=0;i<obj.length;i++)
	  			in.putExtra("CheckList"+i, obj[i]);
			//i.putExtra("CheckList",obj);
			setResult(RESULT_OK, in);
			finish();
		}
		catch(Exception e){
			Log.d("Error=", ""+e.getMessage());
		}
	}
}
