package projects.kankan.model

import jakarta.persistence.*

import projects.kankan.model.BoardColumn

data class Card(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long?,
    var title: String,
    var description: String?,
    var position: Int = 0,
    @Column(name = "board_id", nullable = false) // Foreign key column
    val boardId: Long,
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var column: BoardColumn = BoardColumn.TODO

)

