package projects.kankan.controller

import projects.kankan.dto.AuthRequest
import projects.kankan.service.UserService
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import org.springframework.http.HttpStatus
import projects.kankan.dto.AuthResponse
import kotlin.test.assertEquals

class UserControllerTest {

    private lateinit var userService: UserService
    private lateinit var userController: UserController

    @BeforeEach
    fun setup() {
        MockitoAnnotations.openMocks(this)
        userService = mock(UserService::class.java)
        userController = UserController(userService)
    }

    @Test
    fun `addUser should return OK with user data`() {
        val request = AuthRequest(email = "test@example.com", password = "123456")
        val userResponse = AuthResponse(1, "test@example.com")

        `when`(userService.addUser(request)).thenReturn(userResponse)

        val response = userController.addUser(request)

        assertEquals(HttpStatus.OK, response.statusCode)
        assertEquals(userResponse, response.body)
        verify(userService, times(1)).addUser(request)
    }

    @Test
    fun `login should return OK when credentials are valid`() {
        val request = AuthRequest(email = "test@example.com", password = "123456")
        val loginResponse = AuthResponse(1, "test@example.com")

        `when`(userService.login(request)).thenReturn(loginResponse)

        val response = userController.login(request)

        assertEquals(HttpStatus.OK, response.statusCode)
        assertEquals(loginResponse, response.body)
        verify(userService, times(1)).login(request)
    }

    @Test
    fun `login should return 401 when credentials are invalid`() {
        val request = AuthRequest(email = "wrong@example.com", password = "wrongpass")

        `when`(userService.login(request)).thenReturn(null)

        val response = userController.login(request)

        assertEquals(HttpStatus.UNAUTHORIZED, response.statusCode)
        assertEquals("invalid credentials", response.body)
        verify(userService, times(1)).login(request)
    }
}