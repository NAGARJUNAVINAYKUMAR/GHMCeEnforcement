package com.mtpv.ghmcepettycase;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import com.analogics.thermalAPI.Bluetooth_Printer_3inch_ThermalAPI;
import com.analogics.thermalprinter.AnalogicsThermalPrinter;
import com.mtpv.ghmcenforcement.R;
import com.mtpv.ghmcenforcement.R.drawable;
import com.mtpv.ghmcenforcement.R.id;
import com.mtpv.ghmcenforcement.R.layout;
import com.mtpv.services.DataBase;
import com.mtpv.services.ServiceHelper;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

public class Ph_printDisplay extends Activity implements OnItemSelectedListener, LocationListener {
	final int PROGRESS_DIALOG = 1;
	TextView Tv;
	Button back_Btn, print_Btn;
	String Pidcode , PidName, PsCode, PsName, cadreCD,cadre,password;
	String generateChallan;
	
	double latitude = 0.0;
	double longitude = 0.0;
	String provider = "";
	
	final AnalogicsThermalPrinter actual_printer = new AnalogicsThermalPrinter();
	final Bluetooth_Printer_3inch_ThermalAPI bth_printer = new Bluetooth_Printer_3inch_ThermalAPI();
	GPSTracker gps;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_ph_print);

		SharedPreferences prefs1 = getSharedPreferences("loginValues", MODE_PRIVATE);
		String psName = prefs1.getString("PS_NAME", "");
		String officer_Name1 = prefs1.getString("PID_NAME", "");
		TextView officer_PS = (TextView)findViewById(R.id.officer_PS);
		TextView officer_Name = (TextView)findViewById(R.id.officer_Name);
		TextView companyName = (TextView) findViewById(R.id.CompanyName);
		companyName.startAnimation(AnimationUtils.loadAnimation(this, R.anim.marquee));
		officer_PS.setText(psName);
		officer_Name.setText(officer_Name1);
		
		back_Btn = (Button)findViewById(R.id.back_Btn);
		print_Btn = (Button)findViewById(R.id.print_Btn);
		
		Tv = (TextView)findViewById(R.id.printTv);
		Tv.setText("");
		
		SharedPreferences prefs = getSharedPreferences("printData_phistory_id", MODE_PRIVATE);
		
		String data_to_print = prefs.getString("print", "");
		
		Log.i("data to print :::: print ", ""+data_to_print);
		
		if (Previous_History.previoushistoryFLG) {
			Tv.setText(""+Previous_History.PRINT_DATA_ID);
		}else {
			Tv.setText("");
		}
		
		try {
			String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
					Locale.getDefault()).format(new Date());
			
			String path = android.os.Environment
	                .getExternalStorageDirectory()
	                + File.separator
	                + "GHMC-Videos" + File.separator + GenerateCase.date;
			
			File from = new File(path,"temp.mp4");
			File to = new File(path,ServiceHelper.final_resp_ticketNo+"_"+timeStamp+".mp4");
			from.renameTo(to);
			
				
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		Tv.setText(""+Previous_History.PRINT_DATA_ID);
		Log.i("data_to_print_prevoius history:::", ""+data_to_print);
		
		back_Btn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				
				gps = new GPSTracker(Ph_printDisplay.this);
				 
                // check if GPS enabled     
                if(gps.canGetLocation()){
                     
                    latitude = gps.getLatitude();
                    longitude = gps.getLongitude();
                     
                    Log.i("latitude ::::", ""+latitude);
                    Log.i("longitude :::", ""+longitude);
                        
                }else{
                    // can't get location
                    // GPS or Network is not enabled
                    // Ask user to enable GPS/network in settings
                    gps.showSettingsAlert();
                }
					if (Previous_History.previoushistoryFLG) {
						
					
					
					/*Previous_History.eticketNo=null;
					Previous_History.et_id_text_prev_hist= null;
					Previous_History.et_id_firm_prev_hist = null;
					
					SharedPreferences TableValues = getSharedPreferences("TableValues", MODE_PRIVATE);
					SharedPreferences.Editor val_editor = TableValues.edit();
					val_editor.clear(); 
					val_editor.commit();
					
					
					Intent i = new Intent(Ph_printDisplay.this, Previous_History.class); 
					startActivity(i);*/
						finish();
					}
			}
		});
		
		print_Btn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				new Async_Task_PrintData().execute();
				
			}
		});
		
		 	/*Intent intent = getIntent();
		 
		    generateChallan = intent.getStringExtra("generateChallan");
		     
		    Log.e("generateChallan TEXTVIEW:: ", generateChallan);*/
		
	
	}
	public class Async_Task_PrintData extends AsyncTask<Void, Void, String>{
		//ProgressDialog progress = ProgressDialog.show(Reports.this, "Loading...!", "Please wait......Processing!!!");
		
		@SuppressWarnings("deprecation")
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			showDialog(PROGRESS_DIALOG);
		}
		
		@SuppressWarnings("unused")
		@Override
		protected String doInBackground(Void... params) {
			// TODO Auto-generated method stub
			String bt = "" ;
			DataBase helper= new DataBase(getApplicationContext());
			try {
				android.database.sqlite.SQLiteDatabase db = openOrCreateDatabase(DataBase.DATABASE_NAME,MODE_PRIVATE, null);
				String selectQuery = "SELECT  * FROM " + DataBase.Bluetooth;
			     //SQLiteDatabase db = this.getWritableDatabase();
			    Cursor cursor = db.rawQuery(selectQuery, null);
		       // looping through all rows and adding to list
			    
		        if (cursor.moveToFirst()) {
		            do {
		            	
		            	Log.i("1 :",""+ cursor.getString(0));
		            	bt = cursor.getString(0);
		           	 	
		           	 	//et_bt_address.setText(BLT_Name);
		           	 
	            		} while (cursor.moveToNext());
		        		}
						db.close(); 
					} catch (Exception e) {
						e.printStackTrace();
				
				}

			try{
				Bluetooth_Printer_3inch_ThermalAPI printer = new Bluetooth_Printer_3inch_ThermalAPI();

                String printdata = printer.font_Courier_41(Tv.getText().toString());
                actual_printer.openBT(bt);
                
                actual_printer.printData(printdata);
                Thread.sleep(5000);
                actual_printer.closeBT();
                
			
			}catch(java.lang.IllegalArgumentException ee){
				ee.printStackTrace();
				runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						showToast("Turn on Bluetooth and Configure Bluetooth Settings ");
					}
				});
				
				//showToast("Turn on Bluetooth and Configure Bluetooth Settings ");
			}catch(Exception e){
				e.printStackTrace();
				runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						showToast("Turn on Bluetooth and Configure Bluetooth Settings ");
					}
				});
				
			}
			
			return null;
		}
		
		@SuppressWarnings("deprecation")
		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			removeDialog(PROGRESS_DIALOG);
		}
	}
	
	private void showToast(String msg) {
		// TODO Auto-generated method stub
		Toast toast = Toast.makeText(getApplicationContext(), "" + msg, Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.CENTER, 0, 0);
		View toastView = toast.getView();
		
		ViewGroup group = (ViewGroup) toast.getView();
	    TextView messageTextView = (TextView) group.getChildAt(0);
	    messageTextView.setTextSize(24);
		
    	toastView.setBackgroundResource(R.drawable.toast_background);
	    toast.show();
	}
	
	 protected Dialog onCreateDialog(int id) {
	        switch (id) {
		    case PROGRESS_DIALOG:
				ProgressDialog pd = ProgressDialog.show(this, "", "",	true);
				pd.setContentView(R.layout.custom_progress_dialog);
				pd.setCancelable(false); 
				
				return pd;
				
		    default:
				break;
		        }
				return null; 
	    }
	 
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		//super.onBackPressed();
		showToast("Please Click  on Back to go Back");
	}

	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		// TODO Auto-generated method stub
		
	}
}
