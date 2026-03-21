package projects.kankan.controller

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import projects.kankan.model.Card
import projects.kankan.service.CardService

@RestController
@RequestMapping("/cards")
class CardController(private val cardService: CardService) {

    @GetMapping
    fun getAllCards(): ResponseEntity<List<Card>> {
        val cards = cardService.getAllCards()
        return ResponseEntity.ok(cards)
    }

    @GetMapping("/{id}")
    fun getCardById(@PathVariable id: Long): ResponseEntity<Card> {
        val card = cardService.getCardById(id)
        return ResponseEntity.ok(card)
    }

    @PostMapping
    fun addCard(@RequestBody card: Card): ResponseEntity<Card> {
        val newCard = cardService.addCard(card)
        return ResponseEntity(newCard, HttpStatus.CREATED)
    }

    @PutMapping("/{id}")
    fun updateCard(@PathVariable id: Long, @RequestBody card: Card): ResponseEntity<Card> {
        val updatedCard = cardService.updateCard(id, card)
        return ResponseEntity.ok(updatedCard)
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteCard(@PathVariable id: Long){
        cardService.deleteCard(id)
    }
}