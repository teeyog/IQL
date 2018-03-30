package cn.i4.iql.http.handler;

import cn.i4.iql.http.util.ExportUtil;
import cn.i4.iql.http.util.HdfsUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class HDFSHandler {

    /**
     * 下载json文件
     * @param hdfsPath
     * @param response
     */
    public static void downloadJSON(String hdfsPath, HttpServletResponse response) {
//        String jsons = HdfsUtils.readFile(hdfsPath);
//        //设置文件路径
//        response.setContentType("application/force-download");// 设置强制下载不打开
//        response.addHeader("Content-Disposition", "attachment;fileName=iql_query_" + System.currentTimeMillis() + ".json");// 设置文件名
//        OutputStream os = null;
//        try {
//             os = response.getOutputStream();
//            os.write(jsons.getBytes("GBK"));
//            os.flush();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//            try {
//                if(os != null){
//                    os.close();
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
        //设置文件路径
        response.setContentType("application/force-download");// 设置强制下载不打开
        response.addHeader("Content-Disposition", "attachment;fileName=iql_query_" + System.currentTimeMillis() + ".json");// 设置文件名
        OutputStream os = null;
        FileSystem fileSystem = null;
        try {
            os = response.getOutputStream();
            fileSystem = HdfsUtils.getFS();
            FileStatus[] fstats = fileSystem.listStatus(new Path(hdfsPath));
            for ( FileStatus item : fstats ) {
                // ignoring files like _SUCCESS
                if ( item.getPath().getName().startsWith( "_" ) ) {
                    continue;
                }
                FSDataInputStream fdis = fileSystem.open(item.getPath());
                StringWriter writer = new StringWriter();
                org.apache.commons.io.IOUtils.copy( fdis, writer, "UTF-8" );
                if(!writer.toString().equals(""))
                os.write(writer.toString().getBytes("GBK"));
            }
            os.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
//            IOUtils.closeStream(fileSystem);
            try {
                if(os != null){
                    os.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 下载CSV文件
     * @param hdfsPath
     * @param schema
     * @param response
     */
    public static void downloadCSV(String hdfsPath,String schema,HttpServletResponse response) {
//        List<String> jsons = HdfsUtils.readFileToList(hdfsPath);
//        try(final OutputStream os = response.getOutputStream()) {
//            ExportUtil.responseSetProperties(response);
//            ExportUtil.doExport(jsons,schema,os);
//        }catch(Exception e) {
//            System.out.println(e);
//        }
            FileSystem fileSystem = null;
            OutputStream os = null;
            String[] colNamesArr =  schema.split(",");
            JSONObject json = null;
            try {
                ExportUtil.responseSetProperties(response);
                os = response.getOutputStream();
                fileSystem = HdfsUtils.getFS();
                os = response.getOutputStream();
                // 完成数据csv文件的封装
                // 输出列头
                for (int i = 0; i < colNamesArr.length; i++) {
                    os.write(colNamesArr[i].getBytes("GBK"));
                    os.write(",".getBytes("GBK"));
                }
                os.write("\r\n".getBytes("GBK"));
                FileStatus[] fstats = fileSystem.listStatus(new Path(hdfsPath));
                for ( FileStatus item : fstats ) {
                    // ignoring files like _SUCCESS
                    if ( item.getPath().getName().startsWith( "_" ) ) {
                        continue;
                    }
                    FSDataInputStream fdis = fileSystem.open(item.getPath());
                    StringWriter writer = new StringWriter();
                    org.apache.commons.io.IOUtils.copy( fdis, writer, "UTF-8" );
                    String raw = writer.toString();
                    for ( String str : raw.split( "\n" ) ) {
                        if(!str.equals("")){
                            json = JSON.parseObject(str);
                            for (int j = 0; j < colNamesArr.length; j++) {
                                os.write(json.get(colNamesArr[j]).toString().getBytes("GBK"));
                                os.write(",".getBytes("GBK"));
                            }
                            os.write("\r\n".getBytes("GBK"));
                        }
                    }
                }
                os.flush();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if(os != null){
                        os.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
//            IOUtils.closeStream(fileSystem);
            }
    }

    /**
     * 下载Excel文件
     * @param hdfsPath
     * @param schema
     * @param response
     * @throws Exception
     */
    public static void downloadExcel(String hdfsPath,String schema,HttpServletResponse response) throws Exception{
        {  //我这是根据前端传来的起始时间来查询数据库里的数据，如果没有输入变量要求，保留前两个就行
            String[] headers = schema.split(",");//导出的Excel头部，这个要根据自己项目改一下
            List<String> dataset = HdfsUtils.readFileToList(hdfsPath);
            //下面的完全不动就行了（Excel数据中不包含图片）
            // 声明一个工作薄
            HSSFWorkbook workbook = new HSSFWorkbook();
            // 生成一个表格
            HSSFSheet sheet = workbook.createSheet();
            // 设置表格默认列宽度为15个字节
            sheet.setDefaultColumnWidth((short) 18);
            HSSFRow row = sheet.createRow(0);
            HSSFFont font3 = workbook.createFont();
            font3.setColor(HSSFColor.BLACK.index);//定义Excel数据颜色
            for (short i = 0; i < headers.length; i++) {
                HSSFCell cell = row.createCell(i);
                HSSFRichTextString text = new HSSFRichTextString(headers[i]);
                cell.setCellValue(text);
            }
            //遍历集合数据，产生数据行
            Iterator it = dataset.iterator();
            int index = 0;
            while (it.hasNext()) {
                String line = it.next().toString();
                if(!line.equals("")){
                    index++;
                    row = sheet.createRow(index);
                    JSONObject obj = JSON.parseObject(line);
                    for(short i = 0;i < headers.length; i ++){
                        HSSFCell cell = row.createCell(i);
                        HSSFRichTextString richString = new HSSFRichTextString(obj.get(headers[i]).toString());
                        richString.applyFont(font3);
                        cell.setCellValue(richString);
                    }
                }
            }
            response.setContentType("application/octet-stream");
            response.setHeader("Content-disposition", "attachment;filename=iql_query_" + System.currentTimeMillis() + ".xls");//默认Excel名称
            response.flushBuffer();
            workbook.write(response.getOutputStream());
        }
    }


}
