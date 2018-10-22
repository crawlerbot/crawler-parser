package io.github.crawlerbot.model;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class WebData {

    List<Object> semantic;
    Map<String, Set<String>> meta;
    Map<String, Set<String>> data;

    public WebData() {
    }


    public List<Object> getSemantic() {
        return semantic;
    }

    public void setSemantic(List<Object> semantic) {
        this.semantic = semantic;
    }

    public Map<String, Set<String>> getMeta() {
        return meta;
    }

    public void setMeta(Map<String, Set<String>> meta) {
        this.meta = meta;
    }

    public Map<String, Set<String>> getData() {
        return data;
    }

    public void setData(Map<String, Set<String>> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "WebData{" +
                "semantic=" + semantic +
                ", meta=" + meta +
                ", data=" + data +
                '}';
    }
}
