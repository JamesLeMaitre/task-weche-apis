package dev.perogroupe.wecheapis.services.impls

import dev.perogroupe.wecheapis.dtos.requests.CheckRequestStatusReq
import dev.perogroupe.wecheapis.dtos.requests.NewRequestReq
import dev.perogroupe.wecheapis.dtos.responses.NewRequestResponse
import dev.perogroupe.wecheapis.entities.NewRequest
import dev.perogroupe.wecheapis.entities.Notifications
import dev.perogroupe.wecheapis.events.CheckRequestStatusEvent
import dev.perogroupe.wecheapis.exceptions.BeneficiaryNotFoundException
import dev.perogroupe.wecheapis.exceptions.NewRequestNotFoundException
import dev.perogroupe.wecheapis.exceptions.StructureNotFoundException
import dev.perogroupe.wecheapis.exceptions.UserNotFoundException
import dev.perogroupe.wecheapis.repositories.*
import dev.perogroupe.wecheapis.services.MessageService
import dev.perogroupe.wecheapis.services.NewRequestService
import dev.perogroupe.wecheapis.services.NotificationsService
import dev.perogroupe.wecheapis.services.UploadService
import dev.perogroupe.wecheapis.utils.enums.RequestStatus
import dev.perogroupe.wecheapis.utils.generateRandomString
import dev.perogroupe.wecheapis.utils.response
import dev.perogroupe.wecheapis.utils.toNewRequest
import org.springframework.context.ApplicationEventPublisher
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.util.*

