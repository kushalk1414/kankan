package projects.kankan.projects.kankan.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import projects.kankan.projects.kankan.dto.BoardRequest
import projects.kankan.projects.kankan.dto.CardRequest
import projects.kankan.projects.kankan.dto.CardResponse
import projects.kankan.projects.kankan.service.BoardService

@RestController
@RequestMapping("/boards")
class BoardContoller(private val boardService: BoardService) {

    @PostMapping
    fun createBoard(@RequestBody request: BoardRequest): ResponseEntity<Any> =
        ResponseEntity.ok(boardService.createBoard(request))


}