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
    public JSONObject update(Integer id, String name, Integer pid, Integer isParent) {
        JSONObject obj = new JSONObject();
        if(id == null){
            JobScript jobScript = new JobScript();
            jobScript.setName(name);
            jobScript.setPid(pid);
            jobScript.setIsParent(isParent);
            JobScript script = jobScriptRepository.save(jobScript);
            jobScriptRepository.updateIsParent(1,pid);
            obj.put("id",script.getId());
        }else{
            jobScriptRepository.updateOne(name, pid, isParent, id);
            obj.put("id",id);
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
    public void drag(Integer id,Integer pid,Integer targetPid) {
        jobScriptRepository.updatePid(targetPid,id);
        List<JobScript> byPid = jobScriptRepository.findByPid(pid);
        if(byPid.size() == 0){
            jobScriptRepository.updateIsParent(0,pid);
        }
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

}
