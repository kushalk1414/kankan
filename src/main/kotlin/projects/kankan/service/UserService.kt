package org.example.projects.kankan.service

import org.example.projects.kankan.dto.AuthRequest
import org.example.projects.kankan.dto.AuthResponse
import org.example.projects.kankan.dto.UserDTO
import org.example.projects.kankan.model.User
import org.example.projects.kankan.repository.UserRepository
import org.springframework.stereotype.Service
import java.util.*

@Service
class UserService(private val userRepository: UserRepository) {
        
    fun addUser(authRequest: AuthRequest): AuthResponse {
        val user = authRequest.let {
            User(
                email = authRequest.email,
                password = authRequest.password
            )
        }

        userRepository.save(user)

        return AuthResponse(
            user.id,
            user.email
        )
    }
}