package com.siteminder.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MessageResponse {
    private String id;
    private String message;
}
