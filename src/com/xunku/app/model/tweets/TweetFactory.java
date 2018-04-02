package com.xunku.app.model.tweets;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import weibo4j.model.Comment;
import weibo4j.model.Source;
import weibo4j.model.Status;
import weibo4j.org.json.JSONArray;
import weibo4j.org.json.JSONException;
import weibo4j.org.json.JSONObject;

import com.xunku.app.AppContext;
import com.xunku.app.Utility;
import com.xunku.app.crawl.ItemTweet;
import com.xunku.app.db.AccountManager;
import com.xunku.app.enums.Platform;
import com.xunku.app.enums.PostType;
import com.xunku.app.helpers.SourceHelper;
import com.xunku.app.interfaces.IAccount;
import com.xunku.app.interfaces.ITweet;
import com.xunku.app.interfaces.ITweetStore;
import com.xunku.app.model.TweetFrom;
import com.xunku.app.model.TweetUrl;
import com.xunku.app.model.accounts.AccountFactory;
import com.xunku.app.model.people.PUser;
import com.xunku.app.model.people.Pweet;
import com.xunku.app.parser.TweetUrlParserFactory;
import com.xunku.constant.PortalCST;
import com.xunku.dto.task.TaskTwitterVO;
import com.xunku.pojo.base.Post;
import com.xunku.utils.DateUtils;
import com.xunku.utils.StringUtils;

public class TweetFactory {

	private static final Logger LOG = LoggerFactory
			.getLogger(TweetFactory.class);

	/**
	 * 通过微博正文计算当前微博的转发层级
	 * 
	 * @param status
	 * @return
	 */
	private static int calcLayer(ITweet post) {
		if (post.getType() == PostType.Creative
				|| post.getType() == PostType.Comment) {
			return 0;
		}

		String text = post.getText();
		Platform platform = post.getPlatform();
		if (post.getType() == PostType.Repost) {
			int level = 0;
			if (Utility.isNullOrEmpty(text))
				return level;

			if (platform == Platform.Sina)
				level += text.split("//@").length;
			if (platform == Platform.Tencent)
				level += text.split("||").length;
			if (platform == Platform.Renmin)
				level += text.split("//").length;

			return level;
		}

		return 0;

	}

	/**
	 * 创建来源对象（通过ID）
	 * 
	 * @param fromId
	 * @return
	 */
	public static TweetFrom createFrom(int fromId) {
		return SourceHelper.getSource(fromId);
	}

	/**
	 * 创建来源对象（新浪）
	 * 
	 * @param status
	 * @return
	 */
	public static TweetFrom createFrom(Status status) {
		if (status.getSource() != null) {
			Source source = status.getSource();
			TweetFrom from = SourceHelper.addSource(1, source.getName(), source
					.getUrl());
			return from;
		}
		return TweetFrom.Empty();
	}

	/**
	 * 创建来源对象（腾讯）从JSON字符串创建
	 * 
	 * @param jsonStr
	 * @return
	 */
	public static TweetFrom createFrom(String jsonStr) {
		TweetFrom from = new TweetFrom();
		return from;
	}

