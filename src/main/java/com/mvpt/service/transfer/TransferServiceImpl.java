package com.mvpt.service.transfer;

import com.mvpt.model.Transfer;
import com.mvpt.model.dto.ITransferDTO;
import com.mvpt.model.dto.TransferHistoryDTO;
import com.mvpt.repository.TransferRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class TransferServiceImpl implements TransferService {
    @Autowired
    TransferRepository transferRepository;

    @Override
    public List<Transfer> findAll() {
        return transferRepository.findAll();
    }

    @Override
    public List<TransferHistoryDTO> getAllHistory() {
        return transferRepository.getAllHistory();
    }

    @Override
    public List<ITransferDTO> getAllHistoryItf() {
        return transferRepository.getAllHistoryItf();
    }

    @Override
    public Optional<Transfer> findById(Long id) {
        return Optional.empty();
    }

    @Override
    public Transfer getById(Long id) {
        return null;
    }

    @Override
    public Transfer save(Transfer transfer) {
        return null;
    }

    @Override
    public void remove(Long id) {

    }
}
