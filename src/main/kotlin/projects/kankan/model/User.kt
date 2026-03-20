package projects.kankan.model

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "kankan_user")
data class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(unique = true, nullable = false)
    val email: String,

    @Column(nullable = false)
    val password: String,

    val createdAt: LocalDateTime = LocalDateTime.now())

