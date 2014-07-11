package com.toppatch.mv.samsung.components.policies.bluetooth;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.enterprise.BluetoothPolicy;
import android.app.enterprise.EnterpriseDeviceManager;
import android.content.Context;
import android.util.Log;

import com.toppatch.mv.Constants;
import com.toppatch.mv.samsung.components.Component;

public class BluetoothPowerStateComponent extends Component {

	private static final String TAG = "BluetoothPowerStateComponent";

	public BluetoothPowerStateComponent(Context context) {
		super(context);
	}

	@Override
	public void execute(JSONObject data) {
		if(data!=null){
			Log.d(TAG, "Executing "+data.toString());
			EnterpriseDeviceManager edm = getEdm();
			BluetoothPolicy bluetoothPolicy = edm.getBluetoothPolicy();
			try {
				boolean powerState = data.getBoolean(Constants.PLUG_BT_STATUS_POWER);
				if(edm!=null && bluetoothPolicy!=null){
					bluetoothPolicy.setDiscoverableState(powerState);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}
}