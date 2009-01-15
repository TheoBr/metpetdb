
<h1>General Formatting</h1>
<p>These are the default styles used to create a unified design. <strong>Note:</strong> IE-only rules are highlighted in <span class="ie">blue</span>.</p>

<h2>Headings</h2>

<h3>Default heading styles</h3>
<p>Headings' default margins are zero'd out, and their font size and family are defined. A top margin is put back in to create whitespace for readability.</p>
<pre>
h1, h2, h3 {
	margin: 0;
}
h1 {
	font: 2em Helvetica, sans-serif;
	margin-top: 0.7em;
}
h2 {
	font: 1.5em Helvetica, sans-serif;
	margin-top: 0.7em;
}
</pre>

<h2>Lists</h2>

<h3>&lt;ul&gt;</h3>
<p>Unordered lists by default have their margin and padding zero'd out and their list item style removed. This allows for more control.</p>
<pre>
ul, ul li {
	list-style: none;
	margin: 0;
	padding: 0;
}
</pre>

<h3>&lt;ul class="bullet"&gt;</h3>
<p>To put the bulleted behavior back into unordered lists, the <strong>bullet</strong> class is used. The left margin is put back in, the bullet is replaced by a blue arrow image, and the text is moved closer to the blue arrow.</p>
<pre>
ul.bullet {
	margin-left: 2em;
}
ul.bullet li {
	list-style-image: url(/images/li-bluearrow.gif);
	text-indent: -5px;
}
</pre>

<h2>Links</h2>
<p>The default link styles for the content and footer areas are as follows: They have a dotted bottom border and a blue color.</p>
<pre>
#container a, #container a:link,
#footer a, #footer a:link {
	text-decoration: none;
	color: #0c5ea3;
	border-bottom: 1px dotted;
}
#container a:visited, #footer a:visited {
	color: #7f15b0;
}
#container a:hover, #container a:active,
#footer a:hover, #footer a:active {
	color: #318fdf;
}
</pre>
