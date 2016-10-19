package com.coolweather.app.activity;


import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.coolweather.app.R;
import com.coolweather.app.util.HttpCallbackListener;
import com.coolweather.app.util.HttpUtil;
import com.coolweather.app.util.Utility;

public class WeatherActivity extends Activity{

	private LinearLayout weatherInforLayout;
	/**
	 * for show city name
	 */
	private TextView cityNameText;
	/**
	 * for show the time of published
	 */
	private TextView publishText;
	/**
	 * for show the information of describe of weather 
	 */
	private TextView weatherDespText;
	/**
	 * for show temperature one
	 */
	private TextView temp1Text;
	/**
	 * for show temperature two
	 */
	private TextView temp2Text;
	/**
	 * for show current date
	 */
	private TextView currentDateText;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.weather_layout);
		
		//init the widget
		weatherInforLayout = (LinearLayout) findViewById(R.id.weather_info_layout);
		cityNameText = (TextView) findViewById(R.id.city_name);
		publishText = (TextView) findViewById(R.id.publish_text);
		weatherDespText = (TextView) findViewById(R.id.weather_desp);
		temp1Text = (TextView) findViewById(R.id.temp1);
		temp2Text = (TextView) findViewById(R.id.temp2);
		currentDateText = (TextView) findViewById(R.id.current_date);
		
		String countyCode = getIntent().getStringExtra("county_code");
		if (! TextUtils.isEmpty(countyCode)) {
			//if has countyCode then to query the weather
			publishText.setText("同步中...");
			weatherInforLayout.setVisibility(View.INVISIBLE);
			cityNameText.setVisibility(View.INVISIBLE);
			queryWeatherCode(countyCode);			
		}else {
			//show local weather if doesn't has countyCode
			showWeather();
		}
	}
	
	
	/**
	 * according to the countyCode query the weather
	 */
	private void queryWeatherCode(String countyCode){
		String address = "http://www.weather.com.cn/data/list3/city" +countyCode+".xml";
		Log.d("WeahterActivity",countyCode);
		queryFromServer(address,"countyCode");
	}
	
	
	/**
	 * query weahter according to the weather code
	 */
	private void queryWeatherInfo(String weatherCode){
		try {
			weatherCode = URLEncoder.encode(weatherCode,"UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String address = "http://wthrcdn.etouch.cn/weather_mini?citykey=" +weatherCode;	
		queryFromServer(address,"weatherCode");
		
		
	}
	
	
	/**
	 * according to the address and type of introduced to query weatherCode or weather
	 */
	private void queryFromServer(final String address, final String type){
		HttpUtil.sendHttpRequest(address, new HttpCallbackListener() {
			
			@Override
			public void onFinish(String response) {
				// TODO Auto-generated method stub
				if ("countyCode".equals(type)) {
					if (! TextUtils.isEmpty(response)) {
						//parsing the weatherCode from the data which return from server
						String[] array = response.split("\\|");
						if (array != null && array.length == 2){
							String weatherCode = array[1];
							Log.d("WeahterActivity", weatherCode.toString());
							queryWeatherInfo(weatherCode);
						}
					}
				}else if ("weatherCode".equals(type)) {
					//handle the information return from server
					Log.d("WeatherAcitvity", response);
					Utility.handleWeatherResponse(WeatherActivity.this, response);
					runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							showWeather();
						}
					});
				}
			}
			
			@Override
			public void onError(Exception e) {
				// TODO Auto-generated method stub
				runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						publishText.setText("同步失败");
					}
				});
			}
		});
	}
	
	
	/**
	 * read the weather information from which has store in SharedPrefences, and show the on the activity
	 */
	private void showWeather(){
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		Log.d("WeatherAcivity", "show the weather");
		cityNameText.setText(prefs.getString("city_name", ""));
		temp1Text.setText(prefs.getString("temp1", ""));
		System.out.println("temp1xxxx= "+prefs.getString("city_name", ""));
		temp2Text.setText(prefs.getString("temp2", ""));
		weatherDespText.setText(prefs.getString("weather_desp", ""));
		publishText.setText("今天"+prefs.getString("publish_time", "") + "发布");
		currentDateText.setText(prefs.getString("current_date", ""));
		weatherInforLayout.setVisibility(View.VISIBLE);
		cityNameText.setVisibility(View.VISIBLE);
	}
	
}
