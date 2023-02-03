package com.dextrus.project.controller;
import java.sql.Connection;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.dextrus.project.entity.ConnectionDetails;
import com.dextrus.project.entity.RequestBodyQuery;
import com.dextrus.project.entity.RequestPattern;
import com.dextrus.project.entity.TableDescription;
import com.dextrus.project.entity.TableType;
import com.dextrus.project.service.ConnectionService;

@RestController
@RequestMapping("/dextrus")
public class ConnectionController
{

	@Autowired
	private ConnectionService cs;
//task-1(getConnection)
	@PostMapping("/connect")
	public ResponseEntity<String> getconnectToSqlServer(@RequestBody ConnectionDetails details)
	{
		Connection connection = cs.getSQLServerConnection(details);
		if (connection == null)
			return new ResponseEntity<String>("Server not Connected", HttpStatus.SERVICE_UNAVAILABLE);
		else
			return new ResponseEntity<String>("Server Connected", HttpStatus.OK);
	}

//task-2(get catalog-list)
	@GetMapping("/")
	public ResponseEntity<List<String>> getCatalogs(@RequestBody ConnectionDetails details)
	{
		List<String> catalogs = cs.getCatalogsList(details);
		return new ResponseEntity<List<String>>(catalogs, HttpStatus.OK);
	}

//task-3(get schemas-list)
	@GetMapping("/{catalog}")
	public ResponseEntity<List<String>> getSchemas(@PathVariable String catalog,@RequestBody ConnectionDetails details )		
	{
		
		List<String> schemas = cs.getSchemasList(details, catalog);
		return new ResponseEntity<List<String>>(schemas, HttpStatus.OK);
	}

//task-4(get table-list)
	@GetMapping("/{catalog}/{schema}")
	public ResponseEntity<List<TableType>> getViewsAndTables(@PathVariable String catalog, @PathVariable String schema,@RequestBody ConnectionDetails details)
	 {

		List<TableType> viewsAndTables = cs.getTablesAndViews(details, catalog, schema);
		System.out.println(viewsAndTables.size() + "-------");
		return new ResponseEntity<List<TableType>>(viewsAndTables, HttpStatus.OK);
	}

//task-5(get table-description)
	@GetMapping("/{catalog}/{schema}/{table}")
	public ResponseEntity<List<TableDescription>> getColumnProperties(@PathVariable String catalog,@PathVariable String schema, @PathVariable String table, @RequestBody ConnectionDetails details)
	 {
		
		List<TableDescription> tableDescList = cs.getTableDescription(details, catalog, schema, table);
		return new ResponseEntity<List<TableDescription>>(tableDescList, HttpStatus.OK);
	}
	
//task-6( search by query)
	@GetMapping("/query")
	public ResponseEntity<List<List<Object>>> test(@RequestBody RequestBodyQuery queryBody) 
	{
		ConnectionDetails detail = queryBody.getDetails();
		String query = queryBody.getQuery();
		List<List<Object>> tableDataList = cs.getTableData(detail, query);
		return new ResponseEntity<List<List<Object>>>(tableDataList, HttpStatus.OK);
	}

//task-7(search by Pattern)
	@GetMapping("/search")
	public ResponseEntity<List<TableType>> getTablesByPattern(@RequestBody RequestPattern bodyPattern)
	{
		List<TableType> viewsAndTables = cs.getTablesAndViewsByPattern(bodyPattern.getDetails(),
				bodyPattern.getCatalog(), bodyPattern.getPattern());
		return new ResponseEntity<List<TableType>>(viewsAndTables, HttpStatus.OK);
	}
}
