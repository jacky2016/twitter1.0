package com.xunku.portal;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.xunku.api.strategy.APIManager;
import org.xunku.api.strategy.CheckResult;
import org.xunku.api.strategy.DataHandler;

import com.xunku.app.Utility;

import weibo4j.Business;
import weibo4j.model.Status;
import weibo4j.model.StatusWapper;

/**
 * 提供新浪微博商业搜索接口
 * http://open.weibo.com/wiki/C/2/search/statuses/limited
 * @author MuDebao
 * retcode:
 * 1001		参数错误
 * 1002		指定客户没权限使用该api
 * 1003		指定客户当前小时内api调用次数已达上限
 * 1004		指定客户当前小时内api搜索数据量已达上限
 * 1005		指定客户api搜索数据量已达上限
 *servelet params:
 *customerid		讯库微博系统的客户id，用于根据客户控制调用策略
 *access_token		token
 *q					搜索的关键词
 *starttime			搜索范围起始时间，取值为时间戳，单位为秒。System.currentTimeMillis()/1000
 *endtime			搜索范围结束时间，取值为时间戳，单位为秒。
 *page				页码，默认为1。
 *count				每页返回的数量，默认50，最大50。（默认返回50条）
 */
public class WeiboBusinessSearchServlet extends HttpServlet{

	/**
	 * 
	 */
	private static final long serialVersionUID = 9159425084485227287L;

	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		req.setCharacterEncoding("UTF-8");
		int customerid= Integer.parseInt(req.getParameter("customerid"));
		String access_token= req.getParameter("access_token");
		String q= req.getParameter("q");
		long starttime=Long.parseLong(req.getParameter("starttime"));
		long endtime= Long.parseLong(req.getParameter("endtime"));
		int page= 1;
		long count= 50;
		if(req.getParameter("page")!=null){
			page=Integer.parseInt(req.getParameter("page"));
		}
		if(req.getParameter("count")!=null){
			count=Long.parseLong(req.getParameter("count"));
		}
		count=Math.abs(count);
		page=Math.abs(page);
		count=Math.min(count, 50);
		if(starttime>endtime){
			outputResponse(CheckResult.ParamError().toString(),resp);
			return;
		}
		String data="";
		//检查调用策略
		CheckResult check= APIManager.check(customerid);
		if(check.getRecCode()==1000){
			//调用api，记录log
			Business b = new Business();
			b.setToken(access_token);
			try {
				//String q, Long starttime, Long endtime,
				//int pageIndex, long pageSize
				int dup=check.isDuplicated()?1:0;
				int antispam=check.isAntispam()?1:0;
				StatusWapper wapper = b.searchLimited(q, starttime, endtime,
						page, count,dup,antispam);
				//更新log
				int recordcount=wapper.getStatuses().size();
				APIManager.addAPILog(customerid, recordcount);
				data=wapper.getJson();//Utility.toJSON(wapper);
				DataHandler.saveSearchResult(customerid, q, wapper.getJson(), recordcount);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		else{
			data=check.toString();
		}
		//输出
		outputResponse(data,resp);
	}
	private void outputResponse(String data,HttpServletResponse resp){
		resp.setCharacterEncoding("UTF-8");
		try {
			PrintWriter writer = resp.getWriter();
			writer.print(data);
			writer.flush();
			writer.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
	
}
