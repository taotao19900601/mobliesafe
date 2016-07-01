package com.mt.mobliesafe.activity;

import com.mt.mobliesafe.R;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class LostFindActivity extends Activity {

	private SharedPreferences mSp;
	private TextView tv_safephone;
	private ImageView ivProtect;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mSp = getSharedPreferences("config", MODE_PRIVATE);
		boolean configed = mSp.getBoolean("configed", false);
		if (configed) {
			setContentView(R.layout.activity_lost_find);
			tv_safephone = (TextView) findViewById(R.id.tv_safe_phone);
			ivProtect = (ImageView) findViewById(R.id.iv_protect);
			String phone = mSp.getString("safe_phone", "");
			boolean protect = mSp.getBoolean("protect", false);
			if(protect){
				ivProtect.setImageResource(R.drawable.lock);
			}else{
				ivProtect.setImageResource(R.drawable.unlock);
			}
			tv_safephone.setText(phone);
		} else {
			Intent intent = new Intent(LostFindActivity.this, Setup1Activity.class);
			startActivity(intent);
			finish();
		}

	}
	
	public void reEnter(View view){
		Intent intent = new Intent(this,Setup1Activity.class);
		startActivity(intent);
		finish();
	}
}
