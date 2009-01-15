
<h1>style.css</h1>
<p>These should be included on every page. <strong>Note:</strong> the order in which these rules appear on the stylesheet is important, as some override ones that come before them.</p>
<pre>
/*=====================================
            MetPetDB main CSS
  =====================================*/


/*-------------------------------------
       general layout - page sections
 --------------------------------------*/

body {
	margin: 0;
	width: 100%;
	min-width: 600px;
	font: 9pt/13pt "Lucida Grande",Verdana,Arial,Helvetica,sans-serif;
	color: #000;
	background: #fff url(/images/bg.gif) repeat-y left top;
}
#container {
	padding-left: 201px;	/* LC fullwidth */
	background: transparent url(/images/bg-sidebar.gif) repeat-y left top;
	overflow: hidden;
	border-top: 1px solid #DAEE9B;
}
#content {
	position: relative;
	float: left;
	width: 100%;
}
#sidebar {
	position: relative;
	float: left;
	width: 200px;			/* LC width - 1px border bg image */
	right: 201px;			/* LC fullwidth */
	margin-left: -100%;
	padding-right: 1px;		/*  1px border bg image */
	color: #3a3436;
}
#main {
	float: left;
	width: 72%;
	margin: 0 -1.5em;
	padding-bottom: 1em;
}
#rcol {
	width: 28%;
	margin: 0;
	float: right;
	background: transparent url(/images/bg-rcol.gif) repeat-y left top;
	padding: 0 !important;
}


/*-------------------------------------
         general styles - headers
 --------------------------------------*/
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

/*-------------------------------------
         general styles - lists
 --------------------------------------*/
ul, ul li {
	list-style: none;
	margin: 0;
	padding: 0;
}
ul.bullet {
	margin-left: 2em;
}
ul.bullet li {
	list-style-image: url(/images/li-bluearrow.gif);
	text-indent: -5px;
}


/*-------------------------------------
          general layout - header
 --------------------------------------*/
#header {
	background: #fff;
}
#header h1 {
	margin-top: 0.5em;
}
#header h1 a, #header h1 a:link,#header h1 a:visited {
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
#header h2 {
	margin: 0.5em 0;
	color: #787e66;
	font-size: 1em;
	margin-left: 1em;
}

/*-------------------------------------
          general layout - logbar
 --------------------------------------*/
#logbar {
	background: #1f2326;
	height: 0.7em;
	position: relative;
	border-bottom: 1px solid #000;
}
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
#logbar strong {
	color: #fff;
}

/*-------------------------------------
        general layout - main nav
 --------------------------------------*/
ul#nav {
	background: #F0FDC1;
	padding: 1em 1em 1em 0;
	border-top: 1px solid #DAEE9B;
	border-left: 201px solid #e0edb7;
}
ul#nav li {
	display: inline;
	padding: 1.5em 0.5em 1.5em 1.4em;
}
ul#nav li a, ul#nav li a:link, ul#nav li a:visited {
	text-decoration: none;
	color: #936b3b;
	border-bottom: 1px dotted #86A02D;
}
ul#nav li a:hover, ul#nav li a:active {
	color: #000;
	border-bottom: 1px dotted #728a21;
}
ul#nav li a.cur {
	color: #000;
	border-bottom: 2px solid;
	font-weight: bold;
}

/*-------------------------------------
      general layout - footer nav
 --------------------------------------*/
ul#fnav {
	padding: 1em 1em 1em 0;
	border-bottom: 1px solid #e7ecde;
	border-left: 201px solid #e7ecde;
	background: #f6f6f6;
}
ul#fnav li {
	display: inline;
	padding: 1.5em 0.5em 1.5em 1.4em;
}
ul#fnav li a, ul#fnav li a:link, ul#fnav li a:visited {
	text-decoration: none;
	color: #766a5c;
	border-bottom: 1px dotted #86A02D;
}
ul#fnav li a:hover, ul#fnav li a:active {
	color: #000;
	border-bottom: 1px dotted #728a21;
}
ul#fnav li a.cur {
	color: #000;
	border-bottom: 2px solid;
	font-weight: bold;
}


