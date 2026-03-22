package projects.kankan.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import projects.kankan.model.BoardColumn
import projects.kankan.model.Card

interface CardRepository: JpaRepository<Card, Long> {

    @Query(value = "SELECT * FROM CARDS WHERE name like %?1%", nativeQuery = true)
    fun findCardsByTitle(cardTitle: String): List<Card>

    fun findCardsByBoardId(boardId: Long): List<Card>

    fun findByBoardIdAndColumn(boardId: Long, column: BoardColumn): List<Card>
}