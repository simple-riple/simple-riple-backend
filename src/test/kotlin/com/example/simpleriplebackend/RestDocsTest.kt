package com.example.simpleriplebackend

import com.example.simpleriplebackend.temp.TempDto
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post
import org.springframework.restdocs.operation.preprocess.Preprocessors.*
import org.springframework.restdocs.payload.JsonFieldType
import org.springframework.restdocs.payload.PayloadDocumentation.*
import org.springframework.restdocs.request.RequestDocumentation.*
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status


@AutoConfigureMockMvc
@AutoConfigureRestDocs
@SpringBootTest
class RestDocsTest {

    @Autowired
    lateinit var mockMvc: MockMvc

    @Autowired
    lateinit var objectMapper: ObjectMapper

    @Test
    fun getTempTest() {
        // Given + When
        val result = mockMvc.perform(
            get("/temp/{pathParam}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .param("paramStr", "abs")
                .param("paramNum", "123")
        )

        // Then
        result
            .andExpect(status().isOk)
            .andDo(print())
            .andDo(
                document(
                    "getTemp", preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint()),
                    pathParameters(
                        parameterWithName("pathParam").description("경로 파라미터")
                    ),
                    requestParameters(
                        parameterWithName("paramStr").description("문자 파라미터"),
                        parameterWithName("paramNum").description("숫자 파라미터"),
                    ),
                    responseFields(
                        fieldWithPath("str").type(JsonFieldType.STRING).description("문자 응답"),
                        fieldWithPath("num").type(JsonFieldType.NUMBER).description("숫자 응답"),
                    )
                )
            );
    }

    @Test
    fun postTempTest() {
        // Given
        val requestBody = objectMapper.writeValueAsString(
            TempDto("abc", 123)
        )

        // When
        val result = mockMvc.perform(
            post("/temp")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody)
        )

        // Then
        result
            .andExpect(status().isOk)
            .andDo(print())
            .andDo(
                document(
                    "postTemp", preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint()),
                    requestFields(
                        fieldWithPath("str").type(JsonFieldType.STRING).description("문자 요청"),
                        fieldWithPath("num").type(JsonFieldType.NUMBER).description("숫자 요청"),
                    ),
                    responseFields(
                        fieldWithPath("str").type(JsonFieldType.STRING).description("문자 응답"),
                        fieldWithPath("num").type(JsonFieldType.NUMBER).description("숫자 응답"),
                    )
                )
            );
    }

}