package com.example.bankTransfers.controller;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
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
import com.example.bankTransfers.exception.ErrorResponse;
import com.example.bankTransfers.exception.InvalidSchedulingException;
import com.example.bankTransfers.exception.TransferNotFoundException;
import com.example.bankTransfers.model.Transfer;
import com.example.bankTransfers.repository.AccountRepository;
import com.example.bankTransfers.repository.TransferRepository;
import com.example.bankTransfers.service.TransferService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/transfers")
@CrossOrigin(origins = "*") 
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
	public ResponseEntity<?> createTransfer(@RequestBody TransferRequest request, HttpServletRequest httpRequest) {
		
		try {
			checkAccount(request);
	        BigDecimal fee = transferService.calculateFee(request.getAmount(), request.getScheduledDate());
	        Transfer transfer = new Transfer(request.getFromAccount(), request.getToAccount(), request.getAmount(), request.getScheduledDate(), fee);
	        transferRepository.save(transfer);
	        
	        ApiResponse apiResponse = new ApiResponse(
		            HttpStatus.OK.value(),
		            httpRequest.getRequestURI(),
		            transfer);
	        return ResponseEntity.status(HttpStatus.OK).body(apiResponse); 
		} catch (AccountNotFoundException ex) {
			ErrorResponse errorResponse = new ErrorResponse(
		            HttpStatus.NOT_FOUND.value(),
		            HttpStatus.NOT_FOUND.getReasonPhrase(),
		            ex.getMessage(),
		            httpRequest.getRequestURI());
		    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
		} catch (InvalidSchedulingException ex) {
			ErrorResponse errorResponse = new ErrorResponse(
		            HttpStatus.BAD_REQUEST.value(),
		            HttpStatus.BAD_REQUEST.getReasonPhrase(),
		            ex.getMessage(),
		            httpRequest.getRequestURI());
		        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
		}
    }
	
	@PutMapping("/{id}")
    public ResponseEntity<?> updateTransfer(@PathVariable Long id, @RequestBody TransferRequest request, HttpServletRequest httpRequest) {
		
		try {
			checkAccount(request);
		    
	        Transfer transfer = transferRepository.findById(id).orElseThrow(() -> new TransferNotFoundException("Transfer with ID " + id + " not found"));
	        transfer.setFromAccount(request.getFromAccount() != null ? request.getFromAccount() : transfer.getFromAccount());
	        transfer.setToAccount(request.getToAccount() != null ? request.getToAccount() : transfer.getToAccount());
	        //Se o amount mudar, o fee tb muda. Se a data mudar a regra tb muda
	        if (request.getAmount() != null && request.getScheduledDate() != null) {
	        	transfer.setAmount(request.getAmount());
		        transfer.setScheduledDate(request.getScheduledDate());
		        transfer.setFee(transferService.calculateFee(request.getAmount(), request.getScheduledDate()));
	        } else if(request.getAmount() != null) {
	        	//enviou amount e data nao -> calcular nova fee
	        	transfer.setAmount(request.getAmount());
	        	transfer.setScheduledDate(transfer.getScheduledDate());
	        	transfer.setFee(transferService.calculateFee(request.getAmount(), transfer.getScheduledDate()));
	        } else if(request.getScheduledDate() != null){
	        	//enviou data e amount nao -> calcular nova fee
	        	transfer.setAmount(transfer.getAmount());
	        	transfer.setScheduledDate(request.getScheduledDate());
	        	transfer.setFee(transferService.calculateFee(transfer.getAmount(), request.getScheduledDate()));
	        } else {
	        	//nao enviou nenhum -> mesma fee
		        transfer.setFee(transfer.getFee());
	        }
	        transferRepository.save(transfer);
	        
	        ApiResponse apiResponse = new ApiResponse(
		            HttpStatus.OK.value(),
		            httpRequest.getRequestURI(),
		            transfer);
		    return ResponseEntity.status(HttpStatus.OK).body(apiResponse); 
		} catch (AccountNotFoundException | TransferNotFoundException ex) {
			ErrorResponse errorResponse = new ErrorResponse(
	            HttpStatus.NOT_FOUND.value(),
	            HttpStatus.NOT_FOUND.getReasonPhrase(),
	            ex.getMessage(),
	            httpRequest.getRequestURI());
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
		} catch (InvalidSchedulingException ex) {
			ErrorResponse errorResponse = new ErrorResponse(
		            HttpStatus.BAD_REQUEST.value(),
		            HttpStatus.BAD_REQUEST.getReasonPhrase(),
		            ex.getMessage(),
		            httpRequest.getRequestURI());
		        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
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
    		ErrorResponse errorResponse = new ErrorResponse(
    	            HttpStatus.NOT_FOUND.value(),
    	            HttpStatus.NOT_FOUND.getReasonPhrase(),
    	            ex.getMessage(),
    	            httpRequest.getRequestURI());
    	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    	}
    }
    
	@DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTransfer(@PathVariable Long id, HttpServletRequest httpRequest) {
		try {
	        transferRepository.findById(id)
	                .orElseThrow(() -> new TransferNotFoundException("Transfer with ID " + id + " not found"));

			transferRepository.deleteById(id);
			return ResponseEntity.ok(null);
		} catch (TransferNotFoundException ex) {
			ErrorResponse errorResponse = new ErrorResponse(
    	            HttpStatus.NOT_FOUND.value(),
    	            HttpStatus.NOT_FOUND.getReasonPhrase(),
    	            ex.getMessage(),
    	            httpRequest.getRequestURI());
    	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
		}
    }
	
	private void checkAccount(TransferRequest request) throws AccountNotFoundException {
		if (!accountRepository.existsById(request.getFromAccount()) || !accountRepository.existsById(request.getToAccount())) {
	        throw new AccountNotFoundException("No account was found with that Id");
	    }
	}
}
