package com.marchah.onedayonepic.service;

import java.util.Calendar;
import java.util.HashMap;

import org.json.JSONObject;

import com.marchah.onedayonepic.R;
import com.marchah.onedayonepic.tools.Constants;
import com.marchah.onedayonepic.tools.GetRequestAPI;
import com.marchah.onedayonepic.tools.ImageDownloader;
import com.marchah.onedayonepic.tools.Preferences;
import com.marchah.onedayonepic.tools.Tools;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.content.WakefulBroadcastReceiver;

public class ServiceReceiver extends WakefulBroadcastReceiver {
	
    private AlarmManager amSetWallpaper;
    private AlarmManager amDDLWallpaper;
    private PendingIntent SetPendingIntent;
    private PendingIntent DDLPendingIntent;
    
    @Override
    public void onReceive(Context context, Intent intent) {
    }
    
    private void setSetWallpaperAlarm(Context context) {
    	amSetWallpaper = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
    	Intent Setintent = new Intent(context, SetWallPaperReceiver.class);
    	Setintent.setAction(Constants.IntentAction.SetWallpaper);
    	SetPendingIntent = PendingIntent.getBroadcast(context, 0, Setintent, 0);
    	Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.AM_PM, Calendar.AM);
        calendar.set(Calendar.HOUR_OF_DAY, 7);
        calendar.set(Calendar.MINUTE, 30);
             
    	amSetWallpaper.setInexactRepeating(AlarmManager.RTC_WAKEUP,  
                calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, SetPendingIntent);
    }
    
    private void setDDLPictureAlarm(Context context) {
    	amDDLWallpaper = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
    	Intent DDLintent = new Intent(context, DDLPictureReceiver.class);
    	DDLintent.setAction(Constants.IntentAction.DDLImage);
    	DDLPendingIntent = PendingIntent.getBroadcast(context, 0, DDLintent, 0);
    	Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        
        calendar.set(Calendar.AM_PM, Calendar.AM);
        calendar.set(Calendar.HOUR_OF_DAY, 1);
        calendar.set(Calendar.MINUTE, 1);
        calendar.set(Calendar.SECOND, Preferences.getTimerSynchro(context));
        
    	amDDLWallpaper.setInexactRepeating(AlarmManager.RTC_WAKEUP,  
                calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, DDLPendingIntent);
    }
    
    private void getPicture(final Context context) {
    	final String appName = context.getResources().getString(R.string.app_name);
    	HashMap<String, Integer> data = new HashMap<String, Integer>();
		data.put("idCategorie", Preferences.getIdCategorie(context));
		ImageDownloader ddl = new ImageDownloader(appName, data) {
			protected void onPostExecute(String response) {
				if (response != null)
					Tools.sendNotification(context, response);
				setSetWallpaperAlarm(context);
	        	setDDLPictureAlarm(context);
			}
		};
		ddl.execute(Constants.API.Picture);
    }
    
    private void getTimerSynchro(final Context context) {
    	HashMap<String, String> data = new HashMap<String, String>();
		data.put("nameRequest", "getTimeSynchro");
        GetRequestAPI timerSynchroRequest = new GetRequestAPI(data) {
		   protected void onPostExecute(String response) {
			   try {
				   JSONObject object = new JSONObject(response);
				   Preferences.saveTimerSynchro(context, Integer.parseInt(object.getString("time")));
			   }
			   catch (Exception e) {
				   Preferences.saveTimerSynchro(context, Constants.Service.DefaultTimer);
				   Tools.sendNotification(context, context.getResources().getString(R.string.msg_invalid_api_response));
			   }
			   getPicture(context);
		   }
	    };
		timerSynchroRequest.execute(Constants.API.Index);
    }
    
    public void setAlarm(final Context context) {
        int timer = Preferences.getTimerSynchro(context);
        
        if (timer < 0) {
        	getTimerSynchro(context);
        }
        else {
        	getPicture(context);
        }
        ComponentName receiver = new ComponentName(context, ServiceBootReceiver.class);
        PackageManager pm = context.getPackageManager();

        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);           
    }

    public void cancelAlarm(Context context) {
        if (amSetWallpaper != null)
        	amSetWallpaper.cancel(SetPendingIntent);
        if (amDDLWallpaper != null)
        	amDDLWallpaper.cancel(DDLPendingIntent);
        ComponentName receiver = new ComponentName(context, ServiceBootReceiver.class);
        PackageManager pm = context.getPackageManager();

        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP);
    }
}
