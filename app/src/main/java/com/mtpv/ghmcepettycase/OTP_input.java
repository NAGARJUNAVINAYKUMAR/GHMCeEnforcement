package com.mtpv.ghmcepettycase;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.mtpv.ghmcenforcement.R;
import com.mtpv.services.ServiceHelper;

@SuppressLint("DefaultLocale")
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class OTP_input extends Activity {
	
	final int PROGRESS_DIALOG = 0;
	final int OTP_CNFRMTN_DIALOG = 7;
	
	EditText otp_input ;
	Button otp_cancel, ok_dialog ;
	
	public static String otp_number ="", reg_No, Mobile_No, OTP_date, OTP_No, Verify_status = "N" , close_Decision = "", OTP_status = null ;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_otp_input);
		
		this.setFinishOnTouchOutside(false);
		
		otp_input = (EditText)findViewById(R.id.otp_input);
		ok_dialog = (Button)findViewById(R.id.ok_dialog);
		otp_cancel = (Button)findViewById(R.id.cancel_dialog);
		
		SharedPreferences sharedPreference = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		
		OTP_No = sharedPreference.getString("OTP_Num", "");
		OTP_date  = sharedPreference.getString("OTP_DATE", "");
		Mobile_No  = sharedPreference.getString("MobileNo", "");
		
		Log.i("MobileNo  ::", ""+Mobile_No);
		
		 otp_cancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				TextView title = new TextView(OTP_input.this);
				title.setText("Hyderabad E-Ticket");
				title.setBackgroundColor(Color.RED);
				title.setGravity(Gravity.CENTER);
				title.setTextColor(Color.WHITE);
				title.setTextSize(26);
				title.setTypeface(title.getTypeface(), Typeface.BOLD);
				title.setCompoundDrawablesWithIntrinsicBounds(R.drawable.dialog_logo, 0, R.drawable.dialog_logo, 0);
				title.setPadding(20, 0, 20, 0);
				title.setHeight(70);
				
				String otp_message = "\n Are you sure, You don't Want to Verify OTP ???\n" ;
				
				AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(OTP_input.this, AlertDialog.THEME_HOLO_LIGHT);
				alertDialogBuilder.setCustomTitle(title);
				alertDialogBuilder.setIcon(R.drawable.dialog_logo);
				alertDialogBuilder.setMessage(otp_message);
				alertDialogBuilder.setCancelable(false);
				alertDialogBuilder.setPositiveButton("Yes",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog, int which) {
								// TODO Auto-generated method stub
								 close_Decision = "Y" ;
								 finish();
								 GenerateCase.otp_verify_status = "N";
							}
						});

				alertDialogBuilder.setNegativeButton("No",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog, int which) {
								// TODO Auto-generated method stub
								close_Decision = "N" ;
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
			
			        
			        if (close_Decision.equals("N")) {
						
					}else if(close_Decision.equals("Y")){
						finish();
					}
			}
		});
		 
		ok_dialog.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (otp_input.getText().toString().trim().equals(OTP_No)) {
					Verify_status = "Y";
					
					if (isOnline()) {
						Log.i("***OTP CONFIRMATION STATUS", ""	+ Verify_status);
						GenerateCase.otp_verify_status = "Y";
						
						new Async_otpverify().execute();
					} else {
						showToast("Please check your network connection!");
					}
				}else{
					Verify_status = "N";
					GenerateCase.otp_verify_status = "N";
					showToast("Entered Wrong OTP");
					otp_input.setText("");
				}
			}
		});
		
	}
	
	public Boolean isOnline() {
		ConnectivityManager conManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo nwInfo = conManager.getActiveNetworkInfo();
		return nwInfo != null;
	}
	
	class Async_otpverify extends AsyncTask<Void, Void, String>{

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
			
			ServiceHelper.verifyOTP(Mobile_No, OTP_date, ""+otp_input.getText().toString().trim(), Verify_status);
			
			return null;
		}
		
		@SuppressWarnings("deprecation")
		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			removeDialog(PROGRESS_DIALOG);
			
			if (ServiceHelper.otp_verify_resp.equals("1")) {
				finish();	
				GenerateCase.otp_verify_status = "Y" ;
				showToast("OTP Verification Successfull");
			}else if (ServiceHelper.otp_verify_resp.equals("0")) {
				showToast("OTP Verification Failed");
				GenerateCase.otp_verify_status = "N" ;
			}else {
				showToast("OTP Verification Failed");
			}
			
		}
	}
	
	private void showToast(String msg) {
		// TODO Auto-generated method stub
	//	Toast.makeText(getApplicationContext(), "" + msg, Toast.LENGTH_SHORT).show();
		Toast toast = Toast.makeText(getApplicationContext(), "" + msg, Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.CENTER, 0, 0);
		View toastView = toast.getView();
		
		ViewGroup group = (ViewGroup) toast.getView();
	    TextView messageTextView = (TextView) group.getChildAt(0);
	    messageTextView.setTextSize(24);
		
    	toastView.setBackgroundResource(R.drawable.toast_background);
	    toast.show();
	}
	
	@SuppressWarnings("deprecation")
	@Override
	protected Dialog onCreateDialog(int id) {
		// TODO Auto-generated method stub
		switch (id) {
		case PROGRESS_DIALOG:
			ProgressDialog pd = ProgressDialog.show(this, "", "",	true);
			pd.setContentView(R.layout.custom_progress_dialog);
			pd.setCancelable(false); 
			
			return pd;

		default:
			break;
		}
		return super.onCreateDialog(id);
	}
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
	//	super.onBackPressed();
		showToast("Please Click on Cancel Button to go Back ..!");
	}
}
