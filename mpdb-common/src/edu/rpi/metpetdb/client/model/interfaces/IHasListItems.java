package edu.rpi.metpetdb.client.model.interfaces;

//import java.util.Collection;
//
//import edu.rpi.metpetdb.client.model.MObjectDTO;

//TODO: make a Collection instead of a String[]
public interface IHasListItems {

	public String[] getListItems();

	// no errors locally
	// public Collection<MObjectDTO> getListItems();

	/**
	 * These functions have these errors: "Syntax error on token "extends", ,
	 * expected" getListItems2: "Return type for the method is missing"
	 * getListItems3: "T cannot be resolved to a type"
	 */
	// public Collection<T extends MObjectDTO> getListItems2();
	// public Collection<T> getListItems3();
}
