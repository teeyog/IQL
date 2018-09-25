package iql.common.utils

import java.util

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
}
