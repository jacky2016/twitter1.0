package com.xunku.app.model.tweets;

import java.util.List;

import com.xunku.app.enums.Platform;
import com.xunku.app.enums.PostType;
import com.xunku.app.interfaces.IAccount;
import com.xunku.app.interfaces.ITweet;
import com.xunku.app.interfaces.ITweetStore;
import com.xunku.app.model.TweetFrom;
import com.xunku.app.model.accounts.AccountEmpty;

public class TweetEmpty implements ITweet {

	@Override
	public IAccount getAuthor() {
		return new AccountEmpty();
	}

	@Override
	public int getComments() {
		return 0;
	}

	@Override
	public long getCreated() {
		return System.currentTimeMillis();
	}

	@Override
	public TweetFrom getFrom() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getHomeTimeline() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getId() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<String> getImages() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getLayer() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public ITweet getOriginalTweet() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Platform getPlatform() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getReposts() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public ITweet getSource() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getText() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getTid() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PostType getType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getUcode() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getUid() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getUrl() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setAuthor(IAccount acc) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setSource(ITweet source) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setStore(ITweetStore store) {
		// TODO Auto-generated method stub

	}

	@Override
	public String toJSON() {
		// TODO Auto-generated method stub
		return null;
	}

}
