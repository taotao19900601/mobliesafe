package com.mt.mobliesafe.service;

import com.mt.mobliesafe.utils.ToastUtils;
import com.mt.mobliesate.db.dao.AddressDao;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.text.style.SuperscriptSpan;
import android.util.Log;
import android.widget.Toast;

public class AddressService extends Service {

	private TelephonyManager tm;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
		MyListener listener = new MyListener();
		tm.listen(listener, PhoneStateListener.LISTEN_CALL_STATE); // 监听打电话的状态
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}
	
	class MyListener extends PhoneStateListener{
		@Override
		public void onCallStateChanged(int state, String incomingNumber) {
			super.onCallStateChanged(state, incomingNumber);
			switch (state) {
			case TelephonyManager.CALL_STATE_RINGING:
				Log.i("TAG", "电话铃声响了");
				String address = AddressDao.getAddress(incomingNumber); // 根据来电号码查询归属地
				ToastUtils.showToast(AddressService.this, address);
				break;
			default:
				break;
			}
		}
	}

}
