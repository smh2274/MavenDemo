package com.shi.chapter2.service;

import com.shi.chapter2.model.Customer;

import java.util.List;
import java.util.Map;

/**
 * Created by Bran-Shi on 5/18/2017.
 * 提供客户数据服务
 */
public class CustomerService {

    /**
     * 获取客户列表
     */
    public List<Customer> getCustomerList(String keyword) {
        return null;
    }

    /**
     * 获取客户
     */
    public Customer getCustomer(long id) {
        return null;
    }

    /**
     * 创建客户
     */
    public boolean createCustomer(Map<String, Object> filedMap) {
        return false;
    }

    /**
     * 更新客户
     */
    public boolean updateCustomer(long id, Map<String, Object> filedMap) {
        return false;
    }

    /**
     * 删除客户
     */
    public boolean deleteCustomer(long id){
        return false;
    }
}
