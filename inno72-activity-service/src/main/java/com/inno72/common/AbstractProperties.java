package com.inno72.common;

import java.util.Map;

public abstract class AbstractProperties {
    private Map<String, String> props;

    public AbstractProperties() {
    }

    public String get(String key) {
        return (String)this.props.get(key);
    }

    public boolean containsKey(String key) {
        return this.props.containsKey(key);
    }

    public void setProps(Map<String, String> props) {
        this.props = props;
    }

    public Map<String, String> getProps() {
        return this.props;
    }
}