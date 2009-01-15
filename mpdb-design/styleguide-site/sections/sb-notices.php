
<h1>Notices</h1>
<p>Testing the styles for different types of notices.</p>

<style>
.notice-panel .notice-message {
	padding: 6px 9px;
}
.notice-panel .notice-hide {
	float: right;
	padding: 6px 7px 6px 2px;
}
.notice-panel .notice {
	border: 1px solid #EFEFEF;
	color: #000;
}
.notice-panel .notice-success {
	border-color: #cbee95;
	border-bottom-color: #bee0a2;
	color: #3B5C20;
}
.notice-panel .notice-error {
	border-color: #ffd2d2;
	border-bottom-color: #ffb3a1;
	color: #c41616;
}
.notice-panel .notice-working {
	border-color: #cde3f3;
	border-bottom-color: #a2c6de;
	color: #617684;
}
.notice-panel .notice-warning {
	border-color:#FFF1D2;
	border-bottom-color:#FFC446;
	color: #BC6600;
}
</style>


<h3>General</h3>

<style>

</style>

<div style="width: 300px">
<div class="notice-panel">
	<div class="notice">
		<a title="Hide" class="notice-hide">Hide</a>
		<div class="notice-message">
			<div class="gwt-HTML">This is a general message.</div>
		</div>
	</div>
</div>
</div>

<h3>Working</h3>

<div style="width: 300px">
<div class="notice-panel">
	<div class="notice notice-working">
		<a title="Hide" class="notice-hide">Hide</a>
		<div class="notice-message">
			<div class="gwt-HTML">This is a working message.</div>
		</div>
	</div>
</div>
</div>

<h3>Success</h3>

<div style="width: 300px">
<div class="notice-panel">
	<div class="notice notice-success">
		<a title="Hide" class="notice-hide">Hide</a>
		<div class="notice-message">
			<div class="gwt-HTML">This is a success message.</div>
		</div>
	</div>
</div>
</div>

<h3>Warning</h3>

<div style="width: 300px">
<div class="notice-panel">
	<div class="notice notice-warning">
		<a title="Hide" class="notice-hide">Hide</a>
		<div class="notice-message">
			<div class="gwt-HTML">This is a warning message.</div>
		</div>
	</div>
</div>
</div>

<h3>Error</h3>

<div style="width: 300px">
<div class="notice-panel">
	<div class="notice notice-error">
		<a title="Hide" class="notice-hide">Hide</a>
		<div class="notice-message">
			<div class="gwt-HTML">This is an error message.</div>
		</div>
	</div>
</div>
</div>
