package cn.mc.report.util;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;


import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by admin on 2017/7/21.
 */
public class HttpUtils {


    public static String post(String url,String json,int timeout) throws Exception {
        String info = "";
        // 构造HttpClient的实例
        HttpClient httpClient = new HttpClient();
        // 创建GET方法的实例
        PostMethod postMethod = new PostMethod(url);
        // 使用系统提供的默认的恢复策略
        postMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,new DefaultHttpMethodRetryHandler());
        RequestEntity se = new StringRequestEntity(json, "application/json", "UTF-8");
        postMethod.setRequestEntity(se);
        //设置超时的时间
        postMethod.getParams().setParameter(HttpMethodParams.SO_TIMEOUT, timeout);

        try {
            // 执行getMethod
            int statusCode = httpClient.executeMethod(postMethod);
            if (statusCode != HttpStatus.SC_OK) {
                System.err.println("Method failed: "
                        + postMethod.getStatusLine());
            }
            BufferedReader reader = new BufferedReader(new InputStreamReader(postMethod.getResponseBodyAsStream()));
            StringBuffer stringBuffer = new StringBuffer();
            String str = "";
            while((str = reader.readLine())!=null){
                stringBuffer.append(str);
            }
            info = stringBuffer.toString();
        } catch (HttpException e) {
            // 发生致命的异常，可能是协议不对或者返回的内容有问题
            System.out.println("Please check your provided http address!");
            e.printStackTrace();
        } catch (IOException e) {
            // 发生网络异常
            e.printStackTrace();
        } finally {
            // 释放连接
            postMethod.releaseConnection();
        }
        return info;
    }

    public static String get(String url, Map<String, String> params, int timeout, String charset) throws Exception{
        Long st = System.currentTimeMillis();
        List<NameValuePair> qparams = new ArrayList<NameValuePair>();
        if (params != null) {
            for (Map.Entry<String, String> param : params.entrySet()) {
                qparams.add(new BasicNameValuePair(param.getKey(), param.getValue()));
            }
        }
        String paramstr = URLEncodedUtils.format(qparams,charset);
        if (StringUtils.isNotEmpty(paramstr)) {
            url = url + "?" + paramstr;
        }

        String info = "";
        // 构造HttpClient的实例
        HttpClient httpClient = new HttpClient();
        // 创建GET方法的实例
        GetMethod getMethod = new GetMethod(url);
        // 使用系统提供的默认的恢复策略
        getMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,new DefaultHttpMethodRetryHandler());
        try {
            // 执行getMethod
            int statusCode = httpClient.executeMethod(getMethod);
            if (statusCode != HttpStatus.SC_OK) {
                System.err.println("Method failed: "
                        + getMethod.getStatusLine());
            }
            // 读取内容
            InputStream inputStream = getMethod.getResponseBodyAsStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream,"UTF-8"));
            StringBuffer stringBuffer = new StringBuffer();
            String str= "";
            while((str = br.readLine()) != null){
                stringBuffer.append(str);
            }
            info=stringBuffer.toString();

        } catch (HttpException e) {
            // 发生致命的异常，可能是协议不对或者返回的内容有问题
            System.out.println("Please check your provided http address!");
            e.printStackTrace();
        } catch (IOException e) {
            // 发生网络异常
            e.printStackTrace();
            return null;
        } finally {
            // 释放连接
            getMethod.releaseConnection();
        }
        Long et = System.currentTimeMillis();
        //logger.info(url+",耗时："+(et-st));
        return info;
    }
}
