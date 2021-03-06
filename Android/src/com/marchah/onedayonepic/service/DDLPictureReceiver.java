package com.marchah.onedayonepic.service;

import com.marchah.onedayonepic.tools.Constants;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;

public class DDLPictureReceiver extends WakefulBroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
    	if (intent != null && intent.getAction() != null && intent.getAction().equals(Constants.IntentAction.DDLImage)) {
    		Intent DDLPictureService = new Intent(context, DDLPictureService.class);
			startWakefulService(context, DDLPictureService);
    	}
    }
}
