package edu.rpi.metpetdb.client.ui.objects.details;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.gwt.user.client.History;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

import edu.rpi.metpetdb.client.error.LoginRequiredException;
import edu.rpi.metpetdb.client.locale.LocaleHandler;
import edu.rpi.metpetdb.client.model.ChemicalAnalysis;
import edu.rpi.metpetdb.client.model.Image;
import edu.rpi.metpetdb.client.model.Sample;
import edu.rpi.metpetdb.client.model.Subsample;
import edu.rpi.metpetdb.client.model.User;
import edu.rpi.metpetdb.client.paging.PaginationParameters;
import edu.rpi.metpetdb.client.paging.Results;
import edu.rpi.metpetdb.client.ui.CSS;
import edu.rpi.metpetdb.client.ui.MetPetDBApplication;
import edu.rpi.metpetdb.client.ui.MpDb;
import edu.rpi.metpetdb.client.ui.TokenSpace;
import edu.rpi.metpetdb.client.ui.commands.ServerOp;
import edu.rpi.metpetdb.client.ui.commands.VoidLoggedInOp;
import edu.rpi.metpetdb.client.ui.dialogs.ConfirmationDialogBox;
import edu.rpi.metpetdb.client.ui.dialogs.MDialogBox;
import edu.rpi.metpetdb.client.ui.input.ObjectEditorPanel;
import edu.rpi.metpetdb.client.ui.input.OnEnterPanel;
import edu.rpi.metpetdb.client.ui.input.attributes.GenericAttribute;
import edu.rpi.metpetdb.client.ui.input.attributes.ListboxAttribute;
import edu.rpi.metpetdb.client.ui.input.attributes.RadioButtonAttribute;
import edu.rpi.metpetdb.client.ui.input.attributes.TextAttribute;
import edu.rpi.metpetdb.client.ui.input.attributes.specific.image.AddImageAttribute;
import edu.rpi.metpetdb.client.ui.objects.list.ChemicalAnalysisList;
import edu.rpi.metpetdb.client.ui.objects.list.ChemicalAnalysisListEx;
import edu.rpi.metpetdb.client.ui.objects.list.ImageListViewer;
import edu.rpi.metpetdb.client.ui.widgets.MLink;
import edu.rpi.metpetdb.client.ui.widgets.panels.MPagePanel;
import edu.rpi.metpetdb.client.ui.widgets.panels.MTwoColPanel;

public class SubsampleDetails extends MPagePanel {

	private static GenericAttribute[] subsampleAtts = {
			new TextAttribute(MpDb.doc.Subsample_owner).setReadOnly(true),
			new TextAttribute(MpDb.doc.Subsample_name),
			new RadioButtonAttribute(MpDb.doc.Subsample_publicData,
					LocaleHandler.lc_text.publicDataWarning()),
			new ListboxAttribute(MpDb.doc.Subsample_subsampleType),
			new AddImageAttribute<Subsample>(MpDb.doc.Subsample_images),
			new TextAttribute(MpDb.doc.Subsample_imageCount).setReadOnly(true),
			new TextAttribute(MpDb.doc.Subsample_analysisCount)
					.setReadOnly(true),
	};

	private final ObjectEditorPanel<Subsample> p_subsample;
	private long subsampleId;
	private long sampleId;
	private String sampleNumber;
	private final MLink map = new MLink();
	private Widget images;
	private final MTwoColPanel panel = new MTwoColPanel();

	public SubsampleDetails() {
		p_subsample = new ObjectEditorPanel<Subsample>(subsampleAtts,
				LocaleHandler.lc_text.addSubsample(), LocaleHandler.lc_text
						.addSubsampleDescription(sampleNumber)) {
			protected void loadBean(final AsyncCallback<Subsample> ac) {
				final Subsample s = (Subsample) getBean();
				MpDb.subsample_svc.details(s != null && !s.mIsNew() ? s.getId()
						: subsampleId, ac);
			}

			protected void saveBean(final AsyncCallback<Subsample> ac) {
				makeImagesPublicIfPublic((Subsample) getBean());
				//If the user attempts to make the subsample public, make sure the owning sample is public
				if(((Subsample) getBean()).isPublicData()){
					new ServerOp(){
						@Override
						public void begin(){
							MpDb.sample_svc.details(((Subsample) getBean()).getSampleId(), this);
						}
						public void onSuccess(Object result){
							if(!((Sample) result).isPublicData()){
								//Can't save this subsample as public
								cannotMakePublic();
							} else {
								MpDb.subsample_svc.save((Subsample) getBean(), ac);
							}
						}
					}.begin();
				} else{
					MpDb.subsample_svc.save((Subsample) getBean(), ac);
				}
			}

			protected void deleteBean(final AsyncCallback<Object> ac) {
				sampleId = getBean().getSampleId();
				new ServerOp<Boolean>() {
					public void begin() {
						new ConfirmationDialogBox("Are you sure you want to delete this subsample?"
								, true, this);
					}
					public void onSuccess(final Boolean result) {
						if (result){
							MpDb.subsample_svc.delete(((Subsample) getBean()).getId(), ac);
						} else {
							p_subsample.setEnabled(true);
						}
					}
				}.begin();
			}

			protected boolean canEdit() {
				final Subsample s = ((Subsample) getBean());
				if (MpDb.isCurrentUser(s.getOwner()))
					return true;
				return false;
			}

			protected void onSaveCompletion(final Subsample result) {
				if (History.getToken().equals(
						TokenSpace.detailsOf((Subsample) result))) {
					TokenSpace.dispatch(TokenSpace
							.detailsOf((Subsample) result));
				} else {
					History.newItem(TokenSpace.detailsOf((Subsample) result));
				}
			}

			protected void onLoadCompletion(final Subsample result) {
				super.onLoadCompletion(result);
				final Subsample s = (Subsample) result;
				if (s.getGrid() == null) {
					map.setText("Create Map");
					map.setTargetHistoryToken(TokenSpace.createNewImageMap(s));
				} else {
					map.setText("View Map");
					map
							.setTargetHistoryToken(TokenSpace.detailsOf(s
									.getGrid()));
				}
				setPageTitle(s.getName(), "Subsample");
				addPageActionItem(map);
				images = new ImageListViewer() {

					@Override
					public void update(PaginationParameters p,
							AsyncCallback<Results<Image>> ac) {
						MpDb.image_svc.allImages(result.getId(), p, ac);
					}

					@Override
					public void getAllIds(AsyncCallback<Map<Object, Boolean>> ac) {
						MpDb.image_svc.allImageIds(result.getId(), ac);
					}

				};
				panel.getRightCol().clear();
				panel.getRightCol().add(images);

			}
			protected void onDeleteCompletion(final Object result) {
				History.newItem(TokenSpace.detailsOfSample((sampleId)));
			}
		};

		final OnEnterPanel.ObjectEditor oep = new OnEnterPanel.ObjectEditor(
				p_subsample);
		panel.getLeftCol().add(oep);
		add(panel);
	}

