package io.github.crawlerbot.extractor;

import com.github.jsonldjava.utils.JsonUtils;
import io.github.crawlerbot.Scraper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Set;

class HtmlExtractorTest {

    HtmlExtractor htmlExtractor = new HtmlExtractor();
    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void extract() throws IOException, URISyntaxException {
        File file = new File(getClass().getClassLoader().getResource("detail_news.html").getFile());
        String fileContent = new String(Files.readAllBytes(Paths.get(file.getAbsolutePath())));

        Map<String, Set<String>> result = htmlExtractor.extract(fileContent, "https://vnexpress.net/tin-tuc/thoi-su/hom-nay-quoc-hoi-nghe-gioi-thieu-nhan-su-de-bau-chu-tich-nuoc-3827210.html");

        File fileResult = new File(getClass().getClassLoader().getResource("detail_news_extractor.json").getFile());
        String fileResultContent = new String(Files.readAllBytes(Paths.get(fileResult.getAbsolutePath())));

        Assertions.assertEquals(fileResultContent, JsonUtils.toPrettyString(result));
        System.out.println("result:" + JsonUtils.toPrettyString(result));

        Scraper scraper = new Scraper();
        List<Map<String, Object>> data = scraper.extractSemantic(new URL("https://vnexpress.net/tin-tuc/thoi-su/hom-nay-quoc-hoi-nghe-gioi-thieu-nhan-su-de-bau-chu-tich-nuoc-3827210.html"), 20000);
        System.out.println("data:" + JsonUtils.toPrettyString(data));


    }
}