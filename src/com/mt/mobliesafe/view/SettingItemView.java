package com.mt.mobliesafe.view;

import com.mt.mobliesafe.R;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class SettingItemView extends LinearLayout{

	private TextView tvTitle;
	private TextView tvDesc;
	private CheckBox cbStatus;
	
	String NAMESPACE = "http://schemas.android.com/apk/res/com.mt.mobliesafe";
	private String mTitle;
	private String mDesc_on;
	private String mDesc_off;
	public SettingItemView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView();
	}

	public SettingItemView(Context context, AttributeSet attrs) {
		super(context, attrs);
		//获取自定义属性的值
		mTitle = attrs.getAttributeValue(NAMESPACE, "titles");
		mDesc_on = attrs.getAttributeValue(NAMESPACE,"desc_on");
		mDesc_off = attrs.getAttributeValue(NAMESPACE,"desc_off");
		initView();
//		int attributeCount = attrs.getAttributeCount();
//		for(int i = 0 ; i<attributeCount;i++){
//			String attributeName = attrs.getAttributeName(i);
//			String attributeValue = attrs.getAttributeValue(i);
//			Log.i("TAG", attributeName+":"+attributeValue);
//		}
//	
		
		
		
	}

	public SettingItemView(Context context) {
		super(context);
		initView();
	}
	/**
	 * 初始化布局
	 */
	private void initView(){
		
		View view = View.inflate(getContext(), R.layout.view_settingitem, this);
		tvTitle = (TextView) view.findViewById(R.id.tv_title);
		tvDesc = (TextView) view.findViewById(R.id.tv_desc);
		cbStatus = (CheckBox) view.findViewById(R.id.cb_status);
		
		setTitle(mTitle);
	}
	
	public void setTitle(String title){
		tvTitle.setText(title);
	}
	
	public void setDesc(String desc){
		tvDesc.setText(desc);
	}
	
	public boolean isChecked(){
		return cbStatus.isChecked();
	}
	
	public void setChecked(boolean check){
		cbStatus.setChecked(check);
		
		if(check){
			setDesc(mDesc_on);
		}
		else{
			setDesc(mDesc_off);
		}
	}

}
