package com.bvb.agroGenius.utils;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.nimbusds.jose.shaded.gson.Gson;
import com.nimbusds.jose.shaded.gson.JsonObject;

public class GoogleOAuthUtil {
	
	
	public static String getOauthAccessTokenGoogle(String code) {
		    RestTemplate restTemplate = new RestTemplate();
		    HttpHeaders httpHeaders = new HttpHeaders();
		    httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

		    MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		    params.add("code", code);
		    params.add("redirect_uri", "http://localhost:8081/api/v1/user/signInWithGoogle");
		    params.add("client_id", "663843043809-4mfmmkl3vafhvjh0u17hk5v1cjh7p1hc.apps.googleusercontent.com");
		    params.add("client_secret", "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");
		    params.add("scope", "https%3A%2F%2Fwww.googleapis.com%2Fauth%2Fuserinfo.profile");
		    params.add("scope", "https%3A%2F%2Fwww.googleapis.com%2Fauth%2Fuserinfo.email");
		    params.add("scope", "openid");
		    params.add("grant_type", "authorization_code");

		    HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(params, httpHeaders);

		    String url = "https://oauth2.googleapis.com/token";
		    String response = restTemplate.postForObject(url, requestEntity, String.class);
		    	    		
		    return response;
		}
		
		public static JsonObject getProfileDetailsGoogle(String accessToken) {
			RestTemplate restTemplate = new RestTemplate();
		    HttpHeaders httpHeaders = new HttpHeaders();
		    httpHeaders.setBearerAuth(accessToken);

		    HttpEntity<String> requestEntity = new HttpEntity<>(httpHeaders);

		    String url = "https://www.googleapis.com/oauth2/v2/userinfo";
		    ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, requestEntity, String.class);
		    JsonObject jsonObject = new Gson().fromJson(response.getBody(), JsonObject.class);
		    return jsonObject;
		}

}