/*-------------------------------------
        general layout - left col
 --------------------------------------*/
#sidebar div.titlebar {
	background: #dadada;
	font: 0.8em Verdana, sans-serif;
	padding: 0.8em 0.5em 0.5em;
	color: #324453;
	border-bottom: 1px solid #c8c8c8;
}
#sidebar ul.user-list {
	margin-bottom: 1.2em;
	padding: 0;
}
#sidebar ul.user-list li.odd {
	background: #e6eaf1;
}
#sidebar ul.user-list a, #sidebar ul.user-list a:link, #sidebar ul.user-list a:visited {
	display: block;
	padding: 0.3em 0.7em;
	border: 1px solid transparent;
	border-bottom-color: #ddd;
}
#sidebar ul.user-list a:hover, #sidebar ul.user-list a:active {
	background-color: #fff !important;
}
#sidebar ul.user-list a.cur, #sidebar ul.user-list a.cur:link, #sidebar ul.user-list a.cur:visited  {
	font-weight: bold;
	color: #fff;
	background: #092a53 url(/images/arrow-white.gif) no-repeat right center;
}
#sidebar ul.user-list a.cur:hover, #sidebar ul.user-list a.cur:active  {
	background-color: #163d6c !important;
}
#sidebar ul.user-list a.parent {
	background-image: none !important;
}
#sidebar ul.user-list ul {
	border: 1px solid #7e9dc3;
	border-top: 0;
	background: #e0e7ea;
}
#sidebar ul.user-list ul li.odd {
	background: #d1dce0;
}
#sidebar ul.user-list ul a, #sidebar ul.user-list ul a:link, #sidebar ul.user-list ul a:visited {
	padding-left: 1em;
	font-weight: bold;
}
#sidebar ul.user-list ul a.cur, #sidebar ul.user-list ul a.cur:link, #sidebar ul.user-list ul a.cur:visited  {
	background-color: #3074a4;
}
#sidebar ul.user-list ul a.cur:hover, #sidebar ul.user-list ul a.cur:active  {
	background-color: #4b91c2 !important;
}
#sidebar .notice {
	color: #762525;
}


/*-------------------------------------
        general layout - right col
 --------------------------------------*/
#rcol div.titlebar {
	background: #f5f5f5;
	font: 0.8em Verdana, sans-serif;
	padding: 0.8em 0.5em 0.5em;
	color: #aeaeae;
	border-bottom: 1px solid #ebebea;
}


/*-------------------------------------
  general layout - direct child padding
 --------------------------------------*/
#sidebar div, #sidebar ul, #sidebar p,
#rcol div, #rcol ul, #rcol p {
	font-size: 0.9em;
	line-height: 1.4em;
	padding-left: 0.7em;
	padding-right: 0.7em;
} #sidebar div div, #sidebar ul ul,
  #rcol div div, #rcol ul ul {
	font-size: 1em;
	line-height: 1em;
} #sidebar * div, #sidebar * ul,
  #rcol * div, #rcol * ul {
	padding-left: 0;
	padding-right: 0;
}
#content div, #content p, #content ul, #content ol {
	padding-left: 1.5em;
	padding-right: 1.5em;
} #content * div, #content * p, #content * ul, #content * ol {
	padding-left: 0;
	padding-right: 0;
}
#content h1 {
	padding-left: 0.75em;
	padding-right: 0.75em;
} #content * h1 {
	padding-left: 0;
	padding-right: 0;
}
#content h2 {
	padding-left: 1em;
	padding-right: 1em;
} #content * h2 {
	padding-left: 0;
	padding-right: 0;
}
#main div, #main p, #main ul, #main ol {
	padding-left: 1.5em !important;
	padding-right: 1.5em !important;
} #main div div, #main div p, #main ul ul, #main ol ol {
	padding-left: 0 !important;
	padding-right: 0 !important;
}
#main h1 {
	padding-left: 0.75em;
	padding-right: 0.75em;
} #main * h1 {
	padding-left: 0;
	padding-right: 0;
}
#main h2 {
	padding-left: 1em;
	padding-right: 1em;
} #main * h2 {
	padding-left: 0;
	padding-right: 0;
}
#footer p, #footer div {
	font-size: 0.9em;
} #footer div div {
	font-size: 1em;
}


