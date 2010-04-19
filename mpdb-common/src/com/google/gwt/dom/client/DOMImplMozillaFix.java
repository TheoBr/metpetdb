package com.google.gwt.dom.client;

/**
 * Workaround for GWT issue "DOMImplMozilla.isOrHasChild throws NS_ERROR_DOM_NOT_SUPPORTED_ERR"
 *
 * Aug 21, 2009
 *
 * @author Alex
 */
public class DOMImplMozillaFix extends DOMImplMozilla {

  @Override
  public boolean isOrHasChild(Element parent, Element child) {
    try {
      return super.isOrHasChild(parent, child);
    }
    catch (Throwable e) {
      // we got an exception presumably from using compareDocumentPosition in superclass
      // try delegating to the old GWT implementation of this method that doesn't use compareDocumentPosition
      try {
        // using try/catch block just in case, but in my testing this never throws an exception:
        return isOrHasChild_OldImpl(parent, child);
      }
      catch (Throwable e2) {
        return false;  // give up
      }
    }
  }

  /**
   * This is how isOrHasChild was originally implemented in GWT 1.4
   * before GWT started using the buggy compareDocumentPosition for supposed
   * performace gains.
   */
  private native boolean isOrHasChild_OldImpl(Element parent, Element child) /*-{
    while (child) {
      if (parent.isSameNode(child)) {
        return true;
      }

      try {
        child = child.parentNode;
      } catch(e) {
        // Give up on 'Permission denied to get property
        // HTMLDivElement.parentNode'
        // See https://bugzilla.mozilla.org/show_bug.cgi?id=208427
        return false;
      }

      if (child && (child.nodeType != 1)) {
        child = null;
      }
    }
    return false;
  }-*/;
}
