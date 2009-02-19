package edu.rpi.metpetdb.client.error.dao;

import edu.rpi.metpetdb.client.error.DAOException;
import edu.rpi.metpetdb.client.locale.LocaleHandler;
import edu.rpi.metpetdb.client.model.Sample;

public class SampleNotFoundException extends DAOException {
	private static final long serialVersionUID = 1L;
	private String alias;
	private long id;
	
	public SampleNotFoundException() {
		alias = "";
		id = 0;
	}
	
	public SampleNotFoundException(final Sample tryToLoad) {
		alias = tryToLoad.getAlias();
		id = tryToLoad.getId();
	}

	@Override
	public String format() {
		if (alias != null && alias.length() > 0)
			return LocaleHandler.lc_text.errorDesc_SampleNotFoundAlias(alias);
		else if (id != 0)
			return LocaleHandler.lc_text.errorDesc_SampleNotFoundId(id);
		else
			return LocaleHandler.lc_text.errorDesc_SampleNotFound();
	}

}
