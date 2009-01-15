
<h1>style-ie.css</h1>
<p>These are the patches for IE6 and IE7.  Its not very big, so it can be included on every page via conditional comments (see <a href="/styleguide/?p=layout" title="General Layout">General Layout</a>). All rules that start with <strong>* html</strong> are applied to IE6 and below, and all others apply to all versions of IE. Note that since IE6 rules may have to override default IE rules, all <strong>* html</strong> rules should come last in the stylesheet.</p>
<pre>
/*=====================================
       MetPetDB main CSS IE fixes
                IE 6 + 7
  =====================================*/


/*-------------------------------------
          general styles - tables
 --------------------------------------*/
 
table {
	padding-left: 1.5em;
	padding-right: 1.5em;
}

/*-------------------------------------
            widget - comments
 --------------------------------------*/
#comments {
	margin-bottom: 2em;
}


/*-------------------------------------
         general styles - forms
 --------------------------------------*/
#content .form {
	margin-top: -2em;
}
#content .form fieldset {
	padding: 1em;
	border-style: solid;
	border-color: #afcf73;
}
#content .form fieldset.collapsed legend img {
	margin:0;
}
#content .form fieldset.collapsed ul li {
	float: left;
}
#content .form legend {
	background: #fff;
}
#content .form ol li {
	padding: 0;
}
#content .form ol li label {
	top: 1em;
}
#content .form ol li .submit {
	margin-top: 1.5em;
}

/*-------------------------------------
   general styles - inputs and buttons
 --------------------------------------*/
button {
	padding: 0.1em 0;
}

/*-------------------------------------
        page specific - Search
 --------------------------------------*/
 
#s-location a {
	border: 0 !important;
}
#s-location img {
	display: inline;
	margin-left: 2em;
}
#s-submit ol {
	margin-bottom: 0;
}
#s-options li.l {
	border-left: 1px solid #afcf73;
}



/*=====================================
                IE 6-only
 ======================================*/


/*-------------------------------------
       general layout - left column
 --------------------------------------*/
 
* html #sidebar {
	left: 1px;
}
* html #sidebar ul.bullet li {
	text-indent: 0;
}
* html #sidebar div.titlebar {
	float: left;
	clear: both;
	width: 190px;
	padding-left: 5px;
	padding-right: 5px; 
}
* html #sidebar ul li {
	float: left;
	clear: both;
	width: 100%;
}
* html #sidebar ul li a {
	line-height: 1.4em;
	border: 0 !important;
}
* html #sidebar ul.user-list ul {
	padding: 0;
	margin: 0;
	border: 0;
}
* html #sidebar ul.user-list ul li {
	border-left: 1px solid #7e9dc3;
	margin-right: -1px;
}


/*-------------------------------------
        general layout - footer
 --------------------------------------*/
* html #footer {
	left: 8px;
}

/*-------------------------------------
         general styles - links
 --------------------------------------*/
 
* html a, * html a:link, * html a:visited, * html a:hover, * html a:active {
	border-bottom: 1px solid !important;
}


/*-------------------------------------
        page specific - My Samples 
 --------------------------------------*/

* html a.ssimg, * html a:link.ssimg, * html a:visited.ssimg,
* html a:hover.ssimg, * html a:active.ssimg {
	border: 0 !important;
}
</pre>

