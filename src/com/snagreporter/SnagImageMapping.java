package com.snagreporter;


import com.snagreporter.SnagImageMap;
import com.snagreporter.entity.FloorMaster;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class SnagImageMapping extends Activity {
	SnagImageMap mImageMap;
	 String ImageSource="";
	 public FloorMaster obj;
	 
	@Override
	public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.snag_image_mapping);
       // ImageView imageView = (ImageView) findViewById(R.id.view_Image_myImage);
      //  imageView.setImageResource(R.drawable.floor_plan1);
        
        // find the image map in the view

//		  RelativeLayout mapLayout = (RelativeLayout)findViewById(R.id.plot_image_layout);
//		  ImageView mapImage = new ImageView(this.getApplicationContext());
//		  
//		  RelativeLayout.LayoutParams par=new RelativeLayout.LayoutParams(50,50);
//		  par.setMargins(140, 140, 140,50);
//		  mapImage.setBackgroundResource(R.drawable.plot_pink_gray);
//		  mapImage.setLayoutParams(par);
//		  mapLayout.addView(mapImage);
		 
		  mImageMap = (SnagImageMap)findViewById(R.id.map); 
		 
		  obj=(FloorMaster)getIntent().getExtras().get("Floor");
		  mImageMap.setTag(obj.getID()+"~"+obj.getBuildingID()+"~"+obj.getProjectID());
		  ImageSource=getIntent().getExtras().getString("ImageName");
	        String FilePath=Environment.getExternalStorageDirectory()+"/SnagReporter/Pictures/"+ImageSource;
			bp=BitmapFactory.decodeFile(FilePath);
			if(bp!=null)
			mImageMap.setImageBitmap(bp);
			
			
    }
	Bitmap bp;
	@Override
	public void onDestroy(){
		super.onDestroy();
		bp.recycle();
		bp=null;
	}
	
	
}
