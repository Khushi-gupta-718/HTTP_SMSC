package com.http.smsc.service;

import com.http.smsc.dto.HttpSmscRequest;
import com.http.smsc.pagenationdto.PaginationRequest;
import com.http.smsc.pagenationdto.PaginationResponse;
import com.http.smsc.smscResponse.HttpSmscResponse;

public interface HttpSmscService {
    HttpSmscResponse create(HttpSmscRequest request);
    HttpSmscResponse update(String systemId, HttpSmscRequest request);
    void delete(String systemId);
    HttpSmscResponse get(String systemId);

    PaginationResponse<HttpSmscResponse> getAll(PaginationRequest paginationRequest);
}

