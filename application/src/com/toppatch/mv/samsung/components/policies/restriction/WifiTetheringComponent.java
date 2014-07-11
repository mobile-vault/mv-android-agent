package com.toppatch.mv.samsung.components.policies.restriction;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.enterprise.EnterpriseDeviceManager;
import android.app.enterprise.RestrictionPolicy;
import android.content.Context;
import android.util.Log;

import com.toppatch.mv.Constants;
import com.toppatch.mv.ReportServer;
import com.toppatch.mv.samsung.components.Component;

public class WifiTetheringComponent extends Component {

	private static final String TAG = "WifiTetheringComponent";

	public WifiTetheringComponent(Context context) {
		super(context);
	}

	@Override
	public void execute(JSONObject data) {
		if(data!=null){
			Log.d(TAG, "Executing "+data.toString());
			try {
				boolean enable = data.getBoolean(Constants.RESTRICTION_WIFI_TETHERING_ENABLE);
				EnterpriseDeviceManager edm = getEdm();
				if(edm!=null){
					try{
						RestrictionPolicy restrictionPolicy = edm.getRestrictionPolicy();
						restrictionPolicy.setWifiTethering(enable);
					}catch(Exception e){
						ReportServer.e(TAG+data.toString()+e.toString());
					}
				}
			} catch (JSONException e) {
				//If there is no Constants.ANDROID_BROWSER_ENABLE in the json
				e.printStackTrace();
			}
		}
	}
}
