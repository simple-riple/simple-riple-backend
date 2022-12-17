package com.example.simpleriplebackend.temp

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import springfox.documentation.builders.ApiInfoBuilder
import springfox.documentation.builders.PathSelectors
import springfox.documentation.builders.RequestHandlerSelectors
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spring.web.plugins.Docket

@Configuration
class  TempSwaggerConfig {

    @Bean
    fun tempConfig(): Docket {
        return Docket(DocumentationType.OAS_30)
            .groupName("temp")
            .useDefaultResponseMessages(false)
            .select()
            .apis(RequestHandlerSelectors.basePackage("com.example.simpleriplebackend.temp"))
            .paths(PathSelectors.any())
            .build()
            .apiInfo(
                ApiInfoBuilder()
                    .title("Simple Riple - Temp API")
                    .description("임시 테스트용 API 명세")
                    .version("1.0")
                    .build()
            )
    }
}