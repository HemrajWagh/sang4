package com.snagreporter.chat;

import java.sql.Date;

import com.snagreporter.database.FMDBDatabaseAccess;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.UUID;

import java.util.Random;

import org.json.JSONArray;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import com.snagreporter.EditApartment;
import com.snagreporter.ListItemNames;
import com.snagreporter.R;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.snagreporter.entity.Chat;

/**
 * MessageActivity is a main Activity to show a ListView containing Message items
 * 
 * @author Adil Soomro
 *
 */
public class MessageActivity extends ListActivity {
	String chatid;
	boolean isRunning;
	Boolean flag=true;
	Chat obj,arr[];
	String msgid,mid,msgidc;
	JSONObject jObject;
	private static final String NAMESPACE = "http://tempuri.org/";
	private static final String METHOD_NAME = "SendOrUpdateChatStatus";
	private static final String SOAP_ACTION = "http://tempuri.org/SendOrUpdateChatStatus";
	private static final String URL ="http://Snag2.itakeon.com/SnagReporter_WebS.asmx?WSDL";
	
	private static final String METHOD_NAME1 = "GetMyChatMessage";
	private static final String SOAP_ACTION1 = "http://tempuri.org/GetMyChatMessage";
	
	
	
	
	Handler handler = new Handler();
	
	/** Called when the activity is first created. */
	String messagetxt="";
	ArrayList<Message> messages;//= new ArrayList<Message>();
	AwesomeAdapter adapter;
	EditText text;
	//ListView lv;
	static Random rand = new Random();	
	static String sender;
	Chat chatobj = new Chat();
	String RegUserID="";
	String val="";
	String newMessage="";
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		try{
		setContentView(R.layout.chat_chatmain);
		
		SharedPreferences SP = getSharedPreferences("AppDelegate",MODE_PRIVATE);
       RegUserID=SP.getString("RegUserID", "");
       
       SharedPreferences sharedPref = getSharedPreferences("AppDelegate",MODE_PRIVATE);
       SharedPreferences.Editor prefEditor = sharedPref.edit();
       prefEditor.putBoolean("isInChat",true);
       prefEditor.commit();
       isRunning=true;
       
		FMDBDatabaseAccess fdb=new FMDBDatabaseAccess(MessageActivity.this);
		obj = new Chat();
		msgid="done";
		msgidc="test";
		chatid="";
		
		String str=getIntent().getExtras().getString("chatID");
		
		chatobj._chatWithID=""+str;
	  	chatobj._regUserID=""+RegUserID; 
		
//		chatobj._chatWithID="isp-000002";
//	  	chatobj._regUserID="isp-000006"; 
	  	
	  	 
	  	messages = new ArrayList<Message>();
		text = (EditText) this.findViewById(R.id.text);
		//lv =(ListView)this.findViewById(R.id.list);
		adapter = new AwesomeAdapter(this, messages);
		setListAdapter(adapter);
		
		/////////////
		//////////////////
		arr=fdb.getChat(chatobj);
		
		if(arr!=null)
		{
		int i=arr.length;
		
		for(int j=0;j<i;j++)
		{
			Log.d("check",arr[j]._chatWithID.toString() );
			Log.d("check",chatobj._chatWithID.toString() );
			
			if(!arr[j]._chatWithID.equalsIgnoreCase(chatobj._chatWithID.toString()))
			{
				messagetxt=arr[j]._chatCommet;
				addNewMessage(new Message(arr[j]._chatCommet, false));
			}
			else
			{
				addNewMessage(new Message(arr[j]._chatCommet, true));
			}
		}
		}
        
		//handler.postDelayed(timedTask, 500);
		
		SendMessage task=new SendMessage();
		task.execute();
		}
		catch(Exception e)
		{
			Log.d("Exception",""+e.getMessage());
		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		try{
			isRunning=false;
		}
		catch(Exception e)
		{
			Log.d("Exception",""+e.getMessage());
		}
		super.onDestroy();
	}
	private Runnable timedTask = new Runnable(){

		  
		  public void run() {
		   
			  try{
			synchronized(this){
			}
			
			
			SendMessage task=new SendMessage();
			task.execute();
			  }
				catch(Exception e)
				{
					Log.d("Exception",""+e.getMessage());
				}
			}
	};
	public void sendMessage(View v)
	{
		try{
				updateChat upchat=new updateChat();
				upchat.execute();
		}
		catch(Exception e)
		{
			Log.d("Exception",""+e.getMessage());
		}
		     
	}
	private class updateChat extends AsyncTask<Void, String, String>
     {
		
		 Chat object = new Chat();
		 FMDBDatabaseAccess fdb;
		 
