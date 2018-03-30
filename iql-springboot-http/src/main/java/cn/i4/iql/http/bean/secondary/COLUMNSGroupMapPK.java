package cn.i4.iql.http.bean.secondary;

import java.io.Serializable;

public class COLUMNSGroupMapPK  implements Serializable {

    private Long cdId;
    private String columnName;

    public Long getCdId() {
        return cdId;
    }

    public void setCdId(Long cdId) {
        this.cdId = cdId;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }
}
