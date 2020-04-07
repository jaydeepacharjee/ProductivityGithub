package com.aroha.pet.service;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import com.aroha.pet.exception.ResourceNotFoundException;
import com.aroha.pet.model.DBType;
import com.aroha.pet.model.DbInfo;
import com.aroha.pet.model.QueryInfo;
import com.aroha.pet.model.Question;
import com.aroha.pet.model.User;
import com.aroha.pet.payload.PagedResponse;
import com.aroha.pet.payload.SqlResponse;
import com.aroha.pet.repository.DBRepository;
import com.aroha.pet.repository.UserRepository;
import com.aroha.pet.security.UserPrincipal;

/**
 *
 * @author Sony George | Date : 13 Mar, 2019 3:36:44 PM
 */
@Service
public class DBService {

    @Autowired
    DBRepository dBRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    QueryInfoService queryInfoService;

    private static final Logger logger = LoggerFactory.getLogger(DBService.class);

    public Optional<DbInfo> getDbInfoById(long id) {
        return dBRepository.findById(id);
    }

    public List<DbInfo> getAllDBInfoDataByUser(long userId) {

        long roleId = userRepository.getRole(userId);
        //System.out.println("Role Id is: "+roleId);

        if (roleId == 1) {

            Optional<User> users = userRepository.findById(userId);
            User user = users.get();
            List<DbInfo> list = new ArrayList<DbInfo>();
            for (DbInfo dbInfo : user.getDbs()) {
                DbInfo db = dbInfo;
                list.add(db);
            }
            return list;
        } else {
            List<DbInfo> list2 = dBRepository.findDbFromID();
            return list2;
        }

    }

    public List<DbInfo> getAllDBInfoByUserId(long userId) {
        /*	
         Optional<User> users= userRepository.findById(userId);
         User user=users.get();
         List<DBTypeDataRequest> listDataRequest = new ArrayList<>();
         for(DbInfo dbInfo: user.getDbs()) {
         DbInfo db=dbInfo;
         DBTypeDataRequest dbType=new DBTypeDataRequest();
         dbType.setDbName(db.getDbName());
         dbType.setDbType(db.getDbType());
         dbType.setId(db.getId());
         listDataRequest.add(dbType);
         }
         return listDataRequest; 

         */

        List<DbInfo> list = dBRepository.findDbFromID();

        return list;

    }

    public PagedResponse<DbInfo> getDataBasesCreatedBy(UserPrincipal currentUser, int page, int size) {

        User user = userRepository.findByEmail(currentUser.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", currentUser.getEmail()));
        // Retrieve all DatBase Connections created by the given username
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "createdAt");
        Page<DbInfo> dbList = dBRepository.findByCreatedBy(user.getId(), pageable);

        if (dbList.getNumberOfElements() == 0) {
            return new PagedResponse<>(Collections.emptyList(), dbList.getNumber(), dbList.getSize(),
                    dbList.getTotalElements(), dbList.getTotalPages(), dbList.isLast());
        }

        return new PagedResponse<>(dbList.getContent(), dbList.getNumber(), dbList.getSize(), dbList.getTotalElements(),
                dbList.getTotalPages(), dbList.isLast());
    }

