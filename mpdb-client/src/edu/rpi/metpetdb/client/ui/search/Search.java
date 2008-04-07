package edu.rpi.metpetdb.client.ui.search;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlowPanel;

import edu.rpi.metpetdb.client.locale.LocaleHandler;
import edu.rpi.metpetdb.client.model.MObjectDTO;
import edu.rpi.metpetdb.client.model.SearchSampleDTO;
import edu.rpi.metpetdb.client.ui.MpDb;
import edu.rpi.metpetdb.client.ui.input.ObjectSearchPanel;
import edu.rpi.metpetdb.client.ui.input.attributes.DateAttribute;
import edu.rpi.metpetdb.client.ui.input.attributes.GenericAttribute;
import edu.rpi.metpetdb.client.ui.input.attributes.ListboxAttribute;
import edu.rpi.metpetdb.client.ui.input.attributes.TextAttribute;

public class Search extends FlowPanel {
	private static GenericAttribute[] sampleAtts = {
			new TextAttribute(MpDb.doc.Sample_owner),
			new TextAttribute(MpDb.doc.Sample_sesarNumber),
			new TextAttribute(MpDb.doc.Sample_alias),
			new DateAttribute(MpDb.doc.Sample_collectionDate),
			new ListboxAttribute(MpDb.doc.Sample_rockType)
	};

	private final ObjectSearchPanel p_sample;

	public Search() {
		p_sample = new ObjectSearchPanel(sampleAtts, LocaleHandler.lc_text
				.search(), LocaleHandler.lc_text.search()) {
			protected void searchBean(final AsyncCallback ac) {
				MpDb.search_svc.search((SearchSampleDTO) getBean(), ac);
			}
			protected void onSearchCompletion(final MObjectDTO result) {
				super.onSearchCompletion(result);
				// addExtraElements();
			}
		};
		SearchSampleDTO s = new SearchSampleDTO();
		p_sample.edit(s);
		add(p_sample);
	}
}
