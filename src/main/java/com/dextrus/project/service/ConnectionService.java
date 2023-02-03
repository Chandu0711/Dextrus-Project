package com.dextrus.project.service;

import java.sql.Connection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.dextrus.project.common.CC;
import com.dextrus.project.entity.ConnectionDetails;

import com.dextrus.project.entity.TableDescription;
import com.dextrus.project.entity.TableType;

@Service
public class ConnectionService {

	public Connection getSQLServerConnection(ConnectionDetails details) {
		Connection connection = CC.getConnection(details);
		return connection;
	}

	public List<String> getCatalogsList(ConnectionDetails details) {
		List<String> catalogs = null;
		Connection connection = CC.getConnection(details);
		try {
			ResultSet rs = connection.createStatement().executeQuery("SELECT name FROM sys.databases");
			catalogs = new ArrayList<>();
			while (rs.next()) {
				catalogs.add(rs.getString("name"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return catalogs;
	}

	public List<String> getSchemasList(ConnectionDetails details, String catalog) {
		List<String> schemas = null;
		try {
			Connection connection = CC.getConnection(details);
			String query = "SELECT name FROM " + catalog + ".sys.schemas";
			ResultSet rs = connection.createStatement().executeQuery(query);
			schemas = new ArrayList<>();
			while (rs.next())
				schemas.add(rs.getString("name"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return schemas;
	}

	public List<TableType> getTablesAndViews(ConnectionDetails details, String catalog, String schema) {
		List<TableType> viewsAndTables = new ArrayList<>();
		try {
			Connection connection = CC.getConnection(details);
			PreparedStatement statement = connection.prepareStatement("use " + catalog + "; " + CC.GET_TABLES_QUERY);
			statement.setString(1, catalog);
			statement.setString(2, schema);
			ResultSet resultSet = statement.executeQuery();
			while (resultSet.next()) {
				TableType tableType = new TableType();
				tableType.setTable_name(resultSet.getString("TABLE_NAME"));
				tableType.setTable_type(resultSet.getString("TABLE_TYPE"));
				viewsAndTables.add(tableType);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return viewsAndTables;
	}

	public List<TableDescription> getTableDescription(ConnectionDetails details, String catalog, String schema,
			String table) {
		List<TableDescription> tableDescList = new ArrayList<>();
		try {
			Connection connection = CC.getConnection(details);
			PreparedStatement statement = connection.prepareStatement("use " + catalog + "; " + CC.GET_DESCRIPTION_QUERY);
			table = schema + "." + table;
			statement.setString(1, table);
			ResultSet resultSet = statement.executeQuery();
			while (resultSet.next()) {
				TableDescription td = new TableDescription();
				td.setColumnName(resultSet.getString("COLUMN_NAME"));
				td.setDataType(resultSet.getString("DATA_TYPE"));
				td.setPrecision(resultSet.getInt("PRECISION"));
				td.setMaxlength(resultSet.getInt("MAX_LENGTH"));
				td.setIsNullable(resultSet.getInt("IS_NULLABLE"));
				td.setPrimaryKey(resultSet.getInt("PRIMARY_KEY"));
				tableDescList.add(td);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return tableDescList;
	}

	public List<List<Object>> getTableData(ConnectionDetails details, String query) {
		List<List<Object>> rows = new ArrayList<>();
		try {
			Connection con = CC.getConnection(details);
			Statement statement = con.createStatement();
			ResultSet resultSet = statement.executeQuery(query);
			ResultSetMetaData meta = resultSet.getMetaData();
			int columnCount = meta.getColumnCount();
			while (resultSet.next()) {

				List<Object> row = new ArrayList<>();
				for (int i = 1; i <= columnCount; i++) {

					String columnName = meta.getColumnName(i);
					String columnType = meta.getColumnTypeName(i);
					switch (columnType) {
					case "varchar": {
						row.add(columnName + " : " + resultSet.getString(columnName));
						break;
					}
					case "float": {
						row.add(columnName + " : " + resultSet.getFloat(columnName));
						break;
					}
					case "boolean": {
						row.add(columnName + " : " + resultSet.getBoolean(columnName));
						break;
					}
					case "int": {
						row.add(columnName + " : " + resultSet.getInt(columnName));
						break;
					}
					case "timestamp": {
						row.add(columnName + " : " + resultSet.getTimestamp(columnName));
						break;
					}
					case "decimal": {
						row.add(columnName + " : " + resultSet.getBigDecimal(columnName));
						break;
					}
					case "date": {
						row.add(columnName + " : " + resultSet.getDate(columnName));
						break;
					}
					default:
						row.add("!-!-! " + columnName + " : " + resultSet.getObject(columnName));
						System.out.println("Datatype Not available for Column: " + columnName);
					}
				}
				rows.add(row);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rows;
	}

	public List<TableType> getTablesAndViewsByPattern(ConnectionDetails details, String catalog, String pattern) {
		List<TableType> viewsAndTables = new ArrayList<>();
		try {
			Connection connection = CC.getConnection(details);
			PreparedStatement statement = 
					connection.prepareStatement("use " + catalog + "; " + CC.GET_TABLES_BY_PATTERN_QUERY);
			statement.setString(1, pattern);
			ResultSet resultSet = statement.executeQuery();
			while (resultSet.next()) {
				TableType tableType = new TableType();
				tableType.setTable_name(resultSet.getString("TABLE_NAME"));
				tableType.setTable_type(resultSet.getString("TABLE_TYPE"));
				viewsAndTables.add(tableType);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return viewsAndTables;
	}
}
