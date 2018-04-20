package com.snagreporter;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Array;
import java.util.ArrayList;
import java.util.List;


import com.snagreporter.R;
import com.snagreporter.listitems.Header;
import com.snagreporter.listitems.Item;
import com.snagreporter.listitems.ListItem;
import com.snagreporter.listitems.ListItemBlank;
import com.snagreporter.listitems.ListItemParent;


import android.R.array;
import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.RemoteViews.ActionException;
import android.widget.TextView;
import com.snagreporter.database.*;
import com.snagreporter.entity.BuildingMaster;
import com.snagreporter.entity.FloorMaster;
import com.snagreporter.entity.ProjectMaster;
import com.snagreporter.entity.StdFloorAreas;
public class ViewImagePage extends Activity
{
	
	String PhotoURL;
	ImageView img;
	Display display;
	String FilePath1;
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.view_image);
        
        PhotoURL=getIntent().getExtras().getString("PhotoURL");
        img=(ImageView)findViewById(R.id.view_Image_myImage);
        
        display = getWindowManager().getDefaultDisplay();
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        
         FilePath1=Environment.getExternalStorageDirectory()+File.separator+"SnagReporter/Pictures/"+PhotoURL+".jpg";
		Bitmap Img1 = decodeFile(FilePath1);
		ImageButton btnI=new ImageButton(this);
		btnI.setImageBitmap(Img1);
		//img.setBackgroundDrawable(btnI.getDrawable());
   		img.setImageBitmap(Img1);
        
        
    }
   
   
    @Override
	public void onConfigurationChanged(Configuration newConfig){
		super.onConfigurationChanged(newConfig);
	}
    
    
    public void BackClick(View v){
    	finish();
    }
    private Bitmap decodeFile(String url){//This is the whole url of the image
		File f=new File(url);
		
	    Bitmap b = null;
	    //int IMAGE_MAX_SIZE=200;
	    int maxWidth=display.getWidth();
	    int maxHeight=display.getHeight()-44;
	    try {
	        //Decode image size
	    	
	    	//@@@@@
	    	int angle = 0;
	    	 Matrix mat = new Matrix();
	        try {
	            File f1 = new File(url);
	            ExifInterface exif = new ExifInterface(f1.getPath());
	            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

	            

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
	        
//	        if(oriNew==1){
//	        	int temp=o.outWidth;
//	        	o.outHeight=o.outWidth;
//	        	o.outWidth=temp;
//	        }

	        
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
	        //Toast.makeText(getApplicationContext(), "Resized To="+b.getHeight()+"X"+b.getWidth(), Toast.LENGTH_LONG).show();
	    } catch (IOException e) {
	    }
	    return b;
	}
}