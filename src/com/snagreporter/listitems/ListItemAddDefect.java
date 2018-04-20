package com.snagreporter.listitems;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.snagreporter.R;;
public class ListItemAddDefect implements Item {
	private final String         str1;
	public String         str2;
    private final LayoutInflater inflater;
    public final boolean isDisabled;
    public ListItemAddDefect(LayoutInflater inflater, String head ,String text,boolean isDisabled) {
        this.str1 = head;
        this.inflater = inflater;
        this.str2=text;
        this.isDisabled=isDisabled;
    }
	
	public int getViewType() {
		// TODO Auto-generated method stub
		return 0;
	}

	
	public View getView(LayoutInflater inflater, View convertView) {
		// TODO Auto-generated method stub
		View view;
        if (convertView == null) {
        	if(!isDisabled)
        		view = (View) inflater.inflate(R.layout.row_cell_add_defect, null);
        	else
        		view = (View) inflater.inflate(R.layout.row_cell_add_defect_gray, null);
            // Do some initialization
        } else {
            view = convertView;
        }
        
        
        
        TextView text1 = (TextView) view.findViewById(R.id.row_cell_addDefect_head);
        TextView text2 = (TextView) view.findViewById(R.id.row_cell_addDefect_text);
        text1.setText(str1);
        text2.setText(str2);
       

        return view;
	}

}
