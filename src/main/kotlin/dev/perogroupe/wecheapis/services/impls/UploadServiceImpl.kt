package dev.perogroupe.wecheapis.services.impls

import dev.perogroupe.wecheapis.configs.FileConfig
import dev.perogroupe.wecheapis.entities.File
import dev.perogroupe.wecheapis.exceptions.FileStorageException
import dev.perogroupe.wecheapis.repositories.FileRepository
import dev.perogroupe.wecheapis.services.UploadService
import dev.perogroupe.wecheapis.utils.getFileExtension
import jakarta.annotation.PostConstruct
import org.springframework.core.io.ByteArrayResource
import org.springframework.core.io.Resource
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.servlet.support.ServletUriComponentsBuilder
import java.io.FileNotFoundException
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.StandardCopyOption

@Service
class UploadServiceImpl(
    private val repository: FileRepository,
    private val storageProperties: FileConfig,
) : UploadService {

    private val dirLocation: Path = Paths.get(storageProperties.uploadDir).toAbsolutePath().normalize()


    /**
     * Initializes the upload directory by creating the necessary directories.
     * Throws a FileStorageException if the directory creation fails.
     */
    @PostConstruct
    fun init() {
        try {
            // Create the upload directory if it doesn't exist
            Files.createDirectories(dirLocation)
        } catch (ex: Exception) {
            // Throw an exception if directory creation fails
            throw FileStorageException("Could not create upload directory !")
        }
    }

    /**
     * Deletes a file from the specified directory.
     *
     * @param fileName The name of the file to delete.
     * @throws NoSuchFileException If the file does not exist.
     * @throws IOException If an I/O error occurs.
     */
    override fun deleteFile(fileName: String) {
        // Resolve the file path by combining the directory location and the file name
        val filePath = dirLocation.resolve(fileName)

        // Check if the file exists
        if (Files.exists(filePath)) {
            // Delete the file
            Files.delete(filePath)
        }
    }


    /**
     * Uploads a file to the server and saves its metadata in the database.
     *
     * @param file The file to upload.
     * @param fileName The name of the file.
     * @param originFilePath The path of the original file.
     * @return The saved file metadata.
     * @throws IllegalArgumentException If the file attachment is invalid.
     */
    override fun uploadFile(file: MultipartFile, fileName: String, originFilePath: String): File {
        // Upload the file to the server
        upload(file, fileName, originFilePath)

        // Generate the file URL
        val path = ServletUriComponentsBuilder.fromCurrentContextPath()
            .path(setPath(file, fileName))
            .toUriString()

        // Create the file metadata object
        val fileAttachment = file.contentType?.let { contentType ->
            File(name = fileName, url = path, type = contentType)
        }

        // Save the file metadata in the database and return it
        return fileAttachment?.run {
            repository.save(this)
        } ?: throw IllegalArgumentException("Invalid file attachment")
    }


    fun upload(file: MultipartFile, fileName: String, originFilePath: String): String {
//        val fileName = file.originalFilename ?: throw IllegalArgumentException("Invalid file name")
        val finalName = fileName
        // Check if the file is pdf
       /* if (file.contentType == "application/pdf") {
           finalName = "$fileName.pdf"
        }*/
        // Get the complete path for the file
        val filePath = originFilePath + finalName

        val filePathDir = dirLocation.resolve(filePath)

        // Create the directory if it doesn't exist
        val directory = filePathDir.parent
        if (!Files.exists(directory)) {
            Files.createDirectories(directory)
        }

        // Save the file
        Files.copy(file.inputStream, filePathDir, StandardCopyOption.REPLACE_EXISTING)
        return finalName
    }

    /**
     * Loads a file from the specified path.
     *
     * @param fileName The name of the file to load.
     * @return A resource representing the loaded file.
     * @throws FileNotFoundException If the file is not found or not readable.
     */
    override fun loadFile(fileName: String): Resource {
        // Extract the username from the filename
        val username = fileName.substringAfter("user_").substringBefore("_avatar").replace(" ", "_").lowercase()

        // Construct the correct file path
        val originFilePath = Paths.get("users", "avatars", username, fileName)
        val filePathDir: Path = this.dirLocation.resolve(originFilePath).normalize()

        // Check if the file exists and is readable
        if (!Files.exists(filePathDir) || !Files.isReadable(filePathDir)) {
            // Handle file not found
            return handleFileNotFound()
        }
        // Load the file and return a ByteArrayResource
        return ByteArrayResource(Files.readAllBytes(filePathDir))
    }


    /**
     * Loads a file for the given fileName.
     *
     * @param fileName The name of the file to load.
     * @return A resource representing the loaded file.
     * @throws FileNotFoundException If the file is not found or not readable.
     */
    override fun loadRequestFile(fileName: String): Resource {
        // Extract the username from the filename
        val username = fileName.substringAfter("user_").substringBefore("_avatar").replace(" ", "_").lowercase()

        // Construct the correct file path
        val originFilePath = Paths.get("users", "avatars", username, fileName)
        val filePathDir: Path = this.dirLocation.resolve(originFilePath).normalize()

        // Check if the file exists and is readable
        if (!Files.exists(filePathDir) || !Files.isReadable(filePathDir)) {
            return handleFileNotFound()
        }

        // Load the file and return a ByteArrayResource
        return ByteArrayResource(Files.readAllBytes(filePathDir))
    }


    /**
     * Sets the path based on the file extension.
     *
     * @param file The uploaded file.
     * @param fileName The name of the file.
     * @return The API path based on the file extension and user type.
     */
    fun setPath(file: MultipartFile, fileName: String): String {
        // Get the file extension
        val fileExtension = getFileExtension(file)

        // Check if the file is a PDF
        if (fileExtension == "pdf") {
            return "api/v1/new-request/user/${fileName}"
        }

        // Return the default path for non-PDF files
        return "api/v1/user/${fileName}"
    }


    /**
     * Handles the case when the file is not found or not readable.
     *
     * @throws FileStorageException If the file is not found or not readable.
     * @return A Resource representing the file not found exception.
     */
    private fun handleFileNotFound(): Resource {
        throw FileStorageException("Could not find or read the file")
    }
}