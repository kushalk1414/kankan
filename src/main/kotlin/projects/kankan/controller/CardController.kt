package projects.kankan.controller

import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import projects.kankan.model.Card
import projects.kankan.projects.kankan.dto.CardDTO
import projects.kankan.service.CardService

@RestController
@RequestMapping("/cards")
class CardController(private val cardService: CardService) {

    @GetMapping
    fun getAllCards(@RequestParam("card_title") cardTitle : String?): ResponseEntity<List<CardDTO>> {
        val cards = cardService.getAllCards(cardTitle)
        return ResponseEntity.ok(cards)
    }

    @GetMapping("/{id}")
    fun getCardById(@PathVariable id: Long): ResponseEntity<CardDTO> {
        val card = cardService.getCardById(id)
        return ResponseEntity.ok(card)
    }

    @PostMapping
    fun addCard(@RequestBody @Valid cardDTO: CardDTO): ResponseEntity<CardDTO> {
        val newCard = cardService.addCard(cardDTO)
        return ResponseEntity(newCard, HttpStatus.CREATED)
    }

    @PutMapping("/{id}")
    fun updateCard(@PathVariable id: Long, @RequestBody cardDTO: CardDTO): ResponseEntity<CardDTO> {
        val updatedCard = cardService.updateCard(id, cardDTO)
        println(updatedCard)
        return ResponseEntity.ok(updatedCard)
    }

    @DeleteMapping("/{id}")
    fun deleteCard(@PathVariable id: Long): ResponseEntity<Void> { // Changed return type to ResponseEntity<Void>
        cardService.deleteCard(id)
        return ResponseEntity.noContent().build() // Explicitly return 204 for a successful deletion
    }
}