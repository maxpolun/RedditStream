package com.maxpolun.redditstream;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;

public class LinkListing {
	
	public String kind;
	public String modhash;
	public String after, before;
	
	public interface OnItemListener{
		void OnItem(Link l);
	}
	private OnItemListener _listener;
	public void setOnItemListener(OnItemListener listener){
		_listener = listener;
	}
	private JsonParser _jsonParser;
	LinkListing(JsonParser jsp, OnItemListener onItemListener) {
		_jsonParser = jsp;
		_listener = onItemListener;
	}
	
	public void run() throws IOException {
		try {
			readListingHeading();
		} finally {
			_jsonParser.close();
		}
		
	}

	private void readListingHeading() throws JsonParseException, IOException {
		JsonToken tok = _jsonParser.nextToken();
		if(tok != JsonToken.START_OBJECT) {
			throw new IllegalStateException("Expected the first thing in the JSON to be a START_OBJECT, got " + tok.toString() + " instead.");
		}
		
		while(_jsonParser.nextToken() == JsonToken.FIELD_NAME) {
			readListingField();
		}
	}

	private void readListingField() throws JsonParseException, IOException {
		String name = _jsonParser.getCurrentName(); 
		if(name.equals("kind")){
			JsonToken tok = _jsonParser.nextToken();
			if(tok != JsonToken.VALUE_STRING) {
				throw new IllegalStateException("Expected string listing, got " + tok.toString());
			}
			kind = _jsonParser.getText();
			if(!kind.equals("Listing")) {
				throw new IllegalStateException("Expected kind to be Listing, got " + kind);
			}
		} else if (name.equals("data")) {
			JsonToken tok = _jsonParser.nextToken();
			if(tok != JsonToken.START_OBJECT) {
				throw new IllegalStateException("Expected data to be an object, got " + tok.asString());
			}
			readListingData();
		}
	}
	private void readListingData() throws JsonParseException, IOException {
		JsonToken tok = _jsonParser.nextToken();
		
		while(tok != JsonToken.END_OBJECT){
			String name = _jsonParser.getCurrentName(); 
			if     (name.equals("children")) {readChildren();}
			else if(name.equals("modhash"))  {this.modhash = _jsonParser.nextTextValue();}
			else if(name.equals("after"))    {this.after = _jsonParser.nextTextValue();} 
			else if(name.equals("before"))   {this.before = _jsonParser.nextTextValue();} 
			
			tok = _jsonParser.nextToken();
		}
	}

	private void readChildren() throws JsonParseException, IOException {
		JsonToken tok = _jsonParser.nextToken();
		if(tok != JsonToken.START_ARRAY) {
			throw new IllegalStateException("Expected children to be an Array, got " + tok.asString());
		}
		Link link = new Link();
		tok = _jsonParser.nextToken();
		
		while(tok != JsonToken.END_ARRAY){
			link.readJson(_jsonParser);
			_listener.OnItem(link);
			tok = _jsonParser.nextToken();
		}
	}
}
