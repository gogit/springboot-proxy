package com.thinktag.persist.service;

import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpContent;
import com.google.api.client.http.HttpHeaders;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.JsonObjectParser;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.thinktag.persist.controller.HttpUtils;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Service
public class ProxyService {

    HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
    JsonFactory JSON_FACTORY = new JacksonFactory();

    public void processGet(String forwardingUrl, HttpServletRequest servletRequest,
                           HttpServletResponse servletResponse) throws IOException {
        HttpRequestFactory requestFactory
                = HTTP_TRANSPORT.createRequestFactory(
                (HttpRequest request) -> {
                    request.setParser(new JsonObjectParser(JSON_FACTORY));
                });
        HttpHeaders headers = HttpUtils.getHeaders(servletRequest);
        String requestUrl = String.join("", forwardingUrl, getFullUrl(servletRequest));
        HttpRequest request = requestFactory.buildGetRequest(new GenericUrl(requestUrl));
        request.setHeaders(headers);
        HttpResponse response = request.execute();
        response.getContent();
        servletResponse.getOutputStream().write(HttpUtils.getByteBuffer(response).toByteArray());
    }

    public void processPost(String forwardingUrl, HttpServletRequest servletRequest,
                           HttpServletResponse servletResponse) throws IOException {
        HttpRequestFactory requestFactory
                = HTTP_TRANSPORT.createRequestFactory(
                (HttpRequest request) -> {
                    request.setParser(new JsonObjectParser(JSON_FACTORY));
                });
        HttpHeaders headers = HttpUtils.getHeaders(servletRequest);
        HttpContent content = HttpUtils.getContent(servletRequest);

        String requestUrl = String.join("", forwardingUrl, getFullUrl(servletRequest));
        HttpRequest request = requestFactory.buildPostRequest(new GenericUrl(requestUrl), content);
        request.setHeaders(headers);
        HttpResponse response = request.execute();
        response.getContent();
        servletResponse.getOutputStream().write(HttpUtils.getByteBuffer(response).toByteArray());
    }

    public static String getFullUrl(HttpServletRequest request) {
        StringBuilder requestURL = new StringBuilder(request.getRequestURI());
        String queryString = request.getQueryString();
        if (queryString == null) {
            return requestURL.toString();
        } else {
            return requestURL.append('?').append(queryString).toString();
        }
    }
}
