package com.duole.wizard.setup.utils;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;

public class WifiUtils {
	
	/**
	 * Get the security of a scan result.
	 * 
	 * @param result
	 * @return
	 */
	public static int getSecurity(ScanResult result) {
		if (result.capabilities.contains("WEP")) {
			return 1;
		} else if (result.capabilities.contains("PSK")) {
			return 2;
		} else if (result.capabilities.contains("EAP")) {
			return 3;
		}
		return 0;
	}
	
    /**
     * Set WifiConfiguration
     */
    public static void setWifiConfigurationSettings(WifiConfiguration wc,String capa,String pass){
    	
    	
    	if(capa.contains("WEP")){
    		wc.priority = 40;
    	    wc.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
    	    wc.allowedProtocols.set(WifiConfiguration.Protocol.RSN); 
    	    wc.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
    	    wc.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
    	    wc.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.SHARED);
    	    wc.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
    	    wc.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
    	    wc.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
    	    wc.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP104);
    	    wc.wepKeys[0] = "\"" + pass + "\"";
    	}else{

			wc.preSharedKey = "\""
					+ pass
					+ "\"";
			wc.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
			wc.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
			wc.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
		
			wc.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
			wc.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
			wc.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
			wc.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
			wc.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
			wc.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_EAP);
		
    	}
    	
    }
    
	/** 
     * check whether network is aviable.
     */  
    public static boolean isNetworkAvailable(Context context2){  
    	
        Context context = context2.getApplicationContext();  
        ConnectivityManager connectivity =(ConnectivityManager)  
        context.getSystemService(Context.CONNECTIVITY_SERVICE);  

        if(connectivity == null){  
            return false;  
        }else {  
            NetworkInfo[] info = connectivity.getAllNetworkInfo();  
            if(info != null){  
                for(int i= 0;i<info.length;i++){  
                    if(info[i].getState() == NetworkInfo.State.CONNECTED){  
                        return true;  
                    }  
                }  
            }  
        }  
        return false;  
    }  
}
