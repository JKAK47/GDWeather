package com.example.gdweather.model;

public class Province {

	private int provinceId;
	private String provinceName;
	private String provinceCode;

	public Province(int provinceId, String provinceName, String provinceCode) {
		super();
		this.provinceId = provinceId;
		this.provinceName = provinceName;
		this.provinceCode = provinceCode;
	}

	public Province() {

	}

	@Override
	public String toString() {
		return "Province [provinceId=" + provinceId + ", provinceName="
				+ provinceName + ", provinceCode=" + provinceCode + "]";
	}

	public int getProvinceId() {
		return provinceId;
	}

	public void setProvinceId(int provinceId) {
		this.provinceId = provinceId;
	}

	public String getProvinceName() {
		return provinceName;
	}

	public void setProvinceName(String provinceName) {
		this.provinceName = provinceName;
	}

	public String getProvinceCode() {
		return provinceCode;
	}

	public void setProvinceCode(String provinceCode) {
		this.provinceCode = provinceCode;
	}

}
