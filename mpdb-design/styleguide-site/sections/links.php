
<h1>Links</h1>
<p>The general styles for links.</p>
<p><strong>Note:</strong> IE-only rules are highlighted in <span class="ie">blue</span> to designate their inclusion in the separate IE-only stylesheet.</p>

<p>The default style for links in the content columns and footer is to be blue and have a dotted bottom border. Visited links are purple, and links' color changes to a light blue on hover. Visited links in the left column do not change color, because they are not normal links. IE6 does not support dotted borders, so they are made solid instead. Having unified link styles throughout the page makes them more recognizable, and thus makes navigating the site easier for the user.</p>
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
#sidebar a:visited {
	color: #0c5ea3;
}
#container a:hover, #container a:active,
#footer a:hover, #footer a:active {
	color: #318fdf;
}
<span class="ie">* html a, * html a:link, * html a:visited, * html a:hover, * html a:active {
	border-bottom: 1px solid !important;
}</span>
</pre>