
<h1>Forms</h1>
<p>These are the default styles used to achieve uniform form styling throughout MetPetDB.</p>
<p><strong>Note:</strong> IE-only rules are highlighted in <span class="ie">blue</span> to designate their inclusion in the separate IE-only stylesheet.</p>

<h3>Form sectioning</h3>
<p>Forms are divided into logical sections using the fieldset tag with specific ids. Each fieldset has a legend which acts as the title of the section. The <strong>form</strong> class is given to the containing element of the form sections. Previously, all form styles were assumed to be contained in a form tag, but as such is not always the case they have been generalized to work inside any element.</p>
<pre class="html">
&lt;div class="form"&gt;
&lt;fieldset id="section1"&gt;
	&lt;legend&gt;Section1 Title&lt;/legend&gt;
	...
&lt;/fieldset&gt;
&lt;fieldset id="section2"&gt;
	&lt;legend&gt;Section2 Title&lt;/legend&gt;
	...
&lt;/fieldset&gt;
&lt;/div&gt;
</pre>
<p>The fieldsets have bottom margins and a border to visually separate them. The legends are bolded and have a color and padding for readability. In IE the margin and paddings are a little different for the form and fieldsets. The fieldset border and legend background must also be changed to display correctly in IE.</p>
<pre>
#content .form fieldset {
	margin-bottom: 1em;
	border: 1px dotted #759140;
}
#content .form legend {
	color: #4e681c;
	font-weight: bold;
	padding: 0.4em;
}
<span class="ie">#content .form {
	margin-top: -2em;
}
#content .form fieldset {
	padding: 1em;
	margin-bottom: 0;
	border-style: solid;
	border-color: #afcf73;
}
#content .form legend {
	background: #fff;
}</span>
</pre>


<h3>Default label/input pairing</h3>
<p>The default structure of a form is an ordered list where each list item contains a form input and its label. In this default case, the label is in the "left column" and the input is in the "right column". The right column is positioned by wrapping its contents in a div tag.</p>
<pre class="html">
&lt;fieldset id="section1"&gt;
&lt;legend&gt;Section1 Title&lt;/legend&gt;
&lt;ol&gt;
	&lt;li&gt;&lt;label for="i"&gt;Input1 Label&lt;/label&gt; &lt;div&gt;&lt;input id="i" /&gt;&lt;/div&gt;&lt;/li&gt;
	&lt;li&gt;&lt;label for="j"&gt;Input2 Label&lt;/label&gt; &lt;div&gt;&lt;input id="j" /&gt;&lt;/div&gt;&lt;/li&gt;
	&lt;li&gt;&lt;label for="k"&gt;Input3 Label&lt;/label&gt; &lt;div&gt;&lt;input id="k" /&gt;&lt;/div&gt;&lt;/li&gt;
&lt;/ol&gt;
&lt;/fieldset&gt;
</pre>
<p>The form uses ordered lists (as opposed to unordered lists) for more accessibility in CSS-disabled browsers. They have their margin and padding zero'd out.  The list items have their default style removed, override the default blue color, and have margin and padding values for cross-browser consistency. In IE the list item padding needs to be zero'd out.</p>
<pre>
#content .form ol {
	margin: 0;
	padding: 0;
}
#content .form ol li {
	list-style: none;
	color: #000;
	margin: 0;
	padding: 0.3em 0;
}
<span class="ie">#content .form ol li {
	padding: 0;
}</span>
</pre>
<p>The divs (as mentioned above) are positioned from the left so the form appears to be in two columns. Divs inside of divs should not inherit this margin. The labels are absolutely positioned 0 units from the left so that they are taken out of the flow and allow the divs to line up horizontally with them. The labels also have an increased line height so they line up with the inputs. In IE the labels must be positioned from the top in order to line up with the div.</p>
<pre>
#content .form ol li div {
	margin-left: 12em;
}
#content .form ol li div div {
	margin-left: 0;
}
#content .form ol li label {
	line-height: 1.5;
	position: absolute;
	left: 0;
}
<span class="ie">#content .form ol li label {
	top: 1em;
}</span>
</pre>

<h3>Inputs and Buttons</h3>
<p>All input types have a border and padding by default. The border is darkened on hover to help distinguish which input field is being selected.</p>
<pre>
#content .form input {
	border: 1px solid #ccc;
	padding: 0.1em 0.3em;
}
#content .form input:hover {
	border: 1px solid #999;
}
</pre>
<p>The same styles are applied to all buttons (not just ones in <strong>.form</strong>), except the button also needs to have the pointer coursor defined. Buttons in IE have a lot of padding already, so it should be virtually zero'd out.</p>
<pre>
button {
	border: 1px solid #ccc;
	padding: 0.3em;
	cursor: pointer;
}
button:hover {
	border: 1px solid #999;
}
<span class="ie">button {
	padding: 0.1em 0;
}</span>
</pre>

