package com.mvpt.model.dto;

import com.mvpt.model.Customer;
import com.mvpt.model.Deposit;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Accessors(chain = true)
public class DepositDTO implements Validator {

    private String id;

    @NotBlank(message = "Sender ID is required")
    @Pattern(regexp = "^[0-9]+$", message = "Sender ID only digit")
    private String customerId;

    @NotBlank(message = "Transaction Amount is required")
//    @Pattern(regexp = "^[0-9]+$", message = "Transaction Amount only digit")
//    @Size(min = 1, max = 7, message = "Length of transaction amount in between 50 to 100.000")
//    @DecimalMin(value = "50", message = "Min is 50")
//    @DecimalMax(value = "100000", message = "Max is 100.000")
    private String transactionAmount;

    public Deposit toDeposit(Customer customer) {
        return new Deposit()
                .setId(0l)
                .setTransactionAmount(new BigDecimal(Long.parseLong(transactionAmount)))
                .setCustomer(customer);
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return DepositDTO.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        DepositDTO depositDTO = (DepositDTO) target;

        String transactionAmountStr = depositDTO.getTransactionAmount();

//        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "transactionAmount", "transactionAmount.emptyOrWhitespace", "The transaction amount emptyOrWhitespace");

        if (transactionAmountStr == null) {
            errors.rejectValue("transactionAmount", "transactionAmount.null(", "The transaction is not null");
            return;
        }

        if (transactionAmountStr.isEmpty()) {
            errors.rejectValue("transactionAmount", "transactionAmount.isEmpty(", "The transaction is not empty");
            return;
        }

//        if (transactionAmountStr.length() > 7){
//            errors.rejectValue("transactionAmount", "transactionAmount.length", "The transaction amount length from 2 to 7");
//            return;
//        }

        if (!transactionAmountStr.matches("(^$|[0-9]*$)")){
            errors.rejectValue("transactionAmount", "transactionAmount.matches", "The transaction amount only digit");
            return;
        }

        BigDecimal transactionAmount = new BigDecimal(Long.parseLong(depositDTO.getTransactionAmount()));
        BigDecimal min = new BigDecimal(50L);
        BigDecimal max = new BigDecimal(1000000L);

        if (transactionAmount.compareTo(min) < 0) {
            errors.rejectValue("transactionAmount", "transactionAmount.min", "The transaction amount min is 50");
            return;
        }

        if (transactionAmount.compareTo(max) > 0) {
            errors.rejectValue("transactionAmount", "transactionAmount.max", "The transaction amount max is 1.000.000");
            return;
        }
    }
}
