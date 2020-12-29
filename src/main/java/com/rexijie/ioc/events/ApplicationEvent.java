package com.rexijie.ioc.events;

public interface ApplicationEvent<T> {

    public void onApplicationEvent(T Event);
}
