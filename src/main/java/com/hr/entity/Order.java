package com.hr.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author:HuRan
 * @Description:
 * @Date: Created in 11:39 2018/6/22
 * @Modified By:
 */
@Entity
@Table(name = "t_order")
public class Order {
    @Id
    private Long orderId;

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    private Long userId;

}
