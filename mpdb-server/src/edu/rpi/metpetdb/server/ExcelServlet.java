package edu.rpi.metpetdb.server;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.Session;

import edu.rpi.metpetdb.client.service.ExcelService;
public class ExcelServlet extends HttpServlet{
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(final HttpServletRequest request,
			final HttpServletResponse response) throws ServletException {
		response.setContentType("application/vnd.ms-excel");
		response.addHeader("Content-Disposition", "inline;filename=results.tsv");

		Session session = DataStore.open();
		session.beginTransaction();
		try {
			response.getWriter().write(request.getParameterValues("excel")[0]);
		
		} catch (final IOException ioe) {
			throw new IllegalStateException(ioe.getMessage());
		}
		session.close();
	}

}

