
<h1>Search</h1>
<p>Styles specifically for the search page.</p>
<p>Preview this page's template: <a href="http://mpdb.zakness.com/search.php" title="http://mpdb.zakness.com/search.php">http://mpdb.zakness.com/search.php</a> <a href="http://mpdb.zakness.com/search-collapsed.php" title="http://mpdb.zakness.com/search-collapsed.php">http://mpdb.zakness.com/search-collapsed.php</a></p>
<p>Comment on this page on the wiki: <a href="http://trinity.db.cs.rpi.edu/xwiki/bin/view/Website/tptSearch" title="http://trinity.db.cs.rpi.edu/xwiki/bin/view/Website/tptSearch">http://trinity.db.cs.rpi.edu/xwiki/bin/view/Website/tptSearch</a></p>
<p><strong>Note:</strong> IE-only rules are highlighted in <span class="ie">blue</span> to designate their inclusion in the separate IE-only stylesheet.</p>

<h3>Search options fieldset</h3>
<p>The nested lists in the search options fieldset have a left margin to separate them. The list items have a left border, left padding, and right margin to separate them. In IE the border is solid to match the fieldset border (discusses on the <a href="/styleguide/?p=forms" title="Forms">Forms</a> page).</p>
<pre>
#s-options ul ul {
	margin-left: 1em;
}
#s-options li.l {
	border-left: 1px dotted #759140;
	margin-right: 1em;
	padding-left: 1em;
}
<span class="ie">#s-options li.l {
	border-left: 1px solid #afcf73;
}</span>
</pre>
<p>The b tag in the location fieldset is used as a header, so it is block-level and has a bottom margin.</p>
<pre>
#s-location b {
	margin-bottom: 0.7em;
	display: block;
}
</pre>

<h3>IE fixes</h3>
<p>Links' borders are removed in the location fieldset.</p>
<pre>
<span class="ie">#s-location a {
	border: 0 !important;
}</span>
</pre>
<p>The map image needs a left margin.</p>
<pre>
<span class="ie">#s-location img {
	display: inline;
	margin-left: 2em;
}</span>
</pre>
<p>The list in the submit fieldset's bottom margin must be zero'd out.</p>
<pre>
<span class="ie">#s-submit ol {
	margin-bottom: 0;
}</span>
</pre>
