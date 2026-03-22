package projects.kankan.mappers

import projects.kankan.model.Board
import projects.kankan.model.Card
import projects.kankan.dto.BoardDTO
import projects.kankan.dto.CardDTO

class Mappers {
    fun Card.toDTO(): CardDTO  = CardDTO(
        id,
        title,
        description,
        position,
        boardId,
        column
    )

    fun CardDTO.toEntity(): Card {
        return Card(
            id ?: 0,
            title,
            description,
            position,
            boardId,
            column
        )
    }

    fun Board.toDTO(): BoardDTO = BoardDTO(
        id,
        title,
        columns
    )

    fun BoardDTO.toEntity(): Board {
        return Board(
            id ?: 0,
            title,
            columns
        )
    }
}