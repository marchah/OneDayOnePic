package com.marchah.onedayonepic.service;

import java.util.Calendar;

import com.marchah.onedayonepic.tools.Preferences;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.content.WakefulBroadcastReceiver;

public class ServiceReceiver extends WakefulBroadcastReceiver {
	
    private AlarmManager amSetWallpaper;
    private PendingIntent alarmIntent;
    
    @Override
    public void onReceive(Context context, Intent intent) {
    	
    	if (Preferences.getType(context)) {
    		Intent DDLPictureService = new Intent(context, DDLPictureService.class);
    		startWakefulService(context, DDLPictureService);
    		Preferences.saveType(context, false);
    	}
    	else {
    		Intent schedulingService = new Intent(context, SchedulingService.class);
    		startWakefulService(context, schedulingService);
    		Preferences.saveType(context, true);
    	}
    }
    
    public void setAlarm(Context context) {
    	amSetWallpaper = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, ServiceReceiver.class);
        alarmIntent = PendingIntent.getBroadcast(context, 0, intent, 0);

        // TODO Save timer synchro if -1
        int timer = Preferences.getTimerSynchro(context);
        
        /*Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        // Set the alarm's trigger time to 8:30 a.m.
        calendar.set(Calendar.HOUR_OF_DAY, 8);
        calendar.set(Calendar.MINUTE, 30);*/
        amSetWallpaper.setInexactRepeating(AlarmManager.RTC_WAKEUP,  
                /*calendar.getTimeInMillis()*/ 400000, 400000/*AlarmManager.INTERVAL_DAY*/, alarmIntent);
        
        ComponentName receiver = new ComponentName(context, ServiceBootReceiver.class);
        PackageManager pm = context.getPackageManager();

        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);           
    }

    public void cancelAlarm(Context context) {
        if (amSetWallpaper != null) {
        	amSetWallpaper.cancel(alarmIntent);
        }
        ComponentName receiver = new ComponentName(context, ServiceBootReceiver.class);
        PackageManager pm = context.getPackageManager();

        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP);
    }
}
