package edu.rpi.metpetdb.server.model;

import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.IndexedEmbedded;
import org.hibernate.search.annotations.Store;

public class SampleMineral extends MObject {
	private static final long serialVersionUID = 1L;

	@Field(index = Index.TOKENIZED, store = Store.NO)
	private Float amount;

	@IndexedEmbedded(depth = 1, prefix = "mineral_")
	private Mineral mineral;

	private Sample sample;

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
	}

	public Mineral getMineral() {
		return mineral;
	}

	public Sample getSample() {
		return sample;
	}

	public void setSample(Sample sample) {
		this.sample = sample;
	}

	public String getName() {
		return toString();
	}

	public String toString() {
		if (mineral != null) {
			if (amount != null)
				return mineral.getName() + " (" + amount + ")";
			else
				return mineral.getName();
		} else {
			return super.toString();
		}
	}

	public boolean equals(final Object o) {
		if (o instanceof SampleMineral) {
			final boolean one = ((SampleMineral) o).getMineral()
					.equals(mineral);
			final boolean two = ((SampleMineral) o).getAmount() == null ? ((SampleMineral) o)
					.getAmount() == amount
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

	@Override
	public boolean mIsNew() {
		return false;
	}
}
