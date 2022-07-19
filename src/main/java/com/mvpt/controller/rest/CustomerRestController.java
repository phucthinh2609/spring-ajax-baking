package com.mvpt.controller.rest;


import com.mvpt.exception.EmailExistsException;
import com.mvpt.exception.ResourceNotFoundException;
import com.mvpt.model.Customer;
import com.mvpt.model.Deposit;
import com.mvpt.model.dto.CustomerDTO;
import com.mvpt.model.dto.DepositDTO;
import com.mvpt.service.customer.CustomerService;
import com.mvpt.service.deposit.DepositService;
import com.mvpt.utils.AppUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
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
//            return new ResponseEntity<>(customerOptional.get(), HttpStatus.OK);
            throw new ResourceNotFoundException("Invalid customer Id");
        }

        return new ResponseEntity<>(customerOptional.get().toCustomerDTO(), HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<?> doCreate(@RequestBody CustomerDTO customerDTO) {

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

    @PostMapping("/deposit")
    public ResponseEntity<?> doDeposit(@Validated @RequestBody DepositDTO depositDTO, BindingResult bindingResult){

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
}
