package com.hr.controller;

import com.dangdang.ddframe.rdb.sharding.id.generator.IdGenerator;
import com.hr.entity.Order;
import com.hr.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author:HuRan
 * @Description:
 * @Date: Created in 13:16 2018/6/22
 * @Modified By:
 */
@RestController
@RequestMapping("/order")
public class OrderController {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private IdGenerator idGenerator;
    @RequestMapping("/add")
    public Object add() {
  /*      for (int i = 0; i < 10; i++) {
            Order order = new Order();
            order.setUserId((long) i);
            order.setOrderId((long) i);
            orderRepository.save(order);
        }*/
     for (int i = 10; i < 20; i++) {
            Order order = new Order();
            order.setUserId(1L);
            order.setOrderId(idGenerator.generateId().longValue());
            orderRepository.save(order);
        }
        return "success";
    }

    @RequestMapping("query")
    private Object queryAll() {
        return orderRepository.findAll();
    }
}

