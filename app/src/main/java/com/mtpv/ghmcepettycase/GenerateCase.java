package com.mtpv.ghmcepettycase;

import android.annotation.SuppressLint;
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
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.telephony.TelephonyManager;
import android.text.Html;
import android.text.format.DateFormat;
import android.text.format.Time;
import android.util.Base64;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.mtpv.ghmcenforcement.BuildConfig;
import com.mtpv.ghmcenforcement.R;
import com.mtpv.services.DataBase;
import com.mtpv.services.ServiceHelper;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;

@SuppressLint("SimpleDateFormat")
public class GenerateCase extends Activity implements OnItemSelectedListener,
        LocationListener {

    private static String SelPicId = "1";
    public static String date, Current_Date;
    public static String selectedCircleName = "";
    public static String UNIT_CODE = "23";
    public static String UNIT_NAME = "Hyderabad";

    public static String[][] temp_sections_master2;
    /* FOR PS NAMES */
    ArrayList<String> temp_section_codes_names_arr;
    ArrayList<String> temp_section_names_arr;
    ArrayList<String> temp_section_fine_arr;
    String[][] temp_section_name_code_arr;

    public static boolean generateCaseFLG = false;

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
    double latitude = 0.0;
    double longitude = 0.0;
    String provider = "";
    LocationManager m_locationlistner;
    android.location.Location location;
    public static String gender_txt = "M", Mobilenumber;

    public static byte[] respImage1ByteArray = null;
    public static byte[] respImage2ByteArray = null;
    public static byte[] image1ByteArray = null;

    public static EditText et_width, et_length, et_height, et_tin_no,
            et_shop_name, et_firm_owner_name, et_firm_address, aadhaar_no,
            et_name, et_father_name, et_age, et_mobileNo, et_address;

    public boolean respondentOTPFLG = false, OwnerOTPFLG = false;
    RadioButton owner_based, manager_based, shopkeeper_based, resp_male,
            resp_female, resp_other;

    RadioGroup gender_group;
    RadioGroup select_ps_type;
    RadioButton person_based, shop_based;

    Button get_aadhaar_data, add_panchayathdhars, submit_btn, select_ps_name,
            select_point_name;

    ImageView aadhaar_image, back_btn, image1, image2,
            image3, image4, image5, send_OTP;

    public static ImageView otp_btn, owner_otp_btn;

    /* FOR LOCATION NAMES */
    // ZONE_CD: ZONE_NAME: WARD_CD :CIRCLE_NAME :CIRCLE_CD :CIRCLE_NAME
    // :LOCATION_CD :LOCATION_NAME@
    ArrayList<String> loc_zoneCode_arr;
    ArrayList<String> loc_zoneName_arr;
    ArrayList<String> loc_wardCode_arr;
    ArrayList<String> loc_wardName_arr;
    ArrayList<String> loc_circleCode_arr;
    ArrayList<String> loc_circleName_arr;
    ArrayList<String> loc_code_arr;
    ArrayList<String> loc_name_arr;
    public static boolean circleFLG = false;

    HashMap<String, String> zoneMap = new HashMap<String, String>();
    HashMap<String, String> wardMap = new HashMap<String, String>();
    HashMap<String, String> circleMap = new HashMap<String, String>();
    HashMap<String, String> locationMap = new HashMap<String, String>();

    String[][] psname_name_code_arr;
    String selectedPs_code = null;
    DataBase db;
    Cursor c_psnames, pinpad_cursor, bt_cursor;
    String[] psname_code, psname_name;

    int selected_ps_name = -1;
    int selected_pointby_psname = -1;
    int selected_ward_name = -1;
    int selected_loc_name = -1;
    int ps_code_pos;

    final int PROGRESS_DIALOG = 1;
    final int WHEELER_CODE = 2;
    final int CIRCLE_NAME = 6;
    final int LOCATION_NAME = 8;
    final int PREVIOUS_HOSTORY = 10;

    final int SUB_SECTION_CODE = 3;
    String[] sub_section_code_arr_spot, sub_section_name_arr_spot;
    int selected_sub_section_code = -1;

    String[] section_code_arr_spot, section_name_arr_spot;
    int selected_section_code = -1;
    /* WHELLER DETAILS W_CODE & W_NAME */
    public static String[][] section_code_name;
    public static ArrayList<String> section_name_arr;

    public static String section_code_send = "", sub_section_code_send = "";
    Cursor c;

    String[][] pointNameBYpsname_name_code_arr;
    ArrayList<String> pointNameBy_PsName_arr;// point name for second dialog
    ArrayList<String> pointNameBy_PsName_code_arr;// point code for second

    String[][] subSecNameBysecName_code_arr;
    ArrayList<String> subSecNameBy_secName_arr;// point name for second dialog
    ArrayList<String> subSecNameBy_secName_code_arr;// point code for second

    public static int psCode_selcted = 0, pointCode_selcted = 0,
            booked_ps_code;
    final int PS_CODE_DIALOG = 5;

    Button add_sections, add_sub_section, get_previous_histry;

    /* FOR PS NAMES */
    ArrayList<String> point_codes_fr_names_arr;
    ArrayList<String> point_names_arr;
    String[][] pointname_name_code_arr;

    public static String IMEI, SimNo, run_By = "OWNER", macID;
    public static String gender_type = "M";

    public static byte[] byteArray;
    public static byte[] byteArray2;
    public static byte[] video_byteArray;
    public static String imgString = null, imgString2 = null,
            imgString3 = null, imgString4 = null, Video_sring = null;

    Button select_circle_name, select_location_name;
    public static String otp_verify_status = "N",
            owner_otp_verify_status = "N", based_on = "SHOP";

    public static String circle_no = null;
    public static String[] aadhar_details;

    private int pYear;
    private int pMonth;
    private int pDay;
    private int mSecond;
    private int mHour;
    private int mMinute;

    StringBuilder timestamp;

    public static String get_curent_Address = null;

    TextView office_name, ps_name, adhar_image_exists;
    final int PS_NAME_DIALOG = 22;

    public static String psname_code_toSet, psnameCode, psnameName;

    LinearLayout tin_layout, aadhaar_layout, ll_shop, ll_shoper_info,
            ll_person_info, ll_shop_details, ll_aadhar_details;
    Button get_tin_details;
    RadioButton tin_yes, tin_no, aadhaarYes, aadhaarNo;

    public static String tin_status = "Y", aadhaar_status = "Y",
            image_data = null, owner_image_data = null;

    public static byte[] imageByteArray = null, owner_imageByteArray = null;

    Button add_seized_items;
    public static String offenceDate, firm_regno, respondentName, shopName,
            shopOwner, shopAddress, location_tosend, idProofCode, idProofNo;

    public static ImageView section_tick, seized_tick, panchayathdhar_tick;

    public static EditText owner_aadhaarNo;
    public static EditText owner_aadhaarName;
    public static EditText owner_aadhaarMobileNo;
    public static EditText owner_aadhaarFatherName;
    public static EditText owner_aadhaarAge;
    public static EditText owner_aadhaarAddress;
    RadioButton owner_aadhaar_yes, owner_aadhaar_no;
    LinearLayout owner_aadhaar_details_layout, owner_aadhaar_layout;

    ImageView aadhaar_ownerImage;
    TextView adhar_owner_image_exists;
    Button ownerAadhaar_get;

    LinearLayout challan_type_based;
    RadioButton moving_based, standby_based, owner_gender_male,
            owner_gender_female;

    public static String ownerAdhaar_gender = "M", resp_aadhaar_gender = "M";

    public static String PID_CODE = null, PID_NAME = null, PS_CODE = null,
            PS_NAME = null, CADRE_CODE = null, CADRE_NAME = null,
            SECURITY_CD = null, GHMC_AUTH = null, CONTACT_NO = null,
            AADHAAR_DATA_FLAG = null, TIN_FLAG = null, OTP_NO_FLAG = null,
            CASHLESS_FLAG = null, MOBILE_NO_FLAG = null, RTA_DATA_FLAG = null,
            DL_DATA_FLAG = null, OFFICER_TYPE = null;

    public static String thirty_nine_B_resp = null, ghmc_prevHstry = null;

    String psName = null;

    RadioButton browse_video_yes, browse_video_no;
    RelativeLayout vedio_complete_layout;
    public static String browse_video_value = "0";

    private Uri fileUri;
    public static final int MEDIA_TYPE_VIDEO = 2;
    private static final String IMAGE_DIRECTORY_NAME = "Hello Camera";
    private static final int CAMERA_CAPTURE_VIDEO_REQUEST_CODE = 300;

    VideoView videoPreview;
    File myVideo;
    FileOutputStream fout;

    ImageButton imagebutton;

    public boolean videoPrevwFLG = false;
    String video_file;
    private int position = 0;
    private MediaController mediaController;

    CountDownTimer newtimer;

    ArrayList<String> all_psNames_arr;
    ArrayList<String> all_pointNames_arr;

    public static HashMap<String, String> psNameMap = new HashMap<String, String>();

    public static HashMap<String, String> pointNameMap = new HashMap<String, String>();

    GPSTracker gps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        setContentView(R.layout.activity_generate_case);

        SharedPreferences prefs1 = getSharedPreferences("loginValues", MODE_PRIVATE);
        String psName = prefs1.getString("PS_NAME", "");
        String officer_Name1 = prefs1.getString("PID_NAME", "");
        TextView officer_PS = (TextView)findViewById(R.id.officer_PS);
        TextView officer_Name = (TextView)findViewById(R.id.officer_Name);
        TextView companyName = (TextView) findViewById(R.id.CompanyName);
        companyName.startAnimation(AnimationUtils.loadAnimation(this, R.anim.marquee));
        officer_PS.setText(psName);
        officer_Name.setText(officer_Name1);

        Mobilenumber = "";
        // Thread.setDefaultUncaughtExceptionHandler(new UnCaughtException(GenerateCase.this));

        imgString = null;
        imgString2 = null;
        imgString3 = null;
        imgString4 = null;

        owner_image_data = null;
        image_data = null;

        byteArray = null;
        byteArray2 = null;

        image1ByteArray = null;
        owner_imageByteArray = null;

        Video_sring = null;

        latitude = 0.0;
        longitude = 0.0;
        GPSTracker.locationManager = null;

        SharedPreferences prefs = getSharedPreferences("loginValues", MODE_PRIVATE);
        psName = prefs.getString("PS_NAME", "");
        Log.i("psName :::", "PS Name :" + psName);
        PID_CODE = prefs.getString("PID_CODE", "");
        PID_NAME = prefs.getString("PID_NAME", "");
        PS_CODE = prefs.getString("PS_CODE", "");
        CADRE_CODE = prefs.getString("CADRE_CODE", "");
        CADRE_NAME = prefs.getString("CADRE_NAME", "");
        SECURITY_CD = prefs.getString("SECURITY_CD", "");
        GHMC_AUTH = prefs.getString("GHMC_AUTH", "");
        CONTACT_NO = prefs.getString("CONTACT_NO", "");
        AADHAAR_DATA_FLAG = prefs.getString("AADHAAR_DATA_FLAG", "");
        TIN_FLAG = prefs.getString("TIN_FLAG", "");
        OTP_NO_FLAG = prefs.getString("OTP_NO_FLAG", "");

        CASHLESS_FLAG = prefs.getString("CASHLESS_FLAG", "");
        MOBILE_NO_FLAG = prefs.getString("MOBILE_NO_FLAG", "");
        RTA_DATA_FLAG = prefs.getString("RTA_DATA_FLAG", "");
        DL_DATA_FLAG = prefs.getString("DL_DATA_FLAG", "");

        newtimer = new CountDownTimer(1000000000, 50) {

            public void onTick(long millisUntilFinished) {
                date = (DateFormat.format("dd/MM/yyyy hh:mm:ss", new java.util.Date()).toString());

                Calendar c1 = Calendar.getInstance();
                int mSec = c1.get(Calendar.MILLISECOND);
                SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                String strdate1 = sdf1.format(c1.getTime());
                date = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
                // id_date.setText(strdate1);
                Current_Date = strdate1;
            }

            public void onFinish() {
            }
        };
        newtimer.start();

        try {
            WifiManager manager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
            WifiInfo info = manager.getConnectionInfo();
            macID = info.getMacAddress();

            final Calendar cal = Calendar.getInstance();

            pYear = cal.get(Calendar.YEAR);
            pMonth = cal.get(Calendar.MONTH);
            pDay = cal.get(Calendar.DAY_OF_MONTH);
            mHour = cal.get(Calendar.HOUR_OF_DAY);
            mMinute = cal.get(Calendar.MINUTE);
            mSecond = cal.get(Calendar.SECOND);

        } catch (Exception e) {
            e.printStackTrace();
        }

        challan_type_based = (LinearLayout) findViewById(R.id.challan_type_based);
        moving_based = (RadioButton) findViewById(R.id.moving_based);
        standby_based = (RadioButton) findViewById(R.id.standby_based);

        tin_layout = (LinearLayout) findViewById(R.id.tin_layout);
        get_tin_details = (Button) findViewById(R.id.get_tin_details);

        section_tick = (ImageView) findViewById(R.id.section_tick);
        seized_tick = (ImageView) findViewById(R.id.seized_tick);
        panchayathdhar_tick = (ImageView) findViewById(R.id.panchayathdhars_tick);

        select_circle_name = (Button) findViewById(R.id.select_circle_name);
        select_location_name = (Button) findViewById(R.id.select_location_name);

        vedio_complete_layout = (RelativeLayout) findViewById(R.id.vedio_complete_layout);

        browse_video_yes = (RadioButton) findViewById(R.id.browse_video_yes);
        browse_video_no = (RadioButton) findViewById(R.id.browse_video_no);

        videoPreview = (VideoView) findViewById(R.id.videoPreview);


        section_tick.setVisibility(View.GONE);
        seized_tick.setVisibility(View.GONE);
        panchayathdhar_tick.setVisibility(View.GONE);

        aadhaar_layout = (LinearLayout) findViewById(R.id.aadhaar_layout);
        ll_shop = (LinearLayout) findViewById(R.id.ll_shop);
        ll_shoper_info = (LinearLayout) findViewById(R.id.ll_shoper_info);
        ll_person_info = (LinearLayout) findViewById(R.id.ll_person_info);
        ll_shop_details = (LinearLayout) findViewById(R.id.ll_shop_details);

        ll_shop_details.setVisibility(View.GONE);

        ll_aadhar_details = (LinearLayout) findViewById(R.id.ll_aadhar_details);

        tin_yes = (RadioButton) findViewById(R.id.tin_yes);
        tin_no = (RadioButton) findViewById(R.id.tin_no);

        get_tin_details.setVisibility(View.VISIBLE);
        tin_layout.setVisibility(View.VISIBLE);

        owner_aadhaarNo = (EditText) findViewById(R.id.owner_aadhaar_no);
        owner_aadhaarName = (EditText) findViewById(R.id.et_owner_name);
        owner_aadhaarFatherName = (EditText) findViewById(R.id.et_owner_father_name);
        owner_aadhaarAge = (EditText) findViewById(R.id.et_owner_age);
        owner_aadhaarMobileNo = (EditText) findViewById(R.id.et_owner_mobileNo);
        owner_aadhaarAddress = (EditText) findViewById(R.id.et_owner_address);

        owner_gender_male = (RadioButton) findViewById(R.id.resp_owner_male);
        owner_gender_female = (RadioButton) findViewById(R.id.resp_owner_female);

        adhar_owner_image_exists = (TextView) findViewById(R.id.adhar_owner_image_exists);

        owner_aadhaar_yes = (RadioButton) findViewById(R.id.owner_aadhaar_yes);
        owner_aadhaar_no = (RadioButton) findViewById(R.id.owner_aadhaarNo);

        aadhaar_ownerImage = (ImageView) findViewById(R.id.aadhaar_owner_image);

        ownerAadhaar_get = (Button) findViewById(R.id.get_owner_aadhaar_data);

        owner_aadhaar_details_layout = (LinearLayout) findViewById(R.id.ll_owner_aadhar_details);
        owner_aadhaar_details_layout.setVisibility(View.GONE);

        et_width = (EditText) findViewById(R.id.et_width);
        et_length = (EditText) findViewById(R.id.et_length);
        et_height = (EditText) findViewById(R.id.et_height);

        et_tin_no = (EditText) findViewById(R.id.et_tin_no);
        et_shop_name = (EditText) findViewById(R.id.et_shop_name);
        et_firm_owner_name = (EditText) findViewById(R.id.et_firm_owner_name);

        et_firm_address = (EditText) findViewById(R.id.et_shop_address);

        aadhaar_no = (EditText) findViewById(R.id.aadhaar_no);
        et_name = (EditText) findViewById(R.id.et_name);
        et_father_name = (EditText) findViewById(R.id.et_father_name);
        et_age = (EditText) findViewById(R.id.et_age);
        et_mobileNo = (EditText) findViewById(R.id.et_mobileNo);
        et_address = (EditText) findViewById(R.id.et_address);

        owner_aadhaar_layout = (LinearLayout) findViewById(R.id.owner_aadhaar_layout);

        gender_group = (RadioGroup) findViewById(R.id.gender_group);

        owner_based = (RadioButton) findViewById(R.id.owner_based);
        manager_based = (RadioButton) findViewById(R.id.manager_based);
        shopkeeper_based = (RadioButton) findViewById(R.id.shopkeeper_based);

        resp_male = (RadioButton) findViewById(R.id.resp_male);
        resp_female = (RadioButton) findViewById(R.id.resp_female);
        resp_other = (RadioButton) findViewById(R.id.resp_other);

        aadhaar_image = (ImageView) findViewById(R.id.aadhaar_image);

        aadhaar_image.setClickable(true);

        otp_btn = (ImageView) findViewById(R.id.otp_btn);
        owner_otp_btn = (ImageView) findViewById(R.id.owner_otp_btn);

/*		if (otp_btn.getVisibility() == View.GONE
                || owner_otp_btn.getVisibility() == View.GONE) {
			otp_verify_status = "Y";
		} else {
			otp_verify_status = "N";
		}*/

/*		if ("Y".equals(Dashboard.OtpStatus)) {

			otp_verify_status = "N";
			otp_btn.setVisibility(View.VISIBLE);
			owner_otp_btn.setVisibility(View.VISIBLE);
		} else if ("N".equals(OTP_NO_FLAG)) {
			otp_verify_status = "Y";
			otp_btn.setVisibility(View.GONE);
			owner_otp_btn.setVisibility(View.GONE);
		}*/


        if (Dashboard.OtpStatus.equalsIgnoreCase("Y")) {
            otp_btn.setVisibility(View.VISIBLE);
            owner_otp_btn.setVisibility(View.VISIBLE);
        } else {
            otp_btn.setVisibility(View.GONE);
            owner_otp_btn.setVisibility(View.GONE);
        }


        back_btn = (ImageView) findViewById(R.id.back_btn);

        get_aadhaar_data = (Button) findViewById(R.id.get_aadhaar_data);
        add_panchayathdhars = (Button) findViewById(R.id.add_panchayathdhars);
        submit_btn = (Button) findViewById(R.id.submit_btn);

        adhar_image_exists = (TextView) findViewById(R.id.adhar_image_exists);

        aadhaar_layout.setVisibility(View.VISIBLE);
        get_aadhaar_data.setVisibility(View.VISIBLE);

        aadhaarYes = (RadioButton) findViewById(R.id.aadhaar_yes);
        aadhaarNo = (RadioButton) findViewById(R.id.aadhaarNo);

        add_seized_items = (Button) findViewById(R.id.add_seized);
        add_sections = (Button) findViewById(R.id.add_sections);
        // add_sub_section = (Button)findViewById(R.id.add_sub_sections);

        get_previous_histry = (Button) findViewById(R.id.get_previous_histry);

        image1 = (ImageView) findViewById(R.id.image1);
        image2 = (ImageView) findViewById(R.id.image2);

        select_point_name = (Button) findViewById(R.id.select_pointName_btn);
        select_ps_name = (Button) findViewById(R.id.select_ps_btn);

        select_ps_name.setText(psName);

        select_ps_type = (RadioGroup) findViewById(R.id.select_ps_type);
        person_based = (RadioButton) findViewById(R.id.person_based);
        shop_based = (RadioButton) findViewById(R.id.shop_based);

        SharedPreferences witness_pref = getSharedPreferences(
                "panchayathDhars", MODE_PRIVATE);
        SharedPreferences.Editor editor_pref = witness_pref.edit();
        editor_pref.clear();
        editor_pref.apply();

        if (shop_based.isChecked() == true) {

            ll_shop.setVisibility(View.VISIBLE);
            ll_shoper_info.setVisibility(View.VISIBLE);
            ll_person_info.setVisibility(View.GONE);
            ll_aadhar_details.setVisibility(View.VISIBLE);

            if (owner_based.isChecked() == true) {
                ll_person_info.setVisibility(View.GONE);
            } else {
                ll_person_info.setVisibility(View.VISIBLE);
            }
        }

        ll_shop_details.setVisibility(View.GONE);
        ll_aadhar_details.setVisibility(View.GONE);

        GPSTracker.locationManager = null;
        gps = new GPSTracker(GenerateCase.this);

        // check if GPS enabled
        if (gps.getLocation() != null) {

            latitude = gps.getLatitude();
            longitude = gps.getLongitude();

        } else {

            gps.showSettingsAlert();
        }

        moving_based.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                challan_type_based.setVisibility(View.VISIBLE);
                person_based.setVisibility(View.GONE);
            }
        });

        standby_based.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                challan_type_based.setVisibility(View.VISIBLE);
                shop_based.setVisibility(View.GONE);
            }
        });

        final Calendar cal = Calendar.getInstance();

        pYear = cal.get(Calendar.YEAR);
        pMonth = cal.get(Calendar.MONTH);
        pDay = cal.get(Calendar.DAY_OF_MONTH);
        mHour = cal.get(Calendar.HOUR_OF_DAY);
        mMinute = cal.get(Calendar.MINUTE);
        mSecond = cal.get(Calendar.SECOND);
        updateDisplay();

        owner_aadhaar_yes.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                owner_aadhaar_details_layout.setVisibility(View.GONE);
                ownerAadhaar_get.setVisibility(View.VISIBLE);
                owner_aadhaar_layout.setVisibility(View.VISIBLE);

				/*if (otp_btn.getVisibility() == View.GONE
                        || owner_otp_btn.getVisibility() == View.GONE) {
					otp_verify_status = "Y";
				} else {
					otp_verify_status = "N";
				}

				if ("Y".equals(OTP_NO_FLAG)) {

					otp_verify_status = "N";
					otp_btn.setVisibility(View.VISIBLE);
					owner_otp_btn.setVisibility(View.VISIBLE);
				} else if ("N".equals(OTP_NO_FLAG)) {
					otp_verify_status = "Y";
					otp_btn.setVisibility(View.GONE);
					owner_otp_btn.setVisibility(View.GONE);
				}
*/


                if (Dashboard.OtpStatus.equalsIgnoreCase("Y")) {
                    otp_btn.setVisibility(View.VISIBLE);
                    owner_otp_btn.setVisibility(View.VISIBLE);
                } else {
                    otp_btn.setVisibility(View.GONE);
                    owner_otp_btn.setVisibility(View.GONE);
                }

                owner_aadhaarNo.requestFocus();

                owner_aadhaarNo.setText("");
                owner_aadhaarName.setText("");
                owner_aadhaarFatherName.setText("");
                owner_aadhaarAge.setText("");
                owner_aadhaarAddress.setText("");
                owner_aadhaarMobileNo.setText("");

                owner_aadhaarNo.setEnabled(true);
                owner_aadhaarName.setEnabled(true);
                owner_aadhaarFatherName.setEnabled(true);
                owner_aadhaarAge.setEnabled(true);
                owner_aadhaarAddress.setEnabled(true);
                owner_aadhaarMobileNo.setEnabled(true);

                adhar_owner_image_exists.setText("Owner Image");

                aadhaar_ownerImage.setImageResource(R.drawable.photo);
            }
        });

        db = new DataBase(getApplicationContext());
        try {
            db.open();
            select_ps_name.setText("" + psName);

            c_psnames = DataBase.db.rawQuery("select PS_CODE from "
                    + DataBase.psName_table + " where PS_NAME='"
                    + select_ps_name.getText().toString().trim() + "'", null);

            if (c_psnames.moveToNext()) {
                do {
                    psCode_selcted = Integer.parseInt(c_psnames.getString(0));
                } while (c_psnames.moveToNext());
            }

            if (c_psnames != null) {
                c_psnames.close();
            }
        } catch (Exception e) {
            // TODO: handle exception
            db.close();
        }

        owner_aadhaar_no.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
	/*			if (otp_btn.getVisibility() == View.GONE
						|| owner_otp_btn.getVisibility() == View.GONE) {
					otp_verify_status = "Y";
				} else {
					otp_verify_status = "N";
				}

				if ("Y".equals(OTP_NO_FLAG)) {

					otp_verify_status = "N";
					otp_btn.setVisibility(View.VISIBLE);
					owner_otp_btn.setVisibility(View.VISIBLE);
				} else if ("N".equals(OTP_NO_FLAG)) {
					otp_verify_status = "Y";
					otp_btn.setVisibility(View.GONE);
					owner_otp_btn.setVisibility(View.GONE);
				}*/

                if (Dashboard.OtpStatus.equalsIgnoreCase("Y")) {
                    otp_btn.setVisibility(View.VISIBLE);
                    owner_otp_btn.setVisibility(View.VISIBLE);
                } else {
                    otp_btn.setVisibility(View.GONE);
                    owner_otp_btn.setVisibility(View.GONE);
                }

                owner_aadhaar_details_layout.setVisibility(View.VISIBLE);
                owner_aadhaarNo.setEnabled(true);
                owner_aadhaarName.setEnabled(true);
                owner_aadhaarFatherName.setEnabled(true);
                owner_aadhaarAge.setEnabled(true);
                owner_aadhaarAddress.setEnabled(true);
                owner_aadhaarMobileNo.setEnabled(true);

                ownerAadhaar_get.setVisibility(View.GONE);
                owner_aadhaar_layout.setVisibility(View.GONE);

                owner_aadhaarNo.requestFocus();

                owner_aadhaarNo.setText("");
                owner_aadhaarName.setText("");
                owner_aadhaarFatherName.setText("");
                owner_aadhaarAge.setText("");
                owner_aadhaarAddress.setText("");
                owner_aadhaarMobileNo.setText("");

                adhar_owner_image_exists.setText("Capture Owner Photo");
                aadhaar_ownerImage.setImageResource(R.drawable.camera);
            }
        });

        ownerAadhaar_get.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                VerhoeffCheckDigit ver = new VerhoeffCheckDigit();
                if (owner_aadhaarNo.getText().toString().trim().equals("")) {
                    owner_aadhaarNo.setError(Html.fromHtml("<font color='black'>Please Enter Valid Aadhaar</font>"));
                    owner_aadhaarNo.requestFocus();
                } else if (owner_aadhaarNo.getText().toString().trim().length() > 1
                        && owner_aadhaarNo.getText().toString().trim().length() != 12) {
                    owner_aadhaarNo.setError(Html.fromHtml("<font color='black'>Please Enter Valid Aadhaar</font>"));
                    owner_aadhaarNo.requestFocus();
                } else if (owner_aadhaarNo.getText().toString().trim().length() > 1
                        && owner_aadhaarNo.getText().toString().trim().length() == 12
                        && !ver.isValid(owner_aadhaarNo.getText().toString().trim())) {
                    owner_aadhaarNo.setError(Html.fromHtml("<font color='black'>Please Enter Valid Aadhaar</font>"));
                    owner_aadhaarNo.requestFocus();
                } else if (owner_aadhaarNo.getText().toString().trim().length() > 1
                        && owner_aadhaarNo.getText().toString().trim().length() == 12
                        && ver.isValid(owner_aadhaarNo.getText().toString().trim())) {

                    if (isNetworkAvailable()) {

                        if ("Y".equals(AADHAAR_DATA_FLAG)) {
                            if (isOnline()) {
                                new Async_getOwner_AadhaarData().execute();
                            } else {
                                showToast("Please Check Your Network Connection");

                            }
                        } else {
                            owner_aadhaar_details_layout.setVisibility(View.VISIBLE);
                            owner_aadhaarName.setEnabled(true);
                            owner_aadhaarFatherName.setEnabled(true);
                            owner_aadhaarAge.setEnabled(true);
                            owner_aadhaarAddress.setEnabled(true);

                            owner_aadhaarName.setText("");
                            owner_aadhaarFatherName.setText("");
                            owner_aadhaarAge.setText("");
                            owner_aadhaarAddress.setText("");
                        }

                    } else {
                        showToast("Please Enable Data Connection!!");
                    }
                }
            }
        });

        add_seized_items.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent seize = new Intent(GenerateCase.this, DetainedItems.class);
                startActivity(seize);
            }
        });

        aadhaarYes.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
			/*	if (otp_btn.getVisibility() == View.GONE
						|| owner_otp_btn.getVisibility() == View.GONE) {
					otp_verify_status = "Y";
				} else {
					otp_verify_status = "N";
				}

				if ("Y".equals(OTP_NO_FLAG)) {

					otp_verify_status = "N";
					otp_btn.setVisibility(View.VISIBLE);
					owner_otp_btn.setVisibility(View.VISIBLE);
				} else if ("N".equals(OTP_NO_FLAG)) {
					otp_verify_status = "Y";
					otp_btn.setVisibility(View.GONE);
					owner_otp_btn.setVisibility(View.GONE);
				}*/


                if (Dashboard.OtpStatus.equalsIgnoreCase("Y")) {
                    otp_btn.setVisibility(View.VISIBLE);
                    owner_otp_btn.setVisibility(View.VISIBLE);
                } else {
                    otp_btn.setVisibility(View.GONE);
                    owner_otp_btn.setVisibility(View.GONE);
                }


                aadhaar_status = "Y";
                aadhaar_layout.setVisibility(View.VISIBLE);
                get_aadhaar_data.setVisibility(View.VISIBLE);

                aadhaar_image.setClickable(true);
                adhar_image_exists.setText("Image");

                aadhaar_no.requestFocus();
                et_name.setText("");
                et_father_name.setText("");
                et_age.setText("");
                et_mobileNo.setText("");
                et_address.setText("");
                aadhaar_no.setText("");

                et_name.setEnabled(true);
                et_father_name.setEnabled(true);
                et_age.setEnabled(true);
                et_mobileNo.setEnabled(true);
                et_address.setEnabled(true);
                aadhaar_no.setEnabled(true);

                aadhaar_image.setImageResource(R.drawable.photo);

                ll_aadhar_details.setVisibility(View.GONE);

            }
        });

        aadhaarNo.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
	/*			if (otp_btn.getVisibility() == View.GONE
						|| owner_otp_btn.getVisibility() == View.GONE) {
					otp_verify_status = "Y";
				} else {
					otp_verify_status = "N";
				}

				if ("Y".equals(OTP_NO_FLAG)) {

					otp_verify_status = "N";
					otp_btn.setVisibility(View.VISIBLE);
					owner_otp_btn.setVisibility(View.VISIBLE);
				} else if ("N".equals(OTP_NO_FLAG)) {
					otp_verify_status = "Y";
					otp_btn.setVisibility(View.GONE);
					owner_otp_btn.setVisibility(View.GONE);
				}*/

                if (Dashboard.OtpStatus.equalsIgnoreCase("Y")) {
                    otp_btn.setVisibility(View.VISIBLE);
                    owner_otp_btn.setVisibility(View.VISIBLE);
                } else {
                    otp_btn.setVisibility(View.GONE);
                    owner_otp_btn.setVisibility(View.GONE);
                }

                et_name.setEnabled(true);
                et_father_name.setEnabled(true);
                et_age.setEnabled(true);
                et_mobileNo.setEnabled(true);
                et_address.setEnabled(true);
                aadhaar_no.setEnabled(true);

                aadhaar_status = "N";
                aadhaar_layout.setVisibility(View.GONE);
                get_aadhaar_data.setVisibility(View.GONE);

                aadhaar_image.setClickable(true);
                adhar_image_exists.setText("Image");

                aadhaar_no.requestFocus();

                et_name.setText("");
                et_father_name.setText("");
                et_age.setText("");
                et_mobileNo.setText("");
                et_address.setText("");
                aadhaar_no.setText("");

                et_name.setEnabled(true);
                et_father_name.setEnabled(true);
                et_age.setEnabled(true);
                et_mobileNo.setEnabled(true);
                et_address.setEnabled(true);
                aadhaar_no.setEnabled(true);

                aadhaar_image.setImageResource(R.drawable.photo);

                ll_aadhar_details.setVisibility(View.GONE);

            }
        });

        select_circle_name.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                selected_ward_name = -1;
                // showDialog(CIRCLE_NAME);
                // new Async_getLocation().execute();
                circleFLG = false;
                if (isOnline()) {
                    new Async_getCircleName().execute();
                } else {
                    showToast("Please Check Your Network Connection");
                }
            }
        });

        select_location_name.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                selected_loc_name = -1;
                if (select_circle_name.getText().toString().trim()
                        .equals("Select Ward Name")) {
                    showToast("Please Select Ward Name");
                } else {
                    if (isOnline()) {
                        new Async_getDatabase().execute();
                    } else {
                        showToast("Please Check Your Netork Connection");
                    }
                }

            }
        });

        get_tin_details.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                videoPrevwFLG = false;
                if (et_tin_no.getText().toString().equals("")) {
                    et_tin_no.setError(Html.fromHtml("<font color='black'>Please Enter Tin Number</font>"));
                    et_tin_no.requestFocus();
                } else {
                    if (isNetworkAvailable()) {

                        et_shop_name.setEnabled(true);
                        et_firm_owner_name.setEnabled(true);
                        et_firm_address.setEnabled(true);

                        et_shop_name.setText("");
                        et_firm_owner_name.setText("");
                        et_firm_address.setText("");

                        if ("Y".equals(TIN_FLAG)) {
                            if (isOnline()) {
                                new Async_TinDetails().execute();
                            } else {
                                showToast("Please Check Your Network Connection");
                            }

                        } else {
                            ll_shop_details.setVisibility(View.VISIBLE);
                        }

                    } else {
                        showToast("Please Enable Data Connection!!");
                    }
                }

            }
        });

        aadhaarNo.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
				/*if (otp_btn.getVisibility() == View.GONE
						|| owner_otp_btn.getVisibility() == View.GONE) {
					otp_verify_status = "Y";
				} else {
					otp_verify_status = "N";
				}

				if ("Y".equals(OTP_NO_FLAG)) {

					otp_verify_status = "N";
					otp_btn.setVisibility(View.VISIBLE);
					owner_otp_btn.setVisibility(View.VISIBLE);
				} else if ("N".equals(OTP_NO_FLAG)) {
					otp_verify_status = "Y";
					otp_btn.setVisibility(View.GONE);
					owner_otp_btn.setVisibility(View.GONE);
				}*/

                if (Dashboard.OtpStatus.equalsIgnoreCase("Y")) {
                    otp_btn.setVisibility(View.VISIBLE);
                    owner_otp_btn.setVisibility(View.VISIBLE);
                } else {
                    otp_btn.setVisibility(View.GONE);
                    owner_otp_btn.setVisibility(View.GONE);
                }


                aadhaar_status = "N";

                aadhaar_no.requestFocus();

                et_name.setText("");
                et_father_name.setText("");
                et_age.setText("");
                et_mobileNo.setText("");
                et_address.setText("");
                aadhaar_no.setText("");

                adhar_image_exists.setText("Capture Image");
                aadhaar_image.setClickable(true);

                aadhaar_layout.setVisibility(View.GONE);
                get_aadhaar_data.setVisibility(View.GONE);

                aadhaar_image.setImageResource(R.drawable.camera);
                ll_aadhar_details.setVisibility(View.VISIBLE);
            }
        });

        image1.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                // getLocation();
                //image1.setRotation(0);

                gps = new GPSTracker(GenerateCase.this);
                // check if GPS enabled
                if (gps.getLocation() != null) {
                    latitude = gps.getLatitude();
                    longitude = gps.getLongitude();
                    videoPrevwFLG = false;
                    GenerateCase.SelPicId = "1";
                    image1.setRotation(0);
                    selectImage();

                } else {

                    gps.showSettingsAlert();
                }
            }
        });

        aadhaar_image.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                // getLocation();
                gps = new GPSTracker(GenerateCase.this);

                // check if GPS enabled
                if (gps.getLocation() != null) {

                    latitude = gps.getLatitude();
                    longitude = gps.getLongitude();

                    GenerateCase.SelPicId = "3";
                    selectImage();

                } else {

                    //gps.showSettingsAlert();
                    latitude = 0.0;
                    longitude = 0.0;
                }
            }
        });

        person_based.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
				/*if (otp_btn.getVisibility() == View.GONE
						|| owner_otp_btn.getVisibility() == View.GONE) {
					otp_verify_status = "Y";
				} else {
					otp_verify_status = "N";
				}
				if ("Y".equals(OTP_NO_FLAG)) {
					otp_verify_status = "N";
					otp_btn.setVisibility(View.VISIBLE);
					owner_otp_btn.setVisibility(View.VISIBLE);
				} else if ("N".equals(OTP_NO_FLAG)) {
					otp_verify_status = "Y";
					otp_btn.setVisibility(View.GONE);
					owner_otp_btn.setVisibility(View.GONE);
				}*/


                if (Dashboard.OtpStatus.equalsIgnoreCase("Y")) {
                    otp_btn.setVisibility(View.VISIBLE);
                    owner_otp_btn.setVisibility(View.VISIBLE);
                } else {
                    otp_btn.setVisibility(View.GONE);
                    owner_otp_btn.setVisibility(View.GONE);
                }

                if (gps.canGetLocation()) {
                    latitude = gps.getLatitude();
                    longitude = gps.getLongitude();
                    // \n is for new line
                    // Toast.makeText(getApplicationContext(),
                    // "Your Location is - \nLat: " + latitude + "\nLong: " +
                    // longitude, Toast.LENGTH_LONG).show();
                } else {
                    // can't get location
                    // GPS or Network is not enabled
                    // Ask user to enable GPS/network in settings
                    //gps.showSettingsAlert();
                    latitude = 0.0;
                    longitude = 0.0;
                }

                based_on = "PERSON";
                aadhaar_no.requestFocus();

                aadhaar_no.setText("");
                et_name.setText("");
                et_father_name.setText("");
                et_age.setText("");
                et_mobileNo.setText("");
                et_address.setText("");
                aadhaar_no.setText("");

                et_tin_no.setText("");
                et_shop_name.setText("");
                et_firm_owner_name.setText("");
                et_firm_address.setText("");

                owner_aadhaarNo.setText("");
                owner_aadhaarName.setText("");
                owner_aadhaarFatherName.setText("");
                owner_aadhaarAge.setText("");
                owner_aadhaarAddress.setText("");
                owner_aadhaarMobileNo.setText("");

                aadhaar_no.setEnabled(true);
                et_name.setEnabled(true);
                et_father_name.setEnabled(true);
                et_age.setEnabled(true);
                et_mobileNo.setEnabled(true);
                et_address.setEnabled(true);
                aadhaar_no.setEnabled(true);

                et_tin_no.setEnabled(true);
                et_shop_name.setEnabled(true);
                et_firm_owner_name.setEnabled(true);
                et_firm_address.setEnabled(true);

                owner_aadhaarNo.setEnabled(true);
                owner_aadhaarName.setEnabled(true);
                owner_aadhaarFatherName.setEnabled(true);
                owner_aadhaarAge.setEnabled(true);
                owner_aadhaarAddress.setEnabled(true);
                owner_aadhaarMobileNo.setEnabled(true);

                ll_shop.setVisibility(View.GONE);
                ll_shoper_info.setVisibility(View.GONE);
                ll_person_info.setVisibility(View.VISIBLE);
                ll_aadhar_details.setVisibility(View.GONE);
                aadhaar_no.setText("");
            }
        });

        shop_based.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

				/*if (otp_btn.getVisibility() == View.GONE || owner_otp_btn.getVisibility() == View.GONE) {
					otp_verify_status = "Y";
				} else {
					otp_verify_status = "N";
				}

				if ("Y".equals(OTP_NO_FLAG)) {
					otp_verify_status = "N";
					otp_btn.setVisibility(View.VISIBLE);
					owner_otp_btn.setVisibility(View.VISIBLE);
				} else if ("N".equals(OTP_NO_FLAG)) {
					otp_verify_status = "Y";
					otp_btn.setVisibility(View.GONE);
					owner_otp_btn.setVisibility(View.GONE);
				}
*/
                if (Dashboard.OtpStatus.equalsIgnoreCase("Y")) {
                    otp_btn.setVisibility(View.VISIBLE);
                    owner_otp_btn.setVisibility(View.VISIBLE);
                } else {
                    otp_btn.setVisibility(View.GONE);
                    owner_otp_btn.setVisibility(View.GONE);
                }


                if (gps.canGetLocation()) {
                    latitude = gps.getLatitude();
                    longitude = gps.getLongitude();
                    // \n is for new line
                    // Toast.makeText(getApplicationContext(),
                    // "Your Location is - \nLat: " + latitude + "\nLong: " +
                    // longitude, Toast.LENGTH_LONG).show();
                } else {
                    // can't get location
                    // GPS or Network is not enabled
                    // Ask user to enable GPS/network in settings
                    gps.showSettingsAlert();
                }

                based_on = "SHOP";
                et_tin_no.requestFocus();

                aadhaar_no.setText("");
                et_name.setText("");
                et_father_name.setText("");
                et_age.setText("");
                et_mobileNo.setText("");
                et_address.setText("");
                aadhaar_no.setText("");

                et_tin_no.setText("");
                et_shop_name.setText("");
                et_firm_owner_name.setText("");
                et_firm_address.setText("");

                owner_aadhaarNo.setText("");
                owner_aadhaarName.setText("");
                owner_aadhaarFatherName.setText("");
                owner_aadhaarAge.setText("");
                owner_aadhaarAddress.setText("");
                owner_aadhaarMobileNo.setText("");

                aadhaar_no.setEnabled(true);
                et_name.setEnabled(true);
                et_father_name.setEnabled(true);
                et_age.setEnabled(true);
                et_mobileNo.setEnabled(true);
                et_address.setEnabled(true);
                aadhaar_no.setEnabled(true);

                et_tin_no.setEnabled(true);
                et_shop_name.setEnabled(true);
                et_firm_owner_name.setEnabled(true);
                et_firm_address.setEnabled(true);

                owner_aadhaarNo.setEnabled(true);
                owner_aadhaarName.setEnabled(true);
                owner_aadhaarFatherName.setEnabled(true);
                owner_aadhaarAge.setEnabled(true);
                owner_aadhaarAddress.setEnabled(true);
                owner_aadhaarMobileNo.setEnabled(true);

                ll_shop.setVisibility(View.VISIBLE);
                ll_shoper_info.setVisibility(View.VISIBLE);
                ll_person_info.setVisibility(View.GONE);
                aadhaar_no.setText("");
                ll_aadhar_details.setVisibility(View.GONE);
            }
        });

        videoPrevwFLG = false;

        tin_yes.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
	/*			if (otp_btn.getVisibility() == View.GONE
						|| owner_otp_btn.getVisibility() == View.GONE) {
					otp_verify_status = "Y";
				} else {
					otp_verify_status = "N";
				}

				if ("Y".equals(OTP_NO_FLAG)) {

					otp_verify_status = "N";
					otp_btn.setVisibility(View.VISIBLE);
					owner_otp_btn.setVisibility(View.VISIBLE);
				} else if ("N".equals(OTP_NO_FLAG)) {
					otp_verify_status = "Y";
					otp_btn.setVisibility(View.GONE);
					owner_otp_btn.setVisibility(View.GONE);
				}*/

                if (Dashboard.OtpStatus.equalsIgnoreCase("Y")) {
                    otp_btn.setVisibility(View.VISIBLE);
                    owner_otp_btn.setVisibility(View.VISIBLE);
                } else {
                    otp_btn.setVisibility(View.GONE);
                    owner_otp_btn.setVisibility(View.GONE);
                }
                tin_status = "Y";

                et_tin_no.requestFocus();

                et_tin_no.setText("");
                et_shop_name.setText("");
                et_firm_owner_name.setText("");
                et_firm_address.setText("");

                get_tin_details.setVisibility(View.VISIBLE);
                tin_layout.setVisibility(View.VISIBLE);

                ll_shop_details.setVisibility(View.GONE);

                et_tin_no.setEnabled(true);
                et_shop_name.setEnabled(true);
                et_firm_owner_name.setEnabled(true);
                et_firm_address.setEnabled(true);

            }
        });

        tin_no.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
				/*if (otp_btn.getVisibility() == View.GONE
						|| owner_otp_btn.getVisibility() == View.GONE) {
					otp_verify_status = "Y";
				} else {
					otp_verify_status = "N";
				}

				if ("Y".equals(OTP_NO_FLAG)) {

					otp_verify_status = "N";
					otp_btn.setVisibility(View.VISIBLE);
					owner_otp_btn.setVisibility(View.VISIBLE);
				} else if ("N".equals(OTP_NO_FLAG)) {
					otp_verify_status = "Y";
					otp_btn.setVisibility(View.GONE);
					owner_otp_btn.setVisibility(View.GONE);
				}*/

                if (Dashboard.OtpStatus.equalsIgnoreCase("Y")) {
                    otp_btn.setVisibility(View.VISIBLE);
                    owner_otp_btn.setVisibility(View.VISIBLE);
                } else {
                    otp_btn.setVisibility(View.GONE);
                    owner_otp_btn.setVisibility(View.GONE);
                }

                tin_status = "N";

                et_tin_no.requestFocus();

                get_tin_details.setVisibility(View.GONE);
                tin_layout.setVisibility(View.GONE);

                et_tin_no.setText("");
                et_shop_name.setText("");
                et_firm_owner_name.setText("");
                et_firm_address.setText("");

                et_tin_no.setEnabled(true);
                et_shop_name.setEnabled(true);
                et_firm_owner_name.setEnabled(true);
                et_firm_address.setEnabled(true);

                ll_shop_details.setVisibility(View.VISIBLE);
            }
        });

        get_previous_histry.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (isOnline()) {
                    new Async_getPrevHistry().execute();
                } else {
                    showToast("Please Check Your Network Connection");
                }
            }
        });

        add_sections.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (isOnline()) {
                    new Async_SubSections().execute();
                } else {
                    showToast("Please Check Your Network Connection");
                }
            }
        });

        image2.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                // getLocation();

                //image2.setRotation(0);

                gps = new GPSTracker(GenerateCase.this);

                // check if GPS enabled
                if (gps.getLocation() != null) {

                    latitude = gps.getLatitude();
                    longitude = gps.getLongitude();

                    GenerateCase.SelPicId = "2";
                    image2.setRotation(0);
                    selectImage();
                    // \n is for new line
                    // Toast.makeText(getApplicationContext(),
                    // "Your Location is - \nLat: " + latitude + "\nLong: " +
                    // longitude, Toast.LENGTH_LONG).show();
                } else {
                    // can't get location
                    // GPS or Network is not enabled
                    // Ask user to enable GPS/network in settings
                    gps.showSettingsAlert();
                }
            }
        });

        // GetDatabaseValues();

        /*
         * if (!select_ps_name.getText().toString().trim().equals("")) { new
         * Async_getPointNameByPsName().execute(); }
         */

        try {
            TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
            IMEI = getDeviceID(telephonyManager);

            if (telephonyManager.getSimState() != TelephonyManager.SIM_STATE_ABSENT) {
                SimNo = "" + telephonyManager.getSimSerialNumber();
            } else {
                SimNo = "";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        otp_btn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                String tempContactNumber = et_mobileNo.getText().toString()
                        .trim();

                Mobilenumber = tempContactNumber;

                if (et_mobileNo.getText().toString().trim().equals("")) {
                    et_mobileNo.setError(Html
                            .fromHtml("<font color='black'>Please Enter Mobile</font>"));
                    et_mobileNo.requestFocus();
                    respondentOTPFLG = false;
                } else if (tempContactNumber != null
                        && tempContactNumber.trim().length() > 1
                        && tempContactNumber.trim().length() != 10) {
                    et_mobileNo.setError(Html
                            .fromHtml("<font color='black'>Enter Valid mobile number!!</font>"));
                    et_mobileNo.requestFocus();
                    respondentOTPFLG = false;
                } else if ((tempContactNumber.charAt(0) != '7')
                        || (tempContactNumber.charAt(0) != '8')
                        || (tempContactNumber.charAt(0) != '9')) {
                    respondentOTPFLG = true;
                    if ("Y".equals(OTP_NO_FLAG)) {
                        if (isOnline()) {
                            new Async_getOTP().execute();
                        } else {
                            showToast("Please Check Your Network Connection");
                        }
                    }

                } else {
                    et_mobileNo.setError(Html
                            .fromHtml("<font color='black'>Enter Valid mobile number!!</font>"));
                    et_mobileNo.requestFocus();
                    respondentOTPFLG = false;
                }
            }
        });

        owner_otp_btn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                String tempContactNumber = owner_aadhaarMobileNo.getText()
                        .toString().trim();

                if (owner_aadhaarMobileNo.getText().toString().trim()
                        .equals("")) {
                    owner_aadhaarMobileNo.setError(Html
                            .fromHtml("<font color='black'>Please Enter Mobile</font>"));
                    owner_aadhaarMobileNo.requestFocus();
                    OwnerOTPFLG = false;
                } else if (tempContactNumber.trim().length() > 1 && tempContactNumber.trim().length() != 10) {
                    owner_aadhaarMobileNo.setError(Html
                            .fromHtml("<font color='black'>Enter Valid mobile number!!</font>"));
                    owner_aadhaarMobileNo.requestFocus();
                    OwnerOTPFLG = false;
                } else if ("6".equals(String.valueOf(tempContactNumber.charAt(0)))
                        || "7".equals(String.valueOf(tempContactNumber.charAt(0)))
                        || "8".equals(String.valueOf(tempContactNumber.charAt(0)))
                        || "9".equals(String.valueOf(tempContactNumber.charAt(0)))) {
                    OwnerOTPFLG = true;
                    //		if ("Y".equals(OTP_NO_FLAG)) {
                    if (isOnline()) {
                        new Async_getOTP().execute();
                    } else {
                        showToast("Please Check Your Network Connection");
                    }
                    //}

                } else {
                    owner_aadhaarMobileNo.setError(Html
                            .fromHtml("<font color='black'>Enter Valid mobile number!!</font>"));
                    owner_aadhaarMobileNo.requestFocus();
                }
            }
        });

        owner_based.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                run_By = "OWNER";

                et_tin_no.requestFocus();

                owner_otp_btn.setVisibility(View.VISIBLE);

                aadhaar_layout.setVisibility(View.VISIBLE);
                get_aadhaar_data.setVisibility(View.VISIBLE);

                ll_person_info.setVisibility(View.GONE);

                aadhaar_image.setClickable(true);
                adhar_image_exists.setText("Image");

                et_name.setText("");
                et_father_name.setText("");
                et_age.setText("");
                et_mobileNo.setText("");
                et_address.setText("");
                aadhaar_no.setText("");

                aadhaar_image.setImageResource(R.drawable.photo);
            }
        });

        aadhaar_ownerImage.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                // getLocation();

                gps = new GPSTracker(GenerateCase.this);

                // check if GPS enabled
                if (gps.getLocation() != null) {

                    latitude = gps.getLatitude();
                    longitude = gps.getLongitude();

                    GenerateCase.SelPicId = "4";
                    selectImage();
                    // \n is for new line
                    // Toast.makeText(getApplicationContext(),
                    // "Your Location is - \nLat: " + latitude + "\nLong: " +
                    // longitude, Toast.LENGTH_LONG).show();
                } else {
                    // can't get location
                    // GPS or Network is not enabled
                    // Ask user to enable GPS/network in settings
                    gps.showSettingsAlert();
                }

            }
        });

        manager_based.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                run_By = "MANAGER";

                et_tin_no.requestFocus();

                owner_otp_btn.setVisibility(View.GONE);

                aadhaar_layout.setVisibility(View.VISIBLE);
                get_aadhaar_data.setVisibility(View.VISIBLE);

                ll_person_info.setVisibility(View.VISIBLE);

                aadhaar_image.setClickable(true);
                adhar_image_exists.setText("Capture Respondent Image");

                et_name.setText("");
                et_father_name.setText("");
                et_age.setText("");
                et_mobileNo.setText("");
                et_address.setText("");
                aadhaar_no.setText("");

                aadhaar_image.setImageResource(R.drawable.photo);
            }
        });

        shopkeeper_based.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                run_By = "SHOPKEEPER";

                et_tin_no.requestFocus();

                owner_otp_btn.setVisibility(View.GONE);

                aadhaar_layout.setVisibility(View.VISIBLE);
                get_aadhaar_data.setVisibility(View.VISIBLE);

                ll_person_info.setVisibility(View.VISIBLE);

                aadhaar_image.setClickable(true);
                adhar_image_exists.setText("Capture Respondent Image");

                et_name.setText("");
                et_father_name.setText("");
                et_age.setText("");
                et_mobileNo.setText("");
                et_address.setText("");
                aadhaar_no.setText("");

                aadhaar_image.setImageResource(R.drawable.photo);
            }
        });

        resp_male.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                gender_type = "M";
            }
        });

        resp_male.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                gender_type = "F";
            }
        });
        resp_male.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                gender_type = "O";
            }
        });

        select_ps_name.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (isOnline()) {
                    new Async_GetPS_DB().execute();
                } else {
                    showToast("Please Check Your Network Connection");
                }
            }
        });

        select_point_name.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new Async_task_GetPointNames().execute();
            }
        });

        get_aadhaar_data.setOnClickListener(new OnClickListener() {

            @SuppressWarnings({"rawtypes", "unchecked"})
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                VerhoeffCheckDigit ver = new VerhoeffCheckDigit();
                videoPrevwFLG = false;
                GenerateCase.otp_verify_status = "N";

                if (aadhaar_no.getText().toString().trim().equals("")) {
                    aadhaar_no.setError(Html
                            .fromHtml("<font color='black'>Please Enter Valid Aadhaar</font>"));
                    aadhaar_no.requestFocus();
                } else if (aadhaar_no.getText().toString().trim().length() > 1
                        && aadhaar_no.getText().toString().trim().length() != 12) {
                    aadhaar_no.setError(Html
                            .fromHtml("<font color='black'>Please Enter Valid Aadhaar</font>"));
                    aadhaar_no.requestFocus();
                } else if (aadhaar_no.getText().toString().trim().length() > 1
                        && aadhaar_no.getText().toString().trim().length() == 12
                        && !ver.isValid(aadhaar_no.getText().toString().trim())) {
                    aadhaar_no.setError(Html
                            .fromHtml("<font color='black'>Please Enter Valid Aadhaar</font>"));
                    aadhaar_no.requestFocus();
                } else if (aadhaar_no.getText().toString().trim().length() > 1
                        && aadhaar_no.getText().toString().trim().length() == 12
                        && ver.isValid(aadhaar_no.getText().toString().trim())) {

                    if (isNetworkAvailable()) {

                        if ("Y".equals(AADHAAR_DATA_FLAG)) {

                            if (isOnline()) {
                                new Async_getAadhaarData().execute();
                            } else {
                                showToast("Please Check Your Netowrk Connection");
                            }

                        } else {
                            ll_aadhar_details.setVisibility(View.VISIBLE);
                            et_name.setEnabled(true);
                            et_father_name.setEnabled(true);
                            et_age.setEnabled(true);
                            et_address.setEnabled(true);
                            et_mobileNo.setEnabled(true);

                            et_name.setText("");
                            et_father_name.setText("");
                            et_age.setText("");
                            et_address.setText("");
                            et_mobileNo.setText("");
                        }


                    } else {
                        showToast("Please Enable Data Connection!!");
                    }
                }

            }
        });

        vedio_complete_layout.setVisibility(View.GONE);

        browse_video_yes.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                browse_video_value = "1";
                videoPrevwFLG = true;
                vedio_complete_layout.setVisibility(View.VISIBLE);

              /*  Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);

                fileUri = getOutputMediaFileUri(MEDIA_TYPE_VIDEO);
                // set video quality
                intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
                intent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 5);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri); // set the image file
                // start the video capture Intent
                startActivityForResult(intent, CAMERA_CAPTURE_VIDEO_REQUEST_CODE);*/


                if (Build.VERSION.SDK_INT <= 23) {
                    Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);

                    fileUri = getOutputMediaFileUri(MEDIA_TYPE_VIDEO);

                    // set video quality
                    intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
                    intent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 5);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri); // set the image file
                    // name

                    // start the video capture Intent
                    startActivityForResult(intent, CAMERA_CAPTURE_VIDEO_REQUEST_CODE);
                } else {
                    Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                    fileUri = getOutputMediaFileUri(MEDIA_TYPE_VIDEO);
                    intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
                    intent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 5);
                    //File f = new File(android.os.Environment.getExternalStorageDirectory(), "temp.jpg");
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(GenerateCase.this,
                            BuildConfig.APPLICATION_ID + ".provider", new File(fileUri.getPath())));
                    intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    startActivityForResult(intent, CAMERA_CAPTURE_VIDEO_REQUEST_CODE);
                }


            }
        });

        browse_video_no.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                browse_video_value = "0";
                videoPrevwFLG = false;
                vedio_complete_layout.setVisibility(View.GONE);
            }
        });

        // Set the media controller buttons
        if (mediaController == null) {
            mediaController = new MediaController(GenerateCase.this);
            // Set the videoView that acts as the anchor for the
            // MediaController.
            mediaController.setAnchorView(videoPreview);
            // Set MediaController for VideoView
            videoPreview.setMediaController(mediaController);
        }

        add_panchayathdhars.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                Intent panchayathdhar = new Intent(GenerateCase.this, WitnessForm.class);
                startActivity(panchayathdhar);
            }
        });

        submit_btn.setOnClickListener(new OnClickListener() {

            @SuppressWarnings("unused")
            @Override
            public void onClick(View arg0) {

                try {
                    GPSTracker.locationManager = null;
                    gps = new GPSTracker(GenerateCase.this);
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

                // TODO Auto-generated method stub
                VerhoeffCheckDigit ver = new VerhoeffCheckDigit();

                String tempContactNumber = et_mobileNo.getText().toString().trim();
                String detainedItems = "" + DetainedItems.detItems;


                if (select_ps_name.getText().toString().trim().equals("Select PS Name")) {
                    showToast("Please Select PS Name");

                } else if (select_point_name.getText().toString().trim().equals("Select Point Name")) {
                    showToast("Please Select Point Name");

                } else if (circleFLG && selectedCircleName.trim().length() == 0) {
                    showToast("Please Select location Name");

                } else if (imgString == null && imgString2 == null) {
                    showToast("Please Capture Enforcement Images");

                } else {
                    if (based_on.equals("SHOP") && !based_on.equals("PERSON")) {
                        if (run_By.equals("OWNER")) {
                            // TIN YES OR NO VALIDATIONS
                            if (tin_yes.isChecked() == true) {
                                if (et_tin_no.getText().toString().trim().equals("")) {
                                    et_tin_no.requestFocus();
                                    // et_tin_no.setError(Html.fromHtml("<font color='black'>Please Enter Trade Licence Number</font>"));
                                    showToast("Please Enter Tin No");
                                } else if (et_shop_name.getText().toString().trim().equals("")) {
                                    et_shop_name.setError(Html.fromHtml("<font color='black'>Please Enter Shop Name</font>"));
                                    et_shop_name.requestFocus();

                                } else if (et_firm_owner_name.getText().toString().trim().equals("")) {
                                    et_firm_owner_name.setError(Html.fromHtml("<font color='black'>Please Enter Shop Owner Name</font>"));
                                    et_firm_owner_name.requestFocus();

                                } else if (et_firm_address.getText().toString().trim().equals("")) {
                                    et_firm_address.setError(Html.fromHtml("<font color='black'>Please Enter Shop Owner Address</font>"));
                                    et_firm_address.requestFocus();

                                } else if (owner_aadhaar_yes.isChecked() == true
                                        && tin_yes.isChecked() == true && !based_on.equals("PERSON")
                                        && based_on.equals("SHOP")) {


                                    if (owner_aadhaarNo.getText().toString().trim().equals("")) {
                                        owner_aadhaarNo.setError(Html.fromHtml("<font color='black'>Please Enter Aadhaar Number</font>"));
                                        owner_aadhaarNo.requestFocus();

                                    } else if (owner_aadhaarNo.getText().toString().trim().length() > 1
                                            && owner_aadhaarNo.getText().toString().trim().length() != 12) {
                                        owner_aadhaarNo.setError(Html.fromHtml("<font color='black'>Please Enter Valid Aadhaar</font>"));
                                        owner_aadhaarNo.requestFocus();

                                    } else if (owner_aadhaarNo.getText().toString().trim().length() > 1
                                            && owner_aadhaarNo.getText().toString().trim().length() == 12
                                            && !ver.isValid(owner_aadhaarNo.getText().toString().trim())) {
                                        owner_aadhaarNo.setError(Html.fromHtml("<font color='black'>Please Enter Valid Aadhaar</font>"));
                                        owner_aadhaarNo.requestFocus();

                                    } else if (owner_aadhaarName.getText().toString().trim().equals("")) {
                                        owner_aadhaarName.setError(Html.fromHtml("<font color='black'>Please Enter Name</font>"));
                                        owner_aadhaarName.requestFocus();

                                    } else if (owner_aadhaarFatherName.getText().toString().trim().equals("")) {
                                        owner_aadhaarFatherName.setError(Html.fromHtml("<font color='black'>Please Enter Owner Father Number</font>"));
                                        owner_aadhaarFatherName.requestFocus();

                                    } else if (owner_aadhaarAge.getText().toString().trim().equals("")) {
                                        owner_aadhaarAge.setError(Html.fromHtml("<font color='black'>Please Enter Owner Age</font>"));
                                        owner_aadhaarAge.requestFocus();

                                    } else if (owner_aadhaarMobileNo.getText().toString().trim().equals("")) {
                                        owner_aadhaarMobileNo.setError(Html.fromHtml("<font color='black'>Please Enter Owner Mobile No</font>"));
                                        owner_aadhaarMobileNo.requestFocus();

                                    } else if (owner_aadhaarAddress.getText().toString().trim().equals("")) {
                                        owner_aadhaarAddress.setError(Html.fromHtml("<font color='black'>Please Enter Owner Address</font>"));
                                        owner_aadhaarAddress.requestFocus();

                                    } else if (aadhaar_ownerImage.getDrawable().getConstantState() == getResources().getDrawable(R.drawable.camera).getConstantState()
                                            || aadhaar_ownerImage.getDrawable().getConstantState() == getResources().getDrawable(R.drawable.photo).getConstantState()) {
                                        showToast("Please Select Owner Image");

                                    } else if (Sections.checkedList.isEmpty()) {
                                        showToast("Please Select Atleast one Section");

                                    } else if (DetainedItems.detItems.toString().trim().length() == 0) {
                                        showToast("Please Enter Items Found on Encroachment");
                                    }/*
                                     * else if (otp_verify_status.equals("N")) {
                                     * showToast("Please Verify OTP Number");
                                     * owner_aadhaarMobileNo.requestFocus(); }
                                     */ else {
                                        if (isOnline()) {
                                            new Async_PreviewDetails().execute();
                                        } else {
                                            showToast("Please Check Your Network connection");
                                        }
                                    }

                                } else if (owner_aadhaar_no.isChecked() && tin_yes.isChecked()) {
                                    if (owner_aadhaarName.getText().toString().trim().equals("")) {
                                        owner_aadhaarName.setError(Html.fromHtml("<font color='black'>Please Enter Name</font>"));
                                        owner_aadhaarName.requestFocus();

                                    } else if (owner_aadhaarFatherName.getText().toString().trim().equals("")) {
                                        owner_aadhaarFatherName.setError(Html.fromHtml("<font color='black'>Please Enter Owner Father Number</font>"));
                                        owner_aadhaarFatherName.requestFocus();

                                    } else if (owner_aadhaarAge.getText().toString().trim().equals("")) {
                                        owner_aadhaarAge.setError(Html.fromHtml("<font color='black'>Please Enter Owner Age</font>"));
                                        owner_aadhaarAge.requestFocus();

                                    } else if (owner_aadhaarMobileNo.getText().toString().trim().equals("")) {
                                        owner_aadhaarMobileNo.setError(Html.fromHtml("<font color='black'>Please Enter Owner Mobile No</font>"));
                                        owner_aadhaarMobileNo.requestFocus();

                                    } else if (owner_aadhaarAddress.getText().toString().trim().equals("")) {
                                        owner_aadhaarAddress.setError(Html.fromHtml("<font color='black'>Please Enter Owner Address</font>"));
                                        owner_aadhaarAddress.requestFocus();
                                    } /*
                                     * else if
                                     * (aadhaar_ownerImage.getDrawable().
                                     * getConstantState() ==
                                     * getResources().getDrawable
                                     * (R.drawable.camera).getConstantState()
                                     * ||aadhaar_ownerImage
                                     * .getDrawable().getConstantState() ==
                                     * getResources
                                     * ().getDrawable(R.drawable.photo
                                     * ).getConstantState()) {
                                     * showToast("Please Select Respondent Image"
                                     * ); }
                                     */ else if (Sections.checkedList.isEmpty()) {
                                        showToast("Please Select Atleast one Section");

                                    } else if (DetainedItems.detItems.toString().trim().length() == 0) {
                                        showToast("Please Enter Items Found on Encroachment");
                                    } else if ("Y".equals(Dashboard.OtpStatus.trim()) && otp_verify_status.equals("N")) {
                                        showToast("Please Verify OTP Number");
                                    } else {
                                        if (isOnline()) {
                                            new Async_PreviewDetails().execute();
                                        } else {
                                            showToast("Please Check Your Network connection");
                                        }
                                    }
                                }
                            } else if (tin_no.isChecked() == true) {

                                if (et_shop_name.getText().toString().trim().equals("")) {
                                    et_shop_name.setError(Html.fromHtml("<font color='black'>Please Enter Shop Name</font>"));
                                    et_shop_name.requestFocus();

                                } else if (et_firm_owner_name.getText().toString().trim().equals("")) {
                                    et_firm_owner_name.setError(Html.fromHtml("<font color='black'>Please Enter Shop Owner Name</font>"));
                                    et_firm_owner_name.requestFocus();

                                } else if (et_firm_address.getText().toString().trim().equals("")) {
                                    et_firm_address.setError(Html.fromHtml("<font color='black'>Please Enter Shop Owner Address</font>"));
                                    et_firm_address.requestFocus();

                                } else if (owner_aadhaar_yes.isChecked() == true && tin_no.isChecked() == true) {

                                    if (owner_aadhaarNo.getText().toString().trim().equals("")) {
                                        owner_aadhaarNo.setError(Html.fromHtml("<font color='black'>Please Enter Aadhaar Number</font>"));
                                        owner_aadhaarNo.requestFocus();

                                    } else if (owner_aadhaarNo.getText().toString().trim().length() > 1
                                            && owner_aadhaarNo.getText().toString().trim().length() != 12) {
                                        owner_aadhaarNo.setError(Html.fromHtml("<font color='black'>Please Enter Valid Aadhaar</font>"));
                                        owner_aadhaarNo.requestFocus();

                                    } else if (owner_aadhaarNo.getText().toString().trim().length() > 1
                                            && owner_aadhaarNo.getText().toString().trim().length() == 12
                                            && !ver.isValid(owner_aadhaarNo.getText().toString().trim())) {
                                        owner_aadhaarNo.setError(Html.fromHtml("<font color='black'>Please Enter Valid Aadhaar</font>"));
                                        owner_aadhaarNo.requestFocus();

                                    } else if (owner_aadhaarName.getText().toString().trim().equals("")) {
                                        owner_aadhaarName.setError(Html.fromHtml("<font color='black'>Please Enter Name</font>"));
                                        owner_aadhaarName.requestFocus();

                                    } else if (owner_aadhaarFatherName.getText().toString().trim().equals("")) {
                                        owner_aadhaarFatherName.setError(Html.fromHtml("<font color='black'>Please Enter Owner Father Number</font>"));
                                        owner_aadhaarFatherName.requestFocus();

                                    } else if (owner_aadhaarAge.getText().toString().trim().equals("")) {
                                        owner_aadhaarAge.setError(Html.fromHtml("<font color='black'>Please Enter Owner Age</font>"));
                                        owner_aadhaarAge.requestFocus();

                                    } else if (owner_aadhaarMobileNo.getText().toString().trim().equals("")) {
                                        owner_aadhaarMobileNo.setError(Html.fromHtml("<font color='black'>Please Enter Owner Mobile No</font>"));
                                        owner_aadhaarMobileNo.requestFocus();

                                    } else if (owner_aadhaarAddress.getText().toString().trim().equals("")) {
                                        owner_aadhaarAddress.setError(Html.fromHtml("<font color='black'>Please Enter Owner Address</font>"));
                                        owner_aadhaarAddress.requestFocus();

                                    } else if (aadhaar_ownerImage.getDrawable().getConstantState() == getResources().getDrawable(R.drawable.camera).getConstantState()
                                            || aadhaar_ownerImage.getDrawable().getConstantState() == getResources().getDrawable(R.drawable.photo).getConstantState()) {
                                        showToast("Please Select Respondent Image");
                                    }/*
                                     * else if (otp_verify_status.equals("N")) {
                                     * showToast("Please Verify OTP Number");
                                     * owner_aadhaarMobileNo.requestFocus(); }
                                     */ else if ("Y".equals(Dashboard.OtpStatus.trim()) && otp_verify_status.equals("N")) {
                                        showToast("Please Verify OTP Number");
                                    } else if (DetainedItems.detItems.toString().trim().length() == 0) {
                                        showToast("Please Enter Items Found on Encroachment");

                                    } else if (Sections.checkedList.isEmpty()) {
                                        showToast("Please Select atleast one Section");
                                    } else if (DetainedItems.detItems.toString().trim().length() == 0) {
                                        showToast("Please Enter Items Found on Encroachment");

                                    } else {
                                        if (isOnline()) {
                                            new Async_PreviewDetails().execute();
                                        } else {
                                            showToast("Please Check Your Network connection");
                                        }
                                    }
                                } else if (owner_aadhaar_no.isChecked() == true
                                        && tin_no.isChecked() == true) {

                                    if (owner_aadhaarName.getText().toString()
                                            .trim().equals("")) {
                                        owner_aadhaarName.setError(Html
                                                .fromHtml("<font color='black'>Please Enter Name</font>"));
                                        owner_aadhaarName.requestFocus();
                                    } else if (owner_aadhaarFatherName
                                            .getText().toString().trim()
                                            .equals("")) {
                                        owner_aadhaarFatherName.setError(Html
                                                .fromHtml("<font color='black'>Please Enter Owner Father Number</font>"));
                                        owner_aadhaarFatherName.requestFocus();
                                    } else if (owner_aadhaarAge.getText()
                                            .toString().trim().equals("")) {
                                        owner_aadhaarAge.setError(Html
                                                .fromHtml("<font color='black'>Please Enter Owner Age</font>"));
                                        owner_aadhaarAge.requestFocus();
                                    } else if (owner_aadhaarMobileNo.getText()
                                            .toString().trim().equals("")) {
                                        owner_aadhaarMobileNo.setError(Html
                                                .fromHtml("<font color='black'>Please Enter Owner Mobile No</font>"));
                                        owner_aadhaarMobileNo.requestFocus();
                                    } else if (owner_aadhaarAddress.getText()
                                            .toString().trim().equals("")) {
                                        owner_aadhaarAddress.setError(Html
                                                .fromHtml("<font color='black'>Please Enter Owner Address</font>"));
                                        owner_aadhaarAddress.requestFocus();
                                    } else if (aadhaar_ownerImage.getDrawable()
                                            .getConstantState() == getResources()
                                            .getDrawable(R.drawable.camera)
                                            .getConstantState()
                                            || aadhaar_ownerImage.getDrawable()
                                            .getConstantState() == getResources()
                                            .getDrawable(
                                                    R.drawable.photo)
                                            .getConstantState()) {
                                        showToast("Please Select Respondent Image");
                                    }/*
                                     * else if (otp_verify_status.equals("N")) {
                                     * showToast("Please Verify OTP Number");
                                     * owner_aadhaarMobileNo.requestFocus(); }
                                     */ else if ("Y".equals(Dashboard.OtpStatus.trim()) && otp_verify_status.equals("N")) {
                                        showToast("Please Verify OTP Number");
                                    } else if (Sections.checkedList.isEmpty()) {
                                        showToast("Please Select atleast one Section");
                                    } else if (DetainedItems.detItems
                                            .toString().trim().length() == 0) {
                                        showToast("Please Enter Items Found on Encroachment");
                                    } else {
                                        if (isOnline()) {
                                            new Async_PreviewDetails().execute();
                                        } else {
                                            showToast("Please Check Your Network connection");
                                        }
                                    }
                                }
                            }
                        } else if (run_By.equals("MANAGER")) { // MANAGER
                            // DETAILS
                            // TIN YES OR NO VALIDATIONS
                            if (tin_yes.isChecked() == true) {
                                if (et_tin_no.getText().toString().trim()
                                        .equals("")) {
                                    et_tin_no.requestFocus();
                                    // et_tin_no.setError(Html.fromHtml("<font color='black'>Please Enter Trade Licence Number</font>"));
                                    showToast("Please Enter Tin No");
                                } else if (et_shop_name.getText().toString()
                                        .trim().equals("")) {
                                    et_shop_name.setError(Html
                                            .fromHtml("<font color='black'>Please Enter Shop Name</font>"));
                                    et_shop_name.requestFocus();
                                } else if (et_firm_owner_name.getText()
                                        .toString().trim().equals("")) {
                                    et_firm_owner_name.setError(Html
                                            .fromHtml("<font color='black'>Please Enter Shop Owner Name</font>"));
                                    et_firm_owner_name.requestFocus();
                                } else if (et_firm_address.getText().toString()
                                        .trim().equals("")) {
                                    et_firm_address.setError(Html
                                            .fromHtml("<font color='black'>Please Enter Shop Owner Address</font>"));
                                    et_firm_address.requestFocus();
                                } else if (owner_aadhaar_yes.isChecked() == true) {

                                    if (owner_aadhaarNo.getText().toString()
                                            .trim().equals("")) {
                                        owner_aadhaarNo.setError(Html
                                                .fromHtml("<font color='black'>Please Enter Aadhaar Number</font>"));
                                        owner_aadhaarNo.requestFocus();
                                    } else if (owner_aadhaarNo.getText()
                                            .toString().trim().length() > 1
                                            && owner_aadhaarNo.getText()
                                            .toString().trim().length() != 12) {
                                        owner_aadhaarNo.setError(Html
                                                .fromHtml("<font color='black'>Please Enter Valid Aadhaar</font>"));
                                        owner_aadhaarNo.requestFocus();
                                    } else if (owner_aadhaarNo.getText()
                                            .toString().trim().length() > 1
                                            && owner_aadhaarNo.getText()
                                            .toString().trim().length() == 12
                                            && !ver.isValid(owner_aadhaarNo
                                            .getText().toString()
                                            .trim())) {
                                        owner_aadhaarNo.setError(Html
                                                .fromHtml("<font color='black'>Please Enter Valid Aadhaar</font>"));
                                        owner_aadhaarNo.requestFocus();
                                    } else if (owner_aadhaarName.getText()
                                            .toString().trim().equals("")) {
                                        owner_aadhaarName.setError(Html
                                                .fromHtml("<font color='black'>Please Enter Name</font>"));
                                        owner_aadhaarName.requestFocus();
                                    }/*
                                     * else if
                                     * (owner_aadhaarFatherName.getText()
                                     * .toString().trim().equals("")) {
                                     * owner_aadhaarFatherName
                                     * .setError(Html.fromHtml(
                                     * "<font color='black'>Please Enter Owner Father Number</font>"
                                     * ));
                                     * owner_aadhaarFatherName.requestFocus(); }
                                     */ else if (owner_aadhaarAge.getText()
                                            .toString().trim().equals("")) {
                                        owner_aadhaarAge.setError(Html
                                                .fromHtml("<font color='black'>Please Enter Owner Age</font>"));
                                        owner_aadhaarAge.requestFocus();
                                    } else if (owner_aadhaarMobileNo.getText()
                                            .toString().trim().equals("")) {
                                        owner_aadhaarMobileNo.setError(Html
                                                .fromHtml("<font color='black'>Please Enter Owner Mobile No</font>"));
                                        owner_aadhaarMobileNo.requestFocus();
                                    } else if (owner_aadhaarAddress.getText()
                                            .toString().trim().equals("")) {
                                        owner_aadhaarAddress.setError(Html
                                                .fromHtml("<font color='black'>Please Enter Owner Address</font>"));
                                        owner_aadhaarAddress.requestFocus();
                                    }/*
                                     * else if
                                     * (aadhaar_ownerImage.getDrawable().
                                     * getConstantState() ==
                                     * getResources().getDrawable
                                     * (R.drawable.camera).getConstantState()
                                     * ||aadhaar_ownerImage
                                     * .getDrawable().getConstantState() ==
                                     * getResources
                                     * ().getDrawable(R.drawable.photo
                                     * ).getConstantState()) {
                                     * showToast("Please Select Owner Image"); }
                                     */ else if (aadhaarYes.isChecked() == true) { // SHOP-->MANAGE-->OWNER
                                        // Aadhaar-->RESP
                                        // Aadhaar

                                        if (aadhaar_no.getText().toString()
                                                .trim().equals("")) {
                                            aadhaar_no.setError(Html
                                                    .fromHtml("<font color='black'>Please Enter Valid Aadhaar</font>"));
                                            aadhaar_no.requestFocus();
                                        } else if (aadhaar_no.getText()
                                                .toString().trim().length() > 1
                                                && aadhaar_no.getText()
                                                .toString().trim()
                                                .length() != 12) {
                                            aadhaar_no.setError(Html
                                                    .fromHtml("<font color='black'>Please Enter Valid Aadhaar</font>"));
                                            aadhaar_no.requestFocus();
                                        } else if (aadhaar_no.getText()
                                                .toString().trim().length() > 1
                                                && aadhaar_no.getText()
                                                .toString().trim()
                                                .length() == 12
                                                && !ver.isValid(aadhaar_no
                                                .getText().toString()
                                                .trim())) {
                                            aadhaar_no.setError(Html
                                                    .fromHtml("<font color='black'>Please Enter Valid Aadhaar</font>"));
                                            aadhaar_no.requestFocus();
                                        } else if (et_name.getText().toString()
                                                .trim().equals("")) {
                                            et_name.setError(Html
                                                    .fromHtml("<font color='black'>Please Enter Name</font>"));
                                            et_name.requestFocus();
                                        } else if (et_father_name.getText()
                                                .toString().trim().equals("")) {
                                            et_father_name.setError(Html
                                                    .fromHtml("<font color='black'>Please Enter Father Number</font>"));
                                            et_father_name.requestFocus();
                                        } else if (et_age.getText().toString()
                                                .trim().equals("")) {
                                            et_age.setError(Html
                                                    .fromHtml("<font color='black'>Please Enter Age</font>"));
                                            et_age.requestFocus();
                                        } else if (et_mobileNo.getText()
                                                .toString().trim().equals("")) {
                                            et_mobileNo.setError(Html
                                                    .fromHtml("<font color='black'>Please Enter Age</font>"));
                                            et_mobileNo.requestFocus();
                                        } else if (et_address.getText()
                                                .toString().trim().equals("")) {
                                            et_address.setError(Html
                                                    .fromHtml("<font color='black'>Please Enter Address</font>"));
                                            et_address.requestFocus();
                                        } else if (aadhaar_image.getDrawable()
                                                .getConstantState() == getResources()
                                                .getDrawable(R.drawable.camera)
                                                .getConstantState()
                                                || aadhaar_image.getDrawable()
                                                .getConstantState() == getResources()
                                                .getDrawable(
                                                        R.drawable.photo)
                                                .getConstantState()) {
                                            showToast("Please Select Respondent Image");
                                        }/*
                                         * else if
                                         * (otp_verify_status.equals("N")) {
                                         * showToast
                                         * ("Please Verify OTP Number");
                                         * et_mobileNo.requestFocus(); }
                                         */ else if (Sections.checkedList
                                                .isEmpty()) {
                                            showToast("Please Select atleast one Section");
                                        } else if (DetainedItems.detItems
                                                .toString().trim().length() == 0) {
                                            showToast("Please Enter Items Found on Encroachment");
                                        }/*
                                         * else if
                                         * (otp_verify_status.equals("N")) {
                                         * showToast
                                         * ("Please Verify OTP Number");
                                         * owner_aadhaarMobileNo.requestFocus();
                                         * }
                                         */ else if ("Y".equals(Dashboard.OtpStatus.trim()) && otp_verify_status.equals("N")) {
                                            showToast("Please Verify OTP Number");
                                        } else {
                                            if (isOnline()) {
                                                new Async_PreviewDetails().execute();
                                            } else {
                                                showToast("Please Check Your Network connection");
                                            }
                                        }
                                    } else if (aadhaarNo.isChecked() == true) { // SHOP-->MANAGE-->OWNER
                                        // Aadhaar
                                        // Y-->RESP
                                        // Aadhaar
                                        // N

                                        if (et_name.getText().toString().trim()
                                                .equals("")) {
                                            et_name.setError(Html
                                                    .fromHtml("<font color='black'>Please Enter Name</font>"));
                                            et_name.requestFocus();
                                        } else if (et_father_name.getText()
                                                .toString().trim().equals("")) {
                                            et_father_name.setError(Html
                                                    .fromHtml("<font color='black'>Please Enter Father Number</font>"));
                                            et_father_name.requestFocus();
                                        } else if (et_age.getText().toString()
                                                .trim().equals("")) {
                                            et_age.setError(Html
                                                    .fromHtml("<font color='black'>Please Enter Age</font>"));
                                            et_age.requestFocus();
                                        } else if (et_mobileNo.getText()
                                                .toString().trim().equals("")) {
                                            et_mobileNo.setError(Html
                                                    .fromHtml("<font color='black'>Please Enter Age</font>"));
                                            et_mobileNo.requestFocus();
                                        } else if (et_address.getText()
                                                .toString().trim().equals("")) {
                                            et_address.setError(Html
                                                    .fromHtml("<font color='black'>Please Enter Address</font>"));
                                            et_address.requestFocus();
                                        } else if (aadhaar_image.getDrawable()
                                                .getConstantState() == getResources()
                                                .getDrawable(R.drawable.camera)
                                                .getConstantState()
                                                || aadhaar_image.getDrawable()
                                                .getConstantState() == getResources()
                                                .getDrawable(
                                                        R.drawable.photo)
                                                .getConstantState()) {
                                            showToast("Please Select Respondent Image");
                                        }/*
                                         * else if
                                         * (otp_verify_status.equals("N")) {
                                         * showToast
                                         * ("Please Verify OTP Number");
                                         * et_mobileNo.requestFocus(); }
                                         */ else if ("Y".equals(Dashboard.OtpStatus.trim()) && otp_verify_status.equals("N")) {
                                            showToast("Please Verify OTP Number");
                                        } else if (Sections.checkedList
                                                .isEmpty()) {
                                            showToast("Please Select atleast one Section");
                                        } else if (DetainedItems.detItems
                                                .toString().trim().length() == 0) {
                                            showToast("Please Enter Items Found on Encroachment");
                                        }/*
                                         * else if
                                         * (otp_verify_status.equals("N")) {
                                         * showToast
                                         * ("Please Verify OTP Number");
                                         * owner_aadhaarMobileNo.requestFocus();
                                         * }
                                         */ else {
                                            if (isOnline()) {
                                                new Async_PreviewDetails().execute();
                                            } else {
                                                showToast("Please Check Your Network connection");
                                            }
                                        }
                                    }
                                } else if (owner_aadhaar_no.isChecked() == true) {
                                    if (owner_aadhaarName.getText().toString()
                                            .trim().equals("")) {
                                        owner_aadhaarName.setError(Html
                                                .fromHtml("<font color='black'>Please Enter Name</font>"));
                                        owner_aadhaarName.requestFocus();
                                    }/*
                                     * else if
                                     * (owner_aadhaarFatherName.getText()
                                     * .toString().trim().equals("")) {
                                     * owner_aadhaarFatherName
                                     * .setError(Html.fromHtml(
                                     * "<font color='black'>Please Enter Owner Father Number</font>"
                                     * ));
                                     * owner_aadhaarFatherName.requestFocus(); }
                                     */ else if (owner_aadhaarAge.getText()
                                            .toString().trim().equals("")) {
                                        owner_aadhaarAge.setError(Html
                                                .fromHtml("<font color='black'>Please Enter Owner Age</font>"));
                                        owner_aadhaarAge.requestFocus();
                                    } else if (owner_aadhaarMobileNo.getText()
                                            .toString().trim().equals("")) {
                                        owner_aadhaarMobileNo.setError(Html
                                                .fromHtml("<font color='black'>Please Enter Owner Mobile No</font>"));
                                        owner_aadhaarMobileNo.requestFocus();
                                    } else if (owner_aadhaarAddress.getText()
                                            .toString().trim().equals("")) {
                                        owner_aadhaarAddress.setError(Html
                                                .fromHtml("<font color='black'>Please Enter Owner Address</font>"));
                                        owner_aadhaarAddress.requestFocus();
                                    }/*
                                     * else if
                                     * (aadhaar_ownerImage.getDrawable().
                                     * getConstantState() ==
                                     * getResources().getDrawable
                                     * (R.drawable.camera).getConstantState()
                                     * ||aadhaar_ownerImage
                                     * .getDrawable().getConstantState() ==
                                     * getResources
                                     * ().getDrawable(R.drawable.photo
                                     * ).getConstantState()) {
                                     * showToast("Please Select Respondent Image"
                                     * ); }
                                     */ else if (aadhaarYes.isChecked()) { // SHOP-->MANAGE-->OWNER
                                        // Aadhaar-->RESP
                                        // Aadhaar
                                        Log.i("COndition : ",
                                                "SHOP->MANAGER->O Aadhr Y-->R Aadhr Y : Enrtered");
                                        if (aadhaar_no.getText().toString()
                                                .trim().equals("")) {
                                            aadhaar_no.setError(Html
                                                    .fromHtml("<font color='black'>Please Enter Valid Aadhaar</font>"));
                                            aadhaar_no.requestFocus();
                                        } else if (aadhaar_no.getText()
                                                .toString().trim().length() > 1
                                                && aadhaar_no.getText()
                                                .toString().trim()
                                                .length() != 12) {
                                            aadhaar_no.setError(Html
                                                    .fromHtml("<font color='black'>Please Enter Valid Aadhaar</font>"));
                                            aadhaar_no.requestFocus();
                                        } else if (aadhaar_no.getText()
                                                .toString().trim().length() > 1
                                                && aadhaar_no.getText()
                                                .toString().trim()
                                                .length() == 12
                                                && !ver.isValid(aadhaar_no
                                                .getText().toString()
                                                .trim())) {
                                            aadhaar_no.setError(Html
                                                    .fromHtml("<font color='black'>Please Enter Valid Aadhaar</font>"));
                                            aadhaar_no.requestFocus();
                                        } else if (et_name.getText().toString()
                                                .trim().equals("")) {
                                            et_name.setError(Html
                                                    .fromHtml("<font color='black'>Please Enter Name</font>"));
                                            et_name.requestFocus();
                                        } else if (et_father_name.getText()
                                                .toString().trim().equals("")) {
                                            et_father_name.setError(Html
                                                    .fromHtml("<font color='black'>Please Enter Father Number</font>"));
                                            et_father_name.requestFocus();
                                        } else if (et_age.getText().toString()
                                                .trim().equals("")) {
                                            et_age.setError(Html
                                                    .fromHtml("<font color='black'>Please Enter Age</font>"));
                                            et_age.requestFocus();
                                        } else if (et_mobileNo.getText()
                                                .toString().trim().equals("")) {
                                            et_mobileNo.setError(Html
                                                    .fromHtml("<font color='black'>Please Enter Age</font>"));
                                            et_mobileNo.requestFocus();
                                        } else if (et_address.getText()
                                                .toString().trim().equals("")) {
                                            et_address.setError(Html
                                                    .fromHtml("<font color='black'>Please Enter Address</font>"));
                                            et_address.requestFocus();
                                        } else if (aadhaar_image.getDrawable()
                                                .getConstantState() == getResources()
                                                .getDrawable(R.drawable.camera)
                                                .getConstantState()
                                                || aadhaar_image.getDrawable()
                                                .getConstantState() == getResources()
                                                .getDrawable(
                                                        R.drawable.photo)
                                                .getConstantState()) {
                                            showToast("Please Select Respondent Image");
                                        } else if (Sections.checkedList
                                                .isEmpty()) {
                                            showToast("Please Select atleast one Section");
                                        } else if (DetainedItems.detItems
                                                .toString().trim().length() == 0) {
                                            showToast("Please Enter Items Found on Encroachment");
                                        }/*
                                         * else if
                                         * (otp_verify_status.equals("N")) {
                                         * showToast
                                         * ("Please Verify OTP Number");
                                         * owner_aadhaarMobileNo.requestFocus();
                                         * }
                                         */ else if ("Y".equals(Dashboard.OtpStatus.trim()) && otp_verify_status.equals("N")) {
                                            showToast("Please Verify OTP Number");
                                        } else {
                                            if (isOnline()) {
                                                new Async_PreviewDetails().execute();
                                            } else {
                                                showToast("Please Check Your Network connection");
                                            }
                                        }
                                    } else if (aadhaarNo.isChecked() == true) { // SHOP-->MANAGE-->OWNER
                                        // Aadhaar
                                        // Y-->RESP
                                        // Aadhaar
                                        // N
                                        Log.i("COndition : ",
                                                "SHOP->MANAGER->O Aadhr Y-->R Aadhr Y : Enrtered");
                                        if (et_name.getText().toString().trim()
                                                .equals("")) {
                                            et_name.setError(Html
                                                    .fromHtml("<font color='black'>Please Enter Name</font>"));
                                            et_name.requestFocus();
                                        } else if (et_father_name.getText()
                                                .toString().trim().equals("")) {
                                            et_father_name.setError(Html
                                                    .fromHtml("<font color='black'>Please Enter Father Number</font>"));
                                            et_father_name.requestFocus();
                                        } else if (et_age.getText().toString()
                                                .trim().equals("")) {
                                            et_age.setError(Html
                                                    .fromHtml("<font color='black'>Please Enter Age</font>"));
                                            et_age.requestFocus();
                                        } else if (et_mobileNo.getText()
                                                .toString().trim().equals("")) {
                                            et_mobileNo.setError(Html
                                                    .fromHtml("<font color='black'>Please Enter Age</font>"));
                                            et_mobileNo.requestFocus();
                                        } else if (et_address.getText()
                                                .toString().trim().equals("")) {
                                            et_address.setError(Html
                                                    .fromHtml("<font color='black'>Please Enter Address</font>"));
                                            et_address.requestFocus();
                                        } else if (aadhaar_image.getDrawable()
                                                .getConstantState() == getResources()
                                                .getDrawable(R.drawable.camera)
                                                .getConstantState()
                                                || aadhaar_image.getDrawable()
                                                .getConstantState() == getResources()
                                                .getDrawable(
                                                        R.drawable.photo)
                                                .getConstantState()) {
                                            showToast("Please Select Respondent Image");
                                        }/*
                                         * else if
                                         * (otp_verify_status.equals("N")) {
                                         * showToast
                                         * ("Please Verify OTP Number");
                                         * et_mobileNo.requestFocus(); }
                                         */ else if ("Y".equals(Dashboard.OtpStatus.trim()) && otp_verify_status.equals("N")) {
                                            showToast("Please Verify OTP Number");
                                        } else if (Sections.checkedList
                                                .isEmpty()) {
                                            showToast("Please Select atleast one Section");
                                        } else if (DetainedItems.detItems
                                                .toString().trim().length() == 0) {
                                            showToast("Please Enter Items Found on Encroachment");
                                        } else {
                                            if (isOnline()) {
                                                new Async_PreviewDetails().execute();
                                            } else {
                                                showToast("Please Check Your Network connection");
                                            }
                                        }
                                    }
                                }
                            } else if (tin_no.isChecked() == true) {
                                if (et_shop_name.getText().toString().trim()
                                        .equals("")) {
                                    et_shop_name.setError(Html
                                            .fromHtml("<font color='black'>Please Enter Shop Name</font>"));
                                    et_shop_name.requestFocus();
                                } else if (et_firm_owner_name.getText()
                                        .toString().trim().equals("")) {
                                    et_firm_owner_name.setError(Html
                                            .fromHtml("<font color='black'>Please Enter Shop Owner Name</font>"));
                                    et_firm_owner_name.requestFocus();
                                } else if (et_firm_address.getText().toString()
                                        .trim().equals("")) {
                                    et_firm_address.setError(Html
                                            .fromHtml("<font color='black'>Please Enter Shop Owner Address</font>"));
                                    et_firm_address.requestFocus();
                                } else if (owner_aadhaar_yes.isChecked() == true) {

                                    if (owner_aadhaarNo.getText().toString()
                                            .trim().equals("")) {
                                        owner_aadhaarNo.setError(Html
                                                .fromHtml("<font color='black'>Please Enter Aadhaar Number</font>"));
                                        owner_aadhaarNo.requestFocus();
                                    } else if (owner_aadhaarNo.getText()
                                            .toString().trim().length() > 1
                                            && owner_aadhaarNo.getText()
                                            .toString().trim().length() != 12) {
                                        owner_aadhaarNo.setError(Html
                                                .fromHtml("<font color='black'>Please Enter Valid Aadhaar</font>"));
                                        owner_aadhaarNo.requestFocus();
                                    } else if (owner_aadhaarNo.getText()
                                            .toString().trim().length() > 1
                                            && owner_aadhaarNo.getText()
                                            .toString().trim().length() == 12
                                            && !ver.isValid(owner_aadhaarNo
                                            .getText().toString()
                                            .trim())) {
                                        owner_aadhaarNo.setError(Html
                                                .fromHtml("<font color='black'>Please Enter Valid Aadhaar</font>"));
                                        owner_aadhaarNo.requestFocus();
                                    } else if (owner_aadhaarName.getText()
                                            .toString().trim().equals("")) {
                                        owner_aadhaarName.setError(Html
                                                .fromHtml("<font color='black'>Please Enter Name</font>"));
                                        owner_aadhaarName.requestFocus();
                                    }/*
                                     * else if
                                     * (owner_aadhaarFatherName.getText()
                                     * .toString().trim().equals("")) {
                                     * owner_aadhaarFatherName
                                     * .setError(Html.fromHtml(
                                     * "<font color='black'>Please Enter Owner Father Number</font>"
                                     * ));
                                     * owner_aadhaarFatherName.requestFocus(); }
                                     */ else if (owner_aadhaarAge.getText()
                                            .toString().trim().equals("")) {
                                        owner_aadhaarAge.setError(Html
                                                .fromHtml("<font color='black'>Please Enter Owner Age</font>"));
                                        owner_aadhaarAge.requestFocus();
                                    } else if (owner_aadhaarMobileNo.getText()
                                            .toString().trim().equals("")) {
                                        owner_aadhaarMobileNo.setError(Html
                                                .fromHtml("<font color='black'>Please Enter Owner Mobile No</font>"));
                                        owner_aadhaarMobileNo.requestFocus();
                                    } else if (owner_aadhaarAddress.getText()
                                            .toString().trim().equals("")) {
                                        owner_aadhaarAddress.setError(Html
                                                .fromHtml("<font color='black'>Please Enter Owner Address</font>"));
                                        owner_aadhaarAddress.requestFocus();
                                    }/*
                                     * else if
                                     * (aadhaar_ownerImage.getDrawable().
                                     * getConstantState() ==
                                     * getResources().getDrawable
                                     * (R.drawable.camera).getConstantState()
                                     * ||aadhaar_ownerImage
                                     * .getDrawable().getConstantState() ==
                                     * getResources
                                     * ().getDrawable(R.drawable.photo
                                     * ).getConstantState()) {
                                     * showToast("Please Select Respondent Image"
                                     * ); }
                                     */ else if (aadhaarYes.isChecked()) { // SHOP-->MANAGE-->OWNER
                                        // Aadhaar-->RESP
                                        // Aadhaar
                                        Log.i("COndition : ",
                                                "SHOP->MANAGER->O Aadhr Y-->R Aadhr Y : Enrtered");
                                        if (aadhaar_no.getText().toString()
                                                .trim().equals("")) {
                                            aadhaar_no.setError(Html
                                                    .fromHtml("<font color='black'>Please Enter Valid Aadhaar</font>"));
                                            aadhaar_no.requestFocus();
                                        } else if (aadhaar_no.getText()
                                                .toString().trim().length() > 1
                                                && aadhaar_no.getText()
                                                .toString().trim()
                                                .length() != 12) {
                                            aadhaar_no.setError(Html
                                                    .fromHtml("<font color='black'>Please Enter Valid Aadhaar</font>"));
                                            aadhaar_no.requestFocus();
                                        } else if (aadhaar_no.getText()
                                                .toString().trim().length() > 1
                                                && aadhaar_no.getText()
                                                .toString().trim()
                                                .length() == 12
                                                && !ver.isValid(aadhaar_no
                                                .getText().toString()
                                                .trim())) {
                                            aadhaar_no.setError(Html
                                                    .fromHtml("<font color='black'>Please Enter Valid Aadhaar</font>"));
                                            aadhaar_no.requestFocus();
                                        } else if (et_name.getText().toString()
                                                .trim().equals("")) {
                                            et_name.setError(Html
                                                    .fromHtml("<font color='black'>Please Enter Name</font>"));
                                            et_name.requestFocus();
                                        } else if (et_father_name.getText()
                                                .toString().trim().equals("")) {
                                            et_father_name.setError(Html
                                                    .fromHtml("<font color='black'>Please Enter Father Number</font>"));
                                            et_father_name.requestFocus();
                                        } else if (et_age.getText().toString()
                                                .trim().equals("")) {
                                            et_age.setError(Html
                                                    .fromHtml("<font color='black'>Please Enter Age</font>"));
                                            et_age.requestFocus();
                                        } else if (et_mobileNo.getText()
                                                .toString().trim().equals("")) {
                                            et_mobileNo.setError(Html
                                                    .fromHtml("<font color='black'>Please Enter Age</font>"));
                                            et_mobileNo.requestFocus();
                                        } else if (et_address.getText()
                                                .toString().trim().equals("")) {
                                            et_address.setError(Html
                                                    .fromHtml("<font color='black'>Please Enter Address</font>"));
                                            et_address.requestFocus();
                                        } else if (aadhaar_image.getDrawable()
                                                .getConstantState() == getResources()
                                                .getDrawable(R.drawable.camera)
                                                .getConstantState()
                                                || aadhaar_image.getDrawable()
                                                .getConstantState() == getResources()
                                                .getDrawable(
                                                        R.drawable.photo)
                                                .getConstantState()) {
                                            showToast("Please Select Respondent Image");
                                        }/*
                                         * else if
                                         * (otp_verify_status.equals("N")) {
                                         * showToast
                                         * ("Please Verify OTP Number");
                                         * et_mobileNo.requestFocus(); }
                                         */ else if ("Y".equals(Dashboard.OtpStatus.trim()) && otp_verify_status.equals("N")) {
                                            showToast("Please Verify OTP Number");
                                        } else if (Sections.checkedList
                                                .isEmpty()) {
                                            showToast("Please Select atleast one Section");
                                        } else if (DetainedItems.detItems
                                                .toString().trim().length() == 0) {
                                            showToast("Please Enter Items Found on Encroachment");
                                        } else {
                                            if (isOnline()) {
                                                new Async_PreviewDetails().execute();
                                            } else {
                                                showToast("Please Check Your Network connection");
                                            }
                                        }
                                    } else if (aadhaarNo.isChecked() == true) { // SHOP-->MANAGE-->OWNER
                                        // Aadhaar
                                        // Y-->RESP
                                        // Aadhaar
                                        // N
                                        Log.i("COndition : ",
                                                "SHOP->MANAGER->O Aadhr Y-->R Aadhr Y : Enrtered");
                                        if (et_name.getText().toString().trim()
                                                .equals("")) {
                                            et_name.setError(Html
                                                    .fromHtml("<font color='black'>Please Enter Name</font>"));
                                            et_name.requestFocus();
                                        } else if (et_father_name.getText()
                                                .toString().trim().equals("")) {
                                            et_father_name.setError(Html
                                                    .fromHtml("<font color='black'>Please Enter Father Number</font>"));
                                            et_father_name.requestFocus();
                                        } else if (et_age.getText().toString()
                                                .trim().equals("")) {
                                            et_age.setError(Html
                                                    .fromHtml("<font color='black'>Please Enter Age</font>"));
                                            et_age.requestFocus();
                                        } else if (et_mobileNo.getText()
                                                .toString().trim().equals("")) {
                                            et_mobileNo.setError(Html
                                                    .fromHtml("<font color='black'>Please Enter Age</font>"));
                                            et_mobileNo.requestFocus();
                                        } else if (et_address.getText()
                                                .toString().trim().equals("")) {
                                            et_address.setError(Html
                                                    .fromHtml("<font color='black'>Please Enter Address</font>"));
                                            et_address.requestFocus();
                                        } else if (aadhaar_image.getDrawable()
                                                .getConstantState() == getResources()
                                                .getDrawable(R.drawable.camera)
                                                .getConstantState()
                                                || aadhaar_image.getDrawable()
                                                .getConstantState() == getResources()
                                                .getDrawable(
                                                        R.drawable.photo)
                                                .getConstantState()) {
                                            showToast("Please Select Respondent Image");
                                        } else if (Sections.checkedList
                                                .isEmpty()) {
                                            showToast("Please Select atleast one Section");
                                        } else if (DetainedItems.detItems
                                                .toString().trim().length() == 0) {
                                            showToast("Please Enter Items Found on Encroachment");
                                        }/*
                                         * else if
                                         * (otp_verify_status.equals("N")) {
                                         * showToast
                                         * ("Please Verify OTP Number");
                                         * owner_aadhaarMobileNo.requestFocus();
                                         * }
                                         */ else if ("Y".equals(Dashboard.OtpStatus.trim()) && otp_verify_status.equals("N")) {
                                            showToast("Please Verify OTP Number");
                                        } else {
                                            if (isOnline()) {
                                                new Async_PreviewDetails().execute();
                                            } else {
                                                showToast("Please Check Your Network connection");
                                            }
                                        }
                                    }
                                } else if (owner_aadhaar_no.isChecked() == true) {
                                    if (owner_aadhaarName.getText().toString()
                                            .trim().equals("")) {
                                        owner_aadhaarName.setError(Html
                                                .fromHtml("<font color='black'>Please Enter Name</font>"));
                                        owner_aadhaarName.requestFocus();
                                    }/*
                                     * else if
                                     * (owner_aadhaarFatherName.getText()
                                     * .toString().trim().equals("")) {
                                     * owner_aadhaarFatherName
                                     * .setError(Html.fromHtml(
                                     * "<font color='black'>Please Enter Owner Father Number</font>"
                                     * ));
                                     * owner_aadhaarFatherName.requestFocus(); }
                                     */ else if (owner_aadhaarAge.getText()
                                            .toString().trim().equals("")) {
                                        owner_aadhaarAge.setError(Html
                                                .fromHtml("<font color='black'>Please Enter Owner Age</font>"));
                                        owner_aadhaarAge.requestFocus();
                                    } else if (owner_aadhaarMobileNo.getText()
                                            .toString().trim().equals("")) {
                                        owner_aadhaarMobileNo.setError(Html
                                                .fromHtml("<font color='black'>Please Enter Owner Mobile No</font>"));
                                        owner_aadhaarMobileNo.requestFocus();
                                    } else if (owner_aadhaarAddress.getText()
                                            .toString().trim().equals("")) {
                                        owner_aadhaarAddress.setError(Html
                                                .fromHtml("<font color='black'>Please Enter Owner Address</font>"));
                                        owner_aadhaarAddress.requestFocus();
                                    }/*
                                     * else if
                                     * (aadhaar_ownerImage.getDrawable().
                                     * getConstantState() ==
                                     * getResources().getDrawable
                                     * (R.drawable.camera).getConstantState()||
                                     * aadhaar_ownerImage
                                     * .getDrawable().getConstantState() ==
                                     * getResources
                                     * ().getDrawable(R.drawable.photo
                                     * ).getConstantState()) {
                                     * showToast("Please Select Respondent Image"
                                     * ); }
                                     */ else if (aadhaarYes.isChecked() == true) { // SHOP-->MANAGE-->OWNER
                                        // Aadhaar-->RESP
                                        // Aadhaar

                                        if (aadhaar_no.getText().toString()
                                                .trim().equals("")) {
                                            aadhaar_no.setError(Html
                                                    .fromHtml("<font color='black'>Please Enter Valid Aadhaar</font>"));
                                            aadhaar_no.requestFocus();
                                        } else if (aadhaar_no.getText()
                                                .toString().trim().length() > 1
                                                && aadhaar_no.getText()
                                                .toString().trim()
                                                .length() != 12) {
                                            aadhaar_no.setError(Html
                                                    .fromHtml("<font color='black'>Please Enter Valid Aadhaar</font>"));
                                            aadhaar_no.requestFocus();
                                        } else if (aadhaar_no.getText()
                                                .toString().trim().length() > 1
                                                && aadhaar_no.getText()
                                                .toString().trim()
                                                .length() == 12
                                                && !ver.isValid(aadhaar_no
                                                .getText().toString()
                                                .trim())) {
                                            aadhaar_no.setError(Html
                                                    .fromHtml("<font color='black'>Please Enter Valid Aadhaar</font>"));
                                            aadhaar_no.requestFocus();
                                        } else if (et_name.getText().toString()
                                                .trim().equals("")) {
                                            et_name.setError(Html
                                                    .fromHtml("<font color='black'>Please Enter Name</font>"));
                                            et_name.requestFocus();
                                        } else if (et_father_name.getText()
                                                .toString().trim().equals("")) {
                                            et_father_name.setError(Html
                                                    .fromHtml("<font color='black'>Please Enter Father Number</font>"));
                                            et_father_name.requestFocus();
                                        } else if (et_age.getText().toString()
                                                .trim().equals("")) {
                                            et_age.setError(Html
                                                    .fromHtml("<font color='black'>Please Enter Age</font>"));
                                            et_age.requestFocus();
                                        } else if (et_mobileNo.getText()
                                                .toString().trim().equals("")) {
                                            et_mobileNo.setError(Html
                                                    .fromHtml("<font color='black'>Please Enter Age</font>"));
                                            et_mobileNo.requestFocus();
                                        } else if (et_address.getText()
                                                .toString().trim().equals("")) {
                                            et_address.setError(Html
                                                    .fromHtml("<font color='black'>Please Enter Address</font>"));
                                            et_address.requestFocus();
                                        } else if (aadhaar_image.getDrawable()
                                                .getConstantState() == getResources()
                                                .getDrawable(R.drawable.camera)
                                                .getConstantState()
                                                || aadhaar_image.getDrawable()
                                                .getConstantState() == getResources()
                                                .getDrawable(
                                                        R.drawable.photo)
                                                .getConstantState()) {
                                            showToast("Please Select Respondent Image");
                                        } else if (Sections.checkedList
                                                .isEmpty()) {
                                            showToast("Please Select atleast one Section");
                                        } else if (DetainedItems.detItems
                                                .toString().trim().length() == 0) {
                                            showToast("Please Enter Items Found on Encroachment");
                                        }/*
                                         * else if
                                         * (otp_verify_status.equals("N")) {
                                         * showToast
                                         * ("Please Verify OTP Number");
                                         * owner_aadhaarMobileNo.requestFocus();
                                         * }
                                         */ else if ("Y".equals(Dashboard.OtpStatus.trim()) && otp_verify_status.equals("N")) {
                                            showToast("Please Verify OTP Number");
                                        } else {
                                            if (isOnline()) {
                                                new Async_PreviewDetails().execute();
                                            } else {
                                                showToast("Please Check Your Network connection");
                                            }
                                        }
                                    } else if (aadhaarNo.isChecked() == true) { // SHOP-->MANAGE-->OWNER
                                        // Aadhaar
                                        // Y-->RESP
                                        // Aadhaar
                                        // N
                                        Log.i("COndition : ",
                                                "SHOP->MANAGER->O Aadhr Y-->R Aadhr Y : Enrtered");
                                        if (et_name.getText().toString().trim()
                                                .equals("")) {
                                            et_name.setError(Html
                                                    .fromHtml("<font color='black'>Please Enter Name</font>"));
                                            et_name.requestFocus();
                                        } else if (et_father_name.getText()
                                                .toString().trim().equals("")) {
                                            et_father_name.setError(Html
                                                    .fromHtml("<font color='black'>Please Enter Father Number</font>"));
                                            et_father_name.requestFocus();
                                        } else if (et_age.getText().toString()
                                                .trim().equals("")) {
                                            et_age.setError(Html
                                                    .fromHtml("<font color='black'>Please Enter Age</font>"));
                                            et_age.requestFocus();
                                        } else if (et_mobileNo.getText()
                                                .toString().trim().equals("")) {
                                            et_mobileNo.setError(Html
                                                    .fromHtml("<font color='black'>Please Enter Age</font>"));
                                            et_mobileNo.requestFocus();
                                        } else if (et_address.getText()
                                                .toString().trim().equals("")) {
                                            et_address.setError(Html
                                                    .fromHtml("<font color='black'>Please Enter Address</font>"));
                                            et_address.requestFocus();
                                        } else if (aadhaar_image.getDrawable()
                                                .getConstantState() == getResources()
                                                .getDrawable(R.drawable.camera)
                                                .getConstantState()
                                                || aadhaar_image.getDrawable()
                                                .getConstantState() == getResources()
                                                .getDrawable(
                                                        R.drawable.photo)
                                                .getConstantState()) {
                                            showToast("Please Select Respondent Image");
                                        } else if (Sections.checkedList
                                                .isEmpty()) {
                                            showToast("Please Select atleast one Section");
                                        } else if (DetainedItems.detItems
                                                .toString().trim().length() == 0) {
                                            showToast("Please Enter Items Found on Encroachment");
                                        }/*
                                         * else if
                                         * (otp_verify_status.equals("N")) {
                                         * showToast
                                         * ("Please Verify OTP Number");
                                         * owner_aadhaarMobileNo.requestFocus();
                                         * }
                                         */ else if ("Y".equals(Dashboard.OtpStatus.trim()) && otp_verify_status.equals("N")) {
                                            showToast("Please Verify OTP Number");
                                        } else {
                                            if (isOnline()) {
                                                new Async_PreviewDetails().execute();
                                            } else {
                                                showToast("Please Check Your Network connection");
                                            }
                                        }
                                    }
                                }
                            }

                        } else if (run_By.equals("SHOPKEEPER")) {
                            // MANAGER DETAILS
                            // TIN YES OR NO VALIDATIONS
                            if (tin_yes.isChecked()) {
                                if (et_tin_no.getText().toString().trim()
                                        .equals("")) {
                                    et_tin_no.requestFocus();
                                    // et_tin_no.setError(Html.fromHtml("<font color='black'>Please Enter Trade Licence Number</font>"));
                                    showToast("Please Enter Tin No");
                                } else if (et_shop_name.getText().toString()
                                        .trim().equals("")) {
                                    et_shop_name.setError(Html
                                            .fromHtml("<font color='black'>Please Enter Shop Name</font>"));
                                    et_shop_name.requestFocus();
                                } else if (et_firm_owner_name.getText()
                                        .toString().trim().equals("")) {
                                    et_firm_owner_name.setError(Html
                                            .fromHtml("<font color='black'>Please Enter Shop Owner Name</font>"));
                                    et_firm_owner_name.requestFocus();
                                } else if (et_firm_address.getText().toString()
                                        .trim().equals("")) {
                                    et_firm_address.setError(Html
                                            .fromHtml("<font color='black'>Please Enter Shop Owner Address</font>"));
                                    et_firm_address.requestFocus();
                                } else if (owner_aadhaar_yes.isChecked()) {
                                    if (owner_aadhaarNo.getText().toString()
                                            .trim().equals("")) {
                                        owner_aadhaarNo.setError(Html
                                                .fromHtml("<font color='black'>Please Enter Aadhaar Number</font>"));
                                        owner_aadhaarNo.requestFocus();
                                    } else if (owner_aadhaarNo.getText()
                                            .toString().trim().length() > 1
                                            && owner_aadhaarNo.getText()
                                            .toString().trim().length() != 12) {
                                        owner_aadhaarNo.setError(Html
                                                .fromHtml("<font color='black'>Please Enter Valid Aadhaar</font>"));
                                        owner_aadhaarNo.requestFocus();
                                    } else if (owner_aadhaarNo.getText()
                                            .toString().trim().length() > 1
                                            && owner_aadhaarNo.getText()
                                            .toString().trim().length() == 12
                                            && !ver.isValid(owner_aadhaarNo
                                            .getText().toString()
                                            .trim())) {
                                        owner_aadhaarNo.setError(Html
                                                .fromHtml("<font color='black'>Please Enter Valid Aadhaar</font>"));
                                        owner_aadhaarNo.requestFocus();
                                    } else if (owner_aadhaarName.getText()
                                            .toString().trim().equals("")) {
                                        owner_aadhaarName.setError(Html
                                                .fromHtml("<font color='black'>Please Enter Name</font>"));
                                        owner_aadhaarName.requestFocus();
                                    }/*
                                     * else if
                                     * (owner_aadhaarFatherName.getText()
                                     * .toString().trim().equals("")) {
                                     * owner_aadhaarFatherName
                                     * .setError(Html.fromHtml(
                                     * "<font color='black'>Please Enter Owner Father Number</font>"
                                     * ));
                                     * owner_aadhaarFatherName.requestFocus(); }
                                     */ else if (owner_aadhaarAge.getText()
                                            .toString().trim().equals("")) {
                                        owner_aadhaarAge.setError(Html
                                                .fromHtml("<font color='black'>Please Enter Owner Age</font>"));
                                        owner_aadhaarAge.requestFocus();
                                    } else if (owner_aadhaarMobileNo.getText()
                                            .toString().trim().equals("")) {
                                        owner_aadhaarMobileNo.setError(Html
                                                .fromHtml("<font color='black'>Please Enter Owner Mobile No</font>"));
                                        owner_aadhaarMobileNo.requestFocus();
                                    } else if (owner_aadhaarAddress.getText()
                                            .toString().trim().equals("")) {
                                        owner_aadhaarAddress.setError(Html
                                                .fromHtml("<font color='black'>Please Enter Owner Address</font>"));
                                        owner_aadhaarAddress.requestFocus();
                                    }/*
                                     * else if
                                     * (aadhaar_ownerImage.getDrawable().
                                     * getConstantState() ==
                                     * getResources().getDrawable
                                     * (R.drawable.camera).getConstantState()) {
                                     * showToast("Please Select Owner Image"); }
                                     */ else if (aadhaarYes.isChecked() == true) { // SHOP-->MANAGE-->OWNER
                                        // Aadhaar-->RESP
                                        // Aadhaar
                                        Log.i("COndition : ",
                                                "SHOP->SHOPKEEPER->O Aadhr Y-->R Aadhr Y : Enrtered");
                                        if (aadhaar_no.getText().toString()
                                                .trim().equals("")) {
                                            aadhaar_no.setError(Html
                                                    .fromHtml("<font color='black'>Please Enter Valid Aadhaar</font>"));
                                            aadhaar_no.requestFocus();
                                        } else if (aadhaar_no.getText()
                                                .toString().trim().length() > 1
                                                && aadhaar_no.getText()
                                                .toString().trim()
                                                .length() != 12) {
                                            aadhaar_no.setError(Html
                                                    .fromHtml("<font color='black'>Please Enter Valid Aadhaar</font>"));
                                            aadhaar_no.requestFocus();
                                        } else if (aadhaar_no.getText()
                                                .toString().trim().length() > 1
                                                && aadhaar_no.getText()
                                                .toString().trim()
                                                .length() == 12
                                                && !ver.isValid(aadhaar_no
                                                .getText().toString()
                                                .trim())) {
                                            aadhaar_no.setError(Html
                                                    .fromHtml("<font color='black'>Please Enter Valid Aadhaar</font>"));
                                            aadhaar_no.requestFocus();
                                        } else if (et_name.getText().toString()
                                                .trim().equals("")) {
                                            et_name.setError(Html
                                                    .fromHtml("<font color='black'>Please Enter Name</font>"));
                                            et_name.requestFocus();
                                        } else if (et_father_name.getText()
                                                .toString().trim().equals("")) {
                                            et_father_name.setError(Html
                                                    .fromHtml("<font color='black'>Please Enter Father Number</font>"));
                                            et_father_name.requestFocus();
                                        } else if (et_age.getText().toString()
                                                .trim().equals("")) {
                                            et_age.setError(Html
                                                    .fromHtml("<font color='black'>Please Enter Age</font>"));
                                            et_age.requestFocus();
                                        } else if (et_mobileNo.getText()
                                                .toString().trim().equals("")) {
                                            et_mobileNo.setError(Html
                                                    .fromHtml("<font color='black'>Please Enter Age</font>"));
                                            et_mobileNo.requestFocus();
                                        } else if (et_address.getText()
                                                .toString().trim().equals("")) {
                                            et_address.setError(Html
                                                    .fromHtml("<font color='black'>Please Enter Address</font>"));
                                            et_address.requestFocus();
                                        } else if (aadhaar_image.getDrawable()
                                                .getConstantState() == getResources()
                                                .getDrawable(R.drawable.camera)
                                                .getConstantState()) {
                                            showToast("Please Select Respondent Image");
                                        } else if (Sections.checkedList
                                                .isEmpty()) {
                                            showToast("Please Select atleast one Section");
                                        } else if (DetainedItems.detItems
                                                .toString().trim().length() == 0) {
                                            showToast("Please Enter Items Found on Encroachment");
                                        }/*
                                         * else if
                                         * (otp_verify_status.equals("N")) {
                                         * showToast
                                         * ("Please Verify OTP Number");
                                         * owner_aadhaarMobileNo.requestFocus();
                                         * }
                                         */ else if ("Y".equals(Dashboard.OtpStatus.trim()) && otp_verify_status.equals("N")) {
                                            showToast("Please Verify OTP Number");
                                        } else {
                                            if (isOnline()) {
                                                new Async_PreviewDetails().execute();
                                            } else {
                                                showToast("Please Check Your Network connection");
                                            }
                                        }
                                    } else if (aadhaarNo.isChecked() == true) { // SHOP-->MANAGE-->OWNER
                                        // Aadhaar
                                        // Y-->RESP
                                        // Aadhaar
                                        // N
                                        Log.i("COndition : ",
                                                "SHOP->SHOPKEEPER->O Aadhr Y-->R Aadhr Y : Enrtered");
                                        if (et_name.getText().toString().trim()
                                                .equals("")) {
                                            et_name.setError(Html
                                                    .fromHtml("<font color='black'>Please Enter Name</font>"));
                                            et_name.requestFocus();
                                        } else if (et_father_name.getText()
                                                .toString().trim().equals("")) {
                                            et_father_name.setError(Html
                                                    .fromHtml("<font color='black'>Please Enter Father Number</font>"));
                                            et_father_name.requestFocus();
                                        } else if (et_age.getText().toString()
                                                .trim().equals("")) {
                                            et_age.setError(Html
                                                    .fromHtml("<font color='black'>Please Enter Age</font>"));
                                            et_age.requestFocus();
                                        } else if (et_mobileNo.getText()
                                                .toString().trim().equals("")) {
                                            et_mobileNo.setError(Html
                                                    .fromHtml("<font color='black'>Please Enter Age</font>"));
                                            et_mobileNo.requestFocus();
                                        } else if (et_address.getText()
                                                .toString().trim().equals("")) {
                                            et_address.setError(Html
                                                    .fromHtml("<font color='black'>Please Enter Address</font>"));
                                            et_address.requestFocus();
                                        } else if (aadhaar_image.getDrawable()
                                                .getConstantState() == getResources()
                                                .getDrawable(R.drawable.camera)
                                                .getConstantState()) {
                                            showToast("Please Select Respondent Image");
                                        } else if (Sections.checkedList
                                                .isEmpty()) {
                                            showToast("Please Select atleast one Section");
                                        } else if (DetainedItems.detItems
                                                .toString().trim().length() == 0) {
                                            showToast("Please Enter Items Found on Encroachment");
                                        }/*
                                         * else if
                                         * (otp_verify_status.equals("N")) {
                                         * showToast
                                         * ("Please Verify OTP Number");
                                         * owner_aadhaarMobileNo.requestFocus();
                                         * }
                                         */ else if ("Y".equals(Dashboard.OtpStatus.trim()) && otp_verify_status.equals("N")) {
                                            showToast("Please Verify OTP Number");
                                        } else {
                                            if (isOnline()) {
                                                new Async_PreviewDetails().execute();
                                            } else {
                                                showToast("Please Check Your Network connection");
                                            }
                                        }
                                    }
                                } else if (owner_aadhaar_no.isChecked() == true) {
                                    if (owner_aadhaarName.getText().toString()
                                            .trim().equals("")) {
                                        owner_aadhaarName.setError(Html
                                                .fromHtml("<font color='black'>Please Enter Name</font>"));
                                        owner_aadhaarName.requestFocus();
                                    }/*
                                     * else if
                                     * (owner_aadhaarFatherName.getText()
                                     * .toString().trim().equals("")) {
                                     * owner_aadhaarFatherName
                                     * .setError(Html.fromHtml(
                                     * "<font color='black'>Please Enter Owner Father Number</font>"
                                     * ));
                                     * owner_aadhaarFatherName.requestFocus(); }
                                     */ else if (owner_aadhaarAge.getText()
                                            .toString().trim().equals("")) {
                                        owner_aadhaarAge.setError(Html
                                                .fromHtml("<font color='black'>Please Enter Owner Age</font>"));
                                        owner_aadhaarAge.requestFocus();
                                    } else if (owner_aadhaarMobileNo.getText()
                                            .toString().trim().equals("")) {
                                        owner_aadhaarMobileNo.setError(Html
                                                .fromHtml("<font color='black'>Please Enter Owner Mobile No</font>"));
                                        owner_aadhaarMobileNo.requestFocus();
                                    } else if (owner_aadhaarAddress.getText()
                                            .toString().trim().equals("")) {
                                        owner_aadhaarAddress.setError(Html
                                                .fromHtml("<font color='black'>Please Enter Owner Address</font>"));
                                        owner_aadhaarAddress.requestFocus();
                                    }/*
                                     * else if
                                     * (aadhaar_ownerImage.getDrawable().
                                     * getConstantState() ==
                                     * getResources().getDrawable
                                     * (R.drawable.camera).getConstantState()) {
                                     * showToast("Please Select Owner Image"); }
                                     */ else if (aadhaarYes.isChecked() == true) { // SHOP-->MANAGE-->OWNER
                                        // Aadhaar-->RESP
                                        // Aadhaar
                                        Log.i("COndition : ",
                                                "SHOP->SHOPKEEPER->O Aadhr Y-->R Aadhr Y : Enrtered");
                                        if (aadhaar_no.getText().toString()
                                                .trim().equals("")) {
                                            aadhaar_no.setError(Html
                                                    .fromHtml("<font color='black'>Please Enter Valid Aadhaar</font>"));
                                            aadhaar_no.requestFocus();
                                        } else if (aadhaar_no.getText()
                                                .toString().trim().length() > 1
                                                && aadhaar_no.getText()
                                                .toString().trim()
                                                .length() != 12) {
                                            aadhaar_no.setError(Html
                                                    .fromHtml("<font color='black'>Please Enter Valid Aadhaar</font>"));
                                            aadhaar_no.requestFocus();
                                        } else if (aadhaar_no.getText()
                                                .toString().trim().length() > 1
                                                && aadhaar_no.getText()
                                                .toString().trim()
                                                .length() == 12
                                                && !ver.isValid(aadhaar_no
                                                .getText().toString()
                                                .trim())) {
                                            aadhaar_no.setError(Html
                                                    .fromHtml("<font color='black'>Please Enter Valid Aadhaar</font>"));
                                            aadhaar_no.requestFocus();
                                        } else if (et_name.getText().toString()
                                                .trim().equals("")) {
                                            et_name.setError(Html
                                                    .fromHtml("<font color='black'>Please Enter Name</font>"));
                                            et_name.requestFocus();
                                        } else if (et_father_name.getText()
                                                .toString().trim().equals("")) {
                                            et_father_name.setError(Html
                                                    .fromHtml("<font color='black'>Please Enter Father Number</font>"));
                                            et_father_name.requestFocus();
                                        } else if (et_age.getText().toString()
                                                .trim().equals("")) {
                                            et_age.setError(Html
                                                    .fromHtml("<font color='black'>Please Enter Age</font>"));
                                            et_age.requestFocus();
                                        } else if (et_mobileNo.getText()
                                                .toString().trim().equals("")) {
                                            et_mobileNo.setError(Html
                                                    .fromHtml("<font color='black'>Please Enter Age</font>"));
                                            et_mobileNo.requestFocus();
                                        } else if (et_address.getText()
                                                .toString().trim().equals("")) {
                                            et_address.setError(Html
                                                    .fromHtml("<font color='black'>Please Enter Address</font>"));
                                            et_address.requestFocus();
                                        } else if (aadhaar_image.getDrawable()
                                                .getConstantState() == getResources()
                                                .getDrawable(R.drawable.camera)
                                                .getConstantState()) {
                                            showToast("Please Select Respondent Image");
                                        } else if (Sections.checkedList
                                                .isEmpty()) {
                                            showToast("Please Select atleast one Section");
                                        } else if (DetainedItems.detItems
                                                .toString().trim().length() == 0) {
                                            showToast("Please Enter Items Found on Encroachment");
                                        }/*
                                         * else if
                                         * (otp_verify_status.equals("N")) {
                                         * showToast
                                         * ("Please Verify OTP Number");
                                         * owner_aadhaarMobileNo.requestFocus();
                                         * }
                                         */ else if ("Y".equals(Dashboard.OtpStatus.trim()) && otp_verify_status.equals("N")) {
                                            showToast("Please Verify OTP Number");
                                        } else {
                                            if (isOnline()) {
                                                new Async_PreviewDetails().execute();
                                            } else {
                                                showToast("Please Check Your Network connection");
                                            }
                                        }
                                    } else if (aadhaarNo.isChecked() == true) { // SHOP-->MANAGE-->OWNER
                                        // Aadhaar
                                        // Y-->RESP
                                        // Aadhaar
                                        // N
                                        Log.i("COndition : ",
                                                "SHOP->SHOPKEEPER->O Aadhr Y-->R Aadhr Y : Enrtered");
                                        if (et_name.getText().toString().trim()
                                                .equals("")) {
                                            et_name.setError(Html
                                                    .fromHtml("<font color='black'>Please Enter Name</font>"));
                                            et_name.requestFocus();
                                        } else if (et_father_name.getText()
                                                .toString().trim().equals("")) {
                                            et_father_name.setError(Html
                                                    .fromHtml("<font color='black'>Please Enter Father Number</font>"));
                                            et_father_name.requestFocus();
                                        } else if (et_age.getText().toString()
                                                .trim().equals("")) {
                                            et_age.setError(Html
                                                    .fromHtml("<font color='black'>Please Enter Age</font>"));
                                            et_age.requestFocus();
                                        } else if (et_mobileNo.getText()
                                                .toString().trim().equals("")) {
                                            et_mobileNo.setError(Html
                                                    .fromHtml("<font color='black'>Please Enter Age</font>"));
                                            et_mobileNo.requestFocus();
                                        } else if (et_address.getText()
                                                .toString().trim().equals("")) {
                                            et_address.setError(Html
                                                    .fromHtml("<font color='black'>Please Enter Address</font>"));
                                            et_address.requestFocus();
                                        } else if (aadhaar_image.getDrawable()
                                                .getConstantState() == getResources()
                                                .getDrawable(R.drawable.camera)
                                                .getConstantState()) {
                                            showToast("Please Select Respondent Image");
                                        } else if (Sections.checkedList
                                                .isEmpty()) {
                                            showToast("Please Select atleast one Section");
                                        } else if (DetainedItems.detItems
                                                .toString().trim().length() == 0) {
                                            showToast("Please Enter Items Found on Encroachment");
                                        }/*
                                         * else if
                                         * (otp_verify_status.equals("N")) {
                                         * showToast
                                         * ("Please Verify OTP Number");
                                         * owner_aadhaarMobileNo.requestFocus();
                                         * }
                                         */ else if ("Y".equals(Dashboard.OtpStatus.trim()) && otp_verify_status.equals("N")) {
                                            showToast("Please Verify OTP Number");
                                        } else {
                                            if (isOnline()) {
                                                new Async_PreviewDetails().execute();
                                            } else {
                                                showToast("Please Check Your Network connection");
                                            }
                                        }
                                    }
                                }
                            } else if (tin_no.isChecked() == true) {

                                if (et_shop_name.getText().toString().trim()
                                        .equals("")) {
                                    et_shop_name.setError(Html
                                            .fromHtml("<font color='black'>Please Enter Shop Name</font>"));
                                    et_shop_name.requestFocus();
                                } else if (et_firm_owner_name.getText()
                                        .toString().trim().equals("")) {
                                    et_firm_owner_name.setError(Html
                                            .fromHtml("<font color='black'>Please Enter Shop Owner Name</font>"));
                                    et_firm_owner_name.requestFocus();
                                } else if (et_firm_address.getText().toString()
                                        .trim().equals("")) {
                                    et_firm_address.setError(Html
                                            .fromHtml("<font color='black'>Please Enter Shop Owner Address</font>"));
                                    et_firm_address.requestFocus();
                                } else if (owner_aadhaar_yes.isChecked() == true) {

                                    if (owner_aadhaarNo.getText().toString()
                                            .trim().equals("")) {
                                        owner_aadhaarNo.setError(Html
                                                .fromHtml("<font color='black'>Please Enter Aadhaar Number</font>"));
                                        owner_aadhaarNo.requestFocus();
                                    } else if (owner_aadhaarNo.getText()
                                            .toString().trim().length() > 1
                                            && owner_aadhaarNo.getText()
                                            .toString().trim().length() != 12) {
                                        owner_aadhaarNo.setError(Html
                                                .fromHtml("<font color='black'>Please Enter Valid Aadhaar</font>"));
                                        owner_aadhaarNo.requestFocus();
                                    } else if (owner_aadhaarNo.getText()
                                            .toString().trim().length() > 1
                                            && owner_aadhaarNo.getText()
                                            .toString().trim().length() == 12
                                            && !ver.isValid(owner_aadhaarNo
                                            .getText().toString()
                                            .trim())) {
                                        owner_aadhaarNo.setError(Html
                                                .fromHtml("<font color='black'>Please Enter Valid Aadhaar</font>"));
                                        owner_aadhaarNo.requestFocus();
                                    } else if (owner_aadhaarName.getText()
                                            .toString().trim().equals("")) {
                                        owner_aadhaarName.setError(Html
                                                .fromHtml("<font color='black'>Please Enter Name</font>"));
                                        owner_aadhaarName.requestFocus();
                                    }/*
                                     * else if
                                     * (owner_aadhaarFatherName.getText()
                                     * .toString().trim().equals("")) {
                                     * owner_aadhaarFatherName
                                     * .setError(Html.fromHtml(
                                     * "<font color='black'>Please Enter Owner Father Number</font>"
                                     * ));
                                     * owner_aadhaarFatherName.requestFocus(); }
                                     */ else if (owner_aadhaarAge.getText()
                                            .toString().trim().equals("")) {
                                        owner_aadhaarAge.setError(Html
                                                .fromHtml("<font color='black'>Please Enter Owner Age</font>"));
                                        owner_aadhaarAge.requestFocus();
                                    } else if (owner_aadhaarMobileNo.getText()
                                            .toString().trim().equals("")) {
                                        owner_aadhaarMobileNo.setError(Html
                                                .fromHtml("<font color='black'>Please Enter Owner Mobile No</font>"));
                                        owner_aadhaarMobileNo.requestFocus();
                                    } else if (owner_aadhaarAddress.getText()
                                            .toString().trim().equals("")) {
                                        owner_aadhaarAddress.setError(Html
                                                .fromHtml("<font color='black'>Please Enter Owner Address</font>"));
                                        owner_aadhaarAddress.requestFocus();
                                    }/*
                                     * else if
                                     * (aadhaar_ownerImage.getDrawable().
                                     * getConstantState() ==
                                     * getResources().getDrawable
                                     * (R.drawable.camera).getConstantState()) {
                                     * showToast("Please Select Owner Image"); }
                                     */ else if (aadhaarYes.isChecked() == true) { // SHOP-->MANAGE-->OWNER
                                        // Aadhaar-->RESP
                                        // Aadhaar
                                        Log.i("COndition : ",
                                                "SHOP->SHOPKEEPER->O Aadhr Y-->R Aadhr Y : Enrtered");
                                        if (aadhaar_no.getText().toString()
                                                .trim().equals("")) {
                                            aadhaar_no.setError(Html
                                                    .fromHtml("<font color='black'>Please Enter Valid Aadhaar</font>"));
                                            aadhaar_no.requestFocus();
                                        } else if (aadhaar_no.getText()
                                                .toString().trim().length() > 1
                                                && aadhaar_no.getText()
                                                .toString().trim()
                                                .length() != 12) {
                                            aadhaar_no.setError(Html
                                                    .fromHtml("<font color='black'>Please Enter Valid Aadhaar</font>"));
                                            aadhaar_no.requestFocus();
                                        } else if (aadhaar_no.getText()
                                                .toString().trim().length() > 1
                                                && aadhaar_no.getText()
                                                .toString().trim()
                                                .length() == 12
                                                && !ver.isValid(aadhaar_no
                                                .getText().toString()
                                                .trim())) {
                                            aadhaar_no.setError(Html
                                                    .fromHtml("<font color='black'>Please Enter Valid Aadhaar</font>"));
                                            aadhaar_no.requestFocus();
                                        } else if (et_name.getText().toString()
                                                .trim().equals("")) {
                                            et_name.setError(Html
                                                    .fromHtml("<font color='black'>Please Enter Name</font>"));
                                            et_name.requestFocus();
                                        } else if (et_father_name.getText()
                                                .toString().trim().equals("")) {
                                            et_father_name.setError(Html
                                                    .fromHtml("<font color='black'>Please Enter Father Number</font>"));
                                            et_father_name.requestFocus();
                                        } else if (et_age.getText().toString()
                                                .trim().equals("")) {
                                            et_age.setError(Html
                                                    .fromHtml("<font color='black'>Please Enter Age</font>"));
                                            et_age.requestFocus();
                                        } else if (et_mobileNo.getText()
                                                .toString().trim().equals("")) {
                                            et_mobileNo.setError(Html
                                                    .fromHtml("<font color='black'>Please Enter Age</font>"));
                                            et_mobileNo.requestFocus();
                                        } else if (et_address.getText()
                                                .toString().trim().equals("")) {
                                            et_address.setError(Html
                                                    .fromHtml("<font color='black'>Please Enter Address</font>"));
                                            et_address.requestFocus();
                                        } else if (aadhaar_image.getDrawable()
                                                .getConstantState() == getResources()
                                                .getDrawable(R.drawable.camera)
                                                .getConstantState()) {
                                            showToast("Please Select Respondent Image");
                                        } else if (Sections.checkedList
                                                .isEmpty()) {
                                            showToast("Please Select atleast one Section");
                                        } else if (DetainedItems.detItems
                                                .toString().trim().length() == 0) {
                                            showToast("Please Enter Items Found on Encroachment");
                                        }/*
                                         * else if
                                         * (otp_verify_status.equals("N")) {
                                         * showToast
                                         * ("Please Verify OTP Number");
                                         * owner_aadhaarMobileNo.requestFocus();
                                         * }
                                         */ else if ("Y".equals(Dashboard.OtpStatus.trim()) && otp_verify_status.equals("N")) {
                                            showToast("Please Verify OTP Number");
                                        } else {
                                            if (isOnline()) {
                                                new Async_PreviewDetails().execute();
                                            } else {
                                                showToast("Please Check Your Network connection");
                                            }
                                        }
                                    } else if (aadhaarNo.isChecked() == true) { // SHOP-->MANAGE-->OWNER
                                        // Aadhaar
                                        // Y-->RESP
                                        // Aadhaar
                                        // N
                                        Log.i("COndition : ",
                                                "SHOP->SHOPKEEPER->O Aadhr Y-->R Aadhr N : Enrtered");
                                        if (et_name.getText().toString().trim()
                                                .equals("")) {
                                            et_name.setError(Html
                                                    .fromHtml("<font color='black'>Please Enter Name</font>"));
                                            et_name.requestFocus();
                                        } else if (et_father_name.getText()
                                                .toString().trim().equals("")) {
                                            et_father_name.setError(Html
                                                    .fromHtml("<font color='black'>Please Enter Father Number</font>"));
                                            et_father_name.requestFocus();
                                        } else if (et_age.getText().toString()
                                                .trim().equals("")) {
                                            et_age.setError(Html
                                                    .fromHtml("<font color='black'>Please Enter Age</font>"));
                                            et_age.requestFocus();
                                        } else if (et_mobileNo.getText()
                                                .toString().trim().equals("")) {
                                            et_mobileNo.setError(Html
                                                    .fromHtml("<font color='black'>Please Enter Age</font>"));
                                            et_mobileNo.requestFocus();
                                        } else if (et_address.getText()
                                                .toString().trim().equals("")) {
                                            et_address.setError(Html
                                                    .fromHtml("<font color='black'>Please Enter Address</font>"));
                                            et_address.requestFocus();
                                        } else if (aadhaar_image.getDrawable()
                                                .getConstantState() == getResources()
                                                .getDrawable(R.drawable.camera)
                                                .getConstantState()) {
                                            showToast("Please Select Respondent Image");
                                        } else if (Sections.checkedList
                                                .isEmpty()) {
                                            showToast("Please Select atleast one Section");
                                        } else if (DetainedItems.detItems
                                                .toString().trim().length() == 0) {
                                            showToast("Please Enter Items Found on Encroachment");
                                        }/*
                                         * else if
                                         * (otp_verify_status.equals("N")) {
                                         * showToast
                                         * ("Please Verify OTP Number");
                                         * owner_aadhaarMobileNo.requestFocus();
                                         * }
                                         */ else if ("Y".equals(Dashboard.OtpStatus.trim()) && otp_verify_status.equals("N")) {
                                            showToast("Please Verify OTP Number");
                                        } else {
                                            if (isOnline()) {
                                                new Async_PreviewDetails().execute();
                                            } else {
                                                showToast("Please Check Your Network connection");
                                            }
                                        }
                                    }
                                } else if (owner_aadhaar_no.isChecked() == true) {
                                    if (owner_aadhaarName.getText().toString()
                                            .trim().equals("")) {
                                        owner_aadhaarName.setError(Html
                                                .fromHtml("<font color='black'>Please Enter Name</font>"));
                                        owner_aadhaarName.requestFocus();
                                    }/*
                                     * else if
                                     * (owner_aadhaarFatherName.getText()
                                     * .toString().trim().equals("")) {
                                     * owner_aadhaarFatherName
                                     * .setError(Html.fromHtml(
                                     * "<font color='black'>Please Enter Owner Father Number</font>"
                                     * ));
                                     * owner_aadhaarFatherName.requestFocus(); }
                                     */ else if (owner_aadhaarAge.getText()
                                            .toString().trim().equals("")) {
                                        owner_aadhaarAge.setError(Html
                                                .fromHtml("<font color='black'>Please Enter Owner Age</font>"));
                                        owner_aadhaarAge.requestFocus();
                                    } else if (owner_aadhaarMobileNo.getText()
                                            .toString().trim().equals("")) {
                                        owner_aadhaarMobileNo.setError(Html
                                                .fromHtml("<font color='black'>Please Enter Owner Mobile No</font>"));
                                        owner_aadhaarMobileNo.requestFocus();
                                    } else if (owner_aadhaarAddress.getText()
                                            .toString().trim().equals("")) {
                                        owner_aadhaarAddress.setError(Html
                                                .fromHtml("<font color='black'>Please Enter Owner Address</font>"));
                                        owner_aadhaarAddress.requestFocus();
                                    }/*
                                     * else if
                                     * (aadhaar_ownerImage.getDrawable().
                                     * getConstantState() ==
                                     * getResources().getDrawable
                                     * (R.drawable.camera).getConstantState()) {
                                     * showToast("Please Select Owner Image"); }
                                     */ else if (aadhaarYes.isChecked() == true) { // SHOP-->MANAGE-->OWNER
                                        // Aadhaar-->RESP
                                        // Aadhaar
                                        Log.i("COndition : ",
                                                "SHOP->SHOPKEEPER->O Aadhr Y-->R Aadhr Y : Enrtered");
                                        if (aadhaar_no.getText().toString()
                                                .trim().equals("")) {
                                            aadhaar_no.setError(Html
                                                    .fromHtml("<font color='black'>Please Enter Valid Aadhaar</font>"));
                                            aadhaar_no.requestFocus();
                                        } else if (aadhaar_no.getText()
                                                .toString().trim().length() > 1
                                                && aadhaar_no.getText()
                                                .toString().trim()
                                                .length() != 12) {
                                            aadhaar_no.setError(Html
                                                    .fromHtml("<font color='black'>Please Enter Valid Aadhaar</font>"));
                                            aadhaar_no.requestFocus();
                                        } else if (aadhaar_no.getText()
                                                .toString().trim().length() > 1
                                                && aadhaar_no.getText()
                                                .toString().trim()
                                                .length() == 12
                                                && !ver.isValid(aadhaar_no
                                                .getText().toString()
                                                .trim())) {
                                            aadhaar_no.setError(Html
                                                    .fromHtml("<font color='black'>Please Enter Valid Aadhaar</font>"));
                                            aadhaar_no.requestFocus();
                                        } else if (et_name.getText().toString()
                                                .trim().equals("")) {
                                            et_name.setError(Html
                                                    .fromHtml("<font color='black'>Please Enter Name</font>"));
                                            et_name.requestFocus();
                                        } else if (et_father_name.getText()
                                                .toString().trim().equals("")) {
                                            et_father_name.setError(Html
                                                    .fromHtml("<font color='black'>Please Enter Father Number</font>"));
                                            et_father_name.requestFocus();
                                        } else if (et_age.getText().toString()
                                                .trim().equals("")) {
                                            et_age.setError(Html
                                                    .fromHtml("<font color='black'>Please Enter Age</font>"));
                                            et_age.requestFocus();
                                        } else if (et_mobileNo.getText()
                                                .toString().trim().equals("")) {
                                            et_mobileNo.setError(Html
                                                    .fromHtml("<font color='black'>Please Enter Age</font>"));
                                            et_mobileNo.requestFocus();
                                        } else if (et_address.getText()
                                                .toString().trim().equals("")) {
                                            et_address.setError(Html
                                                    .fromHtml("<font color='black'>Please Enter Address</font>"));
                                            et_address.requestFocus();
                                        } else if (aadhaar_image.getDrawable()
                                                .getConstantState() == getResources()
                                                .getDrawable(R.drawable.camera)
                                                .getConstantState()) {
                                            showToast("Please Select Respondent Image");
                                        } else if (Sections.checkedList
                                                .isEmpty()) {
                                            showToast("Please Select atleast one Section");
                                        } else if (DetainedItems.detItems
                                                .toString().trim().length() == 0) {
                                            showToast("Please Enter Items Found on Encroachment");
                                        }/*
                                         * else if
                                         * (otp_verify_status.equals("N")) {
                                         * showToast
                                         * ("Please Verify OTP Number");
                                         * owner_aadhaarMobileNo.requestFocus();
                                         * }
                                         */ else if ("Y".equals(Dashboard.OtpStatus.trim()) && otp_verify_status.equals("N")) {
                                            showToast("Please Verify OTP Number");
                                        } else {
                                            if (isOnline()) {
                                                new Async_PreviewDetails().execute();
                                            } else {
                                                showToast("Please Check Your Network connection");
                                            }
                                        }
                                    } else if (aadhaarNo.isChecked() == true) { // SHOP-->MANAGE-->OWNER
                                        // Aadhaar
                                        // Y-->RESP
                                        // Aadhaar
                                        // N
                                        Log.i("COndition : ",
                                                "SHOP->SHOPKEEPER->O Aadhr Y-->R Aadhr Y : Enrtered");
                                        if (et_name.getText().toString().trim()
                                                .equals("")) {
                                            et_name.setError(Html
                                                    .fromHtml("<font color='black'>Please Enter Name</font>"));
                                            et_name.requestFocus();
                                        } else if (et_father_name.getText()
                                                .toString().trim().equals("")) {
                                            et_father_name.setError(Html
                                                    .fromHtml("<font color='black'>Please Enter Father Number</font>"));
                                            et_father_name.requestFocus();
                                        } else if (et_age.getText().toString()
                                                .trim().equals("")) {
                                            et_age.setError(Html
                                                    .fromHtml("<font color='black'>Please Enter Age</font>"));
                                            et_age.requestFocus();
                                        } else if (et_mobileNo.getText()
                                                .toString().trim().equals("")) {
                                            et_mobileNo.setError(Html
                                                    .fromHtml("<font color='black'>Please Enter Age</font>"));
                                            et_mobileNo.requestFocus();
                                        } else if (et_address.getText()
                                                .toString().trim().equals("")) {
                                            et_address.setError(Html
                                                    .fromHtml("<font color='black'>Please Enter Address</font>"));
                                            et_address.requestFocus();
                                        } else if (aadhaar_image.getDrawable()
                                                .getConstantState() == getResources()
                                                .getDrawable(R.drawable.camera)
                                                .getConstantState()) {
                                            showToast("Please Select Respondent Image");
                                        } else if (Sections.checkedList
                                                .isEmpty()) {
                                            showToast("Please Select atleast one Section");
                                        } else if (DetainedItems.detItems
                                                .toString().trim().length() == 0) {
                                            showToast("Please Enter Items Found on Encroachment");
                                        }/*
                                         * else if
                                         * (otp_verify_status.equals("N")) {
                                         * showToast
                                         * ("Please Verify OTP Number");
                                         * owner_aadhaarMobileNo.requestFocus();
                                         * }

                                         */ else if ("Y".equals(Dashboard.OtpStatus.trim()) && otp_verify_status.equals("N")) {
                                            showToast("Please Verify OTP Number");
                                        } else {
                                            if (isOnline()) {
                                                new Async_PreviewDetails().execute();
                                            } else {
                                                showToast("Please Check Your Network connection");
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    } else if (based_on.equals("PERSON")
                            && !based_on.equals("SHOP")) {

                        Log.i("COndition : ", "PERSON BASED ENTERED");

                        if (aadhaarYes.isChecked() == true) { // SHOP-->MANAGE-->OWNER
                            // Aadhaar-->RESP
                            // Aadhaar
                            Log.i("COndition : ",
                                    "PERSON->MANAGER->O Aadhr Y-->R Aadhr Y : Enrtered");
                            if (aadhaar_no.getText().toString().trim()
                                    .equals("")) {
                                aadhaar_no.setError(Html
                                        .fromHtml("<font color='black'>Please Enter Valid Aadhaar</font>"));
                                aadhaar_no.requestFocus();
                            } else if (aadhaar_no.getText().toString().trim()
                                    .length() > 1
                                    && aadhaar_no.getText().toString().trim()
                                    .length() != 12) {
                                aadhaar_no.setError(Html
                                        .fromHtml("<font color='black'>Please Enter Valid Aadhaar</font>"));
                                aadhaar_no.requestFocus();
                            } else if (aadhaar_no.getText().toString().trim()
                                    .length() > 1
                                    && aadhaar_no.getText().toString().trim()
                                    .length() == 12
                                    && !ver.isValid(aadhaar_no.getText()
                                    .toString().trim())) {
                                aadhaar_no.setError(Html
                                        .fromHtml("<font color='black'>Please Enter Valid Aadhaar</font>"));
                                aadhaar_no.requestFocus();
                            } else if (et_name.getText().toString().trim()
                                    .equals("")) {
                                et_name.setError(Html
                                        .fromHtml("<font color='black'>Please Enter Name</font>"));
                                et_name.requestFocus();
                            } else if (et_father_name.getText().toString()
                                    .trim().equals("")) {
                                et_father_name.setError(Html
                                        .fromHtml("<font color='black'>Please Enter Father Number</font>"));
                                et_father_name.requestFocus();
                            } else if (et_age.getText().toString().trim()
                                    .equals("")) {
                                et_age.setError(Html
                                        .fromHtml("<font color='black'>Please Enter Age</font>"));
                                et_age.requestFocus();
                            } else if (et_mobileNo.getText().toString().trim()
                                    .equals("")) {
                                et_mobileNo.setError(Html
                                        .fromHtml("<font color='black'>Please Enter Age</font>"));
                                et_mobileNo.requestFocus();
                            } else if (et_address.getText().toString().trim()
                                    .equals("")) {
                                et_address.setError(Html
                                        .fromHtml("<font color='black'>Please Enter Address</font>"));
                                et_address.requestFocus();
                            } else if (aadhaar_image.getDrawable()
                                    .getConstantState() == getResources()
                                    .getDrawable(R.drawable.camera)
                                    .getConstantState()) {
                                showToast("Please Select Respondent Image");
                            } else if (Sections.checkedList.isEmpty()) {
                                showToast("Please Select atleast one Section");
                            } else if (DetainedItems.detItems.toString().trim()
                                    .length() == 0) {
                                showToast("Please Enter Items Found on Encroachment");
                            }/*
                             * else if (otp_verify_status.equals("N")) {
                             * showToast("Please Verify OTP Number");
                             * owner_aadhaarMobileNo.requestFocus(); }
                             */ else if ("Y".equals(Dashboard.OtpStatus.trim()) && otp_verify_status.equals("N")) {
                                showToast("Please Verify OTP Number");
                            } else {
                                if (isOnline()) {
                                    new Async_PreviewDetails().execute();
                                } else {
                                    showToast("Please Check Your Network connection");
                                }
                            }
                        } else if (aadhaarNo.isChecked() == true) { // SHOP-->MANAGE-->OWNER
                            // Aadhaar
                            // Y-->RESP
                            // Aadhaar N
                            Log.i("COndition : ", "PERSON BASED ENTERED");
                            if (et_name.getText().toString().trim().equals("")) {
                                et_name.setError(Html
                                        .fromHtml("<font color='black'>Please Enter Name</font>"));
                                et_name.requestFocus();
                            } else if (et_father_name.getText().toString()
                                    .trim().equals("")) {
                                et_father_name.setError(Html
                                        .fromHtml("<font color='black'>Please Enter Father Number</font>"));
                                et_father_name.requestFocus();
                            } else if (et_age.getText().toString().trim()
                                    .equals("")) {
                                et_age.setError(Html
                                        .fromHtml("<font color='black'>Please Enter Age</font>"));
                                et_age.requestFocus();
                            } else if (et_mobileNo.getText().toString().trim()
                                    .equals("")) {
                                et_mobileNo.setError(Html
                                        .fromHtml("<font color='black'>Please Enter Age</font>"));
                                et_mobileNo.requestFocus();
                            } else if (et_address.getText().toString().trim()
                                    .equals("")) {
                                et_address.setError(Html
                                        .fromHtml("<font color='black'>Please Enter Address</font>"));
                                et_address.requestFocus();
                            } else if (aadhaar_image.getDrawable()
                                    .getConstantState() == getResources()
                                    .getDrawable(R.drawable.camera)
                                    .getConstantState()) {
                                showToast("Please Select Respondent Image");
                            } else if (Sections.checkedList.isEmpty()) {
                                showToast("Please Select atleast one Section");
                            } else if (DetainedItems.detItems.toString().trim()
                                    .length() == 0) {
                                showToast("Please Enter Items Found on Encroachment");
                            }/*
                             * else if (otp_verify_status.equals("N")) {
                             * showToast("Please Verify OTP Number");
                             * owner_aadhaarMobileNo.requestFocus(); }
                             */ else if ("Y".equals(Dashboard.OtpStatus.trim()) && otp_verify_status.equals("N")) {
                                showToast("Please Verify OTP Number");
                            } else {

                                if (isOnline()) {
                                    new Async_PreviewDetails().execute();
                                } else {
                                    showToast("Please Check Your Network connection");
                                }
                            }
                        }
                    }


                }

                /*
                 * else{ if (shop_based.isChecked()==true &&
                 * "SHOP".equals(based_on)) { if (owner_based.isChecked()==true
                 * && "OWNER".equals(run_By)) { if (tin_yes.isChecked()==true) {
                 * if (et_tin_no.getText().toString().trim().equals("")) {
                 * et_tin_no.requestFocus(); //et_tin_no.setError(Html.fromHtml(
                 * "<font color='black'>Please Enter Trade Licence Number</font>"
                 * )); showToast("Please Enter Tin No"); } }else if
                 * (tin_no.isChecked()==true) { if
                 * (et_shop_name.getText().toString().trim().equals("")) {
                 * et_shop_name.setError(Html.fromHtml(
                 * "<font color='black'>Please Enter Shop Name</font>"));
                 * et_shop_name.requestFocus(); }else if
                 * (et_firm_owner_name.getText().toString().trim().equals("")) {
                 * et_firm_owner_name.setError(Html.fromHtml(
                 * "<font color='black'>Please Enter Shop Owner Name</font>"));
                 * et_firm_owner_name.requestFocus(); }else if
                 * (et_firm_address.getText().toString().trim().equals("")) {
                 * et_firm_address.setError(Html.fromHtml(
                 * "<font color='black'>Please Enter Shop Owner Address</font>"
                 * )); et_firm_address.requestFocus(); } }else if
                 * (owner_aadhaar_yes.isChecked()==true) { if
                 * (owner_aadhaarNo.getText().toString().trim().equals("")) {
                 * owner_aadhaarNo.setError(Html.fromHtml(
                 * "<font color='black'>Please Enter Aadhaar Number</font>"));
                 * owner_aadhaarNo.requestFocus(); }else if
                 * (owner_aadhaarNo.getText().toString().trim().length()>1 &&
                 * owner_aadhaarNo.getText().toString().trim().length() != 12) {
                 * owner_aadhaarNo.setError(Html.fromHtml(
                 * "<font color='black'>Please Enter Valid Aadhaar</font>"));
                 * owner_aadhaarNo.requestFocus(); } else if
                 * (owner_aadhaarNo.getText().toString().trim().length()>1 &&
                 * owner_aadhaarNo.getText().toString().trim().length() == 12 &&
                 * !ver.isValid(owner_aadhaarNo.getText().toString().trim())) {
                 * owner_aadhaarNo.setError(Html.fromHtml(
                 * "<font color='black'>Please Enter Valid Aadhaar</font>"));
                 * owner_aadhaarNo.requestFocus(); }else if
                 * (owner_aadhaarName.getText().toString().trim().equals("")) {
                 * owner_aadhaarName.setError(Html.fromHtml(
                 * "<font color='black'>Please Enter Name</font>"));
                 * owner_aadhaarName.requestFocus(); }else if
                 * (owner_aadhaarFatherName
                 * .getText().toString().trim().equals("")) {
                 * owner_aadhaarFatherName.setError(Html.fromHtml(
                 * "<font color='black'>Please Enter Owner Father Number</font>"
                 * )); owner_aadhaarFatherName.requestFocus(); }else if
                 * (owner_aadhaarAge.getText().toString().trim().equals("")) {
                 * owner_aadhaarAge.setError(Html.fromHtml(
                 * "<font color='black'>Please Enter Owner Age</font>"));
                 * owner_aadhaarAge.requestFocus(); }else if
                 * (owner_aadhaarMobileNo
                 * .getText().toString().trim().equals("")) {
                 * owner_aadhaarMobileNo.setError(Html.fromHtml(
                 * "<font color='black'>Please Enter Owner Age</font>"));
                 * owner_aadhaarMobileNo.requestFocus(); }else if
                 * (owner_aadhaarAddress.getText().toString().trim().equals(""))
                 * { owner_aadhaarAddress.setError(Html.fromHtml(
                 * "<font color='black'>Please Enter Owner Address</font>"));
                 * owner_aadhaarAddress.requestFocus(); }else if
                 * (aadhaar_ownerImage.getDrawable().getConstantState() ==
                 * getResources
                 * ().getDrawable(R.drawable.camera).getConstantState()) {
                 * showToast("Please Select Image"); }else if
                 * (otp_verify_status.equals("N")) {
                 * showToast("Please Verify OTP Number");
                 * owner_aadhaarMobileNo.requestFocus(); }else if
                 * (Sections.checkedList.isEmpty()) {
                 * showToast("Please Select atleast one Section"); }else { new
                 * Async_PreviewDetails().execute();
                 *
                 * } }else if (owner_aadhaar_no.isChecked()==true) { if
                 * (owner_aadhaarName.getText().toString().trim().equals("")) {
                 * owner_aadhaarName.setError(Html.fromHtml(
                 * "<font color='black'>Please Enter Name</font>"));
                 * owner_aadhaarName.requestFocus(); }else if
                 * (owner_aadhaarFatherName
                 * .getText().toString().trim().equals("")) {
                 * owner_aadhaarFatherName.setError(Html.fromHtml(
                 * "<font color='black'>Please Enter Owner Father Number</font>"
                 * )); owner_aadhaarFatherName.requestFocus(); }else if
                 * (owner_aadhaarAge.getText().toString().trim().equals("")) {
                 * owner_aadhaarAge.setError(Html.fromHtml(
                 * "<font color='black'>Please Enter Owner Age</font>"));
                 * owner_aadhaarAge.requestFocus(); }else if
                 * (owner_aadhaarMobileNo
                 * .getText().toString().trim().equals("")) {
                 * owner_aadhaarMobileNo.setError(Html.fromHtml(
                 * "<font color='black'>Please Enter Owner Age</font>"));
                 * owner_aadhaarMobileNo.requestFocus(); }else if
                 * (owner_aadhaarAddress.getText().toString().trim().equals(""))
                 * { owner_aadhaarAddress.setError(Html.fromHtml(
                 * "<font color='black'>Please Enter Owner Address</font>"));
                 * owner_aadhaarAddress.requestFocus(); }else if
                 * (aadhaar_ownerImage.getDrawable().getConstantState() ==
                 * getResources
                 * ().getDrawable(R.drawable.camera).getConstantState()) {
                 * showToast("Please Select Image"); }else if
                 * (otp_verify_status.equals("N")) {
                 * showToast("Please Verify OTP Number");
                 * owner_aadhaarMobileNo.requestFocus(); }else if
                 * (Sections.checkedList.isEmpty()) {
                 * showToast("Please Select atleast one Section"); }else { new
                 * Async_PreviewDetails().execute();
                 *
                 * } } }else if (manager_based.isChecked()==true &&
                 * "MANAGER".equals(run_By)) { if (tin_yes.isChecked()==true) {
                 * if (et_tin_no.getText().toString().trim().equals("")) {
                 * //et_tin_no.setError(Html.fromHtml(
                 * "<font color='black'>Please Enter Trade Licence Number</font>"
                 * )); et_tin_no.requestFocus(); } }else if
                 * (tin_no.isChecked()==true) { if
                 * (et_shop_name.getText().toString().trim().equals("")) {
                 * et_shop_name.setError(Html.fromHtml(
                 * "<font color='black'>Please Enter Shop Name</font>"));
                 * et_shop_name.requestFocus(); }else if
                 * (et_firm_owner_name.getText().toString().trim().equals("")) {
                 * et_firm_owner_name.setError(Html.fromHtml(
                 * "<font color='black'>Please Enter Shop Owner Name</font>"));
                 * et_firm_owner_name.requestFocus(); }else if
                 * (et_firm_address.getText().toString().trim().equals("")) {
                 * et_firm_address.setError(Html.fromHtml(
                 * "<font color='black'>Please Enter Shop Owner Address</font>"
                 * )); et_firm_address.requestFocus(); } }
                 *
                 * // Owner Aadhaar Details if
                 * (owner_aadhaar_yes.isChecked()==true) { if
                 * (owner_aadhaarNo.getText().toString().trim().equals("")) {
                 * owner_aadhaarNo.setError(Html.fromHtml(
                 * "<font color='black'>Please Enter Aadhaar Number</font>"));
                 * owner_aadhaarNo.requestFocus(); }else if
                 * (owner_aadhaarNo.getText().toString().trim().length()>1 &&
                 * owner_aadhaarNo.getText().toString().trim().length() != 12) {
                 * owner_aadhaarNo.setError(Html.fromHtml(
                 * "<font color='black'>Please Enter Valid Aadhaar</font>"));
                 * owner_aadhaarNo.requestFocus(); } else if
                 * (owner_aadhaarNo.getText().toString().trim().length()>1 &&
                 * owner_aadhaarNo.getText().toString().trim().length() == 12 &&
                 * !ver.isValid(owner_aadhaarNo.getText().toString().trim())) {
                 * owner_aadhaarNo.setError(Html.fromHtml(
                 * "<font color='black'>Please Enter Valid Aadhaar</font>"));
                 * owner_aadhaarNo.requestFocus(); }else if
                 * (owner_aadhaarName.getText().toString().trim().equals("")) {
                 * owner_aadhaarName.setError(Html.fromHtml(
                 * "<font color='black'>Please Enter Name</font>"));
                 * owner_aadhaarName.requestFocus(); }else if
                 * (owner_aadhaarFatherName
                 * .getText().toString().trim().equals("")) {
                 * owner_aadhaarFatherName.setError(Html.fromHtml(
                 * "<font color='black'>Please Enter Owner Father Number</font>"
                 * )); owner_aadhaarFatherName.requestFocus(); }else if
                 * (owner_aadhaarAge.getText().toString().trim().equals("")) {
                 * owner_aadhaarAge.setError(Html.fromHtml(
                 * "<font color='black'>Please Enter Owner Age</font>"));
                 * owner_aadhaarAge.requestFocus(); }else if
                 * (owner_aadhaarMobileNo
                 * .getText().toString().trim().equals("")) {
                 * owner_aadhaarMobileNo.setError(Html.fromHtml(
                 * "<font color='black'>Please Enter Owner Age</font>"));
                 * owner_aadhaarMobileNo.requestFocus(); }else if
                 * (owner_aadhaarAddress.getText().toString().trim().equals(""))
                 * { owner_aadhaarAddress.setError(Html.fromHtml(
                 * "<font color='black'>Please Enter Owner Address</font>"));
                 * owner_aadhaarAddress.requestFocus(); }else if
                 * (aadhaar_ownerImage.getDrawable().getConstantState() ==
                 * getResources
                 * ().getDrawable(R.drawable.camera).getConstantState()) {
                 * showToast("Please Select Image"); }else if
                 * (otp_verify_status.equals("N")) {
                 * showToast("Please Verify OTP Number");
                 * owner_aadhaarMobileNo.requestFocus(); } }else if
                 * (owner_aadhaar_no.isChecked()==true) { if
                 * (owner_aadhaarName.getText().toString().trim().equals("")) {
                 * owner_aadhaarName.setError(Html.fromHtml(
                 * "<font color='black'>Please Enter Name</font>"));
                 * owner_aadhaarName.requestFocus(); }else if
                 * (owner_aadhaarFatherName
                 * .getText().toString().trim().equals("")) {
                 * owner_aadhaarFatherName.setError(Html.fromHtml(
                 * "<font color='black'>Please Enter Owner Father Number</font>"
                 * )); owner_aadhaarFatherName.requestFocus(); }else if
                 * (owner_aadhaarAge.getText().toString().trim().equals("")) {
                 * owner_aadhaarAge.setError(Html.fromHtml(
                 * "<font color='black'>Please Enter Owner Age</font>"));
                 * owner_aadhaarAge.requestFocus(); }else if
                 * (owner_aadhaarMobileNo
                 * .getText().toString().trim().equals("")) {
                 * owner_aadhaarMobileNo.setError(Html.fromHtml(
                 * "<font color='black'>Please Enter Owner Age</font>"));
                 * owner_aadhaarMobileNo.requestFocus(); }else if
                 * (owner_aadhaarAddress.getText().toString().trim().equals(""))
                 * { owner_aadhaarAddress.setError(Html.fromHtml(
                 * "<font color='black'>Please Enter Owner Address</font>"));
                 * owner_aadhaarAddress.requestFocus(); }else if
                 * (aadhaar_ownerImage.getDrawable().getConstantState() ==
                 * getResources
                 * ().getDrawable(R.drawable.camera).getConstantState()) {
                 * showToast("Please Select Image"); }else if
                 * (otp_verify_status.equals("N")) {
                 * showToast("Please Verify OTP Number");
                 * owner_aadhaarMobileNo.requestFocus(); } }else if
                 * (aadhaarYes.isChecked()==true) { if
                 * (aadhaar_no.getText().toString().trim().equals("")) {
                 * aadhaar_no.setError(Html.fromHtml(
                 * "<font color='black'>Please Enter Valid Aadhaar</font>"));
                 * aadhaar_no.requestFocus(); } else if
                 * (aadhaar_no.getText().toString().trim().length()>1 &&
                 * aadhaar_no.getText().toString().trim().length() != 12) {
                 * aadhaar_no.setError(Html.fromHtml(
                 * "<font color='black'>Please Enter Valid Aadhaar</font>"));
                 * aadhaar_no.requestFocus(); } else if
                 * (aadhaar_no.getText().toString().trim().length()>1 &&
                 * aadhaar_no.getText().toString().trim().length() == 12 &&
                 * !ver.isValid(aadhaar_no.getText().toString().trim())) {
                 * aadhaar_no.setError(Html.fromHtml(
                 * "<font color='black'>Please Enter Valid Aadhaar</font>"));
                 * aadhaar_no.requestFocus(); }else if
                 * (et_name.getText().toString().trim().equals("")) {
                 * et_name.setError
                 * (Html.fromHtml("<font color='black'>Please Enter Name</font>"
                 * )); et_name.requestFocus(); }else if
                 * (et_father_name.getText().toString().trim().equals("")) {
                 * et_father_name.setError(Html.fromHtml(
                 * "<font color='black'>Please Enter Father Number</font>"));
                 * et_father_name.requestFocus(); }else if
                 * (et_age.getText().toString().trim().equals("")) {
                 * et_age.setError
                 * (Html.fromHtml("<font color='black'>Please Enter Age</font>"
                 * )); et_age.requestFocus(); }else if
                 * (et_mobileNo.getText().toString().trim().equals("")) {
                 * et_mobileNo.setError(Html.fromHtml(
                 * "<font color='black'>Please Enter Age</font>"));
                 * et_mobileNo.requestFocus(); }else if
                 * (et_address.getText().toString().trim().equals("")) {
                 * et_address.setError(Html.fromHtml(
                 * "<font color='black'>Please Enter Address</font>"));
                 * et_address.requestFocus(); }else if
                 * (aadhaar_image.getDrawable().getConstantState() ==
                 * getResources
                 * ().getDrawable(R.drawable.camera).getConstantState()) {
                 * showToast("Please Select Image"); }else if
                 * (otp_verify_status.equals("N")) {
                 * showToast("Please Verify OTP Number");
                 * et_mobileNo.requestFocus(); } }else if
                 * (aadhaarNo.isChecked()==true) { if
                 * (et_name.getText().toString().trim().equals("")) {
                 * et_name.setError
                 * (Html.fromHtml("<font color='black'>Please Enter Name</font>"
                 * )); et_name.requestFocus(); }else if
                 * (et_father_name.getText().toString().trim().equals("")) {
                 * et_father_name.setError(Html.fromHtml(
                 * "<font color='black'>Please Enter Father Number</font>"));
                 * et_father_name.requestFocus(); }else if
                 * (et_age.getText().toString().trim().equals("")) {
                 * et_age.setError
                 * (Html.fromHtml("<font color='black'>Please Enter Age</font>"
                 * )); et_age.requestFocus(); }else if
                 * (et_mobileNo.getText().toString().trim().equals("")) {
                 * et_mobileNo.setError(Html.fromHtml(
                 * "<font color='black'>Please Enter Age</font>"));
                 * et_mobileNo.requestFocus(); }else if
                 * (et_address.getText().toString().trim().equals("")) {
                 * et_address.setError(Html.fromHtml(
                 * "<font color='black'>Please Enter Address</font>"));
                 * et_address.requestFocus(); }else if
                 * (aadhaar_image.getDrawable().getConstantState() ==
                 * getResources
                 * ().getDrawable(R.drawable.camera).getConstantState()) {
                 * showToast("Please Select Image"); }else if
                 * (otp_verify_status.equals("N")) {
                 * showToast("Please Verify OTP Number");
                 * et_mobileNo.requestFocus(); } }else if
                 * (Sections.checkedList.isEmpty()) {
                 * showToast("Please Select atleast one Section"); }else { new
                 * Async_PreviewDetails().execute();
                 *
                 * } }else if (shopkeeper_based.isChecked()==true &&
                 * "SHOPKEEPER".equals(run_By)) { if (tin_yes.isChecked()==true)
                 * { if (et_tin_no.getText().toString().trim().equals("")) {
                 * //et_tin_no.setError(Html.fromHtml(
                 * "<font color='black'>Please Enter Trade Licence Number</font>"
                 * )); et_tin_no.requestFocus(); }
                 *
                 * }else if (tin_no.isChecked()==true) { if
                 * (et_shop_name.getText().toString().trim().equals("")) {
                 * et_shop_name.setError(Html.fromHtml(
                 * "<font color='black'>Please Enter Shop Name</font>"));
                 * et_shop_name.requestFocus(); }else if
                 * (et_firm_owner_name.getText().toString().trim().equals("")) {
                 * et_firm_owner_name.setError(Html.fromHtml(
                 * "<font color='black'>Please Enter Shop Owner Name</font>"));
                 * et_firm_owner_name.requestFocus(); }else if
                 * (et_firm_address.getText().toString().trim().equals("")) {
                 * et_firm_address.setError(Html.fromHtml(
                 * "<font color='black'>Please Enter Shop Owner Address</font>"
                 * )); et_firm_address.requestFocus(); } }else if
                 * (owner_aadhaar_yes.isChecked()==true) { if
                 * (owner_aadhaarNo.getText().toString().trim().equals("")) {
                 * owner_aadhaarNo.setError(Html.fromHtml(
                 * "<font color='black'>Please Enter Aadhaar Number</font>"));
                 * owner_aadhaarNo.requestFocus(); }else if
                 * (owner_aadhaarNo.getText().toString().trim().length()!=12) {
                 * owner_aadhaarNo.setError(Html.fromHtml(
                 * "<font color='black'>Please Enter Valid Aadhaar Number</font>"
                 * )); owner_aadhaarNo.requestFocus(); }else if
                 * (owner_aadhaarNo.getText().toString().trim().length()>1 &&
                 * owner_aadhaarNo.getText().toString().trim().length() == 12 &&
                 * !ver.isValid(owner_aadhaarNo.getText().toString().trim())) {
                 * owner_aadhaarNo.setError(Html.fromHtml(
                 * "<font color='black'>Please Enter Valid Aadhaar Number</font>"
                 * )); owner_aadhaarNo.requestFocus(); }else if
                 * (owner_aadhaarName.getText().toString().trim().equals("")) {
                 * owner_aadhaarName.setError(Html.fromHtml(
                 * "<font color='black'>Please Enter Name</font>"));
                 * owner_aadhaarName.requestFocus(); }else if
                 * (owner_aadhaarFatherName
                 * .getText().toString().trim().equals("")) {
                 * owner_aadhaarFatherName.setError(Html.fromHtml(
                 * "<font color='black'>Please Enter Owner Father Number</font>"
                 * )); owner_aadhaarFatherName.requestFocus(); }else if
                 * (owner_aadhaarAge.getText().toString().trim().equals("")) {
                 * owner_aadhaarAge.setError(Html.fromHtml(
                 * "<font color='black'>Please Enter Owner Age</font>"));
                 * owner_aadhaarAge.requestFocus(); }else if
                 * (owner_aadhaarMobileNo
                 * .getText().toString().trim().equals("")) {
                 * owner_aadhaarMobileNo.setError(Html.fromHtml(
                 * "<font color='black'>Please Enter Owner Age</font>"));
                 * owner_aadhaarMobileNo.requestFocus(); }else if
                 * (owner_aadhaarAddress.getText().toString().trim().equals(""))
                 * { owner_aadhaarAddress.setError(Html.fromHtml(
                 * "<font color='black'>Please Enter Owner Address</font>"));
                 * owner_aadhaarAddress.requestFocus(); }else if
                 * (aadhaar_ownerImage.getDrawable().getConstantState() ==
                 * getResources
                 * ().getDrawable(R.drawable.camera).getConstantState()) {
                 * showToast("Please Select Image"); }else if
                 * (owner_otp_verify_status.equals("N")) {
                 * showToast("Please Verify OTP Number");
                 * owner_aadhaarMobileNo.requestFocus(); } }else if
                 * (owner_aadhaar_no.isChecked()==true) { if
                 * (owner_aadhaarName.getText().toString().trim().equals("")) {
                 * owner_aadhaarName.setError(Html.fromHtml(
                 * "<font color='black'>Please Enter Name</font>"));
                 * owner_aadhaarName.requestFocus(); }else if
                 * (owner_aadhaarFatherName
                 * .getText().toString().trim().equals("")) {
                 * owner_aadhaarFatherName.setError(Html.fromHtml(
                 * "<font color='black'>Please Enter Owner Father Number</font>"
                 * )); owner_aadhaarFatherName.requestFocus(); }else if
                 * (owner_aadhaarAge.getText().toString().trim().equals("")) {
                 * owner_aadhaarAge.setError(Html.fromHtml(
                 * "<font color='black'>Please Enter Owner Age</font>"));
                 * owner_aadhaarAge.requestFocus(); }else if
                 * (owner_aadhaarMobileNo
                 * .getText().toString().trim().equals("")) {
                 * owner_aadhaarMobileNo.setError(Html.fromHtml(
                 * "<font color='black'>Please Enter Owner Age</font>"));
                 * owner_aadhaarMobileNo.requestFocus(); }else if
                 * (owner_aadhaarAddress.getText().toString().trim().equals(""))
                 * { owner_aadhaarAddress.setError(Html.fromHtml(
                 * "<font color='black'>Please Enter Owner Address</font>"));
                 * owner_aadhaarAddress.requestFocus(); }else if
                 * (aadhaar_ownerImage.getDrawable().getConstantState() ==
                 * getResources
                 * ().getDrawable(R.drawable.camera).getConstantState()) {
                 * showToast("Please Select Image"); }else if
                 * (owner_otp_verify_status.equals("N")) {
                 * showToast("Please Verify OTP Number");
                 * owner_aadhaarMobileNo.requestFocus(); } }else if
                 * (aadhaarYes.isChecked()==true) { if
                 * (aadhaar_no.getText().toString().trim().equals("")) {
                 * aadhaar_no.setError(Html.fromHtml(
                 * "<font color='black'>Please Enter Valid Aadhaar</font>"));
                 * aadhaar_no.requestFocus(); } else if
                 * (aadhaar_no.getText().toString().trim().length()>1 &&
                 * aadhaar_no.getText().toString().trim().length() != 12) {
                 * aadhaar_no.setError(Html.fromHtml(
                 * "<font color='black'>Please Enter Valid Aadhaar</font>"));
                 * aadhaar_no.requestFocus(); } else if
                 * (aadhaar_no.getText().toString().trim().length()>1 &&
                 * aadhaar_no.getText().toString().trim().length() == 12 &&
                 * !ver.isValid(aadhaar_no.getText().toString().trim())) {
                 * aadhaar_no.setError(Html.fromHtml(
                 * "<font color='black'>Please Enter Valid Aadhaar</font>"));
                 * aadhaar_no.requestFocus(); }else if
                 * (et_name.getText().toString().trim().equals("")) {
                 * et_name.setError
                 * (Html.fromHtml("<font color='black'>Please Enter Name</font>"
                 * )); et_name.requestFocus(); }else if
                 * (et_father_name.getText().toString().trim().equals("")) {
                 * et_father_name.setError(Html.fromHtml(
                 * "<font color='black'>Please Enter Father Number</font>"));
                 * et_father_name.requestFocus(); }else if
                 * (et_age.getText().toString().trim().equals("")) {
                 * et_age.setError
                 * (Html.fromHtml("<font color='black'>Please Enter Age</font>"
                 * )); et_age.requestFocus(); }else if
                 * (et_mobileNo.getText().toString().trim().equals("")) {
                 * et_mobileNo.setError(Html.fromHtml(
                 * "<font color='black'>Please Enter Age</font>"));
                 * et_mobileNo.requestFocus(); }else if
                 * (et_address.getText().toString().trim().equals("")) {
                 * et_address.setError(Html.fromHtml(
                 * "<font color='black'>Please Enter Address</font>"));
                 * et_address.requestFocus(); }else if
                 * (aadhaar_image.getDrawable().getConstantState() ==
                 * getResources
                 * ().getDrawable(R.drawable.camera).getConstantState()) {
                 * showToast("Please Select Image"); }else if
                 * (otp_verify_status.equals("N")) {
                 * showToast("Please Verify OTP Number");
                 * et_mobileNo.requestFocus(); } }else if
                 * (aadhaarNo.isChecked()==true) { if
                 * (et_name.getText().toString().trim().equals("")) {
                 * et_name.setError
                 * (Html.fromHtml("<font color='black'>Please Enter Name</font>"
                 * )); et_name.requestFocus(); }else if
                 * (et_father_name.getText().toString().trim().equals("")) {
                 * et_father_name.setError(Html.fromHtml(
                 * "<font color='black'>Please Enter Father Number</font>"));
                 * et_father_name.requestFocus(); }else if
                 * (et_age.getText().toString().trim().equals("")) {
                 * et_age.setError
                 * (Html.fromHtml("<font color='black'>Please Enter Age</font>"
                 * )); et_age.requestFocus(); }else if
                 * (et_mobileNo.getText().toString().trim().equals("")) {
                 * et_mobileNo.setError(Html.fromHtml(
                 * "<font color='black'>Please Enter Age</font>"));
                 * et_mobileNo.requestFocus(); }else if
                 * (et_address.getText().toString().trim().equals("")) {
                 * et_address.setError(Html.fromHtml(
                 * "<font color='black'>Please Enter Address</font>"));
                 * et_address.requestFocus(); }else if
                 * (aadhaar_image.getDrawable().getConstantState() ==
                 * getResources
                 * ().getDrawable(R.drawable.camera).getConstantState()) {
                 * showToast("Please Select Image"); }else if
                 * (otp_verify_status.equals("N")) {
                 * showToast("Please Verify OTP Number");
                 * et_mobileNo.requestFocus(); }else if
                 * (Sections.checkedList.isEmpty()) {
                 * showToast("Please Select atleast one Section"); }else { new
                 * Async_PreviewDetails().execute();
                 *
                 * } }else if (Sections.checkedList.isEmpty()) {
                 * showToast("Please Select atleast one Section"); }else { new
                 * Async_PreviewDetails().execute();
                 *
                 * } }
                 *
                 *
                 *
                 * }else if (person_based.isChecked()==true &&
                 * "PERSON".equals(based_on)) {
                 * Log.i("Person Based Condition ::::", "Entered"); if
                 * (aadhaarYes.isChecked()==true) { if
                 * (aadhaar_no.getText().toString().trim().equals("")) {
                 * aadhaar_no.setError(Html.fromHtml(
                 * "<font color='black'>Please Enter Valid Aadhaar</font>"));
                 * aadhaar_no.requestFocus(); } else if
                 * (aadhaar_no.getText().toString().trim().length()>1 &&
                 * aadhaar_no.getText().toString().trim().length() != 12) {
                 * aadhaar_no.setError(Html.fromHtml(
                 * "<font color='black'>Please Enter Valid Aadhaar</font>"));
                 * aadhaar_no.requestFocus(); } else if
                 * (aadhaar_no.getText().toString().trim().length()>1 &&
                 * aadhaar_no.getText().toString().trim().length() == 12 &&
                 * !ver.isValid(aadhaar_no.getText().toString().trim())) {
                 * aadhaar_no.setError(Html.fromHtml(
                 * "<font color='black'>Please Enter Valid Aadhaar</font>"));
                 * aadhaar_no.requestFocus(); }else if
                 * (et_name.getText().toString().trim().equals("")) {
                 * et_name.setError
                 * (Html.fromHtml("<font color='black'>Please Enter Name</font>"
                 * )); et_name.requestFocus(); }else if
                 * (et_father_name.getText().toString().trim().equals("")) {
                 * et_father_name.setError(Html.fromHtml(
                 * "<font color='black'>Please Enter Father Number</font>"));
                 * et_father_name.requestFocus(); }else if
                 * (et_age.getText().toString().trim().equals("")) {
                 * et_age.setError
                 * (Html.fromHtml("<font color='black'>Please Enter Age</font>"
                 * )); et_age.requestFocus(); }else if
                 * (et_mobileNo.getText().toString().trim().equals("")) {
                 * et_mobileNo.setError(Html.fromHtml(
                 * "<font color='black'>Please Enter Age</font>"));
                 * et_mobileNo.requestFocus(); }else if
                 * (et_address.getText().toString().trim().equals("")) {
                 * et_address.setError(Html.fromHtml(
                 * "<font color='black'>Please Enter Address</font>"));
                 * et_address.requestFocus(); }else if
                 * (aadhaar_image.getDrawable().getConstantState() ==
                 * getResources
                 * ().getDrawable(R.drawable.camera).getConstantState()) {
                 * showToast("Please Select Image"); }else if
                 * (otp_verify_status.equals("N")) {
                 * showToast("Please Verify OTP Number");
                 * et_mobileNo.requestFocus(); } else if
                 * (Sections.checkedList.isEmpty()) {
                 * showToast("Please Select atleast one Section"); }else {
                 *
                 * new Async_PreviewDetails().execute(); //new
                 * Async_Submit().execute();
                 *
                 * }
                 *
                 * }else if (aadhaarNo.isChecked()==true) { if
                 * (et_name.getText().toString().trim().equals("")) {
                 * et_name.setError
                 * (Html.fromHtml("<font color='black'>Please Enter Name</font>"
                 * )); et_name.requestFocus(); }else if
                 * (et_father_name.getText().toString().trim().equals("")) {
                 * et_father_name.setError(Html.fromHtml(
                 * "<font color='black'>Please Enter Father Number</font>"));
                 * et_father_name.requestFocus(); }else if
                 * (et_age.getText().toString().trim().equals("")) {
                 * et_age.setError
                 * (Html.fromHtml("<font color='black'>Please Enter Age</font>"
                 * )); et_age.requestFocus(); }else if
                 * (et_mobileNo.getText().toString().trim().equals("")) {
                 * et_mobileNo.setError(Html.fromHtml(
                 * "<font color='black'>Please Enter Age</font>"));
                 * et_mobileNo.requestFocus(); }else if
                 * (et_address.getText().toString().trim().equals("")) {
                 * et_address.setError(Html.fromHtml(
                 * "<font color='black'>Please Enter Address</font>"));
                 * et_address.requestFocus(); }else if
                 * (aadhaar_image.getDrawable().getConstantState() ==
                 * getResources
                 * ().getDrawable(R.drawable.camera).getConstantState()) {
                 * showToast("Please Select Image"); }else if
                 * (otp_verify_status.equals("N")) {
                 * showToast("Please Verify OTP Number");
                 * et_mobileNo.requestFocus(); }else if
                 * (Sections.checkedList.isEmpty()) {
                 * showToast("Please Select atleast one Section"); }else {
                 *
                 * new Async_PreviewDetails().execute(); //new
                 * Async_Submit().execute();
                 *
                 * }
                 *
                 * } } }
                 */
            }
        });

        back_btn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                Intent back = new Intent(GenerateCase.this, Dashboard.class);
                startActivity(back);
            }
        });

        pointNameBy_PsName_code_arr = new ArrayList<String>();// point code
        pointNameBy_PsName_arr = new ArrayList<String>();// point name

        subSecNameBy_secName_arr = new ArrayList<String>();// sub section code
        subSecNameBy_secName_arr = new ArrayList<String>();// sub section name

        loc_zoneCode_arr = new ArrayList<String>();
        loc_zoneName_arr = new ArrayList<String>();

        loc_wardCode_arr = new ArrayList<String>();
        loc_wardName_arr = new ArrayList<String>();

        loc_circleCode_arr = new ArrayList<String>();
        loc_circleName_arr = new ArrayList<String>();

        loc_code_arr = new ArrayList<String>();
        loc_name_arr = new ArrayList<String>();

        all_psNames_arr = new ArrayList<String>();
        all_pointNames_arr = new ArrayList<String>();

    }

    private void updateDisplay() {
        timestamp = new StringBuilder().append(pDay).append("/")
                .append(pMonth + 1).append("/").append(pYear).append("/")
                .append(mHour).append(":").append(mMinute).append(":")
                .append(mSecond).append(" ");
    }

    private void GetDatabaseValues() {

        db = new DataBase(getApplicationContext());
        try {
            db.open();
            select_ps_name.setText("" + psName);

            c_psnames = DataBase.db.rawQuery("select * from "
                    + DataBase.psName_table, null);
            if (c_psnames.getCount() == 0) {
                Log.i("PS DETAILS", "0");
            } else {
                psname_code = new String[c_psnames.getCount()];
                psname_name = new String[c_psnames.getCount()];

                int count = 0;
                while (c_psnames.moveToNext()) {
                    psname_code[count] = c_psnames.getString(1);
                    psname_name[count] = c_psnames.getString(2);

                    String name = "" + psname_name[count];

                    if (select_ps_name.getText().toString().trim().equals(name)) {
                        psCode_selcted = Integer.parseInt(psname_code[count]);

                        Log.i("psCode_selcted :::", "" + psCode_selcted);
                    }
                    count++;

                }
            }
            if (c_psnames != null) {
                c_psnames.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            db.close();
        }

    }

    public class Async_task_GetPointNames extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {
            // TODO Auto-generated method stub
            String query = "select PS_CODE from " + DataBase.psName_table + " where PS_NAME='"
                    + select_ps_name.getText().toString().trim() + "'";
            Log.i("query ::::", "" + query);
            // selectedPs_code
            all_psNames_arr.clear();
            db = new DataBase(getApplicationContext());

            try {
                db.open();
                Cursor loc_cursor = DataBase.db.rawQuery(query, null);

                if (loc_cursor.moveToNext()) {
                    do {
                        // String fieldToAdd=c.getString(0);
                        Log.i("fieldToAdd ::", "" + loc_cursor.getString(0));
                        // my_psCode_arr.add(fieldToAdd);
                        selectedPs_code = "" + loc_cursor.getString(0);
                    } while (loc_cursor.moveToNext());
                }
                loc_cursor.close();

            } catch (Exception e) {
                e.printStackTrace();
                if (db != null) {
                    db.close();
                }
            }
            Log.i("selectedPs_code :::", "" + selectedPs_code);
            ServiceHelper.getPointNameByPsNames("" + selectedPs_code);
            // }
            return null;
        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            showDialog(PROGRESS_DIALOG);
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            removeDialog(PROGRESS_DIALOG);

            if (ServiceHelper.point_resp != null) {
                String[] point_array = ServiceHelper.point_resp.split("\\@");
                all_psNames_arr.clear();

                for (String locationDet : point_array) {
                    String[] cirleDet = locationDet.split("\\:");
                    try {
                        db.open();
                        db.insertPointNamesDetails("" + cirleDet[0], "" + cirleDet[1]);
                        // 01-17 11:10:27.298: I/All Points Values :::(11208):
                        // UPPAL BUS STOP
                        // 01-17 11:10:27.306: I/All Points Values :::(11208):
                        // 230109
                        pointNameMap.put(cirleDet[1].trim(), cirleDet[0].trim());
                        Log.i("All Points Values :::", "" + cirleDet[0] + "\n " + cirleDet[1]);
                    } catch (Exception e) {
                        // TODO: handle exception
                        if (db != null) {
                            db.close();
                        }
                    }
                }
                all_pointNames_arr.clear();
                for (int i = 0; i < ServiceHelper.PointNamesBypsNames_master.length; i++) {
                    String allPoints = ServiceHelper.PointNamesBypsNames_master[i].split("\\:")[1];
                    Log.i("allPSnames", allPoints);
                    all_pointNames_arr.add(allPoints);
                }
            }

            runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    // TODO Auto-generated method stub
                    TextView title = new TextView(GenerateCase.this);
                    title.setText("Select Point Name:");
                    title.setBackgroundColor(Color.parseColor("#007300"));
                    title.setGravity(Gravity.CENTER);
                    title.setTextColor(Color.WHITE);
                    title.setTextSize(26);
                    title.setTypeface(title.getTypeface(), Typeface.BOLD);
                    title.setCompoundDrawablesWithIntrinsicBounds(
                            R.drawable.traffic_small, 0, R.drawable.ghmc_small, 0);
                    title.setPadding(10, 0, 10, 0);
                    title.setHeight(70);

                    AlertDialog.Builder builderSingle = new AlertDialog.Builder(GenerateCase.this);
                    builderSingle.setCustomTitle(title);
                    builderSingle.setItems(all_pointNames_arr.toArray(new CharSequence[all_pointNames_arr.size()]),
                            new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(
                                        DialogInterface dialog,
                                        int which) {
                                    // TODO Auto-generated method stub
                                    select_point_name.setText(all_pointNames_arr.get(which));
                                    Log.i("Point Name Selected :::", "" + all_pointNames_arr.get(which));
                                }
                            });
                    builderSingle.show().getWindow().setLayout(500, 800);
                }
            });
        }
    }

    class Async_getPrevHistry extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            showDialog(PROGRESS_DIALOG);
        }

        @Override
        protected String doInBackground(Void... params) {
            // TODO Auto-generated method stub
            String firmRegn_No = "" + et_tin_no.getText().toString().trim();
            String shop_Name = "" + et_shop_name.getText().toString().trim();
            String iD_Details = ""
                    + owner_aadhaarNo.getText().toString().trim();
            String gps_Latitude = "" + latitude;
            String gps_Longitude = "" + longitude;

            ServiceHelper.get39Bhistry(firmRegn_No, shop_Name, iD_Details,
                    gps_Latitude, gps_Longitude);

            ServiceHelper.get_ghmchistry(firmRegn_No, shop_Name, iD_Details,
                    gps_Latitude, gps_Longitude);

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            removeDialog(PROGRESS_DIALOG);

            if (ServiceHelper.tab39b_resp != null) {

                thirty_nine_B_resp = "" + ServiceHelper.tab39b_resp;
                Log.i("thirty_nine_B_resp :::", "" + thirty_nine_B_resp);
                showDialog(PREVIOUS_HOSTORY);

            } else {
                thirty_nine_B_resp = null;
                showToast("No Tab39B Previous History Found!!!");
            }

            if (ServiceHelper.ghmc_hisrty_resp != null) {
                ghmc_prevHstry = "" + ServiceHelper.ghmc_hisrty_resp;
                Log.i("ghmc_prevHstry :::", "" + ghmc_prevHstry);
                showDialog(PREVIOUS_HOSTORY);

            } else {
                ghmc_prevHstry = null;
                showToast("No Previous GHMC History Found!!!");
            }

        }

    }

    // Async_getOwner_AadhaarData
    public class Async_getOwner_AadhaarData extends
            AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showDialog(PROGRESS_DIALOG);
        }

        @Override
        protected String doInBackground(Void... params) {
            if ("SHOP".equals(based_on)) {
                String firmRegn_No = "" + et_tin_no.getText().toString().trim();
                String shop_Name = ""
                        + et_shop_name.getText().toString().trim();
                String iD_No_Details = ""
                        + owner_aadhaarNo.getText().toString().trim();

                System.out.println("iD_No_Details :" + iD_No_Details);
                String gps_Latitude = "" + latitude;
                String gps_Longitude = "" + longitude;

                ServiceHelper.get39Bhistry_owner("", "", iD_No_Details,
                        gps_Latitude, gps_Longitude);

                ServiceHelper.get_ghmchistry_owner("", "", iD_No_Details,
                        gps_Latitude, gps_Longitude);

                /*
                 * ServiceHelper.get39Bhistry_owner(firmRegn_No, shop_Name,
                 * iD_No_Details, gps_Latitude, gps_Longitude);
                 *
                 * ServiceHelper.get_ghmchistry_owner(firmRegn_No, shop_Name,
                 * iD_No_Details, gps_Latitude, gps_Longitude);
                 */

                if ("Y".equals(AADHAAR_DATA_FLAG)) {
                    ServiceHelper.getAadhaar(iD_No_Details, iD_No_Details);
                }

            } else if ("PERSON".equals(based_on)) {
                String firmRegn_No = "";
                String shop_Name = "";
                String iD_No_Details = ""
                        + owner_aadhaarNo.getText().toString().trim();
                String gps_Latitude = "" + latitude;
                String gps_Longitude = "" + longitude;

                ServiceHelper.get39Bhistry("", "", iD_No_Details, gps_Latitude,
                        gps_Longitude);

                ServiceHelper.get_ghmchistry("", "", iD_No_Details,
                        gps_Latitude, gps_Longitude);
                if ("Y".equals(AADHAAR_DATA_FLAG)) {
                    ServiceHelper.getAadhaar(iD_No_Details, iD_No_Details);
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            removeDialog(PROGRESS_DIALOG);
            if (!isNetworkAvailable()) {

                showToast("Please Check Your Network Connection");
            } else {
                /*************** PREVIOUS HISTORY RESPONSE *******************/
                if (ServiceHelper.tab39b_resp != null && ServiceHelper.tab39b_resp != "0") {

                    thirty_nine_B_resp = "" + ServiceHelper.tab39b_resp;
                    showDialog(PREVIOUS_HOSTORY);

                } else {
                    thirty_nine_B_resp = null;
                    showToast("No Tab39B Previous History Found!!!");
                }

                if (ServiceHelper.ghmc_hisrty_resp != null) {
                    ghmc_prevHstry = "" + ServiceHelper.ghmc_hisrty_resp;
                    showDialog(PREVIOUS_HOSTORY);

                } else {
                    ghmc_prevHstry = null;
                    showToast("No Previous GHMC History Found!!!");
                }
                /*************** PREVIOUS HISTORY RESPONSE ENDS *******************/

                /*************** OWNER AADHAAR RESPONSE *******************/
                if ("Y".equals(AADHAAR_DATA_FLAG)) {
                    if (ServiceHelper.aadhaar_resp == null) {
                        // owner_aadhaar_details_layout.setVisibility(View.VISIBLE);
                        owner_aadhaarName.setEnabled(true);
                        owner_aadhaarFatherName.setEnabled(true);
                        owner_aadhaarAge.setEnabled(true);
                        owner_aadhaarAddress.setEnabled(true);

                        owner_aadhaarName.setText("");
                        owner_aadhaarFatherName.setText("");
                        owner_aadhaarAge.setText("");
                        owner_aadhaarAddress.setText("");

                        showToast("No Aadhaar Details Found !!!");
                    } else {

                        owner_aadhaar_details_layout
                                .setVisibility(View.VISIBLE);

                        try {


                            aadhar_details = ServiceHelper.aadhaar_resp
                                    .split("\\@");

                            owner_aadhaarName.setText("" + aadhar_details[0].trim());

                            owner_aadhaarFatherName.setText("" + aadhar_details[1].trim());

                            owner_aadhaarAddress.setText("" + aadhar_details[2]
                                    + ",\t" + aadhar_details[3] + ",\t"
                                    + aadhar_details[4] + ",\t" + aadhar_details[5]
                                    + ",\t" + aadhar_details[6] + ",\t"
                                    + aadhar_details[7]);

                            if (aadhar_details[8].trim().length() == 10) {
                                owner_aadhaarMobileNo.setText(""
                                        + aadhar_details[8]);
                            } else {
                                owner_aadhaarMobileNo.setText("");
                                owner_aadhaarMobileNo.setEnabled(true);
                            }

                            ownerAdhaar_gender = "" + aadhar_details[9];

                            if (ownerAdhaar_gender.equals("M")) {
                                owner_gender_male.setChecked(true);
                            } else if (ownerAdhaar_gender.equals("F")) {
                                owner_gender_female.setChecked(true);
                            }

                            String dob_age = "" + aadhar_details[10];
                            if (dob_age != null && dob_age.length() > 2) {

                                String validate = dob_age.substring(0, 3);
                                Log.i("validate ::::::::", "" + validate);

                                if (validate.contains("-")) {
                                    String[] split_dob = dob_age.split("\\-");
                                    String service_year = "" + split_dob[2];

                                    int final_age = pYear
                                            - Integer.parseInt(service_year);
                                    Log.i("final_age ::::::::", "" + final_age);

                                    owner_aadhaarAge.setText("" + final_age);
                                } else if (validate.contains("/")) {
                                    String[] split_dob = dob_age.split("\\/");
                                    String service_year = "" + split_dob[2];

                                    int final_age = pYear
                                            - Integer.parseInt(service_year);
                                    Log.i("final_age ::::::::", "" + final_age);

                                    owner_aadhaarAge.setText("" + final_age);
                                }
                            }

                            owner_image_data = "" + aadhar_details[13];
                            if (owner_image_data != null && owner_image_data.trim().length() > 100) {
                                owner_imageByteArray = Base64.decode(owner_image_data.getBytes(), 1);
                                Log.i("Image 2 byte[]", "" + Base64.decode(owner_image_data.trim().getBytes(), 1));
                                Bitmap bmp = BitmapFactory.decodeByteArray(owner_imageByteArray, 0, owner_imageByteArray.length);
                                aadhaar_ownerImage.setImageBitmap(bmp);

                                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                                bmp.compress(Bitmap.CompressFormat.JPEG, 10, bytes);
                                byteArray = bytes.toByteArray();
                                imgString4 = Base64.encodeToString(byteArray, Base64.NO_WRAP);

							/*BitmapFactory.Options bfOptions = new BitmapFactory.Options();
			                bfOptions.inDither = false;
			                bfOptions.inPurgeable = true;
			                bfOptions.inInputShareable = true;
			                bfOptions.inTempStorage = new byte[32 * 1024];*/

                            } else {
                                owner_image_data = null;
                            }

                            if (owner_image_data == null) {
                                aadhaar_ownerImage.setImageResource(R.drawable.camera);
                                aadhaar_ownerImage.setClickable(true);
                            }

                            int myColor = getApplicationContext().getResources().getColor(R.color.text_color_all);
                            if (owner_aadhaarName.getText().toString().trim().length() > 1) {
                                owner_aadhaarName.setEnabled(false);
                                owner_aadhaarName.setTextColor(myColor);
                            } else {
                                owner_aadhaarName.setEnabled(true);
                            }

                            if (owner_aadhaarFatherName.getText().toString().trim().length() > 1) {
                                owner_aadhaarFatherName.setEnabled(false);
                                owner_aadhaarFatherName.setTextColor(myColor);
                            } else {
                                owner_aadhaarFatherName.setEnabled(true);
                            }

                            if (owner_aadhaarAge.getText().toString().trim().length() > 1) {
                                owner_aadhaarAge.setEnabled(false);
                                owner_aadhaarAge.setTextColor(myColor);
                            } else {
                                owner_aadhaarAge.setEnabled(true);
                            }

                            if (owner_aadhaarAddress.getText().toString().trim().length() > 1) {
                                owner_aadhaarAddress.setEnabled(false);
                                owner_aadhaarAddress.setTextColor(myColor);
                            } else {
                                owner_aadhaarAddress.setEnabled(true);
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

                /*************** OWNER AADHAAR RESPONSE ENDS *******************/
            }
        }
    }

    public class Async_getAadhaarData extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            showDialog(PROGRESS_DIALOG);
        }

        @Override
        protected String doInBackground(Void... params) {
            // TODO Auto-generated method stub
            if ("SHOP".equals(based_on)) {

                String firmRegn_No = "" + et_tin_no.getText().toString().trim();
                String shop_Name = ""
                        + et_shop_name.getText().toString().trim();
                String iD_Details = "" + aadhaar_no.getText().toString().trim();
                String gps_Latitude = "" + latitude;
                String gps_Longitude = "" + longitude;

                /*
                 * ServiceHelper.get39Bhistry(firmRegn_No, shop_Name,
                 * iD_Details, gps_Latitude, gps_Longitude);
                 * ServiceHelper.get_ghmchistry(firmRegn_No, shop_Name,
                 * iD_Details, gps_Latitude, gps_Longitude);
                 */
                ServiceHelper.get39Bhistry("", "", iD_Details, gps_Latitude,
                        gps_Longitude);
                ServiceHelper.get_ghmchistry("", "", iD_Details, gps_Latitude,
                        gps_Longitude);
                if ("Y".equals(AADHAAR_DATA_FLAG)) {
                    ServiceHelper.getAadhaar(""
                            + aadhaar_no.getText().toString().trim(), ""
                            + aadhaar_no.getText().toString().trim());
                }
            } else if ("PERSON".equals(based_on)) {
                String firmRegn_No = "";
                String shop_Name = "";
                String iD_Details = "" + aadhaar_no.getText().toString().trim();
                String gps_Latitude = "" + latitude;
                String gps_Longitude = "" + longitude;

                ServiceHelper.get39Bhistry("", "", iD_Details, gps_Latitude,
                        gps_Longitude);

                ServiceHelper.get_ghmchistry("", "", iD_Details, gps_Latitude,
                        gps_Longitude);

                if ("Y".equals(AADHAAR_DATA_FLAG)) {
                    ServiceHelper.getAadhaar(""
                            + aadhaar_no.getText().toString().trim(), ""
                            + aadhaar_no.getText().toString().trim());
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            removeDialog(PROGRESS_DIALOG);
            if (!isNetworkAvailable()) {
                // do something
                showToast("Please Check Your Netork Connection");
            } else {
                /*************** PREVIOUS HISTORY RESPONSE ****************/
                if (ServiceHelper.tab39b_resp != null) {

                    thirty_nine_B_resp = "" + ServiceHelper.tab39b_resp;
                    showDialog(PREVIOUS_HOSTORY);

                } else {
                    thirty_nine_B_resp = null;
                    showToast("No Tab39B Previous History Found!!!");
                }

                if (ServiceHelper.ghmc_hisrty_resp != null) {
                    ghmc_prevHstry = "" + ServiceHelper.ghmc_hisrty_resp;
                    showDialog(PREVIOUS_HOSTORY);

                } else {
                    ghmc_prevHstry = null;
                    showToast("No Previous GHMC History Found!!!");
                }
                /*************** PREVIOUS HISTORY RESPONSE Ends ****************/

                /*************** AADHAAR RESPONSE ****************/
                if ("Y".equals(AADHAAR_DATA_FLAG)) {
                    if (ServiceHelper.aadhaar_resp.toString().trim()
                            .equals("0")
                            || ServiceHelper.aadhaar_resp.toString().trim()
                            .equals("NA")
                            || ServiceHelper.aadhaar_resp.toString().trim()
                            .contains("anyTypr{}")) {

                        // ll_aadhar_details.setVisibility(View.VISIBLE);
                        et_name.setEnabled(true);
                        et_father_name.setEnabled(true);
                        et_age.setEnabled(true);
                        et_address.setEnabled(true);
                        et_mobileNo.setEnabled(true);

                        et_name.setText("");
                        et_father_name.setText("");
                        et_age.setText("");
                        et_address.setText("");
                        et_mobileNo.setText("");
                        // 356476567
                        showToast("No Aadhaar Details Found !!!");
                    } else {

                        try {


                            ll_aadhar_details.setVisibility(View.VISIBLE);

                            aadhar_details = ServiceHelper.aadhaar_resp
                                    .split("\\@");

                            et_name.setText("" + aadhar_details[0]);

                            et_father_name.setText("" + aadhar_details[1]);

                            et_address.setText("" + aadhar_details[2] + ","
                                    + aadhar_details[3] + "," + aadhar_details[4]
                                    + "," + aadhar_details[5] + ","
                                    + aadhar_details[6] + "," + aadhar_details[7]);

                            if (aadhar_details[8].trim().length() == 10) {
                                et_mobileNo.setText("" + aadhar_details[8]);
                            } else {
                                et_mobileNo.setText("");
                                et_mobileNo.setEnabled(true);
                            }

                            resp_aadhaar_gender = "" + aadhar_details[9];

                            if (resp_aadhaar_gender.equals("M")) {
                                resp_male.setChecked(true);
                            } else if (ownerAdhaar_gender.equals("F")) {
                                resp_female.setChecked(true);
                            }

                            String dob_age = "" + aadhar_details[10];
                            if (dob_age != null && dob_age.length() > 2) {

                                String validate = dob_age.substring(0, 3);
                                Log.i("validate ::::::::", "" + validate);

                                if (validate.contains("-")) {
                                    String[] split_dob = dob_age.split("\\-");
                                    String service_year = "" + split_dob[2];

                                    int final_age = pYear
                                            - Integer.parseInt(service_year);
                                    Log.i("final_age ::::::::", "" + final_age);

                                    et_age.setText("" + final_age);
                                } else if (validate.contains("/")) {
                                    String[] split_dob = dob_age.split("\\/");
                                    String service_year = "" + split_dob[2];

                                    int final_age = pYear
                                            - Integer.parseInt(service_year);
                                    Log.i("final_age ::::::::", "" + final_age);

                                    et_age.setText("" + final_age);
                                }
                            }

                            image_data = "" + aadhar_details[13];
                            if (image_data != null
                                    && image_data.trim().length() > 100) {
                                imageByteArray = Base64.decode(
                                        image_data.getBytes(), 1);
                                Log.i("Image 2 byte[]",
                                        ""
                                                + Base64.decode(image_data.trim()
                                                .getBytes(), 1));
                                Bitmap bmp = BitmapFactory.decodeByteArray(
                                        imageByteArray, 0, imageByteArray.length);
                                aadhaar_image.setImageBitmap(bmp);

                                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                                bmp.compress(Bitmap.CompressFormat.JPEG, 10, bytes);

                                byteArray = bytes.toByteArray();

                                imgString3 = Base64.encodeToString(byteArray,
                                        Base64.NO_WRAP);
                            } else {
                                image_data = null;
                            }

                            if (image_data == null) {

                                aadhaar_image.setImageResource(R.drawable.camera);

                                aadhaar_image.setClickable(true);

                            }

                            int myColor = getApplicationContext().getResources()
                                    .getColor(R.color.text_color_all);
                            if (et_name.getText().toString().trim().length() > 1) {
                                et_name.setEnabled(false);
                                et_name.setTextColor(myColor);
                            } else {
                                et_name.setEnabled(true);
                            }

                            if (et_father_name.getText().toString().trim().length() > 1) {
                                et_father_name.setEnabled(false);
                                et_father_name.setTextColor(myColor);
                            } else {
                                et_father_name.setEnabled(true);
                            }

                            if (et_age.getText().toString().trim().length() > 1) {
                                et_age.setEnabled(false);
                                et_age.setTextColor(myColor);
                            } else {
                                et_age.setEnabled(true);
                            }

                            if (et_address.getText().toString().trim().length() > 1) {
                                et_address.setEnabled(false);
                                et_address.setTextColor(myColor);
                            } else {
                                et_address.setEnabled(true);
                            }

                            if (et_mobileNo.getText().toString() != null
                                    && et_mobileNo.getText().toString().trim()
                                    .length() > 1) {
                                et_mobileNo.setEnabled(true);
                                et_mobileNo.setTextColor(myColor);
                            } else if (et_mobileNo.getText().toString().trim()
                                    .equals("0")) {
                                et_mobileNo.setEnabled(true);
                                et_mobileNo.setTextColor(myColor);
                            } else {
                                et_mobileNo.setEnabled(true);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

                /*************** AADHAAR RESPONSE ENDS ****************/
            }
        }
    }

    /*********************** Network Check ***************************/
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager
                .getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    /*********************** Network Check ***************************/

    public class Async_getOTP extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            showDialog(PROGRESS_DIALOG);
        }

        @Override
        protected String doInBackground(Void... params) {
            // TODO Auto-generated method stub

            if (OwnerOTPFLG) {
                ServiceHelper.sendOTPtoMobile(owner_aadhaarMobileNo.getText()
                        .toString().trim(), "" + getDate().toUpperCase());
            } else if (respondentOTPFLG) {
                ServiceHelper.sendOTPtoMobile(et_mobileNo.getText().toString()
                        .trim(), "" + getDate().toUpperCase());
            }

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            removeDialog(PROGRESS_DIALOG);
            if (ServiceHelper.otp_Send_resp != null) {
                if ((!ServiceHelper.otp_Send_resp.equals("0")
                        || !ServiceHelper.otp_Send_resp.equals("")
                        || !ServiceHelper.otp_Send_resp.equals("NA") || !ServiceHelper.otp_Send_resp
                        .equals("anyType{}"))) {

                    SharedPreferences sharedPreference = PreferenceManager
                            .getDefaultSharedPreferences(getApplicationContext());

                    SharedPreferences.Editor editor = sharedPreference.edit();

                    editor.putString("OTP_Num", "" + ServiceHelper.otp_Send_resp);
                    editor.putString("OTP_DATE", "" + getDate().toUpperCase());

                    if (OwnerOTPFLG) {
                        editor.putString("MobileNo", "" + owner_aadhaarMobileNo.getText().toString().trim());
                    } else if (respondentOTPFLG) {
                        editor.putString("MobileNo", "" + et_mobileNo.getText().toString().trim());
                    }

                    editor.commit();

                    Intent verify = new Intent(GenerateCase.this,
                            OTP_input.class);
                    startActivity(verify);

                }
            } else {
                showToast("Please try Again !!!");
            }
        }
    }

    public class Async_getDatabase extends AsyncTask<Void, Void, String> {
        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            showDialog(PROGRESS_DIALOG);
        }

        @Override
        protected String doInBackground(Void... params) {
            // TODO Auto-generated method stub
            loc_name_arr.clear();
            try {
                db.open();

                String loc_query = "select LOCATION_NAME from " + DataBase.location_masters_table
                        + " where CIRCLE_NAME='" + select_circle_name.getText().toString().trim() + "'";
                Cursor loc_cursor = DataBase.db.rawQuery(loc_query, null);

                if (loc_cursor.moveToNext()) {
                    do {
                        String fieldToAdd = loc_cursor.getString(0);
                        Log.i("fieldToAdd ::", "" + fieldToAdd);
                        loc_name_arr.add(fieldToAdd);
                    } while (loc_cursor.moveToNext());
                }

                if (loc_cursor != null) {
                    loc_cursor.close();
                }

            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
                if (db != null) {
                    db.close();
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            removeDialog(PROGRESS_DIALOG);
            showDialog(LOCATION_NAME);
        }
    }

    public class Async_GetPS_DB extends AsyncTask<Void, Void, String> {
        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            showDialog(PROGRESS_DIALOG);
        }

        @Override
        protected String doInBackground(Void... params) {

            db = new DataBase(getApplicationContext());

            try {
                db.open();
                all_psNames_arr.clear();
                String db_query = "select PS_CODE, PS_NAME from " + DataBase.psName_table;
                Log.i("db_query :::", "" + db_query);

                Cursor ps_cursor = DataBase.db.rawQuery(db_query, null);

                if (ps_cursor.moveToFirst()) {
                    do {
                        // get the data into array, or class variable
                        Log.i("***PS Details****", "PS CODE :" + ps_cursor.getString(0) + " PS NAME :" + ps_cursor.getString(1));
                        all_psNames_arr.add(ps_cursor.getString(1));
                        psNameMap.put(ps_cursor.getString(1), ps_cursor.getString(0));
                    } while (ps_cursor.moveToNext());
                }
                ps_cursor.close();

            } catch (Exception e) {
                // TODO: handle exception
                db.close();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            removeDialog(PROGRESS_DIALOG);
            showDialog(PS_NAME_DIALOG);
        }
    }

    public static String getDate() {
        Time today = new Time(Time.getCurrentTimezone());
        today.setToNow();
        System.out.println(today.month);
        return today.monthDay + "-" + getMonthName(today.month) + "-" + today.year;
    }

    public static String getMonthName(int month) {
        switch (month + 1) {
            case 1:
                return "Jan";

            case 2:
                return "Feb";

            case 3:
                return "Mar";

            case 4:
                return "Apr";

            case 5:
                return "May";

            case 6:
                return "Jun";

            case 7:
                return "Jul";

            case 8:
                return "Aug";

            case 9:
                return "Sep";

            case 10:
                return "Oct";

            case 11:
                return "Nov";

            case 12:
                return "Dec";
        }

        return "";
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
                        m_locationlistner.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_BW_UPDATES,
                                MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
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

    public class Async_PreviewDetails extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            showDialog(PROGRESS_DIALOG);
        }

        @SuppressWarnings("unused")
        @Override
        protected String doInBackground(Void... params) {

            db = new DataBase(getApplicationContext());
            try {
                db.open();
                // select POINT_CODE from POINT_NAME_TABLE where
                // POINT_NAME='COMPOUNDING BOOTH'
                String poinName_query = "select POINT_CODE from " + DataBase.pointName_table + " where POINT_NAME='"
                        + select_point_name.getText().toString().trim() + "'";

                String psName_query = "select PS_CODE from " + DataBase.psName_table + " where POINT_NAME='"
                        + select_ps_name.getText().toString().trim() + "'";

                Cursor c = DataBase.db.rawQuery(poinName_query, null);
                Cursor point_cursor = DataBase.db.rawQuery(psName_query, null);

                if (c.moveToNext()) {
                    do {
                        pointCode_selcted = Integer.parseInt(c.getString(0));
                    } while (c.moveToNext());
                }

                if (point_cursor.moveToNext()) {
                    do {
                        booked_ps_code = Integer.parseInt(c.getString(0));
                    } while (point_cursor.moveToNext());
                }

                if (c != null) {
                    c.close();
                }

                if (point_cursor != null) {
                    point_cursor.close();
                }

            } catch (Exception e) {
                // TODO: handle exception
                if (db != null) {
                    db.close();
                }
            }

            Calendar c = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            String strDate = sdf.format(c.getTime());


            Calendar cal = Calendar.getInstance();
            int hourofday = cal.get(Calendar.HOUR_OF_DAY);
            int minute = cal.get(Calendar.MINUTE);
            int second = cal.get(Calendar.SECOND);

            StringBuilder time = new StringBuilder();
            time.append(hourofday).append(":").append(minute).append(":").append(second);


            SharedPreferences prefs = getSharedPreferences("loginValues", MODE_PRIVATE);

            String PidCode = prefs.getString("PID_CODE", "");
            String PidName = prefs.getString("PID_NAME", "");
            String login_ps_cd = prefs.getString("PS_CODE", "");
            String bookedPsCode = "" + login_ps_cd;
            String bookedPsName = "" + select_ps_name.getText().toString().trim();
            String pass_word = prefs.getString("SECURITY_CD", "");
            String cadre_cd = prefs.getString("CADRE_CODE", "");
            String cadre_name = prefs.getString("CADRE_NAME", "");

            for (String bookdpsName : GenerateCase.psNameMap.keySet()) {
                if (bookdpsName.trim().equals(bookedPsName)) {
                    bookedPsCode = GenerateCase.psNameMap.get(bookdpsName);
                }
            }

            String unitCode = "" + UNIT_CODE;
            String unitName = "" + UNIT_NAME;
            String pointCode = "" + pointCode_selcted;
            String pointName = "" + select_point_name.getText().toString().trim();
            String pidCd = "" + PidCode;

            for (String point_Name : GenerateCase.pointNameMap.keySet()) {
                if (point_Name.trim().equals(pointName)) {
                    pointCode = GenerateCase.pointNameMap.get(point_Name);
                }
            }

            String pidName = "" + PidName.trim();
            String password = "" + pass_word.trim();
            String cadreCD = cadre_cd.trim();
            String cadre = cadre_name.trim();
            String onlineMode = "1";
            String imageEvidence = "1";
            String offenceImg1 = imgString;
            String offenceDt = strDate.toString();
            String offenceTime = time.toString();
            String firmRegnNo = et_tin_no.getText().toString() != null ? et_tin_no.getText().toString() : "";
            String shopName = et_shop_name.getText().toString() != null ? et_shop_name.getText().toString() : "";
            String shopOwnerName = et_firm_owner_name.getText().toString() != null ? et_firm_owner_name.getText().toString() : "";
            String shopAddress = et_firm_address.getText().toString() != null ? et_firm_address.getText().toString() : "";
            String location = ""; // location by lat and long
            String psCode = bookedPsCode != null ? bookedPsCode.trim() : "";
            String psName = bookedPsName != null ? bookedPsName.trim() : "";
            String respondantName = et_name.getText().toString() != null ? et_name.getText().toString().trim() : "";
            String respondantFatherName = et_father_name.getText().toString() != null ? et_father_name.getText().toString().trim() : "";
            String respondantAddress = et_address.getText().toString() != null ? et_address.getText().toString().trim() : "";
            String respondantContactNo = et_mobileNo.getText().toString() != null ? et_mobileNo.getText().toString().trim() : "";
            String respondantAge = et_age.getText().toString() != null ? et_age.getText().toString().trim() : "";
            String IDCode = "1";
            String IDDetails = aadhaar_no.getText().toString() != null ? aadhaar_no.getText().toString().trim() : "";


            String detainedItems = "" + DetainedItems.detItems;
            String simId = "" + SimNo != null ? SimNo : "";
            String imeiNo = "" + IMEI != null ? IMEI : "";
            String macId = "" + macID != null ? macID : "";
            String gpsLatitude = "" + latitude;
            String gpsLongitude = "" + longitude;
            String totalFine = "0"; // by selected section amount calculations
            String encrHeight = et_height.getText().toString() != null ? et_height.getText().toString() : "";
            String encrWidth = et_width.getText().toString() != null ? et_width.getText().toString() : "";
            String encrLength = et_length.getText().toString() != null ? et_length.getText().toString() : "";
            String shopRunBy = "" + run_By != null ? run_By.trim() : "";
            String ghmcCirNo = "";
            String ghmcCirName = "";
            if (select_circle_name.getText().toString().equals("Select Location Name")) {
                ghmcCirName = "";
            } else {
                ghmcCirName = select_circle_name.getText().toString() != null ? select_circle_name.getText().toString() : "";
            }

            String basedOn = based_on != null ? based_on.trim() : "";
            String offenceImg2 = imgString2;
            String aadharImg = imgString3;
            String seizedImg = DetainedItems.Seize_image;
            String sections = "";
            for (String sec : Sections.checkedList) {
                for (String secViewData : DataBase.secFinalMap.keySet()) {
                    if (sec.equals(secViewData)) {
                        String[] petsec = DataBase.secFinalMap.get(secViewData).split("\\:");

                        if (petsec[1].equals("407(1)")) {
                            sections += petsec[0] + ":" + petsec[1] + ":" + (Integer.parseInt(petsec[2])
                                    * Integer.parseInt(Pet_count.num_of_pets) + "@");
                        } else {
                            sections += DataBase.secFinalMap.get(secViewData) + "@";
                        }
                        break;
                    }
                }
            }

            String owner_Name = owner_aadhaarName.getText().toString() != null ? owner_aadhaarName.getText().toString().trim() : "";
            String owner_Aadhar = owner_aadhaarNo.getText().toString() != null ? owner_aadhaarNo.getText().toString().trim() : "";
            String owner_FName = owner_aadhaarFatherName.getText().toString() != null ? owner_aadhaarFatherName.getText().toString().trim() : "";
            String owner_Age = owner_aadhaarAge.getText().toString() != null ? owner_aadhaarAge.getText().toString().trim() : "";
            String owner_Mobile = owner_aadhaarMobileNo.getText().toString() != null ? owner_aadhaarMobileNo.getText().toString().trim() : "";
            String owner_Address = owner_aadhaarAddress.getText().toString() != null ? owner_aadhaarAddress.getText().toString().trim() : "";

            for (String bookdpsName : GenerateCase.psNameMap.keySet()) {
                if (bookdpsName.trim().equals(bookedPsName)) {
                    bookedPsCode = GenerateCase.psNameMap.get(bookdpsName);
                }
            }

            /********** SHARED PREFERENCES **************/
            SharedPreferences case_Vals = getSharedPreferences("CaseValues", MODE_PRIVATE);
            SharedPreferences.Editor edit = case_Vals.edit();

            edit.putString("image1", "" + offenceImg1);
            edit.putString("image2", "" + offenceImg2);

            edit.putString("unitCode", "" + unitCode);
            edit.putString("unitName", "" + unitName);
            edit.putString("bookedPsCode", "" + bookedPsCode);
            edit.putString("bookedPsName", "" + bookedPsName);
            edit.putString("pointCode", "" + pointCode);
            edit.putString("pointName", "" + pointName);
            edit.putString("pidCd", "" + pidCd);
            edit.putString("pidName", "" + pidName);
            edit.putString("password", "" + pass_word);
            edit.putString("cadreCD", "" + cadre_cd);
            edit.putString("cadre", "" + cadre);
            edit.putString("onlineMode", "" + onlineMode);
            edit.putString("imageEvidence", "" + imageEvidence);
            edit.putString("offenceImg1", "" + offenceImg1);
            edit.putString("offenceDt", "" + offenceDt);
            edit.putString("offenceTime", "" + offenceTime);
            edit.putString("firmRegnNo", "" + firmRegnNo);
            edit.putString("shopName", "" + shopName);
            edit.putString("shopOwnerName", "" + shopOwnerName);
            edit.putString("shopAddress ", "" + shopAddress);
            edit.putString("location", "" + location);
            edit.putString("psCode", "" + psCode);
            edit.putString("psName", "" + psName);
            edit.putString("respondantName", "" + respondantName);
            edit.putString("respondantFatherName", "" + respondantFatherName);
            edit.putString("respondantAddress", "" + respondantAddress);
            edit.putString("respondantContactNo", "" + respondantContactNo);
            edit.putString("respondantAge", "" + respondantAge);
            edit.putString("IDCode", "" + IDCode);
            edit.putString("IDDetails", "" + IDDetails);
            edit.putString("simId", "" + simId);
            edit.putString("imeiNo", "" + imeiNo);
            edit.putString("macId", "" + macId);
            edit.putString("gpsLatitude", "" + gpsLatitude);
            edit.putString("gpsLongitude", "" + gpsLongitude);
            edit.putString("totalFine", "" + totalFine);
            edit.putString("encrHeight", "" + encrHeight);
            edit.putString("encrWidth", "" + encrWidth);
            edit.putString("encrLength", "" + encrLength);
            edit.putString("shopRunBy", "" + shopRunBy);
            edit.putString("ghmcCirNo", "" + ghmcCirNo);
            edit.putString("ghmcCirName", "" + ghmcCirName);
            edit.putString("basedOn", "" + basedOn);
            edit.putString("offenceImg2", "" + offenceImg2);
            edit.putString("aadharImg", "" + imgString3);
            edit.putString("detainedItems", "" + detainedItems);
            edit.putString("seizedImg", "" + seizedImg);
            edit.putString("sections", "" + sections);

            edit.putString("owner_image", "" + imgString4);// owner_imageByteArray
            edit.putString("owner_aadhaarNo", "" + owner_Aadhar);
            edit.putString("owner_name", "" + owner_Name);
            edit.putString("owner_fatherName", "" + owner_FName);
            edit.putString("owner_Age", "" + owner_Age);
            edit.putString("owner_MobileNo", "" + owner_Mobile);
            edit.putString("owner_address", "" + owner_Address);

            edit.commit();


            Intent preview = new Intent(GenerateCase.this, Preview.class);
            startActivity(preview);
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            removeDialog(PROGRESS_DIALOG);
            Intent preview = new Intent(GenerateCase.this, Preview.class);
            startActivity(preview);
        }
    }


    public class Async_getCircleName extends AsyncTask<Void, Void, String> {

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

            ServiceHelper.get_location_masters("" + selectedPs_code);

            return null;
        }

        @SuppressWarnings({"deprecation", "static-access"})
        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            // super.onPostExecute(result);

            loc_circleCode_arr.clear();
            loc_circleName_arr.clear();

            if (ServiceHelper.location_masters_resp != null) {

                String[] total_resp = ServiceHelper.location_masters_resp
                        .split("\\@");

                if (total_resp != null && total_resp.length > 0) {
                    circleFLG = true;
                    for (int i = 0; i < total_resp.length; i++) {
                        String[] del_split = total_resp[i].split("\\:");


                        loc_circleCode_arr.add("" + del_split[4]);
                        loc_circleName_arr.add("" + del_split[7]);


                    }
                }

                /************* POPUP ****************/
                removeDialog(PROGRESS_DIALOG);
                showDialog(CIRCLE_NAME);

            } else {
                circleFLG = false;
                removeDialog(PROGRESS_DIALOG);
                showToast("No Circle Data Found!!!!");

            }

        }
    }

    public class Async_getLocation extends AsyncTask<Void, Void, String> {

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
            // psCode_selcted = 2307 ;
            Log.i("psCode_selcted :::", "" + psCode_selcted);
            ServiceHelper.get_location_masters("" + psCode_selcted);

            return null;
        }

        @SuppressWarnings({"deprecation", "static-access"})
        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            // super.onPostExecute(result);
            if (ServiceHelper.location_masters_resp != null
                    && !ServiceHelper.location_masters_resp.equals("0")
                    && !ServiceHelper.location_masters_resp.equals("anyType{}")
                    && !ServiceHelper.location_masters_resp.equals("NA")) {

                String[] location_master = new String[0];
                location_master = ServiceHelper.location_masters_resp
                        .split("@");

                // SZ:SOUTH:W18:18-Moosarambagh(16):C4A:Circle
                // 4A(14):L18:MOOSARAMBAGH (MOOSARAMBAGH)
                // ZONE_CD: ZONE_NAME: WARD_CD: CIRCLE_NAME: CIRCLE_CD:
                // CIRCLE_NAME: LOCATION_CD: LOCATION_NAME@

                loc_zoneCode_arr.clear();
                loc_zoneName_arr.clear();

                loc_wardCode_arr.clear();
                loc_wardName_arr.clear();

                loc_circleCode_arr.clear();
                loc_circleName_arr.clear();

                loc_code_arr.clear();
                loc_name_arr.clear();

                // display only ward name and location name
                for (String locationDet : location_master) {

                    String[] cirleDet = locationDet.split("\\:");

                    loc_zoneCode_arr.add("" + cirleDet[0]);
                    loc_zoneName_arr.add("" + cirleDet[1]);

                    loc_wardCode_arr.add("" + cirleDet[2]);
                    loc_wardName_arr.add("" + cirleDet[3]);

                    loc_circleCode_arr.add("" + cirleDet[4]);
                    loc_circleName_arr.add("" + cirleDet[5]);

                    loc_code_arr.add("" + cirleDet[6]);
                    loc_name_arr.add("" + cirleDet[7]);

                }

            } else {
                showToast("Please Try Again!!");
            }

            Runnable progressRunnable = new Runnable() {

                @Override
                public void run() {
                    removeDialog(PROGRESS_DIALOG);

                }
            };

            Handler pdCanceller = new Handler();
            pdCanceller.postDelayed(progressRunnable, 5000);
            // removeDialog(PROGRESS_DIALOG);

            showDialog(CIRCLE_NAME);
        }
    }

    public class Async_getPointNameByPsName extends
            AsyncTask<Void, Void, String> {

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
            // psCode_selcted = 2307 ;
            Log.i("psCode_selcted :::", "" + psCode_selcted);
            ServiceHelper.getPointNameByPsNames("" + psCode_selcted);

            return null;
        }

        @SuppressWarnings({"deprecation", "static-access"})
        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            if (!ServiceHelper.PointNamesBypsNames_master.equals("anyType{}")) {

                if (ServiceHelper.PointNamesBypsNames_master.length > 0) {
                    pointNameBy_PsName_code_arr.clear();
                    pointNameBy_PsName_arr.clear();

                    select_point_name.setText("Select Point Name");

                    Log.i("eeByName", ""
                            + ServiceHelper.PointNamesBypsNames_master.length);
                    pointNameBYpsname_name_code_arr = new String[0][0];
                    pointNameBYpsname_name_code_arr = new String[ServiceHelper.PointNamesBypsNames_master.length][2];

                    for (int i = 0; i < ServiceHelper.PointNamesBypsNames_master.length; i++) {
                        pointNameBYpsname_name_code_arr[i] = ServiceHelper.PointNamesBypsNames_master[i]
                                .toString().trim().split("\\:");
                        Log.i("**POINT DETAILS**", ""
                                + pointNameBYpsname_name_code_arr[i][1]
                                .toString().trim());

                    }
                }

                try {
                    db.open();

                    for (int j = 0; j < pointNameBYpsname_name_code_arr.length; j++) {
                        pointNameBy_PsName_code_arr
                                .add(pointNameBYpsname_name_code_arr[j][0]);
                        pointNameBy_PsName_arr
                                .add(pointNameBYpsname_name_code_arr[j][1]);

                        db.insertPointNamesDetails(""
                                + pointNameBYpsname_name_code_arr[j][0], ""
                                + pointNameBYpsname_name_code_arr[j][1]);

                    }
                    Log.i("**PS NAMES**", "" + pointNameBy_PsName_arr.size());
                    select_point_name.setClickable(true);
                } catch (Exception e) {
                    e.printStackTrace();
                    if (db != null) {
                        db.close();
                    }
                }

            }
            removeDialog(PROGRESS_DIALOG);
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
        switch (id) {

            case PS_NAME_DIALOG:
                TextView title = new TextView(GenerateCase.this);
                title.setText("Select PS Name");
                title.setBackgroundColor(Color.parseColor("#007300"));
                title.setGravity(Gravity.CENTER);
                title.setTextColor(Color.WHITE);
                title.setTextSize(26);
                title.setTypeface(title.getTypeface(), Typeface.BOLD);
                title.setCompoundDrawablesWithIntrinsicBounds(
                        R.drawable.traffic_small, 0, R.drawable.ghmc_small, 0);
                title.setPadding(10, 0, 10, 0);
                title.setHeight(80);

                AlertDialog.Builder builderSingle = new AlertDialog.Builder(GenerateCase.this);
                builderSingle.setCustomTitle(title);
                builderSingle.setItems(all_psNames_arr.toArray(new CharSequence[all_psNames_arr.size()]),
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // TODO Auto-generated method stub
                                select_ps_name.setText(all_psNames_arr.get(which));
                                select_point_name.setText("Select Point Name");
                                select_circle_name.setText("Select Location Name");
                            }
                        });
                builderSingle.show().getWindow().setLayout(500, 800);
                return null;

            case PS_CODE_DIALOG:
                TextView title2 = new TextView(GenerateCase.this);
                title2.setText("Select Point Name");
                title2.setBackgroundColor(Color.parseColor("#007300"));
                title2.setGravity(Gravity.CENTER);
                title2.setTextColor(Color.WHITE);
                title2.setTextSize(26);
                title2.setTypeface(title2.getTypeface(), Typeface.BOLD);
                title2.setCompoundDrawablesWithIntrinsicBounds(
                        R.drawable.traffic_small, 0, R.drawable.ghmc_small, 0);
                title2.setPadding(10, 0, 10, 0);
                title2.setHeight(80);

                AlertDialog.Builder builderSingle2 = new AlertDialog.Builder(
                        GenerateCase.this);
                builderSingle2.setCustomTitle(title2);
                builderSingle2.setItems(all_pointNames_arr
                                .toArray(new CharSequence[all_pointNames_arr.size()]),
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // TODO Auto-generated method stub
                                select_ps_name.setText(all_pointNames_arr
                                        .get(which));
                                // select_point_name.setText("Select Point Name");

                            }

                        });

                builderSingle2.show().getWindow().setLayout(500, 800);
                return null;

            case CIRCLE_NAME:
                TextView ward_title = new TextView(this);
                ward_title.setText("Select Location Name");
                ward_title.setBackgroundColor(Color.parseColor("#007300"));
                ward_title.setGravity(Gravity.CENTER);
                ward_title.setTextColor(Color.WHITE);
                ward_title.setTextSize(26);
                ward_title.setTypeface(ward_title.getTypeface(), Typeface.BOLD);
                ward_title.setCompoundDrawablesWithIntrinsicBounds(
                        R.drawable.traffic_small, 0, R.drawable.ghmc_small, 0);
                ward_title.setPadding(10, 0, 10, 0);
                ward_title.setHeight(80);

                AlertDialog.Builder ward_code = new AlertDialog.Builder(this,
                        AlertDialog.THEME_HOLO_LIGHT);
                ward_code.setCustomTitle(ward_title);

                HashSet<String> hashSet = new HashSet<String>();
                hashSet.addAll(loc_circleName_arr);
                loc_circleName_arr.clear();
                loc_circleName_arr.addAll(hashSet);

                ward_code.setSingleChoiceItems((loc_circleName_arr
                                .toArray(new String[loc_circleName_arr.size()])),
                        selected_ward_name, new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // TODO Auto-generated method stub
                                selected_ward_name = which;
                                select_circle_name.setText(""
                                        + loc_circleName_arr.get(which).toString()
                                        .trim());
                                selectedCircleName = ""
                                        + loc_circleName_arr.get(which).toString()
                                        .trim();


                                // circleMap
                                for (String circleName : circleMap.keySet()) {
                                    if (circleName.trim().equals(circleName)) {
                                        circle_no = circleMap.get(circleName);

                                    }
                                }

                                removeDialog(CIRCLE_NAME);
                            }
                        });
                Dialog dg_ward_code = ward_code.create();
                return dg_ward_code;

            case LOCATION_NAME:
                TextView location_title = new TextView(this);
                location_title.setText("Select LOCATION Name");
                location_title.setBackgroundColor(Color.parseColor("#007300"));
                location_title.setGravity(Gravity.CENTER);
                location_title.setTextColor(Color.WHITE);
                location_title.setTextSize(26);
                location_title.setTypeface(location_title.getTypeface(),
                        Typeface.BOLD);
                location_title.setCompoundDrawablesWithIntrinsicBounds(
                        R.drawable.traffic_small, 0, R.drawable.ghmc_small, 0);
                location_title.setPadding(10, 0, 10, 0);
                location_title.setHeight(80);

                AlertDialog.Builder location_code = new AlertDialog.Builder(this,
                        AlertDialog.THEME_HOLO_LIGHT);
                location_code.setCustomTitle(location_title);
                location_code.setSingleChoiceItems(
                        (loc_name_arr.toArray(new String[loc_name_arr.size()])),
                        selected_loc_name, new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // TODO Auto-generated method stub
                                selected_loc_name = which;
                                select_location_name
                                        .setText(""
                                                + loc_name_arr.get(which)
                                                .toString().trim());


                                removeDialog(LOCATION_NAME);
                            }
                        });
                Dialog dg_location_code = location_code.create();
                return dg_location_code;
            case PROGRESS_DIALOG:
                ProgressDialog pd = ProgressDialog.show(this, "", "", true);
                pd.setContentView(R.layout.custom_progress_dialog);
                pd.setCancelable(false);

                return pd;

            case PREVIOUS_HOSTORY:
                String otp_message = null;
                if (thirty_nine_B_resp != null && ghmc_prevHstry == null) {
                    otp_message = "" + thirty_nine_B_resp + "\n";
                } else if (ghmc_prevHstry != null && thirty_nine_B_resp == null) {
                    // otp_message =
                    // "GHMC e-Enforcement Previous History"+"\n"+ghmc_prevHstry+"\n"
                    // ;
                    otp_message = ghmc_prevHstry + "\n";
                } else if (ghmc_prevHstry != null && thirty_nine_B_resp != null) {
                    otp_message = "" + thirty_nine_B_resp + "\n" + "" + "\n"
                            + ghmc_prevHstry + "\n";
                }

                TextView title4 = new TextView(this);
                title4.setText("PREVIOUS HISTORY");
                title4.setBackgroundColor(Color.parseColor("#007300"));
                title4.setGravity(Gravity.CENTER);
                title4.setTextColor(Color.WHITE);
                title4.setTextSize(25);
                title4.setTypeface(title4.getTypeface(), Typeface.BOLD);
                title4.setCompoundDrawablesWithIntrinsicBounds(
                        R.drawable.traffic_small, 0, R.drawable.ghmc_small, 0);
                title4.setPadding(10, 0, 10, 0);
                title4.setHeight(80);

                AlertDialog.Builder alertDialog_Builder = new AlertDialog.Builder(
                        this, AlertDialog.THEME_HOLO_LIGHT);
                alertDialog_Builder.setCustomTitle(title4);
                alertDialog_Builder.setIcon(R.drawable.traffic_small);
                alertDialog_Builder.setMessage(otp_message);
                alertDialog_Builder.setPositiveButton("Ok",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {

                                removeDialog(PREVIOUS_HOSTORY);

                            }
                        });
                alertDialog_Builder.setCancelable(false);
                AlertDialog alert_Dialog = alertDialog_Builder.create();
                alert_Dialog.show();

                alert_Dialog.getWindow().getAttributes();

                TextView textView2 = (TextView) alert_Dialog
                        .findViewById(android.R.id.message);
                textView2.setTextSize(18);
                // textView2.setGravity(Gravity.CENTER);
                textView2.setTypeface(textView2.getTypeface(), Typeface.BOLD);

                Button btn2 = alert_Dialog
                        .getButton(DialogInterface.BUTTON_POSITIVE);
                btn2.setTextSize(22);
                btn2.setTextColor(Color.WHITE);
                btn2.setTypeface(btn2.getTypeface(), Typeface.BOLD);
                btn2.setBackgroundColor(Color.parseColor("#007300"));

                return alert_Dialog;
            case WHEELER_CODE:
                TextView wlr_title = new TextView(this);
                wlr_title.setText("Select Section");
                wlr_title.setBackgroundColor(Color.parseColor("#007300"));
                wlr_title.setGravity(Gravity.CENTER);
                wlr_title.setTextColor(Color.WHITE);
                wlr_title.setTextSize(26);
                wlr_title.setTypeface(wlr_title.getTypeface(), Typeface.BOLD);
                wlr_title.setCompoundDrawablesWithIntrinsicBounds(
                        R.drawable.traffic_small, 0, R.drawable.ghmc_small, 0);
                wlr_title.setPadding(10, 0, 10, 0);
                wlr_title.setHeight(80);

                AlertDialog.Builder ad_whle_code_name = new AlertDialog.Builder(
                        this, AlertDialog.THEME_HOLO_LIGHT);
                ad_whle_code_name.setCustomTitle(wlr_title);//
                ad_whle_code_name.setSingleChoiceItems((section_name_arr_spot),
                        selected_section_code,
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // TODO Auto-generated method stub
                                selected_section_code = which;

                                add_sections.setText(""
                                        + section_name_arr_spot[which]);

                                section_code_send = section_code_arr_spot[which];

                                removeDialog(WHEELER_CODE);

                            }
                        });
                Dialog dg_whle_code_name = ad_whle_code_name.create();
                return dg_whle_code_name;

            case SUB_SECTION_CODE:
                TextView title3 = new TextView(this);
                title3.setText("Select Sub Section");
                title3.setBackgroundColor(Color.parseColor("#007300"));
                title3.setGravity(Gravity.CENTER);
                title3.setTextColor(Color.WHITE);
                title3.setTextSize(26);
                title3.setTypeface(title3.getTypeface(), Typeface.BOLD);
                title3.setCompoundDrawablesWithIntrinsicBounds(
                        R.drawable.traffic_small, 0, R.drawable.ghmc_small, 0);
                title3.setPadding(10, 0, 10, 0);
                title3.setHeight(80);

                AlertDialog.Builder ad_subsec_code = new AlertDialog.Builder(this,
                        AlertDialog.THEME_HOLO_LIGHT);
                ad_subsec_code.setCustomTitle(title3);
                ad_subsec_code.setSingleChoiceItems((subSecNameBy_secName_arr
                                .toArray(new String[subSecNameBy_secName_arr.size()])),
                        selected_sub_section_code,
                        new DialogInterface.OnClickListener() {
                            // connect device and run ok
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // TODO Auto-generated method stub
                                selected_sub_section_code = which;
                                add_sub_section.setText(""
                                        + subSecNameBy_secName_arr.get(which)
                                        .toString().trim());

                                removeDialog(SUB_SECTION_CODE);
                            }
                        });
                Dialog dg_subsec_code = ad_subsec_code.create();
                return dg_subsec_code;

            default:
                break;
        }
        return super.onCreateDialog(id);
    }

    public class Async_SubSections extends AsyncTask<Void, Void, String> {

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
            // psCode_selcted = 2307 ;
            section_code_send = "";
            ServiceHelper.getgetSubSectionsBy_Sectioncode("" + section_code_send, "" + Login.OFFICER_TYPE);
            return null;
        }

        @SuppressWarnings({"deprecation", "static-access"})
        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            removeDialog(PROGRESS_DIALOG);

            if (ServiceHelper.sub_sectionmasters_resp != null) {
                // 15:403:1000@17:406:550@18:407 (1):50@10:413 (1):10000
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

                        db.insertTempSectionDetails(""
                                + temp_sections_master2[j][0], ""
                                + temp_sections_master2[j][1], ""
                                + temp_sections_master2[j][2], ""
                                + temp_sections_master2[j][3]);
                        Log.i("insertTempSectionDetails[i] :::", "" + temp_sections_master2[j][0] + ":" + temp_sections_master2[j][1] + ":"
                                + temp_sections_master2[j][2]);
                        // 18:407(1):50, 20:413(1):5000, 34:402:3000
                    }
                    db2.close();
                } catch (Exception e) {
                    // TODO: handle exception
                    if (db != null) {
                        db.close();
                    }
                }
                Intent section = new Intent(GenerateCase.this, Sections.class);
                startActivity(section);
            }

            /*
             * if (ServiceHelper.sub_section_details_master!=null) {
             * showDialog(SUB_SECTION_CODE);
             *
             * Log.i("SUB SECTION 0 ::::",
             * ""+ServiceHelper.sub_section_details_master[0]);
             * Log.i("SUB SECTION 1 ::::",
             * ""+ServiceHelper.sub_section_details_master[1]);
             *
             * if(ServiceHelper.sub_section_details_master[1]!=null &&
             * !"NA".equals(ServiceHelper.sub_section_details_master[1])){
             *
             * add_sub_section.setText(""+ServiceHelper.sub_section_details_master
             * [1]); Log.i("SUB SECTION ::::",
             * ""+ServiceHelper.sub_section_details_master[1]);
             *
             * subSecNameBy_secName_arr.clear();// point name for second dialog
             * subSecNameBy_secName_code_arr.clear();;// point code for second
             *
             * add_sub_section.setText("Select Sub Section");
             *
             * //Log.i("Settings Async_getPointNameByPsName", "" +
             * ServiceHelper.PointNamesBypsNames_master.length);
             * subSecNameBysecName_code_arr = new String[0][0];
             * subSecNameBysecName_code_arr = new
             * String[ServiceHelper.sub_section_details_master.length][2];
             *
             * for (int i = 0; i <
             * ServiceHelper.sub_section_details_master.length; i++) {
             * subSecNameBysecName_code_arr[i] =
             * ServiceHelper.sub_section_details_master
             * [i].toString().trim().split("\\:");
             * Log.i("**SUB SECTION DETAILS**", ""+
             * subSecNameBysecName_code_arr[i][1].toString().trim());
             *
             * }
             *
             * Intent section = new Intent(GenerateCase.this, Sections.class);
             * startActivity(section);
             *
             * }else { showToast("No Section Avialable for Selected Category");
             * } }
             */
        }
    }

    public class Async_TinDetails extends AsyncTask<Void, Void, String> {

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
            // psCode_selcted = 2307 ;
            String firmRegn_No = "" + et_tin_no.getText().toString().trim();
            ServiceHelper.getTinDetails(""
                    + et_tin_no.getText().toString().trim());

            ServiceHelper.get39Bhistry_tin(firmRegn_No);
            ServiceHelper.get_ghmchistry_tin(firmRegn_No);

            return null;
        }

        @SuppressWarnings({"deprecation", "static-access"})
        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            removeDialog(PROGRESS_DIALOG);

            ServiceHelper.tinDetails_resp = ServiceHelper.tinDetails_resp != null ? ServiceHelper.tinDetails_resp
                    : "";

            if (ServiceHelper.tinDetails_resp != null && !"".equals(ServiceHelper.tinDetails_resp)) {

                if (ServiceHelper.tinDetails_resp.equals("0")) {
                    showToast("No TIN Data Found !!!");
                    ll_shop_details.setVisibility(View.VISIBLE);
                } else if (ServiceHelper.tinDetails_resp.equals("NA")) {
                    showToast("No TIN Data Found !!!");
                    ll_shop_details.setVisibility(View.VISIBLE);
                } else if (ServiceHelper.tinDetails_resp.equals("anyType{}")) {
                    showToast("No Live TIN Access !!!");
                    ll_shop_details.setVisibility(View.VISIBLE);
                } else {

                    String strJson = ServiceHelper.tinDetails_resp;
                    ll_shop_details.setVisibility(View.VISIBLE);
                    // shopName+":"+ownerName+":"+hno+":"+locality+":"+circleName+":"+mobileNo+":"+validity+":"+tenant;
                    // COMSMITH INFOSYS (CSI):K.HARI PRASAD:1-7-206/1,,SOUTH
                    // KAMALA NAGAR,ECIL, KAPRA,
                    // HYDERABAD.:271.0:1-Kapra:9246841682:2012-2013:T
                    String[] split_data = strJson.split("\\:");
                    shopName = "" + split_data[0];
                    shopOwner = "" + split_data[1];
                    shopAddress = "" + split_data[2] + ",\t" + split_data[3];

                    // ghmc_circleName.setText(""+split_data[4]);

                    et_shop_name.setText(shopName);
                    et_firm_owner_name.setText(shopOwner);
                    et_firm_address.setText(shopAddress);

                    // new Async_getPrevHistry().execute();

                    int myColor = getApplicationContext().getResources()
                            .getColor(R.color.text_color_all);
                    if (et_shop_name.getText().toString().trim().length() > 1) {
                        et_shop_name.setEnabled(false);
                        et_shop_name.setTextColor(myColor);
                    } else {
                        et_shop_name.setEnabled(true);
                    }

                    if (et_firm_owner_name.getText().toString().trim().length() > 1) {
                        et_firm_owner_name.setEnabled(false);
                        et_firm_owner_name.setTextColor(myColor);
                    } else {
                        et_firm_owner_name.setEnabled(true);
                    }

                    if (et_firm_address.getText().toString().trim().length() > 1) {
                        et_firm_address.setEnabled(false);
                        et_firm_address.setTextColor(myColor);
                    } else {
                        et_firm_address.setEnabled(true);
                    }

                }

            } else {
                showToast("No Details Found");
            }

            if (ServiceHelper.tab39b_resp != null) {

                thirty_nine_B_resp = "" + ServiceHelper.tab39b_resp;
                showDialog(PREVIOUS_HOSTORY);

            } else {
                thirty_nine_B_resp = null;
                showToast("No Tab39B Previous History Found!!!");
            }

            if (ServiceHelper.ghmc_hisrty_resp != null) {
                ghmc_prevHstry = "" + ServiceHelper.ghmc_hisrty_resp;
                showDialog(PREVIOUS_HOSTORY);

            } else {
                ghmc_prevHstry = null;
                showToast("No Previous GHMC History Found!!!");
            }

        }
    }

    public Boolean isOnline() {
        ConnectivityManager conManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo nwInfo = conManager.getActiveNetworkInfo();
        return nwInfo != null;
    }

    protected void selectImage() {
        // TODO Auto-generated method stub
        final CharSequence[] options = {
                "Open Camera",
                "Choose from Gallery",
                "Cancel"
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(GenerateCase.this);
        builder.setTitle("Add Photo!");
        builder.setItems(options, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Open Camera")) {

                    if (Build.VERSION.SDK_INT <= 23) {
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        File f = new File(android.os.Environment.getExternalStorageDirectory(), "temp.jpg");
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
                        startActivityForResult(intent, 1);
                    } else {
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        File f = new File(android.os.Environment.getExternalStorageDirectory(), "temp.jpg");
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(GenerateCase.this,
                                BuildConfig.APPLICATION_ID + ".provider", f));
                        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        startActivityForResult(intent, 1);
                    }

                } else if (options[item].equals("Choose from Gallery")) {
                    Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, 2);

                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

            String picturePath = "";

            if (requestCode == 1) {

                File f = new File(Environment.getExternalStorageDirectory().toString());

                for (File temp : f.listFiles()) {
                    if (temp.getName().equals("temp.jpg")) {
                        f = temp;
                        break;
                    }
                }
                try {
                    String current_date = GenerateCase.date;
                    Bitmap bitmap;
                    BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
                    bitmap = BitmapFactory.decodeFile(f.getAbsolutePath(), bitmapOptions);

                    String path = android.os.Environment.getExternalStorageDirectory()
                            + File.separator + "GHMC-enforcement" + File.separator + current_date;
                    File camerapath = new File(path);

                    if (!camerapath.exists()) {
                        camerapath.mkdirs();
                    }

                    f.delete();
                    OutputStream outFile = null;
                    File file = new File(path, String.valueOf(System.currentTimeMillis()) + ".jpg");

                    try {
                        outFile = new FileOutputStream(file);
                        Bitmap mutableBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
                        Canvas canvas = new Canvas(mutableBitmap); // bmp is the bitmap to dwaw into

                        Paint paint = new Paint();
                        paint.setColor(Color.RED);
                        paint.setTextSize(80);
                        paint.setTextAlign(Paint.Align.CENTER);

                        int xPos = (canvas.getWidth() / 2);
                        int yPos = (int) ((canvas.getHeight() / 2) - ((paint.descent() + paint.ascent()) / 2));

                        /*canvas.save();
                        canvas.rotate(270f, xPos, yPos);
                        canvas.drawText("Date & Time: " + Current_Date, xPos, yPos + 300, paint);
                        canvas.restore();

                        canvas.save();
                        canvas.rotate(270f, xPos, yPos);
                        canvas.drawText("Lat :" + latitude, xPos, yPos + 400, paint);
                        canvas.restore();

                        canvas.save();
                        canvas.rotate(270f, xPos, yPos);
                        canvas.drawText("Long :" + longitude, xPos, yPos + 500, paint);
                        canvas.rotate(90);
                        canvas.restore();

                        mutableBitmap.compress(Bitmap.CompressFormat.JPEG, 10, outFile);*/

                        canvas.save();
                        canvas.rotate(270f, xPos, yPos);
                        canvas.drawText("Date & Time: " + Current_Date, xPos, yPos + 300, paint);
                        canvas.drawText("Lat :" + latitude, xPos, yPos + 400, paint);
                        canvas.drawText("Long :" + longitude, xPos, yPos + 500, paint);
                        canvas.restore();

                        Display d = getWindowManager().getDefaultDisplay();
                        int x = d.getWidth();
                        int y = d.getHeight();
                        Bitmap scaledBitmap = Bitmap.createScaledBitmap(mutableBitmap, y, x, true);
                        Matrix matrix = new Matrix();
                        matrix.postRotate(90);
                        mutableBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0, scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix, true);
                        mutableBitmap.compress(Bitmap.CompressFormat.JPEG, 20, outFile);

                        outFile.flush();
                        outFile.close();
                        new SingleMediaScanner(this, file);

                    } catch (FileNotFoundException e) {
                        e.printStackTrace();

                    } catch (IOException e) {
                        e.printStackTrace();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    if ("1".equals(GenerateCase.SelPicId) && bitmap != null) {
                        Bitmap mutableBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
                        Canvas canvas = new Canvas(mutableBitmap); // bmp is the bitmap to dwaw into

                        Paint paint = new Paint();
                        paint.setColor(Color.RED);
                        paint.setTextSize(80);
                        paint.setTextAlign(Paint.Align.CENTER);

                        int xPos = (canvas.getWidth() / 2);
                        int yPos = (int) ((canvas.getHeight() / 2) - ((paint.descent() + paint.ascent()) / 2));

                        canvas.save();
                        canvas.rotate(270f, xPos, yPos);
                        canvas.drawText("Date & Time: " + Current_Date, xPos, yPos + 300, paint);
                        canvas.restore();

                        canvas.save();
                        canvas.rotate(270f, xPos, yPos);
                        canvas.drawText("Lat :" + latitude, xPos, yPos + 400, paint);
                        canvas.restore();

                        canvas.save();
                        canvas.rotate(270f, xPos, yPos);
                        canvas.drawText("Long :" + longitude, xPos, yPos + 500, paint);
                        canvas.restore();

                        canvas.rotate(90);

                        image1.setImageBitmap(mutableBitmap);
                        image1.setRotation(0);
                        image1.setRotation(90);


                        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                        mutableBitmap.compress(Bitmap.CompressFormat.JPEG, 10, bytes);

                        byteArray = bytes.toByteArray();
                        try {
                            if (byteArray != null) {
                                imgString = Base64.encodeToString(byteArray, Base64.NO_WRAP);
                            } else {
                                imgString = null;
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        new SingleMediaScanner(this, file);
                    } else if ("2".equals(GenerateCase.SelPicId) && bitmap != null) {
                        Bitmap mutableBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
                        Canvas canvas = new Canvas(mutableBitmap); // bmp is the bitmap to dwaw into

                        Paint paint = new Paint();
                        paint.setColor(Color.RED);
                        paint.setTextSize(80);
                        paint.setTextAlign(Paint.Align.CENTER);

                        int xPos = (canvas.getWidth() / 2);
                        int yPos = (int) ((canvas.getHeight() / 2) - ((paint.descent() + paint.ascent()) / 2));

                        canvas.save();
                        canvas.rotate(270f, xPos, yPos);
                        canvas.drawText("Date & Time: " + Current_Date, xPos, yPos + 300, paint);
                        canvas.restore();

                        canvas.save();
                        canvas.rotate(270f, xPos, yPos);
                        canvas.drawText("Lat :" + latitude, xPos, yPos + 400, paint);
                        canvas.restore();

                        canvas.save();
                        canvas.rotate(270f, xPos, yPos);
                        canvas.drawText("Long :" + longitude, xPos, yPos + 500, paint);
                        canvas.rotate(90);
                        canvas.restore();

                        image2.setImageBitmap(mutableBitmap);

                        image2.setRotation(0);
                        image2.setRotation(90);


                        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                        mutableBitmap.compress(Bitmap.CompressFormat.JPEG, 10, bytes);
                        byteArray = bytes.toByteArray();

                        try {
                            if (byteArray != null) {
                                imgString2 = Base64.encodeToString(byteArray, Base64.NO_WRAP);
                            } else {
                                imgString2 = null;
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        // imgString2 = Base64.encodeToString(byteArray,
                        // Base64.NO_WRAP);

                        new SingleMediaScanner(this, file);

                    } else if ("3".equals(GenerateCase.SelPicId) && bitmap != null) {
                        Bitmap mutableBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);

                        aadhaar_image.setImageBitmap(mutableBitmap);

                        aadhaar_image.setRotation(0.0f);
                        aadhaar_image.setRotation(aadhaar_image.getRotation() + 90);


                        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                        mutableBitmap.compress(Bitmap.CompressFormat.JPEG, 10, bytes);
                        byteArray = bytes.toByteArray();

                        try {
                            if (byteArray != null) {
                                imgString3 = Base64.encodeToString(byteArray, Base64.NO_WRAP);
                            } else {
                                imgString3 = null;
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    } else if ("4".equals(GenerateCase.SelPicId)
                            && bitmap != null) {
                        Bitmap mutableBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);

                        aadhaar_ownerImage.setImageBitmap(mutableBitmap);

                        aadhaar_ownerImage.setRotation(0.0f);
                        aadhaar_ownerImage.setRotation(aadhaar_ownerImage.getRotation() + 90);


                        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                        mutableBitmap.compress(Bitmap.CompressFormat.JPEG, 10, bytes);

                        byteArray = bytes.toByteArray();

                        try {
                            if (byteArray != null) {
                                imgString4 = Base64.encodeToString(byteArray, Base64.NO_WRAP);
                            } else {
                                imgString4 = null;
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    } else if (bitmap == null) {
                        showToast("Image Cannot be Loaded !");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (requestCode == 2) {
                Uri selectedImage = data.getData();
                String[] filePath = {MediaStore.Images.Media.DATA};
                Cursor c = getContentResolver().query(selectedImage, filePath, null, null, null);
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filePath[0]);
                picturePath = c.getString(columnIndex);
                c.close();

                Bitmap thumbnail = (BitmapFactory.decodeFile(picturePath));

                if ("1".equals(GenerateCase.SelPicId) && thumbnail != null) {
                    Bitmap mutableBitmap = thumbnail.copy(Bitmap.Config.ARGB_8888, true);
                    Canvas canvas = new Canvas(mutableBitmap); // bmp is the bitmap to dwaw into

                    Paint paint = new Paint();
                    paint.setColor(Color.RED);
                    paint.setTextSize(100);
                    paint.setTextAlign(Paint.Align.CENTER);

                    int xPos = (canvas.getWidth() / 2);
                    int yPos = (int) ((canvas.getHeight() / 2) - ((paint.descent() + paint.ascent()) / 2));

                    image1.setImageBitmap(mutableBitmap);
                    //image1.setRotation(0.0f);
                    //image1.setRotation(image1.getRotation() + 90);


                    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                    mutableBitmap.compress(Bitmap.CompressFormat.JPEG, 10, bytes);

                    byteArray = bytes.toByteArray();

                    try {
                        if (byteArray != null) {
                            imgString = Base64.encodeToString(byteArray, Base64.NO_WRAP);
                        } else {
                            imgString = null;
                        }
                        Log.i("imgString ::", "" + imgString);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else if ("2".equals(GenerateCase.SelPicId)
                        && thumbnail != null) {
                    Bitmap mutableBitmap = thumbnail.copy(Bitmap.Config.ARGB_8888, true);
                    Canvas canvas = new Canvas(mutableBitmap); // bmp is the bitmap to dwaw into

                    Paint paint = new Paint();
                    paint.setColor(Color.RED);
                    paint.setTextSize(100);
                    paint.setTextAlign(Paint.Align.CENTER);
                    // canvas.drawText("Date & Time: "+Current_Date+"\n"+" Lat :"+latitude+
                    // " Long :"+longitude,1250, 1500, paint);
                    int xPos = (canvas.getWidth() / 2);
                    int yPos = (int) ((canvas.getHeight() / 2) - ((paint.descent() + paint.ascent()) / 2));


                    image2.setImageBitmap(mutableBitmap);
                    //image2.setRotation(0.0f);
                    //image2.setRotation(image2.getRotation() + 90);

                    //image2.setRotation(0f);
                    //image2.setRotation(image2.getRotation() + 90);

                    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                    mutableBitmap.compress(Bitmap.CompressFormat.JPEG, 10, bytes);

                    byteArray = bytes.toByteArray();
                    try {
                        if (byteArray != null) {
                            imgString2 = Base64.encodeToString(byteArray, Base64.NO_WRAP);
                        } else {
                            imgString2 = null;
                        }
                        Log.i("imgString2 ::", "" + imgString2);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else if ("3".equals(GenerateCase.SelPicId) && thumbnail != null) {
                    Bitmap mutableBitmap = thumbnail.copy(Bitmap.Config.ARGB_8888, true);

                    aadhaar_image.setImageBitmap(mutableBitmap);
                    //aadhaar_image.setRotation(0.0f);
                    //aadhaar_image.setRotation(aadhaar_image.getRotation() + 90);


                    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                    mutableBitmap.compress(Bitmap.CompressFormat.JPEG, 10, bytes);

                    byteArray = bytes.toByteArray();

                    imgString3 = Base64.encodeToString(byteArray, Base64.NO_WRAP);
                    try {
                        if (byteArray != null) {
                            imgString3 = Base64.encodeToString(byteArray, Base64.NO_WRAP);
                        } else {
                            imgString3 = null;
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else if ("4".equals(GenerateCase.SelPicId) && thumbnail != null) {
                    Bitmap mutableBitmap = thumbnail.copy(Bitmap.Config.ARGB_8888, true);

                    aadhaar_ownerImage.setImageBitmap(mutableBitmap);
                    //aadhaar_ownerImage.setRotation(0.0f);
                    //aadhaar_ownerImage.setRotation(aadhaar_ownerImage.getRotation() + 90);


                    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                    mutableBitmap.compress(Bitmap.CompressFormat.JPEG, 10, bytes);

                    byteArray = bytes.toByteArray();

                    try {
                        if (byteArray != null) {
                            imgString4 = Base64.encodeToString(byteArray, Base64.NO_WRAP);
                        } else {
                            imgString4 = null;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (thumbnail == null) {
                    showToast("Image Cannot be Loaded !");
                }
            }
        }

        if (requestCode == CAMERA_CAPTURE_VIDEO_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // video successfully recorded
                // preview the recorded video
                if (videoPrevwFLG) {
                    previewVideo();
                    if (null != fileUri) {
                        video_file = getRealVideoPathFromURI(fileUri);
                    } else {
                        video_file = "";
                    }
                } else {
                    videoPreview.stopPlayback();
                }
            } else if (resultCode == RESULT_CANCELED) {
                // user cancelled recording
                Toast.makeText(getApplicationContext(), "User cancelled video recording", Toast.LENGTH_SHORT).show();
            } else {
                // failed to record video
                Toast.makeText(getApplicationContext(), "Sorry! Failed to record video", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void previewVideo() {

        try {
            Log.i("method", "previewVideo called ");
            // hide image preview
            videoPreview.setVisibility(View.VISIBLE);
            videoPreview.setVideoPath(fileUri.getPath());
            // start playing
            videoPreview.start();

            // When the video file ready for playback.
            videoPreview.setOnPreparedListener(new OnPreparedListener() {

                public void onPrepared(MediaPlayer mediaPlayer) {

                    videoPreview.seekTo(position);
                    if (position == 0) {
                        videoPreview.start();
                    }

                    // When video Screen change size.
                    mediaPlayer.setOnVideoSizeChangedListener(new MediaPlayer.OnVideoSizeChangedListener() {
                        @Override
                        public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {

                            // Re-Set the videoView that acts as the
                            // anchor for the MediaController
                            mediaController.setAnchorView(videoPreview);
                        }
                    });
                }
            });


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        fileUri = savedInstanceState.getParcelable("file_uri");
        // Get saved position.
        position = savedInstanceState.getInt("CurrentPosition");
        videoPreview.seekTo(position);
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
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // TODO Auto-generated method stub
        String item = parent.getItemAtPosition(position).toString();

        if (!item.equals("Select Section")) {
            // Showing selected spinner item
            showToast("Selected Section Type  : " + item);
            ((TextView) parent.getChildAt(0)).setTextColor(Color.WHITE);
            ((TextView) parent.getChildAt(0)).setGravity(Gravity.CENTER);
            ((TextView) parent.getChildAt(0)).setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.text_size));

        } else {
            ((TextView) parent.getChildAt(0)).setTextColor(Color.WHITE);
            ((TextView) parent.getChildAt(0)).setGravity(Gravity.CENTER);
            ((TextView) parent.getChildAt(0)).setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.text_size));
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onLocationChanged(Location location) {
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

    public Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(getOutputMediaFile(type));
    }

    private static File getOutputMediaFile(int type) {

        // External sdcard location
        String path = android.os.Environment.getExternalStorageDirectory()
                + File.separator + "GHMC-Videos" + File.separator + GenerateCase.date;
        File mediaStorageDir = new File(path);
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d(IMAGE_DIRECTORY_NAME, "Oops! Failed create " + IMAGE_DIRECTORY_NAME + " directory");
                return null;
            }
        }

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_VIDEO) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator + "temp.mp4");
        } else {
            return null;
        }

        return mediaFile;
    }


    private String getRealVideoPathFromURI(Uri contentURI) {
        String result;
        Cursor cursor = getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) {
            result = contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Video.VideoColumns.DATA);
            if (idx >= 0) {
                result = cursor.getString(idx);
            } else {
                result = null;
            }
            cursor.close();
        }
        return result;

    }


    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        // super.onBackPressed();
        newtimer.cancel();
        showToast("Please Click on Back Button");
    }
}
