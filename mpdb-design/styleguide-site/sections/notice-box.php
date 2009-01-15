
<h1>Notice Box</h1>
<p>Styles specifically for notice boxes that generally appear at the top of a page, displaying a notification. Notifications include success, error, and general notice messages.</p>
<p>Preview this element in a template: <a href="http://mpdb.zakness.com/myprojects.php" title="http://mpdb.zakness.com/myprojects.php">http://mpdb.zakness.com/myprojects.php</a></p>

<h3>Notice message</h3>
<p>A notice message is something the user should be alerted of, but does not fall under success or failure classifications. Notice messages have a heading and a message (which may be a combination of elements such as an <? $n->showLink('lists','options list');?>).</p>
<pre class="html">
&lt;div class="notice-message"&gt;
	&lt;h3&gt;You have 2 new project invitations:&lt;/h3&gt;
	&lt;div class="subsection"&gt;Prof. Adali has invited you to join the project &lt;a href="/projectdetails-awesome.php"&gt;&lt;b&gt;Awesome Project&lt;/b&gt;&lt;/a&gt;.
	&lt;ul class="options"&gt;
		&lt;li&gt;&lt;label for="yes"&gt;&lt;input type="radio" name="invite" id="yes" /&gt; Accept&lt;/label&gt;&lt;/li&gt;
		&lt;li&gt;&lt;label for="no"&gt;&lt;input type="radio" name="invite" id="no" /&gt; Reject&lt;/label&gt;&lt;/li&gt;
		&lt;li&gt;&lt;button type="submit" class="submit"&gt;Confirm&lt;/button&gt;&lt;/li&gt;
	&lt;/ul&gt;	
	&lt;/div&gt;
	&lt;div class="subsection"&gt;Zak L has invited you to join the project &lt;a href="/projectdetails-another.php"&gt;&lt;b&gt;Another One&lt;/b&gt;&lt;/a&gt;.
	&lt;ul class="options"&gt;
		&lt;li&gt;&lt;label for="yes"&gt;&lt;input type="radio" name="invite" id="yes" /&gt; Accept&lt;/label&gt;&lt;/li&gt;
		&lt;li&gt;&lt;label for="no"&gt;&lt;input type="radio" name="invite" id="no" /&gt; Reject&lt;/label&gt;&lt;/li&gt;
		&lt;li&gt;&lt;button type="submit" class="submit"&gt;Confirm&lt;/button&gt;&lt;/li&gt;
	&lt;/ul&gt;	
	&lt;/div&gt;
&lt;/div&gt;
</pre>

<p>It has a light purple background and border, margins and padding, and has an icon in the upper left. Headings are given a bottom margin for readability.</p>
<pre>
#content div.notice-message {
	margin: 1em 1.5em;
	padding: 0.5em 0.5em 0.5em 2.5em;
	background: #f4ecf7 url(/images/iconWarning.gif) no-repeat 0.5em 0.7em;
	border: 1px solid #ddd4e0;
}
#content div.notice-message h3 {
	margin-bottom: 0.5em;
}
</pre>
<p>Some notices (like the 'New projectinvitation' notice) have a list of options.  The default <strong>ul.options</strong> margins are too much, so they are lessened.</p>
<pre>
#content div.notice-message ul.options {
	margin: 0.2em 0;
}
</pre>
