
<h1>General Layout</h1>
<p>This is the required HTML and CSS for the general layout of a page's structure.</p>

<h2>Structure</h2>

<h3>Doctype</h3>
<p>The doctype is set to XHTML 1.0 Transitional so that everything displays as it should cross-browser.</p>
<pre class="html">
&lt;!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"&gt;
</pre>

<h3>Safari CSS patch</h3>
<p>The lang attribute in the html tag is required to take advantage of a Safari-only CSS patch when necessary.</p>
<pre class="html">
&lt;html lang="en"&gt;
</pre>

<h3>IE-only stylesheet</h3>
<p>IE6 and IE7 patches are in a separate stylesheet conditionally included in the head tag.</p>
<pre class="html">
&lt;!--[if IE]&gt;&lt;link rel="stylesheet" type="text/css" href="/css/style-ie.css" media="screen" /&gt;&lt;![endif]--&gt;
</pre>

<h3>2- and 3-column layouts</h3>
<p>A page's content area may have 2 or 3 columns. The changes to the HTML for each are highlighted in <span class="hilite">red</span>. The content div comes before the other column(s) for higher accessibility in CSS-disabled browsers. </p>

<pre class="html twobox l">
<strong>2-column Layout</strong>

&lt;body&gt;
&lt;div id="header"&gt;&lt;/div&gt;
&lt;div id="container"&gt;
	&lt;div id="content"&gt;&lt;/div&gt;
	&lt;div id="sidebar"&gt;&lt;/div&gt;
&lt;/div&gt;
&lt;div id="footer"&gt;&lt;/div&gt;
&lt;/body&gt;



</pre>

<pre class="html twobox r">
<strong>3-column Layout</strong>

&lt;body&gt;
&lt;div id="header"&gt;&lt;/div&gt;
&lt;div id="container"&gt;
	&lt;div id="content"&gt;
		<span class="hilite">&lt;div id="main"&gt;&lt;/div&gt;
		&lt;div id="rcol"&gt;&lt;/div&gt;</span>
	&lt;/div&gt;
	&lt;div id="sidebar"&gt;&lt;/div&gt;
&lt;/div&gt;
&lt;div id="footer"&gt;&lt;/div&gt;
&lt;/body&gt;
</pre>

<h2>2-column layout</h2>
<p><strong>Note:</strong> IE-only rules are highlighted in <span class="ie">blue</span> to designate their inclusion in the separate IE-only stylesheet.</p>

<h3>&lt;body&gt;</h3>
<p>The body has its default margins zero'd out and is the full width of the viewport. It also sets the default font and background colors, as well as a repeated background image for style purposes. It's minimum width must be set to prevent structural breakdown when viewing the 3-column layout in small resolutions.</p>
<pre>
body {
	margin: 0;
	width: 100%;
	min-width: 600px;
	color: #000;
	background: #fff url(/images/bg.gif) repeat-y left top;
}
</pre>

<h3>&lt;div id="header"&gt;</h3>
<p>The header has a background color to hide the body's background image so that it can blend with the logo png.</p>
<pre>
#header {
	background: #fff;
}
</pre>

<h3>&lt;div id="container"&gt;</h3>
<p>The container div holds the content columns. It's default behavior is to contain 2 columns by defining its left padding to be equal to the width of the left column. Its repeated background image is used to create the illusion that the left column takes up the full height of the content area. The top border is for style purposes and hidden overflow fixes browser quirks.</p>
<pre>
#container {
	padding-left: 201px;	/* LC fullwidth */
	background: transparent url(/images/bg-sidebar.gif) repeat-y left top;
	overflow: hidden;
	border-top: 1px solid #DAEE9B;
}
</pre>

<h3>&lt;div id="content"&gt;</h3>
<p>The content div contains the content in the 2-column layout and both the center and right columns in the 3-column layout. It is floated left and takes up the entire available space.</p>
<pre>
#content {
	position: relative;
	float: left;
	width: 100%;
}
</pre>

