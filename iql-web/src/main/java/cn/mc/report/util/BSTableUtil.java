package cn.mc.report.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class BSTableUtil {
	
	public static JSONArray rowspan(JSONArray rows, String column) {
		JSONArray res = new JSONArray();
		int count = 0;
		int index = 0;
		int classes = 1;
		String v = "";
		for(int i=0; i<rows.size(); i++) {
			JSONObject row = rows.getJSONObject(i);
			String name = row.getString(column);
			if(v.equals(name)) {
				count += 1;
				if(i == rows.size()-1) {
					JSONArray a = new JSONArray();
					a.add(0, index);
					a.add(1, count);
					res.add(a);
				}
			} else {
				if(!v.equals("")) {
					JSONArray a = new JSONArray();
					a.add(0, index);
					a.add(1, count);
					res.add(a);
					index = i;
					classes += 1;
				}
				v = name;
				count = 1;
			}
			row.put("classes", classes);
		}
		return res;
	}
	
	public static JSONObject rowspan(JSONArray rows, String...columns) {
		JSONObject o = new JSONObject();
		JSONObject value = new JSONObject();
		for(String column : columns) {
			o.put(column, new JSONArray());
			value.put(column, "");
		}
		for(int i=0; i<rows.size(); i++) {
			JSONObject row = rows.getJSONObject(i);
			for(int j=0; j<columns.length; j++) {
				String column = columns[j];
				String v = row.getString(column);
				if(v.equals(value.getString(column))) {
					JSONArray idxData = o.getJSONArray(column);
					int idx = idxData.size()-1;
					idxData.set(idx, idxData.getInteger(idx)+1);
				} else {
					o.getJSONArray(column).add(1);
					value.put(column, v);
					for(int k=j+1; k<columns.length; k++ ) {
						value.put(columns[k], "");
					}
				}
			}
		}
		return o;
	}
	
}
