package com.xunku.app.model.accounts;

import java.util.ArrayList;
import java.util.List;

import redis.clients.jedis.Jedis;
import weibo4j.model.User;
import weibo4j.org.json.JSONArray;
import weibo4j.org.json.JSONException;
import weibo4j.org.json.JSONObject;

import com.xunku.app.Utility;
import com.xunku.app.crawl.ItemAccount;
import com.xunku.app.db.AccountManager;
import com.xunku.app.enums.GenderEnum;
import com.xunku.app.enums.Platform;
import com.xunku.app.helpers.LocationHelper;
import com.xunku.app.interfaces.IAccount;
import com.xunku.app.interfaces.ITweet;
import com.xunku.app.model.TweetUrl;
import com.xunku.app.model.location.City;
import com.xunku.app.parser.TweetUrlParserFactory;
import com.xunku.dto.task.TaskTwitterVO;

public class AccountFactory {

	public static City createCity(int pCode, int cityCode, Platform platform) {
		return LocationHelper.getCity(pCode, cityCode, platform);
	}

	public static City createCity(int countryCode, int pCode, int cityCode,
			Platform platform) {
		return LocationHelper.getCity(pCode, cityCode, platform);
	}

	public static IAccount createAccount(ItemAccount iAcc) {

		if (iAcc.getDomainid() == 1) {
			// 新浪
			AccountSina acc = new AccountSina();
			acc.setFollowers(iAcc.getFansCount());
			acc.setFriends(iAcc.getFollowCount());
			if (iAcc.getSex().equals("m")) {
				acc.setGender(GenderEnum.Male);
			} else if (iAcc.getSex().equals("f")) {
				acc.setGender(GenderEnum.Famale);
			} else {
				acc.setGender(GenderEnum.Unknow);
			}

			acc.setHead(iAcc.getImage());
			acc.setLocation(iAcc.getAddr());
			acc.setName(iAcc.getUname());
			acc.setScreenName(iAcc.getUname());
			acc.setStatuses(iAcc.getWeiboCount());
			acc.setUcode(iAcc.getUid());
			acc.setVerified(iAcc.isVerify());
			// ????
			acc.setXunku(false);
			return acc;
		}

		if (iAcc.getDomainid() == 2) {
			// 腾讯
		}
		if (iAcc.getDomainid() == 5) {
			// 人民
		}

		return null;
	}

	public static IAccount createAccount(User user) {
		if (user != null) {
			AccountSina account = new AccountSina();
			account.setUcode(user.getId());
			account.setUid(Long.parseLong(user.getId()));
			if (!Utility.isNullOrEmpty(user.getUserDomain())) {
				account.setDomain(user.getUserDomain());
			}
			account.setName(user.getName());
			account.setScreenName(user.getScreenName());
			account.setProvince(user.getProvince());
			account.setCity(createCity(user.getProvince(), user.getCity(),
					Platform.Sina));
			account.setLocation(user.getLocation());
			account.setDescription(user.getDescription());
			account.setUrl(user.getUrl());
			account.setFollowMe(user.isfollowMe());
			// 性别，m：男、f：女、n：未知
			if (user.getGender().equals("m")) {
				account.setGender(GenderEnum.Male);
				;
			} else if (user.getGender().equals("f")) {
				account.setGender(GenderEnum.Famale);
			} else {
				account.setGender(GenderEnum.Unknow);
			}

			account.setCreatedAt(user.getCreatedAt().getTime());
			account.setFollowers(user.getFollowersCount());
			account.setFriends(user.getFriendsCount());
			account.setStatuses(user.getStatusesCount());
			account.setVerified(user.isVerified());
			account.setHead(user.getProfileImageUrl());
			account.setLargeHead(user.getAvatarLarge());
			// account.setTags(null);
			account.setRLevel(0);// 默认是0级
			return account;
		}
		return null;
	}

