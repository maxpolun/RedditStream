package com.maxpolun.redditstream;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.io.SerializedString;

public class User {
	public String name;
	public String modhash;
	public String passwd;
	public String reddit_session;// from cookie
	
	class UserLoginException extends Exception {

		/**
		 * 
		 */
		private static final long serialVersionUID = -2623946137837693149L;
		
	}
	
	public User(String username, String password) {
		name = username;
		passwd = password;
	}

	public void readJson(JsonParser parser) throws JsonParseException, IOException, UserLoginException {
		JsonToken tok = parser.nextToken();
		if(tok != JsonToken.START_OBJECT) {
			throw new IllegalStateException("expected json response to start with an object, got a " + tok.toString() + " instead");
		}
		boolean isJson = parser.nextFieldName(new SerializedString("json"));
		if(!isJson) {
			throw new IllegalStateException("expected json response to have a 'json' element");
		}
		readJsonObject(parser);
		
	}

	private void readJsonObject(JsonParser parser) throws JsonParseException, IOException, UserLoginException {
		JsonToken tok = parser.nextToken();
		if(tok != JsonToken.START_OBJECT) {
			throw new IllegalStateException("expected json object to start an object, got a " + tok.toString() + " instead");
		}
		tok = parser.nextToken();
		while(tok != JsonToken.END_OBJECT) {
			String field = parser.getCurrentName();
			if(field.equals("errors")) {
				readErrors(parser);
			} else if(field.equals("data")) {
				readData(parser);
			}
			tok = parser.nextToken();
		}
	}

	private void readData(JsonParser parser) throws JsonParseException, IOException {
		JsonToken tok = parser.nextToken();
		if(tok != JsonToken.START_OBJECT) {
			throw new IllegalStateException("expected data object to start an object, got a " + tok.toString() + " instead");
		}
		tok = parser.nextToken();
		while(tok != JsonToken.END_OBJECT) {
			String field = parser.getCurrentName();
			if(field.equals("modhash")) {
				this.modhash = parser.nextTextValue();
			} else if(field.equals("cookie")) {
				this.reddit_session = parser.nextTextValue();
			}
			tok = parser.nextToken();
		}
	}

	private void readErrors(JsonParser parser) throws UserLoginException, JsonParseException, IOException {
		JsonToken tok = parser.nextToken();
		if(tok != JsonToken.START_ARRAY) {
			throw new IllegalStateException("expected json object to start an object, got a " + tok.toString() + " instead");
		}
		tok = parser.nextToken();
		while(tok != JsonToken.END_ARRAY) {
			readErrorArray(parser);
			tok = parser.nextToken();
		}
	}

	private void readErrorArray(JsonParser parser) throws JsonParseException, IOException, UserLoginException {
		JsonToken tok = parser.getCurrentToken();
		if(tok != JsonToken.START_ARRAY) {
			throw new IllegalStateException("expected error array to start an array, got a " + tok.toString() + " instead");
		}
		tok = parser.nextToken();
		while(tok != JsonToken.END_ARRAY) {
			String value = parser.getText();
			if(value.equals("WRONG_PASSWORD")){
				throw new UserLoginException(); 
			}
			tok = parser.nextToken();
		}
	}
}
