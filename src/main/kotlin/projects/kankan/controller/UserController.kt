package projects.kankan.controller

import projects.kankan.dto.AuthRequest
import projects.kankan.service.UserService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody

class UserController(private val userService: UserService) {

    @PostMapping("/register")
    fun addUser(@RequestBody request: AuthRequest): ResponseEntity<Any> {
        return ResponseEntity.ok(userService.addUser(request))
    }

    @PostMapping("/login")
    fun login(@RequestBody request: AuthRequest): ResponseEntity<Any> {
        val response = userService.login(request)

        return if(response != null) {
            ResponseEntity.ok(response)
        } else {
            ResponseEntity.status(401).body("invalid credentials")
        }
    }

}