package com.mtpv.ghmcepettycase;

import java.util.ArrayList;
import java.util.HashMap;

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
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mtpv.ghmcenforcement.R;
import com.mtpv.services.DataBase;
import com.mtpv.services.ServiceHelper;

public class Dashboard extends Activity {

	ImageView generate_case, previous_history, duplicate_print, reports,
			download_masters, settings, logout, capture_images, capture_videos;
	TextView psname, officerName;
	final int PROGRESS_DIALOG = 1;
	final int WHEELER_CODE = 2;

	/* FOR PS NAMES */
	ArrayList<String> ps_codes_fr_names_arr;
	ArrayList<String> ps_names_arr;
	String[][] psname_name_code_arr;

	private static String NAMESPACE = "http://service.mother.com";
	public static String methodIDMaster = "getIDProofMaster";
	// public static String methodAuth = "authenticateUser";
	public static String SOAP_ACTION = NAMESPACE + methodIDMaster;

	public static String Id_proof_result = "";
	public static String[] resp_array;

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
		Log.i("psName :::", "" + psName);

		String officer_Name = prefs.getString("PID_NAME", "");
		Log.i("officer_Name :::", "" + officer_Name);

		psname.setText("" + psName);
		officerName.setText("" + officer_Name);

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
					Log.i("Preview.latitude", "" + Preview.latitude);
					Log.i("Preview.longitude", "" + Preview.longitude);

					if ("0.0".equals(String.valueOf(Preview.latitude))
							&& "0.0".equals(String.valueOf(Preview.longitude))) {
						Log.i("calling toast", "called");
						showToast("CHECK GPS SETTINGS");
						
					} else {
						db.open();
						String bt_query = "select * from " + DataBase.Bluetooth;
						String ps_query = "select * from " + DataBase.psName_table;
						String point_query = "select * from " + DataBase.pointName_table;

						Log.i("bt_query ::::", "" + bt_query);
						Log.i("ps_query ::::", "" + ps_query);
						Log.i("point_query ::::", "" + point_query);
						
						Cursor bt_cursor = DataBase.db.rawQuery(bt_query, null);
						Cursor ps_cursor = DataBase.db.rawQuery(ps_query, null);
						Cursor point_cursor = DataBase.db.rawQuery(point_query, null);

						if ((bt_cursor.getCount() == 0)) {
							showToast("Configure Bluetooth Settings!");
						} else if ((ps_cursor.getCount() == 0)) {
							showToast("Configure Download Masters!");
						} else {
							if (isOnline()) {
								Intent generate = new Intent(Dashboard.this, GenerateCase.class);
								startActivity(generate);
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
				Intent ph = new Intent(Dashboard.this, Previous_History.class);
				startActivity(ph);
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
				title.setText("Hyderabad E-Ticket");
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
				Intent dp = new Intent(Dashboard.this, DuplicatPrint.class);
				startActivity(dp);
			}
		});

