package edu.rpi.metpetdb.client.ui.search;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlowPanel;

import edu.rpi.metpetdb.client.locale.LocaleHandler;
import edu.rpi.metpetdb.client.model.SampleDTO;
import edu.rpi.metpetdb.client.model.SearchSampleDTO;
import edu.rpi.metpetdb.client.paging.PaginationParameters;
import edu.rpi.metpetdb.client.paging.Results;
import edu.rpi.metpetdb.client.ui.MpDb;
import edu.rpi.metpetdb.client.ui.input.ObjectSearchPanel;
import edu.rpi.metpetdb.client.ui.input.attributes.CheckBoxesAttribute;
import edu.rpi.metpetdb.client.ui.input.attributes.DateRangeAttribute;
import edu.rpi.metpetdb.client.ui.input.attributes.GenericAttribute;
import edu.rpi.metpetdb.client.ui.input.attributes.TextAttribute;
import edu.rpi.metpetdb.client.ui.objects.list.SampleListEx;

public class Search extends FlowPanel {
	private static GenericAttribute[] searchAtts = {
			/* Keep it simple for now */
			new TextAttribute(MpDb.oc.SearchSample_owner),
			new TextAttribute(MpDb.oc.SearchSample_sesarNumber),
			new TextAttribute(MpDb.oc.SearchSample_alias),
			new DateRangeAttribute(MpDb.oc.SearchSample_collectionDateRange),
			// MpDb.doc.Sample_datePrecision),
			new CheckBoxesAttribute(MpDb.oc.SearchSample_possibleRockTypes),
	// new LocationAttribute(MpDb.oc.SearchSample_location),
	// new MineralAttribute(MpDb.oc.SearchSample_minerals)
	};

	private final ObjectSearchPanel p_searchSample;
	private final SampleListEx sampleList = new SampleListEx() {

		@Override
		public void update(PaginationParameters p,
				AsyncCallback<Results<SampleDTO>> ac) {
			/*
			 * in reality this would somehow tie into the search rpc call to
			 * pass in sorting data and pagination data to the server
			 */
			final Results<SampleDTO> r = new Results<SampleDTO>();
			r.setCount(results.size());
			r.setList(results);
			ac.onSuccess(r);
		}

	};
	private List<SampleDTO> results = new ArrayList<SampleDTO>();

	public Search() {
		p_searchSample = new ObjectSearchPanel(searchAtts,
				LocaleHandler.lc_text.search(), LocaleHandler.lc_text.search()) {
			protected void searchBean(final AsyncCallback<List<SampleDTO>> ac) {
				MpDb.search_svc.search((SearchSampleDTO) getBean(), ac);
			}
			protected void onSearchCompletion(final List<SampleDTO> r) {
				if (r != null) {
					results = r;
					sampleList.refresh();
				}
				p_searchSample.setEnabled(true);
			}

		};
		add(p_searchSample);
		add(sampleList);
	}

	public Search createNew() {
		SearchSampleDTO s = new SearchSampleDTO();
		p_searchSample.edit(s);
		return this;
	}
}
