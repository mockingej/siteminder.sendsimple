package com.siteminder.service.email;

import com.siteminder.model.MessageRequest;
import com.siteminder.model.MessageResponse;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public interface EmailService {
    ResponseEntity<MessageResponse> sendEmail(MessageRequest messageRequest) throws URISyntaxException, IOException, InterruptedException;
    URI buildURI(MessageRequest messageRequest) throws URISyntaxException;
}
