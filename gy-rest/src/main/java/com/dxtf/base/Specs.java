package com.dxtf.base;

import java.util.List;

public class Specs {
    private String id;
    private String name;
    private List<SpecsList> list;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<SpecsList> getList() {
        return list;
    }

    public void setList(List<SpecsList> list) {
        this.list = list;
    }
}
