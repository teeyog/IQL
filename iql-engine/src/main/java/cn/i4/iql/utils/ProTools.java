package cn.i4.iql.utils;


import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import java.io.IOException;
import java.util.Properties;

public class ProTools {

    private static Properties prop;

    private static Properties getPro() {
        FileSystem fs = null;
        try {
            Path pt = new Path("hdfs://i4ns/data/resource/iql_default.properties");
            fs = FileSystem.get(new Configuration());
            FSDataInputStream currencyInputStream = fs.open(pt);
            prop = new Properties();
            prop.load(currencyInputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return prop;
    }

    public static String get(String key) {
        if (prop == null) {
            prop = getPro();
        }
        return prop.get(key).toString();
    }
}
