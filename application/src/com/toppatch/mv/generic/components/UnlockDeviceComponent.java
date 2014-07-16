package com.toppatch.mv.generic.components;

import org.json.JSONObject;

import android.app.admin.DevicePolicyManager;
import android.content.Context;
import android.util.Log;

public class UnlockDeviceComponent extends Component {

	private static final String TAG = "UnlockDeviceComponent";

	private Context context;

	public UnlockDeviceComponent(Context context) {
		super(context);
		this.context = context;
	}

	@Override
	public void execute(JSONObject data) {
		Log.d(TAG, "Execute: " + data);

		/*
		 * No official documentation for removing password completely.
		 * Workaround: reset password to blank password.
		 */
		DevicePolicyManager policyManager =
				(DevicePolicyManager) context.getSystemService(Context.DEVICE_POLICY_SERVICE);
		policyManager.resetPassword("", 0);
	}
}
