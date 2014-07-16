package com.toppatch.mv.generic.components.policies;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

import com.toppatch.mv.Constants;
import com.toppatch.mv.samsung.components.Component;
import com.toppatch.mv.samsung.components.policies.access.AllowFactoryResetComponent;
import com.toppatch.mv.samsung.components.policies.access.AllowRemoveAdminComponent;
import com.toppatch.mv.samsung.components.policies.access.AllowScreenShotComponent;
import com.toppatch.mv.samsung.components.policies.access.ChangeSettingsComponent;
import com.toppatch.mv.samsung.components.policies.access.USBDebuggingComponent;
import com.toppatch.mv.samsung.components.policies.application.AllowAppInstallComponent;
import com.toppatch.mv.samsung.components.policies.application.AndroidMarketComponent;
import com.toppatch.mv.samsung.components.policies.application.BlackListApplicationComponent;
import com.toppatch.mv.samsung.components.policies.application.InstallApplicationComponent;
import com.toppatch.mv.samsung.components.policies.application.UninstallApplicationComponent;
import com.toppatch.mv.samsung.components.policies.application.YoutubeComponent;
import com.toppatch.mv.samsung.components.policies.bluetooth.BluetoothAddToBlackListComponent;
import com.toppatch.mv.samsung.components.policies.bluetooth.BluetoothAddToWhiteListComponent;
import com.toppatch.mv.samsung.components.policies.bluetooth.BluetoothPowerStateComponent;
import com.toppatch.mv.samsung.components.policies.bluetooth.BluetoothStateComponent;
import com.toppatch.mv.samsung.components.policies.browser.AutoFillComponent;
import com.toppatch.mv.samsung.components.policies.browser.CookiesComponent;
import com.toppatch.mv.samsung.components.policies.browser.ForceFraudWarningComponent;
import com.toppatch.mv.samsung.components.policies.browser.HttpProxyComponent;
import com.toppatch.mv.samsung.components.policies.browser.JavascriptComponent;
import com.toppatch.mv.samsung.components.policies.browser.PopupComponent;
import com.toppatch.mv.samsung.components.policies.misc.CameraStateComponent;
import com.toppatch.mv.samsung.components.policies.misc.ExternalStorageEncryptionComponent;
import com.toppatch.mv.samsung.components.policies.misc.InternalStorageEncryptionComponent;
import com.toppatch.mv.samsung.components.policies.restriction.AndroidBeamComponent;
import com.toppatch.mv.samsung.components.policies.restriction.AudioRecordComponent;
import com.toppatch.mv.samsung.components.policies.restriction.BackgroundDataComponent;
import com.toppatch.mv.samsung.components.policies.restriction.BackupComponent;
import com.toppatch.mv.samsung.components.policies.restriction.ClipboardComponent;
import com.toppatch.mv.samsung.components.policies.restriction.GoogleCrashReportComponent;
import com.toppatch.mv.samsung.components.policies.restriction.MicrophoneStateComponent;
import com.toppatch.mv.samsung.components.policies.restriction.WiFiStateComponent;
import com.toppatch.mv.samsung.components.policies.roaming.RoamingDataComponent;
import com.toppatch.mv.samsung.components.policies.roaming.RoamingPushComponent;
import com.toppatch.mv.samsung.components.policies.roaming.RoamingSyncComponent;
import com.toppatch.mv.samsung.components.policies.roaming.RoamingVoiceCallsComponent;
import com.toppatch.mv.samsung.components.policies.vpn.VPNAddComponent;
import com.toppatch.mv.samsung.components.policies.wifi.WifiAddComponent;

public class PolicyControllerComponent extends Component {

	private static final String TAG = "PolicyControllerComponent";

	private Context context;

	public PolicyControllerComponent(Context context) {
		super(context);
		this.context = context;
	}

