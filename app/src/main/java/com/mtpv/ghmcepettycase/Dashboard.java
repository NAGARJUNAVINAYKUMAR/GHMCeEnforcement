package com.mtpv.ghmcepettycase;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.mtpv.ghmcenforcement.BuildConfig;
import com.mtpv.ghmcenforcement.R;
import com.mtpv.services.DataBase;
import com.mtpv.services.ServiceHelper;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.HashMap;

public class Dashboard extends Activity {

	ImageView generate_case, previous_history, duplicate_print, reports,
			download_masters, settings, logout, capture_images, capture_videos;
	TextView psname, officerName;
	final int PROGRESS_DIALOG = 1;
	final int WHEELER_CODE = 2;
	String server = "192.168.11.9", username = "ftpuser", password = "Dk0r$l1qMp6", filename = "Version-1.5.1.apk", netwrk_info_txt = "" ;
	int port = 99;
	private static final int BUFFER_SIZE = 4096;
	ProgressBar progress;
	Dialog dialog;
	int downloadedSize = 0;
	int totalSize = 0;
	TextView cur_val;

	/* FOR PS NAMES */
	ArrayList<String> ps_codes_fr_names_arr;
	ArrayList<String> ps_names_arr;
	String[][] psname_name_code_arr;

	private static String NAMESPACE = "http://service.mother.com";
	public static String methodIDMaster = "getIDProofMaster";
	// public static String methodAuth = "authenticateUser";
	public static String SOAP_ACTION = NAMESPACE + methodIDMaster;

	public static String Id_proof_result = "";
	public static String[] resp_array,otp_Master;

	public static HashMap<String, String> idDetailsMap = new HashMap<String, String>();

	// public static ArrayList<String> idDetails_arr;

	public static String[][] temp_sections_master2;
	/* FOR SECTION NAMES */
	ArrayList<String> temp_section_codes_names_arr;
	ArrayList<String> temp_section_names_arr;
	ArrayList<String> temp_section_fine_arr;
	String[][] temp_section_name_code_arr;

	String[] section_code_arr_spot, section_name_arr_spot;

	int selected_section_code = -1;

	public static int UnitCode = 23;

	/* WHELLER DETAILS W_CODE & W_NAME */
	public static String[][] section_code_name;
	public static ArrayList<String> section_name_arr;

