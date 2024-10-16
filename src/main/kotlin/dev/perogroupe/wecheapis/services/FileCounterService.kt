package dev.perogroupe.wecheapis.services

import dev.perogroupe.wecheapis.utils.enums.FileType

interface FileCounterService {
    fun increment(request: FileType, reference: String): Int
    fun decrement(reference: String)
    fun get(reference: String): Int
}