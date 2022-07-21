package com.mvpt.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter

public class TransferHistoryDTO {

    private Long id;
    private Long senderId;
    private String senderName;
    private Long recipientId;
    private String recipientName;

    @JsonFormat(pattern = "dd/MM/yyyy")
    private Date createdOn;

    @JsonFormat(pattern = "HH:mm:ss", timezone = "Aisa/Ho Chi Minh")
    private Date createdAt;

    private BigDecimal transferAmount;
    private Long fees;
    private BigDecimal feesAmount;
    private BigDecimal transactionAmount;
}
