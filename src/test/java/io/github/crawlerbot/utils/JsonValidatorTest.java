package io.github.crawlerbot.utils;

import org.jsoup.Jsoup;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

class JsonValidatorTest {


    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void validJson() throws IOException {
        File fileResult = new File(getClass().getClassLoader().getResource("wrong_json.html").getFile());
        String fileResultContent = new String(Files.readAllBytes(Paths.get(fileResult.getAbsolutePath())));
        //System.out.println(fileResultContent);

        String textContent = Jsoup.parse(fileResultContent).select("script[type=application/ld+json]").html();

        //System.out.println(textContent);


        System.out.println(JsonValidator.validJson(textContent));
    }
}