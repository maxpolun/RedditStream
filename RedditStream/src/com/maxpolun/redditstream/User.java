package com.maxpolun.redditstream;

public class User {
	public String name;
	public String modhash;
	public String passwd;
	public String reddit_session;// from cookie
	
	public User(String username, String password) {
		name = username;
		passwd = password;
	}
}
