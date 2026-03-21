package projects.kankan.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import projects.kankan.dto.BoardRequest
import projects.kankan.service.BoardService

@RestController
@RequestMapping("/boards")
class BoardContoller(private val boardService: BoardService) {

    @PostMapping
    fun createBoard(@RequestBody request: BoardRequest): ResponseEntity<Any> =
        ResponseEntity.ok(boardService.createBoard(request))


}