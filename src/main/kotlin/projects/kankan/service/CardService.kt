package projects.kankan.service

import org.springframework.stereotype.Service
import projects.kankan.exception.BoardNotFoundException
import projects.kankan.model.Card

import projects.kankan.exception.CardNotFoundException
import projects.kankan.model.Board
import projects.kankan.model.BoardColumn
import projects.kankan.dto.CardDTO
import projects.kankan.repository.BoardRepository
import projects.kankan.repository.CardRepository

@Service
class CardService(
    private val cardRepository: CardRepository,
    private val boardRepository: BoardRepository
    ) {
    private fun validateBoardExists(boardId: Long) {
        if (!boardRepository.existsById(boardId)) {
            throw CardNotFoundException("Board with ID `$boardId` not found, cannot perform card operation.")
        }
    }

    private fun getBoardAndValidateColumn(boardId: Long, column: BoardColumn): Board {
        val board = boardRepository.findById(boardId)
            .orElseThrow { BoardNotFoundException("Board with ID `$boardId` not found.") }
        if (!board.columns.contains(column)) {
            throw IllegalArgumentException("Column `$column` is not a valid column for Board with ID `$boardId`.")
        }
        return board
    }

    private fun getCardAndValidateBoard(boardId: Long, cardId: Long): Card{
        val card = cardRepository.findById(cardId)
            .orElseThrow { CardNotFoundException("Card with ID `$cardId` not found.") }
        if (card.boardId != boardId) {
            throw CardNotFoundException("Card with ID `$cardId` does not belong to Board with ID `$boardId`.")
        }
        return card
    }

    fun getAllCards(cardTitle: String?): List<CardDTO> {
        val cards = cardTitle?.let {
            cardRepository.findCardsByTitle((cardTitle))
        } ?: cardRepository.findAll()

        return cards.map {
            CardDTO(it.id, it.title, it.description, it.position, it.boardId, it.column)
        }
    }

    fun getCardsByBoardId(boardId: Long): List<CardDTO> {
        return getCardsByBoardIdAndColumn(boardId, null)
    }

    fun getCardsByBoardIdAndColumn(boardId: Long, column: BoardColumn?): List<CardDTO> {
        /*validateBoardExists(boardId)*/
        val cards = if (column != null) {
            cardRepository.findByBoardIdAndColumn(boardId, column)
        } else {
            cardRepository.findCardsByBoardId(boardId)
        }
        return cards.map {
            CardDTO(it.id, it.title, it.description, it.position, it.boardId, it.column)
        }
    }

    fun getCardById(boardId: Long, cardId: Long): CardDTO {
        val card = getCardAndValidateBoard(boardId, cardId)
        return card.let {
            CardDTO(it.id, it.title, it.description, it.position, it.boardId, it.column)
        }
    }

    fun addCard(boardId: Long, cardDTO: CardDTO): CardDTO {
        getBoardAndValidateColumn(boardId, cardDTO.column)
        if (cardDTO.boardId != boardId) {
            throw IllegalArgumentException("Card DTO boardId (`${cardDTO.boardId}`) must match path boardId (`$boardId`).")
        }
        val card = cardDTO.let {
            val card = Card(null, it.title, it.description, it.position, it.boardId)
            card
        }
        cardRepository.save(card)

        return card.let {
            CardDTO(it.id, it.title, it.description, it.position, it.boardId, it.column)
        }
    }

    fun updateCard(cardId: Long, boardId: Long, updatedCardDTO: CardDTO): CardDTO {
        getBoardAndValidateColumn(boardId, updatedCardDTO.column)
        val existingCard = getCardAndValidateBoard(boardId, cardId)

        if (updatedCardDTO.boardId != boardId || updatedCardDTO.boardId != existingCard.boardId) {
            throw IllegalArgumentException("Card DTO boardId (`${updatedCardDTO.boardId}`) " +
                    "must match existing card's boardId (`${existingCard.boardId}`) and path boardId (`$boardId`)." +
                    " Card cannot be moved between boards via this endpoint.")
        }

        return existingCard
                .let {
                    it.title = updatedCardDTO.title
                    it.description = updatedCardDTO.description
                    it.position = updatedCardDTO.position
                    it.column = updatedCardDTO.column

                    cardRepository.save(it)

                    CardDTO(it.id, it.title, it.description, it.position, it.boardId, it.column)
            }
        }

    fun deleteCard(cardId: Long, boardId: Long) {
        val existingCard = getCardAndValidateBoard(boardId, cardId)
        cardRepository.delete(existingCard)
    }
}