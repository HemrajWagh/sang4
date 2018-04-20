package com.snagreporter.listitems;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.snagreporter.R;;
public class Header2 implements Item{
	private final String         name;
	private final boolean hasRefresh,fromproject,fromDefect,hasEdit;
	
	
    public Header2(LayoutInflater inflater, String name,boolean hasRefresh,boolean fromprj,boolean fromDefect,boolean hasEdit)
    {
        this.name = name;
        this.hasRefresh=hasRefresh;
        this.fromproject=fromprj;
        this.fromDefect=fromDefect;
        this.hasEdit=hasEdit;
    }
	
	public int getViewType() {
		// TODO Auto-generated method stub
		return 1;
	}

	
	public View getView(LayoutInflater inflater, View convertView) {
		// TODO Auto-generated method stub
		View view;
        if (convertView == null) {
            view = (View) inflater.inflate(R.layout.row_cell_header2, null);
            // Do some initialization
        } else {
            view = convertView;
        }
        Button edit=(Button)view.findViewById(R.id.row_cell_header_editbtn);
       ImageView i=(ImageView)view.findViewById(R.id.header_editpen);
        TextView text = (TextView) view.findViewById(R.id.row_cell_header_text);
        text.setText(name);
      
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
        	
        }
        else if(fromDefect)
        {
        	edit.setVisibility(view.GONE);
        }
        else{
        	btn.setVisibility(View.INVISIBLE);
        	edit.setBackgroundColor(Color.parseColor("#00000000"));
        	edit.setText("");
        	i.setVisibility(View.VISIBLE);
        }
        
        if(!hasEdit){
        	edit.setVisibility(View.GONE);
        	i.setVisibility(View.GONE);
        	RelativeLayout r=(RelativeLayout)view.findViewById(R.id.header2_Rel);
        	r.setVisibility(View.GONE);
        }
        
        //TextView t=text;
        return view;
	}

}
