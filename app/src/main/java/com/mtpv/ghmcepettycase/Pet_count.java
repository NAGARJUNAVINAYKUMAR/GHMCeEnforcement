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
import android.text.Html;
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
public class Pet_count extends Activity {

	final int PROGRESS_DIALOG = 0;
	final int OTP_CNFRMTN_DIALOG = 7;

	public static EditText pet_count_input;
	public static String num_of_pets = null;
	Button otp_cancel, ok_dialog;

	public static int petCountAmount = 0;

	String fine_amount = "0";

	public static String otp_number = "", reg_No, Mobile_No, OTP_date, OTP_No,
			Verify_status = "N", close_Decision = "", OTP_status = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_pet_count);

		this.setFinishOnTouchOutside(false);

		pet_count_input = (EditText) findViewById(R.id.pet_count_input);
		ok_dialog = (Button) findViewById(R.id.ok_dialog);
		otp_cancel = (Button) findViewById(R.id.cancel_dialog);

		SharedPreferences pet = getSharedPreferences("petCountAmount",
				MODE_PRIVATE);

		fine_amount = pet.getString("actual_amnt", fine_amount);
		Log.i("sec_amnt:::", "" + fine_amount);

		otp_cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {/*
										 * // TODO Auto-generated method stub
										 * TextView title = new
										 * TextView(Pet_count.this);
										 * title.setText
										 * ("PET's Count Cancel Status");
										 * title.setBackgroundColor
										 * (Color.GREEN);
										 * title.setGravity(Gravity.CENTER);
										 * title.setTextColor(Color.WHITE);
										 * title.setTextSize(26);
										 * title.setTypeface
										 * (title.getTypeface(), Typeface.BOLD);
										 * title
										 * .setCompoundDrawablesWithIntrinsicBounds
										 * (R.drawable.dialog_logo, 0,
										 * R.drawable.dialog_logo, 0);
										 * title.setPadding(20, 0, 20, 0);
										 * title.setHeight(70);
										 * 
										 * String otp_message =
										 * "\n Are you sure, You don't Want add Pet's Count ???\n"
										 * ;
										 * 
										 * AlertDialog.Builder
										 * alertDialogBuilder = new
										 * AlertDialog.Builder(Pet_count.this,
										 * AlertDialog.THEME_HOLO_LIGHT);
										 * alertDialogBuilder
										 * .setCustomTitle(title);
										 * alertDialogBuilder
										 * .setIcon(R.drawable.dialog_logo);
										 * alertDialogBuilder
										 * .setMessage(otp_message);
										 * alertDialogBuilder
										 * .setCancelable(false);
										 * alertDialogBuilder
										 * .setPositiveButton("Yes", new
										 * DialogInterface.OnClickListener() {
										 * 
										 * @Override public void
										 * onClick(DialogInterface dialog, int
										 * which) { // TODO Auto-generated
										 * method stub finish(); } });
										 * 
										 * alertDialogBuilder.setNegativeButton("No"
										 * , new
										 * DialogInterface.OnClickListener() {
										 * 
										 * @Override public void
										 * onClick(DialogInterface dialog, int
										 * which) { // TODO Auto-generated
										 * method stub } });
										 * 
										 * AlertDialog alertDialog =
										 * alertDialogBuilder.create();
										 * alertDialog.show();
										 * 
										 * alertDialog.getWindow().getAttributes(
										 * );
										 * 
										 * TextView textView = (TextView)
										 * alertDialog
										 * .findViewById(android.R.id.message);
										 * textView.setTextSize(28);
										 * textView.setTypeface
										 * (textView.getTypeface(),
										 * Typeface.BOLD);
										 * textView.setGravity(Gravity.CENTER);
										 * textView.setTextColor(Color.WHITE);
										 * 
										 * Button btn1 =
										 * alertDialog.getButton(DialogInterface
										 * .BUTTON_POSITIVE);
										 * btn1.setTextSize(22);
										 * btn1.setTextColor(Color.WHITE);
										 * btn1.setTypeface(btn1.getTypeface(),
										 * Typeface.BOLD);
										 * btn1.setBackgroundColor(Color.RED);
										 * 
										 * 
										 * Button btn2 =
										 * alertDialog.getButton(DialogInterface
										 * .BUTTON_NEGATIVE);
										 * btn2.setTextSize(22);
										 * btn2.setTextColor(Color.WHITE);
										 * btn2.setTypeface(btn2.getTypeface(),
										 * Typeface.BOLD);
										 * btn2.setBackgroundColor(Color.RED);
										 * 
										 * 
										 * if (close_Decision.equals("N")) {
										 * 
										 * }else if(close_Decision.equals("Y")){
										 * finish(); }
										 */

				finish();

			}
		});

		ok_dialog.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (pet_count_input.getText().toString().trim().equals("")) {
					pet_count_input.setError(Html.fromHtml("<font color='black'>Please Enter Pet's Count</font>"));
					pet_count_input.requestFocus();
				} else {
					SharedPreferences pet = getSharedPreferences("petCountAmount", MODE_PRIVATE);
					SharedPreferences.Editor edit = pet.edit();
					int counting = Integer.parseInt("" + pet_count_input.getText().toString().trim());
					petCountAmount = counting * Integer.parseInt(fine_amount);

					edit.putInt("total_Section_amnt", petCountAmount);
					Log.i("no of pets  :::", "" + counting);
					Log.i("pet_amount :::", "" + petCountAmount);
					edit.commit();
					num_of_pets = pet_count_input.getText().toString().trim();
					finish();
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

	class Async_otpverify extends AsyncTask<Void, Void, String> {

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

			ServiceHelper.verifyOTP(Mobile_No, OTP_date, ""
					+ pet_count_input.getText().toString().trim(),
					Verify_status);

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
				GenerateCase.otp_verify_status = "Y";
				showToast("OTP Verification Successfull");
			} else if (ServiceHelper.otp_verify_resp.equals("0")) {
				showToast("OTP Verification Failed");
				GenerateCase.otp_verify_status = "N";
			} else {
				showToast("OTP Verification Failed");
			}

		}
	}

	private void showToast(String msg) {
		// TODO Auto-generated method stub
		// Toast.makeText(getApplicationContext(), "" + msg,
		// Toast.LENGTH_SHORT).show();
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

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		// super.onBackPressed();
		showToast("Please Click on Cancel Button to go Back ..!");
	}
}
