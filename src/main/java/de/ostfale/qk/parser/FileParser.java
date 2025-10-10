package de.ostfale.qk.parser;

@FunctionalInterface
public interface FileParser<T> {
    T parse(String content);
}
