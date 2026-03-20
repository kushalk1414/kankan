package org.example.projects.kankan.service

import org.example.projects.kankan.dto.AuthRequest
import org.example.projects.kankan.dto.AuthResponse
import org.example.projects.kankan.dto.UserDTO
import org.example.projects.kankan.model.User
import org.example.projects.kankan.repository.UserRepository
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import java.util.*

@Service
class UserService(private val userRepository: UserRepository) {

    private val passwdEncoder = BCryptPasswordEncoder()

    fun addUser(authRequest: AuthRequest): AuthResponse {
        val user = authRequest.let {
            passwdEncoder.encode(authRequest.password)?.let { it1 ->
                User(
                    email = authRequest.email,
                    password = it1
                )
            }
        }

        userRepository.save(user!!)

        return AuthResponse(
            user.id,
            user.email
        )
    }

    fun login(authRequest: AuthRequest): AuthResponse? {
        val user = userRepository.findUserByEmail(authRequest.email).orElse(null)

        if (user != null &&
            passwdEncoder.matches(authRequest.password, user.password)) {
            return AuthResponse(
                user.id,
                user.email
            )
        }

        return null
    }
}