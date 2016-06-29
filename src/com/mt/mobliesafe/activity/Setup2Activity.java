package com.mt.mobliesafe.activity;

/**
 * .9path
 *  四条横线的说明
 *  1.上面的黑线代表 横向拉伸
 *  2.左边的黑线代表 纵向拉伸
 *  3.右边的黑线代表 纵向的文字填充
 *  4.下边的黑线代表 横向的文字填充
 *  
 *  
 * 
 */
import com.mt.mobliesafe.R;
import com.mt.mobliesafe.view.SettingItemView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

public class Setup2Activity extends BaseSetupActivity {

	private SettingItemView sivsim;
	private TelephonyManager telManager;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setup2);
		sivsim = (SettingItemView) findViewById(R.id.siv_sim);
		telManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
		int simState = telManager.getSimState();
		// 手机本身没有sim卡 或sim卡失效
//		if(simState == TelephonyManager.SIM_STATE_ABSENT){
//			sivsim.setEnabled(false);
//			Toast.makeText(this, "请插入sim或检查sim卡是否有效", 1).show();
//			
//		}
		String simStr = mSp.getString("sim", "");
		if(!TextUtils.isEmpty(simStr)){
			sivsim.setChecked(true);
		}else{
			sivsim.setChecked(false);
		}
		sivsim.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if(sivsim.isChecked()){
					sivsim.setChecked(false);
					mSp.edit().remove("sim").commit();
				}else{
					sivsim.setChecked(true);
					// 获取sim序列号
					String simSerialNumber = telManager.getSimSerialNumber();
					mSp.edit().putString("sim", simSerialNumber).commit();
					
				}
			}
		});
		
		
	}

	@Override
	public void showPreviousPage() {
		Intent intent = new Intent(this, Setup1Activity.class);
		startActivity(intent);
		finish();
		overridePendingTransition(R.anim.tran_previous_in,
				R.anim.tran_previous_out);
	}

	@Override
	public void showNextPage() {
		Intent intent = new Intent(this, Setup3Activity.class);
		startActivity(intent);
		finish();
		overridePendingTransition(R.anim.tran_in, R.anim.tran_out);
	}

}
