
<?php 
$p = (isset($_GET['p']))? $_GET['p'] : "changes"; 
$file = "sections/$p.php";
?>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" lang="en">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>MetPetDB Style Guide</title>
<link rel="stylesheet" type="text/css" href="style.css" />
<script type="text/javascript" src="lib.js"></script>
</head>

<body>
<div id="container">

<a name="top"></a>

<div id="header">
<h1><a href="/styleguide/" title="Style Guide">MetPetDB Style Guide</a></h1>
<p>A reference for MPDB's CSS and HTML markup.</p>
</div>

<div id="sidebar">
<?php 
include_once('nav.php'); 

$n = new nav();

$n->add("changes","Change Log");

$n->add("Page Sections");
$n->add("layout","General Layout");
$n->add("logo","Header Logo");
$n->add("logbar","Header Logbar");
$n->add("main-nav","Main Nav");
$n->add("content","Content Area");
$n->add("sidebar","Sidebar");
$n->add("rcol","Right Column");
$n->add("footer-nav","Footer Nav");
$n->add("footer","Footer");

$n->add("General Styles");
$n->add("headings","Headings");
$n->add("links","Links");
$n->add("lists","Lists");
$n->add("forms","Forms");
$n->add("tables","Tables");
$n->add("multi-purpose","Multi-Purpose");

$n->add("Widgets");
$n->add("special-links","Special Links");
$n->add("subpage-tabs","Subpage Tabs");
$n->add("change-view","Change View Options");
$n->add("comments","Comments");
$n->add("notice-box","Notice Box");
$n->add("fav-sample","Favorite Sample");

$n->add("Specific Page Styles");
$n->add("index","Index");
$n->add("search","Search");
$n->add("search-criteria","Search Criteria");
$n->add("sample-details","Sample Details");
$n->add("subsample-analysis","Subsample Analysis");
//$n->add("subsample-map","Subsample Map");
$n->add("dialog-box","Dialog Box");


$n->add("CSS Dump");
$n->add("css-style","style.css");
$n->add("css-style-ie","style-ie.css");
$n->add("css-index","index.css");
$n->add("css-search","search.css");
$n->add("css-sample-details","sample-details.css");
$n->add("css-subsample-analysis","subsample-analysis.css");
$n->add("css-dialog-box","dialog-box.css");

$n->showNav($p);
?>
</div>

<div id="rcol">
<?php if (file_exists($file)) { ?>
<span class="updated">last updated: <?= date("D n/d/y g:ia",filemtime($file)+3*60*60); ?></span>
<?php include_once($file); } else { ?>
<div class="notice">Section still in progress.</div>
<?php } ?>
</div>

<div id="footer">
<div class="tr"><a href="#top" title="Back to top">Back to top</a></div>
<?php include_once('footer.php'); ?>
</div>

</div>
</body>
</html>