<p>Inputs of the checkbox or radio type are included inside their label tag for increased usability. Their default border is removed for style purposes.</p>
<pre class="html">
&lt;li&gt;&lt;label for="i"&gt;&lt;input id="i" type="checkbox" /&gt; Checkbox label&lt;/label&gt;&lt;/li&gt;
</pre>
<pre>
#content .form label input {
	border: 0 !important;
}
</pre>

<h3>Required fields</h3>
<p>To show that a field is required, it has an asterisk inside an em tag in its label, and a <strong>req</strong> class applied to the input (if it is a text input).</p>
<pre class="html">
&lt;li&gt;&lt;label for="i"&gt;Input1 Label &lt;em&gt;*&lt;/em&gt;&lt;/label&gt; &lt;span&gt;&lt;input id="i" type="text" class="req" /&gt;&lt;/span&gt;&lt;/li&gt;
</pre>
<p>The em tags inside label tags are exclusively used to show a field is required. They have a red color, larger font size, are bold, and the default italic quality of the em tag is overridden. The required inputs have a light green background.</p>
<pre>
label em {
	color: #ff4545;
	font-weight: bold;
	font-size: 1.2em;
	font-style: normal;
}
input.req {
	background: #f6ffdd;
}
</pre>

<h3>Invalid fields</h3>
<p>Invalid and invalid+required fields have their respective classes that are applied to the input tag.</p>
<pre class="html">
&lt;li&gt;&lt;label for="i"&gt;Input1 Label&lt;/label&gt; &lt;span&gt;&lt;input id="i" type="text" class="invalid" /&gt;&lt;/span&gt;&lt;/li&gt;
</pre>
<p>Invalid inputs have a blue background and invalid+required inputs have a purple background.</p>
<pre>
input.invalid {
	background: #eeeef7;
}
input.invalidreq {
	background: #eae1fa;
}
</pre>

<h3>Invalid messages</h3>
<p>Invalid messages appear below the invalid inputs inside a span tag with the <strong>invalid-msg</strong> class. They are block level, and have default padding and a smaller, red font.</p>
<pre class="html">
&lt;label&gt;E-mail Address &lt;em&gt;*&lt;/em&gt;&lt;/label&gt;
&lt;div&gt;
	&lt;input type="text" class="invalidreq" /&gt;
	&lt;span class="invalid-msg"&gt;
		&lt;img src="/images/iconWarning.gif"/&gt; 
		Email Address must be provided.
	&lt;/span&gt;
&lt;/div&gt;
</pre>
<pre>
#content .form ol li div span {
	display: block;
	clear: both;
}
.invalid-msg {
	color: #ff4545 !important;
	padding: 0.3em 0;
	font-size: 0.95em;
}
</pre>

<h3>Inputs without labels</h3>
<p>Some inputs do not have labels, in order to align them to the right column, the <strong>colindent</strong> class is assigned to the list item. Note that the div tag is <u>not</u> used.</p>
<pre class="html">
&lt;li class="colindent"&gt;&lt;input id="i" /&gt;&lt;/li&gt;
</pre>
<pre>
#content .form li.colindent {
	padding-left: 12em;
}
</pre>

<h3>Single column labels</h3>
<p>Labels that do not have a second column next to them should not be positioned absolutely. Such labels are contained in an element with the <strong>lone</strong> class, or have the lone class directly applied to them.</p>
<pre class="html">
&lt;li class="lone"&gt;&lt;label for="sall"&gt;&lt;input id="sall" name="sall" type="checkbox" /&gt; All Samples&lt;/label&gt;&lt;/li&gt;
</pre>
<pre>
#content .form label.lone, 
#content .form .lone label {
	position: relative;
}
</pre>

<h3>Submission buttons</h3>
<p>Buttons that submit a form should have the <strong>submit</strong> class which gives the button bolded font (and adds a top margin in IE).</p>
<pre>
#content .form .submit {
	font-weight: bold;
}
<span class="ie">#content .form ol li .submit {
	margin-top: 1.5em;
}</span>
</pre>


