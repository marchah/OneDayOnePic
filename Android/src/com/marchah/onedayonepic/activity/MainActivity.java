package com.marchah.onedayonepic.activity;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.marchah.onedayonepic.R;
import com.marchah.onedayonepic.service.ServiceReceiver;
import com.marchah.onedayonepic.tools.Constants;
import com.marchah.onedayonepic.tools.CustomAdapter;
import com.marchah.onedayonepic.tools.GetRequestAPI;
import com.marchah.onedayonepic.tools.ImageDownloader;
import com.marchah.onedayonepic.tools.Preferences;
import com.marchah.onedayonepic.tools.Tools;

public class MainActivity extends Activity {
	private Spinner sprCategorie;
	private ProgressBar pgbRefreshing;
	private Button btnRefreshing;
	private int idCategorie = 0;
	private int idUser = 0;
	private boolean isInitTrigger = true;
	private ToggleButton tbStatus = null;
	
	private ServiceReceiver service = new ServiceReceiver();
	
	private Typeface typeFont;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_odop);
       
        idCategorie = Preferences.getIdCategorie(getBaseContext());
        idUser = Preferences.getIdUser(getBaseContext());
        sprCategorie = (Spinner)findViewById(R.id.sprCategorie);
        pgbRefreshing = (ProgressBar)findViewById(R.id.pgbRefreshing);
        btnRefreshing = (Button)findViewById(R.id.btnChangePic);        
        tbStatus = (ToggleButton)findViewById(R.id.on_off_tb);
        tbStatus.setChecked(Preferences.getIsAuto(getBaseContext()));
        
        typeFont = Typeface.createFromAsset(getAssets(),Constants.Style.Font); 
        if (typeFont != null) {
        	((TextView)findViewById(R.id.titleTV)).setTypeface(typeFont);
        	btnRefreshing.setTypeface(typeFont);
        	tbStatus.setTypeface(typeFont);
        }
     	       
        sprCategorie.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				if(isInitTrigger){
	                isInitTrigger = false;
	                return;
	            }
				Preferences.saveIdCategorie(getBaseContext(), (int)id);
				changePicture(null);
			}
			@Override
			public void onNothingSelected(AdapterView<?> parent) {}
        });
        GetRequestAPI listCategorieRequest = new GetRequestAPI() {
			   protected void onPostExecute(String response) {
				   try {
					   JSONArray jArray = new JSONArray(response);
					   List<String> listCategorie = new ArrayList<String>();
					   listCategorie.add(getString(R.string.categories_all));
					   for (int i = 0; jArray != null && i != jArray.length(); i++) {
						   JSONObject categorie = jArray.getJSONObject(i);
						   if (categorie.isNull("id") || categorie.isNull("name"))
							   throw new JSONException("");
						   String name = categorie.getString("name");
						   listCategorie.add(name);
					   }
					   CustomAdapter dataAdapter = new CustomAdapter(MainActivity.this, listCategorie, typeFont);
							sprCategorie.setAdapter(dataAdapter);
							sprCategorie.setSelection(idCategorie);
				   }
				   catch (JSONException e) {
					   Toast.makeText(MainActivity.this, getResources().getString(R.string.msg_invalid_api_response), Toast.LENGTH_LONG).show();
				   }
			   }
		   };
	if (!Tools.isOnline(getBaseContext()))
	    Toast.makeText(getBaseContext(), getString(R.string.msg_no_internet), Toast.LENGTH_LONG).show();
	else
	    listCategorieRequest.execute(Constants.API.Categories + getString(R.string.language));
	}

    private void getInfo(final Context context) {
        GetRequestAPI timerSynchroRequest = new GetRequestAPI() {
		   protected void onPostExecute(String response) {
			   try {
				   JSONObject object = new JSONObject(response);
				   Preferences.saveTimerSynchro(context, Integer.parseInt(object.getString("time")));
				   Preferences.saveIdUser(context, Integer.parseInt(object.getString("userId")));
			   }
			   catch (Exception e) {
				   Preferences.saveTimerSynchro(context, Constants.Service.DefaultTimer);
				   Tools.sendNotification(context, context.getResources().getString(R.string.msg_invalid_api_response));
			   }
		   }
	    };
	if (!Tools.isOnline(getBaseContext()))
	    Toast.makeText(getBaseContext(), getString(R.string.msg_no_internet), Toast.LENGTH_LONG).show();
	else
	    timerSynchroRequest.execute(Constants.API.Init);
    }

	
	@Override
	public void onResume(){
	    isInitTrigger = true;
	    super.onResume();            
	    if (Preferences.getTimerSynchro(getBaseContext())<= 0 || Preferences.getIdUser(getBaseContext()) <= 0)
	    	getInfo(getBaseContext());
	}
	
	public void changePicture(View view) {
		final String appName = getResources().getString(R.string.app_name);
		ImageDownloader ddl = new ImageDownloader(appName) {
			protected void onPostExecute(String response) {
				pgbRefreshing.setVisibility(ProgressBar.INVISIBLE);
				btnRefreshing.setClickable(true);
				if (response != null)
					Toast.makeText(MainActivity.this, response, Toast.LENGTH_LONG).show();
				else {
					String ret = Tools.setWallpaper(getBaseContext(), appName);
					if (ret != null)
					    Toast.makeText(MainActivity.this, ret, Toast.LENGTH_LONG).show();
					else
					    Toast.makeText(MainActivity.this, getString(R.string.msg_service_wallpaper_changed), Toast.LENGTH_LONG).show();
				}
			}
		};
		if (!Tools.isOnline(getBaseContext()))
		    Toast.makeText(getBaseContext(), getString(R.string.msg_no_internet), Toast.LENGTH_LONG).show();
		else {
		    ddl.execute(Constants.API.Picture + Preferences.getIdCategorie(getBaseContext()) + "/" + Preferences.getIdUser(getBaseContext()));
		    pgbRefreshing.setVisibility(ProgressBar.VISIBLE);
		    btnRefreshing.setClickable(false);
		}
	}
	
	public void on_offService(View view) {
		if (getResources().getString(R.string.on) == ((ToggleButton)findViewById(R.id.on_off_tb)).getText().toString()) {
			Preferences.saveIsAuto(getBaseContext(), true);
			service.setAlarm(this);
			Toast.makeText(MainActivity.this, getResources().getString(R.string.msg_service_on), Toast.LENGTH_LONG).show();
		}
		else {
			Preferences.saveIsAuto(getBaseContext(), false);
			service.cancelAlarm(this);
			Toast.makeText(MainActivity.this, getResources().getString(R.string.msg_service_off), Toast.LENGTH_LONG).show();
		}
	}
}
