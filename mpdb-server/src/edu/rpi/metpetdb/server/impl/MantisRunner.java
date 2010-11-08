package edu.rpi.metpetdb.server.impl;

import java.io.IOException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;

import edu.rpi.metpetdb.client.model.User;

public class MantisRunner implements Runnable
{

	private User u;
	private String password;

	public MantisRunner(User u, String password) {
		this.u = u;
		this.password = password;

	}

	public void run() {
		HttpClient client = new HttpClient();

		GetMethod get = new GetMethod(
				"http://shyguy.cs.rpi.edu/mantis/scb_user_create.php");

		// curl -O http://shyguy.cs.rpi.edu/mantis/scb_user_create.php
		// -d username=foo -d realname=foo%20bar -d password=test -d
		// password_verify=test -d email=scball@yahoo.com -d
		// access_level=reporter -d enabled=true

		NameValuePair[] nvp = new NameValuePair[] {
				new NameValuePair("username", u.getName()),
				new NameValuePair("realname", u.getName()),
				new NameValuePair("password", password),
				new NameValuePair("password_verify", password),
				new NameValuePair("email", u.getEmailAddress()),
				new NameValuePair("access_level", "25"),
				new NameValuePair("enabled", "true")
		};
		get.setQueryString(nvp);

		try {
			client.executeMethod(get);
			System.out.print(get.getResponseBodyAsString());
		} catch (HttpException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	
/*	 public static void main(String args[]) { 
	
     User u = new User();
	 u.setEmailAddress("scball@yahoo.com"); 
	 u.setName("Steve Ball");
	 
	 String password = "foobar";
	 MantisRunner mr  = new MantisRunner(u, password);
	 mr.run();
	 //Thread t = new Thread(new MantisRunner(u, password)); t.start(); 
	 }
	*/
	
}
