package edu.rpi.metpetdb.client.ui.input.attributes;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import edu.rpi.metpetdb.client.model.ChemicalAnalysis;
import edu.rpi.metpetdb.client.model.Image;
import edu.rpi.metpetdb.client.model.interfaces.HasImage;
import edu.rpi.metpetdb.client.model.validation.ObjectConstraint;
import edu.rpi.metpetdb.client.model.validation.PropertyConstraint;
import edu.rpi.metpetdb.client.model.validation.primitive.DoubleConstraint;
import edu.rpi.metpetdb.client.ui.commands.MCommand;

public class ChooseImageAttribute<DataType extends HasImage> extends GenericAttribute<DataType> {

	private Image image;
	private TextAttribute pointX;
	private TextAttribute pointY;

	public ChooseImageAttribute(final ObjectConstraint<Image> ic,
			final DoubleConstraint x, final DoubleConstraint y) {
		super(new PropertyConstraint[] {
				ic, x, y
		});
		image = null;
		pointX = new TextAttribute(x,true,true,false);
		pointY = new TextAttribute(y,true,true,false);
	}

	public Widget[] createDisplayWidget(final HasImage obj) {
		if (get(obj) != null) {
			final Image image = (Image) get(obj);
			final double x = Double.parseDouble(pointX.get(obj));
			final double y = Double.parseDouble(pointY.get(obj));
			final AbsolutePanel ap = new AbsolutePanel();
			ap.add(new com.google.gwt.user.client.ui.Image(((Image) image)
					.get64x64ServerPath()), 0, 0);
			final com.google.gwt.user.client.ui.Image i = new com.google.gwt.user.client.ui.Image(
					GWT.getModuleBaseURL() + "/images/point0.gif");
			double mmPerPixel = image.getScale() == null || image.getScale() == 0 ? image.getWidth() : image.getScale()/64D;
			ap.add(i, (int) (x/mmPerPixel) - 4,
					(int) (y/mmPerPixel) - 7);
			ap.setWidth("64px");
			ap.setHeight((image.getHeight() / (image.getWidth() / 64)) + "px");
			return new Widget[] {
					ap, pointX.createDisplayWidget(obj)[0],
					pointY.createDisplayWidget(obj)[0],
			};
		} else {
			return new Widget[] {
				new Label("No Image")
			};
		}
	}

	public Widget[] createEditWidget(final HasImage obj, final String id) {
		image = get(obj);
		final VerticalPanel vp = new VerticalPanel();
		final Button b = new Button("Choose Image...", new ClickListener() {
			public void onClick(final Widget sender) {
				new ChooseImageDialog(new MCommand<Image>() {
					@Override
					public void execute(Image selectedImage) {
						vp.remove(1);
						vp.add(new com.google.gwt.user.client.ui.Image(selectedImage
								.get64x64ServerPath()));
						image = selectedImage;
					}
				}, ((ChemicalAnalysis) obj).getSubsample()).show();
			}
		});
		vp.add(b);
		vp.add(createDisplayWidget(obj)[0]);
		return new Widget[] {
				vp, pointX.createEditWidget(obj, id)[0],
				pointY.createEditWidget(obj, id)[0],
		};
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
	protected Image get(final HasImage obj) {
		return obj.getImage();
	}
	protected void set(final HasImage obj, final Object v) {
		mSet(obj, v);
	}
	protected void set(final HasImage obj, final Object v,
			final PropertyConstraint pc) {
		mSet(obj, v, pc);
	}
}
