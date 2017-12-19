package com.mtpv.ghmcepettycase;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.mtpv.ghmcenforcement.R;

/**
 * Created by MANOHAR on 10/27/2017.
 */

public class IPSettings_New extends Activity implements View.OnClickListener {
    public static EditText et_service_url, et_ftp_url;
    Button cancel, save;
    RadioButton live, test;
    public static String Live = "https://www.echallan.org/GHMCWebService";
    public static String open_ftp_fix = "125.16.1.69";

    public static String Test = "http://192.168.11.4/GHMCWebService";

    //Sunil Sys
   // public static String Test = "http://192.168.11.10:8080/GHMCWebService";

    public static String ftp_fix = "192.168.11.9";

    RadioGroup rg_live_test;
    String service_type = "";
    SharedPreferences preference;
    SharedPreferences.Editor editor;
    String SERVICE_URL_PREF = "", FTP_URL_PREF = "", SERVICE_TYPE_PREf = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_ip_settings);

        LoadUIComponents();



        preference = getSharedPreferences("ghmcPref", Context.MODE_PRIVATE);
        editor = preference.edit();

        //This is main varible to set ipsettings defaultly if we change to live then ipsettings live set default one
        SERVICE_TYPE_PREf = preference.getString("servicetype", "test");

        SERVICE_URL_PREF = preference.getString("IP_ADDRESS", "url1");
        FTP_URL_PREF = preference.getString("ftpurl", "url2");

        live = (RadioButton) findViewById(R.id.live);
        test = (RadioButton) findViewById(R.id.test);
        test.setChecked(true);



        if (SERVICE_TYPE_PREf.equals("live")) {
            live.setChecked(true);
            et_service_url.setText("" + Live);
            et_ftp_url.setText("");
            et_ftp_url.setText(open_ftp_fix);
            service_type = "live";
        } else if(SERVICE_TYPE_PREf.equals("test")) {
            et_service_url.setText("" + Test);
            et_ftp_url.setText("");
            et_ftp_url.setText(ftp_fix);
            test.setChecked(true);
            service_type = "test";
        }


    }

    private void clearFields() {
        // TODO Auto-generated method stub
        preference.edit().clear().commit();
        et_service_url.setText("");
        et_ftp_url.setText("");

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

    private void showError(EditText et, String msg) {
        et.setError("" + msg);
    }

    private void LoadUIComponents() {
        // TODO Auto-generated method stub
        et_service_url = (EditText) findViewById(R.id.et_serverUrl);
        et_ftp_url = (EditText) findViewById(R.id.et_ftp_url);
        save = (Button) findViewById(R.id.save);
        cancel = (Button) findViewById(R.id.cancel);

        rg_live_test = (RadioGroup) findViewById(R.id.rg_live_test);
        live=(RadioButton)findViewById(R.id.live);
        test=(RadioButton)findViewById(R.id.test);


        save.setOnClickListener(this);
        cancel.setOnClickListener(this);

        rg_live_test.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // TODO Auto-generated method stub
                switch (checkedId) {
                    case R.id.live:

                        service_type = "live";
                        et_service_url.setText(Live);
                        et_ftp_url.setText(open_ftp_fix);
                        break;

                    case R.id.test:
                        service_type = "test";
                        et_service_url.setText(Test);
                        et_ftp_url.setText(ftp_fix);
                        break;

                    default:
                        break;
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.save:

                if (et_service_url.getText().toString().trim().equals("")) {
                    showError(et_service_url, "Enter Service URL");

                } else if (et_ftp_url.getText().toString().trim().equals("")) {
                    showError(et_ftp_url, "Enter FTP URL");

                } else {



                    preference = getSharedPreferences("ghmcPref", Context.MODE_PRIVATE);
                    editor = preference.edit();

                    if (preference.contains("serviceurl")) {
                        editor.remove("serviceurl");
                        editor.commit();
                    }



                    if (preference.contains("ftpurl")) {
                        editor.remove("ftpurl");
                        editor.commit();
                    }

                    if (preference.contains("servicetype")) {
                        editor.remove("servicetype");
                        editor.commit();
                    }

                    editor.putString("IP_ADDRESS", "" + et_service_url.getText().toString().trim());
                    editor.putString("FTP_URL", "" + et_ftp_url.getText().toString().trim());
                    editor.putString("servicetype", "" + service_type);
                    editor.commit();
                    showToast("Successfully Saved!");
                    finish();
                }
                break;

            case R.id.cancel:
                startActivity(new Intent(this, Login.class));
                break;

            default:
                break;
        }
    }
}
