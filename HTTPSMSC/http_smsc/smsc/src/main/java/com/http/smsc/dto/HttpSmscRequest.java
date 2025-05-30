package com.http.smsc.dto;

import com.http.smsc.validation.ValidCustomHeader;
import com.http.smsc.validation.ValidRequestBodyTemplate;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.http.smsc.exception.BadRequestException;
import com.http.smsc.validation.DrlExtractExpression;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@ValidRequestBodyTemplate
@ValidCustomHeader
@DrlExtractExpression
public class HttpSmscRequest {

    @NotBlank(message = "System ID must not be blank")
    @Size(max = 50, message = "System ID must not exceed 50 characters")
    @Column(name = "system_id", nullable = false, length = 50, unique = true)
    private String systemId;
    
    
    @NotBlank(message = "Password must not be blank")
    @Size(max = 100, message = "Password must not exceed 100 characters")
    @Column(name = "password", length = 100)
    private String password;

    @NotBlank(message = "Server URL must not be blank")
    @Size(max = 255, message = "Server URL must not exceed 255 characters")
    @Column(name = "server_url", nullable = false, length = 255)
    private String serverUrl;

    @NotBlank(message = "Request body template must not be blank")
    @Column(name = "request_body_template", nullable = false, columnDefinition = "TEXT")
    private String requestBodyTemplate;

    @NotBlank(message = "Response extract expression  must not be blank")
    @Column(name = "response_extract_expression", columnDefinition = "TEXT")
    private String responseExtractExpression;

    @NotBlank(message = "DRL extract expression must not be blank")
    @Column(name = "dlr_extract_expression", columnDefinition = "TEXT")
    private String dlrExtractExpression;

    @NotNull(message = "providerType is required")    
    @Column(name = "provider_type", nullable = false, length = 10)
    private ProviderType providerType;

    @NotBlank(message = "Custom headers must not be blank")
    @Column(name = "custom_headers", nullable = false, columnDefinition = "TEXT")
    private String customHeaders;

    public enum ProviderType {
        JSON, XML , TEXT;
    	
    	@JsonCreator
    	public static ProviderType fromString(String value) {
    	    if (value == null || value.trim().isEmpty()) {
    	        throw new BadRequestException("providerType must not be empty");
    	    }
    	    try {
    	        return ProviderType.valueOf(value.trim().toUpperCase());
    	    } catch (IllegalArgumentException e) {
    	        throw new BadRequestException("Unsupported providerType: " + value);
    	    }
    	}
    }


    public String getSystemId() {
        return systemId;
    }

    public void setSystemId(String systemId) {
        this.systemId = systemId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getServerUrl() {
        return serverUrl;
    }

    public void setServerUrl(String serverUrl) {
        this.serverUrl = serverUrl;
    }

    public String getRequestBodyTemplate() {
        return requestBodyTemplate;
    }

    public void setRequestBodyTemplate(String requestBodyTemplate) {
        this.requestBodyTemplate = requestBodyTemplate;
    }

    public String getResponseExtractExpression() {
        return responseExtractExpression;
    }

    public void setResponseExtractExpression(String responseExtractExpression) {
        this.responseExtractExpression = responseExtractExpression;
    }

    public String getDlrExtractExpression() {
        return dlrExtractExpression;
    }

    public void setDlrExtractExpression(String dlrExtractExpression) {
        this.dlrExtractExpression = dlrExtractExpression;
    }

    public ProviderType getProviderType() {
        return providerType;
    }

    public void setProviderType(ProviderType providerType) {
        this.providerType = providerType;
    }

    public String getCustomHeaders() {
        return customHeaders;
    }

    public void setCustomHeaders(String customHeaders) {
        this.customHeaders = customHeaders;
    }
}
