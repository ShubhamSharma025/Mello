// src/main/java/com/yourpackage/mello/controller/TestController.java
package com.yourpackage.mello.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/hello")
    public String sayHello() {
        return "Hello from Mello! Your backend is working!";
    }
}