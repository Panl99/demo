package com.lp.demo.iot.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Data;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class JsonArrayCategorizer {

    /**
     * JSON数组按层级分类
     *
     * @param jsonArrayString json数组字符串
     * @return 返回值为一个Map，其中键是parentId，值是属于该parentId的JSON对象列表
     * @throws Exception
     */
    public static Map<String, List<JsonNode>> categorizeJsonArray(String jsonArrayString) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonArray = mapper.readTree(jsonArrayString);

        Map<String, List<JsonNode>> categorizedJson = new HashMap<>();

        // iterate through the JSON array and categorize each object based on its parentId
        for (JsonNode jsonObject : jsonArray) {
            String parentId = jsonObject.get("parentId").asText();
            List<JsonNode> categoryList = categorizedJson.getOrDefault(parentId, new ArrayList<>());
            categoryList.add(jsonObject);
            categorizedJson.put(parentId, categoryList);
        }

        return categorizedJson;
    }

    /**
     * JSON数组按层级分类
     *
     * @param filePath json数组文件路径
     * @return 返回值为一个Map，其中键是parentId，值是属于该parentId的JSON对象列表
     * @throws Exception
     */
    public static Map<String, List<JsonNode>> categorizeJsonArrayFromFile(String filePath) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonArray = mapper.readTree(new File(filePath));

        Map<String, List<JsonNode>> categorizedJson = new HashMap<>();

        // iterate through the JSON array and categorize each object based on its parentId
        for (JsonNode jsonObject : jsonArray) {
            String parentId = jsonObject.get("parentId").asText();
            List<JsonNode> categoryList = categorizedJson.getOrDefault(parentId, new ArrayList<>());
            categoryList.add(jsonObject);
            categorizedJson.put(parentId, categoryList);
        }

        return categorizedJson;
    }


    /**
     * JSON数组按层级分类
     *
     * @param filePath json数组文件路径
     * @return 返回值为一个JsonNode，其中key是parentId，值是属于该parentId的JSON对象列表
     * @throws Exception
     */
    public static JsonNode categorizeJsonArrayToJSON(String filePath) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonArray = mapper.readTree(new File(filePath));

        Map<String, List<JsonNode>> categorizedJson = new HashMap<>();

        // iterate through the JSON array and categorize each object based on its parentId
        for (JsonNode jsonObject : jsonArray) {
            String parentId = jsonObject.get("parentId").asText();
            List<JsonNode> categoryList = categorizedJson.getOrDefault(parentId, new ArrayList<>());
            categoryList.add(jsonObject);
            categorizedJson.put(parentId, categoryList);
        }

        // build the categorized JSON
        JsonNodeFactory factory = JsonNodeFactory.instance;
        ObjectNode categorizedJsonNode = factory.objectNode();

        for (Map.Entry<String, List<JsonNode>> entry : categorizedJson.entrySet()) {
            String parentId = entry.getKey();
            List<JsonNode> children = entry.getValue();

            ArrayNode childrenNode = factory.arrayNode();
            for (JsonNode child : children) {
                childrenNode.add(child);
            }

            categorizedJsonNode.set(parentId, childrenNode);
        }

        return categorizedJsonNode;
    }

    public static void main(String[] args) throws Exception {
//        String filePath = "src/main/resources/static/device-category.json";
        String filePath = "D:\\Projects\\opensource\\demo\\demo-iot\\src\\main\\resources\\static\\device-category.json";
//        Map<String, List<JsonNode>> categorizedJson = categorizeJsonArrayFromFile(filePath);
//        System.out.println(categorizedJson);

        JsonNode jsonNode = categorizeJsonArrayToJSON(filePath);
        System.out.println(jsonNode);
    }

    @Data
    static class DeviceCategory {
        private Integer id;
        private Integer parentId;
        private String key;
        private String name;
    }

}
