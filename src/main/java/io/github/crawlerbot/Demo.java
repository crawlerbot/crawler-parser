package io.github.crawlerbot;

import com.github.jsonldjava.utils.JsonUtils;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Map;

public class Demo {
    public static void main(String args[]) throws IOException {
        Scraper scraper = new Scraper();
        List<Map<String, Object>> thingList = scraper.extractTo(new URL("https://www.lazada.vn/products/combo-ao-thun-be-trai-chat-lieu-mem-min-vd-i225056154-s307710790.html?spm=a2o4n.home.flashSale.4.1905e182Os10xC&search=1&mp=1"), 20000);
        System.out.println(JsonUtils.toPrettyString(thingList));
    }
}
