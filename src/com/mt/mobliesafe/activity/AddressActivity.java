package com.mt.mobliesafe.activity;

import com.mt.mobliesafe.R;
import com.mt.mobliesate.db.dao.AddressDao;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class AddressActivity extends Activity{
	private EditText etNumber;
	private TextView tvResult;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_address);
		etNumber=(EditText) findViewById(R.id.et_number);
		tvResult=(TextView) findViewById(R.id.tv_result);
	}
	
	public void query(View view){
		String number = etNumber.getText().toString().trim();
		if(!TextUtils.isEmpty(number)){
			String address = AddressDao.getAddress(number);
			tvResult.setText(address);
		}
		
		
	}
}
