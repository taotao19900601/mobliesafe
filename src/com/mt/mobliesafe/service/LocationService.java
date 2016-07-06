package com.mt.mobliesafe.service;

import java.util.List;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
/**
 * 获取经纬度坐标
 * @author admin
 *
 */
public class LocationService extends Service{
	
	private LocationManager locationManager;
	private MyLocationListener listener;
	private SharedPreferences mSp;
	@Override
	public void onCreate() {
		super.onCreate();
		mSp = getSharedPreferences("config", MODE_PRIVATE);
		locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
		Criteria criteria = new Criteria(); // \订立标准
		criteria.setCostAllowed(true); // 是否允许付费 开启移动网络
		criteria.setAccuracy(Criteria.ACCURACY_FINE); // 精确度
		
		String bestProvider = locationManager.getBestProvider(criteria, true); //获得最佳的提供者
//		List<String> allProviders = locationManager.getAllProviders();
//		Log.i("TAG", allProviders.toString());
//		listener = new MyLocationListener();
		locationManager.requestLocationUpdates(bestProvider, 0, 0, listener);
	}
	
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		return super.onStartCommand(intent, flags, startId);
		
	}
	
	class MyLocationListener implements LocationListener{


		// 位置提供者状态发生变化
		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			Log.i("TAG","onStatusChanged");
		}
		
		@Override
		public void onProviderEnabled(String provider) {
			Log.i("TAG","onProviderEnabled");
		}
		
		@Override
		public void onProviderDisabled(String provider) {
			Log.i("TAG","onProviderDisabled");
		}
		// 位置发生变化
		@Override
		public void onLocationChanged(Location location) {
			Log.i("TAG","onLocationChanged");
			double longitude = location.getLongitude();// 经度
			double latitude = location.getLatitude();//纬度
			mSp.edit().putString("location", "longitude:"+longitude+";"+"latitude:"+latitude).commit();
			Log.i("TAG", "longitude:"+longitude+" latitude:"+latitude);
			stopSelf();
		}
		
	}
	
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		// 当activity 销毁时停止更新位置变化 节省电量
		locationManager.removeUpdates(listener);
	}
	
	

}
