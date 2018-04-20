package com.snagreporter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Random;
import java.util.UUID;

import com.snagreporter.R;
import com.snagreporter.paint.DrawPanel;
import com.snagreporter.paint.LockableHoriScrollView;
import com.snagreporter.paint.LockableScrollView;

import de.ipcas.colorPicker.ColorPickerDialog;
import de.ipcas.colorPicker.ColorPickerDialog.OnColorPickedListener;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.ViewFlipper;

public class PhotoView extends Activity 
{
	OnColorPickedListener colorPickedListener;
	Button clrBtn;
	View content;
	RelativeLayout contentCh;
	ImageView myImgvw;
	int chngColor;
	//Boolean isBtn1=false,isBtn2=false,isbtn3=false;
	String strFromImg,strFromImgvw,strFilePath;
	Bitmap myImg;
	String PhotoURL;
	String FileName="";
	//ViewFlipper vf;
	String FilePath="";
	int imgHeight,imgWidth;
	Display display;
	int orienta;
	int oriNew=0;
	float oriWD=0;
	float oriHT=0;
	float zoomedWD=0;
	float zoomedHT=0;
	Button Zin,Zout,Toggle;
	ImageView draw,drag;
	int Zoomed=0;
	String strColor;
	boolean isZoomable=true;
	LockableHoriScrollView LHSV;
	LockableScrollView LSV;
	DrawPanel DRAW1;//,DRAW2;
	Boolean ZinLongClicked=false,ZoutLongClicked=false;
	Object strReturnedValues[];
	@Override
	 public void onCreate(Bundle savedInstanceState)
	 {
	        super.onCreate(savedInstanceState);
	        requestWindowFeature(Window.FEATURE_NO_TITLE);
	        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
	        setContentView(R.layout.photo_view);
	        clrBtn=(Button)findViewById(R.id.photo_view_btnColor);
	       // vf=(ViewFlipper)findViewById(R.id.viewFlipper);
	        
	        Zin=(Button)findViewById(R.id.btnZoomIn);
	        Zout=(Button)findViewById(R.id.btnZoomOut);
	        Toggle=(Button)findViewById(R.id.btnToggle);
	        draw=(ImageView)findViewById(R.id.img_drawicon);
	        drag=(ImageView)findViewById(R.id.img_dragicon);
	        SharedPreferences sp=getSharedPreferences("AppDelegate",MODE_PRIVATE);
	        strColor=sp.getString("ColorCode", "");
	        
	        LHSV=(LockableHoriScrollView)findViewById(R.id.LHSV);
	        LSV=(LockableScrollView)findViewById(R.id.LSV);
	        DRAW1=(DrawPanel)findViewById(R.id.drawpanel1);
	       // DRAW1.mPaint.setColor(Color.);
	        display = getWindowManager().getDefaultDisplay();
	        DisplayMetrics dm = new DisplayMetrics();
	        getWindowManager().getDefaultDisplay().getMetrics(dm);
	       // DRAW2=(DrawPanel)findViewById(R.id.drawpanel2);
	        
	        setpaincolor();
	        /*Zin.setOnLongClickListener(new OnLongClickListener() {
				
				@Override
				public boolean onLongClick(View arg0) {
					// TODO Auto-generated method stub
					try{
						
						if(Integer.parseInt(Zin.getTag().toString())==1){
							float ratio=oriWD/oriHT;
							oriWD+=20;
							float incre=20/ratio;
							oriHT+=incre;
							LinearLayout lp=(LinearLayout)findViewById(R.id.drawpanelLayout);
							RelativeLayout.LayoutParams parm=new RelativeLayout.LayoutParams((int)oriWD, (int)oriHT);
						       lp.setLayoutParams(parm);
						       Bitmap Temp=Bitmap.createScaledBitmap(myImg, (int)oriWD, (int)oriHT, true);
						       ImageButton img=new ImageButton(PhotoView.this);
						       img.setImageBitmap(Temp);	 
						       myImgvw.setBackgroundDrawable(img.getDrawable());
						      // myImgvw.setBackgroundColor(Color.RED);
						       Zoomed++;
						       if(Zoomed>1){
						    	   Zout.setTag(1);
						    	   Zout.setTextColor(Color.parseColor("#000000"));
						    	   Zout.setEnabled(true);
						       }
						}
						ZinLongClicked=true;
						
						
					}
					catch(Exception e){
						Log.d("Error ZoominClick=", ""+e.getMessage());
					}
					
					return false;
				}
			});
	        
	        Zout.setOnLongClickListener(new OnLongClickListener() {
				
				@Override
				public boolean onLongClick(View v) {
					// TODO Auto-generated method stub
					try{
						
						if(Integer.parseInt(Zout.getTag().toString())==1){
							float ratio=oriWD/oriHT;
							oriWD-=20;
							float incre=20/ratio;
							oriHT-=incre;
							LinearLayout lp=(LinearLayout)findViewById(R.id.drawpanelLayout);
							RelativeLayout.LayoutParams parm=new RelativeLayout.LayoutParams((int)oriWD, (int)oriHT);
						       lp.setLayoutParams(parm);
						      
						       Bitmap Temp=Bitmap.createScaledBitmap(myImg, (int)oriWD, (int)oriHT, true);
						       ImageButton img=new ImageButton(PhotoView.this);
						       img.setImageBitmap(Temp);	 
						       myImgvw.setBackgroundDrawable(img.getDrawable());
						       Zoomed--;
						       if(Zoomed==1){
						    	   Zout.setTag(0);
						    	   Zout.setTextColor(Color.parseColor("#dbdbdb"));
						    	   Zout.setEnabled(false);
						       }
						       
						       if(oriWD<display.getWidth() && oriHT<display.getHeight()-44){
						    	   Toggle.setTag(1);
									Toggle.setText("Drag");
									DRAW1.mPaintable=true;
									DRAW2.mPaintable=true;
									LHSV.mScrollable=false;
									LSV.mScrollable=false;
						       }
						}
						ZoutLongClicked=true;
						
					}
					catch(Exception e){
						Log.d("Error ZoomoutClick=", ""+e.getMessage());
					}
					return false;
				}
			});*/
	        
	        
	        Object objNot = getLastNonConfigurationInstance();
	        if(objNot!=null)
	        {
	        	strReturnedValues=(Object[])objNot;
	        	//String str=(String)objNot;
	        	String str=(String) strReturnedValues[0];
	        	oriNew=Integer.parseInt(str);//0-landscape
	        	oriWD=Float.parseFloat(strReturnedValues[1].toString());
	        	oriHT=Float.parseFloat(strReturnedValues[2].toString());
	        	zoomedWD=Float.parseFloat(strReturnedValues[3].toString());
	        	zoomedHT=Float.parseFloat(strReturnedValues[4].toString());
	        	Zoomed=Integer.parseInt(strReturnedValues[5].toString());
	        	int tag=Integer.parseInt(strReturnedValues[6].toString());
	        	Toggle.setTag(tag);
	        	
	        	if(Integer.parseInt(Toggle.getTag().toString())==1){
					
					Toggle.setTag(1);
					//Toggle.setText("Drag");
					drag.setVisibility(View.VISIBLE);
					draw.setVisibility(View.INVISIBLE);
					DRAW1.mPaintable=true;
					//DRAW2.mPaintable=true;
					LHSV.mScrollable=false;
					LSV.mScrollable=false;
					
					
				}
				else
				{
					if((zoomedWD>display.getWidth() || zoomedHT>display.getHeight()-44)){//Zoomed>0 && 
					Toggle.setTag(0);
					//Toggle.setText("Mark");
					drag.setVisibility(View.INVISIBLE);
					draw.setVisibility(View.VISIBLE);
					DRAW1.mPaintable=false;
					//DRAW2.mPaintable=false;
					LHSV.mScrollable=true;
					LSV.mScrollable=true;
					}
				}
	        	
	        	try{
	        		DRAW1.ArrayPaint=(ArrayList<Paint>) strReturnedValues[10];
	        		DRAW1.ArrayPath=(ArrayList<Path>) strReturnedValues[11];
	        		DRAW1.Stroke=(Float) strReturnedValues[12];
	        	}
	        	catch(Exception e){
	        		
	        	}
	        	
	        	
	        }
	        
	         
	        
	        FilePath=getIntent().getExtras().getString("filePath");
	        
	        
//	        FilePath=getIntent().getExtras().getString("filePath");
	        if(FilePath!=null && FilePath.length()!=0){
	        	try
	        	{
	        	myImg=decodeFile(FilePath);
	        	}
	        	catch(OutOfMemoryError e)
	        	{
	        		Log.d("Exception OutOfMemoryError", ""+e.getMessage());
	        	}
	        }
	        else{
	        	myImg=null;
	        	myImg=(Bitmap)getIntent().getParcelableExtra("BitmapImage");
	        }
	        
	        strFromImg=getIntent().getExtras().getString("strFromImg");
	        PhotoURL=getIntent().getExtras().getString("PhotoUrl");
	        imgHeight=getIntent().getExtras().getInt("imgheight");
	        imgWidth=getIntent().getExtras().getInt("imgwidth");
	        myImgvw=(ImageView)findViewById(R.id.photo_view_myImage);
	      
	        content=(View)findViewById(R.id.photo_view_relScreen);
	        contentCh=(	RelativeLayout)findViewById(R.id.rel_paint_main);
	        LinearLayout lp=(LinearLayout)findViewById(R.id.drawpanelLayout);
	        

	        
	        
	        float wd=myImg.getWidth();
	        float ht=myImg.getHeight();
	        if(oriWD==0){
	        	oriWD=wd;
	        	oriHT=ht;
	        	zoomedWD=oriWD;
	        	zoomedHT=oriHT;
	        }
	        else{
	        	
	        	try{
	        		Bitmap Temp=Bitmap.createScaledBitmap(myImg, (int)zoomedWD, (int)zoomedHT, true);
	        		myImg=Temp;
	        		wd=zoomedWD;
		        	ht=zoomedHT;
	        	}
	        	catch(OutOfMemoryError e){
	        		wd=myImg.getWidth();
	    	         ht=myImg.getHeight();
	    	         zoomedWD=oriWD;
	 	        	zoomedHT=oriHT;
	    	       Zoomed=0;
	        	}
	        }
	        
	        //int space=44;
	        
//	        if(wd>display.getWidth() || ht>display.getHeight()-space){
//	        	float tempWD,tempHT;
//	        	tempWD=wd;
//	        	tempHT=ht;
//	        	if(tempWD>tempHT){
//	        		float diff=tempWD-display.getWidth();
//	        		float pc=diff/tempWD;
//	        		tempWD=display.getWidth();
//	        		float cc=ht*pc;
//	        		tempHT-=cc;
//	        	}
//	        	else{
//	        		float diff=tempHT-display.getHeight()-space;
//	        		float pc=diff/tempHT;
//	        		tempHT=display.getHeight()-space;
//	        		float cc=wd*pc;
//	        		tempWD-=cc;
//	        	}
//	        	
//	        	if(tempHT>display.getHeight()-space){
//	        		float diff=tempHT-display.getHeight()-space;
//	        		float pc=diff/tempHT;
//	        		tempHT=display.getHeight()-space;
//	        		float cc=wd*pc;
//	        		tempWD-=cc;
//	        	}
//	        	else if(tempWD>display.getWidth()){
//	        		float diff=tempWD-display.getWidth();
//	        		float pc=diff/tempWD;
//	        		tempWD=display.getWidth();
//	        		float cc=ht*pc;
//	        		tempHT-=cc;
//	        	}
//	        wd=tempWD;
//	        ht=tempHT;
//	        	
//	        }
	        
	        
	        RelativeLayout.LayoutParams parm=new RelativeLayout.LayoutParams((int)wd, (int)ht);
	       lp.setLayoutParams(parm);
	      
	       ImageButton img=new ImageButton(this);
	       img.setImageBitmap(myImg);	 
	        myImgvw.setBackgroundDrawable(img.getDrawable());
	    }
	
	
	@Override
	protected void onDestroy() {
		if(myImg!=null)
			myImg.recycle();
		
		Log.d("image cleared","");
			
		super.onDestroy();
	}
	
