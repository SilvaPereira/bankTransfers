package com.example.bankTransfers.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.bankTransfers.model.Account;

public interface AccountRepository extends JpaRepository<Account, Long> {}

