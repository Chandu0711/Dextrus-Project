package com.dextrus.project.common;
import java.sql.Connection;

import java.sql.DriverManager;

import com.dextrus.project.entity.ConnectionDetails;

public class CC
{
	private static Connection connection= null;
	public static final String GET_TABLES_QUERY = "SELECT TABLE_NAME,TABLE_TYPE FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_CATALOG = ? AND TABLE_SCHEMA = ?";
	public static final String GET_DESCRIPTION_QUERY = "SELECT c.name AS 'COLUMN_NAME', t.name AS 'DATA_TYPE',c.max_length as 'MAX_LENGTH',c.precision AS 'PRECISION',c.is_nullable AS 'IS_NULLABLE', ISNULL(i.is_primary_key, 0) AS 'PRIMARY_KEY' FROM sys.columns c INNER JOIN sys.types t ON c.user_type_id = t.user_type_id LEFT JOIN sys.index_columns ic ON ic.object_id = c.object_id AND ic.column_id = c.column_id LEFT JOIN sys.indexes i ON ic.object_id = i.object_id AND ic.index_id = i.index_id WHERE c.object_id = OBJECT_ID(?);";
	public static final String GET_TABLES_BY_PATTERN_QUERY = "SELECT TABLE_NAME,TABLE_TYPE FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_NAME LIKE ?";
	public static Connection getConnection(ConnectionDetails details)
	{
		try
		{
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			connection = DriverManager.getConnection(details.getUrl(), details.getUsername(),details.getPassword());		
			return connection;
		} 
		catch (Exception e)
		{
			connection = null;
			return connection;
		}
	}
}
