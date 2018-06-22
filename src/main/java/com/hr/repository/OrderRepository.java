package com.hr.repository;

import com.hr.entity.Order;
import org.springframework.data.repository.CrudRepository;

/**
 * @author:HuRan
 * @Description:
 * @Date: Created in 11:43 2018/6/22
 * @Modified By:
 */
public interface OrderRepository extends CrudRepository<Order,Long>{
}
