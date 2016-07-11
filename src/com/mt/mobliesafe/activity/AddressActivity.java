package com.mt.mobliesafe.activity;

import com.mt.mobliesafe.R;
import com.mt.mobliesate.db.dao.AddressDao;

import android.app.Activity;
import android.os.Bundle;
import android.os.Vibrator;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.TextView;

public class AddressActivity extends Activity{
	private EditText etNumber;
	private TextView tvResult;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_address);
		etNumber=(EditText) findViewById(R.id.et_number);
		tvResult=(TextView) findViewById(R.id.tv_result);
		// 设置文本变化的监听
		etNumber.addTextChangedListener(new TextWatcher() {
			
			// 发生变化
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				String address = AddressDao.getAddress(s.toString());
				tvResult.setText(address);
			}
			
			// 变化前
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				
			}
			
			// 变化后
			@Override
			public void afterTextChanged(Editable s) {
				String address = AddressDao.getAddress(s.toString());
				tvResult.setText(address);
			}
		});
	}
	
	public void query(View view){
		String number = etNumber.getText().toString().trim();
		if(!TextUtils.isEmpty(number)){
			String address = AddressDao.getAddress(number);
			tvResult.setText(address);
		}else{
			Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
			etNumber.startAnimation(shake);
			vibrate();
		}
		
		
	}
	// 手机震动
	public void vibrate(){
		Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
		vibrator.vibrate(1000);
	}
}
