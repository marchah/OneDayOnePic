package com.marchah.onedayonepic.tools;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.FileChannel;

import com.example.onedayonepic.R;
import com.marchah.onedayonepic.activity.MainActivity;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.WallpaperManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Environment;
import android.support.v4.app.NotificationCompat;

public class Tools {
	
	public static boolean isExternalStoragePresent() {
		boolean mExternalStorageAvailable = false;
		boolean mExternalStorageWriteable = false;
		String state = Environment.getExternalStorageState();

		if (Environment.MEDIA_MOUNTED.equals(state)) {
			// We can read and write the media
			mExternalStorageAvailable = mExternalStorageWriteable = true;
		}
		else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
			// We can only read the media
			mExternalStorageAvailable = true;
			mExternalStorageWriteable = false;
		}
		else {
			// Something else is wrong. It may be one of many other states, but
			// all we need
			// to know is we can neither read nor write
			mExternalStorageAvailable = mExternalStorageWriteable = false;
		}
		return (mExternalStorageAvailable) && (mExternalStorageWriteable);
	}
	
	private static void copy(File src, File dst) throws IOException {
	    FileInputStream inStream = new FileInputStream(src);
	    FileOutputStream outStream = new FileOutputStream(dst);
	    FileChannel inChannel = inStream.getChannel();
	    FileChannel outChannel = outStream.getChannel();
	    inChannel.transferTo(0, inChannel.size(), outChannel);
	    inStream.close();
	    outStream.close();
	}
	
	public static String setWallpaper(Context context, String appName) {
		WallpaperManager wpm = WallpaperManager.getInstance(context);
		InputStream ins;
		try {
			String src = Environment.getExternalStorageDirectory()+File.separator+appName+File.separator+"Wallpaper"+File.separator+"next.jpg";
			String to = Environment.getExternalStorageDirectory()+File.separator+appName+File.separator+"Wallpaper"+File.separator+"current.jpg";
			copy(new File(src), new File(to));
			ins = new BufferedInputStream(new FileInputStream(to));
			wpm.setStream(ins);
		} catch (Exception e) {
			e.printStackTrace();
			return "Error: Change wallpaper failed";
		}
		return null;
	}
	
	public static String saveImage(Bitmap bmp, String appName) {
		ByteArrayOutputStream bytes = new ByteArrayOutputStream();
	    bmp.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
	    
	    File wallpaperDirectory = new File(Environment.getExternalStorageDirectory()+File.separator+appName+File.separator+"Wallpaper"+File.separator);
		wallpaperDirectory.mkdirs();
	    
		File file = new File(wallpaperDirectory, "next.jpg");
	    try {
	        file.createNewFile();
	        FileOutputStream fos = new FileOutputStream(file);
	        fos.write(bytes.toByteArray());
	        fos.close();
	    } catch (Exception e) {
	    	return "Error: Save Wallpaper Failed.";
	    }
	    return null;
	}
	
	public final static void sendNotification(Context context, String msg) {
		NotificationManager mNotificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);
     
         PendingIntent contentIntent = PendingIntent.getActivity(context, 0,
             new Intent(context, MainActivity.class), 0);

         NotificationCompat.Builder mBuilder =
                 new NotificationCompat.Builder(context)
         .setSmallIcon(R.drawable.ic_launcher)
         .setContentTitle(context.getString(R.string.app_alert))
         .setStyle(new NotificationCompat.BigTextStyle()
         .bigText(msg))
         .setContentText(msg);

         mBuilder.setContentIntent(contentIntent);
         mNotificationManager.notify(Constants.Service.IdNotification++, mBuilder.build());
     }
}
