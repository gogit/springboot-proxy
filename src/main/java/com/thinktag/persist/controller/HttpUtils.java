package com.thinktag.persist.controller;

import com.google.api.client.http.HttpContent;
import com.google.api.client.http.HttpHeaders;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.InputStreamContent;
import org.apache.tomcat.util.http.fileupload.IOUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Enumeration;

public class HttpUtils {

    public static HttpHeaders getHeaders(HttpServletRequest servletRequest) {
        HttpHeaders headers = new HttpHeaders();
        @SuppressWarnings("unchecked")
        Enumeration<String> enumerationOfHeaderNames = servletRequest.getHeaderNames();
        while (enumerationOfHeaderNames.hasMoreElements()) {
            String headerName = enumerationOfHeaderNames.nextElement();
            Object value = servletRequest.getHeader(headerName);
            try {
                switch (headerName) {
                    case "user-agent":
                        headers.setUserAgent((String)value);
                        break;
                    case "content-type":
                        headers.setContentType((String)value);
                        break;
                    case "content-length":
                        headers.setContentLength(Long.parseLong((String)value));
                        break;
                    case "accept":
                        headers.setAccept((String)value);
                        break;
                    default:
                        headers.set(headerName, value);
                        break;
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return headers;
    }

    public static HttpContent getContent(HttpServletRequest request) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        IOUtils.copy(request.getInputStream(), bos);
        System.out.println(new String(bos.toByteArray()));
        HttpContent content = new InputStreamContent(request.getContentType(),
                new ByteArrayInputStream(bos.toByteArray()));
        return content;
    }

    public static ByteArrayOutputStream getByteBuffer(HttpResponse response) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        IOUtils.copy(response.getContent(), bos);
        return bos;
    }


}
