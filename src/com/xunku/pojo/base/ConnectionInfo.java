package com.xunku.pojo.base;

public class ConnectionInfo {

	String driverName;
	String uid;
	String password;
	String connectionString;

	String database;

	public String getDriverName() {
		return driverName;
	}

	public void setDriverName(String driverName) {
		this.driverName = driverName;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getConnectionString() {
		return connectionString;
	}

	public void setConnectionString(String connectionString) {
		this.connectionString = connectionString;
	}

	/**
	 * 连接串：com.microsoft.sqlserver.jdbc.SQLServerDriver,jdbc:sqlserver://192.168.2.13:1433;databaseName=weibo_data,sa,kx*0000
	 * 
	 * @param poolString
	 *            :驱动,连接串,用户名,密码
	 * @return
	 */
	public static ConnectionInfo getConnInfo(String poolString) {

		String[] splits = poolString.split(",");

		ConnectionInfo result = new ConnectionInfo();
		result.driverName = splits[0];
		result.connectionString = splits[1];
		result.uid = splits[2];
		result.password = splits[3];
		int beginIndex = result.connectionString.indexOf("databaseName=");
		int endIndex = result.connectionString.length();
		result.database = result.connectionString.substring(beginIndex + 13,
				endIndex);
		return result;

	}

	public static void main(String[] args) {
		ConnectionInfo info = ConnectionInfo
				.getConnInfo("com.microsoft.sqlserver.jdbc.SQLServerDriver,jdbc:sqlserver://192.168.2.13:1433;databaseName=weibo_data,sa,kx*0000");

		System.out.print(info);

	}

	public String getDatabase() {
		return database;
	}

	public void setDatabase(String database) {
		this.database = database;
	}
}