	public static String section_code_send = "", BLT_Name = null;
	DataBase db;
	Cursor c;
	GPSTracker gps;
	public static String OtpStatus, OtpResponseDelayTime;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_dashboard);

		db = new DataBase(getApplicationContext());

		generate_case = (ImageView) findViewById(R.id.genereatCase);
		previous_history = (ImageView) findViewById(R.id.prev_history);
		duplicate_print = (ImageView) findViewById(R.id.duplicate_print);
		reports = (ImageView) findViewById(R.id.reports);
		download_masters = (ImageView) findViewById(R.id.download_masters);
		settings = (ImageView) findViewById(R.id.settings);

		capture_images = (ImageView) findViewById(R.id.capture_images);
		capture_videos = (ImageView) findViewById(R.id.capture_videos);

		logout = (ImageView) findViewById(R.id.logout);

		psname = (TextView) findViewById(R.id.psname);
		officerName = (TextView) findViewById(R.id.officername);

		SharedPreferences prefs = getSharedPreferences("loginValues",
				MODE_PRIVATE);
		String psName = prefs.getString("PS_NAME", "");
		String officer_Name = prefs.getString("PID_NAME", "");
		psname.setText("" + psName);
		officerName.setText("" + officer_Name);

		if (Login.SECURITY_CD.equals("N")) {

			TextView title = new TextView(this);
			title.setText("GHMC_enforcement");
			title.setBackgroundColor(Color.RED);
			title.setGravity(Gravity.CENTER);
			title.setTextColor(Color.WHITE);
			title.setTextSize(26);
			title.setTypeface(title.getTypeface(), Typeface.BOLD);
			title.setCompoundDrawablesWithIntrinsicBounds(R.drawable.dialog_logo, 0, R.drawable.dialog_logo, 0);
			title.setPadding(20, 0, 20, 0);
			title.setHeight(70);

			String otp_message = "\nPlease Update your Application...! \n";

			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Dashboard.this,
					AlertDialog.THEME_HOLO_LIGHT);
			alertDialogBuilder.setCustomTitle(title);
			alertDialogBuilder.setIcon(R.drawable.dialog_logo);
			alertDialogBuilder.setMessage(otp_message);
			alertDialogBuilder.setCancelable(false);
			alertDialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					if(isOnline()) {
						new Async_UpdateApk().execute();
					}else {
						showToast("Please Check Your Network Connection");
					}
					Login.SECURITY_CD = "Y";
				}
			});

			AlertDialog alertDialog = alertDialogBuilder.create();
			alertDialog.show();

			alertDialog.getWindow().getAttributes();

			TextView textView = (TextView) alertDialog.findViewById(android.R.id.message);
			textView.setTextSize(28);
			textView.setTypeface(textView.getTypeface(), Typeface.BOLD);
			textView.setGravity(Gravity.CENTER);

			Button btn = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
			btn.setTextSize(22);
			btn.setTextColor(Color.WHITE);
			btn.setTypeface(btn.getTypeface(), Typeface.BOLD);
			btn.setBackgroundColor(Color.RED);

		}


		generate_case.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				try {
					try {
						GPSTracker.locationManager = null;
						gps = new GPSTracker(Dashboard.this);
						// check if GPS enabled
						if (gps.getLocation() != null) {
							Preview.latitude = gps.getLatitude();
							Preview.longitude = gps.getLongitude();
						} else {
							gps.showSettingsAlert();
						}
					} catch (Exception e) {
						e.printStackTrace();
					}


					if ("0.0".equals(String.valueOf(Preview.latitude))
							&& "0.0".equals(String.valueOf(Preview.longitude))) {
						showToast("CHECK GPS SETTINGS");

					} else {
						db.open();
						String bt_query = "select * from " + DataBase.Bluetooth;
						String ps_query = "select * from " + DataBase.psName_table;
						String point_query = "select * from " + DataBase.pointName_table;


						Cursor bt_cursor = DataBase.db.rawQuery(bt_query, null);
						Cursor ps_cursor = DataBase.db.rawQuery(ps_query, null);
						Cursor point_cursor = DataBase.db.rawQuery(point_query, null);

						if ((bt_cursor.getCount() == 0)) {
							showToast("Configure Bluetooth Settings!");
						} else if ((ps_cursor.getCount() == 0)) {
							showToast("Configure Download Masters!");
						} else {
							if (isOnline()) {
								OtpStatus = "";
								OtpResponseDelayTime = "";
								Async_getOtpStatusNTime OtpStatusNTime = new Async_getOtpStatusNTime();
								if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
									Thread.currentThread().setPriority(Thread.NORM_PRIORITY);
									OtpStatusNTime.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
								} else {
									OtpStatusNTime.execute();
								}
							} else {
								showToast("Please Check your Network");
							}
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

		previous_history.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(isOnline()) {
					Intent ph = new Intent(Dashboard.this, Previous_History.class);
					startActivity(ph);
				}else {
					showToast("Please Check Your Network Connection");
				}
			}
		});

		capture_images.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				Intent images = new Intent(Dashboard.this, CaptureImages.class);
				startActivity(images);
			}
		});

		capture_videos.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent videos = new Intent(Dashboard.this, CaptureVideo.class);
				startActivity(videos);
			}
		});

		logout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				TextView title = new TextView(Dashboard.this);
				title.setText("GHMC Enforcement");
				title.setBackgroundColor(Color.RED);
				title.setGravity(Gravity.CENTER);
				title.setTextColor(Color.WHITE);
				title.setTextSize(26);
				title.setTypeface(title.getTypeface(), Typeface.BOLD);
				title.setCompoundDrawablesWithIntrinsicBounds(
						R.drawable.dialog_logo, 0, R.drawable.dialog_logo, 0);
				title.setPadding(20, 0, 20, 0);
				title.setHeight(70);

				String otp_message = "\n Are You Sure,\nDo You Want To Exit? \n";

				AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
						Dashboard.this, AlertDialog.THEME_HOLO_LIGHT);
				alertDialogBuilder.setCustomTitle(title);
				alertDialogBuilder.setIcon(R.drawable.dialog_logo);
				alertDialogBuilder.setMessage(otp_message);
				alertDialogBuilder.setCancelable(false);
				alertDialogBuilder.setPositiveButton("Ok",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
												int which) {
								// TODO Auto-generated method stub
								Intent intent = new Intent(
										getApplicationContext(), Login.class);
								intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK
										| Intent.FLAG_ACTIVITY_NEW_TASK);
								startActivity(intent);
								finish();
							}
						});

				alertDialogBuilder.setNegativeButton("Cancel",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
												int which) {
								// TODO Auto-generated method stub

							}
						});

				AlertDialog alertDialog = alertDialogBuilder.create();
				alertDialog.show();

				alertDialog.getWindow().getAttributes();

				TextView textView = (TextView) alertDialog
						.findViewById(android.R.id.message);
				textView.setTextSize(28);
				textView.setTypeface(textView.getTypeface(), Typeface.BOLD);
				textView.setGravity(Gravity.CENTER);

				Button btn = alertDialog
						.getButton(DialogInterface.BUTTON_POSITIVE);
				btn.setTextSize(22);
				btn.setTextColor(Color.WHITE);
				btn.setTypeface(btn.getTypeface(), Typeface.BOLD);
				btn.setBackgroundColor(Color.RED);

				Button btn2 = alertDialog
						.getButton(DialogInterface.BUTTON_NEGATIVE);
				btn2.setTextSize(22);
				btn2.setTextColor(Color.WHITE);
				btn2.setTypeface(btn2.getTypeface(), Typeface.BOLD);
				btn2.setBackgroundColor(Color.RED);
			}
		});

		duplicate_print.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(isOnline()) {
					Intent dp = new Intent(Dashboard.this, DuplicatPrint.class);
					startActivity(dp);
				}else
				{
					showToast("Please Check Your Network Connection");
				}
			}
		});

		reports.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(isOnline()) {
					Intent rp = new Intent(Dashboard.this, Reports.class);
					startActivity(rp);
				}else {
					showToast("Please Check Your Network");
				}
			}
		});

		download_masters.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(isOnline()) {
					new Async_DownloadMasters().execute();
				}else {
					showToast("Please Check Your Network");
				}

			}
		});

		settings.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(isOnline()) {
					Intent setting = new Intent(Dashboard.this, Settings.class);
					startActivity(setting);
				}else {
					showToast("Please Check Your Network");
				}
			}
		});

	}

	public Boolean isOnline() {
		ConnectivityManager conManager = (ConnectivityManager) this
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo nwInfo = conManager.getActiveNetworkInfo();
		return nwInfo != null;
	}



	class Async_DownloadMasters extends AsyncTask<Void, Void, String> {

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			showDialog(PROGRESS_DIALOG);
		}

		@Override
		protected String doInBackground(Void... arg0) {
			// TODO Auto-generated method stub
			ServiceHelper.getgetSubSectionsBy_Sectioncode(""+ section_code_send, ""+ Login.OFFICER_TYPE);
			ServiceHelper.getPSNames(""+ Login.uintCode);


			/*********** SECTIONS RESPONSE *************/
			if (ServiceHelper.sub_sectionmasters_resp != null && !"0".equals(ServiceHelper.sub_sectionmasters_resp)) {
				// 15:403:1000@17:406:550@18:407 (1):50@20:413 (1):10000
				temp_sections_master2 = new String[ServiceHelper.temp_sections_master.length][];
				for (int i = 0; i < ServiceHelper.temp_sections_master.length; i++) {
					temp_sections_master2[i] = ServiceHelper.temp_sections_master[i].split(":");
				}

				temp_section_codes_names_arr = new ArrayList<String>();
				temp_section_names_arr = new ArrayList<String>();
				temp_section_fine_arr = new ArrayList<String>();
				temp_section_codes_names_arr.clear();
				temp_section_names_arr.clear();
				temp_section_fine_arr.clear();

				try {
					db.open();
					SQLiteDatabase db2 = openOrCreateDatabase(DataBase.DATABASE_NAME, MODE_PRIVATE, null);
					// db.execSQL("delete from " + DataBase.USER_TABLE);
					db2.execSQL("DROP TABLE IF EXISTS " + DataBase.temp_sections_table);
					db2.execSQL(DataBase.TEmp_SectionTableCreation);

					for (int j = 0; j < temp_sections_master2.length; j++) {
						temp_section_codes_names_arr.add(temp_sections_master2[j][0]);
						temp_section_names_arr.add(temp_sections_master2[j][1]);
						temp_section_fine_arr.add(temp_sections_master2[j][2]);
						temp_section_fine_arr.add(temp_sections_master2[j][3]);

						db.insertTempSectionDetails(""+ temp_sections_master2[j][0], ""+ temp_sections_master2[j][1], ""
								+ temp_sections_master2[j][2], ""+ temp_sections_master2[j][3]);

					}
					db2.close();
				} catch (Exception e) {
					// TODO: handle exception
					if (db != null) {
						db.close();
					}
				}
			}

			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);

			/*********** PS NAMES RESPONSE *************/
			if (!"0".equals(ServiceHelper.sub_sectionmasters_resp)) {
				psname_name_code_arr = new String[ServiceHelper.psNames_master.length][2];
				for (int i = 0; i < ServiceHelper.psNames_master.length; i++) {
					psname_name_code_arr[i] = ServiceHelper.psNames_master[i].split(":");
				}
				try {
					SQLiteDatabase sdb = openOrCreateDatabase(DataBase.DATABASE_NAME, MODE_PRIVATE, null);
					// db.execSQL("delete from " + DataBase.USER_TABLE);
					sdb.execSQL("DROP TABLE IF EXISTS " + DataBase.Id_details_table);
					sdb.execSQL(DataBase.idDetailsCreation);
					sdb.close();

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

						db.insertPsNameDetails("" + psname_name_code_arr[j][0], "" + psname_name_code_arr[j][1]);
					}
					db2.close();
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
					if (db != null) {
						db.close();
					}
				}
				removeDialog(PROGRESS_DIALOG);
				showToast("Successfully Downloaded Master's!!!!");
			}else
			{
				removeDialog(PROGRESS_DIALOG);
				showToast("Please Check Network Try Again");
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
				psname_name_code_arr[i] = ServiceHelper.psNames_master[i]
						.split(":");

			}

			try {
				db.open();
				ps_codes_fr_names_arr = new ArrayList<String>();
				ps_names_arr = new ArrayList<String>();
				ps_codes_fr_names_arr.clear();
				ps_names_arr.clear();

				SQLiteDatabase db2 = openOrCreateDatabase(
						DataBase.DATABASE_NAME, MODE_PRIVATE, null);
				// db.execSQL("delete from " + DataBase.USER_TABLE);
				db2.execSQL("DROP TABLE IF EXISTS " + DataBase.psName_table);
				db2.execSQL(DataBase.psNamesCreation);

				for (int j = 0; j < psname_name_code_arr.length; j++) {
					ps_codes_fr_names_arr.add(psname_name_code_arr[j][0]);
					ps_names_arr.add(psname_name_code_arr[j][1]);

					db.insertPsNameDetails("" + psname_name_code_arr[j][0], ""
							+ psname_name_code_arr[j][1]);

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
		Log.i("showToast called", "start");
		Toast toast = Toast.makeText(getApplicationContext(), "" + msg,
				Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.CENTER, 0, 0);
		View toastView = toast.getView();

		ViewGroup group = (ViewGroup) toast.getView();
		TextView messageTextView = (TextView) group.getChildAt(0);
		messageTextView.setTextSize(24);

		toastView.setBackgroundResource(R.drawable.toast_background);
		toast.show();
		Log.i("showToast called", "end");
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
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

		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				Dashboard.this, AlertDialog.THEME_HOLO_LIGHT);
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

		TextView textView = (TextView) alertDialog
				.findViewById(android.R.id.message);
		textView.setTextSize(28);
		textView.setTypeface(textView.getTypeface(), Typeface.BOLD);
		textView.setGravity(Gravity.CENTER);

		Button btn = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
		btn.setTextSize(22);
		btn.setTextColor(Color.WHITE);
		btn.setTypeface(btn.getTypeface(), Typeface.BOLD);
		btn.setBackgroundColor(Color.RED);

		Button btn2 = alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE);
		btn2.setTextSize(22);
		btn2.setTextColor(Color.WHITE);
		btn2.setTypeface(btn2.getTypeface(), Typeface.BOLD);
		btn2.setBackgroundColor(Color.RED);

	}

	class Async_UpdateApk extends AsyncTask<Void, Void, String> {

		@SuppressWarnings("deprecation")
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			showDialog(PROGRESS_DIALOG);

		}

		@Override
		protected String doInBackground(Void... arg0) {
			// TODO Auto-generated method stub

			FTPClient ftpClient = new FTPClient();

			try {

				if (null != Login.URL && Login.URL.equals("https://www.echallan.org/GHMCWebService")) {
					server = IP_settings.open_ftp_fix;
				}else{
					server = IP_settings.ftp_fix;
				}

				ftpClient.connect(server, port);
				ftpClient.login(username, password);
				ftpClient.enterLocalPassiveMode();
				ftpClient.setBufferSize(1024 * 1024);
				ftpClient.setFileType(FTP.BINARY_FILE_TYPE);

				// ftp://192.168.11.9:99/23/TabAPK/Version-1.5.1.txt
				File downloadFile1 = new File("/sdcard/Download/GHMC.apk");
				// String remoteFile1 = "/23/TabAPK" + "/" + version;
				String remoteFile1 = "/23/TabAPK" + "/GHMC.apk";

				OutputStream outputStream = new BufferedOutputStream(
						new FileOutputStream(downloadFile1));
				boolean success = ftpClient.retrieveFile(remoteFile1,
						outputStream);

				FileOutputStream fileOutput = new FileOutputStream(
						downloadFile1);
				InputStream inputStream = ftpClient
						.retrieveFileStream(remoteFile1);

				if (inputStream == null || ftpClient.getReplyCode() == 550) {
					// it means that file doesn't exist.
					fileOutput.close();
					outputStream.close();

					runOnUiThread(new Runnable() {

						@SuppressWarnings("deprecation")
						@Override
						public void run() {
							// TODO Auto-generated method stub
							removeDialog(PROGRESS_DIALOG);

							// showToast("your is Upto Date");
							TextView title = new TextView(Dashboard.this);
							title.setText("GHMC enforcement");
							title.setBackgroundColor(Color.RED);
							title.setGravity(Gravity.CENTER);
							title.setTextColor(Color.WHITE);
							title.setTextSize(26);
							title.setTypeface(title.getTypeface(),
									Typeface.BOLD);
							title.setCompoundDrawablesWithIntrinsicBounds(
									R.drawable.dialog_logo, 0,
									R.drawable.dialog_logo, 0);
							title.setPadding(20, 0, 20, 0);
							title.setHeight(70);

							String otp_message = "\n Your Application is Upto Date \n No Need to Update \n";

							AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
									Dashboard.this, AlertDialog.THEME_HOLO_LIGHT);
							alertDialogBuilder.setCustomTitle(title);
							alertDialogBuilder.setIcon(R.drawable.dialog_logo);
							alertDialogBuilder.setMessage(otp_message);
							alertDialogBuilder.setCancelable(false);
							alertDialogBuilder.setPositiveButton("Ok",
									new DialogInterface.OnClickListener() {

										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
											// TODO Auto-generated method stub

										}
									});

							AlertDialog alertDialog = alertDialogBuilder
									.create();
							alertDialog.show();

							alertDialog.getWindow().getAttributes();

							TextView textView = (TextView) alertDialog
									.findViewById(android.R.id.message);
							textView.setTextSize(28);
							textView.setTypeface(textView.getTypeface(),
									Typeface.BOLD);
							textView.setGravity(Gravity.CENTER);

							Button btn1 = alertDialog
									.getButton(DialogInterface.BUTTON_POSITIVE);
							btn1.setTextSize(22);
							btn1.setTextColor(Color.WHITE);
							btn1.setTypeface(btn1.getTypeface(), Typeface.BOLD);
							btn1.setBackgroundColor(Color.RED);

						}
					});
				}

				else {


					totalSize = remoteFile1.length();

					runOnUiThread(new Runnable() {
						@SuppressWarnings("deprecation")
						public void run() {
							removeDialog(PROGRESS_DIALOG);
							showProgress(server);
							progress.setMax(totalSize);
						}
					});

					// create a buffer...
					byte[] buffer = new byte[1024];
					int bufferLength = 0;

					while ((bufferLength = inputStream.read(buffer)) > 0) {
						fileOutput.write(buffer, 0, bufferLength);
						downloadedSize += bufferLength;
						// update the progressbar //
						runOnUiThread(new Runnable() {
							public void run() {
								progress.setProgress(downloadedSize);
								float per = ((float) downloadedSize / totalSize) * 100;
								cur_val.setText((int) per / 1500000 + "%");
							}
						});
					}

					// close the output stream when complete //
					fileOutput.close();
					outputStream.close();

					if (success) {
						ftpClient.logout();
						ftpClient.disconnect();



						finish();

						if (Build.VERSION.SDK_INT <= 23) {

							Intent intent = new Intent(Intent.ACTION_VIEW);
							intent.setDataAndType(
									Uri.fromFile(new File(
											Environment
													.getExternalStorageDirectory()
													+ "/Download/"
													+ "GHMC.apk")),
									"application/vnd.android.package-archive");
							intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
							startActivity(intent);
						}

						else {

							Uri apkUri = FileProvider.getUriForFile(Dashboard.this, BuildConfig.APPLICATION_ID +
									".provider", new File(
									Environment
											.getExternalStorageDirectory()
											+ "/Download/"
											+ "GHMC.apk"));
							Intent intent = new Intent(Intent.ACTION_INSTALL_PACKAGE);
							intent.setData(apkUri);
							intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
							startActivity(intent);
						}

					}
				}

			} catch (SocketException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			// removeDialog(PROGRESS_DIALOG);
		}

		@Override
		protected void onProgressUpdate(Void... values) {
			super.onProgressUpdate(values);
		}

	}
	@SuppressWarnings("deprecation")
	private void showProgress(String server) {
		// TODO Auto-generated method stub
		dialog = new Dialog(Dashboard.this);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.myprogressdialog);
		dialog.setTitle("Download Progress");
		dialog.setCancelable(false);

		TextView text = (TextView) dialog.findViewById(R.id.tv1);
		text.setText("Downloading file ... ");
		cur_val = (TextView) dialog.findViewById(R.id.cur_pg_tv);
		cur_val.setText("It may Take Few Minutes.....");
		dialog.show();

		progress = (ProgressBar) dialog.findViewById(R.id.progress_bar);
		progress.setProgress(0);// initially progress is 0
		progress.setMax(100);
		progress.setIndeterminate(true);
		progress.setProgressDrawable(getResources().getDrawable(
				R.drawable.green_progress));
	}
	public class Async_getOtpStatusNTime extends AsyncTask<Void, Void, String> {
		@Override
		protected String doInBackground(Void... params) {



			String rtaApproverResponse = ServiceHelper.getOtpStatusNTime(String.valueOf(UnitCode));

			return rtaApproverResponse;
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			showDialog(PROGRESS_DIALOG);
		}

		@Override
		protected void onPostExecute(String result) {

			removeDialog(PROGRESS_DIALOG);

			if (result != null && !result.equalsIgnoreCase("") && !result.equalsIgnoreCase("NA|NA")) {


				try {

					otp_Master = new String[0];

					otp_Master = result.split("\\|");

					OtpStatus=otp_Master[0].toString()!= null ? otp_Master[0].toString().trim() : "N";
					OtpResponseDelayTime=otp_Master[1].toString()!= null ? otp_Master[1].toString().trim(): "0";

					Intent generate = new Intent(Dashboard.this, GenerateCase.class);
					startActivity(generate);


				} catch (Exception e) {
					e.printStackTrace();
					OtpStatus="N";
					OtpResponseDelayTime="0";

				}

			}

		}

	}


}
