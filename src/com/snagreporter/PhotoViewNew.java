package com.snagreporter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Random;
import java.util.UUID;
import com.snagreporter.paint.*;
import com.snagreporter.R;
import com.snagreporter.paint.DrawPanel;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.drawable.PaintDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.ViewFlipper;

public class PhotoViewNew extends Activity 
{
	View content;
	ImageView myImgvw;
	
	//Boolean isBtn1=false,isBtn2=false,isbtn3=false;
	String strFromImg,strFromImgvw,strFilePath;
	Bitmap myImg;
	String PhotoURL;
	String FileName="";
	ViewFlipper vf;
	DrawPanel drwPnl;
	int imgHeight,imgWidth;
	@Override
	 public void onCreate(Bundle savedInstanceState)
	    {   
	        super.onCreate(savedInstanceState);
	        requestWindowFeature(Window.FEATURE_NO_TITLE);
	        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
	        setContentView(R.layout.photo_view);
	   
	        myImg=(Bitmap)getIntent().getParcelableExtra("BitmapImage");
	        strFromImg=getIntent().getExtras().getString("strFromImg");
	        PhotoURL=getIntent().getExtras().getString("PhotoUrl");
	        imgHeight=getIntent().getExtras().getInt("imgheight");
	        imgWidth=getIntent().getExtras().getInt("imgwidth");
	        myImgvw=(ImageView)findViewById(R.id.photo_view_myImage);
	      
	        content=(View)findViewById(R.id.photo_view_relScreen);
	        LinearLayout lp=(LinearLayout)findViewById(R.id.drawpanelLayout);
	        
	       // vf=(ViewFlipper)findViewById(R.id.viewFlipper);
	    
	        RelativeLayout.LayoutParams parm=new RelativeLayout.LayoutParams(imgWidth, imgHeight);
	       lp.setLayoutParams(parm);
	      
	       ImageButton img=new ImageButton(this);
	       img.setImageBitmap(myImg);	 
	        myImgvw.setBackgroundDrawable(img.getDrawable());
	 
	        
	       //myImgvw.setImageBitmap(myImg);
	  
	    
	     
	}
	public void btnRetakeClick(View v)
	{
		finish();
	}
	public void btnSaveClick(View v)
	{
		
		Random rnd=new Random();
		int value=rnd.nextInt(10);
		
		Bitmap bitmap;
		View v1 = content;
		v1.setDrawingCacheEnabled(true);
		bitmap = Bitmap.createBitmap(v1.getDrawingCache());
		//myImg=bitmap;
		v1.setDrawingCacheEnabled(false);   
		    
		  
		FileName="";
		if(PhotoURL!=null && PhotoURL.length()>0){
			FileName=PhotoURL;
		}
		else{
			UUID uniqueKey = UUID.randomUUID();
			FileName=uniqueKey.toString();
		}
		File file=new File(Environment.getExternalStorageDirectory()+File.separator+"SnagReporter/Pictures/"+FileName+".jpg");
		strFilePath=file.toString();
		Log.d("file path", ""+strFilePath);
		 try 
         {
//             file.createNewFile();
//             FileOutputStream ostream = new FileOutputStream(file);
//           //  bmImg.compress(CompressFormat.JPEG, 100, ostream);
//             bitmap.compress(CompressFormat.JPEG, 100, ostream);
//            
//             ostream.close();
			 OutputStream outStream = null;
			 outStream = new FileOutputStream(file);
			 bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outStream);
     	     outStream.flush();
     	     outStream.close();
             saveComplete();
         } 
         catch (Exception e) 
         {
            Log.d("Exception btnSaveClick", e.getMessage());
         }
	    
	}
	public void saveComplete()
	{
		Intent i=new Intent();
		if(strFromImg.equalsIgnoreCase("img1"))
		{
			strFromImgvw="img1";
		}
		else if(strFromImg.equalsIgnoreCase("img2"))
		{
			strFromImgvw="img2";
		}
		else if(strFromImg.equalsIgnoreCase("img3"))
		{
			strFromImgvw="img3";
		}
       i.putExtra("strFromImgvw", strFromImgvw);
        i.putExtra("strFilePath", strFilePath);
        i.putExtra("UUID", FileName);
        
        setResult(10, i);
         finish();
	}
	
	public void ClearClick(View v){
		try{
			//DrawPanel d=(DrawPanel)findViewById(R.id.drawpanel1);
			//DrawPanel.ct=1;
			//vf.showNext();
		}
		catch(Exception e){
			Toast.makeText(getApplicationContext(), "Error="+e.getMessage(), Toast.LENGTH_LONG).show();
			Log.d("Error=", ""+e.getMessage());
		}
	}

}
