package com.att.team.keeper.activities;

import com.att.team.keeper.R;

import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class MobileLostPanicActivity extends Activity {
	
	private MediaPlayer mMediaPlayer;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mobile_lost_panic_activity);
		
		mMediaPlayer = MediaPlayer.create(this, R.raw.alert);
		mMediaPlayer.start();
		
		getActionBar().hide();
		
		findViewById(R.id.mobileLostPanic_stopAlertButton).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mMediaPlayer.stop();
			}
		});
	}

}
