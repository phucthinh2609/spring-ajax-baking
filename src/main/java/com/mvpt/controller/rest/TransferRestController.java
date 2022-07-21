package com.mvpt.controller.rest;

import com.mvpt.model.Transfer;
import com.mvpt.model.dto.ITransferDTO;
import com.mvpt.model.dto.TransferHistoryDTO;
import com.mvpt.service.transfer.TransferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/transfers")
public class TransferRestController {

    @Autowired
    private TransferService transferService;

    @GetMapping("/histories")
    public ResponseEntity<?> getAllTransferHistory() {

        List<TransferHistoryDTO> transferHistoryDTOS = transferService.getAllHistory();

        return new ResponseEntity<>(transferHistoryDTOS, HttpStatus.OK);
    }

    @GetMapping("/histories-inf")
    public ResponseEntity<?> getAllTransferHistoryInf() {

        List<ITransferDTO> transferDTOSInf = transferService.getAllHistoryItf();

        return new ResponseEntity<>(transferDTOSInf, HttpStatus.OK);
    }

}