@Service
class NewRequestServiceImpl(
    private val repository: NewRequestRepository,
    private val roleRepository: RoleRepository,
    private val structureRepository: StructureRepository,
    private val uploadService: UploadService,
    private val eventPublisher: ApplicationEventPublisher,
    private val userRepository: UserRepository,
    private val notificationsService: NotificationsService,
    private val messageService: MessageService,
    private val beneficiaryRepository: BeneficiaryRepository,
) : NewRequestService {
    override fun create(request: NewRequestReq): NewRequestResponse {
        val newRequest = request.toNewRequest().copy(
            structure = structureRepository.findById(request.structureId!!)
                .orElseThrow { StructureNotFoundException("Structure not found") },
            requestNumber = generateRandomString(8).uppercase(Locale.getDefault()),
        )
        return repository.save(newRequest).response()
    }

    override fun create(
        firstName: String,
        lastName: String,
        structureId: String,
        beneficiaryId: String,
        firstNameOfPreviousOfficial: String?,
        lastNameOfPreviousOfficial: String?,
        serialNumberOfPreviousOfficial: String?,
        authentication: Authentication,
        serialNumber: String?,
        body: String?,
        grade: String?,
        gradeDate: String?,
        ua: String?,
        positionHeld: String?,
        ppsDate: String?,
        uaDate: String?,
        fonction: String?,
        dateFonction: String?
    ): NewRequestResponse {
        // Find the current user by their username
//        println("Ste 1")
        val currentUser = userRepository.findByUsername(authentication.name)
            .orElseThrow { UserNotFoundException("User with this serial number ${authentication.name} not found") }
//        println("Ste 2")
        // Find the applicant's supervisor
        val userSupervisor = userRepository.findUserByStructureAndRole("ROLE_ADMIN", currentUser.structure!!)
            .orElseThrow { UserNotFoundException("User with this serial number ${currentUser.structure!!} not found") }
//        println("Ste 3")
        // Check if the user has already requested
        if (currentUser.hasRequested) throw Exception("You have already requested")
//        println("Ste 4")
        // Generate a random request number
        val randomNumber = generateRandomString(8).uppercase(Locale.getDefault())
//        println("Ste 5")
        val beneficiary = beneficiaryRepository.findByAttribute(beneficiaryId)
            .orElseThrow { BeneficiaryNotFoundException("Beneficiary not found by this attribute") }

        // Create a new request object
        val newRequest = NewRequestReq(
            firstName = firstName,
            lastName = lastName,
            structureId = structureId,
            beneficiaryId = beneficiaryId,
//            startPeriod = startPeriod,
//            endPeriod = endPeriod,

//            dateOfFirstEntryService = dateOfFirstEntryService,
            firstNameOfPreviousOfficial = firstNameOfPreviousOfficial,
            lastNameOfPreviousOfficial = lastNameOfPreviousOfficial,
            serialNumberOfPreviousOfficial = serialNumberOfPreviousOfficial,
//            gradeOfPreviousOfficial = gradeOfPreviousOfficial,
//            positionHeldOfPreviousOfficial = positionHeldOfPreviousOfficial,
//            bodyOfPreviousOfficial = bodyOfPreviousOfficial,
            serialNumber = serialNumber,
            grade = grade,
            gradeDate = gradeDate,
            ua = ua,
            positionHeld = positionHeld,
//            agentPosition = agentPosition,
            ppsDate = ppsDate,
            uaDate = uaDate,
            fonction = fonction,
            dateFonction = dateFonction!!,
            body = body,
        ).toNewRequest().copy(
            structure = structureRepository.findById(structureId)
                .orElseThrow { StructureNotFoundException("Structure not found") },
            requestNumber = randomNumber,
            beneficiary = beneficiary,
            user = currentUser
        )

        // Send Notification
        val notification = Notifications(
            user = userSupervisor,
            message = "Une nouvelle demande a été envoyé.",
            read = false
        )
        notificationsService.store(notification)

        // Send email to admin
        messageService.sendMail(
            userSupervisor.email,
            "Nouvelle demande d'attestation de présence au poste depuis la plateforme Weche",
            """
    Bonjour ${userSupervisor.firstname},

    Une nouvelle demande d'attestation de présence au poste a été effectuée sur la plateforme. Voici les informations ci-dessous :

    Nom : ${newRequest.firstName} ${newRequest.lastName}
    Email : ${newRequest.user?.email}
    Numéro matricule : ${newRequest.serialNumber}

    Cordialement,

    L'équipe de Weche
    """.trimIndent()
        )

        // Save the new request and get the saved object
        val it = repository.save(newRequest)

        // Create a request for checking the request status
        val req = CheckRequestStatusReq(
            requestNumber = it.requestNumber!!,
            requestStatus = RequestStatus.NEW,
            user = it.user!!,
            comment = "None"
        )

        // Publish an event for checking the request status
        eventPublisher.publishEvent(CheckRequestStatusEvent(this, req))

        return it.response()
    }
       /* return if (appointmentDecree != null && !appointmentDecree.isEmpty && handingOver != null && !handingOver.isEmpty) {
            // Upload both the appointment decree and handing over files
            this.upload(
                appointmentDecree = appointmentDecree,
                handingOver = handingOver,
                id = it.id,
                authentication = authentication
            )
        } else {
            it.response()
        }*/
        // Upload the appointment decree file

        // Upload the appointment decree and handing over files
//    }

    /**
     * Store a new request.
     *
     * @param firstName The first name of the requester.
     * @param lastName The last name of the requester.
     * @param structureId The ID of the structure.
     * @param civilName The civil name of the requester.
     * @param startPeriod The start period of the request.
     * @param endPeriod The end period of the request.
     * @param dateOfFirstEntryService The date of first entry service.
     * @param firstNameOfPreviousOfficial The first name of the previous official.
     * @param lastNameOfPreviousOfficial The last name of the previous official.
     * @param serialNumberOfPreviousOfficial The serial number of the previous official.
     * @param gradeOfPreviousOfficial The grade of the previous official.
     * @param positionHeldOfPreviousOfficial The position held by the previous official.
     * @param bodyOfPreviousOfficial The body of the previous official.
     * @param appointmentDecree The appointment decree file.
     * @param handingOver The handing over file.
     * @param authentication The authentication object.
     * @return The response of the new request.
     * @throws UserNotFoundException If the user with the provided serial number is not found.
     * @throws Exception If the user has already requested.
     */

    override fun store(
        firstName: String,
        lastName: String,
        structureId: String,
        beneficiaryId: String,
//        startPeriod: String,
//        endPeriod: String,
//        dateOfFirstEntryService: String?,
        firstNameOfPreviousOfficial: String?,
        lastNameOfPreviousOfficial: String?,
        serialNumberOfPreviousOfficial: String?,
//        gradeOfPreviousOfficial: String?,
//        positionHeldOfPreviousOfficial: String?,
//        bodyOfPreviousOfficial: String?,
        appointmentDecree: MultipartFile?,
        handingOver: MultipartFile?,
        authentication: Authentication,

        serialNumber: String?,
        body: String?,
        grade: String?,
        gradeDate: String?,
        ua: String?,
        positionHeld: String?,
//        agentPosition: String?,
        ppsDate: String?,
        uaDate: String?,
        fonction: String?,
        dateFonction: String?,
    ): NewRequestResponse {

//        println("Files, files: $appointmentDecree.name $handingOver.")
        // Find the current user by their username
        val currentUser = userRepository.findByUsername(authentication.name)
            .orElseThrow { UserNotFoundException("User with this serial number ${authentication.name} not found") }
        // Find the applicant's supervisor
        val userSupervisor = userRepository.findUserByStructureAndRole("ROLE_ADMIN", currentUser.structure!!)
            .orElseThrow { UserNotFoundException("User with this serial number ${currentUser.structure!!} not found") }

        // Check if the user has already requested
        if (currentUser.hasRequested) throw Exception("You have already requested")

        // Generate a random request number
        val randomNumber = generateRandomString(8).uppercase(Locale.getDefault())

        val beneficiary = beneficiaryRepository.findByAttribute(beneficiaryId)
            .orElseThrow { BeneficiaryNotFoundException("Beneficiary not found by this attribute") }

        // Create a new request object
        val newRequest = NewRequestReq(
            firstName = firstName,
            lastName = lastName,
            structureId = structureId,
            beneficiaryId = beneficiaryId,
//            startPeriod = startPeriod,
//            endPeriod = endPeriod,

//            dateOfFirstEntryService = dateOfFirstEntryService,
            firstNameOfPreviousOfficial = firstNameOfPreviousOfficial,
            lastNameOfPreviousOfficial = lastNameOfPreviousOfficial,
            serialNumberOfPreviousOfficial = serialNumberOfPreviousOfficial,
//            gradeOfPreviousOfficial = gradeOfPreviousOfficial,
//            positionHeldOfPreviousOfficial = positionHeldOfPreviousOfficial,
//            bodyOfPreviousOfficial = bodyOfPreviousOfficial,
            serialNumber = serialNumber,
            grade = grade,
            gradeDate = gradeDate,
            ua = ua,
            positionHeld = positionHeld,
//            agentPosition = agentPosition,
            ppsDate = ppsDate,
            uaDate = uaDate,
            fonction = fonction,
            dateFonction = dateFonction,
            body = body,
        ).toNewRequest().copy(
            structure = structureRepository.findById(structureId)
                .orElseThrow { StructureNotFoundException("Structure not found") },
            requestNumber = randomNumber,
            beneficiary = beneficiary,
            user = currentUser
        )

        // Send Notification
        val notification = Notifications(
            user = userSupervisor,
            message = "Une nouvelle demande a été envoyé.",
            read = false
        )
        notificationsService.store(notification)

        // Send email to admin
        messageService.sendMail(
            userSupervisor.email,
            "Nouvelle demande d'attestation de présence au poste depuis la plateforme Weche",
            """
    Bonjour Administrator,

    Une nouvelle demande d'attestation de présence au poste a été effectuée sur la plateforme. Voici les informations ci-dessous :

    Nom : ${newRequest.firstName} ${newRequest.lastName}
    Email : ${newRequest.user?.email}
    Numéro matricule : ${newRequest.serialNumber}

    Cordialement,

    L'équipe de Weche
    """.trimIndent()
        )

        // Save the new request and get the saved object
        val it = repository.save(newRequest)

        // Create a request for checking the request status
        val req = CheckRequestStatusReq(
            requestNumber = it.requestNumber!!,
            requestStatus = RequestStatus.NEW,
            user = it.user!!,
            comment = "None"
        )

        // Publish an event for checking the request status
        eventPublisher.publishEvent(CheckRequestStatusEvent(this, req))

        return if (appointmentDecree != null && !appointmentDecree.isEmpty && handingOver != null && !handingOver.isEmpty) {
            // Upload both the appointment decree and handing over files
            this.upload(
                appointmentDecree = appointmentDecree,
                handingOver = handingOver,
                id = it.id,
                authentication = authentication
            )
        } else {
            it.response()
        }
        // Upload the appointment decree file

        // Upload the appointment decree and handing over files

    }


    /**
     * Uploads appointment decree and handing over files for a new request.
     *
     * @param appointmentDecree The appointment decree file to upload
     * @param handingOver The handing over file to upload
     * @param id The ID of the request
     * @param authentication The authentication details
     * @return NewRequestResponse after uploading the files
     */
    override fun upload(
        appointmentDecree: MultipartFile,
        handingOver: MultipartFile,
        id: String,
        authentication: Authentication,
    ): NewRequestResponse = repository.findById(id)
        .map { request ->
            val newAppointmentDecree = uploadService.uploadFile(
                appointmentDecree,
                "${appointmentDecree.originalFilename?.replace(" ".toRegex(), "_")?.lowercase()}.${
                    appointmentDecree.originalFilename?.substringAfterLast(".")
                }",
                "users/avatars/${appointmentDecree.originalFilename?.replace(" ".toRegex(), "_")?.lowercase()}/"
            )
            val newHandingOver = uploadService.uploadFile(
                handingOver, "${handingOver.originalFilename?.replace(" ".toRegex(), "_")?.lowercase()}.${
                    handingOver.originalFilename?.substringAfterLast(".")
                }",
                "users/avatars/${handingOver.originalFilename?.replace(" ".toRegex(), "_")?.lowercase()}/"
            )
            val newRequest = request.copy(appointmentDecree = newAppointmentDecree, handingOver = newHandingOver)
            repository.save(newRequest)
            newRequest.response()
        }
        .orElseThrow { NewRequestNotFoundException("New Request not found") }


    /**
     * Retrieves a NewRequestResponse by its request number.
     *
     * @param id The request number of the NewRequestResponse to retrieve.
     * @return The NewRequestResponse with the given request number.
     * @throws NewRequestNotFoundException If no NewRequestResponse is found with the given request number.
     */
    override fun show(id: String): NewRequestResponse =
        // Find the NewRequestResponse by its request number
        repository.findByRequestNumber(id)
            // If found, return the response
            .map { it.response() }
            // If not found, throw an exception
            .orElseThrow { NewRequestNotFoundException("New Request not found") }


    /**
     * Retrieves a list of NewRequestResponse objects by structureId.
     *
     * @param structureId The id of the structure used to filter the requests.
     * @return A list of NewRequestResponse objects filtered by the structureId and request status.
     */
    override fun listByStructure(structureId: String): List<NewRequestResponse> =
