package com.xunku.app.db.cache;

import java.util.List;
import java.util.Set;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import com.xunku.app.Utility;
import com.xunku.app.enums.Platform;
import com.xunku.app.interfaces.IAccount;
import com.xunku.app.interfaces.IFollowerHandler;
import com.xunku.app.model.accounts.AccountFactory;
import com.xunku.constant.RedisCST;
import com.xunku.pojo.base.Pager;

/**
 * 所有的redis存储的value都是经过gzip压缩的，key是不压缩的
 * <p>
 * 这里保存账户的基本信息和帐号的关系信息
 * <p>
 * 不分库存储是考虑以后使用集群迁移数据方便，同时减少切换数据库的次数
 * <p>
 * 
 * @author wujian
 * @created on Jun 5, 2014 3:48:27 PM
 */
public class AccountRedisDB {

	// private static Logger LOG =
	// LoggerFactory.getLogger(AccountRedisDB.class);

	/**
	 * 帐号库
	 */
	private final String SUFFIX_ACCOUNT = "account";
	/**
	 * 粉丝库
	 */
	private final String SUFFIX_FOLLOWER = "follower";
	/**
	 * 关注库
	 */
	private final String SUFFIX_FOLLOWING = "following";
	/**
	 * name索引
	 */
	private final String SUFFIX_INDEX_NAME = "index.name";
	/**
	 * uid索引
	 */
	private final String SUFFIX_INDEX_UID = "index.uid";
	/**
	 * 粉丝索引
	 */
	private final String SUFFIX_INDEX_FOLLOWER_REL = "index.rel.follower";
	/**
	 * 关注索引
	 */
	private final String SUFFIX_INDEX_FOLLOWING_REL = "index.rel.following";
	/**
	 * 状态库
	 */
	private final int STATUS_DB = 1;

	public AccountRedisDB(String host, int port, int timeout) {
		JedisPoolConfig jedisconf = new JedisPoolConfig();
		jedisconf.setMaxActive(1000);
		pool = new JedisPool(jedisconf, host, port, timeout);
	}

	// =====================================================
	private JedisPool pool = null;

	/**
	 * 获得指定key的关键字
	 * 
	 * @param suffix
	 * @param key
	 * @param platform
	 * @return
	 */
	private String getKey(String suffix, String key, Platform platform) {
		return platform + "." + key + "." + suffix;
	}

	/**
	 * 获得指定key和relKey的关键字
	 * 
	 * @param suffix
	 * @param key
	 * @param relkey
	 * @param platform
	 * @return
	 */
	private String getRelKey(String suffix, String key, String relkey,
			Platform platform) {
		return platform + "." + key + ".rel." + relkey + "." + suffix;
	}

	private void setIndex(String key, String value) {
		Jedis jedis = null;
		try {
			jedis = pool.getResource();
			jedis.setnx(key, value);
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (jedis != null)
				pool.returnResource(jedis);
		}
	}

	/**
	 * 添加一个name索引,可以通过name找到id
	 * 
	 * @param acc
	 */
	private void setIndexName(IAccount acc) {
		String key = this.getKey(this.SUFFIX_INDEX_NAME, acc.getName(), acc
				.getPlatform());
		this.setIndex(key, acc.getUcode());

	}

