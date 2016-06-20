package com.mt.mobliesafe.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

public class FocusedTextView extends TextView {
	// 有属性时 走该方法
	public FocusedTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	// 有style样式会走此方法
	public FocusedTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	// 用代码new对象时 走此方法
	public FocusedTextView(Context context) {
		super(context);
	}

	// 有没有获取焦点 跑马灯要运行首先调用此函数 是否有焦点 为true时 跑马灯才有效果
	@Override
	public boolean isFocused() {
		return true;
	}

}
