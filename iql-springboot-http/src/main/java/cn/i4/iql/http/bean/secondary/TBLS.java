package cn.i4.iql.http.bean.secondary;

import javax.persistence.*;

@Entity
@Table(name = "TBLS")
public class TBLS {

    @Id
    @GeneratedValue
    @Column(name = "TBL_ID")
    private Long tblId;
    @Column(nullable = false,name = "CREATE_TIME")
    private Integer createTime;
    @Column(nullable = true,name = "DB_ID")
    private Long dbId;
    @Column(nullable = false,name = "LAST_ACCESS_TIME")
    private Integer lastAccessTime;
    @Column(nullable = true,name = "OWNER")
    private String owner;
    @Column(nullable = false,name = "RETENTION")
    private Integer petention;
    @Column(nullable = true,name = "SD_ID")
    private Long sdId;
    @Column(nullable = true,name = "TBL_NAME")
    private String tblName;
    @Column(nullable = true,name = "TBL_TYPE")
    private String tblType;
    @Column(nullable = true,name = "VIEW_EXPANDED_TEXT")
    private String viewExpandedText;
    @Column(nullable = true,name = "VIEW_ORIGINAL_TEXT")
    private String viewOriginalText;
    @Column(nullable = true,name = "LINK_TARGET_ID")
    private Long linkTargetId;

    public Long getTblId() {
        return tblId;
    }

    public void setTblId(Long tblId) {
        this.tblId = tblId;
    }

    public Integer getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Integer createTime) {
        this.createTime = createTime;
    }

    public Long getDbId() {
        return dbId;
    }

    public void setDbId(Long dbId) {
        this.dbId = dbId;
    }

    public Integer getLastAccessTime() {
        return lastAccessTime;
    }

    public void setLastAccessTime(Integer lastAccessTime) {
        this.lastAccessTime = lastAccessTime;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public Integer getPetention() {
        return petention;
    }

    public void setPetention(Integer petention) {
        this.petention = petention;
    }

    public Long getSdId() {
        return sdId;
    }

    public void setSdId(Long sdId) {
        this.sdId = sdId;
    }

    public String getTblName() {
        return tblName;
    }

    public void setTblName(String tblName) {
        this.tblName = tblName;
    }

    public String getTblType() {
        return tblType;
    }

    public void setTblType(String tblType) {
        this.tblType = tblType;
    }

    public String getViewExpandedText() {
        return viewExpandedText;
    }

    public void setViewExpandedText(String viewExpandedText) {
        this.viewExpandedText = viewExpandedText;
    }

    public String getViewOriginalText() {
        return viewOriginalText;
    }

    public void setViewOriginalText(String viewOriginalText) {
        this.viewOriginalText = viewOriginalText;
    }

    public Long getLinkTargetId() {
        return linkTargetId;
    }

    public void setLinkTargetId(Long linkTargetId) {
        this.linkTargetId = linkTargetId;
    }
}
