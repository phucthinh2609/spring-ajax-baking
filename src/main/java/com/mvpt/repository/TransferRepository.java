package com.mvpt.repository;

import com.mvpt.model.Transfer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface TransferRepository extends JpaRepository<Transfer, Long> {
}
