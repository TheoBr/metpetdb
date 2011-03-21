package edu.rpi.metpetrest.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

public class MyAuthenticationFilter extends OncePerRequestFilter {

	private AuthenticationManager authenticationManager = null;

	@Override
	protected void doFilterInternal(HttpServletRequest req,
			HttpServletResponse res, FilterChain chain)
			throws ServletException, IOException {

		HttpServletRequest request = (HttpServletRequest) req;

		if (request.getParameter("identity") != null) {

			Integer userId = SessionEncrypter.verify(request
					.getParameter("identity"));

			if (userId != null) {
				UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
						userId, userId, new GrantedAuthority[] {});
				authenticationManager.authenticate(token);
				// Only a valid identity should get here
				chain.doFilter(req, res);
			}

		}

		// TODO:  If we can't verify you, send a 403, is this working?
		if (SecurityContextHolder.getContext().getAuthentication() != null  && SecurityContextHolder.getContext().getAuthentication().getName() != null) {
			HttpServletResponse response = (HttpServletResponse) res;
			response.setStatus(403);
		}

	}

	public AuthenticationManager getAuthenticationManager() {
		return authenticationManager;
	}

	public void setAuthenticationManager(
			AuthenticationManager authenticationManager) {
		this.authenticationManager = authenticationManager;
	}

}