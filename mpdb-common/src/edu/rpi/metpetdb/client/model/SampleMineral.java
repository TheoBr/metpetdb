package edu.rpi.metpetdb.client.model;

import edu.rpi.metpetdb.client.error.InvalidPropertyException;

public class SampleMineral extends Mineral {
	public static final int P_amount = 0;

	private Float amount;
	private Mineral mineral;

	public void setAmount(final Float f) {
		amount = f;
	}
	public Float getAmount() {
		if (amount == null)
			return new Float(0);
		else
			return amount;
	}

	public void setMineral(final Mineral m) {
		mineral = m;
		if (m != null) {
			this.setName(m.getName());
			this.setParentId(m.getParentId());
			this.setChildren(m.getChildren());
			this.setId(m.getId());
		}
	}
	public Mineral getMineral() {
		return mineral;
	}

	public String getName() {
		return toString();
	}

	public String toString() {
		if (amount != null)
			return mineral.getName() + " (" + amount + ")";
		else
			return mineral.getName();
	}

	public boolean equals(final Object o) {
		if (o instanceof SampleMineral) {
			final boolean one = ((SampleMineral) o).getMineral()
					.equals(mineral);
			final boolean two = ((SampleMineral) o).getAmount() == null
					? ((SampleMineral) o).getAmount() == amount
					: ((SampleMineral) o).getAmount().equals(amount);
			return one && two;
		} else if (o instanceof Mineral) {
			return ((Mineral) o).equals(mineral);
		}
		return false;
	}

	public int hashCode() {
		return mineral != null && amount != null ? mineral.hashCode()
				+ amount.intValue() : mineral.hashCode();
		// return mineral.hashCode();
	}

	protected Object mSetGet(final int propertyId, final Object newValue) {

		switch (propertyId) {
			case P_amount :
				if (newValue != GET_ONLY)
					setAmount(setFloatValue(newValue));
				return getAmount();
		}
		throw new InvalidPropertyException(propertyId);
	}
}
