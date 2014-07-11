package com.toppatch.mv.samsung.components.policies.wifi;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.enterprise.EnterpriseDeviceManager;
import android.app.enterprise.WifiPolicy;
import android.content.Context;

import com.toppatch.mv.Constants;
import com.toppatch.mv.samsung.components.Component;

public class WifiRemoveComponent extends Component {

	public WifiRemoveComponent(Context context) {
		super(context);
	}

	/**
	 * data is a JSON object having attribute list. 
	 * {
	 * 	"list":[
	 * 			{
	 * 				"ssid": "XYZ"
	 * 			},
	 * 			{
	 * 				"ssid": "PQR"
	 * 			}
	 * 		]
	 * }
	 */
	@Override
	public void execute(JSONObject data) {
		if(data!=null){
			JSONArray wifiList = data.optJSONArray(Constants.WIFI_LIST);
			if(wifiList!=null){
				EnterpriseDeviceManager edm2 = getEdm();
				WifiPolicy wifiPolicy=null;
				if(edm2!=null)
					 wifiPolicy= edm2.getWifiPolicy();
				for(int i=0;i<wifiList.length();i++){
					try {
						JSONObject wifi = wifiList.getJSONObject(i);
						if(wifi!=null && wifiPolicy!=null){
							String ssid = wifi.optString(Constants.WIFI_SSID);
							wifiPolicy.removeNetworkConfiguration(ssid);
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

}
