package com.toppatch.mv.samsung.components;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.enterprise.EnterpriseDeviceManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Environment;
import android.os.StatFs;
import android.util.Log;

import com.toppatch.mv.Config;
import com.toppatch.mv.Constants;
import com.toppatch.mv.ReportServer;
import com.toppatch.mv.helper.AdminHelper;

public class DeviceInformationComponent extends Component {


	private static final String TAG = "DeviceInformationComponent";
	private Context mContext;

	public DeviceInformationComponent(Context context) {
		super(context);
		mContext = context;
	}

	/**
	 * {
	 * 	"uuid": "",
	 * 	"callback": "url"
	 * 	"fields": ["","",""]
	 * }
	 */
	@Override
	public void execute(JSONObject data) {
		String uuid = data.optString(Constants.DEVICE_INFO_UUID,null);
		Log.i(TAG, "uuid:"+uuid);
		String callbackUrl = data.optString(Constants.DEVICE_INFO_CALLBACK,null);
		JSONArray fields = data.optJSONArray(Constants.DEVICE_INFO_FIELDS);
		JSONObject response = null;
		try{
			if(uuid==null || callbackUrl==null){
				response = new JSONObject();
				EnterpriseDeviceManager edm = getEdm();
				if(edm!=null){
					try{
						//						if( fields!=null && fields.length()==0){
						String deviceMaker = edm.getDeviceInventory().getDeviceMaker();
						String deviceName = edm.getDeviceInventory().getDeviceName();
						String deviceOS = edm.getDeviceInventory().getDeviceOS();
						String deviceOSVersion = edm.getDeviceInventory().getDeviceOSVersion();
						String devicePlatform = edm.getDeviceInventory().getDevicePlatform();
						String deviceProcessorSpeed = edm.getDeviceInventory().getDeviceProcessorSpeed();
						String deviceProcessorType = edm.getDeviceInventory().getDeviceProcessorType();
						double deviceBatteryPercent = getCurrentBatteryPercent();
						long totalMemory = getTotalMemory();
						long freeMemory = getFreeMemory();
						long busyMemory = getBusyMemory();

						if(deviceMaker!=null)response.put(Constants.DEVICE_INFO_FIELD_DEVICE_MAKER, deviceMaker);
						if(deviceName!=null)response.put(Constants.DEVICE_INFO_FIELD_DEVICE_NAME, deviceName);
						if(deviceOS!=null)response.put(Constants.DEVICE_INFO_FIELD_DEVICE_OS, deviceOS);
						if(deviceOSVersion!=null)response.put(Constants.DEVICE_INFO_FIELD_OS_VERSION, deviceOSVersion);
						if(devicePlatform!=null)response.put(Constants.DEVICE_INFO_FIELD_PLATFORM, devicePlatform);
						if(deviceProcessorSpeed!=null)response.put(Constants.DEVICE_INFO_FIELD_PROCESSOR_SPEED, deviceProcessorSpeed);
						if(deviceProcessorType!=null)response.put(Constants.DEVICE_INFO_FIELD_PROCESSOR_TYPE, deviceProcessorType);
						if(deviceBatteryPercent>=0)response.put(Constants.DEVICE_INFO_FIELD_BATTERY_PERCENT, deviceBatteryPercent);
						if(totalMemory>0) response.put(Constants.DEVICE_INFO_FIELD_TOTAL_MEMORY, totalMemory);
						if(freeMemory>=0) response.put(Constants.DEVICE_INFO_FIELD_FREE_MEMORY, freeMemory);
						if(busyMemory>=0) response.put(Constants.DEVICE_INFO_FIELD_BUSY_MEMORY, busyMemory);

						//						}
						//						else{
						//							for(int i=0;i<jar.length();i++){
						//								if(jar.getString(i).equals(Constants.DEVICE_INFO_FIELD_DEVICE_MAKER)){
						//									String deviceMaker = edm.getDeviceInventory().getDeviceMaker();
						//									if(deviceMaker!=null)response.put(Constants.DEVICE_INFO_FIELD_DEVICE_MAKER, deviceMaker);
						//								}else if(jar.getString(i).equals(Constants.DEVICE_INFO_FIELD_DEVICE_NAME)){
						//									String deviceName = edm.getDeviceInventory().getDeviceName();
						//									if(deviceName!=null)response.put(Constants.DEVICE_INFO_FIELD_DEVICE_NAME, deviceName);
						//								}else if(jar.getString(i).equals(Constants.DEVICE_INFO_FIELD_DEVICE_OS)){
						//									String deviceOS = edm.getDeviceInventory().getDeviceOS();
						//									if(deviceOS!=null)response.put(Constants.DEVICE_INFO_FIELD_DEVICE_OS, deviceOS);
						//								}else if(jar.getString(i).equals(Constants.DEVICE_INFO_FIELD_OS_VERSION)){
						//									String deviceOSVersion = edm.getDeviceInventory().getDeviceOSVersion();
						//									if(deviceOSVersion!=null)response.put(Constants.DEVICE_INFO_FIELD_OS_VERSION, deviceOSVersion);
						//								}else if(jar.getString(i).equals(Constants.DEVICE_INFO_FIELD_PLATFORM)){
						//									String devicePlatform = edm.getDeviceInventory().getDevicePlatform();
						//									if(devicePlatform!=null)response.put(Constants.DEVICE_INFO_FIELD_PLATFORM, devicePlatform);
						//								}else if(jar.getString(i).equals(Constants.DEVICE_INFO_FIELD_PROCESSOR_SPEED)){
						//									String deviceProcessorSpeed = edm.getDeviceInventory().getDeviceProcessorSpeed();
						//									if(deviceProcessorSpeed!=null)response.put(Constants.DEVICE_INFO_FIELD_PROCESSOR_SPEED, deviceProcessorSpeed);
						//								}else if(jar.getString(i).equals(Constants.DEVICE_INFO_FIELD_PROCESSOR_TYPE)){
						//									String deviceProcessorType = edm.getDeviceInventory().getDeviceProcessorType();
						//									if(deviceProcessorType!=null)response.put(Constants.DEVICE_INFO_FIELD_PROCESSOR_TYPE, deviceProcessorType);
						//								}
						//							}
						//						}
					}catch(Exception e){
						e.printStackTrace();
					}
				}
			}else{
				ReportServer.e("Received uuid = "+uuid+" And callback as "+callbackUrl+" Unable to process");
			}
		}catch(Exception e){
			ReportServer.e(TAG+' '+e.toString());
		}finally{
			ReportServer reportServer=new ReportServer();
			reportServer.new SendResultThread(mContext,uuid,null,response);
		}
	}

