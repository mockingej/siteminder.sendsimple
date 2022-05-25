package com.siteminder.exception;

import com.siteminder.model.MessageResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.net.ConnectException;

@ControllerAdvice
public class ControllerAdvisor extends ResponseEntityExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<MessageResponse> handleIllegalArgumentException(IllegalArgumentException exception) {
        MessageResponse messageResponse = new MessageResponse();
        messageResponse.setMessage(exception.getMessage());
        return new ResponseEntity<>(messageResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EmailServiceException.class)
    public ResponseEntity<MessageResponse> handleEmailServiceException(EmailServiceException exception) {
        MessageResponse messageResponse = new MessageResponse();
        messageResponse.setMessage(exception.getMessage());
        return new ResponseEntity<>(messageResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(ConnectException.class)
    public ResponseEntity<MessageResponse> handleConnectException(ConnectException exception) {
        MessageResponse messageResponse = new MessageResponse();
        messageResponse.setMessage("Cannot connect to Email Service. Kindly try again some time");
        return new ResponseEntity<>(messageResponse, HttpStatus.BAD_REQUEST);
    }

}
