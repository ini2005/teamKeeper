package com.att.team.keeper.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;

import com.att.team.keeper.R;
import com.example.android.wizardpager.MainWizardActivity;
import com.example.android.wizardpager.wizard.model.CustomerInfoPage;

public class SplashActivity extends Activity {

	// Splash screen timer
    private static int SPLASH_TIME_OUT = 3000;
 
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
 
        new Handler().postDelayed(new Runnable() {
 

 
            @Override
            public void run() {

            	Class activityClass = null;
            	
            	SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
				
            	if(sharedPreferences.getString(CustomerInfoPage.NAME_DATA_KEY, null) != null){
            		activityClass = MainActivity.class;
            	}else{
            		activityClass = MainWizardActivity.class;
            	}
				
                Intent i = new Intent(SplashActivity.this, activityClass);
                startActivity(i);
 
                finish();
            }
        }, SPLASH_TIME_OUT);
    }
}
