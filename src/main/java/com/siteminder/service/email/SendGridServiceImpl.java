package com.siteminder.service.email;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.siteminder.constant.EmailProviderConstant;
import com.siteminder.model.MessageRequest;
import com.siteminder.model.MessageResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

@Service(EmailProviderConstant.SENDGRID)
public class SendGridServiceImpl implements EmailService {

    @Value("${sendgrid.api}")
    private String api;

    @Value("${service-name}")
    private String from;

    @Value("${sendgrid.apikey}")
    private String apikey;

    private final HttpClient sendGridClient;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public SendGridServiceImpl(HttpClient sendGridClient) {
        this.sendGridClient = sendGridClient;
    }

    @Override
    public ResponseEntity<MessageResponse> sendEmail(MessageRequest messageRequest) throws URISyntaxException, IOException, InterruptedException {
        URI uri = buildURI(messageRequest);
        HttpRequest request  = HttpRequest
                .newBuilder(uri)
                .header("Authorization", apikey)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.noBody())
                .build();

        HttpResponse<String> clientResponse = sendGridClient.send(request, HttpResponse.BodyHandlers.ofString());
        String body = clientResponse.body();
        MessageResponse response = objectMapper.readValue(body, MessageResponse.class);
        return ResponseEntity.ok().body(response);
    }

    @Override
    public URI buildURI(MessageRequest messageRequest) throws URISyntaxException {
        List<NameValuePair> params = new ArrayList<>();
        BasicNameValuePair subjectParams = new BasicNameValuePair("subject", messageRequest.getSubject());
        BasicNameValuePair fromParams = new BasicNameValuePair("from", from);
        BasicNameValuePair textParams = new BasicNameValuePair("text", messageRequest.getText());

        params.add(fromParams);
        params.add(subjectParams);
        params.add(textParams);

        BasicNameValuePair toParams;
        if (messageRequest.getCc().size() == 1)
            toParams = new BasicNameValuePair("to", messageRequest.getCc().get(0));
        else
            toParams = new BasicNameValuePair("to[]", String.join(", ", messageRequest.getTo()));
        params.add(toParams);

        BasicNameValuePair ccParams;
        if (messageRequest.getCc().size() == 1)
            ccParams = new BasicNameValuePair("cc", messageRequest.getCc().get(0));
        else
            ccParams = new BasicNameValuePair("cc[]", String.join(", ", messageRequest.getCc()));
        params.add(ccParams);

        BasicNameValuePair bccParams;
        if (messageRequest.getCc().size() == 1)
            bccParams = new BasicNameValuePair("bcc", messageRequest.getCc().get(0));
        else
            bccParams = new BasicNameValuePair("bcc[]", String.join(", ", messageRequest.getCc()));
        params.add(bccParams);

        return new URIBuilder(api).setParameters(params).build();
    }
}
