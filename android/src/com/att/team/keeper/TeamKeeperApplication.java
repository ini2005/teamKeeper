package com.att.team.keeper;

import android.app.Application;

import com.att.team.keeper.services.BluetoothService;

public class TeamKeeperApplication extends Application {

	@Override
	public void onCreate() {
		super.onCreate();
		
		BluetoothService.INSTANCE.init(this.getApplicationContext());
	}
	
}
