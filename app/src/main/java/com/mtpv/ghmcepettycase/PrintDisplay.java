package com.mtpv.ghmcepettycase;

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
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.analogics.thermalAPI.Bluetooth_Printer_3inch_ThermalAPI;
import com.analogics.thermalprinter.AnalogicsThermalPrinter;
import com.mtpv.ghmcenforcement.R;
import com.mtpv.services.DataBase;
import com.mtpv.services.ServiceHelper;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class PrintDisplay extends Activity implements OnItemSelectedListener,
		LocationListener {

	final int PROGRESS_DIALOG = 1;
	TextView Tv;
	Button back_Btn, print_Btn;
	String Pidcode, PidName, PsCode, PsName, cadreCD, cadre, password;
	String generateChallan;

	double latitude = 0.0;
	double longitude = 0.0;
	String provider = "";

	final AnalogicsThermalPrinter actual_printer = new AnalogicsThermalPrinter();
	final Bluetooth_Printer_3inch_ThermalAPI bth_printer = new Bluetooth_Printer_3inch_ThermalAPI();
	GPSTracker gps;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_print_display);

		back_Btn = (Button) findViewById(R.id.back_Btn);
		print_Btn = (Button) findViewById(R.id.print_Btn);

		Tv = (TextView) findViewById(R.id.printTv);
		Tv.setText("");

		SharedPreferences prefs = getSharedPreferences("printData", MODE_PRIVATE);
		String data_to_print = prefs.getString("print", "");

		// Log.i("ServiceHelper.print_resp @@@@@@@@@@:",
		// ServiceHelper.print_resp);
		// Log.i("data_to_print @@@@@@@@:", data_to_print);

		if (GenerateCase.generateCaseFLG && !DuplicatPrint.dupprintFLG) {
			Tv.setText("" + data_to_print);

		} else if (!GenerateCase.generateCaseFLG && DuplicatPrint.dupprintFLG) {
			Tv.setText("" + ServiceHelper.duplicatePrint_by_Eticket_resp);

		} else {
			Tv.setText("");
		}

		try {
			String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());

			String path = android.os.Environment.getExternalStorageDirectory() + File.separator + "GHMC-Videos"
					+ File.separator + GenerateCase.date;

			File from = new File(path, "temp.mp4");
			File to = new File(path, ServiceHelper.final_resp_ticketNo + "_" + timeStamp + ".mp4");
			from.renameTo(to);

		} catch (Exception e) {
			e.printStackTrace();
		}

		Tv.setText("" + data_to_print);
		Log.i("data_to_print :::", "" + data_to_print);

		back_Btn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				gps = new GPSTracker(PrintDisplay.this);
				// check if GPS enabled
				if (gps.canGetLocation()) {
					latitude = gps.getLatitude();
					longitude = gps.getLongitude();
					Log.i("latitude ::::", "" + latitude);
					Log.i("longitude :::", "" + longitude);

				} else {
					// can't get location
					// GPS or Network is not enabled
					// Ask user to enable GPS/network in settings
					gps.showSettingsAlert();
				}

				if (GenerateCase.generateCaseFLG && !DuplicatPrint.dupprintFLG) {
					GenerateCase.image1ByteArray = null;
					GenerateCase.imageByteArray = null;
					GenerateCase.image_data = null;
					GenerateCase.imgString = null;
					GenerateCase.imgString2 = null;
					GenerateCase.imgString3 = null;
					GenerateCase.imgString4 = null;
					GenerateCase.owner_image_data = null;

					// GenerateCase.otp_verify_status = "Y" ;
					DetainedItems.Seize_image = null;

					SharedPreferences casvals = getSharedPreferences("CaseValues", MODE_PRIVATE);
					SharedPreferences.Editor val_editor = casvals.edit();
					val_editor.clear();
					val_editor.commit();

					SharedPreferences preferences = getSharedPreferences("ghmcValues", MODE_PRIVATE);
					SharedPreferences.Editor editor = preferences.edit();
					editor.clear();
					editor.commit();

					SharedPreferences witness_pref = getSharedPreferences("panchayathDhars", MODE_PRIVATE);
					SharedPreferences.Editor editor_pref = witness_pref.edit();
					editor_pref.clear();
					editor_pref.commit();

					DetainedItems.detItems.setLength(0);
					DetainedItems.detendItemsA.setLength(0);

					Intent i = new Intent(PrintDisplay.this, Dashboard.class);
					startActivity(i);

				} else if (!GenerateCase.generateCaseFLG && DuplicatPrint.dupprintFLG) {
					Intent i2 = new Intent(PrintDisplay.this, DuplicatPrint.class);
					startActivity(i2);

				} else {
					Intent i3 = new Intent(PrintDisplay.this, Dashboard.class);
					startActivity(i3);
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



	}

	public class Async_Task_PrintData extends AsyncTask<Void, Void, String> {
		// ProgressDialog progress = ProgressDialog.show(Reports.this,
		// "Loading...!", "Please wait......Processing!!!");

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
			String bt = "";
			DataBase helper = new DataBase(getApplicationContext());
			try {
				android.database.sqlite.SQLiteDatabase db = openOrCreateDatabase(
						DataBase.DATABASE_NAME, MODE_PRIVATE, null);
				String selectQuery = "SELECT * FROM " + DataBase.Bluetooth;
				// SQLiteDatabase db = this.getWritableDatabase();
				Cursor cursor = db.rawQuery(selectQuery, null);
				// looping through all rows and adding to list

				if (cursor.moveToFirst()) {
					do {
						bt = cursor.getString(0);
						// et_bt_address.setText(BLT_Name);
					} while (cursor.moveToNext());
				}
				db.close();
			} catch (Exception e) {
				e.printStackTrace();
			}

			try {
				Bluetooth_Printer_3inch_ThermalAPI printer = new Bluetooth_Printer_3inch_ThermalAPI();
				String printdata = printer.font_Courier_41(Tv.getText().toString());
				actual_printer.openBT(bt);
				actual_printer.printData(printdata);
				Thread.sleep(5000);
				actual_printer.closeBT();

			} catch (java.lang.IllegalArgumentException ee) {
				ee.printStackTrace();
				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						showToast("Turn on Bluetooth and Configure Bluetooth Settings ");
					}
				});

				// showToast("Turn on Bluetooth and Configure Bluetooth Settings ");
			} catch (Exception e) {
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
				ProgressDialog pd = ProgressDialog.show(this, "", "", true);
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
		// super.onBackPressed();
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
