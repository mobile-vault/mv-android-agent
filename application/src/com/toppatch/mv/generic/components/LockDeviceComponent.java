package com.toppatch.mv.generic.components;

import org.json.JSONObject;

import android.app.admin.DevicePolicyManager;
import android.content.Context;
import android.util.Log;

import com.toppatch.mv.Constants;

public class LockDeviceComponent extends Component {

	private static final String TAG = "LockDeviceComponent";

	private Context context;

	public LockDeviceComponent(Context context) {
		super(context);

		this.context = context;
	}

	@Override
	public void execute(JSONObject data) {
		Log.d(TAG, "Execute: " + data);

		/*
		 * TODO Handle option to block other Device Administrators from changing password, when implemented on Server.
		 * http://developer.android.com/reference/android/app/admin/DevicePolicyManager.html#resetPassword(java.lang.String, int)
		 */
		DevicePolicyManager policyManager =
				(DevicePolicyManager) context.getSystemService(Context.DEVICE_POLICY_SERVICE);
		policyManager.resetPassword(data.optString(Constants.LOCK_DEVICE_NEW_PASSCODE, null), 0);
		policyManager.lockNow();
	}
}