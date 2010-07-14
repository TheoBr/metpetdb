package edu.rpi.metpetdb.client.model;

import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.IndexedEmbedded;
import org.hibernate.search.annotations.Store;

public class SampleMineral extends MObject implements Comparable<MObject> {
	private static final long serialVersionUID = 1L;

	@Field(index = Index.TOKENIZED, store = Store.NO)
	private String amount;

	@IndexedEmbedded(depth = 1, prefix = "mineral_")
	private Mineral mineral;

	private Sample sample;

	public SampleMineral() {
		
	}
	
	/**
	 * Creates a new SampleMineral with the given value as its Mineral.
	 * 
	 * @param mineral
	 */
	public SampleMineral(Mineral mineral) {
		super();
		this.mineral = mineral;
	}
	
	
	/**
	 * Used to remove the substring "(x)" if found in the name of the mineral.
	 * For instance Biotite (x) should just be Biotite since (x) just means
	 * that the mineral exists for that particular sample.
	 */
	private String strip_x(String sample_mineral) {
		int begin_substring;
		
		if ((begin_substring = sample_mineral.lastIndexOf("(x)")) != -1) {
			/*begin_substring - 1 because of space before mineral and (x).
			 *(Ex) Biotite (x)*/
			sample_mineral = sample_mineral.substring(0, begin_substring-1);
		}
		return sample_mineral;
	}
	
	
	public void setAmount(final String f) {
		amount = f;
	}

	public String getAmount() {
		if (amount == null)
			return "";
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
		String sample_name;
		
		if (mineral != null) {
			sample_name = strip_x(mineral.getName());
		} else {
			sample_name = super.toString();
		}
		return sample_name;
	}

	public String toString() {
		return this.getName();
	}

	public boolean equals(final Object o) {
		boolean isEquals;
		if (o instanceof SampleMineral) {
			Mineral sample_obj = ((SampleMineral) o).getMineral();
			isEquals = sample_obj.equals(mineral);
		} else if (o instanceof Mineral) {
			Mineral sample_obj = (Mineral) o;
			isEquals = sample_obj.equals(mineral);
		} else {
			isEquals = false;
		}
		return isEquals;
	}

	public int hashCode() {
		return mineral != null && amount != null ? mineral.hashCode()
				+ amount.hashCode() : mineral.hashCode();
	}

	@Override
	public boolean mIsNew() {
		return false;
	}

	public int compareTo(MObject sm) {
		if (sm instanceof SampleMineral){
			final Mineral sample_obj = ((SampleMineral)sm).getMineral();
			return this.mineral.getName().compareTo(sample_obj.getName());
		}
		else if (sm instanceof Mineral) {
			return this.mineral.getName().compareTo(((Mineral)sm).getName());
		}
		else {
			throw new ClassCastException("Can only compare SampleMineral to " +
					"SampleMineral or Mineral types");
		}
	}
}
