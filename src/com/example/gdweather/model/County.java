package com.example.gdweather.model;

public class County {

	private int countyId;
	private int cityId;
	private String countyName;
	private String countyCode;

	public County(int countyId, int cityId, String countyName, String countyCode) {
		super();
		this.countyId = countyId;
		this.cityId = cityId;
		this.countyName = countyName;
		this.countyCode = countyCode;
	}

	public County() {

	}

	@Override
	public String toString() {
		return "County [countyId=" + countyId + ", cityId=" + cityId
				+ ", countyName=" + countyName + ", countyCode=" + countyCode
				+ "]";
	}

	public int getCountyId() {
		return countyId;
	}

	public void setCountyId(int countyId) {
		this.countyId = countyId;
	}

	public int getCityId() {
		return cityId;
	}

	public void setCityId(int cityId) {
		this.cityId = cityId;
	}

	public String getCountyName() {
		return countyName;
	}

	public void setCountyName(String countyName) {
		this.countyName = countyName;
	}

	public String getCountyCode() {
		return countyCode;
	}

	public void setCountyCode(String countyCode) {
		this.countyCode = countyCode;
	}

}
