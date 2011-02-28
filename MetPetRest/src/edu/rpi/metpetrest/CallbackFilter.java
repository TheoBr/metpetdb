package edu.rpi.metpetrest;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

public class CallbackFilter implements Filter {

	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
	
		if (request.getParameter("callback") != null)
		{
			String callbackName = request.getParameter("callback");
			
			response.getOutputStream().print( callbackName + "(");
			chain.doFilter(request, response);
			response.getOutputStream().print(");");
			response.flushBuffer();
		}
		else
		{
			chain.doFilter(request, response);
			response.flushBuffer();

		}
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		// TODO Auto-generated method stub

	}

}
