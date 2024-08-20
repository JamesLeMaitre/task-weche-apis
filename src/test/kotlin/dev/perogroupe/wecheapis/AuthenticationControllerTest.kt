package dev.perogroupe.wecheapis

import com.fasterxml.jackson.databind.ObjectMapper
import com.verimsolution.eventapiweb.requests.LoginRequest
import dev.perogroupe.wecheapis.controllers.AuthenticationController
import dev.perogroupe.wecheapis.dtos.responses.clients.JwtResponse
import dev.perogroupe.wecheapis.services.AuthenticationService
import dev.perogroupe.wecheapis.utils.JwtUtils
import org.junit.Test
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED_VALUE
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers


@WebMvcTest(controllers = [AuthenticationController::class])
class AuthenticationControllerTest {

/*    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockBean
    private lateinit var authService: AuthenticationService

    @MockBean
    private lateinit var authenticationProvider: AuthenticationManager

    @MockBean
    private lateinit var utils: JwtUtils

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @Test
    fun `test successful login`() {
        val loginRequest = LoginRequest(username = "ADMIN/2024/E-SERVICE", password = "admin1234")
        val authenticationRequest = UsernamePasswordAuthenticationToken.unauthenticated(loginRequest.username, loginRequest.password)
        val authenticationResponse = UsernamePasswordAuthenticationToken(loginRequest.username, loginRequest.password, emptyList())
        val jwtResponse = JwtResponse(accessToken = "dummy_token", refreshToken = "dummy_refresh_token")

        Mockito.`when`(authenticationProvider.authenticate(authenticationRequest)).thenReturn(authenticationResponse)
        Mockito.`when`(utils.getJwtToken(authenticationResponse)).thenReturn(jwtResponse)

        mockMvc.perform(MockMvcRequestBuilders.post("/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(loginRequest)))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.token").value("dummy_token"))
    }*/


/*    @Test
    fun testMe() {
        val response = mockMvc.perform(
            MockMvcRequestBuilders
                .get("/me")
        )
            .andReturn()
            .response
            .contentAsString
        println(response)
    }*/
}