	private double getCurrentBatteryPercent(){
		if(mContext!=null){
			IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
			Intent batteryStatus = mContext.registerReceiver(null, ifilter);
			int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
			int scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
			double batteryPct = level / (float)scale;			
			return batteryPct;
		}else{
			return  -1;
		}
	}

	/*************************************************************************************************
	Returns size in MegaBytes.

	If you need calculate external memory, change this: 
	    StatFs statFs = new StatFs(Environment.getRootDirectory().getAbsolutePath());
	to this: 
	    StatFs statFs = new StatFs(Environment.getExternalStorageDirectory().getAbsolutePath());  

	 Copied from: http://stackoverflow.com/a/8826357/1234007
	 **************************************************************************************************/
	private long getTotalMemory()
	{
		StatFs statFs = new StatFs(Environment.getRootDirectory().getAbsolutePath());   
		long Total = (statFs.getBlockCount() * statFs.getBlockSize()) / 1048576;
		return Total;
	}

	private long getFreeMemory()
	{
		StatFs statFs = new StatFs(Environment.getRootDirectory().getAbsolutePath());
		long Free  = (statFs.getAvailableBlocks() * statFs.getBlockSize()) / 1048576;
		return Free;
	}

	private long getBusyMemory()
	{
		StatFs statFs = new StatFs(Environment.getRootDirectory().getAbsolutePath());   
		long Total = (statFs.getBlockCount() * statFs.getBlockSize()) / 1048576;
		long Free  = (statFs.getAvailableBlocks() * statFs.getBlockSize()) / 1048576;
		long Busy  = Total - Free;
		return Busy;
	}

	//TODO write code to send the result back to the server... this call may also include some uuid to detect
	/*private class SendResultThread extends Thread{

		public SendResultThread(String uuid,String callback,JSONObject args) {
			Log.d(TAG, "Sending "+args.toString());
			JSONObject jsonObject=new JSONObject();
			try {
				Log.i(TAG, "sendig uuid:"+uuid);
				jsonObject.put("command_uuid",uuid);
				jsonObject.put("gcm_id",AdminHelper.getGCMId(mContext));
				jsonObject.put("result",args);
				Log.i(TAG, "sending device info:"+jsonObject.toString());
				HttpClient client = new DefaultHttpClient();
				HttpPut put = new HttpPut(Config.BASE_URL+"/samsung/save");
				StringEntity entity = new StringEntity(jsonObject.toString());
				entity.setContentType("application/json;charset=UTF-8");//text/plain;charset=UTF-8
				entity.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE,"application/json;charset=UTF-8"));
				put.setEntity(entity); 
				client.execute(put);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Log.i(TAG, "error sending device info:"+jsonObject.toString());
			}
		}

		@Override
		public void run() {

		}
	}*/
}
