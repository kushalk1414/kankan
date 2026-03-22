package projects.kankan.dto

import projects.kankan.model.BoardColumn

data class CardDTO(
    val id: Long?,
    val title: String,
    val description: String?,
    val position: Int = 0,
    val boardId: Long,
    val column: BoardColumn
)