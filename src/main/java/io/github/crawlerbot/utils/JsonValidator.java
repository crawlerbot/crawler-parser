package io.github.crawlerbot.utils;

public class JsonValidator {
    public static String validJson(String jsonStr) {
        //String headLine = jsonStr.substring(jsonStr.indexOf("\"headline\"")+ 10, )
        //jsonStr = jsonStr.replaceAll("\"", "\\\\\'");
        jsonStr = jsonStr.replaceAll(
                "(\\n?\\s*\"headline\"\\s?:\\s?\")[^\\n\"]*(\",?\\n?)", "\"headline\":\"hello world\",");

        jsonStr = jsonStr.replaceAll(
                "(\\n?\\s*\"description\"\\s?:\\s?\")[^\\n\"]*(\",?\\n?)", "\"description\":\"hello world\",");
        return jsonStr;
    }


}
