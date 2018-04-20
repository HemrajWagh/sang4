package com.snagreporter;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;

public class GraphWebView extends Activity 
{
	WebView vw;
	 public void onCreate(Bundle savedInstanceState)
	 {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.graph_web_view);
	        vw=(WebView)findViewById(R.id.graph_web_webvw);
	        vw.getSettings().setJavaScriptEnabled(true);
	        String url=""+getResources().getString(R.string.WS_URL).toString()+"addgraphdata.aspx";
	        vw.loadUrl(url);
    }   
}
