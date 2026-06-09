package com.enviro.assessment.junior.nhletelo.exception;

/**
 * Thrown when a withdrawal request violates business rules
 * (e.g. exceeds 90% of balance, retirement age restriction).
 */
public class WithdrawalValidationException extends RuntimeException {
    public WithdrawalValidationException(String message) {
        super(message);
    }
}
