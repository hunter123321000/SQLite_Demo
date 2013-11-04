package com.hunter123321000.sqllite.test;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;

public class MainActivity extends Activity {
	private Button add, query, delete, update, btn_add_cancel, btn_add_ok;
	private DBHelper DH = null;
	private BTN_onclicklisterner btn_onclick;
	private SQLiteDatabase db;
	private GridView gv;
	private ArrayList<String> ArrayofData = new ArrayList<String>();
	private ArrayAdapter<String> adapter;
	private Dialog dialog_add;
	private EditText et_add_title,et_add_content,et_add_kind;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		findControl();
		openDB();
		db = DH.getWritableDatabase();
	}

	private void add(String Title, String Content, String Kind) {
		ContentValues values = new ContentValues();
		values.put("_TITLE", Title.toString());
		values.put("_CONTENT", Content.toString());
		values.put("_KIND", Kind.toString());
		db.insert("MySample", null, values);
	}

	private void query() {
		Cursor cur = db.rawQuery("SELECT * FROM " + "MySample", null);
		if (cur != null) {
			int i = 0;
			ArrayofData.add("ID");
			ArrayofData.add("Title");
			ArrayofData.add("Content");
			ArrayofData.add("Kind");

			while (cur.moveToNext()) {
				String temp = "";
				temp += cur.getString(0) + "\n";
				ArrayofData.add(temp);
				temp = "";
				temp += cur.getString(1) + "\n";
				ArrayofData.add(temp);
				temp = "";
				temp += cur.getString(2) + "\n";
				ArrayofData.add(temp);
				temp = "";
				temp += cur.getString(3) + "\n";
				ArrayofData.add(temp);
				i++;
				// temp += "\r\n";
			}
			// Log.i("0.0", "Query=" + temp);
		}
	}

	private void update(String Title, String Content, String Kind, String id) {
		// ----第1种方式修改数据----
		ContentValues values = new ContentValues();
		values.put("_TITLE", Title.toString());
		values.put("_CONTENT", Content.toString());
		values.put("_KIND", Kind.toString());
		db.update("MySample", values, "_id " + "=" + id, null);
		// [1]参数table:需要操作的表名
		// [2]参数values:ContentValues
		// [3]参数whereClause:更新的条件
		// [4]参数whereArgs:更新条件对应的值

		// ----第2种方式修改数据----
		// String UPDATA_DATA =
		// "UPDATE MySample SET _TITLE='777' and '_CONTENT='888' and _KIND='999'  WHERE _id=2";
		// db.execSQL(UPDATA_DATA);
		Log.i("0.0", "更新數據成功!");
	}

	private void delete(String id) {
		// ----way1----
		// db.delete("MySample", "_id" + "= 1", null);
		// public int delete(String table, String whereClause, String[]
		// whereArgs)解说
		// [1]参数table:需要操作的表名
		// [2]参数whereClause:删除的条件
		// [3]参数whereArgs:删除条件对应的值

		// ----way2----
		String DELETE_DATA = "DELETE FROM MySample WHERE _id=" + id;
		db.execSQL(DELETE_DATA);
		Log.i("0.0", "刪除成功!");
	}

	private void openDB() {
		DH = new DBHelper(this);
	}

	private void closeDB() {
		DH.close();
	}

	private void findControl() {
		// Dialog
		dialog_add = new Dialog(MainActivity.this);// 指定自定義樣式
		dialog_add.setContentView(R.layout.dialog_add);// 指定自定義layout

		btn_onclick = new BTN_onclicklisterner();

		//Button
		add = (Button) findViewById(R.id.add);
		query = (Button) findViewById(R.id.query);
		delete = (Button) findViewById(R.id.delete);
		update = (Button) findViewById(R.id.update);
		btn_add_cancel = (Button) dialog_add.findViewById(R.id.btn_add_cancel);
		btn_add_ok = (Button) dialog_add.findViewById(R.id.btn_add_ok);

		add.setOnClickListener(btn_onclick);
		query.setOnClickListener(btn_onclick);
		delete.setOnClickListener(btn_onclick);
		update.setOnClickListener(btn_onclick);
		btn_add_cancel.setOnClickListener(btn_onclick);
		btn_add_ok.setOnClickListener(btn_onclick);

		//EditText
		et_add_title=(EditText)dialog_add.findViewById(R.id.et_title);
		et_add_content=(EditText)dialog_add.findViewById(R.id.et_content);
		et_add_kind=(EditText)dialog_add.findViewById(R.id.et_kind);
		//GridView
		gv = (GridView) findViewById(R.id.gv);

	}

	private class BTN_onclicklisterner implements OnClickListener {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.add:
				openDialog("1", "123");
				break;
			case R.id.query:
				query();
				// adapter = new ArrayAdapter<String>(MainActivity.this,
				// android.R.layout.simple_list_item_1, ArrayofData);
				// gv.setAdapter(adapter);
				gv.setAdapter(new MyAdapter(MainActivity.this,
						android.R.layout.simple_list_item_1, ArrayofData));
				break;
			case R.id.delete:
				delete("3");
				break;
			case R.id.update:
				update("777", "888", "999", "2");
				break;
			case R.id.btn_add_cancel:
				dialog_add.dismiss();
				break;
			case R.id.btn_add_ok:				
				add(et_add_title.getText().toString(), et_add_content.getText().toString(), et_add_kind.getText().toString());
				dialog_add.dismiss();
				break;
			}
		}

	}

	private class MyAdapter extends ArrayAdapter<String> {
		public MyAdapter(Context context, int textViewResourceId,
				List<String> objects) {
			super(context, textViewResourceId, objects);
			// TODO Auto-generated constructor stub
		}

		private ArrayList<Integer> coloredItems = new ArrayList<Integer>();

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View v = super.getView(position, convertView, parent);

			if (coloredItems.contains(position)) {
				v.setBackgroundColor(Color.YELLOW);
			} else {
				v.setBackgroundColor(Color.CYAN); // or whatever was original
			}

			return v;
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	private void openDialog(String Mode, String Msg) {
		if (Mode == "1") {
			// TextView AlertMsg = new TextView(MainActivity.this);
			// AlertMsg.setText(Msg);
			// new
			// AlertDialog.Builder(MainActivity.this).setTitle("效果").setView(AlertMsg)
			// .show();
			dialog_add.show();
		} else if (Mode == "2") {
			String url = "file:///android_asset/Sample.html";
			WebView AlertMsg = new WebView(this);
			WebSettings websettings = AlertMsg.getSettings();
			websettings.setSupportZoom(true);
			websettings.setJavaScriptEnabled(true);
			AlertMsg.setWebViewClient(new WebViewClient());
			AlertMsg.loadUrl("file:///android_asset/Sample.html");
			new AlertDialog.Builder(this).setTitle("程式碼").setView(AlertMsg)
					.show();

		}

	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		closeDB();
	}
}
