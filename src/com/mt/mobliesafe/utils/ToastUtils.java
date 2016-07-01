package com.mt.mobliesafe.utils;

import android.content.Context;
import android.widget.Toast;

public class ToastUtils {
	public static void showToast(Context context, String content) {
		Toast.makeText(context, content, Toast.LENGTH_LONG).show();
	}
}
