package com.marchah.onedayonepic.tools;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public final class Preferences {
	
	
   public static void saveIsAuto(Context context, boolean isAuto) {
	   SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
	   SharedPreferences.Editor editor = preferences.edit();
	   editor.putBoolean(Constants.SharedPreferences.IsAuto, isAuto);
	   editor.commit();
   }
   
	public static Boolean getIsAuto(Context context) {
		   SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
		   return preferences.getBoolean(Constants.SharedPreferences.IsAuto, false);
	}
	
	public static void saveIdCategorie(Context context, int idCategorie) {
		   SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
		   SharedPreferences.Editor editor = preferences.edit();
		   editor.putInt(Constants.SharedPreferences.IdCategorie, idCategorie);
		   editor.commit();
	}
		
	public static int getIdCategorie(Context context) {
	   SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
	   return preferences.getInt(Constants.SharedPreferences.IdCategorie, 0);
	}

   public static void saveTimerSynchro(Context context, int timerSynchro) {
	   SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
	   SharedPreferences.Editor editor = preferences.edit();
	   editor.putInt(Constants.SharedPreferences.Timer, timerSynchro);
	   editor.commit();
   }
	
	public static int getTimerSynchro(Context context) {
	   SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
	   return preferences.getInt(Constants.SharedPreferences.Timer, -1);
   }
}
