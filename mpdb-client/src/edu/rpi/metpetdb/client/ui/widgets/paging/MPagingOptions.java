package edu.rpi.metpetdb.client.ui.widgets.paging;

import com.google.gwt.core.client.GWT;
import com.google.gwt.gen2.table.client.PagingScrollTable;
import com.google.gwt.gen2.table.event.client.PageChangeEvent;
import com.google.gwt.gen2.table.event.client.PageChangeHandler;
import com.google.gwt.gen2.table.event.client.PageCountChangeEvent;
import com.google.gwt.gen2.table.event.client.PageCountChangeHandler;
import com.google.gwt.gen2.table.event.client.PageLoadEvent;
import com.google.gwt.gen2.table.event.client.PageLoadHandler;
import com.google.gwt.gen2.table.event.client.PagingFailureEvent;
import com.google.gwt.gen2.table.event.client.PagingFailureHandler;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.InlineHTML;
import com.google.gwt.user.client.ui.KeyboardListenerAdapter;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

import edu.rpi.metpetdb.client.ui.CSS;
import edu.rpi.metpetdb.client.ui.widgets.NumericKeyboardListener;

/**
 * @modified lindez
 * 
 * This is a reimplementation of the gen2 PagingOptions class
 * because the original version is not flexible.
 * 
 * A panel that wraps a {@link PagingScrollTable} and includes options to
 * manipulate the page.
 * 
 * <h3>CSS Style Rules</h3>
 * 
 * <ul class="css">
 * 
 * <li>.pagination { applied to the entire widget }</li>
 * 
 * <li>.pagination .errorMessage { applied to the error message }</li>
 * 
 * <li>.pagination-first { the first page button }</li>
 * 
 * <li>.pagination-last { the last page button }</li>
 * 
 * <li>.pagination-next { the next page button }</li>
 * 
 * <li>.pagination-prev { the previous page button }</li>
 * 
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
    this.table = table;

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
        loadingImage.addStyleName(CSS.INVISIBLE);
        errorLabel.setHTML("");
      }
    });
    table.addPageChangeHandler(new PageChangeHandler() {
      public void onPageChange(PageChangeEvent event) {
        curPageBox.setText((event.getNewPage() + 1) + "");
        loadingImage.removeStyleName(CSS.INVISIBLE);
        errorLabel.setHTML("");
      }
    });
    table.addPagingFailureHandler(new PagingFailureHandler() {
      public void onPagingFailure(PagingFailureEvent event) {
        loadingImage.addStyleName(CSS.INVISIBLE);
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
    ClickListener listener = new ClickListener() {
      public void onClick(Widget sender) {
        if (sender == firstImage) {
          table.gotoFirstPage();
        } else if (sender == lastImage) {
          table.gotoLastPage();
        } else if (sender == nextImage) {
          table.gotoNextPage();
        } else if (sender == prevImage) {
          table.gotoPreviousPage();
        }
      }
    };

    // Add the listener to each image
    firstImage.addClickListener(listener);
    prevImage.addClickListener(listener);
    nextImage.addClickListener(listener);
    lastImage.addClickListener(listener);
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
