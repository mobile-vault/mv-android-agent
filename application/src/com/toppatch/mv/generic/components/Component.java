package com.toppatch.mv.generic.components;

import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

public abstract class Component {

	private static final String TAG = "Component";
	protected Context mContext;

	/**
	 * Getting the system service instance makes it faster.
	 * @param context
	 */
	public Component(Context context) {
		Log.d(TAG, "Generic Component");

		//TODO check the fucking permissions here and throw and exception in case the user don't have enough rights...
		mContext = context;
	}

	/**
	 * Only applications signed with the system firmware signing key can perform certain tasks like Reboot, PowerOff.
	 * Credits: CommonsWare (http://stackoverflow.com/questions/3745523/programmatically-switching-off-android-phone/3745548#3745548)
	 * 
	 * @param data
	 */
	public abstract void execute(JSONObject data);

}
