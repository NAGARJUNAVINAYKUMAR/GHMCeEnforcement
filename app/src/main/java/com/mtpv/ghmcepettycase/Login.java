package com.mtpv.ghmcepettycase;

import java.util.ArrayList;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mtpv.ghmcenforcement.R;
import com.mtpv.services.DataBase;
import com.mtpv.services.ServiceHelper;

public class Login extends Activity implements LocationListener {

	Button cancel, submit;
	EditText user_name, password;
	ImageView ip_settings;

	DataBase db;
	public static String ip_got = null;

	final int PROGRESS_DIALOG = 1;

	public static String URL = "";
	private String url_to_fix = "/services/GHMCWebServiceImpl?wsdl";
	public static String temp_usr = null, temp_psd = null, imei_No = null,
			sim_No = null;

	/* GPS VALUES */
	boolean isGPSEnabled = false;
	// flag for network status
	boolean isNetworkEnabled = false;
	boolean canGetLocation = false;
	// The minimum distance to change Updates in meters
	private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 meters
	// The minimum time between updates in milliseconds
	private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1; // 1 minute
	double latitude = 0.0;
	double longitude = 0.0;
	String provider = "";
	String IMEI = "";
	LocationManager m_locationlistner;
	android.location.Location location;

	public static String[] arr_logindetails;
	// public static String PID_CODE= null, PID_NAME=null
	// ,PS_CODE=null,PS_NAME=null, CADRE_CODE=null, CADRE_NAME=null,
	// SECURITY_CD=null,OFFICER_TYPE=null ;

	public static String PID_CODE = null, PID_NAME = null, PS_CODE = null,
			PS_NAME = null, CADRE_CODE = null, CADRE_NAME = null,
			SECURITY_CD = null, GHMC_AUTH = null, CONTACT_NO = null,
			AADHAAR_DATA_FLAG = null, TIN_FLAG = null, OTP_NO_FLAG = null,
			CASHLESS_FLAG = null, MOBILE_NO_FLAG = null, RTA_DATA_FLAG = null,
			DL_DATA_FLAG = null, OFFICER_TYPE = null;
	
	public static String uintCode = "23";
	public static String uintName = "Hyderabad";

	/* FOR PS NAMES */
	ArrayList<String> ps_codes_fr_names_arr;
	ArrayList<String> ps_names_arr;
	String[][] psname_name_code_arr;
	public static int UnitCode = 23;

	private static final String[] requiredPermissions = new String[] {
			Manifest.permission.READ_PHONE_STATE,
			Manifest.permission.ACCESS_FINE_LOCATION,
			Manifest.permission.ACCESS_NETWORK_STATE,
			Manifest.permission.ACCESS_WIFI_STATE,
			Manifest.permission.ACCESS_COARSE_LOCATION,
			Manifest.permission.INTERNET,
			Manifest.permission.WRITE_EXTERNAL_STORAGE,
			Manifest.permission.READ_EXTERNAL_STORAGE,
			Manifest.permission.INSTALL_SHORTCUT, Manifest.permission.CAMERA,
			Manifest.permission.RECORD_AUDIO, Manifest.permission.ADD_VOICEMAIL
	/* ETC.. */
	};
	private static final int REQUEST_APP_SETTINGS = 168;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		setContentView(R.layout.activity_login);

