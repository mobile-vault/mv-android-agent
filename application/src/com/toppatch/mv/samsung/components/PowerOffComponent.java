package com.toppatch.mv.samsung.components;

import org.json.JSONObject;

import android.app.enterprise.EnterpriseDeviceManager;
import android.content.Context;
import android.util.Log;

import com.toppatch.mv.samsung.components.Component;

public class PowerOffComponent extends Component {

	private static final String TAG = "PowerOffComponent";

	public PowerOffComponent(Context context) {
		super(context);
	}

	@Override
	public void execute(JSONObject data) {
		Log.d(TAG, "Executing "+data);
		EnterpriseDeviceManager edm = getEdm();
		if(edm!=null){
			edm.getSecurityPolicy().powerOffDevice();
		}
	}
}