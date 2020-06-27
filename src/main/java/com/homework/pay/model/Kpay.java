package com.homework.pay.model;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
public class Kpay extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private int originUserId;

    @Column(nullable = false)
    private String roomId;

    @Column(nullable = false)
    private int userId;

    @Column
    private int preMoney;

    @Column
    private int givenMoney;

    @Column(nullable = false)
    private int curMoney;

    @Column(nullable = false)
    private String token;

    @Builder
    public Kpay(int originUserId, String roomId, int userId, int preMoney, int givenMoney, int curMoney, String token) {
        this.originUserId = originUserId;
        this.roomId = roomId;
        this.userId = userId;
        this.preMoney = preMoney;
        this.givenMoney = givenMoney;
        this.curMoney = curMoney;
        this.token = token;
    }
}
