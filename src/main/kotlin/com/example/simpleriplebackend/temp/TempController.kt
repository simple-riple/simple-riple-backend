package com.example.simpleriplebackend.temp

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/temp")
@RestController
class TempController {

    @GetMapping
    fun getTemp(
        @RequestParam paramStr: String,
        @RequestParam paramNum: Int
    ): ResponseEntity<TempDto> {
        val tempDto = TempDto(paramStr, paramNum)
        return ResponseEntity.ok(tempDto)
    }

    @PostMapping
    fun postTemp(
        @RequestBody dto: TempDto
    ): ResponseEntity<TempDto> {
        return ResponseEntity.ok(dto)
    }
}