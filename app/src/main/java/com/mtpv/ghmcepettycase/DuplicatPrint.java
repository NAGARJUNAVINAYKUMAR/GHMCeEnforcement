package com.mtpv.ghmcepettycase;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import com.mtpv.ghmcenforcement.R;
import com.mtpv.services.DataBase;
import com.mtpv.services.ServiceHelper;

@SuppressWarnings("unused")
public class DuplicatPrint extends Activity {
	public static boolean dupprintFLG = false;

	final int PROGRESS_DIALOG = 1;
	EditText regnNo;
	Button date_Btn, get_Btn, cancel2_Btn, back_Btn;
	TextView printTv;
	Button Btn;
	LinearLayout ll;
	LinearLayout layout;
	LinearLayout.LayoutParams params;
	String[] DuplicatePrint1;
	// ArrayList<String> arr_fav_list = new ArrayList<String>();

	private int pYear;
	private int pMonth;
	private int pDay;
	private int mSecond;
	private int mHour;
	private int mMinute;
	static final int DATE_DIALOG_ID = 0;
	static final int TIME_DIALOG_ID = 1;
	StringBuilder timestamp;

	String PidCode = null;// = com.tab39b.MainActivity.PIdCode;
	List<String> duplicateChallan = new ArrayList<String>();
	// List<String> DuplicatePrint1=new ArrayList<String>();
	List<String> textdata = new ArrayList<String>();
	HashMap<String, String> duplicateChallanMap = new HashMap<String, String>();

	static String resp, DuplicatePrint;
	// final WebService WS = new WebService();

	public static String offenceDate = null;

	public static String shopName = null;
	public static String firmRegnNo = null;
	public static String responsdentName = null, button_response = "";

