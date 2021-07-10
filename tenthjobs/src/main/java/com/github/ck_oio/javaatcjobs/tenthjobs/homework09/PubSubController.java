package com.github.ck_oio.javaatcjobs.tenthjobs.homework09;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("pubsub/")
public class PubSubController {
    @Autowired
    private PubSubReduceInventoryMsg pubSub;

    @GetMapping("pub")
    public String pubMsg(String storeName, String goodsName, int count){
        pubSub.publishReduceInventoryMsg(storeName, goodsName, count);
        return "suc";
    }

    @GetMapping("sub")
    public String subMsg(String storeName, String goodsName, int count){
        pubSub.subscirbeReduceInventoryMsg(storeName, goodsName);
        return "suc";
    }

}
