package com.snagreporter.paint;

import java.util.ArrayList;

import android.R.color;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.AvoidXfermode.Mode;
import android.graphics.Bitmap;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.EmbossMaskFilter;
import android.graphics.MaskFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.opengl.Matrix;
import android.util.AttributeSet;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

public class DrawPanel extends View {
	public float height;
	public static int ct=0;
	//public Paint       mPaint;
	public ArrayList<Paint> ArrayPaint=new ArrayList<Paint>();
	public float Stroke=7;
	private MaskFilter  mEmboss;
	private MaskFilter  mBlur;
	public Bitmap  mBitmap;
	private Canvas  mCanvas;
	//public  Path    mPath;
	public ArrayList<Path> ArrayPath=new ArrayList<Path>();
	public boolean mPaintable = true;
	//public static Context c;
	//private Rect r;
	double scalefactor=1.0;
	int scaled=0;
	double translateX=0,translateY=0;
	
	public int PaintColor=Color.RED;
	
	public Paint   mBitmapPaint;
	{
//	mPaint = new Paint();
//	mPaint.setAntiAlias(true);
//	mPaint.setDither(true);
//	mPaint.setColor(Color.RED);
//	mPaint.setStyle(Paint.Style.STROKE);
//	mPaint.setStrokeJoin(Paint.Join.ROUND);
//	mPaint.setStrokeCap(Paint.Cap.ROUND);
//	mPaint.setStrokeWidth(7);
	
	
	mEmboss = new EmbossMaskFilter(new float[] { 1, 1, 1 },
	                               0.4f, 6, 3.5f);

	mBlur = new BlurMaskFilter(8, BlurMaskFilter.Blur.NORMAL);

	
	
}
	
	public DrawPanel(Context context,AttributeSet attrs) {
		super(context,attrs);
		// TODO Auto-generated constructor stub
		mBitmap = Bitmap.createBitmap(50, 50, Bitmap.Config.ARGB_8888);
	    mCanvas = new Canvas(mBitmap);
	    //mPath = new Path();
	    mBitmapPaint = new Paint(Paint.DITHER_FLAG);
	}
		
	
	
	
	public DrawPanel(Context context){
		super(context);
		
	}
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
	    super.onSizeChanged(w, h, oldw, oldh);
//	    if(oldw!=0){
//	    	//scalefactor=w/oldw;
//	    	if(w>oldw)
//	    		scalefactor=2;
//	    	else 
//	    		scalefactor=0.5;
//	    	
//	    	translateX=w-oldw;
//	    	translateY=h-oldh;
//	    }
	}

	@SuppressLint("WrongCall")
	@Override
	protected void onDraw(Canvas canvas) {
		float aa= mBitmap.getWidth();
		float ad= mBitmap.getHeight();
	    canvas.drawBitmap(mBitmap,0,0,mBitmapPaint);
	    if(ct==1){
			  //mPath.reset();
			  
			 //mPath.moveTo(0, 0);
			// mPath.quadTo(0, 0, 0, 0);
			// mPath.lineTo(0, 0);
			 invalidate();
			  
			  //canvas.drawPath(mPath, mPaint);
			  ct=0;
			  onDraw(canvas);
			   
		   }
	    for(int i=0;i<ArrayPath.size();i++)
	    	canvas.drawPath(ArrayPath.get(i), ArrayPaint.get(i));
//	    if(scalefactor!=1){
//	    	canvas.save();
//	    	canvas.scale((float)scalefactor, (float)scalefactor, 0, 0);
//	    	canvas.translate((float)((translateX)/scalefactor), (float)((translateY)/scalefactor));
//	    	canvas.drawPath(mPath, mPaint);
//	    	canvas.restore();
//	    }
	    
	}
	public void ClearDrawing(){
		//mPath.reset();
		if(ArrayPath.size()>0){
			ArrayPath.get(ArrayPath.size()-1).reset();
			ArrayPath.remove(ArrayPath.size()-1);
			ArrayPaint.remove(ArrayPaint.size()-1);
		}
		invalidate();
		//ct=1;
		
	}
	
	
	
	private float mX, mY;
	private static final float TOUCH_TOLERANCE = 4;

	private void touch_start(float x, float y) {
	    //mPath.reset();
		{
			Paint mPaint = new Paint();
			mPaint.setAntiAlias(true);
			mPaint.setDither(true);
			mPaint.setColor(PaintColor);
			mPaint.setStyle(Paint.Style.STROKE);
			mPaint.setStrokeJoin(Paint.Join.ROUND);
			mPaint.setStrokeCap(Paint.Cap.ROUND);
			mPaint.setStrokeWidth(Stroke);
			ArrayPaint.add(mPaint);
		}
		
		
		Path mPath=new Path();
		ArrayPath.add(mPath);
		ArrayPath.get(ArrayPath.size()-1).moveTo(x, y);
	    
	   mX = x;
	    mY = y;
	}
	private void touch_move(float x, float y) {
	   float dx = Math.abs(x - mX);
	    float dy = Math.abs(y - mY);
	  if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
		  ArrayPath.get(ArrayPath.size()-1).quadTo(mX, mY, (x + mX)/2, (y + mY)/2);
	       
	        //Screen3.sounds.play(Screen3.key, 1, 1, 1, 0, 1);
	        //mPath.lineTo(x, y);
	        mX = x;
	        mY = y;
	    }
	}
	private void touch_up(float x, float y) {
		ArrayPath.get(ArrayPath.size()-1).lineTo(mX, mY);
	    
	    //ArrayPath.add(mPath);
	    //mPath=new Path();
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if(mPaintable){
	    float x = event.getX();
	    float y = event.getY();
	    //height=Screen3.getheight();
	    //stroker(height);
	    switch (event.getAction()) {
	        case MotionEvent.ACTION_DOWN:
	      
	        	//if(Screen3.count_1==0 && (Screen3.position>2)){}//changed for free app
//	        	if((Screen3.position>3)){}
//	        	else
	        		touch_start(x, y);
	        
	          invalidate();
	            break;
	        case MotionEvent.ACTION_MOVE:
	        	
	        	//if(Screen3.count_1==0 && (Screen3.position>2)){}
//	        	if((Screen3.position>3)){}
//	        	else
	        		touch_move(x, y);
	        		
	        	
	        	invalidate();
	            break;
	        case MotionEvent.ACTION_UP:
	        	
	        	//if(Screen3.count_1==0 && (Screen3.position>2)){}
//	        	if((Screen3.position>3)){}
//	        	else
	        		touch_up(x,y);
	        		
	        	
	        	invalidate();
	            break;
	    }
		}
	    return true;
	}
	public void stroker(float height){
		if(height<=240){
		for(int i=0;i<ArrayPaint.size();i++)	
			ArrayPaint.get(i).setStrokeWidth(8);
		Stroke=8;
	}
	else if(height>240 && height<=320){
		for(int i=0;i<ArrayPaint.size();i++)	
			ArrayPaint.get(i).setStrokeWidth(12);
		Stroke=12;
	}
	else if(height>320 && height<=480){
		for(int i=0;i<ArrayPaint.size();i++)	
			ArrayPaint.get(i).setStrokeWidth(16);
		Stroke=16;
	}
	else if(height>480){
		for(int i=0;i<ArrayPaint.size();i++)	
			ArrayPaint.get(i).setStrokeWidth(20);
		Stroke=20;
	}
	//else{
		//mPaint.setStrokeWidth(10);
		//Toast.makeText(getContext(), "came to panel else if="+ht,Toast.LENGTH_LONG).show();
	//}
	}
	
	}
