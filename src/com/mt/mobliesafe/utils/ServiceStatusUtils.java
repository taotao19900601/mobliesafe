package com.mt.mobliesafe.utils;

import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;
import android.util.Log;

// 服务状态工具类
public class ServiceStatusUtils {
	
	public static boolean isServiceRunning(Context context , String serviceName){
		// 获取 activitymanager 管理类
		ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		// 获取正在运行的所有服务   信息    返回到list集合
		List<RunningServiceInfo> runningServices = activityManager.getRunningServices(100);
		for (RunningServiceInfo runningServiceInfo : runningServices) {
			// 遍历集合
			String className = runningServiceInfo.service.getClassName();
			if(serviceName.equals(className)){
				return true;
			}
		}
		return false;
	}
}