    public List<DbInfo> getAllDataBasesCreatedBy(UserPrincipal currentUser) {
        User user = userRepository.findByEmail(currentUser.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", currentUser.getEmail()));

        System.out.println("User Id: " + user.getId());
        return dBRepository.findAllByCreatedBy(user.getId());
    }

    public DbInfo save(DbInfo dbInfo) {
        return dBRepository.save(dbInfo);
    }

    public SqlResponse executeQuery(DbInfo dbInfo, final String sqlStr, final UserPrincipal currentUser, Question question) {
        QueryInfo queryInfo = new QueryInfo();
        String sqlStr1 = "";
        queryInfo.setSqlStr(sqlStr);
        if (question.getQuestionDesc().equals("")) {
            queryInfo.setScenario(null);
        }
        queryInfo.setQuestionId(question.getQuestionId());
        queryInfo.setScenario(question.getQuestionDesc());
        queryInfo.setDbType(dbInfo.getDbType());
        queryInfo.setJdbcUrl(dbInfo.getJdbcUrl());
        queryInfo.setUserName(dbInfo.getUserName());
        queryInfo.setPassword(dbInfo.getPassword());
//        queryInfo = queryInfoService.save(queryInfo);
        SqlResponse sqlResponse = new SqlResponse();
        sqlResponse.setDbInfo(dbInfo);
        sqlResponse.setSql(sqlStr);
        Connection con = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            con = getConnection(queryInfo.getJdbcUrl(), queryInfo.getUserName(), queryInfo.getPassword());
            logger.info("Database connected successfully");
            /*
             * stmt = con.createStatement(ResultSet.TYPE_FORWARD_ONLY,
             * ResultSet.CONCUR_READ_ONLY, ResultSet.FETCH_FORWARD);
             */
            String res = sqlStr.trim();
            stmt = con.createStatement();
            JSONArray jsona = null;
            /*
             * if (dbInfo.getDbType().equalsIgnoreCase("oracle") && res.charAt(res.length()
             * - 1) == ';') { sqlStr1 = sqlStr.substring(0, res.length() - 1); rs =
             * stmt.executeQuery(sqlStr1); } else { rs = stmt.executeQuery(res); }
             */

            if (dbInfo.getDbType().equalsIgnoreCase("oracle")) {
                if (res.charAt(res.length() - 1) == ';') {
                    res = res.substring(0, res.length() - 1);
                }

                String quarr[] = res.split(" ");
                switch (quarr[0]) {
                    case "desc":
                    case "Desc":
                    case "Describe":
                    case "describe":
                    case "DESCRIBE":
                    case "DESC":
                        res = res.replace(quarr[0], "select * from");
                        rs = stmt.executeQuery(res);
                        ResultSetMetaData rsmd = rs.getMetaData();
                        jsona = getResultForRSMD(rsmd);
                        break;
                    default:
                        rs = stmt.executeQuery(res);
                        jsona = getResult(rs);
                        break;
                }
            } else {
                rs = stmt.executeQuery(res);
                jsona = getResult(rs);
            }

            sqlResponse.setResult(getJsonArrayAsList(jsona));
            queryInfo.setResultStr(jsona.toString());
            sqlResponse.setStatus("SUCCESS");

        } catch (Exception ex) {
            logger.error("Database connection failed" + ex.getMessage());
            sqlResponse.setException(ex.getMessage());
            sqlResponse.setStatus("ERROR");
        } finally {
            try {
                if (con != null) {
                    con.close();
                    con = null;
                }
                if (stmt != null) {
                    stmt.close();
                    stmt = null;
                }
                if (rs != null) {
                    rs.close();
                    rs = null;
                }
            } catch (Exception x) {
                con = null;
                stmt = null;
                rs = null;
            }
        }
        queryInfo.setExceptionStr(sqlResponse.getException());
        Date date = new Date();
        if (queryInfoService.checkDuplicateQuery(question.getQuestionId(), date, currentUser.getId(), sqlStr)) {
            sqlResponse.setMessage("Duplicate query for the same question");
        } else {
            queryInfoService.save(queryInfo);
        }
        return sqlResponse;
    }

    public List<String> getTableList(DbInfo dbInfo) {
        // TODO query the db using the dbinfo, then get the list of tables based on the database type
        // for each database type, the query to get the list of table will be different
        List<String> tableList = getTableListForDb(dbInfo);
        return tableList;
    }

