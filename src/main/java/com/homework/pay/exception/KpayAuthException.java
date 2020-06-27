package com.homework.pay.exception;

public class KpayAuthException extends RuntimeException {
    private static final String ERROR_MSG = "자신의 뿌림만 볼 수 있습니다.";

    public KpayAuthException() {
        super(ERROR_MSG);
    }
}
