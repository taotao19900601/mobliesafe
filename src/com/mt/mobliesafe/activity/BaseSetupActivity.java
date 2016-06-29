package com.mt.mobliesafe.activity;

import com.mt.mobliesafe.R;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.widget.Toast;

/**
 * 设置引导页的基类 不需要再清单文件中注册 该页面不需要展示
 * 
 * @author admin
 * 
 */
public abstract class BaseSetupActivity extends Activity {
	private GestureDetector detector;
	public  SharedPreferences mSp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mSp = getSharedPreferences("config", MODE_PRIVATE);
		detector = new GestureDetector(this, new SimpleOnGestureListener() {
			// \监听手势滑动事件
			/**
			 * motion e1 表示滑动的起点 motion e2 表示滑动的终点
			 * 
			 * velocityX 垂直方向上的速度 velocityY 水平方向上的速度
			 * 
			 * getRawX 整个屏幕 的坐标体系 getX 基于view的坐标体系
			 */
			public boolean onFling(MotionEvent e1, MotionEvent e2,
					float velocityX, float velocityY) {
				// 防止纵向划
				if (Math.abs(e2.getRawY() - e1.getRawY()) > 300) {
					Toast.makeText(BaseSetupActivity.this, "不能这样划", 1).show();
					return true;
				}
				
				if(Math.abs(velocityX)<100){
					Toast.makeText(BaseSetupActivity.this, "滑动的速度太慢了", 1).show();
					return true;
				}

				// 向右划 返回返回上一页

				if (e2.getRawX() - e1.getRawX() > 200) {
					showPreviousPage();
					return true;
				}

				// 向左划 跳转下一页
				else if (e1.getRawX() - e2.getRawX() > 200) {
					showNextPage();
					return true;
				}

				return super.onFling(e1, e2, velocityX, velocityY);
			};
		});
	}

	// 具体的方法在子类中实现自己的逻辑
	public abstract void showPreviousPage();

	public abstract void showNextPage();

	public void next(View v) {
		showNextPage();
	}

	public void previous(View v) {
		showPreviousPage();

	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		detector.onTouchEvent(event);
		return super.onTouchEvent(event);
	}
}
