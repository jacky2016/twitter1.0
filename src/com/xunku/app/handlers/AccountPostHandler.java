package com.xunku.app.handlers;

import com.xunku.app.db.AccountDB;
import com.xunku.app.enums.PutResult;
import com.xunku.app.interfaces.IPostHandler;
import com.xunku.app.interfaces.ITweet;
import com.xunku.app.stores.TweetStore;

/**
 * 当帖子入库时,会带Account的信息,此处理器负责处理这些帐号信息
 * <p>
 * 如果帖子有作者则更新作者,如果有引用则检查引用的作者是否需要更新
 * 
 * @author wujian
 * @created on Jun 12, 2014 10:45:06 AM
 */
public class AccountPostHandler implements IPostHandler {

	@Override
	public void initialize(TweetStore db) {
		// TODO Auto-generated method stub

	}

	public AccountPostHandler() {
	}

	@Override
	public void processPost(ITweet post, PutResult pr, TweetStore storeDB) {

		// System.out.println("开始处理博主信息");
		AccountDB db = storeDB.getAccountManager().getDB(post.getPlatform());

		if (post.getAuthor() != null) {

			db.updateAccount(post.getAuthor());

			ITweet source = post.getSource();

			if (source != null && source.getAuthor() != null) {
				db.updateAccount(post.getSource().getAuthor());
			}
		}
		// System.out.println("处理博主信息结束");
	}

	@Override
	public String getName() {
		return "AccountPostHandler";
	}

	@Override
	public void flush(TweetStore db) {
		// TODO Auto-generated method stub
		
	}

}
