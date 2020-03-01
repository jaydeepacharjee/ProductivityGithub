package com.aroha.pet.controller;

import java.util.List;
import java.util.Optional;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.aroha.pet.exception.AppException;
import com.aroha.pet.model.DbInfo;
import com.aroha.pet.model.User;
import com.aroha.pet.payload.SqlRequest;
import com.aroha.pet.security.CurrentUser;
import com.aroha.pet.security.UserPrincipal;
import com.aroha.pet.service.DBService;
import com.aroha.pet.service.QueryInfoService;
import com.aroha.pet.service.UserService;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 *
 * @author Sony George | Date : 5 Mar, 2019 5:42:32 PM basic CRUD and a service
 * to test the database using the properties
 */
@RestController
@RequestMapping("/api/db")
public class DataBaseInfoController {

    @Autowired
    DBService dbService;
    @Autowired
    UserService userService;   
    @Autowired
    QueryInfoService quesrService;

    private static final Logger logger = LoggerFactory.getLogger(DataBaseInfoController.class);

    @GetMapping("/getAllDBInfo")
    public ResponseEntity<?> getAllDbInfoByCurrentUser(@CurrentUser UserPrincipal currentUser) {
        return ResponseEntity.ok(dbService.getAllDBInfoDataByUser(currentUser.getId()));
    }

    @GetMapping("/getDBInfo")
    public ResponseEntity<?> getAllDBInfoDataById(@CurrentUser UserPrincipal currentUser) {
        return ResponseEntity.ok(dbService.getAllDBInfoByUserId(currentUser.getId()));
    }

    @PostMapping("/saveDbConfig")
    public ResponseEntity<?> saveDbDetails(@Valid @RequestBody DbInfo dbInfo, @CurrentUser UserPrincipal currentUser) {
        DbInfo db = null;
        try {
            db = dbService.save(dbInfo);
            logger.info("Database saved successfully");
        } catch (Exception ex) {
            logger.error("Database not saved" + ex.getMessage());
            return ResponseEntity.ok(ex.getMessage());
        }
        Optional<User> users = userService.findByEmail(currentUser.getEmail());
        User user = users.get();
        user.getDbs().add(db);
        userService.save(user);
        logger.info("Database saved successfully for user" + user.getEmail());
        return ResponseEntity.ok("Database Configuration Successful");
    }

    @PostMapping("/executeQuery")
    public ResponseEntity<?> executeQuery(@Valid @RequestBody SqlRequest sqlReq, @CurrentUser UserPrincipal currentUser) {
//		System.out.println("Scenario : "+sqlReq.getScenario());
        return ResponseEntity.ok(dbService.executeQuery(sqlReq.getDbInfo(), sqlReq.getSql(), currentUser, sqlReq.getQuestion()));
    }

    @PostMapping("/getTablelist")
    public ResponseEntity<?> getTableList(@Valid @RequestBody DbInfo dbInfo) {
        List<String> list = dbService.getTableList(dbInfo);
        if (list.isEmpty()) {
            return ResponseEntity.ok("No table found");
        }
        return ResponseEntity.ok(list);
    }

    @PostMapping("/getColumnList/{tableName}")
    public ResponseEntity<?> getTableColumnList(@Valid @RequestBody DbInfo dbInfo, @PathVariable(value = "tableName") String tableName) {
        return ResponseEntity.ok(dbService.getTableColumnList(dbInfo, tableName));
    }

    @GetMapping("/test")
    public ResponseEntity<?> test() {
        SqlRequest sqlReq = new SqlRequest();
        DbInfo dbInfo = dbService.getDbConfigById(1L).orElseThrow(() -> new AppException("DbInfo not found for 1."));
        sqlReq.setDbInfo(dbInfo);
        sqlReq.setSql("select * from table limit 100");
        return ResponseEntity.ok(sqlReq);

    }

    /*	@PostMapping("/connect")
	public ResponseEntity<?> testConnection(@Valid @RequestBody DbInfo dbInfo, @CurrentUser UserPrincipal currentUser) {
		SqlRequest sqlReq = new SqlRequest();
		sqlReq.setDbInfo(dbInfo);
		String scenario="";
		if (dbInfo.getDbType().equalsIgnoreCase("MYSQL")) {
			sqlReq.setSql("select curdate()");
		} else if (dbInfo.getDbType().equalsIgnoreCase("mariadb")) {
			sqlReq.setSql("select curdate()");
		} else if (dbInfo.getDbType().equalsIgnoreCase("oracle")) {
			sqlReq.setSql("select * from emp");
		}
		else if (dbInfo.getDbType().equalsIgnoreCase("postgresql")) {
			sqlReq.setSql("select * from emp");
		}
		else if (dbInfo.getDbType().equalsIgnoreCase("mssql")) {
			sqlReq.setSql("SELECT  [s_id] ,[s_name],[s_phone]  FROM [test1].[dbo].[student]");
		}
		else {
			throw new RuntimeException("Connect is Implemented only for MySQL and Oracle");
		}
		SqlResponse sqlRes = dbService.executeQuery(dbInfo, sqlReq.getSql(), currentUser,scenario);
		return ResponseEntity.ok(sqlRes.getStatus());
	}
     */
    @RequestMapping(value = "/checkQuestion", method = RequestMethod.POST)
    public ResponseEntity<?> checkQuestionForId() {
        
        quesrService.getAllQuestions();
        return ResponseEntity.ok("Ok");
    }
}
