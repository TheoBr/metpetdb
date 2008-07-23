package edu.rpi.metpetdb.client.ui.input.attributes;

import com.google.gwt.user.client.ui.Widget;

import edu.rpi.metpetdb.client.model.ChemicalAnalysisDTO;
import edu.rpi.metpetdb.client.model.MObjectDTO;
import edu.rpi.metpetdb.client.model.SubsampleDTO;
import edu.rpi.metpetdb.client.model.validation.PropertyConstraint;
import edu.rpi.metpetdb.client.model.validation.primitive.StringConstraint;
import edu.rpi.metpetdb.client.ui.TokenSpace;
import edu.rpi.metpetdb.client.ui.widgets.MLink;

public class HyperlinkAttribute extends TextAttribute {

	public HyperlinkAttribute(final StringConstraint sc) {
		super(sc);
	}

	public HyperlinkAttribute(final PropertyConstraint pc) {
		super(pc);
	}

	public Widget[] createDisplayWidget(final MObjectDTO obj) {
		final MLink link = new MLink();
		link.setText(get(obj));
		if (obj instanceof SubsampleDTO) {
			link.setTargetHistoryToken(TokenSpace
					.detailsOf(((SubsampleDTO) obj).getSample()));
		} else if (obj instanceof ChemicalAnalysisDTO) {
			if (this.constraints[0].propertyName.equals("subSampleName")) {
				link.setTargetHistoryToken(TokenSpace
						.detailsOf(((ChemicalAnalysisDTO) obj).getSubsample()));
			} else if (this.constraints[0].propertyName.equals("sampleName")) {
				link.setTargetHistoryToken(TokenSpace
						.detailsOf(((ChemicalAnalysisDTO) obj).getSubsample()
								.getSample()));
			}
		}
		return new Widget[] {
			link
		};
	}
}
