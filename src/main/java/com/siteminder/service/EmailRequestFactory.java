package com.siteminder.service;

import com.siteminder.constant.EmailProviderEnum;
import com.siteminder.exception.EmailServiceException;
import com.siteminder.model.MessageRequest;
import com.siteminder.model.MessageResponse;
import com.siteminder.service.email.EmailService;
import com.siteminder.util.EmailUtil;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.net.ConnectException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Log4j2
public class EmailRequestFactory {

    private final BeanFactory beanFactory;

    public EmailRequestFactory(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    public ResponseEntity<MessageResponse> sendMessage(MessageRequest messageRequest) throws URISyntaxException, InterruptedException, EmailServiceException {
        EmailService emailService;
        emailValidation(messageRequest);

        for (EmailProviderEnum emailProvider: EmailProviderEnum.values()) {
            try {
                emailService = beanFactory.getBean(emailProvider.toString(), EmailService.class);
                return emailService.sendEmail(messageRequest);
            } catch (ConnectException exception) {
                log.info(emailProvider + " Email Service is currently unavailable due to incorrect host/port");
                log.info("Connecting to a different email service");
            } catch (IOException exception) {
                log.info(emailProvider + " Email Service is currently unavailable due to: { " + exception.getMessage() +" }");
                log.info("Connecting to a different email service");
            }
        }
        log.info("All Email Services are currently unavailable");
        throw new EmailServiceException("All Email Services are currently unavailable");
    }

    private void emailValidation(MessageRequest messageRequest) {
        if (CollectionUtils.isEmpty(messageRequest.getTo())) throw new IllegalArgumentException("Must have 1 or more recipients");
        messageRequest.setTo(getValidEmails(messageRequest.getTo()));
        if (CollectionUtils.isEmpty(messageRequest.getTo())) throw new IllegalArgumentException("All emails are invalid");

        messageRequest.setCc(getValidEmails(messageRequest.getCc()));
        messageRequest.setBcc(getValidEmails(messageRequest.getBcc()));
        if (StringUtils.isEmpty(messageRequest.getSubject())) throw new IllegalArgumentException("Subject is required");
    }

    private List<String> getValidEmails(List<String> emails) {
        if (CollectionUtils.isEmpty(emails)) return new ArrayList<>();
        return emails.stream().filter(EmailUtil::patternMatches).collect(Collectors.toList());
    }
}
