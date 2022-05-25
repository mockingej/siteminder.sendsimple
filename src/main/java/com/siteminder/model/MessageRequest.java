package com.siteminder.model;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@Data
public class MessageRequest {

    @NotEmpty(message = "To Field is mandatory. Please input one or more recipient")
    private List<String> to;
    private List<String> cc;
    private List<String> bcc;
    @NotBlank(message = "Subject Field is mandatory")
    private String subject;
    private String text;
}
