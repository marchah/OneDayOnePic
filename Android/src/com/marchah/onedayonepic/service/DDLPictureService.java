package com.marchah.onedayonepic.service;

import java.util.HashMap;

import android.app.IntentService;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.example.onedayonepic.R;
import com.marchah.onedayonepic.tools.Constants;
import com.marchah.onedayonepic.tools.ImageDownloader;
import com.marchah.onedayonepic.tools.Preferences;
import com.marchah.onedayonepic.tools.Tools;

public class DDLPictureService extends IntentService {

    NotificationCompat.Builder builder;
	
    public DDLPictureService() {
        super("DDLPictureService");
    }
    
    @Override
    protected void onHandleIntent(Intent intent) {
    	if (!Preferences.getIsAuto(getBaseContext()))
    		return ;
    	final String appName = getResources().getString(R.string.app_name);
    	HashMap<String, Integer> data = new HashMap<String, Integer>();
		data.put("idCategorie", Preferences.getIdCategorie(getBaseContext()));
		ImageDownloader ddl = new ImageDownloader(appName, data) {
			protected void onPostExecute(String response) {
				if (response != null)
					Tools.sendNotification(getBaseContext(), response);
				else
					Tools.sendNotification(getBaseContext(), "Wallpaper Downloaded.");
			}
		};
		ddl.execute(Constants.API.Picture);
        ServiceReceiver.completeWakefulIntent(intent);
    }
}
