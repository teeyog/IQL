package cn.i4.iql.http.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataUtil {

	public static boolean isNumeric(String str){ 
		   Pattern pattern = Pattern.compile("[0-9]+"); 
		   Matcher isNum = pattern.matcher(str);
		   if( !isNum.matches() ){
		       return false; 
		   } 
		   return true; 
	}
	
	public static void sort(JSONArray array, final String field, final String order) {
		Collections.sort(array, new Comparator<Object>() {

			@Override
			public int compare(Object o1, Object o2) {
				JSONObject op = (JSONObject)o1;
				JSONObject oc = (JSONObject)o2;
				if(field==null||order==null){
					return 0;
				}

				if(isNumeric(op.getString(field)) && isNumeric(oc.getString(field))) {
					Double f1 = op.getDouble(field);
					Double f2 = oc.getDouble(field);
					return  f1.compareTo(f2);
				} else {
					String f1 = op.getString(field);
					String f2 = oc.getString(field);
					return f1.compareTo(f2);
				}
			}
		});
		if ("desc".equals(order)) {
			Collections.reverse(array);
		}
	}
	
	public static JSONObject JSONObjectSort(JSONObject object, String order) {
		JSONObject res = new JSONObject(true);
		JSONArray array = new JSONArray();
		for(String key : object.keySet()) {
			JSONObject o = new JSONObject();
			o.put("name", key);
			o.put("count", object.getInteger(key));
			array.add(o);
		}
		sort(array, "count", order);
		for(int i=0; i<array.size(); i++) {
			JSONObject a = array.getJSONObject(i);
			res.put(a.getString("name"), a.getInteger("count"));
		}
		return res;
	}
	
	/**
	 * 分页JSONArray
	 * */
	public static JSONArray pageFormat(JSONArray rows, int offset, int limit) {
		JSONArray res = new JSONArray();
		for(int i=0; i<rows.size(); i++) {
			if(i>=offset) {
				res.add(rows.getJSONObject(i));
				if(--limit==0)
					break;
			}
		}
		return res;
	}

	public static List pageFormate(List list, Integer offset, Integer limit){
		int total = list.size();
		int startIdx = ((offset == 0 ? 1 : offset / limit+1)-1)*limit;
		int endIdx = ((offset == 0 ? 1 : offset / limit+1))*limit;
		if(endIdx>total){
			endIdx = total;
		}
		list = list.subList(startIdx, endIdx);
		return list;
	}
	
	/**
	 * 分页JSONObject
	 * */
	public static JSONArray pageFormat(JSONObject rows, int offset, int limit) {
		JSONArray res = new JSONArray();
		for(String key : rows.keySet()) {
			if(offset--<=0) {
				JSONObject o = new JSONObject();
				o.put("name", key);
				o.put("count", rows.getDouble(key));
				res.add(o);
				if(--limit==0)
					break;
			}
		}
		return res;
	}
	
	/**
	 * 合并JSONArray
	 * */
	public static JSONArray mergeArray(JSONArray one, JSONArray two, String[] field) {
		JSONArray res = new JSONArray();
		JSONObject mergeObj = new JSONObject();
		for(int i=0; i<one.size(); i++) {
			JSONObject row = one.getJSONObject(i);
			mergeObj.put(row.getString(field[0]), row.getDouble(field[1]));
		}
		for(int i=0; i<two.size(); i++) {
			JSONObject row = two.getJSONObject(i);
			String name = row.getString(field[0]);
			double count = row.getDouble(field[1]);
			if(mergeObj.containsKey(name)) {
				mergeObj.put(name, mergeObj.getDouble(name) + count);
			} else {
				mergeObj.put(name, count);
			}
		}
		for(String key : mergeObj.keySet()) {
			JSONObject reso = new JSONObject();
			for(int i=0; i<one.size(); i++) {
				JSONObject o = one.getJSONObject(i);
				if(o.getString(field[0]).equals(key)) {
					o.put(field[1], mergeObj.getDouble(key));
					reso = o;
				}
			}
			for(int i=0; i<two.size(); i++) {
				JSONObject o = two.getJSONObject(i);
				if(o.getString(field[0]).equals(key)) {
					o.put(field[1], mergeObj.getDouble(key));
					reso = o;
				}
			}
			res.add(reso);
		}
		return res;
	}
	
	public static int dateCompare(String date1, String date2) {
		date1 = date1.replace("-", "");
		date2 = date2.replace("-", "");
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
		try {
			Date date01 = df.parse(date1);
			Date date02 = df.parse(date2);
			return date01.compareTo(date02);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	/**
	 * @param columnName 数值列
	 * @param alaisName 列别名
	 * */
	public static JSONArray fullJoin(JSONArray rows1, JSONArray rows2, String columnName, String alaisName) {
		JSONArray res = new JSONArray();
		int length1 = rows1.size();
		int length2 = rows2.size();
		int i=0, j=0;
		Set<String> columns = rows1.getJSONObject(0).keySet();
		while(i<length1 || j<length2) {
			if(i==length1 && j<length2) {
				JSONObject row = rows2.getJSONObject(j);
				JSONObject o = new JSONObject();
				for(String column: columns) {
					o.put(column, null);
				}
				o.put("date", row.getString("date"));
				o.put(alaisName, row.getDouble(columnName));
				res.add(o);
				j++;
			} else if(i<length1 && j==length2) {
				JSONObject row = rows1.getJSONObject(i);
				row.put(alaisName, null);
				res.add(row);
				i++;
			} else {
				JSONObject row1 = rows1.getJSONObject(i);
				JSONObject row2 = rows2.getJSONObject(j);
				int dateDiff = dateCompare(row1.getString("date"), row2.getString("date"));
				if(dateDiff < 0) {
					row1.put(alaisName, null);
					res.add(row1);
					i++;
				} else if(dateDiff > 0) {
					JSONObject o = new JSONObject();
					for(String column: columns) {
						o.put(column, null);
					}
					o.put("date", row2.getString("date"));
					o.put(alaisName, row2.getDouble(columnName));
					res.add(o);
					j++;
				} else {
					row1.put(alaisName, row2.getDouble(columnName));
					res.add(row1);
					i++;
					j++;
				}
			}
		}
		return res;
	}
	
	public static Double divide(Double dividend,Double divisor,int scale){
		if(scale<0){
            throw new IllegalArgumentException("The scale must be a positive integer or zero");
        }
		if(divisor==0.0){
        	return 0.0;
        }
        BigDecimal b1 = new BigDecimal(Double.toString(dividend));
        BigDecimal b2 = new BigDecimal(Double.toString(divisor));
        return b1.divide(b2,scale,BigDecimal.ROUND_HALF_UP).doubleValue();
	}

	
}
