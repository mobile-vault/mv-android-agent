package com.toppatch.mv.ui.activities;


import org.json.JSONArray;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.toppatch.mv.Constants;
import com.toppatch.mv.R;
import com.toppatch.mv.helper.NotificationHelper;
import com.toppatch.mv.samsung.Registration;
import com.toppatch.mv.samsung.components.LockDeviceComponent;
import com.toppatch.mv.samsung.components.NotificationComponent;
import com.toppatch.mv.samsung.components.PowerOffComponent;
import com.toppatch.mv.samsung.components.policies.access.AllowRemoveAdminComponent;
import com.toppatch.mv.samsung.components.policies.access.AllowScreenShotComponent;
import com.toppatch.mv.samsung.components.policies.access.ChangeSettingsComponent;
import com.toppatch.mv.samsung.components.policies.access.AllowFactoryResetComponent;
import com.toppatch.mv.samsung.components.policies.access.USBDebuggingComponent;
import com.toppatch.mv.samsung.components.policies.application.AndroidBrowserComponent;
import com.toppatch.mv.samsung.components.policies.application.AndroidMarketComponent;
import com.toppatch.mv.samsung.components.policies.application.VoiceDialerComponent;
import com.toppatch.mv.samsung.components.policies.application.YoutubeComponent;
import com.toppatch.mv.samsung.components.policies.browser.AutoFillComponent;
import com.toppatch.mv.samsung.components.policies.browser.CookiesComponent;
import com.toppatch.mv.samsung.components.policies.browser.ForceFraudWarningComponent;
import com.toppatch.mv.samsung.components.policies.browser.JavascriptComponent;
import com.toppatch.mv.samsung.components.policies.browser.PopupComponent;
import com.toppatch.mv.samsung.components.policies.restriction.BackgroundDataComponent;
import com.toppatch.mv.samsung.components.policies.restriction.BackupComponent;
import com.toppatch.mv.samsung.components.policies.restriction.BluetoothStateComponent;
import com.toppatch.mv.samsung.components.policies.restriction.BluetoothTetheringComponent;
import com.toppatch.mv.samsung.components.policies.restriction.CellularDataComponent;
import com.toppatch.mv.samsung.components.policies.restriction.ClipboardComponent;
import com.toppatch.mv.samsung.components.policies.restriction.HomeKeyStateComponent;
import com.toppatch.mv.samsung.components.policies.restriction.MicrophoneStateComponent;
import com.toppatch.mv.samsung.components.policies.restriction.MockLocationComponent;
import com.toppatch.mv.samsung.components.policies.restriction.NFCStateComponent;
import com.toppatch.mv.samsung.components.policies.restriction.NonMarketAppsComponent;
import com.toppatch.mv.samsung.components.policies.restriction.NonTrustedAppComponent;
import com.toppatch.mv.samsung.components.policies.restriction.ScreenCaptureComponent;
import com.toppatch.mv.samsung.components.policies.restriction.SdCardStateComponent;
import com.toppatch.mv.samsung.components.policies.restriction.TetheringComponent;
import com.toppatch.mv.samsung.components.policies.restriction.UsbDebuggingComponent;
import com.toppatch.mv.samsung.components.policies.restriction.WiFiStateComponent;
import com.toppatch.mv.samsung.components.policies.restriction.WifiTetheringComponent;
import com.toppatch.mv.samsung.components.policies.wifi.WifiAddComponent;
import com.toppatch.mv.samsung.components.policies.wifi.WifiRemoveComponent;

import android.app.Activity;
import android.app.PendingIntent;
import android.app.enterprise.EnterpriseDeviceManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;
import android.widget.ToggleButton;

public class TestActivity extends Activity implements OnClickListener {

