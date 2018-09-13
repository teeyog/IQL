package iql.web.util;

import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class EChartsUtil {
	
	/**
	 * 一列一条线
	 * */
	public static JSONObject toEChartsLineJSON(JSONArray rows, String[] columns, String[] columnNames, String date) {
		JSONObject res = new JSONObject();
		JSONArray xdata = new JSONArray();
		JSONArray series = new JSONArray();
		
		JSONObject dataObj = new JSONObject();
		for(String column : columns) {
			dataObj.put(column, new JSONArray());
		}
		for(int i=0; i<rows.size(); i++) {
			JSONObject row = rows.getJSONObject(i);
			xdata.add(row.getString(date));
			for(String column : columns) {
				dataObj.getJSONArray(column).add(row.getDouble(column));
			}
		}
		for(int i=0; i<columns.length; i++) {
			JSONObject seriesObj = new JSONObject();
			seriesObj.put("name", columnNames[i]);
			seriesObj.put("data", dataObj.getJSONArray(columns[i]));
			seriesObj.put("type", "line");
			seriesObj.put("smooth", true);
			series.add(seriesObj);
		}
		res.put("series", series);
		res.put("xdata", xdata);
		res.put("legend", JSON.parseArray(JSON.toJSONString(columnNames)));
		return res;
	}
	
	/**
	 * 只有一列 一个类型一条线
	 * */
	public static JSONObject toEChartsLineJSON(JSONArray rows, JSONObject typeNames, String typeColumn, String countColumn, String date) {
		JSONObject res = new JSONObject();
		Set<String> xset = new LinkedHashSet<>();
		JSONObject object = new JSONObject(true);
		JSONArray series = new JSONArray();
		JSONArray legend = new JSONArray();
		
		for(int i=0; i<rows.size(); i++) {
			JSONObject row = rows.getJSONObject(i);
			xset.add(row.getString(date));
		}
		for(String key : typeNames.keySet()) {
			object.put(key, new JSONArray());
		}
		for(Iterator<String> it=xset.iterator(); it.hasNext(); ) {
			String xName = it.next();
			for(String key : object.keySet()) {
				double count = 0;
				for(int i=0; i<rows.size(); i++) {
					JSONObject row = rows.getJSONObject(i);
					if(xName.equals(row.getString(date)) && key.equals(row.getString(typeColumn))) {
						count = row.getDouble(countColumn);
						break;
					}
				}
				object.getJSONArray(key).add(count);
			}
		}
		for(String key : object.keySet()) {
			legend.add(typeNames.getString(key));
			JSONObject so = new JSONObject();
			so.put("name", typeNames.getString(key));
			so.put("data", object.getJSONArray(key));
			so.put("type", "line");
			so.put("smooth", true);
			series.add(so);
		}
		res.put("legend", legend);
		res.put("xdata", JSON.parseArray(JSON.toJSONString(xset)));
		res.put("series", series);
		return res;
	}
	
	/**
	 * 饼图JSONObject
	 * */
	public static JSONObject toEChartsPieJSON(JSONObject rows, String[] columns, String[] columnNames) {
		JSONObject res = new JSONObject();
		JSONArray data = new JSONArray();
		
		for(int i=0; i<columns.length; i++) {
			JSONObject dataObj = new JSONObject();
			dataObj.put("name", columnNames[i]);
			dataObj.put("value", rows.getDouble(columns[i]));
			data.add(dataObj);
		}
		res.put("series", data);
		res.put("legend", JSON.parseArray(JSON.toJSONString(columnNames)));
		return res;
	}
	
	/**
	 * 饼图JSONArray
	 * @param limit 最多显示扇区数 -1显示全部
	 * */
	public static JSONObject toEChartsPieJSON(JSONArray rows, String[] columns, int limit) {
		JSONObject res = new JSONObject();
		JSONArray data = new JSONArray();
		JSONArray legend = new JSONArray();
		
		int maxSize = limit;
		double otherTotal = 0;
		for(int i=0; i<rows.size(); i++) {
			JSONObject row = rows.getJSONObject(i);
			String name = row.getString(columns[0]);
			double count = row.getDouble(columns[1]);
			if(count>0) {
				if(limit==-1) {
					maxSize = data.size()+1;
				}
				if(data.size()<maxSize) {
					JSONObject o = new JSONObject();
					o.put("name", name);
					o.put("value", count);
					data.add(o);
					legend.add(name);
				} else {
					otherTotal += count;
				}
			}
		}
		if(otherTotal>0) {
			JSONObject o = new JSONObject();
			o.put("name", "其他");
			o.put("value", otherTotal);
			data.add(o);
			legend.add("其他");
		}
		res.put("series", data);
		res.put("legend", legend);
		return res;
	}
	
	/**
	 * 单系列柱状图
	 * 只有一行数据时， 一条对应一列
	 * */
	public static JSONObject toEChartsBarJSON(JSONObject row) {
		JSONObject res = new JSONObject();
		JSONArray xdata = new JSONArray();
		JSONArray series = new JSONArray();
		
		for(String key : row.keySet()) {
			xdata.add(key);
			series.add(row.getDouble(key));
		}
		res.put("xdata", xdata);
		res.put("series", series);
		return res;
	}
	
	/**
	 * 单系列柱状图
	 * 多行数据， 一条对应一行
	 * */
	public static JSONObject toEChartsBarJSON(JSONArray rows, String[] columns) {
		JSONObject res = new JSONObject();
		JSONArray xdata = new JSONArray();
		JSONArray series = new JSONArray();
		
		for(int i=0; i<rows.size(); i++) {
			JSONObject row = rows.getJSONObject(i);
			String name = row.getString(columns[0]);
			double count = row.getDouble(columns[1]);
			xdata.add(name);
			series.add(count);
		}
		res.put("xdata", xdata);
		res.put("series", series);
		return res;
	}
	
	/**
	 * 多系列柱状图
	 * 
	 * */
	public static JSONObject toEChartsMultiBarJSON(JSONArray rows, String[] columns, String[] columnNames, String legendName) {
		JSONObject res = new JSONObject();
		JSONArray legend = new JSONArray();
		JSONArray series = new JSONArray();
		
		JSONObject baro = new JSONObject();
		for(String column : columns) {
			baro.put(column, new JSONArray());
		}
		
		for(int i=0; i<rows.size(); i++) {
			JSONObject row = rows.getJSONObject(i);
			legend.add(row.getString(legendName));
			JSONArray data = new JSONArray();
			for(String column : columns) {
				data.add(row.getDouble(column));
			}
			
			JSONObject so = new JSONObject();
			so.put("name", row.getString(legendName));
			so.put("type", "bar");
			so.put("data", data);
			so.put("barMaxWidth", 60);
			series.add(so);
		}
		res.put("legend", legend);
		res.put("xdata", columnNames);
		res.put("series", series);
		return res;
	}
	
	/**
	 * 不能出现“x-x” 或同时“x-y”和“y-x”
	 * */
	public static JSONObject toEChartsSankeyJSON(JSONArray rows) {
		JSONObject res = new JSONObject();
		JSONArray nodes = new JSONArray();
		JSONArray links = new JSONArray();
		
		JSONObject nameo = new JSONObject();
		for(int i=0; i<rows.size(); i++) {
			JSONObject row = rows.getJSONObject(i);
			String from = row.getString("name").split("--")[0];
			String to = row.getString("name").split("--")[1];
			double value = row.getDouble("value");
			double total = row.getDouble("total");
	
			if(nameo.containsKey(to)) {
				JSONArray data = nameo.getJSONArray(to);
				for(int j=0; j<data.size(); j++) {
					JSONArray dj = data.getJSONArray(j);
					if(from.equals(dj.getString(0))) {
						to = to + "(1)";
						break;
					}
				}
			}

			if(!nameo.containsKey(from)) {
				nameo.put(from, new JSONArray());
			}
			JSONArray d1 = new JSONArray();
			d1.add(0, to);
			d1.add(1, value);
			d1.add(2, total);
			nameo.getJSONArray(from).add(d1);
			if(!nameo.containsKey(to)) 
				nameo.put(to, new JSONArray());
		}
		for(String key : nameo.keySet()) {
			JSONArray data = nameo.getJSONArray(key);
			JSONObject o = new JSONObject();
			o.put("name", key);
			nodes.add(o);
			
			if(!data.isEmpty()) {
				for(int i=0; i<data.size(); i++) {
					JSONArray di = data.getJSONArray(i);
					if(key.equals(di.getString(0))) {
						o.put("value", di.getDouble(1));
						o.put("total", di.getDouble(2));
					} else {
						JSONObject linko = new JSONObject();
						linko.put("source", key);
						linko.put("target", di.getString(0));
						linko.put("value", di.getDouble(1));
						linko.put("total", di.getDouble(2));
						links.add(linko);
					}
				}
			}
		}
		res.put("nodes", nodes);
		res.put("links", links);
		return res;
	}
	
	public static void main(String[] args) {
	
	}
	
}