	public void showChemicalAnalysis() {
		FlexTable chemft = new FlexTable();

		final ChemicalAnalysisList list = new ChemicalAnalysisList() {
			public void update(final PaginationParameters p,
					final AsyncCallback<Results<ChemicalAnalysis>> ac) {
				MpDb.chemicalAnalysis_svc.all(p, subsampleId, ac);
			}

			@Override
			public void getAllIds(AsyncCallback<Map<Object, Boolean>> ac) {
				MpDb.chemicalAnalysis_svc.allIdsForSubsample(subsampleId, ac);
			}
		};
		
		chemft.setWidget(1, 0, list);
		chemft.getFlexCellFormatter().setColSpan(1, 0, 10);

		final MLink addChemicalAnalysis = new MLink(LocaleHandler.lc_text
				.addChemicalAnalysis(), new ClickListener() {
			public void onClick(final Widget sender) {
				new VoidLoggedInOp() {
					public void command() {
						if(canEdit()){
							History.newItem(TokenSpace
								.createNewChemicalAnalysis(p_subsample
										.getBean()));
						} else {
							noPermissionWarning();
						}
					}
				}.begin();
			}
		});
		addChemicalAnalysis.setStyleName(CSS.ADDLINK);
		final Label chemAnalysis = new Label("Chemical Analysis");
		chemAnalysis.setStyleName("h2");
		chemft.setWidget(0, 0, chemAnalysis);
		chemft.setWidget(0, 1, addChemicalAnalysis);
		chemft.getFlexCellFormatter().setAlignment(0, 1,
				HasHorizontalAlignment.ALIGN_RIGHT,
				HasVerticalAlignment.ALIGN_MIDDLE);
		chemft.getRowFormatter().setStyleName(0, "mpdb-dataTableLightBlue");
		chemft.setWidth("100%");
		add(chemft);
	}

	public SubsampleDetails showById(final long id) {
		subsampleId = id;
		p_subsample.load();
		showChemicalAnalysis();
		return this;
	}

	public SubsampleDetails createNew(final Sample s) {
		Subsample ss = new Subsample();
		ss.setOwner(MpDb.currentUser());
		s.addSubsample(ss);
		ss.setSampleName(s.getNumber());
		p_subsample.edit(ss);
		sampleNumber = s.getNumber();
		return this;
	}

	public SubsampleDetails edit(final long id) {
		subsampleId = id;
		new ServerOp<Subsample>() {
			public void begin() {
				if (MpDb.isLoggedIn()) {
					final Subsample s = (Subsample) p_subsample.getBean();
					MpDb.subsample_svc.details(s != null && !s.mIsNew() ? s
							.getId() : subsampleId, this);

				} else {
					onFailure(new LoginRequiredException());
				}
			}

			public void onSuccess(final Subsample result) {
				p_subsample.edit((Subsample) result);
			}

			public void cancel() {
				p_subsample.load();
			}
		}.begin();
		return this;
	}
	
	private boolean canEdit(){
		final Subsample s = ((Subsample) p_subsample.getBean());
		return MpDb.isCurrentUser(s.getOwner());
	}
	
	private void noPermissionWarning(){
		final MDialogBox noPermissionBox = new MDialogBox();
		final FlowPanel container = new FlowPanel();
		container.add(new Label("You do not have the correct permissions to add a chemical analysis."));
		Button ok = new Button("Ok");
		ok.addClickListener(new ClickListener(){
			public void onClick(final Widget sender){
				noPermissionBox.hide();
			}
		});
		container.add(ok);
		noPermissionBox.setWidget(container);
		noPermissionBox.show();
	}
	
	private void cannotMakePublic(){
		final MDialogBox box = new MDialogBox();
		final FlowPanel container = new FlowPanel();
		container.add(new Label("Cannot make subsample public. It is owned by a private Sample"));
		Button ok = new Button("Ok");
		ok.addClickListener(new ClickListener(){
			public void onClick(final Widget sender){
				box.hide();
				p_subsample.getBean().setPublicData(false);
				p_subsample.edit(p_subsample.getBean());
			}
		});
		container.add(ok);
		box.setWidget(container);
		box.show();
	}
	
	private void makeImagesPublicIfPublic(Subsample subsample){
		//If it's private, just return, images default to private
		if(!subsample.isPublicData()) return;
		
		Set<edu.rpi.metpetdb.client.model.Image> images = subsample.getImages();
		for(edu.rpi.metpetdb.client.model.Image i: images){
			i.setPublicData(true);
		}
		return;
	}
}
