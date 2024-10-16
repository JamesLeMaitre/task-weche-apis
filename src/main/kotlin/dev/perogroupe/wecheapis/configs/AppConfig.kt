package dev.perogroupe.wecheapis.configs


import dev.perogroupe.wecheapis.dtos.requests.StructureRequest
import dev.perogroupe.wecheapis.dtos.requests.UserAdminRequest
import dev.perogroupe.wecheapis.dtos.requests.UserRequest
import dev.perogroupe.wecheapis.entities.Beneficiary
import dev.perogroupe.wecheapis.entities.Role
import dev.perogroupe.wecheapis.listeners.NotificationsListener
import dev.perogroupe.wecheapis.repositories.BeneficiaryRepository
import dev.perogroupe.wecheapis.repositories.NotificationsRepository
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
        structureService: StructureService,
        beneficiaryRepository: BeneficiaryRepository
    ): CommandLineRunner {
        return CommandLineRunner {
       /*     val roles = listOf(
                Role(roleName = "ROLE_USER"), // Role utilise pour les utilisateurs
                Role(roleName = "ROLE_ADMIN"), // Role utilise pour les administrateurs( supérieurs hiérarchiques)
                Role(roleName = "ROLE_SUPER_ADMIN"), // Role utilise par la DPAF
                Role(roleName = "ROLE_S_SUPER_ADMIN"), // Role utilise par le super super administrateur (admin 0)

            )*/

//            roleRepository.saveAll(roles)

         /*   val beneficiaryList = listOf(
                Beneficiary(name = "Agent de l’Etat sans fonction en service au niveau départemental", attribute = "sans-fonction-departemental"),
                Beneficiary(name = "Agent de l’Etat avec fonction / Agent de l’Etat nommé", attribute = "nomme"),
                Beneficiary(name = "Agent de l’Etat sans fonction", attribute = "sans-fonction")
            )*/
//            beneficiaryRepository.saveAll(beneficiaryList)

      /*      val userRequest = UserRequest(
                firstname = "GBOZO",
                lastname = "Jules",
                email = "dgbozo1@gmail.com",
                password = "deo",
                confirmPassword = "deo",
                phoneNumber = "22991655906",
                serialNumber = "deo",
                birthdate = "01/08/1978"
            )*/
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

         /*   val structureRequest = StructureRequest(
                name = "MINISTERE DE LA FONCTION PUBLIQUE"
            )*/
//            structureService.store(structureRequest)

        }
    }

    /**
     * Call NotificationsListener to delete notifications read after 1 day.
     * use scheduledExecutorService to delete notifications read after 1 day.
     * use  schedule every day at 00:00 AM
     * @return an instance of NotificationsListener.
     * @see NotificationsListener
     *
     */
//    @Bean(name = ["notificationsListener"])
//    fun notificationsListener(): NotificationsListener = NotificationsListener()


}