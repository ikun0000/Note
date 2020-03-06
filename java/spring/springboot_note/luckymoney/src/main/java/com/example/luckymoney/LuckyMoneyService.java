package com.example.luckymoney;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class LuckyMoneyService {

    @Autowired
    private LuckymoneyRepository luckymoneyRepository;

    @Transactional
    public void createTow() {
        Luckymoney luckymoney1 = new Luckymoney();
        luckymoney1.setProducer("aaa");
        luckymoney1.setMoney(new BigDecimal(111));
        luckymoneyRepository.save(luckymoney1);

        Luckymoney luckymoney2 = new Luckymoney();
        luckymoney2.setProducer("aaa");
        luckymoney2.setMoney(new BigDecimal(222));
        luckymoneyRepository.save(luckymoney2);
    }
}
