package com.springbook.biz.auth;

import com.github.scribejava.core.builder.api.DefaultApi20;

public class NaverAPI20 extends DefaultApi20 implements SnsUrls {
	//싱글톤 패턴
//	private static NaverAPI20 _instance;
//	
//	public static NaverAPI20 getInstace() {
//		if(_instance == null) {
//			_instance = new NaverAPI20();
//		}
//		return _instance;
//	}
	private NaverAPI20() {
	}
	
	private static class InstanceHolder {
		//final 타입이 한번 생성 후 수정 불가 
		private static final NaverAPI20 INSTANCE = new NaverAPI20();
	}
	
	public static NaverAPI20 instance() {
		return InstanceHolder.INSTANCE;
	}

	@Override
	public String getAccessTokenEndpoint() {
		return NAVER_ACCESS_TOKEN;
	}

	@Override
	protected String getAuthorizationBaseUrl() {
		return NAVER_AUTH;
	}
	
	
}
