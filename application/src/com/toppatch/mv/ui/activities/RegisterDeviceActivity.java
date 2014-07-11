package com.toppatch.mv.ui.activities;

import com.toppatch.mv.samsung.TopPatchAdmin;

import android.app.Activity;
import android.app.admin.DevicePolicyManager;
import android.app.enterprise.EnterpriseDeviceManager;
import android.app.enterprise.EnterpriseDeviceManager.EnterpriseKeyVersion;
import android.app.enterprise.EnterpriseDeviceManager.EnterpriseSdkVersion;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class RegisterDeviceActivity extends Activity implements OnClickListener {

	private DevicePolicyManager mDPM;
	private ComponentName mDeviceAdmin;
	static final int RESULT_ENABLE = 1;
	private static final String TAG = "RegisterDeviceActivity";
	Button register;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		enableAdmin();
	}

	@Deprecated
	private void setupUI(){
		register = new Button(this);
		register.setText("Register");
		setContentView(register);
	}

	@Deprecated
	private void setupListeners(){
		register.setOnClickListener(this);
	}

	@Override @Deprecated
	public void onClick(View v) {
		if(v.equals(register)){
//			enableAdmin();
			checkSamsungAdminStatus();
			Toast.makeText(getApplicationContext(), "register", Toast.LENGTH_LONG).show();
		}
	}
	
	@Deprecated
	private void checkSamsungAdminStatus(){
		EnterpriseDeviceManager edm = new EnterpriseDeviceManager(getApplicationContext());
		EnterpriseKeyVersion enterpriseKeyVer = edm.getEnterpriseKeyVer();
		EnterpriseSdkVersion enterpriseSdkVer = edm.getEnterpriseSdkVer();
		Toast.makeText(getApplicationContext(), "Key ver: "+enterpriseKeyVer.name()+" Sdk ver: "+enterpriseSdkVer.name(), Toast.LENGTH_LONG).show();		
	}

	private void enableAdmin(){
		mDPM = (DevicePolicyManager)getSystemService(Context.DEVICE_POLICY_SERVICE);
		mDeviceAdmin = new ComponentName(RegisterDeviceActivity.this, TopPatchAdmin.class);
		if(mDPM!=null){
			try {
				Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
				intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, mDeviceAdmin);
				intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "Additional text explaining why this needs to be added.");
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
		Log.e(TAG, "Requestcode = "+requestCode+" result code="+resultCode);
	}
}