package edu.rpi.metpetdb.server;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

public class BulkUploadServlet  extends HttpServlet {
	private static final long serialVersionUID = 1L;
	//private static String baseFolder = MpDbServlet.fileProps.getProperty("bulk.path");

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/plain");

		FileItem uploadItem = getFileItem(request);
		if (uploadItem == null) {
			response.getWriter().write("NO-SCRIPT-DATA");
			return;
		}

		
		
	}

	private FileItem getFileItem(HttpServletRequest request) {
		FileItemFactory factory = new DiskFileItemFactory();
		ServletFileUpload upload = new ServletFileUpload(factory);

		try {
			List items = upload.parseRequest(request);
			Iterator it = items.iterator();
			while (it.hasNext()) {
				FileItem item = (FileItem) it.next();
				if (!item.isFormField()
						&& "bulkUpload".equals(item.getFieldName())) {
					return item;
				}
			}
		} catch (FileUploadException e) {
			return null;
		}

		return null;
	}

}
