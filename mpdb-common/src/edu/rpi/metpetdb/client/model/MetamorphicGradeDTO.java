package edu.rpi.metpetdb.client.model;

//import java.util.Set;



import edu.rpi.metpetdb.client.error.InvalidPropertyException;

public class MetamorphicGradeDTO extends MObjectDTO {
	public static final int P_name = 0;

	private short id;
    
	private String name;

	public short getId() {
		return id;
	}

	public void setId(final short i) {
		id = i;
	}

	public String getName() {
		return name;
	}

	public void setName(final String s) {
		name = s;
	}

	public boolean equals(final Object o) {
		return name != null && o instanceof MetamorphicGradeDTO
				&& name.equals(((MetamorphicGradeDTO) o).name)
				&& ((MetamorphicGradeDTO) o).getId() == id;
	}

	public int hashCode() {
		return name != null ? name.hashCode() + id : 0;
	}

	public String toString() {
		return name;
	}

	public boolean mIsNew() {
		return id == 0;
	}
	protected Object mSetGet(final int propertyId, final Object newValue) {
		switch (propertyId) {
			case P_name :
				if (newValue != GET_ONLY)
					setName((String) newValue);
				return getName();
		}
		throw new InvalidPropertyException(propertyId);
	}
}