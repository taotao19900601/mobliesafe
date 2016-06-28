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
import android.os.Bundle;
import android.view.View;

public class Setup3Activity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setup3);

	}

	public void next(View v) {
		Intent intent = new Intent(this,Setup4Activity.class);
		startActivity(intent);
		finish();
	}
	
	public void previous(View v){
		Intent intent = new Intent(this,Setup2Activity.class);
		startActivity(intent);
		finish();
	}
}