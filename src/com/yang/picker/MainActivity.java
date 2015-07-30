package com.yang.picker;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import org.apache.http.util.EncodingUtils;
import org.json.JSONArray;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.google.gson.Gson;
import com.yang.picker.CityPickerDialog.onCityPickedListener;
import com.yang.picker.address.City;
import com.yang.picker.address.County;
import com.yang.picker.address.Province;
import com.yang.picker.wheel.adapter.AbstractWheelTextAdapter;

public class MainActivity extends Activity {

	private ArrayList<Province> provinces = new ArrayList<Province>();
	private Button selectAreaBtn;
	private Button singlePicker;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		selectAreaBtn = (Button) findViewById(R.id.select_area_btn);
		selectAreaBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (provinces.size() > 0) {
					showAddressDialog();
				} else {
					new InitAreaTask(MainActivity.this).execute(0);
				}
			}
		});
		
		singlePicker =(Button) findViewById(R.id.single_picker);
		singlePicker.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				showDateDialog();
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private void showAddressDialog() {
		new CityPickerDialog(MainActivity.this, provinces, null, null, null,
				new onCityPickedListener() {

					@Override
					public void onPicked(Province selectProvince,
							City selectCity, County selectCounty) {
						StringBuilder address = new StringBuilder();
						address.append(
								selectProvince != null ? selectProvince
										.getAreaName() : "")
								.append(selectCity != null ? selectCity
										.getAreaName() : "")
								.append(selectCounty != null ? selectCounty
										.getAreaName() : "");
						String text = selectCounty != null ? selectCounty
								.getAreaName() : "";
						selectAreaBtn.setText(address);
					}
				}).show();
	}
	
	
	private void showDateDialog() {
		final ArrayList<String> years = new ArrayList<String>(); 
		years.add("2005年");
		years.add("2006年");
		years.add("2007年");
		years.add("2008年");
		years.add("2009年");
		years.add("2010年");
		years.add("2011年");
		years.add("2012年");
		years.add("2013年");
		years.add("2014年");
		years.add("2015年");
		years.add("2016年");
		years.add("2017年");
		years.add("2018年");
		AbstractWheelTextAdapter adapter = new AbstractWheelTextAdapter(MainActivity.this,
				R.layout.wheel_text) {

			@Override
			public int getItemsCount() {

				return years.size();
			}

			@Override
			protected CharSequence getItemText(int index) {

				return years.get(index);
			}
		};
		
		OnePickerDialog picker = new OnePickerDialog(MainActivity.this,adapter , new OnePickerDialog.onSelectListener() {
			
			@Override
			public void onSelect(AbstractWheelTextAdapter adapter, int position) {
				singlePicker.setText(years.get(position));
			}
		});
		picker.show();
	}

	private class InitAreaTask extends AsyncTask<Integer, Integer, Boolean> {

		Context mContext;

		Dialog progressDialog;

		public InitAreaTask(Context context) {
			mContext = context;
			progressDialog = Util.createLoadingDialog(mContext, "请稍等...", true,
					0);
		}

		@Override
		protected void onPreExecute() {

			progressDialog.show();
		}

		@Override
		protected void onPostExecute(Boolean result) {
			progressDialog.dismiss();
			if (provinces.size()>0) {
				showAddressDialog();
			} else {
				Toast.makeText(mContext, "数据初始化失败", Toast.LENGTH_SHORT).show();
			}
		}

		@Override
		protected Boolean doInBackground(Integer... params) {
			String address = null;
			InputStream in = null;
			try {
				in = mContext.getResources().getAssets().open("address.txt");
				byte[] arrayOfByte = new byte[in.available()];
				in.read(arrayOfByte);
				address = EncodingUtils.getString(arrayOfByte, "UTF-8");
				JSONArray jsonList = new JSONArray(address);
				Gson gson = new Gson();
				for (int i = 0; i < jsonList.length(); i++) {
					try {
						provinces.add(gson.fromJson(jsonList.getString(i),
								Province.class));
					} catch (Exception e) {
					}
				}
				return true;
			} catch (Exception e) {
			} finally {
				if (in != null) {
					try {
						in.close();
					} catch (IOException e) {
					}
				}
			}
			return false;
		}

	}
}
