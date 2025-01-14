package com.example.bankTransfers.controller;

import java.time.LocalDate;
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

import com.example.bankTransfers.dto.AccountRequest;
import com.example.bankTransfers.exception.AccountNotFoundException;
import com.example.bankTransfers.exception.ApiResponse;
import com.example.bankTransfers.exception.ErrorResponse;
import com.example.bankTransfers.model.Account;
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
	public ResponseEntity<?> createAccount(@RequestBody AccountRequest request, HttpServletRequest httpRequest) {
		try {
	        
	        LocalDate today = LocalDate.now();
	        Account account = new Account(request.getBalance(), request.getFirstName(), request.getLastName(), today, true);
	        accountRepository.save(account);
	        
	        ApiResponse apiResponse = new ApiResponse(
		            HttpStatus.OK.value(),
		            httpRequest.getRequestURI());
	        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
		} catch (Exception ex) {
			ErrorResponse errorResponse = new ErrorResponse(
	            HttpStatus.INTERNAL_SERVER_ERROR.value(),
	            HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
	            ex.getMessage(),
	            httpRequest.getRequestURI()
	        );
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
	    }
	}
	
	@PutMapping("/{id}")
    public ResponseEntity<?> updateAccount(@PathVariable Long id, @RequestBody AccountRequest request, HttpServletRequest httpRequest) {
		
		try {
			Account account = accountRepository.findById(id).orElseThrow(() -> new AccountNotFoundException("Account with ID " + id + " not found"));
			account.setActive(request.getIsActive() != null ? request.isActive() : account.isActive());
	        account.setBalance(request.getBalance() != null ? request.getBalance() : account.getBalance());
	        account.setFirstName(request.getFirstName() != null ? request.getFirstName() : account.getFirstName());
	        account.setLastName(request.getLastName() != null ? request.getLastName() : account.getLastName());
	        accountRepository.save(account);
			ApiResponse apiResponse = new ApiResponse(
		            HttpStatus.OK.value(),
		            httpRequest.getRequestURI());
		    return ResponseEntity.status(HttpStatus.OK).body(apiResponse); 
		} catch (AccountNotFoundException ex) {
			ErrorResponse errorResponse = new ErrorResponse(
		            HttpStatus.NOT_FOUND.value(),
		            HttpStatus.NOT_FOUND.getReasonPhrase(),
		            ex.getMessage(),
		            httpRequest.getRequestURI());
		    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
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
    		ErrorResponse errorResponse = new ErrorResponse(
    	            HttpStatus.NOT_FOUND.value(),
    	            HttpStatus.NOT_FOUND.getReasonPhrase(),
    	            ex.getMessage(),
    	            httpRequest.getRequestURI());
    	    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    	}
    }
    
    //TODO Delete Account
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAccount(@PathVariable Long id, HttpServletRequest httpRequest) {
		try {
			accountRepository.deleteById(id);
			return ResponseEntity.ok(null);
		} catch (AccountNotFoundException ex) {
			ErrorResponse errorResponse = new ErrorResponse(
    	            HttpStatus.NOT_FOUND.value(),
    	            HttpStatus.NOT_FOUND.getReasonPhrase(),
    	            ex.getMessage(),
    	            httpRequest.getRequestURI());
    	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
		}
    }
   
}
