package com.toppatch.mv.generic.components;

import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

public class PowerOffComponent extends Component {

	private static final String TAG = "PowerOffComponent";

	public PowerOffComponent(Context context) {
		super(context);
	}

	/**
	 * Works well on rooted devices, 
	 * TODO find a way for devices that are not rooted.
	 */
	@Override
	public void execute(JSONObject data) {
		try {
			Process proc = Runtime.getRuntime().exec(new String[] { "su", "-c", "reboot -p" });
			proc.waitFor();
		} catch (Exception e) {
			Log.e(TAG, "Unable to Shutdown", e);
		}
	}
}