package com.example.drools.service.impl;

import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.drools.domain.Message;
import com.example.drools.domain.Product;
import com.example.drools.service.DroolsService;

/**
 * Created by Ares on 2018/6/23.
 */
@Service("droolsService")
public class DroolsServiceImpl implements DroolsService {

	@Autowired
	KieSession kSession;
	
	@Override
    public String fireProductRule() {

		Product product = new Product();
        product.setType(Product.DIAMOND);

        Product product2 = new Product();
        product2.setType(Product.GOLD);

        kSession.insert(product);
        kSession.insert(product2);
        int count = kSession.fireAllRules();
        System.out.println("命中了" + count + "条规则！");
        System.out.println("商品" +product.getType() + "的商品折扣为" + product.getDiscount() + "%。");
        System.out.println("商品" +product2.getType() + "的商品折扣为" + product2.getDiscount() + "%。");
        return "OK";
    }
	
	@Override
    public String fireRule() {
        // load up the knowledge base
        KieServices ks = KieServices.Factory.get();
        KieContainer kContainer = ks.getKieClasspathContainer();
        KieSession kSession = kContainer.newKieSession("ksession-rules");

        // go !
        Message message = new Message();
        message.setMessage("Hello World");
        message.setStatus(Message.HELLO);
        kSession.insert(message);//插入
        kSession.fireAllRules();//执行规则
        kSession.dispose();
        return message.getMessage();
    }
}
