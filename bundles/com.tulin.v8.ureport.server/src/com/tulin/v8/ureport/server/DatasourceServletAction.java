package com.tulin.v8.ureport.server;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.sql.DataSource;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;
import org.springframework.context.ApplicationContext;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementCreatorFactory;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterUtils;
import org.springframework.jdbc.core.namedparam.ParsedSql;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.jdbc.support.JdbcUtils;

import com.bstek.ureport.Utils;
import com.bstek.ureport.build.Context;
import com.bstek.ureport.console.designer.DataResult;
import com.bstek.ureport.console.exception.ReportDesignException;
import com.bstek.ureport.definition.dataset.Field;
import com.bstek.ureport.definition.datasource.DataType;
import com.bstek.ureport.expression.ExpressionUtils;
import com.bstek.ureport.expression.model.Expression;
import com.bstek.ureport.expression.model.data.ExpressionData;
import com.bstek.ureport.expression.model.data.ObjectExpressionData;
import com.bstek.ureport.utils.ProcedureUtils;
import com.tulin.v8.ureport.server.common.UReportWebServer.HTTPRequest;

import chrriis.common.WebServerContent;

public class DatasourceServletAction extends RenderPageServletAction {
	ApplicationContext applicationContext;

	public DatasourceServletAction(HTTPRequest httpRequest) {
		super(httpRequest);
		this.applicationContext = Activator.applicationContext;
	}

	public WebServerContent execute() {
		if (httpRequest.getURLPath().endsWith("/loadBuildinDatasources")) {
			return loadBuildinDatasources();
		} else if (httpRequest.getURLPath().endsWith("/buildDatabaseTables")) {
			return buildDatabaseTables();
		} else if (httpRequest.getURLPath().endsWith("/previewData")) {
			return previewData();
		} else if (httpRequest.getURLPath().endsWith("/buildFields")) {
			return buildFields();
		} else if (httpRequest.getURLPath().endsWith("/testConnection")) {
			return testConnection();
		}
		return null;
	}

