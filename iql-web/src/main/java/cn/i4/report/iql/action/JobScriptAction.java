package cn.i4.report.iql.action;

import cn.i4.report.bean.JobScript;
import cn.i4.report.iql.service.JobScriptRepository;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/jobScript")
public class JobScriptAction {

    @Autowired
    private JobScriptRepository jobScriptRepository;

    /**
     * 获取script stree
     */
    @RequestMapping(value = "/tree")
    public JSONArray getStree(Integer id) {
        Integer pid = id;
        if(pid == null) pid = 0;
        List<JobScript> allJobScript = jobScriptRepository.findByPid(pid);
        return JSON.parseArray(JSON.toJSONString(allJobScript));
    }

    /**
     * 获取script stree
     */
    @PostMapping(value = "/update")
    public JSONObject update(Integer id, String name, Integer pid, Integer isParent, Integer sort) {
        JSONObject obj = new JSONObject();
        if(id == null){
            JobScript jobScript = new JobScript();
            jobScript.setName(name);
            jobScript.setPid(pid);
            jobScript.setIsParent(isParent);
            jobScript.setSort(sort);
            jobScript.setPath(jobScriptRepository.findOne(pid).getPath() + "." + name);
            JobScript script = jobScriptRepository.save(jobScript);
            jobScriptRepository.updateIsParent(1,pid);
            obj.put("id",script.getId());
            obj.put("sort",script.getSort());
        }else{
            jobScriptRepository.updateOne(name, pid, isParent,sort, id);
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
        jobScriptRepository.delete(id);
        List<JobScript> byPid = jobScriptRepository.findByPid(pid);
        if(byPid.size() == 0){
            jobScriptRepository.updateIsParent(0,pid);
        }
        if(isParent == 1){
            removeByPid(id,jobScriptRepository);
        }
    }

    /**
     * drag node
     */
    @PostMapping(value = "/drag")
    public void drag(Integer id,Integer pid,Integer targetPid, Integer sort, Integer targetSort, String name) {
        System.out.println(pid + " -- " + targetPid);
        jobScriptRepository.updateScriptWhereSortGreatThan(targetPid,targetSort + 1);//将大于sort的目标节点都往后移一位
        jobScriptRepository.updatePid(targetPid,targetSort + 1,id);//设置新parent和sort
        jobScriptRepository.updatePathById(jobScriptRepository.findOne(targetPid).getPath() + "." + name,id);//跟新path
        updateChildNodesPathById(id,jobScriptRepository);//跟新子节点path
        List<JobScript> byPid = jobScriptRepository.findByPid(pid);
        if(byPid.size() == 0){
            jobScriptRepository.updateIsParent(0,pid);
        }
    }

    /**
     * get script
     */
    @RequestMapping(value = "/getScript")
    public JSONObject getScript(Integer id) {
        JSONObject obj = new JSONObject();
        obj.put("script",jobScriptRepository.getOne(id).getScript());
        return obj;
    }

    /**
     * get script
     */
    @RequestMapping(value = "/getScriptByPath")
    public String getScriptByPath(String packageName) {
        return jobScriptRepository.findByPath(packageName).iterator().next().getScript();
    }

    /**
     * update script
     */
    @RequestMapping(value = "/updateSript")
    public void updateSript(Integer id,String script) {
        jobScriptRepository.updateScriptById(script,id);
    }


    private static void removeByPid(Integer id,JobScriptRepository jobScriptRepository){
        List<JobScript> jobs = jobScriptRepository.findByPid(id);
        for(JobScript job : jobs){
            jobScriptRepository.delete(job.getId());
            if(job.getIsParent() == 1){
                removeByPid(job.getId(),jobScriptRepository);
            }
        }
    }

    private static void updateChildNodesPathById(Integer id,JobScriptRepository jobScriptRepository){
        List<JobScript> childs = jobScriptRepository.findByPid(id);
        for(JobScript js : childs){
            jobScriptRepository.updatePathById(jobScriptRepository.findOne(id).getPath() + "." + js.getName(),js.getId());
            updateChildNodesPathById(js.getId(),jobScriptRepository);
        }
    }

}
