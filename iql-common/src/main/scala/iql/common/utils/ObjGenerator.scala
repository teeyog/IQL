package iql.common.utils

import java.util
import java.util.Properties

import com.alibaba.fastjson.JSONObject

object ObjGenerator {

    /**
      * JSONGenerator
      *
      * @param tuples
      * @return
      */
    def newJSON(tuples: (String, Object)*) = {
        tuples.foldLeft(new JSONObject()) {
            case (obj, (k, v)) => obj.put(k, v)
                obj
        }
    }

    /**
      * MAPGenerator
      * @param tuples
      * @return
      */
    def newMap(tuples: (String, String)*) = {
        tuples.foldLeft(new util.HashMap[String, String]()) {
            case (map, (k, v)) => map.put(k, v)
                map
        }
    }

    /**
      * PerpertiesGenerator
      * @param tuples
      * @return
      */
    def newProperties(tuples: (String, String)*) = {
        tuples.foldLeft(new Properties()) {
            case (props, (k, v)) => props.put(k, v)
                props
        }
    }
}
