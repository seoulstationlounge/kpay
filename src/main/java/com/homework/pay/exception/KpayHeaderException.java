package com.homework.pay.exception;

public class KpayHeaderException extends RuntimeException {
    private static final String ERROR_MSG = "헤더 값이 올바르지 않습니다.";

    public KpayHeaderException() {
        super(ERROR_MSG);
    }
}
