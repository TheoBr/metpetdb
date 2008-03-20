package edu.rpi.metpetdb.client.paging;

import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;

import edu.rpi.metpetdb.client.model.MObjectDTO;

public class Results<T extends MObjectDTO> implements IsSerializable {

	private List<T> list;
	private int count;

	public Results() {

	}

	public Results(final int count, final List<T> list) {
		this.list = list;
		this.count = count;
	}

	public List<T> getList() {
		return list;
	}

	public void setList(List<T> data) {
		this.list = data;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

}
