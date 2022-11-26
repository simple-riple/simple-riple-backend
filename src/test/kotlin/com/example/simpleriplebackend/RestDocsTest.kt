package com.example.simpleriplebackend

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get
import org.springframework.restdocs.operation.preprocess.Preprocessors.*
import org.springframework.restdocs.payload.JsonFieldType
import org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import org.springframework.restdocs.payload.PayloadDocumentation.responseFields
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status


@AutoConfigureMockMvc
@AutoConfigureRestDocs
@SpringBootTest
class RestDocsTest {

    @Autowired
    lateinit var mockMvc: MockMvc

//    @Autowired
//    lateinit var objectMapper: ObjectMapper

    @Test
    fun temp() {

        val result = mockMvc.perform(
            get("/temp").contentType(MediaType.APPLICATION_JSON)
        )

        result
            .andExpect(status().isOk)
            .andDo(print())
            .andDo(
                document(
                    "temp", preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint()),
                    responseFields(
                        fieldWithPath("res").type(JsonFieldType.STRING).description("응답"),
                    )
                )
            );
    }

}