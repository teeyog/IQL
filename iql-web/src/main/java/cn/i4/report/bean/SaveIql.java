package cn.i4.report.bean;

import com.alibaba.fastjson.JSONObject;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "save_iql")
public class SaveIql {

    @Id
    @GeneratedValue
    private Long id;
    @Column(nullable = true)
    private String iql;
    @Column(nullable = false)
    private String mode;
    @Column(nullable = false)
    private String name;
    @Column(nullable = true)
    private String description;
    @Column(nullable = false,name = "create_time")
    private Timestamp createTime;
    @Column(nullable = false,name = "update_time")
    private Timestamp updateTime;

    public SaveIql() {
    }

    public SaveIql(String iql, String mode, String name, String description, Timestamp createTime, Timestamp updateTime) {
        this.iql = iql;
        this.mode = mode;
        this.name = name;
        this.description = description;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }

    public Timestamp getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Timestamp updateTime) {
        this.updateTime = updateTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String descirption) {
        this.description = descirption;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public JSONObject toJSON() {
        JSONObject object = new JSONObject();
        object.put("id",id);
        object.put("iql",iql);
        object.put("mode",mode);
        object.put("name",name);
        object.put("description",description);
        object.put("createTime",createTime);
        object.put("updateTime",updateTime);
        return object;
    }
}
