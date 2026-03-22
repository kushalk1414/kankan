package projects.kankan.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import projects.kankan.model.Board

interface BoardRepository: JpaRepository<Board, Long> {

    @Query(value = "SELECT * FROM BOARDS WHERE name like %?1%", nativeQuery = true)
    fun findBoardsByTitle(cardTitle: String): List<Board>
}