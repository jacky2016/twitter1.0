package com.xunku.daoImpl.my;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.xunku.app.enums.Platform;
import com.xunku.app.interfaces.ITweet;
import com.xunku.app.model.tweets.TweetFactory;
import com.xunku.constant.IdentityType;
import com.xunku.constant.PortalCST;
import com.xunku.constant.SendTypeByMeEnum;
import com.xunku.constant.WeiboType;
import com.xunku.dao.my.PostDao;
import com.xunku.dto.PagerDTO;
import com.xunku.dto.myTwitter.DataAnalyzeDTO;
import com.xunku.utils.DatabaseUtils;
import com.xunku.utils.DateUtils;
import com.xunku.utils.Pagefile;

public class PostDaoImpl implements PostDao {
    @Override
    public Pagefile<ITweet> queryAtMePost(PagerDTO dto, IdentityType type,
	    Platform form, String searchkey) {
	String whereSql = null;
	if (searchkey == null || searchkey.length() == 0) {
	    searchkey = "";
	}
	switch (type) {
	case ALL:
	    break;
	case ATTENTION:
	    whereSql = "where friend=1 and text like '%" + searchkey + "%'";
	    break;
	case VERIFIED:
	    whereSql = "where vip=1 and text like '%" + searchkey + "%'";
	    break;
	case COMMENTATOR:
	    whereSql = "where navies=1 and text like '%" + searchkey + "%'";
	    break;
	default:
	    break;
	}
	switch (form) {
	case Sina:
	    whereSql = whereSql + " and platform=1";
	    break;
	case Tencent:
	    whereSql = whereSql + " and platform=2";
	    break;
	case Renmin:
	    whereSql = whereSql + " and platform=5";
	    break;
	default:
	    break;
	}
	Connection conn = null;
	CallableStatement cstmt = null;
	ResultSet rs = null;
	ResultSet rs2 = null;
	Pagefile<ITweet> pagefile = null;
	try {
	    conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME)
		    .getConnection();
	    cstmt = conn.prepareCall("{call SupesoftPage(?,?,?,?,?,?,?)}");
	    cstmt.setString(1, "vi_MyPost_queryAtMePost");
	    cstmt.setString(2, "*");
	    cstmt.setInt(3, dto.pageSize);
	    cstmt.setInt(4, dto.pageIndex);
	    cstmt.setString(5, whereSql.toString());
	    cstmt.setString(6, "id");
	    cstmt.setInt(7, 0);
	    cstmt.execute();
	    pagefile = new Pagefile<ITweet>();
	    rs = cstmt.getResultSet();
	    while (rs.next()) {
		ITweet p = TweetFactory.createTweet(rs);
		pagefile.getRows().add(p);
	    }
	    if (cstmt.getMoreResults()) {
		rs2 = cstmt.getResultSet();
		if (rs2.next()) {
		    pagefile.setRealcount(rs2.getInt("RecordCount"));
		}
	    }
	} catch (SQLException e) {
	    e.printStackTrace();
	} finally {
	    this.CloseStatus(rs2, rs, cstmt, conn);
	}
	return pagefile;
    }

    @Override
    public Pagefile<ITweet> commentPostByMe(PagerDTO dto, String ucode,
	    SendTypeByMeEnum sendType) {
	String whereSql = null;
	switch (sendType) {
	case SendToMe:
	    whereSql = "where ucode='" + ucode + "' and type=3";
	    break;
	case SendToOther:
	    whereSql = "where ucode!='" + ucode + "' and type=3";
	    break;
	default:
	    whereSql = "where ucode='" + ucode + "' and type=3";
	    break;
	}
	Connection conn = null;
	CallableStatement cstmt = null;
	ResultSet rs = null;
	ResultSet rs2 = null;
	Pagefile<ITweet> pagefile = null;
	try {
	    conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME)
		    .getConnection();
	    cstmt = conn.prepareCall("{call SupesoftPage(?,?,?,?,?,?,?)}");
	    cstmt.setString(1, "My_Posts");
	    cstmt.setString(2, "*");
	    cstmt.setInt(3, dto.pageSize);
	    cstmt.setInt(4, dto.pageIndex);
	    cstmt.setString(5, whereSql);
	    cstmt.setString(6, "id");
	    cstmt.setInt(7, 0);
	    cstmt.execute();
	    pagefile = new Pagefile<ITweet>();
	    rs = cstmt.getResultSet();
	    while (rs.next()) {
		ITweet p = TweetFactory.createTweet(rs);
		//p.setSource(this.getITweet(conn, p.getSourceTweet()));
		pagefile.getRows().add(p);
	    }
	    if (cstmt.getMoreResults()) {
		rs2 = cstmt.getResultSet();
		if (rs2.next()) {
		    pagefile.setRealcount(rs2.getInt("RecordCount"));
		}
	    }
	} catch (SQLException e) {
	    e.printStackTrace();
	} finally {
	    this.CloseStatus(rs2, rs, cstmt, conn);
	}
	return pagefile;
    }

    private ITweet getITweet(Connection conn,String source){
	String sql = "select * from My_Posts where tid=?";
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	ITweet tweet = null;
	try {
	    pstmt = conn.prepareStatement(sql);
	    pstmt.setString(1, source);
	    rs = pstmt.executeQuery();
	    while(rs.next()){
		tweet = TweetFactory.createTweet(rs);
	    }
	} catch (SQLException e) {
	    e.printStackTrace();
	}finally{
	    this.CloseStatus(rs, pstmt, null);
	}
	return tweet;
    }
    @Override
    public void deleteCommentPost(String tid, int platform) {
	Connection conn = null;
	PreparedStatement pstmt = null;
	try {
	    conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME)
		    .getConnection();
	    String sql = "DELETE FROM My_Posts WHERE tid=? and platform=?";
	    pstmt = conn.prepareStatement(sql);
	    pstmt.setString(1, tid);
	    pstmt.setInt(2, platform);
	    pstmt.execute();
	} catch (SQLException e) {
	    e.printStackTrace();
	} finally {
	    this.CloseStatus(pstmt, conn);
	}
    }

    /*@Override
    public Pagefile<PostDealDTO> queryPostDeal(PagerDTO dto, String startTime,
	    String endTime, String ucode, int customId) {
	Connection conn = null;
	CallableStatement cstmt = null;
	ResultSet rs = null;
	ResultSet rs2 = null;
	Pagefile<PostDealDTO> pagefile = null;
	try {
	    conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME)
		    .getConnection();
	    cstmt = conn.prepareCall("{call SupesoftPage(?,?,?,?,?,?,?)}");
	    cstmt.setString(1, "vi_Post_queryPostDeal");
	    cstmt.setString(2,
		    "ID,CustomID,UserName,NickName,ucode,uid,platform");
	    cstmt.setInt(3, dto.pageSize);
	    cstmt.setInt(4, dto.pageIndex);
	    cstmt.setString(5, "where CustomID=" + customId + " and ucode="
		    + ucode);
	    cstmt.setString(6, "ID");
	    cstmt.setInt(7, 0);
	    cstmt.execute();
	    pagefile = new Pagefile<PostDealDTO>();
	    rs = cstmt.getResultSet();
	    while (rs.next()) {
		PostDealDTO pdeal = new PostDealDTO();
		pdeal.setId(rs.getInt("ID"));
		pdeal.setUseName(rs.getString("NickName"));
		pdeal.setUid(rs.getString("uid"));
		pdeal.setUcode(rs.getString("ucode"));
		int reposts = this.getDealRepostsCount(conn, startTime,
			endTime, ucode);
		pdeal.setReposts(reposts);
		int comments = this.getDealCommentCount(conn, startTime,
			endTime, ucode);
		pdeal.setComments(comments);
		pagefile.getRows().add(pdeal);
	    }
	    if (cstmt.getMoreResults()) {
		rs2 = cstmt.getResultSet();
		if (rs2.next()) {
		    pagefile.setRealcount(rs2.getInt("RecordCount"));
		}
	    }
	} catch (SQLException e) {
	    e.printStackTrace();
	} finally {
	    this.CloseStatus(rs2, rs, cstmt, conn);
	}
	return pagefile;
    }*/

    private int getDealRepostsCount(Connection conn, String startTime,
	    String endTime, String ucode) {

	String sql = "select COUNT(id) as reposts from My_Posts where ucode=? and created between ? and ? and type=2";
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	int reposts = 0;
	try {
	    pstmt = conn.prepareStatement(sql);
	    pstmt.setString(1, ucode);
	    pstmt.setString(2, startTime);
	    pstmt.setString(3, endTime);
	    rs = pstmt.executeQuery();
	    if (rs.next()) {
		reposts = rs.getInt("reposts");
	    }
	} catch (SQLException e) {
	    e.printStackTrace();
	} finally {
	    this.CloseStatus(rs, pstmt, null);
	}
	return reposts;
    }

    private int getDealCommentCount(Connection conn, String startTime,
	    String endTime, String ucode) {

	String sql = "select COUNT(id) as comments from My_Posts where ucode=? and created between ? and ? and type=3";
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	int comments = 0;
	try {
	    pstmt = conn.prepareStatement(sql);
	    pstmt.setString(1, ucode);
	    pstmt.setString(2, startTime);
	    pstmt.setString(3, endTime);
	    rs = pstmt.executeQuery();
	    if (rs.next()) {
		comments = rs.getInt("comments");
	    }
	} catch (SQLException e) {
	    e.printStackTrace();
	} finally {
	    this.CloseStatus(rs, pstmt, null);
	}
	return comments;
    }

    // //////////////////////////////////////////////////////////////////////////

    @Override
    public void deleteById(int pid) {
	Connection conn = null;
	CallableStatement cstmt = null;
	try {
	    conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME)
		    .getConnection();
	    cstmt = conn.prepareCall("{call sp_Post_deleteById(?)}");
	    cstmt.setInt(1, pid);
	    cstmt.executeUpdate();
	} catch (SQLException e) {
	    e.printStackTrace();
	} finally {
	    this.CloseStatus(cstmt, conn);
	}
    }

    @Override
    public void checkPost(int postId, int approved) {
	Connection conn = null;
	PreparedStatement pstmt = null;
	try {
	    conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME)
		    .getConnection();
	    String sql = "UPDATE My_Posts SET Approved=? WHERE ID=?";
	    pstmt = conn.prepareStatement(sql);
	    pstmt.setInt(1, approved);
	    pstmt.setInt(2, postId);
	    pstmt.executeUpdate();
	} catch (SQLException e) {
	    e.printStackTrace();
	} finally {
	    this.CloseStatus(pstmt, conn);
	}
    }

    @Override
    public List<DataAnalyzeDTO> queryDataByPTime(String startTime,
	    String endTime, String uid, int day) {
	Connection conn = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	List<DataAnalyzeDTO> list = new ArrayList<DataAnalyzeDTO>();
	try {
	    conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME)
		    .getConnection();
	    String sql = "select weibos from My_Publish_An where uid=? and sampling between ? and ?";

	    List<String> datelist = DateUtils.getDateAnalyze(startTime,
		    endTime, day);
	    for (String date : datelist) {
		DataAnalyzeDTO dto = new DataAnalyzeDTO();
		String sTime = date + " 00:00:00";
		String eTime = date + " 23:59:59";
		pstmt = conn.prepareStatement(sql);
		pstmt.setString(1, uid);
		pstmt.setString(2, sTime);
		pstmt.setString(3, eTime);
		rs = pstmt.executeQuery();
		int count = 0;
		if (rs.next()) {
		    count = rs.getInt("weibos");
		}
		dto.setStartTime(sTime);
		dto.setEndTime(eTime);
		dto.setPublishCount(count);
		list.add(dto);
	    }
	} catch (SQLException e) {
	    e.printStackTrace();
	} catch (ParseException e) {
	    e.printStackTrace();
	} finally {
	    this.CloseStatus(rs, pstmt, conn);
	}
	return list;
    }

    @Override
    public Map<String, List<Integer>> queryDataByTimeQuantum(String startTime,
	    String endTime, String uid) {
	startTime = startTime + " 00:00:00";
	endTime = endTime + " 23:59:59";
	Connection conn = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	Map<String, List<Integer>> map = new HashMap<String, List<Integer>>();
	try {
	    conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME)
		    .getConnection();
	    String sql = "select weibos from My_Publish_An where uid=? and sampling between ? and ?";
	    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH");
	    Date startDate = format.parse(startTime);
	    Date endDate = format.parse(endTime);
	    List<String> datelist = new ArrayList<String>();
	    while (!startDate.after(endDate)) {
		String stime = format.format(startDate);
		Calendar cd = Calendar.getInstance();
		cd.setTime(startDate);
		cd.add(Calendar.HOUR_OF_DAY, 1);// 增加一小时
		startDate = cd.getTime();
		datelist.add(stime);
	    }

	    for (String date : datelist) {
		String sTime = date + ":00";
		String eTime = date + ":59";
		pstmt = conn.prepareStatement(sql);
		pstmt.setString(1, uid);
		pstmt.setString(2, sTime);
		pstmt.setString(3, eTime);
		rs = pstmt.executeQuery();
		int count = 0;
		if (rs.next()) {
		    count = rs.getInt("weibos");
		}
		String quantum = date.substring(date.length() - 2, date
			.length());
		if (map.containsKey(quantum)) {
		    map.get(quantum).add(count);
		} else {
		    List<Integer> list = new ArrayList<Integer>();
		    list.add(count);
		    map.put(quantum, list);
		}
	    }

	} catch (SQLException e) {
	    e.printStackTrace();
	} catch (ParseException e) {
	    e.printStackTrace();
	} finally {
	    this.CloseStatus(rs, pstmt, conn);
	}
	return map;
    }

    @Override
    public List<DataAnalyzeDTO> queryDataByRepostAnalyze(String startTime,
	    String endTime, String uid, int day) {
	Connection conn = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	List<DataAnalyzeDTO> list = new ArrayList<DataAnalyzeDTO>();
	try {
	    conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME)
		    .getConnection();
	    String sql = "select count(id) as count from My_Posts where type = 2 and uid=? and created between ? and ?";
	    List<String> datelist = DateUtils.getDateAnalyze(startTime,
		    endTime, day);
	    for (String date : datelist) {
		DataAnalyzeDTO dto = new DataAnalyzeDTO();
		String sTime = date + " 00:00:00";
		String eTime = date + " 23:59:59";
		pstmt = conn.prepareStatement(sql);
		pstmt.setString(1, uid);
		pstmt.setString(2, sTime);
		pstmt.setString(3, eTime);
		rs = pstmt.executeQuery();
		int count = 0;
		if (rs.next()) {
		    count = rs.getInt("Count");
		}
		dto.setStartTime(sTime);
		dto.setEndTime(eTime);
		dto.setPublishCount(count);
		list.add(dto);
	    }
	} catch (SQLException e) {
	    e.printStackTrace();
	} catch (ParseException e) {
	    e.printStackTrace();
	} finally {
	    this.CloseStatus(rs, pstmt, conn);
	}
	return list;
    }

    @Override
    public List<DataAnalyzeDTO> queryDataByAtMe(String startTime,
	    String endTime, int aid, int day) {
	Connection conn = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	List<DataAnalyzeDTO> list = new ArrayList<DataAnalyzeDTO>();
	try {
	    conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME)
		    .getConnection();
	    String sql = "select value from My_Mention_An where uid=? and sampling between ? and ?";

	    List<String> datelist = DateUtils.getDateAnalyze(startTime,
		    endTime, day);
	    for (String date : datelist) {
		DataAnalyzeDTO dto = new DataAnalyzeDTO();
		String sTime = date + " 00:00:00";
		String eTime = date + " 23:59:59";
		pstmt = conn.prepareStatement(sql);
		pstmt.setInt(1, aid);
		pstmt.setString(2, sTime);
		pstmt.setString(3, eTime);
		rs = pstmt.executeQuery();
		int count = 0;
		if (rs.next()) {
		    count = rs.getInt("value");
		}
		dto.setStartTime(sTime);
		dto.setEndTime(eTime);
		dto.setPublishCount(count);
		list.add(dto);
	    }
	} catch (SQLException e) {
	    e.printStackTrace();
	} catch (ParseException e) {
	    e.printStackTrace();
	} finally {
	    this.CloseStatus(rs, pstmt, conn);
	}
	return list;
    }

    @Override
    public Pagefile<ITweet> queryRepostsPost(WeiboType type, String startTime,
	    String endTime, String ucode, PagerDTO dto) {
	String whereSql = "";
	switch (type) {
	case RepostPost:
	    whereSql = "where ucode=" + ucode + " and created between '"
		    + startTime + "' and '" + endTime + "' and type=2";
	    break;
	case CommentPost:
	    whereSql = "where ucode=" + ucode + " and created between '"
		    + startTime + "' and '" + endTime + "' and type=3";
	    break;
	default:
	    break;
	}
	Connection conn = null;
	CallableStatement cstmt = null;
	ResultSet rs = null;
	ResultSet rs2 = null;
	Pagefile<ITweet> pagefile = null;
	try {
	    conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME)
		    .getConnection();
	    cstmt = conn.prepareCall("{call SupesoftPage(?,?,?,?,?,?,?)}");
	    cstmt.setString(1, "vi_mypost_commentPostByMe");
	    cstmt.setString(2, "*");
	    cstmt.setInt(3, dto.pageSize);
	    cstmt.setInt(4, dto.pageIndex);
	    cstmt.setString(5, whereSql);
	    cstmt.setString(6, "id");
	    cstmt.setInt(7, 0);
	    cstmt.execute();
	    pagefile = new Pagefile<ITweet>();
	    rs = cstmt.getResultSet();
	    while (rs.next()) {
		ITweet p = TweetFactory.createTweet(rs);
		//p.setSource(this.getITweet(conn, p.getSourceTweet()));
		pagefile.getRows().add(p);
	    }
	    if (cstmt.getMoreResults()) {
		rs2 = cstmt.getResultSet();
		if (rs2.next()) {
		    pagefile.setRealcount(rs2.getInt("RecordCount"));
		}
	    }
	} catch (SQLException e) {
	    e.printStackTrace();
	} finally {
	    this.CloseStatus(rs2, rs, cstmt, conn);
	}
	return pagefile;
    }

    private void CloseStatus(ResultSet rs, PreparedStatement pstmt,
	    Connection conn) {
	this.CloseStatus(null, null, rs, null, pstmt, conn);
    }

    private void CloseStatus(CallableStatement cstmt, Connection conn) {
	this.CloseStatus(null, null, cstmt, conn);
    }

    private void CloseStatus(PreparedStatement pstmt, Connection conn) {
	this.CloseStatus(null, pstmt, conn);
    }

    private void CloseStatus(ResultSet rs2, ResultSet rs,
	    CallableStatement cstmt, Connection conn) {
	this.CloseStatus(null, rs2, rs, cstmt, null, conn);
    }

    private void CloseStatus(ResultSet rs3, ResultSet rs2, ResultSet rs,
	    CallableStatement cstmt, PreparedStatement pstmt, Connection conn) {

	try {
	    if (rs3 != null) {
		rs3.close();
	    }
	    if (rs2 != null) {
		rs2.close();
	    }
	    if (rs != null) {
		rs.close();
	    }
	    if (pstmt != null) {
		pstmt.close();
	    }
	    if (cstmt != null) {
		cstmt.close();
	    }
	    if (conn != null) {
		conn.close();
	    }
	} catch (SQLException e) {
	    e.printStackTrace();
	}
    }

    @Override
    public ITweet queryWeiboById(int id) {
	Connection conn = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	ITweet p = null;
	try {
	    conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME)
		    .getConnection();
	    String sql = "SELECT * FROM My_Posts WHERE id=?";
	    pstmt = conn.prepareStatement(sql);
	    pstmt.setInt(1, id);
	    rs = pstmt.executeQuery();
	    while (rs.next()) {
		p = TweetFactory.createTweet(rs);
	    }
	} catch (SQLException e) {
	    e.printStackTrace();
	} finally {
	    this.CloseStatus(rs, pstmt, conn);
	}
	return p;
    }
}
