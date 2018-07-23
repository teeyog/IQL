package cn.i4.report.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class TreeBuilder {
	
	JSONArray nodes = new JSONArray();
	
	public TreeBuilder() {
		
	}
	
	/**
	 * @param nodes 初始数据
	 * @param node 自定义节点结构
	 * */
	public TreeBuilder(JSONArray nodes) {
		this.nodes = nodes;
	}
	
	public JSONArray buildTree(JSONArray nodes) {
		TreeBuilder treeBuilder = new TreeBuilder(nodes);
		return treeBuilder.buildTree();
	}

    // 构建树形结构
    public JSONArray buildTree() {
        JSONArray treeNodes = new JSONArray();
        JSONArray rootNodes = getRootNodes();
        for (int i=0; i<rootNodes.size(); i++) {
        	JSONObject rootNode = rootNodes.getJSONObject(i);
        	buildChildNodes(rootNode);
            treeNodes.add(rootNode);
        }
        return treeNodes;
    }

    // 递归子节点
    public void buildChildNodes(JSONObject node) {
        JSONArray children = getChildNodes(node);
        if (!children.isEmpty()) {
        	for (int i=0; i<children.size(); i++) {
        		JSONObject child = children.getJSONObject(i);
        		buildChildNodes(child);
        	}
            node.put("children", children);
        }
    }

    // 获取父节点下所有的子节点
    public JSONArray getChildNodes(JSONObject pnode) {
        JSONArray childNodes = new JSONArray();
        for (int i=0; i<nodes.size(); i++) {
        	JSONObject n = nodes.getJSONObject(i);
            if (pnode.getString("id").equals(n.getString("pid"))) {
                childNodes.add(n);
            }
        }
        return childNodes;
    }

    // 判断是否为根节点
    public boolean rootNode(JSONObject node) {
        boolean isRootNode = true;
        for (int i=0; i<nodes.size(); i++) {
        	JSONObject n = nodes.getJSONObject(i);
            if (node.getString("pid").equals(n.getString("id"))) {
                isRootNode = false;
                break;
            }
        }
        return isRootNode;
    }

    // 获取集合中所有的根节点
    public JSONArray getRootNodes() {
        JSONArray rootNodes = new JSONArray();
        for (int i=0; i<nodes.size(); i++ ) {
        	JSONObject n = nodes.getJSONObject(i);
            if (rootNode(n)) {
                rootNodes.add(n);
            }
        }
        return rootNodes;
    }
    
}


