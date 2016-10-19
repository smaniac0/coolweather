package com.coolweather.app.util;



import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;





import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;

import com.coolweather.app.model.City;
import com.coolweather.app.model.CoolWeatherDB;
import com.coolweather.app.model.County;
import com.coolweather.app.model.Province;

public class Utility {

	/**
	 * parsing and processing the data of province which return from service
	 */
	public synchronized static boolean handleProvincesResponse(CoolWeatherDB coolWeatherDB,String response){
		if (!TextUtils.isEmpty(response)) {
			String[] allProvinces = response.split(",");
			if (allProvinces != null && allProvinces.length > 0) {
				for(String p:allProvinces){
					String[] array = p.split("\\|");
					Province province = new Province();
					province.setProvinceName(array[1]);
					province.setProvinceCode(array[0]);
					//put the data which has been parsed to store Province table
					coolWeatherDB.saveProvince(province);
				}
				return true;
			}
		}
		return false;
	}
	
	/**
	 * parsing and processing the data of cities which return from service
	 */
	public static boolean handleCitiesResponse(CoolWeatherDB coolWeatherDB,String response,int provinceId){
		if (!TextUtils.isEmpty(response)) {
			String[] allCitites = response.split(",");
			if (allCitites != null && allCitites.length > 0) {
				for(String c:allCitites){
					String[] array = c.split("\\|");
					City city = new City();
					city.setCityCode(array[0]);
					city.setCityName(array[1]);
					city.setProvinceId(provinceId);
					//put the data which has beeg parsed to store into City table
					coolWeatherDB.saveCity(city);
				}
				return true;
			}
		}
		return false;
	}
	
	/**
	 * parsing and processing the data of county which return from the service
	 */
	public static boolean handleCountiesResponse(CoolWeatherDB coolWeatherDB,String response,int cityId){
		if(!TextUtils.isEmpty(response)){
			String[] allCounties = response.split(",");	
			if (allCounties != null && allCounties.length >0) {
				for(String c:allCounties){
					String[] array = c.split("\\|");
					County county = new County();
					county.setCountyCode(array[0]);
					county.setCountyName(array[1]);
					county.setCityId(cityId);
					//put the data which has been parsed to store into County table
					coolWeatherDB.saveCounty(county);
				}
				return true;
			}			
		}
		return false;
	}
	
	
	/**
	 * parsing the data of JSON which return from server and the put them store in local place
	 */
	public static void handleWeatherResponse(Context context, String response){
		try {
			JSONObject jsonObject = new JSONObject(response);
			JSONObject data = jsonObject.getJSONObject("data");
			JSONArray forecast = data.getJSONArray("forecast");
			JSONObject weatherInfo = forecast.getJSONObject(0);
			String cityName = data.getString("city");			
//			String weatherCode = weatherInfo.getString("cityid");
			String temp1 = weatherInfo.getString("high");			
			String temp2 = weatherInfo.getString("low");			
			String weatherDesp =weatherInfo.getString("type");			
			String publishTime = weatherInfo.getString("date");
			saveWeatherInfo(context,cityName,temp1,temp2,weatherDesp,publishTime);			
		} catch (JSONException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	
	
	/**
	 * put the weather information return from server store in the file of SharedPreferences
	 */
	public static void saveWeatherInfo(Context context,String cityName, String temp1, String temp2, String weatherDesp, String publishTime){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyƒÍM‘¬d»’",Locale.CHINA);
		SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
		editor.putBoolean("city_selected", true);
		editor.putString("city_name", cityName);
//		editor.putString("weather_code", weatherCode);
		editor.putString("temp1", temp1);
		editor.putString("temp2", temp2);
		editor.putString("weather_desp", weatherDesp);
		editor.putString("publish_time", publishTime);
		editor.putString("current_date", sdf.format(new Date()));
		editor.commit();
	}
	
}
