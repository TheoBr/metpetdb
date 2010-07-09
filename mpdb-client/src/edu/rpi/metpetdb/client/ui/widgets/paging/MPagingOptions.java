package edu.rpi.metpetdb.client.ui.widgets.paging;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.gen2.table.client.PagingScrollTable;
import com.google.gwt.gen2.table.event.client.PageChangeEvent;
import com.google.gwt.gen2.table.event.client.PageChangeHandler;
import com.google.gwt.gen2.table.event.client.PageCountChangeEvent;
import com.google.gwt.gen2.table.event.client.PageCountChangeHandler;
import com.google.gwt.gen2.table.event.client.PageLoadEvent;
import com.google.gwt.gen2.table.event.client.PageLoadHandler;
import com.google.gwt.gen2.table.event.client.PagingFailureEvent;
import com.google.gwt.gen2.table.event.client.PagingFailureHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.InlineHTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

import edu.rpi.metpetdb.client.ui.CSS;
import edu.rpi.metpetdb.client.ui.JS;
import edu.rpi.metpetdb.client.ui.widgets.NumericKeyboardListener;

/**
 * @modified lindez, millib2
 * 
 * This is a re-implementation of the gen2 PagingOptions class
 * because the original version is not flexible.
 * 
 * A panel that wraps a {@link PagingScrollTable} and includes options to
 * manipulate the page.
 * 
 * <h3>CSS Style Rules</h3>
 * <ul class="css">
 * <li>.pagination { applied to the entire widget }</li>
 * <li>.pagination .errorMessage { applied to the error message }</li>
 * <li>.pagination-first { the first page button }</li>
 * <li>.pagination-last { the last page button }</li>
 * <li>.pagination-next { the next page button }</li>
 * <li>.pagination-prev { the previous page button }</li>
 * </ul>
 */
public class MPagingOptions extends Composite {

  /**
   * The default style name.
   */
  public static final String DEFAULT_STYLENAME = "pagination";
  public static final String STYLENAME_PREFIX = "pagination";

  /**
   * The label used to display errors.
   */
  private InlineHTML errorLabel;

  /**
   * Goto first page button.
   */
  private Image firstImage;

  /**
   * Goto last page button.
   */
  private Image lastImage;

  /**
   * The loading image.
   */
  private Image loadingImage;
  
  /**
   * Should an additional notice be displayed that data is being fetched.
   */
  private boolean showLoadingNotice;
  
  /**
   * Set to true once the table we belong to is loaded, and the notice positioned.
   */
  private boolean loadingNoticeReady;
  
  /**
   * Set to true while the widget is waiting for data
   */
  private boolean workInProgress;
  
  /**
   * Store a reference to a parent widget so the loading notifier can be centered on it.
   */
  private Widget parentWidget;
  
  /**
   * Widget to be shown if additional loading notice is turned on.
   */
  private PopupPanel loadingWidget;

  /**
   * Goto next page button.
   */
  private Image nextImage;

  /**
   * The HTML field that contains the number of pages.
   */
  private InlineHTML numPagesLabel;

  /**
   * Goto previous page button.
   */
  private Image prevImage;

  /**
   * The box where the user can select the current page.
   */
  private TextBox curPageBox = new TextBox();

  /**
   * The table being affected.
   */
  private PagingScrollTable<?> table;
  
  /**
   * Constructor.
   * 
   * @param table the table being affected
   */
  public MPagingOptions(PagingScrollTable<?> table) {
	  // Do not display a loading notice by default
	  this(table, false);
  }

  /**
   * Constructor.
   * 
   * @param table the table being affected
   * @param whether an additional notice will be displayed while data is being fetched
   */
  public MPagingOptions(PagingScrollTable<?> table, boolean additionalLoadingNotice) {
    this.table = table;
    
    this.showLoadingNotice = additionalLoadingNotice;

    // Create the main widget
    FlowPanel panel = new FlowPanel();
    initWidget(panel);
    setStyleName(DEFAULT_STYLENAME);

    // Create the paging image buttons
    createPageButtons();

    // Create the current page box
    createCurPageBox();

    // Create the page count label
    numPagesLabel = new InlineHTML();

    // Create the loading image
    loadingImage = new Image("images/icon-loading-list.gif");
    loadingImage.setStyleName("table-loading");
    loadingImage.addStyleName(CSS.INVISIBLE);

    // Create the loading notifier
    loadingWidget = new PopupPanel(false, false) {
    	
    	@Override
    	public void onLoad() {
    		// Do nothing ?
    	}
    	
    	/**
    	 * Whenever the loading widget is show it should update its position
    	 * based on that of the designated parent widget.
    	 */
    	@Override
    	public void show() {    		
    		int offsetWidth = loadingWidget.getOffsetWidth();

    		loadingWidget.setPopupPosition(parentWidget.getAbsoluteLeft() 
    				+ (parentWidget.getOffsetWidth() / 2) - (offsetWidth / 2), 
    				parentWidget.getAbsoluteTop() + parentWidget.getOffsetHeight());
    		
    		// TODO: Figure out what's causing this issue and fix it properly
    		// Temporary hack to get rid of duplicate notifier on search page
    		if (parentWidget.getAbsoluteTop() > 10) {
	    		super.show();
	    		JS.scrollWindowToTop();
    		}
    	}
    	
    };
    Label loadingLbl = new Label("Working...");
    loadingLbl.setStyleName("searching-popup");
    loadingWidget.setWidget(loadingLbl);
    loadingWidget.setAnimationEnabled(false);
    
    workInProgress = false;
    
    // Create the error label
    errorLabel = new InlineHTML();
    errorLabel.setStylePrimaryName("errorMessage");

    // Add the widgets to the panel
    panel.add(firstImage);
    panel.add(prevImage);
    panel.add(curPageBox);
    panel.add(numPagesLabel);
    panel.add(nextImage);
    panel.add(lastImage);
    panel.add(loadingImage);

    // Add handlers to the table
    table.addPageLoadHandler(new PageLoadHandler() {
      public void onPageLoad(PageLoadEvent event) {
    	workInProgress = false;
    	hideLoadingNotice();
        errorLabel.setHTML("");
      }
    });
    
    table.addPageChangeHandler(new PageChangeHandler() {
      public void onPageChange(PageChangeEvent event) {
        curPageBox.setText((event.getNewPage() + 1) + "");
        workInProgress = true;
        showLoadingNotice();
        errorLabel.setHTML("");
      }
    });
    
    table.addPagingFailureHandler(new PagingFailureHandler() {
      public void onPagingFailure(PagingFailureEvent event) {
    	workInProgress = false;
    	hideLoadingNotice();
        errorLabel.setHTML(event.getException().getMessage());
      }
    });
    
    table.addPageCountChangeHandler(new PageCountChangeHandler() {
      public void onPageCountChange(PageCountChangeEvent event) {
        setPageCount(event.getNewPageCount());
      }
    });
    
    setPageCount(table.getPageCount());
  }
  
