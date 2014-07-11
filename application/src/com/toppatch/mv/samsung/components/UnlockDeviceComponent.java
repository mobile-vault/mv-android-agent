package com.toppatch.mv.samsung.components;

import org.json.JSONObject;

import android.app.enterprise.EnterpriseDeviceManager;
import android.content.Context;
import android.util.Log;

public class UnlockDeviceComponent extends Component {

	private static final String TAG = "UnlockDeviceComponent";

	public UnlockDeviceComponent(Context context) {
		super(context);
	}

	@Override
	public void execute(JSONObject data) {
		Log.d(TAG, "Executing "+data);
		EnterpriseDeviceManager edm = getEdm();
		if(edm!=null){
			edm.getSecurityPolicy().removeDeviceLockout();
		}
	}
}
