package com.shi.chapter2.service;

import com.shi.chapter2.helper.DatabaseHelper;
import com.shi.chapter2.model.Customer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.shi.chapter2.util.PropsUtil.LOGGER;


/**
 * Created by Bran-Shi on 5/18/2017
 * 提供客户数据服务
 */

public class CustomerService {
/*    private static final String DRIVER;
    private static final String URL;
    private static final String USERNAME;
    private static final String PASSWORD;
    static {
        Properties conf = PropsUtil.loadProps("config.properties");
        DRIVER = conf.getProperty("jdbc.driver");
        URL = conf.getProperty("jdbc.url");
        USERNAME = conf.getProperty("jdbc.username");
        PASSWORD = conf.getProperty("jdbc.password");

        try {
            Class.forName(DRIVER);
        } catch (ClassNotFoundException e) {
            LOGGER.error("can not load jdbc driver");
        }
    }
*/

    /**
     * 获取客户列表
     */

    public List<Customer> getCustomerList() {
        Connection conn = null;
        List<Customer> customerList = new ArrayList<Customer>();
        try {
            String sql = "select * from customer";
            // conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);

            conn = DatabaseHelper.getconnect();
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Customer customer = new Customer();
                customer.setId(rs.getLong("id"));
                customer.setName(rs.getString("name"));
                customer.setContact(rs.getString("contact"));
                customer.setTelephone(rs.getString("telephone"));
                customer.setEmail(rs.getString("email"));
                customer.setRemark(rs.getString("remark"));
                customerList.add(customer);
            }

            //return DatabaseHelper.queryEntityList(Customer.class,sql,conn);
        } catch (SQLException e) {
            e.printStackTrace();
            LOGGER.error("execute sql failure");
        } finally {
            /*if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    LOGGER.error("close connect failure", e);
                }
            }*/
            DatabaseHelper.closeConnect(conn);
        }
        return customerList;
    }
    /**测试
    public static void main(String[] args) {
        CustomerService c = new CustomerService();
        List<Customer> list= c.getCustomerList();
        System.out.println(list.get(0).getContact());
    }
     */
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
    public boolean deleteCustomer(long id) {
        return false;
    }
}
