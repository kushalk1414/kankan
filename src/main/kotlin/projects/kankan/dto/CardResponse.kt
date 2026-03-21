package projects.kankan.projects.kankan.dto

import projects.kankan.projects.kankan.model.BoardColumn

data class CardResponse(
    val id: Long,
    val title: String,
    val description: String?,
    val position: Int,
    val column: BoardColumn
    )