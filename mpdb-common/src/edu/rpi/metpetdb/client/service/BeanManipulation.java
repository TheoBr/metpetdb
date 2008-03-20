package edu.rpi.metpetdb.client.service;

import com.google.gwt.user.client.rpc.AsyncCallback;

import edu.rpi.metpetdb.client.model.MObjectDTO;
import edu.rpi.metpetdb.client.paging.PaginationParameters;

public interface BeanManipulation {
	
	void all(PaginationParameters parameters, AsyncCallback ac);
	void details(long id, AsyncCallback ac);
	void save(MObjectDTO bean, AsyncCallback ac);
	void delete(long id, AsyncCallback ac);

}
