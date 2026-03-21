package projects.kankan.repository

import org.springframework.data.jpa.repository.JpaRepository
import projects.kankan.model.Board

interface BoardRepository: JpaRepository<Board, Long> {

}