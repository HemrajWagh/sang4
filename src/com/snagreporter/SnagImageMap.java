/*
 * Copyright (C) 2011 Scott Lund
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.snagreporter;

import java.util.HashMap;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Scroller;
import android.widget.TableLayout.LayoutParams;
import android.widget.Toast;

import com.snagreporter.database.FMDBDatabaseAccess;
import com.snagreporter.entity.*;

public class SnagImageMap extends ImageView {
	RelativeLayout mapLayout;
	
	SnagMaster [] sm;
	Paint paint = new Paint();
	float x=0.0f,y=0.0f;
	float screenwidth,screenheight;
	float currentX;
	float currentY;
	Boolean isFirst=true;
	Boolean touched=false;
	public Context context;
	// mFitImageToScreen
	// if true - initial image resized to fit the screen, aspect ratio may be broken
	// if false- initial image resized so that no empty screen is visible, aspect ratio maintained
	//           image size will likely be larger than screen
	private boolean mFitImageToScreen=true;
	
	// For certain images, it is best to always resize using the original
	// image bits.  This requires keeping the original image in memory along with the
	// current sized version and thus takes extra memory.
	// If you always want to resize using the original, set mScaleFromOriginal to true
	// If you want to use less memory, and the image scaling up and down repeatedly
	// does not blur or loose quality, set mScaleFromOriginal to false
	private boolean mScaleFromOriginal=true;
	
	// mMaxSize controls the maximum zoom size as a multiplier of the initial size.
	// Allowing size to go too large may result in memory problems.
	//  set this to 1.0f to disable resizing
	private float mMaxSize = 1.5f;
	
	
	/*
	 * Touch event handling variables
	 */
	private VelocityTracker mVelocityTracker;

	private int mTouchSlop;
	private int mMinimumVelocity;
	private int mMaximumVelocity;

	private Scroller mScroller;

	private boolean mIsBeingDragged = false;	
	
	HashMap<Integer,TouchPoint> mTouchPoints = new HashMap<Integer,TouchPoint>();
	TouchPoint mMainTouch=null;
	TouchPoint mPinchTouch=null;
	
	/*
	 * Pinch zoom
	 */
	float mInitialDistance;
	boolean mZoomEstablished=false;
	int mLastDistanceChange=0;
	boolean mZoomPending=false;
	
	/*
	 * Paint objects for drawing info bubbles
	 */
	Paint textPaint;
	Paint textOutlinePaint;
	Paint bubblePaint;
	Paint bubbleShadowPaint;
	
	/*
	 * Bitmap handling
	 */
	Bitmap mImage;
	Bitmap mOriginal;
	

	// Info about the bitmap (sizes, scroll bounds)
	// initial size
	int mImageHeight;  
	int mImageWidth;
	float mAspect;
	// scaled size
	int mExpandWidth;
	int mExpandHeight;
	// the right and bottom edges (for scroll restriction)
	int mRightBound;
	int mBottomBound;
	// the current zoom scaling (X and Y kept separate)
	private float mResizeFactorX;
	private float mResizeFactorY;
	// minimum height/width for the image
	int mMinWidth=-1;
	int mMinHeight=-1;
	// maximum height/width for the image
	int mMaxWidth=-1;
	int mMaxHeight=-1;

	// the position of the top left corner relative to the view	
	int mScrollTop;
	int mScrollLeft;

	// view height and width
	int mViewHeight=-1;
	int mViewWidth=-1;

	/*
	 * containers for the image map areas
	 */
	
	// click handler list

	/*
	 * Constructors
	 */
	public SnagImageMap(Context context) {
		super(context);
		init();
		this.context=context;
	//	sm=getSnag();
	}

	public SnagImageMap(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
		this.context=context;
		//loadAttributes(attrs);		
	}

	public SnagImageMap(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
		this.context=context;
		//loadAttributes(attrs);
	}
	
	/**
	 * get the map name from the attributes and load areas from xml
	 * @param attrs
	 */
	
	/**
	 * parse the maps.xml resource and pull out the areas
	 * @param map - the name of the map to load
	 */
	
	/**
	 * initialize the view
	 */
	public SnagMaster[] getSnag()
	{
		FMDBDatabaseAccess fdb = new FMDBDatabaseAccess(context);
		SnagMaster[] arr= fdb.getSnagsXYvalue(this.getTag().toString());
		return arr;
	}
	private void init() {
		// set up paint objects

		// create a scroller for flinging
		mScroller = new Scroller(getContext());

		// get some default values from the system for touch/drag/fling
		final ViewConfiguration configuration = ViewConfiguration
				.get(getContext());
		mTouchSlop = configuration.getScaledTouchSlop();
		mMinimumVelocity = configuration.getScaledMinimumFlingVelocity();
		mMaximumVelocity = configuration.getScaledMaximumFlingVelocity();
	}

	/*
	 * These methods will be called when images or drawables are set
	 * in the XML for the view.  We handle either bitmaps or drawables
	 * @see android.widget.ImageView#setImageBitmap(android.graphics.Bitmap)
	 */
	@Override
	public void setImageBitmap(Bitmap bm) {
		if (mImage==mOriginal) {
			mOriginal=null;
		} else {
			mOriginal.recycle();
			mOriginal=null;
		}
		if (mImage != null) {
			mImage.recycle();
			mImage=null;
		}
		mImage = bm;
		mOriginal = bm;
		try{
		mImageHeight = mImage.getHeight();
		}
		catch(Exception e){
			mImageHeight=0;
		}
		mImageWidth = mImage.getWidth();
		mAspect = (float)mImageWidth / mImageHeight;
		setInitialImageBounds();
	}

	@Override
	public void setImageDrawable(Drawable drawable) {		
		if (drawable instanceof BitmapDrawable) {
			BitmapDrawable bd = (BitmapDrawable) drawable; 			
			setImageBitmap(bd.getBitmap());
		}
	}

	/*
	 * Called by the scroller when flinging
	 * @see android.view.View#computeScroll()
	 */
	
	@Override
	public void computeScroll() {
		if (mScroller.computeScrollOffset()) {
			int oldX = mScrollLeft;
			int oldY = mScrollTop;
			
			int x = mScroller.getCurrX();
			int y = mScroller.getCurrY();

			if (oldX != x) {
				moveX(x-oldX);
			}
			if (oldY != y) {
				moveY(y-oldY);
			}
		}
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int widthMode = MeasureSpec.getMode(widthMeasureSpec);
		int widthSize = MeasureSpec.getSize(widthMeasureSpec);

		int heightMode = MeasureSpec.getMode(heightMeasureSpec);
		int heightSize = MeasureSpec.getSize(heightMeasureSpec);

		int chosenWidth = chooseDimension(widthMode, widthSize);
		int chosenHeight = chooseDimension(heightMode, heightSize);

		setMeasuredDimension(chosenWidth, chosenHeight);
	}

	private int chooseDimension(int mode, int size) {
		if (mode == MeasureSpec.AT_MOST || mode == MeasureSpec.EXACTLY) {
			return size;
		} else { // (mode == MeasureSpec.UNSPECIFIED)
			return getPreferredSize();
		}
	}

	/**
	 * set the initial bounds of the image
	 */
	void setInitialImageBounds() {
		if (mFitImageToScreen) {
			setInitialImageBoundsFitImage();
		} else {
			setInitialImageBoundsFillScreen();
		}
	}
	
	/**
	 * setInitialImageBoundsFitImage sets the initial image size to match the
	 * screen size.  aspect ratio may be broken
	 */	
	void setInitialImageBoundsFitImage() {
		if (mImage != null) {
			if (mViewWidth > 0) {			
				mMinHeight = mViewHeight;
				mMinWidth = mViewWidth; 
				mMaxWidth = (int)(mMinWidth * mMaxSize);
				mMaxHeight = (int)(mMinHeight * mMaxSize);				
					
				mScrollTop = 0;
				mScrollLeft = 0;
				scaleBitmap(mMinWidth, mMinHeight);
		    }
		}
	}
	
	/**
	 * setInitialImageBoundsFillScreen sets the initial image size to so that there
	 * is no uncovered area of the device
	 */	
	void setInitialImageBoundsFillScreen() {
		if (mImage != null) {
			if (mViewWidth > 0) {
				boolean resize=false;
				
				int newWidth=mImageWidth;
				int newHeight=mImageHeight;
			
				// The setting of these max sizes is very arbitrary
				// Need to find a better way to determine max size
				// to avoid attempts too big a bitmap and throw OOM
				if (mMinWidth==-1) { 
					// set minimums so that the largest
					// direction we always filled (no empty view space)
					// this maintains initial aspect ratio
					if (mViewWidth > mViewHeight) {
						mMinWidth = mViewWidth;
						mMinHeight = (int)(mMinWidth/mAspect);
					} else {
						mMinHeight = mViewHeight;
						mMinWidth = (int)(mAspect*mViewHeight);
					}					
					mMaxWidth = (int)(mMinWidth * 1.5f);
					mMaxHeight = (int)(mMinHeight * 1.5f);
				}
					
				if (newWidth < mMinWidth) {
					newWidth = mMinWidth;
					newHeight = (int) (((float) mMinWidth / mImageWidth) * mImageHeight);
					resize = true;
				}
				if (newHeight < mMinHeight) {
					newHeight = mMinHeight;
					newWidth = (int) (((float) mMinHeight / mImageHeight) * mImageWidth);
					resize = true;
				}
				
				mScrollTop = 0;
				mScrollLeft = 0;
				
				// scale the bitmap
				if (resize) {
					scaleBitmap(newWidth, newHeight);
				} else {
					mExpandWidth=newWidth;
					mExpandHeight=newHeight;					
					mResizeFactorX = ((float) newWidth / mImageWidth);
					mResizeFactorY = ((float) newHeight / mImageHeight);
					mRightBound = 0 - (mExpandWidth - mViewWidth);
					mBottomBound = 0 - (mExpandHeight - mViewHeight);
				}
			}
		}
	}
	
	
	/**
	 * Set the image to new width and height
	 * create a new scaled bitmap and dispose of the previous one
	 * recalculate scaling factor and right and bottom bounds
	 * @param newWidth
	 * @param newHeight
	 */
	public void scaleBitmap(int newWidth, int newHeight) {
		// Technically since we always keep aspect ratio intact
		// we should only need to check one dimension.
		// Need to investigate and fix
		if ((newWidth > mMaxWidth) || (newHeight > mMaxHeight)) {
			newWidth = mMaxWidth;
			newHeight = mMaxHeight;
		}
		if ((newWidth < mMinWidth) || (newHeight < mMinHeight)) {
			newWidth = mMinWidth;
			newHeight = mMinHeight;			
		}

		if ((newWidth != mExpandWidth) || (newHeight!=mExpandHeight)) {	
			// NOTE: depending on the image being used, it may be 
			//       better to keep the original image available and
			//       use those bits for resize.  Repeated grow/shrink
			//       can render some images visually non-appealing
			//       see comments at top of file for mScaleFromOriginal
			// try to create a new bitmap
			// If you get a recycled bitmap exception here, check to make sure
			// you are not setting the bitmap both from XML and in code
			Bitmap newbits = Bitmap.createScaledBitmap(mScaleFromOriginal ? mOriginal:mImage, newWidth,
					newHeight, true);
			// if successful, fix up all the tracking variables
			if (newbits != null) {
				if (mImage!=mOriginal) {
					mImage.recycle();
				}
				mImage = newbits;
				mExpandWidth=newWidth;
				mExpandHeight=newHeight;
				mResizeFactorX = ((float) newWidth / mImageWidth);
				mResizeFactorY = ((float) newHeight / mImageHeight);
				
				mRightBound = mExpandWidth>mViewWidth ? 0 - (mExpandWidth - mViewWidth) : 0;
				mBottomBound = mExpandHeight>mViewHeight ? 0 - (mExpandHeight - mViewHeight) : 0;
			}							
		}
	}
	
	void resizeBitmap( int amount ) {
		int adjustWidth = amount;
		int adjustHeight = (int)(adjustWidth / mAspect);
		scaleBitmap( mExpandWidth+adjustWidth, mExpandHeight+adjustHeight);
	}

	/**
	 * watch for screen size changes and reset the background image
	 */
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);

		// save device height width, we use it a lot of places
		mViewHeight = h;
		mViewWidth = w;

		// fix up the image
		setInitialImageBounds();
	}

	private int getPreferredSize() {
		return 300;
	}

	/**
	 * the onDraw routine when we are using a background image
	 * 
	 * @param canvas
	 */
	protected void drawMap(Canvas canvas) {
		canvas.save();
		
		if (mImage != null) {
			if (!mImage.isRecycled()) {
				canvas.drawBitmap(mImage, mScrollLeft, mScrollTop, null);
			}
		}
		canvas.restore();
	}

	/**
	 * Paint the view
	 *   image first, location decorations next, bubbles on top
	 *   
	 */

	@Override
	protected void onDraw(Canvas canvas)
	{
		drawMap(canvas);
		
		 screenwidth= context.getResources().getDisplayMetrics().widthPixels;
		 screenheight= context.getResources().getDisplayMetrics().heightPixels;
//		
//		FMDBDatabaseAccess fdb = new FMDBDatabaseAccess(context);
//		sm = fdb.getSnagsXYvalue(this.getTag().toString());
//		//sm[0].setXValue(0.4);
		//sm[0].setYValue(0.5);
		 if(sm==null)
		 {
			 sm=getSnag();
		 }
		if(isFirst)
		{
			isFirst=false;
			try{
			for(int i=0;i<sm.length;i++)
			{
			  paint.setStrokeWidth(30);
			  //String str=""+(sm[i].getXValue()*screenwidth);
			  x = sm[i].getXValue()*screenwidth;
			  //str=""+(sm[i].getYValue()*screenheight);
			  y = sm[i].getYValue()*screenheight;
			  paint.setColor(Color.RED);
			  if(x!=0.0f && y!=0.0f)
			  {
				 // canvas.drawRect(x+7.5f, y+7.5f, x-7.5f, y-7.5f,paint);
				 // canvas.drawRect(x+7.5f, y+7.5f, x-7.5f, y-7.5f,paint);
				  SnagImageMapping objmap=(SnagImageMapping) context;
//				  
				  mapLayout =(RelativeLayout)(objmap).findViewById(R.id.plot_image_layout);
				  ImageView mapImage = new ImageView(context);
//				  
			  float value;
				  if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
				  {
					  value=screenheight*0.078125f;
			      }
				  else
    			  {
					  value=screenheight*0.052083332f;
         			  
				  }
				  
				  float imght=value;
				  float imgwdth=imght;
				  RelativeLayout.LayoutParams par=new RelativeLayout.LayoutParams((int)imgwdth,(int)imght);
				  par.leftMargin=(int)(x-(value/2));
				  par.topMargin=(int)(y-(value/2));
				  if(sm[i].getSnagStatus().equalsIgnoreCase("pending"))
				  {
					  mapImage.setBackgroundResource(R.drawable.plot_yellow);
				  }
				  else if(sm[i].getSnagStatus().equalsIgnoreCase("reinspected & unresolved"))
				  {
					  mapImage.setBackgroundResource(R.drawable.plot_red);
				  }
				  else if(sm[i].getSnagStatus().equalsIgnoreCase("resolved"))
				  {
					  mapImage.setBackgroundResource(R.drawable.plot_green);
				  }
				//  R.drawable.pl
				  mapImage.setId(i);
				 // mapImage.setTag();
				  mapImage.setOnClickListener(new OnClickListener() {
					
					public void onClick(View v) {
						// TODO Auto-generated method stub
						
						int id=v.getId();
						float setX,setY;
						 String str2=""+(sm[id].getXValue()*screenwidth);
						 setX = Float.parseFloat(str2);
						 
						 
						 str2=""+(sm[id].getYValue()*screenheight);
						  setY = Float.parseFloat(str2);
						//Toast.makeText(getContext(), "By image click",Toast.LENGTH_SHORT).show();
						//Log.d("x y",""+setX+" "+setY);
						//Button img=new Button(context);
						//img.setBackgroundResource(R.drawable.back_blue_button);
						//img.setText("from"+""+sm[id].getID());
//						 RelativeLayout.LayoutParams par2=new RelativeLayout.LayoutParams(android.widget.RelativeLayout.LayoutParams.WRAP_CONTENT,android.widget.RelativeLayout.LayoutParams.WRAP_CONTENT);
//						 par2.leftMargin=(int)(setX);
//						  par2.topMargin=(int)(setY);
//						  popMsg.setLayoutParams(par2);
//						  popMsg.setText("from"+""+sm[id].getID());
//						  //popMsg.setText("from sfsf dfdfsfd dfdsf df dfdfdf d fdfedfd dfdfdf dfdf df dfdfd ");
//						  popMsg.setVisibility(View.VISIBLE);
						//RelativeLayout.LayoutParams par2=v.(RelativeLayout.LayoutParams)getTag();
						 // img.setLayoutParams(par2);
						 // mapLayout.addView(img);
						  String text="Project - "+sm[id].getProjectName()+"\n"+"Building - "+sm[id].getBuildingName()+"\n"+"Floor - "+sm[id].getFloor()+"\n"+"Apartment - "+sm[id].getApartment()+"\n"+"Area - "+sm[id].getAptAreaName()+"\n"+"SnagType - "+sm[id].getSnagType()+"\n"+"FaultType - "+sm[id].getFaultType()+"\n"+"Status - "+sm[id].getSnagStatus()+"\n"+"ReportDate - "+sm[id].getReportDate()+"\n"+"SnagDetails - "+sm[id].getSnagDetails()+"\n"+"PriorityLevel - "+sm[id].getSnagPriority()+"\n"+"Cost - "+sm[id].getCost()+"\n"+"CostTO - "+sm[id].getCostTo()+"\n"+"AllocatedToName - "+sm[id].getAllocatedToName()+"\n"+"InspectorName - "+sm[id].getInspectorName()+"\n"+"ContractorName - "+sm[id].getContractorName()+"\n"+"SubContractorName - "+sm[id].getSubContractorName()+"\n"+"ContractorStatus - "+sm[id].getContractorStatus();
						 
						  
//						  
						 final int loc=id;
						  new AlertDialog.Builder(context)
				    	    .setTitle("Snag Detail")
				    	    .setMessage(""+text)
				    	    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
				    	        public void onClick(DialogInterface dialog, int which) { 
				    	        	//Toast.makeText(context,""+0.052083332*screenwidth+" "+0.078125*screenheight,Toast.LENGTH_LONG).show();
				    	            // continue with delete
				    	        }
				    	     })
				    	    .show();
					}
				});
				  
				  mapImage.setLayoutParams(par);
				  mapLayout.addView(mapImage);
				  
			  }
			  if(touched)
			  {
			//  if((currentX>=(x-7.5f) && currentX<=(x+7.5f)) && ((currentY<=(y+7.5f)) && currentY>=( y-7.5f)))
			 // {
				  if((currentX>=(x-7.5f) && currentX<=(x+7.5f)) && ((currentY<=(y+7.5f)) && currentY>=( y-7.5f)))
				  {
					  
					 // Toast.makeText(getContext(), "Click",Toast.LENGTH_SHORT).show();
					//  Log.d("in the touch","");
 //			  String strID=sm[i].getID();
 //			  		SnagImageMapping objmap=(SnagImageMapping) context;
////				  
//				  RelativeLayout mapLayout =(RelativeLayout)(objmap).findViewById(R.id.plot_image_layout);
//				  ImageView mapImage = new ImageView(context);
////				  
//				  RelativeLayout.LayoutParams par=new RelativeLayout.LayoutParams(300,300);
//				  mapImage.setBackgroundResource(R.drawable.plot_pink_gray);
//				  par.setMargins((int)(x+7.5f), (int)(y+7.5f), (int)(x-7.5f), (int)(y-7.5f));
//				  mapImage.setLayoutParams(par);
//				  mapLayout.addView(mapImage);
//				  Toast.makeText(getContext(), "Green",Toast.LENGTH_SHORT).show();
				    
			    }
			  touched=false;
			  }
			 
		}
		}
		catch(Exception e){
		}	
	 }
		
	}
	
	/*
	 * Touch handler
	 *   This handler manages an arbitrary number of points
	 *   and detects taps, moves, flings, and zooms 
	 */	

	
	/*public boolean onTouchEvent(MotionEvent ev) {
		int id;
		
	    //getting the touched x and y position
		currentX = ev.getX();
		currentY = ev.getY();
	     touched = true;
	    //invalidate();
	    
	     //if((x>=(x-15) && x<=(x+15)) && ((y<=(y+15)) && y>=(y-15)))
	   
		if (mVelocityTracker == null) {
			mVelocityTracker = VelocityTracker.obtain();
		}
		mVelocityTracker.addMovement(ev);
		
		final int action = ev.getAction();
		
		int pointerCount = ev.getPointerCount(); 
        int index = 0;
        
        if (pointerCount > 1) {
        	// If you are using new API (level 8+) use these constants
        	// instead as they are much better names
        	//index = (action & MotionEvent.ACTION_POINTER_INDEX_MASK);
        	//index = index >> MotionEvent.ACTION_POINTER_INDEX_SHIFT;
        	        	
            // for api 7 and earlier we are stuck with these
        	// constants which are poorly named
        	// ID refers to INDEX, not the actual ID of the pointer
        	index = (action & MotionEvent.ACTION_POINTER_ID_MASK);
        	index = index >> MotionEvent.ACTION_POINTER_ID_SHIFT;
        }

		switch (action & MotionEvent.ACTION_MASK) {
		case MotionEvent.ACTION_DOWN:
			// Clear all touch points
			// In the case where some view up chain is messing with our
			// touch events, it is possible to miss UP and POINTER_UP 
			// events.  Whenever ACTION_DOWN happens, it is intended
			// to always be the first touch, so we will drop tracking
			// for any points that may have been orphaned
			for ( TouchPoint t: mTouchPoints.values() ) {
				onLostTouch(t.getTrackingPointer());
			}
			// fall through planned
		case MotionEvent.ACTION_POINTER_DOWN:
			id = ev.getPointerId(index);
			onTouchDown(id,ev.getX(index),ev.getY(index));
			break;

		case MotionEvent.ACTION_MOVE:
			for (int p=0;p<pointerCount;p++) {
				id = ev.getPointerId(p);
				TouchPoint t = mTouchPoints.get(id);
				if (t!=null) {
					onTouchMove(t,ev.getX(p),ev.getY(p));		
				}
				// after all moves, check to see if we need
				// to process a zoom
			}
			break;
		case MotionEvent.ACTION_UP:
		case MotionEvent.ACTION_POINTER_UP:
			id = ev.getPointerId(index);			
			onTouchUp(id);
			break;
		case MotionEvent.ACTION_CANCEL:
			// Clear all touch points on ACTION_CANCEL
			// according to the google devs, CANCEL means cancel 
			// tracking every touch.  
			// cf: http://groups.google.com/group/android-developers/browse_thread/thread/8b14591ead5608a0/ad711bf24520e5c4?pli=1
			for ( TouchPoint t: mTouchPoints.values() ) {
				onLostTouch(t.getTrackingPointer());
			}
			// let go of the velocity tracker per API Docs
			if (mVelocityTracker != null) {
				mVelocityTracker.recycle();
				mVelocityTracker = null;
			}
			break;
		}
		return true;
	} */
	
	
	/*void onTouchDown(int id, float x, float y) {
        // create a new touch point to track this ID
		TouchPoint t=null;
		synchronized (mTouchPoints) {
			// This test is a bit paranoid and research should
			// be done sot that it can be removed.  We should
			// not find a touch point for the id
			t = mTouchPoints.get(id);
			if (t == null) {
				t = new TouchPoint(id);
				mTouchPoints.put(id,t);
			}
			
			// for pinch zoom, we need to pick two touch points
			// they will be called Main and Pinch 
			if (mMainTouch == null) {
				mMainTouch = t;
			} else {
				if (mPinchTouch == null) {
					mPinchTouch=t;
					// second point established, set up to 
					// handle pinch zoom
					startZoom();
				}
			}
		}
		t.setPosition(x,y);		
	}*/
	
	/*
	 * Track pointer moves
	 */
	/*void onTouchMove(TouchPoint t, float x, float y) {
		// mMainTouch will drag the view, be part of a
		// pinch zoom, or trigger a tap
		if (t == mMainTouch) {
			if (mPinchTouch == null) {
				// only on point down, this is a move
				final int deltaX = (int) (t.getX() - x);
				final int xDiff = (int) Math.abs(t.getX() - x);

				final int deltaY = (int) (t.getY() - y);
				final int yDiff = (int) Math.abs(t.getY() - y);

				if (!mIsBeingDragged) {
					if ((xDiff > mTouchSlop) || (yDiff > mTouchSlop)) {
						// start dragging about once the user has
						// moved the point far enough
						mIsBeingDragged = true;
					}
				} else {
					// being dragged, move the image
					if (xDiff > 0) {
						moveX(-deltaX);
					}
					if (yDiff > 0) {
						moveY(-deltaY);
					}
					t.setPosition(x, y);					
				}
			} else {
				// two fingers down means zoom				
				t.setPosition(x, y);
				onZoom();
			}
		} else {
			if (t == mPinchTouch) {
				// two fingers down means zoom
				t.setPosition(x, y);
				onZoom();
			}
		}
	}*/
	
	/*
	 * touch point released
	 */
	/*void onTouchUp(int id) {
		synchronized (mTouchPoints) {
			TouchPoint t = mTouchPoints.get(id);
			if (t != null) {
				if (t == mMainTouch) {
					if (mPinchTouch==null) {						
						// This is either a fling or tap
						if (mIsBeingDragged) {
							// view was being dragged means this is a fling
							final VelocityTracker velocityTracker = mVelocityTracker;
							velocityTracker.computeCurrentVelocity(1000, mMaximumVelocity);
							
							int xVelocity = (int) velocityTracker.getXVelocity();
							int yVelocity = (int) velocityTracker.getYVelocity();

							int xfling = Math.abs(xVelocity) > mMinimumVelocity ? xVelocity
									: 0;
							int yfling = Math.abs(yVelocity) > mMinimumVelocity ? yVelocity
									: 0;

							if ((xfling != 0) || (yfling != 0)) {
								fling(-xfling, -yfling);
							}

							mIsBeingDragged = false;

							// let go of the velocity tracker
							if (mVelocityTracker != null) {
								mVelocityTracker.recycle();
								mVelocityTracker = null;
							}
						} else {
							// no movement - this was a tap
							invalidate();
							//onScreenTapped((int)mMainTouch.getX(), (int)mMainTouch.getY());
						}
					} 					
					mMainTouch=null;
					mZoomEstablished=false;
				}
				if (t == mPinchTouch) {
					// lost the 2nd pointer
					mPinchTouch=null;
					mZoomEstablished=false;
				}
				mTouchPoints.remove(id);
				// shuffle remaining pointers so that we are still
				// tracking.  This is necessary for proper action
				// on devices that support > 2 touches 
				regroupTouches();
			} else {
				// lost this ID somehow
				// This happens sometimes due to the way some
				// devices manage touch
			}
		}
	}*/
	
	/*
	 * Touch handling varies from device to device, we may think we
	 * are tracking an id which goes missing
	 */
	/*void onLostTouch(int id) {
		synchronized (mTouchPoints) {
			TouchPoint t = mTouchPoints.get(id);
			if (t != null) {
				if (t == mMainTouch) {
					mMainTouch=null;
				}
				if (t == mPinchTouch) {
					mPinchTouch=null;
				}
				mTouchPoints.remove(id);
				regroupTouches();
			}
		}
	}*/
	
	/*
	 * find a touch pointer that is not being used as main or pinch
	 */
	TouchPoint getUnboundPoint() {
		TouchPoint ret=null;		
		for (Integer i : mTouchPoints.keySet()) {
			TouchPoint p = mTouchPoints.get(i);
			if ((p!=mMainTouch)&&(p!=mPinchTouch)) {
				ret = p;
				break;
			}
		}
		return ret;
	}
	
	/*
	 * go through remaining pointers and try to have
	 * MainTouch and then PinchTouch if possible
	 */
	
	void regroupTouches() {
		int s=mTouchPoints.size();
		if (s>0) {
			if (mMainTouch == null) {
				if (mPinchTouch != null) {
					mMainTouch=mPinchTouch;
					mPinchTouch=null;
				} else {
					mMainTouch=getUnboundPoint();
				}
			}
			if (s>1) {
				if (mPinchTouch == null) {
					mPinchTouch=getUnboundPoint();
					startZoom();
				}
			}
		}
	}
	
	/*
	 * Called when the second pointer is down indicating that we
	 * want to do a pinch-zoom action
	 */
	void startZoom() {
		// this boolean tells the system that it needs to 
		// initialize itself before trying to zoom
		// This is cleaner than duplicating code
		// see processZoom
		mZoomEstablished=false;
	}	
	
	/*
	 * one of the pointers for our pinch-zoom action has moved
	 * Remember this until after all touch move actions are processed.
	 */
	void onZoom() {
		mZoomPending=true;
	}

	
	/*
	 * Screen tapped x, y is screen coord from upper left and does not account
	 * for scroll
	 */
	void onScreenTapped(int x, int y) {
		boolean missed = true;	
		if (missed) {
			// managed to miss everything, clear bubbles
			//mBubbleMap.clear();
			invalidate();
		}
	}

	// process a fling by kicking off the scroller
	public void fling(int velocityX, int velocityY) {
		int startX = mScrollLeft;
		int startY = mScrollTop;
		
		mScroller.fling(startX, startY, -velocityX, -velocityY, mRightBound, 0,
				mBottomBound, 0);
        
		invalidate();
	}
	
	/*
	 * move the view to this x, y 
	 */
	public void moveTo(int x, int y) {
		mScrollLeft = x;
		if (mScrollLeft > 0) {
			mScrollLeft = 0;
		}
		if (mScrollLeft < mRightBound) {
			mScrollLeft = mRightBound;
		}
		mScrollTop=y;
		if (mScrollTop > 0) {
			mScrollTop = 0;
		}
		if (mScrollTop < mBottomBound) {
			mScrollTop = mBottomBound;
		}
		invalidate();
	}

	/*
	 * move the view by this delta in X direction
	 */
	public void moveX(int deltaX) {
		mScrollLeft = mScrollLeft + deltaX;
		if (mScrollLeft > 0) {
			mScrollLeft = 0;
		}
		if (mScrollLeft < mRightBound) {
			mScrollLeft = mRightBound;
		}
		invalidate();
	}

	/*
	 * move the view by this delta in Y direction
	 */
	public void moveY(int deltaY) {
		mScrollTop = mScrollTop + deltaY;
		if (mScrollTop > 0) {
			mScrollTop = 0;
		}
		if (mScrollTop < mBottomBound) {
			mScrollTop = mBottomBound;
		}
		invalidate();
	}
	
	/*
	 * A class to track touches
	 */
	class TouchPoint {
		int _id;
		float _x;
		float _y;
		
		TouchPoint(int id) {
			_id=id;
			_x=0f;
			_y=0f;			
		}
		int getTrackingPointer() {
			return _id;
		}
		void setPosition(float x, float y) {
			if ((_x != x) || (_y != y)) {
				_x=x;
				_y=y;
			}
		}
		float getX() {
			return _x;
		}
		float getY() {
			return _y;
		}
	}
}
