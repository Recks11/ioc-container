package com.rexijie.mock;

import java.util.ArrayList;
import java.util.List;

public class NoParam {
    String name = "initial";
    List<String> str;

    public NoParam() {
        // Blank to create empty instance
        str = new ArrayList<>();
        str.add("oh word?");
    }

    public String getName() {
        return this.name;
    }

    @Override
    public String toString() {
        return "NoParam{" +
                "name='" + name + '\'' +
                "arr='" + str.toString()  +'\'' +
                '}';
    }
}
