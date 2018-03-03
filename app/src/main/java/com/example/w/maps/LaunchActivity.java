package com.example.w.maps;
import android.app.LauncherActivity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.app.Activity;
import android.content.Intent;

import com.example.w.maps.R;


public class LaunchActivity extends Activity {
boolean hasLoggedIn;
    SharedPreferences mPrefs,mPrefs1;
    Boolean welcomeScreenShown;
    public static final String PREFS_NAME = "MyPrefsFile";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        //Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //set content view AFTER ABOVE sequence (to avoid crash)
        // this.setContentView(R.layout.launcher);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);


        mPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        welcomeScreenShown=mPrefs.getBoolean("welcomeScreenShown", false);
        if(welcomeScreenShown) {
            Handler mHandler = new Handler();
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {


                    startActivity(new Intent().setClass(LaunchActivity.this, MainActivity.class).setData(getIntent().getData()));
                    finish();
                }
            }, 3000);
        }
        else {
            Handler mHandler = new Handler();
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {


                    startActivity(new Intent().setClass(LaunchActivity.this, InitialActivity.class).setData(getIntent().getData()));
                    finish();
                }
            }, 3000);
        }





    }
	
	
	/*	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}*/






}
