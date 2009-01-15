
<h1>Project Details</h1>
<p>Styles specifically for the project details page.</p>
<p>Preview this page's template: <a href="http://mpdb.zakness.com/project-details.php" title="http://mpdb.zakness.com/project-details.php">http://mpdb.zakness.com/project-details.php</a></p>
<p>Comment on this page on the wiki: <a href="http://trinity.db.cs.rpi.edu/xwiki/bin/view/Website/tptProjectDetails" title="http://trinity.db.cs.rpi.edu/xwiki/bin/view/Website/tptProjectDetails">http://trinity.db.cs.rpi.edu/xwiki/bin/view/Website/tptProjectDetails</a></p>

<h3>Changing table views</h3>
<p>The area on the page that lets you change the data you are viewing uses the <strong>view</strong> class. The font is smaller, a different color, and is aligned to the right.</p>
<pre>
#content p.view {
	font-size: 0.9em;
	margin: 0.8em 0.3em;
	text-align: right;
	color: #555;
}
</pre>
<p>The links are separated with a margin and the current link is bold.</p>
<pre>
#content p.view a {
	margin: 0 0.3em;
}
#content p.view a.cur {
	font-weight: bold;
}
</pre>