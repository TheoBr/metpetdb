
<h1>Header Logbar</h1>
<p>The styles for the MetPetDB logbar in the header.</p>

<h3>HTML content</h3>
<p>The logbar displays information pertaining to the user's logged in status. If they are not logged it, it contains links to login or register. If they are logged in it displays their user name (in a strong tag) and links to their control panel and other user pages. The actual information is contained within a span tag in the logbar div.</p>
<pre class="html twobox l">
<strong>Not logged in</strong>

&lt;div id="logbar"&gt;
&lt;span&gt;&lt;a href="#" title="Login"&gt;Login&lt;/a&gt; &lt;a href="#" title="Register"&gt;Register&lt;/a&gt;&lt;/span&gt;
&lt;/div&gt;



</pre>
<pre class="html twobox r">
<strong>Logged in</strong>

&lt;div id="logbar"&gt;
&lt;span&gt;Logged in as &lt;strong&gt;MyUserName&lt;/strong&gt; | &lt;a href="#" title="My Control Panel"&gt;My Control Panel&lt;/a&gt; &lt;a href="#" title="Edit My Profile"&gt;Edit My Profile&lt;/a&gt; &lt;a href="#" title="Logout"&gt;Logout&lt;/a&gt;&lt;/span&gt;
&lt;/div&gt;
</pre>

<h3>&lt;div id="logbar"&gt;</h3>
<p>The logbar div has a dark background, bottom border, and a set height. It is positioned relatively so that the span can be positioned relative to it.</p>
<pre>
#logbar {
	background: #1f2326;
	height: 0.7em;
	position: relative;
	border-bottom: 1px solid #000;
}
</pre>

<h3>Logbar span</h3>
<p>The span is positioned absolutely from the right of the screen. It has the same background and border as the logbar div. The font is smaller than the default size and light grey in color.</p>
<pre>
#logbar span {
	position: absolute;
	right: 50px;
	top: 0.7em;
	padding: 0 0.3em 0.3em;
	background: #1f2326;
	border: 1px solid #000;
	border-top: 0;
	color: #aaa;
	font-size: 0.9em;
}
</pre>

<h3>Links</h3>
<p>The links are light blue with a dotted bottom border and change to white on hover. They have left and right margins to separate from each other.</p>
<pre>
#logbar span a, #logbar span a:link, #logbar span a:visited {
	text-decoration: none;
	border-bottom: 1px dotted #246fc2;
	color: #78b7fd;
	margin: 0 0.2em;
}
#logbar span a:hover, #logbar span a:active {
	border-bottom: 1px dotted #fff;
	color: #cde5ff;
}
</pre>

<h3>Username</h3>
<p>The username is colored white to make it stand out even more.</p>
<pre>
#logbar strong {
	color: #fff;
}
</pre>

