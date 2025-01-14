package com.example.bankTransfers.controller;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.bankTransfers.dto.TransferRequest;
import com.example.bankTransfers.exception.AccountNotFoundException;
import com.example.bankTransfers.exception.ApiResponse;
import com.example.bankTransfers.exception.InvalidSchedulingException;
import com.example.bankTransfers.exception.TransferNotFoundException;
import com.example.bankTransfers.model.Transfer;
import com.example.bankTransfers.repository.AccountRepository;
import com.example.bankTransfers.repository.TransferRepository;
import com.example.bankTransfers.service.TransferService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/transfers")
public class TransferController {

	private final TransferRepository transferRepository;
	private final AccountRepository accountRepository;
	private final TransferService transferService;
	
	public TransferController(TransferService transferService, TransferRepository transferRepository, AccountRepository accountRepository) {
		this.accountRepository = accountRepository;
		this.transferService = transferService;
	    this.transferRepository = transferRepository;
	}
    
	@PostMapping
	public ResponseEntity<ApiResponse> createTransfer(@RequestBody TransferRequest request, HttpServletRequest httpRequest) {
		
		try {
			checkAccount(request);
	        BigDecimal fee = transferService.calculateFee(request.getAmount(), request.getScheduledDate());
	        Transfer transfer = new Transfer(request.getFromAccount(), request.getToAccount(), request.getAmount(), request.getScheduledDate(), fee);
	        transferRepository.save(transfer);
	        
	        ApiResponse apiResponse = new ApiResponse(
		            HttpStatus.OK.value(),
		            HttpStatus.OK.getReasonPhrase(),
		            null,
		            httpRequest.getRequestURI());
	        return ResponseEntity.status(HttpStatus.OK).body(apiResponse); 
		} catch (AccountNotFoundException ex) {
			ApiResponse apiResponse = new ApiResponse(
		            HttpStatus.NOT_FOUND.value(),
		            HttpStatus.NOT_FOUND.getReasonPhrase(),
		            ex.getMessage(),
		            httpRequest.getRequestURI());
		    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiResponse);
		} catch (InvalidSchedulingException ex) {
			ApiResponse apiResponse = new ApiResponse(
		            HttpStatus.BAD_REQUEST.value(),
		            HttpStatus.BAD_REQUEST.getReasonPhrase(),
		            ex.getMessage(),
		            httpRequest.getRequestURI());
		        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiResponse);
		}
    }
	
	@PutMapping("/{id}")
    public ResponseEntity<ApiResponse> updateTransfer(@PathVariable Long id, @RequestBody TransferRequest request, HttpServletRequest httpRequest) {
		
		try {
			checkAccount(request);
		    
	        Transfer transfer = transferRepository.findById(id).orElseThrow(() -> new TransferNotFoundException("Transfer with ID " + id + " not found"));
	        transfer.setFromAccount(request.getFromAccount());
	        transfer.setToAccount(request.getToAccount());
	        transfer.setAmount(request.getAmount());
	        transfer.setScheduledDate(request.getScheduledDate());
	        transfer.setFee(transferService.calculateFee(request.getAmount(), request.getScheduledDate()));
	        transferRepository.save(transfer);
	        
	        ApiResponse apiResponse = new ApiResponse(
		            HttpStatus.OK.value(),
		            HttpStatus.OK.getReasonPhrase(),
		            null,
		            httpRequest.getRequestURI());
		    return ResponseEntity.status(HttpStatus.OK).body(apiResponse); 
		} catch (AccountNotFoundException | TransferNotFoundException ex) {
			ApiResponse apiResponse = new ApiResponse(
	            HttpStatus.NOT_FOUND.value(),
	            HttpStatus.NOT_FOUND.getReasonPhrase(),
	            ex.getMessage(),
	            httpRequest.getRequestURI());
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiResponse);
		} catch (InvalidSchedulingException ex) {
			ApiResponse apiResponse = new ApiResponse(
		            HttpStatus.BAD_REQUEST.value(),
		            HttpStatus.BAD_REQUEST.getReasonPhrase(),
		            ex.getMessage(),
		            httpRequest.getRequestURI());
		        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiResponse);
		}
    }
	
	@GetMapping
    public List<Transfer> getAllTransfers() {
        return transferRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getTransferById(@PathVariable Long id, HttpServletRequest httpRequest) {
    	try {
            Transfer transfer = transferRepository.findById(id)
                .orElseThrow(() -> new TransferNotFoundException("Transfer with ID " + id + " not found"));
            return ResponseEntity.ok(transfer);
    	} catch (TransferNotFoundException ex) {
    		ApiResponse apiResponse = new ApiResponse(
    	            HttpStatus.NOT_FOUND.value(),
    	            HttpStatus.NOT_FOUND.getReasonPhrase(),
    	            ex.getMessage(),
    	            httpRequest.getRequestURI());
    	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiResponse);
    	}
    }
    
	@DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTransfer(@PathVariable Long id, HttpServletRequest httpRequest) {
		try {
			transferRepository.deleteById(id);
			return ResponseEntity.ok(null);
		} catch (TransferNotFoundException ex) {
			ApiResponse apiResponse = new ApiResponse(
    	            HttpStatus.NOT_FOUND.value(),
    	            HttpStatus.NOT_FOUND.getReasonPhrase(),
    	            ex.getMessage(),
    	            httpRequest.getRequestURI());
    	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiResponse);
		}
        
    }
	
	private void checkAccount(TransferRequest request) throws AccountNotFoundException {
		if (!accountRepository.existsById(request.getFromAccount()) || !accountRepository.existsById(request.getToAccount())) {
	        throw new AccountNotFoundException("No account was found with that Id");
	    }
	}
}
