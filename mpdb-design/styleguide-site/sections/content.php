
<h1>Content Area</h1>
<p>The styles for the main content area.</p>
<p><strong>Note:</strong> IE-only rules are highlighted in <span class="ie">blue</span> to designate their inclusion in the separate IE-only stylesheet.</p>

<h3>Breadcrumbs</h3>
<p>The breadcrumbs div should be present on every page because it provides a clear representation of where the user is in the site. The breadcrumb links always start with a link to the home page, and each element is separated by the <strong>&amp;gt;</strong> special character. The last link (the current page) is inside a span tag.</p>
<pre class="html">
&lt;div id="breadcrumbs"&gt;
You are here: &lt;a href="/" title="Home"&gt;Home&lt;/a&gt; &amp;gt; &lt;a href="#" title="Projects"&gt;Projects&lt;/a&gt; &amp;gt; &lt;a href="#" title="MyProj"&gt;MyProj&lt;/a&gt; &amp;gt; &lt;span&gt;Sample 78-B&lt;/span&gt;
&lt;/div&gt;
</pre>
<p>The breadcrumbs div has a background color, small font, padding, font color, and bottom border. The padding and font size should be the same as left column titlebars so they line up nicely (titlebars covered in the <a href="/styleguide/?p=sidebar" title="Left Column">Left Column</a> page). The span tag should only be used to represent the current page, and has a black font color.</p>
<pre>
#breadcrumbs {
	background: #f7fbff;
	font: 0.8em Verdana, sans-serif;
	padding: 0.8em 0.5em 0.5em;
	color: #749490;
	border-bottom: 1px solid #e6eef1;
}
#breadcrumbs span {
	color: #000;
}
</pre>