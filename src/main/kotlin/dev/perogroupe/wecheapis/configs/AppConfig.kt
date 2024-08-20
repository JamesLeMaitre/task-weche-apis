package dev.perogroupe.wecheapis.configs


import dev.perogroupe.wecheapis.dtos.requests.StructureRequest
import dev.perogroupe.wecheapis.dtos.requests.UserAdminRequest
import dev.perogroupe.wecheapis.dtos.requests.UserRequest
import dev.perogroupe.wecheapis.entities.Role
import dev.perogroupe.wecheapis.repositories.RoleRepository
import dev.perogroupe.wecheapis.services.AuthenticationService
import dev.perogroupe.wecheapis.services.StructureService
import org.springframework.boot.CommandLineRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import java.util.*

@Configuration
class AppConfig {


    /**
     * Bean definition for creating a PasswordEncoder using BCryptPasswordEncoder.
     * @return an instance of PasswordEncoder.
     */
    @Bean
    fun passwordEncoder(): PasswordEncoder = BCryptPasswordEncoder()

    @Bean
    fun runner(
        roleRepository: RoleRepository,
        authenticationService: AuthenticationService,
        structureService: StructureService
    ): CommandLineRunner {
        return CommandLineRunner {
            val roles = listOf(
                Role(roleName = "ROLE_USER"), // Role utilise pour les utilisateurs
                Role(roleName = "ROLE_ADMIN"), // Role utilise pour les administrateurs( supérieurs hiérarchiques)
                Role(roleName = "ROLE_SUPER_ADMIN"), // Role utilise par la DPAF
                Role(roleName = "ROLE_S_SUPER_ADMIN"), // Role utilise par le super super administrateur (admin 0)

            )

//            roleRepository.saveAll(roles)

            val userRequest = UserRequest(
                firstname = "sadmin",
                lastname = "sadmin",
                email = "sadmin@gmail.com",
                password = "sadmin",
                confirmPassword = "sadmin",
                phoneNumber = "22991655906",
                serialNumber = "sadmin",
                birthdate = "01/01/2020"
            )
//            authenticationService.registerUser(userRequest)

           /* val ssuserReq = UserAdminRequest(
                firstname = "James",
                lastname = "Admin",
                email = "jamestchao14@gmail",
                password = "admin",
                confirmPassword = "admin",
                phoneNumber = "22991655906",
                serialNumber = "admin",
                birthdate = "01/01/2020",
                structureId = UUID.randomUUID().toString(),
                roleName = "ROLE_ADMIN"
            )
            authenticationService.registerAdmin(adminReq)*/

            val structureRequest = StructureRequest(
                name = "MINISTERE DE L'EDUCATION"
            )
            structureService.store(structureRequest)

        }
    }
}