/*-------------------------------------
         general layout - footer
 --------------------------------------*/
#footer {
	clear: both;
	position: relative;
	border-top: 1px solid #cddbb5;
	color: #67765a;
	width: 100%;
}
#nsf {
	height: 40px;
	margin: 2.5em 1em 1em 200px;
	padding-bottom: 10px;
	text-align: left;
}
#nsf img {
	background: #aaa;
	float: left;
	margin: -5px 0.7em 0 1.5em;
}
#nsf a:hover img {
	background: #000;
}


/*-------------------------------------
         general layout - content
 --------------------------------------*/
#breadcrumbs {
	background: #f7fbff;
	font: 0.8em Verdana, sans-serif;
	padding: 0.8em 0.5em 0.5em;
	color: #749490;
	border-bottom: 1px solid #e6eef1;
}
#breadcrumbs span {
	color: #000;
}
#content ol {
	margin-left: 2em;
}
#content ol li {
	line-height: 1em;
	margin-bottom: 1em;
	color: #39c;
}
#content ol li span {
	color: #000;
}
#content .notice {
	color: #f17419;
}
#content ul.options {
	margin: 0.7em 0;
}
#content ul.options li {
	display: inline;
	margin-right: 1.5em;
	line-height: 1em;
}


/*-------------------------------------
         general styles - links
 --------------------------------------*/
#container a, #container a:link,
#footer a, #footer a:link {
	text-decoration: none;
	color: #0c5ea3;
	border-bottom: 1px dotted;
}
#container a:visited, #footer a:visited {
	color: #7f15b0;
}
#sidebar a:visited {
	color: #0c5ea3;
}
#container a:hover, #container a:active,
#footer a:hover, #footer a:active {
	color: #318fdf;
}


/*-------------------------------------
         widget - subpage links
 --------------------------------------*/
#content .subpages {
	position: absolute;
	right: 1.5em;
	top: 3.5em;
}
#content .subpages td {
	background: transparent url(/images/tabbar-bg.gif) repeat-x left bottom;
	vertical-align: bottom;
	border-bottom: none;
	line-height: 1;
	padding: 0 0 0 0.4em;
}
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

/*-------------------------------------
        general styles - forms
 --------------------------------------*/
#content .form input {
	border: 1px solid #ccc;
	padding: 0.1em 0.3em;
}
#content .form input:hover {
	border: 1px solid #999;
}
#content .form label input {
	border: 0 !important;
}
#content .form fieldset {
	margin-bottom: 1em;
	border: 1px dotted #759140;
}
#content .form legend {
	color: #4e681c;
	font-weight: bold;
	padding: 0.4em;
}
#content .form ol {
	margin: 0;
	padding: 0;
}
#content .form li {
	position: relative;
	clear: both;
}
#content .form ol li {
	list-style: none;
	color: #000;
	margin: 0;
	padding: 0.3em 0;
}
#content .form ul li.l {
	margin-right: 2em;
	clear: none;
}
#content .form label {
	line-height: 1.5;
	position: absolute;
	left: 0;
}
#content .form ol li div {
	margin-left: 12em;
}
#content .form ol li div div {
	margin-left: 0;
}
#content .form li.colindent {
	padding-left: 12em;
}
#content .form ol li div span {
	display: block;
	clear: both;
}
#content .form fieldset.collapsed {
	border-width: 1px 0 0 0;
}
#content .form fieldset.collapsed ul {
	margin-top: -0.7em;
	margin-left: 1em;
}
#content .form fieldset.collapsed label {
	font-weight: bold;
	position: relative;
}
#content .form fieldset legend img {
	cursor: pointer;
}
#content .form label.lone, 
#content .form .lone label {
	position: relative;
}


/*-------------------------------------
   general styles - inputs and buttons
 --------------------------------------*/
