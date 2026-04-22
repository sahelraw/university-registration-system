package com.uni.demo.security.demo;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/v1/demo-controller") //to set the base URL for the demo controller to be "/api/v1/demo-controller" and then we will implement the endpoints for this controller to test the authentication and authorization in our application and then we will secure these endpoints using the security configuration that we will implement later.
public class DemoController {

    @GetMapping
    public ResponseEntity<String> sayHello() {
        return ResponseEntity.ok("Hello from secured endpoint"); //to return a response with the message "Hello from secured endpoint" to test the authentication and authorization in our application and then we will secure this endpoint using the security configuration that we will implement later to allow only authenticated users to access this endpoint and to return this message to the authenticated users.
    }

}
