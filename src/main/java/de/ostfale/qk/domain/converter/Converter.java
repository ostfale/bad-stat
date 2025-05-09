package de.ostfale.qk.domain.converter;

public interface Converter<T, U> {

    U convertTo(T source);
}

