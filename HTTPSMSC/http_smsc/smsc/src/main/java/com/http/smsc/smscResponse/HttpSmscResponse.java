package com.http.smsc.smscResponse;

import com.http.smsc.dto.HttpSmscRequest.ProviderType;

public class HttpSmscResponse {

    private String systemId;
    private String serverUrl;
    private String requestBodyTemplate;
    private String responseExtractExpression;
    private String dlrExtractExpression;
    private ProviderType providerType;
    private String customHeaders;

    public HttpSmscResponse() {}

    public HttpSmscResponse(String systemId, String serverUrl, String requestBodyTemplate,
                            String responseExtractExpression, String dlrExtractExpression,
                            ProviderType providerType, String customHeaders) {
        this.systemId = systemId;
        this.serverUrl = serverUrl;
        this.requestBodyTemplate = requestBodyTemplate;
        this.responseExtractExpression = responseExtractExpression;
        this.dlrExtractExpression = dlrExtractExpression;
        this.providerType = providerType;
        this.customHeaders = customHeaders;
    }

    public String getSystemId() {
        return systemId;
    }

    public String getServerUrl() {
        return serverUrl;
    }

    public String getRequestBodyTemplate() {
        return requestBodyTemplate;
    }

    public String getResponseExtractExpression() {
        return responseExtractExpression;
    }

    public String getDlrExtractExpression() {
        return dlrExtractExpression;
    }

    public ProviderType getProviderType() {
        return providerType;
    }

    public String getCustomHeaders() {
        return customHeaders;
    }

    public void setSystemId(String systemId) {
        this.systemId = systemId;
    }

    public void setServerUrl(String serverUrl) {
        this.serverUrl = serverUrl;
    }

    public void setRequestBodyTemplate(String requestBodyTemplate) {
        this.requestBodyTemplate = requestBodyTemplate;
    }

    public void setResponseExtractExpression(String responseExtractExpression) {
        this.responseExtractExpression = responseExtractExpression;
    }

    public void setDlrExtractExpression(String dlrExtractExpression) {
        this.dlrExtractExpression = dlrExtractExpression;
    }

    public void setProviderType(ProviderType providerType) {
        this.providerType = providerType;
    }

    public void setCustomHeaders(String customHeaders) {
        this.customHeaders = customHeaders;
    }
}
