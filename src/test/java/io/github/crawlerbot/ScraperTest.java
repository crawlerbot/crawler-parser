package io.github.crawlerbot;

import com.github.jsonldjava.utils.JsonUtils;
import io.github.crawlerbot.model.Entity;
import com.google.common.collect.ImmutableList;
import com.google.schemaorg.SchemaOrgType;
import com.google.schemaorg.core.*;
import com.google.schemaorg.core.datatype.DataType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
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

//
//        Assertions.assertEquals(1, entityList.size());
//        Entity entity = entityList.get(0);

        System.out.println("demo");



//
//        Assertions.assertEquals("{\"@context\": \"https://schema.org\",\"@type\": \"Article\",\"headline\": \"Nhận định Chelsea vs MU - Vòng 9 Ngoại hạng Anh 2018/19\",\"image\": {\"@type\": \"ImageObject\",\"url\": \"https://static.bongda24h.vn/medias/facebook/2018/10/19/thong-tin-luc-luong-doi-hinh-tran-chelsea-vs-mu.png\",\"width\": \"500px\",\"height\": \"300px\"},\"publisher\": {\"@type\": \"Organization\",\"name\": \"Bóng đá 24h\",\"logo\": {\"@type\": \"ImageObject\",\"url\": \"https://bongda24h.vn/images/logo-bongda24h.png\"}},\"keywords\": [\"Chelsea vs MU\",\"Chelsea vs MU vòng 9 Ngoại hạng Anh 2018/19\"],\"datePublished\": \"Fri, 19 Oct 2018 16:36:25 GMT\",\"dateModified\": \"Fri, 19 Oct 2018 16:36:25 GMT\",\"articleSection\": \"sport\",\"creator\": \"Bóng đá 24h\",\"author\": \"Bóng đá 24h\",\"articleBody\": \"Chelsea vs MU trong trận cầu tâm điểm vòng 9 Ngoại hạng Anh 2018/19, cùng điểm qua tình hình lực lượng, đội hình bên phía 2 đội.\n" +
//                "\",\"mainEntityOfPage\": \"True\"}", entity.getRawEntity());
//        assertEvent(entity.getThing());
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
        List<Entity> entityList = scraper.extract(
                new File(getClass().getClassLoader().getResource("jsonld.html").getFile())
        );

        Assertions.assertEquals(1, entityList.size());
        Entity entity = entityList.get(0);

        System.out.println("demo");




        Assertions.assertEquals("<script type=\"application/ld+json\">\n" +
                "{\n" +
                "  \"@context\": \"http://schema.org\",\n" +
                "  \"@type\": \"Event\",\n" +
                "  \"location\": {\n" +
                "    \"@type\": \"Place\",\n" +
                "    \"address\": {\n" +
                "      \"@type\": \"PostalAddress\",\n" +
                "      \"addressLocality\": \"Denver\",\n" +
                "      \"addressRegion\": \"CO\",\n" +
                "      \"postalCode\": \"80209\",\n" +
                "      \"streetAddress\": \"7 S. Broadway\"\n" +
                "    },\n" +
                "    \"name\": \"The Hi-Dive\"\n" +
                "  },\n" +
                "  \"name\": \"Typhoon with Radiation City\",\n" +
                " \"image\": {\n" +
                "            \"@type\": \"ImageObject\",\n" +
                "            \"url\": \"http://cafefcdn.com/zoom/700_438/2018/10/14/photo1539529884921-15395298849361562038225.jpg\",\n" +
                "            \"width\" : \"700\",\n" +
                "            \"height\" : \"438\"\n" +
                "  }," +
                "  \"offers\": {\n" +
                "    \"@type\": \"Offer\",\n" +
                "    \"price\": \"13.00\",\n" +
                "    \"priceCurrency\": \"USD\",\n" +
                "    \"url\": \"http://www.ticketfly.com/purchase/309433\"\n" +
                "  },\n" +
                "  \"startDate\": \"2013-09-14T21:30\"\n" +
                "}\n" +
                "</script>", entity.getRawEntity());
        assertEvent(entity.getThing());
    }

    @Test
    public void scraperMicrodataTest() throws IOException {
        Scraper scraper = new Scraper();
        List<Map<String, Object>>  entityList = scraper.extractSemantic(
                new File(getClass().getClassLoader().getResource("microdata.html").getFile())
        );
       // Assertions.assertEquals(1, entityList.size());
        System.out.println("==================================");
        System.out.println(JsonUtils.toPrettyString(entityList));
        //Entity entity = entityList.get(0);
//        Assertions.assertEquals("<div class=\"event-wrapper\" itemscope itemtype=\"http://schema.org/Event\"> \n" +
//                " <div class=\"event-date\" itemprop=\"startDate\" content=\"2013-09-14T21:30\">\n" +
//                "  Sat Sep 14\n" +
//                " </div> \n" +
//                " <div class=\"event-title\" itemprop=\"name\">\n" +
//                "  Typhoon with Radiation City\n" +
//                " </div> \n" +
//                " <img itemprop=\"image\" src=\"http://localhost/image.png\"> \n" +
//                " <div class=\"event-venue\" itemprop=\"location\" itemscope itemtype=\"http://schema.org/Place\"> \n" +
//                "  <span itemprop=\"name\">The Hi-Dive</span> \n" +
//                "  <div class=\"address\" itemprop=\"address\" itemscope itemtype=\"http://schema.org/PostalAddress\"> \n" +
//                "   <span itemprop=\"streetAddress\">7 S. Broadway</span> \n" +
//                "   <br> \n" +
//                "   <span itemprop=\"addressLocality\">Denver</span>, \n" +
//                "   <span itemprop=\"addressRegion\">CO</span> \n" +
//                "   <span itemprop=\"postalCode\">80209</span> \n" +
//                "  </div> \n" +
//                " </div> \n" +
//                " <div class=\"event-time\">\n" +
//                "  9:30 PM\n" +
//                " </div> \n" +
//                " <span itemprop=\"offers\" itemscope itemtype=\"http://schema.org/Offer\"> \n" +
//                "  <div class=\"event-price\" itemprop=\"price\" content=\"13.00\">\n" +
//                "   $13.00\n" +
//                "  </div> <span itemprop=\"priceCurrency\" content=\"USD\"></span> <a itemprop=\"url\" href=\"http://www.ticketfly.com/purchase/309433\">Tickets</a> </span> \n" +
//                "</div>", entity.getRawEntity());
//        assertEvent(entity.getThing());
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
    public void testMetaExtract() throws IOException {
        Scraper scraper = new Scraper();
        Set<Map<String, List<String>>> data = scraper.extractMeta(new URL("https://kinhdoanh.vnexpress.net/tin-tuc/quoc-te/kinh-te-trung-quoc-lien-tiep-chiu-don-giang-chi-trong-vai-ngay-3826849.html"), 20000);
        System.out.println(JsonUtils.toPrettyString(data));
    }
}
