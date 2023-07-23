package com.cjcameron92.games.standalone.api;

public interface DataSerializer<T> {

    String serialize(T type);
}