	@Override
    public Object onRetainNonConfigurationInstance()
    {
		//Intent i=new Intent();
		Object[] str=new Object[13];
		if(oriNew==0)
        	str[0]="1";
        else
        	str[0]="0";
		str[1]=""+oriWD;
		str[2]=""+oriHT;
		str[3]=""+zoomedWD;
		str[4]=""+zoomedHT;
		str[5]=""+Zoomed;
		str[6]=""+Toggle.getTag().toString();
		
		str[10]=DRAW1.ArrayPaint;
		str[11]=DRAW1.ArrayPath;
		str[12]=DRAW1.Stroke;
		return str;
//        if(oriNew==0)
//        	return "1";
//        else
//        	return "0";
    }
	private Bitmap decodeFile(String url){//This is the whole url of the image
		File f=new File(url);
		
	    Bitmap b = null;
	    //int IMAGE_MAX_SIZE=200;
	    int maxWidth=display.getWidth();//420;//
	    int maxHeight=display.getHeight()-44;//220;//0
	    try {
	        //Decode image size
	    	
	    	//@@@@@
	    	int angle = 0;
	    	 Matrix mat = new Matrix();
	        try {
	            File f1 = new File(FilePath);
	            ExifInterface exif = new ExifInterface(f1.getPath());
	            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

	            f1=null;

	            if (orientation == ExifInterface.ORIENTATION_ROTATE_90) {
	                angle = 90;
	            } 
	            else if (orientation == ExifInterface.ORIENTATION_ROTATE_180) {
	                angle = 180;
	            } 
	            else if (orientation == ExifInterface.ORIENTATION_ROTATE_270) {
	                angle = 270;
	            }
	            
	           
	            if(angle>0){
	            	mat.postRotate(angle);
	            	int temp=maxHeight;
	            	maxHeight=maxWidth;
	            	maxWidth=temp;
	            	
	            	
	            }
	                
	        }
	        catch (IOException e) {
	            Log.w("TAG", "-- Error in setting image");
	        }   
	        catch(OutOfMemoryError oom) {
	            Log.w("TAG", "-- OOM Error in setting image");
	        }
	        
	        //@@@@@
	    	
	    	
	        BitmapFactory.Options o = new BitmapFactory.Options();
	        o.inJustDecodeBounds = true;
	        
	        

	        FileInputStream fis = new FileInputStream(f);
	        BitmapFactory.decodeStream(fis, null, o);
	        
	        fis.close();
	        
	        if(oriNew==1){
	        	int temp=o.outWidth;
	        	o.outHeight=o.outWidth;
	        	o.outWidth=temp;
	        }

	        
	        int scale = 1;
	        if (o.outHeight > maxHeight && o.outWidth > maxWidth) {
	        	if(o.outHeight>o.outWidth){
	        		scale = (int)Math.pow(2, (int) Math.round(Math.log(maxHeight / (double) Math.max(o.outHeight, o.outWidth)) / Math.log(0.5)));
	        	}
	        	else{
	        		scale = (int)Math.pow(2, (int) Math.round(Math.log(maxWidth / (double) Math.max(o.outHeight, o.outWidth)) / Math.log(0.5)));
	        	}
	        }
	        else if (o.outHeight > maxHeight || o.outWidth > maxWidth) {
	        	if(o.outHeight>maxHeight)
	        		scale = (int)Math.pow(2, (int) Math.round(Math.log(maxHeight / (double) Math.max(o.outHeight, o.outWidth)) / Math.log(0.5)));
	        	else
	        		scale = (int)Math.pow(2, (int) Math.round(Math.log(maxWidth / (double) Math.max(o.outHeight, o.outWidth)) / Math.log(0.5)));
	        }

	        //Decode with inSampleSize
	        BitmapFactory.Options o2 = new BitmapFactory.Options();
	        o2.inSampleSize = scale;
	        
	        
	        fis = new FileInputStream(f);
	        b = BitmapFactory.decodeStream(fis, null, o2);
	        try{
	        float wd=b.getWidth();
	        float ht=b.getHeight();
//	        if(angle>0){
//	        	float temp=wd;
//	        	wd=ht;
//	        	ht=temp;
//	        }
	        
	        int space=44;
	        if(scale>1){
	        if((maxWidth>wd  && maxHeight>ht)){
	        	float tempWD,tempHT;
	        	tempWD=wd;
	        	tempHT=ht;
	        	if(tempWD>tempHT){
	        		float diff=display.getWidth()-tempWD;
	        		float pc=diff/tempWD;
	        		tempWD=display.getWidth();
	        		float cc=ht*pc;
	        		tempHT+=cc;
	        	}
	        	else{
	        		float diff=display.getHeight()-space-tempHT;
	        		float pc=diff/tempHT;
	        		tempHT=display.getHeight()-space;
	        		float cc=wd*pc;
	        		tempWD+=cc;
	        	}
	        	
	        	if(tempHT>display.getHeight()-space){
	        		float diff=display.getHeight()-space-tempHT;
	        		float pc=diff/tempHT;
	        		tempHT=display.getHeight()-space;
	        		float cc=wd*pc;
	        		tempWD+=cc;
	        	}
	        	else if(tempWD>display.getWidth()){
	        		float diff=display.getWidth()-tempWD;
	        		float pc=diff/tempWD;
	        		tempWD=display.getWidth();
	        		float cc=ht*pc;
	        		tempHT+=cc;
	        	}
	        wd=tempWD;
	        ht=tempHT;
	       
	        	
	        
	        	b=Bitmap.createScaledBitmap(b, (int)wd,(int) ht, true);
	        	
	        	
	        
	        }
	        else if( (maxWidth==wd && ht>maxHeight) || (maxHeight==ht && wd>maxWidth)){

	        	float tempWD,tempHT;
	        	tempWD=wd;
	        	tempHT=ht;
	        	if(tempWD>tempHT){
	        		float diff=display.getWidth()-tempWD;
	        		float pc=diff/tempWD;
	        		tempWD=display.getWidth();
	        		float cc=ht*pc;
	        		tempHT+=cc;
	        	}
	        	else{
	        		float diff=display.getHeight()-space-tempHT;
	        		float pc=diff/tempHT;
	        		tempHT=display.getHeight()-space;
	        		float cc=wd*pc;
	        		tempWD+=cc;
	        	}
	        	
	        	if(tempHT>display.getHeight()-space){
	        		float diff=display.getHeight()-space-tempHT;
	        		float pc=diff/tempHT;
	        		tempHT=display.getHeight()-space;
	        		float cc=wd*pc;
	        		tempWD+=cc;
	        	}
	        	else if(tempWD>display.getWidth()){
	        		float diff=display.getWidth()-tempWD;
	        		float pc=diff/tempWD;
	        		tempWD=display.getWidth();
	        		float cc=ht*pc;
	        		tempHT+=cc;
	        	}
	        wd=tempWD;
	        ht=tempHT;
	       
	        	
	        
	        	b=Bitmap.createScaledBitmap(b, (int)wd,(int) ht, true);
	        	
	        	
	        
	        
	        }
	        else if( (ht>maxHeight) || (wd>maxWidth)){

	        	float tempWD,tempHT;
	        	tempWD=wd;
	        	tempHT=ht;
	        	if(tempWD>tempHT){
	        		float diff=display.getWidth()-tempWD;
	        		float pc=diff/tempWD;
	        		tempWD=display.getWidth();
	        		float cc=ht*pc;
	        		tempHT+=cc;
	        	}
	        	else{
	        		float diff=display.getHeight()-space-tempHT;
	        		float pc=diff/tempHT;
	        		tempHT=display.getHeight()-space;
	        		float cc=wd*pc;
	        		tempWD+=cc;
	        	}
	        	
	        	if(tempHT>display.getHeight()-space){
	        		float diff=display.getHeight()-space-tempHT;
	        		float pc=diff/tempHT;
	        		tempHT=display.getHeight()-space;
	        		float cc=wd*pc;
	        		tempWD+=cc;
	        	}
	        	else if(tempWD>display.getWidth()){
	        		float diff=display.getWidth()-tempWD;
	        		float pc=diff/tempWD;
	        		tempWD=display.getWidth();
	        		float cc=ht*pc;
	        		tempHT+=cc;
	        	}
	        wd=tempWD;
	        ht=tempHT;
	       
	        	
	        
	        	b=Bitmap.createScaledBitmap(b, (int)wd,(int) ht, true);
	        	
	        	
	        
	        
	        }
	        
	        
	        }
	        else{
	        	if((wd>maxWidth  && ht>maxHeight)){
		        	float tempWD,tempHT;
		        	tempWD=wd;
		        	tempHT=ht;
		        	if(tempWD>tempHT){
		        		float diff=display.getWidth()-tempWD;
		        		float pc=diff/tempWD;
		        		tempWD=display.getWidth();
		        		float cc=ht*pc;
		        		tempHT+=cc;
		        	}
		        	else{
		        		float diff=display.getHeight()-space-tempHT;
		        		float pc=diff/tempHT;
		        		tempHT=display.getHeight()-space;
		        		float cc=wd*pc;
		        		tempWD+=cc;
		        	}
		        	
		        	if(tempHT>display.getHeight()-space){
		        		float diff=display.getHeight()-space-tempHT;
		        		float pc=diff/tempHT;
		        		tempHT=display.getHeight()-space;
		        		float cc=wd*pc;
		        		tempWD+=cc;
		        	}
		        	else if(tempWD>display.getWidth()){
		        		float diff=display.getWidth()-tempWD;
		        		float pc=diff/tempWD;
		        		tempWD=display.getWidth();
		        		float cc=ht*pc;
		        		tempHT+=cc;
		        	}
		        wd=tempWD;
		        ht=tempHT;
		       
		        	
		        
		        	b=Bitmap.createScaledBitmap(b, (int)wd,(int) ht, true);
		        	
		        	
		        
		        }
	        	else if( (ht>maxHeight) || (wd>maxWidth)){

		        	float tempWD,tempHT;
		        	tempWD=wd;
		        	tempHT=ht;
		        	if(tempWD>tempHT){
		        		float diff=display.getWidth()-tempWD;
		        		float pc=diff/tempWD;
		        		tempWD=display.getWidth();
		        		float cc=ht*pc;
		        		tempHT+=cc;
		        	}
		        	else{
		        		float diff=display.getHeight()-space-tempHT;
		        		float pc=diff/tempHT;
		        		tempHT=display.getHeight()-space;
		        		float cc=wd*pc;
		        		tempWD+=cc;
		        	}
		        	
		        	if(tempHT>display.getHeight()-space){
		        		float diff=display.getHeight()-space-tempHT;
		        		float pc=diff/tempHT;
		        		tempHT=display.getHeight()-space;
		        		float cc=wd*pc;
		        		tempWD+=cc;
		        	}
		        	else if(tempWD>display.getWidth()){
		        		float diff=display.getWidth()-tempWD;
		        		float pc=diff/tempWD;
		        		tempWD=display.getWidth();
		        		float cc=ht*pc;
		        		tempHT+=cc;
		        	}
		        wd=tempWD;
		        ht=tempHT;
		       
		        	
		        
		        	b=Bitmap.createScaledBitmap(b, (int)wd,(int) ht, true);
		        	
		        	
		        
		        
		        }
	        }
	        }
	        catch(Exception e){
	        	b = BitmapFactory.decodeStream(fis, null, o2);
	        }
	        
	        
	        if(angle>0){
        		b= Bitmap.createBitmap(b, 0, 0, b.getWidth(), b.getHeight(), mat, true);
        	} 
	        
	        
	       // Log.d("", "");
	        //byte[] arr=getBytesFromBitmap(b);
	        //int i=arr.length;
	       // double d=b.getRowBytes()*b.getHeight();
	        fis.close();
	        f=null;
	        //Toast.makeText(getApplicationContext(), "Resized To="+b.getHeight()+"X"+b.getWidth(), Toast.LENGTH_LONG).show();
	    } catch (IOException e) {
	    }
	    return b;
	}
	
	
	
