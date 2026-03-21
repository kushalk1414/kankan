package projects.kankan.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import io.mockk.just
import io.mockk.runs
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import projects.kankan.exception.CardNotFoundException
import projects.kankan.model.BoardColumn
import projects.kankan.model.Card
import projects.kankan.service.CardService

@WebMvcTest(CardController::class) // This annotation focuses on testing the CardController
class CardControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc // Used to perform HTTP requests

    private val objectMapper: ObjectMapper = ObjectMapper()

    @MockkBean // Mocks the CardService, preventing it from interacting with the real database
    private lateinit var cardService: CardService

    //region Helper functions to create test data
    private fun createCard(id: Long, title: String, column: BoardColumn) =
        Card(id = id, title = title, description = "Description for $title", position = 0, column = column)
    //endregion

    @Test
    fun `getAllCards should return a list of cards`() {
        // Given
        val card1 = createCard(1L, "Task 1", BoardColumn.TODO)
        val card2 = createCard(2L, "Task 2", BoardColumn.IN_PROGRESS)
        val cards = listOf(card1, card2)

        every { cardService.getAllCards() } returns cards

        // When & Then
        mockMvc.perform(get("/cards"))
            .andExpect(status().isOk) // Expect HTTP 200 OK
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.length()").value(2)) // Expect 2 items in the array
            .andExpect(jsonPath("$[0].id").value(1))
            .andExpect(jsonPath("$[0].title").value("Task 1"))
            .andExpect(jsonPath("$[0].column").value("TODO"))
            .andExpect(jsonPath("$[1].id").value(2))
            .andExpect(jsonPath("$[1].title").value("Task 2"))
            .andExpect(jsonPath("$[1].column").value("IN_PROGRESS"))
    }

    @Test
    fun `getCardById should return a card when found`() {
        // Given
        val card = createCard(1L, "Task to find", BoardColumn.TODO)
        every { cardService.getCardById(any()) } returns card

        // When & Then
        mockMvc.perform(get("/cards/1"))
            .andExpect(status().isOk) // Expect HTTP 200 OK
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.title").value("Task to find"))
    }

    @Test
    fun `getCardById should return 404 when card not found`() {
        // Given
        every { cardService.getCardById(99L) } throws CardNotFoundException("Card with ID `99` not found")

        // When & Then
        mockMvc.perform(get("/cards/99"))
            .andExpect(status().isNotFound) // Expect HTTP 404 Not Found
            .andExpect(content().string("Card with ID `99` not found")) // Check the error message
    }

    @Test
    fun `addCard should create a new card`() {
        // Given
        val newCardRequest = Card(id = 0, title = "New Task", description = "New description", position = 0, column = BoardColumn.TODO)
        val savedCard = newCardRequest.copy(id = 5L) // Simulate ID generation

        every { cardService.addCard(any()) } returns savedCard

        // When & Then
        mockMvc.perform(
            post("/cards")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newCardRequest)) // Convert object to JSON string
        )
            .andExpect(status().isCreated) // Expect HTTP 201 Created
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(5))
            .andExpect(jsonPath("$.title").value("New Task"))
            .andExpect(jsonPath("$.column").value("TODO"))
    }

    @Test
    fun `updateCard should update an existing card`() {
        // Given
        val updatedCardRequest = Card(id = 1L, title = "Updated Task", description = "New description", position = 1, column = BoardColumn.IN_PROGRESS)
        val existingCard = createCard(1L, "Original Task", BoardColumn.TODO)

        every { cardService.updateCard(1L, updatedCardRequest) } returns updatedCardRequest

        // When & Then
        mockMvc.perform(
            put("/cards/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedCardRequest))
        )
            .andExpect(status().isOk) // Expect HTTP 200 OK
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.title").value("Updated Task"))
            .andExpect(jsonPath("$.column").value("IN_PROGRESS"))
    }

    @Test
    fun `updateCard should return 404 when card to update is not found`() {
        // Given
        val updatedCardRequest = Card(id = 99L, title = "NonExistent Task", description = null, position = 0, column = BoardColumn.DONE)

        every { cardService.updateCard(99L, updatedCardRequest) } throws CardNotFoundException("Card with ID `99` not found")
        // When & Then
        mockMvc.perform(
            put("/cards/99")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedCardRequest))
        )
            .andExpect(status().isNotFound) // Expect HTTP 404 Not Found
            .andExpect(content().string("Card with ID `99` not found"))
    }

    @Test
    fun `deleteCard should delete a card successfully`() {
        // Given
        // Mocking the delete method to do nothing for a successful deletion scenario
        // `willDoNothing` is used for void methods
        every { cardService.deleteCard(any()) } just runs

        // When & Then
        mockMvc.perform(delete("/cards/1"))
            .andExpect(status().isNoContent)
    }

    @Test
    fun `deleteCard should return 404 when card to delete is not found`() {
        // Given

        val nonExistentID = 100L
        every { cardService.deleteCard(nonExistentID) } throws CardNotFoundException("Card with ID `$nonExistentID` not found")
        // When & Then
        mockMvc.perform(delete("/cards/$nonExistentID"))
            .andExpect(status().isNotFound) // Expect HTTP 404 Not Found
            .andExpect(content().string("Card with ID `$nonExistentID` not found"))
    }
}