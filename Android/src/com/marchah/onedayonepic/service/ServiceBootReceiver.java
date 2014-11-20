package com.marchah.onedayonepic.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class ServiceBootReceiver extends BroadcastReceiver {
    ServiceReceiver alarm = new ServiceReceiver();
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED"))
        {
        		alarm.setAlarm(context);
        }
    }

}
