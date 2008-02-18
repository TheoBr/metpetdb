package edu.rpi.metpetdb.client.ui.input.attributes;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import edu.rpi.metpetdb.client.model.ImageDTO;
import edu.rpi.metpetdb.client.model.MObjectDTO;
import edu.rpi.metpetdb.client.model.MineralAnalysisDTO;
import edu.rpi.metpetdb.client.model.validation.ImageConstraint;
import edu.rpi.metpetdb.client.model.validation.IntegerConstraint;
import edu.rpi.metpetdb.client.model.validation.PropertyConstraint;
import edu.rpi.metpetdb.client.ui.ServerOp;

public class ChooseImageAttribute extends GenericAttribute {

	private ImageDTO image;
	private TextAttribute pointX;
	private TextAttribute pointY;
	public ChooseImageAttribute(final ImageConstraint ic,
			final IntegerConstraint x, final IntegerConstraint y) {
		super(new PropertyConstraint[]{ic, x, y});
		image = null;
		pointX = new TextAttribute(x);
		pointY = new TextAttribute(y);
	}

	public Widget[] createDisplayWidget(final MObjectDTO obj) {
		if (get(obj) != null) {
			final ImageDTO image = (ImageDTO) get(obj);
			final int x = Integer.parseInt(pointX.get(obj));
			final int y = Integer.parseInt(pointY.get(obj));
			final AbsolutePanel ap = new AbsolutePanel();
			ap.add(new com.google.gwt.user.client.ui.Image(((ImageDTO) image)
					.get64x64ServerPath()), 0, 0);
			final com.google.gwt.user.client.ui.Image i = new com.google.gwt.user.client.ui.Image(
					GWT.getModuleBaseURL() + "/images/point0.gif");
			ap.add(i,(int)((64 / (float)image.getWidth()) * x) - 4,(int) ((64 / (float)image.getWidth())* y)- 7);
			ap.setWidth("64px");
			ap.setHeight((image.getHeight() / (image.getWidth() / 64)) + "px");
			return new Widget[]{ap, pointX.createDisplayWidget(obj)[0],
					pointY.createDisplayWidget(obj)[0],};
		} else {
			return new Widget[]{new Label("No Image")};
		}
	}

	public Widget[] createEditWidget(final MObjectDTO obj, final String id) {
		image = get(obj);
		final VerticalPanel vp = new VerticalPanel();
		final Button b = new Button("Choose Image...", new ClickListener() {
			public void onClick(final Widget sender) {
				new ServerOp() {
					public void begin() {
						new ChooseImageDialog(this, ((MineralAnalysisDTO) obj)
								.getSubsample()).show();
					}
					public void onSuccess(final Object result) {
						if (result != null)
							image = (ImageDTO) result;
						vp.remove(1);
						vp.add(new com.google.gwt.user.client.ui.Image(
								((ImageDTO) image).get64x64ServerPath()));
					}
				}.begin();
			}
		});
		vp.add(b);
		vp.add(createDisplayWidget(obj)[0]);
		return new Widget[]{vp, pointX.createEditWidget(obj, id)[0],
				pointY.createEditWidget(obj, id)[0],};
	}

	protected Object get(final Widget editWidget,
			final PropertyConstraint constraint) {
		if (constraint == pointX.getConstraint()) {
			return pointX.get(editWidget);
		} else if (constraint == pointY.getConstraint()) {
			return pointY.get(editWidget);
		} else {
			return this.get(editWidget);
		}
	}
	protected Object get(final Widget editWidget) {
		return image;
	}
	protected ImageDTO get(final MObjectDTO obj) {
		return (ImageDTO) mGet(obj);
	}
	protected void set(final MObjectDTO obj, final Object v) {
		mSet(obj, v);
	}
	protected void set(final MObjectDTO obj, final Object v,
			final PropertyConstraint pc) {
		mSet(obj, v, pc);
	}
}
