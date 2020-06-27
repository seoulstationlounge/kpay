package com.homework.pay.repository;

import com.homework.pay.model.Kpay;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface KpayRepository extends JpaRepository<Kpay, Long> {

    List<Kpay> findByToken(String token);

    Kpay findByUserIdAndToken(int userId, String token);

    @Query("SELECT p FROM Kpay p where p.originUserId = :userId and p.token = :token and p.userId = :userId ORDER BY p.createdDate DESC")
    Kpay findByOrOriginUserIdAndTokenDesc(@Param("userId") int userId, @Param("token") String token);
}
