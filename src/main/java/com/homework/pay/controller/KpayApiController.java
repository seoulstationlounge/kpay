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

        // 돈 뿌리기의 모든 기능을 담고 있는 separate 객체 입니다.
        SeparateMoney separateMoney = SeparateMoney.of(xRoomId, Kmoney.of(kpay.getCurMoney()), xUserId);

        // token을 util에 MakeRandom 객체를 통해 내려 받습니다.
        String token = KpayMakeRandom.makeToken();

        // token이 기존에 있다면 재 발급 받습니다.
        while (kpayRepository.findByOrOriginUserIdAndTokenDesc(xUserId, token) != null) {
            token = KpayMakeRandom.makeToken();
        }

        // 처음 뿌리기 기능은 초기 데이터이기 때문에 단순히 insert합니다. 추후 에는 token을 기반으로 데이터를 쌓습니다.
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

        // 돈 뿌리기의 모든 기능을 담고 있는 separate 객체 입니다.
        SeparateMoney separateMoney = SeparateMoney.of(xRoomId, Kmoney.of(kpay.getCurMoney()), xUserId);

        // 기존에 token이 있는지 체크하기 위한 DB조회 입니다.
        Kpay originKpay = kpayRepository.findByOrOriginUserIdAndTokenDesc(separateMoney.getCurrentKuser().getId(), kpay.getToken());

        // 기존에 token에서 내가 받은 내역이 있는지에 대한 DB조회 입니다.
        Kpay findKpay = kpayRepository.findByUserIdAndToken(separateMoney.getCurrentKuser().getId(), kpay.getToken());

        if (originKpay != null) {
            throw new KpaySelfMoneyException();
        }

        if (findKpay != null) {
            throw new KpayAlreadyGotMoneyException();
        }

        // 랜덤으로 받을 돈을 1000원단위로 받습니다. (랜덤이므로 돈이 남을 수 도 있습니다.)
        int getMoney = separateMoney.getRandomMoney();

        // 기존에 토큰을 index로 받은 내역을 한곳에 쌓습니다. 추후 검색 내역도 같은 table에서 token기반으로 검색 합니다.
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