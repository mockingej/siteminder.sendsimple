package com.siteminder.controller;

import com.siteminder.exception.EmailServiceException;
import com.siteminder.model.MessageRequest;
import com.siteminder.model.MessageResponse;
import com.siteminder.service.EmailRequestFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.io.IOException;
import java.net.URISyntaxException;

@RestController
@RequestMapping(value = "/v1/api/")
public class SendSimpleController {

    private final EmailRequestFactory sendSimpleService;

    public SendSimpleController(EmailRequestFactory sendSimpleService) {
        this.sendSimpleService = sendSimpleService;
    }

    @PostMapping("/send")
    public ResponseEntity<MessageResponse> sendMail(@Valid @RequestBody MessageRequest messageRequest) throws URISyntaxException, IOException, InterruptedException, EmailServiceException {
        return sendSimpleService.sendMessage(messageRequest);
    }
}