    public Map<String, String> getTableColumnList(DbInfo dbInfo, final String tableName) {
        // TODO query the db using the dbinfo, then get the list of columns of the table based on the database type
        // for each database type, the query to get the list of tablecolumns will be different

        Map<String, String> columnList = new HashMap<String, String>();
        Connection con = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            con = getConnection(dbInfo.getJdbcUrl(), dbInfo.getUserName(), dbInfo.getPassword());
            logger.info("Get table column list connection sucessfully");
            stmt = con.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY, ResultSet.FETCH_FORWARD);
            stmt.setMaxRows(1);
            stmt.setFetchSize(1);
            rs = stmt.executeQuery("SELECT * FROM " + tableName);
            ResultSetMetaData metadata = rs.getMetaData();
            int numColumns = metadata.getColumnCount();
            for (int i = 1; i <= numColumns; i++) {
                columnList.put(metadata.getColumnName(i), metadata.getColumnTypeName(i) + "(" + metadata.getColumnDisplaySize(i) + ")"); //+metadata.getColumnClassName(i)
            }
        } catch (Exception ex) {
            logger.error("Get table column list connection is failed " + ex.getMessage());
            throw new RuntimeException(ex);
        } finally {
            try {
                if (con != null) {
                    con.close();
                    con = null;
                }
                if (stmt != null) {
                    stmt.close();
                    stmt = null;
                }
                if (rs != null) {
                    rs.close();
                    rs = null;
                }
            } catch (Exception x) {
                con = null;
                stmt = null;
                rs = null;
            }
        }
        return columnList;
    }

    private String getStackTrace(Exception ex) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        ex.printStackTrace(pw);
        return sw.toString();
    }

    private Connection getConnection(final String jdbcUrl, final String userName, final String password)
            throws Exception {
        Connection conn = DriverManager.getConnection(jdbcUrl, userName, password);
        return conn;
    }

    private JSONArray getResultForRSMD(ResultSetMetaData rsmd) throws Exception {
        JSONArray json = new JSONArray();
        int numColumns = rsmd.getColumnCount();
        JSONObject obj = new JSONObject();
        for (int k = 1; k <= numColumns; k++) {
            String column_name = rsmd.getColumnName(k);
            obj.put(column_name, rsmd.getColumnTypeName(k));
        }
        json.put(obj);
        return json;
    }

    private JSONArray getResult(ResultSet rs) throws Exception {
        JSONArray json = new JSONArray();
        ResultSetMetaData rsmd = rs.getMetaData();
        while (rs.next()) {
            int numColumns = rsmd.getColumnCount();
            JSONObject obj = new JSONObject();
            for (int i = 1; i <= numColumns; i++) {
                String column_name = rsmd.getColumnName(i);
                obj.put(column_name, rs.getObject(column_name));
            }
            json.put(obj);
        }
        return json;
    }

    private List<String> getTableListForDb(final DbInfo dbInfo) {
        DBType dbType = DBType.valueOf(dbInfo.getDbType().toUpperCase());
        if (dbType.getName().startsWith("MYSQL")) {
            return getMySqlTableList(dbInfo, dbType);
        }
        if (dbType.getName().startsWith("maria")) {
            return getMySqlTableList(dbInfo, dbType);
        }
        if (dbType.getName().startsWith("oracle")) {
            return getOracleTableList(dbInfo, dbType);
        }
        if (dbType.getName().startsWith("mssql")) {
            return getMsSqlTableList(dbInfo, dbType);
        }
        if (dbType.getName().startsWith("postgres")) {
            return getPostgresTableList(dbInfo, dbType);
        }
        return null;
    }

    private List<String> getMySqlTableList(DbInfo dbInfo, DBType dbType) {
        Connection con = null;
        Statement stmt = null;
        ResultSet rs = null;
        List<String> tableList = new ArrayList<String>();
        try {
            con = getConnection(dbInfo.getJdbcUrl(), dbInfo.getUserName(), dbInfo.getPassword());
            logger.info("Mysql show  table list connection successfully");
            stmt = con.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY,
                    ResultSet.FETCH_FORWARD);

            rs = stmt.executeQuery("show tables; ");
            while (rs.next()) {
                tableList.add(rs.getString(1));
            }
        } catch (Exception ex) {
            logger.error("Mysql table list connetion error " + ex.getMessage());
            throw new RuntimeException(ex);
        } finally {
            try {
                if (con != null) {
                    con.close();
                    con = null;
                }
                if (stmt != null) {
                    stmt.close();
                    stmt = null;
                }
                if (rs != null) {
                    rs.close();
                    rs = null;
                }
            } catch (Exception x) {
                con = null;
                stmt = null;
                rs = null;
            }
        }
        return tableList;
    }

    private List<String> getOracleTableList(DbInfo dbInfo, DBType dbType) {
        //throw new UnsupportedOperationException("Not supported yet."); // To change body of generated methods, choose
        // Tools | Templates.
        Connection con = null;
        Statement stmt = null;
        ResultSet rs = null;
        List<String> tableList = new ArrayList<String>();
        try {
            con = getConnection(dbInfo.getJdbcUrl(), dbInfo.getUserName(), dbInfo.getPassword());
            logger.info("Oracle table list connection successfully");
            stmt = con.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY,
                    ResultSet.FETCH_FORWARD);

            rs = stmt.executeQuery("select * from tab ");
            while (rs.next()) {
                tableList.add(rs.getString(1));
            }
        } catch (Exception ex) {
            logger.error("Oracle table list connetion error " + ex.getMessage());
            throw new RuntimeException(ex);
        } finally {
            try {
                if (con != null) {
                    con.close();
                    con = null;
                }
                if (stmt != null) {
                    stmt.close();
                    stmt = null;
                }
                if (rs != null) {
                    rs.close();
                    rs = null;
                }
            } catch (Exception x) {
                con = null;
                stmt = null;
                rs = null;
            }
        }
        return tableList;
    }

    private List<String> getMsSqlTableList(DbInfo dbInfo, DBType dbType) {
        //throw new UnsupportedOperationException("Not supported yet."); // To change body of generated methods, choose
        // Tools | Templates.
        //throw new UnsupportedOperationException("Not supported yet."); // To change body of generated methods, choose
        // Tools | Templates.
        Connection con = null;
        Statement stmt = null;
        ResultSet rs = null;
        List<String> tableList = new ArrayList<String>();
        try {
            con = getConnection(dbInfo.getJdbcUrl(), dbInfo.getUserName(), dbInfo.getPassword());
            logger.info("Mysql select  table list connection successfully");
            stmt = con.createStatement();

            rs = stmt.executeQuery("select name from sys.tables");
            while (rs.next()) {
                tableList.add(rs.getString(1));
            }
        } catch (Exception ex) {
            logger.error("Mysql show table list connetion error " + ex.getMessage());
            throw new RuntimeException(ex);
        } finally {
            try {
                if (con != null) {
                    con.close();
                    con = null;
                }
                if (stmt != null) {
                    stmt.close();
                    stmt = null;
                }
                if (rs != null) {
                    rs.close();
                    rs = null;
                }
            } catch (Exception x) {
                con = null;
                stmt = null;
                rs = null;
            }
        }
        return tableList;
    }

    private List<String> getPostgresTableList(DbInfo dbInfo, DBType dbType) {
        // throw new UnsupportedOperationException("Not supported yet."); // To change body of generated methods, choose
        // Tools | Templates.
        Connection con = null;
        Statement stmt = null;
        ResultSet rs = null;
        List<String> tableList = new ArrayList<String>();
        try {
            con = getConnection(dbInfo.getJdbcUrl(), dbInfo.getUserName(), dbInfo.getPassword());
            logger.info("PostGres select  table list connection successfully");
            stmt = con.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY,
                    ResultSet.FETCH_FORWARD);

            rs = stmt.executeQuery("select * from information_schema.tables");
            while (rs.next()) {
                tableList.add(rs.getString(1));
            }
        } catch (Exception ex) {
            logger.error("Postgres table list connetion error " + ex.getMessage());
            throw new RuntimeException(ex);
        } finally {
            try {
                if (con != null) {
                    con.close();
                    con = null;
                }
                if (stmt != null) {
                    stmt.close();
                    stmt = null;
                }
                if (rs != null) {
                    rs.close();
                    rs = null;
                }
            } catch (Exception x) {
                con = null;
                stmt = null;
                rs = null;
            }
        }
        return tableList;
    }

    public Optional<DbInfo> getDbConfigById(long i) {
        return dBRepository.findById(1L);
    }

    public List getJsonArrayAsList(JSONArray jsona) {
        return jsona.toList();
    }
}
