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

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

public class Setup4Activity extends Activity {
	private SharedPreferences mSp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setup4);

	}

	public void next(View v) {
		mSp = getSharedPreferences("config", MODE_PRIVATE);
		mSp.edit().putBoolean("configed", true).commit();
		finish();
		
	}
	
	public void previous(View v){
		Intent intent = new Intent(this,Setup3Activity.class);
		startActivity(intent);
		finish();
	}
}
