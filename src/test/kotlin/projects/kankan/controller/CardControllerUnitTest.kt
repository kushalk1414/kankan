package projects.kankan.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import io.mockk.just
import io.mockk.runs
import io.mockk.verify

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import projects.kankan.exception.CardNotFoundException
import projects.kankan.model.BoardColumn
import projects.kankan.dto.CardDTO
import projects.kankan.service.CardService

@WebMvcTest(CardController::class)
class CardControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    private val objectMapper: ObjectMapper = ObjectMapper()

    @MockkBean
    private lateinit var cardService: CardService

    private fun createCardDTO(id: Long?, title: String, boardId: Long, column: BoardColumn) =
        CardDTO(id = id, title = title, description = "Description for $title", position = 0, boardId = boardId, column = column)

    private val TEST_BOARD_ID = 1L
    private val TEST_CARD_ID = 1L
    private val NON_EXISTENT_CARD_ID = 99L

    @Test
    fun `getCardsByBoardId should return a list of cards`() {
        // Given
        val card1 = createCardDTO(1L, "Task 1", TEST_BOARD_ID, BoardColumn.TODO)
        val card2 = createCardDTO(2L, "Task 2", TEST_BOARD_ID, BoardColumn.IN_PROGRESS)
        val cards = listOf(card1, card2)

        // Mock the service call: getCardsByBoardIdAndColumn(boardId, column)
        every { cardService.getCardsByBoardIdAndColumn(TEST_BOARD_ID, null) } returns cards

        // When & Then
        mockMvc.perform(get("/boards/$TEST_BOARD_ID/cards")) // Updated path
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.length()").value(2))
            .andExpect(jsonPath("$[0].id").value(1))
            .andExpect(jsonPath("$[0].title").value("Task 1"))
            .andExpect(jsonPath("$[0].boardId").value(TEST_BOARD_ID))
            .andExpect(jsonPath("$[0].column").value("TODO"))
            .andExpect(jsonPath("$[1].id").value(2))
            .andExpect(jsonPath("$[1].title").value("Task 2"))
            .andExpect(jsonPath("$[1].boardId").value(TEST_BOARD_ID))
            .andExpect(jsonPath("$[1].column").value("IN_PROGRESS"))
    }

    @Test
    fun `getCardsByBoardId with column filter should return filtered cards`() {
        // Given
        val card1 = createCardDTO(1L, "Task 1", TEST_BOARD_ID, BoardColumn.TODO)
        val cards = listOf(card1)

        every { cardService.getCardsByBoardIdAndColumn(TEST_BOARD_ID, BoardColumn.TODO) } returns cards

        // When & Then
        mockMvc.perform(get("/boards/$TEST_BOARD_ID/cards").param("column", "TODO")) // Updated path with param
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.length()").value(1))
            .andExpect(jsonPath("$[0].id").value(1))
            .andExpect(jsonPath("$[0].title").value("Task 1"))
            .andExpect(jsonPath("$[0].column").value("TODO"))
    }

    @Test
    fun `getCardById should return a card when found`() {
        // Given
        val cardDTO = createCardDTO(TEST_CARD_ID, "Task to find", TEST_BOARD_ID, BoardColumn.TODO)
        every { cardService.getCardById(TEST_BOARD_ID, TEST_CARD_ID) } returns cardDTO // Mock with both boardId and cardId

        // When & Then
        mockMvc.perform(get("/boards/$TEST_BOARD_ID/cards/$TEST_CARD_ID")) // Updated path
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(TEST_CARD_ID))
            .andExpect(jsonPath("$.title").value("Task to find"))
            .andExpect(jsonPath("$.boardId").value(TEST_BOARD_ID))
    }

   /* @Test
    fun `getCardById should return 404 when card not found`() {
        // Given
        val errorMessage = "Card with ID `$NON_EXISTENT_CARD_ID` not found."
        every { cardService.getCardById(TEST_BOARD_ID, NON_EXISTENT_CARD_ID) } throws CardNotFoundException(errorMessage) // Mock with both boardId and cardId

        // When & Then
        mockMvc.perform(get("/boards/$TEST_BOARD_ID/cards/$NON_EXISTENT_CARD_ID")) // Updated path
            .andExpect(status().isNotFound)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON)) // Expect JSON from GlobalExceptionHandler
            .andExpect(jsonPath("$.status").value(404))
            .andExpect(jsonPath("$.error").value("Not Found"))
            .andExpect(jsonPath("$.message").value(errorMessage))
            .andExpect(jsonPath("$.path").value("/boards/$TEST_BOARD_ID/cards/$NON_EXISTENT_CARD_ID"))
    }*/

    @Test
    fun `addCardToBoard should create a new card`() {
        // Given
        val newCardRequestDTO = createCardDTO(null, "New Task", TEST_BOARD_ID, BoardColumn.TODO) // ID is null for creation
        val savedCardDTO = newCardRequestDTO.copy(id = 5L) // Simulate ID generation

        // Mock service call: addCard(boardId, cardDTO)
        // Note: The controller copies the DTO with the path's boardId, so the service receives that.
        every { cardService.addCard(TEST_BOARD_ID, any<CardDTO>()) } returns savedCardDTO

        // When & Then
        mockMvc.perform(
            post("/boards/$TEST_BOARD_ID/cards") // Updated path
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newCardRequestDTO))
        )
            .andExpect(status().isCreated)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(5))
            .andExpect(jsonPath("$.title").value("New Task"))
            .andExpect(jsonPath("$.boardId").value(TEST_BOARD_ID))
            .andExpect(jsonPath("$.column").value("TODO"))
    }

    @Test
    fun `updateCardInBoard should update an existing card`() {
        // Given
        val updatedCardRequestDTO = createCardDTO(TEST_CARD_ID, "Updated Task", TEST_BOARD_ID, BoardColumn.IN_PROGRESS)

        // Mock service call: updateCard(boardId, cardId, cardDTO)
        every { cardService.updateCard(TEST_BOARD_ID, TEST_CARD_ID, any<CardDTO>()) } returns updatedCardRequestDTO

        // When & Then
        mockMvc.perform(
            put("/boards/$TEST_BOARD_ID/cards/$TEST_CARD_ID") // Updated path
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedCardRequestDTO))
        )
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(TEST_CARD_ID))
            .andExpect(jsonPath("$.title").value("Updated Task"))
            .andExpect(jsonPath("$.boardId").value(TEST_BOARD_ID))
            .andExpect(jsonPath("$.column").value("IN_PROGRESS"))
    }

  /*  @Test
    fun `updateCardInBoard should return 404 when card to update is not found`() {
        // Given
        val updatedCardRequestDTO = createCardDTO(NON_EXISTENT_CARD_ID, "NonExistent Task", TEST_BOARD_ID, BoardColumn.DONE)
        val errorMessage = "Card with ID `$NON_EXISTENT_CARD_ID` not found."

        // Mock service call: updateCard(boardId, cardId, cardDTO)
        every { cardService.updateCard(TEST_BOARD_ID, NON_EXISTENT_CARD_ID, any<CardDTO>()) } throws CardNotFoundException(errorMessage)

        // When & Then
        mockMvc.perform(
            put("/boards/$TEST_BOARD_ID/cards/$NON_EXISTENT_CARD_ID") // Updated path
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedCardRequestDTO))
        )
            .andExpect(status().isNotFound)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.status").value(404))
            .andExpect(jsonPath("$.error").value("Not Found"))
            .andExpect(jsonPath("$.message").value(errorMessage))
            .andExpect(jsonPath("$.path").value("/boards/$TEST_BOARD_ID/cards/$NON_EXISTENT_CARD_ID"))
    }
*/
    @Test
    fun `deleteCardFromBoard should delete a card successfully`() {
        // Given
        // Mock service call: deleteCard(boardId, cardId)
        every { cardService.deleteCard(TEST_BOARD_ID, TEST_CARD_ID) } just runs

        // When & Then
        mockMvc.perform(delete("/boards/$TEST_BOARD_ID/cards/$TEST_CARD_ID")) // Updated path
            .andExpect(status().isNoContent)

        verify { cardService.deleteCard(TEST_BOARD_ID, TEST_CARD_ID) } // Verify the service method was called
    }

   /* @Test
    fun `deleteCardFromBoard should return 404 when card to delete is not found`() {
        // Given
        val errorMessage = "Card with ID `$NON_EXISTENT_CARD_ID` not found."

        // Mock service call: deleteCard(boardId, cardId)
        every { cardService.deleteCard(TEST_BOARD_ID, NON_EXISTENT_CARD_ID) } throws CardNotFoundException(errorMessage)

        // When & Then
        mockMvc.perform(delete("/boards/$TEST_BOARD_ID/cards/$NON_EXISTENT_CARD_ID")) // Updated path
            .andDo(print()) // Still useful for debugging
            .andExpect(status().isNotFound)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.status").value(404))
            .andExpect(jsonPath("$.error").value("Not Found"))
            .andExpect(jsonPath("$.message").value(errorMessage))
            .andExpect(jsonPath("$.path").value("/boards/$TEST_BOARD_ID/cards/$NON_EXISTENT_CARD_ID"))

        verify { cardService.deleteCard(TEST_BOARD_ID, NON_EXISTENT_CARD_ID) } // Verify the service method was called
    }*/
}