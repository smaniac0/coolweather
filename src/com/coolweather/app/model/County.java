package com.coolweather.app.model;

import android.R.integer;
import android.provider.MediaStore.Video;



public class County {

	private int id;
	private String countyName;
	private String countCode;
	private int cityId;
	
	public int getId(){
		return id;
	}
	
	public void setId(int id){
		this.id = id;
	}
	
	public String getCountyName(){
		return countyName;
	}
	
	public void setCountyName(String countyName){
		this.countyName = countyName;
	}
	
	public String getCountyCode(){
		return countCode;
	}
	
	public void setCountyCode(String countyCode){
		this.countCode = countyCode;
	}
	
	public int getCityId(){
		return cityId;
	}
	
	public void setCityId(int cityId){
		this.cityId = cityId;
	}
}
