package com.att.team.keeper.requests;

import android.os.HandlerThread;

public class NetworkThread  extends HandlerThread {

	public NetworkThread() {
		super("Network");
		start();
		
	}

}
