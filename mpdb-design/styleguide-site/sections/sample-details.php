
<h1>Sample Details</h1>
<p>Styles specifically for the sample details page.</p>
<p>Preview this page's template: <a href="http://mpdb.zakness.com/sampledetails-78-B-subsamples.php" title="http://mpdb.zakness.com/sampledetails-78-B-subsamples.php">http://mpdb.zakness.com/sampledetails-78-B-subsamples.php</a></p>
<p>Comment on this page on the wiki: <a href="http://trinity.db.cs.rpi.edu/xwiki/bin/view/Website/tptSampleDetails" title="http://trinity.db.cs.rpi.edu/xwiki/bin/view/Website/tptSampleDetails">http://trinity.db.cs.rpi.edu/xwiki/bin/view/Website/tptSampleDetails</a></p>
<p><strong>Note:</strong> IE-only rules are highlighted in <span class="ie">blue</span> to designate their inclusion in the separate IE-only stylesheet.</p>


<h3>Subsamples list</h3>
<p>The subsamples are each in a table inside a div with the id of <strong>ssamples</strong> and classes <strong>section</strong> and <strong>clearfix</strong> (explained in <? $n->showLink('multi-purpose');?>). Each table consists of thumbnail links with the <strong>ssimg</strong> and <strong>nobdr</strong> classes to subsample analysis pages. The subsample number, textual link, and link to the subsample map are in a list next to the thumbnail link.</p>
<pre class="html">
&lt;div id="ssamples" class="section clearfix"&gt;
&lt;table cellspacing="0"&gt;
&lt;tr&gt;
	&lt;td&gt;&lt;a href="/subsample-analysis.php" title="" class="ssimg nobdr"&gt;&lt;img src="/images/78-B-ss01.png" alt="" /&gt;&lt;/a&gt;&lt;/td&gt;
	&lt;td&gt;
	&lt;ul&gt;
		&lt;li&gt;&lt;b&gt;SS01&lt;/b&gt;&lt;/li&gt;
		&lt;li&gt;&lt;a href="/subsample-map.php" title="SS01 Map"&gt;Map&lt;/a&gt;&lt;/li&gt;
		&lt;li&gt;&lt;a href="/subsample-analysis.php" title="SS01 Mineral Analysis"&gt;Spot Analysis&lt;/a&gt;&lt;/li&gt;
	&lt;/ul&gt;
	&lt;/td&gt;
&lt;/tr&gt;
&lt;/table&gt;
&lt;table cellspacing="0"&gt;
&lt;tr&gt;
	&lt;td&gt;&lt;a href="#51" title="" class="ssimg nobdr"&gt;&lt;img src="/images/78-B-ss02.png" alt="" /&gt;&lt;/a&gt;&lt;/td&gt;
	&lt;td&gt;
	&lt;ul&gt;
		&lt;li&gt;&lt;b&gt;SS02&lt;/b&gt;&lt;/li&gt;
		&lt;li&gt;&lt;a href="/subsample-map.php" title="SS01 Map"&gt;Map&lt;/a&gt;&lt;/li&gt;
		&lt;li&gt;&lt;a href="/subsample-analysis.php" title="SS01 Mineral Analysis"&gt;Spot Analysis&lt;/a&gt;&lt;/li&gt;
	&lt;/ul&gt;
	&lt;/td&gt;
&lt;/tr&gt;
&lt;/table&gt;
&lt;/div&gt;
</pre>
<p>The tables are floated to the left, given a left margin, and an inherited with (so they dont take up 100%).</p>
<pre>
#ssamples table {
	float: left;
	margin-right: 1em;
	width: inherit;
}
</pre>
<p>The <code>td</code>s align their contents to the top and remove the default bottom border.</p>
<pre>
#ssamples table td {
	vertical-align: top;
	border-bottom: none;
}
</pre>

<h3>Subsample thumbnails</h3>
<p>The thumbnails have a padding, background color, and border that changes on hover. In IE6 the link's border must be removed.</p>
<pre>
a.ssimg img, a:link.ssimg img {
	padding: 5px;
	background: #f9f9f9;
	border: 1px solid #f0f0f0 !important;
}
a:visited.ssimg img {
	background: #f2e8f6;
	border: 1px solid #e8d9ee !important;
}
a:hover.ssimg img, a:active.ssimg img {
	background: #cfd6ef;
	border: 1px solid #bdc6e5 !important;
}
<span class="ie">* html a.ssimg, * html a:link.ssimg, * html a:visited.ssimg,
* html a:hover.ssimg, * html a:active.ssimg {
	border: 0 !important;
}</span>
</pre>
