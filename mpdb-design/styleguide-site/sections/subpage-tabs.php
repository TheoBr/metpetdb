
<h1>Subpage Tabs</h1>
<p>Subpage tabs are <? $n->showLink('dialog-box');?>-style tabs placed in the content area of a page. Their purpose is to link the user to relevant subpages. A good example of this is on the <a href="/mysamples.php" title="My Samples template">My Samples</a> template.</p>

<p>The links are in a table (with 100% width) absolutely positioned from the top right corner of the content area. The first <code>td</code> should be empty and have the <strong>nobg</strong> class. Note: the crude javascript should be replaced with something better when this widget is implemented.</p>
<pre class="html">
&lt;table width="100%" class="subpages" cellspacing="0"&gt;
	&lt;tr&gt;
	&lt;td width="100%" class="nobg"&gt;&lt;/td&gt;
	&lt;td&gt;&lt;div class="current" onclick="javascript:window.location.href='http://mpdb.zakness.com/mysamples.php';"&gt;All&lt;/div&gt;&lt;/td&gt;
	&lt;td&gt;&lt;div onclick="javascript:window.location.href='http://mpdb.zakness.com/mysamples-new.php';"&gt;Newest&lt;/div&gt;&lt;/td&gt;
	&lt;td&gt;&lt;div onclick="javascript:window.location.href='http://mpdb.zakness.com/mysamples-fav.php';"&gt;Favorites&lt;/div&gt;&lt;/td&gt;
	&lt;/tr&gt;
&lt;/table&gt;
</pre> 
<pre>
#content .subpages {
	position: absolute;
	right: 1.5em;
	top: 3.5em;
}
</pre>

<p>The <code>td</code>s behave similiarly to the <? $n->showLink('dialog-box');?> <code>td</code>s except they have the background image applied directl to them (instead of the <code>tr</code>).</p>
<pre>
#content .subpages td {
	background: transparent url(/images/tabbar-bg.gif) repeat-x left bottom;
	vertical-align: bottom;
	border-bottom: none;
	line-height: 1;
	padding: 0 0 0 0.4em;
}
</pre>
<p>The clickable areas (<code>div</code>s) are styled the same way as the dialog box <code>div</code>s.</p>
<pre>
#content .subpages td div {
	margin: 0 0.2em;
	border: 1px solid #afcf73;
	background: #f5f7f1;
	cursor: pointer;
	padding: 0.4em;
}
#content .subpages td div.current {
	border-bottom-color: #fff;
	font-weight: bold;
	background: #fff;
}
</pre>