		try {
			if (Build.VERSION.SDK_INT > 22 && !hasPermissions(requiredPermissions)) {
				ActivityCompat.requestPermissions(Login.this, new String[] {
						Manifest.permission.READ_EXTERNAL_STORAGE,
						Manifest.permission.READ_PHONE_STATE,
						Manifest.permission.ACCESS_FINE_LOCATION,
						Manifest.permission.ACCESS_NETWORK_STATE,
						Manifest.permission.ACCESS_WIFI_STATE,
						Manifest.permission.ACCESS_COARSE_LOCATION,
						Manifest.permission.INTERNET,
						Manifest.permission.WRITE_EXTERNAL_STORAGE,
						Manifest.permission.READ_EXTERNAL_STORAGE,
						Manifest.permission.READ_SMS,
						Manifest.permission.SEND_SMS,
						Manifest.permission.INSTALL_SHORTCUT,
						Manifest.permission.CAMERA,
						Manifest.permission.RECORD_AUDIO,
						Manifest.permission.ADD_VOICEMAIL }, 1);
			} else {
				getLocation();
				try {
					TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
					IMEI = getDeviceID(telephonyManager);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		db = new DataBase(getApplicationContext());
		getLocation();
		
		try {
			TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
			IMEI = getDeviceID(telephonyManager);
		} catch (Exception e) {
			e.printStackTrace();
		}

		user_name = (EditText) findViewById(R.id.et_userName);
		password = (EditText) findViewById(R.id.et_password);
		
		// LOGIN CREDENTIALS
		user_name.setText("23001004");
		password.setText("WdSt48Pr");

		submit = (Button) findViewById(R.id.submit);
		cancel = (Button) findViewById(R.id.cancel);
		ip_settings = (ImageView) findViewById(R.id.ip_settings);

		try {
			SharedPreferences prefs = getSharedPreferences("ghmcPref", MODE_PRIVATE);
			ip_got = prefs.getString("IP_ADDRESS", "");
			URL = ip_got + url_to_fix;
			Log.i("URL ::: ", "" + URL);
			Log.i("ip_got ::: ", "" + ip_got);
		} catch (Exception e) {
			// TODO: handle exception
		}

		ip_settings.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				getLocation();
				try {
					TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
					IMEI = getDeviceID(telephonyManager);

				} catch (Exception e) {
					e.printStackTrace();
				}
				Intent ip_settings = new Intent(Login.this, IP_settings.class);
				startActivity(ip_settings);
			}
		});
		
		submit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				try {
					SharedPreferences prefs = getSharedPreferences("ghmcPref", MODE_PRIVATE);
					ip_got = prefs.getString("IP_ADDRESS", "");
					URL = ip_got + url_to_fix;
					Log.i("URL ::: ", "" + URL);
					Log.i("ip_got ::: ", "" + ip_got);
				} catch (Exception e) {
					// TODO: handle exception
				}
				
