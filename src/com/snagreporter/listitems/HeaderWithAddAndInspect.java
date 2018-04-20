package com.snagreporter.listitems;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.snagreporter.R;;
public class HeaderWithAddAndInspect implements Item{
	private final String         name;
	private final String         name2;
	private final boolean showButton;
    public HeaderWithAddAndInspect(LayoutInflater inflater, String name, String name2,boolean showButton) {
        this.name = name;
        this.name2 = name2;
        this.showButton=showButton;
    }
	
	public int getViewType() {
		// TODO Auto-generated method stub
		return 1;
	}

	
	public View getView(LayoutInflater inflater, View convertView) {
		// TODO Auto-generated method stub
		View view;
        if (convertView == null) {
            view = (View) inflater.inflate(R.layout.row_cell_header_add_inspect, null);
            // Do some initialization
        } else {
            view = convertView;
        }

        TextView text = (TextView) view.findViewById(R.id.row_cell_header_addtext);
        text.setText(name);
        
        if(!showButton){
        	Button btn1=(Button)view.findViewById(R.id.UpdateInspection_Button);
        	Button btn2=(Button)view.findViewById(R.id.AddDefectClick_Button);
        	btn1.setVisibility(View.GONE);
        	btn2.setVisibility(View.GONE);
        }
//        TextView text2 = (TextView) view.findViewById(R.id.row_cell_header_addtext2);
//        text2.setText(name2);
        
        //TextView t=text;
        return view;
	}

}
