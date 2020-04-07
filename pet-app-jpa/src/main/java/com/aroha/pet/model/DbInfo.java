package com.aroha.pet.model;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import com.aroha.pet.model.audit.UserDateAudit;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 *
 * @author Sony George | Date : 13 Mar, 2019 12:31:25 PM
 */
@Entity
@Table(name = "db_info")
public class DbInfo extends UserDateAudit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    @Size(max = 20)
    private String dbType;
    @NotBlank
    @Size(max = 250)
    private String ipAddress;
    @NotNull
    private int portNo;
    private String dbName;
    private String schemaName;
    @NotBlank
    private String userName;
    @NotBlank
    private String password;
    @Size(max = 150)
    private String jdbcUrl;
    private String status;//Public or Private

    @ManyToMany(mappedBy = "dbs")
    private Set<User> users = new HashSet<User>();

    @JsonIgnore
    public Set<User> getUsers() {
        return users;
    }

    @JsonIgnore
    public void setUsers(Set<User> users) {
        this.users = users;
    }

    public DbInfo() {
        status = "private";
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDbType() {
        return dbType;
    }

    public void setDbType(String dbType) {
        this.dbType = dbType;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public int getPortNo() {
        return portNo;
    }

    public void setPortNo(int portNo) {
        this.portNo = portNo;
    }

    public String getDbName() {
        return dbName;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    public String getSchemaName() {
        return schemaName;
    }

    public void setSchemaName(String schemaName) {
        this.schemaName = schemaName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getJdbcUrl() {
        if (jdbcUrl == null || jdbcUrl.isEmpty()) {
            setJdbcUrl(constructJdbcUrl());
        }
        return jdbcUrl;
    }

    public void setJdbcUrl(String jdbcUrl) {
        this.jdbcUrl = jdbcUrl;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    //	@JsonIgnore
    //	@Transient
    private String constructJdbcUrl() {

        DBType dbTypeLocalVar = DBType.valueOf(getDbType().toUpperCase());
        String jdbcUrlVar = "";
        if (dbTypeLocalVar.getName().contains("MYSQL")) {
            jdbcUrlVar = "jdbc:mysql://" + getIpAddress() + ":" + getPortNo() + "/" + getDbName();
        }
        if (dbTypeLocalVar.getName().contains("maria")) {
            jdbcUrlVar = "jdbc:mariadb://" + getIpAddress() + ":" + getPortNo() + "/" + getDbName();
        }
        if (dbTypeLocalVar.getName().contains("oracle")) {
            jdbcUrlVar = "jdbc:oracle:thin:@" + getIpAddress() + ":" + getPortNo() + ":" + getSchemaName();
        }
        if (dbTypeLocalVar.getName().contains("postgres")) {
            jdbcUrlVar = "jdbc:postgresql://" + getIpAddress() + ":" + getPortNo() + "/" + getSchemaName();

        }
        if (dbTypeLocalVar.getName().contains("mssql")) {
            jdbcUrlVar = "jdbc:sqlserver://" + getIpAddress() + ":" + getPortNo() + ";databaseName=" + getDbName();
        }
        if (jdbcUrlVar.isEmpty()) {
            throw new RuntimeException("Could not constrct JDBC Url from this Object !!!" + this.toString());
        }
        return jdbcUrlVar;
    }

    @Override
    public String toString() {
        return "DbInfo{" + "id=" + id + ", dbType=" + dbType + ", ipAddress=" + ipAddress + ", portNo=" + portNo + ", dbName=" + dbName + ", schemaName=" + schemaName + ", userName=" + userName + ", password=" + password + ", jdbcUrl=" + jdbcUrl + ", status=" + status + '}';

        //return "DbInfo{" + "id=" + id + ", dbType=" + dbType + ", ipAddress=" + ipAddress + ", portNo=" + portNo + ", dbName=" + dbName + ", userName=" + userName + ", password=" + password + ", jdbcUrl=" + jdbcUrl + ", status=" + status + '}';
    }

}
