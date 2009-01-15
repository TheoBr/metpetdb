
<h1>Dialog Box</h1>
<p>Styles specifically for dialog boxes overlayed onto the page via GWT, currently only applicable to the Login box.</p>
<p>Preview this page's template: <a href="http://mpdb.zakness.com/login.php" title="http://mpdb.zakness.com/login.php">http://mpdb.zakness.com/login.php</a></p>
<p>Comment on this page on the wiki: <a href="http://trinity.db.cs.rpi.edu/xwiki/bin/view/Website/tptLogin" title="http://trinity.db.cs.rpi.edu/xwiki/bin/view/Website/tptLogin">http://trinity.db.cs.rpi.edu/xwiki/bin/view/Website/tptLogin</a></p>

<p>The box itself has a background color, padding, border, and set width (in ems).</p>
<pre>
.gwt-DialogBox {
	background: #fff;
	padding: 1em;
	border: 1px solid #759140;
	width: 23em;
}
</pre>
<p>The tab bar has a background image that repeats along the bottom.</p>
<pre>
.gwt-TabBar tr {
	background: transparent url(/images/tabbar-bg.gif) repeat-x scroll left bottom;
}
</pre>
<p>The tab bar items have a background color, border, margins, padding and have the pointer cursor style.</p>
<pre>
.gwt-TabBar .gwt-TabBarItem {
	background: #F5F7F1 none repeat scroll 0%;
	border: 1px solid #AFCF73;
	cursor: pointer;
	margin: 0pt 0.2em;
	padding: 0.2em 0.4em;
}
</pre>
<p>The selected item has its background color changed and bottom border removed to make it look like a part of the selected section.</p>
<pre>
.gwt-TabBar .gwt-TabBarItem-selected {
	background: #FFFFFF none repeat scroll 0%;
	border-bottom-color: #FFFFFF;
	font-weight: bold;
}
</pre>


