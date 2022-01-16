package com.wcreators.produceforexrate.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.Optional;

@Getter
@RequiredArgsConstructor
public enum Strategy {
    EMA_RSI_STOCH("EMA_RSI_STOCH"),
    EMPTY("EMPTY");

    private final String type;

    public static Optional<Strategy> strategyByType(String type) {
        return Arrays.stream(Strategy.values()).filter(s -> s.getType().equals(type)).findFirst();
    }
}
