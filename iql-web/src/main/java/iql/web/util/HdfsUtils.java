package iql.web.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import iql.common.utils.PropsUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.*;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.io.IOUtils;

import java.io.*;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

public class HdfsUtils {

    private static FileSystem fileSystem = null;

    /**
     * ls
     */
    public static void listFiles(String specialPath) {
        FileSystem fileSystem = null;
        try {
            fileSystem = getFS();
            FileStatus[] fstats = fileSystem.listStatus(new Path(specialPath));
            for (FileStatus fstat : fstats) {
                System.out.println(fstat.isDirectory() ? "directory" : "file");
                System.out.println("Permission:" + fstat.getPermission());
                System.out.println("Owner:" + fstat.getOwner());
                System.out.println("Group:" + fstat.getGroup());
                System.out.println("Size:" + fstat.getLen());
                System.out.println("Replication:" + fstat.getReplication());
                System.out.println("Block Size:" + fstat.getBlockSize());
                System.out.println("Name:" + fstat.getPath());
                System.out.println("#############################");
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("link err");
        } finally {
            if (fileSystem != null) {
                try {
                    fileSystem.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    /**
     * cat
     *
     * @param hdfsFilePath
     */
    public static void cat(String hdfsFilePath) {
        FileSystem fileSystem = null;
        try {
            fileSystem = getFS();
            FileStatus[] fstats = fileSystem.listStatus(new Path(hdfsFilePath));
            for ( FileStatus item : fstats ) {
                // ignoring files like _SUCCESS
                if ( item.getPath().getName().startsWith( "_" ) ) {
                    continue;
                }
                FSDataInputStream fdis = fileSystem.open(item.getPath());
                IOUtils.copyBytes(fdis, System.out, 1024);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeStream(fileSystem);
        }
    }

    /**
     * cat
     *
     * @param hdfsFilePath
     */
    public static String readFileToString(String hdfsFilePath) {
        FileSystem fileSystem = null;
        JSONArray results = new JSONArray();
        int returnLines = 1000;
        try {
            fileSystem = getFS();
            FileStatus[] fstats = fileSystem.listStatus(new Path(hdfsFilePath));
            for ( FileStatus item : fstats ) {
                // ignoring files like _SUCCESS
                if ( item.getPath().getName().startsWith( "_" ) ) {
                    continue;
                }
                FSDataInputStream fdis = fileSystem.open(item.getPath());
                StringWriter writer = new StringWriter();
                org.apache.commons.io.IOUtils.copy( fdis, writer, "UTF-8");
                String raw = writer.toString();
                if(!raw.equals("")){
                    for ( String str : raw.split( "\n" )) {
                        results.add(JSON.parseObject(str));
                        if(returnLines -- == 1) return results.toJSONString();
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
//            IOUtils.closeStream(fileSystem);
        }
        return results.toJSONString();
    }

    /**
     * cat
     *
     * @param hdfsFilePath
     */
    public static  List<String> readFileToList(String hdfsFilePath) {
        FileSystem fileSystem = null;
        List<String> results = new ArrayList<String>();
        try {
            fileSystem = getFS();
            FileStatus[] fstats = fileSystem.listStatus(new Path(hdfsFilePath));
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
                    results.add(str);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
//            IOUtils.closeStream(fileSystem);
        }
        return results;
    }

    /**
     * cat
     *
     * @param hdfsFilePath
     */
    public static String readFile(String hdfsFilePath) {
        FileSystem fileSystem = null;
        StringBuilder results = new StringBuilder();
        try {
            fileSystem = getFS();
            FileStatus[] fstats = fileSystem.listStatus(new Path(hdfsFilePath));
            for ( FileStatus item : fstats ) {
                // ignoring files like _SUCCESS
                if ( item.getPath().getName().startsWith( "_" ) ) {
                    continue;
                }
                FSDataInputStream fdis = fileSystem.open(item.getPath());
                StringWriter writer = new StringWriter();
                org.apache.commons.io.IOUtils.copy( fdis, writer, "UTF-8" );
                results.append(writer.toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
//            IOUtils.closeStream(fileSystem);
        }
        return results.toString();
    }

    /**
     * 创建目录
     *
     * @param hdfsFilePath
     */
    public static void mkdir(String hdfsFilePath) {
        FileSystem fileSystem = getFS();
        try {
            boolean success = fileSystem.mkdirs(new Path(hdfsFilePath));
            if (success) {
                System.out.println("Create directory or file successfully");
            }
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            closeFS(fileSystem);
        }


    }

    /**
     * 删除文件或目录
     *
     * @param hdfsFilePath
     * @param recursive    递归
     */
    public static void rm(String hdfsFilePath, boolean recursive) {
        FileSystem fileSystem = getFS();
        try {
            boolean success = fileSystem.delete(new Path(hdfsFilePath), recursive);
            if (success) {
                System.out.println("delete successfully");
            }
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            closeFS(fileSystem);
        }
    }

    /**
     * 上传文件到HDFS
     *
     * @param localFilePath
     * @param hdfsFilePath
     */
    public static void put(String localFilePath, String hdfsFilePath) {
        FileSystem fileSystem = getFS();
        try {
            FSDataOutputStream fdos = fileSystem.create(new Path(hdfsFilePath));
            FileInputStream fis = new FileInputStream(new File(localFilePath));
            IOUtils.copyBytes(fis, fdos, 1024);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeStream(fileSystem);
        }
    }

    public static void read(String fileName) throws Exception {
        // get filesystem
        FileSystem fileSystem = getFS();
        Path readPath = new Path(fileName);
        // open file
        FSDataInputStream inStream = fileSystem.open(readPath);
        try {
            // read
            IOUtils.copyBytes(inStream, System.out, 4096, false);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeStream(inStream);
        }
    }

    /**
     * 下载文件到本地
     *
     * @param localFilePath
     * @param hdfsFilePath
     */
    public static void get(String localFilePath, String hdfsFilePath) {
        FileSystem fileSystem = getFS();
        try {
            FSDataInputStream fsis = fileSystem.open(new Path(hdfsFilePath));
            FileOutputStream fos = new FileOutputStream(new File(localFilePath));
            IOUtils.copyBytes(fsis, fos, 1024);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeStream(fileSystem);
        }
    }

    public void write(String localPath, String hdfspath) throws Exception {
        FileInputStream inStream = new FileInputStream(
                new File(localPath)
        );
        FileSystem fileSystem = this.getFS();
        Path writePath = new Path(hdfspath);
        // Output Stream
        FSDataOutputStream outStream = fileSystem.create(writePath);
        try {
            IOUtils.copyBytes(inStream, outStream, 4096, false);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeStream(inStream);
            IOUtils.closeStream(outStream);
        }
    }


    /**
     * 获取FileSystem实例
     *
     * @return
     */
    public static synchronized  FileSystem getFS() {
        if(fileSystem == null){
            try {
                Configuration conf = new Configuration();
                conf.setBoolean("fs.hdfs.impl.disable.cache", true);
                conf.set("fs.hdfs.impl", org.apache.hadoop.hdfs.DistributedFileSystem.class.getName());
                fileSystem = FileSystem.get(URI.create(PropsUtils.confMap().get("hdfs.url").get()),conf);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return fileSystem;
    }

    /**
     * 关闭FileSystem
     *
     * @param fileSystem
     */
    private static void closeFS(FileSystem fileSystem) {
        if (fileSystem != null) {
            try {
                fileSystem.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public static void main(String[] args) {

        String strings = readFile("/tmp/iql/result/iql_query_result_1521699325228");
        System.out.println(strings);


    }
}
