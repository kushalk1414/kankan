package projects.kankan.service

import org.springframework.stereotype.Service
import projects.kankan.model.Card

import projects.kankan.exception.CardNotFoundException
import projects.kankan.repository.CardRepository
import java.util.*

@Service
class CardService(private val cardRepository: CardRepository) {

    fun getAllCards(): List<Card> = cardRepository.findAll()

    fun getCardById(id: Long): Card = cardRepository.findById(id)
        .orElseThrow {CardNotFoundException("Card with ID `$id` not found")}

    fun addCard(card: Card): Card = cardRepository.save(card)

    fun updateCard(id: Long, updatedCard: Card): Card {
        val existingCard: Optional<Card> = cardRepository.findById(id)

        return if (existingCard.isPresent){
            existingCard.get()
                .let {
                    it.title = updatedCard.title
                    it.description = updatedCard.description
                    it.position = updatedCard.position
                    it.column = updatedCard.column

                    cardRepository.save(it)
                }
        } else {
            throw CardNotFoundException("Card with ID `$id` not found")
        }
    }

    fun deleteCard(id: Long) {
        if (!cardRepository.existsById(id)) {
            throw CardNotFoundException("Card with ID `$id` not found")
        }
        cardRepository.deleteById(id)
    }
}