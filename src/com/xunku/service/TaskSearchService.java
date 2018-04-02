package com.xunku.service;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.xunku.app.enums.Platform;
import com.xunku.app.helpers.DateHelper;
import com.xunku.constant.TimeSortEnum;
import com.xunku.dao.task.TaskDao;
import com.xunku.daoImpl.task.TaskDaoImpl;
import com.xunku.dto.task.TaskSearchDTO;
import com.xunku.dto.task.TaskTwitterVO;
import com.xunku.pojo.base.Pager;
import com.xunku.pojo.task.Task;
import com.xunku.utils.DateUtils;
import com.xunku.utils.Pagefile;
import com.xunku.utils.PropertiesUtils;
import com.xunku.utils.SearchAllParam;
import com.xunku.utils.Search_QueryStringUtils;
import com.xunku.utils.URLUtils;

public class TaskSearchService {

	private static final Logger LOG = LoggerFactory
			.getLogger(TaskSearchService.class);

	/**
	 * 功能描述<获取转发列表>
	 * 
	 * @author wanghui
	 * @param void
	 * @return Pagefile<TaskTwitterVO>
	 * @version twitter 1.0
	 * @date Jul 25, 20145:34:49 PM
	 */
	public Pagefile<TaskTwitterVO> tweetSearchByRepost(Pager pager,
			String transferurl) {
		Pagefile<TaskTwitterVO> pagefile = null;
		try {
			SearchAllParam searchAllParam = this.buildParam(pager, transferurl);
			return this.goSearch(searchAllParam);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return pagefile;
	}

	/**
	 * 搜索事件数据的微博数据
	 * 
	 * @param query
	 * @param startTime
	 * @param endTime
	 * @param pager
	 * @param platform
	 * @return
	 */
	public Pagefile<TaskTwitterVO> tweetEventSearch(String query,
			Date startTime, Date endTime, Pager pager, int platform) {

		if (query == null) {
			return null;
		}

		Pagefile<TaskTwitterVO> pagefile = null;
		// LOG.info("微博舆情搜索的查询串query：" + query);

		try {
			SearchAllParam searchAllParam = this.buildParam(query, startTime,
					endTime, pager, platform);
			return this.goSearch(searchAllParam);

		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return pagefile;
	}

	/**
	 * 查询指定任务的微博数据
	 * 
	 * @param task
	 * @param platform
	 * @param queryString
	 * @return
	 * @throws ParseException
	 * @throws IOException
	 */
	public Pagefile<TaskTwitterVO> taskSearchService(Task task,
			Platform platform, String queryString, Pager pager, Date thanThis)
			throws ParseException, IOException {
		SearchAllParam searchAllParam = this.buildParam(task, platform,
				queryString, pager, thanThis);
		return this.goSearch(searchAllParam);
	}

	/**
	 * 功能描述<微博舆情按任务搜索>
	 * 
	 * @author wanghui
	 * @param void
	 * @return Pagefile
	 * @version twitter 1.0
	 * @throws
	 * @date May 19, 20144:08:22 PM
	 */
	public Pagefile<TaskTwitterVO> taskSearchService(TaskSearchDTO taskDTO) {
		try {
			SearchAllParam searchAllParam = buildParam(taskDTO);
			return this.goSearch(searchAllParam);

		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	private int getSortType(TimeSortEnum timeSort) {
		int sortType = 0;
		switch (timeSort) {
		case Time:
			sortType = 0;
			break;
		case Similarity:
			sortType = 1;
			break;
		case Transpond:
			sortType = 2;
			break;
		case Comment:
			sortType = 3;
			break;
		default:
			sortType = 0;
			break;
		}
		return sortType;
	}

	private void removeHighlight(Pagefile<TaskTwitterVO> pagefile) {
		// remove highlight
		if (pagefile != null) {
			for (TaskTwitterVO vo : pagefile.getRows()) {
				vo.setContent(vo.getContent().replace(
						"<span class=\"highlight\">", "")
						.replace("</span>", ""));
			}
		}
	}

	/**
	 * 构造获得转发微博的查询参数
	 * 
	 * @param pager
	 * @param transferurl
	 * @return
	 */
	private SearchAllParam buildParam(Pager pager, String transferurl) {
		SearchAllParam searchAllParam = new SearchAllParam();

		String query = "(transferurl:\"" + transferurl + "\")";
		searchAllParam.setQueryString(query);
		searchAllParam.setStart(pager.getPageIndex()); // 查询数据开始数
		searchAllParam.setRows(pager.getPageSize()); // 查询条数
		searchAllParam.setDedup(false); // 是否去重
		searchAllParam.setSortType(0); // 排序类型
		searchAllParam.setNeedsummary(true); // 是否显示摘要
		searchAllParam.setStartTime(null); // 开始时间
		searchAllParam.setDataRange(-1); // 搜索时间范围

		return searchAllParam;
	}

	/**
	 * 构建事件搜索参数
	 * 
	 * @param query
	 * @param startTime
	 * @param endTime
	 * @param pager
	 * @param platform
	 * @return
	 */
	private SearchAllParam buildParam(String query, Date startTime,
			Date endTime, Pager pager, int platform) {

		SearchAllParam searchAllParam = new SearchAllParam();
		int dataRange = DateHelper.beforeDays(startTime, endTime);
		String strStartTime = DateHelper.getQuarter(startTime);
		String strEndTime = DateHelper.getQuarter(endTime);
		// 讯库要求的格式为yyyy-MM-dd，吴剑修改
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");

		String queryString2 = Search_QueryStringUtils.makeSearchQueryString(
				query, strStartTime, strEndTime, platform);
		searchAllParam.setQueryString(queryString2);
		/**
		 * ******************************* 拼接所搜引擎认识的查询串
		 * **********************************************
		 */
		searchAllParam.setStart(pager.getPageIndex()); // 查询数据开始数
		searchAllParam.setRows(pager.getPageSize()); // 查询条数
		searchAllParam.setDedup(false); // 是否去重
		searchAllParam.setSortType(0); // 排序类型
		searchAllParam.setNeedsummary(true); // 是否显示摘要
		searchAllParam.setStartTime(format.format(startTime)); // 开始时间
		searchAllParam.setDataRange(dataRange); // 搜索时间范围
		return searchAllParam;
	}

	private String buildQueryString(int taskid) {
		TaskDao dao = new TaskDaoImpl();
		String queryString = dao.queryTaskWord(taskid);
		String rubbish = dao.getRubbishWords(taskid);
		if (rubbish == null) {
			rubbish = "";
		}
		return queryString + rubbish;
	}

	private SearchAllParam buildParam(Task task, Platform platform,
			String queryString, Pager pager, Date thanThis)
			throws ParseException {

		Date end = new Date();

		// Calendar c = Calendar.getInstance();
		// c.setTime(end);

		// 往前推1天
		// c.add(Calendar.DATE, -1);
		Date start = thanThis;

		SearchAllParam searchAllParam = new SearchAllParam();
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
		String startTime = format.format(start);
		// String endTime = format.format(end); // 搜索结束时间
		searchAllParam.setQueryString(queryString);
		int dataRange = DateUtils.beforDays(start, end);

		String sourceCondition = " AND (SourceID:1 OR SourceID:2 OR SourceID:5)";
		if (platform == Platform.Sina) {
			sourceCondition = "AND (SourceID:1)";
		}
		if (platform == Platform.Tencent) {
			sourceCondition = "AND (SourceID:2)";
		}
		if (platform == Platform.Renmin) {
			sourceCondition = "AND (SourceID:5)";
		}

		searchAllParam.setQueryString(queryString + sourceCondition
				+ " AND (fetchtime:[" + DateHelper.getQuarter(start) + " TO "
				+ DateHelper.getQuarter(end) + "])");

		// LOG.info("微博舆情搜索的查询串query：" + queryString2);
		searchAllParam.setStart(pager.getPageIndex()); // 查询数据开始数
		searchAllParam.setRows(pager.getPageSize()); // 查询条数
		searchAllParam.setDedup(false); // 是否去重
		searchAllParam.setSortType(0); // 按时间排序
		searchAllParam.setNeedsummary(true); // 是否显示摘要
		searchAllParam.setStartTime(startTime); // 开始时间
		searchAllParam.setDataRange(dataRange); // 搜索时间范围
		return searchAllParam;

	}

	private SearchAllParam buildParam(TaskSearchDTO dto) throws ParseException {
		SearchAllParam searchAllParam = new SearchAllParam();
		int sortType = getSortType(dto.timeSort);
		String queryString = null; // 查询串
		String startTime = dto.startTime; // 搜索开始时间
		String endTime = dto.endTime; // 搜索结束时间
		int taskId = dto.taskId;
		if (taskId > 0) {
			searchAllParam.setQueryString(this.buildQueryString(taskId));
		}
		queryString = searchAllParam.getQueryString();
		int dataRange = DateUtils.beforDays(startTime, endTime);
		startTime = DateUtils.StringDateFormat(dto.startTime, "yyyy-MM-dd",
				"yyyyMMdd");
		endTime = DateUtils.StringDateFormat(dto.endTime, "yyyy-MM-dd",
				"yyyyMMdd");
		// 获得搜索引擎认识的查询串
		String queryString2 = Search_QueryStringUtils.makeSearchQueryString(
				queryString, startTime, endTime, dto.type, dto.platform);
		searchAllParam.setQueryString(queryString2);

		LOG.info("微博舆情搜索的查询串query：" + queryString2);
		searchAllParam.setStart(dto.pagerDTO.pageIndex); // 查询数据开始数
		searchAllParam.setRows(dto.pagerDTO.pageSize); // 查询条数
		searchAllParam.setDedup(false); // 是否去重
		searchAllParam.setSortType(sortType); // 排序类型
		searchAllParam.setNeedsummary(true); // 是否显示摘要
		searchAllParam.setStartTime(startTime); // 开始时间
		searchAllParam.setDataRange(dataRange); // 搜索时间范围
		return searchAllParam;
	}

	private Pagefile<TaskTwitterVO> goSearch(SearchAllParam searchAllParam)
			throws IOException {
		java.lang.reflect.Type type = new com.google.gson.reflect.TypeToken<SearchAllParam>() {
		}.getType();
		String param = new Gson().toJson(searchAllParam, type);
		String result = URLUtils.getResponseStr(PropertiesUtils.getString(
				"config", "taskSearchAdress"), param + "&1");
		if (result.equals("null")) {
			return null;
		}
		java.lang.reflect.Type type_twitter = new com.google.gson.reflect.TypeToken<Pagefile<TaskTwitterVO>>() {
		}.getType();
		Pagefile<TaskTwitterVO> pagefile = new Gson().fromJson(result,
				type_twitter);
		this.removeHighlight(pagefile);
		return pagefile;
	}

	public static void main(String[] args) {
		String result = null;
		String[] str = { "青岛", "大连" };
		StringBuffer buf = new StringBuffer();
		buf.append(" -(");
		for (int i = 0; i < str.length; i++) {
			buf.append("content:\"" + str[i] + "\" OR content1:\"" + str[i]
					+ "\") AND -(");
		}
		buf.append(")");
		if (buf.toString().equals("OR")) {
			result = null;
		} else {
			result = buf.toString();
			result = result.substring(0, result.lastIndexOf("AND"));
		}
		System.out.println(result);
	}

}
