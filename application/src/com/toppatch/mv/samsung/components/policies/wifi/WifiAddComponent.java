package com.toppatch.mv.samsung.components.policies.wifi;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.enterprise.EnterpriseDeviceManager;
import android.app.enterprise.WifiAdminProfile;
import android.app.enterprise.WifiPolicy;
import android.content.Context;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.util.Log;

import com.toppatch.mv.Constants;
import com.toppatch.mv.samsung.components.Component;

public class WifiAddComponent extends Component {

	private static final String TAG = "WifiAddComponent";
	Context context;
	public WifiAddComponent(Context context) {
		super(context);
		this.context=context;
	}

	/**
	 * {
	 * 	"list":[
	 * 		{
	 * 			"ssid": "",
	 * 			"security": "PSK",
	 * 			"psk": ""
	 * 		},
	 * 		{
	 * 			"ssid":"",
	 * 			"security": "WEP",
	 * 			"key": ""
	 * 		},
	 * 		{
	 * 			"ssid": "",
	 * 			"security": "NONE"
	 * 		}
	 * 	]
	 * }
	 */
	@Override
	public void execute(JSONObject data) {
		if(data!=null){
			Log.d(TAG, "Executing "+data.toString());
			JSONArray wifiList = data.optJSONArray(Constants.WIFI_LIST);
			EnterpriseDeviceManager edm2 = getEdm();
			if(wifiList!=null && wifiList.length()>0 && edm2!=null){
				for(int i=0;i<wifiList.length();i++){
					//First get the ssid from each.
					try {
						JSONObject wifi = wifiList.getJSONObject(i);
						if(wifi!=null && wifi.optString(Constants.WIFI_SSID,null)!=null){
							WifiAdminProfile profile = new WifiAdminProfile();
							profile.ssid = wifi.optString(Constants.WIFI_SSID);
							profile.security = wifi.optString(Constants.WIFI_SECURITY,null);
							if(profile.security!=null){
								if(profile.security.equals(Constants.WIFI_SECURITY_WEP)){
									profile.wepKey1 =wifi.optString(Constants.WIFI_KEY);
									Log.d(TAG, "WEP"+profile.security+"key:+"+profile.wepKey1);
									profile.wepKeyId=0;
								}else if(profile.security.equals(Constants.WIFI_SECURITY_PSK)){
									profile.psk = wifi.optString(Constants.WIFI_KEY);
								}else if(profile.security.equals(Constants.WIFI_SECURITY_NONE)){
									//To future optimizers, remove this else condition. It's doing nothing.
								}
							}
							edm2.getWifiPolicy().setAutomaticConnectionToWifi(wifi.optBoolean("auto_join"));
							
							/*WifiManager wifiManager = (WifiManager) context.getSystemService(context.WIFI_SERVICE);
							 wifiManager.enableNetwork(profile.ssid, true);*/
							WifiPolicy wifiPolicy = edm2.getWifiPolicy();
							if(wifiPolicy!=null){
								Boolean success=wifiPolicy.setWifiProfile(profile);
								if(!success){
									Log.d(TAG, "error adding "+profile.toString());
								}
							}
							WifiManager wifiManager = (WifiManager) context.getSystemService(context.WIFI_SERVICE);
							List<WifiConfiguration> config = wifiManager.getConfiguredNetworks(); 
							Log.d(TAG, "Size: "+config.size());
							for(int j = 0;j<config.size();j++) { 

								if(config.get(j).SSID.contentEquals(profile.ssid)) { 
									config.get(j).hiddenSSID =wifi.optBoolean("hidden_network"); 
									wifiManager.updateNetwork(config.get(j)); 
									wifiManager.saveConfiguration(); 
	
									wifiManager.disconnect();
									wifiManager.enableNetwork(config.get(j).networkId, true);
									wifiManager.reconnect(); 
								} 
							} 
						}
					} catch (Exception e) {
						e.printStackTrace();
						Log.d(TAG, "Exception:"+e.toString());
					}
				}
			}
		}
	}

}
