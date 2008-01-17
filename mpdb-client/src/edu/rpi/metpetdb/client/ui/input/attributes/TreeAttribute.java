package edu.rpi.metpetdb.client.ui.input.attributes;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeItem;
import com.google.gwt.user.client.ui.Widget;

import edu.rpi.metpetdb.client.model.MObject;
import edu.rpi.metpetdb.client.model.interfaces.IHasChildren;
import edu.rpi.metpetdb.client.model.validation.MineralConstraint;
import edu.rpi.metpetdb.client.model.validation.PropertyConstraint;
import edu.rpi.metpetdb.client.ui.widgets.MUnorderedList;

public class TreeAttribute extends GenericAttribute implements ClickListener {

	protected final SimplePanel container;

	private class ExtendedTreeItem extends TreeItem {
		private Object obj;

		public ExtendedTreeItem(final Widget w, final Object o) {
			super(w);
			obj = o;
		}

		public ExtendedTreeItem(final String s, final Object o) {
			super(s);
			obj = o;
		}

		public Object getObject() {
			return obj;
		}

		public void setObject(final Object o) {
			obj = o;
		}
	}

	private ArrayList selectedItems = new ArrayList();
	private ArrayList selectedWidgets = new ArrayList();
	private final int numberOfColumns;
	private final Button collapse;
	private final Button expand;
	private final ArrayList trees;
	private final int maxSelectable;

	public TreeAttribute(final PropertyConstraint pc, final int i) {
		this(pc, i, 0);
	}

	public TreeAttribute(final PropertyConstraint pc, final int i,
			final int maxSelectable) {
		super(pc);
		container = new SimplePanel();
		numberOfColumns = i;
		expand = new Button("Expand", this);
		collapse = new Button("Collapse", this);
		trees = new ArrayList();
		this.maxSelectable = maxSelectable;
	}

	public ArrayList getSelectedItems() {
		return selectedItems;
	}
	public void setSelectedItems(final ArrayList al) {
		selectedItems = al;
	}

	public Widget[] createDisplayWidget(final MObject obj) {
		final MUnorderedList list = new MUnorderedList();
		final Collection c = get(obj);
		if (c != null) {
			final Iterator itr = c.iterator();
			while (itr.hasNext()) {
				final Object object = itr.next();
				final Label l = new Label(object.toString());
				list.add(l);
			}
		}
		container.setWidget(list);
		return new Widget[]{container};
	}

	public Widget[] createEditWidget(final MObject obj, final String id,
			final GenericAttribute ga) {
		final FlowPanel fp = new FlowPanel();
		final HorizontalPanel hp = new HorizontalPanel();
		selectedItems.clear();
		selectedWidgets.clear();
		final Collection allMinerals = get(ga);
		if (allMinerals != null) {
			final Iterator itr = allMinerals.iterator();
			final int numMinerals = (int) Math.ceil((double) (allMinerals
					.size() / (double) numberOfColumns));
			int count = 0;
			for (int i = 0; i < numberOfColumns; ++i) {
				// Find out how many minerals go in this tree
				final Collection mineralSubset = new HashSet();
				while (itr.hasNext() && count < numMinerals) {
					mineralSubset.add(itr.next());
					count++;
				}
				final Tree t = makeTree(mineralSubset, obj);
				applyStyle(t, true);
				hp.add(t);
				trees.add(t);
				count = 0;
			}
			fp.add(hp);
			fp.add(expand);
			fp.add(collapse);
		}
		return new Widget[]{fp};
	}

	private TreeItem makeChildren(final Collection parents,
			final TreeItem root, final MObject obj) {
		if (parents != null) {
			Iterator itr = parents.iterator();
			while (itr.hasNext()) {
				final IHasChildren ihc = (IHasChildren) itr.next();
				final CheckBox cb = new CheckBox(ihc.toString());

				if (get(obj) != null && get(obj).contains(ihc)
						|| contains(get(obj), ihc)) {
					cb.setChecked(true);
					selectedItems.add(ihc);
					selectedWidgets.add(cb);
				}
				final ExtendedTreeItem parent = new ExtendedTreeItem(cb, ihc);
				if (ihc.getChildren() != null && ihc.getChildren().size() > 0) {
					root.addItem(makeChildren(ihc.getChildren(), parent, obj));
				} else
					root.addItem(parent);

				cb.addClickListener(new ClickListener() {
					public void onClick(final Widget w) {
						if (cb.isChecked()) {
							selectedWidgets.add(w);
							selectedItems.add(ihc);
							checkChildren(parent);
							if (maxSelectable != 0
									&& selectedWidgets.size() > maxSelectable) {
								((CheckBox) selectedWidgets.get(0))
										.setChecked(false);
								selectedItems.remove(0);
								selectedWidgets.remove(0);
							}
						} else {
							selectedItems.remove(ihc);
							selectedWidgets.remove(w);
						}
					}
				});
			}
		}
		return root;
	}

