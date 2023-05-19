package com.crm.contacts.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(path = "/api/v1/health")
public class HealthCheckController {

    @GetMapping
    public String healthCheck() {
        return "200";
    }
}
