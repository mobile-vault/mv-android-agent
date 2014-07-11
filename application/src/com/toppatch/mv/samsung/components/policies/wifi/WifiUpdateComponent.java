package com.toppatch.mv.samsung.components.policies.wifi;

import java.util.HashMap;
import java.util.HashSet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.toppatch.mv.Constants;
import com.toppatch.mv.samsung.components.Component;

public class WifiUpdateComponent extends Component {

	private Context mContext;

	public WifiUpdateComponent(Context context) {
		super(context);
		mContext = context;
	}

	/**
	 * TODO
	 * This is the main function to update the wifi information in a device
	 * It is responsible for keeping the track of all the wifis added by the admin.
	 * If the request from the server comes with a different set of wifis, the corresponding SSID wifi has to be removed.
	 * 
	 * {
	 * 	"installed_wifis": [
	 * 			{
	 * 				"ssid": "",
	 * 				...
	 * 			},
	 * 			{
	 * 				"ssid": "",
	 * 				...
	 * 			}
	 * 		]
	 * }
	 */
	@Override
	public void execute(JSONObject data) {
		SharedPreferences wifiPreferences = mContext.getSharedPreferences("wifi", Context.MODE_PRIVATE);
		//Get the list of wifis. TODO use string set. That should be the way to go. 
		//To avoid getting into any errors because of "different" implementation of StringSet in shared preferences, I will go with a JSONArray.
		//It should be changed when time allows us to.
		String installedWifis = wifiPreferences.getString("installed_wifis", null);
		if(installedWifis!=null){
			//Create a hashset from the array.
			try {
				JSONArray jar= new JSONArray(installedWifis);
				HashSet<String> set = new HashSet<String>();
				for(int j=0;j<jar.length();j++){
					set.add(jar.getString(j));
				}

				//Now iterate through the one from the server.
				JSONArray wifiList = data.optJSONArray(Constants.WIFI_LIST);
				JSONArray addWifiList = new JSONArray();
				JSONArray removeWifiList = new JSONArray();
				
				for(int i=0;i<wifiList.length();i++){
					JSONObject wifi = wifiList.getJSONObject(i);
					if(wifi!=null){
						String ssid = wifi.getString(Constants.WIFI_SSID);
						if(set.contains(ssid)){
							set.remove(ssid);							
						}
						//All the wifis that came from the message need to be upserted any case :)
						addWifiList.put(wifi);
					}
				}
				//Now see the set that is left, and remove the ssids present there from the device.
				for(String ssidLeft: set){
					removeWifiList.put((new JSONObject()).put(Constants.WIFI_SSID, ssidLeft));
				}
				
				new WifiAddComponent(mContext).execute((new JSONObject()).put(Constants.WIFI_LIST, addWifiList));
				new WifiRemoveComponent(mContext).execute((new JSONObject()).put(Constants.WIFI_LIST, removeWifiList));
				
				Editor edit= wifiPreferences.edit();
				JSONArray newWifiList = data.optJSONArray(Constants.WIFI_LIST);
				if(wifiList!=null){
					edit.putString("installed_wifis", newWifiList.toString());
					edit.commit();
				}
			} catch (JSONException e) {
				new WifiAddComponent(mContext).execute(data);
				e.printStackTrace();
			}
		}else{
			//All the wifis are new... Send the data to WifiAddComponent, without any modification.
			new WifiAddComponent(mContext).execute(data);
			//Add the wifis in the installed list as a hashmap with ssid as key and true and value.
			Editor edit= wifiPreferences.edit();
			JSONArray wifiList = data.optJSONArray(Constants.WIFI_LIST);
			if(wifiList!=null){
				edit.putString("installed_wifis", wifiList.toString());
				edit.commit();
			}
		}
	}

}
