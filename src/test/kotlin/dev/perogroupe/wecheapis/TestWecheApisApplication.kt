package dev.perogroupe.wecheapis

import org.springframework.boot.fromApplication
import org.springframework.boot.with


fun main(args: Array<String>) {
    fromApplication<WecheApisApplication>().with(TestcontainersConfiguration::class).run(*args)
}