	public static PUser createPeopleUser(JSONObject obj) {

		if (obj == null)
			return null;

		try {
			PUser user = new PUser();
			int gender = obj.getInt("gender");
			user.setGender(gender);
			user.setUserId(obj.getLong("userId"));
			user.setHeadUrl(obj.getString("profileImageUrl"));
			user.setLocation(obj.getString("location"));
			user.setPersonUrl(obj.getString("personUrl"));
			user.setNickName(obj.getString("nickName"));
			return user;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	public static Pweet createPeopleTweet(String jsonStr) {
		try {
			JSONObject json = new JSONObject(jsonStr);
			JSONObject r = json.getJSONObject("result");
			if (r == null) {
				return null;
			}
			JSONArray datas = r.getJSONArray("data");
			if (datas == null) {
				return null;
			}

			if (datas.length() <= 0)
				return null;
			JSONObject data = datas.getJSONObject(0);

			Pweet result = new Pweet();
			result.setBody(data.getString("contentBody"));
			result.setId(data.getLong("contentId"));
			result.setPostTime(data.getLong("postTime"));
			result.setSource(data.getString("source"));

			// 设置人民的ucode
			result.setUser(createPeopleUser(data.getJSONObject("user")));
			return result;

		} catch (JSONException e) {
			LOG.error("创建人民微博对象出错.", e);
			return null;
		}
	}

	/**
	 * 创建原始（来源）微博
	 * 
	 * @param status
	 * @return
	 */
	public static ITweet createdSource(Status status) {
		ITweet post = null;
		if (status.getRetweetedStatus() != null) {
			post = createTweet(status.getRetweetedStatus());
		}
		return post;
	}

	public static ITweet createTweet4Home(Status status, String timeline) {
		Post post = createTweet(status);
		post.setHomeTimeline(timeline);
		return post;
	}

	public static Post createTweet(ItemTweet item, ITweetStore store) {
		try {
			Post post = new Post();
			post.setStore(store);
			post.setComments(item.getComtCount());
			SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
			post.setCreated(format.parse(item.getPostDate()).getTime());
			post.setReposts(item.getRelayCount());
			String text = item.getContent();

			if (Utility.isNullOrEmpty(text)) {
				text = "(表情或者图片)";
			}
			post.setText(text);
			post.setFrom(createFrom((int) item.getAppsource()));
			TweetUrl tu = TweetUrlParserFactory.createTweetUrl(item.getUrl());

			if (tu == null) {
				LOG.info("无法获得该项对应的微博");
				return null;
			}
			post.setTid(tu.getTid());
			post.setPlatform(tu.getPlatform());

			if (item.getSource() != null) {
				post.setType(PostType.Repost);
				TweetUrl sourceTu = TweetUrlParserFactory.createTweetUrl(item
						.getTransferUrl());
				if (sourceTu != null) {
					Post source = TweetFactory.createTweet(item.getSource(),
							store);
					source.setTid(sourceTu.getTid());
					// bug fix...
					// source.setSourceId(sourceTu.getTid());
					source.setPlatform(post.getPlatform());
					source.setText(item.getRelayContent());
					source.setUcode(sourceTu.getUcode());
					source.setStore(store);
					post.setSource(source);
					// post.setOriginalTweet(source);

				} else {
					LOG.info("分析原始微博出错..." + item.getTransferUrl());
				}

			} else {
				post.setType(PostType.Creative);
			}

			post.setLayer(calcLayer(post));
			// 新浪的uid是ucode，腾讯的ucode是ucode
			post.setUcode(item.getUid());
			return post;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;

	}

	/**
	 * 通过人民微博对象创建一个Tweet对象
	 * 
	 * @param pweet
	 * @return
	 */
	public static Post createTweet(Pweet pweet) {
		if (pweet == null)
			return null;
		Post post = new Post();
		post.setTid(String.valueOf(pweet.getId()));
		post.setCreated(pweet.getPostTime());
		post.setPlatform(Platform.Renmin);
		return post;
	}

	/**
	 * 创建Tweet对象（新浪）
	 * 
	 * @param status
	 * @return
	 */
	public static Post createTweet(Status status) {
		if (status != null) {
			try {
				Post post = new Post();
				post.setComments(status.getCommentsCount());
				post.setReposts(status.getRepostsCount());
				post.setCreated(status.getCreatedAt().getTime());
				post.setFrom(createFrom(status));
				post.setPlatform(Platform.Sina);
				// 微博构造器的原始微博和引用微博是同一个
				post.setSource(createdSource(status));
				post.setOriginalTweet(post.getSource());
				post.setText(status.getText());
				post.setTid(status.getId());
				post.setfromXK(false);
				post.setImages(status.getPics());
				if (status.getUser() != null) {
					post.setAuthor(AccountFactory.createAccount(status
							.getUser()));
					if (post.getAuthor() != null) {
						post.setUcode(post.getAuthor().getUcode());
						// 如果有domain则取domain，否则取id·
						// post.ucode = post.author.getUcode();
						// post.uid = post.author.getUid();
					} else {
						// 无博主信息jhrrrrrr
					}
				} else {
					// 此微博已经被删除
					return null;
				}
				if (status.getRetweetedStatus() != null) {
					// 此微博是转发微博
					post.setType(PostType.Repost);
				} else {
					post.setType(PostType.Creative);
				}
				post.setLayer(calcLayer(post));
				return post;
			} catch (Exception ex) {
				ex.printStackTrace();
				return null;
			}

		}
		return null;
	}

	/**
	 * 创建微博对象（新浪）通过评论创建
	 * 
	 * @param comment
	 * @return
	 */
	public static ITweet createCommentTweet(Comment comment) {
		if (comment != null) {
			Post post = new Post();
			post.setfromXK(false);
			if (comment.getUser() == null) {
				// 此评论已经被删除
				return null;
			} else {
				post.setAuthor(AccountFactory.createAccount(comment.getUser()));
				post.setUcode(comment.getUser().getId());
			}
			// 评论类型
			post.setType(PostType.Comment);

			post.setComments(0);// 评论无回复
			post.setReposts(0); // 评论无转发
			post.setCreated(comment.getCreatedAt().getTime());
			post.setFrom(null); // 评论无来源
			post.setPlatform(Platform.Sina);
			post.setLayer(0);// 评论无层级
			// 这个评论的引用评论，如果有
			ITweet ref = TweetFactory.createCommentTweet((comment
					.getReplycomment()));
			post.setSource(ref);

			// 这个评论对应的原始微博，一定有，艹的，原始微博还有可能有原始微博...
			ITweet orgTweet = TweetFactory.createTweet(comment.getStatus());
			post.setOriginalTweet(orgTweet);

			// 如果引用是null，则这个评论应该是评论的微博而不是回复
			if (ref == null) {
				post.setSource(orgTweet);
			}

			comment.getReplycomment();

			post.setText(comment.getText());
			post.setTid(comment.getIdstr());

			return post;
		}

		return null;
	}

	private static ITweet createVoFromRefContent(String content, String ucode,
			String url, Platform platform) {
		TaskTwitterVO svo;
		try {
			svo = StringUtils.parseRefcontent(content, ucode, url, platform);
			ITweet source = TweetFactory.createTweet(svo);
			return source;
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

	public static ITweet createTweet(TaskTwitterVO vo) {
		if (vo != null) {
			Post post = new Post();
			post.setfromXK(true);// 这条微博从讯库来的
			post.setComments(vo.getComtcount());
			post.setContentid(vo.getContentid());
			post.setCreated(DateUtils.stringToDate(PortalCST.FORMET_WEIBO_TIME,
					vo.getPublishdate()).getTime());
			post.setFrom(createFrom((int) vo.getAppsource()));
			post.setImages(vo.getImgurl());
			post.setPlatform(Utility.getPlatform(vo.getSourceID()));
			post.setReposts(vo.getReplycount());
			post.setText(vo.getContent());
			String sourceUrl = vo.getTransferurl();
			if (Utility.isNullOrEmpty(sourceUrl)) {
				post.setType(PostType.Creative);
			} else {
				post.setType(PostType.Repost);
				try {

					TweetUrl tweetUrl = TweetUrlParserFactory
							.createTweetUrl(sourceUrl);
					ITweet source = createVoFromRefContent(vo.getRefcontent(),
							tweetUrl.getUcode(), vo.getTransferurl(), post
									.getPlatform());
					post.setSource(source);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if (vo.getUrl() == null) {
				System.out.println("xxx");
			}

			// add by wanghui
			IAccount acc = AccountFactory.createAccount(vo);
			// 这个作者从讯库来的
			post.setAuthor(acc);

			TweetUrl tweetUrl = TweetUrlParserFactory.createTweetUrl(vo
					.getUrl());
			if (tweetUrl == null) {
				LOG.error("这个Url[" + vo.getUrl() + "]无法分析");
				return null;
			}
			post.setUcode(tweetUrl.getUcode());
			post.setTid(tweetUrl.getTid());
			post.setLayer(calcLayer(post));
			return post;
		}
		return null;
	}

	public static ITweet createTweet(String jsonStr) {
		Post post = null;
		try {
			JSONObject json = new JSONObject(jsonStr);
			int type = json.getInt("type");
			// 只处理，原创、转发、评论三种类型
			if (type != 1 || type != 2 || type != 7) {
				return null;
			}
			post = new Post();
			String text = json.getString("text");
			post.setCreated(json.getLong("timestamp"));
			post.setTid(json.getString("id"));
			post.setComments(json.getInt("mcount"));
			post.setFrom(createFrom(json.getString("from")));
			post.setPlatform(Platform.Tencent);

			/*
			 * String images = json.getString(""); post.images = null;
			 */
			post.setReposts(json.getInt("count"));
			if (type == 2 || type == 7) {
				post.setSource(createTweet(json.getString("source")));
				if (type == 2)
					post.setType(PostType.Repost);
				else if (type == 7)
					post.setType(PostType.Comment);
				else if (type == 1)
					post.setType(PostType.Creative);
			}
			post.setText(text);

			// 根据post上的用户信息创建用户
			post.setAuthor(AccountFactory.createTAccount(json));
			post.setLayer(calcLayer(post));
			return post;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return post;
	}

	public static ITweet createTweet(ResultSet rs) {
		AccountManager manager = AppContext.getInstance().getAccountManager();
		return createTweet(rs, manager);
	}

	public static ITweet createTweet(ResultSet rs, AccountManager accManager) {
		try {
			Post p = new Post();
			p.setComments(rs.getInt("comments"));

			p.setCreated(rs.getLong("created"));
			p.setFrom(createFrom(rs.getInt("xfrom")));
			p.setId(rs.getInt("id"));
			p.setImages(Utility.getImageList(rs.getString("images")));
			p.setLayer(rs.getInt("layer"));
			p.setPlatform(Utility.getPlatform(rs.getInt("platform")));
			p.setReposts(rs.getInt("reposts"));
			if (!Utility.isNullOrEmpty(rs.getString("source"))) {
				// 设置引用对象
				// 转发源只有一个Tid，输出时通过tid来构造转发源对象
				p.setSourceId(rs.getString("source"));
			}

			if (!Utility.isNullOrEmpty(rs.getString("sourceid"))) {
				// 设置原始微博
				p.setOrgTweetId(rs.getString("sourceid"));
			}
			p.setText(rs.getString("text"));
			p.setTid(rs.getString("tid"));
			p.setType(Utility.getPostType(rs.getInt("type")));

			p.setUcode(rs.getString("ucode"));
			// 设置微博的作者
			p.setAuthor(AccountFactory.createAccount(p, accManager));
			return p;

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
}
