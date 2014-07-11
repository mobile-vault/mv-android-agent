package com.toppatch.mv.ui.activities;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.app.admin.DevicePolicyManager;
import android.app.enterprise.license.EnterpriseLicenseManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.toppatch.mv.Config;
import com.toppatch.mv.Constants;
import com.toppatch.mv.Memory;
import com.toppatch.mv.R;
import com.toppatch.mv.gcm.GCMActivity;
import com.toppatch.mv.helper.AdminHelper;
import com.toppatch.mv.helper.WebHelper;
import com.toppatch.mv.samsung.SamsungReceiver;
import com.toppatch.mv.samsung.TopPatchAdmin;

public class LoginActivity2 extends Activity implements OnClickListener,OnEditorActionListener {

	private static final String TAG = "LoginActivity2";
	private static final int RESULT_ENABLE = 9876;
	private ProgressDialog progress;
	private EditText email,passcode;
	private Button submit;
	String gcmId;
	private final int GCM_REG = 1234;
	private SamsungReceiver actionReceiver;

	/**
	 * If we don't have GCM already registered, send it to GCMActivity first to get an id.
	 * Once we have the id, we signin with the same username passcode and registration id.
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		gcmId = AdminHelper.getGCMId(getApplicationContext());
		Log.d("see1", "11ssxxxs:"+gcmId);
		if(gcmId==null){
			Log.d("see", "ssxxxs:"+gcmId);
			Intent i = new Intent(getApplicationContext(), GCMActivity.class);
			gcmId = AdminHelper.getGCMId(getApplicationContext());
			Log.d("see1", "seeeehere"+gcmId);
			startActivityForResult(i, GCM_REG);
		}else{
			setupHandler.sendEmptyMessage(0);
		}
		
		
	}

	private Handler setupHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			setContentView(R.layout.activity_login);
			Animation animLogo = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.only_rotate);
			animLogo.start();
			/*ImageView imageView=(ImageView)findViewById(R.id.logoImage);
			imageView.setAnimation(animLogo);*/
			setupReferences();
			setupListeners();
		};
	};

	private void setupReferences(){
		email = (EditText) findViewById(R.id.email_address);
		passcode = (EditText) findViewById(R.id.password);
		submit = (Button) findViewById(R.id.submit);

		progress = new ProgressDialog(LoginActivity2.this);
		progress.setMessage("Checking Login... Please wait");
		progress.setCancelable(false);
	}

	private void setupListeners(){
		passcode.setOnEditorActionListener(this);
		submit.setOnClickListener(this);
	}

	@Override
	public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
		if(actionId ==EditorInfo.IME_ACTION_DONE){
			startLoginCheck();   
			Toast.makeText(getApplicationContext(), "Test Login", Toast.LENGTH_LONG).show();
			return true;
		}
		return false;
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.submit:
			startLoginCheck();
		}
	}

	@SuppressWarnings("unchecked")
	private void startLoginCheck(){
		Log.d(TAG, "startLoginCheck");
		CheckLoginAsyncTask check = new CheckLoginAsyncTask();
		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair(Constants.LOGIN_ACTIVITY_EMAIL, email.getText().toString()));
		params.add(new BasicNameValuePair(Constants.LOGIN_ACTIVITY_PASSCODE, passcode.getText().toString()));
		params.add(new BasicNameValuePair(Constants.LOGIN_ACTIVITY_GCM, gcmId));
		TelephonyManager mngr = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE); 
		params.add(new BasicNameValuePair(Constants.IMEI, mngr.getDeviceId()));
		Log.d("see", "sss:"+gcmId);
		check.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, params);
	}


	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Log.d(TAG, "onActivityResult Called");
		if(requestCode==GCM_REG){
			gcmId = AdminHelper.getGCMId(getApplicationContext());
			setupHandler.sendEmptyMessage(0);
		}else if(requestCode==RESULT_ENABLE){
			if(resultCode==RESULT_OK){
				//Toast.makeText(getApplicationContext(), "That's a good decision mate", Toast.LENGTH_LONG).show();
			}else{
				Toast.makeText(getApplicationContext(), "Bad decision. You will regret that 20 years down the line!", Toast.LENGTH_LONG).show();
			}
		}
	}

	private class CheckLoginAsyncTask extends AsyncTask<ArrayList<NameValuePair>, Void, Boolean>{
		public CheckLoginAsyncTask() {
			progress.show();
		}

		@Override
		protected Boolean doInBackground(ArrayList<NameValuePair>... params) {
			if(params.length>0){
				Log.d(TAG, "Downloading data from web");
				String response=WebHelper.sendPostRequest(Config.LOGIN_URL, params[0]);
				Log.d(TAG, "Response from server : "+response);
				//TODO
				JSONObject responseAsObject = null;
				if(response!=null){
					try {
						responseAsObject = new JSONObject(response);
						//pass, key, uuid and id in response.
						boolean pass = responseAsObject.optBoolean("pass",false);
						String key = responseAsObject.optString("key", null);
						if(pass){
							if(key!=null){
								Memory.setLicenceKey(key);
								return true;
							}else{
								Log.e(TAG, "Invalid response from server "+response);
								return false;
							}
						}else{
							//Toast.makeText(getApplicationContext(), "Invalid Email Passcode Combination", Toast.LENGTH_LONG).show();
							return false;
						}
					} catch (JSONException e) {
						//Toast.makeText(getApplicationContext(), "Invalid response from server. Please check your internet connection", Toast.LENGTH_LONG).show();
						e.printStackTrace();
						return false;
					}
				}
			}
			return false;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			progress.dismiss();
			if(result){
				Log.d(TAG, "License key="+Memory.getLicenceKey()); //TODO remove this key from here...
				AdminHelper.setUserLoggedIn(getApplicationContext());
				if(Config.debug){
					if(AdminHelper.isUserLoggedIn(getApplicationContext())){
						Log.d(TAG, "user is logged in now");
					}else{
						Log.d(TAG, "user is not logged in yet");
					}
				}
				enableAdmin();
			}else{
				//TODO Show an error messsage
				Toast.makeText(getApplicationContext(), "Invalid Combination. Please Try Again", Toast.LENGTH_LONG).show();
			}
		}
	}

	private void enableAdmin(){
		DevicePolicyManager mDPM = (DevicePolicyManager)getSystemService(Context.DEVICE_POLICY_SERVICE);
		ComponentName mDeviceAdmin = new ComponentName(this, TopPatchAdmin.class);
		if(mDPM!=null){
			try {
				
				//Add filter to listen to the choice made by the user to accept the terms or not.
				IntentFilter filter =new IntentFilter();
				filter.addAction(EnterpriseLicenseManager.ACTION_LICENSE_STATUS);
				actionReceiver = new SamsungReceiver();
				registerReceiver(actionReceiver, filter);
				
				Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
				intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, mDeviceAdmin);
				intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "Please Activate The Admin To Let Us Steal All Your Data And Sell It To Obama!");
				startActivityForResult(intent,RESULT_ENABLE);
			} catch (Exception e) {
				Log.w(TAG, "Exception: " + e);
			}
		}else{
			Log.e(TAG, "mDPM is null");
			//TODO device doesn't support MDM...report this shit.
		}
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if(actionReceiver!=null) 
			unregisterReceiver(actionReceiver);
	}
}
