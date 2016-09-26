package com.example.gdweather.model;

public class City {

	private int cityId;
	private int provinceId;
	private String cityName;
	private String cityCode;

	public City(int cityId, int provinceId, String cityName, String cityCode) {
		super();
		this.cityId = cityId;
		this.provinceId = provinceId;
		this.cityName = cityName;
		this.cityCode = cityCode;
	}

	public City() {

	}

	@Override
	public String toString() {
		return "City [cityId=" + cityId + ", provinceId=" + provinceId
				+ ", cityName=" + cityName + ", cityCode=" + cityCode + "]";
	}

	public int getCityId() {
		return cityId;
	}

	public void setCityId(int cityId) {
		this.cityId = cityId;
	}

	public int getProvinceId() {
		return provinceId;
	}

	public void setProvinceId(int provinceId) {
		this.provinceId = provinceId;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public String getCityCode() {
		return cityCode;
	}

	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}

}