				if (user_name.getText().toString().trim().equals("")) {
					user_name.setError(Html.fromHtml("<font color='black'>Please Enter User Name</font>"));
					user_name.requestFocus();
					
				} else if (password.getText().toString().trim().equals("")) {
					password.setError(Html.fromHtml("<font color='black'>Please Enter Password</font>"));
					password.requestFocus();
					
				} else {
					temp_usr = user_name.getText().toString().trim();
					temp_psd = password.getText().toString().trim();

					SharedPreferences prefs = getSharedPreferences("ghmcPref", MODE_PRIVATE);
					ip_got = prefs.getString("IP_ADDRESS", "");

					if (ip_got.length() > 1) {
						Log.i("IF Block", "Entered");
						SQLiteDatabase db2 = openOrCreateDatabase(DataBase.DATABASE_NAME, MODE_PRIVATE, null);
						// db.execSQL("delete from " + DataBase.USER_TABLE);
						db2.execSQL("DROP TABLE IF EXISTS " + DataBase.IP_TABLE);
						db2.execSQL(DataBase.CREATE_IP_TABLE);
						db2.close();
						
						try {
							db.open();
							String ip_query = "select * from " + DataBase.IP_TABLE;
							Cursor c = DataBase.db.rawQuery(ip_query, null);

							if (c.moveToNext()) {
								do {
									Log.i("IP ADDRESS ::::", "" + c.getString(0));
								} while (c.moveToNext());
							}
							if (c != null) {
								c.close();
							}
						} catch (Exception e) {
							// TODO: handle exception
							if (db != null) {
								db.close();
							}
						}
						LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
						if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
							new Async_Login().execute();
						} else {
							showGPSDisabledAlertToUser();
						}
					} else {
						showToast("Please Save IP Settings !!!");
					}
				}
			}
		});

		cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				user_name.setText("");
				password.setText("");
			}
		});
	}

	public boolean hasPermissions(String... permissions) {
		for (String permission : permissions)
			if (PackageManager.PERMISSION_GRANTED != checkCallingOrSelfPermission(permission))
				return false;
		return true;
	}

	@SuppressLint("Override")
	public void onRequestPermissionsResult(int requestCode,
			String permissions[], int[] grantResults) {
		switch (requestCode) {
		
		case 1: {
			if (grantResults.length > 0
					&& grantResults[0] == PackageManager.PERMISSION_GRANTED) {
				// permission was granted, yay! Do the
				// contacts-related task you need to do.
			} else {
				ActivityCompat.requestPermissions(Login.this, new String[] {
						Manifest.permission.READ_EXTERNAL_STORAGE,
						Manifest.permission.READ_PHONE_STATE,
						Manifest.permission.ACCESS_FINE_LOCATION,
						Manifest.permission.ACCESS_NETWORK_STATE,
						Manifest.permission.ACCESS_WIFI_STATE,
						Manifest.permission.ACCESS_COARSE_LOCATION,
						Manifest.permission.INTERNET,
						Manifest.permission.WRITE_EXTERNAL_STORAGE,
						Manifest.permission.READ_EXTERNAL_STORAGE,
						Manifest.permission.READ_SMS,
						Manifest.permission.SEND_SMS,
						Manifest.permission.INSTALL_SHORTCUT }, 1);

				showToast("Please Enable All the Permissions required to Use the App");
			}
			return;
		}
		}
	}

	class AsyncSections extends AsyncTask<Void, Void, String> {

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			showDialog(PROGRESS_DIALOG);
		}

		@Override
		protected String doInBackground(Void... params) {
			// TODO Auto-generated method stub
			String unitcode = "23";
			ServiceHelper.getSectionMasters("" + unitcode);
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			removeDialog(PROGRESS_DIALOG);
		}
	}

	protected void showGPSDisabledAlertToUser() {
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
		alertDialogBuilder.setMessage("GPS is Disabled in your Device \nPlease Enable GPS?")
				.setCancelable(false)
				.setPositiveButton("Goto Settings",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								Intent callGPSSettingIntent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
								startActivity(callGPSSettingIntent);
							}
						});
		alertDialogBuilder.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}
				});
		AlertDialog alert = alertDialogBuilder.create();
		alert.show();
	}

	private String getDeviceID(TelephonyManager phonyManager) {

		String id = phonyManager.getDeviceId();
		if (id == null) {
			id = "not available";
		}

		int phoneType = phonyManager.getPhoneType();
		
		switch (phoneType) {
		
		case TelephonyManager.PHONE_TYPE_NONE:
			return id;

		case TelephonyManager.PHONE_TYPE_GSM:
			return id;

		case TelephonyManager.PHONE_TYPE_CDMA:
			return id;

		default:
			return "UNKNOWN:ID=" + id;
			
		}
	}

	private void getLocation() {
		try {
			m_locationlistner = (LocationManager) this.getSystemService(LOCATION_SERVICE);
			// getting GPS status
			isGPSEnabled = m_locationlistner.isProviderEnabled(LocationManager.GPS_PROVIDER);
			// getting network status
			isNetworkEnabled = m_locationlistner.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
			if (!isGPSEnabled && !isNetworkEnabled) {
				// no network provider is enabled
				latitude = 0.0;
				longitude = 0.0;
			} else {
				this.canGetLocation = true;
				// First get location from Network Provider
				if (isNetworkEnabled) {
					m_locationlistner.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
							MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
					Log.d("Network", "Network");
					if (m_locationlistner != null) {
						location = m_locationlistner.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
						if (location != null) {
							latitude = location.getLatitude();
							longitude = location.getLongitude();
						} else {
							latitude = 0.0;
							longitude = 0.0;
						}
					}
				}
				// if GPS Enabled get lat/long using GPS Services
				if (isGPSEnabled) {
					if (location == null) {
						m_locationlistner.requestLocationUpdates( LocationManager.GPS_PROVIDER,
								MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
						Log.d("GPS Enabled", "GPS Enabled");
						if (m_locationlistner != null) {
							location = m_locationlistner.getLastKnownLocation(LocationManager.GPS_PROVIDER);
							if (location != null) {
								latitude = location.getLatitude();
								longitude = location.getLongitude();
							} else {
								latitude = 0.0;
								longitude = 0.0;
							}
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/************** NETWORK(GPS) AVAILABILITY **************/
	private boolean isNetworkAvailable() {
		ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
		return activeNetworkInfo != null && activeNetworkInfo.isConnected();
	}

	class Async_Login extends AsyncTask<Void, Void, String> {

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			showDialog(PROGRESS_DIALOG);
		}

		@Override
		protected String doInBackground(Void... arg0) {
			// TODO Auto-generated method stub
			ServiceHelper.getLogin(temp_usr, temp_psd, IMEI, ""+ latitude, ""+ longitude);
			return null;
		}

		@SuppressWarnings("deprecation")
		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			removeDialog(PROGRESS_DIALOG);
			
			if (isNetworkAvailable()) {
				
				if (ServiceHelper.login_resp != null) {
					
					if (ServiceHelper.login_resp.toString().trim().equals("1")) {
						showToast("Invalid Login ID");
						
					} else if (ServiceHelper.login_resp.toString().trim().equals("2")) {
						showToast("Unautherized Device");
						
					} else if (ServiceHelper.login_resp.toString().trim().equals("3")) {
						showToast("Error, Please Contact E Challan Team at 040-27852721");
						
					} else {

						try {
							db.open();
							arr_logindetails = ServiceHelper.login_resp.split("\\|");

							PID_CODE = arr_logindetails[0];//
							PID_NAME = arr_logindetails[1];//
							PS_CODE = arr_logindetails[2];//
							PS_NAME = arr_logindetails[3];//
							CADRE_CODE = arr_logindetails[4];//
							CADRE_NAME = arr_logindetails[5];//
							SECURITY_CD = arr_logindetails[6];//
							OFFICER_TYPE = arr_logindetails[7];
							
							GHMC_AUTH = arr_logindetails[7];
							CONTACT_NO = arr_logindetails[8];
							AADHAAR_DATA_FLAG = arr_logindetails[9];
							TIN_FLAG = arr_logindetails[10];
							OTP_NO_FLAG = arr_logindetails[11];
							CASHLESS_FLAG = arr_logindetails[12];
							MOBILE_NO_FLAG = arr_logindetails[13];
							RTA_DATA_FLAG = arr_logindetails[14];
							DL_DATA_FLAG = arr_logindetails[15];

							ContentValues values = new ContentValues();
							values.put("PID_CODE", PID_CODE);
							values.put("PID_NAME", PID_NAME);
							values.put("PS_CODE", PS_CODE);
							values.put("PS_NAME", PS_NAME);
							values.put("CADRE_CODE", CADRE_CODE);
							values.put("CADRE_NAME", CADRE_NAME);
							values.put("SECURITY_CD", SECURITY_CD);
							values.put("OFFICER_TYPE", OFFICER_TYPE);

							SQLiteDatabase db = openOrCreateDatabase(DataBase.DATABASE_NAME, MODE_PRIVATE, null);
							// db.execSQL("delete from " + DataBase.USER_TABLE);
							db.execSQL("DROP TABLE IF EXISTS "+ DataBase.USER_TABLE);
							db.execSQL(DataBase.CREATE_USER_TABLE);
							db.insert(DataBase.USER_TABLE, null, values); // Inserting row
							System.out.println("*********************OFFICER TABLE Insertion Successfully **********************************");

							SharedPreferences prefs = getSharedPreferences("loginValues", MODE_PRIVATE);
							SharedPreferences.Editor edit = prefs.edit();
							/*
							 * PID_CODE= ""; PID_NAME=""; PS_CODE="";
							 * PS_NAME=""; CADRE_CODE=""; CADRE_NAME="";
							 * SECURITY_CD=""; GHMC_AUTH=""; CONTACT_NO="";
							 * AADHAAR_DATA_FLAG=""; TIN_FLAG="";
							 * OTP_NO_FLAG=""; CASHLESS_FLAG="";
							 * MOBILE_NO_FLAG=""; RTA_DATA_FLAG="";
							 * DL_DATA_FLAG="";
							 */
							edit.putString("PID_CODE", PID_CODE); //0
							edit.putString("PID_NAME", PID_NAME); //1
							edit.putString("PS_CODE", PS_CODE); //2
							edit.putString("PS_NAME", PS_NAME); //3
							edit.putString("CADRE_CODE", CADRE_CODE); //4
							edit.putString("CADRE_NAME", CADRE_NAME); //5
							edit.putString("SECURITY_CD", SECURITY_CD); //6
							edit.putString("OFFICER_TYPE", OFFICER_TYPE); //7
							edit.putString("GHMC_AUTH", OFFICER_TYPE); //7
							edit.putString("CONTACT_NO", CONTACT_NO); //8
							edit.putString("AADHAAR_DATA_FLAG", AADHAAR_DATA_FLAG); //9
							edit.putString("TIN_FLAG", TIN_FLAG); //10
							edit.putString("OTP_NO_FLAG", OTP_NO_FLAG); //11
							edit.putString("CASHLESS_FLAG", CASHLESS_FLAG); //12
							edit.putString("MOBILE_NO_FLAG", MOBILE_NO_FLAG); //13
							edit.putString("RTA_DATA_FLAG", RTA_DATA_FLAG); //14
							edit.putString("DL_DATA_FLAG", DL_DATA_FLAG); //15

							// edit.putString("", "")
							Log.i("PS_NAME :::", "" + PS_NAME);
							Log.i("PID_CODE :::", "" + PID_CODE);
							Log.i("Async_task_GetPsName ::::", "Entered");
							edit.commit();
						} catch (Exception e) {
							// TODO: handle exception
							if (db != null) {
								db.close();
							}
						}
						Intent dashboard = new Intent(getApplicationContext(), Dashboard.class);
						startActivity(dashboard);
					}
				} else {
					showToast("Login Failed!");
				}
			} else {
				showToast("Please Check Network Connection!!!");
			}
		}
	}

	public class Async_task_GetPsName extends AsyncTask<Void, Void, String> {

		@SuppressWarnings("deprecation")
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			showDialog(PROGRESS_DIALOG);
			Log.i("Async_task_GetPsName ::::", "Entered");
		}

		@Override
		protected String doInBackground(Void... params) {
			// TODO Auto-generated method stub
			ServiceHelper.getPSNames("" + Login.uintCode);
			return null;
		}

		@SuppressWarnings("deprecation")
		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			removeDialog(PROGRESS_DIALOG);

			psname_name_code_arr = new String[ServiceHelper.psNames_master.length][2];
			for (int i = 0; i < ServiceHelper.psNames_master.length; i++) {
				psname_name_code_arr[i] = ServiceHelper.psNames_master[i].split(":");
			}

			try {
				db.open();
				ps_codes_fr_names_arr = new ArrayList<String>();
				ps_names_arr = new ArrayList<String>();
				ps_codes_fr_names_arr.clear();
				ps_names_arr.clear();

				SQLiteDatabase db2 = openOrCreateDatabase(DataBase.DATABASE_NAME, MODE_PRIVATE, null);
				// db.execSQL("delete from " + DataBase.USER_TABLE);
				db2.execSQL("DROP TABLE IF EXISTS " + DataBase.psName_table);
				db2.execSQL(DataBase.psNamesCreation);

				for (int j = 0; j < psname_name_code_arr.length; j++) {
					ps_codes_fr_names_arr.add(psname_name_code_arr[j][0]);
					ps_names_arr.add(psname_name_code_arr[j][1]);

					db.insertPsNameDetails(""+ psname_name_code_arr[j][0], ""+ psname_name_code_arr[j][1]);
					Log.i("psname_name_code_arr[i] :::", ""+ psname_name_code_arr[j][0] + psname_name_code_arr[j][1]);
				}
				db2.close();
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				if (db != null) {
					db.close();
				}
			}
		}
	}

	@SuppressWarnings("deprecation")
	@Override
	protected Dialog onCreateDialog(int id) {
		// TODO Auto-generated method stub
		switch (id) {

		case PROGRESS_DIALOG:
			ProgressDialog pd = ProgressDialog.show(this, "", "", true);
			pd.setContentView(R.layout.custom_progress_dialog);
			pd.setCancelable(false);

			return pd;

		default:
			break;
		}
		return super.onCreateDialog(id);
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

	@Override
	public void onLocationChanged(Location arg0) {
		// TODO Auto-generated method stub
		if (location != null) {
			latitude = (float) location.getLatitude();
			longitude = (float) location.getLongitude();
			// speed = location.getSpeed();
			// latitude = gps.getLatitude();
			// longitude = gps.getLongitude();
		} else {
			latitude = 0.0;
			longitude = 0.0;
		}
	}

	@Override
	public void onProviderDisabled(String arg0) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onProviderEnabled(String arg0) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onBackPressed() {
		TextView title = new TextView(this);
		title.setText("GHMC e-Enforcement");
		title.setBackgroundColor(Color.RED);
		title.setGravity(Gravity.CENTER);
		title.setTextColor(Color.WHITE);
		title.setTextSize(26);
		title.setTypeface(title.getTypeface(), Typeface.BOLD);
		title.setCompoundDrawablesWithIntrinsicBounds(R.drawable.traffic_small,
				0, R.drawable.ghmc_small, 0);
		title.setPadding(20, 0, 20, 0);
		title.setHeight(70);

		String otp_message = "\n Are you sure, You want to Leave Application...! \n";

		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Login.this, AlertDialog.THEME_HOLO_LIGHT);
		alertDialogBuilder.setCustomTitle(title);
		alertDialogBuilder.setIcon(R.drawable.dialog_logo);
		alertDialogBuilder.setMessage(otp_message);
		alertDialogBuilder.setCancelable(false);
		alertDialogBuilder.setPositiveButton("Ok",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						Intent intent = new Intent(Intent.ACTION_MAIN);
						intent.addCategory(Intent.CATEGORY_HOME);
						startActivity(intent);
					}
				});

		alertDialogBuilder.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
					}
				});

		AlertDialog alertDialog = alertDialogBuilder.create();
		alertDialog.show();
		alertDialog.getWindow().getAttributes();

		TextView textView = (TextView) alertDialog.findViewById(android.R.id.message);
		textView.setTextSize(28);
		textView.setTypeface(textView.getTypeface(), Typeface.BOLD);
		textView.setGravity(Gravity.CENTER);

		Button btn1 = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
		btn1.setTextSize(22);
		btn1.setTextColor(Color.WHITE);
		btn1.setTypeface(btn1.getTypeface(), Typeface.BOLD);
		btn1.setBackgroundColor(Color.RED);

		Button btn2 = alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE);
		btn2.setTextSize(22);
		btn2.setTextColor(Color.WHITE);
		btn2.setTypeface(btn2.getTypeface(), Typeface.BOLD);
		btn2.setBackgroundColor(Color.RED);
		
	}
}
