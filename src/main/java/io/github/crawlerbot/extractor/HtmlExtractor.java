package io.github.crawlerbot.extractor;

import com.google.schemaorg.JsonLdSerializer;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HtmlExtractor {
    private static String removeTags = "a,iframe,script,em,div,style";
    private static final Logger LOGGER = LoggerFactory.getLogger(JsonLdSerializer.class);
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


    public static Document extractHtml(Document document, String baseUri, String selector, String removeTags) {
        try {
            String detailHtml = document.select(selector).html();
            Document detailDocument = Jsoup.parse(detailHtml, baseUri, Parser.xmlParser());
            detailDocument = removePredefineTags(detailDocument, removeTags);
            detailDocument = removeElementsByListSelectors(detailDocument, removeTags);
            detailDocument = removeNotUsedAttr(detailDocument);
            return detailDocument;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
    public static String extractHtml(String html, String baseUri, String removeTags) {
        try {
            Document detailDocument = Jsoup.parse(html, baseUri, Parser.xmlParser());
            detailDocument = removePredefineTags(detailDocument, removeTags);
            detailDocument = removeElementsByListSelectors(detailDocument, removeTags);
            detailDocument = removeNotUsedAttr(detailDocument);
            return detailDocument.html();
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
    public static String extractText(Document document, String baseUri, String selector, String removeTags) {
        try {
            String detailHtml = document.select(selector).html();
            Document detailDocument = Jsoup.parse(detailHtml, baseUri, Parser.xmlParser());
            detailDocument = removePredefineTags(detailDocument, removeTags);
            detailDocument = removeElementsByListSelectors(detailDocument, removeTags);
            detailDocument = removeNotUsedAttr(detailDocument);
            return detailDocument.text();
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
    public static String extractText(String html, String baseUri, String removeTags) {
        try {
            Document detailDocument = Jsoup.parse(html, baseUri, Parser.xmlParser());
            detailDocument = removePredefineTags(detailDocument, removeTags);
            detailDocument = removeElementsByListSelectors(detailDocument, removeTags);
            detailDocument = removeNotUsedAttr(detailDocument);
            return detailDocument.text();
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

}
