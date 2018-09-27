package iql.web.iql.action;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import iql.web.bean.DataSource;
import iql.web.iql.service.DataSourceRepository;
import iql.web.system.domain.Role;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/dataSource")
public class DataSourceAction {

    @Autowired
    private DataSourceRepository dataSourceRepository;


    public List<DataSource> getDataSourceListByPid(List<DataSource> dataSources,Integer pid){
        ArrayList<DataSource> returnResult = new ArrayList<>();
        Iterator<DataSource> iters = dataSources.iterator();
        while (iters.hasNext()){
            DataSource ds = iters.next();
            if(ds.getPid() == pid){
                returnResult.add(ds);
            }
        }
        return returnResult;
    }


    public JSONArray mapDown(List<DataSource> dataSourcesList,List<DataSource> dataSourceListByPid, List<Integer> ids){
        JSONArray array = new JSONArray();
        for(DataSource ds : dataSourceListByPid){
            JSONObject obj_1 = new JSONObject();
            obj_1.put("id",ds.getId());
            obj_1.put("text",ds.getName());
            obj_1.put("iconCls","icon-filter");
            if(ids.contains(ds.getId())){
                obj_1.put("checked", true);
            }
            List<DataSource> dataSourceListByPid_ = getDataSourceListByPid(dataSourcesList,ds.getId());
            if(dataSourceListByPid_.size() != 0){
                obj_1.put("children",mapDown(dataSourcesList,dataSourceListByPid_,ids));
            }
            array.add(obj_1);
        }
        return array;
    }

    @RequestMapping(value="/roleDataSource")
    public JSONArray findRoleDataSource(Integer id){
        Iterator<Object> iterator = dataSourceRepository.findRoleDataSourceByRoleId(id).iterator();
        List<Integer> ids = new ArrayList<>();
        while (iterator.hasNext()){
            Object[] objs = (Object[]) iterator.next();
            if(Integer.valueOf(objs[2].toString()) == 0){
                ids.add(Integer.valueOf(objs[0].toString()));
            }
        }
        List<DataSource> dataSourcesList = dataSourceRepository.findAll();
        JSONArray resultArray = new JSONArray();
        JSONObject obj = new JSONObject();
        obj.put("text", "<span style='font-weight:bold;color:#3F3F3F';font-size: 120%>全选</span>");
        obj.put("iconCls", "icon-filter");
        obj.put("state","open");
        List<DataSource> dataSourceListByPid = getDataSourceListByPid(dataSourcesList,1);
        obj.put("children",mapDown(dataSourcesList,dataSourceListByPid,ids));
        resultArray.add(obj);
        return resultArray;
    }

    /**
     * 获取datasource stree
     */
    @RequestMapping(value = "/tree")
    public JSONArray getStree(Integer id) {
        Integer pid = id;
        if(pid == null) pid = 0;
        List<DataSource> allDataSource = dataSourceRepository.findByPid(pid);
        return JSON.parseArray(JSON.toJSONString(allDataSource));
    }

    /**
     * 获取datasource stree
     */
    @PostMapping(value = "/update")
    public JSONObject update(Integer id, String name, Integer pid, Integer isParent, Integer sort) {
        JSONObject obj = new JSONObject();
        if(id == null){
            DataSource dataSource = new DataSource();
            dataSource.setName(name);
            dataSource.setPid(pid);
            dataSource.setIsParent(isParent);
            dataSource.setSort(sort);
            dataSource.setPath(dataSourceRepository.findOne(pid).getPath() + "." + name);
            DataSource datasource = dataSourceRepository.save(dataSource);
            dataSourceRepository.updateIsParent(1,pid);
            obj.put("id",datasource.getId());
            obj.put("sort",datasource.getSort());
        }else{
            dataSourceRepository.updateOne(name, pid, isParent,sort, id);
            obj.put("id",id);
            obj.put("sort",sort);
        }
        return obj;
    }

    /**
     * remove node
     */
    @PostMapping(value = "/remove")
    public void remove(Integer id,Integer pid,Integer isParent) {
        dataSourceRepository.delete(id);
        List<DataSource> byPid = dataSourceRepository.findByPid(pid);
        if(byPid.size() == 0){
            dataSourceRepository.updateIsParent(0,pid);
        }
        if(isParent == 1){
            removeByPid(id,dataSourceRepository);
        }
    }

    /**
     * drag node
     */
    @PostMapping(value = "/drag")
    public void drag(Integer id,Integer pid,Integer targetPid, Integer sort, Integer targetSort, String name) {
        System.out.println(pid + " -- " + targetPid);
        dataSourceRepository.updateDataSourceWhereSortGreatThan(targetPid,targetSort + 1);//将大于sort的目标节点都往后移一位
        dataSourceRepository.updatePid(targetPid,targetSort + 1,id);//设置新parent和sort
        dataSourceRepository.updatePathById(dataSourceRepository.findOne(targetPid).getPath() + "." + name,id);//跟新path
        updateChildNodesPathById(id,dataSourceRepository);//跟新子节点path
        List<DataSource> byPid = dataSourceRepository.findByPid(pid);
        if(byPid.size() == 0){
            dataSourceRepository.updateIsParent(0,pid);
        }
    }



    private static void removeByPid(Integer id,DataSourceRepository dataSourceRepository){
        List<DataSource> jobs = dataSourceRepository.findByPid(id);
        for(DataSource job : jobs){
            dataSourceRepository.delete(job.getId());
            if(job.getIsParent() == 1){
                removeByPid(job.getId(),dataSourceRepository);
            }
        }
    }

    private static void updateChildNodesPathById(Integer id,DataSourceRepository dataSourceRepository){
        List<DataSource> childs = dataSourceRepository.findByPid(id);
        for(DataSource js : childs){
            dataSourceRepository.updatePathById(dataSourceRepository.findOne(id).getPath() + "." + js.getName(),js.getId());
            updateChildNodesPathById(js.getId(),dataSourceRepository);
        }
    }

}
