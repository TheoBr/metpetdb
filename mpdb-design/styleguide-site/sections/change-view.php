
<h1>Change View Options</h1>
<p>The change view options appear usually above a table, and offer a way to change which data the table is displaying. A good example of this is on the <a href="/myprojects.php" title="My Projects template">My Projects</a> template.</p>

<p>The options are contained in a paragraph tag, and have a smaller font size, light grey color, and are aligned to the left. A "Quick Filters" section can be added in a <code>span</code> tag and floated to the left with <strong>l</strong> (<? $n->showLink('multi-purpose');?>).</p>
<pre class="html">
&lt;p class="view"&gt;
	&lt;span class="l"&gt;Quick Filters: &lt;a href="#25" title=""&gt;Recent Changes&lt;/a&gt;&lt;/span&gt;
	Change View: &lt;a href="#26" title="" class="cur"&gt;Simple&lt;/a&gt; &lt;a href="#27" title=""&gt;Detailed&lt;/a&gt; | &lt;a href="#28" title=""&gt;Create New View&lt;/a&gt;
&lt;/p&gt;
</pre>
<pre>
p.view {
	font-size: 0.9em;
	margin: 0.8em 0.3em;
	text-align: right;
	color: #555;
}
p.view a {
	margin: 0 0.3em;
}
</pre>
<p>The currently selected option is given the <strong>cur</strong> class which bolds it.</p>
<pre>
p.view a.cur {
	font-weight: bold;
}
</pre>
