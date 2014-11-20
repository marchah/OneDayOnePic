package com.marchah.onedayonepic.service;

import java.util.HashMap;

import com.example.onedayonepic.R;
import com.marchah.onedayonepic.tools.Constants;
import com.marchah.onedayonepic.tools.ImageDownloader;
import com.marchah.onedayonepic.tools.Preferences;
import com.marchah.onedayonepic.tools.Tools;
import com.marchah.onedayonepic.tools.Constants.API;

import android.app.IntentService;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

public class DDLImageService extends IntentService {

    NotificationCompat.Builder builder;
	
    public DDLImageService() {
        super("DDLImageService");
        Tools.sendNotification(getBaseContext(), "Construct DDLImageService");
    }
    
    @Override
    protected void onHandleIntent(Intent intent) {
    	Tools.sendNotification(getBaseContext(), "OnHandleIntent DDLImageService");
    	final String appName = getResources().getString(R.string.app_name);
    	HashMap<String, Integer> data = new HashMap<String, Integer>();
		data.put("idCategorie", Preferences.getIdCategorie(getBaseContext()));
		ImageDownloader ddl = new ImageDownloader(appName, data) {
			protected void onPostExecute(String response) {
				if (response != null){
					Tools.sendNotification(getBaseContext(), response);
				}
				else
					Tools.sendNotification(getBaseContext(), "Wallpaper Downloaded.");
			}
		};
		ddl.execute(Constants.API.Picture);
        ServiceReceiver.completeWakefulIntent(intent);
    }
}
