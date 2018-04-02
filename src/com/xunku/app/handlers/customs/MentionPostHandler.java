package com.xunku.app.handlers.customs;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.xunku.app.AppContext;
import com.xunku.app.Utility;
import com.xunku.app.db.AccountManager;
import com.xunku.app.enums.Platform;
import com.xunku.app.enums.PutResult;
import com.xunku.app.interfaces.IAccount;
import com.xunku.app.interfaces.IPostHandler;
import com.xunku.app.interfaces.ITweet;
import com.xunku.app.model.Pooling;
import com.xunku.app.model.accounts.AccountSina;
import com.xunku.app.stores.TweetStore;
import com.xunku.pojo.office.NavieAccount;

/**
 * Mention@我的帖子处理器,入库@我的帖子时需要插入My_Mention表,该处理器就负责插入数据
 * 
 * @author wujian
 * @created on Jun 11, 2014 2:09:36 PM
 */
public class MentionPostHandler implements IPostHandler {

	public static final String SQL_OFFICIAL_MENTION = "offical.mention.sql";
	
	private List<NavieAccount> navieList = null;

	@Override
	public void initialize(TweetStore db) {

	}

	private AppContext _context = null;

	public MentionPostHandler(AppContext context, List<NavieAccount> navies) {
		_context = context;
		this.navieList = navies;

	}

	private boolean isNavie(String uid) {
		for (NavieAccount acc : this.navieList) {
			if (acc.getUid().equals(uid)) {
				return true;
			}
		}
		return false;
	}

	private AccountManager getAccountDB() {
		return _context.getAccountManager();
	}

	private String normalizeText(String text) {
		// 如果@前面没有空格则添加一个空格
		// 目前是给所有的@前面添加一个空格，这个字符串后面还得来个空格...
		return text.replace("@", " @") + " ";
	}

	private List<String> getMentionedUser(String str) {

		String text = this.normalizeText(str);
		Pattern p = Pattern.compile("\\@(.*?)([\\:]|[\\：]|\\s)");
		List<String> nameList = new ArrayList<String>();
		Matcher m = p.matcher(text);
		while (m.find()) {
			nameList.add(m.group(1));
		}
		return nameList;
	}

	private void put(ITweet post, String uid, Pooling pool) {
		Connection conn = null;
		CallableStatement cstmt = null;
		try {
			conn = pool.getConnection();
			// 插入My_Mention表数据
			// 执行sys_put_My_Mention进行插入
			boolean vip = false;
			boolean friend = false;
			boolean navies = false;
			cstmt = conn
					.prepareCall("{call sys_put_My_Mention(?,?,?,?,?,?,?,?,?,?,?)}");
			cstmt.setString(1, post.getTid());
			cstmt.setString(2, uid);
			cstmt.setInt(3, Utility.getPlatform(post.getPlatform()));
			IAccount author = post.getAuthor();
			if (author != null) {

				if (author.getPlatform() == Platform.Sina) {
					AccountSina acc = (AccountSina) author;
					vip = acc.isVerified();
					// TODO 检查是否是水军，需要调用水军API来实现
					navies = this.isNavie(acc.getUcode());
					// 是不是我关注的人提到了我？uid是我，author是发微博的人
					// 检查我是否关注了这个发微博的人，看看uid是不是author的粉丝
					friend = acc.getFollowMe();
				}
			}
			cstmt.setBoolean(4, vip);
			cstmt.setBoolean(5, friend);
			cstmt.setBoolean(6, navies);
			cstmt.setLong(7, post.getCreated());
			if (author != null) {
				cstmt.setString(8, author.getUcode());
			} else {
				cstmt.setString(8, null);// 作者被删除的时候这里就是null
			}
			cstmt.setString(9, post.getText());
			cstmt.setInt(10, Utility.getPostType(post.getType()));
			boolean hasImg = true;
			if (post.getImages() == null || post.getImages().size() == 0) {
				hasImg = false;
			}
			cstmt.setBoolean(11, hasImg);
			cstmt.execute();

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	public void processPost(ITweet post, PutResult pr, TweetStore storeDB) {
		Pooling pool = storeDB.getPool();
		if (post != null) {
			List<String> mentionUserList = this
					.getMentionedUser(post.getText());
			for (String name : mentionUserList) {
				IAccount acc = this.getAccountDB().accountGetByName(name,
						post.getPlatform());

				if (acc != null) {
					this.put(post, acc.getUcode(), pool);
				} else {
					// 如果为null说明库里没有，是否要调用api获得？
				}
			}

		}
	}

	@Override
	public String getName() {
		return this.getClass().getName();
	}

	@Override
	public void flush(TweetStore db) {
		// TODO Auto-generated method stub
		
	}
}
