package com.mtpv.ghmcepettycase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.text.InputFilter;
import android.text.InputType;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TableRow.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ZoomControls;

import com.analogics.thermalAPI.Bluetooth_Printer_3inch_ThermalAPI;
import com.analogics.thermalprinter.AnalogicsThermalPrinter;
import com.mtpv.ghmcenforcement.R;
import com.mtpv.services.DataBase;
import com.mtpv.services.ServiceHelper;

public class Previous_History extends Activity {

	public static String Id_proof_result = "";

	private static String NAMESPACE = "http://service.mother.com";
	public static String methodIDMaster = "getIDProofMaster";
	// public static String methodAuth = "authenticateUser";
	public static String SOAP_ACTION = NAMESPACE + methodIDMaster;

	public static String[] resp_array;
	public static String selectedId_Code = null;
	public static String Selected_id_prrof;

	public static String CHALLAN_NO, AllDetails;

	DataBase db;

	public static HashMap<String, String> idDetailsMap = new HashMap<String, String>();

	ArrayList<String> idDetails_arr;

	ArrayList<String> id_history_resp_arr;

	ArrayList<String> firm_history_resp_arr;
	ArrayList<String> tin_history_resp_arr;

	final int PROGRESS_DIALOG = 1;

	RadioButton prevH_id_based, firm_based, prevH_GHMCTIN_based;
	// Spinner prevH_id_options_Phistry ;
	Button prevH_id_options;
	public static EditText et_id_text_prev_hist, et_id_firm_prev_hist;

	ImageView firm_history_details, history_details, back_btn;

	LinearLayout prevH_id_layout, firm_prevH_id_layout;

	private ListView lv;

	ArrayList<HashMap<String, String>> prv_det_id_list;

	final int ID_DETAILS_DIALOG = 22;
	/*************** table layout ******************/

	public static TableLayout table_layout;
	LinearLayout prevoius_history;
	public static ScrollView scroll;
	ZoomControls zoom;

	public int rowscount = 0, columnscount = 0, rows = 0, columns = 0;

	TableRow tr;

	public static String eticketNo = null;

	public static String PRINT_DATA_ID, PRINT_DATA_FIRM, PRINT_DATA_TIN;

	public static boolean previoushistoryFLG = false;

