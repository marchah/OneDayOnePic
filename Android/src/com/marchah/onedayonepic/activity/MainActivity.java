package com.marchah.onedayonepic.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.onedayonepic.R;
import com.marchah.onedayonepic.service.ServiceReceiver;
import com.marchah.onedayonepic.tools.Constants;
import com.marchah.onedayonepic.tools.GetRequestAPI;
import com.marchah.onedayonepic.tools.ImageDownloader;
import com.marchah.onedayonepic.tools.Preferences;
import com.marchah.onedayonepic.tools.Tools;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.ToggleButton;

public class MainActivity extends Activity {
	Spinner sprCategorie;
	ProgressBar pgbRefreshing;
	Button btnRefreshing;
	int idCategorie = 0;
	boolean isInitTrigger = true;
	
	ServiceReceiver service = new ServiceReceiver();
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_odop_v2);
       
        idCategorie = Preferences.getIdCategorie(getBaseContext());
        sprCategorie = (Spinner)findViewById(R.id.sprCategorie);
        pgbRefreshing = (ProgressBar)findViewById(R.id.pgbRefreshing);
        btnRefreshing = (Button)findViewById(R.id.btnChangePic);
        sprCategorie.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				if(isInitTrigger){
	                isInitTrigger = false;
	                return;
	            }
				Preferences.saveIdCategorie(getBaseContext(), (int)id);
				Toast.makeText(MainActivity.this, getResources().getString(R.string.msg_categorie_saved), Toast.LENGTH_LONG).show();
			}
			@Override
			public void onNothingSelected(AdapterView<?> parent) {}
        });
        HashMap<String, String> data = new HashMap<String, String>();
		data.put("nameRequest", "getListCategories");
        GetRequestAPI listCategorieRequest = new GetRequestAPI(data) {
			   protected void onPostExecute(String response) {
				   try {
					   JSONArray jArray = new JSONArray(response);
					   List<String> listCategorie = new ArrayList<String>();
					   listCategorie.add("All");
					   for (int i = 0; jArray != null && i != jArray.length(); i++) {
						   JSONObject categorie = jArray.getJSONObject(i);
						   if (categorie.isNull("id") || categorie.isNull("name"))
							   throw new JSONException("");
						   //String id = categorie.getString("id");
						   String name = categorie.getString("name");
						   listCategorie.add(name);
					   }
					   ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(MainActivity.this,
								android.R.layout.simple_spinner_item, listCategorie);
							dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
							sprCategorie.setAdapter(dataAdapter);
							sprCategorie.setSelection(idCategorie);
				   }
				   catch (JSONException e) {
					   Toast.makeText(MainActivity.this, "Error: Invalid Server Response.", Toast.LENGTH_LONG).show();
				   }
			   }
		   };
		   listCategorieRequest.execute(Constants.API.Index);
	}
	
	@Override
	public void onResume(){
		isInitTrigger = true;
	    super.onResume();
	}
	
	public void changePicture(View view) {
		final String appName = getResources().getString(R.string.app_name);
    	HashMap<String, Integer> data = new HashMap<String, Integer>();
		data.put("idCategorie", Preferences.getIdCategorie(getBaseContext()));
		ImageDownloader ddl = new ImageDownloader(appName, data) {
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
						Toast.makeText(MainActivity.this, "Wallpaper changed", Toast.LENGTH_LONG).show();
				}
			}
		};
		ddl.execute(Constants.API.Picture);
		pgbRefreshing.setVisibility(ProgressBar.VISIBLE);
		btnRefreshing.setClickable(false);
	}
	
	public void on_offService(View view) {
		if (getResources().getString(R.string.on) == ((ToggleButton)findViewById(R.id.on_off_tb)).getText().toString()) {
			Preferences.saveIsAuto(getBaseContext(), true);
			service.setAlarm(this);
			Toast.makeText(MainActivity.this, getResources().getString(R.string.msg_service_on), Toast.LENGTH_LONG).show();
		}
		else {
			Preferences.saveIsAuto(getBaseContext(), true);
			// Preferences.saveType(context, false); oui/non ???
			service.cancelAlarm(this);
			Toast.makeText(MainActivity.this, getResources().getString(R.string.msg_service_off), Toast.LENGTH_LONG).show();
		}
	}
}