//        repository.findAllByStructureIdAndRequestStatusIs(structureId, RequestStatus.NEW)
        repository.findAllByStructureIdAndRequestStatusIs(structureId, RequestStatus.NEW)
            .map { it.response() }


    /**
     * Retrieves a page of NewRequestResponse objects by structureId.
     *
     * @param structureId The id of the structure used to filter the requests.
     * @param page The page number to retrieve.
     * @param size The number of items per page.
     * @return A page of NewRequestResponse objects filtered by the structureId and sorted by creation date.
     */
    override fun listByStructure(
        structureId: String,
        page: Int,
        size: Int,
    ): Page<NewRequestResponse> =
        repository
            .findAllByStructureId(structureId, PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "created_at")))
            .map { it.response() }


    /**
     * Counts the number of non-rejected requests for a given user.
     *
     * @param authentication The authentication object containing the user's serial number.
     * @return The count of non-rejected requests for the user.
     * @throws UserNotFoundException If the user with the given serial number is not found.
     */
    override fun count(authentication: Authentication): Long {
        // Find the user by their serial number
        val user = userRepository
            .findByUsername(authentication.name)
            .orElseThrow { UserNotFoundException("User with this serial number ${authentication.name} not found") }

        // Count the number of non-rejected requests for the user's structure
        return repository.countByStructureAndRequestStatusIsNot(user.structure, RequestStatus.REJECTED)
    }

    override fun showByRequestNumber(requestNumber: String): NewRequest = repository.findByRequestNumber(requestNumber)
        .map { req ->
            repository.save(req)
        }
        .orElseThrow { NewRequestNotFoundException("New Request not found") }

    override fun showByRequest(requestNumber: String): NewRequest = repository.findByRequestNumber(requestNumber)
        .map { req ->
            req.requestStatus = RequestStatus.NEW
            repository.save(req)
        }.orElseThrow { NewRequestNotFoundException("New Request not found") }
}