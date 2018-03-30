package cn.i4.iql.http.bean.secondary;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "COLUMNS_V2")
@IdClass(COLUMNSGroupMapPK.class)
public class COLUMNS {

    @Id
    @Column(nullable = false,name = "CD_ID")
    private Long cdId;
    @Column(nullable = true,name = "COMMENT")
    private String comment;
    @Id
    @Column(nullable = false,name = "COLUMN_NAME")
    private String columnName;
    @Column(nullable = true,name = "TYPE_NAME")
    private String typeName;
    @Column(nullable = false,name = "INTEGER_IDX")
    private Integer integerIdx;

    public Long getCdId() {
        return cdId;
    }

    public void setCdId(Long cdId) {
        this.cdId = cdId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public Integer getIntegerIdx() {
        return integerIdx;
    }

    public void setIntegerIdx(Integer integerIdx) {
        this.integerIdx = integerIdx;
    }
}
