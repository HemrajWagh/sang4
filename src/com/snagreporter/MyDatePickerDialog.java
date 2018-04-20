package com.snagreporter;

import java.util.Calendar;
import java.util.Date;

import android.app.DatePickerDialog;
import android.content.Context;
import android.text.format.DateUtils;
import android.util.Log;
import android.widget.DatePicker;


	public class MyDatePickerDialog extends DatePickerDialog{

		private Date maxDate;
		private Date minDate;

		public MyDatePickerDialog(Context context, OnDateSetListener callBack, int year, int monthOfYear, int dayOfMonth) {
		    super(context, callBack, year, monthOfYear, dayOfMonth);        
		    init(year, monthOfYear, dayOfMonth);
		}

		public MyDatePickerDialog(Context context, int theme, OnDateSetListener callBack, int year, int monthOfYear,    int dayOfMonth) {
		    super(context, theme, callBack, year, monthOfYear, dayOfMonth);
		    init(year, monthOfYear, dayOfMonth);
		}

		private void init(int year, int monthOfYear, int dayOfMonth){
		    Calendar cal = Calendar.getInstance();

		    cal.set(cal.get(Calendar.YEAR)-100, Calendar.JANUARY, 1);
		    minDate = cal.getTime();

		    cal.set(year, monthOfYear, dayOfMonth);
		    cal.set(cal.get(Calendar.YEAR)+100, Calendar.JANUARY, 1);
		    maxDate = cal.getTime();

		    cal.set(year, monthOfYear, dayOfMonth);
		}

		public void onDateChanged (final DatePicker view, int year, int month, int day){
			try{
		    Calendar cal = Calendar.getInstance();
		    cal.set(year, month, day);
		    Date currentDate = cal.getTime();

		    Calendar cal2 = Calendar.getInstance();
		    cal2.setTime(maxDate);
		    
		    long UU=currentDate.getTime()-maxDate.getTime();
		    boolean sameDay = cal.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) && cal.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR);
		    final Calendar resetCal = cal; 
		    if(!minDate.before(currentDate) ){
		        cal.setTime(minDate);
		        view.updateDate(resetCal.get(Calendar.YEAR), resetCal.get(Calendar.MONTH), resetCal.get(Calendar.DAY_OF_MONTH));

		    }else if(maxDate.before(currentDate) && !sameDay){
		        cal.setTime(maxDate);
		        view.updateDate(resetCal.get(Calendar.YEAR), resetCal.get(Calendar.MONTH), resetCal.get(Calendar.DAY_OF_MONTH));
		    }   
			}
			catch(Exception e){
				Log.d("Error onDateChanged=", ""+e.getMessage());
			}
		}

		public void setMaxDate(Date date){
		    this.maxDate = date;
		}

		public void setMinDate(Date date){
		    this.minDate = date;
		} 
}
