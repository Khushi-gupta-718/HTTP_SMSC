package com.http.smsc.controller;

import com.http.smsc.dto.HttpSmscRequest;
import com.http.smsc.pagenationdto.PaginationRequest;
import com.http.smsc.pagenationdto.PaginationResponse;
import com.http.smsc.service.HttpSmscService;
import com.http.smsc.smscResponse.HttpSmscResponse;
import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/smsc")
@Validated
public class HttpSmscController {

    private final HttpSmscService service;

    public HttpSmscController(HttpSmscService service) {
        this.service = service;
    }

   
    @PostMapping
    public ResponseEntity<HttpSmscResponse> create(@Valid @RequestBody HttpSmscRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(request));
     }

    
    @PutMapping("/{systemId}")
    public ResponseEntity<HttpSmscResponse> update(
            @PathVariable String systemId,
            @Valid @RequestBody HttpSmscRequest request) {
		return ResponseEntity.ok(service.update(systemId, request));

    }

   
    @DeleteMapping("/{systemId}")
    public ResponseEntity<Void> delete(@PathVariable String systemId) {
        service.delete(systemId);
       return ResponseEntity.noContent().build();

    }

    
    @GetMapping("/{systemId}")
    public ResponseEntity<HttpSmscResponse> get(@PathVariable String systemId) {
		  return ResponseEntity.ok(service.get(systemId));

    }

	

    @GetMapping
    public ResponseEntity<PaginationResponse<HttpSmscResponse>> getAll(@Valid @ModelAttribute PaginationRequest paginationRequest) {
        PaginationResponse<HttpSmscResponse> response = service.getAll(paginationRequest);
        return ResponseEntity.ok(response);
    }


   
}
