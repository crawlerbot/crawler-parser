package io.github.crawlerbot.data;

import io.github.crawlerbot.model.Mapping;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

class MappingFactoryTest {
    private MappingFactory mappingFactory = new MappingFactory();

    @Test
    void getAllMapping() {
    }

    @Test
    void getMappingsByDomain() {
        List<Mapping> result = mappingFactory.getMappingsByDomain("vnexpress.net");
        Assertions.assertEquals(result.size(), 2);
        Assertions.assertEquals(result.get(0).getHost(), "vnexpress.net");
    }
}