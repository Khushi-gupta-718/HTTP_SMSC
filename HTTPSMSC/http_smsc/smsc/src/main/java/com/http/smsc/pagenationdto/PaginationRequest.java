package com.http.smsc.pagenationdto;


import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public class PaginationRequest {
	

    @Min(value = 0, message = "page must be 0 or greater")
    private int page;

    @Min(value = 1, message = "size must be at least 1")
    private int size;

    @NotBlank(message = "sortBy must not be blank")
    private String sortBy;

    
    @NotBlank(message = "sortDir must not be blank")
    private String sortDir;
    public PaginationRequest() {
        this.page = 0;
        this.size = 10;
        this.sortBy = "id";
        this.sortDir = "asc";
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getSortBy() {
        return sortBy;
    }

    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }

    public String getSortDir() {
        return sortDir;
    }

    public void setSortDir(String sortDir) {
        this.sortDir = sortDir;
    }
}
