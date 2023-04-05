package com.tafakkoor.e_learn.config.security.firewall;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.web.firewall.FirewalledRequest;
import org.springframework.security.web.firewall.HttpFirewall;
import org.springframework.security.web.firewall.RequestRejectedException;

public class CustomFirewall implements HttpFirewall {
    @Override
    public FirewalledRequest getFirewalledRequest(HttpServletRequest request) throws RequestRejectedException {
        return null;
    }

    @Override
    public HttpServletResponse getFirewalledResponse(HttpServletResponse response) {
        return null;
    }
}