	private Tree makeTree(final Collection parents, final MObject obj) {
		final Tree t = new Tree();
		if (parents != null) {
			Iterator itr = parents.iterator();
			while (itr.hasNext()) {
				final IHasChildren ihc = (IHasChildren) itr.next();
				final CheckBox cb = new CheckBox(ihc.toString());

				if (get(obj) != null && get(obj).contains(ihc)
						|| contains(get(obj), ihc)) {
					cb.setChecked(true);
					selectedItems.add(ihc);
					selectedWidgets.add(cb);
				}
				final ExtendedTreeItem parent = new ExtendedTreeItem(cb, ihc);
				if (ihc.getChildren() != null && ihc.getChildren().size() > 0) {
					t.addItem(makeChildren(ihc.getChildren(), parent, obj));
				} else
					t.addItem(parent);

				cb.addClickListener(new ClickListener() {
					public void onClick(final Widget w) {
						if (cb.isChecked()) {
							selectedWidgets.add(w);
							selectedItems.add(ihc);
							checkChildren(parent);
							if (maxSelectable != 0
									&& selectedWidgets.size() > maxSelectable) {
								((CheckBox) selectedWidgets.get(0))
										.setChecked(false);
								selectedItems.remove(0);
								selectedWidgets.remove(0);
							}
						} else {
							selectedItems.remove(ihc);
							selectedWidgets.remove(w);
						}
					}
				});
			}
		}
		return t;
	}

	private void checkChildren(final ExtendedTreeItem parent) {
		if (maxSelectable > 1 || maxSelectable == 0) {
			for (int i = 0; i < parent.getChildCount(); ++i) {
				((CheckBox) ((ExtendedTreeItem) parent.getChild(i)).getWidget())
						.setChecked(true);
				selectedItems.add(((ExtendedTreeItem) parent.getChild(i))
						.getObject());
				checkChildren(((ExtendedTreeItem) parent.getChild(i)));
			}
		}
	}

	/**
	 * Special version of contains that uses equals instead of hashcode
	 * 
	 * @param c
	 * @param o
	 * @return
	 */
	public static boolean contains(final Collection c, final Object o) {
		if (c == null)
			return false;
		final Iterator itr = c.iterator();
		if (o == null)
			return false;
		while (itr.hasNext()) {
			if (itr.next().equals(o)) {
				return true;
			}
		}
		return false;
	}

	public Object get(final Widget editWidget) {
		if (maxSelectable == 1 && selectedItems.size() > 0)
			return selectedItems.get(0);
		else if (selectedItems.size() > 0)
			return new HashSet(selectedItems);
		else
			return null;
	}
	public Collection get(final GenericAttribute ga) {
		Collection v = ((MineralConstraint) ga.getConstraint()).getMinerals();
		return v;
	}
	public Collection get(final MObject obj) {
		final Object o = mGet(obj);
		if (o instanceof Collection)
			return (Collection) mGet(obj);
		else if (o != null) {
			final HashSet temp = new HashSet();
			temp.add(o);
			return temp;
		} else {
			return null;
		}
	}
	public void set(final MObject obj, final Object v) {
		if (v instanceof Collection) {
			final Collection c = (Collection) (v != null
					&& ((Collection) v).size() > 0 ? v : null);
			if (maxSelectable == 1 && c != null)
				mSet(obj, c.toArray()[0]);
			else
				mSet(obj, c);
		} else {
			mSet(obj, v);
		}
	}

	public void onClick(final Widget sender) {
		if (sender == expand) {
			final Iterator treeItr = trees.iterator();
			while (treeItr.hasNext()) {
				final Iterator itr = ((Tree) treeItr.next()).treeItemIterator();
				while (itr.hasNext()) {
					final Object obj = itr.next();
					if (obj instanceof ExtendedTreeItem) {
						((ExtendedTreeItem) obj).setState(true);
					}
				}
			}

		} else if (sender == collapse) {
			final Iterator treeItr = trees.iterator();
			while (treeItr.hasNext()) {
				final Iterator itr = ((Tree) treeItr.next()).treeItemIterator();
				while (itr.hasNext()) {
					final Object obj = itr.next();
					if (obj instanceof ExtendedTreeItem) {
						((ExtendedTreeItem) obj).setState(false);
					}
				}
			}
		}
	}
}
