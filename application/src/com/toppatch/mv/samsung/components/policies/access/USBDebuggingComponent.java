package com.toppatch.mv.samsung.components.policies.access;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.enterprise.EnterpriseDeviceManager;
import android.app.enterprise.RestrictionPolicy;
import android.content.Context;
import android.util.Log;

import com.toppatch.mv.Constants;
import com.toppatch.mv.samsung.components.Component;

public class USBDebuggingComponent extends Component {

	private static final String TAG = "USBDebuggingComponent";

	public USBDebuggingComponent(Context context) {
		super(context);
	}

	@Override
	public void execute(JSONObject data) {
		if(data!=null){
			Log.d(TAG, "executing "+data.toString());
			try {
				boolean changeSettingsEnable = data.getBoolean(Constants.ACCESS_USB_DEBUG_ENABLE);
				EnterpriseDeviceManager edm2 = getEdm();
				if(edm2!=null){
					RestrictionPolicy restrictionPolicy = edm2.getRestrictionPolicy();
					restrictionPolicy.setUsbDebuggingEnabled(changeSettingsEnable);
				}
			} catch (JSONException e) {
				Log.d(TAG,"No "+Constants.ACCESS_USB_DEBUG_ENABLE+" sent");
			}
		}
	}

}
