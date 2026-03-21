package projects.kankan.projects.kankan.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import projects.kankan.model.Card
import projects.kankan.projects.kankan.dto.CardRequest
import projects.kankan.projects.kankan.dto.CardResponse
import projects.kankan.projects.kankan.model.BoardColumn
import projects.kankan.projects.kankan.service.CardService
class CardController(private val cardService: CardService) {

    @PostMapping("/{boardId}/cards/{column}")
    fun addCard(
        @PathVariable boardId: Long,
        @PathVariable column: BoardColumn,
        @RequestBody request: CardRequest
    ): ResponseEntity<CardResponse> = ResponseEntity.ok(cardService.addCard(request))
}