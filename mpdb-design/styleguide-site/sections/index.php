
<h1>Index</h1>
<p>Styles specifically for the index page.</p>
<p>Preview this page's template: <a href="http://mpdb.zakness.com" title="http://mpdb.zakness.com">http://mpdb.zakness.com</a></p>
<p>Comment on this page on the wiki: <a href="http://trinity.db.cs.rpi.edu/xwiki/bin/view/Website/tptIndex" title="http://trinity.db.cs.rpi.edu/xwiki/bin/view/Website/tptIndex">http://trinity.db.cs.rpi.edu/xwiki/bin/view/Website/tptIndex</a></p>

<h3>Other database websites list</h3>
<p>The homepage uses the right column to display a list of other useful online databases. The list has a top and bottom margin, and its items are centered and have a bottom margin.</p>
<pre>
#dblist {margin: 1em 0;}
#dblist li {
	text-align: center;
	margin-bottom: 2em;
}
</pre>
<p>The links do not have the default bottom border, and are changed to a block-level display.</p>
<pre>
#dblist a {
	border: 0 !important;
	display: block;
} 
</pre>
<p>The images have a border which changes color on hover.</p>
<pre>
#dblist a img {
	border: 1px solid #aaa !important;
	margin-bottom: 0.3em;
}
#dblist a:hover img {
	border: 1px solid #000 !important;
}
</pre>