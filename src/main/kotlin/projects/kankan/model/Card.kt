package projects.kankan.model

import jakarta.persistence.Column
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id

import projects.kankan.projects.kankan.model.BoardColumn

data class Card(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    val title: String,
    val description: String?,
    val position: Int = 0,
    @Enumerated
    @Column(nullable = false)
    val column: BoardColumn

)

