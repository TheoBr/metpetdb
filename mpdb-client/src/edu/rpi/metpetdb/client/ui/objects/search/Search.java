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
import edu.rpi.metpetdb.server.search.SearchDB;

public class Search extends FlowPanel {
	private static TextBox[] SearchInput = { new NamedTextBox("user_username"),
			new NamedTextBox("sesarNumber") };

	public Search() {
		for (TextBox box : SearchInput) {
			add(box);
		}
		Button b = new Button("Search", new ClickListener() {
			public void onClick(Widget sender) {
				processResults();
			}
		});
		add(b);
	}

	public void processResults() {
		List<AttributeDTO> attributes = new ArrayList<AttributeDTO>();
		for (TextBox box : SearchInput) {
			if (box.getText().length() > 0) {
				attributes.add(new AttributeDTO(box.getName(), box.getText()));
			}
		}
		if (attributes.size() > 0) {
			SearchDB.SampleSearch(attributes);
		}
	}
}
