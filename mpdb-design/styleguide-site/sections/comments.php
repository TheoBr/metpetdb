
<h1>Comments</h1>
<p>The styles for the commenting section in the content area.</p>
<p><strong>Note:</strong> IE-only rules are highlighted in <span class="ie">blue</span> to designate their inclusion in the separate IE-only stylesheet.</p>

<h3>Comments area</h3>
<p>The comments area is contained in a div. It clears floated elements and has a top margin. IE needs this div to have a bottom margin as well.</p>
<pre>
#comments {
	clear: both;
	margin-top: 2em;
}
<span class="ie">#comments {
	margin-bottom: 2em;
}</span>
</pre>
