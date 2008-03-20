package edu.rpi.metpetdb.client.ui.image.browser.widgets;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.MouseListener;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import edu.rpi.metpetdb.client.ui.ServerOp;

public class DraggableProgressBarWidget extends VerticalPanel implements
		ClickListener {

	private final Button increase;
	private final Button decrease;
	private final TextBox value;
	private final DraggableProgressBar dpb;
	private final int min;
	private final int max;
	private final ServerOp continuation;

	public DraggableProgressBarWidget(final int elements, final int minOpacity,
			final int maxOpacity, final ServerOp r) {
		continuation = r;
		increase = new Button("+", this);
		decrease = new Button("-", this);
		value = new TextBox();
		dpb = new DraggableProgressBar(elements);

		dpb.addMouseListener(new MouseListener() {
			public void onMouseEnter(final Widget sender) {
			}

			public void onMouseUp(final Widget sender, final int x, final int y) {
				dpb.setProgress((int) (x / (float) dpb.getOffsetWidth() * 100));
				value.setText(String.valueOf(dpb.getProgress()));
				r.onSuccess(String.valueOf(dpb.getProgress()));
			}

			public void onMouseDown(final Widget sender, final int x,
					final int y) {
			}

			public void onMouseLeave(final Widget sender) {
			}

			public void onMouseMove(final Widget sender, final int x,
					final int y) {
			}
		});

		value.setText(String.valueOf(minOpacity));
		value.setWidth("25px");
		value.addChangeListener(new ChangeListener() {
			public void onChange(final Widget sender) {
				int newValue;
				try {
					newValue = Integer.parseInt(((HasText) sender).getText());
					dpb.setProgress(newValue - 1);
					changeProgress(1);
					r.onSuccess(String.valueOf(dpb.getProgress()));
				} catch (NumberFormatException nfe) {

				}
			}
		});
		this.add(new Label(
				"Click and drag the bar to set the opacity, then click Ok"));

		final HorizontalPanel hp = new HorizontalPanel();

		hp.add(decrease);
		hp.add(dpb);
		hp.add(increase);
		hp.add(value);

		this.add(hp);
		min = minOpacity;
		max = maxOpacity;
	}

	public DraggableProgressBarWidget(final int elements, final int minOpacity,
			final int maxOpacity, int start, final ServerOp r) {
		this(elements, minOpacity, maxOpacity, r);
		if (start > maxOpacity)
			start = maxOpacity;
		if (start < minOpacity)
			start = minOpacity;
		value.setText(String.valueOf(start));
		dpb.setProgress(start);
	}

	public void onClick(final Widget sender) {
		if (sender == increase) {
			doIncrease();
		} else if (sender == decrease) {
			doDecrease();
		}
		continuation.onSuccess(String.valueOf(dpb.getProgress()));
	}

	public void doIncrease() {
		changeProgress(1);
	}

	public void doDecrease() {
		changeProgress(-1);
	}

	// FIXME verify this works with gwt progress bar
	public void changeProgress(final int multiplier) {
		int newValue = (int) dpb.getProgress() + 1 * multiplier;
		if (newValue < min)
			newValue = min;
		if (newValue > max)
			newValue = max;
		dpb.setProgress(newValue);
		value.setText(String.valueOf(newValue));
	}

	// FIXME verify this works with gwt progress bar
	public int getProgress() {
		return (int) dpb.getProgress();
	}

}
