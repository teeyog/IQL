package cn.i4.report.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

public class PhoenixHbaseUtils {
	
	public static Logger logger  =  Logger.getLogger("《Phoenix查询》");

	static final String JDBC_DRIVER_CLASS = "org.apache.phoenix.jdbc.PhoenixDriver";
    static final String JDBC_URL = "jdbc:phoenix:mcserverdsj01";
    static final String USER = null;
    static final String PASSWORD = null;
    static{
    	try {
            Class.forName(JDBC_DRIVER_CLASS);
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }
    }
    
    
    @SuppressWarnings("unchecked")
	public static List<Map> query(String sql) throws SQLException {
        Connection conn = (USER == null ? DriverManager.getConnection(JDBC_URL) : DriverManager.getConnection(JDBC_URL, USER, PASSWORD));
        Statement statement = conn.createStatement();
        logger.info("Phoenix: "+sql);
        ResultSet rs = statement.executeQuery(sql);
        ResultSetMetaData rsm = rs.getMetaData();
        int colCount = rsm.getColumnCount();
        boolean isFirst = true;
        int rowCount = 0;
        List<Map> list = new ArrayList<Map>();
        while (rs.next()) {
            rowCount ++;
            if(rowCount==500000){
            	break;
            }
            if (isFirst) {
                isFirst = false;
            } else {
            }
            Map map = new HashMap();
            for (int i = 0; i < colCount; i++) {
                String colName = rsm.getColumnName(i+1);
                Object val = rs.getObject(i+1);
                map.put(colName.toString(), val);
            }
            list.add(map);
        }
        rs.close();
        statement.close();
        conn.close();
		return list;
    }

    public static int upset(String sql) throws SQLException{
        Connection conn = (USER == null ? DriverManager.getConnection(JDBC_URL) : DriverManager.getConnection(JDBC_URL, USER, PASSWORD));
        Statement statement = conn.createStatement();
        statement.executeUpdate(sql);
        conn.commit();
        statement.close();
        conn.close();
        return 1;
    }
    
    public static JSONObject executeSQL(String sql) throws SQLException {
        Connection conn = (USER == null ? DriverManager.getConnection(JDBC_URL) : DriverManager.getConnection(JDBC_URL, USER, PASSWORD));
        Statement statement = conn.createStatement();
        logger.info("Phoenix: "+sql);
        ResultSet rs = statement.executeQuery(sql);
        ResultSetMetaData rsm = rs.getMetaData();
        int colCount = rsm.getColumnCount();
        boolean isFirst = true;
        int rowCount = 0;
        JSONArray array = new JSONArray();
        JSONObject result = new JSONObject();
        while (rs.next()) {
            rowCount ++;
            if(rowCount==50000){
            	break;
            }
            if (isFirst) {
                isFirst = false;
            } else {
            }
            JSONObject obj = new JSONObject();
            for (int i = 0; i < colCount; i++) {
                String colName = rsm.getColumnName(i+1);
                Object val = rs.getObject(i+1);
                if(colName.toString().equals("REMD")){
                	obj.put(colName.toString(), val==null?"":"#"+val.toString()+"#");
                }else{
                	 obj.put(colName.toString(), val==null?"":val.toString());
                }
            }
            array.add(obj);
        }
        rs.close();
        statement.close();
        conn.close();
        result.put("total", rowCount);
        result.put("rows", array);
		return result;
    }
    
    /**
     * 
     * @param sql
     * @return
     * @throws SQLException
     */
    public static JSONObject executeSQL(String sql,int row,int page) throws SQLException {
        Connection conn = (USER == null ? DriverManager.getConnection(JDBC_URL) : DriverManager.getConnection(JDBC_URL, USER, PASSWORD));
        Statement statement = conn.createStatement();
        statement.setMaxRows(row*page);
        logger.info("Phoenix: "+sql);
        ResultSet rs = statement.executeQuery(sql+"limit "+row*page);
        ResultSetMetaData rsm = rs.getMetaData();
        String countSql = "select count(*)  from"+sql.split("from")[1];
        int colCount = rsm.getColumnCount();
        int tableCount = getCount(countSql);
        int rowCount = 0;
        JSONArray array = new JSONArray();
        JSONObject result = new JSONObject();
        while (rs.next()) {
        	rowCount ++;
            if(rowCount>(page-1)*row && rowCount<=page*row){
            	 JSONObject obj = new JSONObject();
                 for (int i = 0; i < colCount; i++) {
                     String colName = rsm.getColumnName(i+1);
                     Object val = rs.getObject(i+1);
                     if(colName.toString().equals("REMD")){
                     	obj.put(colName.toString(), val==null?"":"#"+val.toString()+"#");
                     }else{
                     	 obj.put(colName.toString(), val==null?"":val.toString());
                     }
                 }
                 array.add(obj);
    		}
        }
        rs.close();
        statement.close();
        conn.close();
        result.put("total", tableCount);
        result.put("rows", array);
		return result;
    }
    
    public static int getCount(String sql) throws SQLException {
        Connection conn = (USER == null ? DriverManager.getConnection(JDBC_URL) : DriverManager.getConnection(JDBC_URL, USER, PASSWORD));
        Statement statement = conn.createStatement();
        ResultSet rs = statement.executeQuery(sql);
        int count = 0;
        while(rs.next()){   
        	count = rs.getInt(1) ; 
        } 
        rs.close();
        statement.close();
        conn.close();
        return count;
    }
    
    
    public static void main(String[] args) throws SQLException {
    	System.out.println("'"+ StringUtils.strip("65464,gh,54,", ",").replace(",", "','")+"'");
		//List<Map> list = PhoenixHbaseUtils.query("select appid,appname,sum(count) as total  from ai_idfa group by appid,appname order by total desc");

		
	}
}