	private WebServerContent loadBuildinDatasources() {
		List<String> datasources = new ArrayList<String>();
		Set<String> keys = com.tlv8.datasource.Utils.getPermitionDatasourceKeys();
		for (String k : keys) {
			datasources.add(k);
		}
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		try {
			ObjectMapper mapper = new ObjectMapper();
			mapper.setSerializationInclusion(Inclusion.NON_NULL);
			mapper.configure(SerializationConfig.Feature.WRITE_DATES_AS_TIMESTAMPS, false);
			mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
			mapper.writeValue(out, datasources);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new DefWebServerContent(out);
	}

	private WebServerContent buildDatabaseTables() {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		Connection conn = null;
		ResultSet rs = null;
		try {
			conn = buildConnection();
			DatabaseMetaData metaData = conn.getMetaData();
			String url = metaData.getURL();
			String schema = null;
			if (url.toLowerCase().contains("oracle")) {
				schema = metaData.getUserName();
			}
			List<Map<String, String>> tables = new ArrayList<Map<String, String>>();
			rs = metaData.getTables(null, schema, "%", new String[] { "TABLE", "VIEW" });
			while (rs.next()) {
				Map<String, String> table = new HashMap<String, String>();
				table.put("name", rs.getString("TABLE_NAME"));
				table.put("type", rs.getString("TABLE_TYPE"));
				tables.add(table);
			}
			writeObjectToJson(out, tables);
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			JdbcUtils.closeResultSet(rs);
			JdbcUtils.closeConnection(conn);
		}
		return new DefWebServerContent(out);
	}

	private WebServerContent buildFields() {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		String sql = httpRequest.getParameter("sql");
		String parameters = httpRequest.getParameter("parameters");
		Connection conn = null;
		final List<Field> fields = new ArrayList<Field>();
		try {
			conn = buildConnection();
			Map<String, Object> map = buildParameters(parameters);
			sql = parseSql(sql, map);
			if (ProcedureUtils.isProcedure(sql)) {
				List<Field> fieldsList = ProcedureUtils.procedureColumnsQuery(sql, map, conn);
				fields.addAll(fieldsList);
			} else {
				DataSource dataSource = new SingleConnectionDataSource(conn, false);
				NamedParameterJdbcTemplate jdbc = new NamedParameterJdbcTemplate(dataSource);
				PreparedStatementCreator statementCreator = getPreparedStatementCreator(sql,
						new MapSqlParameterSource(map));
				jdbc.getJdbcOperations().execute(statementCreator, new PreparedStatementCallback<Object>() {
					@Override
					public Object doInPreparedStatement(PreparedStatement ps) throws SQLException, DataAccessException {
						ResultSet rs = null;
						try {
							rs = ps.executeQuery();
							ResultSetMetaData metadata = rs.getMetaData();
							int columnCount = metadata.getColumnCount();
							for (int i = 0; i < columnCount; i++) {
								String columnName = metadata.getColumnLabel(i + 1);
								fields.add(new Field(columnName));
							}
							return null;
						} finally {
							JdbcUtils.closeResultSet(rs);
						}
					}
				});
			}
			writeObjectToJson(out, fields);
		} catch (Exception ex) {
			throw new ReportDesignException(ex);
		} finally {
			JdbcUtils.closeConnection(conn);
		}
		return new DefWebServerContent(out);
	}

	protected PreparedStatementCreator getPreparedStatementCreator(String sql, SqlParameterSource paramSource) {
		ParsedSql parsedSql = NamedParameterUtils.parseSqlStatement(sql);
		String sqlToUse = NamedParameterUtils.substituteNamedParameters(parsedSql, paramSource);
		Object[] params = NamedParameterUtils.buildValueArray(parsedSql, paramSource, null);
		List<SqlParameter> declaredParameters = NamedParameterUtils.buildSqlParameterList(parsedSql, paramSource);
		PreparedStatementCreatorFactory pscf = new PreparedStatementCreatorFactory(sqlToUse, declaredParameters);
		return pscf.newPreparedStatementCreator(params);
	}

	private WebServerContent previewData() {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		String sql = httpRequest.getParameter("sql");
		String parameters = httpRequest.getParameter("parameters");
		Connection conn = null;
		try {
			conn = buildConnection();
			Map<String, Object> map = buildParameters(parameters);
			sql = parseSql(sql, map);
			List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
			if (ProcedureUtils.isProcedure(sql)) {
				list = ProcedureUtils.procedureQuery(sql, map, conn);
			} else {
				DataSource dataSource = new SingleConnectionDataSource(conn, false);
				NamedParameterJdbcTemplate jdbc = new NamedParameterJdbcTemplate(dataSource);
				list = jdbc.queryForList(sql, map);
			}
			int size = list.size();
			int currentTotal = size;
			if (currentTotal > 500) {
				currentTotal = 500;
			}
			List<Map<String, Object>> ls = new ArrayList<Map<String, Object>>();
			for (int i = 0; i < currentTotal; i++) {
				ls.add(list.get(i));
			}
			DataResult result = new DataResult();
			List<String> fields = new ArrayList<String>();
			if (size > 0) {
				Map<String, Object> item = list.get(0);
				for (String name : item.keySet()) {
					fields.add(name);
				}
			}
			result.setFields(fields);
			result.setCurrentTotal(currentTotal);
			result.setData(ls);
			result.setTotal(size);
			writeObjectToJson(out, result);
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return new DefWebServerContent(out);
	}

	private String parseSql(String sql, Map<String, Object> parameters) {
		sql = sql.trim();
		Context context = new Context(applicationContext, parameters);
		if (sql.startsWith(ExpressionUtils.EXPR_PREFIX) && sql.endsWith(ExpressionUtils.EXPR_SUFFIX)) {
			sql = sql.substring(2, sql.length() - 1);
			Expression expr = ExpressionUtils.parseExpression(sql);
			sql = executeSqlExpr(expr, context);
			return sql;
		} else {
			String sqlForUse = sql;
			Pattern pattern = Pattern.compile("\\$\\{.*?\\}");
			Matcher matcher = pattern.matcher(sqlForUse);
			while (matcher.find()) {
				String substr = matcher.group();
				String sqlExpr = substr.substring(2, substr.length() - 1);
				Expression expr = ExpressionUtils.parseExpression(sqlExpr);
				String result = executeSqlExpr(expr, context);
				sqlForUse = sqlForUse.replace(substr, result);
			}
			Utils.logToConsole("DESIGN SQL:" + sqlForUse);
			return sqlForUse;
		}
	}

	private String executeSqlExpr(Expression sqlExpr, Context context) {
		String sqlForUse = null;
		ExpressionData<?> exprData = sqlExpr.execute(null, null, context);
		if (exprData instanceof ObjectExpressionData) {
			ObjectExpressionData data = (ObjectExpressionData) exprData;
			Object obj = data.getData();
			if (obj != null) {
				String s = obj.toString();
				s = s.replaceAll("\\\\", "");
				sqlForUse = s;
			}
		}
		return sqlForUse;
	}

	@SuppressWarnings({ "unchecked" })
	private Map<String, Object> buildParameters(String parameters)
			throws IOException, JsonParseException, JsonMappingException {
		Map<String, Object> map = new HashMap<String, Object>();
		if (StringUtils.isBlank(parameters)) {
			return map;
		}
		ObjectMapper mapper = new ObjectMapper();
		List<Map<String, Object>> list = mapper.readValue(parameters, ArrayList.class);
		for (Map<String, Object> param : list) {
			String name = param.get("name").toString();
			DataType type = DataType.valueOf(param.get("type").toString());
			String defaultValue = (String) param.get("defaultValue");
			if (defaultValue == null || defaultValue.equals("")) {
				switch (type) {
				case Boolean:
					map.put(name, false);
				case Date:
					map.put(name, new Date());
				case Float:
					map.put(name, Float.valueOf(0));
				case Integer:
					map.put(name, 0);
				case String:
					if (defaultValue != null && defaultValue.equals("")) {
						map.put(name, "");
					} else {
						map.put(name, "null");
					}
					break;
				case List:
					map.put(name, new ArrayList<Object>());
				}
			} else {
				map.put(name, type.parse(defaultValue));
			}
		}
		return map;
	}

	private WebServerContent testConnection() {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		String username = httpRequest.getParameter("username");
		String password = httpRequest.getParameter("password");
		String driver = httpRequest.getParameter("driver");
		String url = httpRequest.getParameter("url");
		Connection conn = null;
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			Class.forName(driver);
			conn = DriverManager.getConnection(url, username, password);
			map.put("result", true);
		} catch (Exception ex) {
			map.put("error", ex.toString());
			map.put("result", false);
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return new DefWebServerContent(out);
	}

	private Connection buildConnection() throws Exception {
		String type = httpRequest.getParameter("type");
		if (type.equals("jdbc")) {
			String username = httpRequest.getParameter("username");
			String password = httpRequest.getParameter("password");
			String driver = httpRequest.getParameter("driver");
			String url = httpRequest.getParameter("url");

			Class.forName(driver);
			Connection conn = DriverManager.getConnection(url, username, password);
			return conn;
		} else {
			String name = httpRequest.getParameter("name");
			Connection conn = com.tlv8.datasource.Utils.getAppConn(name);
			if (conn == null) {
				throw new ReportDesignException("Buildin datasource [" + name + "] not exist.");
			}
			return conn;
		}
	}
}
