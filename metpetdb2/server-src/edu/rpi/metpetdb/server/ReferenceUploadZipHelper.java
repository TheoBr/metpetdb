package edu.rpi.metpetdb.server;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.hibernate.exception.ConstraintViolationException;

import edu.rpi.metpetdb.server.service.ReferenceUploadService;

public class ReferenceUploadZipHelper {

	
	private ReferenceUploadService referenceUploadService;
	
	public String upload(byte[] zipFileBytes, String fileName)
	{
		ZipFile zipFile = null;
		File tempZip = null;
		StringBuffer buff = new StringBuffer();
		//Make a zip file from the uploaded file
		
		try {
			tempZip = File.createTempFile(fileName, ".tmp", new File(System.getProperty("java.io.tmpdir")));

			FileOutputStream fos = new FileOutputStream(tempZip);
			fos.write(zipFileBytes);
			fos.flush();
			fos.close();
			
			zipFile = new ZipFile(tempZip);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		Enumeration<? extends ZipEntry> e = zipFile.entries();
		
		//Try to parse and upload every zip entry that is a .txt file to the georeference table
		while (e.hasMoreElements())
		{
			ZipEntry zipEntry = e.nextElement();
			
			ByteArrayOutputStream baos = null;
			BufferedInputStream bis = null;
			
			try
			{
				
				//Skip directory names and non parseable files
				if (!zipEntry.getName().endsWith(".txt"))
					continue;
				
			 	bis = new BufferedInputStream(zipFile.getInputStream(zipEntry));
			 	baos =	new ByteArrayOutputStream();
				
			
			int bytes_read = 0;
			byte [] chunk = new byte[8096];
			
			while ((bytes_read = bis.read(chunk)) > 0)
					{
					baos.write(chunk, 0, bytes_read);
					}
			
			baos.flush();
			
			referenceUploadService.upload(new String(baos.toByteArray()));
			
			buff.append(zipEntry.getName() + " " + " successfully uploaded<BR>");	
			}
			catch (ConstraintViolationException cve)
			{
				buff.append(zipEntry.getName() + " " + " has been previously uploaded<BR>");
			}
			catch (IllegalArgumentException iae)
			{
				buff.append(iae.getMessage());
			}
			catch (Exception io)
			
			{
				throw new RuntimeException(io);
			}
			finally
			{
				try {
					if (baos != null)
						baos.close();
				} catch (IOException e1) {
					throw new RuntimeException(e1);
				}
				baos = null;
				
				try {
					if (bis != null)
						bis.close();
				} catch (IOException e1) {
					throw new RuntimeException(e1);
				}
				bis = null;
				
			}
		}
		
		
		tempZip.delete();
		
		try {
			zipFile.close();
		} catch (IOException e1) {
			throw new RuntimeException(e1);
		}
		
		return buff.toString();

	}

	public ReferenceUploadService getReferenceUploadService() {
		return referenceUploadService;
	}

	public void setReferenceUploadService(
			ReferenceUploadService referenceUploadService) {
		this.referenceUploadService = referenceUploadService;
	}
}
