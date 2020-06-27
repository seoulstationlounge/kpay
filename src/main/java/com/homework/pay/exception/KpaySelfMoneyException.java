package com.homework.pay.exception;

public class KpaySelfMoneyException extends RuntimeException {
    private static final String ERROR_MSG = "자신이 뿌린돈은 자신이 받을 수 없습니다.";

    public KpaySelfMoneyException() {
        super(ERROR_MSG);
    }
}
