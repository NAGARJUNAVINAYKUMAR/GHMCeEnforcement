package com.mtpv.ghmcepettycase;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Window;

import com.mtpv.ghmcenforcement.R;

public class MainActivity extends Activity{

    private static int SPLASH_TIME_OUT = 2000;
 
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        
        /*******start to check the heap memory***********/
        Runtime rt = Runtime.getRuntime();
        long maxMemory = rt.maxMemory();
        /*******end to check the heap memory***********/

        /*******start to check the class memory***********/
        ActivityManager am = (ActivityManager)getSystemService(ACTIVITY_SERVICE);
        int memoryClass = am.getMemoryClass();
        /*******end to check the class memory***********/
        
		/**************Adding Shortcut of Application**************/
	    SharedPreferences prefs = getSharedPreferences("ShortCutPrefs", MODE_PRIVATE);
	    if(!prefs.getBoolean("isFirstTime", false)){
	        addShortcut();
	        SharedPreferences.Editor editor = prefs.edit();
	        editor.putBoolean("isFirstTime", true);
	        editor.commit();
	    } 
	    /**************Adding Shortcut of Application**************/
 
        new Handler().postDelayed(new Runnable() {
 
            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */
 
            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity
                Intent i = new Intent(MainActivity.this, Login.class);
                startActivity(i);
 
                // close this activity
                finish();
            }
        }, SPLASH_TIME_OUT);
    }
    
    /**************Adding Shortcut of Application**************/
	private void addShortcut() {
		 Intent shortcutIntent = new Intent(getApplicationContext(), MainActivity.class);
	        shortcutIntent.setAction(Intent.ACTION_MAIN);
	        shortcutIntent.addCategory(Intent.CATEGORY_LAUNCHER);
	        int flags = Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED | Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT;
	        shortcutIntent.addFlags(flags);

	        Intent addIntent = new Intent();
	        addIntent.putExtra("duplicate", false);
	        addIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
	        addIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, getResources().getString(R.string.app_name));
	        addIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, Intent.ShortcutIconResource
	                .fromContext(getApplicationContext(), R.drawable.ghmc));
	        addIntent.setAction("com.android.launcher.action.INSTALL_SHORTCUT");
	        getApplicationContext().sendBroadcast(addIntent);
	}
	/**************Adding Shortcut of Application**************/
}