	@Override
	public void execute(JSONObject data) {
		// Implement the application plugin...
		JSONObject application = data.optJSONObject("application");
		if (application != null) {

			JSONObject playstoreEnable = application
					.optJSONObject("playstore_enable");
			ExecutionHelper.executePlayStoreEnable(context, playstoreEnable);

			JSONObject enableRecording = application
					.optJSONObject("enable_recording");
			ExecutionHelper.executeEnableRecording(context, enableRecording);

			JSONObject browserSettings = application
					.optJSONObject("browser_settings");
			ExecutionHelper.executeBrowserSettings(context, browserSettings);

			JSONArray installedApps = application
					.optJSONArray("installed_apps");
			ExecutionHelper.executeInstalledApps(context, installedApps);

			JSONArray removedApps = application.optJSONArray("removed_apps");
			ExecutionHelper.executeRemovedApps(context, removedApps);

			JSONArray blackListedApps = application
					.optJSONArray("blacklisted_apps");
			ExecutionHelper.executeBlackListedApps(context, blackListedApps);

			JSONObject youTubeEnable = application
					.optJSONObject("youtube_enable");
			ExecutionHelper.executeYouTubeEnable(context, youTubeEnable);

			JSONObject allowAppInstall = application
					.optJSONObject("allow_app_installation");
			ExecutionHelper.executeAllowAppInstall(context, allowAppInstall);
			/*
			 * Camera External Storage Encryption Internal Storage Encryption
			 * Microphone state Android Beam
			 */
		} else {
			Log.d(TAG, "Application policies not sent");
		}

		JSONObject hardware = data.optJSONObject("hardware");
		if (hardware != null)
			Log.d(TAG, "Got hardware as " + hardware.toString());
		if (hardware != null) {

			JSONObject cameraEnable = hardware
					.optJSONObject(Constants.PLUG_HARDWARE_CAMERA);
			ExecutionHelper.executeCameraEnable(context, cameraEnable);

			JSONObject externalStorageEncryptionEnable = hardware
					.optJSONObject(Constants.PLUG_HARDWARE_EXTERNAL_STORAGE_ENCRYPTION);
			ExecutionHelper.executeExternalStorageEncryptionEnable(context,
					externalStorageEncryptionEnable);

			JSONObject internalStorageEncryptionEnable = hardware
					.optJSONObject(Constants.PLUG_HARDWARE_INTERNAL_STORAGE_ENCRYPTION);
			ExecutionHelper.executeInternalStorageEncryption(context,
					internalStorageEncryptionEnable);

			JSONObject microphoneEnable = hardware
					.optJSONObject(Constants.PLUG_HARDWARE_MIC_STATE);
			ExecutionHelper.executeMicrophoneState(context, microphoneEnable);

			JSONObject androidBeamEnable = hardware
					.optJSONObject(Constants.PLUG_HARDWARE_ANDROID_BEAM);
			ExecutionHelper.executeAndroidBeam(context, androidBeamEnable);
		} else {
			Log.d(TAG, "Hardware block sent empty");
		}

		JSONObject settings = data.optJSONObject(Constants.PLUG_SETTINGS);
		if (settings != null) {
			JSONObject backgroundDataEnable = settings
					.optJSONObject(Constants.PLUG_SETTINGS_BACKGROUND_DATA);
			ExecutionHelper.executeBackgroundEnable(context,
					backgroundDataEnable);

			JSONObject wifiEnable = settings
					.optJSONObject(Constants.PLUG_SETTINGS_WIFI);
			ExecutionHelper.executeWifiEnable(context, wifiEnable);

			JSONObject backupEnable = settings
					.optJSONObject(Constants.PLUG_SETTINGS_BACKUP);
			ExecutionHelper.executeBackupEnable(context, backupEnable);

			JSONObject clipboardEnable = settings
					.optJSONObject(Constants.PLUG_SETTINGS_CLIPBOARD);
			ExecutionHelper.executeClipboardEnable(context, clipboardEnable);

			JSONObject googleCrashReportEnable = settings
					.optJSONObject(Constants.PLUG_SETTINGS_GOOGLE_CRASH_REPORT);
			ExecutionHelper.executeGoogleCrashReportEnable(context,
					googleCrashReportEnable);

			JSONObject dataWhileRoamingEnable = settings
					.optJSONObject(Constants.PLUG_SETTINGS_DATA_WHILE_ROAMING);
			ExecutionHelper.executeDataWhileRoamingEnable(context,
					dataWhileRoamingEnable);

			JSONObject pushWhileRoamingEnable = settings
					.optJSONObject(Constants.PLUG_SETTINGS_PUSH_WHILE_ROAMING);
			ExecutionHelper.executePushWhileRoamingEnable(context,
					pushWhileRoamingEnable);

			JSONObject syncWhileRoamingEnable = settings
					.optJSONObject(Constants.PLUG_SETTINGS_SYNC_WHILE_ROAMING);
			ExecutionHelper.executeSyncWhileRoamingEnable(context,
					syncWhileRoamingEnable);

			JSONObject voiceCallsWhileRoamingEnable = settings
					.optJSONObject(Constants.PLUG_SETTINGS_VOICE_CALLS_WHILE_ROAMING);
			ExecutionHelper.executeVoiceCallsWhileRoamingEnable(context,
					voiceCallsWhileRoamingEnable);
		} else {
			Log.d(TAG, "Settings block sent empty");
		}

		JSONObject wifi = data.optJSONObject(Constants.PLUG_WIFI);
		if (wifi != null) {
			new WifiAddComponent(context).execute(wifi);
		} else {
			Log.d(TAG, "No wifi data sent");
		}

		JSONObject vpn = data.optJSONObject(Constants.PLUG_VPN);
		if (vpn != null) {
			new VPNAddComponent(context).execute(vpn);
		} else {
			Log.d(TAG, "No VPN information sent");
		}

		JSONObject access = data.optJSONObject(Constants.PLUG_ACCESS);
		if (access != null) {
			try {
				JSONObject changeSettingsEnable = access
						.getJSONObject(Constants.PLUG_ACCESS_CHANGE_SETTINGS);
				new ChangeSettingsComponent(context)
				.execute(changeSettingsEnable);
			} catch (JSONException e) {
				Log.d(TAG, "No change settings sent "
						+ Constants.PLUG_ACCESS_CHANGE_SETTINGS);
			}

			try {
				JSONObject screenShotEnable = access
						.getJSONObject(Constants.PLUG_ACCESS_CAPTURE_ENABLE);
				new AllowScreenShotComponent(context).execute(screenShotEnable);
			} catch (JSONException e) {
				Log.d(TAG, "No " + Constants.PLUG_ACCESS_CAPTURE_ENABLE
						+ " sent");
			}

			try {
				JSONObject changeAdminEnable = access
						.getJSONObject(Constants.PLUG_ACCESS_CHANGE_ADMIN);
				new AllowRemoveAdminComponent(context)
				.execute(changeAdminEnable);
			} catch (JSONException e) {
				Log.d(TAG, "No " + Constants.PLUG_ACCESS_CHANGE_ADMIN + " sent");
			}

			try {
				JSONObject usbDebuggingEnable = access
						.getJSONObject(Constants.PLUG_ACCESS_ENABLE_USB);
				new USBDebuggingComponent(context).execute(usbDebuggingEnable);
			} catch (JSONException e) {
				Log.d(TAG, "No " + Constants.PLUG_ACCESS_ENABLE_USB + " sent");
			}

			try {
				JSONObject factoryResetEnable = access
						.getJSONObject(Constants.PLUG_ACCESS_FACTORY_RESET);
				new AllowFactoryResetComponent(context)
				.execute(factoryResetEnable);
			} catch (JSONException e) {
				Log.d(TAG, "No " + Constants.PLUG_ACCESS_FACTORY_RESET + " sent");
			}
		}

		JSONObject bluetooth = data.optJSONObject(Constants.PLUG_BT);
		if (bluetooth != null) {
			try {
				JSONObject bluetoothStatus = bluetooth
						.getJSONObject(Constants.PLUG_BT_STATUS);
				new BluetoothPowerStateComponent(context)
				.execute(bluetoothStatus);
				new BluetoothStateComponent(context).execute(bluetoothStatus);
			} catch (JSONException e) {
				Log.d(TAG, "No enable settings sent "
						+ Constants.PLUG_BT_STATUS);
			}

			try {
				JSONArray blackListedPairing = bluetooth
						.getJSONArray(Constants.PLUG_BT_BLACK_LISTED);
				JSONObject jsonObject = new JSONObject();
				jsonObject.put(Constants.BLUETOOTH_LIST, blackListedPairing);
				new BluetoothAddToBlackListComponent(context)
				.execute(jsonObject);
			} catch (JSONException e) {
				Log.d(TAG, "No blackList pairings " + Constants.PLUG_BT_BLACK_LISTED);
			}
			try {
				JSONArray whiteListedPairing = bluetooth
						.getJSONArray(Constants.PLUG_BT_WHITE_LISTED);
				JSONObject jsonObject = new JSONObject();
				jsonObject.put(Constants.BLUETOOTH_LIST, whiteListedPairing);
				new BluetoothAddToWhiteListComponent(context)
				.execute(jsonObject);
			} catch (JSONException e) {
				Log.d(TAG, "No whiteList pairings " + Constants.PLUG_BT_WHITE_LISTED);

			}
		}
	}

}

