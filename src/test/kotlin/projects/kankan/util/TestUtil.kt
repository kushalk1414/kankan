package com.kotlinspring.util

import projects.kankan.dto.UserDTO
import projects.kankan.model.User

fun UserList() = listOf(
    User(email = "user1@test.com", password = "testpass1"),
    User(email = "user2@test.com", password = "testpass2")
    )

fun userDTO(
    email: String,
    password: String
) = UserDTO(email = email, password = password)

