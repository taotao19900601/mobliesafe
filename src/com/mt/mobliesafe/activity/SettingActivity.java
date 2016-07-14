package com.mt.mobliesafe.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.mt.mobliesafe.R;
import com.mt.mobliesafe.service.AddressService;
import com.mt.mobliesafe.utils.ServiceStatusUtils;
import com.mt.mobliesafe.view.SettingClickView;
import com.mt.mobliesafe.view.SettingItemView;

public class SettingActivity extends Activity {
	private SettingItemView sivUpdate; //设置升级
	private SettingItemView sivAddress;
	private SharedPreferences mPref;
	private SettingClickView scvStyle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
		mPref = getSharedPreferences("config", MODE_PRIVATE);
		initUpdateView();
		initAddressView();
		initAddressStyle();
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
	// 修改提示框的显示风格
	public void initAddressStyle(){
		scvStyle = (SettingClickView) findViewById(R.id.scv_address_style);
		int style = mPref.getInt("addressstyle", 0);
		scvStyle.setTitle("归属地样式风格");
		scvStyle.setDesc(items[style]);
		scvStyle.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				showSingleChooseDialog();
				
			}
		});
	}
	final String items []= {"半透明","活力橙","卫士蓝","金属灰","苹果绿"};
	public void showSingleChooseDialog(){
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("归属地提示框风格");
		
		int style = mPref.getInt("addressstyle", 0);//读取保存的style
		builder.setSingleChoiceItems(items, style, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				mPref.edit().putInt("addressstyle", which).commit(); // 保存选择的风格
				scvStyle.setDesc(items[which]);
							
			}
		});
		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				
			}
		});
		builder.show();
		
	}
}
