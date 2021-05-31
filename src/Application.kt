package com.renner

import io.ktor.application.*
import io.ktor.jackson.*
import io.ktor.response.*
import io.ktor.request.*
import io.ktor.features.*
import org.slf4j.event.*
import io.ktor.routing.*
import io.ktor.http.*
import java.math.BigDecimal

data class ComputeData(val exp: String, val val_1: String, val val_2: String)
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

    routing {
        get("/") {
            call.respondText("Testroute Called!", contentType = ContentType.Text.Plain)
        }
        post(path = "/compute"){
            val requestData = call.receive<ComputeData>()
            call.respond(compute(requestData))
        }
        get(path = "/test"){
            val a = RationalNumber(BigDecimal(1.25))
            val b = RationalNumber(BigDecimal(4))
            val c = RationalNumber(BigDecimal(2))
            val foo = NumGroup(a, b, Operation.Addition)
            val bar = NumGroup(foo, c, Operation.Multiplication)

            println(a.get() + b.get())

            call.respond(bar.get().toString())
        }
    }
}

fun compute(inData: ComputeData): ReturnData {
    val var_1 = RationalNumber(inData.val_1)
    val var_2 = RationalNumber(inData.val_2)
    var outData = ReturnData("")
    when{
        inData.exp.contains('+') -> {outData.result = (var_1+var_2).toString()}
        inData.exp.contains('-') -> {outData.result = (var_1-var_2).toString()}
        inData.exp.contains('*') -> {outData.result = (var_1*var_2).toString()}
        inData.exp.contains('/') -> {outData.result = (var_1/var_2).toString()}

    }
    return outData
}

