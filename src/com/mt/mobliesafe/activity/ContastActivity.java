package com.mt.mobliesafe.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.mt.mobliesafe.R;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class ContastActivity extends Activity{
	private ListView listView;
	private List <HashMap<String,String>> readContact;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_contact);
		
		listView = (ListView) findViewById(R.id.lv);
		readContact = readContact();
		Log.i("TAG", ""+readContact.toString());
		SimpleAdapter adapter = new SimpleAdapter(this, readContact, R.layout.contact_list_item, new String[]{"name","phone"}, new int[]{R.id.tv_name,R.id.tv_phone});
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				String phone = readContact.get(position).get("phone"); 
				Intent data = new Intent();
				data.putExtra("phone", phone);
				setResult(0, data);
				finish();
			}
		});
	}
	
	public List<HashMap<String, String>> readContact(){
		ArrayList <HashMap<String, String>> list = new ArrayList<HashMap<String, String>> ();
		
		// 通过内容提供者的形式来查询
		Uri dataUri = Uri.parse("content://com.android.contacts/data");
		Uri rawContactUri = Uri.parse("content://com.android.contacts/raw_contacts");
		// 首先从contact2.db 的raw_contacts表中读取联系人的id（ contact_id）
		Cursor rawContactsCursor = getContentResolver().query(rawContactUri, new String[]{"contact_id"}, null, null, null);
		if(rawContactsCursor!=null){
			while(rawContactsCursor.moveToNext()){
				String contactId = rawContactsCursor.getString(0);
				// 其次 通过contact_id 查询data表中的联系人名称和电话
				Cursor dataCursor = getContentResolver().query(dataUri, new String []{"data1","mimetype"}, "contact_id=?", new String[]{contactId}, null);
				if(dataCursor!=null){
					HashMap<String, String>  map = new HashMap<String,String>();
					while(dataCursor.moveToNext()){
						String data1 = dataCursor.getString(0);
						String mimetype = dataCursor.getString(1);
						// 然后根据mimetype来区分那个是联系人那个是电话号码
						if(mimetype.equals("vnd.android.cursor.item/phone_v2")){
							map.put("phone", data1);
						}else if(mimetype.equals("vnd.android.cursor.item/name")){
							map.put("name", data1);
						}
					}
					list.add(map);
					dataCursor.close();
				}
			}
			rawContactsCursor.close();
		}
		return list;
	}
}
