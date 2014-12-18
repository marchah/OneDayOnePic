package com.marchah.onedayonepic.tools;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.os.AsyncTask;

public abstract class GetRequestAPI extends AsyncTask<String, Void, String> {

	protected HashMap<String, String> _mapData;
	protected List<BasicNameValuePair> _params;
	
	public GetRequestAPI(HashMap<String, String> mapData) {
		_mapData = mapData;
		_params = new LinkedList<BasicNameValuePair>();
	}
	
	@Override
	protected void onPreExecute() {
		if (_mapData != null)
			for(Map.Entry<String, String> entry : _mapData.entrySet()){
				_params.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
			}
	}
	
	@Override
	protected String doInBackground(String... urls) {
		if(!urls[0].endsWith("?") && _params.size() > 0)
	        urls[0] += "?";
	    if (_params.size() > 0)
	    	urls[0] += URLEncodedUtils.format(_params, "utf-8");
	    
	    DefaultHttpClient httpclient = new DefaultHttpClient();
        HttpGet request = new HttpGet(urls[0]);
        
        HttpResponse response = null;
        String responseString = null;
        try {
            response = httpclient.execute(request);
            StatusLine statusLine = response.getStatusLine();
            if(statusLine.getStatusCode() == HttpStatus.SC_OK){
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                response.getEntity().writeTo(out);
                out.close();
                responseString = out.toString();
            } else{
                response.getEntity().getContent().close();
                throw new IOException(statusLine.getReasonPhrase());
            }
        } catch (Exception e) {
        	responseString = "error";
        	e.printStackTrace();
        }
        return responseString;
	}

}
