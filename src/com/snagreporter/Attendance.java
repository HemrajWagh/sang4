package com.snagreporter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.UUID;

import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import com.snagreporter.database.FMDBDatabaseAccess;
import com.snagreporter.database.ParseSyncData;
import com.snagreporter.entity.ContractorMaster;
import com.snagreporter.entity.DailyAttendance;
import com.snagreporter.entity.LoginData;
import com.snagreporter.entity.TradeAptAreaDetail;
import com.snagreporter.listitems.Header;
import com.snagreporter.listitems.Item;
import com.snagreporter.listitems.ListItemAddDefect;
import com.snagreporter.menuhandler.MenuHandler;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.ViewPager.LayoutParams;
import android.text.InputType;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class Attendance extends Activity 
{
	private final int DATE_DIALOG_ID=1008; 
	ListView list;
	String URL="";// =""+getResources().getString(R.string.WS_URL).toString()+"SnagReporter_WebS.asmx";
	String NAMESPACE = "http://tempuri.org/";
	String METHOD_NAME_SaveNewDataToTheDataBase = "SaveNewDataToTheDataBase";
	String SOAP_ACTION_SaveNewDataToTheDataBase = "http://tempuri.org/SaveNewDataToTheDataBase";
	String METHOD_NAME_UpdateDataToTheDataBase = "UpdateDataToTheDataBase";
	String SOAP_ACTION_UpdateDataToTheDataBase = "http://tempuri.org/UpdateDataToTheDataBase";
	List<Item> items;
	private int year, month, day,year2, month2, day2;
	TwoTextArrayAdapter adapter;
	boolean isOnline=false;
	String selectedDate,skilledWork,unskilledWork,ContractorType,AttnDate;
	boolean isMenuVisible=false;
	MenuHandler menuhandler;
	String RegUserID="";
	String Fname,LName;

	String SelectedStatus;
	int currentdate;
	LoginData[] arrLoginData;

	String[] strSetValues;
	String[] strGetValues;
	
	View TopMenu;
	String SelectedContractorName="";
	String SelectedContractorID="";
	
	ContractorMaster arrContractor[];
	
	  @Override
	    public void onCreate(Bundle savedInstanceState)
	    {
		  super.onCreate(savedInstanceState);
		  
		  try{
		  setContentView(R.layout.attendance);
		  SharedPreferences SP = getSharedPreferences("AppDelegate",MODE_PRIVATE);
	      RegUserID=SP.getString("RegUserID", "");
	      Boolean id=SP.getBoolean("isOnline", false);
	      isOnline=id;
	      list=(ListView)findViewById(R.id.android_attndnce_items);
	      
	      	menuhandler=new MenuHandler(Attendance.this);
	        TopMenu=new View(this);
	        RelativeLayout.LayoutParams rlp=new RelativeLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
	        TopMenu.setLayoutParams(rlp);
	        LayoutInflater inflater= LayoutInflater.from(this);
	        TopMenu=(View) inflater.inflate(R.layout.popup_menu, null);
	        this.addContentView(TopMenu, rlp);
	        TopMenu.requestLayout();
	        TopMenu.setVisibility(View.INVISIBLE);
	        
	       FMDBDatabaseAccess fdb = new FMDBDatabaseAccess(this);
	       arrContractor = fdb.getAllWorkingContractor();
	       Log.d("",""+arrContractor.length);
	      SelectedStatus="";
	      
	      Object objNot = getLastNonConfigurationInstance();
	        if(objNot!=null)
	        {
	        	strGetValues=(String[])objNot;
	        }
	        if(strGetValues!=null)
 	       {
	        	SelectedContractorName=strGetValues[0];
 	            SelectedStatus=strGetValues[1];
 	            selectedDate=strGetValues[2];
 	            skilledWork=strGetValues[3];
 	            unskilledWork=strGetValues[4];
 	            ContractorType=strGetValues[5];
 	            AttnDate=strGetValues[6];
 	       }
	        	continueprocess();
	        
		  }
			catch(Exception e)
			{
				Log.d("Exception", ""+e.getMessage());
			}
	    }
	  @Override
	    public void onResume()
	  {
	    	super.onResume();
	    	//Toast.makeText(getApplicationContext(), "Came in Resume "+cnt++, Toast.LENGTH_LONG).show();
	        //else{
	        	
	        	
	       // }
	    	
	    }
	  public void ShowTopMenu(){
			TopMenu.setVisibility(View.VISIBLE);
			isMenuVisible=true;
		}
		public void HideTopMenu(){
			TopMenu.setVisibility(View.INVISIBLE);
			isMenuVisible=false;
		}
		public void TopMenuClick(View v){
			if(isMenuVisible){
				HideTopMenu();
			}
			else{
				ShowTopMenu();
			}
		}
		public void TopMenuBGClick(View v){
			HideTopMenu();
			
		}
		public void MenuRoomsheetClick(View v){
			HideTopMenu();
			//menuhandler.MenuRoomsheetClick(CurrentFloor);
		}
		public void MenuReportsClick(View v){
			HideTopMenu();
			menuhandler.MenuReportsClick();
		}
		public void MenuSortFilterClick(View v){
			HideTopMenu();
			menuhandler.MenuSortFilterClick();
		}
		public void MenuChatClick(View v){
			HideTopMenu();
			menuhandler.MenuChatClick();
		}
		public void MenuGraphClick(View v){
			HideTopMenu();
			//menuhandler.MenuGraphClick("project");
		}
		public void MenuChartClick(View v){
			HideTopMenu();
			menuhandler.MenuChartClick();
		}
		public void MenuAttachClick(View v){
			HideTopMenu();
			//menuhandler.MenuAttachClick();
		}
		
	  public void continueprocess()
	  {
		  try{
		  FMDBDatabaseAccess fmdb=new FMDBDatabaseAccess(this);
		  items = new ArrayList<Item>();
		  items.add(new Header(null, "Attendance",false,false,true,false));
		  
		  if(selectedDate==null || selectedDate.length()==0)
		  {
			// selectedDate=getCurrentDate();
			  selectedDate=GetDate();
			  AttnDate=GetUTCdateTimeAsString();
			 // selectedDate=GetUTCdateTimeAsString();
		  }
		  if(skilledWork==null || skilledWork.length()==0)
		  {
			  skilledWork="0";
		  }
		  if(unskilledWork==null || unskilledWork.length()==0)
		  {
			  unskilledWork="0";
		  }
		  
		  final Calendar cal = Calendar.getInstance();
	       if(year==0)
	    	   year = cal.get(Calendar.YEAR);
	       if(month==0)
	    	   month = cal.get(Calendar.MONTH);
	       if(day==0)
	    	   day = cal.get(Calendar.DAY_OF_MONTH);
	   
		  populatedata();
	      
		  list.setDivider(getResources().getDrawable(R.color.transparent));
	       list.setDividerHeight(1);
	       
	        adapter = new TwoTextArrayAdapter(this, items);
	        list.setAdapter(adapter);
	        //list.setOnScrollListener(this);
	        
	        list.setOnItemClickListener(new OnItemClickListener() 
	        {

				public void onItemClick(AdapterView<?> arg0, View arg1,
						int position, long arg3) 
				{
					//Toast.makeText(Attendance.this,""+position,Toast.LENGTH_SHORT).show();
					try{
					AlertDialog.Builder alert=null;
					final EditText input=new EditText(Attendance.this);
					int wantedPosition = 0; // Whatever position you're looking for    
					int firstPosition = 0; // This is the same as child #0
					int wantedChild =0;
					switch (position)
					{
						case 1:
							registerForContextMenu(arg0);
	  			    		
	  			    		openContextMenu(arg0);
	  			    		unregisterForContextMenu(arg0);
						break;
						case 2:
							break;
					    case 3:
					    	final View v2=arg1;
	  						TextView t2=(TextView)v2.findViewById(R.id.row_cell_addDefect_text);
	  						String date=t2.getText().toString();
	  						String arr[]=date.split("-");
	  						currentdate=1;
					    	showDialog(DATE_DIALOG_ID);
					    	break;
					    	
					    case 4:
					    	 alert = new AlertDialog.Builder(Attendance.this);
								alert.setTitle("Skilled Workers");
								 //input = new EditText(EditDefectChageContractorStatus.this);
								input.setInputType(InputType.TYPE_CLASS_NUMBER);
								wantedPosition = 4; // Whatever position you're looking for    
								firstPosition = list.getFirstVisiblePosition() - list.getHeaderViewsCount(); // This is the same as child #0
								wantedChild = wantedPosition - firstPosition;
								if (wantedChild < 0 || wantedChild >= list.getChildCount())
								{
								  Log.d("Child Not Available", "");
								}
								else
								{
									final View v=list.getChildAt(wantedChild);
									TextView t=(TextView)v.findViewById(R.id.row_cell_addDefect_text);
									input.setText(t.getText().toString());
									
									alert.setView(input);
									alert.setPositiveButton("Done", new DialogInterface.OnClickListener() {
										public void onClick(DialogInterface dialog, int whichButton) {
											try{
											int wantedPosition = 4; // Whatever position you're looking for
											int firstPosition = list.getFirstVisiblePosition() - list.getHeaderViewsCount(); // This is the same as child #0
											int wantedChild = wantedPosition - firstPosition;
											// Say, first visible position is 8, you want position 10, wantedChild will now be 2
											// So that means your view is child #2 in the ViewGroup:
											if (wantedChild < 0 || wantedChild >= list.getChildCount()) {
											  Log.d("Child Not Available", "");
											}
											else{
												String value = input.getText().toString();
												  View v2=list.getChildAt(wantedChild);
													TextView t2=(TextView)v2.findViewById(R.id.row_cell_addDefect_text);
													
													
													t2.setText(""+value);
													skilledWork=value;
											}
											}
											catch(Exception e)
											{
												Log.d("Exception", ""+e.getMessage());
											}
										  // Do something with value!
										  }
										});
									     alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
										  public void onClick(DialogInterface dialog, int whichButton) {
										    // Canceled.
										  }
										});

										alert.show();
								}
					    	break;
					    	
					    case 5:
					    	 alert = new AlertDialog.Builder(Attendance.this);
								alert.setTitle("Unskilled Workers");
								//input = new EditText(EditDefectChageContractorStatus.this);
								input.setInputType(InputType.TYPE_CLASS_NUMBER);
								wantedPosition =5; // Whatever position you're looking for    
								firstPosition = list.getFirstVisiblePosition() - list.getHeaderViewsCount(); // This is the same as child #0
								wantedChild = wantedPosition - firstPosition;
								if (wantedChild < 0 || wantedChild >= list.getChildCount())
								{
								  Log.d("Child Not Available", "");
								}
								else
								{
									final View v=list.getChildAt(wantedChild);
									TextView t=(TextView)v.findViewById(R.id.row_cell_addDefect_text);
									input.setText(t.getText().toString());
									
									alert.setView(input);
									alert.setPositiveButton("Done", new DialogInterface.OnClickListener() {
										public void onClick(DialogInterface dialog, int whichButton) {
											try{
											int wantedPosition = 5; // Whatever position you're looking for
											int firstPosition = list.getFirstVisiblePosition() - list.getHeaderViewsCount(); // This is the same as child #0
											int wantedChild = wantedPosition - firstPosition;
											// Say, first visible position is 8, you want position 10, wantedChild will now be 2
											// So that means your view is child #2 in the ViewGroup:
											if (wantedChild < 0 || wantedChild >= list.getChildCount()) {
											  Log.d("Child Not Available", "");
											}
											else{
												String value = input.getText().toString();
												  View v2=list.getChildAt(wantedChild);
													TextView t2=(TextView)v2.findViewById(R.id.row_cell_addDefect_text);
													
													
													t2.setText(""+value);
													unskilledWork=value;
											}
										  
										  // Do something with value!
											}
											catch(Exception e)
											{
												Log.d("Exception", ""+e.getMessage());
											}
										  }
										});
									     alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
										  public void onClick(DialogInterface dialog, int whichButton) {
										    // Canceled.
										  }
										});

										alert.show();
								}
					    	break;
					}
				}
				catch(Exception e)
				{
					Log.d("Exception", ""+e.getMessage());
				}
				}
			});
		  }
			catch(Exception e)
			{
				Log.d("Exception", ""+e.getMessage());
			}
	  }
	  
	  
	  @Override  
	  public void onCreateContextMenu(ContextMenu menu, View v,ContextMenuInfo menuInfo)
	  {  
		  super.onCreateContextMenu(menu, v, menuInfo);  
		 
		  try{
		  menu.setHeaderTitle("Select Contractor");
		  if(arrContractor.length == 0) {
			  
		  }
	  
		  for(int i=0;i<arrContractor.length;i++)
		  {
			  menu.add(i, v.getId(), 0, ""+arrContractor[i].ContractorName);
		  }
		  }
			catch(Exception e)
			{
				Log.d("Exception", ""+e.getMessage());
			}
	  }  

	  @Override
	  public boolean onContextItemSelected(MenuItem item)
	  {
		  int wantedPosition = 1; // Whatever position you're looking for
		  int firstPosition = list.getFirstVisiblePosition() - list.getHeaderViewsCount(); // This is the same as child #0
		  int wantedChild = wantedPosition - firstPosition;
		  // Say, first visible position is 8, you want position 10, wantedChild will now be 2
		  // So that means your view is child #2 in the ViewGroup:
		  if (wantedChild < 0 || wantedChild >= list.getChildCount()) {
			  Log.d("Child Not Available", "");
		  }
		  else{
			  final View v=list.getChildAt(wantedChild);
			  TextView t = (TextView)v.findViewById(R.id.row_cell_addDefect_text);
			  t.setText("" + item.getTitle().toString());
			  //SelectedArea=item.getTitle().toString();
			 
			  SelectedContractorID = arrContractor[item.getGroupId()].ID;
			  ContractorType=arrContractor[item.getGroupId()].TypeOfJob1;
			  SelectedStatus=SelectedContractorName = item.getTitle().toString();
			  items.clear();
			  items = new ArrayList<Item>();
			  items.add(new Header(null, "Attendance",false,false,true,false));
			 
			  selectedDate=GetDate();
			  AttnDate=GetUTCdateTimeAsString();
			  //selectedDate=GetUTCdateTimeAsString();
			  skilledWork="0";
			  unskilledWork="0";
			  
			  populatedata();
			    adapter = new TwoTextArrayAdapter(this, items);
			    list.setAdapter(adapter);
		  }
		  
		  return true;
	}
	  
	  public String getCurrentDate()
	  {
		  String strdate;
		  Date date = new Date();
		  strdate=date.toString();
		  return strdate;
	  }
	  public void populatedata()
	  {
		  /*if(items!=null)
			{
				items.clear	();
			}*/
		 
		  try{
		 if(SelectedStatus.equalsIgnoreCase(""))
		 {
		  if(arrContractor.length == 0)
		  {
			  items.add(new ListItemAddDefect(null, "Select Contractor","No Contractor To Select",false));
		  }
		  else if(arrContractor.length == 1)
		  {
		  	items.add(new ListItemAddDefect(null, "Select Contractor",""+arrContractor[0].ContractorName,false));
		  	ContractorType=arrContractor[0].TypeOfJob1;
		  	SelectedContractorID=arrContractor[0].ID;
		  	SelectedContractorName=arrContractor[0].ContractorName;
		  	  items.add(new ListItemAddDefect(null,"Trade",ContractorType, false));	
		  	  items.add(new ListItemAddDefect(null, "Select Date",""+selectedDate,false));
			  items.add(new ListItemAddDefect(null, "Skilled Workers",""+skilledWork,false));
			  items.add(new ListItemAddDefect(null, "Unskilled Workers",""+unskilledWork,false));
		  }
		  else
		  {
			  items.add(new ListItemAddDefect(null, "Select Contractor","Select Contractor Name From List",false));
		  }
		 }
		 else
		 {
			  items.add(new ListItemAddDefect(null, "Select Contractor",SelectedStatus,false));
			  items.add(new ListItemAddDefect(null,"Trade",ContractorType, false));	
			  items.add(new ListItemAddDefect(null, "Select Date",""+selectedDate,false));
			  items.add(new ListItemAddDefect(null, "Skilled Workers",""+skilledWork,false));
			  items.add(new ListItemAddDefect(null, "Unskilled Workers",""+unskilledWork,false));
		 }
		  }
			catch(Exception e)
			{
				Log.d("Exception", ""+e.getMessage());
			}
	  }
	  
	  @Override
	    protected Dialog onCreateDialog(int id) {
	        switch (id) {
	        case DATE_DIALOG_ID:
	        	DatePickerDialog dd;
	        	if(currentdate==1)
	        	{
	        		dd=new DatePickerDialog(Attendance.this, mDateSetListener, year,month,day);
	        	}
	        	else
	        	{
	        		dd=new DatePickerDialog(Attendance.this, mDateSetListener, year2, month2, day2);
	        	}
	        	return dd;
	        }
	        return null;
	    }
	     
	  private DatePickerDialog.OnDateSetListener mDateSetListener =
	        new DatePickerDialog.OnDateSetListener() {
	            public void onDateSet(DatePicker view, int myear, int monthOfYear, int dayOfMonth) {
	            	if(currentdate==1){
	            		year = myear;
	                    month = monthOfYear;
	                    day = dayOfMonth;
	            	}
	            	else{
	            		year2 = myear;
	                    month2 = monthOfYear;
	                    day2 = dayOfMonth;
	            	}
	                
	                updateDate();
	            }
	    };
	    private DatePickerDialog.OnDateSetListener mDateSetListener2 =
	        new DatePickerDialog.OnDateSetListener() {
	            public void onDateSet(DatePicker view, int myear, int monthOfYear, int dayOfMonth) {
//	            	if(CurrentDate==6){
//	            		year = myear;
//	                    month = monthOfYear;
//	                    day = dayOfMonth;
//	            	}
//	            	else{
	            		year2 = myear;
	                    month2 = monthOfYear;
	                    day2 = dayOfMonth;
	            	//}
	              
	                updateDate();
	            }
	    };
	    
	    private String GetDate(){
//	 	   String[] months={"01","02","03","04","05","06","07","08","09","10","11","12"};
//	 	   StringBuilder date=new StringBuilder().append(day).append('-')
//	 				.append(months[month]).append('-').append(year);
//	 		String dt=date.toString();
	 	   String dt = "";  // Start date
	 	   SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
	 	   Calendar c = Calendar.getInstance();
	 	  
	 	   c.setTime(new Date());
	 	  // c.add(Calendar.DATE, 2);
	 	   
	 	   dt = sdf.format(c.getTime());
	 		return dt;
	    }
	    private String GetPlusDate(){
//	 	   Calendar calendar = Calendar.getInstance();
//	 	   Date now = new Date();
//	 	   calendar.setTime(now);
//	 	   calendar.add(Calendar.DATE,2);
//	 	   Date date=calendar.getTime();
//	 	   

	 	   
	 	   String dt = "";  // Start date
	 	   SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
	 	   Calendar c = Calendar.getInstance();
	 	  
	 	   c.setTime(new Date());
	 	   c.add(Calendar.DATE, 2);
	 	   int dtt=c.get(Calendar.DATE);
	 	   day2=dtt;
	 	   int mon=c.get(Calendar.MONTH);
	 	   month2=mon;
	 	   int yr=c.get(Calendar.YEAR);
	 	   year2=yr;
	 	   dt = sdf.format(c.getTime());
	 	   
	 	   
//	 	   String[] months={"01","02","03","04","05","06","07","08","09","10","11","12"};
//	 	   StringBuilder datestr=new StringBuilder().append(date.getDate()).append('-')
//	 				.append(date.getMonth()).append('-').append(date.getYear());
//	 		 dt=datestr.toString();
	 		return dt;
	    }
	    
	    public void SyncTradeAptAreaDetail(TradeAptAreaDetail obj){
			boolean result=false;
			try
			{
				if(isOnline){
				SoapObject request=new SoapObject(NAMESPACE, METHOD_NAME_SaveNewDataToTheDataBase);
				SoapSerializationEnvelope envelope=new SoapSerializationEnvelope(SoapEnvelope.VER11);
				envelope.dotNet=true;
				envelope.setOutputSoapObject(request);
				
				  String CoumnNames="ID~TradeAptAreaMasterID~TradeDetailID~InspectionGroup~InspectionDescription~InspectionEntry~CreatedBy~CreatedDate~ModifiedBy~ModifiedDate~EnteredOnMachineID";
				  String Values=""+obj.ID+"~"+obj.TradeAptAreaMasterID+"~"+obj.TradeDetailID+"~"+obj.InspectionGroup+"~"+obj.InspectionDescription+"~"+obj.InspectionEntry+"~"+obj.CreatedBy+"~"+obj.CreatedDate+"~"+obj.ModifiedBy+"~"+obj.ModifiedDate+"~"+obj.EnteredOnMachineID;
				  String TableName="TradeAptAreaDetail";
				request.addProperty("_strCoumnNames", CoumnNames);
				request.addProperty("_strValues", Values);
				request.addProperty("_strTableName", TableName);
				
				HttpTransportSE httptransport=new HttpTransportSE(URL);
				httptransport.call(SOAP_ACTION_SaveNewDataToTheDataBase, envelope);
				Object response=envelope.getResponse();
				String output=response.toString();
				if(output!=null)
				{
					
					try{
	    		    	JSONObject jObject = new JSONObject(output);
				        jObject=jObject.getJSONObject("Data");
				        String Value=jObject.getString("Value");
				        if(Value.equalsIgnoreCase("true")){
				        	result=true;
				        }
				        else{
				        	result=false;
				        }
	    		    	}
	    		    	catch(Exception e){
	    		    		Log.d("Error parsing result", "");
	    		    	}
				}
				}
				FMDBDatabaseAccess db=new FMDBDatabaseAccess(Attendance.this);
				if(result)
					obj.IsSyncedToWeb=true;
				else
					obj.IsSyncedToWeb=false;
				//obj.StatusForUpload="Inserted";
				db.insertORupdateTradeAptAreaDetail(obj);
			}
			catch (Exception e)
			{
				Log.d("SyncToWeb", ""+e.getMessage());
			}
		}
	    
	    
	    private void updateDate() 
	    {
	    	try{
	    	String[] months={"01","02","03","04","05","06","07","08","09","10","11","12"};
	    	int wantedPosition=0;
	    	if(currentdate==1)
	    	{
				wantedPosition = 3;
				
			}
	    	int firstPosition = list.getFirstVisiblePosition() - list.getHeaderViewsCount(); // This is the same as child #0
			int wantedChild = wantedPosition - firstPosition;
			// Say, first visible position is 8, you want position 10, wantedChild will now be 2
			// So that means your view is child #2 in the ViewGroup:
			if (wantedChild < 0 || wantedChild >= list.getChildCount())
			{
				  Log.d("Child Not Available", "");
			}
			else
			{
				final View v=list.getChildAt(wantedChild);
				TextView t=(TextView)v.findViewById(R.id.row_cell_addDefect_text);
				if(currentdate==1)
				{
					String strDT=""+day+"-"+(month+1)+"-"+year;
					SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
					Calendar c = Calendar.getInstance();
					try {
						c.setTime(sdf.parse(strDT));
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					strDT = sdf.format(c.getTime());
					AttnDate=GetUTCdateTimeAsString2(c.getTime());
				t.setText(strDT);
				selectedDate=t.getText().toString();
				}
			}
	    	}
			catch(Exception e)
			{
				Log.d("Exception", ""+e.getMessage());
			}
	    }
	  public static class TwoTextArrayAdapter extends ArrayAdapter<Item> 
		 {
		    	private LayoutInflater mInflater;

		        public enum RowType {
		            LIST_ITEM, HEADER_ITEM
		        }

		        private List<Item> items;

		        public TwoTextArrayAdapter(Context context, List<Item> items) {
		            super(context, 0, items);
		            this.items = items;
		            mInflater = LayoutInflater.from(context);
		        }

		        @Override
		        public int getViewTypeCount() {
		            return RowType.values().length;

		        }

		        @Override
		        public int getItemViewType(int position) {
		            return items.get(position).getViewType();
		        }

		        @Override
		        public View getView(int position, View convertView, ViewGroup parent) {
		        	
		        		//View v=items.get(position).getView(mInflater, convertView);
		        		
		        		return items.get(position).getView(mInflater, null);
		        	
		        }
		    }
	  
	  public void SaveClick(View v)
	  {
		  try{
			  if(arrContractor.length!=0)
			  {
			  UploadData task=new UploadData();
			  task.execute(10);
			  finish();
			  }
			 
		  }
		  catch(Exception e){
			  Log.d("Error SaveClick=", ""+e.getMessage());
		  }
	  }
	  
	  protected class UploadData extends AsyncTask<Integer , Integer, Void>
	  {
		  ProgressDialog mProgressdialko=new ProgressDialog(Attendance.this);
		  String Result="";
		  DailyAttendance obj=null;
		  @Override
		protected void onPreExecute() 
		 {
			
			super.onPreExecute();
			if(isOnline){
			/*mProgressdialko.setMessage("Syncronizing...");
			mProgressdialko.setCancelable(false);
			mProgressdialko.show();*/
			}
		}
		  
		  
		@Override
		protected Void doInBackground(Integer... params) 
		{
			
			try{
			String METHOD_NAME = "SaveNewDataToTheDataBase";
    		String NAMESPACE = "http://tempuri.org/";
    		String URL = ""+getResources().getString(R.string.WS_URL).toString()+"SnagReporter_WebS.asmx";//SnagReporter_WCF.svc
    		String SOAP_ACTION = "http://tempuri.org/SaveNewDataToTheDataBase";//
    		String res = "";
    		UUID id=UUID.randomUUID();
    		obj=new DailyAttendance();
    		obj.ID=id.toString();
    		obj.InspectorID=RegUserID;
    		obj.InspectorName="";
    		//Log.d("selecteddate  AttDate",""+selectedDate+" "+AttnDate);
    		//obj.AttendanceDate=selectedDate;
    		obj.AttendanceDate=AttnDate;
    		obj.NoOfSkilledEmp=Integer.parseInt(skilledWork);
    		obj.NoOfUnskilledEmp=Integer.parseInt(unskilledWork);
    		obj.CreatedBy=RegUserID;
    		obj.CreatedDate="";
    		obj.ModifiedBy=RegUserID;
    		obj.ModifiedDate="";
    		obj.EnteredOnMachineID="";
    		obj.ContractorID = SelectedContractorID;
    		obj.ContractorName= SelectedContractorName;
    		
    		try {
    			if(isOnline){
    		    SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
    		    
    		    SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
    		    envelope.dotNet = true;
    		    envelope.setOutputSoapObject(request);
    		    
    		    
		    String CoumnNames="ID~InspectorID~InspectorName~AttendanceDate~NoOfSkilledEmp~NoOfUnskilledEmp~CreatedBy~CreatedDate~ModifiedBy~ModifiedDate~EnteredOnMachineID~ContractorName~ContractorID";
    		     String Values=""+obj.ID+"~"+obj.InspectorID+"~"+obj.InspectorName+"~"+obj.AttendanceDate+"~"+obj.NoOfSkilledEmp+"~"+obj.NoOfUnskilledEmp+"~"+obj.CreatedBy+"~"+obj.CreatedDate+"~"+obj.ModifiedBy+"~"+obj.ModifiedDate+"~"+obj.EnteredOnMachineID+"~"+obj.ContractorName+"~"+obj.ContractorID;
    		    String TableName="DailyAttendance";
    		    
    		    request.addProperty("_strCoumnNames",CoumnNames);
    		    request.addProperty("_strValues",Values);
    		    request.addProperty("_strTableName",TableName);
    		    
    		    
    		    HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
    		    try
    		    {
    		    	androidHttpTransport.call(SOAP_ACTION, envelope);
    		    	Object resonse=envelope.getResponse();
    		    	Result = resonse.toString();
    		    }
    		    catch (Exception e)
    		    {
					Log.d("Exception savedata",""+e.getMessage());
				}
    		    }
    		}
    		 catch(Exception e){
    			 Log.d("Error=", ""+e.getMessage()); 
    		 }
			}
			catch(Exception e)
			{
				Log.d("Exception", ""+e.getMessage());
			}
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result)
		{
			//mProgressdialko.dismiss();
			super.onPostExecute(result);
			try{
			if(isOnline)
				obj.IsSyncedToWeb=true;
			else
				obj.IsSyncedToWeb=false;
			
			FMDBDatabaseAccess db=new FMDBDatabaseAccess(Attendance.this);
			db .InsertOrUpdateIntoDailyAttendance(obj);
			}
			catch(Exception e)
			{
				Log.d("Exception", ""+e.getMessage());
			}
			 //finish();
		}
		  
	  }
	
		 public void BackClick(View v)
		 {
		    	finish();
		 }
		 public void SyncClick(View v)
		 {
			 try{
			 if(isOnline)
			 {
				 ParseSyncData par=new ParseSyncData(Attendance.this);
				 par.start();
			 }
			 else
			 {
				 new AlertDialog.Builder(Attendance.this)
 	    	    
 	    	    .setMessage("Please go online to Sync.")
 	    	    .setPositiveButton("GoOnline", new DialogInterface.OnClickListener() {
 	    	        public void onClick(DialogInterface dialog, int which) { 
 	    	        	try{
 	    	        	Button b=(Button)findViewById(R.id.topbar_statusbtn);
 	    	    		ImageView i=(ImageView)findViewById(R.id.topbar_statusimage);
 	    	    		b.setText("Go Offline");
 	    	        	i.setBackgroundDrawable(getResources().getDrawable(R.drawable.green));
 	    	        	isOnline=true;
 	    	        	SharedPreferences sharedPref = getSharedPreferences("AppDelegate",MODE_PRIVATE);
 	    	            SharedPreferences.Editor prefEditor = sharedPref.edit();
 	    	            prefEditor.putBoolean("isOnline",true);
 	    	            prefEditor.commit();
 	    	            
 	    	           ParseSyncData par=new ParseSyncData(Attendance.this);
 	 				   par.start();
 	    	        	}
 	    	   		catch(Exception e)
 	    	   		{
 	    	   			Log.d("Exception", ""+e.getMessage());
 	    	   		}
 	    	        	
 	    	        }
 	    	     })
 	    	     .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
 	    	        public void onClick(DialogInterface dialog, int which) { 
 	    	            
 	    	        }
 	    	     })
 	    	    .show();

			 }
			 }
				catch(Exception e)
				{
					Log.d("Exception", ""+e.getMessage());
				}
		 }
		 public void ChangeStatusClick(View v)
		 {
			 try{	
		    		Button b=(Button)findViewById(R.id.topbar_statusbtn);
		    		ImageView i=(ImageView)findViewById(R.id.topbar_statusimage);
		    		if(isOnline)
		    		{		    			
		    			b.setText("Go Online");
		    			i.setBackgroundDrawable(getResources().getDrawable(R.drawable.status_online_icon));
		        		isOnline=false;
		        		SharedPreferences sharedPref = getSharedPreferences("AppDelegate",MODE_PRIVATE);
		                SharedPreferences.Editor prefEditor = sharedPref.edit();
		                prefEditor.putBoolean("isOnline",false);
		                prefEditor.commit();
		        	}
		        	else
		        	{
		        		b.setText("Go Offline");
		        		i.setBackgroundDrawable(getResources().getDrawable(R.drawable.green));
		        		isOnline=true;
		        		SharedPreferences sharedPref = getSharedPreferences("AppDelegate",MODE_PRIVATE);
		                SharedPreferences.Editor prefEditor = sharedPref.edit();
		                prefEditor.putBoolean("isOnline",true);
		                prefEditor.commit();
		        	}
		    	}
		    	catch(Exception e){
		    		Log.d("Error=", ""+e.getMessage());
		    	}
		 }
		 
		  @Override
		    public Object onRetainNonConfigurationInstance()
		    {
		    	strSetValues=new String[14];
		    	strSetValues[0]=SelectedContractorName;
		        strSetValues[1]=SelectedStatus;
		        strSetValues[2]=selectedDate;
		        strSetValues[3]=skilledWork;
		        strSetValues[4]=unskilledWork;
		        strSetValues[5]=ContractorType;
		        strSetValues[6]=AttnDate;
		    	return strSetValues;
		    }
		  public String GetUTCdateTimeAsString()
			{
			  
				final String DATEFORMAT = "yyyy-MM-dd HH:mm:ss aaa";

			    final SimpleDateFormat sdf = new SimpleDateFormat(DATEFORMAT);
			   // sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
			   String utcTime = sdf.format(new Date());

			    return utcTime;
			}
		  public String GetUTCdateTimeAsString2(Date dt)
			{
			  
				final String DATEFORMAT = "yyyy-MM-dd HH:mm:ss aaa";

			    final SimpleDateFormat sdf = new SimpleDateFormat(DATEFORMAT);
			   // sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
			   String utcTime = sdf.format(dt);

			    return utcTime;
			}
}
