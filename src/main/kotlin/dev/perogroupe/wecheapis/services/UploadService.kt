package dev.perogroupe.wecheapis.services


import dev.perogroupe.wecheapis.entities.File
import org.springframework.core.io.Resource
import org.springframework.web.multipart.MultipartFile

interface UploadService {
    fun deleteFile(fileName: String)
    fun uploadFile(file: MultipartFile,fileName: String,originFilePath: String): File
    fun loadFile(fileName: String): Resource
    fun loadRequestFile(fileName: String): Resource
}