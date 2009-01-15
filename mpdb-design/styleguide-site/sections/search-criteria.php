
<h1>Search Criteria</h1>
<p>How to populate the summary of the current search criteria.</p>

<h3>Preview</h3>

<p>Search criteria with almost all the stuff filled in. The sections should be expand/collapseable.</p>

<div style="width: 300px; border: 1px solid #aaa">
<div style="padding: 5px; background-color: #aaa;"><strong>Search Criteria</strong></div>
<div style="padding: 5px; background-color: #ccc;"><strong>Rock Type</strong></div>
<div style="padding: 5px; border-bottom:1px solid #aaa;">Blueschist, Metaarkose, Mylonite, Slate</div>
<div style="padding: 5px; background-color: #ccc;"><strong>Location</strong></div>
<div style="padding: 5px; border-bottom:1px solid #aaa;">North: -66.84726249697599<br/>
South: -83.61468904835162<br/>
East: 137.109375<br/>
West: 56.953125</div>
<div style="padding: 5px; background-color: #ccc;"><strong>Minerals</strong></div>
<div style="padding: 5px; border-bottom:1px solid #aaa;">All Tectosilicates</div>
<div style="padding: 5px; border-bottom:1px solid #aaa;">Albites: Oligoclase, Andesine</div>
<div style="padding: 5px; border-bottom:1px solid #aaa;">All Oxides except Hamatite</div>
<div style="padding: 5px; background-color: #ccc;"><strong>Metamorphic Grade</strong></div>
<div style="padding: 5px; border-bottom:1px solid #aaa;">Something, Some other thing, yup</div>
<div style="padding: 5px; background-color: #ccc;"><strong>Chemistry</strong></div>
<div style="padding: 5px; border-bottom:1px solid #aaa;">5.0 &lt; Al &lt; 6.7 [%wt]</div>
<div style="padding: 5px; border-bottom:1px solid #aaa;">12.6 &lt; CeO<sub>2</sub> &lt; 14.7 [%wt]</div>
<div style="padding: 5px; background-color: #ccc;"><strong>Other</strong></div>
<div style="padding: 5px; border-bottom:1px solid #aaa;">Owner: Frank Spear</div>
<div style="padding: 5px; border-bottom:1px solid #aaa;">Collection Date: 10/2007 - 10/2008</div>
</div>

<h3>Location</h3>
<p>Since we decided that the user can choose a location either by entering coordinates <em>or</em> choosing a region/country combination (not both), the Location part will display only one of these:</p>

<div class="twobox l" style="width:45%">
<div style="padding: 5px; background-color: #ccc;"><strong>Location</strong></div>
<div style="padding: 5px; border-bottom:1px solid #aaa;">North: -66.84726249697599<br/>
South: -83.61468904835162<br/>
East: 137.109375<br/>
West: 56.953125</div>
</div>

<div class="twobox r" style="width:45%">
<div style="padding: 5px; background-color: #ccc;"><strong>Location</strong></div>
<div style="padding: 5px; border-bottom:1px solid #aaa;">Region: Valhalla Complex, British Columbia</div>
<div style="padding: 5px; border-bottom:1px solid #aaa;">Country: United States, Belgium</div>
</div>

<p>.</p>

<h3>Minerals</h3>
<p>This can get kinda crazy. I was trying to come up with ways to represent what the user has selected in a meaningful way that also fits in this tiny space. I decided that the hierarchy would have to be compromised a bit. By that I mean if a node has children, it is considered a top-level category, whether it has a parent node or not. This way, a summary can be built, with one "sentence" for each category of Mineral that has some or all children selected.</p>

<p>But, just like everything in life, there is an exception to this rule. The exception occurs when we do not want to "repeat" data. An example would be "All Tectosilicates". We would <em>not</em> want to also display "All Silica", because that is implied.</p>

<p>I think the following pseudo-code will help:</p>

<pre>if category.selected == 0 || 
category.selected == category.total && category.hasparent && category.parent.selected &gt; 1:
	do not show
else if category.selected == category.total:
	show "All [category]"
else if category.total &gt; 6 && category.unselected &lt;= 3:
	show "All [category] except [unselected1], [unselected2], [unselected3]"
else if category.selected &gt; 1:
	show "[category]: [selected1], [selected2], ..., [selectedN]"
</pre>

<p>Other things to note: if a child has children, it is only considered 'selected' by its parent when it has all its children selected. Also, if some (but not all) of its children are selected, its display name should be prepended with "some " when being displayed inside its parent:</p>

<pre>if category.hasparent && category.selected &lt; category.total && category.selected &gt; 0 && category.parent.selected &gt; 1:
		in parent show "some [category]" 
		// (regardless of whether it is in an "except" statement or not)
		// valid example: "All Tectosilicates except some Silica"
		// valid example: "Tectosilicates: Sodalite, some Silica, Leucite"
		// invalid example: "Tectosilicates: some Silica"
			// instead, this should just be "Silica: Coesite, Quartz"
</pre>
