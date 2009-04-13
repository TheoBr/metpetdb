package edu.rpi.metpetdb.client.ui.input.attributes;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ToggleButton;
import com.google.gwt.user.client.ui.Widget;

import edu.rpi.metpetdb.client.model.interfaces.HasChildren;
import edu.rpi.metpetdb.client.model.interfaces.MObject;
import edu.rpi.metpetdb.client.model.validation.ObjectConstraint;
import edu.rpi.metpetdb.client.model.validation.PropertyConstraint;
import edu.rpi.metpetdb.client.model.validation.ValueInCollectionConstraint;
import edu.rpi.metpetdb.client.ui.widgets.MHtmlList;
import edu.rpi.metpetdb.client.ui.widgets.MPartialCheckBox;
import edu.rpi.metpetdb.client.ui.widgets.MPartialCheckBox.CheckedState;

/**
 * 
 * @author goldfd, lindez
 * 
 * The FlyOutAttribute widget is used when the user needs to select items in a 
 * static tree structure, such as the list of searchable minerals. The widget is 
 * composed of FlyOutItems, each having a checkbox, value, and a possible "more" 
 * button which can be clicked to show the children of that item.
 *
 * @param <T>
 */
public class FlyOutAttribute<T extends HasChildren<T>> extends GenericAttribute
		implements ClickListener {
	private HorizontalPanel container;
	
	public class FlyOutItem extends FlowPanel implements ClickListener {
		public T obj;
		public Set<FlyOutItem> children;
		public FlyOutItem parent;
		public FlowPanel fp = new FlowPanel(){
			@Override
			public void clear() {
				for (int i=0; i<this.getWidgetCount(); i++) {
					((FlyOutItem) this.getWidget(i)).setMoreButtonDown(false);
				}
				super.clear();
			}
		};
		public MPartialCheckBox cb;
		private ToggleButton moreBtn;
		
		public FlyOutItem(final FlyOutItem parent, final T value){
			this.parent = parent;
			setStyleName("flyout-item");
			obj = value;
			children = new HashSet();
			cb = new MPartialCheckBox(value.toString());
			cb.addClickListener(this);
			add(cb);
			if (value.getChildren() != null && value.getChildren().size() > 0) {
				moreBtn = new ToggleButton(
						new Image(GWT.getModuleBaseURL() + "/images/icon-flyout-more-up.png"),
						new Image(GWT.getModuleBaseURL() + "/images/icon-flyout-more-down.png"),
						new ClickListener() {
					public void onClick(Widget sender) {
						Widget item = sender.getParent();
						
						// clear flyout lists further down the line
						final int currentIndex = container.getWidgetIndex(item.getParent());
						for (int i = container.getWidgetCount()-1; i > currentIndex; i--) {
							((FlowPanel) container.getWidget(i)).clear();
							container.remove(i);
						}
						
						if (moreBtn.isDown()) {
							// reset sibling moreBtns
							final int siblingCount = ((FlowPanel) item.getParent()).getWidgetCount();
							final int myIndex = ((FlowPanel) item.getParent()).getWidgetIndex(item);
							for (int i=0; i<siblingCount; i++) {
								if (i!=myIndex) {
									((FlyOutItem) ((FlowPanel) item.getParent()).getWidget(i)).setMoreButtonDown(false);
								}
							}
							
							//add children to display
							Iterator<FlyOutItem> itr = children.iterator();
							while (itr.hasNext()){
								fp.add(itr.next());
							}
							fp.setStyleName("flyout-list");
							container.add(fp);
							
							// position flyout list at correct height
							final int offset = item.getAbsoluteTop()-container.getAbsoluteTop()-11;
							DOM.setStyleAttribute(fp.getElement(), "marginTop", offset+"px");
						}
					}
				});
				moreBtn.setStyleName("morebutton");
				add(moreBtn);
			}
		}
		
		public void addChild(final FlyOutItem fo){
			children.add(fo);
		}
		
		public void setState(final CheckedState state){
			cb.setState(state);
		}
		
		public CheckedState getState(){
			return cb.getState();
		}
		
		public MPartialCheckBox getCheckBox() {
			return cb;
		}
		
		/**
		 * Set the More button down. Used mostly to reset sibling
		 * buttons when another button is pressed.
		 * @param down
		 */
		public void setMoreButtonDown(boolean down) {
			if (moreBtn != null)
				moreBtn.setDown(down);
		}
		
		public void onClick(final Widget sender){
			if (cb.getState() == CheckedState.CHECKED) {
				selectedWidgets.add(this);
				selectedItems.add(obj);
			} else if (selectedWidgets.contains(this)){
					selectedWidgets.remove(this);
					selectedItems.remove(obj);
			}
			checkChildren(this, cb.getState());
			checkParents(this.parent, cb.getState());
		}
		
	}


	private ArrayList<T> selectedItems = new ArrayList<T>();
	private ArrayList<Widget> selectedWidgets = new ArrayList<Widget>();
	private final ArrayList<Widget> trees;
	private final int maxSelectable;

	/**
	 * Creates a FlyOutAttribute with no children
	 * @param pc
	 * @param i
	 */
	public FlyOutAttribute(final PropertyConstraint pc, final int i) {
		this(pc, i, 0);
	}

	/**
	 * Creates a FlyOutAttribute
	 * @param pc
	 * @param i
	 * @param maxSelectable
	 */
	public FlyOutAttribute(final PropertyConstraint pc, final int i,
			final int maxSelectable) {
		super(pc);
		container = new HorizontalPanel();
		container.setStyleName("flyout");
		trees = new ArrayList<Widget>();
		this.maxSelectable = maxSelectable;
	}
	
	public ArrayList<?> getSelectedWidgets() {
		return selectedWidgets;
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
			final FlyOutItem fo2 = new FlyOutItem(fo,child);
			if (get(obj) != null && get(obj).contains(child)
					|| contains(get(obj), child)){
				fo2.setState(CheckedState.CHECKED);
				selectedWidgets.add(fo2);
				selectedItems.add(fo2.obj);
				if (fo.getState() == CheckedState.UNCHECKED){
					fo.setState(CheckedState.PARTIALLY_CHECKED);
				}
			}
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
				final FlyOutItem fo= new FlyOutItem(null,parent);
				if (get(obj) != null && get(obj).contains(parent)
						|| contains(get(obj), parent)) {
					fo.setState(CheckedState.CHECKED);
					selectedWidgets.add(fo);
					selectedItems.add(fo.obj);
				}
				if (parent.getChildren() != null && parent.getChildren().size() > 0) {
					makeChildren(fo,parent, obj);
				} 
				fp.add(fo);				
			}
		}
		fp.setStyleName("flyout-parents");
		return fp;
	}

	private void checkChildren(final FlyOutItem parent,
			final CheckedState state) {
		if (maxSelectable > 1 || maxSelectable == 0) {
			Iterator<FlyOutItem> itr = parent.children.iterator();
			while(itr.hasNext()){
				FlyOutItem fo = itr.next();
				fo.setState(state);
				if (state == CheckedState.CHECKED){
					selectedWidgets.add(fo);
					selectedItems.add(fo.obj);
				} else if (state == CheckedState.UNCHECKED){
					selectedWidgets.remove(fo);
					selectedItems.remove(fo.obj);
				}
				checkChildren(fo, state);
			}
		}
	}
	
	private void checkParents(final FlyOutItem parent,
			final CheckedState state) {
		if (parent != null){
			Iterator<FlyOutItem> itr = parent.children.iterator();
			if (state == CheckedState.CHECKED){
				boolean allSelected = true;
				while(itr.hasNext()){
					FlyOutItem fo = itr.next();
					CheckedState childState = fo.getState();
					if (childState == CheckedState.PARTIALLY_CHECKED || childState == CheckedState.UNCHECKED){
						allSelected = false;
						parent.cb.setState(CheckedState.PARTIALLY_CHECKED);
						if (selectedWidgets.contains(parent)){
							selectedWidgets.remove(parent);
							selectedItems.remove(parent.obj);
						}
					}
				}
				if (allSelected){
					parent.cb.setState(CheckedState.CHECKED);
					selectedWidgets.add(parent);
					selectedItems.add(parent.obj);
				}
			} else if (state == CheckedState.UNCHECKED){
				if (selectedWidgets.contains(parent)){
					selectedWidgets.remove(parent);
					selectedItems.remove(parent.obj);
				}
				boolean anySelected = false;
				while(itr.hasNext()){
					FlyOutItem fo = itr.next();
					CheckedState childState = fo.getState();
					if (childState == CheckedState.PARTIALLY_CHECKED || childState == CheckedState.CHECKED){
						anySelected = true;
						parent.cb.setState(CheckedState.PARTIALLY_CHECKED);
					}
				}
				if (!anySelected){
					parent.cb.setState(CheckedState.UNCHECKED);
				}
			}
			checkParents(parent.parent, state);
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
	
	public void addClickListener(final ClickListener c){
		for (int i = 0; i < ((FlowPanel)container.getWidget(0)).getWidgetCount(); i++){
			((FlyOutItem)((FlowPanel)container.getWidget(0)).getWidget(i)).getCheckBox().addClickListener(c);
			addClickListenerRecursive(((FlyOutItem)((FlowPanel)container.getWidget(0)).getWidget(i)),c);
		}
	}
	
	private void addClickListenerRecursive(final FlyOutItem parent, final ClickListener c){
		for (FlyOutItem child : parent.children){
			child.getCheckBox().addClickListener(c);
			addClickListenerRecursive(child,c);
		}
	}

	public void onClick(final Widget sender) {
		
	}
}
