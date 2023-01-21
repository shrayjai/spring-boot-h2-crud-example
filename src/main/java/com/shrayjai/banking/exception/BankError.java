package com.shrayjai.banking.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BankError {

    private String ref;

    private String message;
}
