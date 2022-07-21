package com.mvpt.repository;

import com.mvpt.model.Transfer;
import com.mvpt.model.dto.ITransferDTO;
import com.mvpt.model.dto.TransferHistoryDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface TransferRepository extends JpaRepository<Transfer, Long> {

    @Query("SELECT new com.mvpt.model.dto.TransferHistoryDTO (" +
            "t.id, " +
            "t.sender.id, " +
            "t.sender.fullName, " +
            "t.recipient.id, " +
            "t.recipient.fullName, " +
            "t.createdAt, " +
            "t.createdAt, " +
            "t.transferAmount, " +
            "t.fees, " +
            "t.feesAmount, " +
            "t.transactionAmount" +
            ") " +
            "FROM Transfer AS t"
    )
    List<TransferHistoryDTO> getAllHistory();

    @Query("SELECT NEW com.mvpt.model.dto.TransferHistoryDTO (" +
            "t.id, " +
            "t.sender.id, " +
            "t.sender.fullName, " +
            "t.recipient.id, " +
            "t.recipient.fullName, " +
            "t.createdAt, " +
            "t.createdAt, " +
            "t.transferAmount, " +
            "t.fees, " +
            "t.feesAmount, " +
            "t.transactionAmount) " +
            "FROM Transfer t")

    List<ITransferDTO> getAllHistoryItf();
}
