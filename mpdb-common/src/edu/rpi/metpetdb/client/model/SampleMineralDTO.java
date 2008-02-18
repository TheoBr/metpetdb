package edu.rpi.metpetdb.client.model;

import edu.rpi.metpetdb.client.error.InvalidPropertyException;

public class SampleMineralDTO extends MineralDTO {
	public static final int P_amount = 0;

	private Float amount;
	private MineralDTO mineral;

	public void setAmount(final Float f) {
		amount = f;
	}
	public Float getAmount() {
		if (amount == null)
			return new Float(0);
		else
			return amount;
	}

	public void setMineral(final MineralDTO m) {
		mineral = m;
		if (m != null) {
			this.setName(m.getName());
			this.setParentId(m.getParentId());
			this.setChildren(m.getChildren());
			this.setId(m.getId());
		}
	}
	public MineralDTO getMineral() {
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
		if (o instanceof SampleMineralDTO) {
			final boolean one = ((SampleMineralDTO) o).getMineral()
					.equals(mineral);
			final boolean two = ((SampleMineralDTO) o).getAmount() == null
					? ((SampleMineralDTO) o).getAmount() == amount
					: ((SampleMineralDTO) o).getAmount().equals(amount);
			return one && two;
		} else if (o instanceof MineralDTO) {
			return ((MineralDTO) o).equals(mineral);
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
