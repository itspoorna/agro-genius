package com.bvb.agroGenius.utils;

import java.nio.charset.StandardCharsets;

import javax.crypto.SecretKey;

import com.bvb.agroGenius.security.SecurityConstants;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

public class JWTUtil {
	public static Claims getAllClaimsFromToken(String jwt) {
	    Claims claims = null;
	    try {
	    	SecretKey key = Keys.hmacShaKeyFor(
                    SecurityConstants.JWT_KEY.getBytes(StandardCharsets.UTF_8));
	    	claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(jwt).getBody();
	    } catch (Exception e) {
	        claims = null;
	    }
	    return claims;
	}
}
