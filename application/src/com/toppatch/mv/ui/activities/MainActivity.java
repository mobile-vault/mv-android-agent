package com.toppatch.mv.ui.activities;


import java.util.ArrayList;
import java.util.concurrent.Semaphore;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.toppatch.mv.R;
import com.toppatch.mv.R.layout;
import com.toppatch.mv.R.menu;
import com.toppatch.mv.gcm.GCMActivity;
import com.toppatch.mv.helper.AdminHelper;
import com.toppatch.mv.helper.WebHelper;
import com.toppatch.mv.samsung.Registration;
import com.toppatch.mv.samsung.SamsungReceiver;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.enterprise.license.EnterpriseLicenseManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity {

	private final int LOGIN_REQUEST_CODE =1000;
	private final int WELCOME_REQUEST_CODE =1001;
	private final int GCM_REQUEST_CODE =1002;
	private Button acceptConditions;
	SamsungReceiver samsungReciever;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		String TAG="MainActivity.onCreate";
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Intent intent=getIntent();
		intent.getExtras();
		Boolean bool=intent.getBooleanExtra("fromReciever", false);
		IntentFilter filter = new IntentFilter();
		filter.addAction(EnterpriseLicenseManager.ACTION_LICENSE_STATUS);
		samsungReciever=new SamsungReceiver();
		this.registerReceiver(samsungReciever, filter);
		if(bool==true){
			acceptConditions=(Button)findViewById(R.id.acceptConditionsButton);
			acceptConditions.setText("Sorry you need to accept conditions to proceed....");
			acceptConditions.setClickable(true);
		}else{
		Log.i("see", "oncreate");
		checkConditions();
		}
		

	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		Log.i("see", "ondestroy");
	    
		unregisterReceiver(samsungReciever);
	}
	private void checkConditions(){
		//sendToWelcomePage();
		if(isConnectingToInternet()){
		  if(AdminHelper.isUserLoggedIn(this)){
			if(AdminHelper.isAdminActive(this)){
				if(AdminHelper.isSamsungESDKEnabled(this))
					if(AdminHelper.isGCMRegistered(this)){
						Log.i("see", "gcmif");sendToWelcomePage();}
					else{
						Log.i("see", "gcmelse");sendToGCMPage();}
				else{
					Log.i("see", "register");
					Boolean canRegiser=Registration.register(this); 
					if(!canRegiser)
					sendToLoginPage();
						}
				
			}else{
				sendToLoginPage();
			}
		}else{
			sendToLoginPage();
			}
	}else{
			AlertDialog alertDialog = new AlertDialog.Builder(
					this).create();
			alertDialog.setTitle("Ohh No!!");
			alertDialog.setMessage("No Internet Connection...Exiting..");
            alertDialog.show();
			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					finish();
				}
			},2000);
		}
	}

	private void sendToLoginPage(){
		Intent login = new Intent(getApplicationContext(),LoginActivity2.class);
		startActivityForResult(login, LOGIN_REQUEST_CODE);
	}

	private void sendToWelcomePage(){
		Intent welcome = new Intent(getApplicationContext(),WelcomeActivity.class);
		Log.i("see", "welcome");
		startActivityForResult(welcome, WELCOME_REQUEST_CODE);
	}
	private boolean isConnectingToInternet(){
		ConnectivityManager connectivity = (ConnectivityManager)this.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity != null) 
		{
			NetworkInfo[] info = connectivity.getAllNetworkInfo();
			if (info != null) 
				for (int i = 0; i < info.length; i++) 
					if (info[i].getState() == NetworkInfo.State.CONNECTED)
					{
						return true;
					}

		}
		return false;
	}
	private void sendToGCMPage(){
		Intent gcm = new Intent(getApplicationContext(),GCMActivity.class);
		startActivityForResult(gcm, GCM_REQUEST_CODE);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}


	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Log.i("see", "acresult:"+resultCode);
		if(requestCode==GCM_REQUEST_CODE)
			checkConditions();
	//	else if(requestCode)
			finish();
	}
	public void register(View v)
	{   
		Boolean canRegiser=Registration.register(this); 
		Log.i("see", "hww:"+canRegiser);
		acceptConditions.setText("Processing...Please Wait");
		acceptConditions.setClickable(false);
		if(!canRegiser){
		sendToLoginPage();}
	    
	}
	
}