package com.toppatch.mv.generic.components;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Environment;
import android.os.StatFs;
import android.util.Log;

import com.toppatch.mv.Constants;
import com.toppatch.mv.ReportServer;

public class DeviceInformationComponent extends Component {

	private static final String TAG = "DeviceInformationComponent";

	private Context mContext;

	public DeviceInformationComponent(Context context) {
		super(context);
		mContext = context;
	}

	/**
	 * { "uuid": "", "callback": "url" "fields": ["","",""] }
	 */
	@Override
	public void execute(JSONObject data) {
		String uuid = data.optString(Constants.DEVICE_INFO_UUID, null);
		Log.i(TAG, "uuid:" + uuid);
		String callbackUrl = data.optString(Constants.DEVICE_INFO_CALLBACK,
				null);
		JSONObject response = null;

		try {
			if (uuid == null || callbackUrl == null) {
				response = new JSONObject();

				String deviceMaker = getDeviceMaker();
				String deviceName = getDeviceName();

				String deviceOS = getDeviceOS();
				String deviceOSVersion = getDeviceOSVersion();
				String devicePlatform = getDevicePlatform();

				String deviceProcessorSpeed = getDeviceClockSpeed();
				String deviceProcessorType = getDeviceProcessorType();

				double deviceBatteryPercent = getCurrentBatteryPercent();

				long totalMemory = getTotalMemory();
				long freeMemory = getFreeMemory();
				long busyMemory = getBusyMemory();

				if (deviceMaker != null)
					response.put(Constants.DEVICE_INFO_FIELD_DEVICE_MAKER,
							getDeviceMaker());
				if (deviceName != null)
					response.put(Constants.DEVICE_INFO_FIELD_DEVICE_NAME,
							deviceName);
				if (deviceOS != null)
					response.put(Constants.DEVICE_INFO_FIELD_DEVICE_OS,
							deviceOS);
				if (deviceOSVersion != null)
					response.put(Constants.DEVICE_INFO_FIELD_OS_VERSION,
							deviceOSVersion);
				if (devicePlatform != null)
					response.put(Constants.DEVICE_INFO_FIELD_PLATFORM,
							devicePlatform);
				if (deviceProcessorSpeed != null)
					response.put(Constants.DEVICE_INFO_FIELD_PROCESSOR_SPEED,
							deviceProcessorSpeed);
				if (deviceProcessorType != null)
					response.put(Constants.DEVICE_INFO_FIELD_PROCESSOR_TYPE,
							deviceProcessorType);
				if (deviceBatteryPercent >= 0)
					response.put(Constants.DEVICE_INFO_FIELD_BATTERY_PERCENT,
							deviceBatteryPercent);
				if (totalMemory > 0)
					response.put(Constants.DEVICE_INFO_FIELD_TOTAL_MEMORY,
							totalMemory);
				if (freeMemory >= 0)
					response.put(Constants.DEVICE_INFO_FIELD_FREE_MEMORY,
							freeMemory);
				if (busyMemory >= 0)
					response.put(Constants.DEVICE_INFO_FIELD_BUSY_MEMORY,
							busyMemory);
			}
		} catch (Exception e) {
			ReportServer.e(TAG + ' ' + e.toString());
		} finally {
			ReportServer reportServer = new ReportServer();
			reportServer.new SendResultThread(mContext, uuid, null, response);
		}
	}

	protected String getDeviceMaker() {
		return android.os.Build.MANUFACTURER;
	}

	protected String getDeviceName() {
		return android.os.Build.MODEL;
	}

	protected String getDeviceOS() {
		return System.getProperty("os.name");
	}

	protected String getDeviceOSVersion() {
		return System.getProperty("os.version");
	}

	protected String getDevicePlatform() {
		return android.os.Build.VERSION.RELEASE;
	}

	protected String getDeviceProcessorType() {
		return System.getProperty("os.arch");
	}

	protected String getDeviceClockSpeed() {
		String clockSpeed = null;

		float fraction = 0;
		try {
			Process proc = Runtime.getRuntime()
					.exec("cat /sys/devices/system/cpu/cpu0/cpufreq/cpuinfo_max_freq");
			InputStream is = proc.getInputStream();
			clockSpeed = getStringFromInputStream(is);
			/*
			 * clock speed is unformatted, make it human-readable
			 */
			fraction = calculateFraction(
					Math.round(Float.parseFloat(clockSpeed)), 1000000);
		} catch (IOException e) {
			Log.e(TAG, e.getMessage(), e);
		}

		return Float.toString(fraction) + "GHz";
	}

	protected double getCurrentBatteryPercent() {
		if (mContext != null) {
			IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
			Intent batteryStatus = mContext.registerReceiver(null, ifilter);
			int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
			int scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
			Log.e(TAG, "Lev: " + level + " scale:" + scale);
			double batteryPct = level / (float) scale * 100;
			return batteryPct;
		} else {
			return -1;
		}
	}

	/**
	 * Returns size in MegaBytes.
	 * 
	 * If you need calculate external memory, change:<br/>
	 * StatFs statFs = new StatFs(Environment.getRootDirectory().getAbsolutePath());<br/> 
	 * to:<br/>
	 * StatFs statFs = new StatFs(Environment.getExternalStorageDirectory().getAbsolutePath());<br/>
	 * 
	 * Courtesy: http://stackoverflow.com/a/8826357/1234007
	 */
	protected long getTotalMemory() {
		StatFs statFs = new StatFs(Environment.getRootDirectory()
				.getAbsolutePath());
		long Total = (statFs.getBlockCount() * statFs.getBlockSize()) / 1048576;
		return Total;
	}

	protected long getFreeMemory() {
		StatFs statFs = new StatFs(Environment.getRootDirectory()
				.getAbsolutePath());
		long Free = (statFs.getAvailableBlocks() * statFs.getBlockSize()) / 1048576;
		return Free;
	}

	protected long getBusyMemory() {
		StatFs statFs = new StatFs(Environment.getRootDirectory()
				.getAbsolutePath());
		long Total = (statFs.getBlockCount() * statFs.getBlockSize()) / 1048576;
		long Free = (statFs.getAvailableBlocks() * statFs.getBlockSize()) / 1048576;
		long Busy = Total - Free;
		return Busy;
	}


	private String getStringFromInputStream(InputStream is) {
		StringBuilder sb = new StringBuilder();
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		String line = null;

		try {
			while ((line = br.readLine()) != null) {
				sb.append(line);
				sb.append("\n");
			}
		} catch (IOException e) {
			Log.e(TAG, e.getMessage(), e);
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					Log.e(TAG, e.getMessage(), e);
				}
			}
		}

		return sb.toString();
	}

	private float calculateFraction(long number, long divisor) {
		long truncate = (number * 10L + (divisor / 2L)) / divisor;
		float fraction = (float) truncate * 0.10F;
		return fraction;
	}
}
