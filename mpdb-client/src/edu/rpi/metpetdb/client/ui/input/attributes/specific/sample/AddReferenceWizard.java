package edu.rpi.metpetdb.client.ui.input.attributes.specific.sample;

import com.google.gwt.user.client.Command;

import edu.rpi.metpetdb.client.model.GeoReference;
import edu.rpi.metpetdb.client.ui.MpDb;
import edu.rpi.metpetdb.client.ui.commands.MCommand;
import edu.rpi.metpetdb.client.ui.input.DetailsPanel;
import edu.rpi.metpetdb.client.ui.input.WizardDialog;
import edu.rpi.metpetdb.client.ui.input.attributes.GenericAttribute;
import edu.rpi.metpetdb.client.ui.input.attributes.TextAttribute;

public class AddReferenceWizard extends WizardDialog {

	public AddReferenceWizard(final MCommand<GeoReference> c) {
		final GenericAttribute georef_attributes[] = {
				new TextAttribute(MpDb.doc.GeoReference_title),
				new TextAttribute(MpDb.doc.GeoReference_firstAuthor),
				new TextAttribute(MpDb.doc.GeoReference_secondAuthors),
				new TextAttribute(MpDb.doc.GeoReference_journalName),
				new TextAttribute(MpDb.doc.GeoReference_fullText),
		};
		
		final DetailsPanel<GeoReference> p_georef = new DetailsPanel<GeoReference>(
				georef_attributes);
		final GeoReference geoRef = new GeoReference();
		p_georef.edit(geoRef);
		
		final Command dialog_finish = new Command() {
			public void execute(){
				c.execute(geoRef);
			}
		};

		this.addDialogFinishListener(dialog_finish);
		this.addStep(p_georef, 0, "Add Reference");
	}

}
