package edu.rpi.metpetdb.client.model;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Store;

import edu.rpi.metpetdb.client.model.interfaces.HasChildren;
import edu.rpi.metpetdb.client.model.interfaces.IHasName;

public class Mineral extends MObject implements IHasName, HasChildren<Mineral> {
	private static final long serialVersionUID = 1L;
	private short id;
	private Short parentId;
	private Short realMineralId;
	@Field(index = Index.TOKENIZED, store = Store.NO)
	private String name;

	private Set<Mineral> children;
	private Set<Mineral> parents;

	
	/*Used to remove the substring "(x)" if found in the name of the mineral.
	 *For instance Biotite (x) should just be Biotite since (x) just means
	 *that the mineral exists for that particular sample.*/
	private String strip_x(String sample) {
		int begin_substring;
		
		if ((begin_substring = sample.lastIndexOf("(x)")) != -1) {
			/*begin_substring - 1 because of space before mineral and (x).
			 *(Ex) Biotite (x)*/
			sample = sample.substring(0, begin_substring - 1);
		}
		return sample;
	}
		
	public short getId() {
		return id;
	}

	public void setId(final short i) {
		id = i;
	}

	public String getName() {
		return strip_x(name);
	}

	public void setName(final String s) {
		name = s;
	}

	public Short getParentId() {
		return parentId;
	}

	public void setParentId(final Short i) {
		parentId = i;
	}

	public void setChildren(final Set<Mineral> c) {
		children = c;
	}

	public Set<Mineral> getChildren() {
		return children;
	}

	public Set<Mineral> getParents() {
		return parents;
	}

	public void setParents(Set<Mineral> parents) {
		this.parents = parents;
	}

	public Short getRealMineralId() {
		return realMineralId;
	}

	public void setRealMineralId(Short realMineralId) {
		this.realMineralId = realMineralId;
	}

	public boolean equals(final Object o) {
		boolean isEquals;
		if (o instanceof Mineral) {
			name = this.getName();
			Mineral sample_object = (Mineral) o;
			String value = sample_object.getName();
			isEquals = name.toLowerCase().equals(value.toLowerCase());
		} else if (o instanceof SampleMineral) {
			name = this.getName();
			Mineral sample_object = ((SampleMineral) o).getMineral();
			String value = sample_object.getName();
			isEquals = name.toLowerCase().equals(value.toLowerCase());
		} else {
			isEquals = false;
		}
		return isEquals;
	}

	public int hashCode() {
		return name != null ? name.hashCode() + id : 0;
	}

	public String toString() {
		return this.getName();
	}

	public boolean mIsNew() {
		return id == 0;
	}

	/**
	 * Forces hibernate to load the children as well. We could have put
	 * lazy=false in Mineral.hbm.xml but then that would cause the children to
	 * be loaded all the time when we only want it for this constraint.
	 * 
	 * @param minerals
	 */
	public static void loadChildren(final Collection<Mineral> minerals) {
		if (minerals == null)
			return;
		final Iterator<Mineral> itr = minerals.iterator();
		while (itr.hasNext()) {
			loadChildren(itr.next().getChildren());
		}
	}
}
