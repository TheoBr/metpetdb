package edu.rpi.metpetdb.client.ui;

import java.util.ArrayList;

/*
 * TreeNode used for the breadcrumbs class
 */
public class bcNode {
	private bcNode parent;
	private ArrayList<bcNode> children;
	private String name;
	private String token;
	private String leftSide;
	private boolean isScreen;

	public bcNode() {
		children = new ArrayList<bcNode>();
		isScreen = true;
		name = "";
		token = "";
		leftSide = "";
	}

	public bcNode(final bcNode parent) {
		this();
		this.parent = parent;
	}

	public bcNode(final String name) {
		this();
		this.name = name;
	}

	public bcNode(final bcNode parent, final String name) {
		this();
		this.parent = parent;
		this.name = name;
	}

	public bcNode(final bcNode parent, final String name, final String token) {
		this();
		this.parent = parent;
		this.name = name;
		this.token = token;
	}

	public void setParent(final bcNode parent) {
		this.parent = parent;
	}

	public bcNode getParent() {
		return parent;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void addChild(final bcNode child) {
		children.add(child);
	}

	public ArrayList<bcNode> getChildren() {
		return children;
	}

	public void setToken(final String token) {
		this.token = token;
	}

	public String getToken() {
		return token;
	}

	public void setScreen(final boolean isScreen) {
		this.isScreen = isScreen;
	}

	public boolean isScreen() {
		return isScreen;
	}

	public void setLeftSide(final String leftSide) {
		this.leftSide = leftSide;
	}

	public String getLeftSide() {
		return leftSide;
	}
}
