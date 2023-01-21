package com.shrayjai.banking.service;

import com.shrayjai.banking.exception.AccountNotFoundException;
import com.shrayjai.banking.exception.InsufficientBalanceException;
import com.shrayjai.banking.exception.InvalidRequestException;
import com.shrayjai.banking.model.*;
import com.shrayjai.banking.persistence.Accounts;
import com.shrayjai.banking.persistence.AccountsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class BankService implements IBankService {

    @Autowired
    private AccountsRepository accountsRepository;

    private static final String ACCOUNT_NOT_FOUND = "account not found";

    private static final String SAME_ACCOUNT = "benefactor and beneficiary must be different";

    private static final String INSUFFICIENT_BALANCE = "insufficient balance";

    private static final String SUCCESS = "success";

    @Override
    public BalanceDto balance(String accountNumber) throws AccountNotFoundException {

        Accounts account = accountsRepository.findById(accountNumber).orElseThrow(
            () -> new AccountNotFoundException(ACCOUNT_NOT_FOUND)
        );

        return BalanceDto.builder()
            .accountNumber(account.getAccountNumber())
            .balance(account.getBalance())
            .build();
    }

    @Override
    public TransactionResponse deposit(TransactionRequest request) throws AccountNotFoundException {

        Accounts account = accountsRepository.findById(request.getAccountNumber()).orElseThrow(
            () -> new AccountNotFoundException(ACCOUNT_NOT_FOUND)
        );

        BigDecimal previousBalance = account.getBalance();
        account.setBalance(previousBalance.add(request.getAmount()));

        accountsRepository.save(account);

        return TransactionResponse.builder()
            .accountNumber(account.getAccountNumber())
            .previousBalance(previousBalance)
            .currentBalance(account.getBalance())
            .build();
    }

    @Override
    public TransactionResponse withdraw(TransactionRequest request) throws AccountNotFoundException, InsufficientBalanceException {

        Accounts account = accountsRepository.findById(request.getAccountNumber()).orElseThrow(
            () -> new AccountNotFoundException(ACCOUNT_NOT_FOUND)
        );

        BigDecimal previousBalance = account.getBalance();
        BigDecimal currentBalance = previousBalance.subtract(request.getAmount());
        if (currentBalance.compareTo(BigDecimal.ZERO) < 0)
            throw new InsufficientBalanceException(INSUFFICIENT_BALANCE);
        account.setBalance(currentBalance);

        accountsRepository.save(account);

        return TransactionResponse.builder()
            .accountNumber(account.getAccountNumber())
            .previousBalance(previousBalance)
            .currentBalance(account.getBalance())
            .build();
    }

    @Override
    public TransferResponse transfer(TransferRequest request) throws AccountNotFoundException, InvalidRequestException, InsufficientBalanceException {

        Accounts benefactorAccount = accountsRepository.findById(request.getBenefactor()).orElseThrow(
            () -> new AccountNotFoundException(ACCOUNT_NOT_FOUND)
        );
        Accounts beneficiaryAccount = accountsRepository.findById(request.getBeneficiary()).orElseThrow(
            () -> new AccountNotFoundException(ACCOUNT_NOT_FOUND)
        );

        if (benefactorAccount.getAccountNumber().equals(beneficiaryAccount.getAccountNumber()))
            throw new InvalidRequestException(SAME_ACCOUNT);

        BigDecimal benefactorCurrentBalance = benefactorAccount.getBalance().subtract(request.getAmount());
        if (benefactorCurrentBalance.compareTo(BigDecimal.ZERO) < 0)
            throw new InsufficientBalanceException(INSUFFICIENT_BALANCE);
        benefactorAccount.setBalance(benefactorCurrentBalance);
        beneficiaryAccount.setBalance(beneficiaryAccount.getBalance().add(request.getAmount()));

        accountsRepository.save(benefactorAccount);
        accountsRepository.save(beneficiaryAccount);

        return TransferResponse.builder()
            .beneficiary(request.getBeneficiary())
            .amount(request.getAmount())
            .status(SUCCESS)
            .build();
    }
}
