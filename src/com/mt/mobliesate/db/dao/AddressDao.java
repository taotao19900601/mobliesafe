package com.mt.mobliesate.db.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class AddressDao {

	private static String address = "未知号码";
	private static SQLiteDatabase database;

	public static String getAddress(String number) {
		String path = "data/data/com.mt.mobliesafe/files/address.db";
		database = SQLiteDatabase.openDatabase(path, null,
				SQLiteDatabase.OPEN_READONLY);
		// 匹配手机号码
		if (number.matches("^1[3-8]\\d{9}$")) {
			
			Cursor cursor = database
					.rawQuery(
							"select location from data2 where id=(select outkey from data1 where id=?)",
							new String[] { number.substring(0, 7) });
			if (cursor.moveToNext()) {
				address = cursor.getString(0);
			}
			// 匹配数字
		} else if (number.matches("^\\d+$")) {
			switch (number.length()) {
			case 3:
				address = "报警电话";
				break;
			case 4:
				address = "模拟器";
				break;
			case 5:
				address = "客服电话";
				break;
			case 7:
			case 8:
				address = "本地电话";
				break;
			default:
				if (number.startsWith("0") && number.length() > 10) {
					// 可能是长途电话 匹配4位区号
					Cursor rawQuery = database.rawQuery(
							"select location from data2 where area=?",
							new String[] { number.substring(1, 4) });
					if (rawQuery.moveToNext()) {
						address = rawQuery.getString(0);
					} else {
						rawQuery.close();
						rawQuery = database.rawQuery(
								// 匹配3位区号
								"select location from data2 where area=?",
								new String[] { number.substring(1, 3) });

						if (rawQuery.moveToNext())
							address = rawQuery.getString(0);
						rawQuery.close();
					}
				}

				break;
			}
		}
		database.close();
		return address;
	}

}
