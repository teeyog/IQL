package cn.i4.iql.http.util;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HttpUtils {

    /**
     * post请求
     * @param url
     * @param params
     * @param timeout 超时时间，秒
     * @return
     * @throws IOException
     */
    public static String post(String url, Map<String, String> params, int timeout, String charset) throws Exception {
        org.apache.http.client.HttpClient httpclient = new DefaultHttpClient();
        httpclient.getParams().setIntParameter("http.socket.timeout", timeout * 1000);
        httpclient.getParams().setBooleanParameter("http.protocol.expect-continue", false);
        String retVal = "";
        try {
            List<NameValuePair> formparams = new ArrayList<NameValuePair>();
            if (params != null) {
                for (Map.Entry<String, String> param : params.entrySet()) {
                    formparams.add(new BasicNameValuePair(param.getKey(), param.getValue()));
                }
            }
            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formparams, charset);
            HttpPost httppost = new HttpPost(url);
            httppost.setEntity(entity);
            HttpResponse resp = httpclient.execute(httppost);
            retVal = EntityUtils.toString(resp.getEntity(), charset);
        } catch (IOException e) {
            throw e;
        } finally {
            httpclient.getConnectionManager().shutdown();
        }
        return retVal;
    }


}
