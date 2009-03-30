package edu.rpi.metpetdb.client.ui.input;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.SourcesTabEvents;
import com.google.gwt.user.client.ui.TabListener;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.Widget;

import edu.rpi.metpetdb.client.locale.LocaleHandler;
import edu.rpi.metpetdb.client.ui.commands.ServerOp;
import edu.rpi.metpetdb.client.ui.commands.VoidMCommand;
import edu.rpi.metpetdb.client.ui.dialogs.MDialogBox;

/**
 * Basically accepts multple ObjectEditorPanels, and creates a wizard out of
 * them
 * 
 */
public class WizardDialog extends MDialogBox implements ClickListener {

	// Hold the DetailPanel (basically the wizard pages)
	private ArrayList<DetailsPanel<?>> panels;
	private int currentTab = 0;
	private final TabPanel tabPanel;
	private final Button cancel;
	private final Button next;
	private final Button finish;
	private final Button back;
	private HashSet<Command> tabChangeListeners;
	private HashSet<Command> dialogFinishListeners;

	public WizardDialog() {
		panels = new ArrayList<DetailsPanel<?>>();
		tabPanel = new TabPanel();

		cancel = new Button(LocaleHandler.lc_text.buttonCancel(), this);
		back = new Button("Back", this);
		next = new Button("Next", this);
		finish = new Button("Finish", this);

		tabPanel.addTabListener(new TabListener() {
			public boolean onBeforeTabSelected(SourcesTabEvents sender,
					int tabIndex) {
				return true;
			}
			public void onTabSelected(SourcesTabEvents sender, int tabIndex) {
				if (tabIndex == 0) {
					back.setEnabled(false);
				} else {
					back.setEnabled(true);
				}

				if (tabIndex == ((TabPanel) sender).getWidgetCount() - 1) {
					next.setEnabled(false);
					finish.setEnabled(true);
				} else {
					next.setEnabled(true);
					finish.setEnabled(false);
				}

				if (tabChangeListeners != null) {
					final Iterator<Command> itr = tabChangeListeners.iterator();
					while (itr.hasNext()) {
						itr.next().execute();
					}
				}
			}
		});

		final FlowPanel fp = new FlowPanel();
		fp.add(tabPanel);
		fp.add(cancel);
		fp.add(back);
		fp.add(next);
		fp.add(finish);
		this.setWidget(fp);
	}

	public void addStep(final DetailsPanel<?> stepPanel, final int stepNumber,
			final String stepText) {
		// TODO update next/back/finish
		panels.add(stepNumber, stepPanel);
		tabPanel.add(stepPanel, stepText);
	}

	public void removeStep(final int stepNumber) {
		// TODO update next/back/finish
		if (stepNumber < panels.size()) {
			panels.remove(stepNumber);
			tabPanel.remove(stepNumber);
		}
	}

	public void onClick(final Widget sender) {
		if (sender == cancel) {
			this.hide();
		} else if (sender == back) {
			if (currentTab > 0) {
				--currentTab;
				tabPanel.selectTab(currentTab);
			} else {
				currentTab = 0;
			}
		} else if (sender == next) {
			if (currentTab < tabPanel.getWidgetCount() - 1) {
				++currentTab;
				tabPanel.selectTab(currentTab);
			} else {
				currentTab = tabPanel.getWidgetCount() - 1;
			}
		} else if (sender == finish) {
			setActionsEnabled(false);
			final VoidMCommand tracker = new VoidMCommand() {
				int success = 0;

				public void execute() {
					success++;
					if (success == panels.size()) {
						final Iterator<Command> dfItr = dialogFinishListeners
								.iterator();
						while (dfItr.hasNext()) {
							dfItr.next().execute();
						}
						WizardDialog.this.hide();

					}
					setActionsEnabled(true);
				}
			};
			final Iterator<DetailsPanel<?>> itr = panels.iterator();
			while (itr.hasNext()) {
				itr.next().startValidation(tracker);
			}
		}
	}
	public void show() {
		super.show();
		tabPanel.selectTab(0);
		currentTab = 0;
	}

	public void addTabChangeListener(final Command s) {
		if (tabChangeListeners == null)
			tabChangeListeners = new HashSet<Command>();
		tabChangeListeners.add(s);
	}

	public void addDialogFinishListener(final Command s) {
		if (dialogFinishListeners == null)
			dialogFinishListeners = new HashSet<Command>();
		dialogFinishListeners.add(s);
	}

	public void clearDialogFinishListeners() {
		if (dialogFinishListeners != null)
			dialogFinishListeners.clear();
	}

	public void enableNextButton(final boolean enabled) {
		next.setEnabled(enabled);
	}

	public void enableFinishButton(final boolean enabled) {
		finish.setEnabled(enabled);
	}

	private void setActionsEnabled(final boolean enabled) {
		cancel.setEnabled(enabled);
		back.setEnabled(enabled);
		finish.setEnabled(enabled);
		next.setEnabled(enabled);
	}

}
