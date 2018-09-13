package iql.web.util;

import java.text.NumberFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class DataUtil {

	public static boolean isNumeric(String str){ 
		   Pattern pattern = Pattern.compile("[0-9]*"); 
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
	
	public static String myPercent(double num1, double num2, int Digits) {
		NumberFormat numberFormat = NumberFormat.getNumberInstance();
		numberFormat.setMaximumFractionDigits(Digits);
		return numberFormat.format(num1*100/num2)+"%";
	}
	
}
