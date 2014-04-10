package com.example.android.BluetoothChat;

import android.app.IntentService;
import android.content.Intent;

public class LogService extends IntentService{

	public LogService() {
		super("LogService");
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		String dataString = intent.getDataString();
		
		
	}

}
