package com.mt.mobliesafe.recevier;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
/**
 * 好多国产rom基本都监听不到开机广播
 * @author admin
 *
 */
public class BootCompleteRecevier extends BroadcastReceiver{
	private SharedPreferences mSp;
	private TelephonyManager telephonyManager;

	@Override
	public void onReceive(Context context, Intent intent) {
		mSp = context.getSharedPreferences("config",Context.MODE_PRIVATE);
		String spSim = mSp.getString("sim", null);
		
		if(!TextUtils.isEmpty(spSim)){
			telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
			String simSerialNumber = telephonyManager.getSimSerialNumber();
			if(spSim.equals(simSerialNumber)){
				// sim卡安全
				
			}else{
				//sim卡不安全
				
			}
		}
		
		
	}

}
