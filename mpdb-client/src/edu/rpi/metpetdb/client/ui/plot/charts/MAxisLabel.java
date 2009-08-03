package edu.rpi.metpetdb.client.ui.plot.charts;

import java.util.Enumeration;

import com.objetdirect.tatami.client.gfx.Text;

public class MAxisLabel {
	private String text;
	private double value;
	
	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
	
	public void setValue(double value){
		this.value = value;
	}
	
	public double getValue(){
		return value;
	}
	
	public MAxisLabel(final String text) {
		this.text = text;
	}
	
	public MAxisLabel(final String text, final double value) {
		this(text);
		this.value = value;
	}
}