<h3>&lt;div id="sidebar"&gt;</h3>
<p>The sidebar (left column) has a static width for more control, and must be pulled all the way through the content column width plus the container div's left padding with a negative margin and right positioning. It also has a different default font color. IE6 needs it moved over a bit to account for the background image.</p>
<pre>
#sidebar {
	position: relative;
	float: left;
	width: 200px;			/* LC width - 1px border bg image */
	right: 201px;			/* LC fullwidth */
	margin-left: -100%;
	padding-right: 1px;		/*  1px border bg image */
	color: #3a3436;
}
<span class="ie">* html #sidebar {
	left: 1px;
}</span>
</pre>

<h3>&lt;div id="footer"&gt;</h3>
<p>The footer clears the floated content columns and has a top border for style. In IE6 it must be positioned a tad from the left.</p>
<pre>
#footer {
	clear: both;
	position: relative;
	border-top: 1px solid #cddbb5;
}
<span class="ie">* html #footer {
	left: 8px;
}</span>
</pre>

<h3>Direct child padding</h3>
<p>To make the contents readable, a padding is applied to all direct child elements of the content, sidebar, rcol, and footer divs. The padding is applied to the elements instead of the structural divs themselves to allow certain elements to span from edge to edge of the content area.</p>
<pre>
#content div, #content p, #content ul, #content ol {
	padding-left: 1.5em;
	padding-right: 1.5em;
} #content * div, #content * p, #content * ul, #content * ol {
	padding-left: 0;
	padding-right: 0;
}
</pre>
<p>In the interest of fitting more text (without sacrificing readability), the left and right column children also have a different font size and line height.</p>
<pre>
#sidebar div, #sidebar ul, #sidebar p,
#rcol div, #rcol ul, #rcol p {
	font-size: 0.9em;
	line-height: 1.4em;
	padding-left: 0.7em;
	padding-right: 0.7em;
} #sidebar div div, #sidebar ul ul,
  #rcol div div, #rcol ul ul {
	font-size: 1em;
	line-height: 1em;
} #sidebar * div, #sidebar * ul,
  #rcol * div, #rcol * ul {
	padding-left: 0;
	padding-right: 0;
}
</pre>
<p>Headings have different sized paddings because they have different default font sizes.</p>
<pre>
#content h1 {
	padding-left: 0.75em;
	padding-right: 0.75em;
} #content * h1 {
	padding-left: 0;
	padding-right: 0;
}
#content h2 {
	padding-left: 1em;
	padding-right: 1em;
} #content * h2 {
	padding-left: 0;
	padding-right: 0;
}
</pre>

<h2>3-column layout</h2>
<p>In the 3-col layout, the main div and right column div are both placed inside the content div.</p>

<h3>&lt;div id="main"&gt;</h3>
<p>The main div is where the main content goes. It is floated to the left and has a percentage width. It also has negative left and right margins to counter the content  div's direct child padding. It has a bottom padding for readability.</p>
<pre>
#main {
	float: left;
	width: 72%;
	margin: 0 -1.5em;
	padding-bottom: 1em;
}
</pre>

<h3>&lt;div id="rcol"&gt;</h3>
<p>The right column is floated to the right and has a percentage width with no margin or padding. The repeated background image acts as a left border.</p>
<pre>
#rcol {
	width: 28%;
	margin: 0;
	float: right;
	background: transparent url(/images/bg-rcol.gif) repeat-y left top;
	padding: 0 !important;
}
</pre>

<h3>Direct child padding</h3>
<p>Since the main div acts like the content div, it has the same direct child padding rules.</p>
<pre>
#main div, #main p, #main ul, #main ol {
	padding-left: 1.5em !important;
	padding-right: 1.5em !important;
} #main div div, #main div p, #main ul ul, #main ol ol {
	padding-left: 0 !important;
	padding-right: 0 !important;
}
#main h1 {
	padding-left: 0.75em;
	padding-right: 0.75em;
} #main * h1 {
	padding-left: 0;
	padding-right: 0;
}
#main h2 {
	padding-left: 1em;
	padding-right: 1em;
} #main * h2 {
	padding-left: 0;
	padding-right: 0;
}
</pre>