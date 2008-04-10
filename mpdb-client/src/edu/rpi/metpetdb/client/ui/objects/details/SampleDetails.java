package edu.rpi.metpetdb.client.ui.objects.details;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Widget;

import edu.rpi.metpetdb.client.error.LoginRequiredException;
import edu.rpi.metpetdb.client.locale.LocaleHandler;
import edu.rpi.metpetdb.client.model.MObjectDTO;
import edu.rpi.metpetdb.client.model.SampleDTO;
import edu.rpi.metpetdb.client.ui.MetPetDBApplication;
import edu.rpi.metpetdb.client.ui.MpDb;
import edu.rpi.metpetdb.client.ui.ServerOp;
import edu.rpi.metpetdb.client.ui.Styles;
import edu.rpi.metpetdb.client.ui.input.ObjectEditorPanel;
import edu.rpi.metpetdb.client.ui.input.OnEnterPanel;
import edu.rpi.metpetdb.client.ui.input.attributes.DateAttribute;
import edu.rpi.metpetdb.client.ui.input.attributes.GenericAttribute;
import edu.rpi.metpetdb.client.ui.input.attributes.ListboxAttribute;
import edu.rpi.metpetdb.client.ui.input.attributes.RadioButtonAttribute;
import edu.rpi.metpetdb.client.ui.input.attributes.TextAttribute;
import edu.rpi.metpetdb.client.ui.input.attributes.specific.CommentAttribute;
import edu.rpi.metpetdb.client.ui.input.attributes.specific.LocationAttribute;
import edu.rpi.metpetdb.client.ui.input.attributes.specific.MetamorphicGradeAttribute;
import edu.rpi.metpetdb.client.ui.input.attributes.specific.MineralAttribute;
import edu.rpi.metpetdb.client.ui.input.attributes.specific.ReferenceAttribute;
import edu.rpi.metpetdb.client.ui.input.attributes.specific.RegionAttribute;
import edu.rpi.metpetdb.client.ui.widgets.MLink;

public class SampleDetails extends FlowPanel {
	private static GenericAttribute[] sampleAtts = {
			new TextAttribute(MpDb.doc.Sample_owner).setReadOnly(true),
			new TextAttribute(MpDb.doc.Sample_sesarNumber).setImmutable(true),
			new TextAttribute(MpDb.doc.Sample_alias),
			new DateAttribute(MpDb.doc.Sample_collectionDate,
					MpDb.doc.Sample_datePrecision),
			new ListboxAttribute(MpDb.doc.Sample_rockType),
			new RadioButtonAttribute(MpDb.doc.Sample_publicData),
			new LocationAttribute(MpDb.doc.Sample_location),
			new TextAttribute(MpDb.doc.Sample_latitudeError),
			new TextAttribute(MpDb.doc.Sample_longitudeError),
			new TextAttribute(MpDb.doc.Sample_country),
			new TextAttribute(MpDb.doc.Sample_description),
			new TextAttribute(MpDb.doc.Sample_collector),
			new TextAttribute(MpDb.doc.Sample_locationText),
			new MineralAttribute(MpDb.doc.Sample_minerals),
			new RegionAttribute(MpDb.doc.Sample_regions),
			new MetamorphicGradeAttribute(MpDb.doc.Sample_metamorphicGrades),
			new ReferenceAttribute(MpDb.doc.Sample_references),
			new CommentAttribute(MpDb.doc.Sample_comments),
			new TextAttribute(MpDb.oc.Sample_subsampleCount).setReadOnly(true),
	};

	private final ObjectEditorPanel p_sample;
	private long sampleId;

	public SampleDetails() {
		p_sample = new ObjectEditorPanel(sampleAtts, LocaleHandler.lc_text
				.addSample(), LocaleHandler.lc_text.addSampleDescription()) {
			protected void loadBean(final AsyncCallback ac) {
				final SampleDTO s = (SampleDTO) getBean();
				MpDb.sample_svc.details(s != null && !s.mIsNew() ? s.getId()
						: sampleId, ac);
			}

			protected void saveBean(final AsyncCallback ac) {
				MpDb.sample_svc.save((SampleDTO) getBean(), ac);
			}

			protected void deleteBean(final AsyncCallback ac) {
				MpDb.sample_svc.delete(((SampleDTO) getBean()).getId(), ac);
			}

			protected boolean canEdit() {
				final SampleDTO s = (SampleDTO) getBean();
				if (s.isPublicData())
					return false;
				if (MpDb.isCurrentUser(s.getOwner()))
					return true;
				return false;
			}

			protected void onSaveCompletion(final MObjectDTO result) {
				super.onSaveCompletion(result);
				// addExtraElements();
			}
		};
		final OnEnterPanel.ObjectEditor oep = new OnEnterPanel.ObjectEditor(
				p_sample);
		oep.setStyleName("sd-details");
		add(oep);
	}

	private void addExtraElements() {
		// Add subsample list to sample details
		// final SubsampleList list = new SubsampleList(new DataProvider() {
		// public void update(final PaginationParameters p,
		// final AsyncCallback ac) {
		// MpDb.subsample_svc.all(p, sampleId, ac);
		// }
		// }, null);
		// list.setStyleName("sd-subsamples");
		// add(list);
		// Add MLinks that will be used to add subsample

		final MLink addSubsample = new MLink(LocaleHandler.lc_text
				.addSubsample(), new ClickListener() {
			public void onClick(final Widget sender) {
				new ServerOp() {
					public void begin() {
						if (MpDb.isLoggedIn())
							MetPetDBApplication.show(new SubsampleDetails()
									.createNew((SampleDTO) p_sample.getBean(),
											null));
						else
							onFailure(new LoginRequiredException());
					}

					public void onSuccess(Object result) {
					}
				}.begin();
			}
		});
		addSubsample.addStyleName(Styles.ADDLINK);
		add(addSubsample);
	}

	public void addChemistry() {
		// final Label adder = new Label("Add:");

		final HorizontalPanel hp = new HorizontalPanel();

		final ListBox species = new ListBox();
		species.addItem("Species...", "Species...");
		species.addItem("Element", "Element");
		species.addItem("Oxide", "Oxide");
		species.setVisibleItemCount(1);

		final ListBox type = new ListBox();
		type.addItem("Type...", "Type...");
		type.addItem("Silicate", "Silicate");
		type.addItem("Oxide", "Oxide");
		type.addItem("Carbonate", "Carbonate");
		type.addItem("Phosphate", "Phosphate");
		type.addItem("Other", "Other");
		type.setVisibleItemCount(1);

		if (species.getSelectedIndex() != -1 && !(species.isItemSelected(1))) {
			if (type.getSelectedIndex() != -1 && !(type.isItemSelected(1))) {

			}
		}

		// add(adder);
		hp.add(species);
		hp.add(type);

		add(hp);
	}

	public SampleDetails showById(final long id) {
		sampleId = id;
		p_sample.load();
		addExtraElements();
		// addChemistry();
		return this;
	}

	public SampleDetails createNew() {
		final SampleDTO s = new SampleDTO();
		s.setOwner(MpDb.currentUser());
		p_sample.edit(s);
		return this;
	}
}
