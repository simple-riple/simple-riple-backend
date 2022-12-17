package com.example.simpleriplebackend.admin

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import springfox.documentation.builders.ApiInfoBuilder
import springfox.documentation.builders.PathSelectors
import springfox.documentation.builders.RequestHandlerSelectors
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spring.web.plugins.Docket

@Configuration
class AdminSwaggerConfig {

    @Bean
    fun adminConfig(): Docket {
        return Docket(DocumentationType.OAS_30)
            .groupName("admin")
            .useDefaultResponseMessages(false)
            .select()
            .apis(RequestHandlerSelectors.basePackage("com.example.simpleriplebackend.admin"))
            .paths(PathSelectors.any())
            .build()
            .apiInfo(
                ApiInfoBuilder()
                    .title("Simple Riple - Admin API")
                    .description("어드민 API 명세")
                    .version("1.0")
                    .build()
            )
    }
}