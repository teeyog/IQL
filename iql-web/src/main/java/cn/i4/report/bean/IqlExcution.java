package cn.i4.report.bean;

import com.alibaba.fastjson.JSONObject;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "iql_excution")
public class IqlExcution {

    @Id
    @GeneratedValue
    private Long id;
    @Column(nullable = true)
    private String iql;
    @Column(nullable = true)
    private String code;
    @Column(nullable = false,name = "start_time")
    private Timestamp startTime;
    @Column(nullable = true,name = "take_time")
    private Long takeTime;
    @Column(nullable = false,name = "is_success")
    private Boolean isSuccess;
    @Column(nullable = true,name = "result_path")
    private String resultPath;
    @Column(nullable = true,name = "description")
    private String description;
    @Column(nullable = true,name = "error_message")
    private String errorMessage;
    @Column(nullable = true,name = "table_schema")
    private String tableSchema;
    @Column(nullable = true,name = "variables")
    private String variables;

    public IqlExcution() {
    }

    public IqlExcution(String iql, String code, Timestamp startTime, Long takeTime, Boolean isSuccess, String resultPath, String description, String errorMessage, String tableSchema, String variables) {
        this.iql = iql;
        this.code = code;
        this.startTime = startTime;
        this.takeTime = takeTime;
        this.isSuccess = isSuccess;
        this.resultPath = resultPath;
        this.description = description;
        this.errorMessage = errorMessage;
        this.tableSchema = tableSchema;
        this.variables = variables;
    }

    public String getVariables() {
        return variables;
    }

    public void setVariables(String variables) {
        this.variables = variables;
    }

    public String getTableSchema() {
        return tableSchema;
    }

    public void setTableSchema(String tableSchema) {
        this.tableSchema = tableSchema;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
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
        return isSuccess;
    }

    public void setSuccess(Boolean success) {
        isSuccess = success;
    }

    public String getResultPath() {
        return resultPath;
    }

    public void setResultPath(String resultPath) {
        this.resultPath = resultPath;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public JSONObject toJSON() {
        JSONObject object = new JSONObject();
        object.put("id",id);
        object.put("iql",iql);
        object.put("code",code);
        object.put("startTime",startTime);
        object.put("takeTime",takeTime);
        object.put("isSuccess",isSuccess);
        object.put("resultPath",resultPath);
        object.put("description",description);
        object.put("tableSchema",tableSchema);
        object.put("variables",variables);
        return object;
    }
}
