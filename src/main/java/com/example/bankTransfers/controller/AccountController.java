package com.example.bankTransfers.controller;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.bankTransfers.dto.AccountRequest;
import com.example.bankTransfers.dto.TransferRequest;
import com.example.bankTransfers.exception.AccountNotFoundException;
import com.example.bankTransfers.exception.ApiResponse;
import com.example.bankTransfers.exception.TransferNotFoundException;
import com.example.bankTransfers.model.Account;
import com.example.bankTransfers.model.Transfer;
import com.example.bankTransfers.repository.AccountRepository;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/account")
public class AccountController {

	private final AccountRepository accountRepository;
	
	public AccountController(AccountRepository accountRepository) {
		this.accountRepository = accountRepository;
	}
	
	@PostMapping
	public ResponseEntity<ApiResponse> createAccount(@RequestBody AccountRequest request, HttpServletRequest httpRequest) {
		try {
	        
	        LocalDate today = LocalDate.now();
	        Account account = new Account(request.getBalance(), request.getFirstName(), request.getLastName(), today, true);
	        accountRepository.save(account);
	        
	        ApiResponse apiResponse = new ApiResponse(
		            HttpStatus.OK.value(),
		            HttpStatus.OK.getReasonPhrase(),
		            null,
		            httpRequest.getRequestURI());
	        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
		} catch () {
			
		}
	}
	
	@PutMapping("/{id}")
    public ResponseEntity<ApiResponse> updateAccount(@PathVariable Long id, @RequestBody AccountRequest request, HttpServletRequest httpRequest) {
		
		try {
			//checkAccount(request);
		    
			Account account = accountRepository.findById(id).orElseThrow(() -> new AccountNotFoundException("Account with ID " + id + " not found"));
			account.setActive(request.isActive());
			account.setBalance(request.getBalance());
			account.setFirstName(request.getFirstName());
			account.setLastName(request.getLastName());
	        accountRepository.save(account);
	        
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
		}
	}
	
	@GetMapping
    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getAccountById(@PathVariable Long id, HttpServletRequest httpRequest) {
    	try {
            Account account = accountRepository.findById(id)
                .orElseThrow(() -> new AccountNotFoundException("Account with ID " + id + " not found"));
            return ResponseEntity.ok(account);
    	} catch (AccountNotFoundException ex) {
    		ApiResponse apiResponse = new ApiResponse(
    	            HttpStatus.NOT_FOUND.value(),
    	            HttpStatus.NOT_FOUND.getReasonPhrase(),
    	            ex.getMessage(),
    	            httpRequest.getRequestURI());
    	    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiResponse);
    	}
    }
}
