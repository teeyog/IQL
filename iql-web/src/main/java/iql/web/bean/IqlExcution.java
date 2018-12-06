package iql.web.bean;

import com.alibaba.fastjson.JSONObject;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

@Entity
@Table(name = "iql_excution")
public class IqlExcution implements Serializable{

    @Id
    @GeneratedValue
    private Long id;
    @Column(nullable = true)
    private String iql;
    @Column(nullable = false)
    private String mode;
    @Column(nullable = false,name = "start_time")
    private Timestamp startTime;
    @Column(nullable = true,name = "take_time")
    private Long takeTime;
    @Column(nullable = false,name = "is_success")
    private Boolean success;
    @Column(nullable = true,name = "result_path")
    private String hdfsPath;
    @Column(nullable = true,name = "user")
    private String user;
    @Column(nullable = true,name = "data")
    private String data;
    @Column(nullable = true,name = "data_type")
    private String dataType;
    @Column(nullable = true,name = "table_schema")
    private String schema;
    @Column(nullable = true,name = "variables")
    private String variables;

    public IqlExcution() {
    }

    public IqlExcution(String iql, String mode, Timestamp startTime, Long takeTime, Boolean success, String hdfsPath, String user, String dataType, String data, String schema, String variables) {
        this.iql = iql;
        this.mode = mode;
        this.startTime = startTime;
        this.takeTime = takeTime;
        this.success = success;
        this.hdfsPath = hdfsPath;
        this.user = user;
        this.dataType = dataType;
        this.data = data;
        this.schema = schema;
        this.variables = variables;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getVariables() {
        return variables;
    }

    public void setVariables(String variables) {
        this.variables = variables;
    }

    public String getSchema() {
        return schema;
    }

    public void setSchema(String schema) {
        this.schema = schema;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIql() {
        return iql;
    }

    public void setIql(String iql) {
        this.iql = iql;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public Timestamp getStartTime() {
        return startTime;
    }

    public void setStartTime(Timestamp startTime) {
        this.startTime = startTime;
    }

    public Long getTakeTime() {
        return takeTime;
    }

    public void setTakeTime(Long takeTime) {
        this.takeTime = takeTime;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getHdfsPath() {
        return hdfsPath;
    }

    public void setHdfsPath(String hdfsPath) {
        this.hdfsPath = hdfsPath;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public JSONObject toJSON() {
        JSONObject object = new JSONObject();
        object.put("id",id);
        object.put("iql",iql);
        object.put("mode",mode);
        object.put("startTime",startTime);
        object.put("takeTime",takeTime);
        object.put("isSuccess",success);
        object.put("hdfsPath",hdfsPath);
        object.put("user",user);
        object.put("data",data);
        object.put("dataType",dataType);
        object.put("schema",schema);
        object.put("variables",variables);
        return object;
    }
}
