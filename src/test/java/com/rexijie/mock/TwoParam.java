package com.rexijie.mock;

public class TwoParam {
    String name = "TwoParam";
    NoParam noParam;
    OneParam oneParam;
    public TwoParam(NoParam noParam, OneParam oneParam) {
        this.noParam = noParam;
        this.oneParam = oneParam;
    }

    public NoParam getNoParam() {
        return noParam;
    }

    public void setNoParam(NoParam noParam) {
        this.noParam = noParam;
    }

    public OneParam getOneParam() {
        return oneParam;
    }

    public void setOneParam(OneParam oneParam) {
        this.oneParam = oneParam;
    }

    @Override
    public String toString() {
        return "TwoParam{" +
                "name='" + name + '\'' +
                ", noParam=" + noParam +
                ", oneParam=" + oneParam +
                '}';
    }
}
