package com.homework.pay.model;

import com.homework.pay.exception.KpayHeaderException;
import com.homework.pay.util.KpayMakeRandom;
import lombok.Getter;

@Getter
public class SeparateMoney {

    private final int MINUS_ME = 1;

    private final String roomNumber;
    private Kmoney kmoney;
    private final Kuser currentKuser;

    private SeparateMoney(String roomNumber, Kmoney kmoney, int userId) {
        validationHeader(roomNumber, userId);

        this.roomNumber = roomNumber;
        this.kmoney = kmoney;
        this.currentKuser = Kuser.builder().id(userId).build();
    }

    public static SeparateMoney of(String roomNumber, Kmoney kmoney, int userId) {
        return new SeparateMoney(roomNumber, kmoney, userId);
    }

    public int getRandomMoney() {
        return KpayMakeRandom.makeRandomMoney(kmoney.getKmoney());
    }

    private void validationHeader(String roomNumber, int userId) {
        if (roomNumber == null || userId == 0) {
            throw new KpayHeaderException();
        }
    }
}