	private static final String TAG = "TestActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_test);
		setup();
	}	

	private void setup(){
		findViewById(R.id.android_browser).setOnClickListener(this);
		findViewById(R.id.play_store).setOnClickListener(this);
		findViewById(R.id.youtube).setOnClickListener(this);
		findViewById(R.id.voice_dialer).setOnClickListener(this);
		findViewById(R.id.browser_autofill).setOnClickListener(this);
		findViewById(R.id.browser_cookies).setOnClickListener(this);
		findViewById(R.id.browser_fraud_warning).setOnClickListener(this);
		findViewById(R.id.browser_javascript).setOnClickListener(this);
		findViewById(R.id.browser_popup).setOnClickListener(this);
		findViewById(R.id.lock).setOnClickListener(this);
		findViewById(R.id.samsung).setOnClickListener(this);
		
		findViewById(R.id.roaming_data).setOnClickListener(this);
		findViewById(R.id.roaming_push).setOnClickListener(this);
		findViewById(R.id.roaming_sync).setOnClickListener(this);
		findViewById(R.id.roaming_voice).setOnClickListener(this);
		
		findViewById(R.id.rest_background_data).setOnClickListener(this);
		findViewById(R.id.rest_backup).setOnClickListener(this);
		findViewById(R.id.rest_bluetooth).setOnClickListener(this);
		findViewById(R.id.rest_bt_tethering).setOnClickListener(this);
		findViewById(R.id.rest_cellular_data).setOnClickListener(this);
		findViewById(R.id.rest_clipboard).setOnClickListener(this);
		findViewById(R.id.rest_debugging).setOnClickListener(this);
		findViewById(R.id.rest_home_key).setOnClickListener(this);
		findViewById(R.id.rest_mic).setOnClickListener(this);
		findViewById(R.id.rest_mocklocation).setOnClickListener(this);
		findViewById(R.id.rest_nfc).setOnClickListener(this);
		findViewById(R.id.rest_non_market).setOnClickListener(this);
		findViewById(R.id.rest_non_trusted).setOnClickListener(this);
		findViewById(R.id.rest_screen_capture).setOnClickListener(this);
		findViewById(R.id.rest_sdcard).setOnClickListener(this);
		findViewById(R.id.rest_tethering).setOnClickListener(this);
		findViewById(R.id.rest_wifi_state).setOnClickListener(this);
		findViewById(R.id.rest_wifi_tethering).setOnClickListener(this);
		
		findViewById(R.id.power_off).setOnClickListener(this);
		findViewById(R.id.power_off_intent).setOnClickListener(this);
		
		findViewById(R.id.add_wifi).setOnClickListener(this);
		findViewById(R.id.remove_wifi).setOnClickListener(this);
		
		findViewById(R.id.access_capture_screen).setOnClickListener(this);
		findViewById(R.id.access_change_settings).setOnClickListener(this);
		findViewById(R.id.access_factory_reset).setOnClickListener(this);
		findViewById(R.id.access_remove_admin).setOnClickListener(this);
		findViewById(R.id.access_usb_debugging).setOnClickListener(this);
		
		findViewById(R.id.bt_add_to_blacklist).setOnClickListener(this);
		findViewById(R.id.bt_add_to_whitelist).setOnClickListener(this);
		findViewById(R.id.bt_remove_from_blacklist).setOnClickListener(this);
		findViewById(R.id.bt_remove_from_whitelist).setOnClickListener(this);
		
		findViewById(R.id.notification).setOnClickListener(this);
		findViewById(R.id.notification_through_manager).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		try{
			JSONObject job = new JSONObject();
			boolean enable=false;
			try{
				enable =((ToggleButton)v).isChecked();
			}catch(Exception e){
				//Probably it wasn't a toggle button that was clicked. And we don't give a fuck. Carry on!
			}
			switch(v.getId()){
			case R.id.android_browser:
				AndroidBrowserComponent androidBrowserComponent = new AndroidBrowserComponent(getApplicationContext());
				job.put(Constants.APP_ANDROID_BROWSER_ENABLE, ((ToggleButton)v).isChecked());
				androidBrowserComponent.execute(job);
				break;
			case R.id.youtube:
				YoutubeComponent youtubeComponent = new YoutubeComponent(getApplicationContext());
				job.put(Constants.APP_YOUTUBE_ENABLE, ((ToggleButton)v).isChecked());
				youtubeComponent.execute(job);
				break;
			case R.id.voice_dialer:
				VoiceDialerComponent voiceDialerComponent = new VoiceDialerComponent(getApplicationContext());
				job.put(Constants.APP_VOICE_DIALER_ENABLE, ((ToggleButton)v).isChecked());
				voiceDialerComponent.execute(job);
				break;
			case R.id.play_store:
				AndroidMarketComponent androidMarketComponent = new AndroidMarketComponent(getApplicationContext());
				job.put(Constants.APP_ANDROID_MARKET_ENABLE, ((ToggleButton)v).isChecked());
				androidMarketComponent.execute(job);
				break;
			case R.id.browser_autofill:
				AutoFillComponent autoFillComponent = new AutoFillComponent(getApplicationContext());
				job.put(Constants.BROWSER_AUTOFILL_ENABLE, enable);
				autoFillComponent.execute(job);
				break;
			case R.id.browser_cookies:
				CookiesComponent cookiesComponent = new CookiesComponent(getApplicationContext());
				job.put(Constants.BROWSER_COOKIES_ENABLE, enable);
				cookiesComponent.execute(job);
				break;
			case R.id.browser_fraud_warning:
				ForceFraudWarningComponent fraudWarningComponent = new ForceFraudWarningComponent(getApplicationContext());
				job.put(Constants.BROWSER_FORCE_FRAUD_WARNING_ENABLE, enable);
				fraudWarningComponent.execute(job);
				break;
			case R.id.browser_javascript:
				JavascriptComponent javascriptComponent = new JavascriptComponent(getApplicationContext());
				job.put(Constants.BROWSER_JAVASCRIPT_ENABLE, enable);
				javascriptComponent.execute(job);
				break;
			case R.id.browser_popup:
				PopupComponent popupComponent = new PopupComponent(getApplicationContext());
				job.put(Constants.BROWSER_POPUP_ENABLE, enable);
				popupComponent.execute(job);
				break;
			case R.id.lock:
				LockDeviceComponent deviceComponent = new LockDeviceComponent(getApplicationContext());
				deviceComponent.execute(job);
				break;
			case R.id.samsung:
				Registration.register(getApplicationContext());
			case R.id.roaming_data:
				job.put(Constants.ROAMING_DATA_ENABLE, enable);
				Toast.makeText(getApplicationContext(), "Not implemented", Toast.LENGTH_LONG).show();
				break;
			case R.id.roaming_push:
				Toast.makeText(getApplicationContext(), "Not implemented", Toast.LENGTH_LONG).show();
				break;
			case R.id.roaming_sync:
				Toast.makeText(getApplicationContext(), "Not implemented", Toast.LENGTH_LONG).show();
				break;
			case R.id.roaming_voice:
				Toast.makeText(getApplicationContext(), "Not implemented", Toast.LENGTH_LONG).show();
				break;
			case R.id.rest_background_data:
				BackgroundDataComponent backgroundDataComponent = new BackgroundDataComponent(getApplicationContext());
				job.put(Constants.RESTRICTION_BACKGROUND_DATA_ENABLE, enable);
				backgroundDataComponent.execute(job);
				break;
			case R.id.rest_backup:
				BackupComponent backupComponent = new BackupComponent(getApplicationContext());
				job.put(Constants.RESTRICTION_BACKUP_ENABLE, enable);
				backupComponent.execute(job);
				break;
			case R.id.rest_bluetooth:
				BluetoothStateComponent bluetoothStateComponent = new BluetoothStateComponent(getApplicationContext());
				job.put(Constants.RESTRICTION_BLUETOOTH_STATE_ENABLE, enable);
				bluetoothStateComponent.execute(job);
				break;
			case R.id.rest_bt_tethering:
				BluetoothTetheringComponent bluetoothTetheringComponent = new BluetoothTetheringComponent(getApplicationContext());
				job.put(Constants.RESTRICTION_BLUETOOTH_TETHERING_ENABLE, enable);
				bluetoothTetheringComponent.execute(job);
				break;
			case R.id.rest_cellular_data:
				CellularDataComponent cellularDataComponent = new CellularDataComponent(getApplicationContext());
				job.put(Constants.RESTRICTION_CELLULAR_DATA_ENABLE, enable);
				cellularDataComponent.execute(job);
				break;
			case R.id.rest_clipboard:
				ClipboardComponent clipboardComponent = new ClipboardComponent(getApplicationContext());
				job.put(Constants.RESTRICTION_CLIPBOARD_ENABLE, enable);
				clipboardComponent.execute(job);
				break;
			case R.id.rest_debugging:
				UsbDebuggingComponent usbDebuggingComponent = new UsbDebuggingComponent(getApplicationContext());
				job.put(Constants.RESTRICTION_USB_DEBUGGING_ENABLE, enable);
				usbDebuggingComponent.execute(job);
				break;
			case R.id.rest_home_key:
				HomeKeyStateComponent homeKeyStateComponent = new HomeKeyStateComponent(getApplicationContext());
				job.put(Constants.RESTRICTION_HOME_KEY_ENABLE, enable);
				homeKeyStateComponent.execute(job);
				break;
			case R.id.rest_mic:
				MicrophoneStateComponent microphoneStateComponent = new MicrophoneStateComponent(getApplicationContext());
				job.put(Constants.RESTRICTION_MICROPHONE_ENABLE, enable);
				microphoneStateComponent.execute(job);
				break;
			case R.id.rest_mocklocation:
				MockLocationComponent mockLocationComponent = new MockLocationComponent(getApplicationContext());
				job.put(Constants.RESTRICTION_MOCK_LOCATION_ENABLE, enable);
				mockLocationComponent.execute(job);
				break;
			case R.id.rest_nfc:
				NFCStateComponent nfcStateComponent = new NFCStateComponent(getApplicationContext());
				job.put(Constants.RESTRICTION_NFC_STATE_ENABLE, enable);
				nfcStateComponent.execute(job);
				break;
			case R.id.rest_non_market:
				NonMarketAppsComponent nonMarketAppsComponent = new NonMarketAppsComponent(getApplicationContext());
				job.put(Constants.RESTRICTION_NON_MARKET_APPS_ENABLE, enable);
				nonMarketAppsComponent.execute(job);
				break;
			case R.id.rest_non_trusted:
				NonTrustedAppComponent nonTrustedAppComponent = new NonTrustedAppComponent(getApplicationContext());
				job.put(Constants.RESTRICTION_NON_TRUSTED_APP_ENABLE, enable);
				nonTrustedAppComponent.execute(job);
				break;
			case R.id.rest_screen_capture:
				ScreenCaptureComponent screenCaptureComponent = new ScreenCaptureComponent(getApplicationContext());
				job.put(Constants.RESTRICTION_SCREEN_CAPTURE_ENABLE, enable);
				screenCaptureComponent.execute(job);
				break;
			case R.id.rest_sdcard:
				SdCardStateComponent sdCardStateComponent = new SdCardStateComponent(getApplicationContext());
				job.put(Constants.RESTRICTION_SD_CARD_ENABLE, enable);
				sdCardStateComponent.execute(job);
				break;
			case R.id.rest_tethering:
				TetheringComponent tetheringComponent = new TetheringComponent(getApplicationContext());
				job.put(Constants.RESTRICTION_TETHERING_ENABLE, enable);
				tetheringComponent.execute(job);
				break;
			case R.id.rest_wifi_state:
				WiFiStateComponent wiFiStateComponent = new WiFiStateComponent(getApplicationContext());
				job.put(Constants.RESTRICTION_WIFI_STATE_ENABLE, enable);
				wiFiStateComponent.execute(job);
				break;
			case R.id.rest_wifi_tethering:
				WifiTetheringComponent wifiTetheringComponent = new WifiTetheringComponent(getApplicationContext());
				job.put(Constants.RESTRICTION_WIFI_TETHERING_ENABLE, enable);
				wifiTetheringComponent.execute(job);
				break;
			case R.id.power_off:
				PowerOffComponent powerOffComponent = new PowerOffComponent(getApplicationContext());
				powerOffComponent.execute(job);
				break;
			case R.id.power_off_intent:
				Intent power = new Intent(getApplicationContext(),Class.forName("com.example.mv_testplugin.MainActivity"));
				power.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				PendingIntent pi = PendingIntent.getActivity(getApplicationContext(), 0, power, 0);
				pi.send();
				break;
			case R.id.add_wifi:
				JSONArray wifiList1 = new JSONArray();
				JSONObject hugoMindsInternal1 = new JSONObject();
				hugoMindsInternal1.put(Constants.WIFI_SSID, "HugoMindsInternal");
				hugoMindsInternal1.put(Constants.WIFI_SECURITY, Constants.WIFI_SECURITY_WEP);
				hugoMindsInternal1.put(Constants.WIFI_KEY, "!HugoMinds@Tech!");
				wifiList1.put(hugoMindsInternal1);
				job.put(Constants.WIFI_LIST, wifiList1);
				WifiAddComponent wifiAddComponent = new WifiAddComponent(getApplicationContext());
				wifiAddComponent.execute(job);
				break;
			case R.id.remove_wifi:
				Log.e(TAG, "remove wifi");
				JSONArray wifiList2 = new JSONArray();
				JSONObject hugoMindsInternal2 = new JSONObject();
				hugoMindsInternal2.put(Constants.WIFI_SSID, "HugoMindsInternal");
				wifiList2.put(hugoMindsInternal2);
				job.put(Constants.WIFI_LIST, wifiList2);
				WifiRemoveComponent wifiRemoveComponent = new WifiRemoveComponent(getApplicationContext());
				wifiRemoveComponent.execute(job);
				break;
			case R.id.access_capture_screen:
				AllowScreenShotComponent screenShotComponent = new AllowScreenShotComponent(getApplicationContext());
				job.put(Constants.ACCESS_SCREENSHOT_ENABLE, enable);
				screenShotComponent.execute(job);
				break;
			case R.id.access_change_settings:
				ChangeSettingsComponent changeSettingsComponent = new ChangeSettingsComponent(getApplicationContext());
				job.put(Constants.ACCESS_CHANGE_SETTINGS_ENABLE, enable);
				changeSettingsComponent.execute(job);
				break;
			case R.id.access_factory_reset:
				AllowFactoryResetComponent allowFactoryResetComponent = new AllowFactoryResetComponent(getApplicationContext());
				job.put(Constants.ACCESS_FACTORY_RESET_ENABLE, enable);
				allowFactoryResetComponent.execute(job);
				break;
			case R.id.access_remove_admin:
				AllowRemoveAdminComponent allowRemoveAdminComponent = new AllowRemoveAdminComponent(getApplicationContext());
				job.put(Constants.ACCESS_CHANGE_ADMIN_ENABLE, enable);
				allowRemoveAdminComponent.execute(job);
				break;
			case R.id.access_usb_debugging:
				USBDebuggingComponent usbDebuggingComponent2 = new USBDebuggingComponent(getApplicationContext());
				job.put(Constants.ACCESS_USB_DEBUG_ENABLE, enable);
				usbDebuggingComponent2.execute(job);
				break;
			case R.id.bt_add_to_blacklist:
				break;
			case R.id.bt_add_to_whitelist:
				break;
			case R.id.bt_remove_from_blacklist:
				break;
			case R.id.bt_remove_from_whitelist:
				break;
			case R.id.notification:
				JSONObject notificationData = new JSONObject();
				notificationData.put(Constants.NOTIFICATION_MESSAGE, "Checkout our new website at www.codemymobile.com");
				notificationData.put(Constants.NOTIFICATION_TITLE, "CodeMyMobile");
				notificationData.put(Constants.NOTIFICATION_URL, "http://www.codemymobile.com");
				job.put(Constants.NOTIFICATION_DATA, notificationData);
				NotificationComponent notificationComponent = new NotificationComponent(getApplicationContext());
				notificationComponent.execute(job);
				break;
			case R.id.notification_through_manager:
				String message="Checkout our new website at www.codemymobile.com.Checkout our new website at www.codemymobile.com . Checkout our new website at www.codemymobile.com . Checkout our new website at www.codemymobile.com. Checkout our new website at www.codemymobile.com. Checkout our new website at www.codemymobile.com . Checkout our new website at www.codemymobile.com";
				String title = "CodeMyMobile.com";
//				String url = notification.optString(Constants.NOTIFICATION_URL,null);
				NotificationHelper.sendNotification(getApplicationContext(), title, message, null);
				break;
				
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}