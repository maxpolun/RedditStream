package com.maxpolun.redditstream;

public abstract class Thing {
	public String id;
	public String name;
	public String kind;
	
	// attributes for votable
	public Integer ups;
	public Integer downs;
	public Boolean likes;
	
	// attributes for created
	public Long created;
	public Long created_utc;
}
