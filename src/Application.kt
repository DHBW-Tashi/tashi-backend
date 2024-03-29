package com.renner

import io.ktor.application.*
import io.ktor.jackson.*
import io.ktor.response.*
import io.ktor.request.*
import io.ktor.features.*
import org.slf4j.event.*
import io.ktor.routing.*
import io.ktor.http.*
import java.lang.Exception
import java.math.BigDecimal

data class ComputeData(val exp: String)
data class ReturnData(var result: String)
data class NumberTestData(var numberString: String)

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // Referenced in application.conf
@kotlin.jvm.JvmOverloads
fun Application.module(testing: Boolean = false) {
    install(CallLogging) {
        level = Level.INFO
        filter { call -> call.request.path().startsWith("/") }
    }
    install(ContentNegotiation) {
        jackson()
    }
    install(CORS) {
        method(HttpMethod.Post)
        header(HttpHeaders.AccessControlAllowOrigin)
        anyHost()
        allowCredentials = true
        allowNonSimpleContentTypes = true
    }

    routing {
        get("/") {
            call.respondText("Testroute Called!", contentType = ContentType.Text.Plain)
        }
        post(path = "/compute"){
            val requestData = call.receive<ComputeData>()
            var returnThis: ReturnData
            if (!validateBrackets(requestData.exp)){
                returnThis = ReturnData("Error: Invalid Brackets")
            } else {
                try {
                    returnThis = ReturnData(parse(requestData.exp).toString())
                } catch (e: Exception) {
                    returnThis = ReturnData(e.toString())
                }
            }
            call.respond(returnThis)
        }
        get(path = "/test"){
            val a = RationalNumber("4")
            val b = RationalNumber("2")
            call.respond((a.pow(b)).toString())
        }
    }
}



