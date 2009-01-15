
<h1>Main Navigation</h1>
<p>The styles for the main navigation in the header.</p>

<h3>The nav list</h3>
<p>The main nav is an unordered list with a green background color, padding, and a border for style. The left border is large so as to vertically align the nav with the content column.</p>
<pre class="html">
&lt;ul id="nav"&gt;
	&lt;li&gt;&lt;a href="#" title="Search"&gt;Search&lt;/a&gt;&lt;/li&gt;
	&lt;li&gt;&lt;a href="#" title="News"&gt;News&lt;/a&gt;&lt;/li&gt;
	&lt;li&gt;&lt;a href="#" title="About"&gt;About&lt;/a&gt;&lt;/li&gt;
	&lt;li&gt;&lt;a href="#" title="People"&gt;People&lt;/a&gt;&lt;/li&gt;
	&lt;li&gt;&lt;a href="#" title="FAQ"&gt;FAQ&lt;/a&gt;&lt;/li&gt;
&lt;/ul&gt;
</pre>
<pre>
ul#nav {
	background: #F0FDC1;
	padding: 1em 1em 1em 0;
	border-top: 1px solid #DAEE9B;
	border-left: 201px solid #e0edb7;
}
</pre>
<p>The list items are changed to inline display and given padding.</p>
<pre>
ul#nav li {
	display: inline;
	padding: 1.5em 0.5em 1.5em 1.4em;
}
</pre>

<h3>Nav links</h3>
<p>The links have a dark green color and dotted bottom border that changes color on hover.</p>
<pre>
ul#nav li a, ul#nav li a:link, ul#nav li a:visited {
	text-decoration: none;
	color: #936b3b;
	border-bottom: 1px dotted #86A02D;
}
ul#nav li a:hover, ul#nav li a:active {
	color: #000;
	border-bottom: 1px dotted #728a21;
}
</pre>

<h3>Current page link</h3>
<p>The current page link is styled differently using the <strong>cur</strong> class.</p>
<pre class="html">
&lt;li&gt;&lt;a class="cur" href="#" title="Search"&gt;Search&lt;/a&gt;&lt;/li&gt;
</pre> 
<p>The current page link is black, bold, and has a thicker bottom border. In this case it would be the Search link.</p>
<pre>
ul#nav li a.cur {
	color: #000;
	border-bottom: 2px solid;
	font-weight: bold;
}
</pre>

