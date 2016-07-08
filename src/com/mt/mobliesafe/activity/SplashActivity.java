package com.mt.mobliesafe.activity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.mt.mobliesafe.R;
import com.mt.mobliesafe.utils.StreamUtils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.AssetManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class SplashActivity extends Activity {

	protected static final int CODE_UPDATE_DIALOG = 0; // 更新对话框的标记
	protected static final int CODE_URL_ERROR = 1; // url错误
	protected static final int CODE_NET_ERROR = 2; // 网络错误
	protected static final int CODE_JSON_ERROR = 3; // json错误
	protected static final int CODE_ENTRY_HOME = 4; // 进入home

	private TextView tvVersion;
	private TextView tvProgress;
	private String versionName;
	private String mVersion;
	private int mVersionCode;
	private String mDesc; // 详情
	private String mDownloadUrl;

	private Message msg = Message.obtain();
	private HttpURLConnection conn;

	// 处理异步消息
	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {

			case CODE_UPDATE_DIALOG:
				Toast.makeText(SplashActivity.this, "新版本更新", Toast.LENGTH_LONG)
						.show();
				showUpdateDailog();

				break;

			case CODE_URL_ERROR:
				Toast.makeText(SplashActivity.this, "url错误", Toast.LENGTH_LONG)
						.show();
				enterHome();
				break;

			case CODE_NET_ERROR:
				Toast.makeText(SplashActivity.this, "net错误", Toast.LENGTH_LONG)
						.show();
				enterHome();
				break;

			case CODE_JSON_ERROR:
				Toast.makeText(SplashActivity.this, "json错误", Toast.LENGTH_LONG)
						.show();
				enterHome();
				break;
			case CODE_ENTRY_HOME:
				enterHome();
				break;
			default:
				break;
			}
		};
	};
	private SharedPreferences mSp;
	private RelativeLayout relativeLayout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);

		tvVersion = (TextView) findViewById(R.id.tv_version);
		tvProgress = (TextView) findViewById(R.id.tv_progress);

		relativeLayout = (RelativeLayout) findViewById(R.id.rl_splash);
		tvVersion.setText("版本号：" + getVersionName());

		mSp = getSharedPreferences("config", MODE_PRIVATE);
		boolean autoUpdate = mSp.getBoolean("auto_update", true);

		copyDB("address.db"); // 拷贝数据库

		if (autoUpdate) { // 从sp文件中获取 auto_update 的值 默认为true,否则 进入home主页
			checkVersion(); // 检查版本
		} else {
			mHandler.sendEmptyMessageDelayed(CODE_ENTRY_HOME, 2000);// 发送延迟消息
																	// 2000
		}

		AlphaAnimation alphaAnimation = new AlphaAnimation(0.6f, 1.0f);
		alphaAnimation.setDuration(2000);
		relativeLayout.setAnimation(alphaAnimation);

	}

	private String getVersionName() {
		PackageManager packageManager = getPackageManager();
		try {
			PackageInfo packageInfo = packageManager.getPackageInfo(
					getPackageName(), 0);
			int versionCode = packageInfo.versionCode;
			versionName = packageInfo.versionName;
			System.out.println("versioncode:" + versionCode + " versionName:"
					+ versionName);

		} catch (NameNotFoundException e) {
			e.printStackTrace();
			return "";
		}
		return versionName;
	}

	private int getVersionCode() {
		PackageManager packageManager = getPackageManager();
		try {
			PackageInfo packageInfo = packageManager.getPackageInfo(
					getPackageName(), 0);
			int versionCode = packageInfo.versionCode;
			return versionCode;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return 0;

	}

	private void checkVersion() {
		final long startTime = System.currentTimeMillis();
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					// 模拟器访问ip为 10.0.0.2
					URL url = new URL(
							"http://192.168.40.103:8080/ms/update.json");
					conn = (HttpURLConnection) url.openConnection();
					conn.setRequestMethod("GET");
					conn.setConnectTimeout(5000);
					// 设置相应超时
					conn.setReadTimeout(5000);
					conn.connect();
					final int responseCode = conn.getResponseCode();
					if (responseCode == 200) {
						InputStream inputStream = conn.getInputStream();
						String result = StreamUtils.readFromStream(inputStream);
						// 解析json
						JSONObject jo = new JSONObject(result);
						mVersion = jo.getString("versionName");
						mVersionCode = jo.getInt("versionCode");
						mDesc = jo.getString("description");
						mDownloadUrl = jo.getString("downloadUrl");
						// 服务器上的版本号大于 本地的版本号 说明有更新 ，否则 没有更新 进入主页面
						if (mVersionCode > getVersionCode()) {
							// 有更新
							msg.what = CODE_UPDATE_DIALOG;
							// handler.sendMessage(msg);
						} else {
							msg.what = CODE_ENTRY_HOME;
						}
					}

				} catch (MalformedURLException e) {
					msg.what = CODE_URL_ERROR;

					e.printStackTrace();

				} catch (IOException e) {

					msg.what = CODE_NET_ERROR;
					e.printStackTrace();

				} catch (JSONException e) {

					msg.what = CODE_JSON_ERROR;
					e.printStackTrace();

				}

				finally {
					long endTime = System.currentTimeMillis();
					// 访问网络花费的时间
					long timeUsed = endTime - startTime;
					if (timeUsed < 2000) {
						// 强制休眠 保证显示闪屏 保证2s 防止过快跳转
						try {
							Thread.sleep(2000 - timeUsed);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
					mHandler.sendMessage(msg);

					if (conn != null) {
						conn.disconnect();
					}
				}
			}

		}).start();

	}

	private void showUpdateDailog() {

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("最新版本：" + mVersion);
		builder.setMessage(mDesc);
		builder.setCancelable(false);

		builder.setPositiveButton("立即更新", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				downLoad();
			}
		});
		builder.setNegativeButton("取消", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				enterHome();

			}
		});

		builder.setOnCancelListener(new OnCancelListener() {

			@Override
			public void onCancel(DialogInterface dialog) {
				SplashActivity.this.finish();
			}
		});
		builder.show();
	}

	// 下载apk
	protected void downLoad() {
		// 下载到sd卡 先判断是否有sd
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			final String target = Environment.getExternalStorageDirectory()
					+ "/update.apk";
			HttpUtils httpUtils = new HttpUtils();
			httpUtils.download(mDownloadUrl, target,
					new RequestCallBack<File>() {

						// 文件的下载进度
						@Override
						public void onLoading(long total, long current,
								boolean isUploading) {
							super.onLoading(total, current, isUploading);
							tvProgress.setVisibility(View.VISIBLE);
							tvProgress.setText("下载进度：" + current * 100 / total
									+ "%");
							Log.i("TAG", "apk大小为：" + total + "当前：" + current);
						}

						// 下载成功回调
						@Override
						public void onSuccess(ResponseInfo<File> arg0) {
							Log.i("TAG", "下载成功！");
							Intent intent = new Intent(Intent.ACTION_VIEW);
							intent.addCategory(Intent.CATEGORY_DEFAULT);
							intent.setDataAndType(Uri.fromFile(arg0.result),
									"application/vnd.android.package-archive");
							startActivity(intent); // 下载成功后 跳转系统安装界面
						}

						// 下载失败回调
						@Override
						public void onFailure(HttpException arg0, String arg1) {
							Toast.makeText(SplashActivity.this, "下载失败",
									Toast.LENGTH_LONG).show();
						}

					});
		} else {
			Toast.makeText(SplashActivity.this, "没有sd", Toast.LENGTH_LONG)
					.show();
		}

	}

	private void enterHome() {
		Intent intent = new Intent(this, HomeActivity.class);
		startActivity(intent);
		// startActivityForResult(intent, 0);
		finish();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

	}

	// 拷贝数据库
	public void copyDB(String dbName) {
		File file = new File(getFilesDir(), dbName);
		// Log.i("TAG", "路径：" + file.getAbsolutePath());

		if(file.exists()){
			return;
		}
		InputStream is = null;
		FileOutputStream fileOutputStream = null;

		AssetManager assets = getAssets();
		try {
			is = assets.open(dbName);
			fileOutputStream = new FileOutputStream(file);
			int len = 0;
			byte[] buffer = new byte[1024];
			while ((len = is.read(buffer)) != -1) {
				fileOutputStream.write(buffer, 0, len);
				fileOutputStream.flush();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				fileOutputStream.close();
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}
}
