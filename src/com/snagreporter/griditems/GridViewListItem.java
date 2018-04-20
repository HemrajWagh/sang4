package com.snagreporter.griditems;

import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.snagreporter.R;;
public class GridViewListItem implements GridViewItem {
	private final String         str1;
    private final int level;
    private final LayoutInflater inflater;
    private final Bitmap url;
    private final int iSngCnt;
    private final boolean isInspStartd; 
    //private final int index;
    public GridViewListItem(LayoutInflater inflater, String text1,int level,Bitmap Image,int SngCnt,boolean isInspStrtd) {
        this.str1 = text1;
        //this.str2 = text2;
        this.inflater = inflater;
        this.level=level;
        this.url=Image;
        this.iSngCnt=SngCnt;
        this.isInspStartd=isInspStrtd;
      //  this.index=index;
    }
	
	public int getViewType() {
		// TODO Auto-generated method stub
		return 0;
	}

	public View getView(LayoutInflater inflater, View convertView) {
		// TODO Auto-generated method stub
		View view;
        if (convertView == null) {
            view = (View) inflater.inflate(R.layout.grid_cell, null);
            // Do some initialization
        } else {
            view = convertView;
        }

        TextView text1 = (TextView) view.findViewById(R.id.grid_cell_text);
        ImageView img=(ImageView)view.findViewById(R.id.grid_cell_image);
        ImageView imgInsp=(ImageView)view.findViewById(R.id.grdlst_imgInspStrtd);
        ImageView imgSng=(ImageView)view.findViewById(R.id.grdlst_imgSngCnt);
        TextView txtSncnt=(TextView)view.findViewById(R.id.grdlst_txtSngCnt);
        if(url!=null ){
        	
        	img.setImageBitmap(url);
        }
        else{
        	if(level==0){//Project
            	img.setImageResource(R.drawable.project);
            }
            else if(level==1){//Building
            	
            	img.setImageResource(R.drawable.building);
            }
            else if(level==2){//Floor
            	img.setImageResource(R.drawable.floor);
            	
            }
            else if(level==3){//Apartment
            	
            	img.setImageResource(R.drawable.apartment);
            }
            else if(level==4){//StdFloorArea
            	
            	img.setImageResource(R.drawable.apartment);
            }
        }
        
//        switch (index){
//        case 11:
//        	
//        	break;
//        	
//        }
         
        if(isInspStartd)
        	imgInsp.setVisibility(View.VISIBLE);
        else
        	imgInsp.setVisibility(View.INVISIBLE);
        if(iSngCnt>0)
        {
        	imgSng.setVisibility(View.VISIBLE);
        	txtSncnt.setText(""+iSngCnt);
        }
        else
        {
        	imgSng.setVisibility(View.INVISIBLE);
        	txtSncnt.setText("");
        }
        
        text1.setText(str1);
       

        return view;
	}

}