	/**
	 * 创建腾讯的账号
	 * 
	 * @param jsonStr
	 * @return
	 */
	public static IAccount createAccount(String jsonStr) {
		JSONObject json;
		try {
			json = new JSONObject(jsonStr);
			if (json != null) {
				AccountTencent acc = new AccountTencent();
				acc.setTimestamp(System.currentTimeMillis());
				acc.setCity(createCity(json.getInt("country_code"), json
						.getInt("province_code"), json.getInt("city_code"),
						Platform.Tencent));

				acc.setIntroduction(json.getString("introduction"));

				acc.setFavourites(json.getInt("favnum"));
				acc.setFollowers(json.getInt("fansnum"));
				acc.setFollowings(json.getInt("idolnum"));
				int sex = json.getInt("sex");
				GenderEnum gender = GenderEnum.Unknow;
				if (sex == 1)
					gender = GenderEnum.Male;
				else if (sex == 2)
					gender = GenderEnum.Famale;
				acc.setGender(gender);
				acc.setHead(json.getString("head"));
				acc.setLocation(json.getString("location"));
				acc.setName(json.getString("name"));
				acc.setNick(json.getString("nick"));
				acc.setRegTime(json.getLong("regtime"));

				JSONArray tags = json.getJSONArray("tag");
				for (int i = 0; i < tags.length(); i++) {
					JSONObject obj = tags.getJSONObject(i);
					acc.addTag(obj.getString("name"));
				}
				acc.setUcode(acc.getName());
				acc.setOpenId(json.getString("openid"));
				acc.setHomePage(json.getString("homepage"));
				acc.setVerified(json.getBoolean("isvip"));
				acc.setTweets(json.getInt("tweetnum"));
				return acc;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public static IAccount createAccount(TaskTwitterVO vo) {
		if (vo != null) {
			String url = vo.getUrl();
			TweetUrl tUrl = TweetUrlParserFactory.createTweetUrl(url);
			Platform platform = Utility.getPlatform(vo.getSourceID());
			if (platform == Platform.Sina) {
				AccountSina acc = new AccountSina();
				acc.setXunku(true);
				acc.setUcode(tUrl.getUcode());
				acc.setHead(vo.getHead());
				// name 和screen_name一样
				acc.setName(vo.getAuthor());
				acc.setScreenName(vo.getAuthor());
				if (Utility.isNumber(tUrl.getUcode())) {
					acc.setUid(Long.parseLong(tUrl.getUcode()));
				}
				acc.setVerified(vo.getVerify() == 1);
				acc.setLocation(vo.getAddress());
				// 这里抛弃了
				return acc;
			}
			if (platform == Platform.Tencent) {
				AccountTencent acc = new AccountTencent();
				acc.setXunku(true);
				acc.setUcode(vo.getAuthorcode());
				acc.setName(acc.getUcode());
				acc.setHead(vo.getHead());
				acc.setNick(vo.getAuthor());
				acc.setVerified(vo.getVerify() == 1);
				acc.setLocation(vo.getAddress());
				// acc.setOpenId(vo.getAuthorcode());
				return acc;
			}

			if (platform == Platform.Renmin) {
				AccountRenmin acc = new AccountRenmin();
				acc.setXunku(true);
				acc.setUcode(vo.getAuthorcode());
				acc.setHead(vo.getHead());
				acc.setName(vo.getAuthor());
				return acc;
			}

		}
		return null;
	}

	public static IAccount createEmpty() {

		IAccount account = new AccountEmpty();
		return account;
	}

	/*
	 * public static IAccount createAccount(ITweet tweet) {
	 * 
	 * 
	 * 
	 * return null; }
	 */

	public static IAccount createAccount(ITweet tweet, AccountManager accManager) {
		return accManager.accountGetByTweet(tweet);
		
	}

	public static IAccount createAccount(String key, Platform platform,
			Jedis jedis) {
		if (platform == Platform.Sina) {
			AccountSina acc = new AccountSina();
			acc.setDescription(jedis.hget(key, "des"));
			acc.setHead(jedis.hget(key, "hed"));
			acc.setLargeHead(jedis.hget(key, "lhed"));
			acc.setLocation(jedis.hget(key, "loc"));
			acc.setName(jedis.hget(key, "name"));
			acc.setUcode(jedis.hget(key, "ucode"));
			acc.setUid(Long.parseLong(jedis.hget(key, "uid")));
			acc.setUrl(jedis.hget(key, "url"));
			int cityCode = Integer.parseInt(jedis.hget(key, "city"));
			int provinceCode = Integer.parseInt(jedis.hget(key, "prov"));
			City city = createCity(provinceCode, cityCode, platform);
			acc.setCity(city);
			acc.setFavourites(Integer.parseInt(jedis.hget(key, "favs")));
			acc.setFollowers(Integer.parseInt(jedis.hget(key, "fols")));
			acc.setVerified(Boolean.parseBoolean(jedis.hget(key, "ver")));
			acc.setFriends(Integer.parseInt(jedis.hget(key, "frds")));
			GenderEnum gender = GenderEnum.Unknow;
			int sex = Integer.parseInt(jedis.hget(key, "sex"));
			if (sex == 1)
				gender = GenderEnum.Male;
			else if (sex == 2)
				gender = GenderEnum.Famale;

			acc.setGender(gender);
			acc.setRLevel(Integer.parseInt(jedis.hget(key, "lel")));
			acc.setTimestamp(Long.parseLong(jedis.hget(key, "tst")));
			acc.setStatuses(Integer.parseInt(jedis.hget(key, "tweets")));

			acc.setTags(jedis.hget(key, "tags"));

			acc.setScreenName(jedis.hget(key, "sn"));
			acc.setDomain(jedis.hget(key, "dm"));
			return acc;
		}

		if (platform == Platform.Tencent) {
			// 腾讯帐号还未实现
			AccountTencent acc = new AccountTencent();

			acc.setIntroduction(jedis.hget(key, "des"));
			acc.setHead(jedis.hget(key, "hed"));
			acc.setLocation(jedis.hget(key, "loc"));
			acc.setName(jedis.hget(key, "name"));
			acc.setUcode(jedis.hget(key, "name"));
			acc.setNick(jedis.hget(key, "nick"));
			acc.setFollowers(Integer.parseInt(jedis.hget(key, "fols")));
			acc.setFollowings(Integer.parseInt(jedis.hget(key, "frds")));
			int sex = Integer.parseInt(jedis.hget(key, "sex"));
			if (sex == 1) {
				acc.setGender(GenderEnum.Male);
			} else if (sex == 2) {
				acc.setGender(GenderEnum.Famale);
			} else
				acc.setGender(GenderEnum.Unknow);
			acc.setTimestamp(Long.parseLong(jedis.hget(key, "tst")));
			acc.setTweets(Integer.parseInt(jedis.hget(key, "tweets")));
			acc.setVerified(Boolean.parseBoolean(jedis.hget(key, "ver")));
			acc.setRegTime(Long.parseLong(jedis.hget(key, "reg")));
			return acc;
		}

		return null;

	}

	public static IAccount createTAccount(JSONObject json) {
		try {
			AccountTencent author = new AccountTencent();
			author.setName(json.getString("name"));
			author.setUcode(author.getName());
			author.setOpenId(json.getString("openid"));
			author.setNick(json.getString("nick"));
			author.setHead(json.getString("head"));
			author.setLocation(json.getString("location"));
			author.setCity(createCity(json.getInt("country_code"), json
					.getInt("province_code"), json.getInt("city_code"),
					Platform.Tencent));
			author.setVerified(json.getBoolean("isvip"));
			return author;
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static List<IAccount> createXKAccounts(String source) {

		try {
			JSONArray json = new JSONArray(source);
			List<IAccount> list = new ArrayList<IAccount>();
			for (int i = 0; i < json.length(); i++) {
				list.add(createXKAccount(json.getJSONObject(i)));
			}
			return list;
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static IAccount createXKAccount(JSONObject json) {
		try {
			if (json != null) {
				int domainid = json.getInt("domainid");

				String sex = json.getString("sex");
				GenderEnum gender = GenderEnum.Unknow;
				if (sex.equals("m"))
					gender = GenderEnum.Male;
				else if (sex.equals("f")) {
					gender = GenderEnum.Famale;
				}

				if (domainid == 1) {
					AccountSina acc = new AccountSina();
					acc.setTimestamp(System.currentTimeMillis());
					acc.setFollowers(json.getInt("fansCount"));
					acc.setFriends(json.getInt("followCount"));
					acc.setGender(gender);
					acc.setName(json.getString("uname"));
					acc.setHead(json.getString("image"));
					acc.setLocation(json.getString("addr"));
					acc.setUcode(json.getString("uid"));// 这里可能有问题
					acc.setVerified(json.getBoolean("verify"));
					acc.setStatuses(json.getInt("weiboCount"));
					return acc;

				} else if (domainid == 2) {
					AccountTencent acc = new AccountTencent();
					acc.setTimestamp(System.currentTimeMillis());
					acc.setFollowers(json.getInt("fansCount"));
					acc.setFollowings(json.getInt("followCount"));
					acc.setGender(gender);
					acc.setName(json.getString("uname"));
					acc.setHead(json.getString("image"));
					acc.setLocation(json.getString("addr"));
					acc.setUcode(json.getString("uid"));// 这里可能有问题
					acc.setVerified(json.getBoolean("verify"));
					acc.setTweets(json.getInt("weiboCount"));
					return acc;
				}

				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static IAccount createXKAccount(String jsonStr) {
		try {
			JSONObject json = new JSONObject(jsonStr);
			return createXKAccount(json);
		} catch (JSONException ex) {
			ex.printStackTrace();
			return null;
		}
	}
}
