package com.mt.mobliesafe.recevier;

import com.mt.mobliesafe.utils.ToastUtils;
import com.mt.mobliesate.db.dao.AddressDao;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

// 检测去电状态
public class OutCallRecevier extends BroadcastReceiver{

	@Override
	public void onReceive(Context context, Intent intent) {
		String resultData = getResultData();
		String address = AddressDao.getAddress(resultData);
		ToastUtils.showToast(context, address);
		
	}

}
