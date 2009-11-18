package edu.rpi.metpetdb.server;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class svgServlet extends HttpServlet{
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(final HttpServletRequest request,
			final HttpServletResponse response) throws ServletException {
		response.setContentType("image/svg+xml");
		response.addHeader("Content-Disposition", "inline;filename=plot.svg");

		try {
			String svg;
			// If there is a GET string, fetch by ids
			if (request.getParameter("data") != null) {
				svg = request.getParameterValues("data")[0];
				response.getWriter().write(svg);
			} 

		} catch (final IOException ioe) {
			throw new IllegalStateException(ioe.getMessage());
		} finally {
		}
		
		return;
	}
}
