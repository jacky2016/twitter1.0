package com.xunku.app.helpers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.charset.Charset;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.jolbox.bonecp.BoneCP;
import com.xunku.app.Utility;
import com.xunku.app.enums.PostType;
import com.xunku.app.helpers.ibatis.Resources;
import com.xunku.app.helpers.ibatis.ScriptRunner;
import com.xunku.app.interfaces.IFiller;
import com.xunku.app.model.Pooling;
import com.xunku.app.model.Query;
import com.xunku.constant.PortalCST;
import com.xunku.constant.SQLCST;
import com.xunku.utils.DatabaseUtils;
import com.xunku.utils.Pagefile;
public class DbHelper {

	static String templateTable = null;
	static String templateProce = null;

	static {
		try {
			templateTable = readResource("tweet.store.table.sql");
			templateProce = readResource("tweet.store.proce.sql");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static String getTemplateTable() {
		return templateTable;
	}

	public static String getTemplateProce() {
		return templateProce;
	}

	private static String readResource(String resource) throws IOException {
		Resources.setCharset(Charset.forName("utf-8"));
		Reader reader = Resources.getResourceAsReader(resource);
		BufferedReader r = new BufferedReader(reader);
		StringBuilder b = new StringBuilder();
		String line;
		while ((line = r.readLine()) != null) {
			b.append(line);
			b.append("\r\n");
		}
		return b.toString();
	}

	public static String getResource(String resource) {
		try {
			return readResource(resource);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 规则化查询字符串
	 * 
	 * @param text
	 * @return
	 */
	public static String nomalizeText(String text) {
		return text.replace("'", "''");
	}

	public static void incSpreadVipStatis(int sid, PostType type,
			boolean isVip, Pooling pool) {
		Connection conn = null;
		CallableStatement cstmt = null;
		try {
			// 事件是平台相关的，所以这里不需要平台关键字
			int vip = 0;
			int novip = 0;
			if (isVip)
				vip = 1;
			else
				novip = 1;
			conn = pool.getConnection();
			cstmt = conn
					.prepareCall("{call sys_inc_Spread_Users_Vip_Statis(?,?,?,?)}");
			cstmt.setInt(1, sid);
			cstmt.setInt(2, Utility.getPostType(type));
			cstmt.setInt(3, vip);
			cstmt.setInt(4, novip);
			cstmt.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
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

	}

	/**
	 * 累加统计
	 * 
	 * @param procName
	 * @param sid
	 * @param type
	 * @param property
	 * @param pool
	 */
	public static void incSpreadStatis(String procName, int sid, PostType type,
			String property, Pooling pool) {
		Connection conn = null;
		CallableStatement cstmt = null;
		try {
			// 事件是平台相关的，所以这里不需要平台关键字
			conn = pool.getConnection();
			cstmt = conn.prepareCall("{call " + procName + "(?,?,?)}");
			cstmt.setInt(1, sid);
			cstmt.setInt(2, Utility.getPostType(type));
			cstmt.setString(3, property);
			cstmt.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
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

	}

	public static void incSpreadStatis(String procName, int sid, PostType type,
			String propertyName, boolean property, Pooling pool) {
		Connection conn = null;
		CallableStatement cstmt = null;
		try {
			// 事件是平台相关的，所以这里不需要平台关键字
			conn = pool.getConnection();
			cstmt = conn.prepareCall("{call " + procName + "(?,?,?)}");
			cstmt.setInt(1, sid);
			cstmt.setInt(2, Utility.getPostType(type));
			cstmt.setBoolean(3, property);
			cstmt.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
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

	}

	public static void incSpreadStatis(String procName, int sid, PostType type,
			int property, Pooling pool) {
		Connection conn = null;
		CallableStatement cstmt = null;
		try {
			// 事件是平台相关的，所以这里不需要平台关键字
			conn = pool.getConnection();
			cstmt = conn.prepareCall("{call " + procName + "(?,?,?)}");
			cstmt.setInt(1, sid);
			cstmt.setInt(2, Utility.getPostType(type));
			cstmt.setInt(3, property);
			cstmt.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
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

	}

	/**
	 * 检查指定的连接池上的表里面是否有数据
	 * 
	 * @param pool
	 * @param 查询数据的查询语句
	 * @return
	 */
	public static boolean dbHasData(Pooling pool, String sql) {

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Connection conn = null;
		boolean result = false;
		try {
			conn = pool.getConnection();
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				result = true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (pstmt != null) {
					pstmt.close();
				}
				if (conn != null)
					conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return result;

	}

	/**
	 * 在指定的池上创建一个分页存储过程
	 * 
	 * @param pool
	 */
	public static void dbCreatePagerProc(Pooling pool) {

		try {
			dbCreateSQLObj(pool,
					"select * from sys.objects where name ='SupesoftPage'",
					SQLCST.PROC_CREATE_PAGER);
			runScript("base.SupesoftPageBySql.sql", pool);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 在targetPool 上创建一个关联到sourcePool上的同义词，以方便在targetPool上可以直接关联source上的表查询
	 * <p>
	 * 表明为一一对应，如果已经存在，则不再创建
	 * 
	 * @param targetPool
	 * @param sourcePool
	 */
	public static void dbCreateSynonym(Pooling targetPool, Pooling sourcePool) {

		System.out.println("************** 创建同义词 ***********************");

	}

	/**
	 * 检查某个数据库对象是否存在
	 * 
	 * @param conn
	 * @param objName
	 * @return
	 */
	public static boolean dbExistsSqlObject(BoneCP pool,
			String objName) {

		PreparedStatement pstmt = null;
		PreparedStatement pstmt2 = null;
		ResultSet rs = null;
		Connection conn = null;
		boolean result = false;
		try {
			String existsSql = "SELECT * FROM SYS.OBJECTS WHERE name='"
					+ objName + "'";
			conn = pool.getConnection();
			pstmt = conn.prepareStatement(existsSql);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				result = true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (pstmt2 != null) {
					pstmt2.close();
				}
				if (pstmt != null) {
					pstmt.close();
				}
				if (conn != null)
					conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	/**
	 * 在指定的连接池上执行SQL
	 * 
	 * @param pool
	 *            指定的连接池
	 * @param existsSql
	 *            检查语句，是否有指定的记录或者对象
	 * @param createSql
	 *            创建语句，创建指定的记录或者对象
	 */
	public static void dbCreateSQLObj(Pooling pool, String existsSql,
			String createSql) {
		PreparedStatement pstmt = null;
		PreparedStatement pstmt2 = null;
		ResultSet rs = null;
		Connection conn = null;
		try {
			conn = pool.getConnection();

			pstmt = conn.prepareStatement(existsSql);
			rs = pstmt.executeQuery();
			if (!rs.next()) {
				pstmt2 = conn.prepareStatement(createSql);
				pstmt2.execute();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (pstmt2 != null) {
					pstmt2.close();
				}
				if (pstmt != null) {
					pstmt.close();
				}
				if (conn != null)
					conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 功能描述<检查指定的数据库对象是否存在,不存在就创建>
	 * 
	 * @param conn
	 *            连接对象
	 * @param existsObj
	 *            要创建的sql对象
	 * @param createSql
	 *            要创建sql对象的sql语句
	 */
	public static void dbCreateSQLObject(Pooling pool, String existsObj,
			String createSql) {
		String existsSql = "SELECT * FROM SYS.OBJECTS WHERE name='" + existsObj
				+ "'";

		dbCreateSQLObj(pool, existsSql, createSql);
	}

	/**
	 * 在指定的连接池上执行脚本
	 * 
	 * @param sqlFile
	 *            SQL 脚本文件
	 * @throws Exception
	 */
	public static void execute(String resource, Pooling pool) throws Exception {
		Resources.setCharset(Charset.forName("utf-8"));
		runScript(resource, pool);
	}

	public static void runScript(String resource, Pooling pool)
			throws Exception {
		Connection conn = pool.getConnection();
		try {
			if (conn != null) {
				ScriptRunner runner = new ScriptRunner(conn, false, false);
				runner.runScript(Resources.getResourceAsReader(resource));
			}
		} finally {
			if (conn != null) {
				conn.close();
			}

		}
	}

	/**
	 * 指定分页获得分页对象
	 * 
	 * @param <T>
	 * @param query
	 * @param filler
	 * @return
	 */
	public static <T> Pagefile<T> executePager(Query query, IFiller<T> filler) {

		Connection conn = null;
		CallableStatement cstmt = null;
		Pagefile<T> pagefile = null;
		ResultSet rs = null;
		ResultSet rs2 = null;
		try {
			conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME)
					.getConnection();
			cstmt = conn.prepareCall("{call SupesoftPage(?,?,?,?,?,?,?)}");
			cstmt.setString(1, query.getTableName());
			cstmt.setString(2, query.getFields());
			cstmt.setInt(3, query.getPageSize());
			cstmt.setInt(4, query.getPageIndex());
			cstmt.setString(5, query.getWhere());
			cstmt.setString(6, query.getSortField());
			cstmt.setInt(7, query.getSort());
			cstmt.execute();
			pagefile = new Pagefile<T>();
			rs = cstmt.getResultSet();
			while (rs.next()) {
				pagefile.getRows().add(filler.fill(rs));
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
			Utility.closeConnection(conn, cstmt, rs, rs2);
		}
		return pagefile;
	}

	public static void main(String[] args) {

		try {
			Reader reader = Resources.getResourceAsReader("base.sql");
			System.out.print(reader.read());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
