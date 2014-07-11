package com.toppatch.mv.ui.activities;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import com.toppatch.mv.Config;
import com.toppatch.mv.Constants;
import com.toppatch.mv.Memory;
import com.toppatch.mv.R;
import com.toppatch.mv.R.id;
import com.toppatch.mv.R.layout;
import com.toppatch.mv.helper.AdminHelper;
import com.toppatch.mv.helper.WebHelper;
import com.toppatch.mv.samsung.Registration;
import com.toppatch.mv.samsung.TopPatchAdmin;

import android.app.Activity;
import android.app.ProgressDialog;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;


public class LoginActivity extends Activity implements OnClickListener,OnEditorActionListener{

	private ProgressDialog progress;
	private EditText email,passcode;
	private Button submit;
	private int REGISTER = 1000;
	static final int RESULT_ENABLE = 1;
	private static final int REQUEST_GCM=4;
	private DevicePolicyManager mDPM;
	private ComponentName mDeviceAdmin;
	private static final String TAG="LoginActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		setupReferences();
		setupListeners();
	}

	private void setupReferences(){
		email = (EditText) findViewById(R.id.email_address);
		passcode = (EditText) findViewById(R.id.password);
		submit = (Button) findViewById(R.id.submit);

		progress = new ProgressDialog(LoginActivity.this);
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
			return true;
		}
		return false;
	}


	private class CheckLoginAsyncTask extends AsyncTask<ArrayList<NameValuePair>, Void, Boolean>{

		public CheckLoginAsyncTask() {
			progress.show();
		}

		@Override
		protected Boolean doInBackground(ArrayList<NameValuePair>... params) {
			if(params.length>0){
				String response=WebHelper.sendPostRequest(Config.LOGIN_URL, params[0]);
				//TODO
				JSONObject responseAsObject = null;
				if(response!=null){
					try {
						responseAsObject = new JSONObject(response);
						//pass, key, uuid and id in response.
						boolean pass = responseAsObject.optBoolean("pass",false);
						String key = responseAsObject.optString("key", null);
						String uuid = responseAsObject.optString("uuid", null);
						String id = responseAsObject.optString("id",null);
						if(pass){
							if(key!=null && uuid!=null && id!=null){
								Memory.setLicenceKey(key);
								
							}else{
								Toast.makeText(getApplicationContext(), "Invalid response from server.", Toast.LENGTH_LONG).show();
							}
						}else{
							Toast.makeText(getApplicationContext(), "Invalid Email Passcode Combination", Toast.LENGTH_LONG).show();
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			}
			return true;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			progress.dismiss();
			if(result){
				AdminHelper.setUserLoggedIn(getApplicationContext());
				enableAdmin();
			}else{
				//TODO Show an error messsage
				Toast.makeText(getApplicationContext(), "Invalid Combination. Please Try Again", Toast.LENGTH_LONG).show();
			}
		}
	}

	@SuppressWarnings("unchecked")
	private void startLoginCheck(){
		CheckLoginAsyncTask check = new CheckLoginAsyncTask();
		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
		BasicNameValuePair paramEmail = new BasicNameValuePair(Constants.LOGIN_ACTIVITY_EMAIL, email.getText().toString());
		BasicNameValuePair paramPasscode = new BasicNameValuePair(Constants.LOGIN_ACTIVITY_PASSCODE, passcode.getText().toString());
		params.add(paramEmail);
		params.add(paramPasscode);
		check.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, params);
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.submit:
			startLoginCheck();
		}
	}

	private void enableAdmin(){
		mDPM = (DevicePolicyManager)getSystemService(Context.DEVICE_POLICY_SERVICE);
		mDeviceAdmin = new ComponentName(this, TopPatchAdmin.class);
		if(mDPM!=null){
			try {
				SharedPreferences pref = getSharedPreferences("temp", MODE_PRIVATE);
				Editor edit = pref.edit();
				edit.putString(Constants.LOGIN_ACTIVITY_EMAIL, "aman@aman.com");
				edit.putString(Constants.LOGIN_ACTIVITY_PASSCODE, "1234");
				edit.commit();

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
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode==RESULT_ENABLE){
			if(resultCode==RESULT_OK){
				Registration.register(this);
				setResult(RESULT_OK);
			}else if(resultCode==RESULT_CANCELED){
				Toast.makeText(getApplicationContext(), "You need to enable the admin to continue.", Toast.LENGTH_LONG).show();
				setResult(RESULT_CANCELED);
			}
		}else if(requestCode== REQUEST_GCM){
			finish();
		}
	}
}