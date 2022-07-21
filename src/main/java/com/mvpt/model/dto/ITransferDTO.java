package com.mvpt.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.util.Date;

public interface ITransferDTO {

    Long getId();
    Long getSenderId();
    String getSenderName();
    Long getRecipientId();
    String getRecipientName();

    @JsonFormat(pattern = "dd/MM/yyyy")
    Date getCreatedOn();

    @JsonFormat(pattern = "HH:mm:ss", timezone = "Asia/Ho_Chi_Minh")
    Date getCreatedAt();
    BigDecimal getTransferAmount();
    Long getFees();
    BigDecimal getFeesAmount();
    BigDecimal getTransactionAmount();
}
