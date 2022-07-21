package com.mvpt.controller.rest;


import com.mvpt.exception.DataInputException;
import com.mvpt.exception.EmailExistsException;
import com.mvpt.exception.ResourceNotFoundException;
import com.mvpt.model.BaseEntities;
import com.mvpt.model.Customer;
import com.mvpt.model.Deposit;
import com.mvpt.model.dto.CustomerDTO;
import com.mvpt.model.dto.DepositDTO;
import com.mvpt.model.dto.TransferDTO;
import com.mvpt.model.dto.WithdrawDTO;
import com.mvpt.service.customer.CustomerService;
import com.mvpt.service.deposit.DepositService;
import com.mvpt.utils.AppUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/customers")
public class CustomerRestController {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private DepositService depositService;

    @Autowired
    private AppUtil appUtil;

    @GetMapping()
    public ResponseEntity<?> findAll() {
        try {
            Iterable<CustomerDTO> customerDTOS = customerService.findAllCustomerDTOByDeletedIsFalse();

            return new ResponseEntity<>(customerDTOS, HttpStatus.CREATED) ;

        } catch (Exception ex) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCustomerById(@PathVariable long id) {
        Optional<Customer> customerOptional = customerService.findById(id);

        if (!customerOptional.isPresent()){
            throw new ResourceNotFoundException("Invalid customer Id");
        }

        return new ResponseEntity<>(customerOptional.get().toCustomerDTO(), HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<?> doCreate(@Validated @RequestBody CustomerDTO customerDTO, BindingResult bindingResult) {

        if (bindingResult.hasFieldErrors()) {
            return appUtil.mapErrorToResponse(bindingResult);
        }

        customerDTO.setId(0L);
        customerDTO.setBalance(new BigDecimal(0L));
        customerDTO.getLocationRegion().setId(0L);

        Boolean exitsEmail = customerService.existsByEmail(customerDTO.getEmail());

        if (exitsEmail) {
//            return new ResponseEntity<>("Email exits", HttpStatus.CONFLICT);
            throw new EmailExistsException("Email already exists");
        }

        Customer newCustomer = customerService.save(customerDTO.toCustomer());

        return new ResponseEntity<>(newCustomer.toCustomerDTO(), HttpStatus.CREATED);
    }

    @PostMapping("/edit")
    public ResponseEntity<?> doUpdate(@Validated @RequestBody CustomerDTO customerDTO, BindingResult bindingResult) {

        if (bindingResult.hasFieldErrors()) {
            return appUtil.mapErrorToResponse(bindingResult);
        }

        Optional<CustomerDTO> optionalCustomerDTO = customerService.findCustomerDTOByEmailAndIdIsNot(customerDTO.getEmail(), customerDTO.getId());

        if (optionalCustomerDTO.isPresent()) {
            throw new EmailExistsException("Email already exits");
        }

        try {
            Customer updateCustomer = customerService.save(customerDTO.toCustomer());

            return new ResponseEntity<>(updateCustomer.toCustomerDTO(), HttpStatus.CREATED);

        } catch (DataIntegrityViolationException ex) {
            throw new DataInputException("Invalid update customer information");
        }
    }

    @PostMapping("/deposit")
    public ResponseEntity<?> doDeposit(@Validated @RequestBody DepositDTO depositDTO, BindingResult bindingResult){

        new DepositDTO().validate(depositDTO, bindingResult);

        if (bindingResult.hasFieldErrors()) {
            return appUtil.mapErrorToResponse(bindingResult);
        }

        Optional<Customer> customerOptional = customerService.findById(Long.valueOf(depositDTO.getCustomerId()));

        if (!customerOptional.isPresent()) {
            throw new ResourceNotFoundException("Invalid customer ID");
        }

        Optional<CustomerDTO> customerDTO = customerService.doDeposit(depositDTO);

        return new ResponseEntity<>(customerDTO.get(), HttpStatus.CREATED);
    }

    @PostMapping("/withdraw")
    public ResponseEntity<?> doWithdraw(@Validated @RequestBody WithdrawDTO withdrawDTO, BindingResult bindingResult){

        new WithdrawDTO().validate(withdrawDTO, bindingResult);

        if (bindingResult.hasFieldErrors()) {
            return appUtil.mapErrorToResponse(bindingResult);
        }

        Optional<Customer> customerOptional = customerService.findById(Long.valueOf(withdrawDTO.getCustomerId()));

        if (customerOptional.isPresent()) {
            BigDecimal currentBalance = customerOptional.get().getBalance();
            BigDecimal transactionAmount = new BigDecimal(Long.parseLong(withdrawDTO.getTransactionAmount()));

            if (currentBalance.compareTo(transactionAmount) >=0) {
                try {
                    Optional<CustomerDTO> customerDTO = customerService.doWithdraw(withdrawDTO);
                    return new ResponseEntity<>(customerDTO.get(), HttpStatus.CREATED);

                } catch (DataIntegrityViolationException ex) {
                    throw new ResourceNotFoundException("Invalid customer ID");
                }
            }else {
                throw new DataInputException("Balance not enough");
            }
        }else {
            throw new ResourceNotFoundException("Invalid customer ID");

        }
    }

    @PostMapping("/transfer")
    public ResponseEntity<?> doTransfer(@Validated @RequestBody TransferDTO transferDTO, BindingResult bindingResult){

//        new TransferDTO().validate(transferDTO, bindingResult);

        if (bindingResult.hasFieldErrors()) {
            return appUtil.mapErrorToResponse(bindingResult);
        }

        Optional<Customer> senderOptional = customerService.findById(Long.valueOf(transferDTO.getSenderId()));

        if (!senderOptional.isPresent()) {
            throw new ResourceNotFoundException("Invalid sender ID");
        }

        Optional<Customer> recipientOptional = customerService.findById(Long.valueOf(transferDTO.getRecipientId()));

        if (!recipientOptional.isPresent()) {
            throw new ResourceNotFoundException("Invalid recipient ID");
        }

        BigDecimal currentBalance = senderOptional.get().getBalance();
        BigDecimal transferAmout = new BigDecimal(Long.parseLong(transferDTO.getTransferAmount()));
        Long fees = 10L;
        BigDecimal fessAmount = transferAmout.multiply(BigDecimal.valueOf(fees)).divide(new BigDecimal(100L));
        BigDecimal transactionAmout = transferAmout.add(fessAmount);

        transferDTO.setFees(String.valueOf(fees));
        transferDTO.setFeesAmount(String.valueOf(fessAmount));
        transferDTO.setTransferAmount(String.valueOf(transferAmout));
        transferDTO.setTransactionAmount(String.valueOf(transactionAmout));

        if (transactionAmout.compareTo(currentBalance) > 0) {
            throw new DataInputException("Sender's balance not enough");
        }

        try{
            Map<String, CustomerDTO> result = customerService.doTransfer(transferDTO.toTransfer(senderOptional.get(), recipientOptional.get()));

            return new ResponseEntity<>(result, HttpStatus.CREATED);

        }catch (Exception ex) {
            throw new DataInputException("Transfer fail");
        }
    }

    @PostMapping("/suspensed/{id}")
    public ResponseEntity<?> doSuspended (@PathVariable Long id) {
        Optional<Customer> customerOptional = customerService.findById(id);

        if (customerOptional.isPresent()) {
            try {
                customerOptional.get().setDeleted(true);
                customerService.save(customerOptional.get());

                return new ResponseEntity<>(HttpStatus.CREATED);

            }catch (DataIntegrityViolationException ex) {
                throw new DataInputException("Invalid suspense infomation");
            }
        }else {
            throw new DataInputException("Invalid customer information");
        }
    }
}
