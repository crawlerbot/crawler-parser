package io.github.crawlerbot.extractor;

import com.google.schemaorg.JsonLdSerializer;
import io.github.crawlerbot.data.MappingFactory;
import io.github.crawlerbot.model.Mapping;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.util.*;
import java.util.stream.Collectors;

public class HtmlExtractor {
    private static final Logger LOGGER = LoggerFactory.getLogger(JsonLdSerializer.class);
    private static String removeTags = "a,iframe,script,em,div,style";

    public static Document removeElements(Document detailDocument, String selector) {
        try {
            Elements elements = detailDocument.select(selector);
            if (elements == null || elements.size() == 0) return detailDocument;
            for (Element element : elements) {
                LOGGER.info("remove element:" + element.tag());
                if (element.select("img") == null) {
                    element.remove();
                }
            }
            return detailDocument;
        } catch (Exception ex) {
            return detailDocument;
        }
    }

    public static Document removeElementsByListSelectors(Document detailDocument, String selectors) {
        if (selectors == null || selectors == "") return detailDocument;
        try {
            List<String> removeTags = Arrays.asList(selectors.split(","));
            LOGGER.info("removeElementsByListSelectors:" + removeTags.size());
            for (String tag : removeTags) {
                LOGGER.info("remove cridical tags:" + tag);
                removeElements(detailDocument, tag);
            }
            return detailDocument;
        } catch (Exception ex) {
            return detailDocument;
        }
    }

    public static Document removePredefineTags(Document detailDocument, String tags) {
        if (tags == null || tags == "") return detailDocument;
        try {
            List<String> removeTags = Arrays.asList(tags.split(","));
            LOGGER.info("removeTags:" + removeTags.size());
            for (String tag : removeTags) {
                detailDocument.select(tag).remove();
            }
            return detailDocument;
        } catch (Exception ex) {
            return detailDocument;
        }

    }

    public static Document removeNotUsedAttr(Document detailDocument) {
        List<String> attToRemove = new ArrayList<>();
        for (Element e : detailDocument.getAllElements()) {
            Attributes at = e.attributes();
            for (Attribute a : at) {
                if (!a.getKey().equalsIgnoreCase("src")) {
                    attToRemove.add(a.getKey());
                }
            }
            for (String att : attToRemove) {
                e.removeAttr(att);
            }
        }
        return detailDocument;
    }


    public static Document extractHtml(Document document, String baseUri, String selector, String removeTagsSelector) {
        try {
            String detailHtml = document.select(selector).html();
            Document detailDocument = Jsoup.parse(detailHtml, baseUri, Parser.xmlParser());
            detailDocument = removePredefineTags(detailDocument, removeTags);
            detailDocument = removeElementsByListSelectors(detailDocument, removeTagsSelector);
            detailDocument = removeNotUsedAttr(detailDocument);
            return detailDocument;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public static String extractHtml(String html, String baseUri, String removeTagsSelector) {
        try {
            Document detailDocument = Jsoup.parse(html, baseUri, Parser.xmlParser());
            detailDocument = removePredefineTags(detailDocument, removeTags);
            detailDocument = removeElementsByListSelectors(detailDocument, removeTagsSelector);
            detailDocument = removeNotUsedAttr(detailDocument);
            return detailDocument.html();
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public static String extractText(Document document, String baseUri, String selector, String removeTagsSelector) {
        try {
            String detailHtml = document.select(selector).html();
            Document detailDocument = Jsoup.parse(detailHtml, baseUri, Parser.xmlParser());
            detailDocument = removePredefineTags(detailDocument, removeTags);
            detailDocument = removeElementsByListSelectors(detailDocument, removeTagsSelector);
            detailDocument = removeNotUsedAttr(detailDocument);
            return detailDocument.text();
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public static String extractText(String html, String baseUri, String removeTagsSelector) {
        try {
            Document detailDocument = Jsoup.parse(html, baseUri, Parser.xmlParser());
            detailDocument = removePredefineTags(detailDocument, removeTags);
            detailDocument = removeElementsByListSelectors(detailDocument, removeTagsSelector);
            detailDocument = removeNotUsedAttr(detailDocument);
            return detailDocument.text();
        } catch (Exception ex) {
            ex.printStackTrace();
            return "";
        }
    }

    public static Set<String> extractImage(String html, String baseUri, String removeTagsSelector) {
        try {
            Document detailDocument = Jsoup.parse(html, baseUri, Parser.xmlParser());
            detailDocument = removePredefineTags(detailDocument, removeTags);
            detailDocument = removeElementsByListSelectors(detailDocument, removeTagsSelector);
            detailDocument = removeNotUsedAttr(detailDocument);
            return detailDocument.select("img").stream().map(element -> element.attr("abs:src")).collect(Collectors.toSet());
        } catch (Exception ex) {
            ex.printStackTrace();
            return new HashSet<>();
        }
    }

    public static String getDomainName(String url) {
        try {
            URI uri = new URI(url);
            String domain = uri.getHost();
            return domain.startsWith("www.") ? domain.substring(4) : domain;
        } catch (Exception ex) {
            return null;
        }
    }

    public static Map<String, Set<String>> extract(String html, String baseUri) {
        MappingFactory mappingFactory = MappingFactory.instance();
        String domain = getDomainName(baseUri);
        List<Mapping> mappings = mappingFactory.getMappingsByDomainAndName(domain, "html");
        Map<String, Set<String>> result = new HashMap<>();
        if (mappings == null || mappings.size() == 0) {
            result.put("html", new HashSet<>());
            result.put("articleBody", new HashSet<>());
            result.put("images", new HashSet<>());
            return result;
        }
        Set<String> htmls = mappings.stream().map(mapping -> extractHtml(html, baseUri, mapping.getRemoveTags())).collect(Collectors.toSet());
        Set<String> texts = mappings.stream().map(mapping -> extractText(html, baseUri, mapping.getRemoveTags())).collect(Collectors.toSet());
        Set<String> images = new HashSet<>();
        for (Mapping mapping : mappings) {
            Set<String> currentImages = extractImage(html, baseUri, mapping.getRemoveTags());
            images.addAll(currentImages);
        }
        result.put("html", htmls);
        result.put("articleBody", texts);
        result.put("images", images);
        return result;
    }

}
