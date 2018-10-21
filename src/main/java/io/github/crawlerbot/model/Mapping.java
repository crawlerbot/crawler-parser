package io.github.crawlerbot.model;

import java.io.Serializable;
public class Mapping implements Serializable {

    private static final long serialVersionUID = 1L;
    private String name;
    private String selector;
    private String configName;
    private String attr;
    private String dataType;
    public String getName() {
        return name;
    }

    public Mapping name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSelector() {
        return selector;
    }

    public Mapping selector(String selector) {
        this.selector = selector;
        return this;
    }

    public void setSelector(String selector) {
        this.selector = selector;
    }


    public String getConfigName() {
        return configName;
    }

    public Mapping configName(String configName) {
        this.configName = configName;
        return this;
    }

    public void setConfigName(String configName) {
        this.configName = configName;
    }

    public String getAttr() {
        return attr;
    }

    public Mapping attr(String attr) {
        this.attr = attr;
        return this;
    }

    public void setAttr(String attr) {
        this.attr = attr;
    }

    public String getDataType() {
        return dataType;
    }

    public Mapping dataType(String dataType) {
        this.dataType = dataType;
        return this;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
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
