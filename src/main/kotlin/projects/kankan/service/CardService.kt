package projects.kankan.service

import org.springframework.stereotype.Service
import projects.kankan.model.Card

import projects.kankan.exception.CardNotFoundException
import projects.kankan.projects.kankan.dto.CardDTO
import projects.kankan.repository.CardRepository
import java.util.*

@Service
class CardService(private val cardRepository: CardRepository) {

    fun getAllCards(cardTitle: String?): List<CardDTO> {
        val cards = cardTitle?.let {
            cardRepository.findCardsByTitle((cardTitle))
        } ?: cardRepository.findAll()

        return cards.map {
            CardDTO(it.id, it.title, it.description, it.position, it.column)
        }
    }

    fun getCardById(id: Long): CardDTO {
        val card = cardRepository.findById(id)

        return if (card.isPresent) {
            card.get()
                .let{
                    CardDTO(it.id, it.title, it.description, it.position, it.column)
                }
        }
        else throw CardNotFoundException("Card with ID `$id` not found")


    }
    fun addCard(cardDTO: CardDTO): CardDTO {
        val card = cardDTO.let {
            Card(null, it.title, it.description)
        }
        cardRepository.save(card)

        return card.let {
            CardDTO(it.id, it.title, it.description, it.position, it.column)
        }
    }

    fun updateCard(id: Long, updatedCardDTO: CardDTO): CardDTO {
        val existingCard: Optional<Card> = cardRepository.findById(id)

        return if (existingCard.isPresent){
            existingCard.get()
                .let {
                    it.title = updatedCardDTO.title
                    it.description = updatedCardDTO.description
                    it.position = updatedCardDTO.position
                    it.column = updatedCardDTO.column

                    cardRepository.save(it)

                    CardDTO(it.id, it.title, it.description, it.position, it.column)
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