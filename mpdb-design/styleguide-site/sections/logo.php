
<h1>Header Logo</h1>
<p>The styles for the MetPetDB logo in the header.</p>


<p>The logo is actually a white PNG with a transparent cut-out of the MetPetDB logo. It is linked to the index page to give the user a quick and easy way to get back home.</p>
<pre class="html">
&lt;h1&gt;&lt;a href="/" title="Home"&gt;&lt;img src="/images/logo-mask.png" alt="MetPetDB" /&gt;&lt;/a&gt;&lt;/h1&gt;
</pre>
<p>The logo link has a set height and width, so it must be block display. It has a default font color of white so the alt text does not show if the image hasn't loaded yet. It has a background color that changes on hover, changing the logo color.</p>
<pre>
#header h1 a, #header h1 a:link, #header h1 a:visited {
	text-decoration: none;
	display: block;
	margin-left: 0.7em;
	height: 50px;
	width: 179px;
	color: #fff;
	background: #0c5ea3;
}
#header h1 a:hover, #header h1 a:active {
	background: #083291;
}
</pre>