		 @Override
		 protected void onPreExecute() { 
			 
			 try{
			 	newMessage = text.getText().toString().trim();
				fdb=new FMDBDatabaseAccess(MessageActivity.this);
				if(newMessage.length() > 0)
				{
				Calendar c = Calendar.getInstance();
				SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		        String formattedDate = df.format(c.getTime());
				UUID uid=UUID.randomUUID();
				obj._chatWithName="appDel.strStats_FriendName";
		        obj._chatWithID=chatobj._chatWithID;
		        obj._chatSentBy="You";
		        obj._chatCommet=text.getText().toString();
		        obj._regUserID=chatobj._regUserID;
		        obj._chatSyncDateTime=formattedDate;
		        obj._chatCreatedDateTime=formattedDate;
		        obj._isChatSync=false;
		        obj._chatID=uid.toString();
		        obj._unread=false;
		        obj._isChatDeliverd=false;
		        obj._isChatRead=false;
		        chatid=uid.toString();
		        msgid=obj._chatID;
		        val=obj._chatID+"~"+chatobj._chatWithID+"~"+chatobj._regUserID+"~"+obj._chatCommet+"~"+obj._chatCreatedDateTime;
				}
			 }
				catch(Exception e)
				{
					Log.d("Exception",""+e.getMessage());
				}
	        }
		 protected String doInBackground(Void... params) {
			 
			 try{
			 fdb.insertChat(obj);
		 String ColName="Message_ID~ToUser_ID~FromUser_ID~Message~CreatedDateTime";
		 SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);       
         
         
         request.addProperty("_strCoumnNamesInsert",ColName);
         request.addProperty("_strValuesInsert",val);
         request.addProperty("strMessageIdToUpdate",msgid);
        
        
         
         SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        
         envelope.setOutputSoapObject(request);
         envelope.dotNet = true;
        
         try {
               HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
              
               androidHttpTransport.call(SOAP_ACTION, envelope);

               //SoapObject result = (SoapObject)envelope.bodyIn;
               Object response=envelope.getResponse();
               String resultdata=response.toString();
               jObject = new JSONObject(resultdata);
               if(jObject!=null){
 		    		JSONArray arr = jObject.getJSONArray("data");
 					if(arr!=null){
 						for(int i=0;i<arr.length();i++){
 							JSONObject obj = arr.getJSONObject(i);
 							
 							
 							object._chatID=obj.getString("Message_ID");
 							 
 							 
 							String isread=obj.getString("status");
 							if(isread.equalsIgnoreCase("new"))
 							{
 								fdb.updateChat(object);
 							}
 							else if(isread.equalsIgnoreCase("updated"))
 							{
 								fdb.updateRead(object);
 								
 							}
 							
 						}
 					}
 		    		 }
              // text.setText("");
         } catch (Exception e) {
               Log.d("Exception : ",e.getMessage());
         }
			 }
				catch(Exception e)
				{
					Log.d("Exception",""+e.getMessage());
				}
		return null;
		 
     }
		 
