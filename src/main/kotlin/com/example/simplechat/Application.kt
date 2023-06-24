package com.example.simplechat

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.flyway.FlywayDataSource
import org.springframework.boot.runApplication

@SpringBootApplication
@FlywayDataSource
class SimpleChatApplication

fun main(args: Array<String>) {
    runApplication<SimpleChatApplication>(*args)
}
