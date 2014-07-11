package com.toppatch.mv.samsung.components.policies.wifi;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.enterprise.EnterpriseDeviceManager;
import android.content.Context;
import android.util.Log;

import com.toppatch.mv.Constants;
import com.toppatch.mv.samsung.components.Component;

public class WifiAutoConnectComponent extends Component {

	private static final String TAG = "WifiAutoConnectComponent";

	public WifiAutoConnectComponent(Context context) {
		super(context);
	}

	/**
	 * {"value":true}
	 * {"value":false}
	 */
	@Override
	public void execute(JSONObject data) {
		if(data!=null){
			Log.d(TAG, "Executing "+data.toString());
			try {
				boolean enable = data.getBoolean(Constants.WIFI_AUTO_CONNECT_ENABLE);
				EnterpriseDeviceManager edm2 = getEdm();
				if(edm2!=null){
					try{
						edm2.getWifiPolicy().setAutomaticConnectionToWifi(enable);
					}catch(Exception e){
						e.printStackTrace();
					}
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}
}