		 protected void onPostExecute(String txt) {
			 
			 try{
			 runOnUiThread(new Runnable() {
				
				public void run() {
					// TODO Auto-generated method stub
					try{
					 if(newMessage.length() > 0)
						{
						 addNewMessage(new Message(newMessage, true));
			   				newMessage="";
			   				text.setText(""); 
						}
					}
					catch(Exception e)
					{
						Log.d("Exception",""+e.getMessage());
					}
				}
			});
			 }
				catch(Exception e)
				{
					Log.d("Exception",""+e.getMessage());
				}
			}
     }
	private class SendMessage extends AsyncTask<Void, String, String>
	{
		
		FMDBDatabaseAccess fdb;
		@Override
		protected void onPreExecute() { 
			fdb=new FMDBDatabaseAccess(MessageActivity.this);
        }
		@Override
		protected String doInBackground(Void... params) {
		
			try{
		
				if(isRunning)
				{
					SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME1);       
					Chat object = new Chat();
					 
					 
			        
			        request.addProperty("strFromUserID",chatobj._chatWithID);
			        request.addProperty("strToUserId",chatobj._regUserID);
			      // Log.d("strFromUserID ", chatobj._chatWithID);
			      // Log.d("strToUserId ", chatobj._regUserID);
			        
			       
			        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
			       
			        envelope.setOutputSoapObject(request);
			        envelope.dotNet = true;
			       
			        try {
			              HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
			             
			              androidHttpTransport.call(SOAP_ACTION1, envelope);

			              
			              Object response=envelope.getResponse();
			              String resultdata=response.toString();
			              
			              if(!resultdata.equalsIgnoreCase("anyType{}")){
			            	  jObject = new JSONObject(resultdata.toLowerCase());
			            	  if(jObject!=null){
					    		JSONArray arr = jObject.getJSONArray("data");
								if(arr!=null){
									for(int i=0;i<arr.length();i++){
										JSONObject obj = arr.getJSONObject(i);
										
							              object._chatWithID=obj.getString("fromuser_id");
							              
							              object._chatCommet=obj.getString("message");
							              object._regUserID=obj.getString("touser_id");
							              object._chatSyncDateTime=obj.getString("messagereaddatetime");
									       object._chatCreatedDateTime=obj.getString("createddatetime");
									      
									       object._chatID=obj.getString("message_id");
									       
									        
									    object._chatSentBy="frnd";
									    if(!chatobj._regUserID.equalsIgnoreCase(object._chatWithID.toString()))
									    {
									    	fdb.insertChat(object);
										
										
										
										String isread=obj.getString("status");
										if(isread.equalsIgnoreCase("read"))
										{
											fdb.updateIsRead(object);
										}
										else if(isread.equalsIgnoreCase("delv") ||isread.equalsIgnoreCase("new"))
										{
											
											fdb.updateDelivered(object);
											messagetxt=object._chatCommet;
											
										
										}
									/*	if(messagetxt.length() > 0 && !msgidc.equalsIgnoreCase(mid)){
											
											Log.d("Message    " , messagetxt);
										addNewMessage(new Message(messagetxt, false));
										messagetxt="";
										
									}*/
									}
								}
					    		 }
			              }}
			        } catch (Exception e) {
			              e.printStackTrace();
			        }
				}
		
			}
			catch(Exception e)
			{
				Log.d("Exception",""+e.getMessage());
			}
			
			return null;
			
			
		}
		
		      
		
		@Override
		protected void onPostExecute(String text) {
			
			//handler.postDelayed(timedTask, 5000);
			try{
				if(isRunning)
				{
					runOnUiThread(new Runnable() {
						
						public void run() {
							// TODO Auto-generated method stub
							try{
							if(messagetxt.length() > 0 && !msgidc.equalsIgnoreCase(mid)){
								
								Log.d("Message    " , messagetxt);
							addNewMessage(new Message(messagetxt, false));
							messagetxt="";
							
						}
							}
							catch(Exception e)
							{
								Log.d("Exception",""+e.getMessage());
							}
						}
					});
					
					SendMessage task=new SendMessage();
					task.execute();
				}
			
			}
			catch(Exception e)
			{
				Log.d("Exception",""+e.getMessage());
			}
		}
		
		}
	
	
	void addNewMessage(Message m)
	{
		try{
		messages.add(m);
		adapter.notifyDataSetChanged();
		setSelection(messages.size()-1);
		//lv.setSelection(lv.getChildCount()-1);
		}
		catch(Exception e)
		{
			Log.d("Exception",""+e.getMessage());
		}
	}
	
	
	///@@optionsMenu
	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
		MenuInflater menuInflater = getMenuInflater();
        //if(isOnline)
        	menuInflater.inflate(R.layout.menu, menu);
        //else
        //	menuInflater.inflate(R.layout.menuoff, menu);
        return true;
		//return super.onCreateOptionsMenu(menu);
	}
	 @Override
	    public boolean onOptionsItemSelected(MenuItem item)
	    {
	 
		 switch (item.getItemId())
	        {
	        
	        case R.id.menuBtn_exit:
	        	new AlertDialog.Builder(MessageActivity.this)
	    	    
	    	    .setMessage("Are you sure you want to Exit?")
	    	    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
	    	        public void onClick(DialogInterface dialog, int which) { 
	    	        	
	    	        	setResult(10001);
	    	    		finish();
	    	        	//android.os.Process.killProcess(android.os.Process.myPid());
	    	        }
	    	     })
	    	     .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
	    	        public void onClick(DialogInterface dialog, int which) { 
	    	            
	    	        }
	    	     })
	    	    .show();
	        	return true;
	        case R.id.menuBtn_logout:
	        	new AlertDialog.Builder(MessageActivity.this)
	    	    
	    	    .setMessage("Are you sure you want to Logout?")
	    	    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
	    	        public void onClick(DialogInterface dialog, int which) { 
	    	           /* FMDBDatabaseAccess db=new FMDBDatabaseAccess(ReportTable.this);
	    	            SharedPreferences SP = getSharedPreferences("AppDelegate",MODE_PRIVATE);
	    				db.performLogout(SP.getString("RegUserID", ""));
	    				Intent i=new Intent(ReportTable.this,com.snagreporter.Login_page.class);
	    				i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	    				startActivity(i);*/
	    	        	setResult(10002);
	        			finish();
	    				
	    	        	
	    	        }
	    	     })
	    	     .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
	    	        public void onClick(DialogInterface dialog, int which) { 
	    	            
	    	        	
	    	        }
	    	     })
	    	    .show();
	        	return true;
	        
	 
	        default:
	            return super.onOptionsItemSelected(item);
	        }
	    }
}