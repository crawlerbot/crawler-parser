package io.github.crawlerbot.extractor;

import com.github.jsonldjava.utils.JsonUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.schemaorg.JsonLdSerializer;
import io.github.crawlerbot.model.Mapping;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class MetaExtractor {
    private static final Logger LOGGER = LoggerFactory.getLogger(JsonLdSerializer.class);

    public Map<String, Set<String>> extract(Document document) {
        Map<String, Set<String>> results = new HashMap<>();
        if (document == null || document.html() == null) return results;
        try {
            File file = new File(getClass().getClassLoader().getResource("meta_selector.json").getFile());
            String fileContent = new String(Files.readAllBytes(Paths.get(file.getAbsolutePath())));
            Gson gson = new Gson();
            Type type = new TypeToken<ArrayList<Mapping>>() {
            }.getType();
            List<Mapping> mappings = gson.fromJson(fileContent, type);
            if (mappings == null || mappings.size() == 0) return results;
            for (Mapping mapping : mappings) {
                Set<String> data = parse(mapping, document);

                if (results.get(mapping.getName()) == null) {
                    results.put(mapping.getName(), data);
                } else {
                    Set<String> currentData = results.get(mapping.getName());
                    currentData.addAll(data);
                    results.put(mapping.getName(), currentData);
                }

            }
            LOGGER.info("extract meta result:{},", JsonUtils.toPrettyString(results));
            return results;
        } catch (Exception ex) {
            ex.printStackTrace();
            LOGGER.info("extract exception:{},", ex);
        }
        return results;
    }

    public Set<String> parse(Mapping mapping, Document document) {
        Set<String> data = document.select(mapping.getSelector()).stream().map(element -> element.attr(mapping.getAttr())
        ).collect(Collectors.toSet());
        return data;

    }

}
