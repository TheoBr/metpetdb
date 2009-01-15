
<h1>Subsample Analysis</h1>
<p>Styles specifically for the subsample analysis page.</p>
<p>Preview this page's template: <a href="http://mpdb.zakness.com/subsample-analysis.php" title="http://mpdb.zakness.com/subsample-analysis.php">http://mpdb.zakness.com/subsample-analysis.php</a></p>
<p>Comment on this page on the wiki: <a href="http://trinity.db.cs.rpi.edu/xwiki/bin/view/Website/tptSampleAnalysis" title="http://trinity.db.cs.rpi.edu/xwiki/bin/view/Website/tptSampleAnalysis">http://trinity.db.cs.rpi.edu/xwiki/bin/view/Website/tptSampleAnalysis</a></p>
<p><strong>Note:</strong> IE-only rules are highlighted in <span class="ie">blue</span> to designate their inclusion in the separate IE-only stylesheet.</p>

<h3>Subsample table</h3>
<p>The table has a top margin and collapsed border.</p>
<pre>
#content table.subsample {
	margin-top: 1em;
	border-collapse: collapse;
}
</pre>
<p>Cells have a slightly smaller font size, padding, border, and the text is aligned to the center of the cell.</p>
<pre>
#content table.subsample td {
	font-size: 0.95em;
	padding: 0.3em;
	border: 1px dotted #ddd;
	text-align: center;
}
</pre>
<p>Links have no margin, image links have smaller padding, and heading links do not have borders.</p>
<pre>
#content table.subsample td a {
	margin: 0;
}
#content table.subsample td a.ssimg img {
	padding: 2px;
}
#content table.subsample tr.heading a {
	border: 0 !important;	
}
</pre>