package com.siteminder.config;

import com.siteminder.constant.EmailProviderConstant;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.net.http.HttpClient;

@Configuration
public class HttpClientConfig {

    @Value("${mailgun.user}")
    private String user;

    @Value("${mailgun.apikey}")
    private String apikey;

    @Bean
    @Qualifier(EmailProviderConstant.MAILGUN)
    public HttpClient mailGunClient() {
        return HttpClient.newBuilder().authenticator(new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(user, apikey.toCharArray());
            }
        }).build();
    }


    @Bean
    @Qualifier(EmailProviderConstant.SENDGRID)
    public HttpClient sendGridClient() {
        return HttpClient.newHttpClient();
    }
}
