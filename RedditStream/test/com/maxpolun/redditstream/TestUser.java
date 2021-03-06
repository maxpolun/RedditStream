package com.maxpolun.redditstream;

import static org.junit.Assert.*;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.maxpolun.redditstream.User.UserLoginException;

public class TestUser {

	@Test
	public void readJsonSuccess() {
		String testJson = "{\"json\":{" +
					"\"errors\":[]," + 
					"\"data\":{" +
						"\"modhash\":\"2c9wtezj7aece684bfbbe62a7cdee52d75c2e34950e9cc6aa7\"," + 
						"\"cookie\":\"8865532,2012-07-30T18:10:30,dbb71d3a96a4580908ab06b8de9d2e922f37bbbd\"" +
					"}" + 
				"}}";
		
		JsonFactory factory = new JsonFactory();
		User u = new User("test", "password");
		try {
			JsonParser parser = factory.createJsonParser(testJson);
			u.readJson(parser);
			
			assertEquals("test", u.name);
			assertEquals("password", u.passwd);
			assertEquals("2c9wtezj7aece684bfbbe62a7cdee52d75c2e34950e9cc6aa7", u.modhash);
			assertEquals("8865532,2012-07-30T18:10:30,dbb71d3a96a4580908ab06b8de9d2e922f37bbbd", u.reddit_session);
		} catch (Exception e) {
			e.printStackTrace();
			fail("expected no exceptions to be thrown");
		}
	}
	
	@Test
	public void readJsonFailure(){
		String testJson = "{\"json\":{" +
		"\"errors\":[[" +
			"\"WRONG_PASSWORD\","+
			"\"invalid password\","+
			"\"passwd\""+
		"]]," + 
	"}}";
		JsonFactory factory = new JsonFactory();
		User u = new User("test", "password");
		try {
			JsonParser parser = factory.createJsonParser(testJson);
			u.readJson(parser);
			fail("expected readJson to throw a UserLoginException, but it didn't.");
		} catch (UserLoginException e) {
			
		} catch (Exception e) {
			e.printStackTrace();
			fail("Got the wrong exception");
		}
	}

}
