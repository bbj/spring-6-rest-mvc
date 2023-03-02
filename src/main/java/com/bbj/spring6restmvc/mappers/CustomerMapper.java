package com.bbj.spring6restmvc.mappers;

import com.bbj.spring6restmvc.entities.Customer;
import com.bbj.spring6restmvc.model.CustomerDTO;
import org.mapstruct.Mapper;

@Mapper
public interface CustomerMapper {

    Customer customerDtoToCustomer(CustomerDTO dto);
    CustomerDTO customerToCustomerDto(Customer customer);
}