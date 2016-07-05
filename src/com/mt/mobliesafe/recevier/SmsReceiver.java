package com.mt.mobliesafe.recevier;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.telephony.SmsMessage;
import android.util.Log;
import com.mt.mobliesafe.R;


public class SmsReceiver extends BroadcastReceiver{

	@Override
	public void onReceive(Context context, Intent intent) {
		//拦截短息
		Object[] object = (Object[]) intent.getExtras().get("pdus");
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
		}
		
	}

}
