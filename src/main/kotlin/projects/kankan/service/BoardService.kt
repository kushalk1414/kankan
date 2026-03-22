package projects.kankan.service

import org.springframework.stereotype.Service
import projects.kankan.model.Card
import projects.kankan.exception.BoardNotFoundException
import projects.kankan.model.Board
import projects.kankan.model.BoardColumn
import projects.kankan.dto.BoardDTO
import projects.kankan.repository.BoardRepository
import java.util.Optional

@Service
class BoardService(private val boardRepository: BoardRepository){

    fun addBoard(boardDTO: BoardDTO): BoardDTO {
        val board = boardDTO.let {
            Board(null, it.title, it.columns)
        }

        val savedBoard = boardRepository.save(board)

        return savedBoard.let {
            BoardDTO(it.id, it.title, it.columns)
        }
    }

    fun updateBoard(id: Long, updatedBoardDTO: BoardDTO): BoardDTO {
        val existingBoard: Optional<Board> = boardRepository.findById(id)

        return if (existingBoard.isPresent){
            existingBoard.get()
                .let {
                    it.title = updatedBoardDTO.title
                    it.columns = updatedBoardDTO.columns

                    boardRepository.save(it)

                    BoardDTO(it.id, it.title, it.columns)
                }
        } else throw BoardNotFoundException("Card with ID `$id` not found")
    }

    fun addCardToBoard(card: Card, column: BoardColumn) {

    }
}