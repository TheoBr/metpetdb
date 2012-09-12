package edu.rpi.metpetdb.gwt.client.dto;

import java.io.Serializable;

import com.google.gwt.validation.client.NotNull;
import com.google.gwt.validation.client.interfaces.IValidatable;

public class UploadRequestDTO implements IValidatable, Serializable {

	@NotNull
	private String fileName;
	
	@NotNull
	private byte[] file;
	
	public UploadRequestDTO()
	{
		
	}
	
	public UploadRequestDTO (String fileName)
	{
		this.fileName = fileName;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public byte[] getFile() {
		return file;
	}

	public void setFile(byte[] file) {
		this.file = file;
	}
	
}
