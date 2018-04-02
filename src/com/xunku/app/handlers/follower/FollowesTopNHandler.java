package com.xunku.app.handlers.follower;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.xunku.app.interfaces.IAccount;
import com.xunku.app.interfaces.IFollowerHandler;

public class FollowesTopNHandler implements IFollowerHandler {

	@Override
	public void process(IAccount follower) {
		if (this.followers.size() < topN) {
			this.add(follower);
		} else {
			IAccount min = this.findMin();
			if (min.getFollowers() < follower.getFollowers()) {
				this.followers.remove(min);
				add(follower);
				//this.followers.add(follower);
			}
		}
	}

	private void add(IAccount follower) {
		boolean is = false;
		for (int i = 0; i < this.followers.size(); i++) {
			if (this.followers.get(i).getUcode().equals(follower.getUcode())) {
				is = true;
			}
		}

		if (!is) {
			this.followers.add(follower);
		}
	}

	final int topN = 10;

	List<IAccount> followers;

	public FollowesTopNHandler() {
		this.followers = new ArrayList<IAccount>();
	}

	public List<IAccount> getTopN() {
		Comparator<IAccount> comparator = new Comparator<IAccount>() {
			public int compare(IAccount s1, IAccount s2) {
				return s2.getFollowers() - s1.getFollowers();
			}
		};
		Collections.sort(this.followers, comparator);
		return this.followers;
	}

	private IAccount findMin() {
		IAccount min = null;
		if (this.followers.size() > 0)
			min = this.followers.get(0);
		for (IAccount acc : this.followers) {
			if (acc.getFollowers() < min.getFollowers()) {
				min = acc;
			}
		}
		return min;
	}

}
