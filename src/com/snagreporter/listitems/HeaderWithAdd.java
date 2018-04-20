package com.snagreporter.listitems;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.snagreporter.AddSnagCheckList;
import com.snagreporter.R;;
public class HeaderWithAdd implements Item{
	private final String         name;
	private final String         name2;
private final boolean hasAdd;
    public HeaderWithAdd(LayoutInflater inflater, String name, String name2,boolean hasAdd) {
        this.name = name;
        this.name2 = name2;
        this.hasAdd=hasAdd;
    }
	
	public int getViewType() {
		// TODO Auto-generated method stub
		return 1;
	}

	
	public View getView(LayoutInflater inflater, View convertView) {
		// TODO Auto-generated method stub
		View view;
        if (convertView == null) {
            view = (View) inflater.inflate(R.layout.row_cell_header_add, null);
            // Do some initialization
        } else {
            view = convertView;
        }

        TextView text = (TextView) view.findViewById(R.id.row_cell_header_addtext);
        text.setText(name);
        TextView text2 = (TextView) view.findViewById(R.id.row_cell_header_addtext2);
        text2.setText(name2);
        ImageButton b=(ImageButton)view.findViewById(R.id.row_cell_header_addbtn);
        
        //new code
        text2.setVisibility(View.GONE);
    	b.setVisibility(View.GONE);
        //
        
    	if(!hasAdd){
        	text2.setVisibility(View.GONE);
        	b.setVisibility(View.GONE);
        }
        
        //TextView t=text;
        return view;
	}

}
