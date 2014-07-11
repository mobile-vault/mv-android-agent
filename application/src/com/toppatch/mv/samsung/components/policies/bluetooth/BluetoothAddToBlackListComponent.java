package com.toppatch.mv.samsung.components.policies.bluetooth;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.enterprise.BluetoothPolicy;
import android.app.enterprise.BluetoothSecureModePolicy;
import android.app.enterprise.BluetoothSecureModeWhitelistConfig;
import android.app.enterprise.EnterpriseDeviceManager;
import android.content.Context;
import android.util.Log;

import com.toppatch.mv.Constants;
import com.toppatch.mv.samsung.components.Component;

public class BluetoothAddToBlackListComponent extends Component {

	private static final String TAG = "BluetoothAddToBlackListComponent";

	public BluetoothAddToBlackListComponent(Context context) {
		super(context);
	}

	/**{
	 * "list":[
	 * 	{
	 *   "bluetooth_name": "NAME",
	 *   "bluetooth_cod": 1,
	 *   "bluetooth_uuid": "00001101-0000-1000-8000-00805f9b34fb",
	 *   "bluetooth_pairing": true,
	 *   "android": true,
	 *   "iOS": true
	 *  }
	 * ]
	 * }
	 */
	@Override
	public void execute(JSONObject data) {
		if(data!=null){
			JSONArray list = data.optJSONArray(Constants.BLUETOOTH_LIST);
			EnterpriseDeviceManager edm = getEdm();
			
			if(list!=null && edm!=null){
				ArrayList<String> blackList = new ArrayList<String>();
				for(int i=0;i<list.length();i++){
					JSONObject btItem = list.optJSONObject(i);
					if(btItem!=null){
						String uuid = btItem.optString(Constants.BLUETOOTH_UUID,null);
						blackList.add(uuid);
						Log.d(TAG, "Executing blacklisted:"+uuid);
					}
				}
				BluetoothPolicy bluetoothPolicy = edm.getBluetoothPolicy();
				bluetoothPolicy.activateBluetoothDeviceRestriction(true);
				bluetoothPolicy.removeBluetoothDevicesFromWhiteList(blackList);
				Boolean isBlackListed=bluetoothPolicy.addBluetoothDevicesToBlackList(blackList);
				Log.d(TAG, "Executing blacklisted succesful:"+blackList.toString()+"check:"+isBlackListed);
			}
		}
	}

}
