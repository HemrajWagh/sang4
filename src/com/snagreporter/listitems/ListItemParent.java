package com.snagreporter.listitems;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import com.snagreporter.R;;
public class ListItemParent implements Item {
	private final String         str1;
	private final String         str2;
    private final LayoutInflater inflater;
    public ListItemParent(LayoutInflater inflater, String head ,String text) {
        this.str1 = head;
        //this.str2 = text2;
        this.inflater = inflater;
        this.str2=text;
    }
	
	public int getViewType() {
		// TODO Auto-generated method stub
		return 0;
	}

	
	public View getView(LayoutInflater inflater, View convertView) {
		// TODO Auto-generated method stub
		View view;
        if (convertView == null) {
            view = (View) inflater.inflate(R.layout.row_cell_parent, null);
            // Do some initialization
        } else {
            view = convertView;
        }
        
        
        
        TextView text1 = (TextView) view.findViewById(R.id.row_cell_parent_head);
        TextView text2 = (TextView) view.findViewById(R.id.row_cell_parent_text);
        text1.setText(str1);
        text2.setText(str2);
       

        return view;
	}

}
