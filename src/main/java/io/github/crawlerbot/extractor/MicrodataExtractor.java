package io.github.crawlerbot.extractor;

import com.github.jsonldjava.core.JsonLdOptions;
import com.github.jsonldjava.core.JsonLdProcessor;
import com.github.jsonldjava.utils.JsonUtils;
import com.google.schemaorg.JsonLdSerializer;
import com.google.schemaorg.core.Thing;
import io.github.crawlerbot.SchemaToThingConverter;
import io.github.crawlerbot.model.Entity;
import io.github.crawlerbot.model.Schema;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MicrodataExtractor implements Extractor {

    private static final Logger LOGGER = LoggerFactory.getLogger(MicrodataExtractor.class);

    private static final String ITEM_TYPE = "itemtype";

    private static final String ITEM_SCOPE = "itemscope";

    private static final String ITEM_PROP = "itemprop";

    private static final String HYPERLINK_TAG = "a";

    private static final String IMAGE_TAG = "img";

    @Override
    public List<Entity> getThings(Document document) {
        Elements elements = getElements(document);

        return elements.stream()
                .flatMap(element -> {
                    try {
                        Schema schema = getTree(element);
                        Optional<Thing> optionalThing = SchemaToThingConverter.convert(schema);
                        return optionalThing
                                .map(thing -> Stream.of(new Entity(element.toString(), thing)))
                                .orElseGet(Stream::empty);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        return null;
                    }
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<Map<String, Object>> getThing(Document document) {
        try {
            List<Entity> things = getThings(document);

            List<Map<String, Object>> results = new ArrayList<>();
            JsonLdSerializer serializer = new JsonLdSerializer(true /* setPrettyPrinting */);
            for (Entity entity : things) {
                System.out.println("thing=================:" + serializer.serialize(entity.getThing()));
                String jsonLdStr = serializer.serialize(entity.getThing());
                Object jsonObject = JsonUtils.fromString(jsonLdStr);
                Map context = new HashMap();
                JsonLdOptions options = new JsonLdOptions();
                Map<String, Object> compact = JsonLdProcessor.compact(jsonObject, context, options);
                results.add(compact);
            }
            return results;
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ArrayList<>();
        }
    }

    private Elements getElements(Document document) {
        // All the itemscope that don't have an itemscope in parent (to get the top level item scope)
        String query = String.format("[%s]:not([%<s] [%<s])", ITEM_SCOPE);
        return document.select(query);
    }

    /**
     * Transform a microdata element into a tree
     *
     * @param parent the microdata element
     * @return a tree with the attributes and the objects of the element
     */
    private Schema getTree(Element parent) {
        // Create a copy to not modify the parameter
        Element workingElement = parent.clone();
        // Find all the children itemscope and remove them from the parent
        Elements children = workingElement.children().select(String.format("[%s]:not([%<s] [%<s])", ITEM_SCOPE)).remove();

        // Get all the attributes for the parent
        Elements attributes = workingElement.select(String.format("[%s]:not([%s])", ITEM_PROP, ITEM_SCOPE));

        Map<String, List<String>> properties = attributes.stream()
                .filter(element -> !StringUtils.isEmpty(element.attr(ITEM_PROP)))
                .collect(
                        Collectors.groupingBy(
                                element -> element.attr(ITEM_PROP),
                                Collectors.mapping(
                                        this::getValue, Collectors.toList()
                                )
                        )
                );

        Schema schema = new Schema();
        schema.setType(workingElement.attr(ITEM_TYPE));
        schema.setPropertyName(workingElement.attr(ITEM_PROP));
        schema.setProperties(properties);

        // Find all the objects in this object and map them to Schema
        schema.setChildren(
                children.stream()
                        .map(this::getTree)
                        .collect(Collectors.toList())
        );
        return schema;
    }

    private String getValue(Element element) {
        if (HYPERLINK_TAG.equals(element.tagName()) && element.hasAttr("href")) {
            return element.attr("href");
        }

        if (IMAGE_TAG.equals(element.tagName()) && element.hasAttr("src")) {
            return element.attr("src");
        }

        if (element.hasAttr("content")) {
            return element.attr("content");
        }

        return element.html();
    }
}
