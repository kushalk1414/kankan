package org.example.projects.kankan.dto

import jakarta.validation.constraints.NotBlank

data class UserDTO(
    @get: NotBlank(message = "UserDTO.email must not be blank")
    val email: String,
    @get: NotBlank(message = "UserDTO.email must not be blank")
    val password: String
)