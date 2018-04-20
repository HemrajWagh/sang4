package com.snagreporter.griditems;

import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.snagreporter.R;;
public class GridViewListItemProject implements GridViewItem {
	private final String         str1;
    private final int level;
    private final LayoutInflater inflater;
    private final Bitmap url;
    private final String about;
    private final Typeface font;
    private final Typeface font2;
    private int index;
    private final boolean IsInspStrd;
    private final int iSngCnt;
    
    public GridViewListItemProject(LayoutInflater inflater, String text1,int level,Bitmap Image,String about,Typeface font,int index,Typeface font2,int scnt,boolean inspStrd) {
        this.str1 = text1;
        //this.str2 = text2;
        this.inflater = inflater;
        this.level=level;
        this.url=Image;
        this.about=about;
        this.font=font;
        this.index=index;
        this.font2=font2;
        this.IsInspStrd=inspStrd;
        this.iSngCnt=scnt;
    }
	
	public int getViewType() {
		// TODO Auto-generated method stub
		return 0;
	}

	
	public View getView(LayoutInflater inflater, View convertView) {
		// TODO Auto-generated method stub
		View view;
        if (convertView == null) {
            view = (View) inflater.inflate(R.layout.grid_project_cell, null);
            // Do some initialization
        } else {
            view = convertView;
        }

        TextView text1 = (TextView) view.findViewById(R.id.grid_cell_text);
        ImageView img=(ImageView)view.findViewById(R.id.grid_cell_image);
        ImageView insImg=(ImageView)view.findViewById(R.id.prj_imgInspStrtd);
        ImageView sngImg=(ImageView)view.findViewById(R.id.prj_imgSngCnt);
        TextView txtsnCount=(TextView)view.findViewById(R.id.prjct_txtSngCnt);
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
        
//        if(index==0){
//        	img.setImageResource(R.drawable.apartment);
//        }
//        else if(index==1)
//        {
//        	img.setImageResource(R.drawable.apartment);
//        }
//        else{
//        	img.setImageResource(R.drawable.apartment);
//        }
        
        if(IsInspStrd)
        	insImg.setVisibility(View.VISIBLE);
        else
        	insImg.setVisibility(View.INVISIBLE);
        
        if(iSngCnt>0)
        {
        	txtsnCount.setText(""+iSngCnt);
        	sngImg.setVisibility(View.VISIBLE);
        }
        else
        {
        	txtsnCount.setText("");
        	sngImg.setVisibility(View.INVISIBLE);
        }
        
        text1.setText(str1);
       
      //  TextView text2 = (TextView) view.findViewById(R.id.grid_cell_desc);
       // text2.setText(about);
        text1.setTypeface(font2);
       // text2.setTypeface(font);

        return view;
	}

}
