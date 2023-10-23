package com.example.diaryboard.global.logging;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

public class HttpLogMessage {

    private final String httpMethod;
    private final String requestUri;
    private final HttpStatus httpStatus;
    private final String clientIp;
    private final String headers;
    private final String requestParams;
    private final String requestBody;
    private final String responseBody;
    private final Long elapsedTime;

    public HttpLogMessage(ContentCachingRequestWrapper request, ContentCachingResponseWrapper response,
                          Long elapsedTime) {
        // Request 정보
        this.httpMethod = request.getMethod();
        this.requestUri = request.getRequestURI();
        this.clientIp = getClientIP(request);
        this.headers = getAllHeadersAsString(request);
        this.requestParams = getAllParametersAsString(request);
        this.requestBody = new String(request.getContentAsByteArray(), StandardCharsets.UTF_8);

        // Response 정보
        this.httpStatus = HttpStatus.valueOf(response.getStatus());
        this.responseBody = new String(response.getContentAsByteArray(), StandardCharsets.UTF_8);

        // 실행 시간
        this.elapsedTime = elapsedTime;
    }

    private String getClientIP(ContentCachingRequestWrapper request) {
        if (StringUtils.hasLength(request.getHeader("X-Forwarded-For"))) {
            return request.getHeader("X-Forwarded-For").split(",")[0]; // X-Forwarded-For: client, proxy1, proxy2
        } else if (StringUtils.hasLength(request.getHeader("X-Real-IP"))) {
            return request.getHeader("X-Real-IP"); // X-Real-IP: client
        } else {
            return request.getRemoteAddr(); // 가장 마지막에 요청한 IP
        }
    }

    private String getAllHeadersAsString(ContentCachingRequestWrapper request) {
        Map<String, String> headers = Collections.list(request.getHeaderNames()).stream()
                .collect(Collectors.toMap(
                        header -> header,
                        header -> String.join(", ", Collections.list(request.getHeaders(header)))
                ));

        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(headers);
        } catch (JsonProcessingException e) {
            return "JSON Parsing Error";
        }
    }

    private String getAllParametersAsString(ContentCachingRequestWrapper request) {
        Map<String, String> parameters = request.getParameterMap().entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        e -> String.join(", ", e.getValue())
                ));

        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(parameters);
        } catch (JsonProcessingException e) {
            return "JSON Parsing Error";
        }
    }

    public String toPrettierLog() {
        return "\n" +
                "[REQUEST] " + this.httpMethod + " " + this.requestUri + " " + this.httpStatus +
                " (" + this.elapsedTime + "ms" + ")" +
                "\n>> CLIENT_IP: " + this.clientIp +
                "\n>> HEADERS: " + this.headers +
                "\n>> REQUEST_PARAMS: " + this.requestParams +
                "\n>> REQUEST_BODY: " + this.requestBody +
                "\n>> RESPONSE_BODY: " + this.responseBody + "\n";
    }
}
