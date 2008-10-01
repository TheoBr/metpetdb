package edu.rpi.metpetdb.client.ui.input.attributes;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.MouseListener;
import com.google.gwt.user.client.ui.Widget;

import edu.rpi.metpetdb.client.model.interfaces.HasChildren;
import edu.rpi.metpetdb.client.model.interfaces.MObject;
import edu.rpi.metpetdb.client.model.validation.ObjectConstraint;
import edu.rpi.metpetdb.client.model.validation.PropertyConstraint;
import edu.rpi.metpetdb.client.model.validation.ValueInCollectionConstraint;
import edu.rpi.metpetdb.client.ui.widgets.MHtmlList;
import edu.rpi.metpetdb.client.ui.widgets.MPartialCheckBox;

public class FlyOutAttribute<T extends HasChildren<T>> extends GenericAttribute
		implements ClickListener {
	private HorizontalPanel container;
	
	private class FlyOutItem extends FocusPanel implements ClickListener{
		public T obj;
		public Set<FlyOutItem> children;
		public FlyOutItem parent;
		public FlowPanel fp = new FlowPanel();
		public MPartialCheckBox cb;
		public FlyOutItem(final T parent){
			obj = parent;
			final HorizontalPanel hp = new HorizontalPanel();
			children = new HashSet();
			cb = new MPartialCheckBox(parent.toString());
			hp.add(cb);
			if (parent.getChildren() != null
					&& parent.getChildren().size() > 0) {
				final Label lb = new Label(">>");
				hp.add(lb);
			}					
			add(hp);
			this.addMouseListener(new MouseListener(){
				public void onMouseUp(final Widget sender, final int x, final int y){

				}
				public void onMouseEnter(final Widget sender){	
					Iterator<FlyOutItem> itr = children.iterator();
					while (itr.hasNext()){
						fp.add(itr.next());
					}
					container.add(fp);
				}
				public void onMouseDown(final Widget sender, final int x, final int y){
					
				}
				public void onMouseLeave(final Widget sender){
					container.remove(fp);
				}
				public void onMouseMove(final Widget sender, final int x, final int y){
					
				}
			});
			this.addClickListener(this);
		}
		
		public void addChild(final FlyOutItem fo){
			children.add(fo);
		}
		
		public void setState(final int state){
			cb.setState(state);
		}
		
		public int getState(){
			return cb.getState();
		}
		
		public void onClick(final Widget sender){
			checkChildren(this, cb.getState());
		}
		
	}


	private ArrayList<T> selectedItems = new ArrayList<T>();
	private ArrayList<Widget> selectedWidgets = new ArrayList<Widget>();
	private final ArrayList<Widget> trees;
	private final int maxSelectable;

	public FlyOutAttribute(final PropertyConstraint pc, final int i) {
		this(pc, i, 0);
	}

	public FlyOutAttribute(final PropertyConstraint pc, final int i,
			final int maxSelectable) {
		super(pc);
		container = new HorizontalPanel();
		trees = new ArrayList<Widget>();
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
		container.add(list);
		return new Widget[] {
			container
		};
	}

	public Widget[] createEditWidget(final MObject obj, final String id,
			final GenericAttribute ga) {
		selectedItems.clear();
		selectedWidgets.clear();
		container.clear();
		final Collection<T> treeItems = get(ga);
		if (treeItems != null) {
			final Widget t = makeWidget(treeItems, obj);
			container.add(t);
		}
		return new Widget[] {
			container
		};
	}

	private void makeChildren(final FlyOutItem fo, final T parent, final MObject obj) {
		Iterator<T> itr = parent.getChildren().iterator();
		while (itr.hasNext()){
			final T child = itr.next();
			final FlyOutItem fo2 = new FlyOutItem(child);
			if (child.getChildren() != null
					&& child.getChildren().size() > 0) {
				makeChildren(fo2, child, obj);
			} 
			fo.addChild(fo2);
		}
	}
	private Widget makeWidget(final Collection<T> parents, final MObject obj) {
		final FlowPanel fp = new FlowPanel();
		if (parents != null) {
			Iterator<T> itr = parents.iterator();
			while (itr.hasNext()) {
				final T parent = itr.next();
				final FlyOutItem fo= new FlyOutItem(parent);
//				if (get(obj) != null && get(obj).contains(parent)
//						|| contains(get(obj), parent)) {
//					cb.setChecked(true);
//					selectedItems.add(parent);
//					selectedWidgets.add(cb);
//				}
				if (parent.getChildren() != null && parent.getChildren().size() > 0) {
					makeChildren(fo,parent, obj);
				} 
				fp.add(fo);				
			}
		}
		fp.setStyleName("inline");
		return fp;
	}

	private void checkChildren(final FlyOutItem parent,
			final int state) {
		if (maxSelectable > 1 || maxSelectable == 0) {
			Iterator<FlyOutItem> itr = parent.children.iterator();
			while(itr.hasNext()){
				FlyOutItem fo = itr.next();
				fo.setState(state);
				if (state == 2){
					selectedWidgets.add(fo);
					selectedItems.add(fo.obj);
				} else if (state == 0){
					selectedWidgets.remove(fo);
					selectedItems.remove(fo.obj);
				}
				checkChildren(fo, state);
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
		
	}
}
