package com.homework.pay.model;

import com.homework.pay.exception.KpayLowMoneyException;
import lombok.Getter;

public class Kmoney {

    private static final int MIN_COST = 1000;

    @Getter
    private final int kmoney;

    private Kmoney(int inputMoney) {
        validatePrice(inputMoney);
        this.kmoney = inputMoney;
    }

    public static Kmoney of(int inputMoney) {
        return new Kmoney(inputMoney);
    }

    private void validatePrice(int inputMoney) {
        if (inputMoney < MIN_COST) {
            throw new KpayLowMoneyException();
        }
    }
}
