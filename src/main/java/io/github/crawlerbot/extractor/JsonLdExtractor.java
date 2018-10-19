package io.github.crawlerbot.extractor;

import com.github.jsonldjava.core.JsonLdOptions;
import com.github.jsonldjava.core.JsonLdProcessor;
import com.github.jsonldjava.utils.JsonUtils;
import io.github.crawlerbot.model.Entity;
import com.google.schemaorg.JsonLdSerializer;
import com.google.schemaorg.JsonLdSyntaxException;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class JsonLdExtractor implements Extractor {

    private static final Logger LOGGER = LoggerFactory.getLogger(JsonLdSerializer.class);

    private JsonLdSerializer jsonLdSerializer;

    public JsonLdExtractor() {
        jsonLdSerializer = new JsonLdSerializer(true);
    }

    @Override
    public List<Entity> getThings(Document document) {
        Elements elements = getElements(document);

        return elements.stream()
                .flatMap(element -> parseThings(element).stream())
                .collect(Collectors.toList());
    }

    @Override
    public List<Map<String, Object>> getThing(Document document) {
        Elements elements = getElements(document);
        List<Map<String,Object>> results = new ArrayList<>();
        for(Element element: elements) {
            Map<String, Object> result = parseThing(element);
            results.add(result);
        }
        return results;
    }

    /**
     * Get the useful elements for this extractor (the json ld scripts)
     *
     * @param document the original document
     * @return the list of elements
     */
    private Elements getElements(Document document) {
        return document.select("script[type$=application/ld+json]");
    }

    /**
     * Return a list of things from an element of the source (an application/ld+json script)
     * using the jsonLdSerializer
     *
     * @param element the element to parse
     * @return the list of entity if any
     */
    private List<Entity> parseThings(Element element) {

        try {
            return jsonLdSerializer.deserialize(element.html()).stream()
                    .map(thing -> new Entity(element.toString(), thing))
                    .collect(Collectors.toList());
        } catch (JsonLdSyntaxException e) {
            // Fail to parse the json-ld, return an empty array list
            LOGGER.warn("Error during the json-ld parsing", e);
            return new ArrayList<>();
        }
    }

    /**
     * get json-ld element text
     * @param element
     * @return
     */
    private String getElementJson(Element element) {
        try {
            String elementHtml = element.html();
           // LOGGER.info("start getElementJson json-ld:{}", elementHtml);
            if ((elementHtml.startsWith("{") && elementHtml.endsWith("}"))) return elementHtml;
            String result = elementHtml.substring(elementHtml.indexOf("{"), elementHtml.lastIndexOf("}") + 1);
            return result;
        }catch (Exception ex) {
            return element.html();
        }

    }

    /**
     * parse thing and return map object
     * @param element
     * @return
     */
    private Map<String, Object> parseThing(Element element) {
       try {
           String elementHtml = getElementJson(element);
          // LOGGER.info("start parsing json-ld:{}", elementHtml);
           Object jsonObject = JsonUtils.fromString(elementHtml);
           Map context = new HashMap();
           JsonLdOptions options = new JsonLdOptions();
           Map<String, Object> compact = JsonLdProcessor.compact(jsonObject, context, options);
           return  compact;
       }catch (Exception ex) {
           LOGGER.warn("Error during the json-ld parsing", ex);
           return new HashMap<>();
       }
    }
}
