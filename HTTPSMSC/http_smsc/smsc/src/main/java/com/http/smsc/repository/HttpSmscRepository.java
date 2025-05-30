package com.http.smsc.repository;

import com.http.smsc.dto.HttpSmscEntry;

import org.springframework.data.jpa.repository.JpaRepository;

public interface HttpSmscRepository extends JpaRepository<HttpSmscEntry, String> {

}
