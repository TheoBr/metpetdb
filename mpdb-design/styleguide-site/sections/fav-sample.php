
<h1>Favorite Sample</h1>
<p>The favorite sample icon is a small heart icon that delineates whether a sample is marked as the user's favorite or not. Clicking on the icon should add/remove the sample to/from the user's favorites. A good example of this is on the <a href="/mysamples.php" title="My Samples template">My Samples</a> template.</p>

<p>The icon is actually the background image of a span that contains descriptive text.</p>
<pre class="html">
&lt;span class="fav-sample-no"&gt;Add to favorite samples.&lt;/span&gt; 
&lt;span class="fav-sample-yes"&gt;Remove from favorite samples.&lt;/span&gt;
</pre>
<p>The text is hidden from CSS-enabled browsers with a negative text-indent. In order to do this it must be given a set width and height, be block-level, and be floated.</p>
<pre>
span.fav-sample-no,
span.fav-sample-yes {
	width: 14px;
	height: 12px;
	text-indent: -5000px;
	float: left;
	display: block;
	margin-top: 2px;
	background-color: transparent;
	background-repeat: no-repeat;
	background-position: left 50%;
	cursor: pointer;
}
span.fav-sample-no {background-image: url(/images/fav-no.png);}
span.fav-sample-yes {background-image: url(/images/fav-yes.png);}
</pre>
