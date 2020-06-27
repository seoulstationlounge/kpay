package com.homework.pay.controller;

import com.homework.pay.model.Kpay;
import com.homework.pay.repository.KpayRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/kpay")
public class KpayController {

    @Autowired
    private KpayRepository kpayRepository;

    @GetMapping("/list")
    public String list(Model model) {
        List<Kpay> kpays = kpayRepository.findAll();
        model.addAttribute("kpays", kpays);
        return "kpay/list";
    }
}
