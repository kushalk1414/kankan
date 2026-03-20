package org.example.projects.kankan.controller

import org.example.projects.kankan.dto.AuthRequest
import org.example.projects.kankan.dto.AuthResponse
import org.example.projects.kankan.service.UserService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody

class UserController(private val userService: UserService) {

    @PostMapping("/register")
    fun addUser(@RequestBody request: AuthRequest): ResponseEntity<AuthResponse> {
        return ResponseEntity.ok(userService.addUser(request))
    }

}