package com.snagreporter.listitems;

import com.snagreporter.R;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

public class ChatItem implements Item
{
	private final String name;
    
	public ChatItem(LayoutInflater inflater,String name)
	{
		this.name=name;
	}
	
	public int getViewType() {
		// TODO Auto-generated method stub
		return 1;
	}

	public View getView(LayoutInflater inflater, View convertView) 
	{
		View view;
		 if (convertView == null)
		 {
	            view = (View) inflater.inflate(R.layout.row_cell_listitem, null);
	            // Do some initialization
	     } 
		 else 
		 {
	            view = convertView;
	     }
		 TextView txt=(TextView)view.findViewById(R.id.row_cell_listitemtext);
		 txt.setText(name);
		 
		return view;
	}

}
