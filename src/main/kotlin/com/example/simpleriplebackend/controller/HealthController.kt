package com.example.simpleriplebackend.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDateTime

@RequestMapping("/health")
@RestController
class HealthController {

    @GetMapping
    fun healthCheck(): ResponseEntity<Map<String, String>> {
        val response = mapOf("res" to "I'M STILL ALIVE")
        return ResponseEntity.ok(response);
    }
}