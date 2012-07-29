package com.maxpolun.redditstream;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.sym.Name;

public class Link extends Thing {
	public LinkData data;
	public Link() {
		data = new LinkData();
	}

	public void readJson(JsonParser jsonParser) throws JsonParseException, IOException {
		JsonToken tok = jsonParser.nextToken();
		while(tok != JsonToken.END_OBJECT) {
			String name = jsonParser.getCurrentName();
			if(name.equals("data")) {
				readData(jsonParser);
			} else if(name.equals("kind")){
				kind = jsonParser.nextTextValue(); 
			}
			tok = jsonParser.nextToken();
		}
	}

	private void readData(JsonParser jsonParser) throws JsonParseException, IOException {
		JsonToken tok = jsonParser.nextToken();
		if(tok!=JsonToken.START_OBJECT) {
			throw new IllegalStateException("Expected data to be an object, got " + tok.toString() + " instead.");
		}
		tok = jsonParser.nextToken();
		while(tok != JsonToken.END_OBJECT){
			String name = jsonParser.getCurrentName();
			readDataField(name, jsonParser);
			tok = jsonParser.nextToken();
		}
		
	}

	private void readDataField(String name, JsonParser jsonParser) throws JsonParseException, IOException {
		if     (name.equals("id"))       {this.data.id = jsonParser.nextTextValue(); } 
		else if(name.equals("title"))    {this.data.title = jsonParser.nextTextValue();}
		else if(name.equals("name"))     {this.data.name = jsonParser.nextTextValue();}
		else if(name.equals("ups"))      {this.data.ups = jsonParser.nextIntValue(0);}
		else if(name.equals("downs"))    {this.data.downs = jsonParser.nextIntValue(0);}
		else if(name.equals("score"))    {this.data.score = jsonParser.nextIntValue(0);}
		else if(name.equals("thumbnail")){this.data.thumbnail = jsonParser.nextTextValue();}
		else if(name.equals("url"))      {this.data.url = jsonParser.nextTextValue();}
		else if(name.equals("author"))   {this.data.author = jsonParser.nextTextValue();}
		else if(name.equals("domain"))   {this.data.domain = jsonParser.nextTextValue();}
		else if(name.equals("permalink")){this.data.permalink = jsonParser.nextTextValue();}
		else if(name.equals("media"))    {readMedia(jsonParser);}
		else if(name.equals("media_embed"))    {readMediaEmbed(jsonParser);}
		else {
			jsonParser.nextToken(); // just skip the data;
		}
		
	}

	private void readMediaEmbed(JsonParser jsonParser) throws JsonParseException, IOException {
		ignoreObject(jsonParser);
	}

	private void ignoreObject(JsonParser jsonParser) throws JsonParseException, IOException {
		JsonToken tok = jsonParser.nextToken();
		if(tok!=JsonToken.START_OBJECT) {
			throw new IllegalStateException("Expected data to be an object, got " + tok.toString() + " instead.");
		}
		tok = jsonParser.nextToken();
		while(tok != JsonToken.END_OBJECT){
			jsonParser.nextToken(); // skip the value
			tok = jsonParser.nextToken(); // next name
		}
		
	}

	private void readMedia(JsonParser jsonParser) throws JsonParseException, IOException {
		JsonToken tok = jsonParser.nextToken();
		if(tok!=JsonToken.START_OBJECT) {
			throw new IllegalStateException("Expected data to be an object, got " + tok.toString() + " instead.");
		}
		tok = jsonParser.nextToken();
		while(tok != JsonToken.END_OBJECT){
			String name = jsonParser.getCurrentName();
			if(name.equals("oembed")) {
				ignoreObject(jsonParser);
			} else {
				jsonParser.nextToken();// ignore fields
			}
			tok = jsonParser.nextToken(); // next name
		}
		
	}
}