button {
	border: 1px solid #ccc;
	padding: 0.3em;
	cursor: pointer;
}
button:hover {
	border: 1px solid #999;
}
input.req {
	background: #f6ffdd;
}
input.button {
	cursor: pointer;
}
.invalid-msg {
	color: #ff4545 !important;
	padding: 0.3em 0;
	font-size: 0.95em;
}
input.invalid {
	background: #eeeef7;
}
input.invalidreq {
	background: #eae1fa;
}
label em {
	color: #ff4545;
	font-weight: bold;
	font-size: 1.2em;
	font-style: normal;
}
.submit {
	font-weight: bold;
}


/*-------------------------------------
        general styles - tables
 --------------------------------------*/
#content table {
	border: 0;
	width: 100%;
}
#content tr.even {
	background: #fff;
}
#content tr.odd {
	background: #f9f9f9;
}
#content tr.odd{
	background: #f9f9f9;
}
#content table.tableHeaderTable tr, #content table.tableHeaderTable td {
	background: #faf4df;
	font-weight: bold;
	color: #64230f;
}
#content tr.heading th {
	border-bottom: 1px solid #e2c798;
}
#content table.tableHeaderTable tr.heading th.orderby {
	background: #feddbf;
}
#content th {
	background: #faf4df;
	font-weight: bold;
	color: #64230f;
	text-align: left;
	vertical-align: middle;
	padding: 0.3em 0.5em;
	border-bottom: 1px dotted #e2c798;
}
#content td {
	vertical-align: middle;
	padding: 0.3em 0.5em;
	border-bottom: 1px dotted #ddd;
	color: #333;
}
#content td.orderby {
	font-size: 1em;
	font-weight: bold;
	color: #000;
}
#content td.na {
	color: #d18d75;
}
#content tr.action-selected td {
	background-color: #e3eaf3;
}
#content tr.action-selected td select {
	margin-left: 1em;
}


/*-------------------------------------
           widget - notice box
 --------------------------------------*/
#content div.notice-message {
	margin: 1em 1.5em;
	padding: 0.5em 0.5em 0.5em 2.5em;
	background: #f4ecf7 url(/images/iconWarning.gif) no-repeat 0.5em 0.7em;
	border: 1px solid #ddd4e0;
}
#content div.notice-message ul.options {
	margin: 0.2em 0;
}
#content div.notice-message h3 {
	margin-bottom: 0.5em;
}


/*-------------------------------------
          widget - special links
 --------------------------------------*/
.addlink {
	padding-left: 15px;
	background-image: url(/images/add.gif);
	background-repeat: no-repeat;
	background-position: left center;
}


/*-------------------------------------
          widget - view options bar
 --------------------------------------*/
p.view {
	font-size: 0.9em;
	margin: 0.8em 0.3em;
	text-align: right;
	color: #555;
}
p.view a {
	margin: 0 0.3em;
}
p.view a.cur {
	font-weight: bold;
}

/*-------------------------------------
        widget - favorite sample
 --------------------------------------*/
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

/*-------------------------------------
          widget - comments
 --------------------------------------*/
#comments {
	clear: both;
	margin-top: 2em;
}

/*-------------------------------------
           multi-purpose styles
 --------------------------------------*/

.section {margin-top: 1.5em; margin-bottom: 1.5em;}
.subsection {margin-top: 0.7em; margin-bottom: 0.7em;}
.tc {text-align: center;}
.tr {text-align: right;}
.vc {vertical-align: center;}
.tl {text-align: left;}
.bl {display: block;}
.in {display: inline;}
.r {float: right;}
.l {float: left;}
.clr {clear: both;}
.clrr {clear: right;}
.clrl {clear: left;}
.clrn {clear: none;}
.ctr {margin-left: auto; margin-right: auto;}
.nobdr, a img {border: 0 !important;}
.nobg {background: none !important;}
.smt {font-size: 0.9em;}
.xsmt {font-size: 0.8em;}
.xxsmt {font-size: 0.7em;}
.bigt {font-size: 1.1em;}
.ml1 {margin-left: 1em;}
.mr1 {margin-right: 1em;}
.clearfix:after {
	content: "."; 
	display: block; 
	height: 0; 
	clear: both; 
	visibility: hidden;
}

</pre>

<p>From the IE-only stylesheet:</p>
<pre class="ie">
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
</pre>
