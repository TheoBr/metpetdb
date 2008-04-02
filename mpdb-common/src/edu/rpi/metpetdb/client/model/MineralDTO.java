package edu.rpi.metpetdb.client.model;

import java.util.Set;

import edu.rpi.metpetdb.client.model.interfaces.IHasChildren;
import edu.rpi.metpetdb.client.model.interfaces.IHasName;

public class MineralDTO extends MObjectDTO implements IHasName,
		IHasChildren<MineralDTO> {

	private static final long serialVersionUID = 1L;

	private short id;
	private Short parentId;

	private String name;

	private Set<MineralDTO> children;

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

	public Short getParentId() {
		return parentId;
	}

	public void setParentId(final Short i) {
		parentId = i;
	}

	public void setChildren(final Set<MineralDTO> c) {
		children = c;
	}

	public Set<MineralDTO> getChildren() {
		return children;
	}

	public boolean equals(final Object o) {
		if (o instanceof MineralDTO)
			return name != null && o instanceof MineralDTO
					&& name.equals(((MineralDTO) o).name)
					&& ((MineralDTO) o).getId() == id;
		else if (o instanceof SampleMineralDTO) {
			return name != null
					&& name.equals(((SampleMineralDTO) o).getName())
					&& ((SampleMineralDTO) o).getMineral().getId() == id;
		} else
			return false;
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
}
