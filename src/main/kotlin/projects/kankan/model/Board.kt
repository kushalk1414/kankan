package projects.kankan.model

import jakarta.persistence.*
import jakarta.persistence.Table

@Entity
@Table(name = "boards")
data class Board (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    val title: String,

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
        name = "board_columns",
        joinColumns = [JoinColumn(name = "board_id")]
    )
    @Enumerated(EnumType.STRING)
    var columns: MutableList<BoardColumn> = mutableListOf(
        BoardColumn.TODO,
        BoardColumn.IN_PROGRESS,
        BoardColumn.REVIEW,
        BoardColumn.DONE
    )

)