package com.merchpilot.merchpilot.exception;

import com.merchpilot.merchpilot.common.web.ResponseTransaction;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseTransaction handleNotFound(NotFoundException ex) {
        return ResponseTransaction.buildFailure(404, ex.getMessage(), null);
    }

    @ExceptionHandler(BusinessException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseTransaction handleBusiness(BusinessException ex) {
        return ResponseTransaction.buildFailure(400, ex.getMessage(), null);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseTransaction handleValidation(MethodArgumentNotValidException ex) {
        String msg = ex.getBindingResult().getFieldErrors().stream()
                .map(fe -> fe.getField() + " " + fe.getDefaultMessage())
                .findFirst().orElse("Validation error");
        return ResponseTransaction.buildFailure(400, msg, null);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseTransaction handleConstraint(ConstraintViolationException ex) {
        return ResponseTransaction.buildFailure(400, ex.getMessage(), null);
    }

//    @ExceptionHandler(Exception.class)
//    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
//    public ResponseTransaction handleOther(Exception ex) {
//        return ResponseTransaction.buildFailure(500, "Internal error", ex.getMessage());
//    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseTransaction> handleAll(Exception ex) {
        log.error("Unhandled error", ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ResponseTransaction.buildFailure(500, ex.getMessage(), null));
    }

}
