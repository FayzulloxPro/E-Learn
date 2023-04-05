package com.tafakkoor.e_learn.utils;

import com.tafakkoor.e_learn.repository.AuthUserRepository;
import com.tafakkoor.e_learn.repository.TokenRepository;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    private final AuthUserRepository authUserRepository;
    private final TokenRepository tokenRepository;

    public Main( AuthUserRepository authUserRepository, TokenRepository tokenRepository ) {
        this.authUserRepository = authUserRepository;
        this.tokenRepository = tokenRepository;
    }

    public static void main(String[] args) {

    }

}
