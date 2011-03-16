package edu.rpi.metpetrest;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.filter.OncePerRequestFilter;

public class MyJsonpFilter extends OncePerRequestFilter {

	@Override
	protected void doFilterInternal(HttpServletRequest request,
			HttpServletResponse response, FilterChain chain)
			throws ServletException, IOException {

		if (request.getParameter("callback") != null) {
			String callbackName = request.getParameter("callback");

			response.getOutputStream().print(callbackName + "(");
			chain.doFilter(request, response);
			response.getOutputStream().print(");");
			response.flushBuffer();
		} else {
			chain.doFilter(request, response);
			response.flushBuffer();

		}

	}

}
