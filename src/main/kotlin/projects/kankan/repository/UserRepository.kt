package projects.kankan.repository

import projects.kankan.model.User
import org.springframework.data.jpa.repository.JpaRepository
import java.util.Optional

interface UserRepository: JpaRepository<User, Long> {
    fun findUserByEmail(email: String): Optional<User>
}