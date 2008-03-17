package edu.rpi.metpetdb.server.impl;

import java.io.FileInputStream;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import com.google.gwt.user.client.ui.Grid;

import edu.rpi.metpetdb.client.error.InvalidFormatException;
import edu.rpi.metpetdb.client.model.SampleDTO;
import edu.rpi.metpetdb.client.service.BulkUploadService;
import edu.rpi.metpetdb.server.MpDbServlet;
import edu.rpi.metpetdb.server.bulk.upload.sample.SampleParser;

public class BulkUploadServiceImpl extends MpDbServlet implements BulkUploadService {
	private static final long serialVersionUID = 1L;
	
public Collection<SampleDTO> validate(final String fileOnServer) {
	final Grid table = new Grid();
		
	try{
		final SampleParser sp = new SampleParser(new FileInputStream(fileOnServer));
		sp.initialize();
	} catch (Exception e){
		
		//what to throw?
	}
	final Set<SampleParser.Index> cell_errors = new HashSet<SampleParser.Index>();
	final Set<Integer> col_errors = new HashSet<Integer>();
	final Set<Integer> row_errors = new HashSet<Integer>();
	/*List<List<String>> output = sp.validate(cell_errors, col_errors, row_errors);
	table.resize(output.size(), output.get(0).size());
	Integer i = 0;
	for(List<String> row : output){
		Integer j = 0;
		for(String cell : row){
			table.setText(i, j, cell);
			j++;
		}
		i++;
	}*/
	
	return new HashSet<SampleDTO>();
}
public String save(final String fileOnServer) throws InvalidFormatException{
	return "";
}
}
