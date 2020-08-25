package com.rexijie.mock;

public class OneParam extends NoParam {
    String name = "OneParam";
    NoParam noParam;

    public OneParam(NoParam noParam) {
        this.noParam = noParam;
    }

    public NoParam getNoParam() {
        return noParam;
    }

    public void setNoParam(NoParam noParam) {
        this.noParam = noParam;
    }

    @Override
    public String toString() {
        return "OneParam{" +
                "name='" + name + '\'' +
                ", noParam=" + noParam +
                '}';
    }
}
