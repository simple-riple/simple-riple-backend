package com.example.simpleriplebackend.temp

import io.swagger.annotations.Api
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@Api(description = "임시 테스트용 컨트롤러")
@RequestMapping("/temp")
@RestController
class TempController {

    @Operation(summary = "겟", description = "어쩌구저쩌구 겟")
    @GetMapping("/{pathParam}")
    fun getTemp(
        @Parameter(description="경로 파라미터") @PathVariable("pathParam") pathParam: Int,
        @Parameter(description="문자 파라미터") @RequestParam paramStr: String,
        @Parameter(description="숫자 파라미터") @RequestParam paramNum: Int
    ): ResponseEntity<TempDto> {
        val tempDto = TempDto(paramStr, paramNum)
        return ResponseEntity.ok(tempDto)
    }

    @Operation(summary = "포스트", description = "어쩌구저쩌구 포스트")
    @PostMapping
    fun postTemp(
        @RequestBody dto: TempDto
    ): ResponseEntity<TempDto> {
        return ResponseEntity.ok(dto)
    }
}