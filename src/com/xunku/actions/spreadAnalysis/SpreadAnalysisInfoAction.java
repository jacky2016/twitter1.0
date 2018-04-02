package com.xunku.actions.spreadAnalysis;

import java.util.HashMap;
import java.util.Map;

import com.xunku.actions.ActionBase;
import com.xunku.app.Utility;
import com.xunku.app.interfaces.IAccount;
import com.xunku.app.interfaces.ITweet;
import com.xunku.app.result.SpreadResult;
import com.xunku.cache.Cache;
import com.xunku.cache.CacheManager;
import com.xunku.constant.PortalCST;
import com.xunku.dto.AccountDTO;
import com.xunku.dto.MyPostDTO;
import com.xunku.pojo.base.Account;
import com.xunku.pojo.base.Post;
import com.xunku.pojo.base.User;
import com.xunku.utils.AppServerProxy;

// 点击查看分析按钮 获取微博的详细信息
public class SpreadAnalysisInfoAction extends ActionBase {

	@Override
	public MyPostDTO doAction() {
		String id = this.get("id");
		ITweet post = AppServerProxy.getPostBySpreadId(Integer.parseInt(id));
		MyPostDTO myPostDTO = new MyPostDTO();
		if (post != null) {
			IAccount account = post.getAuthor();
			AccountDTO accountDto = new AccountDTO();
			accountDto.isAjax = false;
			String cityName = "";
			if (account != null) {
				// 从缓存获取城市信息
				Cache cache = CacheManager
						.getCacheInfo(PortalCST.GetCityCacheKey);
				Map<Integer, String> citys = (HashMap<Integer, String>) cache
						.getValue();
				if (citys.containsKey(account.getCity())) {
					cityName = citys.get(account.getCity());
				}
				accountDto.area = cityName + " " + account.getLocation(); // 地点
				if (account.getLargeHead() != null)
					accountDto.imgurl = account.getLargeHead(); // 头像
				accountDto.name = account.getName();// 账号名称
				accountDto.friends = account.getFriends();// 关注数
				accountDto.followers = account.getFollowers();// 粉丝数
			}
			myPostDTO.account = accountDto;// 账号信息
			myPostDTO.text = post.getText();// 内容
			myPostDTO.source = post.getFrom().getName();// 来源
			myPostDTO.commentCount = post.getComments();// 评论数
			myPostDTO.repostCount = post.getReposts();// 转发数
			myPostDTO.url = post.getUrl();// url地址
			myPostDTO.tid = post.getTid(); // 微博id
		}

		return myPostDTO;
	}

}