	/**
	 * 添加一个帐号，Ucode是Key
	 * 
	 * @param acc
	 */
	private void setAccount(IAccount acc) {
		Jedis jedis = null;
		try {
			jedis = pool.getResource();
			String key = this.getKey(this.SUFFIX_ACCOUNT, acc.getUcode(), acc
					.getPlatform());
			if (jedis.exists(key)) {
				// 避免讯库不完整的用户信息覆盖原来的完整信息
				if (!acc.isXunku()) {
					jedis.hmset(key, acc.toHashMap());
				}
			} else {
				jedis.hmset(key, acc.toHashMap());
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (jedis != null)
				pool.returnResource(jedis);
		}
	}

	private String getIndex(String key) {
		Jedis jedis = null;
		String value = null;
		if (Utility.isNullOrEmpty(key)) {
			return value;
		}
		try {
			jedis = pool.getResource();
			return jedis.get(key);
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (jedis != null)
				pool.returnResource(jedis);
		}
		return value;
	}

	/**
	 * 通过name获得该name对应的ucode
	 * 
	 * @param name
	 * @param platform
	 * @return
	 */
	private String getIndexName(String name, Platform platform) {
		String key = this.getKey(this.SUFFIX_INDEX_NAME, name, platform);
		return this.getIndex(key);
	}

	/**
	 * 通过ucode获得该code对应的uid
	 * 
	 * @param ucode
	 * @param platform
	 * @return
	 */
	private String getIndexUID(String uid, Platform platform) {
		String key = this.getKey(this.SUFFIX_INDEX_UID, uid, platform);
		return this.getIndex(key);
	}

	private boolean getIndexRel(String suffix, String ucode, Platform platform,
			String rel) {

		Jedis jedis = null;
		try {
			jedis = pool.getResource();
			String key = this.getRelKey(suffix, ucode, rel, platform);
			return jedis.exists(key);

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (jedis != null)
				pool.returnResource(jedis);
		}

		return false;

	}

	private void setIndexRel(String suffix, String ucode, Platform platform,
			String follower, long timestamp) {
		Jedis jedis = null;
		try {
			jedis = pool.getResource();
			String key = this.getRelKey(suffix, ucode, follower, platform);
			// 关系创建的时间
			/* String.valueOf(System.currentTimeMillis()) */
			// 该key就是用来检查关系是否存在的
			jedis.set(key, String.valueOf(timestamp));

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (jedis != null)
				pool.returnResource(jedis);
		}
	}

	private Set<String> getFriendships(String suffix, String ucode,
			Platform platform, Pager pager) {

		Jedis jedis = null;
		Set<String> followers = null;
		try {
			jedis = pool.getResource();
			String key = this.getKey(suffix, ucode, platform);
			followers = jedis.zrange(key, pager.getPageIndex(), pager
					.getPageSize());
			pager.setRealCnt(jedis.zcount(key, 0, System.currentTimeMillis()));
			return followers;

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (jedis != null)
				pool.returnResource(jedis);
		}

		return null;
	}

	private void putRelationships(String suffix, String ucode,
			Platform platform, List<String> rels) {

		Jedis jedis = null;
		try {
			jedis = pool.getResource();
			String[] values = rels.toArray(new String[rels.size()]);
			String key = this.getKey(suffix, ucode, platform);
			if (values != null && values.length > 0) {
				for (String value : values) {
					jedis.zadd(key, System.currentTimeMillis(), value);
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (jedis != null)
				pool.returnResource(jedis);
		}
	}

	private void putRelationship(String suffix, String ucode,
			Platform platform, String rel, long timestamp) {
		Jedis jedis = null;
		try {
			jedis = pool.getResource();
			String key = this.getKey(suffix, ucode, platform);
			jedis.zadd(key, timestamp, rel);
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (jedis != null)
				pool.returnResource(jedis);
		}
	}

	// =====================================================

	// ======================= 粉丝 ====================

	/**
	 * 判断follower是否是ucode的粉丝
	 * 
	 * @param ucode
	 * @param follower
	 * @param platform
	 * @return
	 */
	public boolean checkFollower(String ucode, String follower,
			Platform platform) {

		return this.getIndexRel(this.SUFFIX_INDEX_FOLLOWER_REL, ucode,
				platform, follower);

	}

	/**
	 * 判断following是否关注了ucode
	 * 
	 * @param ucode
	 * @param following
	 * @param platform
	 * @return
	 */
	public boolean checkFollowing(String ucode, String following,
			Platform platform) {
		return this.getIndexRel(this.SUFFIX_INDEX_FOLLOWING_REL, ucode,
				platform, following);
	}

	/**
	 * 通过ucode获得该用户的粉丝列表
	 * 
	 * @param uid
	 * @param platform
	 * @return
	 */
	public Set<String> getFollowers(String ucode, Platform platform, Pager pager) {
		return this
				.getFriendships(this.SUFFIX_FOLLOWER, ucode, platform, pager);
	}

	/**
	 * 处理粉丝
	 * 
	 * @param ucode
	 *            谁的粉丝
	 * @param platform
	 *            平台
	 * @param handlers
	 *            粉丝处理器
	 */
	public void processFollowers(String ucode, Platform platform,
			List<IFollowerHandler> handlers) {

		int pageSize = 100;
		Pager pager = Pager.createPager(0, pageSize);
		processFollower(ucode, platform, handlers, pager);
		int cnt = (int) pager.getRealCnt();

		int pageCount = cnt / pageSize + 1;

		for (int i = 1; i < pageCount; i++) {
			pager.setPageIndex(i);
			processFollower(ucode, platform, handlers, pager);
		}

	}

	private void processFollower(String ucode, Platform platform,
			List<IFollowerHandler> handlers, Pager pager) {
		Set<String> ucodes = this.getFollowers(ucode, platform, pager);
		for (String fcode : ucodes) {
			IAccount acc = this.getAccount(fcode, platform);
			for (IFollowerHandler handler : handlers) {
				handler.process(acc);
			}
		}
	}

	/**
	 * 获得ucode的粉丝列表
	 * 
	 * @param ucode
	 * @param platform
	 * @param pager
	 * @return
	 */
	public Set<String> getFollowings(String ucode, Platform platform,
			Pager pager) {
		return this.getFriendships(this.SUFFIX_FOLLOWING, ucode, platform,
				pager);

	}

	/**
	 * 保存粉丝关系集合
	 * 
	 * @param uid
	 * @param platform
	 * @param followers
	 */
	public void putFollowers(String ucode, Platform platform,
			List<String> followers) {

		this.putRelationships(this.SUFFIX_FOLLOWER, ucode, platform, followers);
		for (String follower : followers) {
			this.setIndexRel(this.SUFFIX_INDEX_FOLLOWER_REL, ucode, platform,
					follower, System.currentTimeMillis());
		}
	}

	/**
	 * 保存粉丝关系
	 * 
	 * @param ucode
	 * @param platform
	 * @param follower
	 */
	public void putFollower(String ucode, Platform platform, String follower,
			long timestamp) {
		// 建关系
		this.putRelationship(this.SUFFIX_FOLLOWER, ucode, platform, follower,
				timestamp);
		// 建关系索引
		this.setIndexRel(this.SUFFIX_INDEX_FOLLOWER_REL, ucode, platform,
				follower, timestamp);
	}

	/**
	 * 保存关注关系
	 * 
	 * @param ucode
	 * @param paltform
	 * @param friend
	 */
	public void putFollowing(String ucode, Platform platform, String friend,
			long timestamp) {
		this.putRelationship(this.SUFFIX_FOLLOWING, ucode, platform, friend,
				timestamp);
		this.setIndexRel(this.SUFFIX_INDEX_FOLLOWING_REL, ucode, platform,
				friend, timestamp);
	}

	/**
	 * 保存关注关系集合
	 * 
	 * @param ucode
	 * @param platform
	 * @param friends
	 */
	public void putFollowings(String ucode, Platform platform,
			List<String> friends) {
		this.putRelationships(this.SUFFIX_FOLLOWING, ucode, platform, friends);

		for (String friend : friends) {
			this.setIndexRel(this.SUFFIX_INDEX_FOLLOWING_REL, ucode, platform,
					friend, System.currentTimeMillis());
		}
	}

	// ===================== 帐号 =======================

	/**
	 * 通过ucode和平台信息获得帐号信息
	 * 
	 * @param ucode
	 * @param platform
	 * @return
	 */
	public IAccount getAccount(String ucode, Platform platform) {
		Jedis jedis = null;
		try {
			jedis = pool.getResource();
			String key = this.getKey(this.SUFFIX_ACCOUNT, ucode, platform);
			if (jedis.exists(key)) {

				IAccount acc = AccountFactory.createAccount(key, platform,
						jedis);
				return acc;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (jedis != null)
				pool.returnResource(jedis);
		}
		return null;
	}

	/**
	 * 通过uid在缓存里找帐号
	 * 
	 * @param uid
	 * @param platform
	 * @return
	 */
	public IAccount getAccountByUid(String uid, Platform platform) {
		String ucode = this.getIndexUID(uid, platform);

		if (ucode == null) {
			// LOG.info("平台[{}]的帐号，通过uid:[{}]在索引里无法找到对应的UID.", platform, uid);
			return null;
		}

		return this.getAccount(ucode, platform);
	}

	/**
	 * 通过名称在缓存里找帐号
	 * 
	 * @param name
	 * @param platform
	 * @return
	 */
	public IAccount getAccountByName(String name, Platform platform) {
		String ucode = this.getIndexName(name, platform);

		if (ucode == null) {
			// LOG.info("平台[{}]的帐号，通过name:[{}]在索引里无法找到对应的UID.", platform, name);
			return null;
		}

		return this.getAccount(ucode, platform);
	}

	/**
	 * 添加帐号缓存，保存帐号，建立名称、UID索引
	 * 
	 * @param acc
	 */
	public void addAccount(IAccount acc) {
		this.setAccount(acc);
		this.setIndexName(acc);
	}

	/**
	 * 更新帐号信息，更新内容：
	 * <p>
	 * 名称、粉丝数、微博数、关注数、头像信息（大头像）、描述等信息
	 * 
	 * @param acc
	 */
	public void updateAccount(IAccount acc) {

		// 目前只处理新浪的帐号
		if (acc.getPlatform() == Platform.Sina) {
			this.setAccount(acc);

			if (this.getIndexName(acc.getName(), acc.getPlatform()) == null) {
				this.setIndexName(acc);
			}
		}
	}

	/**
	 * 设置redis上的状态
	 * 
	 * @param name
	 * @param value
	 */
	public void setStatus(String name, String value) {
		Jedis jedis = null;
		try {
			jedis = pool.getResource();
			jedis.select(this.STATUS_DB);
			jedis.set(name, value);
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (jedis != null)
				pool.returnResource(jedis);
		}
	}

	/**
	 * 获得状态值
	 * 
	 * @param name
	 * @return
	 */
	public String getStatus(String name) {
		Jedis jedis = null;
		try {
			jedis = pool.getResource();
			jedis.select(this.STATUS_DB);
			return jedis.get(name);
		} catch (Exception ex) {
			ex.printStackTrace();
			return "";
		} finally {
			if (jedis != null)
				pool.returnResource(jedis);
		}
	}

	/**
	 * redis服务器是否已经加载数据
	 * 
	 * @return
	 */
	public boolean redisLoaded() {
		return this.getStatus(RedisCST.FLAG_ACCOUNT_FINISHED).equalsIgnoreCase(
				"true");
	}

	public void reset() {
		this.setStatus(RedisCST.FLAG_ACCOUNT_FINISHED, "false");
	}

	public JedisPool getPool() {
		return this.pool;
	}

	public boolean isReadly() {
		return true;
	}
}
