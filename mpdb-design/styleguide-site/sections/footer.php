
<h1>Footer</h1>
<p>The styles for the footer. It contains the <a href="/styleguide/?p=footer-nav" title="Footer Nav">footer nav</a> and other information.</p>

<h3>Font size</h3>
<p>The footer's direct children (only paragraphs and divs, though) have a smaller font size. </p>
<pre>
#footer p, #footer div {
	font-size: 0.9em;
} #footer div div {
	font-size: 1em;
}
</pre>

<h3>NSF and such</h3>
<p>The text included at the bottom of the footer is contained in the NSF div. It has a link to the NSF and other information. The layout of this part of the footer may change when the actual footer contents is decided upon. The div has defined height, margin, and bottom padding.</p>
<pre>
#nsf {
	height: 40px;
	margin: 2.5em 1em 1em 200px;
	padding-bottom: 10px;
	text-align: left;
}
</pre>
<p>The NSF logo is a link to their website, and behaves similarly to the <a href="/styleguide/?p=logo" title="Logo">MetPetDB logo link</a>. It is floated to the left to allow the other text to wrap alongside it.</p>
<pre>
#nsf img {
	background: #aaa;
	float: left;
	margin: -5px 0.7em 0 1.5em;
}
#nsf a:hover img {
	background: #000;
}
</pre>