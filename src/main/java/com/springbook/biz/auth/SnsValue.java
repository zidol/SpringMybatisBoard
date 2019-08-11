package com.springbook.biz.auth;

import org.apache.commons.lang3.StringUtils;

import com.github.scribejava.apis.FacebookApi;
import com.github.scribejava.apis.GoogleApi20;
import com.github.scribejava.core.builder.api.DefaultApi20;

import lombok.Data;

@Data
public class SnsValue implements SnsUrls {
	private String service;
	private String clientId;
	private String clientSecret;
	private String redirectUrl;
	private DefaultApi20 api20Instance;
	private String profileUrl;
	
	private boolean isNaver;
	private boolean isGoogle;
	private boolean isFacebook;
	
	public SnsValue(String service, String clientId, String clientSecret, String redirectUrl) {
		this.service = service;
		this.clientId = clientId;
		this.clientSecret = clientSecret;
		this.redirectUrl = redirectUrl;
		
		this.isNaver = StringUtils.equalsIgnoreCase("naver", this.service);
		this.isGoogle = StringUtils.equalsIgnoreCase("google", this.service);
		this.isFacebook = StringUtils.equalsIgnoreCase("facebook", this.service);
		
		if (isNaver) {
			this.api20Instance = NaverAPI20.instance();
			this.profileUrl = NAVER_PROFILE_URL;
		} else if (isGoogle) {
			this.api20Instance = GoogleApi20.instance();
			this.profileUrl = GOOGLE_PROFILE_URL;
		} else if (isFacebook) {
			this.api20Instance = FacebookApi.instance();
			this.profileUrl = FACEBOOK_PROFILE_URL;
		}
	}
}
