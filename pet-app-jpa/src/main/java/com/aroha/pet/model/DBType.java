package com.aroha.pet.model;

/**
 *
 * @author Sony George | Date : 13 Mar, 2019 12:49:39 PM
 */
public enum DBType {
	ORACLE("oracle", "oracle.jdbc.driver.OracleDriver"),
	MYSQL("MYSQL", "com.mysql.jdbc.Driver"),
	MARIADB("mariadb", "org.mariadb.jdbc.Driver"),
	POSTGRESQL("postgresql", "org.postgresql.Driver"),
	DB2("db2", "COM.ibm.db2.jdbc.net.DB2Driver"),
	SYBASE("sybase", "com.sybase.jdbc.SybDriver"),
	MONGODB("mongodb", "mongodb.jdbc.MongoDriver"),
	MSSQL("mssql", "com.microsoft.sqlserver.jdbc.SQLServerDriver");

	private String name;
	private String driver;

	private DBType(String dbName, String dbDriver) {
		this.name = dbName;
		this.driver = dbDriver;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDriver() {
		return driver;
	}

	public void setDriver(String driver) {
		this.driver = driver;
	}

}
