package projects.kankan.projects.kankan.dto

import projects.kankan.model.BoardColumn

data class BoardDTO(
    val id : Long?,
    val title: String,
    val columns: MutableList<BoardColumn> = mutableListOf(
        BoardColumn.TODO,
        BoardColumn.IN_PROGRESS,
        BoardColumn.REVIEW,
        BoardColumn.DONE
    )
)