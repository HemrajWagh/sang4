package com.snagreporter.listitems;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import com.snagreporter.R;;
public class ListItem implements Item {
	private final String         str1;
    //private final String         str2;
    private final LayoutInflater inflater;
    public ListItem(LayoutInflater inflater, String text1) {
        this.str1 = text1;
        //this.str2 = text2;
        this.inflater = inflater;
    }
	
	public int getViewType() {
		// TODO Auto-generated method stub
		return 0;
	}

	
	public View getView(LayoutInflater inflater, View convertView) {
		// TODO Auto-generated method stub
		View view;
        if (convertView == null) {
            view = (View) inflater.inflate(R.layout.row_cell, null);
            // Do some initialization
        } else {
            view = convertView;
        }

        TextView text1 = (TextView) view.findViewById(R.id.row_cell_text);
        
        text1.setText(str1);
       

        return view;
	}

}
