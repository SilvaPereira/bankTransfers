package com.example.bankTransfers.controller;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.bankTransfers.model.Transfer;
import com.example.bankTransfers.repository.TransferRepository;
import com.example.bankTransfers.service.TransferService;

@RestController
@RequestMapping("/transfers")
public class TransferController {

	private final TransferRepository transferRepository;
	
	 public TransferController(TransferRepository transferRepository) {
		 this.transferRepository = transferRepository;
     }
    
	@Autowired
	TransferService transferService;
	
	@PostMapping
	public Transfer createTransfer(@RequestBody TransferRequest request) {
        BigDecimal fee = transferService.calculateFee(request.getAmount(), request.getScheduledDate());
        Transfer transfer = new Transfer(null, request.getSourceAccount(), request.getTargetAccount(), request.getAmount(), request.getScheduledDate(), fee);
        return transferRepository.save(transfer);
    }
	
}
