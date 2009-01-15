

// hide email address from spiders
function email(user,show,domain) {
	a="rpi"; b=".edu"; c="@";
	addy = user+c;
	addy += (domain)? domain : a+b;
	display = (show)? show : addy;
	document.write("<a href=\"mailto:"+addy+"\" title=\""+addy+"\">"+display+"</a>");
}

