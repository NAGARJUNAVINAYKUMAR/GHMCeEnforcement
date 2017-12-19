package com.mtpv.ghmcepettycase;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.mtpv.ghmcenforcement.R;
import com.mtpv.services.ServiceHelper;

public class Preview extends Activity implements LocationListener {

	LinearLayout measurement_layout, shop_details_layout,
			person_details_layout, owner_layout, respondent_layout,
			section_layout, panchayathdhar_layout, seized_layout;
	TextView tv_measurement, tv_shop_details, tv_person_details,
			tv_owner_details, tv_respondent_details, tv_section_details,
			tv_panchayathdhar_details, tv_seized, shoprun_by_txt;
	Button cancel_btn, submit_btn;
	ImageView image_display1, image_display2;

	StringBuffer measurement_buffer = new StringBuffer();
	StringBuffer shop_buffer = new StringBuffer();
	StringBuffer person_buffer = new StringBuffer();
	StringBuffer owner_buffer = new StringBuffer();
	StringBuffer respondent_buffer = new StringBuffer();
	StringBuffer section_buffer = new StringBuffer();
	StringBuffer panchayathdhar_buffer = new StringBuffer();
	StringBuffer seized_buffer = new StringBuffer();

	final int PROGRESS_DIALOG = 1;

	public static String panch_name1 = null;
	public static String panch_age1 = null;
	public static String panch_fname1 = null;
	public static String panch_mobileNo1 = null;
	public static String panch_address1 = null;
	public static String panch_gender1 = null;

	public static String panch_name2 = null;
	public static String panch_age2 = null;
	public static String panch_fname2 = null;
	public static String panch_mobileNo2 = null;
	public static String panch_address2 = null;
	public static String panch_gender2 = null;

	/**** CASE VALUES ******/
	public static String image1 = null;
	public static String image2 = null;
	public static String owner_image = null;
	public static String tine_No = null;

	public static String unitCode = null;
	public static String unitName = null;
	public static String bookedPsCode = null;
	public static String bookedPsName = null;
	public static String pointCode = null;
	public static String pointName = null;
	public static String pidCd = null;
	public static String pidName = null;
	public static String password = null;
	public static String cadreCD = null;
	public static String cadre = null;
	public static String onlineMode = null;
	public static String imageEvidence = null;
	public static String offenceImg1 = null;
	public static String offenceDt = null;
	public static String offenceTime = null;
	public static String firmRegnNo = null;
	public static String shopName = null;
	public static String shopOwnerName = null;
	public static String shopAddress = null;
	public static String current_location = null;
	public static String psCode = null;
	public static String psName = null;
	public static String respondantName = null;
	public static String respondantFatherName = null;
	public static String respondantAddress = null;
	public static String respondantContactNo = null;
	public static String respondantAge = null;
	public static String IDCode = null;
	public static String IDDetails = null;
	public static String simId = null;
	public static String imeiNo = null;
	public static String macId = null;
	public static String gpsLatitude = null;
	public static String gpsLongitude = null;
	public static String totalFine = null;
	public static String encrHeight = null;
	public static String encrWidth = null;
	public static String encrLength = null;
	public static String shopRunBy = null;
	public static String ghmcCirNo = null;
	public static String ghmcCirName = null;
	public static String basedOn = null;
	public static String offenceImg2 = null;
	public static String aadharImg = null;
	public static String seizedImg = null;
	public static String detainedItems = null;
	public static String sections = null;

	public static String owner_aadhaarNo = null;
	public static String owner_name = null;
	public static String owner_fatherName = null;
	public static String owner_Age = null;
	public static String owner_MobileNo = null;
	public static String owner_address = null, IMEI = null;

	public static boolean generateCaseFLG = false;

	GPSTracker gps;
	/* GPS VALUES */
	// flag for GPS status
	boolean isGPSEnabled = false;
	// flag for network status
	boolean isNetworkEnabled = false;
	boolean canGetLocation = false;
	// The minimum distance to change Updates in meters
	private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 meters
	// The minimum time between updates in milliseconds
	private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1; // 1 minute
	public static String PsCode = null, PsName = null;
	static double latitude = 0.0;
	static double longitude = 0.0;
	String provider = "";
	LocationManager m_locationlistner;
	android.location.Location location;

	InputStream inputStream = null, inputStream2 = null;

