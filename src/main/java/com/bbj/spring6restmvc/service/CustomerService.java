package com.bbj.spring6restmvc.service;

import com.bbj.spring6restmvc.model.Customer;

import java.util.List;
import java.util.UUID;

/**
 * Created by jt, Spring Framework Guru.
 */
public interface CustomerService {

    Customer getCustomerById(UUID uuid);
    List<Customer> getAllCustomers();
}
