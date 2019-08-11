package com.springbook.biz.auth;

import java.util.Iterator;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.model.OAuth2AccessToken;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Response;
import com.github.scribejava.core.model.Verb;
import com.github.scribejava.core.oauth.OAuth20Service;
import com.springbook.biz.user.UserVO;

public class SNSLogin {
	private OAuth20Service oauthService;
	private SnsValue sns;
	
	public SNSLogin(SnsValue sns) {
		if(sns.getService().equals("facebook")) {
			this.oauthService = new ServiceBuilder(sns.getClientId())
					.apiSecret(sns.getClientSecret())
					.callback(sns.getRedirectUrl())
					.scope("public_profile")
					.build(sns.getApi20Instance());
		} else {
			this.oauthService = new ServiceBuilder(sns.getClientId())
					.apiSecret(sns.getClientSecret())
					.callback(sns.getRedirectUrl())
					.scope("profile")
					.build(sns.getApi20Instance());
		}
		
		this.sns = sns;
	}
	
	public String getNaverAuthURL() {
		return this.oauthService.getAuthorizationUrl();
	}

	public UserVO getUserProfile(String code) throws Exception {
		OAuth2AccessToken accessToken = oauthService.getAccessToken(code);
		OAuthRequest request = new OAuthRequest(Verb.GET, this.sns.getProfileUrl());
		oauthService.signRequest(accessToken, request);
		
		Response response = oauthService.execute(request);
		System.out.println("getBody : " + response.getBody());
		return parseJson(response.getBody());
	}

	private UserVO parseJson(String body) throws Exception {
		System.out.println("============================\n" + body + "\n==================");
		UserVO user = new UserVO();
		//json을 object로 매핑
		ObjectMapper mapper = new ObjectMapper();
		JsonNode rootNode = mapper.readTree(body);
		
		if (this.sns.isGoogle()) {
			String id = rootNode.get("id").asText();
			if (sns.isGoogle())
				user.setGoogleid(id);
			user.setNickname(rootNode.get("displayName").asText());
			JsonNode nameNode = rootNode.path("name");
			String uname = nameNode.get("familyName").asText() + nameNode.get("givenName").asText();
			user.setName(uname);

			Iterator<JsonNode> iterEmails = rootNode.path("emails").elements();
			while(iterEmails.hasNext()) {
				JsonNode emailNode = iterEmails.next();
				String type = emailNode.get("type").asText();
				if (StringUtils.equals(type, "account")) {
					user.setEmail(emailNode.get("value").asText());
					break;
				}
			}
			
		} else if (this.sns.isNaver()) {
			JsonNode resNode = rootNode.get("response");
			user.setName(resNode.get("name").asText());
			user.setNaverid(resNode.get("id").asText());
			user.setNickname(resNode.get("nickname").asText());
			user.setEmail(resNode.get("email").asText());
		} else if (this.sns.isFacebook()) {
			String id = rootNode.get("id").asText();
			String name = rootNode.get("name").asText();
			if (sns.isFacebook())
				user.setFacebookid(id);
			
			user.setName(rootNode.get("name").asText());
			user.setEmail(rootNode.get("email").asText());
		}
		
		return user;
	}
	
	
}
