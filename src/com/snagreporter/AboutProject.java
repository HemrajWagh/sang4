package com.snagreporter;





import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

public class AboutProject extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		try{
		setContentView(R.layout.aboutproject);
		
		
		}
		catch(Exception e)
		{
			Log.d("Exception", ""+e.getMessage());
		}

	}
	
	
}