	/**
	 * Display notifications that work is being done.
	 */
	protected void showLoadingNotice() {
		loadingImage.removeStyleName(CSS.INVISIBLE);
		if (showLoadingNotice && loadingNoticeReady) {
			loadingWidget.show();
		}
	}
  
  /**
   * Hide any notifications that work is being done.
   */
	protected void hideLoadingNotice() {
		loadingImage.addStyleName(CSS.INVISIBLE);
		if (showLoadingNotice && loadingNoticeReady) {
			loadingWidget.hide();
		}
	}
	
	/**
	 * Assigns a widget as the loading notice's "parent". The notice will 
	 * always be displayed centered at the bottom of its parent.
	 * 
	 * @param parent
	 */
	public void setNoticeParent(Widget parent) {
		this.parentWidget = parent;
		loadingNoticeReady = true;
		
		if (workInProgress) {
			loadingWidget.show();
		}
	}

  /**
   * @return the {@link PagingScrollTable}.
   */
  public PagingScrollTable<?> getPagingScrollTable() {
    return table;
  }

  /**
   * @return the error message widget
   */
  public InlineHTML getErrorLabel() {
	  return errorLabel;
  }
  
  /**
   * @return the loading image
   */
  public Image getLoadingImage() {
	  return loadingImage;
  }

  /**
   * Create a box that holds the current page.
   */
  private void createCurPageBox() {
    // Setup the widget
    curPageBox.setStyleName("curpage-box");
    curPageBox.setText("1");
    curPageBox.addKeyboardListener(new NumericKeyboardListener(true));
  }

  /**
   * Create a paging image buttons.
   * 
   * @param images the images to use
   */
  private void createPageButtons() {
    // Create the images
    firstImage = new Image("images/icon-paging-first.png");
    firstImage.addStyleName(STYLENAME_PREFIX + "-first");
    prevImage = new Image("images/icon-paging-prev.png");
    prevImage.addStyleName(STYLENAME_PREFIX + "-prev");
    nextImage = new Image("images/icon-paging-next.png");
    nextImage.addStyleName(STYLENAME_PREFIX + "-next");
    lastImage = new Image("images/icon-paging-last.png");
    lastImage.addStyleName(STYLENAME_PREFIX + "-last");

    // Create the listener
    ClickHandler listener = new ClickHandler() {
      public void onClick(ClickEvent event) {
        if (event.getSource() == firstImage) {
          table.gotoFirstPage();
        } else if (event.getSource() == lastImage) {
          table.gotoLastPage();
        } else if (event.getSource() == nextImage) {
          table.gotoNextPage();
        } else if (event.getSource() == prevImage) {
          table.gotoPreviousPage();
        }
      }
    };

    // Add the listener to each image
    firstImage.addClickHandler(listener);
    prevImage.addClickHandler(listener);
    nextImage.addClickHandler(listener);
    lastImage.addClickHandler(listener);
  }

  /**
   * Get the value of in the page box. If the value is invalid, it will be set
   * to 1 automatically.
   * 
   * @return the value in the page box
   */
  private int getPagingBoxValue() {
    int page = 0;
    try {
      page = Integer.parseInt(curPageBox.getText()) - 1;
    } catch (NumberFormatException e) {
      // This will catch an empty box
      curPageBox.setText("1");
    }

    // Replace values less than 1
    if (page < 1) {
      curPageBox.setText("1");
      page = 0;
    }

    // Return the 0 based page, not the 1 based visible value
    return page;
  }

  /**
   * Set the page count.
   * 
   * @param pageCount the current page count
   */
  private void setPageCount(int pageCount) {
	  if (pageCount < 0) {
		  numPagesLabel.setHTML("");
		  lastImage.setVisible(false);
	  } else {
		  numPagesLabel.setHTML("of " + pageCount);
		  numPagesLabel.setVisible(true);
		  lastImage.setVisible(true);
	  }
  }
}
