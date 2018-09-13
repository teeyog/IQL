package iql.common.utils;

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
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HttpUtils {

	/**
	 * post请求 ，超时默认10秒, 默认utf-8
	 * @param url
	 * @param params
	 * @return
	 * @throws IOException
	 */
	public String post(String url, Map<String, String> params) throws Exception {
		return this.post(url, params, 10, "UTF-8");
	}
	/**
	 * post请求, 超时默认10秒
	 * @param url
	 * @param params
	 * @param charset 编码方式
	 * @return
	 * @throws Exception
	 */
	public String post(String url, Map<String, String> params, String charset) throws Exception {
		return this.post(url, params, 10, charset);
	}
	/**
	 * post请求, 默认utf-8
	 * @param url
	 * @param params
	 * @param timeout 超时时间，秒
	 * @return
	 * @throws Exception
	 */
	public String post(String url, Map<String, String> params, int timeout) throws Exception {
		return this.post(url, params, timeout, "UTF-8");
	}

	/**
	 * HTTP连接
	 */
	public static String get(String url, Map<String, String> params, int timeout, String charset){
		List<NameValuePair> qparams = new ArrayList<NameValuePair>();
		if (params != null) {
			for (Map.Entry<String, String> param : params.entrySet()) {
				qparams.add(new BasicNameValuePair(param.getKey(), param.getValue()));
			}
		}
		String paramstr = URLEncodedUtils.format(qparams, charset);
		if (StringUtils.isNotEmpty(paramstr)) {
			url = url + "?" + paramstr;
		}
		String info = "";
		// 构造HttpClient的实例
		HttpClient httpClient = new HttpClient();
		httpClient.getParams().setIntParameter("http.socket.timeout", timeout * 1000);
		// 创建GET方法的实例
		GetMethod getMethod = new GetMethod(url);
		// 使用系统提供的默认的恢复策略
		getMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,
				new DefaultHttpMethodRetryHandler());
		try {
			// 执行getMethod
			int statusCode = httpClient.executeMethod(getMethod);
			if (statusCode != HttpStatus.SC_OK) {
				System.err.println("Method failed: "
						+ getMethod.getStatusLine());
			}
			// 读取内容
			byte[] responseBody = getMethod.getResponseBody();
			// 处理内容 解密
			try {
				info = new String(responseBody,"UTF-8");
			} catch (Exception e) {
				e.printStackTrace();
			}
			//System.out.println(info);
			//writeFile(date,info);
		} catch (HttpException e) {
			// 发生致命的异常，可能是协议不对或者返回的内容有问题
			System.out.println("Please check your provided http address!");
			e.printStackTrace();
		} catch (IOException e) {
			// 发生网络异常
			e.printStackTrace();
		} finally {
			// 释放连接
			getMethod.releaseConnection();
		}
		return info;
	}



	/**
	 * 使用URLConnection实现GET请求
	 *
	 * 1.实例化一个java.net.URL对象； 2.通过URL对象的openConnection()方法得到一个java.net.URLConnection;
	 * 3.通过URLConnection对象的getInputStream()方法获得输入流； 4.读取输入流； 5.关闭资源；
	 */
	public static String get(String urlStr) throws Exception
	{
		URL url = new URL(urlStr);
		URLConnection urlConnection = url.openConnection(); // 打开连接

		BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), "utf-8")); // 获取输入流
		String line = null;
		StringBuilder sb = new StringBuilder();
		while ((line = br.readLine()) != null)
		{
			sb.append(line + "\n");
		}
		br.close();
		return sb.toString();
	}

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

	/**
	 *
	 * @param url
	 * @param json
	 * @param timeout
	 * @return
	 * @throws Exception
	 */
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
			//System.out.println("Read timed out!");
			// 发生网络异常
			e.printStackTrace();
		} finally {
			// 释放连接
			postMethod.releaseConnection();
		}
		return info;
	}


}