	final AnalogicsThermalPrinter actual_printer = new AnalogicsThermalPrinter();
	final Bluetooth_Printer_3inch_ThermalAPI bth_printer = new Bluetooth_Printer_3inch_ThermalAPI();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_previous__history);

		prevH_id_based = (RadioButton) findViewById(R.id.prevH_id_based);
		firm_based = (RadioButton) findViewById(R.id.firm_based);
		prevH_GHMCTIN_based = (RadioButton) findViewById(R.id.prevH_GHMCTIN_based);

		// prevH_id_options_Phistry =
		// (Spinner)findViewById(R.id.prevH_id_options_Phistry);
		prevH_id_options = (Button) findViewById(R.id.prevH_id_options);
		et_id_text_prev_hist = (EditText) findViewById(R.id.id_text_prev_hist);
		et_id_firm_prev_hist = (EditText) findViewById(R.id.id_firm_prev_hist);

		firm_history_details = (ImageView) findViewById(R.id.firm_history_details);
		history_details = (ImageView) findViewById(R.id.history_details);
		back_btn = (ImageView) findViewById(R.id.back_btn);

		zoom = (ZoomControls) findViewById(R.id.zoomControls1);
		zoom.setVisibility(View.GONE);

		db = new DataBase(Previous_History.this);
		retreiveDBValues();

		table_layout = (TableLayout) findViewById(R.id.tableLayout);
		prevoius_history = (LinearLayout) findViewById(R.id.prevoius_history);
		prevoius_history.setVisibility(View.GONE);
		// Tv = (TextView)findViewById(R.id.printTv);

		ArrayList<String> prv_det_id_list;

		// lv = (ListView) findViewById(R.id.list);
		// Tv.setText("");

		firm_prevH_id_layout = (LinearLayout) findViewById(R.id.firm_prevH_id_layout);
		prevH_id_layout = (LinearLayout) findViewById(R.id.prevH_id_layout);
		firm_prevH_id_layout.setVisibility(View.GONE);

		prevH_id_based.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// finish();
				idDetails_arr = null;
				prevH_id_options.setText("Select ID Proof");
				et_id_text_prev_hist.setHint("ENTER ID PROOF NO");
				firm_prevH_id_layout.setVisibility(View.GONE);
				prevH_id_layout.setVisibility(View.VISIBLE);
				et_id_text_prev_hist.setText("");
				prevoius_history.setVisibility(View.GONE);
			}
		});

		firm_based.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// finish();
				idDetails_arr = null;
				et_id_firm_prev_hist.setText("");
				et_id_firm_prev_hist.setHint("Enter SHOP NAME");
				firm_prevH_id_layout.setVisibility(View.VISIBLE);
				prevH_id_layout.setVisibility(View.GONE);
				prevoius_history.setVisibility(View.GONE);
			}
		});

		prevH_GHMCTIN_based.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				idDetails_arr = null;
				et_id_firm_prev_hist.setText("");
				et_id_firm_prev_hist.setHint("Enter TIN NO");
				firm_prevH_id_layout.setVisibility(View.VISIBLE);
				prevH_id_layout.setVisibility(View.GONE);
				prevoius_history.setVisibility(View.GONE);

			}
		});

		idDetails_arr = new ArrayList<String>();

		prevH_id_options.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				et_id_text_prev_hist.setText("");
				prevoius_history.setVisibility(View.GONE);
				new Async_getIDS().execute();
			}
		});

		history_details.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				VerhoeffCheckDigit ver = new VerhoeffCheckDigit();
				Selected_id_prrof = "" + prevH_id_options.getText().toString();

				/********************************* AADHAAR VALIDATION start ************************/
				if (Selected_id_prrof.toString().equals("AADHAAR")) {
					Log.i("Selected proof", "" + Selected_id_prrof);

					et_id_text_prev_hist.setHint("PLEASE ENTER AADHAAR NO");
					et_id_text_prev_hist.setInputType(InputType.TYPE_CLASS_NUMBER);
					int maxLength = 12;
					et_id_text_prev_hist.setFilters(new InputFilter[] { new InputFilter.LengthFilter(maxLength) });
					if (et_id_text_prev_hist.getText().toString().trim().equals("")) {
						et_id_text_prev_hist.setError(Html.fromHtml("<font color='black'>Please Enter Valid Aadhaar</font>"));
						et_id_text_prev_hist.requestFocus();
					} else if (et_id_text_prev_hist.getText().toString().trim().length() > 1
							&& et_id_text_prev_hist.getText().toString().trim().length() != 12) {
						et_id_text_prev_hist.setError(Html.fromHtml("<font color='black'>Please Enter 12 digit Aadhaar</font>"));
						et_id_text_prev_hist.requestFocus();
					} else if (et_id_text_prev_hist.getText().toString().trim().length() > 1
							&& et_id_text_prev_hist.getText().toString().trim().length() == 12
							&& !ver.isValid(et_id_text_prev_hist.getText().toString().trim())) {
						et_id_text_prev_hist.setError(Html.fromHtml("<font color='black'>Please Enter Valid Aadhaar</font>"));
						et_id_text_prev_hist.requestFocus();
					} else if (et_id_text_prev_hist.getText().toString().trim().length() > 1
							&& et_id_text_prev_hist.getText().toString().trim().length() == 12
							&& ver.isValid(et_id_text_prev_hist.getText().toString().trim())) {
						if (isNetworkAvailable()) {
							new Asyncprevoius_history_ID().execute();
						} else {
							showToast("Please Enable Data Connection!!");
						}
					}
				}
				/********************************* AADHAAR VALIDATION end ************************/

				
				/********************************* DL VALIDATION start ************************/
				else if (Selected_id_prrof.toString().equals("DRIVING LICENCE")) {
					et_id_text_prev_hist.setHint("PLEASE ENTER DL NO");
					et_id_text_prev_hist.setInputType(InputType.TYPE_CLASS_TEXT);
					int maxLength = 20;
					et_id_text_prev_hist.setFilters(new InputFilter[] { new InputFilter.LengthFilter(maxLength) });

					if (et_id_text_prev_hist.getText().toString().trim().equals("")) {
						et_id_text_prev_hist.setError(Html.fromHtml("<font color='black'>Please Enter Valid DL</font>"));
						et_id_text_prev_hist.requestFocus();
					} else if (et_id_text_prev_hist.getText().toString().trim().length() > 1) {

						if (isNetworkAvailable()) {
							new Asyncprevoius_history_ID().execute();
						} else {
							showToast("Please Enable Data Connection!!");
						}
					}
				}
				/********************************* DL VALIDATION end ************************/
				
				
				
				/********************************* PAN VALIDATION start ************************/
				else if (Selected_id_prrof.toString().equals("PAN CARD")) {
					et_id_text_prev_hist.setHint("PLEASE ENTER PAN NO");
					et_id_text_prev_hist.setInputType(InputType.TYPE_CLASS_TEXT);
					int maxLength = 20;
					et_id_text_prev_hist.setFilters(new InputFilter[] { new InputFilter.LengthFilter(maxLength) });

					if (et_id_text_prev_hist.getText().toString().trim().equals("")) {
						et_id_text_prev_hist.setError(Html.fromHtml("<font color='black'>Please Enter Valid PAN NO</font>"));
						et_id_text_prev_hist.requestFocus();
					} else if (et_id_text_prev_hist.getText().toString().trim().length() > 1) {
						if (isNetworkAvailable()) {
							new Asyncprevoius_history_ID().execute();
						} else {
							showToast("Please Enable Data Connection!!");
						}
					}
				}
				/********************************* PAN VALIDATION end ************************/

				
				/********************************* PASSPORT VALIDATION start ************************/
				else if (Selected_id_prrof.toString().equals("PASSPORT")) {
					et_id_text_prev_hist.setHint("PLEASE ENTER PASSPORT NO");
					et_id_text_prev_hist.setInputType(InputType.TYPE_CLASS_TEXT);
					int maxLength = 20;
					et_id_text_prev_hist.setFilters(new InputFilter[] { new InputFilter.LengthFilter(maxLength) });

					if (et_id_text_prev_hist.getText().toString().trim().equals("")) {
						et_id_text_prev_hist.setError(Html.fromHtml("<font color='black'>Please Enter Valid PASSPORT NO</font>"));
						et_id_text_prev_hist.requestFocus();
					} else if (et_id_text_prev_hist.getText().toString().trim().length() > 2) {
						if (isNetworkAvailable()) {
							new Asyncprevoius_history_ID().execute();
						} else {
							showToast("Please Enable Data Connection!!");
						}
					}
				}
				/********************************* PASSPORT VALIDATION end ************************/
				
				
				/********************************* VOTER ID VALIDATION start ************************/
				else if (Selected_id_prrof.toString().equals("VOTER ID")) {
					et_id_text_prev_hist.setHint("PLEASE ENTER VOTER ID NO");
					et_id_text_prev_hist.setInputType(InputType.TYPE_CLASS_NUMBER);
					int maxLength = 20;
					et_id_text_prev_hist.setFilters(new InputFilter[] { new InputFilter.LengthFilter(maxLength) });

					if (et_id_text_prev_hist.getText().toString().trim().equals("")) {
						et_id_text_prev_hist.setError(Html.fromHtml("<font color='black'>Please Enter Valid VOTER ID NO</font>"));
						et_id_text_prev_hist.requestFocus();
					} else if (et_id_text_prev_hist.getText().toString().trim().length() > 2) {
						if (isNetworkAvailable()) {
							new Asyncprevoius_history_ID().execute();
						} else {
							showToast("Please Enable Data Connection!!");
						}
					}
				}
				/********************************* VOTER ID VALIDATION end ************************/
				
				
				/********************************* ELECTRICITY VALIDATION start ************************/
				else if (Selected_id_prrof.toString().equals("ELECTRICITY BILL")) {
					et_id_text_prev_hist.setHint("PLEASE ENTER ELECTRICITY BILL NO");
					et_id_text_prev_hist.setInputType(InputType.TYPE_CLASS_TEXT);
					int maxLength = 20;
					et_id_text_prev_hist.setFilters(new InputFilter[] { new InputFilter.LengthFilter(maxLength) });
					if (et_id_text_prev_hist.getText().toString().trim().equals("")) {
						et_id_text_prev_hist.setError(Html.fromHtml("<font color='black'>Please Enter Valid ELECTRICITY BILL NO</font>"));
						et_id_text_prev_hist.requestFocus();
					} else if (et_id_text_prev_hist.getText().toString().trim().length() > 2) {
						if (isNetworkAvailable()) {
							new Asyncprevoius_history_ID().execute();
						} else {
							showToast("Please Enable Data Connection!!");
						}
					}
				}
				/********************************* ELECTRICITY ID VALIDATION end ************************/
				
				
				/********************************* LPG VALIDATION start ************************/
				else if (Selected_id_prrof.toString().equals("LPG GAS BILL")) {
					et_id_text_prev_hist.setHint("PLEASE ENTER LPG GAS BILL NO");
					et_id_text_prev_hist.setInputType(InputType.TYPE_CLASS_TEXT);
					int maxLength = 20;
					et_id_text_prev_hist.setFilters(new InputFilter[] { new InputFilter.LengthFilter(maxLength) });
					if (et_id_text_prev_hist.getText().toString().trim().equals("")) {
						et_id_text_prev_hist.setError(Html.fromHtml("<font color='black'>Please Enter Valid ELECTRICITY BILL NO</font>"));
						et_id_text_prev_hist.requestFocus();
					} else if (et_id_text_prev_hist.getText().toString().trim().length() > 2) {
						if (isNetworkAvailable()) {
							new Asyncprevoius_history_ID().execute();
						} else {
							showToast("Please Enable Data Connection!!");
						}
					}
				}
				/********************************* ELECTRICITY ID VALIDATION end ************************/
				
				
				/********************************* WATER VALIDATION start ************************/
				else if (Selected_id_prrof.toString().equals("WATER BILL")) {
					et_id_text_prev_hist.setHint("PLEASE ENTER WATER BILL NO");
					et_id_text_prev_hist.setInputType(InputType.TYPE_CLASS_TEXT);
					int maxLength = 20;
					et_id_text_prev_hist.setFilters(new InputFilter[] { new InputFilter.LengthFilter(maxLength) });
					if (et_id_text_prev_hist.getText().toString().trim().equals("")) {
						et_id_text_prev_hist.setError(Html.fromHtml("<font color='black'>Please Enter Valid WATER BILL NO</font>"));
						et_id_text_prev_hist.requestFocus();
					} else if (et_id_text_prev_hist.getText().toString().trim().length() > 2) {
						if (isNetworkAvailable()) {
							new Asyncprevoius_history_ID().execute();
						} else {
							showToast("Please Enable Data Connection!!");
						}
					}
				}
				/********************************* WATER ID VALIDATION end ************************/
				
				
				/********************************* WATER VALIDATION start ************************/
				else if (Selected_id_prrof.toString().equals("BSNL PHONE BILL")) {
					et_id_text_prev_hist.setHint("PLEASE ENTER CONTACT NO");
					et_id_text_prev_hist.setInputType(InputType.TYPE_CLASS_PHONE);
					int maxLength = 11;
					et_id_text_prev_hist.setFilters(new InputFilter[] { new InputFilter.LengthFilter(maxLength) });
					String tempContactNumber = et_id_text_prev_hist.getText().toString().trim();
					if (et_id_text_prev_hist.getText().toString().trim().equals("")) {
						et_id_text_prev_hist.setError(Html.fromHtml("<font color='black'>Please Enter Valid CONTACT NO</font>"));
						et_id_text_prev_hist.requestFocus();
					} else if (et_id_text_prev_hist.getText().toString().length() > 1
							&& et_id_text_prev_hist.getText().toString().length() < 10) {
						et_id_text_prev_hist.setError(Html.fromHtml("<font color='black'>Please Enter Valid 10 digit CONTACT NO</font>"));
						et_id_text_prev_hist.requestFocus();
					} else if ((tempContactNumber.charAt(0) != '7')
							|| (tempContactNumber.charAt(0) != '8')
							|| (tempContactNumber.charAt(0) != '9')) {
						if (isNetworkAvailable()) {
							new Asyncprevoius_history_ID().execute();
						} else {
							showToast("Please Enable Data Connection!!");
						}
					}
				}
				/********************************* WATER ID VALIDATION end ************************/
				
				
				/********************************* TRADE LICENSE VALIDATION start ************************/
				else if (Selected_id_prrof.toString().equals("TRADE LICENSE NO")) {
					et_id_text_prev_hist.setHint("PLEASE ENTER TRADE LICENSE NO");
					if (et_id_text_prev_hist.getText().toString().trim().equals("")) {
						et_id_text_prev_hist.setError(Html.fromHtml("<font color='black'>Please Enter Valid TRADE LICENSE NO</font>"));
						et_id_text_prev_hist.requestFocus();
					} else if (et_id_text_prev_hist.getText().toString().trim().length() > 2) {
						if (isNetworkAvailable()) {
							new Asyncprevoius_history_ID().execute();
						} else {
							showToast("Please Enable Data Connection!!");
						}
					}
				}
				/********************************* TRADE LICENSE ID VALIDATION end ************************/
			}
		});

		firm_history_details.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (firm_based.isChecked() == true) {
					new Asyncprevoius_history_firm().execute();
				}
				if (prevH_GHMCTIN_based.isChecked() == true) {
					new Asyncprevoius_history_GHMCTIN().execute();
				}
			}
		});

		back_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(Previous_History.this, Dashboard.class);
				startActivity(i);
			}
		});

	}

	private void retreiveDBValues() {
		String id_query = "select * from " + DataBase.Id_details_table;
		Log.i("query ::::", "" + id_query);

		Cursor loc_cursor = null;
		// selectedPs_code
		try {
			Log.i("query try block ::::", "Enterd");
			db.open();
			Log.i("query try block ::::", "Enterd 2");
			loc_cursor = DataBase.db.rawQuery(id_query, null);

			if (loc_cursor.moveToFirst()) {
				do {
					Log.i("DB ID PROOFS ::", "0 . " + loc_cursor.getString(0)
							+ "\n" + "1." + loc_cursor.getString(1) + "\n"
							+ "2." + loc_cursor.getString(2));
				} while (loc_cursor.moveToNext());
			}
		} catch (Exception e) {
			e.printStackTrace();
			if (db != null) {
				db.close();
			}
			loc_cursor.close();
		}
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

	private boolean isNetworkAvailable() {
		ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
		return activeNetworkInfo != null && activeNetworkInfo.isConnected();
	}

	public class Async_getIDS extends AsyncTask<Void, Void, String> {

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			showDialog(PROGRESS_DIALOG);
		}

		@Override
		protected String doInBackground(Void... params) {
			// TODO Auto-generated method stub
			try {
				// INSERT ID PROOF DETAILS START
				SoapObject request = new SoapObject(NAMESPACE, "getIDProofMaster");
				SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
						SoapEnvelope.VER11);
				envelope.dotNet = true;
				envelope.setOutputSoapObject(request);
				Log.i("request", "" + request);
				
				// Log.i("WebService.SOAP_ADDRESS ::::::::::::::", "" +
				// WebService.SOAP_ADDRESS);
				HttpTransportSE androidHttpTransport = new HttpTransportSE(Login.URL);
				//Log.i("androidHttpTransport", "" + androidHttpTransport);
				androidHttpTransport.call(SOAP_ACTION, envelope);
				
				Object result = envelope.getResponse();
				Id_proof_result = result.toString();
				Log.i("Id_proof_result :::", ""+ Id_proof_result);
				/*10:TRADE LICENSE NO@
				1:AADHAAR@
				2:DRIVING LICENCE@
				3:PAN CARD@
				4:PASSPORT@
				5:VOTER ID@
				6:ELECTRICITY BILL@
				7:LPG GAS BILL@
				8:WATER BILL@
				9:BSNL PHONE BILL*/
				
				if ("NA".equals(Id_proof_result)
						|| "anyType{}".equals(Id_proof_result)
						|| Id_proof_result == null) {
					Id_proof_result = null;
				} else {
					// resp_array = new String[0];
					Id_proof_result = result.toString();
					Previous_History.resp_array = Id_proof_result.split("@");
					Log.i("enter in to do in background id detils size::::::::::",
							"" + Previous_History.resp_array.length);
				}
				// INSERT ID PROOF DETAILS END
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			removeDialog(PROGRESS_DIALOG);

			/************** for put in to a list start for show *************/
			idDetails_arr = new ArrayList<String>();
			idDetails_arr.clear();
			for (int i = 0; i < resp_array.length; i++) {
				String IdProofs = Previous_History.resp_array[i].split("\\:")[1];
				Log.i("allIdproofs", IdProofs);
				idDetails_arr.add(IdProofs);
			}
			/************** for put in to a list end for show *************/
			
			if (Previous_History.Id_proof_result.trim() != null) {

				Log.i("enter in to post execute id detils size::::::::::", ""
						+ Previous_History.resp_array.length);
				Previous_History.resp_array = Previous_History.Id_proof_result.split("\\@");

				for (String idProofDet : Previous_History.resp_array) {
					String[] idDet = idProofDet.split("\\:");

					try {
						Log.i("All Points Values :::", "" + idDet[0] + "\n " + idDet[1]);
						idDetailsMap.put("" + idDet[1].trim(), "" + idDet[0].trim());
						Log.i("All Points Values :::", "" + idDet[0] + "\n " + idDet[1]);
					} catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
					}
				}
			}

			runOnUiThread(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					TextView title = new TextView(Previous_History.this);
					title.setText("Select ID Prrof:");
					title.setBackgroundColor(Color.parseColor("#007300"));
					title.setGravity(Gravity.CENTER);
					title.setTextColor(Color.WHITE);
					title.setTextSize(26);
					title.setTypeface(title.getTypeface(), Typeface.BOLD);
					title.setCompoundDrawablesWithIntrinsicBounds(
							R.drawable.traffic_small, 0, R.drawable.ghmc_small, 0);
					title.setPadding(10, 0, 10, 0);
					title.setHeight(70);

					AlertDialog.Builder builderSingle = new AlertDialog.Builder(Previous_History.this);
					builderSingle.setCustomTitle(title);
					builderSingle.setItems(idDetails_arr.toArray(new CharSequence[idDetails_arr.size()]),
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									// TODO Auto-generated method stub
									prevH_id_options.setText(idDetails_arr.get(which));
									Log.i("Id proof Selected :::", "" + idDetails_arr.get(which));
									String id_proof_code = null;
									Selected_id_prrof = "" + prevH_id_options.getText().toString();
									for (String id_code : idDetailsMap.keySet()) {
										if (id_code.trim().equals(Selected_id_prrof)) {
											id_proof_code = idDetailsMap.get(id_code);
											Log.i("id_proof_code ::", "" + id_proof_code);
											selectedId_Code = id_proof_code;
										}
										Log.i("id_proof_code ::", "" + id_proof_code);
									}
								}
							});
					builderSingle.show().getWindow().setLayout(500, 800);
				}
			});
		}
	}

	public class Asyncprevoius_history_ID extends AsyncTask<Void, Void, String> {
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			showDialog(PROGRESS_DIALOG);
		}

		@Override
		protected String doInBackground(Void... params) {
			// TODO Auto-generated method stub

			String idCode = "" + selectedId_Code;
			String idValue = et_id_text_prev_hist.getText().toString().trim();
			// String idCode,String idValue,String firmName
			Log.i("Seleceted id  code ::::", "" + selectedId_Code);
			Log.i("Seleceted id   ::::", "" + Selected_id_prrof);
			ServiceHelper.getPreviousHstry(idCode, idValue, "", "");
			id_history_resp_arr = new ArrayList<String>();
			id_history_resp_arr.clear();
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			removeDialog(PROGRESS_DIALOG);

			String response_id = ServiceHelper.prv_hisrty_resp;
			
			/*{"PREVIOUS_DETAILS":[{"CHALLAN_NO":"HYD003B170002046","OFFENCE_DT":"2017-02-17","SECTION_NAME":"39(b)"},
            {"CHALLAN_NO":"HYD003B170003101","OFFENCE_DT":"2017-02-22","SECTION_NAME":"39(b)"},
            {"CHALLAN_NO":"HYD003B170003119","OFFENCE_DT":"2017-03-03","SECTION_NAME":"39(b)"},
            {"CHALLAN_NO":"HYD003B170003173","OFFENCE_DT":"2017-03-07","SECTION_NAME":"39(b)"},
            {"CHALLAN_NO":"HYD003B170003256","OFFENCE_DT":"2017-03-10","SECTION_NAME":"39(b)"},
            {"CHALLAN_NO":"GHMC8000000432","OFFENCE_DT":"2017-02-06","SECTION_NAME":"407(1)"},
            {"CHALLAN_NO":"GHMC8000000470","OFFENCE_DT":"2017-02-10","SECTION_NAME":"407(1)"},
            {"CHALLAN_NO":"GHMC8000000474","OFFENCE_DT":"2017-02-10","SECTION_NAME":"407(1)"}]}*/
			
			SimpleDateFormat parse = new SimpleDateFormat("yyyy-mm-dd");
			SimpleDateFormat format = new SimpleDateFormat("dd-mm-yyyy");
			
			String strJson = response_id;
			if (response_id != null && !"NA".equals(strJson)) {
				ArrayList<String> arrIds = new ArrayList<String>();
				
				try {
					if (strJson != null && !"NA".equals(strJson)) {
						JSONObject jsonObj = new JSONObject(response_id);
						// Getting JSON Array node
						JSONArray PREVIOUS_DETAILS = jsonObj.getJSONArray("PREVIOUS_DETAILS");
						if (PREVIOUS_DETAILS != null && PREVIOUS_DETAILS.length() > 0) {
							prevoius_history.setVisibility(View.VISIBLE);
						}
						table_layout.removeAllViews();
						addHeaders();
						// looping through All Contacts
						for (int i = 0; i < PREVIOUS_DETAILS.length(); i++) {
							JSONObject p = PREVIOUS_DETAILS.getJSONObject(i);
							String CHALLAN_NO = p.optString("CHALLAN_NO").toString();
							String OFFENCE_DT = p.optString("OFFENCE_DT").toString();
							String SECTION_NAME = p.optString("SECTION_NAME").toString();
							Log.i("ticket_no :::::", (i + 1) + "." + CHALLAN_NO);
							rows = PREVIOUS_DETAILS.length();
							arrIds.add(CHALLAN_NO);

							final TableRow row = new TableRow(Previous_History.this);
							row.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
							row.setClickable(true);

							for (int j = 1; j <= 4; j++) {
								if (j == 1) {
									TextView tv = new TextView(Previous_History.this);
									tv.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
									tv.setBackgroundResource(R.drawable.cell_shape);
									tv.setPadding(5, 12, 5, 12); // l,t,r,b
									tv.setText((i + 1) + ".");
									tv.setTextColor(Color.BLACK);
									tv.setGravity(Gravity.CENTER);
									tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.text_size));
									row.addView(tv);
									
								} else if (j == 3) {
									String date = "";
									try {
										date = format.format(parse.parse(OFFENCE_DT));
									} catch (Exception e) {
										e.printStackTrace();
									}
									TextView tv = new TextView(Previous_History.this);
									tv.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
									tv.setBackgroundResource(R.drawable.cell_shape);
									tv.setPadding(5, 12, 5, 12);
									tv.setText(date);
									tv.setTextColor(Color.BLACK);
									tv.setGravity(Gravity.CENTER);
									tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.text_size));
									row.addView(tv);
								} else if (j == 2) {

									final TextView tv = new TextView(Previous_History.this);
									tv.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
									tv.setBackgroundResource(R.drawable.cell_shape);
									tv.setPadding(5, 12, 5, 12);
									tv.setTextColor(Color.BLACK);
									tv.setTextSize(TypedValue.COMPLEX_UNIT_PX,getResources().getDimension(R.dimen.text_size));
									tv.setText(CHALLAN_NO);
									row.addView(tv);

									tv.setOnClickListener(new OnClickListener() {

										@Override
										public void onClick(View v) {
											// TODO Auto-generated method stub

											SharedPreferences TableValues = getSharedPreferences("TableValues", MODE_PRIVATE);
											SharedPreferences.Editor edit = TableValues.edit();

											eticketNo = tv.getText().toString().trim();
											edit.putString("eticketNo", eticketNo);
											edit.commit();

											new Async_getDuplicatePrintByEticket().execute();
											Log.i("ticket number tv:::::::", "" + tv);
											Log.i("ticket number eticket:::::::", "" + eticketNo);
										}
									});

								} else if (j == 4) {

									final TextView tv = new TextView(Previous_History.this);
									tv.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
									tv.setBackgroundResource(R.drawable.cell_shape);
									tv.setPadding(5, 12, 5, 12);
									tv.setTextColor(Color.BLACK);
									tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.text_size));
									tv.setText(SECTION_NAME);
									row.addView(tv);
								}
							}
							table_layout.addView(row);
						}
					} else if (strJson != null && "NA".equals(strJson)) {
						prevoius_history.setVisibility(View.GONE);
					}

				} catch (JSONException e) {
					e.printStackTrace();
				}
			} else if ("NA".equals(strJson) && strJson != null && strJson != "anytype") {
				prevoius_history.setVisibility(View.GONE);
				showToast("No Details Found !!");
			}
			else {
				prevoius_history.setVisibility(View.GONE);
				showToast("No Details Found !!");
			}
		}
	}

	public class Asyncprevoius_history_firm extends AsyncTask<Void, Void, String> {
		
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			showDialog(PROGRESS_DIALOG);

		}

		@Override
		protected String doInBackground(Void... params) {
			// TODO Auto-generated method stub
			// previousHistory(String idCode,String idValue,String firmName,
			// String tinNumber)
			String firmName = et_id_firm_prev_hist.getText().toString().trim();
			ServiceHelper.getPreviousHstryFirm("", "", firmName, "");
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			removeDialog(PROGRESS_DIALOG);
			String response_firm = ServiceHelper.prv_hisrty_resp_firm;

			SimpleDateFormat parse = new SimpleDateFormat("yyyy-mm-dd");
			SimpleDateFormat format = new SimpleDateFormat("dd-mm-yyyy");
			String strJson = response_firm;
			if (response_firm != null && !"NA".equals(response_firm)) {
				ArrayList<String> arrIds = new ArrayList<String>();
				try {

					if (strJson != null && !"NA".equals(strJson)) {

						JSONObject jsonObj = new JSONObject(response_firm);
						// Getting JSON Array node
						JSONArray PREVIOUS_DETAILS = jsonObj
								.getJSONArray("PREVIOUS_DETAILS");
						if (PREVIOUS_DETAILS != null
								&& PREVIOUS_DETAILS.length() > 0) {
							prevoius_history.setVisibility(View.VISIBLE);
						}
						table_layout.removeAllViews();
						addHeaders();
						// looping through All Contacts
						for (int i = 0; i < PREVIOUS_DETAILS.length(); i++) {
							JSONObject p = PREVIOUS_DETAILS.getJSONObject(i);
							String CHALLAN_NO = p.optString("CHALLAN_NO")
									.toString();
							String OFFENCE_DT = p.optString("OFFENCE_DT")
									.toString();
							String SECTION_NAME = p.optString("SECTION_NAME")
									.toString();

							Log.i("ticket_no :::::", (i + 1) + "." + CHALLAN_NO);
							rows = PREVIOUS_DETAILS.length();

							arrIds.add(CHALLAN_NO);

							final TableRow row = new TableRow(
									Previous_History.this);
							row.setLayoutParams(new LayoutParams(
									LayoutParams.WRAP_CONTENT,
									LayoutParams.WRAP_CONTENT));
							row.setClickable(true);

							for (int j = 1; j <= 4; j++) {
								if (j == 1) {
									TextView tv = new TextView(
											Previous_History.this);
									tv.setLayoutParams(new LayoutParams(
											LayoutParams.WRAP_CONTENT,
											LayoutParams.WRAP_CONTENT));
									tv.setBackgroundResource(R.drawable.cell_shape);
									tv.setPadding(5, 12, 5, 12); // l,t,r,b
									tv.setText((i + 1) + ".");
									tv.setTextColor(Color.BLACK);
									tv.setGravity(Gravity.CENTER);
									tv.setTextSize(
											TypedValue.COMPLEX_UNIT_PX,
											getResources().getDimension(
													R.dimen.text_size));
									row.addView(tv);
								} else if (j == 3) {
									String date = "";
									try {
										date = format.format(parse
												.parse(OFFENCE_DT));
									} catch (Exception e) {
										e.printStackTrace();
									}
									TextView tv = new TextView(
											Previous_History.this);
									tv.setLayoutParams(new LayoutParams(
											LayoutParams.WRAP_CONTENT,
											LayoutParams.WRAP_CONTENT));
									tv.setBackgroundResource(R.drawable.cell_shape);
									tv.setPadding(5, 12, 5, 12);
									tv.setText(date);
									tv.setTextColor(Color.BLACK);
									tv.setGravity(Gravity.CENTER);
									tv.setTextSize(
											TypedValue.COMPLEX_UNIT_PX,
											getResources().getDimension(
													R.dimen.text_size));
									row.addView(tv);
								} else if (j == 2) {

									final TextView tv = new TextView(
											Previous_History.this);
									tv.setLayoutParams(new LayoutParams(
											LayoutParams.WRAP_CONTENT,
											LayoutParams.WRAP_CONTENT));
									tv.setBackgroundResource(R.drawable.cell_shape);
									tv.setPadding(5, 12, 5, 12);
									tv.setTextColor(Color.BLACK);
									tv.setTextSize(
											TypedValue.COMPLEX_UNIT_PX,
											getResources().getDimension(
													R.dimen.text_size));
									tv.setText(CHALLAN_NO);
									row.addView(tv);

									tv.setOnClickListener(new OnClickListener() {

										@Override
										public void onClick(View v) {
											// TODO Auto-generated method stub

											SharedPreferences TableValues = getSharedPreferences(
													"TableValues", MODE_PRIVATE);
											SharedPreferences.Editor edit = TableValues
													.edit();

											eticketNo = tv.getText().toString()
													.trim();
											edit.putString("eticketNo",
													eticketNo);
											edit.commit();

											new Async_getDuplicatePrintByEticket()
													.execute();
											Log.i("ticket number tv:::::::", ""
													+ tv);
											Log.i("ticket number eticket:::::::",
													"" + eticketNo);
										}
									});

								} else if (j == 4) {

									final TextView tv = new TextView(
											Previous_History.this);
									tv.setLayoutParams(new LayoutParams(
											LayoutParams.WRAP_CONTENT,
											LayoutParams.WRAP_CONTENT));
									tv.setBackgroundResource(R.drawable.cell_shape);
									tv.setPadding(5, 12, 5, 12);
									tv.setTextColor(Color.BLACK);
									tv.setTextSize(
											TypedValue.COMPLEX_UNIT_PX,
											getResources().getDimension(
													R.dimen.text_size));
									tv.setText(SECTION_NAME);
									row.addView(tv);

								}

							}
							table_layout.addView(row);

						}
					} else if (strJson != null && "NA".equals(strJson)) {
						prevoius_history.setVisibility(View.GONE);

					}

				} catch (JSONException e) {
					e.printStackTrace();
				}
			} else if ("NA".equals(strJson) && strJson != null
					&& strJson != "anytype") {
				prevoius_history.setVisibility(View.GONE);
				showToast("No Details Found !!");
			}

			else {
				prevoius_history.setVisibility(View.GONE);
				showToast("No Details Found !!");

			}

		}

	}

	public class Asyncprevoius_history_GHMCTIN extends
			AsyncTask<Void, String, String> {
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			showDialog(PROGRESS_DIALOG);
		}

		@Override
		protected String doInBackground(Void... params) {
			// previousHistory(String idCode,String idValue,String firmName,
			// String tinNumber)
			String tinNumber = et_id_firm_prev_hist.getText().toString().trim();

			ServiceHelper.getPreviousHstryTin("", "", "", tinNumber);
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			removeDialog(PROGRESS_DIALOG);
			// String response_firm = ServiceHelper.prv_hisrty_resp;
			String response_tin = ServiceHelper.prv_hisrty_resp_tin;
			SimpleDateFormat parse = new SimpleDateFormat("yyyy-mm-dd");
			SimpleDateFormat format = new SimpleDateFormat("dd-mm-yyyy");
			String strJson = response_tin;
			if (response_tin != null && !"NA".equals(response_tin)) {
				ArrayList<String> arrIds = new ArrayList<String>();

				try {

					if (strJson != null && !"NA".equals(strJson)) {

						JSONObject jsonObj = new JSONObject(response_tin);
						// Getting JSON Array node
						JSONArray PREVIOUS_DETAILS = jsonObj
								.getJSONArray("PREVIOUS_DETAILS");
						if (PREVIOUS_DETAILS != null
								&& PREVIOUS_DETAILS.length() > 0) {
							prevoius_history.setVisibility(View.VISIBLE);
						}
						table_layout.removeAllViews();
						addHeaders();
						// looping through All Contacts
						for (int i = 0; i < PREVIOUS_DETAILS.length(); i++) {
							JSONObject p = PREVIOUS_DETAILS.getJSONObject(i);
							String CHALLAN_NO = p.optString("CHALLAN_NO")
									.toString();
							String OFFENCE_DT = p.optString("OFFENCE_DT")
									.toString();
							String SECTION_NAME = p.optString("SECTION_NAME")
									.toString();

							Log.i("ticket_no :::::", (i + 1) + "." + CHALLAN_NO);

							rows = PREVIOUS_DETAILS.length();

							arrIds.add(CHALLAN_NO);

							final TableRow row = new TableRow(
									Previous_History.this);
							row.setLayoutParams(new LayoutParams(
									LayoutParams.WRAP_CONTENT,
									LayoutParams.WRAP_CONTENT));
							row.setClickable(true);

							for (int j = 1; j <= 4; j++) {
								if (j == 1) {
									TextView tv = new TextView(
											Previous_History.this);
									tv.setLayoutParams(new LayoutParams(
											LayoutParams.WRAP_CONTENT,
											LayoutParams.WRAP_CONTENT));
									tv.setBackgroundResource(R.drawable.cell_shape);
									tv.setPadding(5, 12, 5, 12); // l,t,r,b
									tv.setText((i + 1) + ".");
									tv.setTextColor(Color.BLACK);
									tv.setGravity(Gravity.CENTER);
									tv.setTextSize(
											TypedValue.COMPLEX_UNIT_PX,
											getResources().getDimension(
													R.dimen.text_size));
									row.addView(tv);
								} else if (j == 3) {
									String date = "";
									try {
										date = format.format(parse
												.parse(OFFENCE_DT));
									} catch (Exception e) {
										e.printStackTrace();
									}
									TextView tv = new TextView(
											Previous_History.this);
									tv.setLayoutParams(new LayoutParams(
											LayoutParams.WRAP_CONTENT,
											LayoutParams.WRAP_CONTENT));
									tv.setBackgroundResource(R.drawable.cell_shape);
									tv.setPadding(5, 12, 5, 12);
									tv.setText(date);
									tv.setTextColor(Color.BLACK);
									tv.setGravity(Gravity.CENTER);
									tv.setTextSize(
											TypedValue.COMPLEX_UNIT_PX,
											getResources().getDimension(
													R.dimen.text_size));
									row.addView(tv);
								} else if (j == 2) {

									final TextView tv = new TextView(
											Previous_History.this);
									tv.setLayoutParams(new LayoutParams(
											LayoutParams.WRAP_CONTENT,
											LayoutParams.WRAP_CONTENT));
									tv.setBackgroundResource(R.drawable.cell_shape);
									tv.setPadding(5, 12, 5, 12);
									tv.setTextColor(Color.BLACK);
									tv.setTextSize(
											TypedValue.COMPLEX_UNIT_PX,
											getResources().getDimension(
													R.dimen.text_size));
									tv.setText(CHALLAN_NO);
									row.addView(tv);

									tv.setOnClickListener(new OnClickListener() {

										@Override
										public void onClick(View v) {
											// TODO Auto-generated method stub

											SharedPreferences TableValues = getSharedPreferences(
													"TableValues", MODE_PRIVATE);
											SharedPreferences.Editor edit = TableValues
													.edit();

											eticketNo = tv.getText().toString()
													.trim();
											edit.putString("eticketNo",
													eticketNo);
											edit.commit();

											new Async_getDuplicatePrintByEticket()
													.execute();
											Log.i("ticket number tv:::::::", ""
													+ tv);
											Log.i("ticket number eticket:::::::",
													"" + eticketNo);
										}
									});

								} else if (j == 4) {

									final TextView tv = new TextView(
											Previous_History.this);
									tv.setLayoutParams(new LayoutParams(
											LayoutParams.WRAP_CONTENT,
											LayoutParams.WRAP_CONTENT));
									tv.setBackgroundResource(R.drawable.cell_shape);
									tv.setPadding(5, 12, 5, 12);
									tv.setTextColor(Color.BLACK);
									tv.setTextSize(
											TypedValue.COMPLEX_UNIT_PX,
											getResources().getDimension(
													R.dimen.text_size));
									tv.setText(SECTION_NAME);
									row.addView(tv);

								}

							}
							table_layout.addView(row);

						}
					} else if (strJson != null && "NA".equals(strJson)) {
						prevoius_history.setVisibility(View.GONE);

					}

				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

			else if ("NA".equals(strJson) && strJson != null
					&& strJson != "anytype") {
				prevoius_history.setVisibility(View.GONE);
				showToast("No Details Found !!");
			}
		}

	}

	public class Async_getDuplicatePrintByEticket extends
			AsyncTask<Void, Void, String> {
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			showDialog(PROGRESS_DIALOG);
		}

		@Override
		protected String doInBackground(Void... params) {
			// TODO Auto-generated method stub

			SharedPreferences TableValues = getSharedPreferences("TableValues", MODE_PRIVATE);
			eticketNo = TableValues.getString("eticketNo", "");
			Log.i("eticketNo at service calling :::::::", "" + eticketNo);
			ServiceHelper.getDuplicatePrintByEticket(eticketNo);
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			removeDialog(PROGRESS_DIALOG);
			PRINT_DATA_ID = ServiceHelper.print_resp;
			Log.i("print Response by ticket no ::::::::::", "" + PRINT_DATA_ID);

			if (PRINT_DATA_ID != null) {
				previoushistoryFLG = true;
				SharedPreferences prefs = getSharedPreferences("printData_phistory_id", MODE_PRIVATE);
				SharedPreferences.Editor edit = prefs.edit();
				edit.putString("print", "" + PRINT_DATA_ID);
				edit.commit();

				Intent print = new Intent(Previous_History.this, Ph_printDisplay.class);
				startActivity(print);
			} else {
				showToast("PLEASE CHECK YOUR NETWORK CONNECTION");
			}
		}

	}

	@Override
	public void onBackPressed() {
		// do nothing.
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

	public void addHeaders() {
		/** Create a TableRow dynamically **/
		tr = new TableRow(this);
		tr.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

		/** Creating a TextView to add to the row **/
		TextView serialNo = new TextView(this);
		serialNo.setText("S.No");
		serialNo.setBackgroundResource(R.drawable.cell_heading);
		serialNo.setGravity(Gravity.CENTER);
		serialNo.setTextColor(Color.WHITE);
		serialNo.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
		serialNo.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		serialNo.setPadding(5, 11, 5, 11);
		serialNo.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.text_size_header));
		tr.addView(serialNo);

		/** Creating a TextView to add to the row **/
		TextView date_headng = new TextView(this);
		date_headng.setText("Challan Number");
		date_headng.setBackgroundResource(R.drawable.cell_heading);
		date_headng.setGravity(Gravity.CENTER);
		date_headng.setTextColor(Color.WHITE);
		date_headng.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
		date_headng.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		date_headng.setPadding(5, 11, 5, 11);
		date_headng.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.text_size_header));
		tr.addView(date_headng);

		/** Creating another textview **/
		TextView violation_hder = new TextView(this);
		violation_hder.setText("Date");
		violation_hder.setBackgroundResource(R.drawable.cell_heading);
		violation_hder.setGravity(Gravity.CENTER);
		violation_hder.setTextColor(Color.WHITE);
		violation_hder.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		violation_hder.setPadding(5, 11, 5, 11);
		violation_hder.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.text_size_header));
		violation_hder.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
		tr.addView(violation_hder);

		/** Creating another textview **/
		TextView amount_hder = new TextView(this);
		amount_hder.setText("Section Name");
		amount_hder.setBackgroundResource(R.drawable.cell_heading);
		amount_hder.setGravity(Gravity.CENTER);
		amount_hder.setTextColor(Color.WHITE);
		amount_hder.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		amount_hder.setPadding(5, 11, 5, 11);
		amount_hder.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.text_size_header));
		amount_hder.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
		tr.addView(amount_hder);

		table_layout.addView(tr, new TableLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
	}
}

