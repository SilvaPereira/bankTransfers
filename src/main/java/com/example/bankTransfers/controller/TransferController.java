package com.example.bankTransfers.controller;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.bankTransfers.dto.TransferRequest;
import com.example.bankTransfers.model.Transfer;
import com.example.bankTransfers.repository.TransferRepository;
import com.example.bankTransfers.service.TransferService;

@RestController
@RequestMapping("/transfers")
public class TransferController {

	private final TransferRepository transferRepository;
	private final TransferService transferService;
	
	public TransferController(TransferService transferService, TransferRepository transferRepository) {
		this.transferService = transferService;
	    this.transferRepository = transferRepository;
	}
    
	@PostMapping
	public Transfer createTransfer(@RequestBody TransferRequest request) {
		//TODO validar se existem os clientes na base
        BigDecimal fee = transferService.calculateFee(request.getAmount(), request.getScheduledDate());
        Transfer transfer = new Transfer(request.getFromAccount(), request.getToAccount(), request.getAmount(), request.getScheduledDate(), fee);
        return transferRepository.save(transfer);
    }
	
	@PutMapping("/{id}")
    public Transfer updateTransfer(@PathVariable Long id, @RequestBody TransferRequest request) {
		//TODO validar se existem os clientes na base
        Transfer transfer = transferRepository.findById(id).orElseThrow(() -> new RuntimeException("Transfer not found"));
        transfer.setFromAccount(request.getFromAccount());
        transfer.setToAccount(request.getToAccount());
        transfer.setAmount(request.getAmount());
        transfer.setScheduledDate(request.getScheduledDate());
        transfer.setFee(transferService.calculateFee(request.getAmount(), request.getScheduledDate()));
        return transferRepository.save(transfer);
    }
	
	@GetMapping
    public List<Transfer> getAllTransfers() {
        return transferRepository.findAll();
    }

    @GetMapping("/{id}")
    public Transfer getTransferById(@PathVariable Long id) {
        return transferRepository.findById(id).orElseThrow(() -> new RuntimeException("Transfer not found"));
    }
    
	@DeleteMapping("/{id}")
    public void deleteTransfer(@PathVariable Long id) {
        transferRepository.deleteById(id);
    }
}
