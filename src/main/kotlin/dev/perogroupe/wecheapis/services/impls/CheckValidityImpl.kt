package dev.perogroupe.wecheapis.services.impls

import dev.perogroupe.wecheapis.dtos.requests.CheckValidityRequest
import dev.perogroupe.wecheapis.entities.CheckValidity
import dev.perogroupe.wecheapis.repositories.CheckValidityRepository
import dev.perogroupe.wecheapis.services.CheckValidityService
import dev.perogroupe.wecheapis.services.UserService
import dev.perogroupe.wecheapis.utils.checkValidityRequestToCheckValidity
import dev.perogroupe.wecheapis.utils.plus
import dev.perogroupe.wecheapis.utils.plusMinutes
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Service
import java.time.Instant
import java.util.*

/**
 * Implementation of the CheckValidityService.
 */
@Service
class CheckValidityImpl(
    private val repository: CheckValidityRepository,
    private val userService: UserService
) : CheckValidityService {

    /**
     * Stores a new check validity in the database.
     *
     * @param checkValidityRequest the check validity request
     * @return the stored check validity
     */
    override fun store(checkValidityRequest: CheckValidityRequest): CheckValidity {
        /**
         * Create a new check validity object with the provided request
         * and set the appDocumentDateEnd to 90 days from the current date
         */
        val checkValidity = checkValidityRequestToCheckValidity(checkValidityRequest).copy(
//            appDocumentDateEnd = plusMinutes(Date(), 5),
            appDocumentDateEnd = plus(Date(), 90),
        )
        return repository.save(checkValidity)
    }


    /**
     * Updates the check validity in the database.
     *
     * @param request the check validity request
     * @return the updated check validity
     */
    override fun update(request: CheckValidityRequest): CheckValidity =
        repository.save(
            repository.findByUserAndUsed(request.user, false).last().copy(
                appDnr = true,
                appDnrDateDelivery = Instant.now(),
                used = true,
//                appDnrDateEnd = plusMinutes(Date(), 2)
                appDnrDateEnd = plus(Date(), 90)
            )
        )


    /**
     * Checks if the user has a valid check validity.
     *
     * @param authentication the authentication
     * @return true if the user has a valid check validity, false otherwise
     */
    override fun checkValidity(authentication: Authentication): Boolean {
        /**
         * Get the user from the authentication
         * Check if the user has a valid check validity
         * Check if the appDocumentDateEnd is greater than the current time
         * Check if the appDnrDateEnd is greater than the current time
         */
        val checkValidity = repository.findByUserAndUsed(userService.getUser(authentication), true).last()
        return if (checkValidity.appDocumentDateEnd.time > System.currentTimeMillis() && checkValidity.appDnrDateEnd.time > System.currentTimeMillis()) {
          checkValidity.appDnr
        } else {
            false
        }
    }

    override fun checkValidityApp(authentication: Authentication): Boolean {
        /**
         * Get the user from the authentication
         * Check if the user has a valid check validity
         * Check if the appDocumentDateEnd is greater than the current time
         */
        val checkValidity = repository.findByUserAndUsed(userService.getUser(authentication), false).last()
        return if (checkValidity.appDocumentDateEnd.time > System.currentTimeMillis()) {
            checkValidity.appDocument
        } else {
            false
        }
    }


}