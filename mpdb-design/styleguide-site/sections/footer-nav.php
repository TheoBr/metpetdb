
<h1>Footer Navigation</h1>
<p>The styles for the navigation in the footer.</p>


<p>The footer nav behaves the exact same way as the <a href="/styleguide/?p=main-nav" title="Main Nav">Main Nav</a>, except the colors are different.</p>
<pre class="html">
&lt;ul id="fnav"&gt;
	&lt;li&gt;&lt;a href="#" title="Projects"&gt;Projects&lt;/a&gt;&lt;/li&gt;
	&lt;li&gt;&lt;a href="#" title="Search"&gt;Search&lt;/a&gt;&lt;/li&gt;
	&lt;li&gt;&lt;a href="#" title="News"&gt;News&lt;/a&gt;&lt;/li&gt;
	&lt;li&gt;&lt;a href="#" title="About"&gt;About&lt;/a&gt;&lt;/li&gt;
	&lt;li&gt;&lt;a href="#" title="People"&gt;People&lt;/a&gt;&lt;/li&gt;
	&lt;li&gt;&lt;a href="#" title="FAQ"&gt;FAQ&lt;/a&gt;&lt;/li&gt;
&lt;/ul&gt;
</pre>
<pre>
ul#fnav {
	padding: 1em 1em 1em 0;
	border-bottom: 1px solid #e7ecde;
	border-left: 201px solid #e7ecde;
	background: #f6f6f6;
}
ul#fnav li {
	display: inline;
	padding: 1.5em 0.5em 1.5em 1.4em;
}
ul#fnav li a, ul#fnav li a:link, ul#fnav li a:visited {
	text-decoration: none;
	color: #766a5c;
	border-bottom: 1px dotted #86A02D;
}
ul#fnav li a:hover, ul#fnav li a:active {
	color: #000;
	border-bottom: 1px dotted #728a21;
}
ul#fnav li a.cur {
	color: #000;
	border-bottom: 2px solid;
	font-weight: bold;
}
</pre>

