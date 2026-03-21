package projects.kankan.projects.kankan.service

import org.springframework.stereotype.Service
import projects.kankan.model.Card
import projects.kankan.projects.kankan.dto.CardRequest
import projects.kankan.projects.kankan.dto.CardResponse
import projects.kankan.projects.kankan.model.BoardColumn
import projects.kankan.projects.kankan.repository.CardRepository

@Service
class CardService(private val cardRepository: CardRepository) {
    fun addCard(cardRequest: CardRequest): CardResponse {

        val card = Card(
            title = cardRequest.title,
            description = cardRequest.description,
            column = BoardColumn.TODO,
        )

        val savedCard = cardRepository.save(card)

        return CardResponse(
            id = savedCard.id,
            title = savedCard.title,
            description = savedCard.description,
            position = savedCard.position,
            column = savedCard.column
        )
    }
}