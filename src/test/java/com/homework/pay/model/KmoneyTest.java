package com.homework.pay.model;

import com.homework.pay.exception.KpayLowMoneyException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class KmoneyTest {

    @Test
    @DisplayName("1000원보다 작은 금액일시 예외 발생 테스트")
    void 적은금액_입력_테스트() {
        assertThatExceptionOfType(KpayLowMoneyException.class).isThrownBy(() -> Kmoney.of(900));
    }
}