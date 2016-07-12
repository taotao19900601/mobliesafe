package com.mt.mobliesafe.activity;

import com.mt.mobliesafe.R;
import com.mt.mobliesafe.service.AddressService;
import com.mt.mobliesafe.utils.ServiceStatusUtils;
import com.mt.mobliesafe.view.SettingItemView;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class SettingActivity extends Activity {
	private SettingItemView sivUpdate; //设置升级
	private SettingItemView sivAddress;
	private SharedPreferences mPref;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
		mPref = getSharedPreferences("config", MODE_PRIVATE);
		initUpdateView();
		initAddressView();
	}
	
	public void initAddressView(){
		sivAddress = (SettingItemView) findViewById(R.id.siv_address);
		// 判断当前addressservice 是否运行
		boolean serviceRunning = ServiceStatusUtils.isServiceRunning(this,"com.mt.mobliesafe.service.AddressService");
		if(serviceRunning){
			sivAddress.setChecked(true);
		}else{
			sivAddress.setChecked(false);
		}
		
		sivAddress.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (sivAddress.isChecked()) {
					sivAddress.setChecked(false);
					stopService(new Intent(SettingActivity.this, AddressService.class));
				}else{
					sivAddress.setChecked(true);
					startService(new Intent(SettingActivity.this, AddressService.class));
				}
			}
		});
		
	}
	
	public void initUpdateView(){
		sivUpdate = (SettingItemView) findViewById(R.id.siv_update);
//		sivUpdate.setTitle("更新设置");
		boolean autoUpdate = mPref.getBoolean("auto_update", true);
		
		if(autoUpdate){
//			sivUpdate.setDesc("自动更新已开启");
			sivUpdate.setChecked(true);
		}
		else{
//			sivUpdate.setDesc("自动更新已关闭");
			sivUpdate.setChecked(false);
		}
		
		sivUpdate.setChecked(autoUpdate);
//		sivUpdate.setDesc("自动更新已开启");
		
		sivUpdate.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//判断勾选状态
				if(sivUpdate.isChecked()){
					sivUpdate.setChecked(false);
					sivUpdate.setDesc("自动更新已关闭");
					mPref.edit().putBoolean("auto_update", false).commit();
				}
				else{
					sivUpdate.setChecked(true);
					sivUpdate.setDesc("自动更新已开启");
					mPref.edit().putBoolean("auto_update", true).commit();
				}
			}
		});
	}
}
