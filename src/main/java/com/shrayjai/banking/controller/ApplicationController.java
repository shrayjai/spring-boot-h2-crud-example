package com.shrayjai.banking.controller;

import com.shrayjai.banking.exception.AccountNotFoundException;
import com.shrayjai.banking.exception.InsufficientBalanceException;
import com.shrayjai.banking.exception.InvalidRequestException;
import com.shrayjai.banking.model.*;
import com.shrayjai.banking.service.IBankService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1")
public class ApplicationController {

    private static final Logger LOG = LoggerFactory.getLogger(ApplicationController.class.getName());

    @Autowired
    private IBankService bankService;

    @GetMapping("/balance")
    public ResponseEntity<BalanceDto> balance(@RequestHeader String accountNumber)
        throws AccountNotFoundException {

        LOG.debug("Requested /balance >>> {}", accountNumber);

        return ResponseEntity.ok().body(bankService.balance(accountNumber));
    }

    @PostMapping("/deposit")
    public ResponseEntity<TransactionResponse> deposit(@Valid @RequestBody TransactionRequest body)
        throws AccountNotFoundException {

        LOG.debug("Requested /deposit >>> {}", body);

        return ResponseEntity.status(HttpStatus.CREATED).body(bankService.deposit(body));
    }

    @PostMapping("/withdraw")
    public ResponseEntity<TransactionResponse> withdraw(@Valid @RequestBody TransactionRequest body)
        throws AccountNotFoundException, InsufficientBalanceException {

        LOG.debug("Requested /withdraw >>> {}", body);

        return ResponseEntity.status(HttpStatus.CREATED).body(bankService.withdraw(body));
    }

    @PostMapping("/transfer")
    public ResponseEntity<TransferResponse> transfer(@Valid @RequestBody TransferRequest body)
        throws AccountNotFoundException, InvalidRequestException, InsufficientBalanceException {

        LOG.debug("Requested /transfer >>> {}", body);

        return ResponseEntity.status(HttpStatus.CREATED).body(bankService.transfer(body));
    }
}
