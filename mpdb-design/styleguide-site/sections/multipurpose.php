
<h1>Multi-Purpose Styles</h1>
<p>Generic styles that can be used for specific designing needs without having to create new CSS rules.</p>

<h3>Text aligning</h3>
<p>Styles used for aligning text inside block-level elements.</p>
<pre>
.tc {text-align: center;}
.tr {text-align: right;}
.tl {text-align: left;}
.vc {vertical-align: center;}
</pre>

<h3>Font sizing</h3>
<p>Change the font size of an element relative to its parent's font size.</p>
<pre>
.smt {font-size: 0.9em;}
.xsmt {font-size: 0.8em;}
.xxsmt {font-size: 0.7em;}
.bigt {font-size: 1.1em;}
</pre>

<h3>Display box model</h3>
<p>Change an element's display type.</p>
<pre>
.bl {display: block;}
.in {display: inline;}
</pre>

<h3>Floating</h3>
<p>Float an element to the right or left.</p>
<pre>
.r {float: right;}
.l {float: left;}
</pre>

<h3>Clearing</h3>
<p>Useful for fixing unwanted behavior when mixing floated and non-floated elements. The <strong>clearfix</strong> class is used when you do not have a clearing element that follows the floated elements. It is applied to the last floated element.</p>
<pre>
.clr {clear: both;}
.clrr {clear: right;}
.clrl {clear: left;}
.clrn {clear: none;}
.clearfix:after {
	content: "."; 
	display: block; 
	height: 0; 
	clear: both; 
	visibility: hidden;
}
</pre>

<h3>Centering</h3>
<p>Horizontally center a block level element with a defined width.</p>
<pre>
.ctr {margin-left: auto; margin-right: auto;}
</pre>

<h3>No border</h3>
<p>Remove borders around images contained in links (and supply a no-border class).</p>
<pre>
.nobdr, a img {border: 0 !important;}
</pre>