package com.maxpolun.redditstream;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.maxpolun.redditstream.User.UserLoginException;

public class Reddit {
	public String userAgent;
	
	public Reddit(String ua){
		userAgent = ua;
	}
	public User login(String username, String password) throws IOException, UserLoginException {
		User u = new User(username, password);
		URL url = new URL(Router.loginRoute(u) + "api_type=json&user=" + URLEncoder.encode(u.name, "UTF-8") +
				"&passwd=" + URLEncoder.encode(u.passwd, "UTF-8"));
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		
		
		Reader resp = postUserLogin(u, conn);
		JsonFactory factory = new JsonFactory();
		JsonParser parser = factory.createJsonParser(resp);
		u.readJson(parser);
		return u;
	}
	
	private Reader postUserLogin(User u, HttpURLConnection conn) throws IOException {
		try {
			conn.setRequestProperty("User-Agent", userAgent);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			conn.setFixedLengthStreamingMode(0);
			conn.setDoOutput(true);
			DataOutputStream ostream = new DataOutputStream(conn.getOutputStream());
			try {
				ostream.flush();
			} finally {
				ostream.close();	
			}
			
			conn.setDoInput(true);
			return new BufferedReader(new InputStreamReader(conn.getInputStream()));
			
		} finally {
			if(conn != null) {
				conn.disconnect();
			}
		}
	}
}
