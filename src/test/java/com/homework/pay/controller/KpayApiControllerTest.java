package com.homework.pay.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.homework.pay.model.Kpay;
import com.homework.pay.repository.KpayRepository;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class KpayApiControllerTest {

    @Autowired
    KpayRepository kpayRepository;

    @Autowired
    private WebApplicationContext context;

    private MockMvc mvc;

    @Test
    public void sprinkleInputMoney() throws Exception {
        //givne
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .build();

        int originUser = 123;
        String roomId = "777";
        Kpay kpay = Kpay.builder()
                .curMoney(10000)
                .build();

        String url = "http://localhost:8080/api/kpay/inputMoney";

        //when
        mvc.perform(post(url)
                .header("X-USER-ID", originUser)
                .header("X-ROOM-ID", roomId)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(new ObjectMapper().writeValueAsString(kpay)))
                .andDo(print())
                .andExpect(status().isOk());

        //then
        List<Kpay> all = kpayRepository.findAll();

        assertThat(all.get(0).getOriginUserId()).isEqualTo(originUser);
        assertThat(all.get(0).getRoomId()).isEqualTo(roomId);

        kpayRepository.deleteAll();
    }

    @Test
    void sprinkleOutputMoney() throws Exception {
        //givne
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .build();

        int originUser = 123;
        int userId = 234;
        String roomId = "777";
        Kpay kpay = Kpay.builder()
                .curMoney(10000)
                .build();

        String url_first = "http://localhost:8080/api/kpay/inputMoney";
        String url_second = "http://localhost:8080/api/kpay/outputMoney";

        //when
        mvc.perform(post(url_first)
                .header("X-USER-ID", originUser)
                .header("X-ROOM-ID", roomId)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(new ObjectMapper().writeValueAsString(kpay)))
                .andExpect(status().isOk());

        List<Kpay> kpayTokenList = kpayRepository.findAll();

        Kpay kpay_token = Kpay.builder()
                .roomId(roomId)
                .curMoney(10000)
                .originUserId(originUser)
                .userId(userId)
                .token(kpayTokenList.get(0).getToken())
                .build();

        mvc.perform(post(url_second)
                .header("X-USER-ID", userId)
                .header("X-ROOM-ID", roomId)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(new ObjectMapper().writeValueAsString(kpay_token)))
                .andExpect(status().isOk());

        //then
        List<Kpay> all = kpayRepository.findAll();

        assertThat(all.get(0).getOriginUserId()).isEqualTo(originUser);
        assertThat(all.get(0).getRoomId()).isEqualTo(roomId);

        assertThat(all.get(1).getUserId()).isEqualTo(userId);
        assertThat(all.get(1).getRoomId()).isEqualTo(roomId);

        kpayRepository.deleteAll();
    }

    @Test
    void findByToken() throws Exception {
        //givne
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .build();

        int originUser = 123;
        String roomId = "777";
        Kpay kpay = Kpay.builder()
                .curMoney(10000)
                .build();

        String url = "http://localhost:8080/api/kpay/inputMoney";

        //when
        mvc.perform(post(url)
                .header("X-USER-ID", originUser)
                .header("X-ROOM-ID", roomId)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(new ObjectMapper().writeValueAsString(kpay)))
                .andDo(print())
                .andExpect(status().isOk());

        //then
        List<Kpay> kpayTokenList = kpayRepository.findAll();

        String url_token = "http://localhost:8080/api/kpay/" + kpayTokenList.get(0).getToken();

        //when
        mvc.perform(get(url_token)
                .header("X-USER-ID", originUser)
                .header("X-ROOM-ID", roomId)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(new ObjectMapper().writeValueAsString(kpay)))
                .andDo(print())
                .andExpect(status().isOk());

        kpayRepository.deleteAll();
    }
}