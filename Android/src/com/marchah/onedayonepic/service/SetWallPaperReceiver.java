package com.marchah.onedayonepic.service;

import com.marchah.onedayonepic.tools.Constants;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;

public class SetWallPaperReceiver extends WakefulBroadcastReceiver {   
    @Override
    public void onReceive(Context context, Intent intent) {
    	if (intent != null && intent.getAction() != null && intent.getAction().equals(Constants.IntentAction.SetWallpaper)) {
    		Intent schedulingService = new Intent(context, SetWallPaperService.class);
    		startWakefulService(context, schedulingService);
    	}
    }
}
