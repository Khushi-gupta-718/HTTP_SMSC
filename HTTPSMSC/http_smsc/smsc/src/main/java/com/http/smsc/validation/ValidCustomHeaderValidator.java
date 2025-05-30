package com.http.smsc.validation;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.http.smsc.dto.HttpSmscRequest;
import com.http.smsc.dto.HttpSmscRequest.ProviderType;
import com.jayway.jsonpath.InvalidPathException;
import com.jayway.jsonpath.JsonPath;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;


public class ValidCustomHeaderValidator implements ConstraintValidator<ValidCustomHeader, HttpSmscRequest> {

    private final ObjectMapper objectMapper = new ObjectMapper();
    @Override
    public boolean isValid(HttpSmscRequest request, ConstraintValidatorContext context) {
        if (request == null) return true;  

        ProviderType providerType = request.getProviderType();
        String customHeaders = request.getCustomHeaders();

        if (providerType == null) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(" providerType not acceptable")
                   .addPropertyNode("providerType")
                   .addConstraintViolation();
            return false;
        }
        JsonNode headersNode;
        try {
            headersNode = objectMapper.readTree(customHeaders);
        } catch (Exception e) {
            context.buildConstraintViolationWithTemplate("customHeaders must be a valid JSON object")
                    .addPropertyNode("customHeaders")
                    .addConstraintViolation();
            return false;
        }

        JsonNode contentTypeNode = headersNode.get("Content-Type");
        if (contentTypeNode == null || contentTypeNode.asText().isBlank()) {
            context.buildConstraintViolationWithTemplate("customHeaders must contain a valid 'Content-Type'")
                    .addPropertyNode("customHeaders")
                    .addConstraintViolation();
            return false;
        }

        String contentType = contentTypeNode.asText().trim().toLowerCase();

        switch (providerType) {
            case JSON:
            	
                if (!contentType.equals("application/json")) {
                    context.buildConstraintViolationWithTemplate("For JSON, Content-Type must be 'application/json'")
                            .addPropertyNode("customHeaders")
                            .addConstraintViolation();
                    return false;
                }
                if (request.getResponseExtractExpression() != null &&
                        !isValidJsonPath(request.getResponseExtractExpression())) {
                    context.buildConstraintViolationWithTemplate("For JSON, responseExtractExpression must be a valid JSONPath")
                            .addPropertyNode("responseExtractExpression")
                            .addConstraintViolation();
                    return false;
                }
                break;

            case XML:
                if (!contentType.equals("text/xml")) {
                    context.buildConstraintViolationWithTemplate("For XML, Content-Type must be 'text/xml'")
                            .addPropertyNode("customHeaders")
                            .addConstraintViolation();
                    return false;
                }
                if (request.getResponseExtractExpression() != null &&
                        !isValidXPath(request.getResponseExtractExpression())) {
                    context.buildConstraintViolationWithTemplate("For XML, responseExtractExpression must be a valid XPath")
                            .addPropertyNode("responseExtractExpression")
                            .addConstraintViolation();
                    return false;
                }
                break;

            case TEXT:
                break;

            default:
                context.buildConstraintViolationWithTemplate("Unsupported providerType: " + providerType)
                        .addPropertyNode("providerType")
                        .addConstraintViolation();
                return false;
        }

        return true;
    }
    private boolean isValidJsonPath(String jsonPath) {
        try {
            JsonPath.compile(jsonPath);
            return true;
        } catch (InvalidPathException e) {
            return false;
        }
    }

    private boolean isValidXPath(String xpathExpr) {
        try {
            XPath xPath = XPathFactory.newInstance().newXPath();
            xPath.compile(xpathExpr);
            return true;
        } catch (XPathExpressionException e) {
            return false;
        }
    }
}
