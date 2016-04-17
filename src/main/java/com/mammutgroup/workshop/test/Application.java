package com.mammutgroup.workshop.test;

import com.mammutgroup.workshop.test.customer.CustomerCrudRestTest;

import java.io.IOException;

/**
 * @author mushtu
 * @since 4/17/16.
 */
public class Application {

    public static void main(String[] args)
    {
        try {
            new CustomerCrudRestTest().createCustomer();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
