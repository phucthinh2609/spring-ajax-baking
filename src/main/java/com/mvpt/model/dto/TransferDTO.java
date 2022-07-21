package com.mvpt.model.dto;

import com.mvpt.model.Customer;
import com.mvpt.model.Transfer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.Column;
import javax.validation.constraints.*;
import java.math.BigDecimal;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Accessors(chain = true)

public class TransferDTO {

    @NotBlank(message = "Sender is required")
    @Pattern(regexp = "^[0-9]+$", message = "Sender ID only digit")
    private String senderId;

//    private String email;
//    private String senderName;

    @NotBlank(message = "Recipient is required")
    @Pattern(regexp = "^[0-9]+$", message = "Recipient ID only digit")
    private String recipientId;

//    private String balance;

    @NotBlank(message = "Transfer Amount is required")
    @Size(min = 1, max = 7, message = "length of transfer amount in between 50 to 100.000")
    private String transferAmount;

    private String fees;
    private String feesAmount;
    private String transactionAmount;

    public Transfer toTransfer(Customer sender, Customer recipient) {
        return new Transfer()
                .setSender(sender)
                .setRecipient(recipient)
                .setTransferAmount(new BigDecimal(Long.parseLong(transferAmount)))
                .setFees(Long.parseLong(fees))
                .setFeesAmount(new BigDecimal(Long.parseLong(feesAmount)))
                .setTransactionAmount(new BigDecimal(Long.parseLong(transactionAmount)));
    }

}
