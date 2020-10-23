package com.developerblog.app.security;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import io.jsonwebtoken.Jwts;


public class AutorizationFilter extends BasicAuthenticationFilter{

	public AutorizationFilter(AuthenticationManager authenticationManager) {
		super(authenticationManager);
	}

	@Override
	protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
			throws IOException, ServletException {
		
		   String header = req.getHeader(SecurityConstants.HEADER_STRING);
	        
	        if (header == null || !header.startsWith(SecurityConstants.TOKEN_PREFIX)) {
	            chain.doFilter(req, res);
	            return;
	        }
		
	        UsernamePasswordAuthenticationToken authentication = getAuthentication(req);
	        SecurityContextHolder.getContext().setAuthentication(authentication);
	        chain.doFilter(req, res);
	}
	
	 private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
	        String token = request.getHeader(SecurityConstants.HEADER_STRING);
	        
	        if (token != null) {
	            
	            token = token.replace(SecurityConstants.TOKEN_PREFIX, "");
	            
	            String user = Jwts.parser()
	                    .setSigningKey(SecurityConstants.TOKEN_SECRET )
	                    .parseClaimsJws( token )
	                    .getBody()
	                    .getSubject();
	            
	            if (user != null) {
	                return new UsernamePasswordAuthenticationToken(user, null, (Collection<? extends GrantedAuthority>) new ArrayList<Object>());
	            }
	            
	            return null;
	        }
	        
	        return null;
	    }

	
	

}
