package com.homework.pay.controller;

import com.homework.pay.exception.KpayAlreadyGotMoneyException;
import com.homework.pay.exception.KpayAuthException;
import com.homework.pay.exception.KpaySelfMoneyException;
import com.homework.pay.model.Kmoney;
import com.homework.pay.model.Kpay;
import com.homework.pay.model.SeparateMoney;
import com.homework.pay.repository.KpayRepository;
import com.homework.pay.util.KpayMakeRandom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/api")
class KpayApiController {

    @Autowired
    private KpayRepository kpayRepository;

    @PostMapping("/kpay/inputMoney")
    String sprinkleInputMoney(@RequestBody(required = false) Kpay kpay,
                              @RequestHeader(value = "X-USER-ID") int xUserId,
                              @RequestHeader(value = "X-ROOM-ID") String xRoomId) {

        SeparateMoney separateMoney = SeparateMoney.of(xRoomId, Kmoney.of(kpay.getCurMoney()), xUserId);
        String token = KpayMakeRandom.makeToken();

        while (kpayRepository.findByOrOriginUserIdAndTokenDesc(xUserId, token) != null) {
            token = KpayMakeRandom.makeToken();
        }

        kpayRepository.save(Kpay.builder()
                .originUserId(separateMoney.getCurrentKuser().getId())
                .roomId(separateMoney.getRoomNumber())
                .userId(separateMoney.getCurrentKuser().getId())
                .preMoney(0)
                .givenMoney(0)
                .curMoney(kpay.getCurMoney())
                .token(token)
                .build()
        );

        return token;
    }

    @PostMapping("/kpay/outputMoney")
    Kpay sprinkleOutputMoney(@RequestBody(required = false) Kpay kpay,
                             @RequestHeader(value = "X-USER-ID") int xUserId,
                             @RequestHeader(value = "X-ROOM-ID") String xRoomId) {

        SeparateMoney separateMoney = SeparateMoney.of(xRoomId, Kmoney.of(kpay.getCurMoney()), xUserId);

        Kpay originKpay = kpayRepository.findByOrOriginUserIdAndTokenDesc(separateMoney.getCurrentKuser().getId(), kpay.getToken());
        Kpay findKpay = kpayRepository.findByUserIdAndToken(separateMoney.getCurrentKuser().getId(), kpay.getToken());

        if (originKpay != null) {
            throw new KpaySelfMoneyException();
        }

        if (findKpay != null) {
            throw new KpayAlreadyGotMoneyException();
        }

        int getMoney = separateMoney.getRandomMoney();

        return kpayRepository.save(Kpay.builder()
                .originUserId(kpay.getOriginUserId())
                .roomId(separateMoney.getRoomNumber())
                .userId(separateMoney.getCurrentKuser().getId())
                .preMoney(separateMoney.getKmoney().getKmoney())
                .givenMoney(getMoney)
                .curMoney(separateMoney.getKmoney().getKmoney() - getMoney)
                .token(kpay.getToken())
                .build()
        );
    }

    @GetMapping("/kpay/{token}")
    List<Kpay> findByToken(@PathVariable String token,
                           @RequestHeader(value = "X-USER-ID") int xUserId,
                           @RequestHeader(value = "X-ROOM-ID") String xRoomId) {
        List<Kpay> kpays = kpayRepository.findByToken(token).stream()
                .filter(kpay -> kpay.getOriginUserId()
                        == xUserId)
                .collect(toList());

        if (kpays == null && kpays.size() == 0) {
            throw new KpayAuthException();
        }

        return kpays;
    }
}