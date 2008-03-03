package edu.rpi.metpetdb.client.ui.objects.search;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

import edu.rpi.metpetdb.client.model.AttributeDTO;
import edu.rpi.metpetdb.client.ui.input.attributes.NamedTextBox;

public class Search extends FlowPanel implements ClickListener {
	private static TextBox[] SearchInput = { new NamedTextBox("user_username"),
			new NamedTextBox("sesarNumber") };
	
	final Button search;

	public Search() {
		for (TextBox box : SearchInput) {
			add(box);
		}
		search = new Button("Search", new ClickListener() {
			public void onClick(Widget sender) {
				processResults();
			}
		});
		add(search);
	}

	public void processResults() {
		List<AttributeDTO> attributes = new ArrayList<AttributeDTO>();
		for (TextBox box : SearchInput) {
			if (box.getText().length() > 0) {
				attributes.add(new AttributeDTO(box.getName(), box.getText()));
			}
		}
		if (attributes.size() > 0) {
			/* FIXME: in order to make a call to the server we need to use an RPC call
			 * for example see how SampleDetails loads  a bean, it calls MpDb.sample_svc.details(...)
			 * in your case you need to make a new Async class under client.service to handle
			 * search service calls
			 */
			//SearchDB.SampleSearch(attributes);
		}
	}
	
	public void onClick(final Widget sender) {
		if (sender == search) {
			processResults();
		}
	}
}
