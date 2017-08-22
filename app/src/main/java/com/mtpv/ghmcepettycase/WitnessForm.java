package com.mtpv.ghmcepettycase;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Html;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.mtpv.ghmcenforcement.R;

public class WitnessForm extends Activity {
	
	Spinner witness_id_options ;
	public static EditText panch_name1, panch_name2, panch_fatherName1, panch_fatherName2, panch_mobileNo1, panch_mobileNo2, panch_address1, panch_address2  ;
	RadioButton wt_male, wt_female,wt_male2, wt_female2, wt_other, wt_id_proof_yes, wt_id_proof_no ;
	ImageView wt_get_details, back_btn, submit_btn ;
	LinearLayout wt_get_the_ID_details, panchaythdhar_layout2 ;
	Button panchayatdhar_btn, add_panchayathdhaar;
	
	public static String wt_gender_txt1="", wt_gender_txt2="" ;
	public static boolean addingFLG = false ;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_witness_form);
		//Thread.setDefaultUncaughtExceptionHandler(new UnCaughtException(WitnessForm.this));
		
		SharedPreferences witness_pref = getSharedPreferences("panchayathDhars", MODE_PRIVATE);
		SharedPreferences.Editor editor_pref = witness_pref.edit();
		editor_pref.clear(); 
		editor_pref.commit();
		
		panchaythdhar_layout2 = (LinearLayout)findViewById(R.id.panchaythdhar_layout2);
		panchaythdhar_layout2.setVisibility(View.GONE);
		
		panch_name1 = (EditText)findViewById(R.id.panch_name1);
		panch_fatherName1 = (EditText)findViewById(R.id.panch_father1); 
		panch_mobileNo1 = (EditText)findViewById(R.id.panchth_phone1);
		panch_address1 = (EditText)findViewById(R.id.panchth_address1);
		
		wt_male = (RadioButton)findViewById(R.id.panchth_male1);
		wt_female = (RadioButton)findViewById(R.id.panchth_female1);

		wt_male2 = (RadioButton)findViewById(R.id.panchth_male2);
		wt_female2 = (RadioButton)findViewById(R.id.panchth_female2);

		panch_name2 = (EditText)findViewById(R.id.panch_name2);
		panch_fatherName2 = (EditText)findViewById(R.id.panch_father2); 
		panch_mobileNo2 = (EditText)findViewById(R.id.panchth_phone2);
		panch_address2 = (EditText)findViewById(R.id.panchth_address2);

		
		panch_mobileNo1.setText("");
		panch_mobileNo2.setText("");
		
		back_btn = (ImageView)findViewById(R.id.back_btn);
		panchayatdhar_btn = (Button)findViewById(R.id.panchayatdhar_btn);
		
		add_panchayathdhaar = (Button)findViewById(R.id.add_panchayath2);
		
		back_btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				SharedPreferences witness_pref = getSharedPreferences("panchayathDhars", MODE_PRIVATE);
				SharedPreferences.Editor editor_pref = witness_pref.edit();
				editor_pref.clear(); 
				editor_pref.commit();
				finish();
			}
		});
		
		wt_male.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				wt_gender_txt1="M" ; 
			}
		});
		wt_female.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				wt_gender_txt1="F" ;
			}
		});
		
		wt_male2.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				wt_gender_txt2="M" ;
			}
		});
		wt_female2.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				wt_gender_txt2="F" ;
			}
		});
		
		
		add_panchayathdhaar.setVisibility(View.VISIBLE);
		
		add_panchayathdhaar.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (!addingFLG) {
					panchaythdhar_layout2.setVisibility(View.VISIBLE);
					add_panchayathdhaar.setVisibility(View.VISIBLE);
					add_panchayathdhaar.setText("Remove Panchayathdhar");	
					addingFLG = true ;
				}else if (addingFLG) {
					panch_name2.setText("");
					panch_fatherName2.setText(""); 
					panch_mobileNo2.setText("");
					panch_address2.setText("");
					
					panchaythdhar_layout2.setVisibility(View.GONE);
					add_panchayathdhaar.setVisibility(View.VISIBLE);
					add_panchayathdhaar.setText("Add Panchayathdhar");	
					addingFLG = false ;
				}
			}
		});
		
		panchayatdhar_btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (panch_name1.getText().toString().trim().equals("")) {
					panch_name1.setError(Html.fromHtml("<font color='black'>Please Enter Name</font>"));
					panch_name1.requestFocus();
				}else if (panch_fatherName1.getText().toString().trim().equals("")) {
					panch_fatherName1.setError(Html.fromHtml("<font color='black'>Please Enter Name</font>"));
					panch_fatherName1.requestFocus();
				}else if (panch_address1.getText().toString().trim().equals("")) {
					panch_address1.setError(Html.fromHtml("<font color='black'>Please Enter Address</font>"));
					panch_address1.requestFocus();
				}else if (panchaythdhar_layout2.getVisibility()==View.VISIBLE && panch_name2.getText().toString().trim().equals("")) {
					panch_name2.setError(Html.fromHtml("<font color='black'>Please Enter Name</font>"));
					panch_name2.requestFocus();
				}else if (panchaythdhar_layout2.getVisibility()==View.VISIBLE && panch_fatherName2.getText().toString().trim().equals("")) {
					panch_fatherName2.setError(Html.fromHtml("<font color='black'>Please Enter Father Name</font>"));
					panch_fatherName2.requestFocus();
				}else if (panchaythdhar_layout2.getVisibility()==View.VISIBLE && panch_address2.getText().toString().trim().equals("")) {
					panch_address2.setError(Html.fromHtml("<font color='black'>Please Enter Name</font>"));
					panch_address2.requestFocus();
				}else{
					if (!panch_name1.getText().toString().trim().equals("")) {
							SharedPreferences myprefs = getSharedPreferences("panchayathDhars", MODE_PRIVATE);
							SharedPreferences.Editor edit = myprefs.edit() ;
						
							edit.putString("Pancha_Name1", ""+panch_name1.getText().toString().trim()!=null?panch_name1.getText().toString().trim():"");
							edit.putString("Pancha_Fname1", ""+panch_fatherName1.getText().toString().trim()!=null?panch_fatherName1.getText().toString().trim():"");
							edit.putString("Pancha_MobileNo1", ""+panch_mobileNo1.getText().toString().trim()!=null?panch_mobileNo1.getText().toString().trim():"");
							edit.putString("Pancha_address1", ""+panch_address1.getText().toString().trim()!=null?panch_address1.getText().toString().trim():"");
							edit.putString("Pancha_gender1", ""+wt_gender_txt1.trim());
						
							edit.commit();
							if (!panch_name2.getText().toString().trim().equals("")) {
								myprefs = getSharedPreferences("panchayathDhars", MODE_PRIVATE);
								edit = myprefs.edit() ;
							
								edit.putString("Pancha_Name1", ""+panch_name1.getText().toString().trim()!=null?panch_name1.getText().toString().trim():"");
								edit.putString("Pancha_Fname1", ""+panch_fatherName1.getText().toString().trim()!=null?panch_fatherName1.getText().toString().trim():"");
								edit.putString("Pancha_MobileNo1", ""+panch_mobileNo1.getText().toString().trim()!=null?panch_mobileNo1.getText().toString().trim():"");
								edit.putString("Pancha_address1", ""+panch_address1.getText().toString().trim()!=null?panch_address1.getText().toString().trim():"");
								edit.putString("Pancha_gender1", ""+wt_gender_txt1.trim());
								
								edit.putString("Pancha_Name2", ""+panch_name2.getText().toString().trim()!=null?panch_name2.getText().toString().trim():"");
								edit.putString("Pancha_Fname2", ""+panch_fatherName2.getText().toString().trim()!=null?panch_fatherName2.getText().toString().trim():"");
								edit.putString("Pancha_MobileNo2", ""+panch_mobileNo2.getText().toString().trim()!=null?panch_mobileNo2.getText().toString().trim():"");
								edit.putString("Pancha_address2", ""+panch_address2.getText().toString().trim()!=null?panch_address2.getText().toString().trim():"");
								edit.putString("Pancha_gender2", ""+wt_gender_txt2.trim());
							
								edit.commit();
							}
							
					}
					
					GenerateCase.panchayathdhar_tick.setVisibility(View.VISIBLE);	
					
					finish();
				}
			}
		});
		
	}
	
	@SuppressWarnings("unused")
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
	public void onBackPressed() {
		// TODO Auto-generated method stub
		//super.onBackPressed();
		
		SharedPreferences witness_pref = getSharedPreferences("panchayathDhars", MODE_PRIVATE);
		SharedPreferences.Editor editor_pref = witness_pref.edit();
		editor_pref.clear(); 
		editor_pref.commit();
		
		showToast("PLease Click on Back Button");
	}
}