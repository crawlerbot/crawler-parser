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
    public Set<Map<String, List<String>>> extract(Document document) {
        Set<Map<String, List<String>>> results = new HashSet<>();
        if (document == null || document.html() == null) return results;
        try {
            File file = new File(getClass().getClassLoader().getResource("meta_selector.json").getFile());
            String fileContent = new String(Files.readAllBytes(Paths.get(file.getAbsolutePath())));
            Gson gson = new Gson();
            Type type = new TypeToken<ArrayList<Mapping>>() {
            }.getType();
            List<Mapping> mappings = gson.fromJson(fileContent, type);
            if (mappings == null || mappings.size() == 0) return results;
            Set<Map<String, List<String>>> result =  mappings.stream().map(mapping -> parse(mapping, document)
            ).collect(Collectors.toSet());
            LOGGER.info("extract meta result:{},", JsonUtils.toPrettyString(result));
            return result;
        } catch (Exception ex) {
            ex.printStackTrace();
            LOGGER.info("extract exception:{},", ex);
        }
        return results;
    }

    public Map<String, List<String>> parse(Mapping mapping, Document document) {
        List<String> data = document.select(mapping.getSelector()).stream().map(element -> element.attr(mapping.getAttr())
        ).collect(Collectors.toList());
        Map<String, List<String>> result = new HashMap<>();
        if(data != null && data.size()>0) {
            result.put(mapping.getName(), data);
            return result;
        } else {
            result.put(mapping.getName(), new ArrayList<>());
            return result;
        }

    }

}
