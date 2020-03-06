package com.example.luckymoney;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@RestController
public class LuckymoneyController {

    @Autowired
    private LuckymoneyRepository luckymoneyRepository;

    @Autowired
    private LuckyMoneyService luckyMoneyService;

    /**
     * 获取红包列表
     */
    @GetMapping("/luckymoneys")
    public List<Luckymoney> list() {
        return luckymoneyRepository.findAll();
    }

    /**
     * 创建红包
     */
    @PostMapping("create")
    public Luckymoney create(@RequestParam("p") String producer,
                             @RequestParam("m") BigDecimal money) {
        Luckymoney luckymoney = new Luckymoney();
        luckymoney.setMoney(money);
        luckymoney.setProducer(producer);
        return luckymoneyRepository.save(luckymoney);
    }

    /**
     * 通过ID查询红包
     */
    @GetMapping("/find/{id}")
    public Luckymoney findById(@PathVariable("id") Integer id) {
        return luckymoneyRepository.findById(id).orElse(null);
    }

    /**
     * 更新红包
     */
    @PutMapping("/recvmoney/{id}")
    public Luckymoney updateMoney(@PathVariable("id") Integer id,
                                  @RequestParam("c") String consumer) {

        Optional<Luckymoney> optional = luckymoneyRepository.findById(id);
        if (optional.isPresent()) {
            Luckymoney luckymoney = optional.get();
            luckymoney.setConsumer(consumer);
            return luckymoneyRepository.save(luckymoney);
        }
        return null;
    }

    @GetMapping("/two")
    public void create() {
        luckyMoneyService.createTow();
    }

}
