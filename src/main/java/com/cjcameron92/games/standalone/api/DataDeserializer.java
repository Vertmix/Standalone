package com.cjcameron92.games.standalone.api;

public interface DataDeserializer<T> {

    T deserialize(String string);
}
