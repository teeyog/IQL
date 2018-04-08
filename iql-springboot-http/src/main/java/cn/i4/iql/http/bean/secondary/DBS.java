package cn.i4.iql.http.bean.secondary;

import javax.persistence.*;

@Entity
@Table(name = "DBS")
public class DBS {
    @Id
    @GeneratedValue
    @Column(name = "DB_ID")
    private Long dbId;
    @Column(nullable = true,name = "DESC")
    private String desc;
    @Column(nullable = false,name = "DB_LOCATION_URI")
    private String dbLocationUrl;
    @Column(nullable = true,name = "NAME")
    private String name;
    @Column(nullable = true,name = "OWNER_NAME")
    private String ownerName;
    @Column(nullable = true,name = "OWNER_TYPE")
    private String ownerType;

    public Long getDbId() {
        return dbId;
    }

    public void setDbId(Long dbId) {
        this.dbId = dbId;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getDbLocationUrl() {
        return dbLocationUrl;
    }

    public void setDbLocationUrl(String dbLocationUrl) {
        this.dbLocationUrl = dbLocationUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getOwnerType() {
        return ownerType;
    }

    public void setOwnerType(String ownerType) {
        this.ownerType = ownerType;
    }
}
