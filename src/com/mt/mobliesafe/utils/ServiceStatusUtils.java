package com.mt.mobliesafe.utils;

import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;
import android.util.Log;

// 服务状态工具类
public class ServiceStatusUtils {
	
	public static boolean isServiceRunning(Context context , String serviceName){
		ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningServiceInfo> runningServices = activityManager.getRunningServices(100);
		for (RunningServiceInfo runningServiceInfo : runningServices) {
			String className = runningServiceInfo.service.getClassName();
			if(serviceName.equals(className)){
				return true;
			}
		}
		return false;
	}
}
