package com.http.smsc.validation;

import com.http.smsc.dto.HttpSmscRequest;
import com.http.smsc.dto.HttpSmscRequest.ProviderType;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ValidateDlrExtractExpressionContent implements ConstraintValidator<DrlExtractExpression, HttpSmscRequest> {

    @Override
    public boolean isValid(HttpSmscRequest request, ConstraintValidatorContext context) {
        if (request == null || request.getProviderType() == null || request.getDlrExtractExpression() == null) {
            return true; 
        }

        String expression = request.getDlrExtractExpression().trim();
        ProviderType providerType = request.getProviderType();

        boolean valid = true;
        String errorMessage = "";

        switch (providerType) {
            case XML -> {
                boolean validStatus = expression.matches("(?s).*<status>\\$\\.status</status>.*");//(?s)hello.*world
                boolean validDate   = expression.matches("(?s).*<date>\\$\\.date</date>.*");
                boolean validMsgid  = expression.matches("(?s).*<msgid>\\$\\.msgid</msgid>.*");

                if (!validStatus || !validDate || !validMsgid) {
                    valid = false;
                    errorMessage = "For XML, dlrExtractExpression must include: <status>$.status</status>, <date>$.date</date>, <msgid>$.msgid</msgid>";
                }
            }

            case JSON -> {
                boolean validStatus = expression.contains("\"status\": \"$.status\"") || expression.contains("'status': '$.status'");
                boolean validDate   = expression.contains("\"date\": \"$.date\"") || expression.contains("'date': '$.date'");
                boolean validMsgid  = expression.contains("\"msgid\": \"$.msgid\"") || expression.contains("'msgid': '$.msgid'");

                if (!validStatus || !validDate || !validMsgid) {
                    valid = false;
                    errorMessage = "For JSON, dlrExtractExpression must include: \"status\": \"$.status\", \"date\": \"$.date\", \"msgid\": \"$.msgid\"";
                }
            }

            case TEXT -> {
                valid = true;
            }

            default -> {
                valid = false;
                errorMessage = "Unsupported provider type: " + providerType;
            }
        }

        if (!valid) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(errorMessage)
                    .addPropertyNode("dlrExtractExpression")
                    .addConstraintViolation();
        }

        return valid;
    }
}
