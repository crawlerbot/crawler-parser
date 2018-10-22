package io.github.crawlerbot.model;

import java.io.Serializable;

public class Mapping implements Serializable {

    private static final long serialVersionUID = 1L;
    private String name;
    private String selector;
    private String configName;
    private String host;
    private String attr;
    private String dataType;
    private String removeTags;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Mapping name(String name) {
        this.name = name;
        return this;
    }

    public String getSelector() {
        return selector;
    }

    public void setSelector(String selector) {
        this.selector = selector;
    }

    public Mapping selector(String selector) {
        this.selector = selector;
        return this;
    }

    public String getRemoveTags() {
        return removeTags;
    }

    public void setRemoveTags(String removeTags) {
        this.removeTags = removeTags;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getConfigName() {
        return configName;
    }

    public void setConfigName(String configName) {
        this.configName = configName;
    }

    public Mapping configName(String configName) {
        this.configName = configName;
        return this;
    }

    public String getAttr() {
        return attr;
    }

    public void setAttr(String attr) {
        this.attr = attr;
    }

    public Mapping attr(String attr) {
        this.attr = attr;
        return this;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public Mapping dataType(String dataType) {
        this.dataType = dataType;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        return false;
    }


    @Override
    public String toString() {
        return "Mapping{" +
                ", name='" + getName() + "'" +
                ", selector='" + getSelector() + "'" +
                ", configName='" + getConfigName() + "'" +
                ", attr='" + getAttr() + "'" +
                ", dataType='" + getDataType() + "'" +
                "}";
    }
}
