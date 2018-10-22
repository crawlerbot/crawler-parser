package io.github.crawlerbot;

import com.github.jsonldjava.utils.JsonUtils;
import com.google.common.collect.ImmutableList;
import com.google.schemaorg.SchemaOrgType;
import com.google.schemaorg.core.*;
import com.google.schemaorg.core.datatype.DataType;
import io.github.crawlerbot.extractor.HtmlExtractor;
import io.github.crawlerbot.model.Entity;
import io.github.crawlerbot.model.WebData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ScraperTest {

    @Test
    public void demo() throws IOException {
        Scraper scraper = new Scraper();
        List<Map<String, Object>> entityList = scraper.extractSemantic(
                new File(getClass().getClassLoader().getResource("demo.html").getFile())
        );
        System.out.println(JsonUtils.toPrettyString(entityList));
        System.out.println("demo");
    }

    @Test
    public void demo2() throws IOException {
        Scraper scraper = new Scraper();
        List<Entity> entityList = scraper.extract(
                new File(getClass().getClassLoader().getResource("demo.html").getFile())
        );
        Assertions.assertEquals(1, entityList.size());
        Entity entity = entityList.get(0);
        System.out.println("demo");
        System.out.println(JsonUtils.toPrettyString(entity.getThing()));
    }

    @Test
    public void scraperJsonLdTest() throws IOException {
        Scraper scraper = new Scraper();
        List<Map<String, Object>> entityList = scraper.extractSemantic(
                new File(getClass().getClassLoader().getResource("jsonld.html").getFile())
        );
        File resultFile = new File(getClass().getClassLoader().getResource("extract_jsonld_resutl.json").getFile());
        String resultFileContent = new String(Files.readAllBytes(Paths.get(resultFile.getAbsolutePath())));
        Assertions.assertEquals(resultFileContent, JsonUtils.toPrettyString(entityList));
        System.out.println(JsonUtils.toPrettyString(entityList));
    }

    @Test
    public void scraperMicrodataTest() throws IOException {
        Scraper scraper = new Scraper();
        List<Map<String, Object>> entityList = scraper.extractSemantic(
                new File(getClass().getClassLoader().getResource("microdata.html").getFile())
        );
        System.out.println(JsonUtils.toPrettyString(entityList));
        File resultFile = new File(getClass().getClassLoader().getResource("extract_micro_data_result.json").getFile());
        String resultFileContent = new String(Files.readAllBytes(Paths.get(resultFile.getAbsolutePath())));
        Assertions.assertEquals(resultFileContent, JsonUtils.toPrettyString(entityList));
    }


    @Test
    public void scraperFakeNamespaceTest() throws IOException {
        Scraper scraper = new Scraper();
        List<Entity> entityList = scraper.extract(
                new File(getClass().getClassLoader().getResource("fakeNamespace.html").getFile())
        );
        Assertions.assertEquals(0, entityList.size());
    }

    private void assertEvent(Thing thing) {
        Assertions.assertTrue(thing instanceof Event);
        Event event = (Event) thing;
        assertUniqueValue(event.getStartDateList(), "2013-09-14T21:30");
        assertUniqueValue(event.getNameList(), "Typhoon with Radiation City");
        assertUniqueValue(event.getImageList(), "http://localhost/image.png");

        Assertions.assertEquals(1, event.getLocationList().size());
        Assertions.assertTrue(event.getLocationList().get(0) instanceof Place);
        Place place = (Place) event.getLocationList().get(0);
        assertUniqueValue(place.getNameList(), "The Hi-Dive");

        Assertions.assertEquals(1, place.getAddressList().size());
        Assertions.assertTrue(place.getAddressList().get(0) instanceof PostalAddress);
        PostalAddress postalAddress = (PostalAddress) place.getAddressList().get(0);
        assertUniqueValue(postalAddress.getStreetAddressList(), "7 S. Broadway");
        assertUniqueValue(postalAddress.getAddressLocalityList(), "Denver");
        assertUniqueValue(postalAddress.getAddressRegionList(), "CO");
        assertUniqueValue(postalAddress.getPostalCodeList(), "80209");

        Assertions.assertEquals(1, event.getOffersList().size());
        Assertions.assertTrue(event.getOffersList().get(0) instanceof Offer);
        Offer offer = (Offer) event.getOffersList().get(0);
        assertUniqueValue(offer.getPriceList(), "13.00");
        assertUniqueValue(offer.getPriceCurrencyList(), "USD");
        assertUniqueValue(offer.getUrlList(), "http://www.ticketfly.com/purchase/309433");
    }

    private void assertUniqueValue(ImmutableList<SchemaOrgType> values, String expected) {
        Assertions.assertEquals(1, values.size());
        Assertions.assertEquals(expected, ((DataType) values.get(0)).getValue());
    }

    @Test
    public void testMetaExtract() throws IOException, URISyntaxException {
        Scraper scraper = new Scraper();
        File resultFile = new File(getClass().getClassLoader().getResource("extract_meta_result.json").getFile());
        String resultFileContent = new String(Files.readAllBytes(Paths.get(resultFile.getAbsolutePath())));
        Map<String, Set<String>> data = scraper.extractMeta(new URL("https://www.rte.ie/news/2018/1021/1005726-death-of-woman-in-dublin-treated-as-suspicious/"), 20000);
        Assertions.assertEquals(resultFileContent, JsonUtils.toPrettyString(data));
        //System.out.println("data:=======");
        System.out.println(JsonUtils.toPrettyString(data));
    }

    @Test
    void extractHtml() throws IOException, URISyntaxException {
        Scraper scraper = new Scraper();
        File file = new File(getClass().getClassLoader().getResource("detail_news.html").getFile());
        String fileContent = new String(Files.readAllBytes(Paths.get(file.getAbsolutePath())));
        WebData webData = scraper.extractHtmls(fileContent,"https://vnexpress.net/tin-tuc/thoi-su/hom-nay-quoc-hoi-nghe-gioi-thieu-nhan-su-de-bau-chu-tich-nuoc-3827210.html" );



        //Map<String, Set<String>> result = HtmlExtractor.extract(fileContent, "https://vnexpress.net/tin-tuc/thoi-su/hom-nay-quoc-hoi-nghe-gioi-thieu-nhan-su-de-bau-chu-tich-nuoc-3827210.html");
        System.out.println(JsonUtils.toPrettyString(webData));

                //scraper.extractHtml(new URL("https://vnexpress.net/tin-tuc/thoi-su/hom-nay-quoc-hoi-nghe-gioi-thieu-nhan-su-de-bau-chu-tich-nuoc-3827210.html"), 200000);
        //System.out.println(JsonUtils.toPrettyString(webData));

       // Map<String, Set<String>> result = HtmlExtractor.extract(fileContent, "https://vnexpress.net/tin-tuc/thoi-su/hom-nay-quoc-hoi-nghe-gioi-thieu-nhan-su-de-bau-chu-tich-nuoc-3827210.html");
    }
}