	public byte[] getBytesFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(CompressFormat.JPEG, 70, stream);
        return stream.toByteArray();
    }
	
	public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
    // Raw height and width of image
    final int height = options.outHeight;
    final int width = options.outWidth;
    int inSampleSize = 1;

    if (height > reqHeight || width > reqWidth) {

        // Calculate ratios of height and width to requested height and width
        final int heightRatio = Math.round((float) height / (float) reqHeight);
        final int widthRatio = Math.round((float) width / (float) reqWidth);

        // Choose the smallest ratio as inSampleSize value, this will guarantee
        // a final image with both dimensions larger than or equal to the
        // requested height and width.
        inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
    }

    return inSampleSize;
}
	
//	@Override
//	public void onConfigurationChanged(Configuration newConfig){
//		super.onConfigurationChanged(newConfig);
//		
//	}
	public void btnRetakeClick(View v)
	{
		finish();
	}
	public void btnSaveClick(View v)
	{
		
		//float wd=myImg.getWidth();
        //float ht=myImg.getHeight();
        

        
//        LinearLayout lp=(LinearLayout)findViewById(R.id.drawpanelLayout);
//        RelativeLayout.LayoutParams parm=new RelativeLayout.LayoutParams((int)wd, (int)ht);
//       lp.setLayoutParams(parm);
//      
//       ImageButton img=new ImageButton(this);
//       img.setImageBitmap(myImg);	 
//        myImgvw.setBackgroundDrawable(img.getDrawable());
		
		Random rnd=new Random();
		int value=rnd.nextInt(10);
		
		Bitmap bitmap=null;
		View v1 = content;
		v1.setDrawingCacheEnabled(true);
		//bitmap = Bitmap.createBitmap(v1.getDrawingCache());
		try
		{
		bitmap=loadBitmapFromView(contentCh);
		}
		catch(OutOfMemoryError e)
		{
			Log.d("Exception",""+e.getMessage());
		}
		//myImg=bitmap;
		v1.setDrawingCacheEnabled(false);   
		    
		  
		FileName="";
		if(PhotoURL!=null && PhotoURL.length()>0 && !PhotoURL.equalsIgnoreCase("null") && !PhotoURL.equalsIgnoreCase("(null)")){
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

			 OutputStream outStream = null;
			 outStream = new FileOutputStream(file);
			 int MAX_IMAGE_SIZE=500*1024;
			 int compressQuality = 103; // quality decreasing by 5 every loop. (start from 99)
			 int streamLength = MAX_IMAGE_SIZE;
			 while (streamLength >= MAX_IMAGE_SIZE) {
			     ByteArrayOutputStream bmpStream = new ByteArrayOutputStream();
			     compressQuality -= 3;
			     Log.d("", "Quality: " + compressQuality);
			     bitmap.compress(Bitmap.CompressFormat.JPEG, compressQuality, bmpStream);
			     byte[] bmpPicByteArray = bmpStream.toByteArray();
			     streamLength = bmpPicByteArray.length;
			     Log.d("", "Size: " + streamLength);
			 }
			 
			 bitmap.compress(Bitmap.CompressFormat.JPEG, compressQuality, outStream);
			 
			 
     	     outStream.flush();
     	     outStream.close();
             saveComplete();
         } 
         catch (Exception e) 
         {
            Log.d("Exception btnSaveClick", ""+e.getMessage());
         }
	    
	}
	
	public Bitmap loadBitmapFromView(View view) {
		
//		float sd=v.getLayoutParams().height;
//	    Bitmap b = Bitmap.createBitmap( v.getLayoutParams().width, v.getLayoutParams().height, Bitmap.Config.ARGB_8888);                
//	    Canvas c = new Canvas(b);
//	    v.layout(0,0, v.getLayoutParams().width, v.getLayoutParams().height);
//	    v.draw(c);
//	    return b;
		 Bitmap returnedBitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(),Bitmap.Config.ARGB_8888);
		 
		 //float ff=returnedBitmap.getHeight();
		 //float ff2=returnedBitmap.getWidth();
		 
		    Canvas canvas = new Canvas(returnedBitmap);
