package projects.kankan.board.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

import projects.kankan.projects.kankan.controller.BoardContoller
import projects.kankan.projects.kankan.dto.BoardRequest
import projects.kankan.projects.kankan.dto.BoardResponse
import projects.kankan.projects.kankan.service.BoardService

@WebMvcTest(BoardContoller::class)
@AutoConfigureMockMvc
class BoardCardControllerUnitTest {

    @Autowired
    lateinit var mockMvc: MockMvc

    private val objectMapper = ObjectMapper()

    @MockkBean
    lateinit var boardService: BoardService

    @Test
    fun `should create board`() {
        val request = BoardRequest(title = "My First Board")
        val response = BoardResponse(id = 1, title = "My First Board")

        every { boardService.createBoard(any()) } returns response

        mockMvc.perform(
            post("/boards")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.title").value("My First Board"))
    }
}