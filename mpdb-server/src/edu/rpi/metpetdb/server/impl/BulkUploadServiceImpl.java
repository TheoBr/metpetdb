package edu.rpi.metpetdb.server.impl;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.gwt.user.client.ui.Grid;

import edu.rpi.metpetdb.client.error.InvalidFormatException;
import edu.rpi.metpetdb.client.service.BulkUploadService;
import edu.rpi.metpetdb.server.MpDbServlet;
import edu.rpi.metpetdb.server.bulk.upload.sample.SampleParser;

public class BulkUploadServiceImpl extends MpDbServlet implements BulkUploadService {
	private static final long serialVersionUID = 1L;
	
public Grid validate(final String fileOnServer) throws IOException{
	final Grid table = new Grid();
		final SampleParser sp = new SampleParser(new FileInputStream(fileOnServer));
	try{
		sp.initialize();
	} catch (IOException ioe){
		throw ioe;
		//what to throw?
	}
	final Set<SampleParser.Index> cell_errors = new HashSet<SampleParser.Index>();
	final Set<Integer> col_errors = new HashSet<Integer>();
	final Set<Integer> row_errors = new HashSet<Integer>();
	List<List<String>> output = sp.validate(cell_errors, col_errors, row_errors);
	table.resize(output.size(), output.get(0).size());
	Integer i = 0;
	for(List<String> row : output){
		Integer j = 0;
		for(String cell : row){
			table.setText(i, j, cell);
			j++;
		}
		i++;
	}
	
	return table;
}
public String save(final String fileOnServer) throws IOException, InvalidFormatException{
	return "";
}
}
