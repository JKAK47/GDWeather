package com.example.gdweather.util;

public interface HttpCallbackListener {

	public void onFinish(String result);

	public void onError(Exception e);
}
