package com.mt.mobliesafe.activity;

/**
 * .9path
 *  四条横线的说明
 *  1.上面的黑线代表 横向拉伸
 *  2.左边的黑线代表 纵向拉伸
 *  3.右边的黑线代表 纵向的文字填充
 *  4.下边的黑线代表 横向的文字填充
 *  
 *  
 * 
 */
import com.mt.mobliesafe.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class Setup3Activity extends BaseSetupActivity {
	private EditText etPhone;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setup3);
		etPhone = (EditText) findViewById(R.id.et_phone);
	}

	@Override
	public void showPreviousPage() {
		Intent intent = new Intent(this, Setup2Activity.class);
		startActivity(intent);
		overridePendingTransition(R.anim.tran_previous_in,
				R.anim.tran_previous_out);
		finish();
	}

	@Override
	public void showNextPage() {
		Intent intent = new Intent(this, Setup4Activity.class);
		startActivity(intent);
		finish();
		overridePendingTransition(R.anim.tran_in, R.anim.tran_out);
	}

	public void selectContast(View view) {
		Intent intent = new Intent(this, ContastActivity.class);
		startActivityForResult(intent, 0);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		String phone = data.getStringExtra("phone");
		phone = phone.replaceAll("-", "").replaceAll(" ", "");
		etPhone.setText(phone);
	}
}
