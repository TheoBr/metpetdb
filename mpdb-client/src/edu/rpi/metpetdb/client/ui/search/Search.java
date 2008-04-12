package edu.rpi.metpetdb.client.ui.search;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

import edu.rpi.metpetdb.client.locale.LocaleHandler;
import edu.rpi.metpetdb.client.model.SampleDTO;
import edu.rpi.metpetdb.client.model.SearchSampleDTO;
import edu.rpi.metpetdb.client.ui.MpDb;
import edu.rpi.metpetdb.client.ui.input.ObjectSearchPanel;
import edu.rpi.metpetdb.client.ui.input.attributes.GenericAttribute;
import edu.rpi.metpetdb.client.ui.input.attributes.TextAttribute;

public class Search extends FlowPanel {
	private static GenericAttribute[] searchAtts = {
			/* Keep it simple for now */
			// new TextAttribute(MpDb.doc.Sample_owner),
			// new TextAttribute(MpDb.doc.Sample_sesarNumber),
			new TextAttribute(MpDb.oc.SearchSample_alias),
			new TextAttribute(MpDb.oc.SearchSample_sesarNumber),
	// new DateAttribute(MpDb.doc.Sample_collectionDate),
	// new ListboxAttribute(MpDb.doc.Sample_rockType)
	};

	private final ObjectSearchPanel p_searchSample;
	private final VerticalPanel results = new VerticalPanel();

	public Search() {
		p_searchSample = new ObjectSearchPanel(searchAtts,
				LocaleHandler.lc_text.search(), LocaleHandler.lc_text.search()) {
			protected void searchBean(final AsyncCallback<List<SampleDTO>> ac) {
				MpDb.search_svc.search((SearchSampleDTO) getBean(), ac);
			}
			protected void onSearchCompletion(final List<SampleDTO> r) {
				results.clear();
				for (SampleDTO s : r) {
					results.add(new Label(s.getAlias()));
				}
			}

		};
		add(p_searchSample);
		add(results);
	}

	public Search createNew() {
		SearchSampleDTO s = new SearchSampleDTO();
		p_searchSample.edit(s);
		return this;
	}
}
