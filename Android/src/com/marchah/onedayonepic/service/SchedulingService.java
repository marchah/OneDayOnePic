package com.marchah.onedayonepic.service;

import java.util.HashMap;

import com.example.onedayonepic.R;
import com.marchah.onedayonepic.tools.Preferences;
import com.marchah.onedayonepic.tools.Tools;

import android.app.IntentService;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

public class SchedulingService extends IntentService {

    NotificationCompat.Builder builder;
	
    public SchedulingService() {
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
			Tools.sendNotification(getBaseContext(), "Wallpaper changed");
        ServiceReceiver.completeWakefulIntent(intent);
    }
}
