package com.maxpolun.redditstream;

import static org.junit.Assert.*;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;


public class TestLinkListing {
	Link testLink;
	@Test
	public void readListing() {
		String testJson = "{\"kind\":\"Listing\"," +
				"\"data\": {\"modhash\":\"1234567890\"," +
				"\"children\":[{" +
				"\"kind\":\"t3\","+
				"\"data\":{" +
					"\"domain\":\"example.com\"," +
					"\"author\":\"maxpolun\"," + 
					"\"id\":\"abcdef\"," + 
					"\"media\":{\"oembed\":{\"title\":\"don't care\"},\"type\":\"don't care\"},"+
					"\"saved\":false"+
				"}}],\"after\":null,\"before\":null" + 
				"}}";
		JsonFactory factory = new JsonFactory();
		try {
			JsonParser jsonParser = factory.createJsonParser(testJson);
			testLink = null;
			LinkListing ll = new LinkListing(jsonParser, new LinkListing.OnItemListener() {
				@Override
				public void OnItem(Link l) {
					testLink = l;
				}
			});
			ll.run();
			assertEquals("Listing", ll.kind);
			assertEquals("1234567890", ll.modhash);
			assertNotNull(testLink);
			assertEquals("t3", testLink.kind);
			assertEquals("example.com", testLink.data.domain);
			assertEquals("maxpolun", testLink.data.author);
			assertEquals("abcdef", testLink.data.id);
			assertNull(testLink.data.media);// we don't want to read media links currently
			assertEquals(false, testLink.data.saved);
		} catch (Exception e) {
			e.printStackTrace();
			fail("expected no exceptions to be thrown");
		}
		
	}
}
