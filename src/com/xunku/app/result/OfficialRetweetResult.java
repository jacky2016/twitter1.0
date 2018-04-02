package com.xunku.app.result;

import java.util.Map;

public class OfficialRetweetResult {
	Map<Long, Integer> _retweets;
	Map<Long, Integer> _comments;

	public OfficialRetweetResult(Map<Long, Integer> retweets,
			Map<Long, Integer> comments) {
		this._comments = comments;
		this._retweets = retweets;
	}

	public Map<Long, Integer> get_retweets() {
		return _retweets;
	}

	public void set_retweets(Map<Long, Integer> _retweets) {
		this._retweets = _retweets;
	}

	public Map<Long, Integer> get_comments() {
		return _comments;
	}

	public void set_comments(Map<Long, Integer> _comments) {
		this._comments = _comments;
	}
}
