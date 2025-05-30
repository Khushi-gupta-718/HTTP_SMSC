package com.http.smsc.service.impl;

import com.http.smsc.dto.HttpSmscEntry;
import com.http.smsc.dto.HttpSmscRequest;
import com.http.smsc.dto.HttpSmscRequest.ProviderType;
import com.http.smsc.exception.*;
import com.http.smsc.pagenationdto.PaginationRequest;
import com.http.smsc.pagenationdto.PaginationResponse;
import com.http.smsc.repository.HttpSmscRepository;
import com.http.smsc.service.HttpSmscService;
import com.http.smsc.smscResponse.HttpSmscResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HttpSmscServiceImpl implements HttpSmscService {

	private static final Logger logger = LoggerFactory.getLogger(HttpSmscServiceImpl.class);

	private final HttpSmscRepository repository;

	public HttpSmscServiceImpl(HttpSmscRepository repository) {
		this.repository = repository;
	}

	@Override
	public HttpSmscResponse create(HttpSmscRequest request) {
		try {
			validateRequest(request);
			HttpSmscEntry entry = mapToEntity(request);
			HttpSmscEntry saved = repository.save(entry);
			logger.info("Created HttpSmscEntry with systemId: {}", saved.getSystemId());
			return mapToResponse(saved);
		} catch (BadRequestException e) {
			logger.warn("Bad request during create operation: {}", e.getMessage());
			throw e;
		} catch (Exception e) {
			logger.error("Unexpected error during create operation", e);
			throw new InternalServerError("Internal server error while creating HttpSmscEntry");
		}
	}

	@Override
	public HttpSmscResponse update(String systemId, HttpSmscRequest request) {
		try {
			validateRequest(request);
			HttpSmscEntry existing = repository.findById(systemId).orElseThrow(
					() -> new ResourceNotFoundException("HttpSmscEntry not found with systemId: " + systemId));

			updateEntityFromRequest(existing, request);

			HttpSmscEntry updated = repository.save(existing);
			logger.info("Updated HttpSmscEntry with systemId: {}", updated.getSystemId());
			return mapToResponse(updated);
		} catch (BadRequestException e) {
			logger.warn("Bad request during update operation: {}", e.getMessage());
			throw e;
		} catch (ResourceNotFoundException e) {
			logger.warn("Update failed: {}", e.getMessage());
			throw e;
		} catch (Exception e) {
			logger.error("Unexpected error during update operation", e);
			throw new InternalServerError("Internal server error while updating HttpSmscEntry");
		}
	}

	@Override
	public void delete(String systemId) {
		try {
			if (!repository.existsById(systemId)) {
				throw new ResourceNotFoundException("HttpSmscEntry not found with systemId: " + systemId);
			}
			repository.deleteById(systemId);
			logger.info("Deleted HttpSmscEntry with systemId: {}", systemId);
		} catch (ResourceNotFoundException e) {
			logger.warn("Delete failed: {}", e.getMessage());
			throw e;
		} catch (Exception e) {
			logger.error("Unexpected error during delete operation", e);
			throw new InternalServerError("Internal server error while deleting HttpSmscEntry");
		}
	}

	@Override
	public HttpSmscResponse get(String systemId) {
		try {
			return repository.findById(systemId).map(this::mapToResponse).orElseThrow(
					() -> new ResourceNotFoundException("HttpSmscEntry not found with systemId: " + systemId));
		} catch (ResourceNotFoundException e) {
			logger.warn("Get failed: {}", e.getMessage());
			throw e;
		} catch (Exception e) {
			logger.error("Unexpected error during get operation", e);
			throw new InternalServerError("Internal server error while fetching HttpSmscEntry");
		}
	}


	@Override
	public PaginationResponse<HttpSmscResponse> getAll(PaginationRequest paginationRequest) {
	    validatePaginationRequest(paginationRequest);

	    String sortByInput = paginationRequest.getSortBy().trim().toLowerCase();
	    String sortBy;
	    if (sortByInput.equals("systemid")) {
	        sortBy = "systemId"; 
	    } else {
	        throw new BadRequestException("sortBy must be 'systemId'");
	    }
	    
	    Sort sort = paginationRequest.getSortDir().equalsIgnoreCase("asc")
	            ? Sort.by(sortBy).ascending()
	            : Sort.by(sortBy).descending();

	    Pageable pageable = PageRequest.of(paginationRequest.getPage(), paginationRequest.getSize(), sort);

	    Page<HttpSmscEntry> pageEntries = repository.findAll(pageable);

	    if (pageEntries.isEmpty()) {
	        throw new ResourceNotFoundException("No HttpSmscEntry records found");
	    }

	    List<HttpSmscResponse> responses = pageEntries.stream()
	            .filter(entry -> {
	                try {
	                    ProviderType.valueOf(entry.getProviderType().trim().toUpperCase());
	                    return true;
	                } catch (IllegalArgumentException e) {
	                    logger.warn("Invalid providerType '{}' for systemId '{}'", entry.getProviderType(), entry.getSystemId());
	                    return false;
	                }
	            })
	            .map(this::mapToResponse)
	            .toList();

	    if (responses.isEmpty()) {
	        throw new ResourceNotFoundException("No valid HttpSmscEntry records found after filtering");
	    }

	    return new PaginationResponse<>(
	            responses,
	            pageEntries.getNumber(),
	            pageEntries.getSize(),
	            pageEntries.getTotalElements(),
	            pageEntries.getTotalPages(),
	            pageEntries.isLast()
	    );
	}


	private void validatePaginationRequest(PaginationRequest paginationRequest) {
		if (paginationRequest == null) {
			throw new BadRequestException("PaginationRequest cannot be null");
		}
	}

	private HttpSmscEntry mapToEntity(HttpSmscRequest request) {
		HttpSmscEntry entry = new HttpSmscEntry();
		entry.setSystemId(request.getSystemId());
		entry.setPassword(request.getPassword());
		entry.setServerUrl(request.getServerUrl());
		entry.setRequestBodyTemplate(request.getRequestBodyTemplate());
		entry.setResponseExtractExpression(request.getResponseExtractExpression());
		entry.setDlrExtractExpression(request.getDlrExtractExpression());
		entry.setProviderType(request.getProviderType().name());
		entry.setCustomHeaders(request.getCustomHeaders());
		return entry;
	}

	private void updateEntityFromRequest(HttpSmscEntry entity, HttpSmscRequest request) {
		entity.setPassword(request.getPassword());
		entity.setServerUrl(request.getServerUrl());
		entity.setRequestBodyTemplate(request.getRequestBodyTemplate());
		entity.setResponseExtractExpression(request.getResponseExtractExpression());
		entity.setDlrExtractExpression(request.getDlrExtractExpression());
		entity.setProviderType(request.getProviderType().name());
		entity.setCustomHeaders(request.getCustomHeaders());
	}

	private HttpSmscResponse mapToResponse(HttpSmscEntry e) {
		HttpSmscResponse r = new HttpSmscResponse();
		r.setSystemId(e.getSystemId());
		r.setServerUrl(e.getServerUrl());
		r.setRequestBodyTemplate(e.getRequestBodyTemplate());
		r.setResponseExtractExpression(e.getResponseExtractExpression());
		r.setDlrExtractExpression(e.getDlrExtractExpression());
		r.setCustomHeaders(e.getCustomHeaders());

		String dbProviderType = e.getProviderType();

		try {
			
			ProviderType providerType = ProviderType.valueOf(dbProviderType.trim().toUpperCase());
			r.setProviderType(providerType);
		} catch (BadRequestException ex) {
			logger.error("Unsupported providerType '{}' in DB for systemId '{}'", dbProviderType, e.getSystemId());
			throw new InternalServerError(
					"Unsupported providerType: " + dbProviderType + " for systemId: " + e.getSystemId());
		}

		return r;
	}

	private void validateRequest(HttpSmscRequest request) {
		if (request == null) {
			throw new BadRequestException("Request cannot be null");
		}

}
}
