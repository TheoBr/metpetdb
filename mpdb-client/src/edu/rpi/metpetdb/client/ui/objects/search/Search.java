package edu.rpi.metpetdb.client.ui.objects.search;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

import edu.rpi.metpetdb.client.ui.input.attributes.NamedTextBox;

public class Search extends FlowPanel {
	private static TextBox[] SearchInput = {
			new NamedTextBox("user_username"),
			new NamedTextBox("sesarNumber")};

	public Search() {
		for(TextBox box : SearchInput)
		{
			add(box);
		}
		Button b = new Button("Jump!", new ClickListener() {
		      public void onClick(Widget sender) {
		        Window.alert("How high?");
		      }
		    });
		add(b);
	}
}
