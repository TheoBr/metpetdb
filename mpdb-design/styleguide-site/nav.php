<?php

class navItem {
	var $href, $title, $text;
	
	function navItem($h, $t, $x="") {
		$this->href = $h;
		$this->title = $t;
		$this->text = htmlspecialchars((empty($x))? $t : $x); 
	}
}

class nav {
	var $id = "nav";
	var $dir = "/styleguide/";
	var $page = "p";
	var $items = array();
	
	function nav($i="", $d="", $p="") {
		if (!empty($i)) $this->id = $i;
		if (!empty($d)) $this->dir = $d;
		if (!empty($p)) $this->page = $p;
	}
	
	function add($h, $t="", $x="")	{
		$item = (empty($t))? $h : new navItem($h, $t, $x);
		array_push($this->items, $item);
	}
	
	function showLink($text, $show="") {
		foreach ($this->items as $i) {
			if ($i->href == $text) {
				echo "<a href=\"".$this->dir."?".$this->page."=".$i->href."\" title=\"".$i->title."\">";
				echo (empty($show))? $i->text : $show;
				echo "</a>";
				return;
			}
		}
		echo "[Link missing]";
	}
	
	function showNav($cur) {
		if (!empty($this->items)) {
			echo "<ul id=\"".$this->id."\">\n";
			foreach ($this->items as $i) {
				$link = $i;
				$class = ($cur == $i->href)? " class=\"cur\"" : "";
				if (is_object($i)) $link = "<a href=\"".$this->dir."?".$this->page."=".$i->href."\" title=\"".$i->title."\">".$i->text."</a>";
				else $class = " class=\"txt\"";
				echo "\t<li$class>$link</li>\n";
			}
			echo "</ul>\n";
		} else {
			echo "<p>No nav.</p>\n";
		}
	}
}

?>
