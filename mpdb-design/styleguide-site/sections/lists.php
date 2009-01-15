
<h1>Lists</h1>
<p>Lists are used for regular lists of items and for navigation.</p>

<h3>Unordered lists</h3>
<p>Unordered lists by default have their margin and padding zero'd out and their list item style removed. This allows for more control.</p>
<pre>
ul, ul li {
	list-style: none;
	margin: 0;
	padding: 0;
}
</pre>

<h3>Bulleted lists</h3>
<p>To put the bulleted behavior back into unordered lists, the <strong>bullet</strong> class is used.</p>
<pre class="html">
&lt;ul class="bullet"&gt;
	&lt;li&gt;item 1&lt;/li&gt;
	&lt;li&gt;item 2&lt;/li&gt;
	&lt;li&gt;item 3&lt;/li&gt;
&lt;/ul&gt;
</pre>
<p>The left margin is put back in, the bullet is replaced by a blue arrow image, and the text is moved closer to the blue arrow.</p>
<pre>
ul.bullet {
	margin-left: 2em;
}
ul.bullet li {
	list-style-image: url(/images/li-bluearrow.gif);
	text-indent: -5px;
}
</pre>

<h3>Ordered lists</h3>
<p>Ordered lists have blue number markers for style. To achieve this effect each list item's contents must be contained in a span tag.</p>
<pre class="html">
&lt;ol&gt;
	&lt;li&gt;&lt;span&gt;item 1&lt;/span&gt;&lt;/li&gt;
	&lt;li&gt;&lt;span&gt;item 2&lt;/span&gt;&lt;/li&gt;
	&lt;li&gt;&lt;span&gt;item 3&lt;/span&gt;&lt;/li&gt;
&lt;/ol&gt;
</pre>
<p>They have a left margin and their list items have a bottom margin and increased line height for readability. The list item has a default blue color, and the spans in each list item have a black color.</p>
<pre>
#content ol {
	margin-left: 2em;
}
#content ol li {
	line-height: 1em;
	margin-bottom: 1em;
	color: #39c;
}
#content ol li span {
	color: #000;
}
</pre>

<h3>Options list</h3>
<p>The options list is an inline list of options for a certain page, usually located right under the content heading. A good example of this is on the <a href="/sampledetails-78-B.php" title="Sample Details">Sample Details</a> template.</p>
<pre class="html">
&lt;ul class="options"&gt;
	&lt;li&gt;&lt;a href="#" title="option 1"&gt;option 1&lt;/a&gt;&lt;/li&gt;
	&lt;li&gt;&lt;a href="#" title="option 2"&gt;option 2&lt;/a&gt;&lt;/li&gt;
	&lt;li&gt;&lt;a href="#" title="option 3"&gt;option 3&lt;/a&gt;&lt;/li&gt;
&lt;/ul&gt;
</pre>
<p>The list has top and bottom margins for readability. The list items are displayed inline and have right margins separating them.</p>
<pre>
#content ul.options {
	margin: 0.7em 0;
}
#content ul.options li {
	display: inline;
	margin-right: 1.5em;
	line-height: 1em;
}
</pre>