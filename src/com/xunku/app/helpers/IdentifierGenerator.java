package com.xunku.app.helpers;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Queue;

import com.xunku.app.AppContext;

public class IdentifierGenerator {

	static final int poolSize = 10;
	static IdentifierGenerator _generator;
	private AppContext _context;

	public static synchronized IdentifierGenerator getInstance(
			AppContext context) {

		if (_generator == null) {
			_generator = new IdentifierGenerator();
			_generator._context = context;
			_generator.initStart();

		}

		return _generator;

	}

	Queue<Long> _idQueue;
	long _start;

	public synchronized long getId() {
		if (this._idQueue.size() <= 0) {
			this.generate(_start);
		}
		_start = this._idQueue.poll();
		return _start;
	}

	// ======================================================

	private void updateStart(long start) {
		Connection conn = null;
		CallableStatement cs = null;
		ResultSet rs = null;
		try {
			conn = _context.getPoolingHome().getConnection();
			cs = conn
					.prepareCall("insert into Base_Identifies(lastid)values(?)");

			cs.setLong(1, start + 1);
			cs.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (cs != null) {
					cs.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	private void initStart() {
		Connection conn = null;
		CallableStatement cs = null;
		ResultSet rs = null;
		try {
			conn = _context.getPoolingHome().getConnection();
			cs = conn.prepareCall("select max(lastId) from Base_Identifies");

			rs = cs.executeQuery();
			while (rs.next()) {
				this._start = rs.getLong(1);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (cs != null) {
					cs.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	private void generate(long start) {

		for (int i = 0; i < poolSize; i++) {
			this._idQueue.offer(start + i);
		}

		this.updateStart(start + poolSize);
	}

}
