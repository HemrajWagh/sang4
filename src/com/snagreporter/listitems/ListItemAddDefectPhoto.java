package com.snagreporter.listitems;

import java.io.File;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.snagreporter.R;
import com.snagreporter.R.drawable;
public class ListItemAddDefectPhoto implements Item {
	private final String        photoUrl1,photoUrl2,photoUrl3,str1;
	public ImageButton         url1,url2,url3;
    private final LayoutInflater inflater;
    private final int tag;
    private final Boolean isforLoad;
    private final boolean isDisabled;
    public ListItemAddDefectPhoto(LayoutInflater inflater, String head ,ImageButton url1,ImageButton url2,ImageButton url3,int tag,Boolean isforLoad,String photoUrl1,String photoUrl2,String photoUrl3,boolean isDisabled)
    {
        this.str1 = head;
        this.inflater = inflater;
        this.url1=url1;
        this.url2=url2;
        this.url3=url3;
        this.tag=tag;
        this.isforLoad=isforLoad;
        this.photoUrl1=photoUrl1;
        this.photoUrl2=photoUrl2;
        this.photoUrl3=photoUrl3;
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
            view = (View) inflater.inflate(R.layout.row_cell_add_defect_photo, null);
        	else
        		view = (View) inflater.inflate(R.layout.row_cell_add_defect_photo_gray, null);
            // Do some initialization
        } else {
            view = convertView;
        }
        //view = (View) inflater.inflate(R.layout.row_cell_add_defect_photo, null);
        
        
        TextView text1 = (TextView) view.findViewById(R.id.row_cell_addDefectPhoto_head);
        ImageButton btn1 = (ImageButton) view.findViewById(R.id.row_cell_addDefectPhoto_img1);
        btn1.setTag(tag);
        ImageButton btn2 = (ImageButton) view.findViewById(R.id.row_cell_addDefectPhoto_img2);
        btn2.setTag(tag);
        ImageButton btn3 = (ImageButton) view.findViewById(R.id.row_cell_addDefectPhoto_img3);
        btn3.setTag(tag);
        ProgressBar pbar=(ProgressBar)view.findViewById(R.id.row_cell_progress);
        text1.setText(str1);
        ProgressBar pbar2=(ProgressBar)view.findViewById(R.id.row_cell_progress2);
        ProgressBar pbar3=(ProgressBar)view.findViewById(R.id.row_cell_progress3);
        Bitmap BtnImageBmp;
        if(url1!=null)
        {
        	
//			
//        	
//        	BtnImageBmp=BitmapFactory.decodeFile(url1);
//        	
//        	btn1.setImageBitmap(BtnImageBmp);
        	//url1.buildDrawingCache();
        	//BtnImageBmp=url1.getDrawingCache();
        	//btn1.setBackgroundColor(Color.TRANSPARENT);
        	//btn1.setImageBitmap(BtnImageBmp);
        	btn1.setBackgroundDrawable(url1.getDrawable());
        }
        else if(isforLoad)
        {
        	if(photoUrl1!=null && photoUrl1.length()>0 && !(photoUrl1.equalsIgnoreCase("null")))
        	{
        		//btn1.setBackgroundResource(R.drawable.loading);
        		if(pbar!=null)
        		pbar.setVisibility(View.VISIBLE);
        		if(btn1!=null)
        		btn1.setVisibility(View.INVISIBLE);
        	}
        	else
        	{
        		if(btn1!=null)
        		btn1.setBackgroundResource(R.drawable.image_add);
        	}
        }
        else
        {
        	if(btn1!=null)
        	btn1.setBackgroundResource(R.drawable.image_add);
        }
       
        if(url2!=null){
//        	url2.buildDrawingCache();
//        	BtnImageBmp=url2.getDrawingCache();
//        	btn2.setBackgroundColor(Color.TRANSPARENT);
//        	btn2.setImageBitmap(BtnImageBmp);
        	btn2.setBackgroundDrawable(url2.getDrawable());
        }
        else if(isforLoad)
        {
        	
        	if(photoUrl2!=null && photoUrl2.length()>0 && !(photoUrl2.equalsIgnoreCase("null")))
        	{
        		if(pbar2!=null)
        		pbar2.setVisibility(View.VISIBLE);
        		if(btn2!=null)
        		btn2.setVisibility(View.INVISIBLE);
        	}
        	else
        	{
        		if(btn2!=null)
        		btn2.setBackgroundResource(R.drawable.image_add);
        	}
        }
        else
        {
        	if(btn2!=null)
        	btn2.setBackgroundResource(R.drawable.image_add);
        }
        
        if(url3!=null){
//        	url3.buildDrawingCache();
//        	BtnImageBmp=url3.getDrawingCache();
//        	btn3.setBackgroundColor(Color.TRANSPARENT);
//        	btn3.setImageBitmap(BtnImageBmp);
        	if(btn3!=null)
        	btn3.setBackgroundDrawable(url3.getDrawable());
        }
        else if(isforLoad)
        {
        	
        	if(photoUrl3!=null && photoUrl3.length()>0 && !(photoUrl3.equalsIgnoreCase("null")))
        	{
        		if(pbar3!=null)
        		pbar3.setVisibility(View.VISIBLE);
        		if(btn3!=null)
        		btn3.setVisibility(View.INVISIBLE);
        	}
        	else
        	{
        		if(btn3!=null)
        		btn3.setBackgroundResource(R.drawable.image_add);
        	}
        }
        else
        {
        	if(btn3!=null)
        	btn3.setBackgroundResource(R.drawable.image_add);
        }
        
        return view;
	}

}
