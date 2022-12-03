package com.example.simpleriplebackend.admin

import io.swagger.annotations.Api
import io.swagger.v3.oas.annotations.Operation
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Api(description = "어드민 테스트용 컨트롤러")
@RequestMapping("/admin")
@RestController
class AdminController {

    @Operation(summary = "겟", description = "어쩌구저쩌구 겟")
    @GetMapping
    fun getTemp(): ResponseEntity<String> {
        return ResponseEntity.ok("")
    }
}