<h3>Multiple column form items</h3>
<p>To organize form elements into columns not following the label/input pairing model, an unordered list is used with the <strong>clearfix</strong> class. Each column is a list item floated to the left with the <strong>l</strong> class (the classes are discussed on the <a href="/styleguide/p?=multi-purpose" title="Multi-Purpose Styles">Multi-Purpose Styles</a> page). Each column can contain any elements organized in any way. Note that an ordered list could be used, but it is recommended to use an unordered list for special positioning and ordered lists for the default two-column setup for consistency.</p>
<pre class="html">
&lt;fieldset id="section1"&gt;
&lt;legend&gt;Section1 Title&lt;/legend&gt;
&lt;ol&gt;
	&lt;li&gt;Here's some options arranged in 4 columns:
		&lt;ul class="clearfix"&gt;
			&lt;li class="l"&gt;&lt;label&gt;&lt;input type="checkbox" /&gt; Text&lt;/label&gt;&lt;/li&gt;
			&lt;li class="l"&gt;&lt;label&gt;&lt;input type="checkbox" /&gt; Text&lt;/label&gt;&lt;/li&gt;
			&lt;li class="l"&gt;This third column has a list of stuff in it:
				&lt;ol&gt;
					&lt;li&gt;&lt;label&gt;&lt;input type="checkbox" /&gt; Text&lt;/label&gt;&lt;/li&gt;
					&lt;li&gt;&lt;label&gt;&lt;input type="checkbox" /&gt; Text&lt;/label&gt;&lt;/li&gt;
					&lt;li&gt;&lt;label&gt;&lt;input type="checkbox" /&gt; Text&lt;/label&gt;&lt;/li&gt;
				&lt;/ol&gt;
			&lt;/li&gt;
			&lt;li class="l"&gt;&lt;label&gt;&lt;input type="checkbox" /&gt; Text&lt;/label&gt;&lt;/li&gt;
		&lt;/ul&gt;
	&lt;/li&gt;
&lt;/ol&gt;
&lt;/fieldset&gt;
</pre>
<p>Because multiple columns are floated, other list items (on new lines) must clear them. Multiple columns are separated by a right margin and must not clear anything.</p>
<pre>
#content .form li {
	position: relative;
	clear: both;
}
#content .form ul li.l {
	margin-right: 2em;
	clear: none;
}
</pre>

<h3>Collapsed fieldsets</h3>
<p>Some fieldsets may be collapsed and expanded to facilitate navigating a long form. The fieldset is given a <strong>collapsed</strong> class, and all data that is shown in the collapsed fieldset is contained in an unordered list. Inside the legend is the plus/minus image used to expand or collapse the fieldset. The changes between expanded and collapsed fieldset HTML is shown below (changes in <span class="hilite">red</span>).</p>
<pre class="html">
<strong>Expanded:</strong>
&lt;fieldset id="s-other"&gt;
&lt;legend&gt;&lt;img src="/images/minus.gif" alt="collapse" id="exp-other" /&gt; Other Information&lt;/legend&gt;
	&lt;ul&gt;
		&lt;li&gt;&lt;label for="samplenum"&gt;Sample Number&lt;/label&gt; &lt;div&gt;&lt;input type="text" size="10" name="samplenum" id="samplenum"/&gt;&lt;/div&gt;&lt;/li&gt;
		&lt;li&gt;&lt;label for="cmnts"&gt;Comments&lt;/label&gt; &lt;div&gt;&lt;input type="text" name="cmnts" id="cmnts"/&gt;&lt;/div&gt;&lt;/li&gt;
	&lt;/ul&gt; 
&lt;/fieldset&gt;

<strong>Collapsed:</strong>
&lt;fieldset id="s-other" <span class="hilite">class="collapsed"</span>&gt;
&lt;legend&gt;&lt;img src="/images/<span class="hilite">plus</span>.gif" alt="<span class="hilite">expand</span>" id="exp-other" /&gt; Other Information&lt;/legend&gt;
	&lt;ul&gt;
		&lt;li&gt;<span class="hilite">&lt;label&gt;Sample No.:&lt;/label&gt; 34786764768</span>&lt;/li&gt;
		&lt;li&gt;<span class="hilite">&lt;label&gt;Comments:&lt;/label&gt; This sample is off da hook!</span>&lt;/li&gt;
	&lt;/ul&gt; 
&lt;/fieldset&gt;
</pre>
<p>The fieldset's border is removed on all but the top edge. The contents are brought closer to the legend with a negative margin. Since IE has wonky fieldset support, list items must be floated (or else the border is displayed weirdly).</p> 
<pre>
#content .form fieldset.collapsed {
	border-width: 1px 0 0 0;
}
#content .form fieldset.collapsed ul {
	margin-top: -0.7em;
	margin-left: 1em;
}
<span class="ie">#content .form .fieldset.collapsed ul li {
	float: left;
}</span>
</pre>
<p>The plus/minus image is made to look like a link by changing its cursor graphic. In IE the image sometimes has a margin (huh?) so it must be zero'd out.</p>
<pre>
#content .form .fieldset legend img {
	cursor: pointer;
}
<span class="ie">#content .form fieldset.collapsed legend img {
	margin:0;
}</span>
</pre>
<p>Labels are bolded</p>
<pre>
#content .form fieldset.collapsed label {
	font-weight: bold;
}
</pre>




