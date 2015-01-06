package com.marchah.onedayonepic.tools;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;

import android.graphics.BitmapFactory;
import android.os.AsyncTask;

public abstract class ImageDownloader extends AsyncTask<String, Integer, String> {

	private String  _appName;
	protected HashMap<String, Integer> _mapData;
	protected List<BasicNameValuePair> _params;
	
	public ImageDownloader(String appName) {
		_appName = appName;
		_params = new LinkedList<BasicNameValuePair>();
	}
	
	@Override
	protected void onPreExecute() {
		if (_mapData != null)
			for(Map.Entry<String, Integer> entry : _mapData.entrySet()){
				_params.add(new BasicNameValuePair(entry.getKey(), entry.getValue().toString()));
			}
	}
	
	@Override
	protected String doInBackground(String... urls) {
		if (!Tools.isExternalStoragePresent())
			return "Error: SD card not present";
		if(!urls[0].endsWith("?") && _params.size() > 0)
	        urls[0] += "?";
	    if (_params.size() > 0)
	    	urls[0] += URLEncodedUtils.format(_params, "utf-8");
		try {
	        URL url = new URL(urls[0]);
	        HttpURLConnection connection = (HttpURLConnection) url
	                .openConnection();
	        connection.setDoInput(true);
	        connection.connect();
	        return Tools.saveImage(BitmapFactory.decodeStream(connection.getInputStream()), _appName);

	    } catch (IOException e) {
	        e.printStackTrace();
	        return "Error: Downloading Wallpaper Failed.";
	    }
	}
}
