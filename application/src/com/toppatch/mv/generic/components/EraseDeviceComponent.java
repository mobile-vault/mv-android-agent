package com.toppatch.mv.generic.components;

import org.json.JSONObject;

import android.app.admin.DevicePolicyManager;
import android.content.Context;
import android.util.Log;


public class EraseDeviceComponent extends Component {

	private static final String TAG = "EraseDeviceComponent";

	private Context context;

	public EraseDeviceComponent(Context context) {
		super(context);
		this.context=context;
	}

	@Override
	public void execute(JSONObject data) {
		Log.d(TAG, "Execute: " + data);

		/*
		 * TODO Handle WIPE_EXTERNAL_STORAGE option, when implemented on server.
		 * http://developer.android.com/reference/android/app/admin/DevicePolicyManager.html#wipeData(int)
		 */
		DevicePolicyManager policyManager =
				(DevicePolicyManager) context.getSystemService(Context.DEVICE_POLICY_SERVICE);
		policyManager.wipeData(0);
	}
}