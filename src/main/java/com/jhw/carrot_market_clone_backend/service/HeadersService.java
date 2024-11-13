package com.jhw.carrot_market_clone_backend.service;

import org.springframework.http.HttpHeaders;

public class HeadersService {

    public static void addAuthorization(HttpHeaders headers, String token) {
        headers.set("Authorization", "Bearer " + token);
    }
}
