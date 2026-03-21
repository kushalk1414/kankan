package projects.kankan.model

import jakarta.persistence.Column
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id

import projects.kankan.model.BoardColumn

data class Card(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    var title: String,
    var description: String?,
    var position: Int = 0,
    @Enumerated
    @Column(nullable = false)
    var column: BoardColumn = BoardColumn.TODO

)

