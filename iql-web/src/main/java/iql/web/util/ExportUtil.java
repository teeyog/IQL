package iql.web.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;


public class ExportUtil {

    /**
     * CSV文件列分隔符
     */
    private static final String CSV_COLUMN_SEPARATOR = ",";

    /**
     * CSV文件列分隔符
     */
    private static final String CSV_RN = "\r\n";

    /**
     * @paramdataList集合数据
     * @paramcolNames表头部数据
     * @parammapKey查找的对应数据
     * @paramresponse返回结果
     */
    public static boolean doExport(List<String> dataList, String colNames, OutputStream os) {
        try {
            StringBuffer buf = new StringBuffer();
            String[] colNamesArr = null;
            colNamesArr = colNames.split(",");
            // 完成数据csv文件的封装
            // 输出列头
            for (int i = 0; i < colNamesArr.length; i++) {
                buf.append(colNamesArr[i]).append(CSV_COLUMN_SEPARATOR);
            }
            buf.append(CSV_RN);
            if (null != dataList) {// 输出数据
                JSONObject json = null;
                for (String data : dataList) {
                    if(!data.equals("")){
                        json = JSON.parseObject(data);
                        for (int j = 0; j < colNamesArr.length; j++) {
                            buf.append(json.get(colNamesArr[j])).append(CSV_COLUMN_SEPARATOR);
                        }
                        buf.append(CSV_RN);
                    }
                }
            }
            // 写出响应
            os.write(buf.toString().getBytes("GBK"));
            os.flush();
            return true;
        } catch (Exception e) {
        }
        return false;
    }

    /**
     * @throwsUnsupportedEncodingException setHeader
     */
    public static void responseSetProperties(HttpServletResponse response) throws UnsupportedEncodingException {
        // 设置文件后缀
        String fn = "iql_query_" + System.currentTimeMillis() + ".csv";
        // 读取字符编码
        String utf = "UTF-8";
        // 设置响应
        response.setContentType("application/ms-txt.numberformat:@");
        response.setCharacterEncoding(utf);
        response.setHeader("Pragma", "public");
        response.setHeader("Cache-Control", "max-age=30");
        response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(fn, utf));
    }

}
