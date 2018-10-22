package io.github.crawlerbot;

import io.github.crawlerbot.client.HttpEngine;
import io.github.crawlerbot.extractor.*;
import io.github.crawlerbot.model.Entity;
import io.github.crawlerbot.model.WebData;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class Scraper {

    private static final Logger LOGGER = LoggerFactory.getLogger(Scraper.class);

    private List<Extractor> extractors;
    private MetaExtractor metaExtractor = new MetaExtractor();
    private HtmlExtractor htmlExtractor = new HtmlExtractor();
    public Scraper() {
        extractors = Arrays.asList(
                new JsonLdExtractor(),
                new MicrodataExtractor()
        );
    }

    public Document fetchByJsoup(String url, int timeout) throws IOException {
       /* Document document = Jsoup.connect(url)
                .userAgent("Mozilla/5.0 (X11; Linux i686; rv:10.0) Gecko/20100101 Firefox/10.0")
                .referrer("http://www.google.com")
                .timeout(timeout)
                .get();
        return  document;*/
       return new HttpEngine(null).doFetch(url);
    }

    public Map<String, Set<String>> extractData(URL url, int timeout) throws IOException, URISyntaxException {
        Document document = fetchByJsoup(url.toURI().toString(), timeout);
        return htmlExtractor.extract(document, url.toURI().toString());
    }

    public Map<String, Set<String>> extractMeta(URL url, int timeout) throws IOException, URISyntaxException {
        Document document = fetchByJsoup(url.toURI().toString(), timeout);
        return metaExtractor.extract(document);
    }

    public List<Entity> extract(File file) throws IOException {
        Document document = Jsoup.parse(file, "UTF-8");
        return scrap(document);
    }

    public Map<String, Set<String>> extractMeta(File file) throws IOException {
        Document document = Jsoup.parse(file, "UTF-8");
        return metaExtractor.extract(document);
    }

    public List<Object> extractSemantic(File file) throws IOException {
        Document document = Jsoup.parse(file, "UTF-8");
        return extractors.stream()
                .flatMap(extractor -> extractor.getThing(document).stream())
                .collect(Collectors.toList());
    }

    public List<Entity> extract(URL url, int timeout) throws IOException, URISyntaxException {
        Document document = fetchByJsoup(url.toURI().toString(), timeout);
        return scrap(document);
    }

    public List<Object> extractSemantic(URL url, int timeout) throws IOException, URISyntaxException {
        Document document = fetchByJsoup(url.toURI().toString(), timeout);
        return extractors.stream()
                .flatMap(extractor -> extractor.getThing(document).stream())
                .collect(Collectors.toList());
    }

    public List<Object> extractSemantic(String html) {
        Document document = Jsoup.parse(html);
        return extractors.stream()
                .flatMap(extractor -> extractor.getThing(document).stream())
                .collect(Collectors.toList());
    }

    public List<Entity> extract(String html) {
        Document document = Jsoup.parse(html);
        return scrap(document);
    }

    public Map<String, Set<String>> extractMeta(String html) throws IOException {
        Document document = Jsoup.parse(html);
        return metaExtractor.extract(document);
    }

    private List<Object> scraps(Document document) {
        return new JsonLdExtractor().getThing(document);
    }

    private List<Entity> scrap(Document document) {
        return extractors.stream()
                .flatMap(extractor -> extractor.getThings(document).stream())
                .collect(Collectors.toList());
    }


    public WebData extractHtml(URL url, int timeout) throws IOException, URISyntaxException {
        Document document = fetchByJsoup(url.toURI().toString(), timeout);
        List<Object> semantic = extractors.stream()
                .flatMap(extractor -> extractor.getThing(document).stream())
                .collect(Collectors.toList());
        Map<String, Set<String>> meta = metaExtractor.extract(document);
        Map<String, Set<String>> data = htmlExtractor.extract(document, url.toURI().toString());
        WebData webData = new WebData();
        webData.setSemantic(semantic);
        webData.setMeta(meta);
        webData.setData(data);
        return webData;
    }
    public WebData extractHtmls(String  html, String url){



        Document detailDocument = Jsoup.parse(html, url, Parser.xmlParser());
        List<Object> semantic = extractors.stream()
                .flatMap(extractor -> extractor.getThing(detailDocument).stream())
                .collect(Collectors.toList());
        Map<String, Set<String>> meta = metaExtractor.extract(detailDocument);
        Map<String, Set<String>> data = htmlExtractor.extract(detailDocument, url);
        WebData webData = new WebData();
        webData.setSemantic(semantic);
        webData.setMeta(meta);
        webData.setData(data);
        return webData;
    }


}
