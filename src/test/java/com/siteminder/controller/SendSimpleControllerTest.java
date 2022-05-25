package com.siteminder.controller;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.siteminder.model.MessageRequest;
import com.siteminder.service.EmailRequestFactory;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ContextConfiguration(classes = {SendSimpleController.class})
@ExtendWith(SpringExtension.class)
class SendSimpleControllerTest {
    @MockBean
    private EmailRequestFactory emailRequestFactory;

    @Autowired
    private SendSimpleController sendSimpleController;

    /**
     * Method under test: {@link SendSimpleController#sendMail(MessageRequest)}
     */
    @Test
    void testSendMail() throws Exception {
        when(this.emailRequestFactory.sendMessage((MessageRequest) any()))
                .thenReturn(new ResponseEntity<>(HttpStatus.CONTINUE));

        MessageRequest messageRequest = new MessageRequest();
        messageRequest.setBcc(new ArrayList<>());
        messageRequest.setCc(new ArrayList<>());
        messageRequest.setSubject("Hello from the Dreaming Spires");
        messageRequest.setText("Text");
        messageRequest.setTo(new ArrayList<>());
        String content = (new ObjectMapper()).writeValueAsString(messageRequest);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/v1/api/send")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(this.sendSimpleController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().is(100));
    }
}

