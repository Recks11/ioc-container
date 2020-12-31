package com.rexijie.ioc.events;

public interface EventListener<T extends Event> {

    public void onApplicationEvent(T Event);
}
