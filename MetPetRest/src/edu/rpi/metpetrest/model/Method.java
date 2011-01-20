package edu.rpi.metpetrest.model;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

public class Method {
	@XmlAttribute(name = "name")
	private String name;

	@XmlAttribute(name = "lab")
	private String lab;

	@XmlElement(name = "item")
	private List<Item> items = new ArrayList<Item>();

	public Method() {
	}

	public Method(String lab, String name) {
		this.lab = lab;
		this.name = name;

	}

	public void addItem(Item item) {
		this.items.add(item);
	}

	public void addItems(List<Item> items) {
		for (Item currItem : items) {
			if (currItem.getName() != null && !currItem.getName().equals(""))
				this.items.add(currItem);
		}
	}

	public List<Item> getItems() {
		return items;
	}
}
