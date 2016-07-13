package com.mt.mobliesafe.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.view.WindowManager;
import android.widget.TextView;

import com.mt.mobliesate.db.dao.AddressDao;

public class AddressService extends Service {

	private TelephonyManager tm;
	private MyListener listener;
	private OutCallRecevier recevier;
	private WindowManager mWM;
	private TextView view;

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

	// 定义去电 广播监听
	class OutCallRecevier extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			String resultData = getResultData();
			String address = AddressDao.getAddress(resultData);
			showToast(address);
		}

	}
	// 监听来电
	class MyListener extends PhoneStateListener {
		@Override
		public void onCallStateChanged(int state, String incomingNumber) { 
			switch (state) {
			case TelephonyManager.CALL_STATE_RINGING: // 电话铃声响了
				String address = AddressDao.getAddress(incomingNumber);
				showToast(address);
				break;
			case TelephonyManager.CALL_STATE_IDLE:
				if(mWM!=null){
					mWM.removeView(view); // 移除界面
					view = null;
				}
				break;
			default:
				break;
			}
		}
	}
	/**
	 * 自定义归属地浮窗
	 */
	public void showToast(String text){
		
		
		mWM = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);

		WindowManager.LayoutParams params = new WindowManager.LayoutParams();
		params.height = WindowManager.LayoutParams.WRAP_CONTENT;
		params.width = WindowManager.LayoutParams.WRAP_CONTENT;
		params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
				| WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
				| WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
		params.format = PixelFormat.TRANSLUCENT;
		params.type = WindowManager.LayoutParams.TYPE_TOAST;
		params.setTitle("Toast");

		view = new TextView(this);
		view.setText(text);
		view.setTextColor(Color.RED);
		view.setTextSize(20.0f);
		mWM.addView(view, params);// 将view添加在屏幕上(Window)
	}

}
