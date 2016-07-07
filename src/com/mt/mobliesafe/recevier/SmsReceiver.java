package com.mt.mobliesafe.recevier;

import android.app.admin.DevicePolicyManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.telephony.SmsMessage;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.mt.mobliesafe.R;
import com.mt.mobliesafe.service.LocationService;


public class SmsReceiver extends BroadcastReceiver{
	private DevicePolicyManager mDPM;
	private ComponentName mDeviceAdminSample;
	private Context mContext;

	@Override
	public void onReceive(Context context, Intent intent) {
		mContext =context;
		mDPM = (DevicePolicyManager) mContext.getSystemService(Context.DEVICE_POLICY_SERVICE);
		mDeviceAdminSample = new ComponentName(mContext, AdminReceiver.class);
		
		//拦截短息
		Object[] object = (Object[]) intent.getExtras().get("pdus");
		// 短息为140个字节 超出部分会分多条信息发送 所以是个数组      因为短信指令很短  所以for 执行一次
		for(Object obj : object){
			SmsMessage message = SmsMessage.createFromPdu((byte[])obj);
			String phoneNumber = message.getOriginatingAddress();
			String messageBody = message.getMessageBody();
			Log.i("TAG","phonenum:"+phoneNumber+" message:"+messageBody);
			if("#*alarm*#".equals(messageBody)){
				MediaPlayer mediaPlayer = MediaPlayer.create(mContext,R.raw.ylzs);
				mediaPlayer.setVolume(1.0f, 1.0f);
				mediaPlayer.setLooping(true);
				mediaPlayer.start();
				abortBroadcast();// 中断广播 短信app就收不到短信了
			}
			else if("#*location*#".equals(messageBody)){
				//获取经纬度坐标
				Intent service = new Intent(mContext, LocationService.class);
				mContext.startService(service);
				String location = mContext.getSharedPreferences("config", Context.MODE_PRIVATE).getString("location", "");
				abortBroadcast();
				Log.i("TAG", "location:"+location);
			}
			
			// 销毁数据
			else if("#*wipedata*#".equals(messageBody)){
				// 激活设备管理器
				activeAdmin();
				clearData();
				abortBroadcast();
			}
			// 锁屏
			else if("#*lockscreen*#".equals(messageBody)){
				// 激活设备管理器
				activeAdmin();
				lockscreen();
				abortBroadcast();
			}
			
		}
		
	}
	
	public void lockscreen() {
		if (mDPM.isAdminActive(mDeviceAdminSample)) {
			mDPM.lockNow();// 设置锁屏
			mDPM.resetPassword("123456", 0);
		} else {
			Toast.makeText(mContext, "设备管理器是否激活", 1).show();
		}

	}

	// 激活设备管理器
	public void activeAdmin() {
		Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);

		intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN,
				mDeviceAdminSample);
		intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "heheheheh");
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); 
		mContext.startActivity(intent);
		Log.i("TAG", "activeAdmin()---");
	}
	
	
	public void clearData() {
		if (mDPM.isAdminActive(mDeviceAdminSample)) {
			mDPM.wipeData(0); // 清理数据 恢复出厂设置
		} else {
			Toast.makeText(mContext, "先激活设备管理器", 1).show();
		}
	}

}
