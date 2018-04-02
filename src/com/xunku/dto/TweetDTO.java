package com.xunku.dto;

import java.util.List;

import com.xunku.app.helpers.DateHelper;
import com.xunku.app.interfaces.IAccount;
import com.xunku.app.interfaces.ITweet;

public class TweetDTO {

	public TweetDTO(ITweet post) {
		IAccount acc = post.getAuthor();
		author_head = acc.getHead();
		author_name = acc.getDisplayName();

		comments = post.getComments();
		retweets = post.getReposts();
		created = DateHelper.formatDate(post.getCreated());
		from = post.getFrom().getName();
		images = post.getImages();
		text = post.getText();
		url = post.getUrl();
	}

	public String author_name;
	public String author_head;
	public String url;

	public String text;
	public int retweets;
	public int comments;
	public String created;
	public String from;
	public TweetDTO source;
	public List<String> images;
}
