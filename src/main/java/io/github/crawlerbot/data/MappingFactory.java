package io.github.crawlerbot.data;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import io.github.crawlerbot.model.Mapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MappingFactory {

    private static final Logger LOGGER = LoggerFactory.getLogger(MappingFactory.class);
    private static final String fileName = "content_selector.json";
    private static List<Mapping> mappings;
    private static MappingFactory instance;

    public static MappingFactory instance() {
        if (instance == null)
            instance = new MappingFactory();
        return instance;
    }

    public static List<Mapping> getAllMapping() {
        try {
            File file = new File(MappingFactory.class.getClassLoader().getResource(fileName).getFile());
            String fileContent = new String(Files.readAllBytes(Paths.get(file.getAbsolutePath())));
            Gson gson = new Gson();
            Type type = new TypeToken<ArrayList<Mapping>>() {
            }.getType();
            mappings = gson.fromJson(fileContent, type);
            LOGGER.info("total mappings: {}", mappings.size());

        } catch (Exception ex) {
        }
        return mappings;
    }

    public List<Mapping> getMappingsByDomain(String domain) {
        if (mappings == null || mappings.size() == 0)
            mappings = getAllMapping();
        List<Mapping> result = mappings.stream().filter(mapping ->
                mapping.getHost().equals(domain)
        ).collect(Collectors.toList());
        LOGGER.info("total mappings by domain:{}, is: {}", domain, result.size());
        return result;
    }

    public List<Mapping> getMappingsByDomainAndName(String domain, String name) {
        if (mappings == null || mappings.size() == 0)
            mappings = getAllMapping();
        List<Mapping> result = mappings.stream().filter(mapping ->
                mapping.getHost().equals(domain) && mapping.getName().equals(name)
        ).collect(Collectors.toList());
        LOGGER.info("total mappings by domain:{}, is: {}", domain, result.size());
        return result;
    }

}
