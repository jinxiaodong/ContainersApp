package com.project.container.network.utils;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonMethod;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by Emcy-fu ;
 * on data 2017/6/21 ;
 */

public class JsonUtil {
    public static ObjectMapper mapper = null;
    public JSONObject jsonData;

    public JsonUtil() {
    }

    public JsonUtil(JSONObject jsonData) {
        this.jsonData = jsonData;
    }

    public static <T> T parse(String jsonString, Class<T> clsBean) {
        try {
            mapper = getObjectMapper();
            return mapper.readValue(jsonString, clsBean);
        } catch (JsonParseException var3) {
            var3.printStackTrace();
        } catch (JsonMappingException var4) {
            var4.printStackTrace();
        } catch (IOException var5) {
            var5.printStackTrace();
        }

        return null;
    }

    public static <T> ArrayList<T> parseList(String jsonString, Class<T> clsBean) {
        try {
            mapper = getObjectMapper();
            JsonParser e = mapper.getJsonFactory().createJsonParser(jsonString);
            JsonNode nodes = e.readValueAsTree();
            if(nodes != null && nodes.size() != 0) {
                ArrayList list = new ArrayList(nodes.size());
                Iterator var5 = nodes.iterator();

                while(var5.hasNext()) {
                    JsonNode node = (JsonNode)var5.next();
                    list.add(mapper.readValue(node, clsBean));
                }

                return list;
            } else {
                return new ArrayList();
            }
        } catch (Exception var7) {
            var7.printStackTrace();
            return null;
        }
    }

    public static String objectToString(Object o) {
        String result = "";

        try {
            mapper = getObjectMapper();
            result = mapper.writeValueAsString(o);
        } catch (Exception var3) {
            var3.printStackTrace();
        }

        return result;
    }

    public static ObjectMapper getObjectMapper() {
        if(mapper == null) {
            mapper = new ObjectMapper();
            mapper.setVisibility(JsonMethod.FIELD, JsonAutoDetect.Visibility.ANY);
            mapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            mapper.setSerializationInclusion(JsonSerialize.Inclusion.NON_NULL);
        }

        return mapper;
    }
}
