package com.mt.mobliesafe.activity;

import com.mt.mobliesafe.R;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

public class LostFindActivity extends Activity {

	private SharedPreferences mSp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mSp = getSharedPreferences("config", MODE_PRIVATE);
		boolean configed = mSp.getBoolean("configed", false);
		if (configed) {
			setContentView(R.layout.activity_lost_find);
		} else {
			Intent intent = new Intent(LostFindActivity.this, Setup1Activity.class);
			startActivity(intent);
			finish();
		}

	}
}
