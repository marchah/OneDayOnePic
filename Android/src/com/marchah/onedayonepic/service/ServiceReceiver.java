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

		ImageDownloader ddl = new ImageDownloader(appName) {
			protected void onPostExecute(String response) {
				if (response != null)
					Tools.sendNotification(context, response);
				setSetWallpaperAlarm(context);
				setDDLPictureAlarm(context);
			}
		};
		ddl.execute(Constants.API.Picture + Preferences.getIdCategorie(context) + "/" + Preferences.getIdUser(context));
    }
        
    public void setAlarm(final Context context) {
	getPicture(context);
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
