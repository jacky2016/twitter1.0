package com.xunku.daoImpl.task;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.xunku.app.AppContext;
import com.xunku.app.Utility;
import com.xunku.app.enums.Platform;
import com.xunku.app.interfaces.ITweet;
import com.xunku.app.model.accounts.AccountFactory;
import com.xunku.app.model.tweets.TweetFactory;
import com.xunku.constant.DealStatusEnum;
import com.xunku.constant.PortalCST;
import com.xunku.dao.task.CollectionDao;
import com.xunku.dto.PagerDTO;
import com.xunku.pojo.base.Post;
import com.xunku.pojo.task.Collection;
import com.xunku.utils.DatabaseUtils;
import com.xunku.utils.Pagefile;

public class CollectionDaoImpl implements CollectionDao {

    @Override
    public int insert(Collection c) {
	Connection conn = null;
	CallableStatement cstmt = null;
	int cid = 0;
	try {
	    conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME)
		    .getConnection();
	    cstmt = conn
		    .prepareCall("{call sp_Collection_insert(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}");
	    cstmt.setInt(1, c.getGroupID());
	    cstmt.setInt(2, c.getCreator());
	    cstmt.setString(3, c.getPost().getUid());
	    cstmt.setString(4, c.getPost().getUcode());
	    cstmt.setInt(5, Utility.getPlatform(c.getPost().getPlatform()));
	    cstmt.setString(6, c.getPost().getTid());
	    cstmt.setInt(7, Utility.getPostType(c.getPost().getType()));
	    cstmt.setString(8, c.getPost().getText());
	    cstmt.setLong(9, c.getPost().getCreated());
	    cstmt.setInt(10, c.getPost().getFrom().getId());
	    cstmt.setInt(11, c.getPost().getReposts());
	    cstmt.setInt(12, c.getPost().getComments());
	    cstmt.setString(13, Utility.getImageList(c.getPost().getImages()));
	    cstmt.setString(14, c.getPost().getUrl());
	    cstmt.setInt(15, c.getPost().getLayer());
	    if (c.getPost().getSource() != null) {
		cstmt.setString(16, c.getPost().getSource().getTid());
	    } else {
		cstmt.setString(16, null);
	    }
	    if (c.getPost().getSource() == null) {
		cstmt.setString(17, null);
		cstmt.setString(18, null);
		cstmt.setInt(19, 0);
		cstmt.setString(20, null);
		cstmt.setInt(21, 0);
		cstmt.setString(22, null);
		cstmt.setString(23, null);
		cstmt.setString(24, null);
		cstmt.setInt(25, 0);
		cstmt.setInt(26, 0);
		cstmt.setString(27, null);
		cstmt.setString(28, null);
		cstmt.setInt(29, 0);
	    } else {
		cstmt.setString(17, c.getPost().getSource().getUid());
		cstmt.setString(18, c.getPost().getSource().getUcode());
		cstmt.setInt(19, Utility.getPlatform(c.getPost().getSource()
			.getPlatform()));
		cstmt.setString(20, c.getPost().getSource().getTid());
		cstmt.setInt(21, Utility.getPostType(c.getPost().getSource()
			.getType()));
		cstmt.setString(22, c.getPost().getSource().getText());
		cstmt.setLong(23, c.getPost().getSource().getCreated());
		cstmt.setInt(24, c.getPost().getSource().getFrom().getId());
		cstmt.setInt(25, c.getPost().getSource().getReposts());
		cstmt.setInt(26, c.getPost().getSource().getComments());
		cstmt.setString(27, Utility.getImageList(c.getPost()
			.getSource().getImages()));
		cstmt.setString(28, c.getPost().getSource().getUrl());
		cstmt.setInt(29, c.getPost().getSource().getLayer());
	    }
	    cstmt.registerOutParameter(30, java.sql.Types.INTEGER);
	    cstmt.execute();
	    cid = cstmt.getInt(30);
	} catch (SQLException e) {
	    e.printStackTrace();
	} finally {
	    this.CloseStatus(cstmt, conn);
	}
	return cid;
    }

    @Override
    public Pagefile<Collection> queryWeiboDeal(DealStatusEnum status,
	    String startTime, String endTime, Platform platform, int gid,
	    PagerDTO dto) {
	startTime = startTime + " 00:00:00";
	endTime = endTime + " 23:59:59";
	StringBuffer buf = new StringBuffer();
	switch (status) {
	case DEALLING:
	    buf.append("where Status=0 and CreateTime between '" + startTime
		    + "' and '" + endTime + "' and GroupID=" + gid);
	    break;
	case DEALLED:
	    buf.append("where Status=1 and CreateTime between '" + startTime
		    + "' and '" + endTime + "' and GroupID=" + gid);
	    break;
	case NODEAL:
	    buf.append("where Status=2 and CreateTime between '" + startTime
		    + "' and '" + endTime + "' and GroupID=" + gid);
	    break;
	default:
	    buf.append("where Status=0 and CreateTime between '" + startTime
		    + "' and '" + endTime + "' and GroupID=" + gid);
	    break;
	}
	Connection conn = null;
	CallableStatement cstmt = null;
	ResultSet rs = null;
	ResultSet rs2 = null;
	Pagefile<Collection> pagefile = null;
	try {
	    conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME)
		    .getConnection();
	    cstmt = conn.prepareCall("{call SupesoftPage(?,?,?,?,?,?,?)}");
	    cstmt.setString(1, "Base_Collections");
	    cstmt.setString(2, "*");
	    cstmt.setInt(3, dto.pageSize);
	    cstmt.setInt(4, dto.pageIndex);
	    cstmt.setString(5, buf.toString());
	    cstmt.setString(6, "id");
	    cstmt.setInt(7, 1);
	    cstmt.execute();
	    rs = cstmt.getResultSet();
	    pagefile = new Pagefile<Collection>();
	    while (rs.next()) {
		Collection collection = new Collection();
		collection.setId(rs.getInt("ID"));
		collection.setGroupID(gid);
		collection.setStatus(rs.getInt("Status"));
		collection.setPostId(rs.getInt("PostID"));
		collection.setProcessor(rs.getInt("Processor"));
		collection.setPost(this.getITweet(conn, collection.getPostId(),
			status, platform));
		pagefile.getRows().add(collection);
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

    private ITweet getITweet(Connection conn, int postid,
	    DealStatusEnum status, Platform platform) {
	StringBuffer buf = new StringBuffer();
	switch (status) {
	case DEALLING:
	    buf.append("select * from Base_Collection_Posts where id=? ");
	    break;
	case DEALLED:
	    buf.append("select * from Base_Collection_Posts where id=? ");
	    break;
	case NODEAL:
	    buf.append("select * from Base_Collection_Posts where id=? ");
	    break;
	default:
	    buf.append("select * from Base_Collection_Posts where id=? ");
	    break;
	}
	switch (platform) {
	case Sina:
	    buf.append("and platform=" + Utility.getPlatform(platform));
	    break;
	case Tencent:
	    buf.append("and platform=" + Utility.getPlatform(platform));
	    break;
	case Renmin:
	    buf.append("and platform=" + Utility.getPlatform(platform));
	    break;
	default:
	    break;
	}
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	ITweet tweet = null;
	try {
	    pstmt = conn.prepareStatement(buf.toString());
	    pstmt.setInt(1, postid);
	    rs = pstmt.executeQuery();
	    while (rs.next()) {
		Post post = this.getITweet(rs);
		if (post.getSourceId() != null) {
		    post.setSource(this.getITweet(conn, post.getSourceId(),
			    post.getPlatform()));
		}
		tweet = post;
	    }
	} catch (SQLException e) {
	    e.printStackTrace();
	} finally {
	    this.CloseStatus(rs, pstmt, null);
	}
	return tweet;
    }

    private Post getITweet(ResultSet rs) throws SQLException {
	Post p = new Post();
	p.setComments(rs.getInt("comments"));

	p.setCreated(rs.getLong("created"));
	p.setFrom(TweetFactory.createFrom(rs.getInt("xfrom")));
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
	p.setAuthor(AccountFactory.createAccount(p, AppContext.getInstance()
		.getAccountManager()));
	return p;
    }

    private ITweet getITweet(Connection conn, String tid, Platform platform) {

	PreparedStatement pstmt = null;
	ResultSet rs = null;
	ITweet tweet = null;
	try {
	    pstmt = conn
		    .prepareStatement("select * from Base_Collection_Posts where tid=? and platform=?");
	    pstmt.setString(1, tid);
	    pstmt.setInt(2, Utility.getPlatform(platform));
	    rs = pstmt.executeQuery();
	    while (rs.next()) {
		tweet = this.getITweet(rs);
	    }
	} catch (SQLException e) {
	    e.printStackTrace();
	} finally {
	    this.CloseStatus(rs, pstmt, null);
	}
	return tweet;
    }

    @Override
    public boolean updateStatus(int cid, int status) {
	Connection conn = null;
	PreparedStatement pstmt = null;
	boolean isUpdate = false;
	try {
	    conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME)
		    .getConnection();
	    String sql = "UPDATE Base_Collections SET Status=? WHERE ID=?";
	    pstmt = conn.prepareStatement(sql);
	    pstmt.setInt(1, status);
	    pstmt.setInt(2, cid);
	    int flag = pstmt.executeUpdate();
	    if (flag > 0) {
		isUpdate = true;
	    }
	} catch (SQLException e) {
	    e.printStackTrace();
	} finally {
	    this.CloseStatus(pstmt, conn);
	}
	return isUpdate;
    }

    @Override
    public ITweet queryCollectionPost(String tid, int platform) {
	Connection conn = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	ITweet p = null;
	try {
	    conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME)
		    .getConnection();
	    String sql = "SELECT * FROM Base_Collection_Posts WHERE tid=? AND platform=?";
	    pstmt = conn.prepareStatement(sql);
	    pstmt.setString(1, tid);
	    pstmt.setInt(2, platform);
	    rs = pstmt.executeQuery();
	    while (rs.next()) {
		p = TweetFactory.createTweet(rs);
		// p.setSource(this.getITweet(conn,
		// p.getOriginalTweet().getTid(),Platform.get));
	    }
	} catch (SQLException e) {
	    e.printStackTrace();
	} finally {
	    this.CloseStatus(rs, pstmt, conn);
	}
	return p;
    }

    @Override
    public boolean deleteByCid(int cid) {
	Connection conn = null;
	CallableStatement cstmt = null;
	boolean isDelete = false;
	try {
	    conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME)
		    .getConnection();
	    cstmt = conn.prepareCall("{call sp_Collection_deleteByCid(?)}");
	    cstmt.setInt(1, cid);
	    int flag = cstmt.executeUpdate();
	    if (flag > 0) {
		isDelete = true;
	    }
	} catch (SQLException e) {
	    e.printStackTrace();
	} finally {
	    this.CloseStatus(cstmt, conn);
	}
	return isDelete;
    }

    @Override
    public Map<String, Integer> checkCollectionPostStatus(List<String> tids,
	    int customid) {
	Connection conn = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	Map<String, Integer> map = new HashMap<String, Integer>();
	try {
	    conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME)
		    .getConnection();
	    String sql = "SELECT a.PostID FROM Base_Collections a JOIN Base_Collection_Posts b ON a.PostID=b.id WHERE a.Creator IN (select id from Base_Users where CustomID=?) AND b.tid=?";
	    for (String tid : tids) {
		pstmt = conn.prepareStatement(sql);
		pstmt.setInt(1, customid);
		pstmt.setString(2, tid);
		rs = pstmt.executeQuery();
		while (rs.next()) {
		    int cid = rs.getInt("PostID");
		    map.put(tid, cid);
		}
	    }
	    for (String tid : tids) {
		if (!map.containsKey(tid)) {
		    map.put(tid, 0);
		}
	    }
	} catch (SQLException e) {
	    e.printStackTrace();
	} finally {
	    this.CloseStatus(rs, pstmt, conn);
	}
	return map;
    }

    @Override
    public int queryCollectionTotal(int customId) {
	Connection conn = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	int count = 0;
	try {
	    conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME)
		    .getConnection();
	    String sql = "select COUNT(PostID) as count from Base_Collections where GroupID in (select ID from Base_Collection_Group where CustomID=?)";
	    pstmt = conn.prepareStatement(sql);
	    pstmt.setInt(1, customId);
	    rs = pstmt.executeQuery();
	    if (rs.next()) {
		count = rs.getInt("count");
	    }
	} catch (SQLException e) {
	    e.printStackTrace();
	} finally {
	    this.CloseStatus(rs, pstmt, conn);
	}
	return count;
    }

    private void CloseStatus(PreparedStatement pstmt, Connection conn) {
	this.CloseStatus(null, pstmt, conn);
    }

    private void CloseStatus(ResultSet rs, PreparedStatement pstmt,
	    Connection conn) {
	this.CloseStatus(null, null, rs, null, pstmt, conn);
    }

    private void CloseStatus(CallableStatement cstmt, Connection conn) {
	this.CloseStatus(null, null, cstmt, conn);
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
    public boolean checkIsDealStatus(int customid, long postid) {
	Connection conn = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	boolean isCheck = false;
	try {
	    conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME)
		    .getConnection();
	    String sql = "select ID from Base_Collections where Status in (1,2) and PostID=? and Creator in (select ID from Base_Users where CustomID=?)";
	    pstmt = conn.prepareStatement(sql);
	    pstmt.setLong(1, postid);
	    pstmt.setInt(2, customid);
	    rs = pstmt.executeQuery();
	    if(rs.next()){
		int flag = rs.getInt("ID");
		if(flag > 0){
		    isCheck = true;
		}
	    }
	} catch (SQLException e) {
	    e.printStackTrace();
	} finally {
	    this.CloseStatus(rs, pstmt, conn);
	}
	return isCheck;
    }

}
