
<h1>Tables</h1>
<p>The general styles for tables displaying tabular data.</p>
<p><strong>Note:</strong> IE-only rules are highlighted in <span class="ie">blue</span> to designate their inclusion in the separate IE-only stylesheet.</p>

<h3>The table</h3>
<p>Tables are contained in an extra div to display correctly in IE (otherwise horizontal scrolling occurs). They also have to have their cellspacing attribute zero'd out. The heading row is given the <strong>heading</strong> class and contains table headings (<code>&lt;th&gt;</code>). The links in the heading row can be clicked to sort the table by that column. The column that the table is being sorted by is given the <strong>orderby</strong> class. Finally, alternate rows are given the <strong>odd</strong> class.</p>
<pre class="html">
&lt;div&gt; &lt;!-- extra div fixes IE table margins :/ --&gt;
&lt;table cellspacing="0"&gt;
	&lt;tr class="heading"&gt;
		&lt;th class="orderby"&gt;&lt;a href="#" title="Order by Sample"&gt;Sample&lt;/a&gt;&lt;/th&gt;
		&lt;th&gt;&lt;a href="#" title="Order by Mineral Assemblage"&gt;Mineral Assemblage&lt;/a&gt;&lt;/th&gt;
		&lt;th&gt;&lt;a href="#" title="Order by Region"&gt;Region&lt;/a&gt;&lt;/th&gt;
	&lt;/tr&gt;
	&lt;tr&gt;
		&lt;td class="orderby"&gt;&lt;a href="#" title="78-B"&gt;78-B&lt;/a&gt;&lt;/td&gt;
		&lt;td&gt;Garnet, Staurolite, Biotite, Muscovite, Plagioclase&lt;/td&gt;
		&lt;td class="na"&gt;Unknown&lt;/td&gt;
	&lt;/tr&gt;
	&lt;tr class="odd"&gt;
		&lt;td class="orderby"&gt;&lt;a href="#" title="80-D"&gt;80-D&lt;/a&gt;&lt;/td&gt;
		&lt;td&gt;Garnet, Biotite, Muscovite, Plagioclase&lt;/td&gt;
		&lt;td&gt;Mt. Moosilauke, NH &lt;a href="#2" title="Map"&gt;Map&lt;/a&gt;&lt;/td&gt;
	&lt;/tr&gt;
&lt;/table&gt;
&lt;/div&gt;
</pre>
<p>Default-styled tables take up the entire width of the content column and have no border. In IE they need left and right padding.</p>
<pre>
#content table {
	width: 100%;
	border: 0;
}
<span class="ie">table {
	padding-left: 1.5em;
	padding-right: 1.5em;
}</span>
</pre>

<h3>Rows and cells</h3>
<p>Alternate rows have different background colors.</p>
<pre>
#content tr.odd {
	background: #f9f9f9;
}
</pre>
<p>Cells have padding and bottom border. The font color is a dark grey.</p>
<pre>
#content td {
	vertical-align: middle;
	padding: 0.3em 0.5em;
	border-bottom: 1px dotted #ddd;
	color: #333;
}
</pre>
<p>Cells in the column that the table is being sorted by have the <strong>orderby</strong> class which bolds them, increases their font size, and makes the font color black.</p>
<pre>
#content td.orderby {
	font-size: 1em;
	font-weight: bold;
	color: #000;
}
</pre>
<p>Cells that contain data that is unavailable or not defined have the <strong>na</strong> class which makes their font color a light brownish red.</p>
<pre>
#content td.na {
	color: #d18d75;
}
</pre>

<h3>Table headings</h3>
<p>Table headings are found in the first row of a row-display table, or are the first cell of each row in a column-display table. They are styled similarly to regular data cells, except they have a different background, border, and font color, and the text is bold.</p>
<pre>
#content th {
	background: #faf4df;
	font-weight: bold;
	color: #64230f;
	text-align: left;
	vertical-align: middle;
	padding: 0.3em 0.5em;
	border-bottom: 1px dotted #e2c798;
}
</pre>
<p>The heading cells have a different bottom border for style.</p>
<pre>
#content tr.heading td {
	border-bottom: 1px dotted #e2c798;
}
</pre>
<p>To further designate the orderby column, the heading for the column has a different background color.</p>
<pre>
#content tr.heading td.orderby {
	background: #feddbf;
}
</pre>

<h3>Tables with actionable rows</h3>
<p>Some tables will have checkboxes for each row entry, and an action select box at the bottom that allows certain actions to be performed on the selected rows. This bottom row is given the <strong>action-selected</strong> class and also contains a checkbox to select all rows.</p>
<pre class="html">
&lt;tr class="heading"&gt;
	&lt;td&gt;&lt;/td&gt;
	&lt;th class="orderby"&gt;&lt;a href="#" title="Order by Sample"&gt;Sample&lt;/a&gt;&lt;/th&gt;
	&lt;th&gt;&lt;a href="#" title="Order by Mineral Assemblage"&gt;Mineral Assemblage&lt;/a&gt;&lt;/th&gt;
	&lt;th&gt;&lt;a href="#" title="Order by Region"&gt;Region&lt;/a&gt;&lt;/th&gt;
&lt;/tr&gt;
&lt;tr&gt;
	&lt;td&gt;&lt;input type="checkbox" /&gt;&lt;/td&gt;
	&lt;td class="orderby"&gt;&lt;a href="#" title="78-B"&gt;78-B&lt;/a&gt;&lt;/td&gt;
	&lt;td&gt;Garnet, Staurolite, Biotite, Muscovite, Plagioclase&lt;/td&gt;
	&lt;td class="na"&gt;Unknown&lt;/td&gt;
&lt;/tr&gt;
&lt;tr class="odd"&gt;
	&lt;td&gt;&lt;input type="checkbox" /&gt;&lt;/td&gt;
	&lt;td class="orderby"&gt;&lt;a href="#" title="80-D"&gt;80-D&lt;/a&gt;&lt;/td&gt;
	&lt;td&gt;Garnet, Biotite, Muscovite, Plagioclase&lt;/td&gt;
	&lt;td&gt;Mt. Moosilauke, NH &lt;a href="#2" title="Map"&gt;Map&lt;/a&gt;&lt;/td&gt;
&lt;/tr&gt;
&lt;tr class="action-selected"&gt;
	&lt;td colspan="4"&gt;
		&lt;label for="selectall"&gt;&lt;input type="checkbox" id="selectall" /&gt; Select All&lt;/label&gt;
		&lt;select&gt;
			&lt;option&gt;Remove&lt;/option&gt;
			&lt;option&gt;Add to 'My Second Project'&lt;/option&gt;
			&lt;option&gt;Add to 'Research group1'&lt;/option&gt;
		&lt;/select&gt;
		&lt;button class="submit" type="submit"&gt;Apply to Selected&lt;/button&gt;
	&lt;/td&gt;
&lt;/tr&gt;
</pre>
<p>The action row has a blue-grey background color, and the select box has a left margin to add whitespace between it and the select all label.</p>
<pre>
#content tr.action-selected td {
	background-color: #e3eaf3;
}
#content tr.action-selected td select {
	margin-left: 1em;
}
</pre>