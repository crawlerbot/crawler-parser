package io.github.crawlerbot.extractor;

import io.github.crawlerbot.model.Entity;
import org.jsoup.nodes.Document;

import java.util.List;
import java.util.Map;

//@FunctionalInterface
public interface Extractor {

    List<Entity> getThings(Document document);

    List<Object> getThing(Document document);
}
