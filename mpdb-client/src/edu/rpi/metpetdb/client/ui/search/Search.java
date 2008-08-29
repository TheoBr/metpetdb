package edu.rpi.metpetdb.client.ui.search;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Hidden;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

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
import edu.rpi.metpetdb.client.ui.input.attributes.specific.MineralAttribute;
import edu.rpi.metpetdb.client.ui.input.attributes.specific.SearchChemistryAttribute;
import edu.rpi.metpetdb.client.ui.input.attributes.specific.SearchLocationAttribute;
import edu.rpi.metpetdb.client.ui.objects.list.SampleListEx;

public class Search extends FlowPanel implements ClickListener {
	private static GenericAttribute[] searchAtts = {
			/* Keep it simple for now */
			new TextAttribute(MpDb.oc.SearchSample_owner),
			new TextAttribute(MpDb.oc.SearchSample_sesarNumber),
			new TextAttribute(MpDb.oc.SearchSample_alias),
			new DateRangeAttribute(MpDb.oc.SearchSample_collectionDateRange),
			// MpDb.doc.Sample_datePrecision),
			new CheckBoxesAttribute(MpDb.oc.SearchSample_possibleRockTypes, 4),
			new SearchLocationAttribute(MpDb.oc.SearchSample_boundingBox),
			new MineralAttribute(MpDb.doc.SearchSample_minerals),
			new SearchChemistryAttribute(MpDb.doc.SearchSample_elements,
					MpDb.doc.SearchSample_oxides)
	};

	private static final String samplesParameter = "Samples";
	private Button exportExcelButton;
	private Button exportGoogleEarthButton;
	private final ObjectSearchPanel p_searchSample;
	private final SampleListEx sampleList = new SampleListEx(
			LocaleHandler.lc_text.search_noSamplesFound()) {

		@Override
		public void update(PaginationParameters p,
				AsyncCallback<Results<SampleDTO>> ac) {
			/*
			 * in reality this would somehow tie into the search rpc call to
			 * pass in sorting data and pagination data to the server
			 */
			// int displayResults;
			// if (results.size() - p.getFirstResult() >= p.getMaxResults())
			// displayResults = p.getMaxResults();
			// else
			// displayResults = results.size() - p.getFirstResult();
			final Results<SampleDTO> r = new Results<SampleDTO>();
			r.setCount(results.size());
			r.setList(results);
			// r.setList(results.subList(p.getFirstResult(), p.getFirstResult()
			// + displayResults));
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
		addResultListHeader();
		add(sampleList);
	}

	private void addResultListHeader() {

		final HorizontalPanel hpExport = new HorizontalPanel();

		final Label exportResultsLabel = new Label(LocaleHandler.lc_text
				.search_exportResults());
		final Label exportExcelLabel = new Label(LocaleHandler.lc_text
				.search_exportExcel());
		final Label exportGoogleEarthLabel = new Label(LocaleHandler.lc_text
				.search_exportKML());

		exportExcelButton = new Button(LocaleHandler.lc_text
				.buttonExportExcel(), this);
		exportGoogleEarthButton = new Button(LocaleHandler.lc_text
				.buttonExportKML(), this);

		exportResultsLabel.setStyleName("bold");
		exportExcelButton.setStyleName("bold");
		exportExcelButton.addStyleName("Beta");
		exportGoogleEarthButton.setStyleName("bold");

		final VerticalPanel vpExcel = new VerticalPanel();
		final VerticalPanel vpGoogleEarth = new VerticalPanel();

		vpExcel.add(exportExcelButton);
		vpExcel.add(exportExcelLabel);

		vpGoogleEarth.add(exportGoogleEarthButton);
		vpGoogleEarth.add(exportGoogleEarthLabel);

		hpExport.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);

		hpExport.add(exportResultsLabel);
		hpExport.add(vpExcel);
		hpExport.add(vpGoogleEarth);

		hpExport.setStyleName("mpdb-dataTableBlue");
		hpExport.setWidth("100%");

		add(hpExport);

	}

	public Search createNew() {
		SearchSampleDTO s = new SearchSampleDTO();
		p_searchSample.edit(s);
		return this;
	}

	public void onClick(Widget sender) {
		if (sender == exportExcelButton) {

		} else if (sender == exportGoogleEarthButton) {
			final FormPanel fp = new FormPanel();
			fp.setMethod(FormPanel.METHOD_GET);
			fp.setEncoding(FormPanel.ENCODING_URLENCODED);
			final HorizontalPanel hp = new HorizontalPanel();
			for (int i = 0; i < results.size(); i++) {
				Hidden sample = new Hidden(samplesParameter, String
						.valueOf(results.get(i).getId()));
				hp.add(sample);
			}
			fp.add(hp);
			fp.setAction(GWT.getModuleBaseURL() + "BasicKML.kml?");
			fp.setVisible(false);
			add(fp);
			fp.submit();
		}
	}
}
