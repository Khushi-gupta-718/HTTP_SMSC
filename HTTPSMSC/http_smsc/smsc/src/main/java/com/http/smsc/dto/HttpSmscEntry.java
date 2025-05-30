package com.http.smsc.dto;
 
import jakarta.persistence.Column;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import jakarta.persistence.Lob;

import jakarta.persistence.Table;

import lombok.AllArgsConstructor;

import lombok.Data;

import lombok.NoArgsConstructor;

import lombok.ToString;
 
@Entity

@Table(name = "http_smsc")

@Data

@NoArgsConstructor

@AllArgsConstructor

@ToString

public class HttpSmscEntry {
 
	@Id
    @Column(name = "system_id", nullable = false, length = 50)
	private String systemId;
 
	@Column(name = "password", length = 100)

	private String password;
 
	@Column(name = "server_url", nullable = false, length = 255)

	private String serverUrl;
 
	// Request body template (e.g., JSON or XML with placeholders)

	@Lob

	@Column(name = "request_body_template", columnDefinition = "TEXT")

	private String requestBodyTemplate;
 
	// Expression to extract a value from the response (e.g., JSONPath, XPath)

	@Lob

	@Column(name = "response_extract_expression", columnDefinition = "TEXT")

	private String responseExtractExpression;
 
	// Expression to extract a value from the DLR (Delivery Receipt)

	@Lob

	@Column(name = "dlr_extract_expression", columnDefinition = "TEXT")

	private String dlrExtractExpression;
 
	@Column(name = "provider_type", length = 50)

	private String providerType;
 
	// Custom HTTP headers as JSON string

	@Lob

	@Column(name = "custom_headers", columnDefinition = "TEXT")

	private String customHeaders;
 
}

 