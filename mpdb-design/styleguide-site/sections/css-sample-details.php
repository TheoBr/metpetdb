
<h1>sample-details.css</h1>
<p>CSS for the sample details page.</p>
<pre>
/*-------------------------------------
             sample details
 --------------------------------------*/
#ssamples table {
	float: left;
	margin-right: 1em;
	width: inherit;
}
#ssamples table td {
	vertical-align: top;
	border-bottom: none;
}
a.ssimg img, a:link.ssimg img {
	padding: 5px;
	background: #f9f9f9;
	border: 1px solid #f0f0f0 !important;
}
a:visited.ssimg img {
	background: #f2e8f6;
	border: 1px solid #e8d9ee !important;
}
a:hover.ssimg img, a:active.ssimg img {
	background: #cfd6ef;
	border: 1px solid #bdc6e5 !important;
}
</pre>

<p>From the IE-only stylesheet:</p>
<pre class="ie">
/*-------------------------------------
             sample details
 --------------------------------------*/
* html a.ssimg, * html a:link.ssimg, * html a:visited.ssimg,
* html a:hover.ssimg, * html a:active.ssimg {
	border: 0 !important;
}
</pre>