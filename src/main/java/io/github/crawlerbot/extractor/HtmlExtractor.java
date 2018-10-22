package io.github.crawlerbot.extractor;
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
    private static final Logger LOGGER = LoggerFactory.getLogger(HtmlExtractor.class);
    private static String removeTags = "a,iframe,script,em,div,style";

    public  Document removeElements(Document detailDocument, String selector) {
        try {
            Elements elements = detailDocument.select(selector);
            if (elements == null || elements.size() == 0) return detailDocument;
            for (Element element : elements) {
                if (element.select("img") == null) {
                    element.remove();
                }
            }
            return detailDocument;
        } catch (Exception ex) {
            return detailDocument;
        }
    }

    public  Document removeElementsByListSelectors(Document detailDocument, String selectors) {
        if (selectors == null || selectors == "") return detailDocument;
        try {
            List<String> removeTags = Arrays.asList(selectors.split(","));
            for (String tag : removeTags) {
                removeElements(detailDocument, tag);
            }
            return detailDocument;
        } catch (Exception ex) {
            return detailDocument;
        }
    }

    public  Document removePredefineTags(Document detailDocument, String tags) {
        if (tags == null || tags == "") return detailDocument;
        try {
            List<String> removeTags = Arrays.asList(tags.split(","));
            for (String tag : removeTags) {
                detailDocument.select(tag).remove();
            }
            return detailDocument;
        } catch (Exception ex) {
            return detailDocument;
        }

    }

    public  Document removeNotUsedAttr(Document detailDocument) {
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


    public  String extractHtml(Document document, String baseUri, String selector, String removeTagsSelector) {
        try {
            String detailHtml = document.select(selector).html();
            Document detailDocument = Jsoup.parse(detailHtml, baseUri, Parser.xmlParser());
            detailDocument = removePredefineTags(detailDocument, removeTags);
            detailDocument = removeElementsByListSelectors(detailDocument, removeTagsSelector);
            detailDocument = removeNotUsedAttr(detailDocument);
            return detailDocument.html();
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

//

    public  String extractText(Document document, String baseUri, String selector, String removeTagsSelector) {
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

    public  String extractText(String html, String baseUri, String removeTagsSelector) {
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

    public  Set<String> extractImage(String html, String baseUri, String removeTagsSelector) {
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
    public  Set<String> extractImage(Document detailDocument, String baseUri, String removeTagsSelector) {
        try {

            detailDocument = removePredefineTags(detailDocument, removeTags);
            detailDocument = removeElementsByListSelectors(detailDocument, removeTagsSelector);
            detailDocument = removeNotUsedAttr(detailDocument);
            return detailDocument.select("img").stream().map(element -> element.attr("abs:src")).collect(Collectors.toSet());
        } catch (Exception ex) {
            ex.printStackTrace();
            return new HashSet<>();
        }
    }

    public  Set<String> extractCategory(Document document, Mapping mapping) {
        return document.select(mapping.getSelector()).stream().map(element -> element.text()).collect(Collectors.toSet());
    }
    public  Map<String, Set<String>> extractCategories(String html, String baseUri) {
        try {
            MappingFactory mappingFactory = MappingFactory.instance();
            Document detailDocument = Jsoup.parse(html, baseUri, Parser.xmlParser());
            String domain = getDomainName(baseUri);
            List<Mapping> mappings = mappingFactory.getMappingsByDomainAndName(domain, "categories");
            Set<String> categories = new HashSet<>();
            for(Mapping mapping:mappings) {
                Set<String> data = extractCategory(detailDocument, mapping);
                categories.addAll(data);
            }
            Map<String, Set<String>> result  = new HashMap<>();
            result.put("categories", categories);
            return result;
        } catch (Exception ex) {
            ex.printStackTrace();
            Map<String, Set<String>> result =  new HashMap<>();
            result.put("categories", new HashSet<>());
            return result;
        }
    }
    public  Map<String, Set<String>> extractCategories(Document detailDocument, String baseUri) {
        try {
            MappingFactory mappingFactory = MappingFactory.instance();
            String domain = getDomainName(baseUri);
            List<Mapping> mappings = mappingFactory.getMappingsByDomainAndName(domain, "categories");
            Set<String> categories = new HashSet<>();
            for(Mapping mapping:mappings) {
                Set<String> data = extractCategory(detailDocument, mapping);
                LOGGER.info("data extractCategories========================: from mapping:{}, result:{}",mapping, data);
                categories.addAll(data);
            }
            Map<String, Set<String>> result  = new HashMap<>();
            result.put("categories", categories);
            return result;
        } catch (Exception ex) {
            ex.printStackTrace();
            Map<String, Set<String>> result =  new HashMap<>();
            result.put("categories", new HashSet<>());
            return result;
        }
    }

    public  String getDomainName(String url) {
        try {
            URI uri = new URI(url);
            String domain = uri.getHost();
            return domain.startsWith("www.") ? domain.substring(4) : domain;
        } catch (Exception ex) {
            return null;
        }
    }

    public  Map<String, Set<String>> extract(String html, String baseUri) {
        Document detailDocument = Jsoup.parse(html, baseUri, Parser.xmlParser());
        MappingFactory mappingFactory = MappingFactory.instance();
        String domain = getDomainName(baseUri);
        List<Mapping> mappings = mappingFactory.getMappingsByDomainAndName(domain, "html");
        Map<String, Set<String>> result = new HashMap<>();
        if (mappings == null || mappings.size() == 0) {
            result.put("html", new HashSet<>());
            result.put("articleBody", new HashSet<>());
            result.put("images", new HashSet<>());
            result.put("categories", new HashSet<>());
            return result;
        }

        Set<String> htmls = mappings.stream().map(mapping -> extractHtml(detailDocument, baseUri, mapping.getSelector(), mapping.getRemoveTags())).collect(Collectors.toSet());
        Set<String> texts = mappings.stream().map(mapping -> extractText(detailDocument, baseUri, mapping.getSelector(), mapping.getRemoveTags())).collect(Collectors.toSet());
        Set<String> images = new HashSet<>();
        for (Mapping mapping : mappings) {
            Set<String> currentImages = extractImage(html, baseUri, mapping.getRemoveTags());
            images.addAll(currentImages);
        }
        Map<String, Set<String>> categories = extractCategories(html, baseUri);
        result.put("html", htmls);
        result.put("articleBody", texts);
        result.put("images", images);
        result.put("categories", categories.get("categories"));

        return result;
    }
    public  Map<String, Set<String>> extract(Document detailDocument, String baseUri) {
        MappingFactory mappingFactory = MappingFactory.instance();
        String domain = getDomainName(baseUri);
        List<Mapping> mappings = mappingFactory.getMappingsByDomainAndName(domain, "html");
        Map<String, Set<String>> result = new HashMap<>();
        if (mappings == null || mappings.size() == 0) {
            result.put("html", new HashSet<>());
            result.put("articleBody", new HashSet<>());
            result.put("images", new HashSet<>());
            result.put("categories", new HashSet<>());
            return result;
        }

        Set<String> htmls = mappings.stream().map(mapping -> extractHtml(Jsoup.parse(detailDocument.html()), baseUri, mapping.getSelector(), mapping.getRemoveTags())).collect(Collectors.toSet());
        Set<String> texts = mappings.stream().map(mapping -> extractText(Jsoup.parse(detailDocument.html()), baseUri, mapping.getSelector(), mapping.getRemoveTags())).collect(Collectors.toSet());
        Set<String> images = new HashSet<>();
        for (Mapping mapping : mappings) {
            Set<String> currentImages = extractImage(Jsoup.parse(detailDocument.html()), baseUri, mapping.getRemoveTags());
            images.addAll(currentImages);
        }
        Map<String, Set<String>> categories = extractCategories(Jsoup.parse(detailDocument.html()), baseUri);
        result.put("html", htmls);
        result.put("articleBody", texts);
        result.put("images", images);
        result.put("categories", categories.get("categories"));

        return result;
    }

}
