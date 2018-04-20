package com.snagreporter;

import java.io.File;

import com.snagreporter.R;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RemoteViews.ActionException;

public class MainPage extends Activity 
{
ImageView imgVw1,imgVw2,imgVw3;
	//Boolean isBtn1,isBtn2,isBtn3;
	String strFromImg,strFromImgvw,strFilePath,strMenuType;
	Bitmap BtnImageBmp,BtnYourImg;
	private static final int CAMERA_PIC_REQUEST = 1337;
	private Bitmap mBitmap;
	//int MychildActivity=2;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.upload_photo);
        
        imgVw1=(ImageView)findViewById(R.id.upload_photo_imgvw1);
        imgVw2=(ImageView)findViewById(R.id.upload_photo_imgvw2);
        imgVw3=(ImageView)findViewById(R.id.upload_photo_imgvw3);
        
        
    }
   
    @Override  
	   public void onCreateContextMenu(ContextMenu menu, View v,ContextMenuInfo menuInfo)
    {  
	super.onCreateContextMenu(menu, v, menuInfo);  
	    menu.setHeaderTitle("Choose Photo");  
	    if(strMenuType.equalsIgnoreCase("new"))
	    {
	    menu.add(0, v.getId(), 0, "Choose from Library");  
 	    menu.add(0, v.getId(), 0, "Take with Camera");
	    }
	    else
	    {
	    	 menu.add(0, v.getId(), 0, "Remove"); 
	    	menu.add(0, v.getId(), 0, "Choose from Library");  
	  	    menu.add(0, v.getId(), 0, "Take with Camera");
	    }
	    
	}  
 @Override
	public boolean onContextItemSelected(MenuItem item)
	{
	 if(item.getTitle()=="Choose from Library")
	 {
		 Intent intent=new Intent();
		 intent.setType("image/*");
		 intent.setAction(Intent.ACTION_GET_CONTENT);
		 startActivityForResult(intent,0);
		 return true;
	 }
	 else if(item.getTitle()=="Take with Camera"){
		 Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
			
			startActivityForResult(cameraIntent, CAMERA_PIC_REQUEST);
		 return true;
	 }
	 else
	
	 return true;
	}
 public void btnUpLoadImage1(View v)
 {
	 registerForContextMenu(v);
	strFromImg="img1";
	if(imgVw1.getDrawable()==null)
	{
		strMenuType="new";
	}
	else
	{
		strMenuType="update";
		
	}
		openContextMenu(v);
		unregisterForContextMenu(v);
		
 }
 public void btnUpLoadImage2(View v)
 {
	 registerForContextMenu(v);
	 strFromImg="img2";
	 if(imgVw2.getDrawable()==null)
		{
			strMenuType="new";
		}
		else
		{
			strMenuType="update";
			
		}
	 openContextMenu(v);
		unregisterForContextMenu(v);
		
 }
 public void btnUpLoadImage3(View v)
 {
	 registerForContextMenu(v);
	 strFromImg="img3";
	 if(imgVw3.getDrawable()==null)
	{
			strMenuType="new";
	}
	 else
	{
			strMenuType="update";
	}
	 openContextMenu(v);
	unregisterForContextMenu(v);
		
 }
 protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) 
 {
	 super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
	 switch(requestCode)
	 {
     case 0:
         if(resultCode == RESULT_OK)
         { 
             Uri selectedImage = imageReturnedIntent.getData();
             String[] filePathColumn = {MediaStore.Images.Media.DATA};

             Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
             cursor.moveToFirst();

             int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
             String filePath = cursor.getString(columnIndex);
             cursor.close();
                
           BtnYourImg = BitmapFactory.decodeFile(filePath);
             //showImage();
           
             Intent i=new Intent(this,com.snagreporter.PhotoView.class);
             i.putExtra("BitmapImage", BtnYourImg);
            i.putExtra("strFromImg", strFromImg);
            // startActivity(i);
           
            startActivityForResult(i, 2);
            break;
            
         }
     case 2:
     {
    	 
    	 if(resultCode==10)
    	 {
    	// Log.d("strFromImgvw", "strFromImgvw");
    	 strFromImgvw=imageReturnedIntent.getExtras().getString("strFromImgvw");
    	 strFilePath=imageReturnedIntent.getExtras().getString("strFilePath");
    	//BtnImageBmp=(Bitmap)getIntent().getParcelableExtra("btnBitmap");
    	 BtnImageBmp=BitmapFactory.decodeFile(strFilePath);
    	 if(strFromImgvw.equalsIgnoreCase("img1"))
    	 {
    		 imgVw1.setImageBitmap(BtnImageBmp);
    	 }
    	 else if(strFromImgvw.equalsIgnoreCase("img2"))
    	 {
    		 imgVw2.setImageBitmap(BtnImageBmp);
    	 }
    	 else if(strFromImgvw.equalsIgnoreCase("img3"))
    	 {
    		 imgVw3.setImageBitmap(BtnImageBmp);
    	 }
    	 //Log.d("strFromImgvw",""+strFromImgvw);
    	 }
    	 break;
     }
     case CAMERA_PIC_REQUEST:
     {
    	 mBitmap = (Bitmap) imageReturnedIntent.getExtras().get("data");
      	
    	 Intent i=new Intent(this,com.snagreporter.PhotoView.class);
         i.putExtra("BitmapImage", mBitmap);
         i.putExtra("strFromImg", strFromImg);
         startActivityForResult(i, 2);
     }
     
     
     
     }
 }

}