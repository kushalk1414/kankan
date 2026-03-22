package projects.kankan.controller

import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import projects.kankan.dto.BoardDTO
import projects.kankan.service.BoardService

@RestController
@RequestMapping("/boards")
class BoardContoller(private val boardService: BoardService) {

    @PostMapping
    fun addBoard(@RequestBody @Valid boardDTO: BoardDTO): ResponseEntity<BoardDTO> {
        val newBoard: BoardDTO = boardService.addBoard(boardDTO)
        return ResponseEntity(newBoard, HttpStatus.CREATED)
    }

}