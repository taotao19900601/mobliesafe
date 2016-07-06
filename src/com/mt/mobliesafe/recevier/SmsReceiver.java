package com.mt.mobliesafe.recevier;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.sax.StartElementListener;
import android.telephony.SmsMessage;
import android.util.Log;
import com.mt.mobliesafe.R;
import com.mt.mobliesafe.service.LocationService;


public class SmsReceiver extends BroadcastReceiver{

	@Override
	public void onReceive(Context context, Intent intent) {
		//拦截短息
		Object[] object = (Object[]) intent.getExtras().get("pdus");
		// 短息为140个字节 超出部分会分多条信息发送 所以是个数组      因为短信指令很短  所以for 执行一次
		for(Object obj : object){
			SmsMessage message = SmsMessage.createFromPdu((byte[])obj);
			String phoneNumber = message.getOriginatingAddress();
			String messageBody = message.getMessageBody();
			Log.i("TAG","phonenum:"+phoneNumber+" message:"+messageBody);
			if("#*alarm*#".equals(messageBody)){
				MediaPlayer mediaPlayer = MediaPlayer.create(context,R.raw.ylzs);
				mediaPlayer.setVolume(1.0f, 1.0f);
				mediaPlayer.setLooping(true);
				mediaPlayer.start();
				abortBroadcast();// 中断广播 短信app就收不到短信了
			}
			else if("#*location*#".equals(messageBody)){
				//获取经纬度坐标
				Intent service = new Intent(context, LocationService.class);
				context.startService(service);
				String location = context.getSharedPreferences("config", Context.MODE_PRIVATE).getString("location", "");
				abortBroadcast();
				Log.i("TAG", "location:"+location);
			}
			
		}
		
	}

}
