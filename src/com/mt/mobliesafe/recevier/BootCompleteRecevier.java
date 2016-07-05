package com.mt.mobliesafe.recevier;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;

/**
 * 好多国产rom基本都监听不到开机广播
 * 
 * @author admin
 * 
 */
public class BootCompleteRecevier extends BroadcastReceiver {
	private SharedPreferences mSp;
	private TelephonyManager telephonyManager;

	@Override
	public void onReceive(Context context, Intent intent) {

		mSp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
		boolean protect = mSp.getBoolean("protect", false);
		if (protect) {
			String spSim = mSp.getString("sim", null);

			if (!TextUtils.isEmpty(spSim)) {
				telephonyManager = (TelephonyManager) context
						.getSystemService(Context.TELEPHONY_SERVICE);
				String simSerialNumber = telephonyManager.getSimSerialNumber();
				if (spSim.equals(simSerialNumber)) {
					// sim卡安全
					Log.i("TAG", "sim safe");
				} else {
					// sim卡不安全
					Log.i("TAG", "sim not safe");
					String safephone = mSp.getString("safe_phone", "");
					SmsManager smManager = SmsManager.getDefault(); 
					smManager.sendTextMessage(safephone, null, "sim card changed", null, null);
						
					
				}
			}
		}

	}

}
