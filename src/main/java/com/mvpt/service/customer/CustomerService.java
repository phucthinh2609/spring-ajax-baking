package com.mvpt.service.customer;

import com.mvpt.model.Customer;
import com.mvpt.model.Deposit;
import com.mvpt.model.Transfer;
import com.mvpt.service.IGeneralService;

import java.util.List;

public interface CustomerService extends IGeneralService<Customer> {

    List<Customer> findAllByIdNot(Long id);

    Boolean existsByEmail(String email);

    void deposit(Customer customer, Deposit deposit);

    void doTransfer(Transfer transfer);
}
