package com.example.bankTransfers.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.bankTransfers.model.Transfer;

public interface TransferRepository extends JpaRepository<Transfer, Long> {}


