package iql.common.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class ShellUtils {

    private final static String[] HOSTS = {"dsj01"};

    /**
     * 运行一个shell命令
     */
    public static HashSet<String> runShellCommand(String command) throws IOException {
        HashSet<String> set = new HashSet<String>();
        try {
            Process process = Runtime.getRuntime().exec(new String[]{"/bin/sh", "-c", command});
            InputStream stderr = process.getInputStream();
            InputStreamReader isr = new InputStreamReader(stderr);
            BufferedReader br = new BufferedReader(isr);
            String line = null;
            while ((line = br.readLine()) != null) {
                set.add(line);
            }
            int exitValue = process.waitFor();
            if (0 != exitValue) {
                System.out.println("call shell failed. error code is :" + exitValue);
            }
            br.close();
        } catch (Throwable e) {
            System.out.println("call shell failed. " + e);
        }
        return set;
    }

    public static HashMap<String, HashMap<String, String>> getJobInfo() throws IOException {
//        HashMap<String, String> yarnJobs = getYarnJobs();
        HashMap<String, HashMap<String, String>> jobInfo = new HashMap<String, HashMap<String, String>>();
        for (String host : HOSTS){
            HashSet<String> appids = runShellCommand(" ssh " + host + " 'cd /data/yarn/container-logs/;ls'");
            for (String appid : appids){
                HashSet<String> executorIds = runShellCommand(" ssh " + host + " 'cd /data/yarn/container-logs/" + appid + ";ls'");
                for(String executorId : executorIds){
                    HashMap<String, String> appInfo = jobInfo.getOrDefault(appid,new HashMap<String, String>());
                    appInfo.put(executorId,host);
                    jobInfo.put(appid,appInfo);
                }
            }
        }
        return jobInfo;
    }

    public static HashMap<String,String> getYarnJobs()  throws IOException {
        HashSet<String> yarnJob = runShellCommand("ssh dsj01 yarn application -list | awk '{print $1,$2}' | grep application");
        HashMap<String, String> jobMap = new HashMap<>();
        for(String job : yarnJob){
            if (job.startsWith("application")) {
                String[] kv = job.split(" ");
                jobMap.put(kv[0],kv[1]);
            }
        }
        return jobMap;
    }

//


}
