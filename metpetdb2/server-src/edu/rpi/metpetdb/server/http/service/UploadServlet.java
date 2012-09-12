package edu.rpi.metpetdb.server.http.service;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import edu.rpi.metpetdb.server.ReferenceUploadZipHelper;

//import edu.rpi.metpetdb.gwt.client.UploadService;
//import edu.rpi.metpetdb.gwt.client.dto.UploadRequestDTO;
//import edu.rpi.metpetdb.gwt.client.dto.UserDTO;

public class UploadServlet extends HttpServlet {

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		ServletFileUpload upload = new ServletFileUpload();

		List<FileItem> items = parseMultipartRequestForFiles(request, upload);

		WebApplicationContext ctx = WebApplicationContextUtils
				.getWebApplicationContext(this.getServletContext());

		Long userId = (Long) request.getSession().getAttribute("userId");

		StringBuffer results = new StringBuffer();
		
		//Upload each reference zip file
		for (FileItem fi : items) {
			if (fi.isFormField())
				continue;
			else

			{
				ReferenceUploadZipHelper ruzh = (ReferenceUploadZipHelper) ctx.getBean("referenceUploadZipHelper");
				results.append (ruzh.upload(fi.get(), fi.getName()));
			//<pre> </pre>
			}
		}

		try {
			response.getWriter().write(results.toString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private List<FileItem> parseMultipartRequestForFiles(
			HttpServletRequest request, ServletFileUpload upload) {
		DiskFileItemFactory factory = new DiskFileItemFactory(200000, new File(
				System.getProperty("java.io.tmpdir")));

		upload.setFileItemFactory(factory);

		List<FileItem> items;
		try {
			items = upload.parseRequest(request);
		} catch (FileUploadException e) {
			throw new RuntimeException(e);
		}
		return items;
	}
}
