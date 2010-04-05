package edu.rpi.metpetdb.client.ui.input.attributes.specific;


import java.util.ArrayList;
import java.util.Set;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;

import edu.rpi.metpetdb.client.model.validation.PropertyConstraint;
import edu.rpi.metpetdb.client.ui.widgets.MGoogleEarthPopUp;
import edu.rpi.metpetdb.client.ui.widgets.MSuggestText;
import edu.rpi.metpetdb.client.ui.widgets.panels.MultipleInputPanel;

public abstract class MetamorphicRegionSuggestText extends MultipleSuggestTextAttribute {
	boolean buttonVisible;
	private MGoogleEarthPopUp earthPopup = new MGoogleEarthPopUp();

	public MetamorphicRegionSuggestText(PropertyConstraint sc, boolean addShow) {
		super(sc, addShow);
	}


	public MultipleInputPanel createOptionalSuggestBox(final String s) {
		final MultipleInputPanel panel = new MultipleInputPanel();
		final MSuggestText st = new MSuggestText(suggestions,true){
			@Override
			public void notifyCompletion(){
				onChange(this);
			}
		};
		st.suggestBox.setText(s);
		st.suggestBox.addChangeListener(this);
		st.suggestBox.addEventHandler(this);
		final FlowPanel fp= new FlowPanel();
		fp.add(st);
		Button viewRegions= new Button("View Metamorphic Regions");
		viewRegions.addClickListener(new ClickListener() {
			public void onClick(final Widget sender) {
				earthPopup.createUIMetamorphicRegions();
				earthPopup.show();
			}
				
		});
		fp.add(viewRegions);
		panel.addButton.addClickListener(new ClickListener() {
			public void onClick(final Widget sender) {
				MultipleInputPanel t = MetamorphicRegionSuggestText.this.createOptionalSuggestBox(null);
				editList.add(t);
				realEditWidgets.add(t.getInputWidget());
				setStyles();
			}
		});
		panel.removeButton.addClickListener(new ClickListener() {
			public void onClick(final Widget sender) {
				// If there are more than one entry spaces...
				if (realEditWidgets.size() > 1) {
					// remove one
					editList.remove(panel);
					realEditWidgets.remove(fp);
					onChange(panel);
				}
				setStyles();
			}
		});
		panel.setInputWidget(fp);
		return panel;
	}
	
	public void createSuggest(final Set<String> options){
		setSuggestions(options);
		ArrayList<MultipleInputPanel> realEditList = new ArrayList<MultipleInputPanel>();
		for (int i = 0; i < realEditWidgets.size(); i++) {
			MSuggestText temp = (MSuggestText)((FlowPanel) ((MultipleInputPanel) editList.getWidget(i)).getInputWidget()).getWidget(0);
//			if (temp != null) {
				MultipleInputPanel p = createOptionalSuggestBox(temp.getText());
				realEditList.add(p);
				realEditWidgets.set(i, p.getInputWidget());
				temp.suggestBox.addChangeListener(this);
				temp.suggestBox.addEventHandler(this);
//			}
		}
		editList.clear();
		while (realEditList.size() > 0){
			final MultipleInputPanel w = (MultipleInputPanel) realEditList.get(0);
			realEditList.remove(w);
			editList.add(w);
		}
		setStyles();
	}
	
	private void doViewGoogleEarth() {
		earthPopup.createUIMetamorphicRegions();
		earthPopup.show();
	}

	

}
