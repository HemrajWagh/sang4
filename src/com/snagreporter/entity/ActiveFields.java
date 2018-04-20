package com.snagreporter.entity;

import com.snagreporter.R;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.snagreporter.listitems.Item;

public class ActiveFields implements Item{

	private final String  str1;
	 private final LayoutInflater inflater;
	 private final boolean hasTwoText;
	 private final String SecondString;
	public ActiveFields(LayoutInflater inflater,String name,boolean hasTwoText,String SecondString) 
	{
		 this.inflater = inflater;
		this.str1=name;
		this.hasTwoText=hasTwoText;
		this.SecondString=SecondString;
	}
	public int getViewType() {
		// TODO Auto-generated method stub
		return 0;
	}

	public View getView(LayoutInflater inflater, View convertView)
	{
		View view;
		 if (convertView == null)
		 {
			
				 view = (View) inflater.inflate(R.layout.row_cell_activefields,null);
//			 else
//				 view = (View) inflater.inflate(R.layout.row_cell_activefields2,null);
		 }
		 else
		 {
			 view = convertView;
		 }
		 
		 TextView txt=(TextView)view.findViewById(R.id.row_cell_activefields_text);
		 txt.setText(str1);
		 if(hasTwoText){
			 TextView txt2=(TextView)view.findViewById(R.id.row_cell_activefields_text2);
			 txt2.setText(""+SecondString);
		 }
			 
		 
		 return view;
	}

}
