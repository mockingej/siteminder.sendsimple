package com.siteminder.service.email;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.siteminder.constant.EmailProviderConstant;
import com.siteminder.model.MessageRequest;
import com.siteminder.model.MessageResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

@Service(EmailProviderConstant.MAILGUN)
public class MailGunServiceImpl implements EmailService {

    @Value("${mailgun.api}")
    private String api;

    @Value("${service-name}")
    private String from;

    @Qualifier(EmailProviderConstant.MAILGUN)
    private final HttpClient mailGunClient;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public MailGunServiceImpl(HttpClient mailGunClient) {
        this.mailGunClient = mailGunClient;
    }

    @Override
    public ResponseEntity<MessageResponse> sendEmail(MessageRequest messageRequest) throws URISyntaxException, IOException, InterruptedException {
        URI uri = buildURI(messageRequest);
        HttpRequest request = HttpRequest
                .newBuilder(uri)
                .POST(HttpRequest.BodyPublishers.noBody())
                .build();

        HttpResponse<String> clientResponse = mailGunClient.send(request, HttpResponse.BodyHandlers.ofString());
        String body = clientResponse.body();
        MessageResponse response = objectMapper.readValue(body, MessageResponse.class);
        return ResponseEntity.ok().body(response);
    }

    @Override
    public URI buildURI(MessageRequest messageRequest) throws URISyntaxException {
        List<NameValuePair> params = new ArrayList<>();

        BasicNameValuePair toParams = new BasicNameValuePair("to", String.join(", ", messageRequest.getTo()));
        BasicNameValuePair fromParams = new BasicNameValuePair("from", from);
        BasicNameValuePair subjectParams = new BasicNameValuePair("subject", messageRequest.getSubject());
        BasicNameValuePair textParams = new BasicNameValuePair("text", messageRequest.getText());

        params.add(toParams);
        params.add(fromParams);
        params.add(subjectParams);
        params.add(textParams);

        if (!CollectionUtils.isEmpty(messageRequest.getCc())) {
            BasicNameValuePair ccParams = new BasicNameValuePair("cc", String.join(", ", messageRequest.getCc()));
            params.add(ccParams);
        }

        if (!CollectionUtils.isEmpty(messageRequest.getBcc())) {
            BasicNameValuePair bccParams = new BasicNameValuePair("bcc", String.join(", ", messageRequest.getBcc()));
            params.add(bccParams);
        }

        return new URIBuilder(api).setParameters(params).build();
    }
}
