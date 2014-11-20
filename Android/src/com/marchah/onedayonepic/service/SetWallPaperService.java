package com.marchah.onedayonepic.service;

import com.example.onedayonepic.R;
import com.marchah.onedayonepic.tools.Preferences;
import com.marchah.onedayonepic.tools.Tools;

import android.app.IntentService;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

public class SetWallPaperService extends IntentService {

    NotificationCompat.Builder builder;
	
    public SetWallPaperService() {
        super("SetWallPaperService");
    }
    
    @Override
    protected void onHandleIntent(Intent intent) {
    	if (!Preferences.getIsAuto(getBaseContext()))
    		return ;
    	final String appName = getResources().getString(R.string.app_name);
    	String ret = Tools.setWallpaper(getBaseContext(), appName);
		if (ret != null)
			Tools.sendNotification(getBaseContext(), ret);
		else
			Tools.sendNotification(getBaseContext(), getResources().getString(R.string.msg_service_wallpaper_changed));
        ServiceReceiver.completeWakefulIntent(intent);
    }
}
