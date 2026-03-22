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

import projects.kankan.controller.BoardContoller
import projects.kankan.dto.BoardDTO
import projects.kankan.service.BoardService

@WebMvcTest(BoardContoller::class)
@AutoConfigureMockMvc
class BoardControllerUnitTest {

    @Autowired
    lateinit var mockMvc: MockMvc

    private val objectMapper = ObjectMapper()

    @MockkBean
    lateinit var boardService: BoardService

    @Test
    fun `should create board`() {
        val boardDTO = BoardDTO(null, title = "My First Board")
        val createdBoardDTO = BoardDTO(id = 1, title = "My First Board")

        every { boardService.addBoard(any()) } returns createdBoardDTO

        mockMvc.perform(
            post("/boards")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(boardDTO))
        )
            .andExpect(status().isCreated)
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.title").value("My First Board"))
    }
}