package io.github.crawlerbot;

import com.github.jsonldjava.utils.JsonUtils;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Map;

public class Demo {
    public static void main(String args[]) throws IOException {
        Scraper scraper = new Scraper();
        List<Map<String, Object>> thingList = scraper.extractTo(new URL("https://www.msn.com/en-ie/news/world/a-president-who-believes-he-is-entitled-to-his-own-facts/ar-BBOAvmJ?li=BBr5PkO"), 20000);
        System.out.println("start results:=========================");
        System.out.println(JsonUtils.toPrettyString(thingList));
        System.out.println("end results:=========================");
    }
}
