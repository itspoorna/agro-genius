package com.bvb.agroGenius.filter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

import javax.crypto.SecretKey;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.bvb.agroGenius.security.SecurityConstants;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JWTTokenValidatorFilter  extends OncePerRequestFilter {
	
	public static final List<String> EXCLUSION_LIST = Arrays.asList("/products/getAll","/contact","/user/signUp","/user/signIn", "/user/signInWithGoogle","/validator","/validator/verify");

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		 String jwt = request.getHeader(SecurityConstants.JWT_HEADER);
	        if (null != jwt) {
	            try {
	                SecretKey key = Keys.hmacShaKeyFor(
	                        SecurityConstants.JWT_KEY.getBytes(StandardCharsets.UTF_8));

	                Claims claims = Jwts.parserBuilder()
	                        .setSigningKey(key)
	                        .build()
	                        .parseClaimsJws(jwt)
	                        .getBody();
	                String username = String.valueOf(claims.get("username"));
	                String authorities = (String) claims.get("authorities");
	                Authentication auth = new UsernamePasswordAuthenticationToken(username, null,
	                        AuthorityUtils.commaSeparatedStringToAuthorityList(authorities));
	                SecurityContextHolder.getContext().setAuthentication(auth);
	            } catch (Exception e) {
	                throw new BadCredentialsException("Invalid Token received!");
	            }

	        }
	        filterChain.doFilter(request, response);
	}
   
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return EXCLUSION_LIST.contains(request.getServletPath());
    }

}

