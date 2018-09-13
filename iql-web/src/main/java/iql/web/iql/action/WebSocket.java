package iql.web.iql.action;

import org.springframework.stereotype.Component;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

//此处定义接口的uri
@ServerEndpoint("/log")
@Component
public class WebSocket {
    private Process process;
    private InputStream inputStream;

    /**
     * 新的WebSocket请求开启
     */
    @OnOpen
    public void onOpen(Session session) {
        String[] params = session.getRequestURI().toString().split("\\?")[1].split("&amp;");
        HashMap<String, String> map = new HashMap<>();
        for(String p : params){
            map.put(p.split("=")[0],p.split("=")[1]);
        }
        String appname = map.get("appname").split("\\[")[1].split("\\]")[0];
        String command = "ssh " + map.get("host") + " 'tail -f -n 100 /data/yarn/container-logs/" + appname + "/" + map.get("executor") + "/" + map.get("logtype") + "'";
        System.out.println(command);
        try {
            // 执行tail -f命令
            process = Runtime.getRuntime().exec(new String[]{"/bin/sh", "-c", command});
            inputStream = process.getInputStream();
            // 一定要启动新的线程，防止InputStream阻塞处理WebSocket的线程
            LogTailThread thread = new LogTailThread(inputStream, session);
            thread.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * WebSocket请求关闭
     */
    @OnClose
    public void onClose() {
        try {
            if(inputStream != null)
                inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(process != null)
            process.destroy();
    }

    @OnError
    public void onError(Throwable thr) {
        thr.printStackTrace();
    }
}

class LogTailThread extends Thread {

    private BufferedReader reader;
    private Session session;

    public LogTailThread(InputStream in, Session session) {
        this.reader = new BufferedReader(new InputStreamReader(in));
        this.session = session;
    }

    @Override
    public void run() {
        String line;
        try {
            while((line = reader.readLine()) != null) {
                // 将实时日志通过WebSocket发送给客户端，给每一行添加一个HTML换行
                session.getBasicRemote().sendText(line + "<br/>");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}