//		    Drawable bgDrawable =view.getBackground();
//		    if (bgDrawable!=null) 
//		        bgDrawable.draw(canvas);
//		    else 
//		        canvas.drawColor(Color.WHITE);
		    view.draw(canvas);
		    returnedBitmap=Bitmap.createScaledBitmap(returnedBitmap, (int)oriWD,(int) oriHT, true);
		    //float a=returnedBitmap.getHeight();
		    return returnedBitmap;
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
			DRAW1.ClearDrawing();
		}
		catch(Exception e){
			//Toast.makeText(getApplicationContext(), "Error="+e.getMessage(), Toast.LENGTH_LONG).show();
			Log.d("Error=", ""+e.getMessage());
		}
	}
	
	 public void ScalePaths(float sx,float sy){
			try {
				
				Matrix scaleMatrix = new Matrix();
				scaleMatrix.setScale(sx,sy, 0.5f, 0.5f);
				for(int i=0;i<DRAW1.ArrayPath.size();i++){
					//pathArray.get(i).transform(scaleMatrix); 
					
					RectF rectF = new RectF();
					DRAW1.ArrayPath.get(i).computeBounds(rectF, true);
					
					scaleMatrix.setScale(sx, sy,0.5f,0.5f);
					DRAW1.ArrayPath.get(i).transform(scaleMatrix); 
				}
				
			} catch (Exception e) {
				// TODO: handle exception
				Log.d("Error=", ""+e.getMessage());
			}
		}
	 
	 public void SetStrokeWidth(float scale){
		 try{
			 
			 for(int i=0;i<DRAW1.ArrayPaint.size();i++){
				 
				 DRAW1.ArrayPaint.get(i).setStrokeWidth((float)(DRAW1.Stroke));
				 
			 }
		 }
		 catch(Exception e ){
			 Log.d("Error=", ""+e.getMessage());
		 }
	 }
	
	public void ZoominClick(View v){
		try{
			//if(!ZinLongClicked){
			if(Integer.parseInt(Zin.getTag().toString())==1){
				float ratio=zoomedWD/zoomedHT;
				zoomedWD*=1.5;
				float incre=50/ratio;
				zoomedHT*=1.5;
				
				LinearLayout lp=(LinearLayout)findViewById(R.id.drawpanelLayout);
				RelativeLayout.LayoutParams parm=new RelativeLayout.LayoutParams((int)zoomedWD, (int)zoomedHT);
			       
			       try{
			    	   if(zoomedHT<=oriHT*5 || zoomedWD<=oriWD*5){
			    		   lp.setLayoutParams(parm);
			    		   Bitmap Temp=Bitmap.createScaledBitmap(myImg, (int)zoomedWD, (int)zoomedHT, true);
			    		   
			    		   ImageButton img=new ImageButton(this);
			    		   img.setImageBitmap(Temp);
			    		   
			    		   myImgvw.setBackgroundDrawable(img.getDrawable());
			    		   // myImgvw.setBackgroundColor(Color.RED);
			    		   Temp=null;
			    		   img=null;
			    		   Zoomed++;
			    		   Zout.setTag(1);
			    		   Zout.setEnabled(true);
			    		   //DRAW1.mPaint.setStrokeWidth((float)(DRAW1.mPaint.getStrokeWidth()*1.5));
			    		   //DRAW1.ClearDrawing();
			    		   SetStrokeWidth(1.5f);
			    		   ScalePaths(1.5f,1.5f);
//							DRAW2.mPaint.setStrokeWidth((float)(DRAW2.mPaint.getStrokeWidth()*1.5));
			    	   }
			    	   else{
			    		   Zin.setTag(0);
			    	   }
			       }
			       catch(Exception e){
			    	   Zin.setTag(0);
			    	   float ratio2=zoomedWD/zoomedHT;
						zoomedWD/=1.5;
						float incre2=50/ratio2;
						zoomedHT/=1.5;
						parm=new RelativeLayout.LayoutParams((int)zoomedWD, (int)zoomedHT);
					       lp.setLayoutParams(parm);
			       }
			       catch(OutOfMemoryError e){
			    	   Zin.setTag(0);
			    	   float ratio2=zoomedWD/zoomedHT;
						zoomedWD/=1.5;
						float incre2=50/ratio2;
						zoomedHT/=1.5;
						parm=new RelativeLayout.LayoutParams((int)zoomedWD, (int)zoomedHT);
					       lp.setLayoutParams(parm);
			       }
//			       if(Zoomed>0){
//			    	   Zout.setTag(1);
//			    	   Zout.setTextColor(Color.parseColor("#000000"));
//			    	   Zout.setEnabled(true);
//			       }
			}
			//}
			//ZinLongClicked=false;
		}
		catch(Exception e){
			Log.d("Error ZoominClick=", ""+e.getMessage());
		}
	}
	
	
	
	public void ZoomoutClick(View v){
		try{
			//if(!ZoutLongClicked){
			if(Integer.parseInt(Zout.getTag().toString())==1){
				float ratio=zoomedWD/zoomedHT;
				zoomedWD/=1.5;
				float incre=50/ratio;
				zoomedHT/=1.5;
				
				Zoomed--;
				//float dd=(float) ((float)display.getWidth()*0.25);
				//float hh=(float) (((float)display.getHeight()-44)*0.25);
			       if( (zoomedWD<=(display.getWidth()*0.25) || zoomedHT<=(display.getHeight()-44)*0.25)){
			    	   Zout.setTag(0);
			    	   Zout.setTextColor(Color.parseColor("#dbdbdb"));
			    	   Zout.setEnabled(false);
			    	   zoomedWD*=1.5;
			    	   zoomedHT*=1.5;
			       }
			       else{
			    	   try{
			    	   Bitmap Temp=Bitmap.createScaledBitmap(myImg, (int)zoomedWD, (int)zoomedHT, true);
			    	   LinearLayout lp=(LinearLayout)findViewById(R.id.drawpanelLayout);
			    	   RelativeLayout.LayoutParams parm=new RelativeLayout.LayoutParams((int)zoomedWD, (int)zoomedHT);
			    	   lp.setLayoutParams(parm);
			      
			       
			       ImageButton img=new ImageButton(this);
			       img.setImageBitmap(Temp);	 
			       myImgvw.setBackgroundDrawable(img.getDrawable());
			       //DRAW1.mPaint.setStrokeWidth((float)(DRAW1.mPaint.getStrokeWidth()/1.5));
			       SetStrokeWidth(1/1.5f);
			       //DRAW1.ClearDrawing();
			       ScalePaths((1/1.5f),(1/1.5f));
			       Temp=null;
			    	   }
			    	   catch(OutOfMemoryError e){
			    		   zoomedWD/=1.5;
			    		   zoomedHT/=1.5;
			    		   Bitmap Temp=Bitmap.createScaledBitmap(myImg, (int)zoomedWD, (int)zoomedHT, true);
				    	   LinearLayout lp=(LinearLayout)findViewById(R.id.drawpanelLayout);
				    	   RelativeLayout.LayoutParams parm=new RelativeLayout.LayoutParams((int)zoomedWD, (int)zoomedHT);
				    	   lp.setLayoutParams(parm);
				    	   ImageButton img=new ImageButton(this);
				    	   img.setImageBitmap(Temp);	 
				    	   myImgvw.setBackgroundDrawable(img.getDrawable());
				    	   //DRAW1.mPaint.setStrokeWidth((float)(DRAW1.mPaint.getStrokeWidth()/1.5));
				    	   SetStrokeWidth(1/1.5f);
				    	   //DRAW1.ClearDrawing();
				    	   ScalePaths((1/1.5f),(1/1.5f));
				    	   Temp=null;
			    	   }
					//DRAW2.mPaint.setStrokeWidth((float)(DRAW2.mPaint.getStrokeWidth()/1.5));
			       }
			       
			       if(zoomedWD<display.getWidth() && zoomedHT<display.getHeight()-44){
			    	   Toggle.setTag(1);
						//Toggle.setText("Drag");
						drag.setVisibility(View.VISIBLE);
						draw.setVisibility(View.INVISIBLE);
						DRAW1.mPaintable=true;
						//DRAW2.mPaintable=true;
						LHSV.mScrollable=false;
						LSV.mScrollable=false;
			       }
			       Zin.setTag(1);
			}
			//}
			//ZoutLongClicked=false;
		}
		catch(Exception e){
			Log.d("Error ZoomoutClick=", ""+e.getMessage());
		}
	}
	
	public void ToggleClick(View v){
		try{
			if(Integer.parseInt(Toggle.getTag().toString())==0){
				
				Toggle.setTag(1);
				//Toggle.setText("Drag");
				drag.setVisibility(View.VISIBLE);
				draw.setVisibility(View.INVISIBLE);
				DRAW1.mPaintable=true;
				//DRAW2.mPaintable=true;
				LHSV.mScrollable=false;
				LSV.mScrollable=false;
				
				
			}
			else{
				if((zoomedWD>display.getWidth() || zoomedHT>display.getHeight()-44)){//Zoomed>0 && 
				Toggle.setTag(0);
				//Toggle.setText("Mark");
				drag.setVisibility(View.INVISIBLE);
				draw.setVisibility(View.VISIBLE);
				DRAW1.mPaintable=false;
				//DRAW2.mPaintable=false;
				LHSV.mScrollable=true;
				LSV.mScrollable=true;
				}
			}
		}
		catch(Exception e){
			Log.d("Error ToggleClick=", ""+e.getMessage());
		}
	}
	public void setpaincolor()
	{
		try
		{
			 SharedPreferences sp=getSharedPreferences("AppDelegate",MODE_PRIVATE);
		     strColor=sp.getString("ColorCode", "");
			if(strColor!=null && strColor.length()>0)
			{
				chngColor=Integer.parseInt(strColor);
				
				//DRAW1.mPaint.setColor(chngColor);
				DRAW1.PaintColor=chngColor;
				clrBtn.setBackgroundColor(chngColor);
			}
			else{
				chngColor=Color.RED;
				DRAW1.PaintColor=chngColor;
				clrBtn.setBackgroundColor(chngColor);
			}
		}
		catch (Exception e)
		{
			Log.d("Exception",""+e.getMessage());
		}
	}
	
	public void colorClick(View v)
	{
		int clr=0xff000000; 
		try
		{
			
			Dialog	dialog1 = new ColorPickerDialog(PhotoView.this);
			dialog1.show();
				
			//setpaincolor();
			
		}
		catch (Exception e)
		{
			Log.d("Exception",""+e.getMessage());
		}
	}




	
}
