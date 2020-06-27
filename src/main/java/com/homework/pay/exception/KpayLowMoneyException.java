package com.homework.pay.exception;

public class KpayLowMoneyException extends RuntimeException {
    private static final String ERROR_MSG = "금액은 1000원 이상이여야 합니다.";

    public KpayLowMoneyException() {
        super(ERROR_MSG);
    }
}
