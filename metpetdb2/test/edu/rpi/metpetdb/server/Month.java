package edu.rpi.metpetdb.server;

public enum Month {
	Aug(7), Sept(8), Oct(9), Nov(10), Dec(11), Jan(0), Feb(1), Mar(2), Apr(3), May(4), June(5), July(6);
	
	int monthVal = 0;
	
	Month(int monthVal)
	{
		this.monthVal = monthVal;
	}
}