	@SuppressLint("WrongViewCast")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_preview);

		image1 = null;
		image2 = null;

		panch_name1 = null;
		panch_age1 = null;
		panch_fname1 = null;
		panch_mobileNo1 = null;
		panch_address1 = null;
		panch_gender1 = null;

		panch_name2 = null;
		panch_age2 = null;
		panch_fname2 = null;
		panch_mobileNo2 = null;
		panch_address2 = null;
		panch_gender2 = null;

		/**** CASE VALUES ******/
		image1 = null;
		image2 = null;
		owner_image = null;
		tine_No = null;

		unitCode = null;
		unitName = null;
		bookedPsCode = null;
		bookedPsName = null;
		pointCode = null;
		pointName = null;
		pidCd = null;
		pidName = null;
		password = null;
		cadreCD = null;
		cadre = null;
		onlineMode = null;
		imageEvidence = null;
		offenceImg1 = null;
		offenceDt = null;
		offenceTime = null;
		firmRegnNo = null;
		shopName = null;
		shopOwnerName = null;
		shopAddress = null;
		current_location = null;
		psCode = null;
		psName = null;
		respondantName = null;
		respondantFatherName = null;
		respondantAddress = null;
		respondantContactNo = null;
		respondantAge = null;
		IDCode = null;
		IDDetails = null;
		simId = null;
		imeiNo = null;
		macId = null;
		gpsLatitude = null;
		gpsLongitude = null;
		totalFine = null;
		encrHeight = null;
		encrWidth = null;
		encrLength = null;
		shopRunBy = null;
		ghmcCirNo = null;
		ghmcCirName = null;
		basedOn = null;
		offenceImg2 = null;
		aadharImg = null;
		seizedImg = null;
		detainedItems = null;
		sections = null;

		owner_aadhaarNo = null;
		owner_name = null;
		owner_fatherName = null;
		owner_Age = null;
		owner_MobileNo = null;
		owner_address = null;
		IMEI = null;

		getLocation();

		image_display1 = (ImageView) findViewById(R.id.image1);
		image_display2 = (ImageView) findViewById(R.id.image2);

		image_display1.setVisibility(View.GONE);
		image_display2.setVisibility(View.GONE);

		cancel_btn = (Button) findViewById(R.id.cancel);
		cancel_btn.setVisibility(View.VISIBLE);

		submit_btn = (Button) findViewById(R.id.submit);

		measurement_layout = (LinearLayout) findViewById(R.id.measurements_layout);
		shop_details_layout = (LinearLayout) findViewById(R.id.shop_details_layout);
		person_details_layout = (LinearLayout) findViewById(R.id.person_details_layout);
		owner_layout = (LinearLayout) findViewById(R.id.owner_details_layout);
		respondent_layout = (LinearLayout) findViewById(R.id.respondent_layout);
		section_layout = (LinearLayout) findViewById(R.id.Sections_layout);
		panchayathdhar_layout = (LinearLayout) findViewById(R.id.Panchayathdhar_layout);
		seized_layout = (LinearLayout) findViewById(R.id.seizeditems_layout);

		measurement_layout.setVisibility(View.GONE);
		shop_details_layout.setVisibility(View.GONE);
		person_details_layout.setVisibility(View.GONE);
		owner_layout.setVisibility(View.GONE);
		respondent_layout.setVisibility(View.GONE);
		section_layout.setVisibility(View.GONE);
		panchayathdhar_layout.setVisibility(View.GONE);
		seized_layout.setVisibility(View.GONE);

		tv_measurement = (TextView) findViewById(R.id.measurements);
		tv_shop_details = (TextView) findViewById(R.id.shop_details);
		tv_person_details = (TextView) findViewById(R.id.person_details);
		tv_owner_details = (TextView) findViewById(R.id.owner_details);
		tv_respondent_details = (TextView) findViewById(R.id.respondent_details);
		tv_section_details = (TextView) findViewById(R.id.sections_details);
		tv_panchayathdhar_details = (TextView) findViewById(R.id.Panchayathdhar_details);
		tv_seized = (TextView) findViewById(R.id.seizeditems_details);

		shoprun_by_txt = (TextView) findViewById(R.id.shoprun_by_txt);

		/******* WITNESS VALUES ***********/
		SharedPreferences myprefs = getSharedPreferences("panchayathDhars", MODE_PRIVATE);
		panch_name1 = myprefs.getString("Pancha_Name1", "");
		panch_fname1 = myprefs.getString("Pancha_Fname1", "");
		panch_mobileNo1 = myprefs.getString("Pancha_MobileNo1", "");
		panch_address1 = myprefs.getString("Pancha_address1", "");
		panch_gender1 = myprefs.getString("Pancha_gender1", "");

		panch_name2 = myprefs.getString("Pancha_Name2", "");
		panch_fname2 = myprefs.getString("Pancha_Fname2", "");
		panch_mobileNo2 = myprefs.getString("Pancha_MobileNo2", "");
		panch_address2 = myprefs.getString("Pancha_address2", "");
		panch_gender2 = myprefs.getString("Pancha_gender2", "");

		/**** CASE VALUES ******/
		SharedPreferences case_Vals = getSharedPreferences("CaseValues", MODE_PRIVATE);

		tine_No = case_Vals.getString("TinNo", "");

		image1 = case_Vals.getString("image1", "");
		image2 = case_Vals.getString("image2", "");

		owner_image = case_Vals.getString("owner_image", "");

		unitCode = case_Vals.getString("unitCode", "");
		unitName = case_Vals.getString("unitName", "");
		bookedPsCode = case_Vals.getString("bookedPsCode", "");
		bookedPsName = case_Vals.getString("bookedPsName", "");
		pointCode = case_Vals.getString("pointCode", "");
		pointName = case_Vals.getString("pointName", "");
		pidCd = case_Vals.getString("pidCd", "");
		pidName = case_Vals.getString("pidName", "");
		password = case_Vals.getString("password", "");
		cadreCD = case_Vals.getString("cadreCD", "");
		cadre = case_Vals.getString("cadre", "");
		onlineMode = case_Vals.getString("onlineMode", "");
		imageEvidence = case_Vals.getString("imageEvidence", "");
		offenceImg1 = case_Vals.getString("offenceImg1", "");
		offenceDt = case_Vals.getString("offenceDt", "");
		offenceTime = case_Vals.getString("offenceTime", "");
		firmRegnNo = case_Vals.getString("firmRegnNo", "");
		shopName = case_Vals.getString("shopName", "");
		shopOwnerName = case_Vals.getString("shopOwnerName", "");
		shopAddress = case_Vals.getString("shopAddress ", "");
		current_location = case_Vals.getString("location", "");
		psCode = case_Vals.getString("psCode", "");
		psName = case_Vals.getString("psName", "");
		respondantName = case_Vals.getString("respondantName", "");
		respondantFatherName = case_Vals.getString("respondantFatherName", "");
		respondantAddress = case_Vals.getString("respondantAddress", "");
		respondantContactNo = case_Vals.getString("respondantContactNo", "");
		respondantAge = case_Vals.getString("respondantAge", "");
		IDCode = case_Vals.getString("IDCode", "");
		IDDetails = case_Vals.getString("IDDetails", "");
		simId = case_Vals.getString("simId", "");
		imeiNo = case_Vals.getString("imeiNo", "");
		macId = case_Vals.getString("macId", "");
		/*
		 * gpsLatitude = case_Vals.getString("gpsLatitude",""); gpsLongitude =
		 * case_Vals.getString("gpsLongitude","");
		 */

		gpsLatitude = "" + latitude;
		gpsLongitude = "" + longitude;

		totalFine = case_Vals.getString("totalFine", "");
		encrHeight = case_Vals.getString("encrHeight", "");
		encrWidth = case_Vals.getString("encrWidth", "");
		encrLength = case_Vals.getString("encrLength", "");
		shopRunBy = case_Vals.getString("shopRunBy", "");
		ghmcCirNo = case_Vals.getString("ghmcCirNo", "");
		ghmcCirName = case_Vals.getString("ghmcCirName", "");
		basedOn = case_Vals.getString("basedOn", "");
		offenceImg2 = case_Vals.getString("offenceImg2", "");
		aadharImg = case_Vals.getString("aadharImg", "");
		seizedImg = case_Vals.getString("seizedImg", "");
		detainedItems = case_Vals.getString("detainedItems", "");
		sections = case_Vals.getString("sections", "");

		owner_aadhaarNo = case_Vals.getString("owner_aadhaarNo", "");
		owner_name = case_Vals.getString("owner_name", "");
		owner_fatherName = case_Vals.getString("owner_fatherName", "");
		owner_Age = case_Vals.getString("owner_Age", "");
		owner_MobileNo = case_Vals.getString("owner_MobileNo", "");
		owner_address = case_Vals.getString("owner_address", "");


		if (shopRunBy.equals("OWNER")) {
			shoprun_by_txt.setText("Owner Details");
		} else if (shopRunBy.equals("MANAGER")) {
			shoprun_by_txt.setText("Manager Details");
		} else if (shopRunBy.equals("SHOPKEEPER")) {
			shoprun_by_txt.setText("ShopKeeper Details");
		}

		/*
		 * owner_aadhaarNo",owner_aadhaarNo"","); owner_name",owner_name"",");
		 * owner_fatherName",owner_fatherName"","); owner_Age",owner_Age"",");
		 * owner_MobileNo",owner_MobileNo"",");
		 * owner_address",owner_address"",");
		 */

		if (GenerateCase.imgString != null || GenerateCase.imgString2 != null) {
			// measurement_layout.setVisibility(View.VISIBLE);

			if (GenerateCase.imgString != null) {
				try {
					image_display1.setVisibility(View.VISIBLE);
					byte[] decodedString = Base64.decode(GenerateCase.imgString, Base64.NO_WRAP);
					inputStream = new ByteArrayInputStream(decodedString);
					Bitmap bitmap = BitmapFactory.decodeStream(inputStream);// out
																			// of
																			// memory
																			// error
					image_display1.setImageBitmap(bitmap);
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				image_display1.setVisibility(View.GONE);
			}

			if (GenerateCase.imgString2 != null) {
				try {
					image_display2.setVisibility(View.VISIBLE);
					byte[] decodedString2 = Base64.decode(GenerateCase.imgString2, Base64.NO_WRAP);
					inputStream2 = new ByteArrayInputStream(decodedString2);
					Bitmap bitmap2 = BitmapFactory.decodeStream(inputStream2);
					
					image_display2.setImageBitmap(bitmap2);

				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				image_display2.setVisibility(View.GONE);
			}
		}

		if (basedOn.equals("PERSON")) {
			if (!GenerateCase.et_width.getText().toString().trim().equals("")) {
				measurement_layout.setVisibility(View.VISIBLE);
				// measurement_buffer.append("\t\t\t\t e-Enforcement Measurements"+"\n\n");
				measurement_buffer.append("Width \t\t: \t\t"+ GenerateCase.et_width.getText().toString().trim() + "\n");
				measurement_buffer.append("Height \t\t: \t\t" + GenerateCase.et_length.getText().toString().trim() + "\n\n");
				
				tv_measurement.setText("" + measurement_buffer);
				
			} else {
				measurement_layout.setVisibility(View.GONE);
				measurement_buffer.setLength(0);
				measurement_buffer.delete(0, measurement_buffer.length());
			}
			shop_details_layout.setVisibility(View.GONE);
			owner_layout.setVisibility(View.GONE);

			if (!respondantName.equals("")) {
				respondent_layout.setVisibility(View.VISIBLE);
				// respondent_buffer.append("\t\t\t\t Respondent Details"+"\n");
				respondent_buffer.append("Aadhaar No  \t\t: \t\t" + IDDetails + "\n");
				respondent_buffer.append("Name \t\t: \t\t" + respondantName + "\n");
				respondent_buffer.append("Father Name \t\t: \t\t" + respondantFatherName + "\n");
				respondent_buffer.append("Age \t\t: \t\t" + respondantAge + "\n");
				respondent_buffer.append("Mobile No \t\t: \t\t" + respondantContactNo + "\n");
				respondent_buffer.append("Address \t\t: \t\t" + respondantAddress + "\n");
				
				tv_respondent_details.setText("" + respondent_buffer);
				
			} else {
				respondent_layout.setVisibility(View.GONE);
				respondent_buffer.setLength(0);
				respondent_buffer.delete(0, respondent_buffer.length());
			}

			// panch_name1 = panch_name1!=null?panch_name1.trim():"";
			if (!panch_name1.equals("")) {
				Log.i("PAncha :::", "Name --" + panch_name1);
				panchayathdhar_layout.setVisibility(View.VISIBLE);
				
				// panchayathdhar_buffer.append("\t\t\t\t Panchayathdhar 1 Details"+"\n\n");
				panchayathdhar_buffer.append("Name \t\t: \t\t" + panch_name1 + "\n");
				panchayathdhar_buffer.append("Father Name \t\t: \t\t" + panch_fname1 + "\n");
				panchayathdhar_buffer.append("Address \t\t: \t\t" + panch_address1 + "\n\n");
				// panchayathdhar_buffer.append("Mobile No \t\t: \t\t"+panch_mobileNo1+"\n");

				if (panch_name2 != null) {
					panchayathdhar_buffer.append("\t\t\t\t Panchayathdhar 2 Details" + "\n\n");
					panchayathdhar_buffer.append("Name \t\t: \t\t" + panch_name2 + "\n");
					panchayathdhar_buffer.append("Father Name \t\t: \t\t" + panch_fname2 + "\n");
					panchayathdhar_buffer.append("Address \t\t: \t\t" + panch_address2 + "\n\n");
					// panchayathdhar_buffer.append("Mobile No \t\t:"+panch_mobileNo2+"\n");
				}
				tv_panchayathdhar_details.setText("" + panchayathdhar_buffer);

			} else {
				panchayathdhar_layout.setVisibility(View.GONE);
				panchayathdhar_buffer.setLength(0);
				panchayathdhar_buffer.delete(0, panchayathdhar_buffer.length());
			}

			if (sections != null) {
				section_layout.setVisibility(View.VISIBLE);
				section_buffer.append("\t\t\t\t Section Details" + "\n\n");
				section_buffer.append("" + sections + "\n\n");
				
				tv_section_details.setText("" + section_buffer);
				
			} else {
				section_layout.setVisibility(View.GONE);
				section_buffer.setLength(0);
				section_buffer.delete(0, section_buffer.length());
			}

			if (detainedItems != null) {
				seized_layout.setVisibility(View.VISIBLE);

				String detItems = DetainedItems.detItems.toString().trim().split("\\$")[0].replace("@", ",");;

				seized_buffer.append("\t\tItem Names and Quantity" + "\n");
				seized_buffer.append("" + detItems + "\n");

				tv_seized.setText("" + seized_buffer);
				
			} else {
				seized_layout.setVisibility(View.GONE);
				seized_buffer.setLength(0);
				seized_buffer.delete(0, seized_buffer.length());
			}
			
		} else if (basedOn.equals("SHOP")) {
			if (!GenerateCase.et_width.getText().toString().trim().equals("")) {
				measurement_layout.setVisibility(View.VISIBLE);
				// measurement_buffer.append("\t\t\t\t e-Enforcement Measurements"+"\n\n");
				measurement_buffer.append("Width \t\t: \t\t" + GenerateCase.et_width.getText().toString().trim() + "\n");
				measurement_buffer.append("Height \t\t: \t\t" + GenerateCase.et_length.getText().toString().trim() + "\n\n");

				tv_measurement.setText("" + measurement_buffer);
				
			} else {
				measurement_layout.setVisibility(View.GONE);
				measurement_buffer.setLength(0);
				measurement_buffer.delete(0, measurement_buffer.length());
			}

			if (!shopName.equals("")) {
				shop_details_layout.setVisibility(View.VISIBLE);
				Log.i("shopName ::::", "" + shopName);
				Log.i("shopOwnerName ::::", "" + shopOwnerName);
				Log.i("shopAddress ::::", "" + shopAddress);
				// shop_buffer.append("\t\t\t\t Shop Details"+"\n\n");
				shop_buffer.append("TIN No \t\t: \t\t" + firmRegnNo + "\n");
				shop_buffer.append("Shop Name \t\t: \t\t" + shopName + "\n");
				shop_buffer.append("Shop Owner \t\t: \t\t" + shopOwnerName + "\n");
				shop_buffer.append("Shop Addres \t\t: \t\t" + shopAddress + "\n\n");

				tv_shop_details.setText("" + shop_buffer);

			} else {
				shop_details_layout.setVisibility(View.GONE);
				shop_buffer.setLength(0);
				shop_buffer.delete(0, shop_buffer.length());
			}

			if (!owner_name.equals("")) {
				owner_layout.setVisibility(View.VISIBLE);
				// owner_buffer.append("\t\t\t\t Owner Details"+"\n\n");
				owner_buffer.append("Aadhaar No \t\t: \t\t" + owner_aadhaarNo + "\n");
				owner_buffer.append("Name \t\t: \t\t" + owner_name + "\n");
				owner_buffer.append("Father Name \t\t: \t\t" + owner_fatherName + "\n");
				owner_buffer.append("Age \t\t: \t\t" + owner_Age + "\n");
				owner_buffer.append("Mobile No \t\t: \t\t" + owner_MobileNo + "\n");
				owner_buffer.append("Address \t\t: \t\t" + owner_address + "\n\n");

				tv_owner_details.setText("" + owner_buffer);
				
			} else {
				owner_layout.setVisibility(View.GONE);
				owner_buffer.setLength(0);
				owner_buffer.delete(0, owner_buffer.length());
			}

			if (!respondantName.equals("")) {
				respondent_layout.setVisibility(View.VISIBLE);
				// respondent_buffer.append("\t\t\t\t Respondent Details"+"\n");
				respondent_buffer.append("Aadhaar No \t\t: \t\t" + IDDetails + "\n");
				respondent_buffer.append("Name \t\t: \t\t" + respondantName + "\n");
				respondent_buffer.append("Father Name \t\t: \t\t" + respondantFatherName + "\n");
				respondent_buffer.append("Age \t\t: \t\t" + respondantAge + "\n");
				respondent_buffer.append("Mobile No \t\t: \t\t" + respondantContactNo + "\n");
				respondent_buffer.append("Address \t\t: \t\t" + respondantAddress + "\n");

				tv_respondent_details.setText("" + respondent_buffer);
				
			} else {
				respondent_layout.setVisibility(View.GONE);
				respondent_buffer.setLength(0);
				respondent_buffer.delete(0, respondent_buffer.length());
			}

			// panch_name1 = panch_name1!=null?panch_name1.trim():"";
			if (!panch_name1.equals("")) {
				Log.i("PAncha ----:::", "Name --" + panch_name1);
				panchayathdhar_layout.setVisibility(View.VISIBLE);
				// panchayathdhar_buffer.append("\t\t\t\t Panchayathdhar 1 Details"+"\n\n");
				panchayathdhar_buffer.append("Name \t\t: \t\t" + panch_name1 + "\n");
				panchayathdhar_buffer.append("Father Name \t\t: \t\t" + panch_fname1 + "\n");
				panchayathdhar_buffer.append("Address \t\t: \t\t" + panch_address1 + "\n\n");
				// panchayathdhar_buffer.append("Mobile No \t\t: \t\t"+panch_mobileNo1+"\n");

				if (!panch_name2.equals("")) {
					panchayathdhar_buffer.append("\t\t\t\t Panchayathdhar 2 Details" + "\n\n");
					panchayathdhar_buffer.append("Name \t\t: \t\t" + panch_name2 + "\n");
					panchayathdhar_buffer.append("Father Name \t\t: \t\t" + panch_fname2 + "\n");
					panchayathdhar_buffer.append("Address \t\t: \t\t" + panch_address2 + "\n\n");
					// panchayathdhar_buffer.append("Mobile No \t\t:"+panch_mobileNo2+"\n");
				}
				tv_panchayathdhar_details.setText("" + panchayathdhar_buffer);

			} else {
				panchayathdhar_layout.setVisibility(View.GONE);
				panchayathdhar_buffer.setLength(0);
				panchayathdhar_buffer.delete(0, panchayathdhar_buffer.length());
			}

			if (sections != null) {
				section_layout.setVisibility(View.VISIBLE);
				section_buffer.append("\t\t\t\t Section Details" + "\n\n");
				section_buffer.append("" + sections + "\n\n");

				tv_section_details.setText("" + section_buffer);
				
			} else {
				section_layout.setVisibility(View.GONE);
				section_buffer.setLength(0);
				section_buffer.delete(0, section_buffer.length());
			}

			if (detainedItems != null) {
				seized_layout.setVisibility(View.VISIBLE);
				String detItems = DetainedItems.detItems.toString().trim().split("\\$")[0].replace("@", ",");
				// detItems.replace("@", ",");
				seized_buffer.append("\t\tItem Names and Quantity" + "\n");
				seized_buffer.append("" + detItems + "\n");

				tv_seized.setText("" + seized_buffer);
				
			} else {
				seized_layout.setVisibility(View.GONE);
				seized_buffer.setLength(0);
				seized_buffer.delete(0, seized_buffer.length());
			}
		}

		cancel_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				/*
				 * SharedPreferences case_Vals =
				 * getSharedPreferences("CaseValues", MODE_PRIVATE);
				 * SharedPreferences.Editor edit = case_Vals.edit() ;
				 * 
				 * edit.putString("image1",""); edit.putString("image2","");
				 * 
				 * edit.putString("aadharImg","");
				 * edit.putString("seizedImg","");
				 * 
				 * edit.commit();
				 * 
				 * image1 = null ; image2 = null ;
				 */
				measurement_buffer.setLength(0);
				measurement_buffer.delete(0, measurement_buffer.length());
				
				shop_buffer.setLength(0);
				shop_buffer.delete(0, shop_buffer.length());
				
				owner_buffer.setLength(0);
				owner_buffer.delete(0, owner_buffer.length());
				
				respondent_buffer.setLength(0);
				respondent_buffer.delete(0, respondent_buffer.length());
				
				panchayathdhar_buffer.setLength(0);
				panchayathdhar_buffer.delete(0, panchayathdhar_buffer.length());
				
				section_buffer.setLength(0);
				section_buffer.delete(0, section_buffer.length());
				
				seized_buffer.setLength(0);
				seized_buffer.delete(0, seized_buffer.length());

				finish();
				
			}
		});

		submit_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(isOnline()) {
					new Async_Submit().execute();
				}else
				{
					showToast("Please Check Your Network Connection");
				}
			}
		});
	}
	public Boolean isOnline() {
		ConnectivityManager conManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo nwInfo = conManager.getActiveNetworkInfo();
		return nwInfo != null;
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

	public class Async_Submit extends AsyncTask<Void, Void, String> {

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			showDialog(PROGRESS_DIALOG);
		}

		@Override
		protected String doInBackground(Void... params) {
			// TODO Auto-generated method stub
			Calendar c = Calendar.getInstance();
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
			String strDate = sdf.format(c.getTime());
			Log.i("strDate ::", "" + strDate);

			Calendar cal = Calendar.getInstance();
			int hourofday = cal.get(Calendar.HOUR_OF_DAY);
			int minute = cal.get(Calendar.MINUTE);
			int second = cal.get(Calendar.SECOND);
			
			StringBuilder time = new StringBuilder();
			time.append(hourofday).append(":").append(minute).append(":").append(second);
			Log.i("time ::", "" + time);
			GenerateCase.generateCaseFLG = true;

			try {
				GPSTracker.locationManager = null;
				gps = new GPSTracker(Preview.this);
				// check if GPS enabled
				if (gps.getLocation() != null) {
					latitude = gps.getLatitude();
					longitude = gps.getLongitude();
				} else {
					gps.showSettingsAlert();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			ServiceHelper.generate_Challan(unitCode, unitName, bookedPsCode, bookedPsName, pointCode, pointName, pidCd, pidName,
					password, cadreCD, cadre, onlineMode, imageEvidence, offenceImg1, offenceDt, offenceTime, firmRegnNo, shopName,
					shopOwnerName, shopAddress, current_location, psCode, psName, respondantName, respondantFatherName,
					respondantAddress, respondantContactNo, respondantAge, IDCode, IDDetails, panch_name1, panch_fname1,
					panch_address1, panch_name2, panch_fname2, panch_address2, detainedItems, simId, imeiNo, macId, "" + latitude, 
					"" + longitude, totalFine, encrHeight, encrWidth, encrLength, shopRunBy, ghmcCirNo, ghmcCirName, basedOn,
					offenceImg2, aadharImg, seizedImg, sections, owner_aadhaarNo, owner_name, owner_fatherName, owner_address, 
					owner_Age, owner_MobileNo, GenerateCase.browse_video_value, owner_image);
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			removeDialog(PROGRESS_DIALOG);
			
			// if (isNetworkEnabled) {
			ServiceHelper.final_resp = ServiceHelper.final_resp != null ? ServiceHelper.final_resp : "";

			if (ServiceHelper.final_resp.equals("1$1")) {
				TextView title = new TextView(Preview.this);
				title.setText("GHMC e-Enforcement");
				title.setBackgroundColor(Color.RED);
				title.setGravity(Gravity.CENTER);
				title.setTextColor(Color.WHITE);
				title.setTextSize(26);
				title.setTypeface(title.getTypeface(), Typeface.BOLD);
				title.setCompoundDrawablesWithIntrinsicBounds(R.drawable.traffic_small, 0, R.drawable.ghmc_small, 0);
				title.setPadding(20, 0, 20, 0);
				title.setHeight(60);

				String otp_message = "\n Already Case has been \n Generated on Same \n Tin Number or Shop Name or Aadhaar Number!!!\n";

				AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Preview.this, AlertDialog.THEME_HOLO_LIGHT);
				alertDialogBuilder.setCustomTitle(title);
				alertDialogBuilder.setIcon(R.drawable.traffic_small);
				alertDialogBuilder.setMessage(otp_message);
				alertDialogBuilder.setCancelable(false);
				alertDialogBuilder.setPositiveButton("Ok",
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

				TextView textView = (TextView) alertDialog.findViewById(android.R.id.message);
				textView.setTextSize(28);
				textView.setTypeface(textView.getTypeface(), Typeface.BOLD);
				textView.setGravity(Gravity.CENTER);

				Button btn1 = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
				btn1.setTextSize(22);
				btn1.setTextColor(Color.WHITE);
				btn1.setTypeface(btn1.getTypeface(), Typeface.BOLD);
				btn1.setBackgroundColor(Color.RED);

			} else if (ServiceHelper.final_resp.equals("0$0")) {
				TextView title = new TextView(Preview.this);
				title.setText("GHMC e-Enforcement");
				title.setBackgroundColor(Color.RED);
				title.setGravity(Gravity.CENTER);
				title.setTextColor(Color.WHITE);
				title.setTextSize(26);
				title.setTypeface(title.getTypeface(), Typeface.BOLD);
				title.setCompoundDrawablesWithIntrinsicBounds(R.drawable.traffic_small, 0, R.drawable.ghmc_small, 0);
				title.setPadding(20, 0, 20, 0);
				title.setHeight(70);

				String otp_message = "\n Ticket Not Approved by\n \t\t\t\t\t GHMC!!!\n";

				AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Preview.this, AlertDialog.THEME_HOLO_LIGHT);
				alertDialogBuilder.setCustomTitle(title);
				alertDialogBuilder.setIcon(R.drawable.traffic_small);
				alertDialogBuilder.setMessage(otp_message);
				alertDialogBuilder.setCancelable(false);
				alertDialogBuilder.setPositiveButton("Ok",
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

				TextView textView = (TextView) alertDialog.findViewById(android.R.id.message);
				textView.setTextSize(28);
				textView.setTypeface(textView.getTypeface(), Typeface.BOLD);
				textView.setGravity(Gravity.CENTER);

				Button btn1 = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
				btn1.setTextSize(22);
				btn1.setTextColor(Color.WHITE);
				btn1.setTypeface(btn1.getTypeface(), Typeface.BOLD);
				btn1.setBackgroundColor(Color.RED);

			} else if (ServiceHelper.final_resp.equals("NA$NA")) {
				TextView title = new TextView(Preview.this);
				title.setText("GHMC e-Enforcement");
				title.setBackgroundColor(Color.RED);
				title.setGravity(Gravity.CENTER);
				title.setTextColor(Color.WHITE);
				title.setTextSize(26);
				title.setTypeface(title.getTypeface(), Typeface.BOLD);
				title.setCompoundDrawablesWithIntrinsicBounds(R.drawable.traffic_small, 0, R.drawable.ghmc_small, 0);
				title.setPadding(20, 0, 20, 0);
				title.setHeight(70);

				String otp_message = "\n Ticket Genration failed\n Due to Error!!!\n";

				AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Preview.this, AlertDialog.THEME_HOLO_LIGHT);
				alertDialogBuilder.setCustomTitle(title);
				alertDialogBuilder.setIcon(R.drawable.traffic_small);
				alertDialogBuilder.setMessage(otp_message);
				alertDialogBuilder.setCancelable(false);
				alertDialogBuilder.setPositiveButton("Ok",
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

				TextView textView = (TextView) alertDialog.findViewById(android.R.id.message);
				textView.setTextSize(28);
				textView.setTypeface(textView.getTypeface(), Typeface.BOLD);
				textView.setGravity(Gravity.CENTER);

				Button btn1 = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
				btn1.setTextSize(22);
				btn1.setTextColor(Color.WHITE);
				btn1.setTypeface(btn1.getTypeface(), Typeface.BOLD);
				btn1.setBackgroundColor(Color.RED);

			} else if ((ServiceHelper.final_resp != null && ServiceHelper.final_resp.trim().length() > 10)) {
				showToast("Ticket Generation Successfull");

				// Log.i("ServiceHelper.final_resp... :",
				// ServiceHelper.print_resp);
				String printdata = ServiceHelper.final_resp.trim().split("\\$")[0];
				generateCaseFLG = true;
				SharedPreferences prefs = getSharedPreferences("printData", MODE_PRIVATE);
				SharedPreferences.Editor edit = prefs.edit();
				edit.putString("print", "" + printdata);
				edit.commit();
				Intent print = new Intent(Preview.this, PrintDisplay.class);
				startActivity(print);

			} else {
				generateCaseFLG = false;
				showToast("Ticket Generation Failed");
			}
			/*
			 * } else { generateCaseFLG = false ;
			 * showToast("Please Check your network!!!"); }
			 */
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
							gpsLatitude = "" + latitude;
							gpsLongitude = "" + longitude;

							/*
							 * runOnUiThread(new Runnable() {
							 * 
							 * @Override public void run() { // TODO
							 * Auto-generated method stub Geocoder geocoder;
							 * List<Address> addresses; geocoder = new
							 * Geocoder(GenerateCase.this, Locale.getDefault());
							 * 
							 * try { addresses =
							 * geocoder.getFromLocation(latitude, longitude, 1);
							 * 
							 * String address =
							 * addresses.get(0).getAddressLine(0); // If any
							 * additional address line present than only, check
							 * with max available address lines by
							 * getMaxAddressLineIndex() String city =
							 * addresses.get(0).getLocality(); String state =
							 * addresses.get(0).getAdminArea(); String country =
							 * addresses.get(0).getCountryName(); String
							 * postalCode = addresses.get(0).getPostalCode();
							 * String knownName =
							 * addresses.get(0).getFeatureName();
							 * 
							 * get_curent_Address =
							 * ""+address+","+city+","+state ; Log.i("address",
							 * ""+address+","+city+","+state); } catch
							 * (IOException e) { // TODO Auto-generated catch
							 * block e.printStackTrace(); } // Here 1 represent
							 * max location result to returned, by documents it
							 * recommended 1 to 5
							 * 
							 * 
							 * } });
							 */
						} else {
							latitude = 0.0;
							longitude = 0.0;
						}
					}
				}
				// if GPS Enabled get lat/long using GPS Services
				if (isGPSEnabled) {
					if (location == null) {
						m_locationlistner.requestLocationUpdates(LocationManager.GPS_PROVIDER,
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

		TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		Log.i("****", "LocationAndIMEIValues");
		Log.i("**imei**", "" + getDeviceID(telephonyManager) + "\nlat :" + latitude + "\nlong :" + longitude);
		IMEI = getDeviceID(telephonyManager);
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
	public void onBackPressed() {
		// TODO Auto-generated method stub
		// super.onBackPressed();
		measurement_buffer.setLength(0);
		measurement_buffer.delete(0, measurement_buffer.length());
		shop_buffer.setLength(0);
		shop_buffer.delete(0, shop_buffer.length());
		owner_buffer.setLength(0);
		owner_buffer.delete(0, owner_buffer.length());
		respondent_buffer.setLength(0);
		respondent_buffer.delete(0, respondent_buffer.length());
		panchayathdhar_buffer.setLength(0);
		panchayathdhar_buffer.delete(0, panchayathdhar_buffer.length());
		section_buffer.setLength(0);
		section_buffer.delete(0, section_buffer.length());
		seized_buffer.setLength(0);
		seized_buffer.delete(0, seized_buffer.length());
		finish();
	}
}
