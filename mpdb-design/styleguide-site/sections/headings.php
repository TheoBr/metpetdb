
<h1>Headings</h1>
<p>Heading styles.</p>
<p><strong>Note:</strong> IE-only rules are highlighted in <span class="ie">blue</span> to designate their inclusion in the separate IE-only stylesheet.</p>

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

<h3>Headings in &lt;div id="header"&gt;</h3>
<p>The header h1 has a slightly smallering top margin for style purposes. It contains the logo/homepage link. The header h2 contains the short description of MetPetDB, and has smaller top and bottom margins and font size, a color, and a left margin to line up with the logo.</p>
<pre>
#header h1 {
	margin-top: 0.5em;
}
#header h2 {
	margin: 0.5em 0;
	color: #787e66;
	font-size: 1em;
	margin-left: 1em;
}
</pre>