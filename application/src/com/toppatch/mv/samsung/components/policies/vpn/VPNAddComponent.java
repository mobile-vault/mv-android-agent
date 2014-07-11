

package com.toppatch.mv.samsung.components.policies.vpn;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.enterprise.EnterpriseDeviceManager;
import android.app.enterprise.VpnAdminProfile;
import android.app.enterprise.VpnPolicy;
import android.content.Context;
import android.util.Log;

import com.toppatch.mv.Constants;
import com.toppatch.mv.samsung.components.Component;

public class VPNAddComponent extends Component {

	private static final String TAG = "VPNAddComponent";

	public VPNAddComponent(Context context) {
		super(context);
	}

	/**
	 * {
	 * 	"list":[
	 * 			{
	 * 				"vpn_name":""
	 * 				...
	 * 				...
	 * 				...
	 * 			}
	 * 		]
	 * }
	 */
	@Override
	public void execute(JSONObject data) {
		if(data!=null){
			Log.d(TAG, "Executing "+data.toString());
			JSONArray vpnList = data.optJSONArray(Constants.VPN_LIST);
			EnterpriseDeviceManager edm2 = getEdm();
			if(vpnList!=null && vpnList.length()>0){
				for(int i=0;i<vpnList.length();i++){
					try {
						JSONObject vpn = vpnList.getJSONObject(i);
						if(vpn!=null){
							VpnAdminProfile profile = new VpnAdminProfile();
							if(vpn.optString(Constants.VPN_DNS_SERVER)!=null){
								profile.dnsServers = new ArrayList<String>();
								profile.dnsServers.add(vpn.optString(Constants.VPN_DNS_SERVER));
							}
							if(vpn.optString(Constants.VPN_FORWARD_ROUTES)!=null){
								profile.forwardRoutes = new ArrayList<String>();
								profile.forwardRoutes.add(vpn.optString(Constants.VPN_FORWARD_ROUTES));
							}
							if(vpn.optString(Constants.VPN_SEARCH_DOMAINS)!=null){
								profile.searchDomains = new ArrayList<String>();
								profile.searchDomains.add(vpn.optString(Constants.VPN_SEARCH_DOMAINS));
							}
							profile.IPSecCaCertificate = vpn.optString(Constants.VPN_IPSEC_CA_CERTIFICATE);
							profile.ipsecIdentifier = vpn.optString(Constants.VPN_IPSEC_ID);
							profile.IPSecPreSharedKey = vpn.optString(Constants.VPN_IPSEC_PRESHARED_KEY);
							profile.IPSecUserCertificate = vpn.optString(Constants.VPN_IPSEC_USER_CERTIFICATE);
							profile.L2TPSecret = vpn.optString(Constants.VPN_L2TP_SECRET);
							profile.L2TPSecretEnable = vpn.optBoolean(Constants.VPN_L2TP_SECRET_ENABLE);
							profile.PPTPEncryptionEnable = vpn.optBoolean(Constants.VPN_PPTP_ENCRYPRION_ENABLE);
							profile.profileName = vpn.optString(Constants.VPN_PROFILE_NAME);
							profile.serverName = vpn.optString(Constants.VPN_SERVER_NAME);
							profile.userName = vpn.optString(Constants.VPN_USER_NAME);
							profile.userPassword = vpn.optString(Constants.VPN_USER_PASSWORD);
							profile.vpnType =vpn.optString(Constants.VPN_TYPE);
							Log.d(TAG,"Adding VPN "+profile.profileName);
								/*  try {
								     boolean result = edm2.getSecurityPolicy().unlockCredentialStorage("abc34567");
								      int status = edm2.getSecurityPolicy().getCredentialStorageStatus();
								      Log.w(TAG,"Unlock"+status);
								      if(true == result) {
								    	  Log.w(TAG,"Unlock");
									  } // KeyStore is unlocked
								      else{
								    	  Log.w(TAG,"Locked");
								      }
								  } catch(SecurityException e) {
								      Log.w(TAG,"SecurityException: "+e);
								  }*/
							// Add the VPN..
							VpnPolicy vpnPolicy = edm2.getVpnPolicy();
							if(vpnPolicy!=null){
								try{
									// This is dangerous. try catch it.
									if(vpnPolicy.createProfile(profile)){
										Log.d(TAG, "VPN Added");
									}else{
										Log.d(TAG, "Oh No.Could not add VPN"+profile.ipsecIdentifier+profile.IPSecPreSharedKey);
									}
								}catch(Exception e){
									e.printStackTrace();
								}
							}
						}

					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			}			
		}
	}

}