class ExecutionHelper {

	private static final String TAG = "ExecutionHelper";

	public static void executePlayStoreEnable(Context context,
			JSONObject playStore) {
		if (playStore != null) {
			new AndroidMarketComponent(context).execute(playStore);
		} else {
			Log.d(TAG, "Play store enable not sent");
		}
	}

	public static void executeYouTubeEnable(Context context, JSONObject youtube) {
		if (youtube != null) {
			new YoutubeComponent(context).execute(youtube);
		} else {
			Log.d(TAG, "Enable youtube not sent");
		}
	}

	public static void executeAllowAppInstall(Context context,
			JSONObject appInstall) {
		if (appInstall != null) {
			new AllowAppInstallComponent(context).execute(appInstall);
		} else {
			Log.d(TAG, "Enable youtube not sent");
		}
	}

	public static void executeEnableRecording(Context context,
			JSONObject recording) {
		if (recording != null) {
			new AudioRecordComponent(context).execute(recording);
		} else {
			Log.d(TAG, "Enable recording not sent");
		}
	}

	public static void executeBrowserSettings(Context context,
			JSONObject browser) {
		if (browser != null) {
			try {
				boolean enableAutofill = browser.getBoolean("enable_autofill");
				JSONObject jsonObject = new JSONObject();
				jsonObject.put(Constants.BROWSER_AUTOFILL_ENABLE,
						enableAutofill);
				new AutoFillComponent(context).execute(jsonObject);
			} catch (JSONException e) {
				e.printStackTrace();
			}

			try {
				boolean enableJavaScript = browser
						.getBoolean("enable_javascript");
				JSONObject jsonObject = new JSONObject();
				jsonObject.put(Constants.BROWSER_JAVASCRIPT_ENABLE,
						enableJavaScript);
				new JavascriptComponent(context).execute(jsonObject);
			} catch (JSONException e) {
				e.printStackTrace();
			}

			try {
				boolean enableCookies = browser.getBoolean("enable_cookies");
				JSONObject jsonObject = new JSONObject();
				jsonObject.put(Constants.BROWSER_COOKIES_ENABLE, enableCookies);
				new CookiesComponent(context).execute(jsonObject);
			} catch (JSONException e) {
				e.printStackTrace();
			}

			try {
				boolean enablePopUps = browser.getBoolean("enable_popups");
				JSONObject jsonObject = new JSONObject();
				jsonObject.put(Constants.BROWSER_POPUP_ENABLE, enablePopUps);
				new PopupComponent(context).execute(jsonObject);
			} catch (JSONException e) {
				e.printStackTrace();
			}

			try {
				boolean forceFraudWarnings = browser
						.getBoolean("force_fraud_warnings");
				JSONObject jsonObject = new JSONObject();
				jsonObject.put(Constants.BROWSER_FORCE_FRAUD_WARNING_ENABLE,
						forceFraudWarnings);
				new ForceFraudWarningComponent(context).execute(jsonObject);
			} catch (JSONException e) {
				e.printStackTrace();
			}

			try {
				boolean enableHttpProxy = browser
						.getBoolean("enable_http_proxy");
				String proxyValue = null;
				if (enableHttpProxy) {
					proxyValue = browser.getString("http_proxy_value");
				} else {
					proxyValue = null;
				}
				JSONObject jsonObject = new JSONObject();
				jsonObject.put(Constants.BROWSER_HTTP_PROXY, proxyValue);
				new HttpProxyComponent(context).execute(jsonObject);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}

	public static void executeInstalledApps(Context context, JSONArray apps) {
		if (apps != null && apps.length() > 0) {
			JSONObject jsonObject = new JSONObject();
			try {
				jsonObject.put(Constants.APP_ANDROID_INSTALL, apps.toString());
				new InstallApplicationComponent(context).execute(jsonObject);
			} catch (JSONException e) {
				e.printStackTrace();
			}

		}
		Log.e(TAG, apps.toString());
	}

	public static void executeRemovedApps(Context context, JSONArray apps) {
		if (apps != null && apps.length() > 0) {
			Log.e(TAG, apps.toString());
			JSONObject jsonObject = new JSONObject();
			try {
				jsonObject
				.put(Constants.APP_ANDROID_UNINSTALL, apps.toString());
				new UninstallApplicationComponent(context).execute(jsonObject);
			} catch (JSONException e) {
				e.printStackTrace();
			}

		}
	}

	public static void executeBlackListedApps(Context context, JSONArray apps) {
		if (apps != null && apps.length() > 0) {
			Log.e(TAG, apps.toString());
			JSONObject job = new JSONObject();
			try {
				job.put(Constants.APP_ANDROID_BLACK_LIST, apps.toString());
				new BlackListApplicationComponent(context).execute(job);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}

	public static void executeCameraEnable(Context context, JSONObject obj) {
		if (obj != null) {
			new CameraStateComponent(context).execute(obj);
		} else {
			Log.d(TAG, "No camera enable command sent");
		}
	}

	public static void executeExternalStorageEncryptionEnable(Context context,
			JSONObject obj) {
		if (obj != null) {
			new ExternalStorageEncryptionComponent(context).execute(obj);
		} else {
			Log.d(TAG, "No external storage encryption sent");
		}
	}

	public static void executeInternalStorageEncryption(Context context,
			JSONObject obj) {
		if (obj != null) {
			new InternalStorageEncryptionComponent(context).execute(obj);
		} else {
			Log.d(TAG, "No internal storage encryption sent");
		}
	}

	public static void executeMicrophoneState(Context context, JSONObject obj) {
		if (obj != null) {
			new MicrophoneStateComponent(context).execute(obj);
		} else {
			Log.d(TAG, "No microphone state sent");
		}
	}

	public static void executeAndroidBeam(Context context, JSONObject obj) {
		if (obj != null) {
			new AndroidBeamComponent(context).execute(obj);
		} else {
			Log.d(TAG, "No android beam message sent");
		}
	}

	public static void executeBackgroundEnable(Context context, JSONObject obj) {
		String fun = "executeBackgroundEnable";
		if (obj != null) {
			new BackgroundDataComponent(context).execute(obj);
		} else {
			Log.d(TAG, "No " + fun + " sent");
		}
	}

	public static void executeWifiEnable(Context context, JSONObject obj) {
		String fun = "executeWifiEnable";
		if (obj != null) {
			new WiFiStateComponent(context).execute(obj);
		} else {
			Log.d(TAG, "No " + fun + " sent");
		}
	}

	public static void executeBackupEnable(Context context, JSONObject obj) {
		String fun = "executeBackgroundEnable";
		if (obj != null) {
			new BackupComponent(context).execute(obj);
		} else {
			Log.d(TAG, "No " + fun + " sent");
		}
	}

	public static void executeClipboardEnable(Context context, JSONObject obj) {
		String fun = "executeClipboardEnable";
		if (obj != null) {
			new ClipboardComponent(context).execute(obj);
		} else {
			Log.d(TAG, "No " + fun + " sent");
		}
	}

	public static void executeGoogleCrashReportEnable(Context context,
			JSONObject obj) {
		String fun = "executeGoogleCrashReportEnable";
		if (obj != null) {
			new GoogleCrashReportComponent(context).execute(obj);
		} else {
			Log.d(TAG, "No " + fun + " sent");
		}
	}

	public static void executeDataWhileRoamingEnable(Context context,
			JSONObject obj) {
		String fun = "executeDataWhileRoamingEnable";
		if (obj != null) {
			new RoamingDataComponent(context).execute(obj);
		} else {
			Log.d(TAG, "No " + fun + " sent");
		}
	}

	public static void executePushWhileRoamingEnable(Context context,
			JSONObject obj) {
		String fun = "executePushWhileRoamingEnable";
		if (obj != null) {
			new RoamingPushComponent(context).execute(obj);
		} else {
			Log.d(TAG, "No " + fun + " sent");
		}
	}

	public static void executeSyncWhileRoamingEnable(Context context,
			JSONObject obj) {
		String fun = "executeSyncWhileRoamingEnable";
		if (obj != null) {
			new RoamingSyncComponent(context).execute(obj);
		} else {
			Log.d(TAG, "No " + fun + " sent");
		}
	}

	public static void executeVoiceCallsWhileRoamingEnable(Context context,
			JSONObject obj) {
		String fun = "executeVoiceCallsWhileRoamingEnable";
		if (obj != null) {
			new RoamingVoiceCallsComponent(context).execute(obj);
		} else {
			Log.d(TAG, "No " + fun + " sent");
		}
	}
}
