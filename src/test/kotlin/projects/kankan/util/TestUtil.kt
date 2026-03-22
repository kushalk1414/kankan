package com.kotlinspring.util

import projects.kankan.dto.UserDTO
import projects.kankan.model.BoardColumn
import projects.kankan.model.Card
import projects.kankan.model.User
import projects.kankan.projects.kankan.dto.CardDTO

fun userList() = listOf(
    User(email = "user1@test.com", password = "testpass1"),
    User(email = "user2@test.com", password = "testpass2")
    )

fun userDTO(
    email: String,
    password: String
) = UserDTO(email = email, password = password)

public fun createCardDTO(id: Long, title: String, column: BoardColumn) =
    CardDTO(id = id, title = title, description = "Description for $title", position = 0, column = column)