package com.mvpt.controller.rest;


import com.mvpt.exception.EmailExistsException;
import com.mvpt.model.Customer;
import com.mvpt.model.*;
import com.mvpt.model.dto.TestDTO;
import com.mvpt.service.customer.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/customers")
public class CustomerRestController {

    @Autowired
    private CustomerService customerService;

    @PostMapping("/test")
    @ResponseBody
    public String showTest(@RequestBody TestDTO testDTO) {

        return "Hello world";
    }

    @PostMapping("/create")
    public ResponseEntity<?> doCreate(@RequestBody Customer customer) {

        customer.setId(0L);
        customer.setBalance(new BigDecimal(0L));
        customer.getLocationRegion().setId(0L);

        Boolean exitsEmail = customerService.existsByEmail(customer.getEmail());

        if (exitsEmail) {
//            return new ResponseEntity<>("Email exits", HttpStatus.CONFLICT);
            throw new EmailExistsException("Email already exists");
        }

        Customer newCustomer = customerService.save(customer);

        return new ResponseEntity<>(newCustomer, HttpStatus.CREATED);
    }
}
