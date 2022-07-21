package com.mvpt.service.transfer;

import com.mvpt.model.Transfer;
import com.mvpt.model.dto.ITransferDTO;
import com.mvpt.model.dto.TransferHistoryDTO;
import com.mvpt.service.IGeneralService;

import java.util.List;

public interface TransferService extends IGeneralService<Transfer> {

    List<TransferHistoryDTO> getAllHistory();

    List<ITransferDTO> getAllHistoryItf();


}
