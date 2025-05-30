package com.http.smsc.validation;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.http.smsc.dto.HttpSmscRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathFactory;
import java.io.StringReader;
import java.util.Arrays;
import java.util.List;

public class RequestBodyTemplateValidator implements ConstraintValidator<ValidRequestBodyTemplate, HttpSmscRequest> {

    private static final List<String> REQUIRED_FIELDS = Arrays.asList("text", "destination", "dlrwebhookurl");
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public boolean isValid(HttpSmscRequest request, ConstraintValidatorContext context) {
        if (request == null) return true;

        String template = request.getRequestBodyTemplate();
        HttpSmscRequest.ProviderType providerType = request.getProviderType();

        if (template == null || template.isBlank() || providerType == null) return true;

        context.disableDefaultConstraintViolation();

        try {
            if (providerType == HttpSmscRequest.ProviderType.JSON) {
                if (!isValidJson(template)) {
                    addViolation(context, "requestBodyTemplate must be valid JSON.");
                    return false;
                }

                if (!containsStrictJsonPlaceholders(template)) {
                    addViolation(context, "JSON must strictly contain placeholders like \"text\": \"${text}\" for all required fields: " + REQUIRED_FIELDS);
                    return false;
                }

            } else if (providerType == HttpSmscRequest.ProviderType.XML) {
                if (!isValidXml(template)) {
                    addViolation(context, "requestBodyTemplate must be valid XML.");
                    return false;
                }

                if (!containsStrictXmlPlaceholders(template)) {
                    addViolation(context, "XML must strictly contain placeholders like <text>${text}</text> for all required fields: " + REQUIRED_FIELDS);
                    return false;
                }
            }
        } catch (Exception e) {
            addViolation(context, "Validation failed: " + e.getMessage());
            return false;
        }

        return true;
    }

    private void addViolation(ConstraintValidatorContext context, String message) {
        context.buildConstraintViolationWithTemplate(message)
                .addPropertyNode("requestBodyTemplate")
                .addConstraintViolation();
    }

    private boolean isValidJson(String json) {
        try {
            objectMapper.readTree(json);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private boolean containsStrictJsonPlaceholders(String json) {
        try {
            JsonNode root = objectMapper.readTree(json);
            for (String field : REQUIRED_FIELDS) {
                JsonNode node = root.get(field);
                if (node == null || !node.asText().equals("${" + field + "}")) {
                    return false;
                }
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private boolean isValidXml(String xml) {
        try {
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            builder.parse(new InputSource(new StringReader(xml)));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private boolean containsStrictXmlPlaceholders(String xml) {
        try {
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document doc = builder.parse(new InputSource(new StringReader(xml)));
            XPath xpath = XPathFactory.newInstance().newXPath();

            for (String field : REQUIRED_FIELDS) {
                String expression = String.format("//*[local-name()='%s']", field);
                String value = xpath.evaluate(expression, doc).trim();

                if (!value.equals("${" + field + "}")) {
                    return false;
                }
            }

            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
