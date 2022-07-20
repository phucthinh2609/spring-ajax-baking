package com.mvpt.service.customer;

import com.mvpt.model.Customer;
import com.mvpt.model.Transfer;
import com.mvpt.model.dto.CustomerDTO;
import com.mvpt.model.dto.DepositDTO;
import com.mvpt.model.dto.WithdrawDTO;
import com.mvpt.service.IGeneralService;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface CustomerService extends IGeneralService<Customer> {

    List<Customer> findAllByDeletedFalse();

    List<CustomerDTO> findAllCustomerDTOByDeletedIsFalse();

    Optional<CustomerDTO> getCustomerDTOById(Long id);

    Optional<CustomerDTO> findCustomerDTOByEmailAndIdIsNot(String email, Long id);

    List<Customer> findAllByIdNot(Long id);

    Boolean existsByEmail(String email);

    Optional<CustomerDTO> doDeposit(DepositDTO depositDTO);

    Optional<CustomerDTO> doWithdraw(WithdrawDTO withdrawDTO);

    Map<String, CustomerDTO> doTransfer(Transfer transfer);
}
