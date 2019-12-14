package com.example.luckymoney;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.math.BigDecimal;

@Controller
@ResponseBody
public class HelloController {

    @Autowired
    private LimitConfig limitConfig;

    // @RequestMapping(value = "/hello", method = RequestMethod.GET)
    @GetMapping({"/hello", "/hi"})
    public String say() {
        return "Max Money: " + limitConfig.getMaxMoney()
                + "<br />Min Money: " + limitConfig.getMinMoney()
                + "<br />Description: " + limitConfig.getDescription();

//        return "index";
    }

    @GetMapping("/number/{id}")
    @ResponseBody
    public String say2(@PathVariable("id") int id, @RequestParam(value = "a", required = false, defaultValue = "1") int id2) {
        return "" + (id * id2);
    }
}
