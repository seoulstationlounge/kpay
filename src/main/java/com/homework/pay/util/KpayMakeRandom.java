package com.homework.pay.util;

import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toList;

public class KpayMakeRandom {
    private static final int MIN = 0;
    private static final int MAX = 9;
    private static final int SIZE = 3;

    private static final int MINUM_MONEY = 1000;

    private static final Random rnd = new Random();


    private static final List<Integer> token = IntStream.rangeClosed(MIN, MAX)
            .boxed()
            .collect(toList());

    private KpayMakeRandom() {};

    public static String makeToken() {
        Collections.shuffle(token);
        StringBuilder tokenBuilder = new StringBuilder();

        List<Integer> tokens = token.stream()
                .limit(SIZE)
                .collect(toList());

        tokens.forEach(tokenBuilder::append);
        return tokenBuilder.toString();
    }

    public static int makeRandomMoney(int totalMoney) {
        return rnd.nextInt(totalMoney / MINUM_MONEY) * MINUM_MONEY;
    }
}
