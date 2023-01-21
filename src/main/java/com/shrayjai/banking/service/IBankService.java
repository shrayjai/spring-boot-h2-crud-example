package com.shrayjai.banking.service;

import com.shrayjai.banking.exception.AccountNotFoundException;
import com.shrayjai.banking.exception.InsufficientBalanceException;
import com.shrayjai.banking.exception.InvalidRequestException;
import com.shrayjai.banking.model.*;

public interface IBankService {

    BalanceDto balance(String accountNumber) throws AccountNotFoundException;

    TransactionResponse deposit(TransactionRequest request) throws AccountNotFoundException;

    TransactionResponse withdraw(TransactionRequest request) throws AccountNotFoundException, InsufficientBalanceException;

    TransferResponse transfer(TransferRequest request) throws AccountNotFoundException, InvalidRequestException, InsufficientBalanceException;
}
