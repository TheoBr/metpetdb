
<h1>Special Links</h1>
<p>These are links that are styled differently than the default ones defined in the <? $n->showLink('links');?> page.</p>

<h3>Action links</h3>
<p>For links that add content, a plus icon is placed in the background. A good example of this can be found in the <a href="/myprojects.php" title="My Projects template">My Projects</a> template. Note: the 'add' icon is not the same as the 'expand' icon.</p>
<pre class="html">
&lt;a href="#30" title="Create a new project." class="addlink"&gt;Create New Project&lt;/a&gt;
</pre>
<pre>
.addlink {
	padding-left: 15px;
	background-image: url(/images/add.gif);
	background-repeat: no-repeat;
	background-position: left center;
}
</pre>
