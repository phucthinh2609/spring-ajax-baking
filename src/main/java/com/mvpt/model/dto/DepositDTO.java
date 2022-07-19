package com.mvpt.model.dto;

import com.mvpt.model.Customer;
import com.mvpt.model.Deposit;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Accessors(chain = true)
public class DepositDTO {

    private String id;

    @NotBlank(message = "Sender ID is required")
    @Pattern(regexp = "^[0-9]+$", message = "Sender ID only digit")
    private String customerId;

    @NotBlank(message = "Transaction Amount is required")
    @Pattern(regexp = "^[0-9]+$", message = "Transaction Amount only digit")
    @Size(min = 1, max = 7, message = "Length of transaction amount in between 50 to 100.000")
    @DecimalMin(value = "50", message = "Min is 50")
    @DecimalMax(value = "100000", message = "Max is 100.000")
    private String transactionAmount;

    public Deposit toDeposit(Customer customer) {
        return new Deposit()
                .setId(0l)
                .setTransactionAmount(new BigDecimal(Long.parseLong(transactionAmount)))
                .setCustomer(customer);
    }
}