		reports.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent rp = new Intent(Dashboard.this, Reports.class);
				startActivity(rp);
			}
		});

		download_masters.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				new Async_DownloadMasters().execute();
			}
		});

		settings.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent setting = new Intent(Dashboard.this, Settings.class);
				startActivity(setting);
			}
		});

	}

	public Boolean isOnline() {
		ConnectivityManager conManager = (ConnectivityManager) this
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo nwInfo = conManager.getActiveNetworkInfo();
		return nwInfo != null;
	}

	// Async_SectionsMasters
	class Async_SectionsMasters extends AsyncTask<Void, Void, String> {

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			showDialog(PROGRESS_DIALOG);
		}

		@Override
		protected String doInBackground(Void... arg0) {
			// TODO Auto-generated method stub
			SharedPreferences prefs = getSharedPreferences("ghmcValues",
					MODE_PRIVATE);
			String unitCd = prefs.getString("UNIT_CODE", "");

			ServiceHelper.getSectionMasters("" + unitCd);
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			removeDialog(PROGRESS_DIALOG);
			if (ServiceHelper.sectionmasters_resp != null) {

				if ((!ServiceHelper.sectionmasters_resp.equals("0"))
						&& (ServiceHelper.section_details_master.length > 0)) {

					section_code_name = new String[ServiceHelper.section_details_master.length][2];
					section_name_arr = new ArrayList<String>();

					SQLiteDatabase db2 = openOrCreateDatabase(
							DataBase.DATABASE_NAME, MODE_PRIVATE, null);
					// db.execSQL("delete from " + DataBase.USER_TABLE);
					db2.execSQL("DROP TABLE IF EXISTS "
							+ DataBase.sections_table);
					db2.execSQL(DataBase.SectionTableCreation);

					System.out
							.println("*********************OFFICER TABLE Insertion Successfully **********************************");

					db.open();
					for (int i = 0; i < ServiceHelper.section_details_master.length; i++) {
						section_code_name[i] = (ServiceHelper.section_details_master[i]
								.split(":"));
						section_name_arr.add(section_code_name[i][1]);
						db.insertSectionDetails("" + section_code_name[i][0],
								"" + section_code_name[i][1]);

						Log.i("DATA :::::", "" + section_code_name[i][0] + ", "
								+ section_code_name[i][1]);
					}
					db.close();
					db2.close();

					Intent generate = new Intent(Dashboard.this,
							GenerateCase.class);
					startActivity(generate);

				} else {
					showToast("Try again");
					section_code_name = new String[0][0];
					section_name_arr.clear();
				}
			} else {
				showToast("Try again");
				section_code_name = new String[0][0];
				section_name_arr.clear();
			}

		}
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
			/*
			 * try { // INSERT ID PROOF DETAILS START SoapObject request = new
			 * SoapObject(NAMESPACE, "getIDProofMaster");
			 * SoapSerializationEnvelope envelope = new
			 * SoapSerializationEnvelope(SoapEnvelope.VER11); envelope.dotNet =
			 * true; envelope.setOutputSoapObject(request); Log.i("request", ""
			 * + request); //Log.i("WebService.SOAP_ADDRESS ::::::::::::::", ""
			 * + WebService.SOAP_ADDRESS); HttpTransportSE androidHttpTransport
			 * = new HttpTransportSE(Login.URL); Log.i("androidHttpTransport",
			 * "" + androidHttpTransport);
			 * androidHttpTransport.call(SOAP_ACTION, envelope); Object result =
			 * envelope.getResponse(); Id_proof_result = result.toString();
			 * Log.i("**  ID DETAILS  response***", "" + Id_proof_result);
			 * 
			 * 
			 * if ("NA".equals(Id_proof_result)
			 * ||"anyType{}".equals(Id_proof_result) || Id_proof_result == null)
			 * { Id_proof_result = null ; } else { //resp_array = new String[0];
			 * Id_proof_result = result.toString(); Dashboard.resp_array =
			 * Id_proof_result.split("@");
			 * Log.i("enter in to do in background id detils size::::::::::",
			 * ""+Dashboard.resp_array.length); }
			 * 
			 * // INSERT ID PROOF DETAILS END } catch (Exception e) { // TODO:
			 * handle exception e.printStackTrace(); }
			 */

			/*********** SECTIONS RESPONSE *************/
			if (ServiceHelper.sub_sectionmasters_resp != null) {
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
						Log.i("insertTempSectionDetails[i] :::", ""+ temp_sections_master2[j][0] + ":"
								+ temp_sections_master2[j][1] + ":"+ temp_sections_master2[j][2]);
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

			/*	
		*//************** for put in to a list start for show *************/
			/*
			 * idDetails_arr = new ArrayList<String>();
			 * 
			 * idDetails_arr.clear(); for (int i = 0; i < resp_array.length;
			 * i++) { String IdProofs = resp_array[i].split("\\:")[1];
			 * 
			 * Log.i("allIdproofs", IdProofs); idDetails_arr.add(IdProofs); }
			 *//************** for put in to a list end for show *************/
			/*
			 * 
			 * 
			 * if (Dashboard.Id_proof_result.trim()!=null) {
			 * 
			 * Log.i("enter in to post execute id detils size::::::::::",
			 * ""+Dashboard.resp_array.length); Dashboard.resp_array =
			 * Dashboard.Id_proof_result.split("\\@"); //SQLiteDatabase sdb =
			 * null ;
			 * 
			 * SQLiteDatabase db2 =
			 * openOrCreateDatabase(DataBase.DATABASE_NAME,MODE_PRIVATE, null);
			 * //db.execSQL("delete from " + DataBase.USER_TABLE);
			 * db2.execSQL("DROP TABLE IF EXISTS " + DataBase.Id_details_table);
			 * db2.execSQL(DataBase.idDetailsCreation);
			 * 
			 * for (String idProofDet:Dashboard.resp_array) { String
			 * []idDet=idProofDet.split("\\:");
			 * 
			 * try { db.open(); db.insertIdProofDetails(""+idDet[0],
			 * ""+idDet[1]);
			 * Log.i("All Points Values :::",""+idDet[0]+"\n "+idDet[1]);
			 * idDetailsMap.put(""+idDet[1].trim(), ""+idDet[0].trim());
			 * Log.i("All Points Values :::",""+idDet[0]+"\n "+idDet[1]); }
			 * catch (Exception e) { // TODO: handle exception if (db!=null) {
			 * db.close(); } if (db2!=null) { db2.close(); }
			 * 
			 * }
			 * 
			 * } }
			 */

			/*********** PS NAMES RESPONSE *************/
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

					db.insertPsNameDetails("" + psname_name_code_arr[j][0], ""+ psname_name_code_arr[j][1]);
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
			removeDialog(PROGRESS_DIALOG);
			showToast("Successfully Downloaded Master's!!!!");
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
					Log.i("psname_name_code_arr[i] :::", ""
							+ psname_name_code_arr[j][0]
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
}