	// DataBase db;

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_duplicat_print);

		SharedPreferences prefs1 = getSharedPreferences("loginValues", MODE_PRIVATE);
		String psName = prefs1.getString("PS_NAME", "");
		String officer_Name1 = prefs1.getString("PID_NAME", "");
		TextView officer_PS = (TextView)findViewById(R.id.officer_PS);
		TextView officer_Name = (TextView)findViewById(R.id.officer_Name);
		TextView companyName = (TextView) findViewById(R.id.CompanyName);
		companyName.startAnimation(AnimationUtils.loadAnimation(this, R.anim.marquee));
		officer_PS.setText(psName);
		officer_Name.setText(officer_Name1);

		Thread.setDefaultUncaughtExceptionHandler(new UnCaughtException(DuplicatPrint.this));
		
		date_Btn = (Button) findViewById(R.id.duplicate_date_btn);
		get_Btn = (Button) findViewById(R.id.get_dup_print);
		cancel2_Btn = (Button) findViewById(R.id.cancel_dup_print);
		// back_Btn = (Button)findViewById(R.id.back_Btn);

		printTv = (TextView) findViewById(R.id.dup_text);

		try {
			android.database.sqlite.SQLiteDatabase db = openOrCreateDatabase(
					DataBase.DATABASE_NAME, MODE_PRIVATE, null);
			String selectQuery = "SELECT * FROM " + DataBase.USER_TABLE;
			// SQLiteDatabase db = this.getWritableDatabase();
			Cursor cursor = db.rawQuery(selectQuery, null);
			// looping through all rows and adding to list

			if (cursor.moveToFirst()) {
				do {

					PidCode = cursor.getString(0);


				} while (cursor.moveToNext());
			}
			db.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		final Calendar cal = Calendar.getInstance();
		pYear = cal.get(Calendar.YEAR);
		pMonth = cal.get(Calendar.MONTH);
		pDay = cal.get(Calendar.DAY_OF_MONTH);
		mHour = cal.get(Calendar.HOUR_OF_DAY);
		mMinute = cal.get(Calendar.MINUTE);
		mSecond = cal.get(Calendar.SECOND);
		updateDisplay();

		date_Btn.setOnClickListener(new View.OnClickListener() {

			@SuppressWarnings("deprecation")
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showDialog(DATE_DIALOG_ID);

			}
		});

		layout = (LinearLayout) findViewById(R.id.h1);
		params = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.WRAP_CONTENT);

		params.setMargins(40, 10, 0, 0);
		layout.setLayoutParams(params);
		layout.setGravity(Gravity.CENTER_HORIZONTAL);

		get_Btn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(isOnline()) {
					new Async_task_getDetails().execute();
				}else {
					showToast("Please Check Your Network Connection");
				}

			}

		});

		cancel2_Btn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent back = new Intent(DuplicatPrint.this, Dashboard.class);
				startActivity(back);

			}
		});

	}

	public class Async_task_getDetails extends AsyncTask<Void, Void, String> {

		@SuppressWarnings("deprecation")
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			showDialog(PROGRESS_DIALOG);
		}

		@Override
		protected String doInBackground(Void... params) {
			// TODO Auto-generated method stub
			offenceDate = date_Btn.getText().toString();
			ServiceHelper.getDuplicatePrint(offenceDate, PidCode);
			return null;
		}

		@SuppressWarnings("deprecation")
		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			// progress.dismiss();
			removeDialog(PROGRESS_DIALOG);

			if (ServiceHelper.duplicatePrint_resp.trim() == null
					|| ServiceHelper.duplicatePrint_resp.trim().equals("NA")
					|| ServiceHelper.duplicatePrint_resp.trim().equals("0^NA^NA")
					|| ServiceHelper.duplicatePrint_resp.trim().equals("anyTyp{}")) {
				showToast("No Details Found !!");
			} else {
				DuplicatePrint = ServiceHelper.duplicatePrint_resp.trim();
				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						layout.removeAllViews();
						try {
							String[] split = DuplicatePrint.split("\\:");

							for (int i = 0; i < split.length; i++) {
								Log.i("split" + i, "" + split[i]);

								duplicateChallan.add("" + split[i]);
								textdata.add("" + split[i]);

								duplicateChallanMap.put("" + split[i], ""+ split[i]);

								ll = new LinearLayout(DuplicatPrint.this, null);
								LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
										LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
								ll.setOrientation(LinearLayout.HORIZONTAL);
								params.setMargins(0, 10, 0, 0);
								ll.setLayoutParams(params);

								Btn = new Button(DuplicatPrint.this);
								Btn.setId(i);
								Btn.setWidth(400);
								Btn.setText("" + split[i]);
								Btn.setTextColor(Color.WHITE);
								Btn.setBackgroundResource(R.drawable.button_background);
								Btn.setGravity(Gravity.CENTER);
								Btn.setOnClickListener(new View.OnClickListener() {

									@Override
									public void onClick(View v) {
										// TODO Auto-generated method stub
										CharSequence selectedButtonValue = ((Button) v).getText();
										Log.e("text :", selectedButtonValue.toString());

										for (String buttonText : duplicateChallanMap.keySet()) {
											if (buttonText.equals(selectedButtonValue.toString())) {

												Log.i("etcicket ", duplicateChallanMap.get(buttonText));
												button_response = duplicateChallanMap.get(buttonText);
												dupprintFLG = false;
												if(isOnline()) {
													new Async_task_eTicket().execute();
												}else
												{
													showToast("Please Check Your Network Connection");
												}

											}
										}
									}
								});
								ll.addView(Btn);
								layout.addView(ll);
							}

						} catch (Exception e) {
							runOnUiThread(new Runnable() {
								@Override
								public void run() {
									// TODO Auto-generated method stub

									showToast("Data Not Found");
								}
							});
						}

					}
				});
			}
		}
	}

	/************************************************************************* Date methos ********************************************/

	public class Async_task_eTicket extends AsyncTask<Void, Void, String> {
		String getDuplicatePrintByEticket = "";

		@Override
		protected String doInBackground(Void... params) {
			// button_response
			// getDuplicatePrintByEticket =
			// WS.getDuplicatePrintByEticket(button_response);
			ServiceHelper.getDupPrintBy_Eticket(button_response);
			return null;
		}

		@SuppressWarnings("deprecation")
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			showDialog(PROGRESS_DIALOG);
		}

		@SuppressWarnings("deprecation")
		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			removeDialog(PROGRESS_DIALOG);

			if (ServiceHelper.duplicatePrint_by_Eticket_resp.equals(null) && ServiceHelper.duplicatePrint_by_Eticket_resp.equals("0^NA^NA")) {
				dupprintFLG = false;
			} else if (ServiceHelper.duplicatePrint_by_Eticket_resp
					.equals("anyType{}")) {
				dupprintFLG = false;
			} else {
				dupprintFLG = true;
				String Challan_response = ""
						+ ServiceHelper.duplicatePrint_by_Eticket_resp;
				Intent i = new Intent(DuplicatPrint.this, PrintDisplay.class);
				startActivity(i);

				SharedPreferences prefs = getSharedPreferences("printData",
						MODE_PRIVATE);
				SharedPreferences.Editor edit = prefs.edit();

				edit.putString("print", "" + Challan_response);

				edit.commit();
			}
		}
	}

	private DatePickerDialog.OnDateSetListener pDateSetListener = new 
			DatePickerDialog.OnDateSetListener() {

		public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
			pYear = year;
			pMonth = monthOfYear;
			pDay = dayOfMonth;
			updateDisplay();
		}
	};

	// Register TimePickerDialog listener
	private TimePickerDialog.OnTimeSetListener mTimeSetListener = new 
			TimePickerDialog.OnTimeSetListener() {
		// the callback received when the user "sets" the TimePickerDialog in
		// the dialog
		/*
		 * public void onTimeSet(TimePicker view, int hourOfDay, int min) { hour
		 * = hourOfDay; minute = min; // Set the Selected Date in Select date
		 * Button btnSelectTime.setText("Time selected :"+hour+"-"+minute); }
		 */
		@Override
		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			// TODO Auto-generated method stub
			mHour = hourOfDay;
			mMinute = minute;
			updateDisplay();
			// displayToast();
		}
	};

	/** Updates the date in the TextView */

	@SuppressLint("SimpleDateFormat")
	private void updateDisplay() {

		date_Btn.setText(new StringBuilder()
				// Month is 0 based so add 1
				.append(pDay).append("/").append(pMonth + 1).append("/")
				.append(pYear).append(" "));
		timestamp = new StringBuilder().append(pDay).append("/")
				.append(pMonth + 1).append("/").append(pYear).append("/")
				.append(mHour).append(":").append(mMinute).append(":")
				.append(mSecond).append(" ");

	}

	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DATE_DIALOG_ID:
			return new DatePickerDialog(this, pDateSetListener, pYear, pMonth, pDay);

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

	private void showToast(String msg) {
		// TODO Auto-generated method stub
		Toast toast = Toast.makeText(getApplicationContext(), "" + msg,
				Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.CENTER, 0, 0);
		View toastView = toast.getView();

		ViewGroup group = (ViewGroup) toast.getView();
		TextView messageTextView = (TextView) group.getChildAt(0);
		messageTextView.setTextSize(24);

		toastView.setBackgroundResource(R.drawable.toast_background);
		toast.show();
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		// super.onBackPressed();
		Intent back = new Intent(DuplicatPrint.this, Dashboard.class);
		startActivity(back);
	}


	public Boolean isOnline() {
		ConnectivityManager conManager = (ConnectivityManager) this
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo nwInfo = conManager.getActiveNetworkInfo();
		return nwInfo != null;
	}

}
