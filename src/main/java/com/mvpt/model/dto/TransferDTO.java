package com.mvpt.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.*;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TransferDTO {

    @NotBlank(message = "Sender is required")
    @Pattern(regexp = "^[0-9]$", message = "Sender ID only digit")
    private String senderId;

    private String email;
    private String senderName;

    @NotBlank(message = "Recipient is required")
    @Pattern(regexp = "^[0-9]$", message = "Recipient ID only digit")
    private String recipientId;

    private String balance;

    @NotBlank(message = "Transfer Amount is required")
    @Size(min = 1, max = 7, message = "length of transfer amount in between 50 to 100.000")
    @DecimalMin(value = "50", message = "Min is 50")
    @DecimalMax(value = "100000", message = "Min is 100.000")
    private String transferAmount;

}
