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

import edu.rpi.metpetdb.client.model.interfaces.MObject;
import edu.rpi.metpetdb.client.model.interfaces.HasChildren;
import edu.rpi.metpetdb.client.model.validation.ObjectConstraint;
import edu.rpi.metpetdb.client.model.validation.PropertyConstraint;
import edu.rpi.metpetdb.client.model.validation.ValueInCollectionConstraint;
import edu.rpi.metpetdb.client.ui.widgets.MHtmlList;

/**
 * 
 * @author anthony
 * 
 * @param <
 * 		T> the type of the items in the tree
 */
public class TreeAttribute<T extends HasChildren<T>> extends GenericAttribute
		implements ClickListener {

	protected final SimplePanel container;

	private class ExtendedTreeItem extends TreeItem {
		private T obj;

		public ExtendedTreeItem(final Widget w, final T o) {
			super(w);
			obj = o;
		}

		public ExtendedTreeItem(final String s, final T o) {
			super(s);
			obj = o;
		}

		public T getObject() {
			return obj;
		}

		public void setObject(final T o) {
			obj = o;
		}
	}

	private ArrayList<T> selectedItems = new ArrayList<T>();
	private ArrayList<Widget> selectedWidgets = new ArrayList<Widget>();
	private final int numberOfColumns;
	private final Button collapse;
	private final Button expand;
	private final ArrayList<Tree> trees;
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
		trees = new ArrayList<Tree>();
		this.maxSelectable = maxSelectable;
	}

	public ArrayList<?> getSelectedItems() {
		return selectedItems;
	}
	public void setSelectedItems(final ArrayList<T> al) {
		selectedItems = al;
	}

	public Widget[] createDisplayWidget(final MObject obj) {
		final Collection<T> c = get(obj);
		return createDisplayWidget(obj, c);
	}

	public Widget[] createDisplayWidget(final MObject obj, final Collection<?> c) {
		final MHtmlList list = new MHtmlList();
		if (c != null) {
			final Iterator<?> itr = c.iterator();
			while (itr.hasNext()) {
				final Object object = itr.next();
				final Label l = new Label(object.toString());
				list.add(l);
			}
		}
		container.setWidget(list);
		return new Widget[] {
			container
		};
	}

	public Widget[] createEditWidget(final MObject obj, final String id,
			final GenericAttribute ga) {
		final FlowPanel fp = new FlowPanel();
		final HorizontalPanel hp = new HorizontalPanel();
		selectedItems.clear();
		selectedWidgets.clear();
		final Collection<T> treeItems = get(ga);
		if (treeItems != null) {
			final Iterator<T> itr = treeItems.iterator();
			final int numMinerals = (int) Math
					.ceil((double) (treeItems.size() / (double) numberOfColumns));
			int count = 0;
			for (int i = 0; i < numberOfColumns; ++i) {
				// Find out how many items go in this tree
				final Collection<T> treeItemSubset = new ArrayList<T>();
				while (itr.hasNext() && count < numMinerals) {
					treeItemSubset.add(itr.next());
					count++;
				}
				final Tree t = makeTree(treeItemSubset, obj);
				applyStyle(t, true);
				hp.add(t);
				trees.add(t);
				count = 0;
			}
			fp.add(hp);
			fp.add(expand);
			fp.add(collapse);
		}
		return new Widget[] {
			fp
		};
	}

	private TreeItem makeChildren(final Collection<T> parents,
			final TreeItem root, final MObject obj) {
		if (parents != null) {
			Iterator<T> itr = parents.iterator();
			while (itr.hasNext()) {
				final T parent = itr.next();
				final CheckBox cb = new CheckBox(parent.toString());

				if (get(obj) != null && get(obj).contains(parent)
						|| contains(get(obj), parent)) {
					cb.setChecked(true);
					selectedItems.add(parent);
					selectedWidgets.add(cb);
				}
				final ExtendedTreeItem parentTreeItem = new ExtendedTreeItem(
						cb, parent);
				if (parent.getChildren() != null
						&& parent.getChildren().size() > 0) {
					root.addItem(makeChildren(parent.getChildren(),
							parentTreeItem, obj));
				} else
					root.addItem(parentTreeItem);

				cb.addClickListener(new ClickListener() {
					public void onClick(final Widget w) {
						checkChildren(parentTreeItem, cb.isChecked());
						if (cb.isChecked()) {
							selectedWidgets.add(w);
							selectedItems.add(parent);
							if (maxSelectable != 0
									&& selectedWidgets.size() > maxSelectable) {
								((CheckBox) selectedWidgets.get(0))
										.setChecked(false);
								selectedItems.remove(0);
								selectedWidgets.remove(0);
							}
						} else {
							selectedItems.remove(parent);
							selectedWidgets.remove(w);
						}
					}
				});
			}
		}
		return root;
	}
	private Tree makeTree(final Collection<T> parents, final MObject obj) {
		final Tree t = new Tree();
		if (parents != null) {
			Iterator<T> itr = parents.iterator();
			while (itr.hasNext()) {
				final T parent = itr.next();
				final CheckBox cb = new CheckBox(parent.toString());

				if (get(obj) != null && get(obj).contains(parent)
						|| contains(get(obj), parent)) {
					cb.setChecked(true);
					selectedItems.add(parent);
					selectedWidgets.add(cb);
				}
				final ExtendedTreeItem parentTreeItem = new ExtendedTreeItem(
						cb, parent);
				if (parent.getChildren() != null
						&& parent.getChildren().size() > 0) {
					t.addItem(makeChildren(parent.getChildren(),
							parentTreeItem, obj));
				} else
					t.addItem(parentTreeItem);

				cb.addClickListener(new ClickListener() {
					public void onClick(final Widget w) {
						checkChildren(parentTreeItem, cb.isChecked());
						if (cb.isChecked()) {
							selectedWidgets.add(w);
							selectedItems.add(parent);
							if (maxSelectable != 0
									&& selectedWidgets.size() > maxSelectable) {
								((CheckBox) selectedWidgets.get(0))
										.setChecked(false);
								selectedItems.remove(0);
								selectedWidgets.remove(0);
							}
						} else {
							selectedItems.remove(parent);
							selectedWidgets.remove(w);
						}
					}
				});
			}
		}
		return t;
	}

	private void checkChildren(final ExtendedTreeItem parent,
			final boolean check) {
		if (maxSelectable > 1 || maxSelectable == 0) {
			for (int i = 0; i < parent.getChildCount(); ++i) {
				((CheckBox) ((ExtendedTreeItem) parent.getChild(i)).getWidget())
						.setChecked(check);
				if (check) {
					selectedItems.add(((ExtendedTreeItem) parent.getChild(i))
							.getObject());
				} else {
					selectedItems
							.remove(((ExtendedTreeItem) parent.getChild(i))
									.getObject());
				}
				checkChildren(((ExtendedTreeItem) parent.getChild(i)), check);
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
	public static boolean contains(final Collection<?> c, final Object o) {
		if (c == null)
			return false;
		final Iterator<?> itr = c.iterator();
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
			return new HashSet<T>(selectedItems);
		else
			return null;
	}
	public Collection<T> get(final GenericAttribute ga) {
		final PropertyConstraint pc = ga.getConstraint();
		if (pc instanceof ObjectConstraint) {
			final ObjectConstraint oc = (ObjectConstraint) ga.getConstraint();
			return (Collection<T>) oc.getValueInCollectionConstraint()
					.getValues();
		} else {
			final ValueInCollectionConstraint voc = (ValueInCollectionConstraint) ga
					.getConstraint();
			return (Collection<T>) voc.getValues();
		}
	}
	public Collection<T> get(final MObject obj) {
		final Object o = mGet(obj);
		if (o instanceof Collection)
			return (Collection<T>) mGet(obj);
		else if (o != null) {
			final Collection<T> temp = new HashSet<T>();
			temp.add((T) o);
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
			final Iterator<Tree> treeItr = trees.iterator();
			while (treeItr.hasNext()) {
				final Iterator<TreeItem> itr = ((Tree) treeItr.next())
						.treeItemIterator();
				while (itr.hasNext()) {
					final TreeItem obj = itr.next();
					if (obj instanceof TreeAttribute.ExtendedTreeItem) {
						((ExtendedTreeItem) obj).setState(true);
					}
				}
			}

		} else if (sender == collapse) {
			final Iterator<Tree> treeItr = trees.iterator();
			while (treeItr.hasNext()) {
				final Iterator<TreeItem> itr = ((Tree) treeItr.next())
						.treeItemIterator();
				while (itr.hasNext()) {
					final Object obj = itr.next();
					if (obj instanceof TreeAttribute.ExtendedTreeItem) {
						((ExtendedTreeItem) obj).setState(false);
					}
				}
			}
		}
	}
}
