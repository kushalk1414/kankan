package projects.kankan.controller

import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import projects.kankan.model.BoardColumn
import projects.kankan.dto.CardDTO
import projects.kankan.service.CardService

@RestController
@RequestMapping("boards/{boardId}/cards")
class CardController(private val cardService: CardService) {

    @GetMapping
    fun getCardsByBoardId(
        @PathVariable boardId: Long,
        @RequestParam(required = false) column: BoardColumn? // NEW: Optional query parameter for column
    ): ResponseEntity<List<CardDTO>> {
        val cards = cardService.getCardsByBoardIdAndColumn(boardId, column)
        return ResponseEntity.ok(cards)
    }

    @GetMapping("/{cardId}")
    fun getCardById(@PathVariable boardId: Long, @PathVariable cardId: Long): ResponseEntity<CardDTO> {
        val card = cardService.getCardById(boardId, cardId)
        return ResponseEntity.ok(card)
    }

    @PostMapping
    fun addCardToBoard(@PathVariable boardId: Long, @RequestBody cardDTO: CardDTO): ResponseEntity<CardDTO> {
        val cardToCreate = cardDTO.copy(boardId = boardId)
        val newCard: CardDTO = cardService.addCard(boardId, cardToCreate)
        return ResponseEntity(newCard, HttpStatus.CREATED)
    }

    @PutMapping("/{cardId}")
    fun updateCardInBoard(@PathVariable boardId: Long, @PathVariable cardId: Long, @RequestBody cardDTO: CardDTO): ResponseEntity<CardDTO> {
        val cardToUpdate = cardDTO.copy(id = cardId, boardId = boardId)
        val updatedCard = cardService.updateCard(boardId, cardId, cardToUpdate)
        return ResponseEntity.ok(updatedCard)
    }

    @DeleteMapping("/{cardId}") // Added DELETE endpoint
    fun deleteCardFromBoard(@PathVariable boardId: Long, @PathVariable cardId: Long): ResponseEntity<Void> {
        cardService.deleteCard(boardId, cardId)
        return ResponseEntity.noContent().build()
    }

}