package com.snagreporter.listitems;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.snagreporter.R;;
public class Header implements Item{
	private final String         name;
	private final boolean hasRefresh,fromproject,fromDefect,hasAdd;
	
	
    public Header(LayoutInflater inflater, String name,boolean hasRefresh,boolean fromprj,boolean fromDefect,boolean hasAdd)
    {
        this.name = name;
        this.hasRefresh=hasRefresh;
        this.fromproject=fromprj;
        this.fromDefect=fromDefect;
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
            view = (View) inflater.inflate(R.layout.row_cell_header, null);
            // Do some initialization
        } else {
            view = convertView;
        }
        Button edit=(Button)view.findViewById(R.id.row_cell_header_editbtn);
       ImageView i=(ImageView)view.findViewById(R.id.header_editpen);
       ImageView i2=(ImageView)view.findViewById(R.id.header_add);
        TextView text = (TextView) view.findViewById(R.id.row_cell_header_text);
        text.setText(name);
        
        TextView text2 = (TextView) view.findViewById(R.id.addnewprojectText);
      
        Button btn=(Button)view.findViewById(R.id.row_cell_header_refresh);
        if(hasRefresh){
        	btn.setVisibility(View.VISIBLE);
        }
        else{
        	btn.setVisibility(View.GONE);
        }
        if(fromproject)
        {
        	edit.setText("Add New");
        	edit.setBackgroundColor(Color.parseColor("#00000000"));
        	edit.setText("");
        	i2.setVisibility(View.VISIBLE);
        	text2.setVisibility(View.VISIBLE);
        	
        	//new code 
        	text2.setVisibility(View.GONE);
        	i2.setVisibility(View.GONE);
        	i.setVisibility(View.GONE);
        	edit.setVisibility(View.GONE);
        	//
        }
        else if(fromDefect)
        {
        	edit.setVisibility(view.GONE);
        }
        else{
        	
        	edit.setBackgroundColor(Color.parseColor("#00000000"));
        	edit.setText("");
        	i.setVisibility(View.VISIBLE);
        }
        
        if(!hasAdd){
        	text2.setVisibility(View.GONE);
        	i2.setVisibility(View.GONE);
        	i.setVisibility(View.GONE);
        	edit.setVisibility(View.GONE);
        }
        //for all new code
        edit.setVisibility(View.GONE);
        i.setVisibility(View.GONE);
        //
        //TextView t=text;
        return view;
	}

}
