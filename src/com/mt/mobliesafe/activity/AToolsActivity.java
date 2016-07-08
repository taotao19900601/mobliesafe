package com.mt.mobliesafe.activity;

import com.mt.mobliesafe.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class AToolsActivity extends Activity{
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_atools);
		
	}
	
	public void numberAddressQuery(View view){
		startActivity(new Intent(this,AddressActivity.class));
	}
}
