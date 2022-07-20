package com.mvpt.service.customer;

import com.mvpt.model.Customer;
import com.mvpt.model.LocationRegion;
import com.mvpt.model.Transfer;
import com.mvpt.model.dto.CustomerDTO;
import com.mvpt.model.dto.DepositDTO;
import com.mvpt.model.dto.WithdrawDTO;
import com.mvpt.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;


@Service
@Transactional
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private DepositRepository depositRepository;

    @Autowired
    private WithdrawRepository withdrawRepository;

    @Autowired
    private TransferRepository transferRepository;

    @Autowired
    private LocationRegionRepository locationRegionRepository;

    @Override
    public List<Customer> findAll() {
        return customerRepository.findAll();
    }

    @Override
    public List<Customer> findAllByDeletedFalse() {
        return customerRepository.findAllByDeletedFalse();
    }

    @Override
    public List<CustomerDTO> findAllCustomerDTOByDeletedIsFalse() {
        return customerRepository.findAllCustomerDTOByDeletedIsFalse();
    }

    @Override
    public Optional<Customer> findById(Long id) {
        return customerRepository.findById(id);
    }

    @Override
    public Optional<CustomerDTO> getCustomerDTOById(Long id) {
        return customerRepository.getCustomerDTOById(id);
    }

    @Override
    public Optional<CustomerDTO> findCustomerDTOByEmailAndIdIsNot(String email, Long id) {
        return customerRepository.findCustomerDTOByEmailAndIdIsNot(email, id);
    }

    @Override
    public List<Customer> findAllByIdNot(Long id) {
        return customerRepository.findAllByIdNot(id);
    }

    @Override
    public Customer getById(Long id) {
        return customerRepository.getById(id);
    }

    @Override
    public Boolean existsByEmail(String email) {
        return customerRepository.existsByEmail(email);
    }

    @Override
    public Customer save(Customer customer) {
        LocationRegion locationRegion = locationRegionRepository.save(customer.getLocationRegion());
        customer.setLocationRegion(locationRegion);
        return customerRepository.save(customer);
    }

    @Override
    public Optional<CustomerDTO> doDeposit(DepositDTO depositDTO) {
        long customerId = Long.parseLong(depositDTO.getCustomerId());
        BigDecimal transactionAmount = new BigDecimal(Long.parseLong(depositDTO.getTransactionAmount()));

        customerRepository.incrementBalance(customerId, transactionAmount);

        Optional<CustomerDTO> customerDTO = customerRepository.getCustomerDTOById(customerId);

        depositRepository.save(depositDTO.toDeposit(customerDTO.get().toCustomer()));

        return customerDTO;
    }

    @Override
    public Optional<CustomerDTO> doWithdraw(WithdrawDTO withdrawDTO) {
        long customerId = Long.parseLong(withdrawDTO.getCustomerId());
        BigDecimal transactionAmount = new BigDecimal(Long.parseLong(withdrawDTO.getTransactionAmount()));

        customerRepository.reduceBalance(customerId, transactionAmount);

        Optional<CustomerDTO> customerDTO = customerRepository.getCustomerDTOById(customerId);

        withdrawRepository.save(withdrawDTO.toWithdraw(customerDTO.get().toCustomer()));

        return customerDTO;
    }

    @Override
    public Map<String, CustomerDTO> doTransfer(Transfer transfer) {

        Map<String, CustomerDTO> result =  new HashMap<>();

        customerRepository.reduceBalance(transfer.getSender().getId(), transfer.getTransactionAmount());

        customerRepository.incrementBalance(transfer.getRecipient().getId(), transfer.getTransferAmount());

        transferRepository.save(transfer);

        Optional<CustomerDTO> sender = customerRepository.getCustomerDTOById(transfer.getSender().getId());
        Optional<CustomerDTO> recipient = customerRepository.getCustomerDTOById(transfer.getRecipient().getId());

        result.put("sender", sender.get());
        result.put("recipient", recipient.get());

        return result;
    }

    @Override
    public void remove(Long id) {

    }
}
