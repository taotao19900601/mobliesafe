package com.mt.mobliesafe.service;

import com.mt.mobliesafe.utils.ToastUtils;
import com.mt.mobliesate.db.dao.AddressDao;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.text.style.SuperscriptSpan;
import android.util.Log;
import android.widget.Toast;

public class AddressService extends Service {

	private TelephonyManager tm;
	private MyListener listener;
	private OutCallRecevier recevier;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
		listener = new MyListener();
		tm.listen(listener, PhoneStateListener.LISTEN_CALL_STATE); // 监听打电话的状态
		// 动态注册广播
		recevier = new OutCallRecevier();
		IntentFilter filter = new IntentFilter();
		filter.addAction("android.intent.action.NEW_OUTGOING_CALL");
		registerReceiver(recevier, filter);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		tm.listen(listener, PhoneStateListener.LISTEN_NONE); // 停止来电监听
		// 动态注销广播
		unregisterReceiver(recevier);
	}

	// 定义来电广播接受器 监听
	class OutCallRecevier extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			String resultData = getResultData();
			String address = AddressDao.getAddress(resultData);
			ToastUtils.showToast(context, address);
		}

	}

	class MyListener extends PhoneStateListener {
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
