package com.homework.pay.exception;

public class KpayAlreadyGotMoneyException extends RuntimeException {
    private static final String ERROR_MSG = "이미 뿌린돈을 받았습니다.";

    public KpayAlreadyGotMoneyException() {
        super(ERROR_MSG);
    }
}
