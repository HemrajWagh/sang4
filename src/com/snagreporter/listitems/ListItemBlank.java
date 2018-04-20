package com.snagreporter.listitems;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import com.snagreporter.R;;
public class ListItemBlank implements Item {
	private final String         str1;
	
    private final LayoutInflater inflater;
    public ListItemBlank(LayoutInflater inflater, String head) {
        this.str1 = head;
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
            view = (View) inflater.inflate(R.layout.row_cell_blank, null);
            // Do some initialization
        } else {
            view = convertView;
        }
        
        
        
        TextView text1 = (TextView) view.findViewById(R.id.row_cell_blank_text);
        
        text1.setText(str1);
        
       

        return view;
	}

}
