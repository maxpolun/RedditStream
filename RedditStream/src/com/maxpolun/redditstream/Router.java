package com.maxpolun.redditstream;
/**
 * @author Max Polun
 * generates the routes used used in the rest of the package
 */
public class Router {
	public static String loginRoute(User u) {
		return "https://ssl.reddit.com/api/login" + u.name;
	}
	public static String submitRoute() {
		return "http://www.reddit.com/api/submit";
	}
	public static String subredditRoute(String subreddit) {
		return "http://www.reddit.com/r/" + subreddit + "/.json";
	}
	public static String userRoute(User u) {
		return "http://www.reddit.com/user/" + u.name + "/about.json";
	}
	public static String linkByIdRoute(String kind, String id) {
		return linkByIdRoute(kind + "_" + id);
	}
	public static String linkByIdRoute(String fullname) {
		return "http://www.reddit.com/by_id/" + fullname + ".json";
	}
	public static String commentsRoute(String id) {
		return "http://www.reddit.com/comments/"+id+".json";
	}
	public static String nextLinkRoute(String subreddit, int count, String linkId) {
		return submitRoute() + "&count=" + Integer.toString(count) + "&after=" + linkId;
	}
	public static String nextCommentRoute(String linkId, String commentId){
		return "http://reddit.com/comments/"+linkId+"/_/"+commentId+".json";
	}
	public static String voteRoute() {
		return "http://www.reddit.com/api/vote";
	}

}
