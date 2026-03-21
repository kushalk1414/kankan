package projects.kankan.service

import org.springframework.stereotype.Service
import projects.kankan.model.Card
import projects.kankan.dto.BoardRequest
import projects.kankan.dto.BoardResponse
import projects.kankan.model.Board
import projects.kankan.model.BoardColumn
import projects.kankan.repository.BoardRepository

@Service
class BoardService(private val boardRepository: BoardRepository){

    fun createBoard(boardRequest: BoardRequest): BoardResponse {
        val board = Board(
            title = boardRequest.title,
        )

        val savedBoard = boardRepository.save(board)

        return BoardResponse(
            id = savedBoard.id,
            title = savedBoard.title
        )
    }

    fun addCard(card: Card, column: BoardColumn) {

    }
}