package edu.rpi.metpetdb.client.ui.input;

import java.util.Iterator;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FocusWidget;
import com.google.gwt.user.client.ui.HasFocus;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.Widget;

import edu.rpi.metpetdb.client.error.ValidationException;

public class FocusSupport {
	/**
	 * Validate all {@link DetailsPanel}s contained within a widget.
	 * 
	 * @param w
	 * 		the widget to start at; may itself be a DetailsPanel.
	 * @return true if all DetailsPanels (if any exist) are valid right now;
	 * 	false otherwise. True is returned if no DetailsPanels were found.
	 */
	public static boolean validateEdit(final Widget w) {
		if (w instanceof DetailsPanel) {
			return ((DetailsPanel<?>) w).validateEdit();
		}

		if (w instanceof Panel) {
			boolean valid = true;
			final Iterator<Widget> i = ((Panel) w).iterator();
			while (i.hasNext())
				if (!validateEdit((Widget) i.next()))
					valid = false;
			return valid;
		}

		return true;
	}

	/**
	 * Change the enabled status of all contained FocusWidgets.
	 * 
	 * @param w
	 * 		root widget to start searching through.
	 * @param enabled
	 * 		true to enable the discovered FocusWidgets; false to disable them.
	 */
	public static void setEnabled(final Widget w, final boolean enabled) {
		if (w instanceof DetailsPanel)
			((DetailsPanel<?>) w).setEnabled(enabled);
		else if (w instanceof FocusWidget)
			((FocusWidget) w).setEnabled(enabled);
		else if (w instanceof Panel) {
			final Iterator<Widget> i = ((Panel) w).iterator();
			while (i.hasNext())
				setEnabled((Widget) i.next(), enabled);
		}
	}

	/**
	 * Determine if a particular ValidationException can be shown.
	 * <p>
	 * This method searches through the tree rooted at <code>w</code> to
	 * determine if a contained widget is able to show the error described by
	 * <code>e</code>. Often this happens in a {@link DetailsPanel}, where the
	 * exception is meant to be shown next to a particular field.
	 * </p>
	 * 
	 * @param w
	 * 		root of the tree to search.
	 * @param e
	 * 		the exception in question.
	 * @return true if the tree rooted at <code>w</code> can show <code>e</code>
	 * 	; false otherwise.
	 * @see #showValidationException(Widget, ValidationException)
	 */
	public static boolean handlesValidationException(final Widget w,
			final ValidationException e) {
		if (w instanceof DetailsPanel)
			return ((DetailsPanel<?>) w).handlesValidationException(e);
		else if (w instanceof Panel) {
			final Iterator<Widget> i = ((Panel) w).iterator();
			while (i.hasNext())
				if (handlesValidationException((Widget) i.next(), e))
					return true;
		}
		return false;
	}

	/**
	 * Display a validation exception.
	 * 
	 * @param w
	 * 		root of the tree to start searching for a widget to show the error.
	 * @param e
	 * 		the error to be shown.
	 * @return true if either <code>w</code> or one of its children accepted
	 * 	responsibility for the error and has displayed it; false otherwise.
	 */
	public static boolean showValidationException(final Widget w,
			final ValidationException e) {
		if (w instanceof DetailsPanel)
			return ((DetailsPanel<?>) w).showValidationException(e);
		else if (w instanceof Panel) {
			final Iterator<Widget> i = ((Panel) w).iterator();
			while (i.hasNext())
				if (showValidationException((Widget) i.next(), e))
					return true;
		}
		return false;
	}

	/**
	 * Request the first available child widget to take focus.
	 * 
	 * @param w
	 * 		root to search from. May also implement HasFocus itself.
	 * @return true if a widget was identified that implements HasFocus and we
	 * 	asked it to take the focus; false otherwise.
	 */
	public static boolean requestFocus(final Widget w) {
		// Refuse to take focus on a disabled widget.
		//
		if (w instanceof FocusWidget) {
			final FocusWidget f = (FocusWidget) w;
			if (!f.isEnabled())
				return false;
		}

		// Object claims to support it, give it a try.
		//
		if (w instanceof HasFocus) {
			DeferredCommand.addCommand(new Command() {
				public void execute() {
					((HasFocus) w).setFocus(true);
				}
			});

			return true;
		}

		// Our DetailsPanel class is weird; it puts Buttons that are
		// otherwise at the end of the display at the front. We don't
		// want these to be picked up for focus by default, so we skip
		// over them.
		//
		if (w instanceof DetailsPanel) {
			final Iterator<Widget> i = ((Panel) w).iterator();
			while (i.hasNext()) {
				final Widget c = (Widget) i.next();
				if (!(c instanceof Button) && requestFocus(c))
					return true;
			}
		}

		if (w instanceof Panel) {
			final Iterator<Widget> i = ((Panel) w).iterator();
			while (i.hasNext())
				if (requestFocus((Widget) i.next()))
					return true;
		}
		return false;
	}

	private FocusSupport() {
	}
}
