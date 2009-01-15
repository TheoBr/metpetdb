
<h1>Sidebar</h1>
<p>The styles for the sidebar. Usually this area contains user-specific navigation.</p>
<p><strong>Note:</strong> IE-only rules are highlighted in <span class="ie">blue</span> to designate their inclusion in the separate IE-only stylesheet.</p>


<h3>Titlebar</h3>
<p>The sidebar is split up into sections of user navigation. The sections each have a title bar that labels the section and usually provides a link to manage the section.</p>
<pre class="html">
&lt;div class="titlebar"&gt;My Projects (&lt;a href="" title="Manage My Projects"&gt;manage&lt;/a&gt;)&lt;/div&gt;
</pre>
<p>The titlebar is styled almost exactly the same as the breadcrumbs div (covered in the <a href="/styleguide/?p=content" title="Content Column">Content Column</a> page) except for the colors.</p>
<pre>
#sidebar div.titlebar {
	background: #dadada;
	font: 0.8em Verdana, sans-serif;
	padding: 0.8em 0.5em 0.5em;
	color: #324453;
	border-bottom: 1px solid #c8c8c8;
}
</pre>

<h3>User lists</h3>
<p>After each titlebar usually comes a list of user data. Each list item usually contains a single link.</p>
<pre class="html">
&lt;ul class="user-list"&gt;
	&lt;li&gt;&lt;a href="#" title="Saved Search 1"&gt;Saved Search 1&lt;/a&gt;&lt;/li&gt;
	&lt;li class="odd"&gt;&lt;a href="#" title="Saved Search 2"&gt;Saved Search 2&lt;/a&gt;&lt;/li&gt;
	&lt;li&gt;&lt;a href="#" title="Saved Search 3"&gt;Saved Search 3&lt;/a&gt;&lt;/li&gt;
&lt;/ul&gt;
</pre>
<p>Each list has a bottom margin and zero padding. Alternating rows are given a different background color.</p>
<pre>
#sidebar ul.user-list {
	margin-bottom: 1.2em;
	padding: 0;
}
#sidebar ul.user-list li.odd {
	background: #e6eaf1;
}
</pre>

<h3>User list links</h3>
<p>Links are block-level (so the entire width is clickable) and have a padding and bottom border. The background color changes on hover.</p>
<pre>
#sidebar ul.user-list a, #sidebar ul.user-list a:link, #sidebar ul.user-list a:visited {
	display: block;
	padding: 0.3em 0.7em;
	border: 1px solid transparent;
	border-bottom-color: #ddd;
}
#sidebar ul.user-list a:hover, #sidebar ul.user-list a:active {
	background-color: #fff !important;
}
</pre>

<h3>Current link</h3>
<p>The currently selected link is given a <strong>cur</strong> class. It has a dark background, an arrow background image, and bolded white font. The background color changes on hover.</p>
<pre>
#sidebar ul.user-list a.cur, #sidebar ul.user-list a.cur:link, #sidebar ul.user-list a.cur:visited  {
	font-weight: bold;
	color: #fff;
	background: #092a53 url(/images/arrow-white.gif) no-repeat right center;
}
#sidebar ul.user-list a.cur:hover, #sidebar ul.user-list a.cur:active  {
	background-color: #163d6c !important;
}
</pre>

<h3>User list subsets</h3>
<p>Sometimes an item in the user list has its own data that should be displayed as a subset of said item.  An example would be the samples contained in a certain project. In this case, the parent link is given <strong>cur</strong> and <strong>parent</strong> classes.</p>
<pre class="html">
&lt;ul class="user-list"&gt;
	&lt;li&gt;&lt;a href="#" class="cur parent" title=""&gt;MyProj&lt;/a&gt;
		&lt;ul&gt;
		&lt;li&gt;&lt;a href="#" class="cur" title="Sample 78-B"&gt;Sample 78-B&lt;/a&gt;&lt;/li&gt;
		&lt;li class="odd"&gt;&lt;a href="#" title="Sample 80-D"&gt;Sample 80-D&lt;/a&gt;&lt;/li&gt;
		&lt;li&gt;&lt;a href="#" title="Sample 90-A"&gt;Sample 90-A&lt;/a&gt;&lt;/li&gt;
		&lt;li class="odd"&gt;&lt;a href="#" title="Sample 92-D"&gt;Sample 92-D&lt;/a&gt;&lt;/li&gt;
		&lt;/ul&gt;
	&lt;/li&gt;
&lt;/ul&gt;
</pre>
<p>The parent class has no background image. The subset list has a border and different default background color.</p>
<pre>
#sidebar ul.user-list a.parent {
	background-image: none !important;
}
#sidebar ul.user-list ul {
	border: 1px solid #7e9dc3;
	border-top: 0;
	background: #e0e7ea;
}
#sidebar ul.user-list ul li.odd {
	background: #d1dce0;
}
</pre>

<h3>Subset links</h3>
<p>The subset links have a left padding and bold font. The currently selected subset link has a different background color than the default currently selected background color.</p>
<pre>
#sidebar ul.user-list ul a, #sidebar ul.user-list ul a:link, #sidebar ul.user-list ul a:visited {
	padding-left: 1em;
	font-weight: bold;
}
#sidebar ul.user-list ul a.cur, #sidebar ul.user-list ul a.cur:link, #sidebar ul.user-list ul a.cur:visited  {
	background-color: #3074a4;
}
#sidebar ul.user-list ul a.cur:hover, #sidebar ul.user-list ul a.cur:active  {
	background-color: #4b91c2 !important;
}
</pre>

<h2>IE6 fixes</h2>
<p>IE6's treatment of the left column elements is extremely wonky, so a bunch of fixes must be implemented.</p>

<h3>Lists</h3>
<p>The text indent must be increased from a negative value to zero on bulleted lists.</p>
<pre>
<span class="ie">* html #sidebar ul.bullet li {
	text-indent: 0;
}</span>
</pre>
<p>To get rid of unwanted whitespace, list items are floated to the left and cleared on both sides.</p>
<pre>
<span class="ie">* html #sidebar ul li {
	float: left;
	clear: both;
	width: 100%;
}</span>
</pre>
<p>Nested lists inherit padding and margin, so they must be zero'd out. The border is handled on the nest list's items instead of the actual list.</p>
<pre>
<span class="ie">* html #sidebar ul.user-list ul {
	padding: 0;
	margin: 0;
	border: 0;
}
* html #sidebar ul.user-list ul li {
	border-left: 1px solid #7e9dc3;
	margin-right: -1px;
}</span>
</pre>
<h3>Links</h3>
<p>To display correctly, the links must have an increased line height and no border.</p>
<pre>
<span class="ie">* html #sidebar ul li a {
	line-height: 1.4em;
	border: 0 !important;
}</span>
</pre>

<h3>Titlebar</h3>
<p>Using ems as units for padding the titlebar in IE6 does not work, so pixels are substituted. Also, it must be floated to get rid of unwanted whitespace.</p>
<pre>
<span class="ie">* html #sidebar div.titlebar {
	float: left;
	clear: both;
	width: 190px;
	padding-left: 5px;
	padding-right: 5px; 
}</span>
</pre>
